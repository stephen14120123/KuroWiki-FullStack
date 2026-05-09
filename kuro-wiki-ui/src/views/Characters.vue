<template>
  <div class="p-6 space-y-6">
    <h1 class="text-2xl font-bold">角色</h1>

    <!-- 筛选控制台 -->
    <FilterConsole @change="onFilterChange" />

    <!-- 骨架屏 -->
    <div v-if="loading" class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-4">
      <div v-for="n in 12" :key="n" class="w-full">
        <el-skeleton animated class="skeleton-glow">
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

    <!-- 角色卡片列表 -->
    <div v-else-if="filteredList.length" class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-4">
      <GachaCard
        v-for="item in filteredList"
        :key="item.id"
        v-memo="[item.id, item.name, item.rarity, item.element, item.weaponType, item.imageUrl]"
        :item="{ name: item.name, image: item.imageUrl, rarity: item.rarity, type: `${item.element} · ${item.weaponType}` }"
        :detail-link="`/character/${item.id}`"
      />
    </div>

    <!-- 空状态 -->
    <div v-else class="flex flex-col items-center justify-center py-20 text-gray-500">
      <p class="text-sm">没有匹配的角色</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import GachaCard from '@/components/GachaCard.vue'
import FilterConsole from '@/components/FilterConsole.vue'
import type { FilterState } from '@/components/FilterConsole.vue'
import { fetchCharacters } from '@/api/characters'
import type { CharacterItem } from '@/api/characters'

const loading = ref(true)
const characters = ref<CharacterItem[]>([])
const currentFilters = ref<FilterState>({
  rarity: null,
  element: null,
  weaponType: null,
})

function onFilterChange(filters: FilterState) {
  currentFilters.value = filters
}

const filteredList = computed(() => {
  return characters.value.filter((c) => {
    if (currentFilters.value.rarity && c.rarity !== currentFilters.value.rarity) return false
    if (currentFilters.value.element && c.element !== currentFilters.value.element) return false
    if (currentFilters.value.weaponType && c.weaponType !== currentFilters.value.weaponType) return false
    return true
  })
})

onMounted(async () => {
  try {
    const res = await fetchCharacters() as any
    characters.value = res.data ?? []
  } catch (err: any) {
    ElMessage.error(err?.message || '获取角色列表失败，请稍后重试')
  } finally {
    loading.value = false
  }
})
</script>
