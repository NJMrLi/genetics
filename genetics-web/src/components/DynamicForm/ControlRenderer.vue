<template>
  <!-- INPUT -->
  <el-input
    v-if="controlType === 'INPUT'"
    :model-value="modelValue"
    :placeholder="control?.placeholder || '请输入'"
    :disabled="disabled"
    @update:model-value="emit('update:modelValue', $event)"
  />

  <!-- TEXTAREA -->
  <el-input
    v-else-if="controlType === 'TEXTAREA'"
    type="textarea"
    :model-value="modelValue"
    :placeholder="control?.placeholder || '请输入'"
    :disabled="disabled"
    :rows="3"
    @update:model-value="emit('update:modelValue', $event)"
  />

  <!-- NUMBER -->
  <el-input-number
    v-else-if="controlType === 'NUMBER'"
    :model-value="modelValue"
    :disabled="disabled"
    style="width: 100%"
    @update:model-value="emit('update:modelValue', $event)"
  />

  <!-- SELECT -->
  <el-select
    v-else-if="controlType === 'SELECT'"
    :model-value="modelValue"
    :placeholder="control?.placeholder || '请选择'"
    :disabled="disabled"
    style="width: 100%"
    clearable
    @update:model-value="emit('update:modelValue', $event)"
  >
    <el-option
      v-for="opt in selectOptions"
      :key="opt.value"
      :label="opt.label"
      :value="opt.value"
    />
  </el-select>

  <!-- SWITCH -->
  <el-switch
    v-else-if="controlType === 'SWITCH'"
    :model-value="modelValue"
    :disabled="disabled"
    @update:model-value="emit('update:modelValue', $event)"
  />

  <!-- DATE -->
  <el-date-picker
    v-else-if="controlType === 'DATE'"
    :model-value="modelValue"
    type="date"
    :placeholder="control?.placeholder || '请选择日期'"
    :disabled="disabled"
    value-format="YYYY-MM-DD"
    style="width: 100%"
    @update:model-value="emit('update:modelValue', $event)"
  />

  <!-- UPLOAD -->
  <div v-else-if="controlType === 'UPLOAD'" class="upload-wrapper">
    <el-upload
      :action="uploadUrl"
      :limit="uploadConfig?.maxCount || 3"
      :accept="uploadConfig?.accept || '*'"
      :file-list="fileList"
      :disabled="disabled"
      list-type="text"
      @success="handleUploadSuccess"
      @remove="handleRemoveFile"
      @exceed="handleExceed"
    >
      <el-button type="primary" :disabled="disabled">
        <el-icon><Upload /></el-icon>
        点击上传
      </el-button>
      <template #tip>
        <div class="el-upload__tip">
          支持格式: {{ uploadConfig?.accept || '不限' }}，
          单文件最大: {{ uploadConfig?.maxSizeMB || 10 }}MB，
          最多{{ uploadConfig?.maxCount || 3 }}个文件
        </div>
      </template>
    </el-upload>
  </div>

  <!-- 未知类型 fallback -->
  <el-input
    v-else
    :model-value="modelValue"
    :placeholder="control?.placeholder || '请输入'"
    :disabled="disabled"
    @update:model-value="emit('update:modelValue', $event)"
  />
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  cell: { type: Object, required: true },
  control: { type: Object, default: null },
  modelValue: { default: null },
  disabled: { type: Boolean, default: false }
})

const emit = defineEmits(['update:modelValue'])

const controlType = computed(() => props.cell?.controlType || 'INPUT')

// 下拉选项
const selectOptions = computed(() => {
  const opts = props.control?.selectOptions
  if (!opts) return []
  if (Array.isArray(opts)) return opts
  try {
    return JSON.parse(opts)
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

// 文件上传 URL（可根据实际配置）
const uploadUrl = '/api/upload'

// 文件列表管理
const fileList = ref([])
watch(() => props.modelValue, val => {
  if (Array.isArray(val)) {
    fileList.value = val.map(f => ({ name: f.fileName, url: f.fileUrl }))
  }
}, { immediate: true })

function handleUploadSuccess(response, file, files) {
  const fileData = files.map(f => ({
    fileName: f.name,
    fileUrl: f.response?.data?.url || f.url,
    fileSize: f.size
  }))
  emit('update:modelValue', fileData)
}

function handleRemoveFile(file, files) {
  const fileData = files.map(f => ({
    fileName: f.name,
    fileUrl: f.url,
    fileSize: f.size
  }))
  emit('update:modelValue', fileData)
}

function handleExceed(files, fileList) {
  ElMessage.warning(`最多上传 ${uploadConfig.value?.maxCount || 3} 个文件`)
}
</script>

<style scoped>
.upload-wrapper { width: 100%; }
</style>
