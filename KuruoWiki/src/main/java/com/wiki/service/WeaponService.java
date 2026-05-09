package com.wiki.service;

import com.wiki.dao.WeaponMapper;
import com.wiki.entity.WeaponInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class WeaponService {

    @Autowired
    private WeaponMapper weaponMapper;

    public List<WeaponInfo> getAllWeapons() {
        return weaponMapper.getAllWeapons();
    }

    public WeaponInfo getWeaponById(Integer id) {
        return weaponMapper.getWeaponById(id);
    }

    public void saveWeapon(WeaponInfo weapon) {
        int rows = weaponMapper.insertWeapon(weapon);
        if (rows == 0) {
            throw new IllegalArgumentException("武器添加失败");
        }
    }

    public void updateWeapon(WeaponInfo weapon) {
        int rows = weaponMapper.updateWeapon(weapon);
        if (rows == 0) {
            throw new IllegalArgumentException("武器修改失败，武器可能不存在");
        }
    }

    public void deleteWeapon(Integer id) {
        int rows = weaponMapper.deleteWeapon(id);
        if (rows == 0) {
            throw new IllegalArgumentException("武器删除失败，武器可能不存在");
        }
    }
}
