package com.wiki.controller;

import com.wiki.common.RequiresRole;
import com.wiki.common.Result;
import com.wiki.entity.WeaponInfo;
import com.wiki.service.WeaponService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 武器模块 RESTful 接口
 */
@Api(tags = "武器管理")
@RestController
@RequestMapping("/api/weapons")
public class WeaponController {

    @Autowired
    private WeaponService weaponService;

    @ApiOperation("获取所有武器列表")
    @GetMapping({"", "/list"})
    public Result<List<WeaponInfo>> getAll() {
        return Result.success(weaponService.getAllWeapons());
    }

    @ApiOperation("获取单个武器详情")
    @GetMapping("/{id:\\d+}")
    public Result<WeaponInfo> getDetail(
            @ApiParam(value = "武器ID", required = true) @PathVariable Integer id) {
        WeaponInfo weapon = weaponService.getWeaponById(id);
        if (weapon == null) {
            throw new IllegalArgumentException("未找到该武器");
        }
        return Result.success(weapon);
    }

    @ApiOperation("新增武器（管理员）")
    @PostMapping
    @RequiresRole(1)
    public Result<String> addWeapon(
            @ApiParam(value = "武器信息", required = true) @Validated @RequestBody WeaponInfo weapon) {
        weaponService.saveWeapon(weapon);
        return Result.success("武器添加成功");
    }

    @ApiOperation("修改武器（管理员）")
    @PutMapping
    @RequiresRole(1)
    public Result<String> updateWeapon(
            @ApiParam(value = "武器信息（含ID）", required = true) @Validated @RequestBody WeaponInfo weapon) {
        if (weapon.getId() == null) {
            throw new IllegalArgumentException("武器 ID 不能为空");
        }
        weaponService.updateWeapon(weapon);
        return Result.success("武器修改成功");
    }

    @ApiOperation("删除武器（管理员）")
    @DeleteMapping("/{id:\\d+}")
    @RequiresRole(1)
    public Result<String> deleteWeapon(
            @ApiParam(value = "武器ID", required = true) @PathVariable Integer id) {
        weaponService.deleteWeapon(id);
        return Result.success("武器删除成功");
    }
}
