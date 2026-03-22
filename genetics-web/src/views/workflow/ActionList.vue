<template>
  <div class="page-container">
    <div class="page-header">
      <div class="title">动作管理</div>
      <n-button type="primary" @click="openDialog()">
        <template #icon><n-icon><AddOutline /></n-icon></template>
        新增动作
      </n-button>
    </div>

    <n-card :bordered="false">
      <n-data-table
        :columns="columns"
        :data="list"
        :loading="loading"
        :bordered="false"
      />
    </n-card>

    <!-- 编辑弹窗 -->
    <n-modal v-model:show="showDialog" preset="card" :title="form.id ? '编辑动作' : '新增动作'" style="width: 500px">
      <n-form ref="formRef" :model="form" :rules="rules" label-placement="left" label-width="100">
        <n-form-item label="动作编码" path="actionCode">
          <n-input v-model:value="form.actionCode" placeholder="如: submit, auditPass" :disabled="!!form.id" />
        </n-form-item>
        <n-form-item label="动作名称" path="actionName">
          <n-input v-model:value="form.actionName" placeholder="如: 提交, 审核通过" />
        </n-form-item>
        <n-form-item label="按钮类型">
          <n-select v-model:value="form.buttonType" :options="buttonTypeOptions" />
        </n-form-item>
        <n-form-item label="图标">
          <n-input v-model:value="form.icon" placeholder="Ionicons 图标名称" />
        </n-form-item>
        <n-form-item label="需要备注">
          <n-switch v-model:value="form.needRemark" />
        </n-form-item>
        <n-form-item label="排序">
          <n-input-number v-model:value="form.sort" :min="0" />
        </n-form-item>
        <n-form-item label="描述">
          <n-input v-model:value="form.description" type="textarea" />
        </n-form-item>
      </n-form>
      <template #footer>
        <n-space justify="end">
          <n-button @click="showDialog = false">取消</n-button>
          <n-button type="primary" :loading="submitting" @click="handleSubmit">确定</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, h } from 'vue'
import {
  NButton, NCard, NDataTable, NIcon, NTag, NModal, NForm, NFormItem,
  NInput, NInputNumber, NSwitch, NSelect, NSpace, useMessage, useDialog
} from 'naive-ui'
import { AddOutline, CreateOutline, TrashOutline } from '@vicons/ionicons5'
import * as ionIcons from '@vicons/ionicons5'
import { listWorkflowActions, saveWorkflowAction, deleteWorkflowAction } from '@/api/workflowAction'

const message = useMessage()
const dialog = useDialog()
const loading = ref(false)
const submitting = ref(false)
const showDialog = ref(false)
const list = ref([])
const formRef = ref(null)

const form = reactive({
  id: null,
  actionCode: '',
  actionName: '',
  icon: '',
  buttonType: 'primary',
  needRemark: false,
  sort: 0,
  description: ''
})

const rules = {
  actionCode: [{ required: true, message: '请输入动作编码', trigger: 'blur' }],
  actionName: [{ required: true, message: '请输入动作名称', trigger: 'blur' }]
}

const buttonTypeOptions = [
  { label: '主要 (Primary)', value: 'primary' },
  { label: '信息 (Info)', value: 'info' },
  { label: '成功 (Success)', value: 'success' },
  { label: '警告 (Warning)', value: 'warning' },
  { label: '错误 (Error)', value: 'error' }
]

const columns = [
  { title: '排序', key: 'sort', width: 70 },
  { title: '动作名称', key: 'actionName', width: 120 },
  { title: '编码', key: 'actionCode', width: 120 },
  {
    title: '图标',
    key: 'icon',
    width: 80,
    render: (row) => {
      if (!row.icon || !ionIcons[row.icon]) return null
      return h(NIcon, { size: 20 }, { default: () => h(ionIcons[row.icon]) })
    }
  },
  {
    title: '按钮类型',
    key: 'buttonType',
    width: 100,
    render: (row) => h(NTag, { type: row.buttonType }, { default: () => row.buttonType })
  },
  {
    title: '默认备注',
    key: 'needRemark',
    width: 100,
    render: (row) => row.needRemark ? '是' : '否'
  },
  {
    title: '操作',
    key: 'actions',
    width: 150,
    render: (row) => h(NSpace, null, {
      default: () => [
        h(NButton, { size: 'small', onClick: () => openDialog(row) }, { icon: () => h(NIcon, null, { default: () => h(CreateOutline) }) }),
        h(NButton, { size: 'small', type: 'error', onClick: () => confirmDelete(row) }, { icon: () => h(NIcon, null, { default: () => h(TrashOutline) }) })
      ]
    })
  }
]

async function fetchList() {
  loading.value = true
  try {
    const res = await listWorkflowActions()
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

function openDialog(row) {
  if (row) {
    Object.assign(form, row)
  } else {
    Object.assign(form, {
      id: null, actionCode: '', actionName: '', icon: '', buttonType: 'primary', needRemark: false, sort: 0, description: ''
    })
  }
  showDialog.value = true
}

async function handleSubmit() {
  await formRef.value?.validate()
  submitting.value = true
  try {
    await saveWorkflowAction(form)
    message.success('保存成功')
    showDialog.value = false
    fetchList()
  } finally {
    submitting.value = false
  }
}

function confirmDelete(row) {
  dialog.warning({
    title: '删除确认',
    content: `确认删除动作「${row.actionName}」吗？`,
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await deleteWorkflowAction(row.id)
      message.success('删除成功')
      fetchList()
    }
  })
}

onMounted(fetchList)
</script>

<style scoped>
.page-container { padding: 24px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.title { font-size: 20px; font-weight: 600; }
</style>
