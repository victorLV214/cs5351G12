<template>
  <el-card class="r-cards">
    <template #header><div class="card-header">
        <div class="header-left"><span class="title">requirment list</span><el-tag class="count-tag" type="info">total {{ total }} requirements</el-tag>
        </div>
      <div class="addR">
        <el-button type="primary" :icon="Plus" @click="addReq"></el-button>
      </div></div></template>


    <div class="search"><el-form :inline="true" :model="Params" class="sF"><el-form-item label="requirements">
          <el-input v-model="Params.title" placeholder="" clearable style="width: 200px"/>
        </el-form-item><el-form-item label="priority">
          <el-select v-model="Params.priority" placeholder="select priority" clearable style="width: 200px">
            <el-option label="high" value="1" />
            <el-option label="medium" value="2" />
            <el-option label="low" value="3" /></el-select>
        </el-form-item>
        <el-form-item label="status">
          <el-select v-model="Params.status" placeholder="status" clearable style="width: 200px"><el-option label="pending" value="待处理" />
            <el-option label="processing" value="进行中" /><el-option label="completed" value="已完成" />
          </el-select></el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="getReqs">select</el-button>
          <el-button :icon="Refresh" @click="clearAll">Refresh</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="rList">
      <el-table
          v-loading="loading"
          :data="requirementsList"
          style="width: 100%"
      >
        <el-table-column label="ID" align="center">
          <template #default="{ row }">
            <el-link type="primary" @click="handleView(row)">{{ row.requirementId }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="title" align="center" prop="title" />
        <el-table-column label="description" align="center" prop="description" show-overflow-tooltip />
        <el-table-column label="priority" align="center" prop="priority"></el-table-column>
        <el-table-column label="status" align="center" prop="status"></el-table-column>
        <el-table-column label="createTime" align="center" prop="createTime" width="180" />





      </el-table>


      <div class="pagination-container">
        <el-pagination
            :page-sizes="[10, 20, 50, 100]"
            :total="total"
            v-model:current-page="Params.pageNum"
            v-model:page-size="Params.pageSize"

            layout="total, sizes, prev, pager, next, jumper" @size-change="sizeP" @current-change="cP"
        />
      </div>
    </div>
  </el-card>
  <!-- 在el-card结束标签前添加 -->
  <el-dialog
      v-model="dialogVisible"
      title="需求详情"
      width="60%"
  >
    <el-form
        ref="formRef"
        :model="currentRequirement"
        :rules="rules"
        label-width="120px"
    >
      <el-form-item label="需求ID" prop="requirementId">
        <span>{{ currentRequirement?.requirementId }}</span>
      </el-form-item>

      <el-form-item label="需求标题" prop="title">
        <el-input v-model="currentRequirement.title" :disabled="!isEdit"/>
      </el-form-item>

      <el-form-item label="优先级" prop="priority">
        <el-select v-model="currentRequirement.priority" :disabled="!isEdit">
          <el-option label="high" value="1" />
          <el-option label="medium" value="2" />
          <el-option label="low" value="3" />
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="currentRequirement.status" :disabled="!isEdit">
          <el-option label="pending" value="待处理" />
          <el-option label="processing" value="进行中" />
          <el-option label="completed" value="已完成" />
        </el-select>
      </el-form-item>

      <el-form-item label="需求类型" prop="type">
        <el-input v-model="currentRequirement.type" :disabled="!isEdit"/>
      </el-form-item>

      <el-form-item label="指派给" prop="assignedTo">
        <el-input v-model="currentRequirement.assignedTo" :disabled="!isEdit"/>
      </el-form-item>

      <el-form-item label="描述" prop="description">
        <el-input
            v-model="currentRequirement.description"
            type="textarea"
            :rows="3"
            :disabled="!isEdit"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
            v-model="currentRequirement.remarks"
            type="textarea"
            :rows="3"
            :disabled="!isEdit"
        />
      </el-form-item>
    </el-form>

    <template #footer>
  <span class="dialog-footer">
    <el-button @click="dialogVisible  = false">关闭</el-button>
    <!-- 添加删除按钮，仅当创建者是当前用户时显示 -->
    <el-button
        v-if="currentRequirement?.createBy === username"
        type="danger"
        @click="handleDelete(currentRequirement)"
    >
      删除
    </el-button>
    <el-button v-if="!isEdit" type="primary" @click="handleEdit">编辑</el-button>
    <template v-else>
      <el-button @click="cancelEdit">取消</el-button>
      <el-button type="primary" @click="submitEdit">保存</el-button>
    </template>
  </span>
    </template>
  </el-dialog>
  <!-- 新增需求对话框 -->
  <el-dialog
      v-model="addDialogVisible"
      title="新增需求"
      width="60%"
  >
    <el-form
        ref="addFormRef"
        :model="reqForm"
        :rules="rules"
        label-width="120px"
    >
      <el-form-item label="需求标题" prop="title">
        <el-input v-model="reqForm.title" placeholder="请输入需求标题"/>
      </el-form-item>

      <el-form-item label="优先级" prop="priority">
        <el-select v-model="reqForm.priority" placeholder="请选择优先级">
          <el-option label="high" value="1" />
          <el-option label="medium" value="2" />
          <el-option label="low" value="3" />
        </el-select>
      </el-form-item>

      <el-form-item label="需求类型" prop="type">
        <el-input v-model="reqForm.type" placeholder="请输入需求类型"/>
      </el-form-item>

      <el-form-item label="指派给" prop="assignedTo">
        <el-input v-model="reqForm.assignedTo" placeholder="请输入负责人"/>
      </el-form-item>

      <el-form-item label="描述" prop="description">
        <el-input
            v-model="reqForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入需求描述"
        />
      </el-form-item>

      <el-form-item label="备注" prop="remarks">
        <el-input
            v-model="reqForm.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
        />
      </el-form-item>
    </el-form>

    <template #footer>
    <span class="dialog-footer">
      <el-button @click="addDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitAdd">确定</el-button>
    </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import { Search, Refresh, Plus, Edit, Delete, Download, View } from '@element-plus/icons-vue'

import {useRoute} from "vue-router";
import {
  addRequirement,
  delRequirement,
  getRequirementDetail,
  listRequirement,
  updateRequirement
} from "@/api/project/requirements.js";
import {ElMessage, ElMessageBox} from "element-plus";
import useUserStore from "@/store/modules/user.js";
import {getUser} from "@/api/system/user.js";
const requirementsList = ref([])
const userStore = useUserStore()
const total = ref(0)
const route = useRoute()
const username=ref(null)
const projectId=route.params.id
const loading = ref(false)
const dialogVisible = ref(false)
const currentRequirement = ref(null)
const formRef = ref(null)
const isEdit = ref(false)
const originalData = ref(null) // 用于存储原始数据
const addDialogVisible = ref(false)
const addFormRef = ref(null)
const deleteVisible = ref(false)
const id =userStore.id
const reqForm = ref({
  title: '',
  priority: '',
  status: '待处理',  // 默认状态
  type: '',
  assignedTo: '',
  description: '',
  remarks: '',
  projectId: projectId,  // 使用当前项目ID
  delFlag: 1,
  createBy: username.value,
})
const Params = ref({
  pageNum: 1,
  pageSize: 10,
  title: '',
  type: '',
  priority: '',
  status: '',
  assignedTo: '',
  projectId: projectId,
  delFlag:1,
})
const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  // 添加其他需要验证的字段
}
const  gettheUser = async () => {
  const res = await getUser(id)
  username.value = res.data.userName
  console.log(username.value)
}
const getReqs = async () => {
  loading.value = true
    const a = await listRequirement(Params.value)
    requirementsList.value = a.rows
    total.value = a.total
    loading.value = false
}

