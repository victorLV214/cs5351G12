<template>
  <div class="project-container">
    <el-card class="project-info-card">
      <template #header>
        <div class="card-header">
          <span class="title">Project Detail</span>
          <el-button type="primary" @click="doEdit" :icon="Edit">Edit</el-button>
        </div>

      </template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="projectName">{{ pInfo.projectName }}</el-descriptions-item>
        <el-descriptions-item label="projectCode">{{ pInfo.projectCode }}</el-descriptions-item>
        <el-descriptions-item label="status">{{ pInfo.status }}</el-descriptions-item>
        <el-descriptions-item label="startDate">{{ pInfo.startDate }}</el-descriptions-item>
        <el-descriptions-item label="expectedEndDate">{{ pInfo.expectedEndDate }}</el-descriptions-item>
        <el-descriptions-item label="actualEndDate">{{ pInfo.actualEndDate }}</el-descriptions-item>
        <el-descriptions-item label="priority">{{ pInfo.priority }}</el-descriptions-item>
        <el-descriptions-item label="completionPercentage">{{ pInfo.completionPercentage }}%</el-descriptions-item>
        <el-descriptions-item label="budget">$ {{ pInfo.budget }}</el-descriptions-item>
        <el-descriptions-item label="actualCost">$ {{ pInfo.actualCost }}</el-descriptions-item>
        <el-descriptions-item label="description">{{ pInfo.description }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
    
    <el-dialog v-model="boolVisible" title="Edit Project" width="50%">
      <el-form :model="editForm" label-width="120px">
        <el-form-item label="projectName"><el-input v-model="editForm.projectName" /></el-form-item>
        <el-form-item label="projectCode"><el-input v-model="editForm.projectCode" /></el-form-item>
        <el-form-item label="status"><el-input v-model="editForm.status" /></el-form-item>
        <el-form-item label="startDate"><el-date-picker v-model="editForm.startDate" type="date" /></el-form-item>
        <el-form-item label="expectedEndDate"><el-date-picker v-model="editForm.expectedEndDate" type="date" /></el-form-item>
        <el-form-item label="actualEndDate"><el-date-picker v-model="editForm.actualEndDate" type="date" /></el-form-item>
        <el-form-item label="priority"><el-input v-model="editForm.priority" /></el-form-item>
        <el-form-item label="completionPercentage"><el-input v-model="editForm.completionPercentage" type="number" /></el-form-item>
        <el-form-item label="budget"><el-input v-model="editForm.budget" type="number" /></el-form-item>
        <el-form-item label="actualCost"><el-input v-model="editForm.actualCost" type="number" /></el-form-item>
        <el-form-item label="description"><el-input v-model="editForm.description" type="textarea" /></el-form-item>
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
import {Edit} from "@element-plus/icons-vue";

const pInfo = ref({})
const route = useRoute()
const projectId = route.params.id
const boolVisible = ref(false)
const editForm = ref({})


const getInfo = async () => {
  const res = await getProject(projectId)
  pInfo.value = res.data
}


const doEdit = () => {
  editForm.value = { ...pInfo.value }
  boolVisible.value = true
}


const doNew = async () => {
    const r = await updateProject(editForm.value)
  ElMessage.success('success')
  boolVisible.value = false
  getInfo() // 刷新数据

}

onMounted(() => {
  getInfo()
})
</script>

<style scoped>
@import "cp.scss";
</style>