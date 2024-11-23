<template>
  <div class="project-container">
    <el-card class="project-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">
            <img src="@/assets/icons/png/project-list.png" style="width: 25px;
                vertical-align: middle; position: relative; top: -2px;">
            Project List
          </span>
          <el-button type="primary" @click="handleAddProjects" class="add-button">
            <img src="@/assets/icons/png/plus.png" style="width: 15px;
                vertical-align: middle; position: relative; right: 5px;">
            New Project
          </el-button>
        </div>
      </template>
      <el-table
          :data="projectList" style="width: 100%" class="project-table" :row-class-name="tableRowClassName" @row-click="doRCS">
        <el-table-column prop="projectName" label="Project Name">
          <template #default="scope">
            <a
                href="javascript:;"
                @click="checkProjectAccess(scope.row)"
                class="project-link"
            >
              {{ scope.row.projectName }}
            </a>
          </template>
        </el-table-column>
        <el-table-column prop="projectCode" label="Project Code" />
        <el-table-column prop="createTime" label="Create Time" />
        <el-table-column label="Action" width="180">
          <template #default="scope"><el-dropdown trigger="click">
              <el-button type="primary" link class="action-button">
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown><el-dropdown-menu>
                  <el-dropdown-item @click="getDetail(scope.row)"><el-icon><Document /></el-icon> detail
                  </el-dropdown-item>
                <el-dropdown-item @click="handleEdit(scope.row)">
                    <el-icon><EditPen /></el-icon> edit
                  </el-dropdown-item>
                <el-dropdown-item @click="handleDelete(scope.row)">
                    <el-icon><Delete /></el-icon> delete
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="pFormVisi" :title="projectFormTitle" width="600px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="formDataForAddProject" :rules="rulesForForm" label-width="150px">
        <el-form-item label="projectName" prop="projectName">
          <el-input v-model="formDataForAddProject.projectName" placeholder="projectName"/>
        </el-form-item>
        <el-form-item label="projectCode" prop="projectCode"><el-input v-model="formDataForAddProject.projectCode" placeholder="projectCode"/>
        </el-form-item>
        <el-form-item label="description" prop="description">
          <el-input v-model="formDataForAddProject.description" type="textarea"
              placeholder="description"/>
        </el-form-item>
        <el-form-item label="startDate" prop="startDate">
          <el-date-picker
              v-model="formDataForAddProject.startDate"
              type="date" placeholder="choose startDate"
              format="YYYY-MM-DD" value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="expectedEndDate" prop="expectedEndDate">
          <el-date-picker
              v-model="formDataForAddProject.expectedEndDate"
              type="date" placeholder="expectedEndDate"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"/>
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
          <el-input-number
              v-model="formDataForAddProject.budget"
              :precision="2"
              :step="1000"
              :min="0"
              placeholder="input the budget"/>
        </el-form-item>
        <el-form-item label="remark" prop="remark">
          <el-input
              v-model="formDataForAddProject.remark"
              type="textarea"
              placeholder="remark"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="pFormVisi = false">cancel</el-button>
          <el-button type="primary" @click="submitForm">confirm</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="detailV" title="details" width="800px">
      <div class="detail-container" v-if="cPro">
        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><InfoFilled /></el-icon>
           info</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="projectName">{{ cPro.projectName }}</el-descriptions-item>
            <el-descriptions-item label="projectCode">{{ cPro.projectCode }}</el-descriptions-item>
            <el-descriptions-item label="createTime">{{ cPro.createTime }}</el-descriptions-item>
            <el-descriptions-item label="status">
              <el-tag :type="setStatusType(cPro.status)">{{ cPro.status }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="startDate">{{ cPro.startDate }}</el-descriptions-item>
            <el-descriptions-item label="expectedEndDate">{{ cPro.expectedEndDate }}</el-descriptions-item>
            <el-descriptions-item label="priority">
              <el-tag :type="getPriorityType(cPro.priority)">
                {{ showPriority(cPro.priority) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="budget">{{ cPro.budget }}</el-descriptions-item>
          </el-descriptions>
        </div>


        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><Document /></el-icon>
            description
          </h3>
          <div class="description-content">{{ cPro.description}}</div>
        </div>


        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><ChatLineSquare /></el-icon>
            remark
          </h3>
          <div class="description-content">{{ cPro.remark }}</div>
        </div>


        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><User /></el-icon>
            member

          </h3>
          <el-table :data="cPMember" style="width: 100%">
            <el-table-column prop="userName" label="userName" />
            <el-table-column prop="nickName" label="Name" />
            <el-table-column prop="role" label="role" />
            <el-table-column prop="joinDate" label="joinDate" />
            <el-table-column prop="allocationPercentage" label="allocationPercentage">
              <template #default="{ row }">
                {{ row.allocationPercentage }}%
              </template>
            </el-table-column>
            <el-table-column prop="notes" label="notes" show-overflow-tooltip />

          </el-table>
        </div>
      </div>
    </el-dialog>

    <el-dialog v-model="booleanForVis" :title="projectUpdateFormTitle" width="600px" :close-on-click-modal="false">
      <el-form ref="updateFormRef" :model="theBiggsetForm" :rules="rulesForUpdateForm" label-width="150px">
        <el-form-item label="projectName" prop="projectName">
          <el-input v-model="theBiggsetForm.projectName" placeholder="projectName"/>
        </el-form-item>
        <el-form-item label="projectCode" prop="projectCode">
          <el-input v-model="theBiggsetForm.projectCode" placeholder="projectCode"/>
        </el-form-item>
        <el-form-item label="description" prop="description">
          <el-input v-model="theBiggsetForm.description" type="textarea" placeholder="description"/>
        </el-form-item>
        <el-form-item label="startDate" prop="startDate">
          <el-date-picker v-model="theBiggsetForm.startDate" type="date" placeholder="choose startDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="expectedEndDate" prop="expectedEndDate">
          <el-date-picker v-model="theBiggsetForm.expectedEndDate" type="date" placeholder="expectedEndDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="actualEndDate" prop="actualEndDate">
          <el-date-picker v-model="theBiggsetForm.actualEndDate" type="date" placeholder="actualEndDate" format="YYYY-MM-DD" value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="priority" prop="priority">
          <el-select v-model="theBiggsetForm.priority" placeholder="priority">
            <el-option label="low" :value="1"/><el-option label="medium" :value="2"/><el-option label="high" :value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item label="status" prop="status">
          <el-select v-model="theBiggsetForm.status" placeholder="Status">
            <el-option label="Not Started" value="Not Started"/>
            <el-option label="In Progress" value="In Progress"/>
            <el-option label="Completed" value="Completed"/>
            <el-option label="Paused" value="Paused"/>
          </el-select>
        </el-form-item>
        <el-form-item label="completionPercentage" prop="completionPercentage">
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
          <el-button @click="booleanForVis = false">cancel</el-button><el-button type="primary" @click="updateForm">confirm</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { ref, onMounted ,reactive} from 'vue'

import { ElMessage } from 'element-plus'
import {listProject, addProject, getProject, delProject, updateProject} from '@/api/project/index.js'

import { listProjectMember } from '@/api/project/member.js'
import {  EditPen, Delete, InfoFilled, Document, ChatLineSquare, User, Plus, MoreFilled,
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

const currentRow = ref(null)
const pFormVisi = ref(false)
const booleanForVis = ref(false)

import useUserStore from "@/store/modules/user.js"


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

const setStatusType = (status) => {
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

const getList = async () => {
  const userStore = useUserStore()
  const userId = userStore.id

    const allProject = await listProject({
      pageNum: 1,
      pageSize: 999,
      userId:userId,
    })
    // console.log('allProject:', allProject)
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
        ElMessage.success('Project created successfully')
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
        ElMessage.success('编辑成功')
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
    projectUpdateFormTitle.value = 'edit'
    if (updateFormRef.value) {
      updateFormRef.value.resetFields()
    }
    Object.assign(theBiggsetForm, projectRes.data);

}

const doDelete = async (row) => {
    await delProject(row.projectId)
    ElMessage.success('false')
    getList() // 刷新列表
}


onMounted(() => {
  getList()
})
</script>

<style scoped>
@import "index.scss";
</style>