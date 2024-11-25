<template>
  <el-card class="r-cards">
    <template #header><div class="card-header">
      <div class="header-left"><span class="title">Defects List</span><el-tag class="count-tag" type="info">total {{ total }} defectId</el-tag>
      </div>
      <div class="addR">
        <el-button type="primary" :icon="Plus" @click="addReq"></el-button>
      </div></div></template>

    <div class="search"><el-form :inline="true" :model="Params" class="sF"><el-form-item label="">
      <el-input v-model="Params.title" placeholder="" clearable style="width: 200px"/>
    </el-form-item>

      <el-form-item>
        <el-button type="primary" :icon="Search" @click="getReqs">Select</el-button><el-button :icon="Refresh" @click="clearAll">Refresh</el-button>
      </el-form-item>
    </el-form>
    </div>
    <div class="rList">
      <el-table v-loading="loading" :data="reqList" style="width: 100%">
<!--        <el-table-column label="ID" align="center">-->
<!--          <template #default="{ row }"><el-link type="primary" @click="clickV(row)">{{ row.defectId }}</el-link>-->
<!--          </template>-->
<!--        </el-table-column>-->
        <el-table-column label="Title" align="center" prop="title"/>
        <el-table-column label="Description" align="center" prop="description" show-overflow-tooltip />
        <el-table-column label="Priority" align="center" prop="priority"></el-table-column>
        <el-table-column label="Status" align="center" prop="status"></el-table-column>
        <el-table-column label="CreateTime" align="center" prop="createTime" width="180" />
        <el-table-column label="Operations" align="center" width="180" >
          <template #default="{ row }">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-button size="small" type="primary" class="w-100"
                           @click="clickV(row)" :icon="Edit" v-if="booladmin">Edit
                </el-button>
              </el-col>
              <el-col :span="12">
                <el-button size="small" type="danger" class="w-100"
                           @click="delReq(row)" :icon="Delete" v-if="booladmin">Delete</el-button>
              </el-col>
            </el-row>
          </template>
        </el-table-column>

      </el-table>
      <div class="pagination-container">
        <el-pagination :page-sizes="[10, 20, 50, 100]" :total="total" v-model:current-page="Params.pageNum" v-model:page-size="Params.pageSize"
                       layout="total, sizes, prev, pager, next, jumper" @size-change="sizeP" @current-change="cP"/>
      </div>
    </div>
  </el-card>

  <el-dialog v-model="boolVis1" title="Requirement Details" width="60%">
    <el-form ref="formRef" :model="curReq"  label-width="120px">
      <el-form-item label="Requirement ID" prop="requirementId"><span>{{ curReq?.defectId }}</span></el-form-item>
      <el-form-item label="Title" prop="title"><el-input v-model="curReq.title" :disabled="!isEdit"/>
      </el-form-item><el-form-item label="Priority" prop="priority">
      <el-select v-model="curReq.priority" :disabled="!isEdit">
        <el-option label="High - 1" value="1" /><el-option label="Medium - 2" value="2" /><el-option label="Low - 3" value="3" /></el-select>
    </el-form-item><el-form-item label="Status" prop="status">
      <el-select v-model="curReq.status" :disabled="!isEdit"><el-option label="pending" value="pending" /><el-option label="processing" value="processing" /><el-option label="fixed" value="fixed" />
      </el-select>
    </el-form-item>
      <el-form-item label="Type" prop="type"><el-input v-model="curReq.type" :disabled="!isEdit"/></el-form-item>
      <el-form-item label="Assigned To" prop="assignedTo"><el-input v-model="curReq.assignedTo" :disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="Description" prop="description"><el-input v-model="curReq.description" type="textarea" :rows="3":disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="Remarks" prop="remarks">
        <el-input v-model="curReq.remarks" type="textarea" :rows="3" :disabled="!isEdit"/></el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="boolVis1  = false">Exit</el-button>
