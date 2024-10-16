<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <!-- 统计数据 -->
      <el-col :xs="24" :sm="8" :md="8" :lg="8" :xl="8" v-for="(item, index) in statisticsData" :key="index">
        <el-card class="box-card">
          <div class="card-header">
            <span>{{ item.title }}</span>
          </div>
          <div class="statistic-value">{{ item.value }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 团队成员活跃度 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>团队成员活跃度</span>
            </div>
          </template>
          <div ref="teamActivityChart" style="height: 300px;"></div>
        </el-card>
      </el-col>

      <!-- 本周完成任务统计 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>本周完成任务统计</span>
            </div>
          </template>
          <div ref="weeklyTasksChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 项目进度可视化 -->
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>项目进度可视化</span>
            </div>
          </template>
          <div ref="projectProgressChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 项目概览 -->
      <el-col :xs="24" :sm="24" :md="24" :lg="24" :xl="24">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>项目概览</span>
            </div>
          </template>
          <div v-for="project in projects" :key="project.id" class="project-item">
            <h3>{{ project.name }}</h3>
            <el-progress :percentage="project.progress" :format="percentageFormat" />
            <div class="project-metrics">
              <span v-for="(metric, index) in project.metrics" :key="index">
                {{ metric.name }}: {{ metric.value }}
              </span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import {ref, onMounted} from 'vue'
import * as echarts from 'echarts'

const version = ref('3.8.7')

const statisticsData = ref([
  {title: '进行中的项目', value: 12},
  {title: '待处理任务', value: 45},
  {title: '即将到期的任务', value: 8}
])

const projects = ref([
  {
    id: 1,
    name: '重要项目A',
    progress: 75,
    metrics: [
      {name: '完成任务', value: '45/60'},
      {name: '剩余天数', value: '15天'}
    ]
  },
  {
    id: 2,
    name: '重要项目B',
    progress: 40,
    metrics: [
      {name: '完成任务', value: '20/50'},
      {name: '剩余天数', value: '30天'}
    ]
  }
])

const teamActivityChart = ref(null)
const weeklyTasksChart = ref(null)
const projectProgressChart = ref(null)

const percentageFormat = (percentage) => {
  return percentage + '%'
}

onMounted(() => {
  // 团队成员活跃度图表
  const teamActivity = echarts.init(teamActivityChart.value)
  teamActivity.setOption({
    title: {text: '团队成员活跃度'},
    tooltip: {},
    xAxis: {data: ['张三', '李四', '王五', '赵六', '钱七']},
    yAxis: {},
    series: [{
      name: '活跃度',
      type: 'bar',
      data: [5, 20, 36, 10, 10]
    }]
  })

  // 本周完成任务统计图表
  const weeklyTasks = echarts.init(weeklyTasksChart.value)
  weeklyTasks.setOption({
    title: {text: '本周完成任务统计'},
    tooltip: {},
    xAxis: {type: 'category', data: ['周一', '周二', '周三', '周四', '周五']},
    yAxis: {type: 'value'},
    series: [{
      data: [10, 15, 8, 20, 12],
      type: 'line',
      smooth: true
    }]
  })

  // 项目进度可视化图表
  const projectProgress = echarts.init(projectProgressChart.value)
  projectProgress.setOption({
    title: {text: '项目进度'},
    tooltip: {
      trigger: 'axis',
      axisPointer: {type: 'cross'}
    },
    legend: {},
    xAxis: [{type: 'category', data: ['1月', '2月', '3月', '4月', '5月', '6月']}],
    yAxis: [{type: 'value', name: '完成度', min: 0, max: 100, interval: 20}],
    series: [
      {
        name: '项目A',
        type: 'line',
        data: [10, 25, 40, 55, 70, 85]
      },
      {
        name: '项目B',
        type: 'line',
        data: [5, 15, 30, 40, 50, 65]
      }
    ]
  })
})

function goTarget(url) {
  window.open(url, '__blank')
}
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
  }

  .project-item {
    margin-bottom: 20px;
    padding-bottom: 20px;
    border-bottom: 1px solid #eee;

    &:last-child {
      border-bottom: none;
    }

    h3 {
      margin-bottom: 10px;
    }

    .project-metrics {
      display: flex;
      justify-content: space-between;
      margin-top: 10px;

      span {
        font-size: 14px;
        color: #606266;
      }
    }
  }

  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>