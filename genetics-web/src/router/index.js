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
    path: '/template/designer/:id?',
    name: 'TemplateDesigner',
    component: () => import('@/views/template/TemplateDesigner.vue'),
    meta: { title: '模板设计器' }
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
