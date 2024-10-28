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
              <span>My Responsible Work Items</span>
            </div>
          </template>
          <div class="work-items-list">
            <el-table :data="myWorkItems" style="width: 100%">
              <el-table-column prop="title" label="title" />
              <el-table-column prop="dueDate" label="dueDate" width="120" />
              <el-table-column prop="status" label="status" width="100">
                <template #default="scope">
                  <el-tag :type="setStatusType(scope.row.status)">
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
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

let activeChart1 = null
let taskChart1 = null
let projectChart = null

const myWorkItems = ref([])
const projectDaat = ref([])
const projects = ref([])
const teamActivityChartRef = ref(null)
const weeklyTasksChartRef = ref(null)
const projectProgressChartRef = ref(null)



const getStatistics = async () => {
    const allList = await listProject({
      pageNum: 1,
      pageSize: 999
    })
    // console.log("aallList",allList)
      const projectList = allList.rows

    let ongoingProjects = 0
    for (let i = 0; i < projectList.length; i++) {
    if (projectList[i].status === '进行中') {
      ongoingProjects++
    }
  }
    let planProjects = 0
    for (let i = 0; i < projectList.length; i++) {
    if (projectList[i].status === '规划中') {
      planProjects++
    }}

      let completedProjects = 0
    for (let i = 0; i < projectList.length; i++) {
    if (projectList[i].status === '已完成') {
      completedProjects++
      }
    }



  const allTasks = await listItem({
    pageNum: 1,
    pageSize: 999
  })
  console.log(allTasks)
  let unfinishedTasks= 0
  for (let i = 0; i < allTasks.rows.length; i++) {
    if (allTasks.rows[i].status === '未完成') {
      unfinishedTasks++
    }
  }

  const nowDate = new Date()
  const now = nowDate.getTime()
  const oneWeek = new Date(now - 604800000)


  let nearlyTasks = 0
  for (let i = 0; i < allTasks.rows.length; i++) {
    const deadline = new Date(allTasks.rows[i].dueDate)

    if (allTasks.rows[i].status === '已完成') {
      continue
    }
    if (deadline <= oneWeek && deadline >= now) {
      nearlyTasks++
    }
  }


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
    // console.log("allProjects",allProjects)


    let projectIds=[]
    for (let i = 0; i < allProjects.rows.length; i++) {
      projectIds.push(allProjects.rows[i].projectId)
    }
    const allMember = await listProjectMember({
      projectIds: projectIds,
      pageSize: 999,
      pageNum: 1
    })
    // console.log("allMember",allMember)
    const teamMemberIds = new Set()
    for (let i = 0; i < allMember.rows.length; i++) {
      teamMemberIds.add(allMember.rows[i].userId)
      // console.log("teamMemberIds",teamMemberIds)
    }
    teamMemberIds.add(userId)

    const allUsers = await listUser({
      pageSize: 999,
      pageNum: 1
    })
  // console.log("allUsers",allUsers)

    const userNameMap = {}
    for (let i = 0; i < allUsers.rows.length; i++) {
      const user = allUsers.rows[i]
      if (teamMemberIds.has(user.userId)) {
        userNameMap[user.userId] = user.nickName
      }
    }


    const thisMonth = new Date()
    thisMonth.setDate(thisMonth.getDate() - 30)


    const allTasks = await listItem({
      status: '已完成',
      projectIds: projectIds,
      pageNum: 1,
      pageSize: 999
    })
    // console.log("allTasks",allTasks)



    const memberStats = {}

    for (let i = 0; i < allTasks.rows.length; i++) {
      const task = allTasks.rows[i]
      const completionDate = new Date(task.completedDate || task.updateTime)
      if (completionDate >= thisMonth && task.status === '已完成' ) {
        const assignedNames = userNameMap[task.assignedTo]
        if (!memberStats[assignedNames]) {
          memberStats[assignedNames] = 1
        } else {memberStats[assignedNames]++}
      }
    }

    const currentUserName = userNameMap[userId]
    if (!memberStats[currentUserName]) {
      memberStats[currentUserName] = 0
    }


    const members = Object.keys(memberStats)
    const activities = []

    for (let i = 0; i < members.length; i++) {
      const member = members[i]
      activities.push({
        value: memberStats[member],
        itemStyle: {
          color: getRandomColor()
        }
      })
    }

    if (activeChart1) {
      activeChart1.setOption({
        title: {
          text: 'Team members task completion status in the past 30 days',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          formatter: (params) => {
            const data = params[0]
            return `${data.name}<br/>Number of completed tasks：${data.value}`
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
          name: 'nums',
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
    // console.log(allList)
      const weekDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    const dailyTasks = [0, 0, 0, 0, 0, 0, 0]


    for (let i = 0; i < allList.rows.length; i++) {
      if (allList.rows[i].updateTime) {
        const completedDate = new Date(allList.rows[i].updateTime)
        if (completedDate >= startDay1) {
          let dayIndex = completedDate.getDay() || 7
          dailyTasks[dayIndex - 1]++
        }
      }
    }

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
            name: 'nums'
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

  } catch (error) {
    ElMessage.error('获取周任务统计失败')
    console.error('获取周任务统计失败:', error)
  }
}


const getRandomColor = () => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399','#4B77BE', '#3498DB', '#5DADE2', '#1ABC9C', '#2E86C1', '#A569BD', '#EC7063', '#F39C12', '#E74C3C', '#BFC9CA']
  return colors[Math.floor(Math.random() * colors.length)]
}

const calculateProgress = async (project) => {
  try {

    const allTasksforCalc = await listItem({
      projectId: project.projectId,
      pageNum: 1,
      pageSize: 999
    })
    // console.log("allTasksforCalc",allTasksforCalc)

    const allTasks = allTasksforCalc.rows
    // console.log("allTasks",allTasks)
    const completedTasks = []
    for (let i = 0; i < allTasks.length; i++) {
      if (allTasks[i].status === '已完成') {completedTasks.push(allTasks[i])
      }
    }
    const progress = Math.round((completedTasks.length / allTasks.length) * 100)
    return progress
  } catch (error) {
    console.error('计算项目进度失败:', error)
    return 0
  }
}


const getMyWorkItems = async () => {
  try {
    const userStore = useUserStore()
    const response = await listItem({
      assignedTo: userStore.id,
      pageNum: 1,
      pageSize: 999,
    })
    // console.log("response",response)

      myWorkItems.value = response.rows.map(item => ({
        title: item.title,
        dueDate: item.dueDate,
        status: item.status
      }))
    myWorkItems.value = []
    for (let i = 0; i < response.rows.length; i++) {
      myWorkItems.value.push({
        title: response.rows[i].title,
        dueDate: response.rows[i].dueDate,
        status: response.rows[i].status
      })
    }
  } catch (error) {
    console.error('获取工作项失败:', error)
  }
}
// 状态标签类型
const setStatusType = (status) => {
  const statusMap = {
    '未开始': 'info',
    '进行中': 'primary',
    '已完成': 'success',
    '已逾期': 'danger'
  }
  return statusMap[status] || 'info'
}
// 初始化图表
const initCharts = () => {
  activeChart1 = echarts.init(teamActivityChartRef.value)
  taskChart1 = echarts.init(weeklyTasksChartRef.value)
  projectChart = echarts.init(projectProgressChartRef.value)


  getTeamActivityData()
  getWeeklyTasksData()
  getProjectProgressData()
  getMyWorkItems()
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
    // console.log("userProjectforV",userProjectforV)


    const projectIds = []
    for (let i = 0; i < userProjectforV.rows.length; i++) {
      projectIds.push(userProjectforV.rows[i].projectId)
    }



    const allProjects = await listProject({
      projectIds: projectIds,
      pageNum: 1,
      pageSize: 999
    })
    // console.log("allProjects",allProjects)

    if (allProjects.rows) {
      const projectList = allProjects.rows
      const names = projectList.map(p => p.projectName)

      const progressPromises = []
      for (let i = 0; i < projectList.length; i++) {
        progressPromises.push(calculateProgress(projectList[i]))
      }


      const progress = await Promise.all(progressPromises)


      if (projectChart) {
        projectChart.setOption({
          title: {
            text: 'Participate in project progress',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            formatter: (params) => {
              const project = projectList[params[0].dataIndex]
              return `${project.projectName}<br/>Completion progress：${params[0].value}%`
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
            name: 'progress',
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
                color: getRandomColor(),
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
.project-overview-card {
  margin-bottom: 20px;
}

.project-overview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.project-overview-switcher {
  display: flex;
  align-items: center;
  gap: 8px;
}

.project-overview-switcher .el-icon {
  cursor: pointer;
}

.project-overview-switcher .switcher-disabled {
  color: var(--el-text-color-placeholder);
  cursor: not-allowed;
}

.project-overview-content {
  padding: 10px 0;
}

.project-overview-title {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: bold;
}

.project-overview-progress {
  margin: 20px 0;
}

.project-overview-metrics {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
}

.project-metric-item {
  text-align: center;
  flex: 1;
}

.metric-label {
  display: block;
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.metric-value {
  display: block;
  font-size: 20px;
  font-weight: bold;
  margin-top: 5px;
  color: var(--el-color-primary);
}

</style>