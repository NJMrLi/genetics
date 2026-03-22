<template>
  <n-modal
    v-model:show="show"
    preset="card"
    :title="`配置动作表单 - ${actionName}`"
    style="width: 1000px; height: 85vh"
    class="workflow-form-modal"
    :segmented="{ content: 'soft', footer: 'soft' }"
  >
    <!-- 右上角工具栏 -->
    <template #header-extra>
      <n-space>
        <n-button size="small" secondary type="primary" @click="showPreview = true">
          <template #icon><n-icon><EyeOutline /></n-icon></template>
          预览表单
        </n-button>
        <n-button size="small" secondary @click="showJson = true">
          <template #icon><n-icon><CodeSlashOutline /></n-icon></template>
          查看 JSON
        </n-button>
      </n-space>
    </template>

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

  <!-- 预览弹窗 -->
  <n-modal v-model:show="showPreview" preset="card" title="表单预览" style="width: 800px">
    <div style="padding: 20px; background: #f8f8f8; border-radius: 8px">
      <dynamic-form :schema="store.jsonSchema" v-model="previewData" />
    </div>
    <template #footer>
      <n-space justify="end">
        <n-button @click="showPreview = false">关闭预览</n-button>
      </n-space>
    </template>
  </n-modal>

  <!-- JSON 查看弹窗 -->
  <n-modal v-model:show="showJson" preset="card" title="JSON Schema" style="width: 600px">
    <n-scrollbar style="max-height: 500px">
      <div style="padding: 12px; background: #1e1e1e; border-radius: 4px">
        <n-code :code="JSON.stringify(store.jsonSchema, null, 2)" language="json" word-wrap />
      </div>
    </n-scrollbar>
    <template #footer>
      <n-space justify="end">
        <n-button @click="showJson = false">关闭</n-button>
      </n-space>
    </template>
  </n-modal>
</template>

<script setup>
import { ref, watch, onUnmounted } from 'vue'
import { NModal, NButton, NSpace, NIcon, NScrollbar, NCode } from 'naive-ui'
import { EyeOutline, CodeSlashOutline } from '@vicons/ionicons5'
import ControlPanel from '@/components/FormDesigner/ControlPanel.vue'
import CanvasPanel from '@/components/FormDesigner/Canvas.vue'
import DynamicForm from '@/components/DynamicForm/DynamicForm.vue'
import { useFormDesignerStore } from '@/stores/formDesigner'

const props = defineProps({
  show: Boolean,
  actionName: String,
  initialSchema: [Object, String]
})

const emit = defineEmits(['update:show', 'confirm'])

const show = ref(props.show)
const store = useFormDesignerStore()

// 预览相关
const showPreview = ref(false)
const previewData = ref({})
const showJson = ref(false)

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
        store.loadTemplate(schema)
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
  height: 65vh;
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
