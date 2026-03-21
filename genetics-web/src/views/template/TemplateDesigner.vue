<template>
  <div class="designer-page">
    <!-- 顶部基本信息配置 -->
    <n-card size="small" class="info-card">
      <div class="info-form">
        <div class="form-row">
          <div class="form-item">
            <label class="form-label">模板名称 <span class="required">*</span></label>
            <n-input v-model:value="store.templateInfo.templateName" placeholder="模板名称" style="width: 180px" />
          </div>
          <div class="form-item">
            <label class="form-label">版本</label>
            <n-input v-model:value="store.templateInfo.version" placeholder="1.0.0" style="width: 80px" />
          </div>
          <div class="form-item">
            <label class="form-label">国家</label>
            <n-select
              v-model:value="store.templateInfo.countryCode"
              :options="countryOptions"
              placeholder="国家"
              style="width: 140px"
              clearable
            />
          </div>
          <div class="form-item">
            <label class="form-label">一级服务</label>
            <n-select
              v-model:value="store.templateInfo.serviceCodeL1"
              :options="l1Options"
              placeholder="一级"
              style="width: 120px"
              clearable
              @update:value="onL1Change"
            />
          </div>
          <div class="form-item">
            <label class="form-label">二级服务</label>
            <n-select
              v-model:value="store.templateInfo.serviceCodeL2"
              :options="l2Options"
              placeholder="二级"
              style="width: 120px"
              clearable
              :disabled="!store.templateInfo.serviceCodeL1"
              @update:value="onL2Change"
            />
          </div>
          <div class="form-item">
            <label class="form-label">三级服务</label>
            <n-select
              v-model:value="store.templateInfo.serviceCodeL3"
              :options="l3Options"
              placeholder="三级"
              style="width: 140px"
              clearable
              :disabled="!store.templateInfo.serviceCodeL2"
            />
          </div>
        </div>
        <div class="form-actions">
          <n-button type="primary" :loading="saving" @click="handleSave">保存草稿</n-button>
          <n-button @click="goBack">返回</n-button>
        </div>
      </div>
    </n-card>

    <!-- 设计器主体 -->
    <div class="designer-body">
      <!-- 左侧控件面板 -->
      <div class="left-panel">
        <control-panel />
      </div>
      <!-- 右侧画板 -->
      <div class="right-panel">
        <canvas-panel />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NCard,
  NInput,
  NSelect,
  NButton,
  useMessage
} from 'naive-ui'
import { useFormDesignerStore } from '@/stores/formDesigner'
import ControlPanel from '@/components/FormDesigner/ControlPanel.vue'
import CanvasPanel from '@/components/FormDesigner/Canvas.vue'
import { getTemplate, createTemplate, updateTemplate } from '@/api/formTemplate'
import { getCountries } from '@/api/basic'

// 服务类目数据
const SERVICE_CATEGORIES = [
  { id: 1, parentId: 0, name: 'VAT服务', code: '01' },
  { id: 2, parentId: 0, name: 'EPR服务', code: '02' },
  { id: 3, parentId: 1, name: 'VAT服务', code: '0101' },
  { id: 5, parentId: 2, name: '包装法', code: '0201' },
  { id: 6, parentId: 2, name: 'WEEE法', code: '0202' },
  { id: 7, parentId: 2, name: '电池法', code: '0203' },
  { id: 8, parentId: 2, name: '家具法', code: '0204' },
  { id: 9, parentId: 2, name: '轮胎法', code: '0205' },
  { id: 10, parentId: 2, name: '印刷纸法', code: '0206' },
  { id: 11, parentId: 2, name: '纺织品法', code: '0207' },
  { id: 12, parentId: 2, name: '化学用品法', code: '0208' },
  { id: 13, parentId: 2, name: '刺穿医疗设备法', code: '0209' },
  { id: 14, parentId: 3, name: 'VAT新注册申报', code: '010101' },
  { id: 15, parentId: 3, name: 'VAT转代理申报', code: '010102' },
  { id: 16, parentId: 3, name: 'VAT申报续费', code: '010103' },
  { id: 17, parentId: 3, name: 'VAT注销', code: '010104' },
  { id: 18, parentId: 3, name: 'VAT补申报', code: '010105' },
  { id: 20, parentId: 5, name: '包装法新注册申报', code: '020101' },
  { id: 21, parentId: 5, name: '包装法转代理申报', code: '020102' },
  { id: 22, parentId: 5, name: '包装法申报续费', code: '020103' },
  { id: 23, parentId: 5, name: '包装法注销', code: '020104' }
]

