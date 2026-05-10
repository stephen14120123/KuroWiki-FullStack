<template>
  <div class="filter-console space-y-3 p-4 rounded-xl bg-gray-900/60 backdrop-blur-sm border border-gray-800/50">
    <!-- 属性筛选 -->
    <div class="filter-row">
      <span class="filter-label">属性</span>
      <div class="filter-tags">
        <button
          v-for="el in elementOptions"
          :key="el"
          class="filter-tag"
          :class="{ 'filter-tag--active': filters.element === el }"
          @click="toggle('element', el)"
        >
          {{ el }}
        </button>
      </div>
    </div>

    <!-- 武器类型筛选 -->
    <div class="filter-row">
      <span class="filter-label">武器</span>
      <div class="filter-tags">
        <button
          v-for="w in weaponOptions"
          :key="w"
          class="filter-tag"
          :class="{ 'filter-tag--active': filters.weaponType === w }"
          @click="toggle('weaponType', w)"
        >
          {{ w }}
        </button>
      </div>
    </div>

    <!-- 星级筛选 -->
    <div class="filter-row">
      <span class="filter-label">星级</span>
      <div class="filter-tags">
        <button
          v-for="r in rarityOptions"
          :key="r.value"
          class="filter-tag"
          :class="{ 'filter-tag--active': filters.rarity === r.value }"
          @click="toggle('rarity', r.value)"
        >
          {{ r.label }}
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, watch } from 'vue'

export interface FilterState {
  rarity: number | null
  element: string | null
  weaponType: string | null
}

const emit = defineEmits<{
  change: [filters: FilterState]
}>()

const filters = reactive<FilterState>({
  rarity: null,
  element: null,
  weaponType: null,
})

const rarityOptions = [
  { label: '5★', value: 5 },
  { label: '4★', value: 4 },
]

const elementOptions = ['热熔', '冷凝', '气动', '导电', '衍射', '湮灭']
const weaponOptions = ['迅刀', '长刃', '臂铠', '音感仪', '佩枪']

function toggle(key: keyof FilterState, val: any) {
  (filters as any)[key] = filters[key] === val ? null : val
}

watch(filters, () => {
  emit('change', { ...filters })
}, { deep: true })
</script>

<style scoped>
@reference "tailwindcss";

.filter-row {
  @apply flex items-center gap-3;
}
.filter-label {
  @apply text-xs text-gray-500 w-10 shrink-0 uppercase tracking-wider;
}
.filter-tags {
  @apply flex flex-wrap gap-2;
}
.filter-tag {
  @apply px-3 py-1.5 text-xs rounded-lg bg-gray-800/80 text-gray-400 border border-gray-700/50 transition-all duration-200 hover:text-white hover:border-gray-500 hover:bg-gray-700/60 cursor-pointer select-none;
}
.filter-tag--active {
  @apply text-white bg-indigo-500/25 border-indigo-400/60 shadow-sm shadow-indigo-500/20;
}
</style>
