<template>
  <div class="p-6 space-y-6">
    <h1 class="text-2xl font-bold">武器</h1>

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

    <!-- 武器卡片列表 -->
    <div v-else-if="weapons.length" class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-4">
      <GachaCard
        v-for="item in weapons"
        :key="item.id"
        :item="{ name: item.name, image: item.imageUrl, rarity: item.rarity, type: item.weaponType }"
        :detail-link="`/weapon/${item.id}`"
      />
    </div>

    <!-- 空状态 -->
    <div v-else class="flex flex-col items-center justify-center py-20 text-gray-500">
      <p class="text-sm">暂无武器数据</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import GachaCard from '@/components/GachaCard.vue'
import { fetchWeapons } from '@/api/weapons'
import type { WeaponItem } from '@/api/weapons'

const loading = ref(true)
const weapons = ref<WeaponItem[]>([])

onMounted(async () => {
  try {
    const res = await fetchWeapons() as any
    weapons.value = res.data ?? []
  } catch (err: any) {
    ElMessage.error(err?.message || '获取武器列表失败')
  } finally {
    loading.value = false
  }
})
</script>
