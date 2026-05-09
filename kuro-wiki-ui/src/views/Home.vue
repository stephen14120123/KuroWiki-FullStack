<template>
  <div class="p-6 space-y-10">
    <!-- 欢迎横幅 -->
    <div class="text-center py-8">
      <h1 class="text-3xl font-bold text-white">Kuro Wiki</h1>
      <p class="mt-2 text-sm text-gray-400">鸣潮图鉴 · 角色 · 武器 · 声骸</p>
    </div>

    <!-- 角色推荐 -->
    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <h2 class="text-lg font-semibold text-white">最新角色</h2>
        <router-link to="/characters" class="text-xs text-indigo-400 hover:text-indigo-300">查看全部 →</router-link>
      </div>
      <div v-if="loadingChars" class="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <el-skeleton v-for="n in 4" :key="n" animated>
          <template #template>
            <el-skeleton-item variant="image" class="w-full aspect-[3/4] rounded-xl" />
          </template>
        </el-skeleton>
      </div>
      <div v-else class="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <GachaCard
          v-for="item in characters"
          :key="item.id"
          :item="{ name: item.name, image: item.imageUrl, rarity: item.rarity, type: `${item.element} · ${item.weaponType}` }"
          :detail-link="`/character/${item.id}`"
        />
      </div>
    </section>

    <!-- 武器推荐 -->
    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <h2 class="text-lg font-semibold text-white">最新武器</h2>
        <router-link to="/weapons" class="text-xs text-indigo-400 hover:text-indigo-300">查看全部 →</router-link>
      </div>
      <div v-if="loadingWeapons" class="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <el-skeleton v-for="n in 4" :key="n" animated>
          <template #template>
            <el-skeleton-item variant="image" class="w-full aspect-[3/4] rounded-xl" />
          </template>
        </el-skeleton>
      </div>
      <div v-else class="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <GachaCard
          v-for="item in weapons"
          :key="item.id"
          :item="{ name: item.name, image: item.imageUrl, rarity: item.rarity, type: item.weaponType }"
          :detail-link="`/weapon/${item.id}`"
        />
      </div>
    </section>

    <!-- 声骸推荐 -->
    <section class="space-y-4">
      <div class="flex items-center justify-between">
        <h2 class="text-lg font-semibold text-white">最新声骸</h2>
        <router-link to="/echoes" class="text-xs text-indigo-400 hover:text-indigo-300">查看全部 →</router-link>
      </div>
      <div v-if="loadingEchoes" class="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <el-skeleton v-for="n in 4" :key="n" animated>
          <template #template>
            <el-skeleton-item variant="image" class="w-full aspect-[3/4] rounded-xl" />
          </template>
        </el-skeleton>
      </div>
      <div v-else class="grid grid-cols-2 sm:grid-cols-4 gap-4">
        <GachaCard
          v-for="item in echoes"
          :key="item.id"
          :item="{ name: item.name, image: item.imageUrl, rarity: item.cost, type: item.sonataEffect || '通用' }"
          :detail-link="`/echo/${item.id}`"
        />
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import GachaCard from '@/components/GachaCard.vue'
import { fetchCharacters } from '@/api/characters'
import { fetchWeapons } from '@/api/weapons'
import { fetchEchoes } from '@/api/echoes'
import type { CharacterItem } from '@/api/characters'
import type { WeaponItem } from '@/api/weapons'
import type { EchoItem } from '@/api/echoes'

const loadingChars = ref(true)
const loadingWeapons = ref(true)
const loadingEchoes = ref(true)

const characters = ref<CharacterItem[]>([])
const weapons = ref<WeaponItem[]>([])
const echoes = ref<EchoItem[]>([])

onMounted(async () => {
  // 并行请求三个接口
  const [charRes, weaponRes, echoRes] = await Promise.allSettled([
    fetchCharacters(),
    fetchWeapons(),
    fetchEchoes(),
  ])

  if (charRes.status === 'fulfilled') {
    const data = (charRes.value as any).data ?? []
    characters.value = data.slice(0, 4)
  }
  loadingChars.value = false

  if (weaponRes.status === 'fulfilled') {
    const data = (weaponRes.value as any).data ?? []
    weapons.value = data.slice(0, 4)
  }
  loadingWeapons.value = false

  if (echoRes.status === 'fulfilled') {
    const data = (echoRes.value as any).data ?? []
    echoes.value = data.slice(0, 4)
  }
  loadingEchoes.value = false
})
</script>
