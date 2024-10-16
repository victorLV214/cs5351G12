<template>
   <el-form ref="pwdRef" :model="user" :rules="rules" label-width="80px">
      <el-form-item label="old" prop="oldPassword">
         <el-input v-model="user.oldPassword" placeholder="oldPassword" type="password" show-password />
      </el-form-item>
      <el-form-item label="new" prop="newPassword">
         <el-input v-model="user.newPassword" placeholder="newPassword" type="password" show-password />
      </el-form-item>
      <el-form-item label="confirm" prop="confirmPassword">
         <el-input v-model="user.confirmPassword" placeholder="confirmPassword" type="password" show-password/>
      </el-form-item>
      <el-form-item>
      <el-button type="primary" @click="submit">save</el-button>
      <el-button type="danger" @click="close">exit</el-button>
      </el-form-item>
   </el-form>
</template>

<script setup>
import { updateUserPwd } from "@/api/system/user";

const { proxy } = getCurrentInstance();

const user = reactive({
  oldPassword: undefined,
  newPassword: undefined,
  confirmPassword: undefined
});

const equalToPassword = (rule, value, callback) => {
  if (user.newPassword !== value) {
    callback(new Error("Entered passwords differ!"));
  } else {
    callback();
  }
};
const rules = ref({
  oldPassword: [{ required: true, message: "Old password cannot be empty", trigger: "blur" }],
  newPassword: [{ required: true, message: "The new password cannot be empty", trigger: "blur" }, { min: 6, max: 20, message: "Length ranging from 6 to 20 characters", trigger: "blur" }],
  confirmPassword: [{ required: true, message: "Confirm password cannot be empty", trigger: "blur" }, { required: true, validator: equalToPassword, trigger: "blur" }]
});

/** 提交按钮 */
function submit() {
  proxy.$refs.pwdRef.validate(valid => {
    if (valid) {
      updateUserPwd(user.oldPassword, user.newPassword).then(response => {
        proxy.$modal.msgSuccess("Modified successfully");
      });
    }
  });
};
/** 关闭按钮 */
function close() {
  proxy.$tab.closePage();
};
</script>
