<template>
  <div class="workflow-config">
    <!-- 顶部工具栏 -->
    <div class="config-toolbar">
      <n-space align="center" :wrap="false">
        <span class="toolbar-section">快速加载：</span>
        <n-button-group size="small">
          <n-button @click="loadDefaultConfig">
            <template #icon><n-icon><GitNetworkOutline /></n-icon></template>
            默认流程
          </n-button>
          <n-button @click="loadVatConfig">VAT流程</n-button>
          <n-button @click="loadEprConfig">EPR流程</n-button>
        </n-button-group>
        <n-divider vertical />
        <n-button size="small" @click="clearAll">
          <template #icon><n-icon><TrashOutline /></n-icon></template>
          清空
        </n-button>
        <n-button size="small" @click="showJsonModal = true">
          <template #icon><n-icon><CodeSlashOutline /></n-icon></template>
          查看JSON
        </n-button>
        <n-divider vertical />
        <span class="toolbar-tip">
          <n-icon size="13" color="#aaa"><InformationCircleOutline /></n-icon>
          拖拽左侧节点到画布 · 拖拽节点底部绿点连线
        </span>
      </n-space>
    </div>

    <div class="designer-layout">
      <!-- 左侧节点库 -->
      <div class="node-palette">
        <div class="palette-section">状态节点</div>
        <div
          v-for="state in stateOptions"
          :key="state.value"
          class="palette-item palette-state"
          :style="{ '--node-color': getStateColor(state.value) }"
          draggable="true"
          @dragstart="onDragStart($event, { type: 'state', ...state })"
        >
          <span class="palette-dot"></span>
          {{ state.label }}
        </div>
      </div>

      <!-- Vue Flow 画布 -->
      <div class="flow-container" ref="flowWrapper" @drop="onDrop" @dragover.prevent>
        <VueFlow
          id="workflow-flow"
          v-model:nodes="nodes"
          v-model:edges="edges"
          :default-edge-options="defaultEdgeOptions"
          :connect-on-click="false"
          :nodes-connectable="true"
          :zoom-on-scroll="true"
          fit-view-on-init
          @connect="onConnect"
          @edge-click="onEdgeClick"
          @pane-click="onPaneClick"
          @node-click="onNodeClick"
        >
          <Background pattern-color="#f0f0f0" :gap="20" />
          <Controls position="bottom-right" />

          <!-- 状态节点 -->
          <template #node-state="nodeProps">
            <div
              class="flow-node flow-node-state"
              :class="{ selected: nodeProps.selected }"
              :style="{ '--c': getStateColor(nodeProps.data.stateId) }"
            >
              <div class="flow-node-body">
                <span class="flow-node-icon" :style="{ background: getStateColor(nodeProps.data.stateId) }"></span>
                <span class="flow-node-label">{{ nodeProps.data.label }}</span>
              </div>
              <div v-if="config.allowTerminateFrom?.includes(nodeProps.data.stateId)" class="flow-node-tag">可终止</div>
              <Handle type="target" :position="Position.Top" class="fh fh-top" />
              <Handle type="source" :position="Position.Bottom" class="fh fh-bottom" />
            </div>
          </template>
        </VueFlow>

        <!-- 空状态提示 -->
        <div v-if="nodes.length === 0" class="canvas-empty">
          <n-icon size="48" color="#ddd"><GitNetworkOutline /></n-icon>
          <p>从左侧拖拽节点到此处开始配置</p>
          <p style="font-size:12px;color:#bbb">或点击上方快速加载预设流程</p>
        </div>
      </div>

      <!-- 右侧属性面板 -->
      <div class="property-panel">
        <!-- 连线属性 -->
        <template v-if="selectedEdge">
          <div class="panel-header">
            <n-icon size="14" color="#18a058"><GitMergeOutline /></n-icon>
            流转规则
          </div>
          <n-form size="small" label-placement="top" :show-feedback="false" class="panel-form">
            <n-form-item label="选择操作">
              <n-select
                v-model:value="selectedEdge.data.action"
                :options="presetActions"
                placeholder="请选择预设操作"
                @update:value="onActionSelect"
              />
            </n-form-item>
            <n-form-item label="操作代码 (Key)">
              <n-input :value="selectedEdge.data.action" disabled placeholder="由操作自动生成" />
            </n-form-item>
            <n-form-item label="适用条件">
              <n-select
                v-model:value="selectedEdge.data.condition"
                :options="conditionOptions"
                placeholder="通用（不限条件）"
                clearable
              />
            </n-form-item>
            <n-form-item label="需要填写备注">
              <n-switch v-model:value="selectedEdge.data.needRemark" size="small" />
            </n-form-item>
            <n-form-item label="关联业务表单">
              <n-button size="small" secondary block type="primary" @click="openFormDesigner">
                <template #icon><n-icon><CreateOutline /></n-icon></template>
                {{ selectedEdge.data.formSchema ? '编辑表单' : '配置表单' }}
              </n-button>
              <div v-if="selectedEdge.data.formSchema" class="form-tag">已配置</div>
            </n-form-item>
          </n-form>
          <n-button type="error" size="small" block style="margin-top:16px" ghost @click="removeSelectedEdge">
            <template #icon><n-icon><TrashOutline /></n-icon></template>
            删除此连线
          </n-button>
        </template>

        <!-- 节点属性 -->
        <template v-else-if="selectedNode">
          <div class="panel-header">
            <n-icon size="14" color="#2080f0"><SquareOutline /></n-icon>
            节点配置
          </div>
          <n-form size="small" label-placement="top" :show-feedback="false" class="panel-form">
            <n-form-item label="状态名称">
              <n-input :value="selectedNode.data.label" disabled />
            </n-form-item>
            <n-form-item v-if="selectedNode.type === 'state'" label="允许终止服务">
              <n-switch
                :value="config.allowTerminateFrom?.includes(selectedNode.data.stateId)"
                @update:value="toggleTerminate(selectedNode.data.stateId)"
                size="small"
              />
            </n-form-item>
          </n-form>
          <n-button type="error" size="small" block style="margin-top:16px" ghost @click="removeSelectedNode">
            <template #icon><n-icon><TrashOutline /></n-icon></template>
            删除此节点
          </n-button>
        </template>

        <!-- 默认提示 -->
        <div v-else class="panel-empty">
          <n-icon size="36" color="#e0e0e0"><InformationCircleOutline /></n-icon>
          <p>点击节点或连线<br/>进行属性配置</p>
        </div>
      </div>
    </div>
  </div>

  <!-- JSON 预览弹窗 -->
  <n-modal v-model:show="showJsonModal" preset="card" title="流程配置 JSON" style="width:680px;max-height:80vh">
    <n-scrollbar style="max-height:60vh">
      <pre class="json-preview">{{ JSON.stringify(config, null, 2) }}</pre>
    </n-scrollbar>
  </n-modal>

  <!-- 动作表单设计器弹窗 -->
  <workflow-form-modal
    v-if="selectedEdge"
    v-model:show="showFormModal"
    :action-name="selectedEdge.data.actionName"
    :initial-schema="selectedEdge.data.formSchema"
    @confirm="handleFormConfirm"
  />
