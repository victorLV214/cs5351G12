<template>
  <div class="app-container design1">
    <el-row :gutter="20"><el-col :span="4.8" v-for="(item, index) in projectDaat" :key="index">
        <el-card class="box-card">
          <div class="card-header"><span>{{ item.title }}</span></div><div class="ss">{{ item.value }}</div></el-card>
    </el-col></el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card class="box-card">
          <template #header><div class="card-header"><span>Team members activity</span>
          </div>
          </template><div ref="chartR" style="height: 300px;"></div></el-card>
      </el-col><el-col :span="12"   ><el-card class="box-card"><template #header><div class="card-header"><span>Complete task statistics this week</span>
            </div>
    </template>

        <div ref="weekChart" style="height: 300px;"></div></el-card></el-col></el-row><el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card class="box-card">


          <template #header><div class="card-header">
              <span>Project progress visualization</span></div>
          </template><div ref="projectC" style="height: 300px;"></div>
        </el-card>
      </el-col>
    <el-col :span="12">
        <el-card class="box-card"><template #header>
            <div class="card-header"><span>My Responsible Work Items</span>
            </div>
          </template><div class="itemlist">
          <el-table :data="myWorkItems" style="width: 100%">
            <el-table-column prop="title" label="title" /><el-table-column prop="dueDate" label="dueDate" width="120" />
            <el-table-column prop="status" label="status" width="100">
              </el-table-column>
          </el-table></div></el-card></el-col></el-row>
  </div>
</template>
<script setup name="Index">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import { listItem } from '@/api/project/item'

let activeChart1 = null
let taskChart1 = null
let projectChart = null

const myWorkItems = ref([])
const projectDaat = ref([])
const projects = ref([])


import { listProject } from '@/api/project/index'
import {listProjectMember} from "@/api/project/member.js";
import useUserStore from "@/store/modules/user.js";
import {listUser} from "@/api/system/user.js";
const chartR = ref(null)
const weekChart = ref(null)
const projectC = ref(null)

const getData = async () => {
    const allList = await listProject({
      pageNum: 1,
      pageSize: 999
    })
    // console.log("aallList",allList)
      const d1 = allList.rows

    let ongoingProjects = 0
  let endProject = 0
  let planProjects = 0

  for (let i = 0; i < d1.length; i++) {
    if (d1[i].status === '进行中' || d1[i].status === 'ongoing') {
      ongoingProjects++
    }
  }
  for (let i = 0; i < d1.length; i++) {
    if (d1[i].status === '规划中' || d1[i].status === 'planning') {
      planProjects++
    }
  }
  for (let i = 0; i < d1.length; i++) {
    if (d1[i].status === '已完成' || d1[i].status === 'completed') {
      endProject++
    }
  }




  const allTasks = await listItem({
    pageNum: 1,
    pageSize: 999
  })
  // console.log(allTasks)
  let notFinishT= 0
  for (let i = 0; i < allTasks.rows.length; i++) {
    if (allTasks.rows[i].status === '未完成' || allTasks.rows[i].status === 'uncompleted') {
      notFinishT++
    }
  }

  const nowDate = new Date()
  const now = nowDate.getTime()
  const oneWeek = new Date(now - 604800000)


  let nearT = 0
  for (let i = 0; i < allTasks.rows.length; i++) {
    const deadline = new Date(allTasks.rows[i].dueDate)

    if (allTasks.rows[i].status === '已完成' || allTasks.rows[i].status === 'completed') {
      continue
    }
    if (deadline <= oneWeek && deadline >= now) {
      nearT++
    }
  }


  projectDaat.value = [
    { title: 'running', value: ongoingProjects }, { title: 'planning', value: planProjects }, { title: 'completed', value: endProject },
    { title: 'unfinish', value: notFinishT },{ title: 'recently', value: nearT }
  ]

  projects.value = d1
}

