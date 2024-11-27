<template>
  <el-card class="r-cards">
    <template #header>
      <div class="card-header">
          <div class="header-left">
            <span class="title">Requirement List</span>
            <el-tag class="count-tag" type="info">total {{ total }} requirements</el-tag>
          </div>
          <div class="addR">
            <el-button type="primary" :icon="Plus" @click="addReq">New</el-button>
            <el-button type="success" :icon="TrendCharts" @click="showGantt">Gantt Chart</el-button>
            <el-button type="primary" :icon="Download" @click="downloadRequirementList()">Download</el-button>
          </div>
        </div>
    </template>

    <div class="search">
      <el-form :inline="true" :model="Params" class="sF">
        <el-form-item label="requirements">
          <el-input v-model="Params.title" placeholder="" clearable style="width: 200px"/>
        </el-form-item>
        <el-form-item label="priority">
          <el-select v-model="Params.priority" placeholder="select priority" clearable style="width: 200px">
            <el-option label="high" value="1"/>
            <el-option label="medium" value="2"/>
            <el-option label="low" value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item label="status">
          <el-select v-model="Params.status" placeholder="status" clearable style="width: 200px">
            <el-option label="pending" value="pending"/>
            <el-option label="processing" value="processing"/>
            <el-option label="completed" value="completed"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="getReqs">select</el-button>
          <el-button :icon="Refresh" @click="clearAll">Refresh</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="rList">
      <el-table v-loading="loading" :data="reqList" style="width: 100%">
<!--        <el-table-column label="ID" align="center" prop="requirementId">-->
<!--          <template #default="{ row }">-->
<!--            <el-link type="primary" @click="clickV(row)">{{ row.requirementId }}</el-link>-->
<!--          </template>-->
<!--          <template #header><span class="header-text">ID</span></template>-->
<!--        </el-table-column>-->
        <el-table-column label="title" align="center" prop="title"/>
        <el-table-column label="description" align="center" prop="description" show-overflow-tooltip/>
        <el-table-column label="priority" align="center" prop="priority"></el-table-column>
        <el-table-column label="status" align="center" prop="status"></el-table-column>
        <el-table-column label="createTime" align="center" prop="createTime" width="180"/>
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
                           @click="delReq(row)"  v-if="booladmin">Delete</el-button>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination :page-sizes="[10, 20, 50, 100]" :total="total" v-model:current-page="Params.pageNum"
                       v-model:page-size="Params.pageSize"
                       layout="total, sizes, prev, pager, next, jumper" @size-change="sizeP" @current-change="cP"/>
      </div>
    </div>
  </el-card>

  <el-dialog v-model="visibButten" title="requirement details" width="60%">
    <el-form ref="formRef" :model="curReq" :rules="rules" label-width="120px">
      <el-form-item label="requirement ID" prop="requirementId">
        <span>{{ curReq?.requirementId }}</span>
      </el-form-item>
      <el-form-item label="title" prop="title">
        <el-input v-model="curReq.title" :disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="priority" prop="priority">
        <el-select v-model="curReq.priority" :disabled="!isEdit">
          <el-option label="high" value="1"/>
          <el-option label="medium" value="2"/>
          <el-option label="low" value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item label="status" prop="status">
        <el-select v-model="curReq.status" :disabled="!isEdit">
          <el-option label="pending" value="pending"/>
          <el-option label="processing" value="processing"/>
          <el-option label="completed" value="completed"/>
        </el-select>
      </el-form-item>
      <el-form-item label="type" prop="type">
        <el-input v-model="curReq.type" :disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="assignedTo" prop="assignedTo">
        <el-input v-model="curReq.assignedTo" :disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="description" prop="description">
        <el-input v-model="curReq.description" type="textarea" :rows="3" :disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="remarks" prop="remarks">
        <el-input v-model="curReq.remarks" type="textarea" :rows="3" :disabled="!isEdit"/>
      </el-form-item>
    </el-form>

    <template #footer>
      <span class="dialog-footer">
        <el-button @click="visibButten  = false">Cancel</el-button>
