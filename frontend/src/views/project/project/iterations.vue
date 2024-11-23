<template>
  <div class="app-container">
    <div class="mb-20">
      <el-button type="primary" @click="openDialog">新增迭代</el-button>
    </div>

    <el-table :data="iterationList" v-loading="loading" border>
      <el-table-column label="迭代名称" prop="name" />
      <el-table-column label="目标" prop="goal" show-overflow-tooltip />
      <el-table-column label="开始时间" prop="startDate">
        <template #default="scope">
          {{ formatDate(scope.row.startDate) }}
        </template>
      </el-table-column>
      <el-table-column label="结束时间" prop="endDate">
        <template #default="scope">
          {{ formatDate(scope.row.endDate) }}
        </template>
      </el-table-column>
      <el-table-column label="计划故事点" prop="plannedStoryPoints" />
      <el-table-column label="完成故事点" prop="completedStoryPoints" />
      <el-table-column label="状态" prop="status">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" link @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="600px"
        @close="resetForm"
    >
      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          label-width="120px"
      >
        <el-form-item label="迭代名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入迭代名称" />
        </el-form-item>
        <el-form-item label="迭代目标" prop="goal">
          <el-input v-model="form.goal" type="textarea" placeholder="请输入迭代目标" />
        </el-form-item>
        <el-form-item label="开始时间" prop="startDate">
          <el-date-picker
              v-model="form.startDate"
              type="datetime"
              placeholder="选择开始时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endDate">
          <el-date-picker
              v-model="form.endDate"
              type="datetime"
              placeholder="选择结束时间"
              value-format="YYYY-MM-DD HH:mm:ss"
              style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="计划故事点" prop="plannedStoryPoints">
          <el-input-number
              v-model="form.plannedStoryPoints"
              :min="0"
              controls-position="right"
          />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
              v-model="form.description"
              type="textarea"
              placeholder="请输入描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {useRoute} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {listIteration, addIteration, updateIteration, deleteIteration} from '@/api/project/iteration'

const route = useRoute()
const projectId = route.params.id // 获取路由中的项目ID

const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const iterationList = ref([])

const formRef = ref(null)
const form = ref({
  iterationId: undefined,
  projectId: projectId,
  name: '',
  goal: '',
  startDate: '',
  endDate: '',
  plannedStoryPoints: 0,
  completedStoryPoints: 0,
  description: '',
  status: '0'
})

const rules = {
  name: [
    {required: true, message: '请输入迭代名称', trigger: 'blur'}
  ],
  startDate: [
    {required: true, message: '请选择开始时间', trigger: 'change'}
  ],
  endDate: [
    {required: true, message: '请选择结束时间', trigger: 'change'}
  ],
  plannedStoryPoints: [
    {required: true, message: '请输入计划故事点', trigger: 'change'}
  ]
}

// 获取迭代列表
const getList = async () => {
  loading.value = true
  try {
    const res = await listIteration({
      projectId: projectId
    })
    iterationList.value = res.data || []
  } catch (error) {
    console.error('获取迭代列表失败:', error)
    ElMessage.error('获取迭代列表失败')
  } finally {
    loading.value = false
  }
}

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleString()
}

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    '0': 'info',    // 未开始
    '1': 'warning', // 进行中
    '2': 'success', // 已完成
    '3': 'danger'   // 已关闭
  }
  return statusMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    '0': '未开始',
    '1': '进行中',
    '2': '已完成',
    '3': '已关闭'
  }
  return statusMap[status] || '未知状态'
}

// 打开新增对话框
const openDialog = () => {
  dialogTitle.value = '新增迭代'
  dialogVisible.value = true
}

// 编辑迭代
const handleEdit = (row) => {
  dialogTitle.value = '编辑迭代'
  form.value = {...row, projectId: projectId}
  dialogVisible.value = true
}

// 删除迭代
const handleDelete = (row) => {
  ElMessageBox.confirm('确认要删除该迭代吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteIteration(row.iterationId)
      ElMessage.success('删除成功')
      getList()
    } catch (error) {
      console.error('删除失败:', error)
      ElMessage.error('删除失败')
    }
  })
}

// 提交表单
const submitForm = () => {
  formRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        if (form.value.iterationId) {
          await updateIteration(form.value)
          ElMessage.success('修改成功')
        } else {
          await addIteration(form.value)
          ElMessage.success('新增成功')
        }
        dialogVisible.value = false
        getList()
      } catch (error) {
        console.error('操作失败:', error)
        ElMessage.error('操作失败')
      }
    }
  })
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  form.value = {
    iterationId: undefined,
    projectId: projectId,
    name: '',
    goal: '',
    startDate: '',
    endDate: '',
    plannedStoryPoints: 0,
    completedStoryPoints: 0,
    description: '',
    status: '0'
  }
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.dialog-footer {
  padding-right: 20px;
  text-align: right;
}
</style>