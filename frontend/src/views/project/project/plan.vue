<template>
  <div class="project-container">
    <el-card class="project-info-card">
      <template #header>
        <div class="card-header">
          <span class="title">Project detail</span>
          <el-button type="primary" @click="doEdit">Edit</el-button>
        </div>
      </template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="Project Name">{{ projectInfo.projectName }}</el-descriptions-item>
        <el-descriptions-item label="Project Code">{{ projectInfo.projectCode }}</el-descriptions-item>
        <el-descriptions-item label="Status">{{ projectInfo.status }}</el-descriptions-item>
        <el-descriptions-item label="Start Date">{{ projectInfo.startDate }}</el-descriptions-item>
        <el-descriptions-item label="Expected End Date">{{ projectInfo.expectedEndDate }}</el-descriptions-item>
        <el-descriptions-item label="Actual End Date">{{ projectInfo.actualEndDate }}</el-descriptions-item>
        <el-descriptions-item label="Priority">{{ projectInfo.priority }}</el-descriptions-item>
        <el-descriptions-item label="Progress">{{ projectInfo.completionPercentage }}%</el-descriptions-item>
        <el-descriptions-item label="Budget">$ {{ projectInfo.budget }}</el-descriptions-item>
        <el-descriptions-item label="Actual Cost">$ {{ projectInfo.actualCost }}</el-descriptions-item>
        <el-descriptions-item label="Description">{{ projectInfo.description }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
    
    <el-dialog v-model="boolVisible" title="Edit Project" width="50%">
      <el-form :model="editForm" label-width="120px">
        <el-form-item label="Project Name"><el-input v-model="editForm.projectName" /></el-form-item>
        <el-form-item label="Project Code"><el-input v-model="editForm.projectCode" /></el-form-item>
        <el-form-item label="Status"><el-input v-model="editForm.status" /></el-form-item>
        <el-form-item label="Start Date"><el-date-picker v-model="editForm.startDate" type="date" /></el-form-item>
        <el-form-item label="Expected End Date"><el-date-picker v-model="editForm.expectedEndDate" type="date" /></el-form-item>
        <el-form-item label="Actual End Date"><el-date-picker v-model="editForm.actualEndDate" type="date" /></el-form-item>
        <el-form-item label="Priority"><el-input v-model="editForm.priority" /></el-form-item>
        <el-form-item label="Progress"><el-input v-model="editForm.completionPercentage" type="number" /></el-form-item>
        <el-form-item label="Budget"><el-input v-model="editForm.budget" type="number" /></el-form-item>
        <el-form-item label="Actual Cost"><el-input v-model="editForm.actualCost" type="number" /></el-form-item>
        <el-form-item label="Description"><el-input v-model="editForm.description" type="textarea" /></el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="boolVisible = false">Cancel</el-button>
          <el-button type="primary" @click="doNew">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getProject, updateProject } from "@/api/project";
import { useRoute } from "vue-router";
import { ElMessage } from 'element-plus';

const projectInfo = ref({})
const route = useRoute()
const projectId = route.params.id
const boolVisible = ref(false)
const editForm = ref({})

// 获取项目基本信息
const getInfo = async () => {
  const res = await getProject(projectId)
  if (res.code === 200) {
    projectInfo.value = res.data
  }
}

// 打开编辑对话框
const doEdit = () => {
  editForm.value = { ...projectInfo.value }
  boolVisible.value = true
}

// 更新项目信息
const doNew = async () => {
    const r = await updateProject(editForm.value)
    if (r.code === 200) {
      ElMessage.success('success')
      boolVisible.value = false
      getInfo() // 刷新数据
    }
}

onMounted(() => {
  getInfo()
})
</script>

<style scoped>
@import "cp.scss";
</style>