<template>
   <div class="app-container">
      <div>
         <div class="bg-body-secondary" style="height: 100px"></div>
         <div class="d-flex">
            <userAvatar class="rounded-circle border border-white border-3 shadow ms-4" style="top: -30px" />
            <div class="ms-4 mt-2">
               <div class="fs-2 fw-bold">{{ state.user.userName }}</div>
               <div class="fs-6 text-body-secondary" v-if="state.user.dept">{{ state.user.dept.deptName }} / {{ state.postGroup }}</div>
            </div>
            <resetPwd />
         </div>
      </div>
      <el-row :gutter="20">
         <el-col :span="8">
            <el-card>
               <template #header>Information</template>
               <el-row>
                  <el-col :span="8"><svg-icon icon-class="user" class="me-1" /> username</el-col>
                  <el-col :span="16">{{ state.user.userName }}</el-col>
               </el-row>
               <el-row class="mt-2">
                  <el-col :span="8"><svg-icon icon-class="phone" class="me-1" /> phone number</el-col>
                  <el-col :span="16">{{ state.user.phonenumber }}</el-col>
               </el-row>
               <el-row class="mt-2">
                  <el-col :span="8"><svg-icon icon-class="email" class="me-1" /> email</el-col>
                  <el-col :span="16">{{ state.user.email }}</el-col>
               </el-row>
               <el-row class="mt-2">
                  <el-col :span="8"><svg-icon icon-class="peoples" class="me-1" /> role</el-col>
                  <el-col :span="16">{{ state.roleGroup }}</el-col>
               </el-row>
               <hr>
               <el-row>
                  <el-col :span="8"><svg-icon icon-class="date" class="me-1" /> create-time</el-col>
                  <el-col :span="16">{{ state.user.createTime }}</el-col>
               </el-row>
               <el-row class="mt-2">
                  <el-col :span="8"><svg-icon icon-class="time" class="me-1" /> last login time</el-col>
                  <el-col :span="16">{{ state.user.loginDate }}</el-col>
               </el-row>
            </el-card>
         </el-col>
         <el-col :span="16">
            <el-card></el-card>
         </el-col>
      </el-row>
      <!-- <el-row :gutter="20">
         <el-col :span="18" :xs="24">
            <el-card class="box-card">
               <template v-slot:header>
                 <div class="clearfix">
                   <span>information</span>
                 </div>
               </template>
               <div>
                  <div class="text-center">
                     <userAvatar />
                  </div>
                  <ul class="list-group list-group-striped">
                     <li class="list-group-item">
                        <svg-icon icon-class="user" /> username
                        <div class="pull-right">{{ state.user.userName }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="phone"/> phone number
                        <div class="pull-right">{{ state.user.phonenumber }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="email"/> email
                        <div class="pull-right">{{ state.user.email }}</div>
                     </li>
                      <li class="list-group-item">
                        <svg-icon icon-class="time"/> last login time
                        <div class="pull-right">{{ state.user.loginDate }}</div>
                      </li>



                     <li class="list-group-item">
                        <svg-icon icon-class="tree"/> department
                        <div class="pull-right" v-if="state.user.dept">{{ state.user.dept.deptName }} / {{ state.postGroup }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="peoples"/> role
                        <div class="pull-right">{{ state.roleGroup }}</div>
                     </li>
                     <li class="list-group-item">
                        <svg-icon icon-class="date" /> create-time
                        <div class="pull-right">{{ state.user.createTime }}</div>
                     </li>
                  </ul>
               </div>
            </el-card>
         </el-col>
        <el-divider />
        <br>
        <br>  <br>

         <el-col :span="18" :xs="24">
            <el-card>
               <template v-slot:header>
                 <div class="clearfix">
                   <span>basic information</span>
                 </div>
               </template>
               <el-tabs v-model="activeTab">
                  <el-tab-pane label="information" name="userinfo">
                     <userInfo :user="state.user" />
                  </el-tab-pane>
                  <el-tab-pane label="reset password" name="resetPwd">
                     <resetPwd />
                  </el-tab-pane>
               </el-tabs>
            </el-card>
         </el-col>
      </el-row> -->
   </div>
</template>

<script setup name="Profile">
import userAvatar from "./userAvatar";
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import { getUserProfile } from "@/api/system/user";

import { EditPen } from '@element-plus/icons-vue'

const activeTab = ref("userinfo");
const state = reactive({
  user: {},
  roleGroup: {},
  postGroup: {}
});

function getUser() {
  getUserProfile().then(response => {
    state.user = response.data;
    console.log(response);
    state.roleGroup = response.roleGroup;
    state.postGroup = response.postGroup;
  });
};

getUser();
</script>
