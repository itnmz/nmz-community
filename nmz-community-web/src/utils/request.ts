import axios from 'axios'
import { ElMessage } from 'element-plus'
import 'element-plus/es/components/message/style/css'
import router from '@/router'
import type { Result } from '@/types/api'

/**
 * axios 实例。
 *
 * 响应拦截器直接把 res.data（即后端的 Result<T>）返回，
 * 所以调用方拿到的就是 Result，不用再 .data.data 两层剥。
 */
const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/',
  timeout: 10_000
})

instance.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

instance.interceptors.response.use(
  (response) => response.data,
  (error) => {
    const status: number | undefined = error.response?.status
    const message: string =
      error.response?.data?.message ?? '网络异常，请稍后重试'

    if (status === 401) {
      // 密码错误、未登录、token 过期，后端统一返回 401
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      ElMessage.error(message)
      router.push({
        name: 'login',
        query: { redirect: router.currentRoute.value.fullPath }
      })
    } else {
      ElMessage.error(message)
    }
    return Promise.reject(error)
  }
)

/** 供业务层调用的类型化外壳：直接拿到 Result<T> */
export interface HttpClient {
  get<T>(url: string, params?: Record<string, unknown>): Promise<Result<T>>
  post<T>(url: string, data?: unknown): Promise<Result<T>>
  put<T>(url: string, data?: unknown): Promise<Result<T>>
  delete<T>(url: string, params?: Record<string, unknown>): Promise<Result<T>>
}

export default instance as unknown as HttpClient
