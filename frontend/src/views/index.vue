<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8" v-for="(item, index) in projectDaat" :key="index">
        <el-card class="box-card">
          <div class="card-header">
            <span>{{ item.title }}</span>
          </div>
          <div class="statistic-value">{{ item.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>Team member activity level</span>
            </div>
          </template>
          <div ref="teamActivityChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>Complete task statistics this week</span>
            </div>
          </template>
          <div ref="weeklyTasksChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>Project progress visualization</span>
            </div>
          </template>
          <div ref="projectProgressChartRef" style="height: 300px;"></div>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>Recent Project Overview</span>
            </div>
          </template>
          <div v-for="project in recentProjects" :key="project.id" class="project-item">
            <h3>{{ project.projectName }}</h3>
            <el-progress :percentage="calculateProgress(project)" :format="percentageFormat" />
            <div class="project-metrics">
              <span>status: {{ project.status }}</span>
              <span>startDate: {{ formatDate(project.startDate) }}</span>
              <span>actualEndDate: {{ formatDate(project.actualEndDate) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script setup name="Index">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { listItem } from '@/api/project/item'
import { listProject } from '@/api/project/index'
// 图表实例
let activeChart1 = null
let taskChart1 = null
let projectChart = null
// 初始化ref
const projectDaat = ref([])
const projects = ref([])
const teamActivityChartRef = ref(null)
const weeklyTasksChartRef = ref(null)
const projectProgressChartRef = ref(null)
// 计算最近5个项目
const recentProjects = computed(function() {
  let result = projects.value.slice() // 创建数组副本
  result.sort(function(a, b) {
    return new Date(b.startDate) - new Date(a.startDate)
  })
  return result.slice(0, 5)
})

// 获取统计数据
const getStatistics = async () => {
    const response = await listProject({
      pageNum: 1,
      pageSize: 999
    })
      const projectList = response.rows
      const ongoingProjects = projectList.filter(p => p.status === '进行中').length
      const planProjects = projectList.filter(p => p.status === '规划中').length
      const completedProjects = projectList.filter(p => p.status === '已完成').length
      projectDaat.value = [
        { title: '进行中的项目', value: ongoingProjects }, { title: '规划中项目', value: planProjects }, { title: '已完成项目', value: completedProjects }
      ]
      projects.value = projectList


}

// 获取团队成员活跃度数据
const getTeamActivityData = async () => {
  try {
    const response = await listItem({
      status: '已完成',
      pageNum: 1,
      pageSize: 999
    })

    if (response.code === 200 && response.rows) {
      const memberStats = {}
      response.rows.forEach(item => {
        const memberName = item.updateBy || 'null'
        memberStats[memberName] = (memberStats[memberName] || 0) + 1
      })
      const members = Object.keys(memberStats)

      const activities = members.map(member => ({
        value: memberStats[member],
        itemStyle: {
          color: getRandomColor()
        }
      }))

      if (activeChart1) {
        activeChart1.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '15%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: members,
            axisLabel: {
              interval: 0,
              rotate: 30
            }
          },
          yAxis: {
            type: 'value',
            name: '完成任务数'
          },
          series: [{
            type: 'bar',
            data: activities,
            label: {
              show: true,
              position: 'top'
            },
            barWidth: '40%'
          }]
        })
      }
    }
  } catch (error) {
    ElMessage.error('获取团队活跃度数据失败')
    console.error('获取团队活跃度数据失败:', error)
  }
}

// 获取本周任务统计
const getWeeklyTasksData = async () => {
  try {
    const now = new Date()
    const startDay1 = new Date(now)
    startDay1.setDate(now.getDate() - now.getDay() + 1)
    startDay1.setHours(0, 0, 0, 0)

    const response = await listItem({
      status: '已完成',
      pageNum: 1,
      pageSize: 999
    })

    if (response.code === 200 && response.rows) {
      const weekDays = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      const dailyTasks = new Array(7).fill(0)

      response.rows.forEach(item => {
        if (item.completedDate) {
          const completedDate = new Date(item.completedDate)
          if (completedDate >= startDay1) {
            const dayIndex = completedDate.getDay() || 7
            dailyTasks[dayIndex - 1]++
          }
        }
      })

      if (taskChart1) {
        taskChart1.setOption({
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: weekDays
          },
          yAxis: {
            type: 'value',
            name: '完成任务数'
          },
          series: [{
            data: dailyTasks,
            type: 'bar',
            label: {
              show: true,
              position: 'top'
            },
            itemStyle: {
              color: '#409EFF'
            }
          }]
        })
      }
    }
  } catch (error) {
    ElMessage.error('获取周任务统计失败')
    console.error('获取周任务统计失败:', error)
  }
}

