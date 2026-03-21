<template>
  <div class="form-page">
    <!-- 顶部信息栏 -->
    <n-card v-if="detail" size="small">
      <div class="info-bar-content">
        <div class="info-items">
          <span class="info-item"><b>服务单：</b>{{ detail.templateName }}</span>
          <span class="info-item"><b>版本：</b>{{ detail.version }}</span>
          <span class="info-item"><b>国家：</b>{{ detail.countryCode }}</span>
          <span class="info-item"><b>服务类型：</b>{{ detail.serviceCodeL3 }}</span>
          <n-tag :type="orderTagType" size="small">{{ detail.orderStatusName }}</n-tag>
        </div>
        <div class="form-actions" v-if="detail.status === 0">
          <n-button @click="goBack">返回</n-button>
          <n-button :loading="saving" @click="handleSave">保存草稿</n-button>
          <n-button type="primary" :loading="submitting" @click="confirmSubmit">提交服务单</n-button>
        </div>
        <div class="form-actions" v-else>
          <n-button @click="goBack">返回</n-button>
        </div>
      </div>
    </n-card>

    <!-- 服务单附加信息卡 -->
    <n-card v-if="detail" title="服务信息" size="small">
      <n-form :model="metaForm" label-width="100px" label-placement="left">
        <n-grid :cols="3" :x-gap="24">
          <n-gi>
            <n-form-item label="业务状态">
              <n-select
                v-model:value="metaForm.orderStatusId"
                :options="orderStatusOptions"
                placeholder="请选择业务状态"
                :disabled="detail.status !== 0"
              />
            </n-form-item>
          </n-gi>
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

    <!-- 动态表单 -->
    <n-card v-if="detail" size="small">
      <dynamic-form
        v-if="detail.jsonSchema"
        ref="dynamicFormRef"
        :schema="detail.jsonSchema"
        :control-details="detail.controlDetails"
        v-model="formData"
        :readonly="detail.status !== 0"
      />
      <n-empty v-else description="该模板暂无表单配置" />
    </n-card>

    <!-- 提交结果展示 -->
    <n-card v-if="submitResult" title="提交结果（已转换实体对象）" size="small">
      <n-code :code="JSON.stringify(submitResult, null, 2)" language="json" />
    </n-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
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
  useMessage,
  useDialog
} from 'naive-ui'
import DynamicForm from '@/components/DynamicForm/DynamicForm.vue'
import { getInstance, saveInstance, submitInstance, getOrderStatusOptions } from '@/api/formInstance'

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

// 服务信息表单
const metaForm = reactive({
  orderStatusId: null,
  serviceStartTime: null,
  serviceEndTime: null
})

const orderTagType = computed(() => {
  const tagType = orderStatusMap.value[metaForm.orderStatusId]?.tagType || 'info'
  const mapping = {
    'success': 'success',
    'warning': 'warning',
    'danger': 'error',
    'info': 'default'
  }
  return mapping[tagType] || 'default'
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
  } finally {
    loading.value = false
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
    message.success('草稿保存成功')
    await fetchDetail()
  } finally {
    saving.value = false
  }
}

function confirmSubmit() {
  dialog.warning({
    title: '提交确认',
    content: '提交后不可再修改，确认提交？',
    positiveText: '确认提交',
    negativeText: '取消',
    onPositiveClick: handleSubmit
  })
}

async function handleSubmit() {
  try {
    await dynamicFormRef.value?.validate()
  } catch {
    message.warning('请完整填写必填项')
    return
  }
  submitting.value = true
  try {
    await saveInstance(instanceId, {
      formData: formData.value,
      orderStatusId: metaForm.orderStatusId,
      serviceStartTime: metaForm.serviceStartTime ? new Date(metaForm.serviceStartTime).toISOString() : null,
      serviceEndTime: metaForm.serviceEndTime ? new Date(metaForm.serviceEndTime).toISOString() : null
    })
    const res = await submitInstance(instanceId)
    message.success('提交成功！')
    submitResult.value = res.data
    await fetchDetail()
  } finally {
    submitting.value = false
  }
}

function goBack() {
  router.push('/instance')
}

onMounted(async () => {
  await loadOrderStatusOptions()
  await fetchDetail()
})
</script>

<style scoped>
.form-page { display: flex; flex-direction: column; gap: 16px; }

.info-bar-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.info-items { display: flex; align-items: center; gap: 16px; flex-wrap: wrap; }
.info-item { font-size: 14px; color: #666; }
.form-actions { display: flex; gap: 8px; flex-shrink: 0; }
</style>
