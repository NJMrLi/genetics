
<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">模板管理</div>
      <n-button type="primary" @click="goCreate">
        <template #icon><n-icon><AddOutline /></n-icon></template>
        新建模板
      </n-button>
    </div>

    <div class="toolbar">
      <n-space>
        <n-select
          v-model:value="query.countryCode"
          :options="countryOptions"
          placeholder="国家"
          clearable
          style="width: 180px"
          @update:value="fetchList"
        />
        <n-input
          v-model:value="query.serviceCodeL3"
          placeholder="服务类目代码"
          clearable
          style="width: 200px"
          @keyup.enter="fetchList"
        >
          <template #prefix>
            <n-icon><SearchOutline /></n-icon>
          </template>
        </n-input>
        <n-button type="primary" ghost @click="fetchList">
          查询
        </n-button>
        <n-button @click="resetQuery">
          重置
        </n-button>
      </n-space>
    </div>

    <n-card :bordered="false" content-style="padding: 0;">
      <n-data-table
        :columns="columns"
        :data="list"
        :loading="loading"
        :bordered="false"
        :scroll-x="1000"
        remote
        :pagination="pagination"
      />
    </n-card>
  </div>
</template>

<script setup>
import { h, ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import {
  NButton,
  NSpace,
  NTag,
  NTooltip,
  NSelect,
  NInput,
  NDataTable,
  NIcon,
  NCard,
  useMessage,
  useDialog
} from 'naive-ui'
import { 
  SearchOutline, 
  AddOutline, 
  CheckmarkCircleOutline, 
  AlertCircleOutline,
  CreateOutline,
  GitNetworkOutline,
  CloudUploadOutline,
  TrashOutline
} from '@vicons/ionicons5'
import { listTemplates, publishTemplate, deleteTemplate } from '@/api/formTemplate'
import { getCountries } from '@/api/basic'

const router = useRouter()
const message = useMessage()
const dialog = useDialog()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const countries = ref([])
const query = reactive({ page: 1, size: 10, countryCode: null, serviceCodeL3: '' })

// 服务类目映射 (简化显示)
const SERVICE_NAME_MAP = {
  '01': 'VAT',
  '02': 'EPR',
  '0101': 'VAT注册申报',
  '010101': 'VAT新注册申报'
  // ... 其他映射可以从接口获取或保持现状
}

function getServiceName(code) {
  return SERVICE_NAME_MAP[code] || code
}

const countryOptions = computed(() => 
  countries.value.map(c => ({
    label: `${c.nameCn} (${c.code})`,
    value: c.code
  }))
)

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

function getCountryName(code) {
  const c = countries.value.find(c => c.code === code)
  return c ? c.nameCn : code
}

function renderConfigStatus(configured, label) {
  return h(NTooltip, null, {
    trigger: () => h(NIcon, {
      size: 18,
      color: configured ? '#18a058' : '#faad14',
      style: 'cursor: help'
    }, { default: () => h(configured ? CheckmarkCircleOutline : AlertCircleOutline) }),
    default: () => configured ? `已配置${label}` : `未配置${label}`
  })
}

const columns = [
  { title: 'ID', key: 'id', width: 80, align: 'center' },
  { title: '模板名称', key: 'templateName', minWidth: 150 },
  { title: '版本', key: 'version', width: 80, align: 'center' },
  {
    title: '国家',
    key: 'countryCode',
    width: 100,
    render: (row) => h(NTag, { bordered: false, type: 'info' }, { default: () => getCountryName(row.countryCode) })
  },
  {
    title: '服务类目',
    key: 'serviceCodeL3',
    minWidth: 150,
    render: (row) => getServiceName(row.serviceCodeL3)
  },
  {
    title: '配置',
    key: 'configStatus',
    width: 100,
    align: 'center',
    render: (row) => {
      // 在动作驱动模式下，检查是否有任何动作配置了表单
      let hasForm = false
      if (row.workflowConfig) {
        try {
          const config = typeof row.workflowConfig === 'string' ? JSON.parse(row.workflowConfig) : row.workflowConfig
          hasForm = config.transitions?.some(t => t.formSchema && t.formSchema !== '{}')
        } catch (e) {
          hasForm = false
        }
      }
      const hasWorkflow = row.workflowConfig != null
      return h(NSpace, { size: 12, justify: 'center' }, {
        default: () => [
          renderConfigStatus(hasForm, '表单'),
          renderConfigStatus(hasWorkflow, '流程')
        ]
      })
    }
  },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) => h(NTag, {
      type: row.status === 1 ? 'success' : 'warning',
      round: true,
      bordered: false
    }, { default: () => row.status === 1 ? '已发布' : '草稿' })
  },
  {
    title: '操作',
    key: 'actions',
    width: 380,
    fixed: 'right',
    render: (row) => h(NSpace, { size: 'small', wrap: false }, {
      default: () => [
        h(NButton, {
          size: 'small',
          quaternary: true,
          type: 'primary',
          onClick: () => goWorkflow(row.id)
        }, { 
          default: () => '流程',
          icon: () => h(NIcon, null, { default: () => h(GitNetworkOutline) })
        }),
        row.status === 0 ? h(NButton, {
          size: 'small',
          secondary: true,
          type: 'success',
          onClick: () => handlePublish(row.id)
        }, { 
          default: () => '发布',
          icon: () => h(NIcon, null, { default: () => h(CloudUploadOutline) })
        }) : null,
        h(NButton, {
          size: 'small',
          quaternary: true,
          type: 'error',
          onClick: () => confirmDelete(row)
        }, { 
          default: () => '删除',
          icon: () => h(NIcon, null, { default: () => h(TrashOutline) })
        })
      ]
    })
  }
]

async function fetchList() {
  loading.value = true
  try {
    const res = await listTemplates(query)
    list.value = res.data.records
    pagination.itemCount = res.data.total
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  query.countryCode = null
  query.serviceCodeL3 = ''
  query.page = 1
  pagination.page = 1
  fetchList()
}

async function fetchCountries() {
  const res = await getCountries()
  countries.value = res.data
}

function goCreate() {
  router.push('/template/create')
}

function goWorkflow(id) {
  router.push(`/template/workflow-designer/${id}`)
}

async function handlePublish(id) {
  await publishTemplate(id)
  message.success('发布成功')
  fetchList()
}

function confirmDelete(row) {
  dialog.warning({
    title: '删除确认',
    content: `确认删除模板 "${row.templateName}" 吗？此操作不可撤销。`,
    positiveText: '确认删除',
    negativeText: '取消',
    onPositiveClick: async () => {
      await deleteTemplate(row.id)
      message.success('删除成功')
      fetchList()
    }
  })
}

onMounted(() => {
  fetchCountries()
  fetchList()
})
</script>

<style scoped>
.page-container {
  width: 100%;
}
</style>
