import request from '@/utils/request'

/** 通用响应结构 */
export interface ApiResponse<T> {
  code: number
  data: T
  message: string
}

/** 角色列表项（与后端 CharacterInfo 严格对应） */
export interface CharacterItem {
  id: number
  name: string
  rarity: number
  element: string
  weaponType: string
  description?: string
  imageUrl?: string
  hp?: number
  atk?: number
  def?: number
  crit?: number
  energy?: number
  // 以下为详情扩展字段
  backstory?: string
  skills?: string
  buildGuide?: string
}

/** 前端展示用的图片字段别名（兼容 GachaCard 的 image prop） */
export type CharacterListItem = CharacterItem & { image?: string }

/** 获取角色列表 */
export function fetchCharacters() {
  return request.get<ApiResponse<CharacterItem[]>>('/characters')
}

/** 多条件分页搜索角色 */
export function searchCharacters(params: {
  name?: string
  element?: string
  weaponType?: string
  rarity?: number
  pageNum?: number
  pageSize?: number
}) {
  return request.get('/characters/search', { params })
}

/** 获取角色详情 */
export function fetchCharacterDetail(id: number | string) {
  return request.get<ApiResponse<CharacterItem>>(`/characters/${id}`)
}
