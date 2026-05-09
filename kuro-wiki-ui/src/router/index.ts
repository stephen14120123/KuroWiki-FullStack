import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
  },
  {
    path: '/',
    component: () => import('@/layouts/DefaultLayout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('@/views/Home.vue') },
      { path: 'characters', name: 'Characters', component: () => import('@/views/Characters.vue') },
      { path: 'character/:id', name: 'CharacterDetail', component: () => import('@/views/CharacterDetail.vue') },
      { path: 'weapons', name: 'Weapons', component: () => import('@/views/Weapons.vue') },
      { path: 'weapon/:id', name: 'WeaponDetail', component: () => import('@/views/WeaponDetail.vue') },
      { path: 'echoes', name: 'Echoes', component: () => import('@/views/Echoes.vue') },
      { path: 'echo/:id', name: 'EchoDetail', component: () => import('@/views/EchoDetail.vue') },
      { path: 'strategies', name: 'Strategies', component: () => import('@/views/Strategies.vue') },
      { path: 'strategy/:id', name: 'StrategyDetail', component: () => import('@/views/StrategyDetail.vue') },
    ],
  },
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAdmin: true },
    children: [
      { path: '', redirect: '/admin/characters' },
      { path: 'characters', name: 'AdminCharacters', component: () => import('@/views/admin/CharacterManage.vue') },
      { path: 'weapons', name: 'AdminWeapons', component: () => import('@/views/admin/WeaponManage.vue') },
      { path: 'echoes', name: 'AdminEchoes', component: () => import('@/views/admin/EchoManage.vue') },
      { path: 'strategies', name: 'AdminStrategies', component: () => import('@/views/admin/StrategyManage.vue') },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫：管理后台权限拦截
router.beforeEach((to, _from, next) => {
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null')
    const token = localStorage.getItem('token')

    if (!token) {
      next({ name: 'Login' })
    } else if (!userInfo || userInfo.role !== 1) {
      // 非管理员，重定向到首页
      import('element-plus').then(({ ElMessage }) => {
        ElMessage.warning('权限不足，仅管理员可访问后台')
      })
      next({ name: 'Home' })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
