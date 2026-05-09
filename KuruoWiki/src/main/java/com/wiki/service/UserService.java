package com.wiki.service;

import com.wiki.dao.StrategyMapper;
import com.wiki.dao.UserMapper;
import com.wiki.entity.StrategyGuide;
import com.wiki.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户业务层
 *
 * @author KuroWiki
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StrategyMapper strategyMapper;

    /**
     * 用户登录校验
     *
     * @param username 用户名
     * @param password 密码
     * @return 匹配的用户实体，不存在返回 null
     */
    public User login(String username, String password) {
        return userMapper.login(username, password);
    }

    /**
     * 获取所有用户列表（管理员用）
     */
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    /**
     * 删除用户
     */
    public boolean deleteUser(Integer id) {
        return userMapper.deleteUser(id) > 0;
    }

    /**
     * 获取指定用户发布的攻略列表
     *
     * @param userId 用户 ID
     * @return 该用户的攻略列表
     */
    public List<StrategyGuide> getUserStrategies(Integer userId) {
        return strategyMapper.getStrategiesByUserId(userId);
    }
}