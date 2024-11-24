<template>
  <div class="project-detail-container">

    <div class="top-navbar">
      <el-menu :default-active="setC" class="project-menu" mode="horizontal" @select="doTrans"
      >
        <el-menu-item index="overview">
          <el-icon><DataBoard /></el-icon>
          <span>Overview</span>
        </el-menu-item>

        <el-menu-item index="plan">
          <el-icon><Calendar/></el-icon>
          <span>Project Plan</span>
        </el-menu-item>

        <el-menu-item index="requirements">
          <el-icon><List /></el-icon>
          <span>Requirements</span>
        </el-menu-item>

        <el-menu-item index="iterations">
          <el-icon><Refresh /></el-icon>
          <span>Iterations</span>
        </el-menu-item>

        <el-menu-item index="tasks">
          <el-icon><Tickets /></el-icon>
          <span>Tasks</span>
        </el-menu-item>
        <el-menu-item index="defects">
          <el-icon><Warning /></el-icon>
          <span>Defects</span>
        </el-menu-item>

        <el-menu-item index="releases">
          <el-icon><Files/></el-icon>
          <span>Jobs</span>
        </el-menu-item>

        <el-menu-item index="resources">
          <el-icon><Avatar/></el-icon>
          <span>Human Resources</span>
        </el-menu-item>

        <el-menu-item index="reports">
          <el-icon><PieChart/></el-icon>
          <span>Reports</span>
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
  PieChart, AlarmClock, Avatar
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()


const setC = ref('overview')
const doTrans = (key) => {
  router.push(`/project/detail/${route.params.id}/${key}`)
}

onMounted(() => {
  const path1 = route.path.split('/')
  setC.value = path1[path1.length - 1] || 'overview'
})

</script>
<style scoped>
@import "./scss/detail.scss";
</style>