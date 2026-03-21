<template>
  <div class="page">
    <div class="page-toolbar">
      <n-space>
        <n-select
          v-model:value="query.orderStatusId"
          :options="orderStatusOptions"
          placeholder="业务状态"
          clearable
          style="width: 160px"
          @update:value="fetchList"
        />
        <n-button type="primary" @click="fetchList">
          <template #icon><n-icon><SearchOutline /></n-icon></template>
          查询
        </n-button>
      </n-space>
      <n-button type="primary" @click="createDialogVisible = true">
        <template #icon><n-icon><AddOutline /></n-icon></template>
        新建服务单
      </n-button>
    </div>

    <n-data-table
      :columns="columns"
      :data="list"
      :loading="loading"
      :bordered="false"
    />

    <n-pagination
      v-model:page="query.page"
      v-model:page-size="query.size"
      :item-count="total"
      :page-sizes="[10, 20, 50]"
      show-size-picker
      style="margin-top: 16px; justify-content: flex-end"
      @update:page="fetchList"
      @update:page-size="fetchList"
    />

    <!-- 新建服务单弹窗：选择模板 -->
    <n-modal
      v-model:show="createDialogVisible"
      title="选择服务单模板"
      preset="dialog"
      style="width: 700px"
    >
      <n-data-table
        :columns="templateColumns"
        :data="templates"
        :loading="templateLoading"
        :bordered="false"
        :row-key="row => row.id"
        @update:checked-row-keys="handleTemplateSelect"
      />
      <template #action>
        <n-space>
          <n-button @click="createDialogVisible = false">取消</n-button>
          <n-button type="primary" :disabled="!selectedTemplateId" :loading="creating" @click="handleCreate">
            创建服务单
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { h, ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  NButton,
  NSpace,
  NTag,
  NSelect,
  NDataTable,
  NPagination,
  NModal,
  NIcon,
  useMessage
} from 'naive-ui'
import { SearchOutline, AddOutline } from '@vicons/ionicons5'
import { listInstances, createInstance, submitInstance, getOrderStatusOptions } from '@/api/formInstance'
import { listTemplates } from '@/api/formTemplate'

const router = useRouter()
const message = useMessage()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = reactive({ page: 1, size: 20, orderStatusId: null })

const createDialogVisible = ref(false)
const templates = ref([])
const templateLoading = ref(false)
const selectedTemplateId = ref(null)
const creating = ref(false)

// 业务状态枚举
const orderStatusOptions = ref([])
const orderStatusMap = ref({})

const columns = [
  { title: 'ID', key: 'id', width: 70 },
  { title: '服务单名称', key: 'templateName' },
  { title: '版本', key: 'version', width: 80 },
  { title: '国家', key: 'countryCode', width: 80 },
  { title: '服务类型', key: 'serviceCodeL3', width: 120 },
  {
    title: '业务状态',
    key: 'orderStatusId',
    width: 120,
    render: (row) => h(NTag, {
      type: getTagType(row.orderStatusId),
      size: 'small'
    }, { default: () => orderStatusName(row.orderStatusId) })
  },
  { title: '服务开始', key: 'serviceStartTime', width: 165 },
  { title: '服务结束', key: 'serviceEndTime', width: 165 },
  { title: '创建时间', key: 'createTime', width: 165 },
  {
    title: '操作',
    key: 'actions',
    width: 180,
    render: (row) => h(NSpace, null, {
      default: () => [
        h(NButton, {
          size: 'small',
          quaternary: true,
          onClick: () => goForm(row.id)
        }, { default: () => row.status === 0 ? '填写' : '查看' }),
        row.status === 0 ? h(NButton, {
          size: 'small',
          quaternary: true,
          type: 'success',
          onClick: () => handleSubmit(row.id)
        }, { default: () => '提交' }) : null
      ]
    })
  }
]

const templateColumns = [
  { title: 'ID', key: 'id', width: 60 },
  { title: '模板名称', key: 'templateName' },
  { title: '国家', key: 'countryCode', width: 80 },
  { title: '服务类型', key: 'serviceCodeL3', width: 120 },
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
    total.value = res.data.total
  } finally {
    loading.value = false
  }
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
.page { display: flex; flex-direction: column; gap: 16px; }
.page-toolbar { display: flex; justify-content: space-between; align-items: center; }
</style>
