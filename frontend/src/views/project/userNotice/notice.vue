<template>
  <div class="notice-container">
    <el-card>
      <div class="notice-list">
        <div v-for="item in noticeList" :key="item.noticeId" class="notice-item" @click="handleRead(item)">
          <div class="notice-content">
            <div class="notice-status" :class="{ 'unread': item.userReadStatus === 0 }"></div>
            <div class="notice-info">
              <div class="notice-title">{{ item.noticeTitle }}</div>
              <div class="notice-detail" v-html="item.noticeContent"></div>
              <div class="notice-footer">
                <span class="notice-time">{{ item.createTime }}</span>
                <span class="notice-type">{{ item.noticeType === '1' ? '安全通知' : '功能通知' }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getUserNoticeList, updateNoticeStatus } from '@/api/notice/noticeapi'
import useUserStore from '@/store/modules/user'

const noticeList = ref([])
const userStore = useUserStore()

const getNoticeList = async () => {
  try {
    const res = await getUserNoticeList(userStore.id)
    if (res.code === 200) {
      noticeList.value = res.rows
    }
  } catch (error) {
    console.error('获取通知失败:', error)
  }
}

const handleRead = async (notice) => {
  if (notice.userReadStatus === 0) {
    try {
      const data = {
        noticeId: notice.noticeId,
        userId: userStore.id,
        isRead: 1,
        readTime: new Date().toISOString(),
        createBy: userStore.name,
        updateBy: userStore.name
      }

      const res = await updateNoticeStatus(data)
      if (res.code === 200) {
        notice.userReadStatus = 1
        getNoticeList()
      }
    } catch (error) {
      console.error('更新通知状态失败:', error)
    }
  }
}

onMounted(() => {
  getNoticeList()
})
</script>

<style scoped>
.notice-container {
  padding: 20px;
}
.notice-list {
  max-height: 600px;
  overflow-y: auto;
}
.notice-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.notice-content {
  display: flex;
  align-items: flex-start;
}
.notice-status {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  margin-right: 10px;
  margin-top: 8px;
}
.notice-status.unread {
  background-color: #f56c6c;
}
.notice-info {
  flex: 1;
}
.notice-title {
  font-weight: bold;
  margin-bottom: 8px;
}
.notice-detail {
  color: #666;
  margin-bottom: 8px;
}
.notice-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #999;
  font-size: 12px;
}
.notice-type {
  background-color: #f0f2f5;
  padding: 2px 8px;
  border-radius: 4px;
}
</style>