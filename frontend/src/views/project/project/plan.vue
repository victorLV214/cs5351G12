<template>
  <div class="project-container">

    <el-card class="project-info-card">
      <template #header>
        <div class="card-header">
          <span class="title">Project Overview</span>
        </div>
      </template>
      <el-descriptions :column="3" border>
        <el-descriptions-item label="Project Name">{{ projectInfo.projectName }}</el-descriptions-item>
        <el-descriptions-item label="Project Code">{{ projectInfo.projectCode }}</el-descriptions-item>
        <el-descriptions-item label="Status">
          <el-tag :type="getStatusType(projectInfo.status)">{{ projectInfo.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Start Date">{{ projectInfo.startDate }}</el-descriptions-item>
        <el-descriptions-item label="Expected End Date">{{ projectInfo.expectedEndDate }}</el-descriptions-item>
        <el-descriptions-item label="Actual End Date">{{ projectInfo.actualEndDate }}</el-descriptions-item>
        <el-descriptions-item label="Priority">
          <el-tag :type="getPriorityType(projectInfo.priority)">
            {{ getPriorityLabel(projectInfo.priority) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Progress">
          <el-progress
              :percentage="projectInfo.completionPercentage"
              :format="(percentage) => percentage + '%'"
          />
        </el-descriptions-item>
        <el-descriptions-item label="Budget">$ {{ formatMoney(projectInfo.budget) }}</el-descriptions-item>
        <el-descriptions-item label="Actual Cost">$ {{ formatMoney(projectInfo.actualCost) }}</el-descriptions-item>
        <el-descriptions-item label="Description" :span="3">{{ projectInfo.description }}</el-descriptions-item>
      </el-descriptions>
    </el-card>


    <h1>Project Plan</h1>
    <div class="task-form">
      <el-form :model="taskForm" ref="taskForm" label-width="120px" class="task-form">
        <el-form-item label="Task Name" required>
          <el-input v-model="taskForm.name" placeholder="Enter task name" type="text" clearable style="width: 200px"/>
        </el-form-item>
        <el-form-item label="Assigned To" required>
          <el-select v-model="taskForm.assignedTo" placeholder="Select assignee">
            <el-option
                v-for="member in memberList"
                :key="member.userId"
                :label="member.nickName"
                :value="member.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Deadline" required>
          <el-date-picker v-model="taskForm.deadline" type="Date" placeholder="Select date" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="addTask">Add Task</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="tasks" style="width: 100%">
      <el-table-column prop="name" label="Task Name" />
      <el-table-column prop="assignedTo" label="Assigned To">
        <template #default="scope">
          <span>{{ getMemberName(scope.row.assignedTo) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="deadline" label="Deadline" />
      <el-table-column prop="status" label="Status" />
      <el-table-column label="Actions">
        <template #default="scope">
          <el-button @click="editTask(scope.row)">Edit</el-button>
          <el-button type="danger" @click="deleteTask(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>




    <h2>Project Members</h2>
    <el-form :model="memberForm" ref="memberForm" label-width="120px" class="member-form">
      <el-form-item label="Member Name" required>
        <el-input v-model="memberForm.nickName" placeholder="Enter member name" />
      </el-form-item>
      <el-form-item label="Email" required>
        <el-input v-model="memberForm.email" placeholder="Enter email" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="addMember">Add Member</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="memberList" style="width: 100%">
      <el-table-column prop="nickName" label="Member Name" />
      <el-table-column prop="email" label="Email" />
      <el-table-column label="Actions">
        <template #default="scope">
          <el-button @click="editMember(scope.row)">Edit</el-button>
          <el-button type="danger" @click="deleteMember(scope.row.userId)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getProject, addProject, delProject } from "@/api/project";
import { listProjectMember, addProjectMember, delProjectMember } from "@/api/project/member";
import { getUser } from "@/api/system/user.js";
import { useRoute } from "vue-router";
import { listRequirement } from "@/api/project/requirements.js";
import * as echarts from 'echarts';

const projectInfo = ref({})
const route = useRoute()
const projectId = route.params.id
const memberList = ref([])
const taskForm = ref({
  name: '',
  assignedTo: '',
  deadline: null,
});

const memberForm = ref({
  nickName: '',
  email: '',
});

const tasks = ref([]);

// 获取项目基本信息
const fetchProjectInfo = async () => {
  try {
    const res = await getProject(projectId)
    if (res.code === 200) {
      projectInfo.value = res.data
    }
  } catch (error) {
    console.error('Failed to fetch project info:', error)
  }
}

// 状态标签类型
const getStatusType = (status) => {
  const statusMap = {
    'In Progress': 'primary',
    'Completed': 'success',
    'Delayed': 'warning',
    'Cancelled': 'danger'
  }
  return statusMap[status] || 'info'
}

// 优先级标签类型
const getPriorityType = (priority) => {
  const priorityMap = {
    1: 'danger',
    2: 'warning',
    3: 'info'
  }
  return priorityMap[priority] || 'info'
}

// 优先级显示文本
const getPriorityLabel = (priority) => {
  const priorityLabels = {
    1: 'High',
    2: 'Medium',
    3: 'Low'
  }
  return priorityLabels[priority] || 'Unknown'
}

// 格式化金额
const formatMoney = (amount) => {
  return amount?.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

// 原有的功能函数
const fetchProjectDetails = async () => {
  try {
    const response = await getProject(projectId);
    tasks.value = response.data.tasks || [];
  } catch (error) {
    console.error("Failed to fetch project details:", error);
  }
};

const fetchMembers = async () => {
  const res = await listProjectMember(projectId)
  memberList.value = res.rows
  const details = []
  for(let i=0; i<memberList.value.length; i++){
    const user = memberList.value[i]
    const userDetail = await getUser(user.userId)
    details.push(userDetail.data)
  }
  memberList.value = details
};

const addTask = async () => {
  if (!taskForm.value.name || !taskForm.value.assignedTo || !taskForm.value.deadline) {
    alert("Please fill in all fields.");
    return;
  }
  try {
    const newTask = {
      ...taskForm.value,
      projectId,
    };
    await addProject(newTask);
    await fetchProjectDetails();
    resetTaskForm();
  } catch (error) {
    console.error("Failed to add task:", error);
  }
};

const resetTaskForm = () => {
  taskForm.value = {
    name: '',
    assignedTo: '',
    deadline: null,
  };
};

const deleteTask = async (taskId) => {
  try {
    await delProject(taskId);
    await fetchProjectDetails();
  } catch (error) {
    console.error("Failed to delete task:", error);
  }
};

const addMember = async () => {
  if (!memberForm.value.nickName || !memberForm.value.email) {
    alert("Please fill in all fields.");
    return;
  }
  try {
    const newMember = {
      ...memberForm.value,
      projectId,
    };
    await addProjectMember(newMember);
    fetchMembers();
    resetMemberForm();
  } catch (error) {
    console.error("Failed to add member:", error);
  }
};

const resetMemberForm = () => {
  memberForm.value = {
    nickName: '',
    email: '',
  };
};

const deleteMember = async (memberId) => {
  try {
    await delProjectMember(memberId);
    fetchMembers();
  } catch (error) {
    console.error("Failed to delete member:", error);
  }
};

// 获取成员名称（如果需要的话补充此方法）
const getMemberName = (userId) => {
  const member = memberList.value.find(m => m.userId === userId);
  return member ? member.nickName : 'Unknown';
};

onMounted(() => {
  fetchProjectInfo();  // 获取项目基本信息
  fetchProjectDetails();
  fetchMembers();
});
</script>

<style scoped>
.project-container {
  padding: 20px;
}

.project-info-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 18px;
  font-weight: bold;
}

.task-form, .member-form {
  margin-bottom: 20px;
}
</style>