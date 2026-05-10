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
    <template v-else-if="character">
      <!-- 上半部分：立绘 + 基础信息 -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
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
          <div class="absolute top-3 left-3 flex gap-0.5">
            <span v-for="n in character.rarity" :key="n" class="text-amber-400 text-lg">★</span>
          </div>
        </div>

        <!-- 右侧：基础信息 -->
        <div class="space-y-6">
          <el-descriptions title="基础信息" :column="1" border class="detail-desc">
            <el-descriptions-item label="姓名">{{ character.name }}</el-descriptions-item>
            <el-descriptions-item label="星级">
              <span v-for="n in character.rarity" :key="n" class="text-amber-400">★</span>
            </el-descriptions-item>
            <el-descriptions-item label="共鸣属性">
              <el-tag type="primary" effect="dark" size="small">{{ character.element }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="武器类型">{{ character.weaponType }}</el-descriptions-item>
          </el-descriptions>

          <el-descriptions v-if="hasStats" title="基础数值" :column="2" border class="detail-desc">
            <el-descriptions-item v-if="character.hp" label="生命值">{{ character.hp }}</el-descriptions-item>
            <el-descriptions-item v-if="character.atk" label="攻击力">{{ character.atk }}</el-descriptions-item>
            <el-descriptions-item v-if="character.def" label="防御力">{{ character.def }}</el-descriptions-item>
            <el-descriptions-item v-if="character.crit" label="暴击率">{{ character.crit }}%</el-descriptions-item>
            <el-descriptions-item v-if="character.energy" label="能量">{{ character.energy }}</el-descriptions-item>
          </el-descriptions>

          <!-- 能力雷达图 -->
          <div v-if="hasStats" class="rounded-xl bg-gray-800/40 border border-gray-700/40 p-2">
            <AbilityRadar
              :element="character.element"
              :hp="character.hp"
              :atk="character.atk"
              :def="character.def"
              :crit="character.crit"
              :energy="character.energy"
            />
          </div>

          <!-- 背景故事 -->
          <div v-if="character.backstory" class="space-y-2">
            <h3 class="text-sm font-semibold text-gray-400 uppercase tracking-wider">背景故事</h3>
            <p class="text-sm text-gray-300 leading-relaxed">{{ character.backstory }}</p>
          </div>
        </div>
      </div>

      <!-- 下半部分：Tab 切换区域 -->
      <div class="mt-8">
        <el-tabs v-model="activeTab" class="detail-tabs">
          <!-- 技能 Tab -->
          <el-tab-pane label="共鸣技能" name="skills">
            <div v-if="parsedSkills.length" class="grid grid-cols-1 sm:grid-cols-2 gap-3 pt-4">
              <div
                v-for="(skill, idx) in parsedSkills"
                :key="idx"
                class="p-4 rounded-lg bg-gray-800/60 border border-gray-700/40 hover:border-indigo-500/40 transition-colors"
              >
                <p class="text-sm font-semibold text-indigo-300">{{ skill.name }}</p>
                <p class="text-xs text-gray-400 mt-2 leading-relaxed">{{ skill.desc }}</p>
              </div>
            </div>
            <p v-else class="text-sm text-gray-500 pt-4">暂无技能数据</p>
          </el-tab-pane>

          <!-- 推荐武器 Tab -->
          <el-tab-pane label="推荐武器" name="weapons">
            <div v-if="recommendedWeapons.length" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 pt-4">
              <router-link
                v-for="w in recommendedWeapons"
                :key="w.id"
                :to="`/weapon/${w.id}`"
                class="p-4 rounded-lg bg-gray-800/60 border border-gray-700/40 hover:border-indigo-500/40 transition-all hover:-translate-y-0.5"
              >
                <div class="flex items-center gap-3">
                  <img v-if="w.imageUrl" :src="w.imageUrl" class="w-12 h-12 rounded object-cover" />
                  <div class="flex-1 min-w-0">
                    <p class="text-sm font-semibold text-white truncate">{{ w.name }}</p>
                    <p class="text-xs text-amber-400 mt-0.5">{{ '★'.repeat(w.rarity) }}</p>
                    <p v-if="w.subStatType" class="text-xs text-gray-400 mt-0.5">{{ w.subStatType }} {{ w.subStatValue }}</p>
                  </div>
                </div>
                <p v-if="w.skillDesc" class="text-xs text-gray-500 mt-2 line-clamp-2">{{ w.skillDesc }}</p>
              </router-link>
            </div>
            <p v-else class="text-sm text-gray-500 pt-4">暂无同类型武器数据</p>
          </el-tab-pane>

          <!-- 推荐声骸 Tab -->
          <el-tab-pane label="声骸搭配" name="echoes">
            <div v-if="recommendedEchoes.length" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3 pt-4">
              <router-link
                v-for="e in recommendedEchoes"
                :key="e.id"
                :to="`/echo/${e.id}`"
                class="p-4 rounded-lg bg-gray-800/60 border border-gray-700/40 hover:border-indigo-500/40 transition-all hover:-translate-y-0.5"
              >
                <div class="flex items-center gap-3">
                  <img v-if="e.imageUrl" :src="e.imageUrl" class="w-10 h-10 rounded object-cover" />
                  <div class="flex-1 min-w-0">
                    <p class="text-sm font-semibold text-white truncate">{{ e.name }}</p>
                    <div class="flex items-center gap-2 mt-0.5">
                      <span class="text-xs px-1.5 py-0.5 rounded bg-indigo-500/20 text-indigo-300">Cost {{ e.cost }}</span>
                      <span v-if="e.sonataEffect" class="text-xs text-gray-400">{{ e.sonataEffect }}</span>
                    </div>
                  </div>
                </div>
                <p v-if="e.skillDesc" class="text-xs text-gray-500 mt-2 line-clamp-2">{{ e.skillDesc }}</p>
              </router-link>
            </div>
            <p v-else class="text-sm text-gray-500 pt-4">暂无声骸推荐数据</p>
          </el-tab-pane>

          <!-- 养成建议 Tab -->
          <el-tab-pane label="养成建议" name="build">
            <div v-if="character.buildGuide" class="pt-4">
              <p class="text-sm text-gray-300 leading-relaxed whitespace-pre-line">{{ character.buildGuide }}</p>
            </div>
            <p v-else class="text-sm text-gray-500 pt-4">暂无养成建议</p>
          </el-tab-pane>
        </el-tabs>
      </div>
    </template>

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
import { fetchWeapons } from '@/api/weapons'
import { fetchEchoes } from '@/api/echoes'
import AbilityRadar from '@/components/AbilityRadar.vue'
import type { CharacterItem } from '@/api/characters'
import type { WeaponItem } from '@/api/weapons'
import type { EchoItem } from '@/api/echoes'

const route = useRoute()
const router = useRouter()

const loading = ref(true)
const character = ref<CharacterItem | null>(null)
const recommendedWeapons = ref<WeaponItem[]>([])
const recommendedEchoes = ref<EchoItem[]>([])
const activeTab = ref('skills')

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

    // 加载关联数据
    if (character.value) {
      loadRecommendations()
    }
  } catch (err: any) {
    ElMessage.error(err?.message || '获取角色详情失败')
  } finally {
    loading.value = false
  }
})

