<template>
  <div class="notice-container">
    <el-card>
      <div class="notice-list">
        
        
        <div v-for="item in allM" :key="item.noticeId" class="notice-item" @click="readddd(item)">
          <div class="notice-content">
            <div class="notice-status" :class="{ 'unread': item.userReadStatus === 0 }"></div>
            <div class="notice-info">
              <div class="notice-title">{{ item.noticeTitle }}</div>
              <div class="notice-detail" v-html="item.noticeContent"></div>
              <div class="notice-footer"><span class="notice-time">{{ item.createTime }}</span></div>
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

const allM = ref([])
const userStore = useUserStore()



const getMA = async () => {
    const r = await getUserNoticeList(userStore.id)
      allM.value = r.rows
  

}

const readddd = async (notice) => {
  if (notice.userReadStatus === 0) {
      const data = {
        noticeId: notice.noticeId,
        userId: userStore.id,
        isRead: 1,
      }

      const res = await updateNoticeStatus(data)
        notice.userReadStatus = 1
        getMA()

  }
}

onMounted(() => {
  getMA()
})
</script>

<style scoped>
@import url('note.scss');
</style>