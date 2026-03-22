
<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">服务单管理</div>
      <n-button type="primary" @click="createDialogVisible = true">
        <template #icon><n-icon><AddOutline /></n-icon></template>
        新建服务单
      </n-button>
    </div>

    <div class="toolbar">
      <n-space>
        <n-select
          v-model:value="query.orderStatusId"
          :options="orderStatusOptions"
          placeholder="业务状态"
          clearable
          style="width: 200px"
          @update:value="fetchList"
        />
        <n-button type="primary" ghost @click="fetchList">
          <template #icon><n-icon><SearchOutline /></n-icon></template>
          查询
        </n-button>
        <n-button @click="resetQuery">重置</n-button>
      </n-space>
    </div>

    <n-card :bordered="false" content-style="padding: 0;">
      <n-data-table
        :columns="columns"
        :data="list"
        :loading="loading"
        :bordered="false"
        remote
        :pagination="pagination"
      />
    </n-card>

    <!-- 新建服务单弹窗：选择模板 -->
    <n-modal
      v-model:show="createDialogVisible"
      title="选择服务单模板"
      preset="card"
      style="width: 800px"
      :segmented="{ content: 'soft', footer: 'soft' }"
    >
      <n-alert type="info" style="margin-bottom: 16px">
        请从已发布的模板中选择一个来创建新的服务单实例。
      </n-alert>
      <n-data-table
        :columns="templateColumns"
        :data="templates"
        :loading="templateLoading"
        :bordered="false"
        :row-key="row => row.id"
        @update:checked-row-keys="handleTemplateSelect"
        max-height="400"
      />
      <template #footer>
        <n-space justify="end">
          <n-button @click="createDialogVisible = false">取消</n-button>
          <n-button type="primary" :disabled="!selectedTemplateId" :loading="creating" @click="handleCreate">
            立即创建
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { h, ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  NButton,
  NSpace,
  NTag,
  NSelect,
  NDataTable,
  NModal,
  NIcon,
  NCard,
  NAlert,
  NInput,
  useMessage,
  useDialog
} from 'naive-ui'
import { SearchOutline, AddOutline, CreateOutline, EyeOutline, SendOutline, FlashOutline } from '@vicons/ionicons5'
import { 
  listInstances, 
  createInstance, 
  submitInstance, 
  getOrderStatusOptions,
  getAvailableActions,
  executeTransition
} from '@/api/formInstance'
import { listTemplates } from '@/api/formTemplate'

const router = useRouter()
const message = useMessage()
const dialog = useDialog()
const loading = ref(false)
const list = ref([])
const query = reactive({ page: 1, size: 10, orderStatusId: null })

// 动作缓存：instanceId -> actions[]
const actionsCache = ref({})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  showSizePicker: true,
  pageSizes: [10, 20, 50],
  itemCount: 0,
  onChange: (page) => {
    query.page = page
    pagination.page = page
    fetchList()
  },
  onUpdatePageSize: (pageSize) => {
    query.size = pageSize
    pagination.pageSize = pageSize
    query.page = 1
    pagination.page = 1
    fetchList()
  }
})

const createDialogVisible = ref(false)
const templates = ref([])
const templateLoading = ref(false)
const selectedTemplateId = ref(null)
const creating = ref(false)

// 业务状态枚举
const orderStatusOptions = ref([])
const orderStatusMap = ref({})

const columns = [
  { title: 'ID', key: 'id', width: 70, align: 'center' },
  { title: '服务单名称', key: 'templateName', minWidth: 150 },
  { title: '版本', key: 'version', width: 80, align: 'center' },
  { 
    title: '国家', 
    key: 'countryCode', 
    width: 90,
    render: (row) => h(NTag, { bordered: false, type: 'info', size: 'small' }, { default: () => row.countryCode })
  },
  { title: '服务类型', key: 'serviceCodeL3', width: 120 },
  {
    title: '业务状态',
    key: 'orderStatusId',
    width: 120,
    render: (row) => h(NTag, {
      type: getTagType(row.orderStatusId),
      round: true,
      bordered: false,
      size: 'small'
    }, { default: () => orderStatusName(row.orderStatusId) })
  },
  { title: '创建时间', key: 'createTime', width: 170 },
  {
    title: '操作',
    key: 'actions',
    width: 280,
    fixed: 'right',
    render: (row) => {
      const btns = []
      
      // 1. 如果状态是 待提交(10) 或 已驳回(50)，显示【提交资料】进入表单填写
      if (row.orderStatusId === 10 || row.orderStatusId === 50) {
        btns.push(h(NButton, {
          size: 'small',
          secondary: true,
          type: 'primary',
          onClick: () => goForm(row.id)
        }, { 
          default: () => '提交资料',
          icon: () => h(NIcon, null, { default: () => h(CreateOutline) })
        }))
      } else {
        // 2. 其他状态，先显示【查看】
        btns.push(h(NButton, {
          size: 'small',
          secondary: true,
          type: 'info',
          onClick: () => goForm(row.id)
        }, { 
          default: () => '查看',
          icon: () => h(NIcon, null, { default: () => h(EyeOutline) })
        }))
        
        // 3. 动态加载流程动作（如：审核通过、审核不通过、已终止等）
        const actions = actionsCache.value[row.id] || []
        actions.forEach(action => {
          btns.push(h(NButton, {
            size: 'small',
            secondary: true,
            type: action.buttonType || 'primary',
            onClick: () => handleWorkflowAction(row.id, action)
          }, { 
            default: () => action.actionName,
            icon: () => h(NIcon, null, { default: () => h(FlashOutline) })
          }))
        })
      }

      return h(NSpace, { size: 'small', wrap: false }, { default: () => btns })
    }
  }
]