<!--        <el-button v-if="curReq?.createBy === username" type="danger" @click="delReq(curReq)">Delete</el-button>-->
        <el-button v-if="!isEdit" type="primary" @click="ISEDITT">Edit</el-button>
        <template v-else>
          <el-button @click="donotEDITT">Cancel</el-button>
          <el-button type="primary" @click="doEDIT">Save</el-button>
        </template>
      </span>
    </template>
  </el-dialog>

  <el-dialog v-model="addButten" title="Add New Defeat" width="60%">
    <el-form ref="addFormRef" :model="reqForm" :rules="rule1" label-width="120px">
      <el-form-item label="Title" prop="title">
        <el-input v-model="reqForm.title" placeholder="title"/>
      </el-form-item>
      <el-form-item label="Priority" prop="priority">
        <el-select v-model="reqForm.priority" placeholder="priority">
          <el-option label="High" value="1" />
          <el-option label="Medium" value="2" />
          <el-option label="Low" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="Severity" prop="severity">
        <el-select v-model="reqForm.severity" placeholder="severity">
          <el-option label="Critical" value="4" /><el-option label="Major" value="3" /><el-option label="Minor" value="2" />
          <el-option label="Trivial" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-select v-model="reqForm.status" placeholder="Select status"><el-option label="Pending" value="pending" /><el-option label="Processing" value="processing" /><el-option label="Fixed" value="fixed" /><el-option label="To Verify" value="to_verify" /><el-option label="Closed" value="closed" />
          <el-option label="Reopened" value="reopened" /><el-option label="Rejected" value="rejected" />
        </el-select>
      </el-form-item>
      <el-form-item label="Assigned To" prop="assignedTo"><el-input v-model="reqForm.assignedTo" placeholder="Assignto"/></el-form-item>

      <el-form-item label="Description" prop="description">
        <el-input v-model="reqForm.description" type="textarea" :rows="3" placeholder="description"/>
      </el-form-item>

      <el-form-item label="Expected Version" prop="expectedFixVersion">
        <el-input v-model="reqForm.expectedFixVersion" placeholder="expectedFixVersion"/>
      </el-form-item>

      <el-form-item label="Related Req ID" prop="relatedRequirementId">
        <el-input v-model="reqForm.relatedRequirementId" placeholder="relatedRequirementId"/>
      </el-form-item>

      <el-form-item label="Remarks" prop="remarks">
        <el-input v-model="reqForm.remarks" type="textarea" :rows="3" placeholder="Additional remarks"
        />
      </el-form-item>
    </el-form>

    <template #footer>
    <span class="dialog-footer"><el-button @click="addButten = false">Cancel</el-button>
      <el-button type="primary" @click="doAdd">Add</el-button>
    </span>
    </template>
  </el-dialog>


</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import {Search, Refresh, Plus, Download, Edit, Document, EditPen, Delete} from '@element-plus/icons-vue'
import 'dhtmlx-gantt/codebase/dhtmlxgantt.css'
import 'dhtmlx-gantt/codebase/dhtmlxgantt.js'
import { useRoute } from "vue-router"
import { listRole } from '@/api/system/role.js'
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
const userStore = useUserStore()
const route = useRoute()
const projectId = route.params.id
const booladmin = ref(false)
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
  priority: '',
  status: '',
  assignedTo: '',
  projectId: projectId, // 109
  severity: '',
  reportedBy: '',
  resolvedAt: '',
  actualFixVersion: '',
  expectedFixVersion: '',
  relatedRequirementId: ''
})
const boolVis1 = ref(false)
const curReq = ref(null)
const formRef = ref(null)
const reqForm = ref({
  title: '',
  priority: '',
  status: 'pending',
  severity: '',
  assignedTo: '',
  description: '',
  environment: '',
  expectedFixVersion: '',
  projectId: projectId,
  relatedRequirementId: '',
  remarks: '',
  reportedBy: id,
  reproductionSteps: '',
})
const addFormRef = ref(null)
const delButten = ref(false)






const gettheUser = async () => {
  const res = await getUser(id)
  username.value = res.data.userName

}

const getReqs = async () => {
  loading.value = true
  const a = await listDefect(Params.value)
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

const checkRoles = async () => {

  const res = await listRole()
  console.log('res:', res)
  booladmin.value = res.rows.some(role => role.roleKey === 'admin')

}
const addReq = () => {
  addButten.value = true
  reqForm.value = {
    title: '',
    priority: '',
    status: 'pending',
    severity: '',
    assignedTo: '',
    description: '',
    remarks: '',
    projectId: projectId,
    reportedBy: id,
    actualFixVersion: '',
    expectedFixVersion: '',
    relatedRequirementId: ''
  }
}


const doAdd = async () => {
  if (!addFormRef.value) return
  await addFormRef.value.validate()
  loading.value = true
  await addDefect(reqForm.value)
  addButten.value = false
  await getReqs()
  loading.value = false

}


const clickV = async (row) => {
  loading.value = true
  const res = await getDefect(row.defectId)
  curReq.value = res.data
  firstData.value = JSON.parse(JSON.stringify(res.data))
  isEdit.value = false
  delButten.value = curReq.value.createBy === username.value
  boolVis1.value = true
  loading.value = false
}

const ISEDITT = () => {
  isEdit.value = true
}

const donotEDITT = () => {
  curReq.value = JSON.parse(JSON.stringify(firstData.value))
  isEdit.value = false
}

const doEDIT = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  loading.value = true
  await updateDefect(curReq.value)
  isEdit.value = false
  boolVis1.value = false
  await getReqs()
  loading.value = false

}

const delReq = async (row) => {
  await ElMessageBox.confirm(
      'This action will permanently delete this defect. Continue?',
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
  checkRoles()
})

const rule1 = {
  severity: [
    { required: true, message: 'severity', trigger: 'change' }
  ],
  status: [
    { required: true, message: 'status', trigger: 'change' }
  ],
  priority: [
    { required: true, message: 'priority', trigger: 'change' }
  ],
  title: [
    { required: true, message: 'title', trigger: 'blur' }
  ]

}

</script>

<style scoped>
@import "./scss/defect.scss";
</style>