<!--        <el-button v-if="curReq?.createBy === username" type="danger" @click="delReq(curReq)">-->
<!--          Delete-->
<!--        </el-button>-->
        <el-button v-if="!isEdit" type="primary" @click="ISEDITT">
          Click To Edit
        </el-button>
        <template v-else>
          <el-button type="primary" @click="doEdit">Save</el-button>
        </template>
      </span>
    </template>
  </el-dialog>

  <el-dialog v-model="addButten" title="add new requirements" width="60%">
    <el-form ref="addFormRef" :model="reqForm" :rules="rules" label-width="120px">
      <el-form-item label="title" prop="title">
        <el-input v-model="reqForm.title" placeholder="title"/>
      </el-form-item>
      <el-form-item label="priority" prop="priority">
        <el-select v-model="reqForm.priority" placeholder="priority">
          <el-option label="high" value="1"/>
          <el-option label="medium" value="2"/>
          <el-option label="low" value="3"/>
        </el-select>
      </el-form-item>
      <el-form-item label="DueDate" prop="dueDate">
        <el-date-picker v-model="reqForm.dueDate" type="datetime" placeholder="Select due date"
                        format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>
      <el-form-item label="type" prop="type">
        <el-input v-model="reqForm.type" placeholder="type"/>
      </el-form-item>

      <el-form-item label="assignedTo" prop="assignedTo">
        <el-input v-model="reqForm.assignedTo" placeholder="assignedTo"/>
      </el-form-item>

      <el-form-item label="description" prop="description">
        <el-input v-model="reqForm.description" type="textarea" placeholder="description"/>
      </el-form-item>

      <el-form-item label="remarks" prop="remarks">
        <el-input v-model="reqForm.remarks" type="textarea" :rows="3" placeholder="remarks"/>
      </el-form-item>
    </el-form>

    <template #footer>
    <span class="dialog-footer"><el-button @click="addButten = false">cancel</el-button>
      <el-button type="primary" @click="doAdd">ADD</el-button>
    </span>
    </template>
  </el-dialog>
  <el-dialog v-model="ganttButten" title="Gantt Chart" width="90%" :destroy-on-close="true" :close-on-click-modal="false"
             :close-on-press-escape="false" @opened="showGantt" @close="destroyGantt">
    <div ref="ganttContainer" style="height: 600px; width: 100%;"></div>
  </el-dialog>

</template>

<script setup>
import {ref, onMounted, onUnmounted, watch, nextTick} from 'vue'
import {Search, Refresh, Plus, Download, Edit, Delete, TrendCharts} from '@element-plus/icons-vue'
import 'dhtmlx-gantt/codebase/dhtmlxgantt.css'
import 'dhtmlx-gantt/codebase/dhtmlxgantt.js'
import {useRoute} from "vue-router"
import {
  addRequirement,
  delRequirement, exportRequirement,
  getRequirementDetail,
  listRequirement,
  updateRequirement
} from "@/api/project/requirements.js"
import { listRole } from '@/api/system/role.js'
import useUserStore from "@/store/modules/user.js"
import {getUser} from "@/api/system/user.js"
import {gantt} from "dhtmlx-gantt"
import {ElMessage, ElMessageBox} from "element-plus";
const booladmin=ref(false)
const userStore = useUserStore()
const route = useRoute()
const projectId = route.params.id
const ganttContainer = ref(null)
const ganttButten = ref(false)
const reqList = ref([])
const total = ref(0)
const isEdit = ref(false)
const firstData = ref(null)
const addButten = ref(false)
const username = ref(null)
const loading = ref(false)
const reqForm = ref({
  title: '',
  priority: '',
  status: 'processing',
  type: '',
  assignedTo: '',
  description: '',
  dueDate: '',
  remarks: '',
  projectId: projectId,
  delFlag: 1,
  createBy: username.value,
})
const checkRoles = async () => {

  const res = await listRole()
  console.log('res:', res)
  booladmin.value = res.rows.some(role => role.roleKey === 'admin')

}
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
const visibButten = ref(false)
const curReq = ref(null)
const formRef = ref(null)

const addFormRef = ref(null)
const delButten = ref(false)
let ganttInstance = null


const initGantt = () => {
  gantt.config.autosize = "y"
  gantt.config.date_format = "%Y-%m-%d"
  gantt.config.task_height = 20
  gantt.config.scale_height = 50
  gantt.config.row_height = 40
  gantt.config.scales = [
    {unit: "month", step: 1, format: "%F %Y"},
    {unit: "week", step: 1, format: "Week #%W"},
    {unit: "day", step: 1, format: "%d %D"}
  ]


  gantt.config.columns = [
    {name: "text", label: "RequirementName", width: 200, tree: true},
    {name: "start_date", label: "StartDate", align: "center", width: 100},
    {name: "end_date", label: "EndDate", align: "center", width: 100},
    {

      name: "progress",
      label: "Progress",
      align: "center",
      width: 80,
      template: (obj) => (obj.progress * 100).toFixed(0) + "%"
    },
    {
      name: "status",
      label: "Status",
      align: "center",
      width: 80,
      template: (obj) => {
        const statusMap = {
          'completed': '<span style="color: #67C23A">Completed</span>',
          'processing': '<span style="color: #409EFF">In Progress</span>',
          'pending': '<span style="color: #909399">Pending</span>'

        }
        return statusMap[obj.status] || obj.status
      }
    }
  ]

  gantt.templates.task_class = (start, end, task) => {
    switch (task.status) {
      case 'completed':
        return 'completed-task'
      case 'processing':
        return 'progress-task'
      default:
        return 'pending-task'
    }

  }

  gantt.templates.tooltip_text = (start, end, task) => {
    return `<b>Task:</b> ${task.text}<br/>
            <b>Start:</b> ${gantt.date.date_to_str("%Y-%m-%d")(start)}<br/>
            <b>End:</b> ${gantt.date.date_to_str("%Y-%m-%d")(end)}<br/>
            <b>Progress:</b> ${(task.progress * 100).toFixed(0)}%<br/>
            <b>Status:</b> ${task.status}`
  }
}


