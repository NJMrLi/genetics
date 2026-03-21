<template>
  <div class="canvas">
    <!-- 画板工具栏 -->
    <div class="canvas-toolbar">
      <span class="canvas-title">画板</span>
      <n-space>
        <n-select
          v-model:value="store.columns"
          :options="columnOptions"
          style="width: 120px"
          @update:value="handleColumnsChange"
        />
        <n-button type="primary" size="small" @click="store.addRow">
          <template #icon><n-icon><AddOutline /></n-icon></template>
          添加行
        </n-button>
      </n-space>
    </div>

    <!-- 画板内容 -->
    <div class="canvas-body">
      <n-empty v-if="!store.rows.length" description="拖拽左侧控件到此处，或点击「添加行」" />

      <div
        v-for="(row, rowIndex) in store.rows"
        :key="rowIndex"
        class="canvas-row"
      >
        <!-- 行工具栏 -->
        <div class="row-toolbar">
          <span class="row-label">第 {{ rowIndex + 1 }} 行</span>
          <n-button quaternary size="small" type="error" @click="store.removeRow(rowIndex)">
            <template #icon><n-icon><TrashOutline /></n-icon></template>
          </n-button>
        </div>

        <!-- 网格拖拽区域 -->
        <draggable
          v-model="store.rows[rowIndex].cells"
          :group="{ name: 'controls', pull: true, put: true }"
          item-key="controlId"
          class="canvas-grid"
          :style="gridStyle"
          @add="(e) => handleAdd(e, rowIndex)"
        >
          <template #item="{ element: cell, index: colIndex }">
            <div
              class="canvas-cell"
              :style="{ gridColumn: `span ${cell.colSpan}` }"
              @click="showControlDetail(cell)"
            >
              <!-- 控件卡片 -->
              <div class="cell-card" :class="{ active: selectedCell?.controlId === cell.controlId }">
                <div class="cell-header">
                  <n-input
                    v-model:value="cell.label"
                    size="small"
                    placeholder="标签名"
                    class="cell-label-input"
                    @click.stop
                  />
                  <n-space :size="4">
                    <n-select
                      v-model:value="cell.colSpan"
                      :options="getColSpanOptions()"
                      size="small"
                      style="width: 80px"
                      @update:value="store.setCellColSpan(rowIndex, colIndex, cell.colSpan)"
                      @click.stop
                    />
                    <n-button quaternary size="small" type="error" @click.stop="store.removeCellFromRow(rowIndex, colIndex)">
                      <template #icon><n-icon><CloseOutline /></n-icon></template>
                    </n-button>
                  </n-space>
                </div>
                <div class="cell-body">
                  <n-tag :type="getTagType(cell.controlType)" size="small">{{ cell.controlType }}</n-tag>
                  <span class="cell-key">{{ cell.controlKey }}</span>
                </div>
              </div>
            </div>
          </template>

          <!-- 空行拖拽提示 -->
          <template #footer>
            <div v-if="!row.cells.length" class="drop-placeholder">
              拖拽控件到此行
            </div>
          </template>
        </draggable>
      </div>
    </div>

    <!-- 控件详情抽屉 -->
    <n-drawer
      v-model:show="detailDrawerVisible"
      :width="400"
      placement="right"
      :mask-closable="true"
    >
      <n-drawer-content :title="`控件配置 - ${selectedCell?.label || ''}`" closable>
        <n-descriptions v-if="selectedControl" :columns="1" bordered size="small">
          <n-descriptions-item label="控件名称">
            {{ selectedControl.controlName }}
          </n-descriptions-item>
          <n-descriptions-item label="控件Key">
            {{ selectedControl.controlKey }}
          </n-descriptions-item>
          <n-descriptions-item label="控件类型">
            <n-tag :type="getTagType(selectedControl.controlType)" size="small">
              {{ selectedControl.controlType }}
            </n-tag>
          </n-descriptions-item>
          <n-descriptions-item label="是否必填">
            {{ selectedControl.required ? '是' : '否' }}
          </n-descriptions-item>
          <n-descriptions-item v-if="selectedControl.placeholder" label="占位文本">
            {{ selectedControl.placeholder }}
          </n-descriptions-item>
          <n-descriptions-item v-if="selectedControl.tips" label="说明提示">
            {{ selectedControl.tips }}
          </n-descriptions-item>
          <n-descriptions-item v-if="selectedControl.minLength || selectedControl.maxLength" label="长度限制">
            {{ selectedControl.minLength ? '最少' + selectedControl.minLength : '' }}
            {{ selectedControl.minLength && selectedControl.maxLength ? ' / ' : '' }}
            {{ selectedControl.maxLength ? '最多' + selectedControl.maxLength : '' }}
          </n-descriptions-item>
          <n-descriptions-item v-if="selectedControl.regexPattern" label="正则表达式">
            <n-code :code="selectedControl.regexPattern" language="javascript" />
          </n-descriptions-item>
          
          <!-- SELECT 类型展示选项 -->
          <n-descriptions-item v-if="selectedControl.controlType === 'SELECT' && selectOptions.length" label="下拉选项">
            <n-list size="small" bordered>
              <n-list-item v-for="opt in selectOptions" :key="opt.value">
                <n-space>
                  <n-tag size="small">{{ opt.value }}</n-tag>
                  <span>{{ opt.label }}</span>
                </n-space>
              </n-list-item>
            </n-list>
          </n-descriptions-item>

          <!-- UPLOAD 类型展示配置 -->
          <n-descriptions-item v-if="selectedControl.controlType === 'UPLOAD' && uploadConfig" label="上传配置">
            <n-space vertical>
              <span>最大文件数: {{ uploadConfig.maxCount || 3 }}</span>
              <span>文件格式: {{ uploadConfig.accept || '不限' }}</span>
              <span>最大大小: {{ uploadConfig.maxSizeMB || 10 }}MB</span>
            </n-space>
          </n-descriptions-item>
        </n-descriptions>
        <n-empty v-else description="未找到控件配置" />
      </n-drawer-content>
    </n-drawer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import {
  NSelect,
  NButton,
  NInput,
  NTag,
  NSpace,
  NIcon,
  NEmpty,
  NDrawer,
  NDrawerContent,
  NDescriptions,
  NDescriptionsItem,
  NList,
  NListItem,
  NCode
} from 'naive-ui'
import { AddOutline, TrashOutline, CloseOutline } from '@vicons/ionicons5'
import draggable from 'vuedraggable'
import { useFormDesignerStore } from '@/stores/formDesigner'
import { listAllControls } from '@/api/formControl'

