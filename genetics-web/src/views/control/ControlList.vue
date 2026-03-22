<template>
  <div class="page-container">
    <!-- 工具栏 -->
    <div class="page-header">
      <div class="header-left">
        <n-button v-if="viewMode === 'detail'" circle secondary @click="backToGrid" style="margin-right: 12px">
          <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
        </n-button>
        <div class="title">
          {{ viewMode === 'grid' ? '控件管理' : `控件详情 - ${currentGroup?.label}` }}
        </div>
      </div>
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

    <div class="toolbar" v-if="viewMode === 'grid'">
      <n-space>
        <n-input
          v-model:value="query.keyword"
          placeholder="搜索业务类名/中文名"
          clearable
          style="width: 260px"
          @keyup.enter="page = 1"
        >
          <template #prefix>
            <n-icon><SearchOutline /></n-icon>
          </template>
        </n-input>
        <n-button type="primary" ghost @click="page = 1">
          查询
        </n-button>
      </n-space>
    </div>

    <n-spin :show="loading">
      <!-- 1. 卡片网格视图 -->
      <div v-if="viewMode === 'grid'" class="grid-view">
        <n-grid :cols="4" :x-gap="16" :y-gap="16" v-if="paginatedGroups.length">
          <n-gi v-for="group in paginatedGroups" :key="group.name">
            <n-card
              hoverable
              class="group-card"
              @click="showDetail(group)"
              :content-style="{ padding: '20px' }"
            >
              <div class="card-content">
                <div class="card-icon">
                  <n-icon size="32" color="#18a058"><CubeOutline /></n-icon>
                </div>
                <div class="card-info">
                  <div class="card-title">{{ group.label }}</div>
                  <div class="card-subtitle">{{ group.name }}</div>
                </div>
              </div>
              <template #footer>
                <div class="card-footer">
                  <n-space justify="space-between" align="center">
                    <n-statistic label="总控件" :value="group.count" size="small" />
                    <n-statistic label="已启用" :value="group.enabledCount" size="small" />
                    <n-button quaternary circle type="primary">
                      <template #icon><n-icon><ChevronForwardOutline /></n-icon></template>
                    </n-button>
                  </n-space>
                </div>
              </template>
            </n-card>
          </n-gi>
        </n-grid>
        <n-empty v-else description="暂无控件分组" style="padding: 40px" />
        
        <div class="pagination-container" v-if="groupStats.length > pageSize">
          <n-pagination
            v-model:page="page"
            :item-count="groupStats.length"
            :page-size="pageSize"
            show-quick-jumper
          />
        </div>
      </div>

      <!-- 2. 详情视图 -->
      <div v-else class="detail-view">
        <n-card :bordered="false" content-style="padding: 0;">
          <n-data-table
            :columns="columns"
            :data="currentGroupControls"
            :bordered="false"
            size="small"
            style="min-height: 400px"
          />
        </n-card>
      </div>
    </n-spin>

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
  NDataTable,
  NSpin,
  NEmpty,
  NIcon,
  NPagination,
  NCard,
  NStatistic,
  NTooltip,
  NAlert,
  useMessage,
  useDialog
} from 'naive-ui'
import { 
  SearchOutline, 
  AddOutline, 
  CreateOutline, 
  TrashOutline, 
  CloudUploadOutline,
  ChevronForwardOutline,
  ArrowBackOutline,
  CubeOutline
} from '@vicons/ionicons5'
import { listAllControls, createControl, updateControl, deleteControl, getBusinessTypes } from '@/api/formControl'

const message = useMessage()
const dialog = useDialog()

// 视图模式: 'grid' - 分类卡片网格, 'detail' - 分类下的控件详情
const viewMode = ref('grid')
const currentGroup = ref(null) // 当前选中的业务类型

// 分页相关
const page = ref(1)
const pageSize = ref(12) // 每页 12 个卡片

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
  'CompanyBusiness': '业务信息',
  'CompanyUser': '员工信息',
  'CompanyEprCategoryBrand': 'EPR类目',
  'CompanyOverseaManage': '境外经理人',
  'CompanyPersonnel': '人员信息',
  'CompanySales': '销售额信息'
}

const loading = ref(false)
const submitting = ref(false)
const list = ref([])
const query = reactive({ controlType: '', keyword: '' })
const businessTypeOptions = ref([])

// 获取所有业务分组及其控件数量
const groupStats = computed(() => {
  const stats = {}
  list.value.forEach(item => {
    const prefix = item.controlKey?.split('.')[0] || '其他'
    if (!stats[prefix]) {
      stats[prefix] = {
        name: prefix,
        label: GROUP_MAPPING[prefix] || prefix,
        count: 0,
        enabledCount: 0
      }
    }
    stats[prefix].count++
    if (item.enabled) stats[prefix].enabledCount++
  })
  
  let result = Object.values(stats)
  if (query.keyword) {
    const kw = query.keyword.toLowerCase()
    result = result.filter(g => 
      g.name.toLowerCase().includes(kw) || 
      g.label.toLowerCase().includes(kw)
    )
  }
  
  return result.sort((a, b) => b.count - a.count)
})

// 分页后的分组
const paginatedGroups = computed(() => {
  const start = (page.value - 1) * pageSize.value
  const end = start + pageSize.value
  return groupStats.value.slice(start, end)
})

// 当前选中分组下的控件列表
const currentGroupControls = computed(() => {
  if (!currentGroup.value) return []
  return list.value
    .filter(item => (item.controlKey?.split('.')[0] || '其他') === currentGroup.value.name)
    .sort((a, b) => (a.sort || 0) - (b.sort || 0))
})

// 切换到详情视图
function showDetail(group) {
  currentGroup.value = group
  viewMode.value = 'detail'
}

// 返回网格视图
function backToGrid() {
  viewMode.value = 'grid'
  currentGroup.value = null
}

const CONTROL_TYPES = [
  { label: '输入框', value: 'INPUT' },
  { label: '多行文本', value: 'TEXTAREA' },
  { label: '数字', value: 'NUMBER' },
  { label: '下拉框', value: 'SELECT' },
  { label: '开关', value: 'SWITCH' },
  { label: '日期', value: 'DATE' },
  { label: '文件上传', value: 'UPLOAD' }
]

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
  } finally {
    loading.value = false
  }
}

function openDialog(row) {
  if (row) {
    Object.assign(form, row)
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
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.header-left {
  display: flex;
  align-items: center;
}
.title {
  font-size: 20px;
  font-weight: 600;
}
.toolbar {
  margin-bottom: 24px;
}

.group-card {
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}
.group-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}

.card-content {
  display: flex;
  align-items: center;
  gap: 16px;
}
.card-icon {
  background: rgba(24, 160, 88, 0.1);
  padding: 12px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--n-title-text-color);
}
.card-subtitle {
  font-size: 12px;
  color: var(--n-text-color-3);
  margin-top: 4px;
}
.card-footer {
  border-top: 1px solid var(--n-border-color);
  padding-top: 12px;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
}

.detail-view {
  animation: slide-in 0.3s ease-out;
}

@keyframes slide-in {
  from { opacity: 0; transform: translateX(20px); }
  to { opacity: 1; transform: translateX(0); }
}
</style>
