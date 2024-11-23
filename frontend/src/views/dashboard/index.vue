<template>
  <div class="dashboard" v-loading="loading">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>
                 <img src="@/assets/icons/png/overview.png" style="width: 25px;
                  vertical-align: middle; position: relative; top: -2px;">
                Project Overview
              </span>
            </div>
          </template>
          <div class="statistics"><div class="stat-item">
            <div class="label">Planned Projects</div><div class="value">{{ dDataD.plannedProjects.length }}</div></div>
            <div class="stat-item"><div class="label">Ongoing Projects</div><div class="value">{{ dDataD.ongoingProjects.length }}</div>
            </div>
            <div class="stat-item"><div class="label">Completed Projects</div><div class="value">{{ dDataD.completedProjects.length }}</div></div></div>
        </el-card>
      </el-col>


      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>
                <img src="@/assets/icons/png/running.png" style="width: 25px;
                  vertical-align: middle; position: relative; top: -2px;">
                Ongoing Tasks
              </span>
              <span class="task-count">Total: {{ dDataD.ongoingTasks.length }}</span>
            </div>
          </template>
          <el-scrollbar height="300px"><div v-for="task in dDataD.ongoingTasks" :key="task.taskId" class="task-item"><div class="task-title">{{ task.title }}</div>
              <div class="task-info"><el-tag size="small" :type="['danger', 'warning', 'info'][task.priority - 1] || ''">{{ ['High', 'Medium', 'Low'][task.priority - 1] || 'None' }}</el-tag><span class="due-date">Due: {{ task.dueDate }}</span></div></div></el-scrollbar>
        </el-card></el-col>


      <el-col :span="8">
        <el-card class="box-card"><template #header>
          <div class="card-header">
            <span>
               <img src="@/assets/icons/png/completed.png" style="width: 25px;
                  vertical-align: middle; position: relative; top: -2px;">
              Completed Tasks
            </span>
            <span class="task-count">Total: {{ dDataD.completedTasks.length }}</span></div></template><el-scrollbar height="300px"><div v-for="task in dDataD.completedTasks" :key="task.taskId" class="task-item"><div class="task-title">{{ task.title }}</div><div class="task-info"><el-tag size="small" type="success">Completed</el-tag>
              <span class="complete-date">Completed: {{ task.completedAt }}</span></div></div></el-scrollbar></el-card>
      </el-col></el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardData } from '@/api/project/dashboard'

const loading = ref(true)
const dDataD = ref({
  plannedProjects: [],
  ongoingProjects: [],
  completedProjects: [],
  ongoingTasks: [],
  completedTasks: []
})

const getDataD = async () => {
  loading.value = true
  const res = await getDashboardData()
  if (res.code === 200) {
    dDataD.value = res.data
  }
  loading.value = false
}

onMounted(() => {
  getDataD()
})
</script>

<style scoped>
@import "dcss.scss";
</style>