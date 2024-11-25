<template>
  <div>
    <el-card>
      <div class="toolbar">
        <el-button type="primary" @click="doAdd">Create Work Item</el-button>
      </div>

      <el-table :data="list" v-loading="loading">
<!--        <el-table-column label="ID" prop="workItemId" width="80" align="center"/>-->
        <el-table-column label="Title" prop="title" width="300" align="center"/>
        <el-table-column label="Type" prop="type" width="200" align="center"/>
        <el-table-column label="Status" prop="status" width="150" align="center"/>
        <el-table-column label="Priority" prop="priority" width="100" align="center"/>
        <el-table-column label="Assigned" prop="userName" width="200" align="center"/>
        <el-table-column label="Start Date" width="180">
          <template #default="scope">
            <el-date-picker v-model="scope.row.startDate" type="date" size="small" style="width: 130px" @change="changedate2(scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column label="Due Date" width="180">
          <template #default="scope">
            <el-date-picker v-model="scope.row.dueDate" type="date" size="small" style="width: 130px" @change="changeDate(scope.row)"/>
          </template>
        </el-table-column>
        <el-table-column label="Estimated" prop="estimatedEffort" width="100"/>
        <el-table-column label="Actual" width="200">
          <template #default="scope">
            <el-input-number v-model="scope.row.actualEffort" :precision="1" :step="0.5" @change="changePoint1(scope.row)" size="small" style="width: 90px"/>
          </template>
        </el-table-column>
        <el-table-column label="Operations" align="center" width="100" >
          <template #default="{ row }">
            <el-button size="small" type="danger" class="w-100"
                       @click="delWorkItem(row)" :icon="Delete" v-if="booladmin">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
    
      <!-- <div id="ganttContainer" style="height: 500px;"></div> -->

    </el-card>

    <el-dialog title="Create Work Item" v-model="addDialog" width="600px">
      <el-form :model="form" label-width="120px">
        <el-form-item label="Title" required>
          <el-input v-model="form.title" placeholder="Please input title"/>
        </el-form-item>

        <el-form-item label="Type" required>
          <el-select v-model="form.type" placeholder="Select type">
            <el-option label="Development Task" value="Development Task"/>
            <el-option label="Test Task" value="Test Task"/>
            <el-option label="Design Task" value="Design Task"/>
          </el-select>
        </el-form-item>


        <el-form-item label="Assigned To" required>
          <el-select v-model="form.assignedTo" placeholder="Select member">
            <el-option v-for="member in members" :key="member.userId" :label="member.userName" :value="member.userId"/>
          </el-select>
        </el-form-item>

        <el-form-item label="Iteration">
          <el-select v-model="form.iterationId" placeholder="Select iteration">
            <el-option v-for="iter in iterations" :key="iter.iterationId" :label="iter.iterationName" :value="iter.iterationId"/>
          </el-select>
        </el-form-item>

        <el-form-item label="Related Req.">
          <el-select v-model="form.relatedRequirementId" placeholder="Select requirement">
            <el-option v-for="req in requirements" :key="req.requirementId" :label="req.title" :value="req.requirementId"/>
          </el-select>
        </el-form-item>

        <el-form-item label="Priority" required>
          <el-select v-model="form.priority" placeholder="Select priority">
            <el-option label="P1" :value="1"/>
            <el-option label="P2" :value="2"/>
            <el-option label="P3" :value="3"/>
            <el-option label="P4" :value="4"/>
          </el-select>
        </el-form-item>

        <el-form-item label="Status" required>
          <el-select v-model="form.status" placeholder="Select status">
            <el-option label="Not Started" value="Not Started"/>
            <el-option label="In Progress" value="In Progress"/>
            <el-option label="Completed" value="Completed"/>
          </el-select>
        </el-form-item>

        <el-form-item label="Start Date" required>
          <el-date-picker v-model="form.startDate" type="date" placeholder="Select date"/>
        </el-form-item>

        <el-form-item label="Due Date" required>
          <el-date-picker v-model="form.dueDate" type="date" placeholder="Select date"/>
        </el-form-item>

        <el-form-item label="Est. Effort">
          <el-input-number v-model="form.estimatedEffort" :precision="1" :step="0.5"/>
        </el-form-item>

        <el-form-item label="Description">
          <el-input v-model="form.description" type="textarea" rows="3"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="addDialog = false">Cancel</el-button>
        <el-button type="primary" @click="doADD2">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useRoute} from 'vue-router'
