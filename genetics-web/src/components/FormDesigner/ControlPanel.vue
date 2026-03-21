<template>
  <div class="control-panel">
    <div class="panel-header">
      <span>控件列表</span>
      <el-select
        v-model="selectedBizType"
        placeholder="业务分组"
        size="small"
        clearable
        @change="onBizTypeChange"
      >
        <el-option v-for="bt in bizTypeOptions" :key="bt" :value="bt" :label="GROUP_MAPPING[bt] || bt" />
      </el-select>
      <el-input
        v-model="keyword"
        placeholder="搜索控件"
        size="small"
        clearable
        prefix-icon="Search"
      />
    </div>
    <div v-loading="loading" class="panel-body">
      <div v-for="group in displayGroups" :key="group.bizType" class="control-group">
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
      <el-empty v-if="!loading && !displayGroups.length" description="暂无控件" :image-size="60" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import draggable from 'vuedraggable'
import { getGroupedControls, getControlsByBusinessType, getBusinessTypes } from '@/api/formControl'

const GROUP_MAPPING = {
  'Company': '公司信息',
  'CompanyLegalPerson': '法人信息',
  'CompanyTaxNo': '税号信息',
  'CompanyAttachment': '附件信息',
  'CompanyService': '服务信息',
  'CompanyBank': '銀行信息',
  'CompanyContact': '联系人信息',
  'CompanyAddress': '地址信息',
  'CompanyShareholder': '股东信息',
  'CompanyBusiness': '业务信息'
}

const loading = ref(false)
const keyword = ref('')
const selectedBizType = ref('')
const bizTypeOptions = ref([])

// 分组数据：{ Company: [...], CompanyBank: [...] }
const groupedData = ref({})

// 根据分组数据 + 关键词过滤，转为数组展示
const displayGroups = computed(() => {
  const kw = keyword.value.toLowerCase()
  return Object.entries(groupedData.value)
    .map(([bizType, controls]) => ({
      bizType,
      label: GROUP_MAPPING[bizType] || bizType,
      controls: kw
        ? controls.filter(c =>
            c.controlName.toLowerCase().includes(kw) ||
            c.controlKey.toLowerCase().includes(kw)
          )
        : controls
    }))
    .filter(g => g.controls.length > 0)
})

async function loadAllGroups() {
  loading.value = true
  try {
    const res = await getGroupedControls()
    groupedData.value = res.data || {}
  } finally {
    loading.value = false
  }
}

async function onBizTypeChange(val) {
  if (!val) {
    await loadAllGroups()
    return
  }
  loading.value = true
  try {
    const res = await getControlsByBusinessType(val)
    groupedData.value = { [val]: res.data || [] }
  } finally {
    loading.value = false
  }
}

function cloneControl(control) {
  return {
    colIndex: 0,
    colSpan: 1,
    controlId: control.id,
    controlKey: control.controlKey,
    controlType: control.controlType,
    label: control.controlName
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

onMounted(async () => {
  const [btRes] = await Promise.all([
    getBusinessTypes()
  ])
  bizTypeOptions.value = btRes.data || []
  await loadAllGroups()
})
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