const store = useFormDesignerStore()

const columnOptions = [
  { label: '1列布局', value: 1 },
  { label: '2列布局', value: 2 },
  { label: '3列布局', value: 3 },
  { label: '4列布局', value: 4 }
]

const gridStyle = computed(() => ({
  display: 'grid',
  gridTemplateColumns: `repeat(${store.columns}, 1fr)`,
  gap: '8px',
  minHeight: '60px'
}))

const detailDrawerVisible = ref(false)
const selectedCell = ref(null)
const selectedControl = ref(null)
const allControls = ref([])

const selectOptions = computed(() => {
  if (!selectedControl.value?.selectOptions) return []
  try {
    const opts = selectedControl.value.selectOptions
    if (Array.isArray(opts)) return opts
    return JSON.parse(opts)
  } catch {
    return []
  }
})

const uploadConfig = computed(() => {
  if (!selectedControl.value?.uploadConfig) return null
  try {
    const cfg = selectedControl.value.uploadConfig
    if (typeof cfg === 'object') return cfg
    return JSON.parse(cfg)
  } catch {
    return null
  }
})

function getColSpanOptions() {
  return Array.from({ length: store.columns }, (_, i) => ({
    label: `${i + 1}/${store.columns}列`,
    value: i + 1
  }))
}

function handleColumnsChange() {
  store.rows.forEach(row => {
    row.cells.forEach(cell => {
      if (cell.colSpan > store.columns) {
        cell.colSpan = store.columns
      }
    })
  })
}

function handleAdd(event, rowIndex) {
  const row = store.rows[rowIndex]
  if (row) {
    row.cells.forEach((cell, idx) => {
      cell.colIndex = idx
      if (!cell.colSpan) cell.colSpan = 1
    })
  }
}

function getTagType(type) {
  const map = {
    INPUT: 'default',
    TEXTAREA: 'info',
    SELECT: 'success',
    SWITCH: 'warning',
    UPLOAD: 'error',
    DATE: 'default',
    NUMBER: 'info'
  }
  return map[type] || 'default'
}

async function showControlDetail(cell) {
  selectedCell.value = cell
  
  // 如果还没有加载过所有控件，先加载
  if (allControls.value.length === 0) {
    try {
      const res = await listAllControls()
      allControls.value = res.data || []
    } catch {
      allControls.value = []
    }
  }
  
  // 查找控件详情
  selectedControl.value = allControls.value.find(c => c.id === cell.controlId) || null
  detailDrawerVisible.value = true
}
</script>

<style scoped>
.canvas {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #f8f8f8;
}

.canvas-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 16px;
  background: #fff;
  border-bottom: 1px solid #e0e0e6;
}

.canvas-title {
  font-weight: 600;
  font-size: 14px;
  color: #333;
}

.canvas-body {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.canvas-row {
  background: #fff;
  border-radius: 8px;
  border: 1px solid #e0e0e6;
  overflow: hidden;
}

.row-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 12px;
  background: #f5f5f5;
  border-bottom: 1px solid #e0e0e6;
}

.row-label {
  font-size: 12px;
  color: #666;
}

.canvas-grid {
  padding: 8px;
}

.canvas-cell {
  cursor: pointer;
}

.cell-card {
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  background: #fff;
  overflow: hidden;
  cursor: move;
  transition: all 0.2s;
}

.cell-card:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  border-color: #18a058;
}

.cell-card.active {
  border-color: #18a058;
  box-shadow: 0 0 0 2px rgba(24, 160, 88, 0.2);
}

.cell-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 8px;
  background: #f5f5f5;
  border-bottom: 1px solid #e0e0e6;
  gap: 6px;
}

.cell-label-input {
  flex: 1;
}

.cell-body {
  padding: 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.cell-key {
  font-size: 12px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.drop-placeholder {
  text-align: center;
  color: #999;
  font-size: 13px;
  padding: 20px;
  border: 2px dashed #d9d9d9;
  border-radius: 6px;
}
</style>
