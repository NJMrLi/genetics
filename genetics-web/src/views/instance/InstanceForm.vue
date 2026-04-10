
<template>
  <div class="page-container">
    <div class="page-header">
      <div class="header-left">
        <n-button quaternary circle @click="goBack">
          <template #icon><n-icon><ArrowBackOutline /></n-icon></template>
        </n-button>
        <div class="title" v-if="detail">
          {{ detail.templateName }}
          <n-tag :type="orderTagType" round size="small" style="margin-left: 12px">
            {{ detail.orderStatusName }}
          </n-tag>
        </div>
      </div>
      <div class="header-actions" v-if="detail">
        <n-space>
          <n-button @click="goBack">取消</n-button>
          <!-- 只有在 待提交(10) 或 已驳回(50) 状态下显示 保存和提交 -->
          <template v-if="detail.orderStatusId === 10 || detail.orderStatusId === 50">
            <n-button secondary @click="handleSave" :loading="saving">
              <template #icon><n-icon><SaveOutline /></n-icon></template>
              保存
            </n-button>
            <n-button type="primary" @click="confirmSubmit" :loading="submitting">
              <template #icon><n-icon><SendOutline /></n-icon></template>
              提交
            </n-button>
          </template>
          <!-- 其他状态下，如果是管理员等角色，可能也会显示动态动作（可选） -->
        </n-space>
      </div>
    </div>

    <n-grid :cols="24" :x-gap="16">
      <n-gi :span="18">
        <n-space vertical :size="16">
          <n-alert title="信息汇总" type="info" ghost>
            这里展示了服务单在整个生命周期中已填写的全部数据。
          </n-alert>

          <!-- 动态表单：编辑模式显示表单，非编辑模式显示详情汇总 -->
          <n-card :title="selectedAction ? `当前任务：${selectedAction.actionName}` : '单据数据'" :bordered="false">
            <template #header-extra>
              <n-tag v-if="selectedAction" type="primary" size="small">流转至：{{ selectedAction.toName || '下一环节' }}</n-tag>
              <n-tag v-else type="info" size="small">{{ isEditMode ? '编辑数据' : '数据汇总' }}</n-tag>
            </template>
            
            <n-space vertical :size="24">
              <!-- 1. 数据对照区：显示各历史阶段的表单原貌 -->
              <div v-if="historySchemas.length > 0 || historyDataList.length > 0">
                <div style="font-size: 14px; font-weight: 500; margin-bottom: 16px; color: #18a058; display: flex; align-items: center;">
                  <n-icon style="margin-right: 4px"><InformationCircleOutline /></n-icon>
                  流程表单历史回顾
                </div>
                
                <n-space vertical :size="32">
                  <!-- 循环渲染历史动作表单 -->
                  <div v-for="history in historySchemas" :key="history.action" class="history-section">
                    <div style="font-size: 12px; color: #999; margin-bottom: 8px;">阶段：{{ history.actionName }}</div>
                    <dynamic-form
                      :schema="history.formSchema"
                      :control-details="detail.controlDetails"
                      :model-value="formData"
                      readonly
                    />
                  </div>
                </n-space>
                
                <n-divider dashed title-placement="left" v-if="isEditMode && activeFormSchema">正在处理的任务</n-divider>
              </div>

              <!-- 2. 当前任务表单：该阶段必须填写的字段 -->
              <div v-if="isEditMode && activeFormSchema">
                <dynamic-form
                  ref="dynamicFormRef"
                  :schema="activeFormSchema"
                  :control-details="detail.controlDetails"
                  v-model="formData"
                />
              </div>

              <!-- 3. 无内容提示 -->
              <div v-if="!historyDataList.length && !activeFormSchema && detail">
                <n-empty description="该阶段暂无需要填写的表单内容" />
              </div>
            </n-space>
          </n-card>

          <!-- 历史备注 -->
          <n-card title="流程历史" :bordered="false">
             <!-- TODO: 这里可以展示操作记录 -->
             <n-text depth="3">暂无操作历史记录</n-text>
          </n-card>
        </n-space>
      </n-gi>

      <n-gi :span="6">
        <n-card title="操作面板" :bordered="false" class="sticky-card">
          <n-space vertical>
            <div class="status-info">
              <n-text depth="3">当前状态</n-text>
              <div class="status-val">{{ detail?.orderStatusName || '未知' }}</div>
            </div>
            
            <n-divider />

            <n-space vertical v-if="availableActions.length">
              <n-button
                v-for="action in availableActions"
                :key="action.action"
                block
                :type="getActionType(action.action)"
                @click="handleWorkflowAction(action)"
              >
                <template #icon>
                  <n-icon><FlashOutline /></n-icon>
                </template>
                {{ action.actionName }}
              </n-button>
            </n-space>
            <n-empty v-else description="当前无可执行操作" />
          </n-space>
        </n-card>
      </n-gi>
    </n-grid>

    <!-- 动作执行弹窗（含备注和动态表单） -->
    <n-modal
      v-model:show="showActionModal"
      preset="card"
      :title="currentAction?.actionName"
      style="width: 600px"
    >
      <n-space vertical :size="20">
        <!-- 动态表单内容 -->
        <div v-if="currentAction?.formSchema">
          <dynamic-form
            :schema="currentAction.formSchema"
            :control-details="detail?.controlDetails"
            v-model="actionFormData"
          />
        </div>

        <!-- 备注框 -->
        <n-form-item label="原因/备注" v-if="currentAction?.needRemark || currentAction?.formSchema">
          <n-input
            v-model:value="actionRemark"
            type="textarea"
            placeholder="请输入备注说明..."
            :rows="3"
          />
        </n-form-item>
      </n-space>

      <template #footer>
        <n-space justify="end">
          <n-button @click="showActionModal = false">取消</n-button>
          <n-button type="primary" @click="handleActionSubmit">确认执行</n-button>
        </n-space>
      </template>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, h, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NCard,
  NButton,
  NTag,
  NSelect,
  NDatePicker,
  NForm,
  NFormItem,
  NGrid,
  NGi,
  NEmpty,
  NCode,
  NInput,
  NDescriptions,
  NDescriptionsItem,
  NText,
  NSpace,
  NIcon,
  NScrollbar,
  NModal,
  useMessage,
  useDialog
} from 'naive-ui'
import { 
  ArrowBackOutline, 
  SaveOutline, 
  SendOutline,
  InformationCircleOutline
} from '@vicons/ionicons5'
import DynamicForm from '@/components/DynamicForm/DynamicForm.vue'
import { 
  getInstance, 
  saveInstance, 
  executeAction,
  getOrderStatusOptions, 
  getAvailableActions, 
  executeTransition 
} from '@/api/formInstance'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const dialog = useDialog()
const instanceId = route.params.id

