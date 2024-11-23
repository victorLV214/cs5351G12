<template>
  <div class="app-container">
    <el-card class="box-card">
      <div class="project-overview">
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-title">Project Progress</div>
              <el-progress
                  type="circle"
                  :percentage="projectData.completionPercentage"
                  :color="getProgressColor"
              />
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-title">Budget Execution</div>
              <div class="stat-content">
                <div class="budget-info">
                  <p>Total Budget: ${{ formatNumber(projectData.budget) }}</p>
                  <p>Actual Cost: ${{ formatNumber(projectData.actualCost) }}</p>
                </div>
                <el-progress
                    :percentage="getBudgetPercentage"
                    :color="getBudgetColor"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-card">
              <div class="stat-title">Task Completion</div>
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
              <div class="stat-title">
                Project Overview
              </div>
              <div class="project-info">
                <p><i class="el-icon-user"></i> Team Size: {{ projectData.memberCount }} members</p>
                <p><i class="el-icon-data-analysis"></i> Iterations: {{ projectData.iterationCount }}</p>
                <p><i class="el-icon-timer"></i> Start Date: {{ projectData.startDate }}</p>
                <p><i class="el-icon-timer"></i> Expected End: {{ projectData.expectedEndDate }}</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getProjectStatistics } from '@/api/project'

export default {
  name: 'ProjectReports',
  data() {
    return {
      projectData: {
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
      }
    }
  },
  computed: {
    getProgressColor() {
      return (percentage) => {
        if (percentage < 30) return '#ff4949'
        if (percentage < 70) return '#e6a23c'
        return '#67c23a'
      }
    },
    getBudgetPercentage() {
      return (this.projectData.actualCost / this.projectData.budget) * 100
    },
    getBudgetColor() {
      const percentage = this.getBudgetPercentage
      if (percentage > 100) return '#ff4949'
      if (percentage > 80) return '#e6a23c'
      return '#67c23a'
    }
  },
  created() {
    this.getProjectData()
  },
  methods: {
    async getProjectData() {
      try {
        // Assuming project ID is passed through route params
        const projectId = this.$route.params.id
        const response = await getProjectStatistics(projectId)
        if (response.code === 200) {
          this.projectData = response.data
        }
      } catch (error) {
        console.error('Failed to fetch project statistics:', error)
        this.$message.error('Failed to load project data')
      }
    },
    formatNumber(num) {
      return num.toLocaleString('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style scoped>
.project-overview {
  padding: 20px;
}

.stat-card {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  height: 100%;
}

.stat-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #303133;
}

.stat-content {
  text-align: center;
}

.budget-info {
  margin-bottom: 15px;
}

.budget-info p {
  margin: 5px 0;
  color: #606266;
}

.task-stats {
  text-align: center;
  margin-top: 20px;
}

.stat-item {
  padding: 10px;
}

.stat-number {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.project-info p {
  margin: 10px 0;
  color: #606266;
}

.project-info i {
  margin-right: 8px;
}
</style>