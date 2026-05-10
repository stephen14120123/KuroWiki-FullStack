package com.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wiki.dao.CharacterMapper;
import com.wiki.dto.CharacterQueryDTO;
import com.wiki.entity.CharacterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CharacterService {

    @Autowired
    private CharacterMapper characterMapper;

    /**
     * 获取所有角色列表（缓存）
     * 缓存 key: characters::all
     */
    @Cacheable(value = "characters", key = "'all'")
    public List<CharacterInfo> getAllCharacters() {
        return characterMapper.getAllCharacters();
    }

    /**
     * 获取单个角色详情（缓存）
     * 缓存 key: characters::detail::{id}
     */
    @Cacheable(value = "characters", key = "'detail::' + #id")
    public CharacterInfo getCharacterById(Integer id) {
        return characterMapper.getCharacterById(id);
    }

    public PageInfo<CharacterInfo> getCharactersByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CharacterInfo> list = characterMapper.getAllCharacters();
        return new PageInfo<>(list);
    }

    /**
     * 多条件分页查询（不走缓存，直接查库）
     * <p>
     * 架构建议：带复杂筛选条件的分页查询不适合缓存，原因：
     * 1. 查询参数组合多（name × element × weaponType × rarity × page），缓存命中率极低
     * 2. 分页数据时效性要求高，缓存一致性维护成本大
     * 3. 角色总量有限（百级），数据库查询本身很快
     * 所以这里直接查库，让 Redis 缓存专注于高频的"全量列表"和"详情"接口。
     * </p>
     */
    public PageInfo<CharacterInfo> queryByCondition(CharacterQueryDTO query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<CharacterInfo> list = characterMapper.queryByCondition(query);
        return new PageInfo<>(list);
    }

    /**
     * 新增角色 — 清除列表缓存
     */
    @CacheEvict(value = "characters", allEntries = true)
    public void saveCharacter(CharacterInfo character) {
        int rows = characterMapper.insertCharacter(character);
        if (rows == 0) {
            throw new IllegalArgumentException("添加角色失败");
        }
    }

    /**
     * 修改角色 — 清除所有角色缓存（列表 + 详情）
     */
    @CacheEvict(value = "characters", allEntries = true)
    public void updateCharacter(CharacterInfo character) {
        int rows = characterMapper.updateCharacter(character);
        if (rows == 0) {
            throw new IllegalArgumentException("修改角色失败，角色可能不存在");
        }
    }

    /**
     * 删除角色 — 清除所有角色缓存
     */
    @CacheEvict(value = "characters", allEntries = true)
    public void deleteCharacter(Integer id) {
        int rows = characterMapper.deleteCharacter(id);
        if (rows == 0) {
            throw new IllegalArgumentException("删除角色失败，角色可能不存在");
        }
    }
}