</template>

<script setup>
import { ref, watch, nextTick, onMounted } from 'vue'
import { listWorkflowActions } from '@/api/workflowAction'
import { VueFlow, useVueFlow, Position, MarkerType } from '@vue-flow/core'
import { Background } from '@vue-flow/background'
import { Controls } from '@vue-flow/controls'
import { Handle } from '@vue-flow/core'
import dagre from 'dagre'
import '@vue-flow/core/dist/style.css'
import '@vue-flow/controls/dist/style.css'
import {
  NSpace, NButton, NButtonGroup, NIcon, NDivider,
  NForm, NFormItem, NInput, NSelect, NSwitch,
  NModal, NScrollbar,
  useMessage
} from 'naive-ui'
import {
  InformationCircleOutline, GitNetworkOutline,
  TrashOutline, GitMergeOutline, SquareOutline,
  CodeSlashOutline, CreateOutline
} from '@vicons/ionicons5'
import WorkflowFormModal from './WorkflowFormModal.vue'

const props = defineProps({ modelValue: { type: Object, default: null } })
const emit = defineEmits(['update:modelValue'])
const message = useMessage()

const { setNodes, setEdges, fitView } = useVueFlow('workflow-flow')

// -------- 数据定义 --------
const stateOptions = [
  { label: '待提交', value: 10 },
  { label: '待审核', value: 20 },
  { label: '待递交', value: 30 },
  { label: '当地同事', value: 33 },
  { label: '税局处理', value: 32 },
  { label: '组织处理', value: 31 },
  { label: '已完成', value: 40 },
  { label: '已驳回', value: 50 },
  { label: '已终止', value: 99 }
]

