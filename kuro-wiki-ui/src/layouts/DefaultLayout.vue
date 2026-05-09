<template>
  <div class="relative min-h-screen bg-gray-950 text-white overflow-hidden" @mousemove="onMouseMove">
    <!-- 背景光斑层 -->
    <div
      class="pointer-events-none fixed inset-0 z-0 transition-opacity duration-300"
      :style="spotStyle"
    />

    <!-- 顶部导航 -->
    <TopNav />

    <!-- 页面内容 + 过渡动画 -->
    <main class="relative z-10 pt-16">
      <router-view v-slot="{ Component }">
        <Transition name="fade-slide" mode="out-in">
          <component :is="Component" />
        </Transition>
      </router-view>
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import TopNav from '@/components/TopNav.vue'

const mouseX = ref(0)
const mouseY = ref(0)

function onMouseMove(e: MouseEvent) {
  mouseX.value = e.clientX
  mouseY.value = e.clientY
}

const spotStyle = computed(() => ({
  background: `radial-gradient(600px circle at ${mouseX.value}px ${mouseY.value}px, rgba(99, 102, 241, 0.12), transparent 60%)`,
}))
</script>

<style scoped>
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}
.fade-slide-enter-from {
  opacity: 0;
  transform: translateY(12px);
}
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-12px);
}
</style>
