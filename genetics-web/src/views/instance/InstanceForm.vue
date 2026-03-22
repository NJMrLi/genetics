
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

    <n-grid :cols="24" :x-gap="24" :y-gap="24">
      <!-- 左侧：动态表单 -->
      <n-gi :span="16">
        <n-card title="表单详情" :bordered="false" class="form-card">
          <template #header-extra>
            <n-text depth="3">版本: {{ detail?.version }}</n-text>
          </template>
          <dynamic-form
            v-if="detail?.jsonSchema"
            ref="dynamicFormRef"
            :schema="detail.jsonSchema"
            :control-details="detail.controlDetails"
            v-model="formData"
            :readonly="detail.orderStatusId !== 10 && detail.orderStatusId !== 50"
          />
          <n-empty v-else-if="detail" description="该模板暂无表单配置" />
        </n-card>
      </n-gi>

      <!-- 右侧：侧边栏信息 -->
      <n-gi :span="8">
        <n-space vertical :size="24">
          <n-card title="基础信息" :bordered="false">
            <n-descriptions label-placement="left" :column="1" bordered label-style="width: 100px">
              <n-descriptions-item label="国家">
                <n-tag type="info" :bordered="false" size="small">{{ detail?.countryCode }}</n-tag>
              </n-descriptions-item>
              <n-descriptions-item label="服务类型">
                {{ detail?.serviceCodeL3 }}
              </n-descriptions-item>
              <n-descriptions-item label="创建时间">
                {{ detail?.createTime }}
              </n-descriptions-item>
            </n-descriptions>
          </n-card>

          <!-- 服务单附加信息卡 -->
          <n-card v-if="detail" title="服务周期" :bordered="false">
            <n-form :model="metaForm" label-placement="top">
              <n-grid :cols="1" :y-gap="12">
                <n-gi>
                  <n-form-item label="服务开始时间">
                    <n-date-picker
                      v-model:value="metaForm.serviceStartTime"
                      type="datetime"
                      placeholder="选择开始时间"
                      :disabled="detail.status !== 0"
                      style="width: 100%"
                    />
                  </n-form-item>
                </n-gi>
                <n-gi>
                  <n-form-item label="服务结束时间">
                    <n-date-picker
                      v-model:value="metaForm.serviceEndTime"
                      type="datetime"
                      placeholder="选择结束时间"
                      :disabled="detail.status !== 0"
                      style="width: 100%"
                    />
                  </n-form-item>
                </n-gi>
              </n-grid>
            </n-form>
          </n-card>

          <n-card v-if="submitResult" title="转换结果 (Debug)" :bordered="false">
            <n-scrollbar style="max-height: 300px">
              <n-code :code="JSON.stringify(submitResult, null, 2)" language="json" word-wrap />
            </n-scrollbar>
          </n-card>
        </n-space>
      </n-gi>
    </n-grid>
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

function handleWorkflowAction(action) {
  const remark = ref('')
  if (action.needRemark) {
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

async function doTransition(action, remark) {
  try {
    await executeTransition(instanceId, action, remark)
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

onMounted(() => {
  loadOrderStatusOptions()
  fetchDetail()
})
</script>

<style scoped>
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

.form-card {
  height: 100%;
}

:deep(.n-descriptions-table-header) {
  width: 100px;
}
</style>
