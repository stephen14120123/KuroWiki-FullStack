<template>
  <div class="relative min-h-[calc(100vh-4rem)] p-6" @mousemove="onMouseMove">
    <!-- 鼠标跟随光斑 -->
    <div class="pointer-events-none fixed inset-0 z-0 transition-opacity duration-300" :style="spotStyle" />

    <!-- 返回按钮 -->
    <router-link to="/characters" class="relative z-10 inline-flex items-center gap-1 text-sm text-indigo-400 hover:text-indigo-300 transition-colors">
      ← 返回角色列表
    </router-link>

    <!-- 加载骨架屏 -->
    <div v-if="loading" class="relative z-10 mt-6 flex flex-col lg:flex-row gap-8">
      <div class="w-full lg:w-80 shrink-0">
        <el-skeleton animated class="skeleton-glow">
          <template #template>
            <el-skeleton-item variant="image" class="w-full aspect-[3/4] rounded-2xl" />
          </template>
        </el-skeleton>
      </div>
      <div class="flex-1 space-y-4">
        <el-skeleton animated :rows="8" class="skeleton-glow" />
      </div>
    </div>

    <!-- 主内容 -->
    <div v-else-if="detail" class="relative z-10 mt-6 flex flex-col lg:flex-row gap-8">
      <!-- 左侧：角色立绘 -->
      <div class="w-full lg:w-80 shrink-0">
        <div class="character-portrait sticky top-20 rounded-2xl overflow-hidden bg-gray-800/50 border border-gray-700/50">
          <img
            v-if="detail.image"
            :src="detail.image"
            :alt="detail.name"
            class="w-full aspect-[3/4] object-cover"
          />
          <div v-else class="w-full aspect-[3/4] flex items-center justify-center text-gray-500">
            No Image
          </div>
          <!-- 角色名称浮层 -->
          <div class="absolute bottom-0 left-0 right-0 p-4 bg-gradient-to-t from-gray-950/90 to-transparent">
            <h1 class="text-xl font-bold">{{ detail.name }}</h1>
            <div class="mt-1 flex items-center gap-2">
              <span class="text-amber-400 text-sm">
                <span v-for="n in detail.rarity" :key="n">★</span>
              </span>
              <span class="text-xs text-gray-400">{{ detail.element }} · {{ detail.weaponType }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：信息面板 -->
      <div class="flex-1 min-w-0">
        <el-tabs class="detail-tabs" model-value="background">
          <!-- 背景故事 -->
          <el-tab-pane label="背景故事" name="background">
            <div class="prose-content">
              <p v-if="detail.background" class="text-gray-300 leading-relaxed whitespace-pre-line">
                {{ detail.background }}
              </p>
              <p v-else class="text-gray-500 text-sm">暂无背景故事</p>
            </div>
          </el-tab-pane>

          <!-- 技能信息 -->
          <el-tab-pane label="技能" name="skills">
            <div v-if="detail.skills?.length" class="space-y-4">
              <div
                v-for="skill in detail.skills"
                :key="skill.name"
                class="p-4 rounded-xl bg-gray-800/40 border border-gray-700/40"
              >
                <div class="flex items-center gap-2 mb-2">
                  <span class="text-sm font-semibold text-white">{{ skill.name }}</span>
                  <span class="text-xs px-2 py-0.5 rounded bg-indigo-500/20 text-indigo-300">{{ skill.type }}</span>
                </div>
                <p class="text-sm text-gray-400 leading-relaxed">{{ skill.description }}</p>
              </div>
            </div>
            <p v-else class="text-gray-500 text-sm">暂无技能数据</p>
          </el-tab-pane>

          <!-- 养成建议 -->
          <el-tab-pane label="养成建议" name="build">
            <div class="prose-content">
              <p v-if="detail.buildSuggestion" class="text-gray-300 leading-relaxed whitespace-pre-line">
                {{ detail.buildSuggestion }}
              </p>
              <p v-else class="text-gray-500 text-sm">暂无养成建议</p>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else class="relative z-10 mt-6 text-center py-20">
      <p class="text-gray-500">角色数据加载失败</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { fetchCharacterDetail } from '@/api/characters'
import type { CharacterDetail } from '@/api/characters'

const route = useRoute()
const loading = ref(true)
const detail = ref<CharacterDetail | null>(null)

// 鼠标光斑
const mouseX = ref(0)
const mouseY = ref(0)

function onMouseMove(e: MouseEvent) {
  mouseX.value = e.clientX
  mouseY.value = e.clientY
}

const spotStyle = computed(() => ({
  background: `radial-gradient(600px circle at ${mouseX.value}px ${mouseY.value}px, rgba(99, 102, 241, 0.1), transparent 60%)`,
}))

onMounted(async () => {
  try {
    const res = await fetchCharacterDetail(route.params.id as string) as any
    detail.value = res.data ?? null
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
@reference "tailwindcss";

/* 角色立绘呼吸动画 */
.character-portrait {
  animation: portrait-breathe 4s ease-in-out infinite;
}

@keyframes portrait-breathe {
  0%, 100% {
    transform: scale(1);
    box-shadow: 0 0 20px rgba(99, 102, 241, 0.1);
  }
  50% {
    transform: scale(1.01);
    box-shadow: 0 0 40px rgba(99, 102, 241, 0.2);
  }
}

/* Tabs 暗黑风格覆盖 */
.detail-tabs :deep(.el-tabs__header) {
  @apply border-b border-gray-700/50;
}
.detail-tabs :deep(.el-tabs__item) {
  @apply text-gray-400 text-sm;
}
.detail-tabs :deep(.el-tabs__item.is-active) {
  @apply text-indigo-400;
}
.detail-tabs :deep(.el-tabs__active-bar) {
  @apply bg-indigo-400;
}
.detail-tabs :deep(.el-tabs__nav-wrap::after) {
  @apply bg-transparent;
}
</style>
