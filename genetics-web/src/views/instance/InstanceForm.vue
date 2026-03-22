
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

          <!-- 动态表单改为预览模式（信息汇总） -->
          <n-card title="单据数据" :bordered="false">
            <template #header-extra>
              <n-tag type="info" size="small">全流程汇总</n-tag>
            </template>
            <n-empty v-if="!Object.keys(formData).length" description="暂无填写数据" />
            <n-descriptions v-else label-placement="left" :column="2" bordered>
              <n-descriptions-item v-for="(val, key) in formData" :key="key" :label="key">
                {{ val }}
              </n-descriptions-item>
            </n-descriptions>
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
import { ref, reactive, computed, onMounted, h } from 'vue'
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
  submitInstance, 
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
    detail.value = res.data
    formData.value = res.data.formData || {}
    metaForm.orderStatusId = res.data.orderStatusId
    metaForm.serviceStartTime = res.data.serviceStartTime ? new Date(res.data.serviceStartTime).getTime() : null
    metaForm.serviceEndTime = res.data.serviceEndTime ? new Date(res.data.serviceEndTime).getTime() : null
    
    if (res.data.status !== 0) {
      await loadAvailableActions()
    }
  } finally {
    loading.value = false
  }
}

async function loadAvailableActions() {
  try {
    const res = await getAvailableActions(instanceId)
    availableActions.value = res.data || []
  } catch {
    availableActions.value = []
  }
}

async function handleWorkflowAction(action) {
  currentAction.value = action
  actionRemark.value = ''
  actionFormData.value = {}
  
  if (action.formSchema) {
    // 如果有配置表单，打开弹窗
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
        await doTransition(action.action, remark.value)
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
        await doTransition(action.action, '')
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
  try {
    // 1. 表单校验
    await dynamicFormRef.value?.validate()
    
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
          await submitInstance(instanceId)
          
          message.success('提交成功')
          goBack() // 返回列表并触发刷新
        } catch (err) {
          message.error('操作失败: ' + (err.message || '未知错误'))
        } finally {
          submitting.value = false
        }
      }
    })
  } catch (errors) {
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
