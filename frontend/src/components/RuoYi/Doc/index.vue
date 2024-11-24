<template>
  <div>
    <svg-icon icon-class="question" style="font-size: 20px" @click="goto" />
  </div>
</template>




<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Message } from '@element-plus/icons-vue'

const router = useRouter()
const url = ref('/doc/index');

function goto() {
  router.push(url.value)
}
</script>