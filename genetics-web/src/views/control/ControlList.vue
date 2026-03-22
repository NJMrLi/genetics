<template>
  <div class="page-container">
    <!-- 工具栏 -->
    <div class="page-header">
      <div class="title">控件管理</div>
      <n-space>
        <n-button secondary @click="importDialogVisible = true">
          <template #icon><n-icon><CloudUploadOutline /></n-icon></template>
          JSON 导入
        </n-button>
        <n-button type="primary" @click="openDialog()">
          <template #icon><n-icon><AddOutline /></n-icon></template>
          新增控件
        </n-button>
      </n-space>
    </div>

    <div class="toolbar">
      <n-space>
        <n-select
          v-model:value="query.groupName"
          :options="businessTypeOptions"
          placeholder="业务分组"
          clearable
          style="width: 160px"
          @update:value="fetchList"
        />
        <n-select
          v-model:value="query.controlType"
          :options="CONTROL_TYPES"
          placeholder="控件类型"
          clearable
          style="width: 140px"
          @update:value="fetchList"
        />
        <n-input
          v-model:value="query.keyword"
          placeholder="搜索控件名/key"
          clearable
          style="width: 200px"
          @keyup.enter="fetchList"
        />
        <n-button type="primary" ghost @click="fetchList">
          <template #icon><n-icon><SearchOutline /></n-icon></template>
          查询
        </n-button>
      </n-space>
    </div>

    <n-card :bordered="false" content-style="padding: 0;">
      <!-- 按业务分组展示 -->
      <n-spin :show="loading">
        <n-collapse v-model:expanded-names="activeGroups" style="padding: 16px">
          <n-collapse-item
            v-for="(items, groupName) in groupedList"
            :key="groupName"
            :name="groupName"
          >
            <template #header>
              <span class="group-header">{{ groupName }} ({{ items.length }})</span>
            </template>
            <n-data-table :columns="columns" :data="items" :bordered="false" size="small" />
          </n-collapse-item>
        </n-collapse>
        <n-empty v-if="!loading && !Object.keys(groupedList).length" description="暂无控件" />
      </n-spin>
    </n-card>

    <!-- 新增/编辑弹窗 -->
    <n-modal
      v-model:show="dialogVisible"
      :title="form.id ? '编辑控件' : '新增控件'"
      preset="dialog"
      style="width: 680px"
    >
      <n-form ref="formRef" :model="form" :rules="rules" label-width="100px" label-placement="left">
        <n-grid :cols="2" :x-gap="16">
          <n-gi>
            <n-form-item label="控件名称" path="controlName">
              <n-input v-model:value="form.controlName" placeholder="如：公司名称" />
            </n-form-item>
          </n-gi>
          <n-gi>
            <n-form-item label="控件Key" path="controlKey">
              <n-input v-model:value="form.controlKey" placeholder="如：Company.companyName" />
            </n-form-item>
          </n-gi>
          <n-gi>
            <n-form-item label="业务类型">
              <n-input v-model:value="form.businessType" placeholder="自动从controlKey提取" disabled />
            </n-form-item>
          </n-gi>
          <n-gi>
            <n-form-item label="控件类型" path="controlType">
              <n-select v-model:value="form.controlType" :options="CONTROL_TYPES" />
            </n-form-item>
          </n-gi>
          <n-gi>
            <n-form-item label="是否必填">
              <n-switch v-model:value="form.required" />
            </n-form-item>
          </n-gi>
        </n-grid>
        <n-form-item label="占位文本">
          <n-input v-model:value="form.placeholder" placeholder="占位提示文字" />
        </n-form-item>
        <n-form-item label="控件说明">
          <n-input v-model:value="form.tips" type="textarea" :rows="2" />
        </n-form-item>
        <n-grid :cols="2" :x-gap="16">
          <n-gi>
            <n-form-item label="最小长度">
              <n-input-number v-model:value="form.minLength" :min="0" style="width: 100%" />
            </n-form-item>
          </n-gi>
          <n-gi>
            <n-form-item label="最大长度">
              <n-input-number v-model:value="form.maxLength" :min="0" style="width: 100%" />
            </n-form-item>
          </n-gi>
        </n-grid>
        <n-form-item label="正则表达式">
          <n-input v-model:value="form.regexPattern" placeholder="如：^[a-zA-Z]{2,100}$" />
        </n-form-item>
        <n-form-item label="校验失败提示">
          <n-input v-model:value="form.regexMessage" placeholder="格式不正确，请重新输入" />
        </n-form-item>

        <!-- SELECT 类型展示选项配置 -->
        <n-form-item v-if="form.controlType === 'SELECT'" label="下拉选项">
          <n-input
            v-model:value="form.selectOptions"
            type="textarea"
            :rows="3"
            placeholder='JSON格式，如：[{"label":"德国","value":"DEU"}]'
          />
        </n-form-item>

        <!-- UPLOAD 类型展示上传配置 -->
        <n-form-item v-if="form.controlType === 'UPLOAD'" label="上传配置">
          <n-input
            v-model:value="form.uploadConfig"
            type="textarea"
            :rows="3"
            placeholder='JSON格式，如：{"maxCount":3,"accept":".pdf,.jpg","maxSizeMB":10}'
          />
        </n-form-item>

        <n-grid :cols="2" :x-gap="16">
          <n-gi>
            <n-form-item label="排序">
              <n-input-number v-model:value="form.sort" :min="0" style="width: 100%" />
            </n-form-item>
          </n-gi>
          <n-gi>
            <n-form-item label="是否启用">
              <n-switch v-model:value="form.enabled" />
            </n-form-item>
          </n-gi>
        </n-grid>
      </n-form>
      <template #action>
        <n-space>
          <n-button @click="dialogVisible = false">取消</n-button>
          <n-button type="primary" :loading="submitting" @click="handleSubmit">保存</n-button>
        </n-space>
      </template>
    </n-modal>

    <!-- JSON 导入弹窗 -->
    <n-modal
      v-model:show="importDialogVisible"
      preset="card"
      title="批量导入控件 (JSON)"
      style="width: 800px"
    >
      <n-alert type="info" style="margin-bottom: 16px">
        请输入 JSON 格式的控件定义数组。如果控件 Key 已存在，将执行更新操作。
      </n-alert>
      <n-input
        v-model:value="importJson"
        type="textarea"
        placeholder='[
  {
    "controlName": "公司名称",
    "controlKey": "Company.name",
    "controlType": "INPUT",
    "required": true,
    "placeholder": "请输入公司名称"
  }
]'
        :rows="15"
        style="font-family: monospace"
      />
      <template #footer>
        <n-space justify="end">
          <n-button @click="importDialogVisible = false">取消</n-button>
          <n-button type="primary" :loading="importing" @click="handleImport">
            执行导入
          </n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { h, ref, reactive, onMounted, computed, watch } from 'vue'
