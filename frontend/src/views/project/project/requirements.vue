<template>
  <el-card class="r-cards">
    <template #header>
      <div class="card-header">
        <div class="header-left">
          <span class="title">Requirement List</span>
          <el-tag class="count-tag" type="info">total {{ total }} requirements</el-tag>
        </div>
        <div class="addR">
          <el-button type="primary" :icon="Plus" @click="showDialog">New</el-button>
          <el-button type="success" :icon="TrendCharts" @click="showGantt">Gantt Chart</el-button>
          <el-button type="primary" :icon="Download" @click="downloadRequirementList()">Download</el-button>
        </div>
      </div>
    </template>

    <div class="search">
      <el-form :inline="true" :model="Params" class="sF">
        <div class="search-bar" style="margin-right: 20px">
          <el-input v-model="searchQuery" placeholder="Search requirements..."
                    clearable style="width: 210px; margin-bottom: 20px;">
            <template #prefix>
              <el-icon><search/></el-icon>
            </template>
          </el-input>
        </div>
        <el-form-item label="Priority">
          <el-select v-model="Params.priority" placeholder="select priority"
                     clearable style="width: 150px">
            <el-option label="High" value="1"/>
            <el-option label="Medium" value="2"/>
            <el-option label="Low" value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="Params.status" placeholder="status" clearable style="width: 150px">
            <el-option label="Pending" value="pending"/>
            <el-option label="Processing" value="processing"/>
            <el-option label="Completed" value="completed"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="getReqs">select</el-button>
          <el-button :icon="Refresh" @click="clearAll">Refresh</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="rList">
      <el-table v-loading="loading" :data="filteredReqList" style="width: 100%">
        <el-table-column label="Title" align="center" prop="title"/>
        <el-table-column label="Description" align="center" prop="description" show-overflow-tooltip/>
        <el-table-column label="Priority" align="center">
          <template #default="{ row }">
            {{ priorityMap[row.priority] }}
          </template>
        </el-table-column>
        <el-table-column label="Status" align="center" prop="status"></el-table-column>
        <el-table-column label="Create Time" align="center" prop="createTime" width="180"/>
        <el-table-column label="Operations" align="center" width="180">
          <template #default="{ row }">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-button size="small" type="primary" class="w-100" @click="clickV(row)" :icon="Edit" v-if="booladmin">Edit</el-button>
              </el-col>
              <el-col :span="12">
                <el-button size="small" type="danger" class="w-100" @click="delReq(row)" v-if="booladmin">Delete</el-button>
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

  <el-dialog v-model="ganttButten" title="Gantt Chart" width="90%" :destroy-on-close="true" :close-on-click-modal="false"
             :close-on-press-escape="false" @opened="showGantt" @close="destroyGantt">
    <div ref="ganttContainer" style="height: 600px; width: 100%;"></div>
  </el-dialog>

  <el-dialog :title="title" v-model="show" width="500px" @close="rF1">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
      <el-form-item label="Title" prop="title">
        <el-input v-model="form.title" placeholder="Please input requirement title" />
      </el-form-item>
      <el-form-item label="Description" prop="description">
        <el-input type="textarea" v-model="form.description" placeholder="Please input requirement description" />
      </el-form-item>
      <el-form-item label="Priority" prop="priority">
        <el-select v-model="form.priority" placeholder="Select priority">
          <el-option label="High" value="1" />
          <el-option label="Medium" value="2" />
          <el-option label="Low" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-select v-model="form.status" placeholder="Select status">
          <el-option label="Pending" value="pending" />
          <el-option label="Processing" value="processing" />
          <el-option label="Completed" value="completed" />
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="show = false" style="margin-left: 280px">Cancel</el-button>
      <el-button type="primary" @click="doAdd('formRef')">Confirm</el-button>
    </div>
  </el-dialog>

  <el-dialog title="Edit Requirement" v-model="showForEdit" width="500px" @close="rF1">
    <el-form ref="formRef" :model="curReq" :rules="rules" label-width="100px">
      <el-form-item label="Title" prop="title">
        <el-input v-model="curReq.title" placeholder="Please input requirement title" />
      </el-form-item>
      <el-form-item label="Description" prop="description">
        <el-input type="textarea" v-model="curReq.description" placeholder="Please input requirement description" />
      </el-form-item>
      <el-form-item label="Priority" prop="priority">
        <el-select v-model="curReq.priorityLabel" :placeholder="curReq.priorityLabel">
          <el-option label="High" value="1" />
          <el-option label="Medium" value="2" />
          <el-option label="Low" value="3" />
        </el-select>
      </el-form-item>


      <el-form-item label="Status" prop="status">
        <el-select v-model="curReq.status" placeholder="Select status">
          <el-option label="Pending" value="pending" />
          <el-option label="Processing" value="processing" />
          <el-option label="Completed" value="completed" />
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="showForEdit = false" style="margin-left: 280px">Cancel</el-button>
      <el-button type="primary" @click="doEdit('formRef')">Confirm</el-button>
    </div>
  </el-dialog>

</template>

<script setup>
import {ref, onMounted, onUnmounted, watch, nextTick, computed} from 'vue'
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
const searchQuery = ref('');

const show = ref(false)
const showForEdit = ref(false)
const title = ref('')

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

const form = ref({
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
});

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


const showDialog = () => {
  title.value = 'Add Requirement'
  show.value = true
}

const doAdd = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  loading.value = true
  await addRequirement(form.value)
  addButten.value = false
  await getReqs()
  loading.value = false
}

const clickV = async (row) => {
  loading.value = true;
  const res = await getRequirementDetail(row.requirementId);
  const requirementData = {
    ...res.data,
    priorityLabel: priorityMap[res.data.priority]
  };
  curReq.value = res.data;
  firstData.value = JSON.parse(JSON.stringify(requirementData));
  isEdit.value = false;
  // delButten.value = this.curReq.value.createBy === this.username.value;
  visibButten.value = true;
  loading.value = false;
  showForEdit.value = true; // 显示对话框
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
  showForEdit.value = false
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

const priorityMap = {
  1: 'High',
  2: 'Medium',
  3: 'Low'
};



// 计算属性：过滤后的需求列表
const filteredReqList = computed(() => {
  return reqList.value.filter(req => {
    return req.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        req.description.toLowerCase().includes(searchQuery.value.toLowerCase());
  });
});


onUnmounted(() => {
  if (gantt) {
    gantt.clearAll()
  }
})

</script>

<style scoped>
@import "req.scss";
</style>