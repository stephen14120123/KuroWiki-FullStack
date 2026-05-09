<template>
  <div class="p-6 max-w-5xl mx-auto space-y-6">
    <!-- 顶部导航 -->
    <div class="flex items-center gap-3">
      <el-button :icon="ArrowLeft" @click="router.back()" round>返回</el-button>
      <h1 class="text-xl font-bold text-white">角色详情</h1>
    </div>

    <!-- 加载骨架 -->
    <div v-if="loading" class="grid grid-cols-1 md:grid-cols-2 gap-8">
      <el-skeleton animated>
        <template #template>
          <el-skeleton-item variant="image" class="w-full aspect-[3/4] rounded-xl" />
        </template>
      </el-skeleton>
      <el-skeleton animated :rows="8" />
    </div>

    <!-- 主体内容 -->
    <div v-else-if="character" class="grid grid-cols-1 md:grid-cols-2 gap-8">
      <!-- 左侧：角色立绘 -->
      <div class="relative rounded-xl overflow-hidden bg-gray-800/50 border border-gray-700/50">
        <img
          v-if="character.imageUrl"
          :src="character.imageUrl"
          :alt="character.name"
          class="w-full h-full object-cover"
        />
        <div v-else class="w-full aspect-[3/4] flex items-center justify-center text-gray-500">
          暂无立绘
        </div>
        <!-- 星级角标 -->
        <div class="absolute top-3 left-3 flex gap-0.5">
          <span v-for="n in character.rarity" :key="n" class="text-amber-400 text-lg">★</span>
        </div>
      </div>

      <!-- 右侧：角色信息 -->
      <div class="space-y-6">
        <!-- 基础信息 -->
        <el-descriptions
          title="基础信息"
          :column="1"
          border
          class="character-desc"
        >
          <el-descriptions-item label="姓名">{{ character.name }}</el-descriptions-item>
          <el-descriptions-item label="星级">
            <span v-for="n in character.rarity" :key="n" class="text-amber-400">★</span>
          </el-descriptions-item>
          <el-descriptions-item label="共鸣属性">
            <el-tag type="primary" effect="dark" size="small">{{ character.element }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="武器类型">{{ character.weaponType }}</el-descriptions-item>
        </el-descriptions>

        <!-- 数值面板 -->
        <el-descriptions
          v-if="hasStats"
          title="基础数值"
          :column="2"
          border
          class="character-desc"
        >
          <el-descriptions-item v-if="character.hp" label="生命值">{{ character.hp }}</el-descriptions-item>
          <el-descriptions-item v-if="character.atk" label="攻击力">{{ character.atk }}</el-descriptions-item>
          <el-descriptions-item v-if="character.def" label="防御力">{{ character.def }}</el-descriptions-item>
          <el-descriptions-item v-if="character.crit" label="暴击率">{{ character.crit }}%</el-descriptions-item>
          <el-descriptions-item v-if="character.energy" label="能量">{{ character.energy }}</el-descriptions-item>
        </el-descriptions>

        <!-- 背景故事 -->
        <div v-if="character.backstory" class="space-y-2">
          <h3 class="text-sm font-semibold text-gray-400 uppercase tracking-wider">背景故事</h3>
          <p class="text-sm text-gray-300 leading-relaxed">{{ character.backstory }}</p>
        </div>

        <!-- 技能信息 -->
        <div v-if="parsedSkills.length" class="space-y-2">
          <h3 class="text-sm font-semibold text-gray-400 uppercase tracking-wider">技能</h3>
          <div class="space-y-2">
            <div
              v-for="(skill, idx) in parsedSkills"
              :key="idx"
              class="p-3 rounded-lg bg-gray-800/60 border border-gray-700/40"
            >
              <p class="text-sm font-medium text-indigo-300">{{ skill.name }}</p>
              <p class="text-xs text-gray-400 mt-1">{{ skill.desc }}</p>
            </div>
          </div>
        </div>

        <!-- 养成建议 -->
        <div v-if="character.buildGuide" class="space-y-2">
          <h3 class="text-sm font-semibold text-gray-400 uppercase tracking-wider">养成建议</h3>
          <p class="text-sm text-gray-300 leading-relaxed whitespace-pre-line">{{ character.buildGuide }}</p>
        </div>
      </div>
    </div>

    <!-- 错误状态 -->
    <div v-else class="flex flex-col items-center justify-center py-20 text-gray-500">
      <p>未找到该角色信息</p>
      <el-button class="mt-4" @click="router.back()">返回列表</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { fetchCharacterDetail } from '@/api/characters'
import type { CharacterItem } from '@/api/characters'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const character = ref<CharacterItem | null>(null)

const hasStats = computed(() => {
  if (!character.value) return false
  const c = character.value
  return c.hp || c.atk || c.def || c.crit || c.energy
})

const parsedSkills = computed(() => {
  if (!character.value?.skills) return []
  try {
    const skills = JSON.parse(character.value.skills)
    return Array.isArray(skills) ? skills : []
  } catch {
    return []
  }
})

onMounted(async () => {
  const id = route.params.id as string
  try {
    const res = await fetchCharacterDetail(id) as any
    character.value = res.data ?? null
  } catch (err: any) {
    ElMessage.error(err?.message || '获取角色详情失败')
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.character-desc :deep(.el-descriptions__title) {
  color: #e5e7eb;
}
.character-desc :deep(.el-descriptions__label) {
  color: #9ca3af;
  background-color: rgba(31, 41, 55, 0.6);
}
.character-desc :deep(.el-descriptions__content) {
  color: #f3f4f6;
  background-color: rgba(17, 24, 39, 0.6);
}
</style>
