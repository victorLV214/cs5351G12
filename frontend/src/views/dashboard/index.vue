<template>
  <div class="dashboard" v-loading="loading">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="box-card"><template #header>
          <div class="card-header"><span>Project Overview</span></div></template>
          <div class="statistics"><div class="stat-item">
            <div class="label">Planned Projects</div><div class="value">{{ dashboardData.plannedProjects.length }}</div></div>
            <div class="stat-item"><div class="label">Ongoing Projects</div><div class="value">{{ dashboardData.ongoingProjects.length }}</div>
            </div>
            <div class="stat-item"><div class="label">Completed Projects</div><div class="value">{{ dashboardData.completedProjects.length }}</div></div></div>
        </el-card>
      </el-col>


      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header"><span>Ongoing Tasks</span><span class="task-count">Total: {{ dashboardData.ongoingTasks.length }}</span></div></template>
          <el-scrollbar height="300px"><div v-for="task in dashboardData.ongoingTasks" :key="task.taskId" class="task-item"><div class="task-title">{{ task.title }}</div>
              <div class="task-info"><el-tag size="small" :type="['danger', 'warning', 'info'][task.priority - 1] || ''">{{ ['High', 'Medium', 'Low'][task.priority - 1] || 'None' }}</el-tag><span class="due-date">Due: {{ task.dueDate }}</span></div></div></el-scrollbar>
        </el-card></el-col>


      <el-col :span="8">
        <el-card class="box-card"><template #header><div class="card-header"><span>Completed Tasks</span><span class="task-count">Total: {{ dashboardData.completedTasks.length }}</span></div></template><el-scrollbar height="300px"><div v-for="task in dashboardData.completedTasks" :key="task.taskId" class="task-item"><div class="task-title">{{ task.title }}</div><div class="task-info"><el-tag size="small" type="success">Completed</el-tag>
              <span class="complete-date">Completed: {{ task.completedAt }}</span></div></div></el-scrollbar></el-card>
      </el-col></el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardData } from '@/api/project/dashboard'

const loading = ref(true)
const dashboardData = ref({
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
    dashboardData.value = res.data
  }
  loading.value = false
}

onMounted(() => {
  getDataD()
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}
.box-card {
  margin-bottom: 20px;
}
.statistics {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.stat-item {
  text-align: center;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.stat-item .label {
  color: #606266;
  margin-bottom: 8px;
}

.stat-item .value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.task-item {
  padding: 12px;
  border-bottom: 1px solid #EBEEF5;
}

.task-title {
  margin-bottom: 8px;
  font-weight: bold;
}

.task-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #909399;
  font-size: 12px;
}
</style>