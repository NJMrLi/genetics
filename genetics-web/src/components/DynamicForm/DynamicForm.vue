<template>
  <n-form v-if="parsedSchema?.rows" ref="formRef" :model="formData" label-placement="top" class="dynamic-form">
    <div
      v-for="row in parsedSchema.rows"
      :key="row.rowIndex"
      class="form-row"
      :style="getRowStyle(row)"
    >
      <div
        v-for="cell in row.cells"
        :key="cell.controlId"
        :style="{ gridColumn: `span ${cell.colSpan}` }"
      >
        <n-form-item
          :label="cell.label"
          :path="cell.controlKey"
          :rule="buildRules(cell.controlId)"
        >
          <control-renderer
            :cell="cell"
            :control="controlMap[cell.controlId]"
            :model-value="formData[cell.controlKey]"
            :disabled="readonly"
            @update:model-value="val => formData[cell.controlKey] = val"
          />
          <!-- Tips 提示 -->
          <template #feedback v-if="controlMap[cell.controlId]?.tips">
            <span class="control-tips">
              {{ controlMap[cell.controlId].tips }}
            </span>
          </template>
        </n-form-item>
      </div>
    </div>
  </n-form>
  <n-empty v-else description="未配置表单内容" />
</template>

<script setup>
import { computed, ref } from 'vue'
import { NForm, NFormItem } from 'naive-ui'
import ControlRenderer from './ControlRenderer.vue'

const props = defineProps({
  schema: {
    type: [Object, String],
    required: true
  },
  controlDetails: {
    type: Array,
    default: () => []
  },
  modelValue: {
    type: Object,
    default: () => ({})
  },
  readonly: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])

const formRef = ref(null)

// 兼容 schema 是字符串的情况
const parsedSchema = computed(() => {
  if (typeof props.schema === 'string') {
    try {
      let s = JSON.parse(props.schema)
      if (typeof s === 'string') s = JSON.parse(s)
      return s
    } catch (e) {
      console.error('DynamicForm: schema 解析失败', e)
      return null
    }
  }
  return props.schema
})

// controlId -> controlDetail 映射
const controlMap = computed(() => {
  const map = {}
  props.controlDetails.forEach(c => {
    map[c.controlId] = c
  })
  return map
})

// 双向绑定表单数据
const formData = computed({
  get: () => props.modelValue,
  set: val => emit('update:modelValue', val)
})

// 获取行样式
function getRowStyle(row) {
  return {
    display: 'grid',
    gridTemplateColumns: `repeat(${row.columns || parsedSchema.value?.columns || 2}, 1fr)`,
    gap: '0 16px'
  }
}

// 根据控件配置构建校验规则
function buildRules(controlId) {
  const control = controlMap.value[controlId]
  if (!control) return []
  
  const rules = []
  
  if (control.required) {
    rules.push({
      required: true,
      message: `${control.controlName || '该字段'}不能为空`,
      trigger: ['blur', 'change']
    })
  }
  
  if (control.minLength) {
    rules.push({
      min: control.minLength,
      message: `最少${control.minLength}个字符`,
      trigger: 'blur'
    })
  }
  
  if (control.maxLength) {
    rules.push({
      max: control.maxLength,
      message: `最多${control.maxLength}个字符`,
      trigger: 'blur'
    })
  }
  
  if (control.regexPattern) {
    rules.push({
      pattern: new RegExp(control.regexPattern),
      message: control.regexMessage || '格式不正确',
      trigger: 'blur'
    })
  }
  
  return rules
}

// 暴露表单验证方法
async function validate() {
  return formRef.value?.validate()
}

defineExpose({ validate })
</script>

<style scoped>
.dynamic-form { width: 100%; }
.form-row { margin-bottom: 8px; }
.control-tips {
  font-size: 12px;
  color: #999;
}
</style>
