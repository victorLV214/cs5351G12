<template>
  <div class="app-container">
    <el-card class="box-card">
      <div class="mb-2">
        <el-button type="primary" @click="handleAdd">添加成员</el-button>
      </div>
      <el-table v-loading="loading" :data="memberList">
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
        <el-table-column label="操作" align="center">
          <template #default="{ row }">
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加成员对话框 -->
    <el-dialog title="添加项目成员" v-model="dialogVisible" width="600px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择用户" prop="userId">
          <el-select v-model="form.userId" placeholder="请选择用户" filterable>
            <el-option
                v-for="item in userList"
                :key="item.userId"
                :label="item.userName"
                :value="item.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-input v-model="form.role" placeholder="请输入角色" />
        </el-form-item>
        <el-form-item label="工作量分配" prop="allocationPercentage">
          <el-input-number
              v-model="form.allocationPercentage"
              :min="0"
              :max="100"
              placeholder="请输入分配比例"
          />
        </el-form-item>
        <el-form-item label="权限" prop="permissions">
          <el-select v-model="form.permissions" multiple placeholder="请选择权限">
            <el-option label="读取" value="read" />
            <el-option label="写入" value="write" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="notes">
          <el-input type="textarea" v-model="form.notes" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { listProjectMember, addProjectMember, delProjectMember } from '@/api/project/member'
import { listUser } from '@/api/system/user'

export default {
  name: 'ProjectMemberList',
  data() {
    return {
      loading: false,
      memberList: [],
      dialogVisible: false,
      userList: [],
      form: {
        userId: undefined,
        role: '',
        allocationPercentage: 50,
        permissions: [],
        notes: '',
        isActive: 1,
        projectId: this.$route.params.id,
        joinDate: new Date().toISOString()
      },
      rules: {
        userId: [{ required: true, message: '请选择用户', trigger: 'change' }],
        role: [{ required: true, message: '请输入角色', trigger: 'blur' }],
        allocationPercentage: [{ required: true, message: '请输入分配比例', trigger: 'blur' }],
        permissions: [{ required: true, message: '请选择权限', trigger: 'change' }]
      },
      queryParams: {
        projectId: this.$route.params.id
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listProjectMember(this.queryParams).then(response => {
        this.memberList = response.rows
        this.loading = false
      }).catch(error => {
        console.error(error)
        this.loading = false
      })
    },
    handleAdd() {
      this.dialogVisible = true
      listUser().then(response => {
        this.userList = response.rows
      })
    },
    submitForm() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.permissions = this.form.permissions.join(',')
          addProjectMember(this.form).then(() => {
            this.$message.success('添加成功')
            this.dialogVisible = false
            this.getList()
          })
        }
      })
    },
    // 删除单个成员
    handleDelete(row) {
      this.$modal.confirm('确认删除该成员吗？').then(() => {
        delProjectMember(row.projectMemberId).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      })
    }
  }
}
</script>