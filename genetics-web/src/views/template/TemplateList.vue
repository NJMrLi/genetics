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
      <n-button type="primary" @click="goDesigner()">
        <template #icon><n-icon><AddOutline /></n-icon></template>
        新建模板
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
  NInput,
  NDataTable,
  NPagination,
  NIcon,
  useMessage,
  useDialog
} from 'naive-ui'
import { SearchOutline, AddOutline } from '@vicons/ionicons5'
import { listTemplates, publishTemplate, deleteTemplate } from '@/api/formTemplate'
import { getCountries } from '@/api/basic'

const router = useRouter()
const message = useMessage()
const dialog = useDialog()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const countries = ref([])
const query = reactive({ page: 1, size: 20, countryCode: '', serviceCodeL3: '' })

const countryOptions = computed(() => 
  countries.value.map(c => ({
    label: `${c.code} ${c.nameCn}`,
    value: c.code
  }))
)

const columns = [
  { title: 'ID', key: 'id', width: 60 },
  { title: '模板名称', key: 'templateName' },
  { title: '版本', key: 'version', width: 80 },
  { title: '国家', key: 'countryCode', width: 80 },
  { title: '一级', key: 'serviceCodeL1', width: 80 },
  { title: '二级', key: 'serviceCodeL2', width: 90 },
  { title: '三级', key: 'serviceCodeL3', width: 100 },
  {
    title: '状态',
    key: 'status',
    width: 100,
    render: (row) => h(NTag, {
      type: row.status === 1 ? 'success' : 'default',
      size: 'small'
    }, { default: () => row.status === 1 ? '已发布' : '草稿' })
  },
  {
    title: '操作',
    key: 'actions',
    width: 220,
    render: (row) => h(NSpace, null, {
      default: () => [
        h(NButton, {
          size: 'small',
          quaternary: true,
          onClick: () => goDesigner(row.id)
        }, { default: () => '编辑' }),
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

function goDesigner(id) {
  router.push(id ? `/template/designer/${id}` : '/template/designer')
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
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-toolbar { display: flex; justify-content: space-between; align-items: center; }
</style>
