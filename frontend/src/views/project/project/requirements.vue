<template>
  <el-card class="r-cards">
    <template #header><div class="card-header">
        <div class="header-left"><span class="title">requirment list</span><el-tag class="count-tag" type="info">total {{ total }} requirements</el-tag>
        </div>
      <div class="addR">
        <el-button type="primary" :icon="Plus" @click="addReq"></el-button>
        <el-button type="primary" :icon="Download" @click="showGantt"></el-button>
      </div></div></template>


    <div class="search"><el-form :inline="true" :model="Params" class="sF"><el-form-item label="requirements">
          <el-input v-model="Params.title" placeholder="" clearable style="width: 200px"/>
        </el-form-item><el-form-item label="priority">
          <el-select v-model="Params.priority" placeholder="select priority" clearable style="width: 200px">
            <el-option label="high" value="1" />
            <el-option label="medium" value="2" />
            <el-option label="low" value="3" /></el-select>
        </el-form-item>
        <el-form-item label="status">
          <el-select v-model="Params.status" placeholder="status" clearable style="width: 200px"><el-option label="pending" value="待处理" />
            <el-option label="processing" value="进行中" /><el-option label="completed" value="已完成" />
          </el-select></el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="getReqs">select</el-button>
          <el-button :icon="Refresh" @click="clearAll">Refresh</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="rList">
      <el-table
          v-loading="loading"
          :data="reqList"
          style="width: 100%"
      >
        <el-table-column label="ID" align="center">
          <template #default="{ row }">
            <el-link type="primary" @click="clickV(row)">{{ row.requirementId }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="title" align="center" prop="title" />
        <el-table-column label="description" align="center" prop="description" show-overflow-tooltip />
        <el-table-column label="priority" align="center" prop="priority"></el-table-column>
        <el-table-column label="status" align="center" prop="status"></el-table-column>
        <el-table-column label="createTime" align="center" prop="createTime" width="180" />




        
        

      </el-table>


      <div class="pagination-container">
        <el-pagination
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            v-model:current-page="Params.pageNum"
            v-model:page-size="Params.pageSize"

            layout="total, sizes, prev, pager, next, jumper" @size-change="sizeP" @current-change="cP"
        />
      </div>
    </div>
  </el-card>

  <el-dialog
      v-model="dialogVisible"
      title="requirement details"
      width="60%"
  >
    <el-form ref="formRef" :model="curReq" :rules="rules" label-width="120px">
      <el-form-item label="requirement ID" prop="requirementId">
        <span>{{ curReq?.requirementId }}</span>
      </el-form-item>

      <el-form-item label="title" prop="title">
        <el-input v-model="curReq.title" :disabled="!isEdit"/>
      </el-form-item>

      <el-form-item label="priority" prop="priority">
        <el-select v-model="curReq.priority" :disabled="!isEdit">
          <el-option label="high" value="1" />
          <el-option label="medium" value="2" />
          <el-option label="low" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item label="status" prop="status">
        <el-select v-model="curReq.status" :disabled="!isEdit">
          <el-option label="pending" value="待处理" />
          <el-option label="processing" value="进行中" /><el-option label="completed" value="已完成" />
        </el-select>
      </el-form-item>
      <el-form-item label="type" prop="type">
        <el-input v-model="curReq.type" :disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="assignedTo" prop="assignedTo">
        <el-input v-model="curReq.assignedTo" :disabled="!isEdit"/>
      </el-form-item>
      <el-form-item label="description" prop="description"><el-input v-model="curReq.description" type="textarea" :rows="3"
            :disabled="!isEdit"/>
      </el-form-item>

      <el-form-item label="remarks" prop="remarks">
        <el-input
            v-model="curReq.remarks"
            type="textarea"
            :rows="3"
            :disabled="!isEdit"
        />
      </el-form-item>
    </el-form>

    <template #footer>
  <span class="dialog-footer">
    <el-button @click="dialogVisible  = false">cancel</el-button>
    <el-button v-if="curReq?.createBy === username" type="danger" @click="delReq(curReq)">DEL
    </el-button>
    <el-button v-if="!isEdit" type="primary" @click="ISEDITT">EDit</el-button>
    <template v-else>
      <el-button @click="donotEDITT">CALCEL</el-button>
      <el-button type="primary" @click="submitEdit">SAVE</el-button>
    </template>
  </span></template>
  </el-dialog>
  <!-- 新增需求对话框 -->
  <el-dialog v-model="addButten" title="add new requirements" width="60%"
  >
    <el-form ref="addFormRef" :model="reqForm" :rules="rules" label-width="120px"
    >
      <el-form-item label="title" prop="title">
        <el-input v-model="reqForm.title" placeholder="title"/>
      </el-form-item>

      <el-form-item label="priority" prop="priority">
        <el-select v-model="reqForm.priority" placeholder="priority">
          <el-option label="high" value="1" />
          <el-option label="medium" value="2" />
          <el-option label="low" value="3" />
        </el-select>
      </el-form-item>

      <el-form-item label="type" prop="type">
        <el-input v-model="reqForm.type" placeholder="type"/>
      </el-form-item>

      <el-form-item label="assignedTo" prop="assignedTo">
        <el-input v-model="reqForm.assignedTo" placeholder="assignedTo"/>
      </el-form-item>

      <el-form-item label="description" prop="description">
        <el-input
            v-model="reqForm.description"
            type="textarea"
            placeholder="description"
        />
      </el-form-item>

      <el-form-item label="remarks" prop="remarks">
        <el-input v-model="reqForm.remarks" type="textarea"
            :rows="3"
            placeholder="remarks"
        />
      </el-form-item>
    </el-form>

    <template #footer>
    <span class="dialog-footer">
      <el-button @click="addButten = false">cancel</el-button>
      <el-button type="primary" @click="submitAdd">ADD</el-button>
    </span>
    </template>
  </el-dialog>
  <el-dialog v-model="ganttDialogVisible"
      title="Requirements Gantt Chart"
      width="90%"
      :destroy-on-close="true"
  >
    <div ref="ganttContainer" style="height: 600px; width: 100%;"></div>
  </el-dialog>

</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { Search, Refresh, Plus, Download } from '@element-plus/icons-vue'
import 'dhtmlx-gantt/codebase/dhtmlxgantt.css'
import 'dhtmlx-gantt/codebase/dhtmlxgantt.js'
import {useRoute} from "vue-router";
import {
  addRequirement,
  delRequirement,
  getRequirementDetail,
  listRequirement,
  updateRequirement
} from "@/api/project/requirements.js";
import {ElMessage, ElMessageBox} from "element-plus";
const ganttContainer = ref(null)
const ganttDialogVisible = ref(false)
import useUserStore from "@/store/modules/user.js";
import {getUser} from "@/api/system/user.js";
const reqList = ref([])
const userStore = useUserStore()
const total = ref(0)
const route = useRoute()
const username=ref(null)
const projectId=route.params.id
const loading = ref(false)
const dialogVisible = ref(false)
const curReq = ref(null)
const formRef = ref(null)
const isEdit = ref(false)
const firstData = ref(null) // 用于存储原始数据
const addButten = ref(false)
const addFormRef = ref(null)
const delButten = ref(false)
const id =userStore.id
const reqForm = ref({
  title: '',
  priority: '',
  status: 'processing',  // 默认状态
  type: '',
  assignedTo: '',
  description: '',
  remarks: '',
  projectId: projectId,  // 使用当前项目ID
  delFlag: 1,
  createBy: username.value,
})
const Params = ref({
  pageNum: 1,
  pageSize: 10,
  title: '',
  type: '',
  priority: '',
  status: '',
  assignedTo: '',
  projectId: projectId,
  delFlag:1,
})
const initGantt = () => {
  gantt.config.autosize = "y"
  gantt.config.date_format = "%Y-%m-%d"
  gantt.config.task_height = 20
  gantt.config.scale_height = 50
  gantt.config.row_height = 40


  gantt.config.scales = [
    { unit: "month", step: 1, format: "%F %Y" },
    { unit: "week", step: 1, format: "Week #%W" },
    { unit: "day", step: 1, format: "%d %D" }
  ]
  gantt.config.columns = [
    { name: "text", label: "Requirement Name", width: 200, tree: true, tooltip: "需求名称" },
    { name: "start_date", label: "Start Date", align: "center", width: 100, tooltip: "开始日期" },
    { name: "end_date", label: "End Date", align: "center", width: 100, tooltip: "结束日期" },
    {
      name: "progress",
      label: "Progress",
      align: "center",
      width: 80,
      tooltip: "Progress",
      template: (obj) => (obj.progress * 100).toFixed(0) + "%"
    },
    {
      name: "status",
      label: "Status",
      align: "center",
      width: 80,
      tooltip: "status",
      template: (obj) => {
        const status = obj.status 
        const statusMap = {
          'completed': '<span style="color: #67C23A">Completed</span>',
          'processing': '<span style="color: #409EFF">In Progress</span>',
          'pending': '<span style="color: #909399">Pending</span>'
        }
        return statusMap[status] || status
      }
    }
  ]
  gantt.templates.task_class = (start, end, task) => {
    switch (task.status) {
      case 'completed': return 'completed-task'
      case 'processing': return 'progress-task'
      default: return 'pending-task'
    }
  }
  
  gantt.templates.tooltip_text = (start, end, task) => {
    return `<b>Name:</b> ${task.text}<br/>
            <b>Start:</b> ${gantt.date.date_to_str("%Y-%m-%d")(start)}<br/>
            <b>End:</b> ${gantt.date.date_to_str("%Y-%m-%d")(end)}<br/>
            <b>Progress:</b> ${(task.progress * 100).toFixed(0)}%<br/>
            <b>Status:</b> ${task.status}`
  }
}
const loadData = (requirements) => {
  const tasks = {
    data: requirements.map(req => ({
      id: req.requirementId,
      text: req.title,
      start_date: new Date(req.createTime),
      end_date: new Date(req.endTime || new Date().setDate(new Date(req.createTime).getDate() + 7)),
      progress: req.status === '已完成' ? 1 :
          req.status === '进行中' ? 0.5 : 0,
      status: req.status
    }))
  }
  gantt.parse(tasks)
}
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],

}
const  gettheUser = async () => {
  const res = await getUser(id)
  username.value = res.data.userName
  console.log(username.value)
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
    delFlag:1,
    type: '',
    priority: '',
    status: '',
    assignedTo: '',
    projectId: projectId
  }
  getReqs()
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
// 提交新增
const submitAdd = async () => {
  if (!addFormRef.value) return
  
  
    await addFormRef.value.validate()
    loading.value = true
    await addRequirement(reqForm.value)
    addButten.value = false
    await getReqs() // 刷新列表
    loading.value = false
  
}


