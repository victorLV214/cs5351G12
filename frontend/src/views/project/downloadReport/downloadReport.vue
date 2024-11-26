<template>
  <div class="project-container">
    <el-card>
      <el-table :data="p">
        <el-table-column prop="projectName" label="Project Name" />
        <el-table-column>
          <template #default="{ row }">
            <el-button type="primary" :icon="Download" @click=downloadProject(row)>Project</el-button>
          </template>
        </el-table-column>
        <el-table-column>
          <template #default="{ row }">
            <el-button type="primary" :icon="Download" @click=downloadMember(row)>Members</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listProject } from '@/api/project/index.js'
import { ElMessage } from 'element-plus'
import { exportProject, exportProjectMember} from '@/api/download.js'
import { Download, Plus } from "@element-plus/icons-vue";

const p = ref([])

const getList = async () => {
  const r = await listProject({
    pageNum: 1,
    pageSize: 999
  })
  p.value = r.rows
}

const downloadProject = async (row) => {
  const query = {
    projectId: row.projectId
  }
  console.log('Query:', query)
  const res = await exportProject(query)

  const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
  const href = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = href
  link.download = `project_${row.projectName}.xlsx`
  link.click()
}

const downloadMember = async (row) => {
  const query = {
    projectId: row.projectId
  }
  console.log('Query:', query)
  const res = await exportProjectMember(query)

  const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
  const href = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = href
  link.download = `project_members_${row.projectName}.xlsx`
  link.click()
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.project-container {
  padding: 20px;
}
</style>