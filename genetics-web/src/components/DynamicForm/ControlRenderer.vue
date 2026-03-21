<template>
  <!-- INPUT -->
  <n-input
    v-if="controlType === 'INPUT'"
    :value="modelValue"
    :placeholder="control?.placeholder || ''"
    :disabled="disabled"
    @update:value="emit('update:modelValue', $event)"
  />

  <!-- TEXTAREA -->
  <n-input
    v-else-if="controlType === 'TEXTAREA'"
    type="textarea"
    :value="modelValue"
    :placeholder="control?.placeholder || ''"
    :disabled="disabled"
    :rows="3"
    @update:value="emit('update:modelValue', $event)"
  />

  <!-- NUMBER -->
  <n-input-number
    v-else-if="controlType === 'NUMBER'"
    :value="modelValue"
    :disabled="disabled"
    :placeholder="control?.placeholder || ''"
    style="width: 100%"
    @update:value="emit('update:modelValue', $event)"
  />

  <!-- SELECT -->
  <n-select
    v-else-if="controlType === 'SELECT'"
    :value="modelValue"
    :options="selectOptions"
    :placeholder="control?.placeholder || '请选择'"
    :disabled="disabled"
    clearable
    @update:value="emit('update:modelValue', $event)"
  />

  <!-- SWITCH -->
  <n-switch
    v-else-if="controlType === 'SWITCH'"
    :value="modelValue"
    :disabled="disabled"
    @update:value="emit('update:modelValue', $event)"
  />

  <!-- DATE -->
  <n-date-picker
    v-else-if="controlType === 'DATE'"
    :value="dateValue"
    :placeholder="control?.placeholder || '请选择日期'"
    :disabled="disabled"
    type="date"
    style="width: 100%"
    @update:value="handleDateChange"
  />

  <!-- UPLOAD -->
  <div v-else-if="controlType === 'UPLOAD'" class="upload-wrapper">
    <n-upload
      :multiple="true"
      :max="uploadConfig?.maxCount || 3"
      :accept="uploadConfig?.accept || '*'"
      :disabled="disabled"
      :default-file-list="fileList"
      @change="handleFileChange"
    >
      <n-button>点击上传</n-button>
    </n-upload>
    <div class="upload-tip">
      支持格式: {{ uploadConfig?.accept || '不限' }}，
      单文件最大: {{ uploadConfig?.maxSizeMB || 10 }}MB，
      最多{{ uploadConfig?.maxCount || 3 }}个文件
    </div>
  </div>

  <!-- 未知类型 fallback -->
  <n-input
    v-else
    :value="modelValue"
    :placeholder="control?.placeholder || ''"
    :disabled="disabled"
    @update:value="emit('update:modelValue', $event)"
  />
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import {
  NInput,
  NInputNumber,
  NSelect,
  NSwitch,
  NDatePicker,
  NUpload,
  NButton,
  useMessage
} from 'naive-ui'

const props = defineProps({
  cell: { type: Object, required: true },
  control: { type: Object, default: null },
  modelValue: { default: null },
  disabled: { type: Boolean, default: false }
})

const emit = defineEmits(['update:modelValue'])
const message = useMessage()

const controlType = computed(() => props.cell?.controlType || 'INPUT')

// 下拉选项
const selectOptions = computed(() => {
  const opts = props.control?.selectOptions
  if (!opts) return []
  if (Array.isArray(opts)) return opts.map(o => ({ label: o.label, value: o.value }))
  try {
    return JSON.parse(opts).map(o => ({ label: o.label, value: o.value }))
  } catch {
    return []
  }
})

// 上传配置
const uploadConfig = computed(() => {
  const cfg = props.control?.uploadConfig
  if (!cfg) return {}
  if (typeof cfg === 'object') return cfg
  try {
    return JSON.parse(cfg)
  } catch {
    return {}
  }
})

// 日期值转换
const dateValue = computed(() => {
  if (!props.modelValue) return null
  if (typeof props.modelValue === 'number') return props.modelValue
  return new Date(props.modelValue).getTime()
})

function handleDateChange(timestamp) {
  if (!timestamp) {
    emit('update:modelValue', null)
    return
  }
  const date = new Date(timestamp)
  emit('update:modelValue', date.toISOString().split('T')[0])
}

// 文件列表管理
const fileList = ref([])

watch(() => props.modelValue, val => {
  if (Array.isArray(val)) {
    fileList.value = val.map((f, i) => ({
      id: i,
      name: f.fileName,
      url: f.fileUrl,
      status: 'finished'
    }))
  }
}, { immediate: true })

function handleFileChange({ fileList: files }) {
  const maxCount = uploadConfig.value?.maxCount || 3
  if (files.length > maxCount) {
    message.warning(`最多上传 ${maxCount} 个文件`)
    return
  }

  const fileData = files
    .filter(f => f.status === 'finished')
    .map(f => ({
      fileName: f.name,
      fileUrl: f.url || f.file?.url || '',
      fileSize: f.file?.size || 0
    }))
  
  emit('update:modelValue', fileData)
}
</script>

<style scoped>
.upload-wrapper {
  width: 100%;
}

.upload-tip {
  font-size: 12px;
  color: #999;
  margin-top: 8px;
}
</style>