const clickV = async (row) => {

    loading.value = true

    const res = await getRequirementDetail(row.requirementId)
    // console.log(res)
    curReq.value = res.data
    firstData.value = JSON.parse(JSON.stringify(res.data)) 
    isEdit.value = false
    if(curReq.value.createBy === username.value){delButten.value = true}
    dialogVisible.value = true


    loading.value = false

}

const ISEDITT = () => {
  isEdit.value = true
}
const donotEDITT = () => {
  curReq.value = structuredClone(firstData.value)
  isEdit.value = false
}
const showGantt = () => {
  ganttDialogVisible.value = true

  nextTick(() => {
    if (ganttContainer.value) {
      initGantt()
      gantt.init(ganttContainer.value)
      loadData(reqList.value)
    }
  })
}
const submitEdit = async () => {

    await formRef.value.validate()

    loading.value = true
    await updateRequirement(curReq.value)

    isEdit.value = false
    dialogVisible.value = false
    await getReqs() // 刷新列表
    loading.value = false

}

const delReq = (row) => {

      then(async () => {

          loading.value = true
          await delRequirement(row.requirementId)

          dialogVisible.value = false
          await getReqs() // 刷新列表

          loading.value = false

      })

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
onUnmounted(() => {
  if (gantt) {
    gantt.destructor();
  }
});
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