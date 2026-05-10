<template>
  <div class="p-6 space-y-6">
    <h1 class="text-2xl font-bold">角色图鉴</h1>

    <!-- 筛选控制台 -->
    <FilterConsole @change="onFilterChange" />

    <!-- 骨架屏 -->
    <div v-if="loading" class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-4">
      <div v-for="n in 12" :key="n" class="w-full">
        <el-skeleton animated>
          <template #template>
            <el-skeleton-item variant="image" class="w-full aspect-[3/4] rounded-xl" />
            <div class="mt-2 space-y-2 px-1">
              <el-skeleton-item variant="text" class="w-3/4 h-4" />
              <el-skeleton-item variant="text" class="w-1/2 h-3" />
            </div>
          </template>
        </el-skeleton>
      </div>
    </div>

    <!-- 角色卡片网格 -->
    <div v-else-if="characters.length" class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-4">
      <router-link
        v-for="item in characters"
        :key="item.id"
        :to="`/character/${item.id}`"
        class="char-card group relative rounded-xl overflow-hidden bg-gray-800/50 border border-gray-700/40 transition-all duration-300 hover:border-indigo-400/50 hover:-translate-y-1 hover:shadow-lg hover:shadow-indigo-500/10"
      >
        <!-- 立绘图片 -->
        <div class="aspect-[3/4] overflow-hidden bg-gray-900">
          <img
            v-if="item.imageUrl"
            :src="item.imageUrl"
            :alt="item.name"
            loading="lazy"
            class="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
          />
          <div v-else class="w-full h-full flex items-center justify-center text-gray-600 text-sm">
            暂无立绘
          </div>
        </div>

        <!-- 星级角标 -->
        <div class="absolute top-2 left-2 flex gap-0.5">
          <span v-for="n in item.rarity" :key="n" class="text-amber-400 text-xs drop-shadow">★</span>
        </div>

        <!-- 属性标签 -->
        <div class="absolute top-2 right-2">
          <span class="px-1.5 py-0.5 text-[10px] rounded bg-black/50 backdrop-blur-sm" :class="elementColor(item.element)">
            {{ item.element }}
          </span>
        </div>

        <!-- 底部信息 -->
        <div class="p-3 bg-gray-900/90 backdrop-blur-sm">
          <h3 class="text-sm font-semibold text-white truncate">{{ item.name }}</h3>
          <p class="mt-0.5 text-xs text-gray-500">{{ item.weaponType }}</p>
        </div>
      </router-link>
    </div>

    <!-- 空状态 -->
    <div v-else class="flex flex-col items-center justify-center py-20 text-gray-500">
      <p class="text-sm">没有匹配的角色</p>
    </div>

    <!-- 分页 -->
    <div v-if="total > pageSize" class="flex justify-center pt-4">
      <el-pagination
        v-model:current-page="pageNum"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadData"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import FilterConsole from '@/components/FilterConsole.vue'
import type { FilterState } from '@/components/FilterConsole.vue'
import { searchCharacters } from '@/api/characters'
import type { CharacterItem } from '@/api/characters'

const loading = ref(true)
const characters = ref<CharacterItem[]>([])
const pageNum = ref(1)
const pageSize = 18
const total = ref(0)

const currentFilters = ref<FilterState>({
  rarity: null,
  element: null,
  weaponType: null,
})

function onFilterChange(filters: FilterState) {
  currentFilters.value = filters
  pageNum.value = 1
  loadData()
}

async function loadData() {
  loading.value = true
  try {
    const params: any = {
      pageNum: pageNum.value,
      pageSize,
    }
    if (currentFilters.value.element) params.element = currentFilters.value.element
    if (currentFilters.value.weaponType) params.weaponType = currentFilters.value.weaponType
    if (currentFilters.value.rarity) params.rarity = currentFilters.value.rarity

    const res = await searchCharacters(params) as any
    characters.value = res.data?.list ?? []
    total.value = res.data?.total ?? 0
  } catch (err: any) {
    ElMessage.error(err?.message || '获取角色列表失败')
  } finally {
    loading.value = false
  }
}

function elementColor(element: string) {
  const map: Record<string, string> = {
    '热熔': 'text-orange-300',
    '冷凝': 'text-cyan-300',
    '气动': 'text-green-300',
    '导电': 'text-purple-300',
    '衍射': 'text-yellow-300',
    '湮灭': 'text-red-300',
  }
  return map[element] || 'text-gray-300'
}

onMounted(() => loadData())
</script>

<style scoped>
.char-card {
  will-change: transform, box-shadow;
}
</style>
