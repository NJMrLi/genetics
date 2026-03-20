<template>
  <div class="page">
    <div class="page-toolbar">
      <div class="toolbar-left">
        <el-select v-model="query.orderStatusId" placeholder="业务状态" clearable size="small" style="width:140px" @change="fetchList">
          <el-option
            v-for="s in orderStatusOptions"
            :key="s.code"
            :value="s.code"
            :label="s.name"
          />
        </el-select>
        <el-button size="small" @click="fetchList">查询</el-button>
      </div>
      <el-button type="primary" size="small" @click="createDialogVisible = true">
        <el-icon><Plus /></el-icon>新建服务单
      </el-button>
    </div>

    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="templateName" label="服务单名称" />
      <el-table-column prop="version" label="版本" width="80" />
      <el-table-column prop="countryCode" label="国家" width="80" />
      <el-table-column prop="serviceCodeL3" label="服务类型" width="110" />
      <el-table-column label="业务状态" width="120">
        <template #default="{ row }">
          <el-tag :type="orderStatusTag(row.orderStatusId)" size="small">
            {{ orderStatusName(row.orderStatusId) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="serviceStartTime" label="服务开始" width="165" />
      <el-table-column prop="serviceEndTime" label="服务结束" width="165" />
      <el-table-column prop="createTime" label="创建时间" width="165" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="goForm(row.id)">
            {{ row.status === 0 ? '填写' : '查看' }}
          </el-button>
          <el-button
            v-if="row.status === 0"
            size="small"
            type="success"
            @click="handleSubmit(row.id)"
          >提交</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      v-model:current-page="query.page"
      v-model:page-size="query.size"
      :total="total"
      layout="total, prev, pager, next"
      class="pagination"
      @change="fetchList"
    />

    <!-- 新建服务单弹窗：选择模板 -->
    <el-dialog v-model="createDialogVisible" title="选择服务单模板" width="600px">
      <el-table
        :data="templates"
        border
        highlight-current-row
        @current-change="selectedTemplate = $event"
        v-loading="templateLoading"
      >
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="templateName" label="模板名称" />
        <el-table-column prop="countryCode" label="国家" width="70" />
        <el-table-column prop="serviceCodeL3" label="服务类型" width="100" />
        <el-table-column prop="version" label="版本" width="70" />
      </el-table>
      <template #footer>
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" :disabled="!selectedTemplate" :loading="creating" @click="handleCreate">
          创建服务单
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { listInstances, createInstance, submitInstance, getOrderStatusOptions } from '@/api/formInstance'
import { listTemplates } from '@/api/formTemplate'

const router = useRouter()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = reactive({ page: 1, size: 20, orderStatusId: null })

const createDialogVisible = ref(false)
const templates = ref([])
const templateLoading = ref(false)
const selectedTemplate = ref(null)
const creating = ref(false)

// 业务状态枚举（从后端加载）
const orderStatusOptions = ref([])
const orderStatusMap = ref({})   // code → { name, tagType }

async function loadOrderStatusOptions() {
  const res = await getOrderStatusOptions()
  orderStatusOptions.value = res.data
  orderStatusMap.value = Object.fromEntries(res.data.map(s => [s.code, s]))
}

function orderStatusName(code) {
  return orderStatusMap.value[code]?.name || code
}
function orderStatusTag(code) {
  return orderStatusMap.value[code]?.tagType || 'info'
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

function goForm(id) {
  router.push(`/instance/form/${id}`)
}

async function handleCreate() {
  if (!selectedTemplate.value) return
  creating.value = true
  try {
    const res = await createInstance({ templateId: selectedTemplate.value.id })
    ElMessage.success('服务单创建成功')
    createDialogVisible.value = false
    router.push(`/instance/form/${res.data.instanceId}`)
  } finally {
    creating.value = false
  }
}

async function handleSubmit(id) {
  const res = await submitInstance(id)
  ElMessage.success('提交成功')
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
.toolbar-left { display: flex; gap: 8px; align-items: center; }
.pagination { justify-content: flex-end; }
</style>
