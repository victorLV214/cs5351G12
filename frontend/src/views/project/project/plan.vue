<template>
  <div class="project-container">
    <el-card class="project-info-card">
      <template #header>
        <div class="card-header">
          <span class="title">
            <el-icon><calendar/></el-icon>
            Project Detail</span>
          <el-button type="primary" @click="doEdit" :icon="Edit" v-if="booladmin">Edit</el-button>
        </div>

      </template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="Project Name">{{ pInfo.projectName }}</el-descriptions-item>
        <el-descriptions-item label="Project Code">{{ pInfo.projectCode }}</el-descriptions-item>
        <el-descriptions-item label="Status">{{ pInfo.status }}</el-descriptions-item>
        <el-descriptions-item label="Start Date">{{ pInfo.startDate }}</el-descriptions-item>
        <el-descriptions-item label="Expected End Date">{{ pInfo.expectedEndDate }}</el-descriptions-item>
        <el-descriptions-item label="Actual End Date">{{ pInfo.actualEndDate }}</el-descriptions-item>
        <el-descriptions-item label="Priority">{{ pInfo.priority }}</el-descriptions-item>
        <el-descriptions-item label="Completion Percentage">{{ pInfo.completionPercentage }}%</el-descriptions-item>
        <el-descriptions-item label="Budget">$ {{ pInfo.budget }}</el-descriptions-item>
        <el-descriptions-item label="Actual Cost">$ {{ pInfo.actualCost }}</el-descriptions-item>
        <el-descriptions-item label="Description">{{ pInfo.description }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
    
    <el-dialog v-model="boolVisible" title="Edit Project" width="50%">
      <el-form :model="editForm" label-width="120px">
        <el-form-item label="Project Name"><el-input v-model="editForm.projectName" /></el-form-item>
        <el-form-item label="Project Code"><el-input v-model="editForm.projectCode" /></el-form-item>
        <el-form-item label="Status"><el-input v-model="editForm.status" /></el-form-item>
        <el-form-item label="Start"><el-date-picker v-model="editForm.startDate" type="date" /></el-form-item>
        <el-form-item label="Expected End"><el-date-picker v-model="editForm.expectedEndDate" type="date" /></el-form-item>
        <el-form-item label="Actual End"><el-date-picker v-model="editForm.actualEndDate" type="date" /></el-form-item>
        <el-form-item label="Priority"><el-input v-model="editForm.priority" /></el-form-item>
        <el-form-item label="Completion"><el-input v-model="editForm.completionPercentage" type="number" /></el-form-item>
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
import { listRole } from '@/api/system/role.js'
const booladmin=ref(false)
import { ElMessage } from 'element-plus';
import {Calendar, Edit, Notebook} from "@element-plus/icons-vue";

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

const checkRoles = async () => {

  const res = await listRole()
  console.log('res:', res)
  booladmin.value = res.rows.some(role => role.roleKey === 'admin')

}
onMounted(() => {
  getInfo()
  checkRoles()
})
</script>

<style scoped>
@import "cp.scss";
</style>