<template>
  <n-config-provider :theme-overrides="themeOverrides">
    <n-message-provider>
      <n-dialog-provider>
        <n-notification-provider>
          <n-layout has-sider class="app-layout">
            <n-layout-sider
              bordered
              collapse-mode="width"
              :collapsed-width="64"
              :width="220"
              :native-scrollbar="false"
              show-trigger
              @collapse="collapsed = true"
              @expand="collapsed = false"
            >
              <div class="logo">
                <n-icon :size="24" color="#18a058">
                  <DocumentTextOutline />
                </n-icon>
                <span v-show="!collapsed" class="logo-text">VAT & EPR 表单系统</span>
              </div>
              <n-menu
                :collapsed="collapsed"
                :collapsed-width="64"
                :collapsed-icon-size="22"
                :options="menuOptions"
                :value="activeKey"
                @update:value="handleMenuSelect"
              />
            </n-layout-sider>
            <n-layout>
              <n-layout-header bordered class="header">
                <span class="page-title">{{ currentTitle }}</span>
              </n-layout-header>
              <n-layout-content class="main-content">
                <router-view />
              </n-layout-content>
            </n-layout>
          </n-layout>
        </n-notification-provider>
      </n-dialog-provider>
    </n-message-provider>
  </n-config-provider>
</template>

<script setup>
import { h, computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  NConfigProvider,
  NLayout,
  NLayoutSider,
  NLayoutHeader,
  NLayoutContent,
  NMenu,
  NIcon,
  NMessageProvider,
  NDialogProvider,
  NNotificationProvider
} from 'naive-ui'
import {
  DocumentTextOutline,
  GridOutline,
  SettingsOutline
} from '@vicons/ionicons5'

const route = useRoute()
const router = useRouter()
const collapsed = ref(false)

const activeKey = computed(() => {
  const path = route.path
  if (path.startsWith('/instance')) return 'instance'
  if (path.startsWith('/template')) return 'template'
  if (path.startsWith('/control')) return 'control'
  return 'instance'
})

const currentTitle = computed(() => route.meta?.title || 'VAT & EPR 动态表单系统')

function renderIcon(icon) {
  return () => h(NIcon, null, { default: () => h(icon) })
}

const menuOptions = [
  {
    label: '服务单管理',
    key: 'instance',
    icon: renderIcon(DocumentTextOutline)
  },
  {
    label: '模板管理',
    key: 'template',
    icon: renderIcon(GridOutline)
  },
  {
    label: '控件管理',
    key: 'control',
    icon: renderIcon(SettingsOutline)
  }
]

function handleMenuSelect(key) {
  router.push(`/${key}`)
}

const themeOverrides = {
  common: {
    primaryColor: '#18a058',
    primaryColorHover: '#36ad6a',
    primaryColorPressed: '#0c7a43',
    primaryColorSuppl: '#36ad6a'
  },
  Menu: {
    itemTextColorActive: '#18a058',
    itemTextColorActiveHover: '#36ad6a',
    itemIconColorActive: '#18a058',
    arrowColorActive: '#18a058'
  }
}
</script>

<style>
* { box-sizing: border-box; margin: 0; padding: 0; }
html, body, #app { height: 100%; }

.app-layout {
  height: 100vh;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 0 16px;
  border-bottom: 1px solid #e0e0e6;
}

.logo-text {
  font-size: 15px;
  font-weight: 600;
  color: #18a058;
  white-space: nowrap;
}

.header {
  height: 60px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  background: #fff;
}

.page-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.main-content {
  padding: 24px;
  background: #f5f7f9;
  min-height: calc(100vh - 60px);
}
</style>
