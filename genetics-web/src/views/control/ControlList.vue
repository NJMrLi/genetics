<template>
  <div class="page">
    <!-- 工具栏 -->
    <div class="page-toolbar">
      <div class="toolbar-left">
        <el-select v-model="query.controlType" placeholder="控件类型" clearable size="small" style="width:120px" @change="fetchList">
          <el-option v-for="t in CONTROL_TYPES" :key="t.value" :value="t.value" :label="t.label" />
        </el-select>
        <el-input v-model="query.keyword" placeholder="搜索控件名/key" clearable size="small" style="width:200px" @change="fetchList" />
        <el-button size="small" @click="fetchList">查询</el-button>
      </div>
      <el-button type="primary" size="small" @click="openDialog()">
        <el-icon><Plus /></el-icon>新增控件
      </el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="list" border stripe v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="controlName" label="控件名称" width="140" />
      <el-table-column prop="controlKey" label="控件Key" width="220" show-overflow-tooltip />
      <el-table-column prop="controlType" label="类型" width="100">
        <template #default="{ row }">
          <el-tag size="small">{{ row.controlType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="required" label="必填" width="60">
        <template #default="{ row }">
          <el-tag :type="row.required ? 'danger' : 'info'" size="small">{{ row.required ? '是' : '否' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="tips" label="说明" show-overflow-tooltip />
      <el-table-column prop="enabled" label="启用" width="70">
        <template #default="{ row }">
          <el-switch :model-value="row.enabled" @change="toggleEnabled(row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="openDialog(row)">编辑</el-button>
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

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑控件' : '新增控件'" width="680px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="控件名称" prop="controlName">
              <el-input v-model="form.controlName" placeholder="如：公司名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控件Key" prop="controlKey">
              <el-input v-model="form.controlKey" placeholder="如：Company.companyName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="控件类型" prop="controlType">
              <el-select v-model="form.controlType" style="width:100%">
                <el-option v-for="t in CONTROL_TYPES" :key="t.value" :value="t.value" :label="t.label" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否必填" prop="required">
              <el-switch v-model="form.required" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="占位文本">
              <el-input v-model="form.placeholder" placeholder="占位提示文字" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="控件说明(TIPS)">
              <el-input v-model="form.tips" type="textarea" :rows="2" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最小长度">
              <el-input-number v-model="form.minLength" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大长度">
              <el-input-number v-model="form.maxLength" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="正则表达式">
              <el-input v-model="form.regexPattern" placeholder="如：^[a-zA-Z]{2,100}$" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="校验失败提示">
              <el-input v-model="form.regexMessage" placeholder="格式不正确，请重新输入" />
            </el-form-item>
          </el-col>

          <!-- SELECT 类型展示选项配置 -->
          <el-col v-if="form.controlType === 'SELECT'" :span="24">
            <el-form-item label="下拉选项">
              <el-input
                v-model="form.selectOptions"
                type="textarea"
                :rows="3"
                placeholder='JSON格式，如：[{"label":"德国","value":"DEU"}]'
              />
            </el-form-item>
          </el-col>

          <!-- UPLOAD 类型展示上传配置 -->
          <el-col v-if="form.controlType === 'UPLOAD'" :span="24">
            <el-form-item label="上传配置">
              <el-input
                v-model="form.uploadConfig"
                type="textarea"
                :rows="3"
                placeholder='JSON格式，如：{"maxCount":3,"accept":".pdf,.jpg","maxSizeMB":10}'
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="排序">
              <el-input-number v-model="form.sort" :min="0" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用">
              <el-switch v-model="form.enabled" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listControls, createControl, updateControl, deleteControl } from '@/api/formControl'

const CONTROL_TYPES = [
  { value: 'INPUT', label: '输入框' },
  { value: 'TEXTAREA', label: '多行文本' },
  { value: 'NUMBER', label: '数字' },
  { value: 'SELECT', label: '下拉框' },
  { value: 'SWITCH', label: '开关' },
  { value: 'DATE', label: '日期' },
  { value: 'UPLOAD', label: '文件上传' }
]

const loading = ref(false)
const submitting = ref(false)
const list = ref([])
const total = ref(0)
const query = reactive({ page: 1, size: 20, controlType: '', keyword: '' })

const dialogVisible = ref(false)
const formRef = ref(null)
const form = reactive({
  id: null, controlName: '', controlKey: '', controlType: 'INPUT',
  placeholder: '', tips: '', required: false, regexPattern: '', regexMessage: '',
  minLength: null, maxLength: null, selectOptions: '', uploadConfig: '',
  defaultValue: '', sort: 0, enabled: true
})

const rules = {
  controlName: [{ required: true, message: '请输入控件名称', trigger: 'blur' }],
  controlKey: [
    { required: true, message: '请输入控件Key', trigger: 'blur' },
    { pattern: /^[A-Za-z]+\.[A-Za-z]+$/, message: '格式必须为 ClassName.fieldName', trigger: 'blur' }
  ],
  controlType: [{ required: true, message: '请选择控件类型', trigger: 'change' }]
}

async function fetchList() {
  loading.value = true
  try {
    const res = await listControls(query)
    list.value = res.data.records
    total.value = res.data.total
  } finally {
    loading.value = false
  }
}

function openDialog(row) {
  if (row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      id: null, controlName: '', controlKey: '', controlType: 'INPUT',
      placeholder: '', tips: '', required: false, regexPattern: '', regexMessage: '',
      minLength: null, maxLength: null, selectOptions: '', uploadConfig: '',
      defaultValue: '', sort: 0, enabled: true
    })
  }
  dialogVisible.value = true
}

async function handleSubmit() {
  await formRef.value?.validate()
  submitting.value = true
  try {
    if (form.id) {
      await updateControl(form.id, form)
    } else {
      await createControl(form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    fetchList()
  } finally {
    submitting.value = false
  }
}

async function handleDelete(id) {
  await deleteControl(id)
  ElMessage.success('删除成功')
  fetchList()
}

async function toggleEnabled(row) {
  await updateControl(row.id, { ...row, enabled: !row.enabled })
  ElMessage.success('更新成功')
  fetchList()
}

onMounted(fetchList)
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; height: 100%; }
.page-toolbar { display: flex; justify-content: space-between; align-items: center; }
.toolbar-left { display: flex; gap: 8px; align-items: center; }
.pagination { justify-content: flex-end; }
</style>
