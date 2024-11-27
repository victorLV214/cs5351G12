<template>
  <div class="app-container">
    <div class="mb-20">
      <el-button type="primary" :icon="Plus" @click="doDiga">Add Iteration</el-button>
      <el-button type="primary" :icon="Download" @click="downloadIterationList()" class="download-button">Download</el-button>
    </div>

    <el-table :data="list" v-loading="loading" border>
      <el-table-column label="Iteration Name" prop="name" />
      <el-table-column label="Start Date" prop="startDate" />
      <el-table-column label="End Date" prop="endDate" />
      <el-table-column label="Story Points" prop="plannedStoryPoints" />
      <el-table-column label="Burndown Chart" width="250" align="center">
        <template #default="scope">
          <el-button type="success" link @click="showAll(scope.row)">
            <el-icon><TrendCharts/></el-icon>
            View Chart
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="Operations" align="center" width="180" >
        <template #default="{ row }">
          <el-row :gutter="10">
            <el-col :span="12">
              <el-button size="small" type="primary" class="w-100"
                         @click="doEdit(row)" :icon="Edit" v-if="booladmin">Edit
              </el-button>
            </el-col>
            <el-col :span="12">
              <el-button size="small" type="danger" class="w-100"
                         @click="delIteration(row)" :icon="Delete" v-if="booladmin">Delete</el-button>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="title" v-model="show" width="500px" @close="rF1">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" placeholder="Please input iteration name" />
        </el-form-item>
        <el-form-item label="Start Date" prop="startDate">
          <el-date-picker v-model="form.startDate" type="date" placeholder="Select start date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="End Date" prop="endDate">
          <el-date-picker v-model="form.endDate" type="date" placeholder="Select end date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="Story Points" prop="plannedStoryPoints">
          <el-input-number v-model="form.plannedStoryPoints" :min="0" controls-position="right" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="show = false">Cancel</el-button>
          <el-button type="primary" @click="sF1">Confirm</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog title="Work Items" v-model="bVisItem" width="1200px" destroy-on-close>
      <div style="display: flex; margin-bottom: 20px;">
        <div id="burndownChart" style="width: 100%; height: 300px;"></div>
      </div>
      <el-table :data="wList" v-loading="bItem" border>
        <el-table-column label="Title" prop="title" />
        <el-table-column label="Type" prop="type" />
        <el-table-column label="Status" prop="status" />
        <el-table-column label="Priority" prop="priority" />
        <el-table-column label="Estimated" prop="estimatedEffort" />
        <el-table-column label="Actual" prop="actualEffort" />
        <el-table-column label="Start Date" prop="startDate" />
        <el-table-column label="Due Date" prop="dueDate" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {listIteration, addIteration, updateIteration, exportIteration} from '@/api/project/iteration'
import { listItem } from '@/api/project/item'
import * as echarts from 'echarts'
import {Delete, Download, Edit, Plus, TrendCharts} from "@element-plus/icons-vue";
import {delDefect} from "@/api/project/defect.js";
import {listRole} from "@/api/system/role.js";

const route = useRoute()
const projectId = route.params.id

const loading = ref(false)
const show = ref(false)
const title = ref('')
const list = ref([])
const booladmin = ref(false)

const bVisItem = ref(false)
const bItem = ref(false)
const wList = ref([])

const formRef = ref(null)
const form = ref({
  iterationId: undefined,
  projectId: projectId,
  name: '',
  startDate: '',
  endDate: '',
  plannedStoryPoints: 0,
  status:'1',
})
const checkRoles = async () => {

  const res = await listRole()
  console.log('res:', res)
  booladmin.value = res.rows.some(role => role.roleKey === 'admin')

}
const rules = {
  name: [
    { required: true, message: 'Please input iteration name', trigger: 'blur' }
  ],
  startDate: [
    { required: true, message: 'Please select start date', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: 'Please select end date', trigger: 'change' }
  ],
  plannedStoryPoints: [
    { required: true, message: 'Please input story points', trigger: 'change' }
  ]
}

const getList = async () => {
  loading.value = true
  const res = await listIteration({ projectId: projectId })
  list.value = res.rows
  loading.value = false
}

