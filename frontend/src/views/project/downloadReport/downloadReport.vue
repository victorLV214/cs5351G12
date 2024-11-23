<template>
  <div class="project-container">
    <el-card>
      <el-table :data="p">
        <el-table-column prop="projectName" label="Project Name" />
        <el-table-column ><el-button @click="downloadProject">download</el-button></el-table-column>


      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listProject } from '@/api/project/index.js'
import { ElMessage } from 'element-plus'
import {exportProject} from '@/api/download.js'
const p = ref([])

const getList = async () => {
  const r = await listProject({
    pageNum: 1,
    pageSize: 999
  })
  p.value = r.rows
}


const downloadProject = async (row) => {
  console.log(row)
  const res = await exportProject({
    projectId: row.projectId
  })


  const blob = new Blob([res], {type: 'application/vnd.ms-excel'})



  const herf = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = window.URL.createObjectURL(blob)
  link.download = `1.xlsx`
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