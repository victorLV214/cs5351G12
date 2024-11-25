<template>
  <div class="app-container">
    <el-card class="box-card">
      <div class="mb-2">
        <el-button type="primary" @click="doADD">ADD</el-button>
      </div>
      <el-table v-loading="loading" :data="members">
        <el-table-column label="Member ID" align="center" prop="projectMemberId" />
        <el-table-column label="Role" align="center" prop="role" />
        <el-table-column label="Join Date" align="center" prop="joinDate" />
        <el-table-column label="Allocation" align="center">
          <template #default="{ row }">
            {{ row.allocationPercentage }}%
          </template>
        </el-table-column>
        <el-table-column label="Status" align="center">
          <template #default="{ row }">
            <el-tag :type="row.isActive === 1 ? 'success' : 'info'">
              {{ row.isActive === 1 ? 'Active' : 'Inactive' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Notes" align="center" prop="notes" />
        <el-table-column label="Operations" align="center" width="180" >
          <template #default="{ row }">
            <el-row :gutter="10">
              <el-col :span="12">
                <el-button size="small" type="primary" class="w-100"
                           @click="editMember(row)" :icon="Edit">Edit
                </el-button>
              </el-col>
              <el-col :span="12">
                <el-button size="small" type="danger" class="w-100"
                           @click="doDelete(row)" :icon="Delete">Delete</el-button>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog title="user" v-model="buttenVis" width="600px">
      <el-form ref="formRef" :model="form" :ruleX="ruleX" label-width="100px">
        <el-form-item label="Role" prop="role">
          <el-input v-model="form.role" placeholder="Role" />
        </el-form-item>
        <el-form-item label="Allocation" prop="allocationPercentage">
          <el-input-number v-model="form.allocationPercentage" :min="0" :max="100"/>&nbsp;%
        </el-form-item>
        <el-form-item label="Permissions" prop="permissions">
          <el-select v-model="form.permissions" multiple placeholder="permissions">
            <el-option label="read" value="read"/>
            <el-option label="write" value="write"/>
          </el-select>
        </el-form-item>
        <el-form-item label="Notes" prop="notes">
          <el-input type="textarea" v-model="form.notes" placeholder="notes"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="buttenVis = false">Cancel</el-button>
        <el-button type="primary" @click="subF">Confirm</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, onMounted} from 'vue'
import {useRoute} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import {listUser} from '@/api/system/user'

const route = useRoute()
const formRef = ref(null)
const loading = ref(false)
const members = ref([])
const buttenVis = ref(false)
const usersss = ref([])
import {listProjectMember, addProjectMember, updateProjectMember, delProjectMember} from '@/api/project/member'
import {Delete, Edit} from "@element-plus/icons-vue";

const form = reactive({
  projectMemberId: null, // 用于区分新增和编辑操作
  userId: undefined,
  role: 'Member', // 设置默认角色
  allocationPercentage: 50, // 默认分配百分比
  permissions: ['read'], // 默认权限
  notes: '', // 默认备注为空
  isActive: 1, // 默认状态为激活
  projectId: route.params.id, // 从路由参数获取项目ID
  joinDate: new Date().toISOString() // 默认加入日期为当前日期
})

const ruleX = {
  userId: [{required: true, message: 'userId', trigger: 'change'}],
  role: [{required: true, message: 'role', trigger: 'blur'}],
  allocationPercentage: [{required: true, message: 'allocationPercentage', trigger: 'blur'}],
  permissions: [{required: true, message: 'permissions', trigger: 'change'}]
}

const queryParams = reactive({
  projectId: route.params.id
})

const getList = async () => {
  loading.value = true
  const response = await listProjectMember(queryParams)
  members.value = response.rows
  loading.value = false
}

const doADD = async () => {
  buttenVis.value = true
  const response = await listUser()
  usersss.value = response.rows
  form.projectMemberId = null // 新增操作时清空 projectMemberId
  form.userId = usersss.value.length > 0 ? usersss.value[0].userId : undefined
  form.role = 'Member'
  form.allocationPercentage = 50
  form.permissions = ['read']
  form.notes = ''
  form.isActive = 1
  form.joinDate = new Date().toISOString()
  console.log(usersss)
}

const subF = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      form.permissions = form.permissions.join(',')
      if (form.projectMemberId) {
        await updateProjectMember(form)
      } else {
        await addProjectMember(form)
      }
      ElMessage.success('success')
      buttenVis.value = false
      await getList()
    }
  })
}

const doDelete = async (row) => {
  await ElMessageBox.confirm(
      'This action will permanently delete this Member. Continue?',
      'Warning',
      {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  )
  await delProjectMember(row.projectMemberId)
  ElMessage.success('success')
  await getList()
}

const editMember = (row) => {
  form.projectMemberId = row.projectMemberId // 编辑操作时设置 projectMemberId
  form.userId = row.userId
  form.role = row.role
  form.allocationPercentage = row.allocationPercentage
  form.permissions = row.permissions.split(',')
  form.notes = row.notes
  form.isActive = row.isActive
  form.projectId = row.projectId
  form.joinDate = row.joinDate
  buttenVis.value = true
}

onMounted(() => {
  getList()
})
</script>