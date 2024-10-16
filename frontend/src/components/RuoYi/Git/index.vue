<template>
  <div>
    <svg-icon icon-class="message" @click="goto" />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Message } from '@element-plus/icons-vue'

const router = useRouter()
const url = ref('/system/notice')

function goto() {
  router.push(url.value)
}
</script>