const conditionOptions = [
  { label: '通用（不限条件）', value: null },
  { label: 'VAT', value: 'VAT' },
  { label: 'EPR', value: 'EPR' }
]

const presetActions = ref([])

const stateColors = {
  10: '#18a058', 20: '#f0a020', 30: '#2080f0',
  31: '#8a2be2', 32: '#e91e8c', 33: '#00bcd4',
  40: '#52c41a', 50: '#d03050', 99: '#909399'
}
function getStateColor(id) { return stateColors[id] || '#909399' }

const defaultEdgeOptions = {
  type: 'smoothstep',
  animated: false,
  markerEnd: { type: MarkerType.ArrowClosed, width: 16, height: 16, color: '#b0b0b0' },
  style: { stroke: '#b0b0b0', strokeWidth: 1.5 }
}

const NODE_W = 140
const NODE_H = 52

// -------- dagre 自动布局 --------
function getLayoutedNodes(nodeList, edgeList) {
  const g = new dagre.graphlib.Graph()
  g.setDefaultEdgeLabel(() => ({}))
  g.setGraph({ rankdir: 'TB', nodesep: 70, ranksep: 90, marginx: 40, marginy: 40 })

  nodeList.forEach(n => {
    g.setNode(n.id, { width: NODE_W, height: NODE_H })
  })
  edgeList.forEach(e => g.setEdge(e.source, e.target))
  dagre.layout(g)

  return nodeList.map(n => {
    const { x, y, width, height } = g.node(n.id)
    return { ...n, position: { x: x - width / 2, y: y - height / 2 } }
  })
}

// -------- 响应式状态 --------
const nodes = ref([])
const edges = ref([])
const selectedEdge = ref(null)
const selectedNode = ref(null)
const flowWrapper = ref(null)
const config = ref({ transitions: [], allowTerminateFrom: [] })
let edgeSeq = 100
const showJsonModal = ref(false)

// -------- 数据同步：edges → config.transitions --------
watch(edges, (val) => {
  config.value.transitions = val.map(e => ({
    from: e.data?.from,
    to: e.data?.to,
    action: e.data?.action || '',
    actionName: e.data?.actionName || '',
    needRemark: e.data?.needRemark || false,
    condition: e.data?.condition || null,
    formSchema: e.data?.formSchema || null
  }))
}, { deep: true })

// -------- 数据同步：config → emit --------
watch(config, (val) => {
  emit('update:modelValue', JSON.parse(JSON.stringify(val)))
}, { deep: true })

// -------- 从外部 modelValue 初始化（挂载后执行） --------
onMounted(async () => {
  const res = await listWorkflowActions()
  presetActions.value = (res.data || []).map(a => ({
    label: a.actionName,
    value: a.actionCode,
    needRemark: a.needRemark,
    icon: a.icon
  }))

  nextTick(() => {
    if (props.modelValue?.transitions?.length) {
      config.value = JSON.parse(JSON.stringify(props.modelValue))
      applyToCanvas(props.modelValue)
    }
  })
})

