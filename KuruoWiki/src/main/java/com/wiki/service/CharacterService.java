package com.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wiki.dao.CharacterMapper;
import com.wiki.entity.CharacterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CharacterService {

    @Autowired
    private CharacterMapper characterMapper;

    public List<CharacterInfo> getAllCharacters() {
        return characterMapper.getAllCharacters();
    }

    public CharacterInfo getCharacterById(Integer id) {
        return characterMapper.getCharacterById(id);
    }

    public PageInfo<CharacterInfo> getCharactersByPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CharacterInfo> list = characterMapper.getAllCharacters();
        return new PageInfo<>(list);
    }

    public void saveCharacter(CharacterInfo character) {
        int rows = characterMapper.insertCharacter(character);
        if (rows == 0) {
            throw new IllegalArgumentException("添加角色失败");
        }
    }

    public void updateCharacter(CharacterInfo character) {
        int rows = characterMapper.updateCharacter(character);
        if (rows == 0) {
            throw new IllegalArgumentException("修改角色失败，角色可能不存在");
        }
    }

    public void deleteCharacter(Integer id) {
        int rows = characterMapper.deleteCharacter(id);
        if (rows == 0) {
            throw new IllegalArgumentException("删除角色失败，角色可能不存在");
        }
    }
}
