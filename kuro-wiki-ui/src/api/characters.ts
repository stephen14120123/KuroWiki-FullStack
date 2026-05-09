import request from '@/utils/request'

export interface CharacterItem {
  id: number
  name: string
  image?: string
  rarity: number
  element: string
  weaponType: string
}

export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}

/** 获取角色列表 */
export function fetchCharacters() {
  return request.get<ApiResponse<CharacterItem[]>>('/characters/list')
}

/** 角色详情 */
export interface CharacterDetail {
  id: number
  name: string
  image?: string
  rarity: number
  element: string
  weaponType: string
  background?: string
  skills?: SkillInfo[]
  buildSuggestion?: string
}

export interface SkillInfo {
  name: string
  description: string
  type: string
}

/** 获取角色详情 */
export function fetchCharacterDetail(id: number | string) {
  return request.get<ApiResponse<CharacterDetail>>(`/characters/info/${id}`)
}