// -------- 渲染到画布 --------
function applyToCanvas(cfg) {
  const stateIds = new Set()
  cfg.transitions.forEach(t => {
    if (t.from != null) stateIds.add(t.from)
    if (t.to != null) stateIds.add(t.to)
  })

  const rawNodes = Array.from(stateIds).map(stateId => {
    const state = stateOptions.find(s => s.value === stateId)
    return {
      id: `node-${stateId}`,
      type: 'state',
      position: { x: 0, y: 0 },
      data: { stateId, label: state?.label || String(stateId) }
    }
  })

  const rawEdges = cfg.transitions
    .filter(t => t.from != null && t.to != null)
    .map((t, i) => ({
      id: `edge-${i}`,
      source: `node-${t.from}`,
      target: `node-${t.to}`,
      label: t.actionName || '',
      ...defaultEdgeOptions,
      data: { from: t.from, to: t.to, action: t.action, actionName: t.actionName, needRemark: t.needRemark, condition: t.condition, formSchema: t.formSchema }
    }))

  const layoutedNodes = getLayoutedNodes(rawNodes, rawEdges)
  nodes.value = layoutedNodes
  edges.value = rawEdges
  setNodes(layoutedNodes)
  setEdges(rawEdges)
  nextTick(() => fitView({ padding: 0.25, duration: 300 }))
}

// -------- 拖拽新增节点 --------
const dragPayload = ref(null)
function onDragStart(e, payload) {
  dragPayload.value = payload
  e.dataTransfer.effectAllowed = 'move'
}

function onDrop(e) {
  if (!dragPayload.value) return
  const payload = dragPayload.value
  dragPayload.value = null

  const nodeId = `node-${payload.value}`

  if (nodes.value.find(n => n.id === nodeId)) {
    message.warning('该状态节点已在画布上')
    return
  }

  // 坐标转换：屏幕坐标 → 画布坐标
  const rect = flowWrapper.value.getBoundingClientRect()
  nodes.value = [...nodes.value, {
    id: nodeId,
    type: 'state',
    position: { x: e.clientX - rect.left - NODE_W / 2, y: e.clientY - rect.top - NODE_H / 2 },
    data: { stateId: payload.value, label: payload.label }
  }]
}

// -------- 连线 --------
function onConnect(params) {
  const fromNode = nodes.value.find(n => n.id === params.source)
  const toNode = nodes.value.find(n => n.id === params.target)
  if (!fromNode || !toNode) return

  const exists = edges.value.find(e => e.source === params.source && e.target === params.target)
  if (exists) { message.warning('该流转规则已存在'); return }

  const fromState = fromNode.data?.stateId ?? null
  const toState = toNode.data?.stateId ?? null

  edges.value = [...edges.value, {
    id: `edge-${edgeSeq++}`,
    source: params.source,
    target: params.target,
    label: '请选择操作',
    ...defaultEdgeOptions,
    data: { from: fromState, to: toState, action: null, actionName: '', needRemark: false, condition: null, formSchema: null }
  }]
}

// -------- 动作表单配置 --------
const showFormModal = ref(false)
function openFormDesigner() {
  if (selectedEdge.value) {
    showFormModal.value = true
  }
}

function handleFormConfirm(schema) {
  const edge = edges.value.find(e => e.id === selectedEdge.value?.id)
  if (edge) {
    edge.data.formSchema = schema
    message.success('表单配置已更新')
  }
}

// -------- 点击事件 --------
function onEdgeClick({ edge }) {
  selectedEdge.value = edge
  selectedNode.value = null
}
function onNodeClick({ node }) {
  selectedNode.value = node
  selectedEdge.value = null
}
function onPaneClick() {
  selectedEdge.value = null
  selectedNode.value = null
}

function onActionSelect(val) {
  const edge = edges.value.find(e => e.id === selectedEdge.value?.id)
  const action = presetActions.value.find(a => a.value === val)
  if (edge && action) {
    edge.data.actionName = action.label
    edge.label = action.label
    if (action.needRemark !== undefined) {
      edge.data.needRemark = action.needRemark
    }
  }
}

function removeSelectedEdge() {
  edges.value = edges.value.filter(e => e.id !== selectedEdge.value?.id)
  selectedEdge.value = null
}

function removeSelectedNode() {
  const nodeId = selectedNode.value?.id
  nodes.value = nodes.value.filter(n => n.id !== nodeId)
  edges.value = edges.value.filter(e => e.source !== nodeId && e.target !== nodeId)
  selectedNode.value = null
}

function toggleTerminate(stateId) {
  const arr = config.value.allowTerminateFrom || []
  const idx = arr.indexOf(stateId)
  if (idx > -1) arr.splice(idx, 1)
  else arr.push(stateId)
}

