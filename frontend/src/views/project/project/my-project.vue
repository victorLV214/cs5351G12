<template>
  <div class="project-container">
    <el-card class="project-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">
            <img src="@/assets/icons/png/project-list.png" style="width: 25px;
                vertical-align: middle; position: relative; top: -2px;" alt="">
            My Project List
          </span>
          <div class="button-group" v-if="booladmin">
            <el-button type="primary" @click="addP" class="add-button">
              <el-icon><Plus/></el-icon>
              &nbsp;New
            </el-button>
            <el-button type="primary" @click="downloadProjectList()" class="download-button">
              <el-icon><Download/></el-icon>
              &nbsp;Download
            </el-button>
          </div>
        </div>
      </template>
      <el-table :data="projectList" style="width: 100%" class="project-table" :row-class-name="tableRowClassName" @row-click="doRCS">
        <el-table-column prop="projectName" label="Project Name">
          <template #default="scope">
            <a href="javascript:;" @click="checkProjectAccess(scope.row)" class="project-link text-decoration-none">{{ scope.row.projectName }}</a>
          </template>
        </el-table-column>
        <el-table-column prop="projectCode" label="Project Code" align="center"/>
        <el-table-column prop="createTime" label="Create Time" align="center"/>
        <el-table-column label="Operations" width="240" align="center">
          <template #default="scope">
            <el-row :gutter="10">
              <el-col :span="8">
                <el-button size="small" type="primary" class="w-100"
                           @click="getDetail(scope.row)" :icon="Document">Detail</el-button>
              </el-col>
              <el-col :span="8">
                <el-button size="small" type="success" class="w-100"
                           @click="doEdit(scope.row)" :icon="EditPen" v-if="booladmin">Edit</el-button>
              </el-col>
              <el-col :span="8">
                <el-button size="small" type="danger" class="w-100"
                           @click="doDelete(scope.row)" :icon="Delete" v-if="booladmin">Delete</el-button>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
    </el-card>



    <el-dialog v-model="pFormVisi" :title="projectFormTitle" width="800px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="formDataForAddProject" :rules="rulesForForm" label-width="150px">
        <el-form-item label="projectName" prop="projectName">
          <el-input v-model="formDataForAddProject.projectName" placeholder="projectName"/>
        </el-form-item>
        <el-form-item label="projectCode" prop="projectCode"><el-input v-model="formDataForAddProject.projectCode" placeholder="projectCode"/>
        </el-form-item>
        <el-form-item label="description" prop="description">
          <el-input v-model="formDataForAddProject.description" type="textarea" placeholder="description"/>
        </el-form-item>
        <el-form-item label="startDate" prop="startDate">
          <el-date-picker v-model="formDataForAddProject.startDate" type="date" placeholder="choose startDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="expectedEndDate" prop="expectedEndDate">
          <el-date-picker v-model="formDataForAddProject.expectedEndDate" type="date" placeholder="expectedEndDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="priority" prop="priority">
          <el-select v-model="formDataForAddProject.priority" placeholder="priority">
            <el-option label="low" :value="1"/>
            <el-option label="medium" :value="2"/><el-option label="high" :value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item label="status" prop="status">
          <el-select v-model="formDataForAddProject.status" placeholder="Status">
            <el-option label="Not Started" value="Not Started"/>
            <el-option label="In Progress" value="In Progress"/>
            <el-option label="Completed" value="Completed"/>
            <el-option label="Paused" value="Paused"/>
          </el-select>
        </el-form-item>
        <el-form-item label="budget" prop="budget">
          <el-input-number v-model="formDataForAddProject.budget" :precision="2" :step="1000" :min="0" placeholder="input the budget"/>
        </el-form-item>
        <el-form-item label="remark" prop="remark">
          <el-input v-model="formDataForAddProject.remark" type="textarea" placeholder="remark"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="pFormVisi = false">cancel</el-button>
          <el-button type="primary" @click="subF">confirm</el-button>
        </div>
      </template>
    </el-dialog>


    <el-dialog v-model="detailV" width="800px">
      <div class="detail-container" v-if="cPro">
        <div class="detail-section">
          <div class="section-title" style="margin-bottom: 10px">
            <el-icon><InfoFilled/></el-icon>&nbsp;
            <strong>Basic Information</strong>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="Project Name">{{ cPro.projectName }}</el-descriptions-item>
            <el-descriptions-item label="Project Code">{{ cPro.projectCode }}</el-descriptions-item>
            <el-descriptions-item label="Create Time">{{ cPro.createTime }}</el-descriptions-item>
            <el-descriptions-item label="Status">
              <el-tag :type="setS(cPro.status)">{{ cPro.status }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="Start Date">{{ cPro.startDate }}</el-descriptions-item>
            <el-descriptions-item label="Expected End Date">{{ cPro.expectedEndDate }}</el-descriptions-item>
            <el-descriptions-item label="Priority">
              <el-tag :type="getPP(cPro.priority)">
                {{ showPriority(cPro.priority) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="Budget">{{ cPro.budget }}</el-descriptions-item>
          </el-descriptions>
        </div>


        <div class="detail-section">
          <div class="section-title" style="margin-bottom: 10px; margin-top: 20px">
            <el-icon><InfoFilled/></el-icon>&nbsp;
            <strong>Description</strong>
          </div>
          <div class="description-content">{{ cPro.description}}</div>
        </div>

        <div class="detail-section">
          <div class="section-title" style="margin-bottom: 10px; margin-top: 20px">
            <el-icon><InfoFilled/></el-icon>&nbsp;
            <strong>Remark</strong>
          </div>
          <div class="description-content">{{ cPro.remark }}</div>
        </div>

        <div class="detail-section">
          <div class="section-title" style="margin-bottom: 10px; margin-top: 20px">
            <el-icon><InfoFilled/></el-icon>&nbsp;
            <strong>Members</strong>
          </div>
          <el-table :data="cPMember" style="width: 100%">
            <el-table-column prop="userName" label="User" align="center"/>
            <el-table-column prop="nickName" label="Name" align="center"/>
            <el-table-column prop="role" label="Role" align="center"/>
            <el-table-column prop="joinDate" label="Join Date" align="center"/>
            <el-table-column prop="allocationPercentage" label="Percentage" align="center">
              <template #default="{ row }">
                {{ row.allocationPercentage }}%
              </template>
            </el-table-column>
            <el-table-column prop="notes" label="Notes" show-overflow-tooltip align="center"/>

          </el-table>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="booleanForVis" :title="projectUpdateFormTitle" width="600px" :close-on-click-modal="false">
      <el-form ref="updateFormRef" :model="theBiggsetForm" :rules="rulesForUpdateForm" label-width="150px">
        <el-form-item label="Project Name" prop="projectName">
          <el-input v-model="theBiggsetForm.projectName" placeholder="projectName"/>
        </el-form-item>
          <el-form-item label="Project Code" prop="projectCode">
            <el-input v-model="theBiggsetForm.projectCode" placeholder="projectCode"/>
          </el-form-item>
            <el-form-item label="Description" prop="description">
              <el-input v-model="theBiggsetForm.description" type="textarea" placeholder="description"/>
            </el-form-item>
              <el-form-item label="Start Date" prop="startDate">
                <el-date-picker v-model="theBiggsetForm.startDate" type="date" placeholder="choose startDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD"/>
              </el-form-item>
                <el-form-item label="Expected End Date" prop="expectedEndDate">
                  <el-date-picker v-model="theBiggsetForm.expectedEndDate" type="date" placeholder="expectedEndDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD"/>
                </el-form-item>
                  <el-form-item label="Actual End Date" prop="actualEndDate">
                    <el-date-picker v-model="theBiggsetForm.actualEndDate" type="date" placeholder="actualEndDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD"/>
                  </el-form-item>
                    <el-form-item label="Priority" prop="priority">
                      <el-select v-model="theBiggsetForm.priority" placeholder="priority">
                        <el-option label="low" :value="1"/><el-option label="medium" :value="2"/><el-option label="high" :value="3"/>
                      </el-select>
                      </el-form-item>
                      <el-form-item label="Status" prop="status">
                        <el-select v-model="theBiggsetForm.status" placeholder="Status">
                          <el-option label="Not Started" value="Not Started"/>
                          <el-option label="In Progress" value="In Progress"/>
                          <el-option label="Completed" value="Completed"/>
                          <el-option label="Paused" value="Paused"/>
                        </el-select>
                          </el-form-item>
                          <el-form-item label="Percentage" prop="completionPercentage">
                            <el-input-number
                                v-model="theBiggsetForm.completionPercentage" :precision="1" :step="5" :min="0" :max="100" placeholder="input the completionPercentage"/>
                          </el-form-item>
          <el-form-item label="budget" prop="budget">
            <el-input-number
                v-model="theBiggsetForm.budget" :precision="2" :step="1000" :min="0" placeholder="input the budget"/></el-form-item>
        <el-form-item label="actualCost" prop="actualCost">
          <el-input-number v-model="theBiggsetForm.actualCost" :precision="2" :step="1000" :min="0" placeholder="input the actualCost"/>
        </el-form-item>
        <el-form-item label="remark" prop="remark">
          <el-input v-model="theBiggsetForm.remark" type="textarea" placeholder="remark"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="booleanForVis = false">cancel</el-button><el-button type="primary" @click="reloadFprm">confirm</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted ,reactive} from 'vue'

import {ElMessage, ElMessageBox} from 'element-plus'
import {listProject, addProject, getProject, delProject, updateProject, listMyProject} from '@/api/project/index.js'

import { listProjectMember } from '@/api/project/member.js'
import {
  EditPen, Delete, InfoFilled, Document, ChatLineSquare, User, Plus, MoreFilled, Download,
} from '@element-plus/icons-vue'
const projectFormTitle = ref('')
const projectUpdateFormTitle = ref('')
const formRef = ref(null)
const updateFormRef = ref(null)
import {getUser} from "@/api/system/user.js";
import { useRouter } from 'vue-router'
const router = useRouter()
const userStore = useUserStore()
import { addProjectMember, } from '@/api/project/member.js'
import { listRole } from '@/api/system/role.js'
const currentRow = ref(null)
const pFormVisi = ref(false)
const booleanForVis = ref(false)

import useUserStore from "@/store/modules/user.js"
import {exportMyProjectList, exportProject} from "@/api/download.js";

const booladmin=ref(false)

const detailV = ref(false)
const cPro = ref(null)
const cPMember = ref([])
const formDataForAddProject = reactive({
  projectName: '',
  projectCode: '',
  description: '',
  startDate: '',
  expectedEndDate: '',
  priority: 2,
  status: 'Not Started',
  budget: 0,
  remark: '',
  projectManagerId: '',
})
const theBiggsetForm = reactive({
  actualCost: 0,
  actualEndDate: '',
  budget: 0,
  completionPercentage: 0.0,
  delFlag: '0',
  description: '',
  expectedEndDate: '',
  priority: 2,
  projectCode: '',
  projectId: 0,
  projectManagerId: '',
  projectName: '',
  remark: '',
  startDate: '',
  status: '',
})
const checkRoles = async () => {

    const res = await listRole()
  console.log('res:', res)
  booladmin.value = res.rows.some(role => role.roleKey === 'admin')

}
const checkProjectAccess = async (project) => {


    const response = await listProjectMember({
      projectId: project.projectId,
      pageNum: 1,
      pageSize: 999
    })
  // console.log('response:', response)
    const cID = userStore.id
    const isMember = response.rows.some(member => member.userId === cID)
    if (isMember) {
      router.push(`/project/detail/${project.projectId}`)
    } else {
      ElMessage.error('You do not have permission to access this project')
    }

}


const tableRowClassName = ({ row }) => {
  if (currentRow.value && currentRow.value === row) {
    return 'selected-row'
  }
  return ''
}



const doRCS = (row) => {
  currentRow.value = row
}


const getDetail = async (row) => {
    const id = row.projectId
    const projectRes = await getProject(id)
    // console.log('projectRes:', projectRes)
    cPro.value = projectRes.data
    await myGetMembers(id)
    detailV.value = true

}


const showPriority = (priority) => {
  const priorityMap = {
    1: 'Low',
    2: 'Medium',
    3: 'High'
  }
  return priorityMap[priority] || 'unknown'
}



const myGetMembers = async (projectId) => {

    const members = await listProjectMember({
      projectId: projectId,
      pageNum: 1,
      pageSize: 999
    })
    // console.log('members:', members)
    const inf = []
    for (const member of members.rows) {
      try {
        const u1 = await getUser(member.userId)
        inf.push({
          ...member,
          userName: u1.data.userName,
          nickName: u1.data.nickName,
          email: u1.data.email
        })
      } catch (error) {
        console.error('error:', error)
        inf.push({
          ...member,
          userName: 'unknown',
          nickName: '-',
          email: '-'
        })
      }
    }
    cPMember.value = inf
    

}

const setS = (status) => {
  const statusMap = {
    'Not Started': 'info',
    'In Progress': '',//error!!!
    'Completed': 'success',
    'Paused': 'warning'
  }
  return statusMap[status] || ''
}


const getPP = (priority) => {
  const priorityMap = {
    1: 'info',
    2: 'warning',
    3: 'danger'
  }
  return priorityMap[priority]
}


const rulesForForm = {
  projectName: [
    { required: true, message: 'Please enter project name', trigger: 'blur' }
  ],
  projectCode: [
    { required: true, message: 'Please enter project code', trigger: 'blur' }
  ],
  startDate: [
    { required: true, message: 'Please select start date', trigger: 'change' }
  ],
  status: [
    { required: true, message: 'Please select project status', trigger: 'change' }
  ],
  priority: [
    { required: true, message: 'Please select priority', trigger: 'change' }
  ]
}

const rulesForUpdateForm = {

}

const projectList = ref([])

const addP = () => {
  pFormVisi.value = true
  projectFormTitle.value = 'add project'
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

//获取项目列表
const getList = async () => {
  const userStore = useUserStore()
  const userId = userStore.id

    const allProject = await listMyProject({
      pageNum: 1,
      pageSize: 999,
      userId: userId,
    })
    projectList.value = allProject.rows
}


const subF = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {

        const userStore = useUserStore()
        const userId = userStore.id
        formDataForAddProject.projectManagerId = userId
      
        const projectResponse = await addProject(formDataForAddProject)
        const theData = {
          projectId: projectResponse.data.projectId, // Assuming the response includes the new project ID
          userId: userId,
          role: 'creator',
          joinDate: formDataForAddProject.startDate,
          allocationPercentage: 100,
          isActive: 1,
          permissions: 'read,write,admin',
          notes: 'Creator',
          remark: 'creator'
        }
        await addProjectMember(theData)
        ElMessage.success('Project Create Success')
        pFormVisi.value = false
        getList() // Refresh the list
    }
  })
}

const reloadFprm = async () => {
  if (!updateFormRef.value) return
  await updateFormRef.value.validate(async (valid) => {
    if (valid) {
        await updateProject(theBiggsetForm)
        ElMessage.success('Edit Success')
        booleanForVis.value = false
        getList() // 刷新列表
    }
  })
}

const doEdit = async (row) => {

    const id = row.projectId
    const projectRes = await getProject(id)
    theBiggsetForm.projectId = id
    booleanForVis.value = true
    projectUpdateFormTitle.value = 'Edit'
    if (updateFormRef.value) {
      updateFormRef.value.resetFields()
    }
    Object.assign(theBiggsetForm, projectRes.data);

}

const doDelete = async (row) => {
    await ElMessageBox.confirm(
        'This action will permanently delete this project. Continue?',
        'Warning',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
    )
    await delProject(row.projectId)
    ElMessage.success('Delete Success')
    await getList() // 刷新列表
}

// 下载项目列表
const downloadProjectList = async () => {
  const query = {}
  const res = await exportMyProjectList(query)
  const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
  const href = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = href
  link.download = `my_project_list.xlsx`
  link.click()
}

onMounted(() => {
  getList()
  checkRoles()
})
</script>

<style scoped>
@import "index.scss";
</style>