<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :xs="24" :sm="4" :md="4" :lg="4.8" :xl="4.8" v-for="(item, index) in projectDaat" :key="index">
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
import {listProjectMember} from "@/api/project/member.js";
import useUserStore from "@/store/modules/user.js";
import {listUser} from "@/api/system/user.js";
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


const getStatistics = async () => {
    const allList = await listProject({
      pageNum: 1,
      pageSize: 999
    })
      const projectList = allList.rows
      const ongoingProjects = projectList.filter(p => p.status === '进行中').length
      const planProjects = projectList.filter(p => p.status === '规划中').length
      const completedProjects = projectList.filter(p => p.status === '已完成').length
  const allTasks = await listItem({
    pageNum: 1,
    pageSize: 999
  })

  const unfinishedTasks = allTasks.rows.filter(task => task.status !== '已完成').length
  const nowDate = new Date()
  const now = nowDate.getTime()
  const oneWeek = new Date(now - 7 * 24 * 3600 * 1000)

  const nearlyTasks = allTasks.rows.filter(task => {
    if (task.status === '已完成') return false
    const deadline = new Date(task.due_date)
    return deadline <= oneWeek && deadline >= now
  }).length

  projectDaat.value = [
    { title: '进行中的项目', value: ongoingProjects }, { title: '规划中项目', value: planProjects }, { title: '已完成项目', value: completedProjects },
    { title: '未完成任务', value: unfinishedTasks },{ title: '近期任务', value: nearlyTasks }
  ]
  projects.value = projectList
}

const getTeamActivityData = async () => {
  try {
    const userStore = useUserStore()
    const userId = userStore.id
    
    const allProjects = await listProjectMember({
      userId: userId,
      pageSize: 999,
      pageNum: 1
    })

    const projectIds = allProjects.rows.map(item => item.projectId)

    const allMember = await listProjectMember({
      projectIds: projectIds,
      pageSize: 999,
      pageNum: 1
    })
    const teamMemberIds = new Set()
    allMember.rows.forEach(member => {teamMemberIds.add(member.userId)})
    teamMemberIds.add(userId)


    const allUsers = await listUser({
      pageSize: 999,
      pageNum: 1
    })

    const userNameMap = {}
    allUsers.rows.forEach(user => {
        if (teamMemberIds.has(user.userId)) {
          userNameMap[user.userId] = user.nickName}
      })
    
    const thisMonth = new Date()
    thisMonth.setDate(thisMonth.getDate() - 30)
    const allTasks = await listItem({
      status: '已完成',
      projectIds: projectIds,
      pageNum: 1,
      pageSize: 999
    })
    
    const memberStats = {}
    allTasks.rows.forEach(task => {
      const completionDate = new Date(task.completedDate || task.updateTime)
      if (completionDate >= thisMonth &&
          task.status === '已完成' &&
          task.assignedTo &&
          teamMemberIds.has(task.assignedTo)) {
        const assignedNames = userNameMap[task.assignedTo] || `用户${task.assignedTo}`
        memberStats[assignedNames] = (memberStats[assignedNames] || 0) + 1
      }
    })

    const currentUserName = userNameMap[userId]
    if (!memberStats[currentUserName]) {
      memberStats[currentUserName] = 0
    }


    const members = Object.keys(memberStats)
    const activities = members.map(member => ({
      value: memberStats[member],
      itemStyle: {
        color: member === currentUserName ? '#409EFF' : getRandomColor()
      }
    }))

    if (activeChart1) {
      activeChart1.setOption({
        title: {
          text: '近30天团队成员任务完成情况',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          formatter: (params) => {
            const data = params[0]
            return `${data.name}<br/>完成任务数：${data.value}`
          }
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
            rotate: 30,
            overflow: 'break'
          }
        },
        yAxis: {
          type: 'value',
          name: '完成任务数',
          minInterval: 1
        },
        series: [{
          type: 'bar',
          data: activities,
          label: {
            show: true,
            position: 'top',
            formatter: '{c}'
          },
          barWidth: '40%'
        }]
      })
    }
  } catch (error) {
    ElMessage.error('获取团队活跃度数据失败')
    console.error('获取团队活跃度数据失败:', error)
  }
}


const getWeeklyTasksData = async () => {
  try {
    const now = new Date()
    const startDay1 = new Date(now)
    startDay1.setDate(now.getDate() - now.getDay() + 1)
    startDay1.setHours(0, 0, 0, 0)

    const allList = await listItem({
      status: '已完成',
      pageNum: 1,
      pageSize: 999
    })
  console.log(allList)
    if (allList.code === 200) {
      const weekDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      const dailyTasks = new Array(7).fill(0)

      allList.rows.forEach(item => {
        if (item.updateTime) {
          const completedDate = new Date(item.updateTime)
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

const calculateProgress = async (project) => {
  try {
    // 获取项目所有任务
    const allTasksforCalc = await listItem({
      projectId: project.projectId,
      pageNum: 1,
      pageSize: 999
    })

    const allTasks = allTasksforCalc.rows
    const completedTasks = allTasks.filter(task => task.status === '已完成')
    const progress = Math.round((completedTasks.length / allTasks.length) * 100)
    return progress
  } catch (error) {
    console.error('计算项目进度失败:', error)
    return 0
  }
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
    const userStore = useUserStore()
    const userId = userStore.id
    const userProjectforV = await listProjectMember({
      userId: userId,
      pageSize: 999,
      pageNum: 1
    })
    const projectIds = userProjectforV.rows.map(item => item.projectId)

    const allProjects = await listProject({
      projectIds: projectIds,
      pageNum: 1,
      pageSize: 999
    })


    if (allProjects.code === 200 && allProjects.rows) {
      const projectList = allProjects.rows
      const names = projectList.map(p => p.projectName)
      const progressPromises = projectList.map(p => calculateProgress(p))
      const progress = await Promise.all(progressPromises)

      if (projectChart) {
        projectChart.setOption({
          title: {
            text: '参与项目进度',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            formatter: (params) => {
              const project = projectList[params[0].dataIndex]
              return `${project.projectName}<br/>完成进度：${params[0].value}%`
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '15%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: names,
            axisLabel: {
              interval: 0,
              rotate: 30,
              overflow: 'break'
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
            data: progress.map(value => ({
              value: value,
              itemStyle: {
                color: value < 30 ? '#FF4949' :
                    value < 70 ? '#FFA500' : '#67C23A'
              }
            })),
            label: {
              show: true,
              position: 'top',
              formatter: '{c}%'
            },
            barWidth: '40%'
          }]
        })
      }
    }
  } catch (error) {
    ElMessage.error('获取项目进度数据失败')
    console.error('获取项目进度数据失败:', error)
  }
}

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