const clearAll = () => {
  Params.value = {
    pageNum: 1,
    pageSize: 10,
    title: '',
    delFlag:1,
    type: '',
    priority: '',
    status: '',
    assignedTo: '',
    projectId: projectId
  }
  getReqs()
}
const addReq = () => {
  addDialogVisible.value = true
  reqForm.value = {
    title: '',
    priority: '',
    status: '待处理',
    type: '',
    assignedTo: '',
    description: '',
    remarks: '',
    projectId: projectId,
    delFlag: 1,
    createBy: username.value,
  }
}
// 提交新增
const submitAdd = async () => {
  if (!addFormRef.value) return

  try {
    await addFormRef.value.validate()
    loading.value = true
    await addRequirement(reqForm.value)

    ElMessage.success('添加成功')
    addDialogVisible.value = false
    await getReqs() // 刷新列表
  } catch (error) {
    ElMessage.error('添加失败')
  } finally {
    loading.value = false
  }
}


const handleView = async (row) => {
  try {
    loading.value = true

    const res = await getRequirementDetail(row.requirementId)
    console.log(res)
    currentRequirement.value = res.data
    originalData.value = JSON.parse(JSON.stringify(res.data)) // 保存原始数据的副本
    isEdit.value = false
    if(currentRequirement.value.createBy === username.value){
      deleteVisible.value = true
    }
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('获取详情失败')
  } finally {
    loading.value = false
  }
}
// 修改
const handleEdit = () => {
  isEdit.value = true
}
const cancelEdit = () => {
  currentRequirement.value = JSON.parse(JSON.stringify(originalData.value))
  isEdit.value = false
}
const submitEdit = async () => {
  if (!formRef.value) return

  try {
    await formRef.value.validate()

    loading.value = true
    await updateRequirement(currentRequirement.value)

    ElMessage.success('更新成功')
    isEdit.value = false
    dialogVisible.value = false
    await getReqs() // 刷新列表
  } catch (error) {
    ElMessage.error('更新失败')
  } finally {
    loading.value = false
  }
}
// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm(
      '确认要删除这个需求吗？',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(async () => {
        try {
          loading.value = true
          await delRequirement(row.requirementId)
          ElMessage.success('删除成功')
          dialogVisible.value = false
          await getReqs() // 刷新列表
        } catch (error) {
          ElMessage.error('删除失败')
        } finally {
          loading.value = false
        }
      })
      .catch(() => {
        ElMessage.info('已取消删除')
      })
}
const sizeP = (val) => {
  Params.value.pageSize = val
  getReqs()
}
const cP = (val) => {
  Params.value.pageNum = val
  getReqs()
}


onMounted(() => {
  getReqs()
  gettheUser()
})
</script>

<style scoped>
.r-cards {
  margin: 16px;
  min-height: calc(100vh - 150px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.count-tag {
  font-size: 13px;
}

.search {
  margin-bottom: 16px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.sF {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.rList {
  margin-top: 16px;
}
@media screen and (max-width: 768px) {
  .sF {
    :deep(.el-form-item) {
      margin-bottom: 16px;
    }
  }
}

:deep(.el-form-item__label) {
  font-weight: normal;
}

:deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #e4e7ed;
}

:deep(.el-card__body) {
  padding: 20px;
}
</style>