import {
  NButton,
  NSpace,
  NTag,
  NSwitch,
  NSelect,
  NInput,
  NInputNumber,
  NModal,
  NForm,
  NFormItem,
  NGrid,
  NGi,
  NCollapse,
  NCollapseItem,
  NDataTable,
  NSpin,
  NEmpty,
  NIcon,
  useMessage,
  useDialog
} from 'naive-ui'
import { SearchOutline, AddOutline, CreateOutline, TrashOutline, CloudUploadOutline } from '@vicons/ionicons5'
import { listAllControls, createControl, updateControl, deleteControl, getBusinessTypes } from '@/api/formControl'

const message = useMessage()
const dialog = useDialog()

const CONTROL_TYPES = [
  { label: '输入框', value: 'INPUT' },
  { label: '多行文本', value: 'TEXTAREA' },
  { label: '数字', value: 'NUMBER' },
  { label: '下拉框', value: 'SELECT' },
  { label: '开关', value: 'SWITCH' },
  { label: '日期', value: 'DATE' },
  { label: '文件上传', value: 'UPLOAD' }
]

// JSON 导入相关
const importDialogVisible = ref(false)
const importJson = ref('')
const importing = ref(false)

async function handleImport() {
  if (!importJson.value.trim()) {
    message.warning('请输入 JSON 内容')
    return
  }
  
  let data = []
  try {
    data = JSON.parse(importJson.value)
    if (!Array.isArray(data)) {
      message.error('JSON 必须是一个数组')
      return
    }
  } catch (e) {
    message.error('JSON 格式错误: ' + e.message)
    return
  }
  
  importing.value = true
  let successCount = 0
  let failCount = 0
  
  try {
    for (const item of data) {
      if (!item.controlKey || !item.controlName) {
        failCount++
        continue
      }
      
      // 检查是否存在
      const existing = list.value.find(c => c.controlKey === item.controlKey)
      try {
        if (existing) {
          await updateControl(existing.id, { ...existing, ...item })
        } else {
          await createControl(item)
        }
        successCount++
      } catch (err) {
        failCount++
      }
    }
    
    message.success(`导入完成：成功 ${successCount} 个，失败 ${failCount} 个`)
    importDialogVisible.value = false
    importJson.value = ''
    fetchList()
    fetchBusinessTypes()
  } finally {
    importing.value = false
  }
}

// 业务分组映射
const GROUP_MAPPING = {
  'Company': '公司信息',
  'CompanyLegalPerson': '法人信息',
  'CompanyTaxNo': '税号信息',
  'CompanyAttachment': '附件信息',
  'CompanyService': '服务信息',
  'CompanyBank': '银行信息',
  'CompanyContact': '联系人信息',
  'CompanyAddress': '地址信息',
  'CompanyShareholder': '股东信息',
  'CompanyBusiness': '业务信息'
}

const loading = ref(false)
const submitting = ref(false)
const list = ref([])
const query = reactive({ groupName: '', controlType: '', keyword: '' })
const activeGroups = ref([])
const businessTypeOptions = ref([])