const doDiga = () => {
  title.value = 'Add Iteration'
  show.value = true
}

const doEdit = (row) => {
  title.value = 'Edit Iteration'
  form.value = { ...row, projectId: projectId }
  show.value = true
}

const delIteration = async (row) => {
  await ElMessageBox.confirm(
      'This action will permanently delete this iteration. Continue?',
      'Warning',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
  loading.value = true
  await delDefect(row.defectId)
  boolVis1.value = false
  await getReqs()
  loading.value = false
}

const showAll = async (row) => {
  bVisItem.value = true
  bItem.value = true
  const res = await listItem({
    iterationId: row.iterationId,
    projectId: projectId
  })
  wList.value = res.rows
  bItem.value = false

  setTimeout(() => {
    drawPaint(row, res.rows)
  }, 100)
}

const drawPaint = (iteration, items) => {
  const chartDom = document.getElementById('burndownChart')
  const myChart = echarts.init(chartDom)


  let total = 0;
  for (const item of items) {
    total += Number(item.actualEffort) || 0;
  }


  const getDateline = (startDate, endDate) => {
    let dates = []
    let today = new Date(startDate)
    let end = new Date(endDate)
    while (today <= end) {
      dates.push(today.toISOString().split('T')[0])
      today.setDate(today.getDate() + 1)
    }
    return dates
  }

  const dateList = getDateline(iteration.startDate, iteration.endDate)


  const idea = []
  const totalDays = dateList.length
  const totalPoints = total
  for (let i = 0; i < totalDays; i++) {
    idea.push(+(totalPoints - (totalPoints / (totalDays - 1)) * i).toFixed(1))
  }


  const actural = Array(dateList.length).fill(totalPoints)
  items.forEach(item => {
    if (item.actualEffort && item.dueDate) {
      const dateIndex = dateList.findIndex(date => date === item.dueDate)
      if (dateIndex !== -1) {
        for (let i = dateIndex + 1; i < actural.length; i++) {
          actural[i] = +(actural[i] - Number(item.actualEffort)).toFixed(1)
        }
      }
    }
  })

  const option = {
    title: {
      text: 'Sprint Burndown Chart',
      left: 'center'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross'
      }
    },
    legend: {
      data: ['IdealBurndown', 'ActualBurndown'],
      bottom: '0%'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '10%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dateList,
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: 'Remaining Work (Points)',
      min: 0
    },
    series: [
      {
        name: 'IdealBurndown',
        type: 'line',
        data: idea,
        smooth: true,
        lineStyle: {
          type: 'dashed'
        }
      },
      {
        name: 'ActualBurndown',
        type: 'line',
        data: actural,
        smooth: true,
        itemStyle: {
          color: '#5470c7'
        },
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
            offset: 0,
            color: '#5470c7'
          }, {
            offset: 1,
            color: '#5470c7'
          }])
        }
      }
    ]
  }

  myChart.setOption(option)

  const resizeHandler = () => {
    myChart.resize()
  }
  window.addEventListener('resize', resizeHandler)


  watch(bVisItem, (newVal) => {
    if (!newVal) {
      window.removeEventListener('resize', resizeHandler)
      myChart.dispose()
    }
  })
}


const sF1 = () => {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      if (form.value.iterationId) {
        await updateIteration(form.value)
        ElMessage.success('Update successful')
      } else {
        await addIteration(form.value)
        ElMessage.success('Add successful')
      }
      show.value = false
      getList()
    }
  })
}

const rF1 = () => {
  formRef.value?.resetFields()
  form.value = {
    iterationId: undefined,
    projectId: projectId,
    name: '',
    startDate: '',
    endDate: '',
    plannedStoryPoints: 0
  }
}

// 下载项目迭代列表
const downloadIterationList = async () => {
  const query = {
    projectId: projectId
  }
  const res = await exportIteration(query)
  const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
  const href = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = href
  link.download = `project_iteration_list.xlsx`
  link.click()
}

onMounted(() => {
  getList()
  checkRoles()
})
</script>

<style scoped>
@import "it.scss";
</style>