const loading = ref(false)
const saving = ref(false)
const submitting = ref(false)
const detail = ref(null)
const formData = ref({})
const dynamicFormRef = ref(null)
const submitResult = ref(null)

// 是否处于编辑模式
const isEditMode = computed(() => {
  if (!detail.value) return false
  // 1. 只要当前选中的动作有关联表单，就属于编辑模式
  if (activeFormSchema.value) return true
  
  // 2. 传统的业务状态判断
  const statusId = detail.value.orderStatusId
  return statusId === 10 || statusId === 50
})

// 业务状态枚举
const orderStatusOptions = ref([])
const orderStatusMap = ref({})

// 工作流可用操作
const availableActions = ref([])

// 动作表单相关
const showActionModal = ref(false)
const currentAction = ref(null)
const actionFormData = ref({})
const actionRemark = ref('')

// 当前选中的动作（用于平铺表单显示）
const selectedAction = ref(null)

// 自动初始化选中动作 (优先找带表单的动作)
watch(availableActions, (newActions) => {
  if (newActions && newActions.length > 0) {
    // 找第一个带 Schema 的动作作为主填写任务
    selectedAction.value = newActions.find(a => a.formSchema) || newActions[0]
  }
}, { immediate: true })

// 当前页面真正要渲染的表单 Schema
const activeFormSchema = computed(() => {
  // 1. 如果选中了动作且它带 Schema，显示动作表单
  if (selectedAction.value?.formSchema) return selectedAction.value.formSchema
  
  // 2. 否则，回退到基础表单 Schema
  return detail.value?.jsonSchema || null
})

