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

    <!-- 右侧用户状态 -->
    <div class="ml-auto flex items-center gap-3">
      <template v-if="userStore.isLoggedIn">
        <!-- 管理后台入口（仅管理员可见） -->
        <router-link v-if="userStore.isAdmin" to="/admin" class="auth-btn">后台</router-link>
        <!-- 用户信息 -->
        <div class="flex items-center gap-2">
          <div class="w-8 h-8 rounded-full bg-indigo-500/30 border border-indigo-400/50 flex items-center justify-center text-xs text-indigo-300 font-bold">
            {{ avatarText }}
          </div>
          <span class="text-sm text-gray-300">{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</span>
        </div>
        <!-- 退出按钮 -->
        <button class="auth-btn" @click="handleLogout">退出</button>
      </template>
      <template v-else>
        <router-link to="/login" class="auth-btn">登录</router-link>
      </template>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const avatarText = computed(() => {
  const name = userStore.userInfo?.nickname || userStore.userInfo?.username || ''
  return name.charAt(0).toUpperCase()
})

function handleLogout() {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}

const menuItems = [
  { path: '/', label: '首页' },
  { path: '/characters', label: '角色' },
  { path: '/weapons', label: '武器' },
  { path: '/echoes', label: '声骸' },
  { path: '/strategies', label: '攻略' },
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

@keyframes auth-breathe {
  0%, 100% {
    box-shadow: 0 0 8px rgba(99, 102, 241, 0.15);
  }
  50% {
    box-shadow: 0 0 18px rgba(99, 102, 241, 0.35);
  }
}
</style>
