<template>
  <div class="login-page min-h-screen flex items-center justify-center bg-gray-950 relative overflow-hidden" @mousemove="onMouseMove">
    <!-- 背景光斑 -->
    <div class="pointer-events-none fixed inset-0 z-0" :style="spotStyle" />

    <!-- 背景网格装饰 -->
    <div class="pointer-events-none fixed inset-0 z-0 opacity-[0.03]" style="background-image: linear-gradient(rgba(255,255,255,0.1) 1px, transparent 1px), linear-gradient(90deg, rgba(255,255,255,0.1) 1px, transparent 1px); background-size: 60px 60px;" />

    <!-- 登录卡片 -->
    <div class="relative z-10 w-full max-w-sm mx-4">
      <div class="login-card p-8 rounded-2xl bg-gray-900/70 backdrop-blur-xl border border-gray-700/50 shadow-2xl shadow-indigo-500/5">
        <!-- Logo -->
        <div class="text-center mb-8">
          <h1 class="text-2xl font-bold text-white tracking-wide">Kuro Wiki</h1>
          <p class="mt-2 text-xs text-gray-500 tracking-widest uppercase">System Access</p>
        </div>

        <!-- 表单 -->
        <form @submit.prevent="handleLogin" class="space-y-5">
          <!-- 账号 -->
          <div class="space-y-1.5">
            <label class="text-xs text-gray-500 uppercase tracking-wider">Account</label>
            <div class="relative">
              <input
                v-model="form.username"
                type="text"
                autocomplete="username"
                placeholder="输入账号"
                class="login-input w-full px-4 py-3 rounded-lg bg-gray-800/60 border border-gray-700/60 text-white text-sm placeholder-gray-600 outline-none transition-all duration-200 focus:border-indigo-500/60 focus:shadow-sm focus:shadow-indigo-500/10"
              />
            </div>
          </div>

          <!-- 密码 -->
          <div class="space-y-1.5">
            <label class="text-xs text-gray-500 uppercase tracking-wider">Password</label>
            <div class="relative">
              <input
                v-model="form.password"
                type="password"
                autocomplete="current-password"
                placeholder="输入密码"
                class="login-input w-full px-4 py-3 rounded-lg bg-gray-800/60 border border-gray-700/60 text-white text-sm placeholder-gray-600 outline-none transition-all duration-200 focus:border-indigo-500/60 focus:shadow-sm focus:shadow-indigo-500/10"
              />
            </div>
          </div>

          <!-- 登录按钮 -->
          <button
            type="submit"
            :disabled="submitting"
            class="login-btn w-full py-3 mt-2 rounded-lg text-sm font-medium text-white transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span v-if="!submitting">进 入 系 统</span>
            <span v-else class="flex items-center justify-center gap-2">
              <svg class="animate-spin h-4 w-4" viewBox="0 0 24 24" fill="none">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
              </svg>
              验证中...
            </span>
          </button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/user'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const form = reactive({
  username: '',
  password: '',
})
const submitting = ref(false)

// 鼠标光斑
const mouseX = ref(0)
const mouseY = ref(0)

function onMouseMove(e: MouseEvent) {
  mouseX.value = e.clientX
  mouseY.value = e.clientY
}

const spotStyle = computed(() => ({
  background: `radial-gradient(500px circle at ${mouseX.value}px ${mouseY.value}px, rgba(99, 102, 241, 0.08), transparent 60%)`,
}))

async function handleLogin() {
  const username = form.username.trim()
  const password = form.password.trim()

  if (!username || !password) {
    ElMessage.warning('请输入账号和密码')
    return
  }

  submitting.value = true
  try {
    const res = await login({ username, password }) as any
    const token = res.data?.token
    const user = res.data?.user
    if (token) {
      userStore.setLogin(token, user)
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(res.message || '登录失败，未获取到 Token')
    }
  } catch (err: any) {
    console.error('登录异常', err)
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
/* 登录按钮呼吸灯 */
.login-btn {
  background: linear-gradient(135deg, rgba(99, 102, 241, 0.8), rgba(139, 92, 246, 0.8));
  box-shadow: 0 0 20px rgba(99, 102, 241, 0.3);
  animation: btn-breathe 3s ease-in-out infinite;
}

.login-btn:hover:not(:disabled) {
  box-shadow: 0 0 30px rgba(99, 102, 241, 0.5);
  transform: translateY(-1px);
}

@keyframes btn-breathe {
  0%, 100% {
    box-shadow: 0 0 15px rgba(99, 102, 241, 0.25);
  }
  50% {
    box-shadow: 0 0 30px rgba(99, 102, 241, 0.45);
  }
}

/* 卡片微光边框 */
.login-card {
  animation: card-glow 4s ease-in-out infinite;
}

@keyframes card-glow {
  0%, 100% {
    border-color: rgba(75, 85, 99, 0.5);
  }
  50% {
    border-color: rgba(99, 102, 241, 0.3);
  }
}
</style>