const showGantt = async () => {
  ganttButten.value = true
  await nextTick()
  if (ganttContainer.value) {
    gantt.clearAll()
    initGantt()
    gantt.init(ganttContainer.value)
    loadGanttData()
  }


}


const rules = {
  title: [{required: true, message: 'Please input title', trigger: 'blur'}],
  priority: [{required: true, message: 'Please select priority', trigger: 'change'}],
  status: [{required: true, message: 'Please select status', trigger: 'change'}],
}


const gettheUser = async () => {
  const res = await getUser(id)
  username.value = res.data.userName

}

const getReqs = async () => {
  loading.value = true
  const a = await listRequirement(Params.value)
  reqList.value = a.rows
  total.value = a.total
  loading.value = false

}

const clearAll = () => {
  Params.value = {
    pageNum: 1,
    pageSize: 10,
    title: '',
    delFlag: 1,
    type: '',
    priority: '',
    status: '',
    assignedTo: '',
    projectId: projectId
  }


  getReqs()
}
const loadGanttData = () => {
  const tasks = {
    data: reqList.value.map(req => ({
      id: req.requirementId,
      text: req.title,
      start_date: req.createTime ? new Date(req.createTime) : new Date(),
      end_date: req.endTime ? new Date(req.endTime) : new Date(new Date().setDate(new Date().getDate() + 7)),
      progress: req.status === 'completed' ? 1 :
          req.status === 'processing' ? 0.5 : 0,
      status: req.status
    }))
  }
  gantt.parse(tasks)
}


const destroyGantt = () => {
  if (ganttInstance) {
    ganttInstance.clearAll()
    ganttInstance = null
  }
}


const addReq = () => {
  addButten.value = true
  reqForm.value = {
    title: '',
    priority: '',
    status: 'pending',
    type: '',
    assignedTo: '',
    description: '',
    remarks: '',
    projectId: projectId,
    delFlag: 1,
    createBy: username.value,
  }
}

const doAdd = async () => {
  if (!addFormRef.value) return
  await addFormRef.value.validate()
  loading.value = true
  await addRequirement(reqForm.value)
  addButten.value = false
  await getReqs()
  loading.value = false
}


const clickV = async (row) => {
  loading.value = true
  const res = await getRequirementDetail(row.requirementId)
  curReq.value = res.data
  firstData.value = JSON.parse(JSON.stringify(res.data))
  isEdit.value = false
  delButten.value = curReq.value.createBy === username.value
  visibButten.value = true
  loading.value = false
}

const ISEDITT = () => {
  isEdit.value = true
}

const donotEDITT = () => {
  curReq.value = JSON.parse(JSON.stringify(firstData.value))
  isEdit.value = false
}

const doEdit = async row => {
  if (!formRef.value) return
  await formRef.value.validate()
  loading.value = true
  await updateRequirement(curReq.value)
  isEdit.value = false
  visibButten.value = false
  await getReqs()
  loading.value = false

}

const delReq = async (row) => {
  await ElMessageBox.confirm(
      'This action will permanently delete this Requirement. Continue?',
      'Warning',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
  loading.value = true
  await delRequirement(row.requirementId)
  visibButten.value = false
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

watch(ganttButten, (newVal) => {
  if (!newVal) {
    nextTick(() => {
      destroyGantt()
    })
  }
})

// 下载需求列表
const downloadRequirementList = async () => {
  const query = {
    projectId: projectId
  }
  const res = await exportRequirement(query)
  const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
  const href = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = href
  link.download = `project_requirement_list.xlsx`
  link.click()
}

onUnmounted(() => {
  if (gantt) {
    gantt.clearAll()
  }
})
</script>

<style scoped>
@import "req.scss";
</style>