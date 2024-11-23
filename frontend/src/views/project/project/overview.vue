<template>
  <div class="cards-container">  <el-card class="project-info-card">
    <template #header><div class="card-header"><span class="title">basic info</span><span class="status-tag">{{ projectInfo.status }}</span>
      </div>
    </template>
    <div class="info-content"><div class="info-row"><div class="info-item">
        <span class="label">projectName:</span><span class="value">{{ projectInfo.projectName }}</span>
    </div>
        <div class="info-item"><span class="label">projectCode:</span><span class="value">{{ projectInfo.projectCode }}</span></div>
      </div><div class="info-row"><div class="info-item"><span class="label">startDate:</span><span class="value">{{ projectInfo.startDate }}</span>
        </div><div class="info-item"><span class="label">expectedEndDate:</span><span class="value">{{ projectInfo.expectedEndDate }}</span></div>
      </div>
      <div class="info-row">
        <div class="info-item"><span class="label">completionPercentage:</span>
          <el-progress :percentage="projectInfo.completionPercentage"

          />
        </div>
      </div>

      <div class="info-row"><div class="info-item full-width">
        <span class="label">description:</span><span class="value">{{ projectInfo.description }}</span>
      </div>
      </div>
    </div>
  </el-card>
    <el-card class="chart-card">
      <template #header>
        <div class="card-header">
          <span class="title">static</span></div></template>
      <div ref="chart1" style="height: 400px;"></div>
    </el-card></div>
  <div class="cards-container">
  <el-card class="countdown-card">
    <template #header>
      <div class="card-header"><span class="title">Time remaining</span></div>
    </template>
    <div class="Timer">
      <div class="Timerinfo"><div class="time-remaining"><span class="days">{{ times1 }}</span></div><div class="calendar-container">
        <el-calendar v-model="currentDate" /></div>
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
          v-if="mmeber.length > 0"
          :data="mmeber"
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
const mmeber = ref([])
const getInfo =  async () => {
  const projectGet = await getProject(
      projectId
  )
  // console.log(projectGet)
  projectInfo.value = projectGet.data
  const expectEndDate = projectGet.data.expectedEndDate
  // console.log(expectEndDate)
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



const getRList = async () => {
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

// console.log(projectId)
  const res = await listProjectMember({
    projectId: projectId
  })
  // console.log(res)
    mmeber.value = res.rows

  const details = []
  for(let i=0;i<mmeber.value.length;i++){
    const user = mmeber.value[i]
    const userDetail = await getUser(user.userId)
    details.push(userDetail.data)
  }
  mmeber.value = details
  // console.log(mmeber.value)

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
      name: 'requirments'
    },
    series: [
      {
        name: 'requirments',
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
  getRList()
  getMembers()
})

</script>

<style scoped>
@import "over.scss";
</style>