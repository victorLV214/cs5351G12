<template>
  <div>
    <el-badge :value="noticeCount" :max="99" class="notice-badge">
      <svg-icon icon-class="message" @click="goto" />
    </el-badge>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Message } from '@element-plus/icons-vue'

const router = useRouter()
const url = ref('/system/notice')
const noticeCount = ref(0)

function goto() {
  router.push(url.value)
}

// 模拟获取通知数量的函数
async function fetchNoticeCount() {
  // 这里应该是从API获取实际的通知数量
  // 现在我们用一个模拟的数字
  noticeCount.value = 5
}

onMounted(() => {
  fetchNoticeCount()
})
</script>

<style scoped>
.notice-badge {
  margin-right: 2px;

}

.notice-badge :deep(.el-badge__content) {
  background-color: #f56c6c;
  top:10px;
}
</style>