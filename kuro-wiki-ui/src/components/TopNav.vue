<template>
  <nav class="fixed top-0 left-0 right-0 z-50 h-16 flex items-center px-6 bg-gray-900/80 backdrop-blur-md border-b border-gray-800">
    <!-- Logo -->
    <router-link to="/" class="text-xl font-bold tracking-wide text-indigo-400 mr-10">
      Kuro Wiki
    </router-link>

    <!-- 菜单 -->
    <ul class="flex gap-1">
      <li v-for="item in menuItems" :key="item.path">
        <router-link
          :to="item.path"
          class="nav-link"
          active-class="nav-link--active"
        >
          {{ item.label }}
        </router-link>
      </li>
    </ul>

    <!-- 右侧登录/用户按钮 -->
    <div class="ml-auto">
      <router-link
        v-if="!isLoggedIn"
        to="/login"
        class="auth-btn"
      >
        登录
      </router-link>
      <button
        v-else
        class="auth-btn auth-btn--active"
        @click="handleLogout"
      >
        个人中心
      </button>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isLoggedIn = ref(false)

onMounted(() => {
  isLoggedIn.value = !!localStorage.getItem('token')
})

function handleLogout() {
  localStorage.removeItem('token')
  isLoggedIn.value = false
  router.push('/login')
}

const menuItems = [
  { path: '/', label: '首页' },
  { path: '/characters', label: '角色' },
  { path: '/weapons', label: '武器' },
  { path: '/echoes', label: '声骸' },
]
</script>

<style scoped>
@reference "tailwindcss";

.nav-link {
  @apply px-4 py-2 rounded-lg text-sm font-medium text-gray-300 transition-all duration-200 hover:text-white hover:bg-white/10;
}
.nav-link--active {
  @apply text-white bg-indigo-500/20 shadow-sm shadow-indigo-500/10;
}

.auth-btn {
  @apply px-4 py-1.5 rounded-lg text-sm font-medium text-indigo-300 border border-indigo-500/40 bg-transparent transition-all duration-300 hover:text-white hover:border-indigo-400 hover:shadow-md hover:shadow-indigo-500/20;
  animation: auth-breathe 3s ease-in-out infinite;
}

.auth-btn--active {
  @apply text-indigo-200 border-indigo-400/50;
}

@keyframes auth-breathe {
  0%, 100% {
    box-shadow: 0 0 8px rgba(99, 102, 241, 0.15);
  }
  50% {
    box-shadow: 0 0 18px rgba(99, 102, 241, 0.35);
  }
}
</style>
