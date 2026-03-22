<template>
  <n-modal
    v-model:show="show"
    preset="card"
    :title="`配置动作表单 - ${actionName}`"
    style="width: 1000px; height: 80vh"
    class="workflow-form-modal"
    :segmented="{ content: 'soft', footer: 'soft' }"
  >
    <div class="modal-layout">
      <!-- 左侧控件库 -->
      <div class="left-panel">
        <control-panel />
      </div>
      
      <!-- 右侧画板 -->
      <div class="right-panel">
        <canvas-panel />
      </div>
    </div>
    
    <template #footer>
      <n-space justify="end">
        <n-button @click="show = false">取消</n-button>
        <n-button type="primary" @click="handleConfirm">确认配置</n-button>
      </n-space>
    </template>
  </n-modal>
</template>

<script setup>
import { ref, watch, onUnmounted } from 'vue'
import { NModal, NButton, NSpace } from 'naive-ui'
import ControlPanel from '@/components/FormDesigner/ControlPanel.vue'
import CanvasPanel from '@/components/FormDesigner/Canvas.vue'
import { useFormDesignerStore } from '@/stores/formDesigner'

const props = defineProps({
  show: Boolean,
  actionName: String,
  initialSchema: [Object, String]
})

const emit = defineEmits(['update:show', 'confirm'])

const show = ref(props.show)
const store = useFormDesignerStore()

watch(() => props.show, (val) => {
  show.value = val
  if (val) {
    // 初始化设计器数据
    store.reset()
    if (props.initialSchema) {
      let schema = props.initialSchema
      if (typeof schema === 'string') {
        try {
          schema = JSON.parse(schema)
        } catch (e) {
          schema = null
        }
      }
      if (schema) {
        store.columns = schema.columns || 2
        store.rows = schema.rows || []
      }
    }
  }
})

watch(show, (val) => {
  emit('update:show', val)
})

function handleConfirm() {
  const schema = JSON.stringify(store.jsonSchema)
  emit('confirm', schema)
  show.value = false
}

onUnmounted(() => {
  store.reset()
})
</script>

<style scoped>
.workflow-form-modal :deep(.n-card__content) {
  padding: 0;
}

.modal-layout {
  display: flex;
  height: 60vh;
  overflow: hidden;
}

.left-panel {
  width: 260px;
  flex-shrink: 0;
  border-right: 1px solid #e0e0e6;
  background: #fff;
}

.right-panel {
  flex: 1;
  background: #f8f8f8;
  overflow: hidden;
}
</style>
