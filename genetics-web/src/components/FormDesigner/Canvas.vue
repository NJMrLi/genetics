<template>
  <div class="canvas">
    <!-- 画板工具栏 -->
    <div class="canvas-toolbar">
      <span class="canvas-title">画板</span>
      <div class="toolbar-actions">
        <el-select v-model="store.columns" size="small" style="width: 100px" @change="handleColumnsChange">
          <el-option :value="1" label="1列布局" />
          <el-option :value="2" label="2列布局" />
          <el-option :value="3" label="3列布局" />
          <el-option :value="4" label="4列布局" />
        </el-select>
        <el-button size="small" type="primary" @click="store.addRow">
          <el-icon><Plus /></el-icon>添加行
        </el-button>
      </div>
    </div>

    <!-- 画板内容 -->
    <div class="canvas-body">
      <el-empty v-if="!store.rows.length" description="拖拽左侧控件到此处，或点击「添加行」" />

      <div
        v-for="(row, rowIndex) in store.rows"
        :key="rowIndex"
        class="canvas-row"
      >
        <!-- 行工具栏 -->
        <div class="row-toolbar">
          <span class="row-label">第 {{ rowIndex + 1 }} 行</span>
          <el-button size="small" type="danger" text @click="store.removeRow(rowIndex)">
            <el-icon><Delete /></el-icon>
          </el-button>
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
            >
              <!-- 控件卡片 -->
              <div class="cell-card">
                <div class="cell-header">
                  <el-input
                    v-model="cell.label"
                    size="small"
                    placeholder="标签名"
                    class="cell-label-input"
                  />
                  <div class="cell-actions">
                    <!-- 调整跨列 -->
                    <el-tooltip content="调整列宽">
                      <el-select
                        v-model="cell.colSpan"
                        size="small"
                        style="width: 70px"
                        @change="store.setCellColSpan(rowIndex, colIndex, cell.colSpan)"
                      >
                        <el-option
                          v-for="n in store.columns"
                          :key="n"
                          :value="n"
                          :label="`${n}/${store.columns}列`"
                        />
                      </el-select>
                    </el-tooltip>
                    <el-button
                      size="small"
                      type="danger"
                      text
                      @click="store.removeCellFromRow(rowIndex, colIndex)"
                    >
                      <el-icon><Close /></el-icon>
                    </el-button>
                  </div>
                </div>
                <div class="cell-body">
                  <el-tag :type="getTagType(cell.controlType)" size="small">{{ cell.controlType }}</el-tag>
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
  </div>
</template>

<script setup>
import { computed } from 'vue'
import draggable from 'vuedraggable'
import { useFormDesignerStore } from '@/stores/formDesigner'

const store = useFormDesignerStore()

const gridStyle = computed(() => ({
  display: 'grid',
  gridTemplateColumns: `repeat(${store.columns}, 1fr)`,
  gap: '8px',
  minHeight: '60px'
}))

function handleColumnsChange() {
  // 列数变化时，重置所有cell的colSpan为1，防止超出
  store.rows.forEach(row => {
    row.cells.forEach(cell => {
      if (cell.colSpan > store.columns) {
        cell.colSpan = store.columns
      }
    })
  })
}

function handleAdd(event, rowIndex) {
  // 拖拽新增时重新分配colIndex
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
    INPUT: '',
    TEXTAREA: 'info',
    SELECT: 'success',
    SWITCH: 'warning',
    UPLOAD: 'danger',
    DATE: '',
    NUMBER: 'info'
  }
  return map[type] || ''
}
</script>

<style scoped>
.canvas {
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #f8f9fa;
}
.canvas-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 16px;
  background: #fff;
  border-bottom: 1px solid #eee;
}
.canvas-title { font-weight: bold; font-size: 14px; }
.toolbar-actions { display: flex; align-items: center; gap: 8px; }
.canvas-body { flex: 1; overflow-y: auto; padding: 16px; display: flex; flex-direction: column; gap: 12px; }
.canvas-row { background: #fff; border-radius: 8px; border: 1px solid #e4e7ed; overflow: hidden; }
.row-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 12px;
  background: #f5f7fa;
  border-bottom: 1px solid #eee;
}
.row-label { font-size: 12px; color: #909399; }
.canvas-grid { padding: 8px; }
.canvas-cell { }
.cell-card {
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  background: #fff;
  overflow: hidden;
  cursor: move;
  transition: box-shadow 0.2s;
}
.cell-card:hover { box-shadow: 0 2px 8px rgba(0,0,0,0.15); border-color: #409eff; }
.cell-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 6px 8px;
  background: #f5f7fa;
  border-bottom: 1px solid #eee;
  gap: 6px;
}
.cell-label-input { flex: 1; }
.cell-actions { display: flex; align-items: center; gap: 4px; flex-shrink: 0; }
.cell-body { padding: 8px; display: flex; align-items: center; gap: 8px; }
.cell-key { font-size: 12px; color: #606266; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.drop-placeholder {
  text-align: center;
  color: #c0c4cc;
  font-size: 13px;
  padding: 20px;
  border: 2px dashed #dcdfe6;
  border-radius: 6px;
}
</style>
