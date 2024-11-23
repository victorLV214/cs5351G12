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
        <el-table-column label="operation" align="center">
          <template #default="{ row }">
            <el-button type="danger" link @click="doDELETE1(row)">delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>


    <el-dialog title="user" v-model="buttenVis" width="600px">
      <el-form ref="formRef" :model="form" :ruleX="ruleX" label-width="100px">
        <el-form-item label="user" prop="userId">
          <el-select v-model="form.userId" placeholder="userId" filterable>
            <el-option
                v-for="item in usersss"
                :key="item.userId"
                :label="item.userName"
                :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="role" prop="role">
          <el-input v-model="form.role" placeholder="role" />
        </el-form-item>
        <el-form-item label="allocationPercentage" prop="allocationPercentage">
          <el-input-number v-model="form.allocationPercentage" :min="0" :max="100" placeholder="allocationPercentage"
          />
        </el-form-item>
        <el-form-item label="permissions" prop="permissions">
          <el-select v-model="form.permissions" multiple placeholder="permissions">
            <el-option label="read" value="read" />
            <el-option label="write" value="write" />
          </el-select>
        </el-form-item>
        <el-form-item label="notes" prop="notes">
          <el-input type="textarea" v-model="form.notes" placeholder="notes" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="buttenVis = false">CAL</el-button>
        <el-button type="primary" @click="subF">CONFIRM</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'


const route = useRoute()
const formRef = ref(null)
const loading = ref(false)
const members = ref([])
const buttenVis = ref(false)
const usersss = ref([])
import { listProjectMember, addProjectMember, delProjectMember } from '@/api/project/member'

const form = reactive({
  userId: undefined,
  role: '',
  allocationPercentage: 50,
  permissions: [],
  notes: '',
  isActive: 1,
  projectId: route.params.id,
  joinDate: new Date().toISOString()
})

const ruleX = {
  userId: [{ required: true, message: 'userId', trigger: 'change' }],
  role: [{ required: true, message: 'role', trigger: 'blur' }],
  allocationPercentage: [{ required: true, message: 'allocationPercentage', trigger: 'blur' }],
  permissions: [{ required: true, message: 'permissions', trigger: 'change' }]
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
}

const subF = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      form.permissions = form.permissions.join(',')
      await addProjectMember(form)
      ElMessage.success('success')
      buttenVis.value = false
      getList()
    }
  })
}

const doDELETE1 = async (row) => {
    await ElMessageBox.confirm('delete？')
    await delProjectMember(row.projectMemberId)
    ElMessage.success('success')
    getList()

}

onMounted(() => {
  getList()
})
</script>