
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
              inverted
              @collapse="collapsed = true"
              @expand="collapsed = false"
            >
              <div class="logo">
                <n-icon :size="28" color="#18a058">
                  <DocumentTextOutline />
                </n-icon>
                <span v-show="!collapsed" class="logo-text">Genetics VAT</span>
              </div>
              <n-menu
                :collapsed="collapsed"
                :collapsed-width="64"
                :collapsed-icon-size="22"
                :options="menuOptions"
                :value="activeKey"
                inverted
                @update:value="handleMenuSelect"
              />
              <div class="sider-footer" v-show="!collapsed">
                <n-text depth="3" style="font-size: 12px">v1.0.0 © 2026 Genetics</n-text>
              </div>
            </n-layout-sider>
            <n-layout>
              <n-layout-header bordered class="header">
                <div class="header-left">
                  <n-breadcrumb>
                    <n-breadcrumb-item>首页</n-breadcrumb-item>
                    <n-breadcrumb-item>{{ currentTitle }}</n-breadcrumb-item>
                  </n-breadcrumb>
                </div>
                <div class="header-right">
                  <n-space align="center" :size="20">
                    <n-tooltip trigger="hover">
                      <template #trigger>
                        <n-button quaternary circle>
                          <template #icon><n-icon><NotificationsOutline /></n-icon></template>
                        </n-button>
                      </template>
                      通知
                    </n-tooltip>
                    <n-dropdown :options="userOptions" @select="handleUserSelect">
                      <div class="user-info">
                        <n-avatar round size="small" color="#18a058">Admin</n-avatar>
                        <span class="username">管理员</span>
                      </div>
                    </n-dropdown>
                  </n-space>
                </div>
              </n-layout-header>
              <n-layout-content class="main-content" :native-scrollbar="false">
                <div class="content-wrapper">
                  <router-view v-slot="{ Component }">
                    <transition name="fade" mode="out-in">
                      <component :is="Component" />
                    </transition>
                  </router-view>
                </div>
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
  NNotificationProvider,
  NBreadcrumb,
  NBreadcrumbItem,
  NSpace,
  NAvatar,
  NDropdown,
  NButton,
  NTooltip,
  NText
} from 'naive-ui'
import {
  DocumentTextOutline,
  GridOutline,
  SettingsOutline,
  NotificationsOutline,
  PersonOutline,
  LogOutOutline
} from '@vicons/ionicons5'
import { themeOverrides } from './theme'

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

const currentTitle = computed(() => route.meta?.title || '管理后台')

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

const userOptions = [
  {
    label: '个人信息',
    key: 'profile',
    icon: renderIcon(PersonOutline)
  },
  {
    label: '退出登录',
    key: 'logout',
    icon: renderIcon(LogOutOutline)
  }
]

function handleMenuSelect(key) {
  router.push(`/${key}`)
}

function handleUserSelect(key) {
  if (key === 'logout') {
    // router.push('/login')
  }
}
</script>

<style scoped>
.app-layout {
  height: 100vh;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  padding: 0 16px;
  gap: 12px;
  background: #002140;
}

.logo-text {
  font-size: 18px;
  font-weight: 700;
  color: #fff;
  white-space: nowrap;
  letter-spacing: 1px;
}

.header {
  height: 64px;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 10;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
  transition: background 0.2s;
}

.user-info:hover {
  background: #f3f3f5;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.main-content {
  background: #f5f7f9;
}

.content-wrapper {
  padding: 24px;
  min-height: calc(100vh - 64px);
}

.sider-footer {
  position: absolute;
  bottom: 24px;
  width: 100%;
  text-align: center;
  padding: 0 16px;
}

/* Page Transition */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
