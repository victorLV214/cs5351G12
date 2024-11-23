<template>
  <div class="project-container">
    <el-card>
      <el-table :data="p">
        <el-table-column prop="projectName" label="Project Name" />
        <el-table-column >
          <el-button type="primary" :icon="Download" @click="downloadProject">Project</el-button>
        </el-table-column>
        <el-table-column >
          <el-button type="primary" :icon="Download" @click="downloadMember">Members</el-button>
        </el-table-column>

      </el-table>
    </el-card>
  </div>
</template>



<script setup>
import { ref, onMounted } from 'vue'
import { listProject } from '@/api/project/index.js'
import { ElMessage } from 'element-plus'
import {exportProject,exportname} from '@/api/download.js'
import {Download, Plus} from "@element-plus/icons-vue";
const p = ref([])
const getList = async () => {
  const r = await listProject({
    pageNum: 1,
    pageSize: 999
  })
  p.value = r.rows
}

const downloadProject = async (row) => {

  const res = await exportProject(
  )


  const blob = new Blob([res], {type: 'application/vnd.ms-excel'})



  const herf = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = window.URL.createObjectURL(blob)
  link.download = `projects.xlsx`
  link.click()



}
const downloadMember = async (row) => {

  const res = await exportname()

  const blob = new Blob([res], {type: 'application/vnd.ms-excel'})



  const herf = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = window.URL.createObjectURL(blob)
  link.download = `project_members.xlsx`
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