import {listItem, addItem, updateItem, delItem} from '@/api/project/item'
import {listProjectMember} from '@/api/project/member'
import {listRequirement} from '@/api/project/requirements'
import {listIteration} from '@/api/project/iteration'
import {addNotice} from "@/api/notice/noticeapi.js";
import { listRole } from '@/api/system/role.js'
import { gantt } from 'dhtmlx-gantt'
import 'dhtmlx-gantt/codebase/dhtmlxgantt.css'
import {Delete, Edit} from "@element-plus/icons-vue";
import {delTask} from "@/api/project/tasks.js";
import {ElMessageBox} from "element-plus";
const booladmin=ref(false)
const route = useRoute()
const projectId = route.params.id

const list = ref([])
const total = ref(0)
const loading = ref(false)
const members = ref([])
const iterations = ref([])
const requirements = ref([])
const addDialog = ref(false)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  projectId: projectId
})

const form = ref({
  title: '',
  type: '',
  assignedTo: null,
  iterationId: null,
  relatedRequirementId: null,
  priority: null,
  status: '',
  startDate: '',
  dueDate: '',
  estimatedEffort: 0,
  description: '',
  projectId: projectId
})

async function getList() {
  loading.value = true
  const res = await listItem(queryParams.value)
  // console.log(res)
  list.value = res.rows
  total.value = res.total
  loading.value = false
}
async function delWorkItem(row) {
  await ElMessageBox.confirm(
      'This action will permanently delete this Work Item. Continue?',
      'Warning',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
  await delItem(row.workItemId);
  getList();

}

async function getAllProjectData() {
  const memR = await listProjectMember({projectId: projectId})
  members.value = memR.rows
  // console.log(memR)
  const iterR = await listIteration({projectId: projectId})
  iterations.value = iterR.rows
//console.log(iterR)
  const reqR = await listRequirement({projectId: projectId})
  requirements.value = reqR.rows
  // console.log(reqR)
}
const checkRoles = async () => {

  const res = await listRole()
  console.log('res:', res)
  booladmin.value = res.rows.some(role => role.roleKey === 'admin')

}
async function changePoint1(row) {
  await updateItem({
    workItemId: row.workItemId,
    actualEffort: row.actualEffort})

}

async function changedate2(row) {
  await updateItem({
    workItemId: row.workItemId,
    startDate: row.startDate
  })
}

async function changeDate(row) {
  await updateItem({
    workItemId: row.workItemId,
    dueDate: row.dueDate
  })

}
function doAdd() {
  addDialog.value = true
  form.value = {
    title: '',
    type: '',
    assignedTo: null,
    iterationId: null,
    relatedRequirementId: null,
    priority: null,
    status: '',
    startDate: '',
    dueDate: '',
    estimatedEffort: 0,
    description: '',
    projectId: projectId
  }
}

async function doADD2() {
    await addItem(form.value)
    addDialog.value = false
    getList()
  const assignedUser = members.value.find(m => m.userId === form.value.assignedTo)
  await addNotice({
    sysNotice: {
      noticeTitle: 'NewWorkItem',
      noticeContent: `newworkitem:${form.value.title}`,
      noticeType: '1',
    },
    userIds: [assignedUser.userId]
  })
}


onMounted(() => {
  getAllProjectData()
  checkRoles()
  getList()
})


</script>