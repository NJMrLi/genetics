import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/instance'
  },
  {
    path: '/control',
    name: 'ControlList',
    component: () => import('@/views/control/ControlList.vue'),
    meta: { title: '控件管理' }
  },
  {
    path: '/template',
    name: 'TemplateList',
    component: () => import('@/views/template/TemplateList.vue'),
    meta: { title: '服务单模板' }
  },
  {
    path: '/template/create',
    name: 'TemplateCreate',
    component: () => import('@/views/template/TemplateCreate.vue'),
    meta: { title: '新建模板' }
  },
  {
    path: '/template/form-designer/:id',
    name: 'FormDesigner',
    component: () => import('@/views/template/TemplateDesigner.vue'),
    meta: { title: '表单配置' }
  },
  {
    path: '/template/workflow-designer/:id',
    name: 'WorkflowDesigner',
    component: () => import('@/views/template/WorkflowDesigner.vue'),
    meta: { title: '流程配置' }
  },
  {
    path: '/instance',
    name: 'InstanceList',
    component: () => import('@/views/instance/InstanceList.vue'),
    meta: { title: '服务单管理' }
  },
  {
    path: '/instance/form/:id',
    name: 'InstanceForm',
    component: () => import('@/views/instance/InstanceForm.vue'),
    meta: { title: '填写服务单' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
