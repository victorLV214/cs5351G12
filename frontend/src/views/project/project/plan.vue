<template>
  <div>
    <h1>Project Plan</h1>
    <div class="task-form">
    <el-form :model="taskForm" ref="taskForm" label-width="120px" class="task-form">
      <el-form-item label="Task Name" required>
        <el-input v-model="taskForm.name" placeholder="Enter task name" type="text"  clearable style="width: 200px"/>
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
import { getProject, addProject, delProject } from "@/api/project"; // Project API functions
import { listProjectMember, addProjectMember, delProjectMember } from "@/api/project/member";
import {getUser} from "@/api/system/user.js"; // Member API functions
import{useRoute} from "vue-router";
import {listRequirement} from "@/api/project/requirements.js";
import * as echarts from 'echarts'


const projectInfo = ref({})
const route = useRoute()
const projectId=route.params.id
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

const tasks = ref([]);/* insert logic to retrieve project ID */

// Fetch project details and associated tasks
const fetchProjectDetails = async () => {
  try {
    const response = await getProject(projectId);
    tasks.value = response.data.tasks || [];
  } catch (error) {
    console.error("Failed to fetch project details:", error);
  }
};

// Fetch project members
const fetchMembers = async () => {

  const res = await listProjectMember(projectId)
  // console.log(res)
  memberList.value = res.rows

  const details = []
  for(let i=0;i<memberList.value.length;i++){
    const user = memberList.value[i]
    const userDetail = await getUser(user.userId)
    details.push(userDetail.data)
  }
  memberList.value = details
  console.log(memberList.value)

}

// Add a new task to the project
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
    await addProject(newTask); // Adjust according to your API structure
    await fetchProjectDetails(); // Refresh task list
    resetTaskForm();
  } catch (error) {
    console.error("Failed to add task:", error);
  }
};

// Reset the task form
const resetTaskForm = () => {
  taskForm.value = {
    name: '',
    assignedTo: '',
    deadline: null,
  };
};

// Delete a task from the project
const deleteTask = async (taskId) => {
  try {
    await delProject(taskId); // Adjust delete method if necessary
    await fetchProjectDetails(); // Refresh task list
  } catch (error) {
    console.error("Failed to delete task:", error);
  }
};

// Get member name by userId
const getMembers = async () => {


  const res = await listProjectMember(projectId)
  // console.log(res)
  memberList.value = res.rows

  const details = []
  for(let i=0;i<memberList.value.length;i++){
    const user = memberList.value[i]
    const userDetail = await getUser(user.userId)
    details.push(userDetail.data)
  }
  memberList.value = details
  console.log(memberList.value)

}

// Add a new project member
const addMember = async () => {
  if (!memberForm.value.nickName || !memberForm.value.email) {
    alert("Please fill in all fields.");
    return;
  }
  try {
    const newMember = {
      ...memberForm.value,
      projectId, // Associate with the project
    };
    await addProjectMember(newMember); // Call to add member
    fetchMembers(); // Refresh member list
    resetMemberForm();
  } catch (error) {
    console.error("Failed to add member:", error);
  }
};

// Reset the member form
const resetMemberForm = () => {
  memberForm.value = {
    nickName: '',
    email: '',
  };
};

// Delete a project member
const deleteMember = async (memberId) => {
  try {
    await delProjectMember(memberId); // Call to delete member
    fetchMembers(); // Refresh member list
  } catch (error) {
    console.error("Failed to delete member:", error);
  }
};

// On mounted lifecycle hook to fetch initial data
onMounted(() => {
  fetchProjectDetails(); // Fetch tasks for the project
  fetchMembers(); // Fetch members
});
</script>

<style scoped>
.task-form, .member-form {
  margin-bottom: 20px;
}
</style>
