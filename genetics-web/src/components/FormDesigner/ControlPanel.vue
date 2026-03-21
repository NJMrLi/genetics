<template>
  <div class="control-panel">
    <div class="panel-header">
      <span class="panel-title">控件列表</span>
      <n-select
        v-model:value="selectedBizType"
        :options="bizTypeOptionsFormatted"
        placeholder="业务分组"
        clearable
        size="small"
        @update:value="onBizTypeChange"
      />
      <n-input
        v-model:value="keyword"
        placeholder="搜索控件"
        clearable
        size="small"
      >
        <template #prefix>
          <n-icon><SearchOutline /></n-icon>
        </template>
      </n-input>
    </div>
    <div class="panel-body">
      <n-spin :show="loading">
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
                <n-icon :size="18" :color="'#18a058'">
                  <component :is="getIcon(element.controlType)" />
                </n-icon>
                <div class="control-info">
                  <div class="control-name">{{ element.controlName }}</div>
                  <div class="control-key">{{ element.controlKey }}</div>
                </div>
              </div>
            </template>
          </draggable>
        </div>
        <n-empty v-if="!loading && !displayGroups.length" description="暂无控件" size="small" />
      </n-spin>
    </div>
  </div>
</template>

<script setup>
import { h, ref, computed, onMounted } from 'vue'
import { NSelect, NInput, NIcon, NSpin, NEmpty } from 'naive-ui'
import {
  SearchOutline,
  CreateOutline,
  DocumentTextOutline,
  CalculatorOutline,
  ChevronDownOutline,
  ToggleOutline,
  CalendarOutline,
  CloudUploadOutline
} from '@vicons/ionicons5'
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
const groupedData = ref({})

const bizTypeOptionsFormatted = computed(() =>
  bizTypeOptions.value.map(bt => ({
    label: GROUP_MAPPING[bt] || bt,
    value: bt
  }))
)

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
    INPUT: CreateOutline,
    TEXTAREA: DocumentTextOutline,
    NUMBER: CalculatorOutline,
    SELECT: ChevronDownOutline,
    SWITCH: ToggleOutline,
    DATE: CalendarOutline,
    UPLOAD: CloudUploadOutline
  }
  return map[type] || CreateOutline
}

onMounted(async () => {
  const btRes = await getBusinessTypes()
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
}

.panel-header {
  padding: 12px;
  border-bottom: 1px solid #e0e0e6;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.panel-title {
  font-weight: 600;
  font-size: 14px;
  color: #333;
}

.panel-body {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.control-group {
  margin-bottom: 12px;
}

.group-title {
  font-size: 12px;
  color: #666;
  padding: 4px 8px;
  background: #f5f5f5;
  border-radius: 4px;
  margin-bottom: 6px;
}

.drag-area {
  min-height: 10px;
}

.control-item {
  display: flex;
  align-items: center;
  padding: 8px;
  margin-bottom: 4px;
  background: #f0f9f4;
  border: 1px solid #d0e8d8;
  border-radius: 6px;
  cursor: grab;
  gap: 8px;
  transition: box-shadow 0.2s;
}

.control-item:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.15);
}

.control-item:active {
  cursor: grabbing;
}

.control-info {
  flex: 1;
  min-width: 0;
}

.control-name {
  font-size: 13px;
  color: #333;
  font-weight: 500;
}

.control-key {
  font-size: 11px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
