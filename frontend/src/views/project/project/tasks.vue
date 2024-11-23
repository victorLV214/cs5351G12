<template>
  <el-card class="r-cards">
    <template #header><div class="card-header">
      <div class="header-left"><span class="title">WORKs LIST</span><el-tag class="count-tag" type="info">total {{ total }} WORKS</el-tag>
      </div>
      <div class="addR">
        <el-button type="primary" :icon="Plus" @click="addReq"></el-button>
      </div></div></template>

    <div class="search"><el-form :inline="true" :model="Params" class="sF"><el-form-item label="">
      <el-input v-model="Params.title" placeholder="" clearable style="width: 200px"/>
    </el-form-item>
      <el-form-item>
        <el-button type="primary" :icon="Search" @click="getReqs">select</el-button><el-button :icon="Refresh" @click="clearAll">Refresh</el-button>
      </el-form-item>
    </el-form>
    </div>
    <div class="rList">
      <el-table v-loading="loading" :data="reqList" style="width: 100%"
      >
        <el-table-column label="ID" align="center">
          <template #default="{ row }"><el-link type="primary" @click="clickV(row)">{{ row.workItemId }}</el-link>
          </template>
        </el-table-column><el-table-column label="title" align="center" prop="title" /><el-table-column label="description" align="center" prop="description" show-overflow-tooltip /><el-table-column label="priority" align="center" prop="priority"></el-table-column><el-table-column label="status" align="center" prop="status"></el-table-column>
        <el-table-column label="createTime" align="center" prop="createTime" width="180" />







      </el-table>

      <div class="pagination-container">
        <el-pagination :page-sizes="[10, 20, 50, 100]" :total="total" v-model:current-page="Params.pageNum" v-model:page-size="Params.pageSize"

                       layout="total, sizes, prev, pager, next, jumper" @size-change="sizeP" @current-change="cP"
        />
      </div>
    </div>
  </el-card>

  <el-dialog
      v-model="dialogVisible" title="requirement details" width="60%"
  >
    <el-form ref="formRef" :model="curReq"  label-width="120px">
      <el-form-item label="requirement ID" prop="requirementId"><span>{{ curReq?.workItemId }}</span></el-form-item>
      <el-form-item label="title" prop="title"><el-input v-model="curReq.title" :disabled="!isEdit"/>
      </el-form-item><el-form-item label="priority" prop="priority">
      <el-select v-model="curReq.priority" :disabled="!isEdit">
        <el-option label="high" value="1" /><el-option label="medium" value="2" /><el-option label="low" value="3" /></el-select>
    </el-form-item><el-form-item label="status" prop="status">
      <el-select v-model="curReq.status" :disabled="!isEdit"><el-option label="pending" value="待处理" /><el-option label="processing" value="进行中" /><el-option label="fixed" value="已修复" />
      </el-select>
    </el-form-item>
      <el-form-item label="type" prop="type"><el-input v-model="curReq.type" :disabled="!isEdit"/></el-form-item>
      <el-form-item label="assignedTo" prop="assignedTo"><el-input v-model="curReq.assignedTo" :disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="description" prop="description"><el-input v-model="curReq.description" type="textarea" :rows="3":disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="remarks" prop="remarks">
        <el-input v-model="curReq.remarks" type="textarea" :rows="3" :disabled="!isEdit"
        /></el-form-item>
    </el-form><template #footer>
  <span class="dialog-footer">
    <el-button @click="dialogVisible  = false">cancel</el-button>
    <el-button v-if="curReq?.createBy === username" type="danger" @click="delReq(curReq)">DEL</el-button>
    <el-button v-if="!isEdit" type="primary" @click="ISEDITT">EDit</el-button><template v-else><el-button @click="donotEDITT">CALCEL</el-button>
    <el-button type="primary" @click="submitEdit">SAVE</el-button></template>
  </span></template>
  </el-dialog>

  <el-dialog v-model="addButten" title="add new requirements" width="60%">
    <el-form ref="addFormRef" :model="reqForm"  label-width="120px">
      <el-form-item label="Title" prop="title">
        <el-input v-model="reqForm.title" placeholder="Please input defect title"/>
      </el-form-item>
      <el-form-item label="Priority" prop="priority">
        <el-select v-model="reqForm.priority" placeholder="Select priority">
          <el-option label="High" value="1" />
          <el-option label="Medium" value="2" />
          <el-option label="Low" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="Severity" prop="severity">
        <el-select v-model="reqForm.severity" placeholder="Select severity">
          <el-option label="Critical" value="4" /><el-option label="Major" value="3" /><el-option label="Minor" value="2" />
          <el-option label="Trivial" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-select v-model="reqForm.status" placeholder="Select status"><el-option label="Pending" value="pending" /><el-option label="Processing" value="processing" /><el-option label="Fixed" value="fixed" /><el-option label="To Verify" value="to_verify" /><el-option label="Closed" value="closed" />
          <el-option label="Reopened" value="reopened" /><el-option label="Rejected" value="rejected" />
        </el-select>
      </el-form-item>
      <el-form-item label="type" prop="type">
        <el-input v-model="reqForm.type" placeholder="Please input defect type"/>
      </el-form-item>

      <el-form-item label="Assigned To" prop="assignedTo"><el-input v-model="reqForm.assignedTo" placeholder="Assign to"/></el-form-item>

      <el-form-item label="Description" prop="description">
        <el-input v-model="reqForm.description" type="textarea" :rows="3" placeholder="Detailed description of the defect"
        />
      </el-form-item>

      <el-form-item label="Expected Version" prop="expectedFixVersion"><el-input v-model="reqForm.expectedFixVersion" placeholder="Expected fix version"/></el-form-item>

      <el-form-item label="Related Req ID" prop="relatedRequirementId"><el-input v-model="reqForm.relatedRequirementId" placeholder="Related requirement ID"/>
      </el-form-item>

      <el-form-item label="Remarks" prop="remarks">
        <el-input v-model="reqForm.remarks" type="textarea" :rows="3" placeholder="Additional remarks"
        />
      </el-form-item>
    </el-form>

    <template #footer>
    <span class="dialog-footer"><el-button @click="addButten = false">cancel</el-button>
      <el-button type="primary" @click="submitAdd">ADD</el-button>
    </span>
    </template>
  </el-dialog>


