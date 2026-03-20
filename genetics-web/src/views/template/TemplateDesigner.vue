<template>
  <div class="designer-page">
    <!-- 顶部基本信息配置 -->
    <el-card class="info-card">
      <el-form :model="store.templateInfo" :rules="infoRules" ref="infoFormRef" inline label-width="90px">
        <el-form-item label="模板名称" prop="templateName">
          <el-input v-model="store.templateInfo.templateName" placeholder="模板名称" style="width:200px" />
        </el-form-item>
        <el-form-item label="版本" prop="version">
          <el-input v-model="store.templateInfo.version" placeholder="1.0.0" style="width:100px" />
        </el-form-item>
        <el-form-item label="国家" prop="countryCode">
          <el-select v-model="store.templateInfo.countryCode" placeholder="国家" style="width:140px" clearable>
            <el-option v-for="c in countries" :key="c.code" :value="c.code" :label="`${c.code} ${c.nameCn}`" />
          </el-select>
        </el-form-item>
        <el-form-item label="一级服务" prop="serviceCodeL1">
          <el-select v-model="store.templateInfo.serviceCodeL1" placeholder="一级" style="width:130px" clearable @change="onL1Change">
            <el-option v-for="s in l1List" :key="s.id" :value="s.code" :label="s.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="二级服务" prop="serviceCodeL2">
          <el-select v-model="store.templateInfo.serviceCodeL2" placeholder="二级" style="width:150px" clearable @change="onL2Change">
            <el-option v-for="s in l2List" :key="s.id" :value="s.code" :label="s.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="三级服务" prop="serviceCodeL3">
          <el-select v-model="store.templateInfo.serviceCodeL3" placeholder="三级" style="width:170px" clearable>
            <el-option v-for="s in l3List" :key="s.id" :value="s.code" :label="s.name" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="saving" @click="handleSave">保存草稿</el-button>
          <el-button @click="goBack">返回</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 设计器主体 -->
    <div class="designer-body">
      <!-- 左侧控件面板 -->
      <div class="left-panel">
        <control-panel :controls="allControls" />
      </div>
      <!-- 右侧画板 -->
      <div class="right-panel">
        <canvas-panel />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useFormDesignerStore } from '@/stores/formDesigner'
import ControlPanel from '@/components/FormDesigner/ControlPanel.vue'
import CanvasPanel from '@/components/FormDesigner/Canvas.vue'
import { listAllControls } from '@/api/formControl'
import { getTemplate, createTemplate, updateTemplate } from '@/api/formTemplate'
import { getCountries } from '@/api/basic'

// 服务类目数据（内嵌，实际可改为调用后端接口）
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
const store = useFormDesignerStore()
const templateId = route.params.id

const allControls = ref([])
const countries = ref([])
const l1List = ref(SERVICE_CATEGORIES.filter(s => s.parentId === 0))
const l2List = ref([])
const l3List = ref([])
const saving = ref(false)
const infoFormRef = ref(null)

const infoRules = {
  templateName: [{ required: true, message: '请输入模板名称', trigger: 'blur' }],
  countryCode: [{ required: true, message: '请选择国家', trigger: 'change' }],
  serviceCodeL1: [{ required: true, message: '请选择一级服务', trigger: 'change' }],
  serviceCodeL2: [{ required: true, message: '请选择二级服务', trigger: 'change' }],
  serviceCodeL3: [{ required: true, message: '请选择三级服务', trigger: 'change' }]
}

function onL1Change(code) {
  store.templateInfo.serviceCodeL2 = ''
  store.templateInfo.serviceCodeL3 = ''
  l2List.value = []
  l3List.value = []
  const parent = SERVICE_CATEGORIES.find(s => s.code === code && s.parentId === 0)
  if (parent) {
    l2List.value = SERVICE_CATEGORIES.filter(s => s.parentId === parent.id)
  }
}

function onL2Change(code) {
  store.templateInfo.serviceCodeL3 = ''
  l3List.value = []
  const parent = SERVICE_CATEGORIES.find(s => s.code === code)
  if (parent) {
    l3List.value = SERVICE_CATEGORIES.filter(s => s.parentId === parent.id)
  }
}

async function handleSave() {
  await infoFormRef.value?.validate()
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
    ElMessage.success('保存成功')
  } finally {
    saving.value = false
  }
}

function goBack() {
  router.push('/template')
}

onMounted(async () => {
  const [controlsRes, countriesRes] = await Promise.all([
    listAllControls(),
    getCountries()
  ])
  allControls.value = controlsRes.data
  countries.value = countriesRes.data

  if (templateId) {
    const res = await getTemplate(templateId)
    store.loadTemplate(res.data)
    // 恢复联动选项
    if (store.templateInfo.serviceCodeL1) onL1Change(store.templateInfo.serviceCodeL1)
    if (store.templateInfo.serviceCodeL2) onL2Change(store.templateInfo.serviceCodeL2)
  }
})

onUnmounted(() => {
  store.reset()
})
</script>

<style scoped>
.designer-page { display: flex; flex-direction: column; height: calc(100vh - 100px); gap: 12px; }
.info-card :deep(.el-card__body) { padding: 12px 16px; }
.designer-body { flex: 1; display: flex; min-height: 0; border-radius: 8px; overflow: hidden; border: 1px solid #e4e7ed; background: #fff; }
.left-panel { width: 260px; flex-shrink: 0; }
.right-panel { flex: 1; overflow: hidden; }
</style>
