<template>
  <el-container class="app-container">
    <el-aside width="200px" class="sidebar">
      <div class="logo">
        <span>VAT & EPR 表单系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/instance">
          <el-icon><Document /></el-icon>
          <span>服务单管理</span>
        </el-menu-item>
        <el-menu-item index="/template">
          <el-icon><Grid /></el-icon>
          <span>模板管理</span>
        </el-menu-item>
        <el-menu-item index="/control">
          <el-icon><Setting /></el-icon>
          <span>控件管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="page-title">{{ currentTitle }}</span>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta?.title || 'VAT & EPR 动态表单系统')
</script>

<style>
* { box-sizing: border-box; margin: 0; padding: 0; }
html, body, #app { height: 100%; }
.app-container { height: 100vh; }
.sidebar { background: #304156; }
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 14px;
  font-weight: bold;
  border-bottom: 1px solid #3a4d65;
  padding: 0 12px;
  text-align: center;
}
.header {
  background: #fff;
  border-bottom: 1px solid #eee;
  display: flex;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}
.page-title { font-size: 16px; font-weight: bold; color: #333; }
.main { background: #f5f7fa; padding: 20px; }
</style>
