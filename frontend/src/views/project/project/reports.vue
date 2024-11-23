<template>
  <div class="app-container">
    <el-card class="box-card">
      <div class="project-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-title">completionPercentage</div>
              <el-progress type="circle" :percentage="projectData.completionPercentage" :color="setColor"/></div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-title">BudgetExecution</div>
              <div class="stat-content">
                <div class="budget-info"><p>TotalBudget: ${{ formatNumber(projectData.budget) }}</p><p>Actual Cost: ${{ formatNumber(projectData.actualCost) }}</p>
                </div><el-progress :percentage="calPerc" :color="setBcolor"/></div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-title">TaskCompletion</div>
              <el-row class="task-stats">
                <el-col :span="8">
                  <div class="stat-item">
                    <div class="stat-number">{{ projectData.completedTasks }}/{{ projectData.totalTasks }}</div>
                    <div class="stat-label">Tasks</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="stat-item">
                    <div class="stat-number">{{ projectData.completedRequirements }}/{{ projectData.totalRequirements }}</div>
                    <div class="stat-label">Requirements</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="stat-item">
                    <div class="stat-number">{{ projectData.completedDefects }}/{{ projectData.totalDefects }}</div>
                    <div class="stat-label">Defects</div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-title">Project-Overview</div>
              <div class="project-info">
                <p><i class="el-icon-user"></i> memberCount: {{ projectData.memberCount }} members</p>
                <p><i class="el-icon-data-analysis"></i> iterationCount: {{ projectData.iterationCount }}</p>
                <p><i class="el-icon-timer"></i> startDate: {{ projectData.startDate }}</p>
                <p><i class="el-icon-timer"></i> expectedEndDate: {{ projectData.expectedEndDate }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>
<script setup>


import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getProjectStatistics } from '@/api/project'

const route = useRoute()

const projectData = ref({
  completionPercentage: 0,
  budget: 0,
  actualCost: 0,
  totalTasks: 0,
  completedTasks: 0,
  totalRequirements: 0,
  completedRequirements: 0,
  totalDefects: 0,
  completedDefects: 0,
  memberCount: 0,
  iterationCount: 0,
  startDate: '',
  expectedEndDate: ''
})

const setColor = (percentage) => {
  if (percentage < 30) return '#ff4949'
  if (percentage < 70) return '#e6a23c'
  return '#67c23a'
}

const calPerc = computed(() =>
    (projectData.value.actualCost / projectData.value.budget) * 100
)



const setBcolor = computed(() => {
  const percentage = calPerc.value
  switch (true) {
    case percentage > 100: return '#ff4949'
    case percentage > 80: return '#e6a23c'
    default: return '#67c23a'
  }
})

const getAll = async () => {
    const projectId = route.params.id
    const response = await getProjectStatistics(projectId)
    projectData.value = response.data

}

const formatNumber = (num) => {
  return num.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

onMounted(() => {
  getAll()
})
</script>

<style scoped>
@import './scss/report.scss';
</style>