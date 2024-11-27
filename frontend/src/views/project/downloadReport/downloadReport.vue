<template>
  <div class="project-container">
    <el-card>
      <div class="header">
        <h3>Project Download Center</h3>
        <el-button type="info" @click="goBack">Back</el-button>
      </div>
      <el-table :data="p">
        <el-table-column prop="projectName" label="Project Name" />
        <el-table-column label="Download Members">
          <template #default="{ row }">
            <el-button type="primary" :icon="Download" @click="confirmDownload('Members', row, downloadMember)">Members</el-button>
          </template>
        </el-table-column>
        <el-table-column label="Download Requirements">
          <template #default="{ row }">
            <el-button type="primary" :icon="Download" @click="confirmDownload('Requirements', row, downloadRequirementList)">Requirements</el-button>
          </template>
        </el-table-column>
        <el-table-column label="Download Iterations">
          <template #default="{ row }">
            <el-button type="primary" :icon="Download" @click="confirmDownload('Iterations', row, downloadIterationList)">Iterations</el-button>
          </template>
        </el-table-column>
        <el-table-column label="Download Tasks">
          <template #default="{ row }">
            <el-button type="primary" :icon="Download" @click="confirmDownload('Tasks', row, downloadTaskList)">Tasks</el-button>
          </template>
        </el-table-column>
        <el-table-column label="Download Work Items">
          <template #default="{ row }">
            <el-button type="primary" :icon="Download" @click="confirmDownload('Work Items', row, downloadWorkItemList)">Work Items</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {listProject} from '@/api/project/index.js'
import {ElMessage, ElMessageBox} from 'element-plus'
import {exportProject, exportProjectMember} from '@/api/download.js'
import {Download} from "@element-plus/icons-vue";
import {exportRequirement} from "@/api/project/requirements.js";
import {exportIteration} from "@/api/project/iteration.js";
import {exportTask} from "@/api/project/tasks.js";
import {exportItem} from "@/api/project/item.js";

const p = ref([])

const getList = async () => {
  try {
    const r = await listProject({
      pageNum: 1,
      pageSize: 999
    })
    p.value = r.rows
  } catch (error) {
    ElMessage.error('Failed to fetch project list')
  }
}

const downloadFile = async (query, exportFunction, fileName) => {
  try {
    const res = await exportFunction(query)
    const blob = new Blob([res], {type: 'application/vnd.ms-excel'})
    const href = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = href
    link.download = fileName
    link.click()
    ElMessage.success('Download successful')
  } catch (error) {
    ElMessage.error('Failed to download file')
  }
}

const confirmDownload = (type, row, downloadFunction) => {
  ElMessageBox.confirm(
      `Are you sure you want to download ${type} for project ${row.projectName}?`,
      'Download Confirmation',
      {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning',
      }
  ).then(() => {
    downloadFunction(row)
  }).catch(() => {
    ElMessage.info('Download canceled')
  })
}

const downloadMember = (row) => {
  const query = {projectId: row.projectId}
  downloadFile(query, exportProjectMember, `project_members_${row.projectName}.xlsx`)
}

const downloadRequirementList = (row) => {
  const query = {projectId: row.projectId}
  downloadFile(query, exportRequirement, `project_requirement_list_${row.projectName}.xlsx`)
}

const downloadIterationList = (row) => {
  const query = {projectId: row.projectId}
  downloadFile(query, exportIteration, `project_iteration_list_${row.projectName}.xlsx`)
}

const downloadTaskList = (row) => {
  const query = {projectId: row.projectId}
  downloadFile(query, exportTask, `project_task_list_${row.projectName}.xlsx`)
}

const downloadWorkItemList = (row) => {
  const query = {projectId: row.projectId}
  downloadFile(query, exportItem, `project_workItem_list_${row.projectName}.xlsx`)
}

const goBack = () => {
  window.history.back()
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.project-container {
  padding: 20px;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
