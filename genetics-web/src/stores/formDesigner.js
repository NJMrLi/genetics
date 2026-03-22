import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

/**
 * 表单设计器 Store
 * 管理画板中的行列结构和控件放置
 */
export const useFormDesignerStore = defineStore('formDesigner', () => {
  // 行数据：[{ rowIndex, columns, cells: [{ colIndex, colSpan, controlId, controlKey, controlType, label }] }]
  const rows = ref([])

  // 模板基本信息
  const templateInfo = ref({
    templateName: '',
    version: '1.0.0',
    countryCode: '',
    serviceCodeL1: '',
    serviceCodeL2: '',
    serviceCodeL3: '',
    remark: '',
    status: 0
  })

  // 生成 JSON Schema
  const jsonSchema = computed(() => ({
    layout: 'grid',
    rows: rows.value
  }))

  // 添加一行
  function addRow() {
    const rowIndex = rows.value.length
    rows.value.push({ rowIndex, columns: 2, cells: [] })
  }

  // 修改行布局列数
  function setRowColumns(rowIndex, cols) {
    const row = rows.value[rowIndex]
    if (row) {
      row.columns = cols
      // 校验该行下所有 cell 的 colSpan 不能超过新的列数
      row.cells.forEach(cell => {
        if (cell.colSpan > cols) {
          cell.colSpan = cols
        }
      })
    }
  }

  // 删除一行
  function removeRow(rowIndex) {
    rows.value.splice(rowIndex, 1)
    // 重新排序
    rows.value.forEach((row, idx) => (row.rowIndex = idx))
  }

  // 向某行添加控件
  function addCellToRow(rowIndex, control) {
    const row = rows.value[rowIndex]
    if (!row) return
    const colIndex = row.cells.length
    row.cells.push({
      colIndex,
      colSpan: 1,
      controlId: control.id,
      controlKey: control.controlKey,
      controlType: control.controlType,
      label: control.controlName
    })
  }

  // 从行中移除控件
  function removeCellFromRow(rowIndex, colIndex) {
    const row = rows.value[rowIndex]
    if (!row) return
    row.cells.splice(colIndex, 1)
    row.cells.forEach((cell, idx) => (cell.colIndex = idx))
  }

  // 修改列跨度
  function setCellColSpan(rowIndex, colIndex, colSpan) {
    const row = rows.value[rowIndex]
    if (!row) return
    const cell = row.cells[colIndex]
    if (cell) cell.colSpan = colSpan
  }

  // 修改标签
  function setCellLabel(rowIndex, colIndex, label) {
    const row = rows.value[rowIndex]
    if (!row) return
    const cell = row.cells[colIndex]
    if (cell) cell.label = label
  }

  // 加载已有模板数据
  function loadTemplate(templateDetail) {
    const schema = templateDetail.jsonSchema
    templateInfo.value = {
      templateName: templateDetail.templateName,
      version: templateDetail.version,
      countryCode: templateDetail.countryCode,
      serviceCodeL1: templateDetail.serviceCodeL1,
      serviceCodeL2: templateDetail.serviceCodeL2,
      serviceCodeL3: templateDetail.serviceCodeL3,
      remark: templateDetail.remark || '',
      status: templateDetail.status
    }
    if (schema) {
      rows.value = (schema.rows || []).map(r => ({
        ...r,
        columns: r.columns || schema.columns || 2 // 兼容旧版本数据
      }))
    }
  }

  // 清空画板
  function reset() {
    rows.value = []
    templateInfo.value = {
      templateName: '',
      version: '1.0.0',
      countryCode: '',
      serviceCodeL1: '',
      serviceCodeL2: '',
      serviceCodeL3: '',
      remark: '',
      status: 0
    }
  }

  return {
    columns,
    rows,
    templateInfo,
    jsonSchema,
    addRow,
    removeRow,
    addCellToRow,
    removeCellFromRow,
    setCellColSpan,
    setCellLabel,
    loadTemplate,
    reset
  }
})
