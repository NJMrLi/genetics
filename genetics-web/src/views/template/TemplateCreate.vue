<template>
  <div class="create-page">
    <n-card title="新建模板 - 基础信息配置">
      <n-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-placement="left"
        label-width="100"
        require-mark-placement="right-hanging"
      >
        <n-form-item label="模板名称" path="templateName">
          <n-input v-model:value="form.templateName" placeholder="请输入模板名称" style="width: 300px" />
        </n-form-item>

        <n-form-item label="版本" path="version">
          <n-input v-model:value="form.version" placeholder="1.0.0" style="width: 120px" />
        </n-form-item>

        <n-form-item label="国家" path="countryCode">
          <n-select
            v-model:value="form.countryCode"
            :options="countryOptions"
            placeholder="请选择国家"
            style="width: 200px"
            clearable
          />
        </n-form-item>

        <n-form-item label="服务类目" required>
          <n-space>
            <n-select
              v-model:value="form.serviceCodeL1"
              :options="l1Options"
              placeholder="一级服务"
              style="width: 140px"
              clearable
              @update:value="onL1Change"
            />
            <n-select
              v-model:value="form.serviceCodeL2"
              :options="l2Options"
              placeholder="二级服务"
              style="width: 140px"
              clearable
              :disabled="!form.serviceCodeL1"
              @update:value="onL2Change"
            />
            <n-select
              v-model:value="form.serviceCodeL3"
              :options="l3Options"
              placeholder="三级服务"
              style="width: 160px"
              clearable
              :disabled="!form.serviceCodeL2"
            />
          </n-space>
        </n-form-item>

        <n-form-item label="描述" path="remark">
          <n-input
            v-model:value="form.remark"
            type="textarea"
            placeholder="请输入模板描述"
            style="width: 500px"
            :rows="3"
          />
        </n-form-item>
      </n-form>

      <div class="form-actions">
        <n-button @click="goBack">取消</n-button>
        <n-button type="primary" :loading="saving" @click="handleSave">保存并继续</n-button>
      </div>
    </n-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  NCard,
  NForm,
  NFormItem,
  NInput,
  NSelect,
  NSpace,
  NButton,
  useMessage
} from 'naive-ui'
import { createTemplate } from '@/api/formTemplate'
import { getCountries } from '@/api/basic'

const router = useRouter()
const message = useMessage()
const formRef = ref(null)
const saving = ref(false)
const countries = ref([])

const form = ref({
  templateName: '',
  version: '1.0.0',
  countryCode: null,
  serviceCodeL1: null,
  serviceCodeL2: null,
  serviceCodeL3: null,
  remark: ''
})

const rules = {
  templateName: { required: true, message: '请输入模板名称', trigger: 'blur' },
  countryCode: { required: true, message: '请选择国家', trigger: 'change' }
}

// 服务类目数据
const SERVICE_CATEGORIES = [
  { id: 1, parentId: 0, name: 'VAT', code: '01' },
  { id: 2, parentId: 0, name: 'EPR', code: '02' },
  { id: 3, parentId: 1, name: 'VAT注册申报', code: '0101' },
  { id: 4, parentId: 1, name: 'VAT申报', code: '0102' },
  { id: 5, parentId: 2, name: '包装法', code: '0201' },
  { id: 6, parentId: 2, name: '电池法', code: '0202' },
  { id: 7, parentId: 2, name: 'WEEE', code: '0203' },
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

const countryOptions = computed(() =>
  countries.value.map(c => ({
    label: `${c.nameCn} (${c.code})`,
    value: c.code
  }))
)

const l1Options = computed(() =>
  SERVICE_CATEGORIES.filter(s => s.parentId === 0).map(s => ({
    label: s.name,
    value: s.code
  }))
)

const l2Options = computed(() => {
  const parent = SERVICE_CATEGORIES.find(s => s.code === form.value.serviceCodeL1)
  if (!parent) return []
  return SERVICE_CATEGORIES.filter(s => s.parentId === parent.id).map(s => ({
    label: s.name,
    value: s.code
  }))
})

const l3Options = computed(() => {
  const parent = SERVICE_CATEGORIES.find(s => s.code === form.value.serviceCodeL2)
  if (!parent) return []
  return SERVICE_CATEGORIES.filter(s => s.parentId === parent.id).map(s => ({
    label: s.name,
    value: s.code
  }))
})

function onL1Change() {
  form.value.serviceCodeL2 = null
  form.value.serviceCodeL3 = null
}

function onL2Change() {
  form.value.serviceCodeL3 = null
}

async function handleSave() {
  try {
    await formRef.value?.validate()
  } catch {
    return
  }

  if (!form.value.serviceCodeL1 || !form.value.serviceCodeL2 || !form.value.serviceCodeL3) {
    message.warning('请选择完整的服务类目')
    return
  }

  saving.value = true
  try {
    const res = await createTemplate(form.value)
    message.success('创建成功')
    // 跳转到表单配置页面
    router.push(`/template/form-designer/${res.data.id}`)
  } finally {
    saving.value = false
  }
}

function goBack() {
  router.push('/template')
}

onMounted(async () => {
  const res = await getCountries()
  countries.value = res.data
})
</script>

<style scoped>
.create-page {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
  padding-top: 24px;
  border-top: 1px solid #e0e0e6;
}
</style>
