<template>
  <div class="navbar">
    <div class="d-flex align-items-center">
      <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
      <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!settingsStore.topNav" />
      <top-nav id="topmenu-container" class="topmenu-container" v-if="settingsStore.topNav" />
    </div>

    <div class="right-menu d-flex align-items-center">
      <template v-if="appStore.device !== 'mobile'">
        <header-search id="header-search" class="right-menu-item" />
      </template>

      <el-tooltip content="notice" effect="dark" placement="bottom">
        <el-badge :value="unreadCount" class="right-menu-item hover-effect item">
          <el-icon @click="handleNoticeClick" class="mt-1" style="font-size: 25px"><Bell /></el-icon>
        </el-badge>
      </el-tooltip>

      <template v-if="appStore.device !== 'mobile'">
        <el-tooltip content="docs" effect="dark" placement="bottom">
          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
        </el-tooltip>

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <!-- <el-tooltip content="布局大小" effect="dark" placement="bottom">
          <size-select id="size-select" class="right-menu-item hover-effect" />
        </el-tooltip> -->
      </template>
      <div class="avatar-container">
        <el-dropdown @command="handleCommand" class="right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <img :src="userStore.avatar" class="user-avatar" />
            <el-icon><caret-bottom /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <router-link class="text-decoration-none" to="/user/profile">
                <el-dropdown-item><strong>Profile</strong></el-dropdown-item>
              </router-link>
              <router-link class="text-decoration-none" to="/system/user" v-if="userStore.roles && userStore.roles.includes('admin')">
                <el-dropdown-item><strong>User Manage</strong></el-dropdown-item>
              </router-link>
              <el-dropdown-item divided command="logout">
                <span style="color: #ed5565">
                  <strong>Logout</strong>
                </span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup>
import {useRouter} from 'vue-router'

const router = useRouter()
import {ElMessageBox} from 'element-plus'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import HeaderSearch from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import useAppStore from '@/store/modules/app'
import useUserStore from '@/store/modules/user'
import useSettingsStore from '@/store/modules/settings'
import {Bell} from '@element-plus/icons-vue'
import {ref, onMounted, onBeforeUnmount} from 'vue'
import {getUserNoticeList} from '@/api/notice/noticeapi'
import Detail from "@/views/project/project/detail.vue";

const unreadCount = ref(0)
const appStore = useAppStore()
const userStore = useUserStore()
const settingsStore = useSettingsStore()
let intervalId = null

function toggleSideBar() {
  appStore.toggleSideBar()
}


async function getUnreadCount() {
  try {
    const res = await getUserNoticeList(userStore.id)
    unreadCount.value = res.rows.filter(notice => notice.userReadStatus === 0).length
  } catch (error) {
    console.error('Error fetching unread count:', error)
  }
}

const handleNoticeClick = () => {
  router.push('/userNotice/notice')  // 跳转到通知页面
}

function handleCommand(command) {
  switch (command) {
    case "setLayout":
      setLayout()
      break
    case "logout":
      logout()
      break
    default:
      break
  }
}

function logout() {
  ElMessageBox.confirm("Log out and exit the system?", 'Warning', {
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel',
    type: 'warning'
  }).then(() => {
    userStore.logOut().then(() => {
      location.href = '/index'
    })
  }).catch(() => {
  })
}

const emits = defineEmits(['setLayout'])

function setLayout() {
  emits('setLayout')
}

// 在组件挂载时获取未读消息数量并设置轮询
onMounted(() => {
  getUnreadCount()
  intervalId = setInterval(getUnreadCount, 60000)
})

// 在组件卸载时清除定时器
onBeforeUnmount(() => {
  if (intervalId) {
    clearInterval(intervalId)
  }
})

</script>

<style lang='scss' scoped>
.navbar {
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  :deep(.el-badge__content.el-badge__content--danger) {
    top: 25px; // 调整这个值来改变红点的垂直位置
    right: 18px;
  }

  .breadcrumb-container {
    float: left;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    display: flex;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 40px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        i {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>