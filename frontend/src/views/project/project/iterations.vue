
<template>
  <el-page-header>   <el-button type="primary" :icon="Plus" @click="handleAdd">新增迭代</el-button></el-page-header>
  <el-table
      :data="tableData"
      style="width: 100%"
      @row-click="handleRowClick"
  >
    <el-table-column prop="name" label="name" width="180"></el-table-column>
    <el-table-column prop="description" label="description"></el-table-column>
    <el-table-column prop="startDate" label="startDate" width="180"></el-table-column>
    <el-table-column prop="endDate" label="endDate" width="180"></el-table-column>
    <el-table-column prop="status" label="status" width="120"></el-table-column>
  </el-table>
  <el-dialog v-model="addDialog" :show-close="false" width="500">
    <template #header>
      <div class="dialog-footer">
        <el-button @click="addDialog = false">Cancel</el-button>
        <el-button type="primary" @click="submitForm">submit</el-button>
      </div>
    </template>
    <el-form ref="iterationFormRef" :model="iForm"  label-width="100px">
      <el-form-item label="name" prop="name"><el-input v-model="iForm.name" placeholder="name" />
      </el-form-item>
      <el-form-item label="description" prop="description">
        <el-input v-model="iForm.description" type="textarea" placeholder="description" />
      </el-form-item>
      <el-form-item label="dateRange" prop="dateRange">
        <el-date-picker
            v-model="dateRange" type="daterange" range-separator="------" start-placeholder="start date" end-placeholder="end date" value-format="YYYY-MM-DD"
        />
      </el-form-item>
      <el-form-item label="status" prop="status">
        <el-select v-model="iForm.status">
          <el-option label="未开始" value="未开始" />
          <el-option label="进行中" value="进行中" />
          <el-option label="已完成" value="已完成" />
        </el-select>
      </el-form-item>
    </el-form>

  </el-dialog>
  <el-dialog v-model="reqListForS" title="需求列表" width="800px">
    <el-table :data="reqList">
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="status" label="状态" width="100"></el-table-column>



    </el-table>
  </el-dialog>
</template>

<script setup>


import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'

import { useRoute } from "vue-router"
import {
  addRequirement,
  delRequirement,
  getRequirementDetail,
  listRequirement,
  updateRequirement
} from "@/api/project/requirements.js"
import { ElMessage, ElMessageBox } from "element-plus"
import useUserStore from "@/store/modules/user.js"
import { getUser } from "@/api/system/user.js"
import { gantt } from "dhtmlx-gantt"
import {addIteration, listIteration} from "@/api/project/iteration.js";
import {listItem} from "@/api/project/item.js";
const userStore = useUserStore()
const route = useRoute()
const projectId = route.params.id
const dateRange = ref([])
const ganttContainer = ref(null)
const ganttDialogVisible = ref(false)
const reqList = ref([])
const total = ref(0)
const isEdit = ref(false)
const firstData = ref(null)
const addButten = ref(false)
const reqListForS = ref(false)
const username = ref(null)
const addDialog=ref(false)
const loading = ref(false)
const reqForm = ref({
  title: '',
  priority: '',
  status: 'processing',
  type: '',
  assignedTo: '',
  description: '',
  remarks: '',
  projectId: projectId,
  delFlag: 1,
  createBy: username.value,
})
const iForm = ref({
  name: '',
  description: '',
  startDate: '',
  endDate: '',
  status: '未开始',
  projectId: projectId,

})
const id = userStore.id
const Params = ref({
  pageNum: 1,
  pageSize: 10,
  title: '',
  type: '',
  priority: '',
  status: '',
  assignedTo: '',
  projectId: projectId,
  delFlag: 1,
})
const handleRowClick = async (row) => {
  const p = {
    iterationId: row.iterationId,
    projectId: projectId
  }
  const req = await listItem(p)
  reqList.value = req.rows
  total.value = req.total
  reqListForS.value = true
}
const dialogVisible = ref(false)
const curReq = ref(null)
const formRef = ref(null)

const addFormRef = ref(null)
const delButten = ref(false)
let ganttInstance = null
const tableData = ref([])

const getData = async () => {
  const res = await listIteration(Params.value)
  console.log(res)
  tableData.value = res.rows
}
const handleAdd = () => {
  addDialog.value = true
  iForm.value = {
    name: '',
    description: '',
    startDate: '',
    endDate: '',
    status: '未开始',
    projectId: projectId,
  }
  dateRange.value = []


}


const submitForm=()=>{
  console.log(iForm.value)
  addDialog.value=false
  const submitData = {
    ...iForm.value,
    startDate: dateRange.value[0],
    endDate: dateRange.value[1]
  }

    addIteration(submitData)
    ElMessage.success('添加成功')
    addDialog.value = false
    getData() // 刷新列表
}
onMounted(() => {
  getData()
})
</script>

<style>
.el-table .warning-row {
  --el-table-tr-bg-color: var(--el-color-warning-light-9);
}
.el-table .success-row {
  --el-table-tr-bg-color: var(--el-color-success-light-9);
}
</style>

