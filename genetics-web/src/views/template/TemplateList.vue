<template>
  <div class="page">
    <div class="page-toolbar">
      <n-space>
        <n-select
          v-model:value="query.countryCode"
          :options="countryOptions"
          placeholder="国家"
          clearable
          style="width: 160px"
          @update:value="fetchList"
        />
        <n-input
          v-model:value="query.serviceCodeL3"
          placeholder="三级服务code"
          clearable
          style="width: 180px"
          @keyup.enter="fetchList"
        />
        <n-button type="primary" @click="fetchList">
          <template #icon><n-icon><SearchOutline /></n-icon></template>
          查询
        </n-button>
      </n-space>
      <n-button type="primary" @click="goCreate">
        <template #icon><n-icon><AddOutline /></n-icon></template>
        新建模板
      </n-button>
    </div>

    <n-data-table
      :columns="columns"
      :data="list"
      :loading="loading"
      :bordered="false"
      :scroll-x="900"
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
  </div>
</template>

<script setup>
import { h, ref, reactive, onMounted, computed, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  NButton,
  NSpace,
  NTag,
  NTooltip,
  NSelect,
  NInput,
  NDataTable,
  NPagination,
  NIcon,
  useMessage,
  useDialog
} from 'naive-ui'
import { SearchOutline, AddOutline, CheckmarkCircleOutline, AlertCircleOutline } from '@vicons/ionicons5'
import { listTemplates, publishTemplate, deleteTemplate } from '@/api/formTemplate'
import { getCountries } from '@/api/basic'

// 服务类目映射
const SERVICE_CATEGORIES = [
  { id: 1, parentId: 0, name: 'VAT', code: '01' },
  { id: 2, parentId: 0, name: 'EPR', code: '02' },
  { id: 3, parentId: 1, name: 'VAT注册申报', code: '0101' },
  { id: 4, parentId: 1, name: 'VAT申报', code: '0102' },
  { id: 5, parentId: 2, name: '包装法', code: '0201' },
  { id: 6, parentId: 2, name: '电池法', code: '0202' },
  { id: 7, parentId: 2, name: 'WEEE', code: '0203' },
  { id: 8, parentId: 2, name: '家具法', code: '0204' },
  { id: 9, parentId: 2, name: '轮胎法', code: '0205' },
  { id: 10, parentId: 2, name: '印刷纸法', code: '0206' },
  { id: 11, parentId: 2, name: '纺织品法', code: '0207' },
  { id: 12, parentId: 2, name: '化学用品法', code: '0208' },
  { id: 13, parentId: 2, name: '刺穿医疗设备法', code: '0209' },
  { id: 14, parentId: 3, name: 'VAT新注册申报', code: '010101' },
  { id: 15, parentId: 3, name: 'VAT转代理申报', code: '010102' },
  { id: 16, parentId: 3, name: 'VAT申报续费', code: '010103' },
  { id: 17, parentId: 3, name: 'VAT注销', code: '010104' },
  { id: 18, parentId: 3, name: 'VAT补申报', code: '010105' },
  { id: 20, parentId: 5, name: '包装法新注册申报', code: '020101' },
  { id: 21, parentId: 5, name: '包装法转代理申报', code: '020102' },
  { id: 22, parentId: 5, name: '包装法申报续费', code: '020103' },
  { id: 23, parentId: 5, name: '包装法注销', code: '020104' }
]

function getServiceName(code) {
  const item = SERVICE_CATEGORIES.find(s => s.code === code)
  return item ? item.name : code
}

const router = useRouter()
const route = useRoute()
const message = useMessage()
const dialog = useDialog()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const countries = ref([])
const query = reactive({ page: 1, size: 20, countryCode: '', serviceCodeL3: '' })

const countryOptions = computed(() => 
  countries.value.map(c => ({
    label: `${c.nameCn} (${c.code})`,
    value: c.code
  }))
)

// 根据 code 找国家中文名
function getCountryName(code) {
  const c = countries.value.find(c => c.code === code)
  return c ? c.nameCn : code
}

// 配置状态图标渲染
function renderConfigStatus(configured, label) {
  return h(NTooltip, null, {
    trigger: () => h(NIcon, {
      size: 16,
      color: configured ? '#18a058' : '#ccc',
      style: 'cursor: default'
    }, { default: () => h(configured ? CheckmarkCircleOutline : AlertCircleOutline) }),
    default: () => configured ? `已配置${label}` : `未配置${label}`
  })
}

const columns = [
  { title: 'ID', key: 'id', width: 60 },
  { title: '模板名称', key: 'templateName', minWidth: 120 },
  { title: '版本', key: 'version', width: 70 },
  {
    title: '国家',
    key: 'countryCode',
    width: 90,
    render: (row) => getCountryName(row.countryCode)
  },
  {
    title: '服务类目',
    key: 'serviceCodeL3',
    minWidth: 120,
    render: (row) => getServiceName(row.serviceCodeL3)
  },
  {
    title: '配置状态',
    key: 'configStatus',
    width: 90,
    render: (row) => {
      const hasForm = row.jsonSchema && row.jsonSchema !== '{}' && row.jsonSchema !== 'null'
      const hasWorkflow = row.workflowConfig != null
      return h(NSpace, { size: 6, wrap: false }, {
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
    width: 70,
    render: (row) => h(NTag, {
      type: row.status === 1 ? 'success' : 'default',
      size: 'small'
    }, { default: () => row.status === 1 ? '已发布' : '草稿' })
  },
  {
    title: '操作',
    key: 'actions',
    width: 260,
    render: (row) => h(NSpace, { wrap: false }, {
      default: () => [
        h(NButton, {
          size: 'small',
          quaternary: true,
          onClick: () => goDesigner(row.id)
        }, { default: () => '表单配置' }),
        h(NButton, {
          size: 'small',
          quaternary: true,
          onClick: () => goWorkflow(row.id)
        }, { default: () => '流程配置' }),
        row.status === 0 ? h(NButton, {
          size: 'small',
          quaternary: true,
          type: 'success',
          onClick: () => handlePublish(row.id)
        }, { default: () => '发布' }) : null,
        h(NButton, {
          size: 'small',
          quaternary: true,
          type: 'error',
          onClick: () => confirmDelete(row)
        }, { default: () => '删除' })
      ]
    })
  }
]

async function fetchList() {
  loading.value = true
  try {
    const res = await listTemplates(query)
    list.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

async function fetchCountries() {
  const res = await getCountries()
  countries.value = res.data
}

function goCreate() {
  router.push('/template/create')
}

function goDesigner(id) {
  router.push(`/template/form-designer/${id}`)
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
    content: '确认删除该模板？',
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await deleteTemplate(row.id)
      message.success('删除成功')
      fetchList()
    }
  })
}

onMounted(() => {
  fetchList()
  fetchCountries()
})

// 从子页面返回时刷新列表
watch(() => route.path, (newPath) => {
  if (newPath === '/template') {
    fetchList()
  }
})
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-toolbar { display: flex; justify-content: space-between; align-items: center; }
</style>
