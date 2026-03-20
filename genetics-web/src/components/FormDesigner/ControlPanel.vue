<template>
  <div class="control-panel">
    <div class="panel-header">
      <span>控件列表</span>
      <el-input
        v-model="keyword"
        placeholder="搜索控件"
        size="small"
        clearable
        prefix-icon="Search"
      />
    </div>
    <div class="panel-body">
      <!-- 按类型分组 -->
      <div v-for="group in groupedControls" :key="group.type" class="control-group">
        <div class="group-title">{{ group.label }}</div>
        <draggable
          :list="group.controls"
          :group="{ name: 'controls', pull: 'clone', put: false }"
          :clone="cloneControl"
          item-key="id"
          class="drag-area"
          :sort="false"
        >
          <template #item="{ element }">
            <div class="control-item" :title="element.controlKey">
              <el-icon class="control-icon"><component :is="getIcon(element.controlType)" /></el-icon>
              <div class="control-info">
                <div class="control-name">{{ element.controlName }}</div>
                <div class="control-key">{{ element.controlKey }}</div>
              </div>
            </div>
          </template>
        </draggable>
      </div>
      <el-empty v-if="!controls.length" description="暂无控件" :image-size="60" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import draggable from 'vuedraggable'

const props = defineProps({
  controls: {
    type: Array,
    default: () => []
  }
})

const keyword = ref('')

const TYPE_GROUPS = [
  { type: 'INPUT', label: '输入框' },
  { type: 'TEXTAREA', label: '多行文本' },
  { type: 'NUMBER', label: '数字' },
  { type: 'SELECT', label: '下拉框' },
  { type: 'SWITCH', label: '开关' },
  { type: 'DATE', label: '日期' },
  { type: 'UPLOAD', label: '文件上传' }
]

const filteredControls = computed(() => {
  if (!keyword.value) return props.controls
  const kw = keyword.value.toLowerCase()
  return props.controls.filter(c =>
    c.controlName.toLowerCase().includes(kw) ||
    c.controlKey.toLowerCase().includes(kw)
  )
})

const groupedControls = computed(() => {
  return TYPE_GROUPS
    .map(g => ({
      ...g,
      controls: filteredControls.value.filter(c => c.controlType === g.type)
    }))
    .filter(g => g.controls.length > 0)
})

function cloneControl(control) {
  return {
    colIndex: 0,
    colSpan: 1,
    controlId: control.id,
    controlKey: control.controlKey,
    controlType: control.controlType,
    label: control.controlName   // 控件名称作为表单标签
  }
}

function getIcon(type) {
  const map = {
    INPUT: 'EditPen',
    TEXTAREA: 'Memo',
    NUMBER: 'Coin',
    SELECT: 'ArrowDown',
    SWITCH: 'Switch',
    DATE: 'Calendar',
    UPLOAD: 'Upload'
  }
  return map[type] || 'EditPen'
}
</script>

<style scoped>
.control-panel {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #fff;
  border-right: 1px solid #eee;
}
.panel-header {
  padding: 12px;
  border-bottom: 1px solid #eee;
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-weight: bold;
  font-size: 14px;
}
.panel-body { flex: 1; overflow-y: auto; padding: 8px; }
.control-group { margin-bottom: 12px; }
.group-title {
  font-size: 12px;
  color: #909399;
  padding: 4px 8px;
  background: #f5f7fa;
  border-radius: 4px;
  margin-bottom: 6px;
}
.drag-area { min-height: 10px; }
.control-item {
  display: flex;
  align-items: center;
  padding: 8px;
  margin-bottom: 4px;
  background: #f0f7ff;
  border: 1px solid #b3d8ff;
  border-radius: 6px;
  cursor: grab;
  gap: 8px;
  transition: box-shadow 0.2s;
}
.control-item:hover { box-shadow: 0 2px 8px rgba(0,0,0,0.15); }
.control-item:active { cursor: grabbing; }
.control-icon { color: #409eff; font-size: 16px; flex-shrink: 0; }
.control-info { flex: 1; min-width: 0; }
.control-name { font-size: 13px; color: #303133; font-weight: 500; }
.control-key { font-size: 11px; color: #909399; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
</style>
