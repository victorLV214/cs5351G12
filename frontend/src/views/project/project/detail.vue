<template>
  <div class="project-detail-container">

    <div class="top-navbar">
      <el-menu
          :default-active="setBarMenuChoice"
          class="project-menu"
          mode="horizontal"
          @select="handleSelect"
      >
        <el-menu-item index="overview">
          <el-icon><DataBoard /></el-icon>
          <span>项目概览</span>
        </el-menu-item>

        <el-menu-item index="plan">
          <el-icon><Calendar /></el-icon>
          <span>项目规划</span>
        </el-menu-item>

        <el-menu-item index="requirements">
          <el-icon><List /></el-icon>
          <span>需求管理</span>
        </el-menu-item>

        <el-menu-item index="defects">
          <el-icon><Warning /></el-icon>
          <span>缺陷管理</span>
        </el-menu-item>

        <el-menu-item index="tasks">
          <el-icon><Tickets /></el-icon>
          <span>工作项</span>
        </el-menu-item>

        <el-menu-item index="iterations">
          <el-icon><Refresh /></el-icon>
          <span>迭代管理</span>
        </el-menu-item>

        <el-menu-item index="releases">
          <el-icon><Upload /></el-icon>
          <span>发布管理</span>
        </el-menu-item>

        <el-menu-item index="baseline">
          <el-icon><Files /></el-icon>
          <span>基线管理</span>
        </el-menu-item>

        <el-menu-item index="reviews">
          <el-icon><ChatDotRound /></el-icon>
          <span>评审管理</span>
        </el-menu-item>

        <el-menu-item index="testing">
          <el-icon><Histogram /></el-icon>
          <span>测试管理</span>
        </el-menu-item>

        <el-menu-item index="documents">
          <el-icon><Document /></el-icon>
          <span>文档管理</span>
        </el-menu-item>

        <el-menu-item index="resources">
          <el-icon><Connection /></el-icon>
          <span>资源管理</span>
        </el-menu-item>

        <el-menu-item index="reports">
          <el-icon><PieChart /></el-icon>
          <span>统计报表</span>
        </el-menu-item>
      </el-menu>
    </div>

    <!-- 内容区 -->
    <div class="main-content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>

import { ref, onMounted } from 'vue'

import { useRoute, useRouter } from 'vue-router'

import {
  DataBoard,
  Calendar,
  List,
  Warning,
  Tickets,
  Refresh,
  Upload,
  Files,
  ChatDotRound,
  Histogram,
  Document,
  Connection,
  PieChart
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()


const setBarMenuChoice = ref('overview')
const handleSelect = (key) => {
  router.push(`/project/detail/${route.params.id}/${key}`)
}

onMounted(() => {
  const pathArray = route.path.split('/')
  setBarMenuChoice.value = pathArray[pathArray.length - 1] || 'overview'
})

</script>
<style scoped>
.project-detail-container {
  display: flex;
  flex-direction: column;
  height: calc(100vh - 84px);
  background-color: #f5f7fa;
}

.top-navbar {
  background-color: #fff;
  box-shadow: 0 2px 8px 0 rgba(29,35,41,.05);
}

.project-menu {
  display: flex;
  flex-wrap: nowrap;
  overflow-x: auto;
  border-bottom: none;
}

/* 隐藏水平滚动条但保持功能 */
.project-menu::-webkit-scrollbar {
  height: 0;
}

.main-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
}

:deep(.el-menu--horizontal) {
  border-bottom: none;
}

:deep(.el-menu--horizontal > .el-menu-item) {
  height: 50px;
  line-height: 50px;
  padding: 0 16px;
}

:deep(.el-menu-item .el-icon) {
  margin-right: 4px;
}

:deep(.el-menu--horizontal > .el-menu-item.is-active) {
  border-bottom: 2px solid var(--el-menu-active-color);
  background-color: #ecf5ff;
}


:deep(.el-menu--horizontal) {
  white-space: nowrap;
  flex-wrap: nowrap;
}

:deep(.el-menu--horizontal > .el-menu-item) {
  float: none;
  display: inline-flex;
  align-items: center;
}
</style>