// 获取所有历史阶段的 Schema (用于渲染已填写的历史表单)
const historySchemas = computed(() => {
  if (!detail.value?.workflowConfig?.transitions) return []
  
  // 核心逻辑：找出所有不是当前选中动作、且有表单、且表单中有数据的历史动作
  return detail.value.workflowConfig.transitions.filter(t => {
    // 1. 必须有表单
    if (!t.formSchema) return false
    // 2. 不是当前正在填写的动作 (避免重复显示)
    if (selectedAction.value?.action === t.action) return false
    
    // 3. 该表单中至少有一个字段在 formData 中有值 (代表该阶段被填过)
    // 逻辑：解析 Schema 拿到所有 key，检查 formData
    let schema = t.formSchema
    if (typeof schema === 'string') {
      try { schema = JSON.parse(schema) } catch(e) { return false }
    }
    
    const fields = []
    schema.rows?.forEach(r => r.cells?.forEach(c => fields.push(c.controlKey)))
    return fields.some(f => formData.value[f] !== undefined && formData.value[f] !== '')
  })
})

// 格式化后的历史数据列表 (带 Label，包含当前正在填写的实时数据)
const historyDataList = computed(() => {
  // 核心逻辑：我们不再区分“历史”和“当前”，而是显示该服务单目前所有的 formData。
  // 因为 Vue 的响应式，你在下方输入时，这里会同步更新，实现实时对照。
  if (!formData.value || !detail.value?.controlDetails) return []
  
  const entries = Object.entries(formData.value)
  if (entries.length === 0) return []

  return entries.map(([key, value]) => {
    // 匹配控件中文名
    const control = detail.value.controlDetails.find(c => c.controlKey === key)
    return {
      key,
      label: control?.controlName || key,
      value: (value === null || value === undefined || value === '') ? '-' : value
    }
  }).filter(item => item.value !== '-') // 仅显示有内容的，避免干扰
})

// 服务信息表单
const metaForm = reactive({
  orderStatusId: null,
  serviceStartTime: null,
  serviceEndTime: null
})

const orderTagType = computed(() => {
  const code = metaForm.orderStatusId
  if (!code) return 'default'
  const tagType = orderStatusMap.value[code]?.tagType || 'info'
  const mapping = {
    'success': 'success',
    'warning': 'warning',
    'danger': 'error',
    'info': 'info'
  }
  return mapping[tagType] || 'info'
})

async function loadOrderStatusOptions() {
  const res = await getOrderStatusOptions()
  orderStatusOptions.value = (res.data || []).map(s => ({
    label: s.name,
    value: s.code
  }))
  orderStatusMap.value = Object.fromEntries(res.data.map(s => [s.code, s]))
}

async function fetchDetail() {
  loading.value = true
  try {
    const res = await getInstance(instanceId)
    const data = res.data
    
    // 兼容 jsonSchema 是字符串的情况 (防止双重编码遗留问题)
    if (data.jsonSchema && typeof data.jsonSchema === 'string') {
      try {
        data.jsonSchema = JSON.parse(data.jsonSchema)
        // 如果解析后还是字符串，说明编码了两次
        if (typeof data.jsonSchema === 'string') {
          data.jsonSchema = JSON.parse(data.jsonSchema)
        }
      } catch (e) {
        console.error('解析 jsonSchema 失败:', e)
      }
    }
    
    detail.value = data
    formData.value = data.formData || {}
    metaForm.orderStatusId = data.orderStatusId
    metaForm.serviceStartTime = data.serviceStartTime ? new Date(data.serviceStartTime).getTime() : null
    metaForm.serviceEndTime = data.serviceEndTime ? new Date(data.serviceEndTime).getTime() : null
    
    // 始终尝试加载可用操作，即使是草稿状态
    await loadAvailableActions()
  } finally {
    loading.value = false
  }
}

async function loadAvailableActions() {
  try {
    const res = await getAvailableActions(instanceId)
    availableActions.value = res.data || []
  } catch (err) {
    console.error('加载可用操作失败:', err)
    availableActions.value = []
  }
}