// 工具函数
const getRandomColor = () => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399']
  return colors[Math.floor(Math.random() * colors.length)]
}

const calculateProgress = (project) => {
  if (!project.startDate || !project.actualEndDate) return 0

  const start = new Date(project.startDate)
  const end = new Date(project.actualEndDate)
  const now = new Date()

  if (project.status === 'completed') return 100
  if (now < start) return 0

  const total = end - start
  const current = now - start
  return Math.min(Math.round((current / total) * 100), 100)
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString()
}

const percentageFormat = (percentage) => {
  return percentage + '%'
}

// 初始化图表
const initCharts = () => {
  activeChart1 = echarts.init(teamActivityChartRef.value)
  taskChart1 = echarts.init(weeklyTasksChartRef.value)
  projectChart = echarts.init(projectProgressChartRef.value)

  getTeamActivityData()
  getWeeklyTasksData()
  getProjectProgressData()
}

// 获取项目进度数据
const getProjectProgressData = async () => {
  try {
    const response = await listProject({
      pageNum: 1,
      pageSize: 999
    })

    if (response.code === 200 && response.rows) {
      const projectList = response.rows
      const names = projectList.map(p => p.projectName)
      const progress = projectList.map(p => calculateProgress(p))

      if (projectChart) {
        projectChart.setOption({
          tooltip: {
            trigger: 'axis',
            formatter: '{b}: {c}%'
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: names,
            axisLabel: {
              interval: 0,
              rotate: 30
            }
          },
          yAxis: {
            type: 'value',
            name: '进度',
            min: 0,
            max: 100,
            interval: 20,
            axisLabel: {
              formatter: '{value}%'
            }
          },
          series: [{
            type: 'bar',
            data: progress,
            label: {
              show: true,
              position: 'top',
              formatter: '{c}%'
            },
            itemStyle: {
              color: function(params) {
                const value = params.value
                if (value < 30) return '#FF4949'
                if (value < 70) return '#FFA500'
                return '#67C23A'
              }
            }
          }]
        })
      }
    }
  } catch (error) {
    ElMessage.error('获取项目进度数据失败')
    console.error('获取项目进度数据失败:', error)
  }
}

// 窗口大小变化处理
const handleResize = () => {
  activeChart1?.resize()
  taskChart1?.resize()
  projectChart?.resize()
}

onMounted(() => {
  getStatistics()
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  activeChart1?.dispose()
  taskChart1?.dispose()
  projectChart?.dispose()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped lang="scss">
.home {
  .box-card {
    margin-bottom: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .statistic-value {
    font-size: 24px;
    font-weight: bold;
    color: #409EFF;
    text-align: center;
    margin-top: 10px;
  }

  .project-item {
    margin-bottom: 20px;
    padding-bottom: 20px;
    border-bottom: 1px solid #eee;

    &:last-child {
      border-bottom: none;
      margin-bottom: 0;
      padding-bottom: 0;
    }

    h3 {
      margin: 0 0 15px 0;
      font-size: 16px;
    }

    .project-metrics {
      display: flex;
      justify-content: space-between;
      margin-top: 10px;
      flex-wrap: wrap;
      gap: 10px;

      span {
        font-size: 14px;
        color: #606266;
      }
    }
  }
}
</style>