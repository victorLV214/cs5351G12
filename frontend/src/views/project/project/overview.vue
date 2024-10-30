<template>
  <div class="cards-container">  <el-card class="project-info-card">
    <template #header>
      <div class="card-header">
        <span class="title">basic info</span>
        <span class="status-tag">{{ projectInfo.status }}</span>
      </div>
    </template>
    <div class="info-content">
      <div class="info-row">
        <div class="info-item">
          <span class="label">projectName:</span>
          <span class="value">{{ projectInfo.projectName }}</span>
        </div>
        <div class="info-item">
          <span class="label">projectCode:</span>
          <span class="value">{{ projectInfo.projectCode }}</span>
        </div>
      </div>
      <div class="info-row">
        <div class="info-item">
          <span class="label">startDate:</span>
          <span class="value">{{ projectInfo.startDate }}</span>
        </div>
        <div class="info-item">
          <span class="label">expectedEndDate:</span>
          <span class="value">{{ projectInfo.expectedEndDate }}</span>
        </div>
      </div>
      <div class="info-row">
        <div class="info-item">
          <span class="label">completionPercentage:</span>
          <el-progress
              :percentage="projectInfo.completionPercentage"

          />
        </div>
      </div>

      <div class="info-row">
        <div class="info-item full-width">
          <span class="label">description:</span>
          <span class="value">{{ projectInfo.description }}</span>
        </div>
      </div>
    </div>
  </el-card>
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span class="title">static</span>
        </div>
      </template>
      <div ref="chart1" style="height: 300px;"></div>
    </el-card></div>
  <div class="cards-container">
  <el-card class="countdown-card">
    <template #header>
      <div class="card-header">
        <span class="title">Time remaining</span>
      </div>
    </template>
    <div class="Timer">
      <div class="Timerinfo">
        <div class="time-remaining">
          <span class="days">{{ times1 }}</span>
        </div>
        <div class="calendar-container">
          <el-calendar v-model="currentDate" />
        </div>
      </div>
    </div>

  </el-card>
  <el-card class="member-card">
    <template #header>
      <div class="card-header">
        <span class="title">Team Members</span>
      </div>
    </template>
    <div class="member-list">
      <el-table
          v-if="memberList.length > 0"
          :data="memberList"
          style="width: 100%"
      >
        <el-table-column prop="nickName" label="Name" />
        <el-table-column prop="phonenumber" label="Phone" />
        <el-table-column prop="remark" label="remark" />
      </el-table>
    </div>
  </el-card>
  </div>

</template>

<script setup>
import { ref, computed } from 'vue'
import {getProject} from "@/api/project/index.js";
import { onMounted } from 'vue'
import{useRoute} from "vue-router";
import {listRequirement} from "@/api/project/requirements.js";
import * as echarts from 'echarts'
import {listProjectMember} from "@/api/project/member.js";
import {getUser} from "@/api/system/user.js";

const projectInfo = ref({})
const route = useRoute()
const projectId=route.params.id
const chart1 = ref(null)
const currentDate = ref(new Date())
let chart = null
let times1="over time"
const memberList = ref([])
const getInfo =  async () => {
  const projectGet = await getProject(
      projectId
  )
  console.log(projectGet)
  projectInfo.value = projectGet.data
  const expectEndDate = projectGet.data.expectedEndDate
  console.log(expectEndDate)
  const remainingDays = computed(() => {
    const now = new Date()
    const end = new Date(expectEndDate)
    const diff = end - now
    const days = Math.floor(diff / (1000 * 60 * 60 * 24))
    return days
  })
  const day = remainingDays.value
  // console.log(day)
  if(day<0){
    times1
  }else{
    times1=day
  }
}
const getRequirementList = async () => {
  const requirementListGet = await listRequirement(
      {projectId:projectId}
  )

  // console.log(requirementListGet)
  const requirementList = requirementListGet.rows
  const allType = {};
  for (let i = 0; i < requirementList.length; i++) {
    const requirement1 = requirementList[i];
    const type = requirement1.type || '未分类';
    if (allType[type]) {
      allType[type]++;
    } else {
      allType[type] = 1;
    }
  }
  initChart(allType)

}
const getMembers = async () => {


    const res = await listProjectMember(projectId)
  // console.log(res)
    memberList.value = res.rows

  const details = []
  for(let i=0;i<memberList.value.length;i++){
  const user = memberList.value[i]
  const userDetail = await getUser(user.userId)
  details.push(userDetail.data)
  }
  memberList.value = details
  console.log(memberList.value)

}
const initChart = (statsData) => {
  if (!chart1.value) return

  chart = echarts.init(chart1.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: Object.keys(statsData),
      axisLabel: {
        interval: 0,
        rotate: 30
      }
    },
    yAxis: {
      type: 'value',
      name: '需求数量'
    },
    series: [
      {
        name: '需求数量',
        type: 'bar',
        data: Object.values(statsData),
        itemStyle: {
          color: '#409EFF'
        },
        label: {
          show: true,
          position: 'top'
        }
      }
    ]
  }

  chart.setOption(option)
}

onMounted(() => {
  getInfo()
  getRequirementList()
  getMembers()
})

</script>

<style scoped>

.countdown-card{
  flex: 0 0 45%; /* 设置固定宽度为50% */
  max-width: 50%;

}
.member-card{
  flex: 0 0 45%; /* 设置固定宽度为50% */
  max-width: 50%;
}
.project-info-card, .chart-card {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.cards-container {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
  align-items: stretch;
}

.title {
  font-size: 16px;
  font-weight: bold;
}

.status-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}



.info-content {
  padding: 10px 0;
}

.info-row {
  display: flex;
  margin-bottom: 15px;
}

.info-item {
  flex: 1;
  display: flex;
  align-items: center;
}

.info-item.full-width {
  flex: 2;
}

.label {
  color: #606266;
  margin-right: 10px;
  min-width: 80px;
}

.value {
  color: #303133;
}



:deep(.el-progress) {
  width: 200px;
}
</style>