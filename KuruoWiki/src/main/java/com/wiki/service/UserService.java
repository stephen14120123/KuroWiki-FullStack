package com.wiki.service;

import com.wiki.dao.StrategyMapper;
import com.wiki.dao.UserMapper;
import com.wiki.entity.StrategyGuide;
import com.wiki.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户业务层
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 用户登录校验
     * 优先使用 BCrypt 校验，如果用户密码是旧的明文格式则兼容明文比对。
     */
    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return null;
        }

        // BCrypt 加密的密码以 $2a$ 或 $2b$ 开头
        if (user.getPassword().startsWith("$2a$") || user.getPassword().startsWith("$2b$")) {
            // BCrypt 校验
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        } else {
            // 兼容旧的明文密码
            if (password.equals(user.getPassword())) {
                // 顺便把明文密码升级为 BCrypt（可选）
                String encoded = passwordEncoder.encode(password);
                user.setPassword(encoded);
                // 这里可以异步更新数据库，暂不实现
                return user;
            }
        }

        return null;
    }

    /**
     * 用户注册
     */
    public void register(String username, String password, String nickname) {
        // 检查用户名是否已存在
        User existing = userMapper.findByUsername(username);
        if (existing != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // BCrypt 加密
        user.setNickname(nickname != null ? nickname : username);
        user.setRole(0); // 默认普通用户

        userMapper.insertUser(user);
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
     */
    public List<StrategyGuide> getUserStrategies(Integer userId) {
        return strategyMapper.getStrategiesByUserId(userId);
    }
}
