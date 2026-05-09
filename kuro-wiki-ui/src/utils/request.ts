import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import pinia from '@/stores'

const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 15000,
})

// 请求拦截器：自动携带 Token
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const userStore = useUserStore(pinia)
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器：统一处理业务错误和 HTTP 错误
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response
    if (data.code !== 0 && data.code !== 200) {
      // 业务层 401：Token 过期或未登录
      if (data.code === 401) {
        const userStore = useUserStore(pinia)
        userStore.logout()
        ElMessage.error(data.message || '登录已过期，请重新登录')
        window.location.href = '/login'
        return Promise.reject(new Error(data.message))
      }
      // 业务层 403：无权限
      if (data.code === 403) {
        ElMessage.warning(data.message || '权限不足，无法执行此操作')
        return Promise.reject(new Error(data.message))
      }
      ElMessage.error(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
    return data
  },
  (error) => {
    const status = error.response?.status
    const msg = error.response?.data?.message || error.message || '网络异常'

    // HTTP 401：Token 无效或过期
    if (status === 401) {
      const userStore = useUserStore(pinia)
      userStore.logout()
      ElMessage.error('登录已过期，请重新登录')
      window.location.href = '/login'
      return Promise.reject(error)
    }

    // HTTP 403：无权限
    if (status === 403) {
      ElMessage.warning('权限不足，无法执行此操作')
      return Promise.reject(error)
    }

    ElMessage.error(msg)
    return Promise.reject(error)
  }
)

export default service
