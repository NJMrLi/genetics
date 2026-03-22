<template>
  <div class="workflow-page">
    <!-- 顶部标题栏 -->
    <n-card size="small" class="header-card">
      <div class="header-content">
        <div class="template-info">
          <span class="info-label">模板：</span>
          <span class="info-value">{{ templateInfo.templateName || '未命名' }}</span>
          <n-divider vertical />
          <span class="info-label">版本：</span>
          <span class="info-value">{{ templateInfo.version || '1.0.0' }}</span>
        </div>
        <div class="form-actions">
          <n-button @click="goBack">返回</n-button>
          <n-button type="primary" :loading="saving" @click="handleSave">保存</n-button>
        </div>
      </div>
    </n-card>

    <!-- 工作流设计器：等数据加载完再挂载，保证 onMounted 时 modelValue 已有值 -->
    <div class="designer-body">
      <workflow-config v-if="loaded" v-model="workflowConfig" />
      <div v-else class="loading-placeholder">
        <n-spin size="large" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NCard,
  NButton,
  NDivider,
  NSpin,
  useMessage
} from 'naive-ui'
import WorkflowConfig from '@/components/FormDesigner/WorkflowConfig.vue'
import { getTemplate, updateTemplate } from '@/api/formTemplate'

const route = useRoute()
const router = useRouter()
const message = useMessage()
const templateId = route.params.id

const saving = ref(false)
const templateInfo = ref({})
const workflowConfig = ref(null)
const loaded = ref(false)  // 数据加载完成后再挂载画布

async function loadTemplate() {
  if (!templateId) return
  const res = await getTemplate(templateId)
  templateInfo.value = res.data
  workflowConfig.value = res.data.workflowConfig || null
  loaded.value = true
}

async function handleSave() {
  saving.value = true
  try {
    await updateTemplate(templateId, {
      templateName: templateInfo.value.templateName,
      version: templateInfo.value.version,
      countryCode: templateInfo.value.countryCode,
      serviceCodeL1: templateInfo.value.serviceCodeL1,
      serviceCodeL2: templateInfo.value.serviceCodeL2,
      serviceCodeL3: templateInfo.value.serviceCodeL3,
      remark: templateInfo.value.remark,
      jsonSchema: templateInfo.value.jsonSchema,
      workflowConfig: workflowConfig.value
    })
    message.success('保存成功')
  } finally {
    saving.value = false
  }
}

function goBack() {
  router.push('/template')
}

onMounted(() => {
  loadTemplate()
})
</script>

<style scoped>
.workflow-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 100px);
  gap: 12px;
}

.header-card :deep(.n-card__content) {
  padding: 12px 16px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.template-info {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 14px;
}

.info-label {
  color: #666;
}

.info-value {
  color: #333;
  font-weight: 500;
}

.form-actions {
  display: flex;
  gap: 8px;
}

.designer-body {
  flex: 1;
  min-height: 0;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e0e0e6;
  background: #fff;
}

.loading-placeholder {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
