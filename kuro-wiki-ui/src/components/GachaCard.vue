<template>
  <div
    class="gacha-card group relative w-full rounded-xl overflow-hidden cursor-pointer select-none"
    @mouseenter="active = true"
    @mouseleave="onLeave"
    @mousemove="onMove"
    :style="cardStyle"
  >
    <!-- 高光吸附层 -->
    <div
      class="pointer-events-none absolute inset-0 z-20 opacity-0 group-hover:opacity-100 transition-opacity duration-300"
      :style="glareStyle"
    />

    <!-- 卡片图片 -->
    <div class="aspect-[3/4] bg-gray-800 overflow-hidden relative">
      <img
        v-if="item.image"
        :src="item.image"
        :alt="item.name"
        loading="lazy"
        class="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
      />
      <div v-else class="w-full h-full flex items-center justify-center text-gray-500 text-sm">
        No Image
      </div>

      <!-- 详情按钮 -->
      <router-link
        v-if="detailLink"
        :to="detailLink"
        class="absolute bottom-2 right-2 z-30 px-2 py-1 text-xs rounded-md bg-indigo-500/80 text-white opacity-0 group-hover:opacity-100 transition-opacity duration-200 hover:bg-indigo-400"
        @click.stop
      >
        详情
      </router-link>
    </div>

    <!-- 卡片信息 -->
    <div class="relative z-10 p-3 bg-gray-900/90 backdrop-blur-sm">
      <h3 class="text-sm font-semibold text-white truncate">{{ item.name }}</h3>
      <p v-if="item.rarity" class="mt-1 flex gap-0.5">
        <span
          v-for="n in item.rarity"
          :key="n"
          class="text-amber-400 text-xs"
        >★</span>
      </p>
      <p v-if="item.type" class="mt-1 text-xs text-gray-400">{{ item.type }}</p>
    </div>

    <!-- 边框光效 -->
    <div class="absolute inset-0 rounded-xl border border-white/10 group-hover:border-indigo-400/40 transition-colors duration-300 z-30 pointer-events-none" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

export interface GachaItem {
  name: string
  image?: string
  rarity?: number
  type?: string
}

defineProps<{
  item: GachaItem
  detailLink?: string
}>()

const active = ref(false)
const rotateX = ref(0)
const rotateY = ref(0)
const glareX = ref(50)
const glareY = ref(50)

function onMove(e: MouseEvent) {
  if (!active.value) return
  const el = e.currentTarget as HTMLElement
  const rect = el.getBoundingClientRect()
  const x = e.clientX - rect.left
  const y = e.clientY - rect.top
  const centerX = rect.width / 2
  const centerY = rect.height / 2

  rotateY.value = ((x - centerX) / centerX) * 15
  rotateX.value = ((centerY - y) / centerY) * 15

  glareX.value = (x / rect.width) * 100
  glareY.value = (y / rect.height) * 100
}

function onLeave() {
  active.value = false
  rotateX.value = 0
  rotateY.value = 0
}

const cardStyle = computed(() => ({
  transform: `perspective(600px) rotateX(${rotateX.value}deg) rotateY(${rotateY.value}deg)`,
  transition: active.value ? 'transform 0.1s ease-out' : 'transform 0.4s ease-out',
}))

const glareStyle = computed(() => ({
  background: `radial-gradient(circle at ${glareX.value}% ${glareY.value}%, rgba(255,255,255,0.25) 0%, transparent 60%)`,
}))
</script>

<style scoped>
.gacha-card {
  transform-style: preserve-3d;
  will-change: transform;
}
</style>