const templateColumns = [
  { type: 'selection', multiple: false },
  { title: 'ID', key: 'id', width: 60 },
  { title: '模板名称', key: 'templateName' },
  { title: '国家', key: 'countryCode', width: 80 },
  { title: '版本', key: 'version', width: 80 }
]

async function loadOrderStatusOptions() {
  const res = await getOrderStatusOptions()
  orderStatusOptions.value = (res.data || []).map(s => ({
    label: s.name,
    value: s.code
  }))
  orderStatusMap.value = Object.fromEntries(res.data.map(s => [s.code, s]))
}

function orderStatusName(code) {
  return orderStatusMap.value[code]?.name || code
}

function getTagType(code) {
  const tagType = orderStatusMap.value[code]?.tagType || 'info'
  const mapping = {
    'success': 'success',
    'warning': 'warning',
    'danger': 'error',
    'info': 'default'
  }
  return mapping[tagType] || 'default'
}

async function fetchList() {
  loading.value = true
  try {
    const params = { ...query }
    if (!params.orderStatusId) delete params.orderStatusId
    const res = await listInstances(params)
    list.value = res.data.records
    pagination.itemCount = res.data.total
    
    // 异步加载所有非草稿实例的可用操作
    list.value.forEach(row => {
      if (row.status !== 0) {
        loadActions(row.id)
      } else {
        actionsCache.value[row.id] = []
      }
    })
  } finally {
    loading.value = false
  }
}

async function loadActions(instanceId) {
  try {
    const res = await getAvailableActions(instanceId)
    actionsCache.value[instanceId] = res.data || []
  } catch {
    actionsCache.value[instanceId] = []
  }
}

function handleWorkflowAction(instanceId, action) {
  const remark = ref('')
  if (action.needRemark) {
    dialog.create({
      title: action.actionName,
      content: () => h(NInput, {
        type: 'textarea',
        placeholder: action.remarkLabel || '请输入备注/原因',
        value: remark.value,
        'on-update:value': (val) => { remark.value = val }
      }),
      positiveText: '确认',
      negativeText: '取消',
      onPositiveClick: async () => {
        await doTransition(instanceId, action.action, remark.value)
      }
    })
  } else {
    dialog.create({
      title: '确认操作',
      content: `确定要执行「${action.actionName}」吗？`,
      positiveText: '确认',
      negativeText: '取消',
      onPositiveClick: async () => {
        await doTransition(instanceId, action.action, '')
      }
    })
  }
}

async function doTransition(instanceId, action, remark) {
  try {
    await executeTransition(instanceId, action, remark)
    message.success('操作成功')
    fetchList() // 刷新列表以获取最新状态和可用动作
  } catch (error) {
    message.error(error.message || '操作失败')
  }
}

function resetQuery() {
  query.orderStatusId = null
  query.page = 1
  pagination.page = 1
  fetchList()
}

async function loadTemplates() {
  templateLoading.value = true
  try {
    const res = await listTemplates({ page: 1, size: 100, status: 1 })
    templates.value = res.data.records
  } finally {
    templateLoading.value = false
  }
}

function handleTemplateSelect(keys) {
  selectedTemplateId.value = keys[0] || null
}

function goForm(id) {
  router.push(`/instance/form/${id}`)
}

async function handleCreate() {
  if (!selectedTemplateId.value) return
  creating.value = true
  try {
    const res = await createInstance({ templateId: selectedTemplateId.value })
    message.success('服务单创建成功')
    createDialogVisible.value = false
    router.push(`/instance/form/${res.data.instanceId}`)
  } finally {
    creating.value = false
  }
}

async function handleSubmit(id) {
  await submitInstance(id)
  message.success('提交成功')
  fetchList()
}

onMounted(() => {
  loadOrderStatusOptions()
  fetchList()
  loadTemplates()
})
</script>

<style scoped>
.page-container {
  width: 100%;
}
</style>