const route = useRoute()
const router = useRouter()
const message = useMessage()
const store = useFormDesignerStore()
const templateId = route.params.id

const countries = ref([])
const saving = ref(false)
const l2List = ref([])
const l3List = ref([])

// 计算选项
const countryOptions = computed(() =>
  countries.value.map(c => ({
    label: `${c.code} ${c.nameCn}`,
    value: c.code
  }))
)

const l1Options = computed(() =>
  SERVICE_CATEGORIES
    .filter(s => s.parentId === 0)
    .map(s => ({ label: s.name, value: s.code }))
)

const l2Options = computed(() =>
  l2List.value.map(s => ({ label: s.name, value: s.code }))
)

const l3Options = computed(() =>
  l3List.value.map(s => ({ label: s.name, value: s.code }))
)

function onL1Change(code) {
  store.templateInfo.serviceCodeL2 = ''
  store.templateInfo.serviceCodeL3 = ''
  l2List.value = []
  l3List.value = []
  
  if (code) {
    const parent = SERVICE_CATEGORIES.find(s => s.code === code && s.parentId === 0)
    if (parent) {
      l2List.value = SERVICE_CATEGORIES.filter(s => s.parentId === parent.id)
    }
  }
}

function onL2Change(code) {
  store.templateInfo.serviceCodeL3 = ''
  l3List.value = []
  
  if (code) {
    const parent = SERVICE_CATEGORIES.find(s => s.code === code)
    if (parent) {
      l3List.value = SERVICE_CATEGORIES.filter(s => s.parentId === parent.id)
    }
  }
}

async function handleSave() {
  if (!store.templateInfo.templateName) {
    message.warning('请输入模板名称')
    return
  }
  if (!store.templateInfo.countryCode) {
    message.warning('请选择国家')
    return
  }
  if (!store.templateInfo.serviceCodeL1 || !store.templateInfo.serviceCodeL2 || !store.templateInfo.serviceCodeL3) {
    message.warning('请选择完整的服务类目')
    return
  }

  saving.value = true
  try {
    const payload = {
      ...store.templateInfo,
      jsonSchema: store.jsonSchema
    }
    if (templateId) {
      await updateTemplate(templateId, payload)
    } else {
      const res = await createTemplate(payload)
      router.replace(`/template/designer/${res.data.id}`)
    }
    message.success('保存成功')
  } finally {
    saving.value = false
  }
}

function goBack() {
  router.push('/template')
}

onMounted(async () => {
  const countriesRes = await getCountries()
  countries.value = countriesRes.data

  if (templateId) {
    const res = await getTemplate(templateId)
    store.loadTemplate(res.data)
    // 恢复联动选项
    if (store.templateInfo.serviceCodeL1) {
      onL1Change(store.templateInfo.serviceCodeL1)
    }
    if (store.templateInfo.serviceCodeL2) {
      onL2Change(store.templateInfo.serviceCodeL2)
    }
  }
})

onUnmounted(() => {
  store.reset()
})
</script>

<style scoped>
.designer-page {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 100px);
  gap: 12px;
}

.info-card :deep(.n-card__content) {
  padding: 12px 16px;
}

.info-form {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-end;
  gap: 16px;
}

.form-row {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  flex: 1;
}

.form-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.form-label {
  font-size: 12px;
  color: #666;
  white-space: nowrap;
}

.required {
  color: #d03050;
}

.form-actions {
  display: flex;
  gap: 8px;
  margin-left: auto;
}

.designer-body {
  flex: 1;
  display: flex;
  min-height: 0;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid #e0e0e6;
  background: #fff;
}

.left-panel {
  width: 280px;
  flex-shrink: 0;
  border-right: 1px solid #e0e0e6;
}

.right-panel {
  flex: 1;
  overflow: hidden;
}
</style>
