<template>
  <div class="form-page" v-loading="loading">
    <!-- 顶部信息栏 -->
    <el-card class="info-bar" v-if="detail">
      <div class="info-bar-content">
        <div class="info-items">
          <span class="info-item"><b>服务单：</b>{{ detail.templateName }}</span>
          <span class="info-item"><b>版本：</b>{{ detail.version }}</span>
          <span class="info-item"><b>国家：</b>{{ detail.countryCode }}</span>
          <span class="info-item"><b>服务类型：</b>{{ detail.serviceCodeL3 }}</span>
          <el-tag :type="orderTagType" size="small">{{ detail.orderStatusName }}</el-tag>
        </div>
        <div class="form-actions" v-if="detail.status === 0">
          <el-button @click="goBack">返回</el-button>
          <el-button :loading="saving" @click="handleSave">保存草稿</el-button>
          <el-popconfirm title="提交后不可再修改，确认提交？" @confirm="handleSubmit">
            <template #reference>
              <el-button type="primary" :loading="submitting">提交服务单</el-button>
            </template>
          </el-popconfirm>
        </div>
        <div class="form-actions" v-else>
          <el-button @click="goBack">返回</el-button>
        </div>
      </div>
    </el-card>

    <!-- 服务单附加信息卡（状态、时间） -->
    <el-card class="meta-card" v-if="detail">
      <template #header><span>服务信息</span></template>
      <el-form :model="metaForm" label-width="100px" size="small">
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="业务状态">
              <el-select
                v-model="metaForm.orderStatusId"
                placeholder="请选择业务状态"
                style="width:100%"
                :disabled="detail.status !== 0"
              >
                <el-option
                  v-for="s in orderStatusOptions"
                  :key="s.code"
                  :value="s.code"
                  :label="s.name"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="服务开始时间">
              <el-date-picker
                v-model="metaForm.serviceStartTime"
                type="datetime"
                placeholder="选择开始时间"
                value-format="YYYY-MM-DDTHH:mm:ss"
                style="width:100%"
                :disabled="detail.status !== 0"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="服务结束时间">
              <el-date-picker
                v-model="metaForm.serviceEndTime"
                type="datetime"
                placeholder="选择结束时间"
                value-format="YYYY-MM-DDTHH:mm:ss"
                style="width:100%"
                :disabled="detail.status !== 0"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <!-- 动态表单 -->
    <el-card class="form-card" v-if="detail">
      <dynamic-form
        v-if="detail.jsonSchema"
        ref="dynamicFormRef"
        :schema="detail.jsonSchema"
        :control-details="detail.controlDetails"
        v-model="formData"
        :readonly="detail.status !== 0"
      />
      <el-empty v-else description="该模板暂无表单配置" />
    </el-card>

    <!-- 提交结果展示（仅提交后） -->
    <el-card v-if="submitResult" class="result-card">
      <template #header>
        <span>提交结果（已转换实体对象）</span>
      </template>
      <pre class="result-json">{{ JSON.stringify(submitResult, null, 2) }}</pre>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import DynamicForm from '@/components/DynamicForm/DynamicForm.vue'
import { getInstance, saveInstance, submitInstance, getOrderStatusOptions } from '@/api/formInstance'

const route = useRoute()
const router = useRouter()
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

// 服务信息表单（业务状态、服务时间）
const metaForm = reactive({
  orderStatusId: null,
  serviceStartTime: null,
  serviceEndTime: null
})

const orderTagType = computed(() => {
  return orderStatusMap.value[metaForm.orderStatusId]?.tagType || 'info'
})

async function loadOrderStatusOptions() {
  const res = await getOrderStatusOptions()
  orderStatusOptions.value = res.data
  orderStatusMap.value = Object.fromEntries(res.data.map(s => [s.code, s]))
}

async function fetchDetail() {
  loading.value = true
  try {
    const res = await getInstance(instanceId)
    detail.value = res.data
    formData.value = res.data.formData || {}
    // 同步服务信息
    metaForm.orderStatusId = res.data.orderStatusId
    metaForm.serviceStartTime = res.data.serviceStartTime
    metaForm.serviceEndTime = res.data.serviceEndTime
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
      serviceStartTime: metaForm.serviceStartTime,
      serviceEndTime: metaForm.serviceEndTime
    })
    ElMessage.success('草稿保存成功')
    await fetchDetail()
  } finally {
    saving.value = false
  }
}

async function handleSubmit() {
  // 先执行表单校验
  try {
    await dynamicFormRef.value?.validate()
  } catch {
    ElMessage.warning('请完整填写必填项')
    return
  }
  // 先保存最新数据再提交
  submitting.value = true
  try {
    await saveInstance(instanceId, {
      formData: formData.value,
      orderStatusId: metaForm.orderStatusId,
      serviceStartTime: metaForm.serviceStartTime,
      serviceEndTime: metaForm.serviceEndTime
    })
    const res = await submitInstance(instanceId)
    ElMessage.success('提交成功！')
    submitResult.value = res.data
    // 刷新详情更新状态
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
.info-bar :deep(.el-card__body) { padding: 12px 16px; }
.info-bar-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}
.info-items { display: flex; align-items: center; gap: 16px; flex-wrap: wrap; }
.info-item { font-size: 14px; color: #606266; }
.form-actions { display: flex; gap: 8px; flex-shrink: 0; }
.meta-card :deep(.el-card__body) { padding: 16px 24px 4px; }
.meta-card :deep(.el-card__header) { padding: 10px 24px; font-size: 14px; font-weight: 500; color: #303133; }
.form-card :deep(.el-card__body) { padding: 24px; }
.result-card :deep(.el-card__body) { padding: 16px; }
.result-json {
  background: #f5f7fa;
  padding: 16px;
  border-radius: 6px;
  font-size: 13px;
  color: #303133;
  overflow: auto;
  max-height: 400px;
}
</style>
