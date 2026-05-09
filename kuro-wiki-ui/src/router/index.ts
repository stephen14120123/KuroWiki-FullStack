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
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
      },
      {
        path: 'characters',
        name: 'Characters',
        component: () => import('@/views/Characters.vue'),
      },
      {
        path: 'character/:id',
        name: 'CharacterDetail',
        component: () => import('@/views/CharacterDetail.vue'),
      },
      {
        path: 'weapons',
        name: 'Weapons',
        component: () => import('@/views/Weapons.vue'),
      },
      {
        path: 'echoes',
        name: 'Echoes',
        component: () => import('@/views/Echoes.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 全局前置守卫（暂时关闭，方便预览）
// router.beforeEach((to, _from, next) => {
//   const token = localStorage.getItem('token')
//   if (to.name !== 'Login' && !token) {
//     next({ name: 'Login' })
//   } else {
//     next()
//   }
// })

export default router