async function loadRecommendations() {
  // 并行加载武器和声骸
  const [weaponRes, echoRes] = await Promise.allSettled([
    fetchWeapons(),
    fetchEchoes(),
  ])

  // 根据角色武器类型筛选推荐武器
  if (weaponRes.status === 'fulfilled') {
    const allWeapons: WeaponItem[] = (weaponRes.value as any).data ?? []
    recommendedWeapons.value = allWeapons
      .filter(w => w.weaponType === character.value?.weaponType)
      .slice(0, 6)
  }

  // 展示所有声骸（后续可根据角色属性做更精准推荐）
  if (echoRes.status === 'fulfilled') {
    const allEchoes: EchoItem[] = (echoRes.value as any).data ?? []
    recommendedEchoes.value = allEchoes.slice(0, 6)
  }
}
</script>

<style scoped>
.detail-desc :deep(.el-descriptions__title) {
  color: #e5e7eb;
}
.detail-desc :deep(.el-descriptions__label) {
  color: #9ca3af;
  background-color: rgba(31, 41, 55, 0.6);
}
.detail-desc :deep(.el-descriptions__content) {
  color: #f3f4f6;
  background-color: rgba(17, 24, 39, 0.6);
}

.detail-tabs :deep(.el-tabs__item) {
  color: #9ca3af;
}
.detail-tabs :deep(.el-tabs__item.is-active) {
  color: #818cf8;
}
.detail-tabs :deep(.el-tabs__active-bar) {
  background-color: #6366f1;
}
.detail-tabs :deep(.el-tabs__nav-wrap::after) {
  background-color: rgba(75, 85, 99, 0.3);
}
</style>