const getD4 = async () => {
  try {
    const userStore = useUserStore()
    const userId = userStore.id
    
    const allP = await listProjectMember({
      userId: userId,
      pageSize: 999,
      pageNum: 1
    })
    // console.log("allP",allP)


    let projectIds=[]
    for (let i = 0; i < allP.rows.length; i++) {
      projectIds.push(allP.rows[i].projectId)
    }
    const allMember = await listProjectMember({
      projectIds: projectIds,
      pageSize: 999,
      pageNum: 1
    })
    // console.log("allMember",allMember)
    const tid3 = new Set()
    for (let i = 0; i < allMember.rows.length; i++) {
      tid3.add(allMember.rows[i].userId)
      // console.log("tid3",tid3)
    }
    tid3.add(userId)

    const allUsers = await listUser({
      pageSize: 999,
      pageNum: 1
    })
  // console.log("allUsers",allUsers)

    const nameNick = {}
    for (let i = 0; i < allUsers.rows.length; i++) {
      const user = allUsers.rows[i]
      if (tid3.has(user.userId)) {
        nameNick[user.userId] = user.nickName
      }
    }


    const thisMonth = new Date()
    thisMonth.setDate(thisMonth.getDate() - 30)


    const allTasks = await listItem({
      status: 'completed',
      projectIds: projectIds,
      pageNum: 1,
      pageSize: 999
    })
    // console.log("allTasks",allTasks)



    const mSta = {}

    for (let i = 0; i < allTasks.rows.length; i++) {
      const task = allTasks.rows[i]
      const doneDate = new Date(task.doneDate || task.updateTime)
      if (doneDate >= thisMonth && (task.status === '已完成' || task.status === 'completed')) {
        const xiangguanName = nameNick[task.assignedTo]
        if (!mSta[xiangguanName]) {
          mSta[xiangguanName] = 1
        } else {mSta[xiangguanName]++}
      }
    }

    const nameNow = nameNick[userId]
    if (!mSta[nameNow]) {
      mSta[nameNow] = 0
    }


    const members = Object.keys(mSta)
    const activities = []

    for (let i = 0; i < members.length; i++) {
      const member = members[i]
      activities.push({
        value: mSta[member],
        itemStyle: {
          color: setColors()
        }
      })
    }




    if (activeChart1) {
      activeChart1.setOption({
        title: {
          text: 'task completion status in the past 30 days',
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
    // ElMessage.error('')
    console.error('e:', error)
  }
}


const getWeek = async () => {
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
        const doneD = new Date(allList.rows[i].updateTime)
        if (doneD >= startDay1) {
          let dayIndex = doneD.getDay() || 7
          dailyTasks[dayIndex - 1]++
        }
      }
    }

      if (taskChart1) {
        taskChart1.setOption({
          tooltip: {trigger: 'axis', axisPointer: { type: 'shadow' }
          },
          grid: {left: '3%', right: '4%', bottom: '3%', containLabel: true
          },
          xAxis: {type: 'category',
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
    // ElMessage.error('1', error)
    console.error('2', error)
  }
}


const setColors = () => {
  const colors = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399','#4B77BE', '#3498DB', '#5DADE2', '#1ABC9C', '#2E86C1', '#A569BD', '#EC7063', '#F39C12', '#E74C3C', '#BFC9CA']
  return colors[Math.floor(Math.random() * colors.length)]
}

const calFun = async (project) => {


    const allTs2 = await listItem({
      projectId: project.projectId,
      pageNum: 1,
      pageSize: 999
    })
    // console.log("allTs2",allTs2)

    const allTasks = allTs2.rows
    // console.log("allTasks",allTasks)
    const taskL = []
    for (let i = 0; i < allTasks.length; i++) {
      if (allTasks[i].status === '已完成'|| allTasks[i].status==='completed') {taskL.push(allTasks[i])
      }
    }
    const progress = Math.round((taskL.length / allTasks.length) * 100)
    return progress

}


const getMyWorkItems = async () => {

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
  // console.log("myWorkItems",myWorkItems)
    myWorkItems.value = []
    for (let i = 0; i < response.rows.length; i++) {
      myWorkItems.value.push({
        title: response.rows[i].title,
        dueDate: response.rows[i].dueDate,
        status: response.rows[i].status
      })
    }
  // console.log("myWorkItems",myWorkItems)
}


const initCharts = () => {
  activeChart1 = echarts.init(chartR.value)
  taskChart1 = echarts.init(weekChart.value)
  projectChart = echarts.init(projectC.value)
  
  getD4()
  getWeek()
  getProgress()
  getMyWorkItems()
}


const getProgress = async () => {

    const userStore = useUserStore()
    const userId = userStore.id
    const getmem = await listProjectMember({userId: userId,
      pageSize: 999,
      pageNum: 1
    })
    // console.log("getmem",getmem)


    const projectIds = []
    for (let i = 0; i < getmem.rows.length; i++) {
      projectIds.push(getmem.rows[i].projectId)
    }



    const allP = await listProject({
      projectIds: projectIds,
      pageNum: 1,
      pageSize: 999
    })
    // console.log("allP",allP)

    if (allP.rows) {
      const d1 = allP.rows
      const names = d1.map(p => p.projectName)


      const promisess = []
      for (let i = 0; i < d1.length; i++) {
        promisess.push(calFun(d1[i]))
      }


      const progress = await Promise.all(promisess)


      if (projectChart) {
        projectChart.setOption({
          title: {
            text: 'project progress',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            formatter: (params) => {
              const project = d1[params[0].dataIndex]
              return `${project.projectName}<br/>Completed progress：${params[0].value}%`
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
                color: setColors(),
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

}

const handleResize = () => {
  activeChart1?.resize()
  taskChart1?.resize()
  projectChart?.resize()
}

onMounted(() => {
  getData()
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

<style lang="scss">
@import 'design1.scss';
</style>