<template>
  <div class="project-container">
    <el-card class="project-card">
      <template #header>
        <div class="card-header">
          <span class="header-title">Project List</span>
          <el-button type="primary" @click="handleAddProjects" class="add-button">New Project</el-button>
        </div>
      </template>
      <el-table
          :data="projectList" style="width: 100%" class="project-table" :row-class-name="tableRowClassName" @row-click="handleRowsClickSingleProject">
        <el-table-column prop="projectName" label="Project Name">
          <template #default="scope">
            <router-link
                :to="`/project/detail/${scope.row.projectId}`"
                class="project-link"
            >
              {{ scope.row.projectName }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="projectCode" label="Project Code" />
        <el-table-column prop="createTime" label="Create Time" />
        <el-table-column label="Action" width="180">
          <template #default="scope">

            <el-dropdown trigger="click">
              <el-button type="primary" link class="action-button">
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleEdit(scope.row)">
                    <el-icon><EditPen /></el-icon> edit
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleDetails(scope.row)">
                    <el-icon><Document /></el-icon> detail
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog v-model="projectFormVisible" :title="projectFormTitle" width="600px" :close-on-click-modal="false">
      <el-form ref="formRef" :model="formDataForAddProject" :rules="rulesForForm" label-width="120px">
        <el-form-item label="projectName" prop="projectName">
          <el-input v-model="formDataForAddProject.projectName" placeholder="projectName"/>
        </el-form-item>
        <el-form-item label="projectCode" prop="projectCode">
          <el-input v-model="formDataForAddProject.projectCode" placeholder="projectCode"/>
        </el-form-item>
        <el-form-item label="description" prop="description">
          <el-input
              v-model="formDataForAddProject.description"
              type="textarea"
              placeholder="description"/>
        </el-form-item>
        <el-form-item label="startDate" prop="startDate">
          <el-date-picker
              v-model="formDataForAddProject.startDate"
              type="date"
              placeholder="choose startDate"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="expectedEndDate" prop="expectedEndDate">
          <el-date-picker
              v-model="formDataForAddProject.expectedEndDate"
              type="date"
              placeholder="expectedEndDate"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="priority" prop="priority">
          <el-select v-model="formDataForAddProject.priority" placeholder="priority">
            <el-option label="low" :value="1"/>
            <el-option label="medium" :value="2"/>
            <el-option label="high" :value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item label="status" prop="status">
          <el-select v-model="formDataForAddProject.status" placeholder="status">
            <el-option label="未开始" value="未开始"/>
            <el-option label="进行中" value="进行中"/>
            <el-option label="已完成" value="已完成"/>
            <el-option label="已暂停" value="已暂停"/>
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
          <el-button @click="projectFormVisible = false">cancle</el-button>
          <el-button type="primary" @click="submitForm">confirm</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="detailV" title="details" width="800px">
      <div class="detail-container" v-if="currentProject">
        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><InfoFilled /></el-icon>
            基本信息
          </h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="projectName">{{ currentProject.projectName }}</el-descriptions-item>
            <el-descriptions-item label="projectCode">{{ currentProject.projectCode }}</el-descriptions-item>
            <el-descriptions-item label="createTime">{{ currentProject.createTime }}</el-descriptions-item>
            <el-descriptions-item label="status">
              <el-tag :type="setStatusType(currentProject.status)">{{ currentProject.status }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="startDate">{{ currentProject.startDate }}</el-descriptions-item>
            <el-descriptions-item label="expectedEndDate">{{ currentProject.expectedEndDate }}</el-descriptions-item>
            <el-descriptions-item label="priority">
              <el-tag :type="getPriorityType(currentProject.priority)">
                {{ getPriorityLabel(currentProject.priority) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="budget">{{ currentProject.budget }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 项目描述部分 -->
        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><Document /></el-icon>
            description
          </h3>
          <div class="description-content">{{ currentProject.description}}</div>
        </div>

        <!-- 备注部分 -->
        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><ChatLineSquare /></el-icon>
            remark
          </h3>
          <div class="description-content">{{ currentProject.remark }}</div>
        </div>

        <!-- 项目成员部分 -->
        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><User /></el-icon>
            member

          </h3>
          <el-table :data="currentProjectMembers" style="width: 100%">
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



  </div>
</template>

<script setup>
import { ref, onMounted ,reactive} from 'vue'

import { ElMessage } from 'element-plus'
import { listProject, addProject, getProject } from '@/api/project/index.js'
import useUserStore from "@/store/modules/user.js";
import { listProjectMember } from '@/api/project/member.js'
import {  EditPen, Delete, InfoFilled, Document, ChatLineSquare, User, Plus, MoreFilled,
  } from '@element-plus/icons-vue'
import {getUser} from "@/api/system/user.js";



const currentRow = ref(null)
const projectFormVisible = ref(false)
const projectFormTitle = ref('')
const formRef = ref(null)
const detailV = ref(false)
const currentProject = ref(null)
const currentProjectMembers = ref([])
const formDataForAddProject = reactive({
  projectName: '',
  projectCode: '',//  项目编码
  description: '',
  startDate: '',
  expectedEndDate: '',
  priority: 2,//优先级（1=低，2=中，3=高）
  status: '未开始',
  budget: 0,
  remark: '',
  projectManagerId: '',// 项目经理
})






const tableRowClassName = ({ row }) => {
  if (currentRow.value && currentRow.value === row) {
    return 'selected-row'
  }
  return ''
} // for select the row



const handleRowsClickSingleProject = (row) => {
  currentRow.value = row
}


const handleDetails = async (row) => {
  try {
    const id = row.projectId
    const projectRes = await getProject(id)
    // console.log('projectRes:', projectRes)
    currentProject.value = projectRes.data
    await myGetMembers(id)
    
    detailV.value = true
  } catch (error) {
    console.error('获取项目详情失败:', error)
    ElMessage.error('获取项目详情失败')
  }
}




const myGetMembers = async (projectId) => {
  try {
    const members = await listProjectMember({
      projectId: projectId,
      pageNum: 1,
      pageSize: 999
    })
    // console.log('members:', members)
    const membersWithUserInfo = []
    for (const member of members.rows) {
      try {
        const userInfo = await getUser(member.userId)
        membersWithUserInfo.push({
          ...member,
          userName: userInfo.data.userName,
          nickName: userInfo.data.nickName,
          email: userInfo.data.email
        })
      } catch (error) {
        console.error('获取用户信息失败:', error)
        membersWithUserInfo.push({
          ...member,
          userName: '未知用户',
          nickName: '-',
          email: '-'
        })
      }
    }
    currentProjectMembers.value = membersWithUserInfo
    
  } catch (error) {
    
    console.error('获取项目成员列表失败:', error)
    ElMessage.error('获取项目成员列表失败')
  }
}

const setStatusType = (status) => {
  const statusMap = {
    '未开始': 'info',
    '进行中': '',//error!!!
    '已完成': 'success',
    '已暂停': 'warning'
  }
  return statusMap[status] || ''
}

// 获取优先级对应的类型和标签
const getPriorityType = (priority) => {
  const priorityMap = {
    1: 'info',
    2: 'warning',
    3: 'danger'
  }
  return priorityMap[priority]
}

const getPriorityLabel = (priority) => {
  const priorityMap = {
    1: '低',
    2: '中',
    3: '高'
  }
  return priorityMap[priority] || 'unknown'
}
const rulesForForm = {
  projectName: [
    { required: true, message: '请输入项目名称', trigger: 'blur' }
  ],
  projectCode: [
    { required: true, message: '请输入项目编码', trigger: 'blur' }
  ],
  startDate: [
    { required: true, message: '请选择开始日期', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择项目状态', trigger: 'change' }
  ],
  priority: [
    { required: true, message: '请选择优先级', trigger: 'change' }
  ]
}

const projectList = ref([])

const handleAddProjects = () => {
  projectFormVisible.value = true
  projectFormTitle.value = '新增项目'
  // 重置表单
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const getList = async () => {
  const userStore = useUserStore()
  const userId = userStore.id
  try {
    const allProject = await listProject({
      pageNum: 1,
      pageSize: 999,
      userId:userId,
    })
    // console.log('allProject:', allProject)
    projectList.value = allProject.rows
  } catch (error) {
    console.error('获取项目列表失败:', error)
    ElMessage.error('获取项目列表失败')
  }
}

const submitForm = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const userStore = useUserStore()
        const userId = userStore.id
        formDataForAddProject.projectManagerId = userId
        await addProject(formDataForAddProject)

        ElMessage.success('新增成功')
        projectFormVisible.value = false
        getList() // 刷新列表
      } catch (error) {
        ElMessage.error('新增失败')
      }
    }
  })
}

const handleEdit = (row) => {

  console.log('编辑:', row)
}


onMounted(() => {
  getList()
})
</script>

<style scoped>
.project-container {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 48px);
}

.project-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
}

.header-title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.add-button {
  padding: 8px 16px;
  font-weight: 500;
}

.project-table {
  margin-top: 8px;
}

.action-button {
  padding: 4px 0;
  margin: 0 8px;
  font-size: 14px;
}

.action-button:first-child {
  margin-left: 0;
}

:deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #ebeef5;
}

:deep(.el-table) {
  --el-table-header-bg-color: #f5f7fa;
}

:deep(.el-table th) {
  font-weight: 600;
}

:deep(.el-table__row) {
  transition: background-color 0.3s;
}

:deep(.el-table__row:hover) {
  background-color: #f5f7fa;
}
</style>