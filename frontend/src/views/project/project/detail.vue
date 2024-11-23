<template>
  <div class="project-detail-container">

    <div class="top-navbar">
      <el-menu :default-active="setC" class="project-menu" mode="horizontal" @select="doTrans"
      >
        <el-menu-item index="overview">
          <el-icon><DataBoard /></el-icon>
          <span>overview</span>
        </el-menu-item>

        <el-menu-item index="plan">
          <el-icon><Calendar /></el-icon>
<!--          <span>项目规划</span>-->
          <span>project plan</span>
        </el-menu-item>

        <el-menu-item index="requirements">
          <el-icon><List /></el-icon>
          <span>requirements</span>
        </el-menu-item>

        <el-menu-item index="iterations">
          <el-icon><Refresh /></el-icon>
          <span>iterations</span>
        </el-menu-item>

        <el-menu-item index="tasks">
          <el-icon><Tickets /></el-icon>
          <span>tasks</span>
        </el-menu-item>
        <el-menu-item index="defects">
          <el-icon><Warning /></el-icon>
          <span>defects</span>
        </el-menu-item>





        <el-menu-item index="releases">
          <el-icon><Upload /></el-icon>
          <span>jobs</span>
        </el-menu-item>

<!--        <el-menu-item index="baseline">-->
<!--          <el-icon><Files /></el-icon>-->
<!--          <span>基线管理</span>-->
<!--        </el-menu-item>-->

<!--        <el-menu-item index="reviews">-->
<!--          <el-icon><ChatDotRound /></el-icon>-->
<!--          <span>评审管理</span>-->
<!--        </el-menu-item>-->

<!--        <el-menu-item index="testing">-->
<!--          <el-icon><Histogram /></el-icon>-->
<!--          <span>测试管理</span>-->
<!--        </el-menu-item>-->

<!--        <el-menu-item index="documents">-->
<!--          <el-icon><Document /></el-icon>-->
<!--          <span>文档管理</span>-->
<!--        </el-menu-item>-->

        <el-menu-item index="resources">
          <el-icon><Connection /></el-icon>
          <span>human resources</span>
        </el-menu-item>

        <el-menu-item index="reports">
          <el-icon><PieChart /></el-icon>
          <span>reports</span>
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