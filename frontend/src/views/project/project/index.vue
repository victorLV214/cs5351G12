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
            <!-- 添加更多按钮 -->
            <el-dropdown trigger="click">
              <el-button type="primary" link class="action-button">
                <el-icon><MoreFilled /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleEdit(scope.row)">
                    <el-icon><EditPen /></el-icon> 编辑
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleDetails(scope.row)">
                    <el-icon><Document /></el-icon> 详情
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
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="formDataForAddProject.projectName" placeholder="请输入项目名称"/>
        </el-form-item>
        <el-form-item label="项目编码" prop="projectCode">
          <el-input v-model="formDataForAddProject.projectCode" placeholder="请输入项目编码"/>
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input
              v-model="formDataForAddProject.description"
              type="textarea"
              placeholder="请输入项目描述"/>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
          <el-date-picker
              v-model="formDataForAddProject.startDate"
              type="date"
              placeholder="请选择开始日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="预计结束日期" prop="expectedEndDate">
          <el-date-picker
              v-model="formDataForAddProject.expectedEndDate"
              type="date"
              placeholder="请选择预计结束日期"
              format="YYYY-MM-DD"
              value-format="YYYY-MM-DD"/>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="formDataForAddProject.priority" placeholder="请选择优先级">
            <el-option label="低" :value="1"/>
            <el-option label="中" :value="2"/>
            <el-option label="高" :value="3"/>
          </el-select>
        </el-form-item>
        <el-form-item label="项目状态" prop="status">
          <el-select v-model="formDataForAddProject.status" placeholder="请选择项目状态">
            <el-option label="未开始" value="未开始"/>
            <el-option label="进行中" value="进行中"/>
            <el-option label="已完成" value="已完成"/>
            <el-option label="已暂停" value="已暂停"/>
          </el-select>
        </el-form-item>
        <el-form-item label="项目预算" prop="budget">
          <el-input-number
              v-model="formDataForAddProject.budget"
              :precision="2"
              :step="1000"
              :min="0"
              placeholder="请输入项目预算"/>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
              v-model="formDataForAddProject.remark"
              type="textarea"
              placeholder="请输入备注"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="projectFormVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="detailV" title="项目详情" width="800px">
      <div class="detail-container" v-if="currentProject">
        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><InfoFilled /></el-icon>
            基本信息
          </h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="项目名称">{{ currentProject.projectName }}</el-descriptions-item>
            <el-descriptions-item label="项目编码">{{ currentProject.projectCode }}</el-descriptions-item>
            <el-descriptions-item label="创建时间">{{ currentProject.createTime }}</el-descriptions-item>
            <el-descriptions-item label="项目状态">
              <el-tag :type="getStatusType(currentProject.status)">{{ currentProject.status }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="开始日期">{{ currentProject.startDate }}</el-descriptions-item>
            <el-descriptions-item label="预计结束日期">{{ currentProject.expectedEndDate }}</el-descriptions-item>
            <el-descriptions-item label="优先级">
              <el-tag :type="getPriorityType(currentProject.priority)">
                {{ getPriorityLabel(currentProject.priority) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="项目预算">{{ currentProject.budget }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 项目描述部分 -->
        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><Document /></el-icon>
            项目描述
          </h3>
          <div class="description-content">{{ currentProject.description}}</div>
        </div>

        <!-- 备注部分 -->
        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><ChatLineSquare /></el-icon>
            备注
          </h3>
          <div class="description-content">{{ currentProject.remark }}</div>
        </div>

        <!-- 项目成员部分 -->
        <div class="detail-section">
          <h3 class="section-title">
            <el-icon><User /></el-icon>
            项目成员

          </h3>
          <el-table :data="currentProjectMembers" style="width: 100%">
            <el-table-column prop="userName" label="成员姓名" />
            <el-table-column prop="nickName" label="昵称" />
            <el-table-column prop="role" label="角色" />
            <el-table-column prop="joinDate" label="加入时间" />
            <el-table-column prop="allocationPercentage" label="工时分配">
              <template #default="{ row }">
                {{ row.allocationPercentage }}%
              </template>
            </el-table-column>
            <el-table-column prop="notes" label="备注" show-overflow-tooltip />

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
}
const handleRowsClickSingleProject = (row) => {
  currentRow.value = row
}
const handleDetails = async (row) => {
  try {
    const id = row.projectId
    const projectRes = await getProject(id)
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

const getStatusType = (status) => {
  const statusMap = {
    '未开始': 'info',
    '进行中': '',
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
  return priorityMap[priority] || '未知'
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
  // 处理编辑项目
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