<template>
  <div class="page">
    <div class="page-toolbar">
      <div class="toolbar-left">
        <el-select v-model="query.countryCode" placeholder="国家" clearable size="small" style="width:120px" @change="fetchList">
          <el-option v-for="c in countries" :key="c.code" :value="c.code" :label="`${c.code} ${c.nameCn}`" />
        </el-select>
        <el-input v-model="query.serviceCodeL3" placeholder="三级服务code" clearable size="small" style="width:160px" @change="fetchList" />
        <el-button size="small" @click="fetchList">查询</el-button>
      </div>
      <el-button type="primary" size="small" @click="goDesigner()">
        <el-icon><Plus /></el-icon>新建模板
      </el-button>
    </div>

    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="templateName" label="模板名称" />
      <el-table-column prop="version" label="版本" width="80" />
      <el-table-column prop="countryCode" label="国家" width="80" />
      <el-table-column prop="serviceCodeL1" label="一级" width="70" />
      <el-table-column prop="serviceCodeL2" label="二级" width="80" />
      <el-table-column prop="serviceCodeL3" label="三级" width="90" />
      <el-table-column prop="status" label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
            {{ row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="goDesigner(row.id)">编辑</el-button>
          <el-button
            v-if="row.status === 0"
            size="small"
            type="success"
            @click="handlePublish(row.id)"
          >发布</el-button>
          <el-popconfirm title="确认删除？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { listTemplates, publishTemplate, deleteTemplate } from '@/api/formTemplate'
import { getCountries } from '@/api/basic'

const router = useRouter()
const loading = ref(false)
const list = ref([])
const total = ref(0)
const countries = ref([])
const query = reactive({ page: 1, size: 20, countryCode: '', serviceCodeL3: '' })

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
  ElMessage.success('发布成功')
  fetchList()
}

async function handleDelete(id) {
  await deleteTemplate(id)
  ElMessage.success('删除成功')
  fetchList()
}

onMounted(() => {
  fetchList()
  fetchCountries()
})
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-toolbar { display: flex; justify-content: space-between; align-items: center; }
.toolbar-left { display: flex; gap: 8px; align-items: center; }
.pagination { justify-content: flex-end; }
</style>