function clearAll() {
  nodes.value = []
  edges.value = []
  setNodes([])
  setEdges([])
  config.value = { transitions: [], allowTerminateFrom: [] }
  selectedEdge.value = null
  selectedNode.value = null
}

// -------- 预设配置 --------
function applyConfig(cfg, msg) {
  config.value = JSON.parse(JSON.stringify(cfg))
  applyToCanvas(cfg)
  message.success(msg)
}

function loadDefaultConfig() {
  applyConfig({
    transitions: [
      { from: 10, to: 20, action: 'submit', actionName: '提交', needRemark: false, condition: null },
      { from: 20, to: 30, action: 'auditPass', actionName: '审核通过', needRemark: false, condition: null },
      { from: 20, to: 50, action: 'auditReject', actionName: '审核驳回', needRemark: true, condition: null },
      { from: 50, to: 20, action: 'resubmit', actionName: '重新提交', needRemark: false, condition: null },
      { from: 30, to: 33, action: 'submitLocal', actionName: '递交当地同事', needRemark: false, condition: null },
      { from: 33, to: 32, action: 'submitTax', actionName: '递交税局', needRemark: false, condition: 'VAT' },
      { from: 33, to: 31, action: 'submitOrg', actionName: '递交组织', needRemark: false, condition: 'EPR' },
      { from: 32, to: 40, action: 'complete', actionName: '完成', needRemark: false, condition: null },
      { from: 31, to: 40, action: 'complete', actionName: '完成', needRemark: false, condition: null }
    ],
    allowTerminateFrom: [10, 20, 30, 31, 32, 33, 50]
  }, '已加载默认流程')
}

function loadVatConfig() {
  applyConfig({
    transitions: [
      { from: 10, to: 20, action: 'submit', actionName: '提交', needRemark: false, condition: null },
      { from: 20, to: 30, action: 'auditPass', actionName: '审核通过', needRemark: false, condition: null },
      { from: 20, to: 50, action: 'auditReject', actionName: '审核驳回', needRemark: true, condition: null },
      { from: 50, to: 20, action: 'resubmit', actionName: '重新提交', needRemark: false, condition: null },
      { from: 30, to: 33, action: 'submitLocal', actionName: '递交当地同事', needRemark: false, condition: null },
      { from: 33, to: 32, action: 'submitTax', actionName: '递交税局', needRemark: false, condition: null },
      { from: 32, to: 40, action: 'complete', actionName: '完成', needRemark: false, condition: null }
    ],
    allowTerminateFrom: [10, 20, 30, 32, 33, 50]
  }, '已加载VAT流程')
}

function loadEprConfig() {
  applyConfig({
    transitions: [
      { from: 10, to: 20, action: 'submit', actionName: '提交', needRemark: false, condition: null },
      { from: 20, to: 30, action: 'auditPass', actionName: '审核通过', needRemark: false, condition: null },
      { from: 20, to: 50, action: 'auditReject', actionName: '审核驳回', needRemark: true, condition: null },
      { from: 50, to: 20, action: 'resubmit', actionName: '重新提交', needRemark: false, condition: null },
      { from: 30, to: 33, action: 'submitLocal', actionName: '递交当地同事', needRemark: false, condition: null },
      { from: 33, to: 31, action: 'submitOrg', actionName: '递交组织', needRemark: false, condition: null },
      { from: 31, to: 40, action: 'complete', actionName: '完成', needRemark: false, condition: null }
    ],
    allowTerminateFrom: [10, 20, 30, 31, 33, 50]
  }, '已加载EPR流程')
}
</script>

<style>
/* 全局覆盖 VueFlow 样式，不能 scoped */
.vue-flow__edge-text {
  font-size: 11px;
  fill: #555;
}
.vue-flow__edge.selected .vue-flow__edge-path,
.vue-flow__edge:hover .vue-flow__edge-path {
  stroke: #18a058 !important;
  stroke-width: 2px !important;
}
.vue-flow__controls {
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  border-radius: 8px;
  overflow: hidden;
}
.vue-flow__controls-button {
  background: #fff;
  border: none;
  border-bottom: 1px solid #f0f0f0;
}
.vue-flow__controls-button:hover {
  background: #f6ffed;
}
</style>

