<template>
  <div class="notice-container">
    <el-card>
      <div class="notice-header">
        <el-input v-model="searchQuery" placeholder="Search Notifications..."
                  class="search-bar" style="margin-left: 40px; width:240px"/>
        <el-button @click="markAllAsRead" class="mark-all-read" style="margin-right: 8px">
          <el-icon><tickets/></el-icon>&nbsp; Mark All as Read
        </el-button>
      </div>
      <div class="notice-list">
        <div v-for="item in filteredNotices" :key="item.noticeId" class="notice-item">
          <div class="notice-content">
            <div class="notice-status" :class="{ 'unread': item.userReadStatus === 0 }"></div>
            <div class="notice-info">
              <div class="notice-title">{{ item.noticeTitle }}</div>
              <div class="notice-detail" v-html="item.noticeContent"></div>
              <div class="notice-footer">
                <span class="notice-time">{{ formatTime(item.createTime) }}</span>
                <el-button @click.stop="toggleReadStatus(item)" class="read-toggle" :class="{ 'read': item.userReadStatus === 1, 'unread': item.userReadStatus === 0 }">
                  {{ item.userReadStatus === 0 ? 'Read' : 'Mark as Unread' }}
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <el-pagination
          v-if="allM.length > itemsPerPage"
          @current-change="handlePageChange"
          :current-page="currentPage"
          :page-size="itemsPerPage"
          layout="prev, pager, next"
          :total="allM.length">
      </el-pagination>
    </el-card>

  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getUserNoticeList, updateNoticeStatus } from '@/api/notice/noticeapi'
import useUserStore from '@/store/modules/user'
import {MagicStick, Notebook, Reading, ReadingLamp, Ticket} from "@element-plus/icons-vue";

const allM = ref([])
const userStore = useUserStore()
const searchQuery = ref('')
const currentPage = ref(1)
const itemsPerPage = ref(10)
const dialogVisible = ref(false)
const currentNotice = ref(null)

const getMA = async () => {
  try {
    const r = await getUserNoticeList(userStore.id)
    allM.value = r.rows
  } catch (error) {
    console.error('Failed to fetch notices:', error)
  }
}

const toggleReadStatus = async (notice) => {
  try {
    const newStatus = notice.userReadStatus === 0 ? 1 : 0
    const data = {
      noticeId: notice.noticeId,
      userId: userStore.id,
      isRead: newStatus,
    }
    await updateNoticeStatus(data)
    notice.userReadStatus = newStatus
  } catch (error) {
    console.error('Failed to update notice status:', error)
  }
}

const markAllAsRead = async () => {
  try {
    const unreadNotices = allM.value.filter(notice => notice.userReadStatus === 0)
    const updatePromises = unreadNotices.map(notice => toggleReadStatus(notice))
    await Promise.all(updatePromises)
  } catch (error) {
    console.error('Failed to mark all as read:', error)
  }
}

const filteredNotices = computed(() => {
  return allM.value.filter(notice =>
      notice.noticeTitle.includes(searchQuery.value) ||
      notice.noticeContent.includes(searchQuery.value)
  ).slice((currentPage.value - 1) * itemsPerPage.value, currentPage.value * itemsPerPage.value)
})

const handlePageChange = (page) => {
  currentPage.value = page
}

const formatTime = (time) => {
  const date = new Date(time)
  return date.toLocaleString()
}

const showDetails = (notice) => {
  currentNotice.value = notice
  dialogVisible.value = true
}

onMounted(() => {
  getMA()
})
</script>

<style scoped>
@import url('note.scss');

.notice-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.search-bar {
  width: 60%;
}

.mark-all-read {
  background-color: #409EFF;
  color: white;
}

.notice-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #ebeef5;
  width: 100%;
}

.notice-content {
  display: flex;
  flex: 1; /* 让.notice-content占满父级元素 */
  align-items: center;
}

.notice-status {
  flex-shrink: 0; /* 防止.notice-status缩小 */
  width: 10px; /* 固定宽度 */
  height: 10px; /* 固定高度 */
  border-radius: 50%; /* 使其成为圆形 */
  background-color: transparent; /* 已读状态下的透明背景 */
}

.notice-status.unread {
  width: 10px; /* 调整宽度 */
  height: 10px; /* 调整高度 */
  border-radius: 50%; /* 使其成为圆形 */
  background-color: #f56c6c; /* 红色 */
}

.notice-info {
  flex: 1; /* 让.notice-info占满剩余空间 */
  margin-left: 10px;
}

.notice-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.read-toggle {
  margin-left: 10px;
}

.read-toggle {
  margin-left: 10px;
}

.read-toggle.read {
  //background-color: #edf5fe;
  //color: white;
}

.read-toggle.unread {
  background-color: #edf5fe;
  //color: white;
}
</style>
