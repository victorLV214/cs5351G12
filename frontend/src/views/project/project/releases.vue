<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <el-button type="primary" :icon="Plus" @click="doAdd">Add Task</el-button>
          <el-button type="primary" :icon="Download" @click="downloadTaskList">Download</el-button>
        </div>
      </template>

      <div class="search">
        <el-form :inline="true" :model="Params" class="sF" label-width="70">
          <el-form-item label="Title">
            <el-input v-model="Params.title"
                      placeholder="Input title"
                      clearable
                      style="width: 180px"
                      @keyup.enter="handleQuery"/>
          </el-form-item>
          <el-form-item label="Status">
            <el-select v-model="Params.status" placeholder="Select status" clearable style="width: 180px">
              <el-option label="To Do" value="To Do" />
              <el-option label="In Progress" value="In Progress" />
              <el-option label="Done" value="Done" />
              <el-option label="Blocked" value="Blocked" />
            </el-select>
          </el-form-item>
          <el-form-item label="Priority">
            <el-select v-model="Params.priority" placeholder="Select priority" clearable style="width: 180px">
              <el-option label="Low - 0" :value="0" />
              <el-option label="Medium - 1" :value="1" />
              <el-option label="High - 2" :value="2" />
              <el-option label="Urgent - 3" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="Complexity">
            <el-select v-model="Params.complexity" placeholder="Select complexity" clearable style="width: 180px">
              <el-option label="Simple - 0" :value="0" />
              <el-option label="Medium - 1" :value="1" />
              <el-option label="Complex - 2" :value="2" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :icon="Search" @click="handleQuery">Search</el-button>
            <el-button :icon="Refresh" @click="resetQuery">Refresh</el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table v-loading="loading" :data="treeD" row-key="taskId" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
        <el-table-column prop="title" label="Title" min-width="200">
          <template #default="{ row }">
            <span>{{ row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status">{{ row.status }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="priority" label="Priority" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.priority">{{ row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assignedTo" label="Assignee" width="120" align="center" />
        <el-table-column prop="dueDate" label="Due Date" width="120" align="center">
          <template #default="{ row }">
            <span>{{ row.dueDate }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Hours" width="150" align="center">
          <template #default="{ row }">
            <span>{{ row.estimatedHours || 0 }}/{{ row.actualHours || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="complexity" label="Complexity" width="100" align="center">
          <template #default="{ row }">
            {{ row.complexity }}
          </template>
        </el-table-column>
        <el-table-column label="Operations" align="center" width="180" >
          <template #default="{ row }">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-button size="small" type="primary" class="w-100"
                           @click="doEDIT(row)" :icon="Edit" v-if="booladmin">Edit
                </el-button>
              </el-col>
              <el-col :span="12">
                <el-button size="small" type="danger" class="w-100"
                           @click="del(row)" :icon="Delete" v-if="booladmin">Delete</el-button>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog :title="butTit" v-model="buttonVis" width="500px" append-to-body>
      <el-form ref="taskFormRef" :model="taskForm" :rules="rules" label-width="100px">
        <el-form-item label="Title" prop="title">
          <el-input v-model="taskForm.title" placeholder="Please input title" />
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="taskForm.description" type="textarea" rows="3" placeholder="Please input description" />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-select v-model="taskForm.status" placeholder="Select status">
            <el-option label="To Do" value="To Do" />
            <el-option label="In Progress" value="In Progress" />
            <el-option label="Done" value="Done" />
            <el-option label="Blocked" value="Blocked" />
          </el-select>
        </el-form-item>
        <el-form-item label="Priority" prop="priority">
          <el-select v-model="taskForm.priority" placeholder="Select priority">
            <el-option label="Low" :value="0" />
            <el-option label="Medium" :value="1" />
            <el-option label="High" :value="2" />
            <el-option label="Urgent" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="Parent Task">
          <el-select v-model="taskForm.parentTaskId" placeholder="parentTaskId" clearable>
            <el-option v-for="task in taskD" :key="task.taskId" :label="task.title" :value="task.taskId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="Due Date">
          <el-date-picker v-model="taskForm.dueDate" type="date" placeholder="Select date" value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="Hours">
          <el-input-number v-model="taskForm.estimatedHours" :precision="2" :step="0.5" placeholder="Estimated" />
        </el-form-item>
        <el-form-item label="Complexity">
          <el-select v-model="taskForm.complexity" placeholder="Select complexity">
            <el-option label="Simple" :value="0" />
            <el-option label="Medium" :value="1" />
            <el-option label="Complex" :value="2" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="buttonVis = false">Cancel</el-button>
          <el-button type="primary" @click="subF">Confirm</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import {listTask, getTask, addTask, updateTask, delTask, exportTask} from '@/api/project/tasks'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {Delete, Download, Edit, Plus, Refresh, Search} from "@element-plus/icons-vue";

const route = useRoute()
const taskFormRef = ref(null)
import { listRole } from '@/api/system/role.js'
const loading = ref(false)
const taskD = ref([])
const treeD = ref([])
const buttonVis = ref(false)
const butTit = ref('')
const booladmin = ref(false)
const projectId = route.params.id
const queryParams = reactive({
  projectId: route.params.id
})
const Params = reactive({
  projectId: projectId,
})
const handleQuery = async () => {
  loading.value = true
  const searchProject = await listTask(Params)
  taskD.value = searchProject.rows
  treeD.value = doEleTree(taskD.value)
  loading.value = false
}
const resetQuery = async () => {
  Params.title = ''
  Params.status = ''
  Params.priority = ''
  Params.complexity = ''
  getList()
}
const checkRoles = async () => {

  const res = await listRole()
  console.log('res:', res)
  booladmin.value = res.rows.some(role => role.roleKey === 'admin')

}
const taskForm = reactive({
  taskId: undefined,
  projectId: route.params.id,
  title: '',
  description: '',
  status: 'To Do',
  priority: 0,
  parentTaskId: undefined,
  dueDate: '',
  estimatedHours: 0,
  complexity: 0
})

const rules = {
  title: [{ required: true, message: 'title', trigger: 'blur' }],
  status: [{ required: true, message: 'status', trigger: 'change' }],
  priority: [{ required: true, message: 'priority', trigger: 'change' }]
}

const getList = async () => {
  loading.value = true
  const response = await listTask(queryParams)
  taskD.value = response.rows
  treeD.value = doEleTree(taskD.value)
  loading.value = false
}

const doEleTree = (list) => {
  const map = {}
  const result = []

  list.forEach(item => {
    map[item.taskId] = { ...item, children: [] }
  })

  list.forEach(item => {
    const node = map[item.taskId]
    if (item.parentTaskId && map[item.parentTaskId]) {
      map[item.parentTaskId].children.push(node)
    } else {
      result.push(node)
    }
  })

  return result
}

const doAdd = () => {
  butTit.value = 'ADD'
  Object.assign(taskForm, {
    taskId: undefined,
    projectId: route.params.id,
    title: '',
    description: '',
    status: 'To Do',
    priority: 0,
    parentTaskId: undefined,
    dueDate: '',
    estimatedHours: 0,
    complexity: 0
  })
  buttonVis.value = true
}

const doEDIT = async (row) => {
  butTit.value = 'Edit'
  const response = await getTask(row.taskId)
  Object.assign(taskForm, response.data)
  buttonVis.value = true
}

const subF = async () => {
  if (!taskFormRef.value) return
  await taskFormRef.value.validate(async (valid) => {
    if (valid) {
      if (taskForm.taskId) {
        await updateTask(taskForm)
        ElMessage.success('Update successful')
      } else {
        await addTask(taskForm)
        ElMessage.success('Add successful')
      }
      buttonVis.value = false
      getList()
    }
  })
}

const del = async (row) => {
  await ElMessageBox.confirm(
      'This action will permanently delete this Task. Continue?',
      'Warning',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
  await delTask(row.taskId)
  ElMessage.success('Delete successful')
  getList()
}

// 下载项目任务列表
const downloadTaskList = async () => {
  const query = {
    projectId: projectId
  }
  const res = await exportTask(query)
  const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
  const href = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = href
  link.download = `project_task_list.xlsx`
  link.click()
}

// 页面加载时获取列表
onMounted(() => {
  getList()
  checkRoles()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.dialog-footer {
  text-align: right;
}
</style>