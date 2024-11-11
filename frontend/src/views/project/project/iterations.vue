<template>
  <div>
    <h1>Iteration Management</h1>

    <div class="iteration-form">
      <el-form :model="sprintForm" ref="sprintForm" label-width="120px">
        <el-form-item label="Sprint Name" required>
          <el-input v-model="sprintForm.name" placeholder="Enter sprint name" clearable />
        </el-form-item>
        <el-form-item label="Start Date" required>
          <el-date-picker v-model="sprintForm.startDate" type="Date" placeholder="Select start date" />
        </el-form-item>
        <el-form-item label="End Date" required>
          <el-date-picker v-model="sprintForm.endDate" type="Date" placeholder="Select end date" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="AddSprint">Add Sprint</el-button>
        </el-form-item>
      </el-form>
    </div>

    <el-table :data="sprints" style="width: 100%">
      <el-table-column prop="name" label="Sprint Name" />
      <el-table-column prop="startDate" label="Start Date" />
      <el-table-column prop="endDate" label="End Date" />
      <el-table-column label="Actions">
        <template #default="scope">
          <el-button @click="editSprint(scope.row)">Edit</el-button>
          <el-button type="danger" @click="deleteSprint(scope.row.id)">Delete</el-button>
          <el-button @click="loadBurndownData(scope.row.id)">View Burndown</el-button>
        </template>
      </el-table-column>
    </el-table>

    <h2>Burndown Chart for the Active Sprint</h2>
    <div ref="chartContainer" class="chart-container" style="height: 400px;"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue';
import * as echarts from 'echarts';
import {
  listSprints, addSprint, delSprint, getBurndownData
} from "@/api/project/sprint"; // Modify according to your file structure

const sprintForm = ref({ name: '', startDate: '', endDate: '' });
const sprints = ref([]);
const chartContainer = ref(null);
let chart = null;

// Fetch the initial data for sprints
const fetchSprints = async () => {
  try {
    const response = await listSprints();
    sprints.value = response.data; // Assuming this gives you the array of sprints
  } catch (error) {
    console.error("Error fetching sprints:", error);
  }
};

// Load burndown data for a specific sprint
const loadBurndownData = async (sprintId) => {
  try {
    const response = await getBurndownData(sprintId);
    renderChart(response.data); // Response should include burndown data
  } catch (error) {
    console.error("Error fetching burndown data:", error);
  }
};

// Render the burndown chart
const renderChart = (burndownData) => {
  if (!chartContainer.value) return;

  if (chart) chart.dispose();

  chart = echarts.init(chartContainer.value);

  const option = {
    title: {
      text: 'Sprint Burndown Chart'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: burndownData.map(item => item.date), // Assuming the response includes an array of objects with `date`
    },
    yAxis: {
      type: 'value',
      name: 'Remaining Work'
    },
    series: [
      {
        name: 'Remaining Tasks',
        type: 'line',
        data: burndownData.map(item => item.remaining), // Assuming each item has a `remaining` property
        itemStyle: {
          color: '#409EFF'
        },
        label: {
          show: true,
          position: 'top'
        }
      }
    ]
  };

  chart.setOption(option);
};

// Adding a sprint
const AddSprint = async () => {
  try {
    const response = await addSprint(sprintForm.value);
    sprints.value.push(response.data); // Directly push the new sprint data to the list
    await fetchSprints(); // Refresh the sprints list
    sprintForm.value = { name: '', startDate: '', endDate: '' }; // Reset form
  } catch (error) {
    console.error("Failed to add sprint:", error);
  }
};

// Editing a sprint
const editSprint = (sprint) => {
  sprintForm.value = { ...sprint }; // Fill form with sprint data for editing
};

// Deleting a sprint
const deleteSprint = async (sprintId) => {
  try {
    await delSprint(sprintId);
    await fetchSprints(); // Refresh the list after deletion
  } catch (error) {
    console.error("Failed to delete sprint:", error);
  }
};

// Lifecycle hooks
onMounted(() => {
  fetchSprints();
});

onBeforeUnmount(() => {
  if (chart) {
    chart.dispose();
  }
});
</script>

<style scoped>
.iteration-management {
  padding: 20px;
}

.chart-container {
  margin-top: 20px;
}
</style>
