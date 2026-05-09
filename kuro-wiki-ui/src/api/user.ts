import request from '@/utils/request'
import type { ApiResponse } from '@/api/characters'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResult {
  token: string
}

/** 用户登录 */
export function login(params: LoginParams) {
  return request.post<ApiResponse<LoginResult>>('/user/login', params)
}
