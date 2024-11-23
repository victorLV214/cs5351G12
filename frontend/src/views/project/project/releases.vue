<template>
  <div class="app-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <el-button type="primary" @click="handleAdd">Add Task</el-button>
        </div>
      </template>

      <el-table
          v-loading="loading"
          :data="taskTreeData"
          row-key="taskId"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
      >
        <el-table-column prop="title" label="Title" min-width="200">
          <template #default="{ row }">
            <span>{{ row.title }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="status" label="Status" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="priority" label="Priority" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getPriorityType(row.priority)">
              {{ getPriorityLabel(row.priority) }}
            </el-tag>
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
            {{ getComplexityLabel(row.complexity) }}
          </template>
        </el-table-column>

        <el-table-column label="Operations" align="center" width="150">
          <template #default="{ row }">
            <el-button type="text" @click="handleEdit(row)">Edit</el-button>
            <el-button type="text" style="color: red" @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Add/Edit Dialog -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="500px" append-to-body>
      <el-form ref="taskForm" :model="taskForm" :rules="rules" label-width="100px">
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
          <el-select v-model="taskForm.parentTaskId" placeholder="Select parent task" clearable>
            <el-option
                v-for="task in taskList"
                :key="task.taskId"
                :label="task.title"
                :value="task.taskId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Due Date">
          <el-date-picker
              v-model="taskForm.dueDate"
              type="date"
              placeholder="Select date"
              value-format="YYYY-MM-DD"
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
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="submitForm">Confirm</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { listTask, getTask, addTask, updateTask, delTask } from '@/api/project/tasks'

export default {
  name: 'ProjectTaskList',
  data() {
    return {
      loading: false,
      taskList: [],
      taskTreeData: [],
      queryParams: {
        projectId: this.$route.params.id
      },
      dialogVisible: false,
      dialogTitle: '',
      taskForm: {
        taskId: undefined,
        projectId: this.$route.params.id,
        title: '',
        description: '',
        status: 'To Do',
        priority: 0,
        parentTaskId: undefined,
        dueDate: '',
        estimatedHours: 0,
        complexity: 0
      },
      rules: {
        title: [{ required: true, message: 'Please input title', trigger: 'blur' }],
        status: [{ required: true, message: 'Please select status', trigger: 'change' }],
        priority: [{ required: true, message: 'Please select priority', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listTask(this.queryParams).then(response => {
        this.taskList = response.rows
        this.taskTreeData = this.convertToTree(this.taskList)
        this.loading = false
      })
    },
    convertToTree(list) {
      const map = {}
      const result = []

      list.forEach(item => {
        map[item.taskId] = { ...item, children: [] }
      })

      list.forEach(item => {
        const node = map[item.taskId]
        if (item.parentTaskId) {
          const parent = map[item.parentTaskId]
          if (parent) {
            parent.children.push(node)
          } else {
            result.push(node)
          }
        } else {
          result.push(node)
        }
      })

      return result
    },
    handleAdd() {
      this.dialogTitle = 'Add Task'
      this.taskForm = {
        projectId: this.$route.params.id,
        title: '',
        description: '',
        status: 'To Do',
        priority: 0,
        parentTaskId: undefined,
        dueDate: '',
        estimatedHours: 0,
        complexity: 0
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = 'Edit Task'
      getTask(row.taskId).then(response => {
        this.taskForm = response.data
        this.dialogVisible = true
      })
    },
    handleDelete(row) {
      this.$confirm('Are you sure to delete this task?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        delTask(row.taskId).then(() => {
          this.$message.success('Delete successful')
          this.getList()
        })
      }).catch(() => {})
    },
    submitForm() {
      this.$refs.taskForm.validate(valid => {
        if (valid) {
          if (this.taskForm.taskId) {
            updateTask(this.taskForm).then(() => {
              this.$message.success('Update successful')
              this.dialogVisible = false
              this.getList()
            })
          } else {
            addTask(this.taskForm).then(() => {
              this.$message.success('Add successful')
              this.dialogVisible = false
              this.getList()
            })
          }
        }
      })
    },
    getStatusType(status) {
      const statusMap = {
        'To Do': 'info',
        'In Progress': 'warning',
        'Done': 'success',
        'Blocked': 'danger'
      }
      return statusMap[status] || 'info'
    },
    getPriorityType(priority) {
      const priorityMap = {
        0: 'info',    // Low
        1: 'warning', // Medium
        2: 'success', // High
        3: 'danger'   // Urgent
      }
      return priorityMap[priority] || 'info'
    },
    getPriorityLabel(priority) {
      const priorityMap = {
        0: 'Low',
        1: 'Medium',
        2: 'High',
        3: 'Urgent'
      }
      return priorityMap[priority] || 'Unknown'
    },
    getComplexityLabel(complexity) {
      const complexityMap = {
        0: 'Simple',
        1: 'Medium',
        2: 'Complex'
      }
      return complexityMap[complexity] || 'Unknown'
    }
  }
}
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