<style scoped>
.workflow-config {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #fff;
}

/* 工具栏 */
.config-toolbar {
  padding: 10px 16px;
  border-bottom: 1px solid #eee;
  background: #fff;
  flex-shrink: 0;
}
.toolbar-section {
  font-size: 13px;
  color: #666;
  white-space: nowrap;
}
.toolbar-tip {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #bbb;
  white-space: nowrap;
}

/* 主体布局 */
.designer-layout {
  display: flex;
  flex: 1;
  overflow: hidden;
}

/* 左侧节点库 */
.node-palette {
  width: 120px;
  flex-shrink: 0;
  background: #fafafa;
  border-right: 1px solid #eee;
  padding: 12px 8px;
  overflow-y: auto;
}
.palette-section {
  font-size: 11px;
  font-weight: 600;
  color: #999;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 8px;
  padding: 0 4px;
}
.palette-item {
  display: flex;
  align-items: center;
  gap: 7px;
  padding: 7px 10px;
  margin-bottom: 5px;
  background: #fff;
  border-radius: 6px;
  border: 1px solid #e8e8e8;
  cursor: grab;
  font-size: 12px;
  color: #333;
  transition: all 0.15s;
  user-select: none;
}
.palette-item:hover {
  border-color: #18a058;
  color: #18a058;
  box-shadow: 0 2px 6px rgba(24,160,88,0.15);
}
.palette-item:active { cursor: grabbing; transform: scale(0.96); }
.palette-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--node-color, #909399);
  flex-shrink: 0;
}

/* 画布 */
.flow-container {
  flex: 1;
  position: relative;
  background: #fafbfc;
}
.canvas-empty {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #ccc;
  font-size: 14px;
  pointer-events: none;
}

/* 状态节点 */
.flow-node-state {
  background: #fff;
  border: 1.5px solid var(--c, #ddd);
  border-radius: 8px;
  width: 140px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
  transition: box-shadow 0.15s, border-color 0.15s;
}
.flow-node-state.selected,
.flow-node-state:hover {
  box-shadow: 0 0 0 2px var(--c, #18a058), 0 2px 8px rgba(0,0,0,0.12);
}
.flow-node-body {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
}
.flow-node-icon {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}
.flow-node-label {
  font-size: 13px;
  color: #222;
  font-weight: 500;
}
.flow-node-tag {
  font-size: 10px;
  color: #f0a020;
  padding: 2px 8px 4px;
  border-top: 1px dashed #f0e0c0;
  text-align: center;
  background: #fffbe6;
  border-radius: 0 0 6px 6px;
}

/* Handle 连接点 */
.fh {
  width: 12px !important;
  height: 12px !important;
  background: #18a058 !important;
  border: 2px solid #fff !important;
  border-radius: 50% !important;
  opacity: 0;
  transition: opacity 0.15s;
}
.flow-node-state:hover .fh {
  opacity: 1;
}
.fh-top { top: -7px !important; }
.fh-bottom { bottom: -7px !important; }
.fh-left { left: -7px !important; }
.fh-right { right: -7px !important; }

/* 右侧属性面板 */
.property-panel {
  width: 220px;
  flex-shrink: 0;
  background: #fff;
  border-left: 1px solid #eee;
  padding: 16px 14px;
  overflow-y: auto;
}
.panel-header {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
  font-weight: 600;
  color: #333;
  margin-bottom: 16px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}
.panel-form :deep(.n-form-item) {
  margin-bottom: 12px;
}
.form-tag {
  font-size: 11px;
  color: #18a058;
  margin-top: 4px;
  text-align: center;
}
.panel-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 48px 0;
  color: #bbb;
  font-size: 13px;
  text-align: center;
  line-height: 1.6;
}

.json-preview {
  font-family: 'Consolas', 'Monaco', monospace;
  font-size: 13px;
  line-height: 1.6;
  color: #333;
  background: #f8f8f8;
  padding: 16px;
  border-radius: 6px;
  white-space: pre-wrap;
  word-break: break-all;
  margin: 0;
}
</style>