// 表格列定义
const columns = [
  { title: 'ID', key: 'id', width: 60 },
  { title: '控件名称', key: 'controlName', width: 140 },
  { title: '控件Key', key: 'controlKey', width: 220, ellipsis: { tooltip: true } },
  {
    title: '类型',
    key: 'controlType',
    width: 100,
    render: (row) => h(NTag, { size: 'small' }, { default: () => row.controlType })
  },
  {
    title: '必填',
    key: 'required',
    width: 70,
    render: (row) => h(NTag, {
      size: 'small',
      type: row.required ? 'error' : 'default'
    }, { default: () => row.required ? '是' : '否' })
  },
  { title: '说明', key: 'tips', ellipsis: { tooltip: true } },
  {
    title: '启用',
    key: 'enabled',
    width: 80,
    render: (row) => h(NSwitch, {
      value: row.enabled,
      onUpdateValue: () => toggleEnabled(row)
    })
  },
  {
    title: '操作',
    key: 'actions',
    width: 180,
    fixed: 'right',
    render: (row) => h(NSpace, { size: 'small', wrap: false }, {
      default: () => [
        h(NButton, {
          size: 'small',
          secondary: true,
          type: 'primary',
          onClick: () => openDialog(row)
        }, { 
          default: () => '编辑',
          icon: () => h(NIcon, null, { default: () => h(CreateOutline) })
        }),
        h(NButton, {
          size: 'small',
          secondary: true,
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

// 从 controlKey 提取业务类型前缀
function getGroupPrefix(controlKey) {
  if (!controlKey) return '其他'
  return controlKey.split('.')[0]
}

// 分组显示标签
function getGroupLabel(prefix) {
  return GROUP_MAPPING[prefix] || prefix
}

// 计算分组后的列表
const groupedList = computed(() => {
  let filtered = list.value
  
  if (query.groupName) {
    filtered = filtered.filter(item => getGroupPrefix(item.controlKey) === query.groupName)
  }
  
  if (query.controlType) {
    filtered = filtered.filter(item => item.controlType === query.controlType)
  }
  
  if (query.keyword) {
    const kw = query.keyword.toLowerCase()
    filtered = filtered.filter(item => 
      (item.controlName && item.controlName.toLowerCase().includes(kw)) ||
      (item.controlKey && item.controlKey.toLowerCase().includes(kw))
    )
  }
  
  const groups = {}
  filtered.forEach(item => {
    const prefix = getGroupPrefix(item.controlKey)
    const label = getGroupLabel(prefix)
    if (!groups[label]) {
      groups[label] = []
    }
    groups[label].push(item)
  })
  
  Object.keys(groups).forEach(key => {
    groups[key].sort((a, b) => (a.sort || 0) - (b.sort || 0))
  })
  
  return groups
})

const dialogVisible = ref(false)
const formRef = ref(null)
const form = reactive({
  id: null, controlName: '', controlKey: '', businessType: '', controlType: 'INPUT',
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

watch(() => form.controlKey, (val) => {
  if (val && val.includes('.')) {
    form.businessType = val.split('.')[0]
  }
})

async function fetchList() {
  loading.value = true
  try {
    const res = await listAllControls()
    list.value = res.data || []
    activeGroups.value = Object.keys(groupedList.value)
  } finally {
    loading.value = false
  }
}

function openDialog(row) {
  if (row) {
    Object.assign(form, row)
    if (!form.businessType && form.controlKey) {
      form.businessType = form.controlKey.split('.')[0]
    }
  } else {
    Object.assign(form, {
      id: null, controlName: '', controlKey: '', businessType: '', controlType: 'INPUT',
      placeholder: '', tips: '', required: false, regexPattern: '', regexMessage: '',
      minLength: null, maxLength: null, selectOptions: '', uploadConfig: '',
      defaultValue: '', sort: 0, enabled: true
    })
  }
  dialogVisible.value = true
}

async function handleSubmit() {
  try {
    await formRef.value?.validate()
  } catch {
    return
  }
  submitting.value = true
  try {
    if (form.id) {
      await updateControl(form.id, form)
    } else {
      await createControl(form)
    }
    message.success('保存成功')
    dialogVisible.value = false
    fetchList()
  } finally {
    submitting.value = false
  }
}

function confirmDelete(row) {
  dialog.warning({
    title: '删除确认',
    content: '确认删除该控件？',
    positiveText: '确认',
    negativeText: '取消',
    onPositiveClick: async () => {
      await deleteControl(row.id)
      message.success('删除成功')
      fetchList()
    }
  })
}

async function toggleEnabled(row) {
  await updateControl(row.id, { ...row, enabled: !row.enabled })
  message.success('更新成功')
  fetchList()
}

async function fetchBusinessTypes() {
  try {
    const res = await getBusinessTypes()
    businessTypeOptions.value = (res.data || []).map(item => ({
      label: GROUP_MAPPING[item] || item,
      value: item
    }))
  } catch {
    businessTypeOptions.value = Object.keys(GROUP_MAPPING).map(key => ({
      label: GROUP_MAPPING[key],
      value: key
    }))
  }
}

onMounted(() => {
  fetchList()
  fetchBusinessTypes()
})
</script>

<style scoped>
.page-container { width: 100%; }
.group-header { font-weight: 600; font-size: 14px; }
</style>
