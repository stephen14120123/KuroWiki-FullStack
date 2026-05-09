package com.wiki.controller;

import com.wiki.common.RequiresRole;
import com.wiki.common.Result;
import com.wiki.entity.CharacterInfo;
import com.wiki.entity.EchoInfo;
import com.wiki.entity.StrategyGuide;
import com.wiki.entity.WeaponInfo;
import com.wiki.service.CharacterService;
import com.wiki.service.EchoService;
import com.wiki.service.StrategyService;
import com.wiki.service.UserService;
import com.wiki.service.WeaponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员专用接口
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private StrategyService strategyService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private WeaponService weaponService;

    @Autowired
    private EchoService echoService;

    // ==========================================
    // 用户管理
    // ==========================================
    @GetMapping("/users")
    @RequiresRole(1)
    public Result<List<?>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @DeleteMapping("/user/{userId}")
    @RequiresRole(1)
    public Result<String> deleteUser(@PathVariable Integer userId) {
        boolean success = userService.deleteUser(userId);
        return success ? Result.success("用户已删除") : Result.error("删除失败");
    }

    // ==========================================
    // 攻略管理
    // ==========================================
    @GetMapping("/strategies")
    @RequiresRole(1)
    public Result<List<StrategyGuide>> getAllStrategies() {
        return Result.success(strategyService.getAllStrategies());
    }

    @DeleteMapping("/strategy/{strategyId}")
    @RequiresRole(1)
    public Result<String> deleteStrategy(@PathVariable Integer strategyId) {
        boolean success = strategyService.removeStrategy(strategyId);
        return success ? Result.success("攻略已删除") : Result.error("删除失败");
    }

    // ==========================================
    // 角色管理
    // ==========================================
    @GetMapping("/characters")
    @RequiresRole(1)
    public Result<List<CharacterInfo>> getCharacters() {
        return Result.success(characterService.getAllCharacters());
    }

    @PostMapping("/character")
    @RequiresRole(1)
    public Result<String> addCharacter(@RequestBody CharacterInfo character) {
        characterService.saveCharacter(character);
        return Result.success("角色已添加");
    }

    @PutMapping("/character")
    @RequiresRole(1)
    public Result<String> updateCharacter(@RequestBody CharacterInfo character) {
        characterService.updateCharacter(character);
        return Result.success("角色已更新");
    }

    @DeleteMapping("/character/{id}")
    @RequiresRole(1)
    public Result<String> deleteCharacter(@PathVariable Integer id) {
        characterService.deleteCharacter(id);
        return Result.success("角色已删除");
    }

    // ==========================================
    // 武器管理
    // ==========================================
    @GetMapping("/weapons")
    @RequiresRole(1)
    public Result<List<WeaponInfo>> getWeapons() {
        return Result.success(weaponService.getAllWeapons());
    }

    @PostMapping("/weapon")
    @RequiresRole(1)
    public Result<String> addWeapon(@RequestBody WeaponInfo weapon) {
        weaponService.saveWeapon(weapon);
        return Result.success("武器已添加");
    }

    @PutMapping("/weapon")
    @RequiresRole(1)
    public Result<String> updateWeapon(@RequestBody WeaponInfo weapon) {
        weaponService.updateWeapon(weapon);
        return Result.success("武器已更新");
    }

    @DeleteMapping("/weapon/{id}")
    @RequiresRole(1)
    public Result<String> deleteWeapon(@PathVariable Integer id) {
        weaponService.deleteWeapon(id);
        return Result.success("武器已删除");
    }

    // ==========================================
    // 声骸管理
    // ==========================================
    @GetMapping("/echoes")
    @RequiresRole(1)
    public Result<List<EchoInfo>> getEchoes() {
        return Result.success(echoService.getAllEchoes());
    }

    @PostMapping("/echo")
    @RequiresRole(1)
    public Result<String> addEcho(@RequestBody EchoInfo echo) {
        echoService.saveEcho(echo);
        return Result.success("声骸已添加");
    }

    @PutMapping("/echo")
    @RequiresRole(1)
    public Result<String> updateEcho(@RequestBody EchoInfo echo) {
        echoService.updateEcho(echo);
        return Result.success("声骸已更新");
    }

    @DeleteMapping("/echo/{id}")
    @RequiresRole(1)
    public Result<String> deleteEcho(@PathVariable Integer id) {
        echoService.deleteEcho(id);
        return Result.success("声骸已删除");
    }
}