</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'
import 'dhtmlx-gantt/codebase/dhtmlxgantt.css'
import 'dhtmlx-gantt/codebase/dhtmlxgantt.js'
import { useRoute } from "vue-router"
import{getItem, delItem} from "@/api/project/item.js";
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

import {addDefect, delDefect, getDefect, listDefect, updateDefect} from "@/api/project/defect.js";
import {addItem, listItem, updateItem} from "@/api/project/item.js";
const userStore = useUserStore()
const route = useRoute()
const projectId = route.params.id

const reqList = ref([])
const total = ref(0)
const isEdit = ref(false)
const firstData = ref(null)
const addButten = ref(false)
const username = ref(null)
const loading = ref(false)


const id = userStore.id
const Params = ref({
  pageNum: 1,
  pageSize: 10,
  title: '',
  description: '',
  type: '',
  status: '',
  priority: '',
  startDate: '',
  dueDate: '',
  actualEffort: '',
  estimatedEffort: '',
  assignedTo: '',
  projectId: projectId,
  iterationId: '',
  parentWorkItemId: '',
  relatedDefectId: '',
  relatedRequirementId: '',
  remark: ''
})
const dialogVisible = ref(false)
const curReq = ref(null)
const formRef = ref(null)
const reqForm = ref({
  title: '',
  description: '',
  type: '',
  status: '',
  priority: '',
  startDate: '',
  dueDate: '',
  estimatedEffort: '',
  assignedTo: '',
  projectId: projectId,
  iterationId: null,
  parentWorkItemId: null,
  relatedDefectId: null,
  relatedRequirementId: null,
  remark: ''
})
const addFormRef = ref(null)
const delButten = ref(false)
let ganttInstance = null









const gettheUser = async () => {
  const res = await getUser(id)
  username.value = res.data.userName

}

const getReqs = async () => {
  loading.value = true

  const a = await listItem(Params.value)
  console.log("Defects", a)
  reqList.value = a.rows
  total.value = a.total


  loading.value = false

}

const clearAll = () => {
  Params.value = {
    pageNum: 1,
    pageSize: 10,
    title: '',
    priority: '',
    status: '',
    reportedBy: '',
    resolvedAt: '',
    actualFixVersion: '',
    expectedFixVersion: '',
    relatedRequirementId: '',
    assignedTo: '',
    projectId: projectId,
    severity: '',

  }

  getReqs()
}


const addReq = () => {
  addButten.value = true
  reqForm.value = {
    assignedTo: '',
    description: '',
    remarks: '',
    projectId: projectId,
    reportedBy: id,
    actualFixVersion: '',
    expectedFixVersion: '',
    relatedRequirementId: '',
    title: '',
    priority: '',
    status: 'pending',
    severity: '',

  }
}

const submitAdd = async () => {
  if (!addFormRef.value) return
  await addFormRef.value.validate()
  loading.value = true
  await addItem(reqForm.value)
  addButten.value = false
  await getReqs()
  loading.value = false

}

const clickV = async (row) => {
  loading.value = true
  const res = await getItem(row.workItemId)
  curReq.value = res.data
  firstData.value = JSON.parse(JSON.stringify(res.data))
  isEdit.value = false
  delButten.value = curReq.value.createBy === username.value
  dialogVisible.value = true
  loading.value = false
}

const ISEDITT = () => {
  isEdit.value = true
}

const donotEDITT = () => {
  curReq.value = JSON.parse(JSON.stringify(firstData.value))
  isEdit.value = false
}

const submitEdit = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  loading.value = true
  await updateItem(curReq.value)
  isEdit.value = false
  dialogVisible.value = false
  await getReqs()
  loading.value = false

}

const delReq = async (row) => {
  loading.value = true
  await delItem(row.defectId)
  dialogVisible.value = false
  await getReqs()
  loading.value = false
}

const sizeP = (val) => {
  Params.value.pageSize = val
  getReqs()
}

const cP = (val) => {
  Params.value.pageNum = val
  getReqs()
}


onMounted(() => {
  getReqs()
  gettheUser()
})



</script>

<style scoped>
.r-cards {
  margin: 16px;
  min-height: calc(100vh - 150px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.count-tag {
  font-size: 13px;
}

.search {
  margin-bottom: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.sF {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.rList {
  margin-top: 16px;
}
@media screen and (max-width: 768px) {
  .sF {
    :deep(.el-form-item) {
      margin-bottom: 16px;
    }
  }
}

:deep(.el-form-item__label) {
  font-weight: normal;
}

:deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
}

:deep(.el-card__body) {
  padding: 20px;
}
.completed-task {
  background-color: #67C23A;
}
.progress-task {
  background-color: #409EFF;
}
.pending-task {
  background-color: #909399;
}
</style>