async function handleWorkflowAction(action) {
  // 如果点击的是不同的动作，且带表单，则先切换显示该动作的表单
  if (action.formSchema && selectedAction.value?.action !== action.action) {
    selectedAction.value = action
    return // 仅切换，不执行
  }

  currentAction.value = action
  actionRemark.value = ''
  actionFormData.value = {}
  
  // 如果动作带表单，且这个表单不是当前页面正在显示的那个（避免重复显示）
  if (action.formSchema && action.formSchema !== activeFormSchema.value) {
    showActionModal.value = true
  } else if (action.needRemark) {
    // 仅需要备注
    const remark = ref('')
    dialog.create({
      title: action.actionName,
      content: () => h(NInput, {
        type: 'textarea',
        placeholder: action.remarkLabel || '请输入原因/备注',
        value: remark.value,
        'on-update:value': (val) => { remark.value = val }
      }),
      positiveText: '确认',
      negativeText: '取消',
      onPositiveClick: async () => {
        // 如果当前执行的动作正是页面上平铺显示的动作，则需要带上表单数据
        const extraData = (action.action === selectedAction.value?.action) ? formData.value : null
        await doTransition(action.action, remark.value, extraData)
      }
    })
  } else {
    // 直接确认
    dialog.create({
      title: '确认操作',
      content: `确定要执行「${action.actionName}」吗？`,
      positiveText: '确认',
      negativeText: '取消',
      onPositiveClick: async () => {
        // 如果当前执行的动作正是页面上平铺显示的动作，则需要带上表单数据
        const extraData = (action.action === selectedAction.value?.action) ? formData.value : null
        await doTransition(action.action, '', extraData)
      }
    })
  }
}

async function handleActionSubmit() {
  await doTransition(currentAction.value.action, actionRemark.value, actionFormData.value)
  showActionModal.value = false
}

async function doTransition(action, remark, extraData = null) {
  try {
    await executeTransition(instanceId, {
      action,
      remark,
      actionFormData: extraData
    })
    message.success('操作成功')
    await fetchDetail()
  } catch (error) {
    message.error(error.message || '操作失败')
  }
}

async function handleSave() {
  saving.value = true
  try {
    await saveInstance(instanceId, {
      formData: formData.value,
      orderStatusId: metaForm.orderStatusId,
      serviceStartTime: metaForm.serviceStartTime ? new Date(metaForm.serviceStartTime).toISOString() : null,
      serviceEndTime: metaForm.serviceEndTime ? new Date(metaForm.serviceEndTime).toISOString() : null
    })
    message.success('保存成功')
    goBack() // 返回列表并触发刷新
  } finally {
    saving.value = false
  }
}

async function confirmSubmit() {
  if (!isEditMode.value) return

  try {
    // 1. 表单校验 (如果组件存在)
    if (dynamicFormRef.value) {
      await dynamicFormRef.value.validate()
    }
    
    dialog.info({
      title: '提交确认',
      content: '确认提交并更新业务状态吗？',
      positiveText: '确认',
      negativeText: '取消',
      onPositiveClick: async () => {
        submitting.value = true
        try {
          // 2. 先保存当前表单内容
          await saveInstance(instanceId, {
            formData: formData.value,
            orderStatusId: metaForm.orderStatusId,
            serviceStartTime: metaForm.serviceStartTime ? new Date(metaForm.serviceStartTime).toISOString() : null,
            serviceEndTime: metaForm.serviceEndTime ? new Date(metaForm.serviceEndTime).toISOString() : null
          })
          
          // 3. 执行提交（更新状态及触发业务逻辑）
          await executeAction(instanceId, { action: 'submit' })
          
          message.success('提交成功')
          goBack() // 返回列表并触发刷新
        } catch (err) {
          console.error('提交失败:', err)
          message.error('提交失败: ' + (err.response?.data?.message || err.message || '未知错误'))
        } finally {
          submitting.value = false
        }
      }
    })
  } catch (errors) {
    console.warn('校验不通过:', errors)
    message.error('表单校验未通过，请检查填写内容')
  }
}

function goBack() {
  router.push('/instance')
}

function getActionType(action) {
  if (action === 'auditReject' || action === 'terminate') return 'error'
  if (action === 'auditPass' || action === 'complete') return 'success'
  if (action.includes('submit')) return 'primary'
  return 'info'
}

onMounted(() => {
  loadOrderStatusOptions()
  fetchDetail()
})
</script>

<style scoped>
.page-container {
  padding: 24px;
}
.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}
.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.title {
  font-size: 20px;
  font-weight: 600;
  display: flex;
  align-items: center;
}
.sticky-card {
  position: sticky;
  top: 24px;
}
.status-info {
  text-align: center;
}
.status-val {
  font-size: 18px;
  font-weight: 600;
  color: #18a058;
  margin-top: 4px;
}
.form-card {
  height: 100%;
}
:deep(.n-descriptions-table-header) {
  width: 100px;
}
</style>
