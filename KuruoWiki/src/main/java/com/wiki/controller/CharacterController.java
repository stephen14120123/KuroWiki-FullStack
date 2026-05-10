package com.wiki.controller;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.wiki.common.RequiresRole;
import com.wiki.common.Result;
import com.wiki.dao.CharacterMapper;
import com.wiki.dto.CharacterExcelDTO;
import com.wiki.dto.CharacterQueryDTO;
import com.wiki.entity.CharacterInfo;
import com.wiki.listener.CharacterExcelListener;
import com.wiki.service.CharacterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 角色模块 RESTful 接口
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    @Autowired
    private CharacterService characterService;

    @Autowired
    private CharacterMapper characterMapper;

    @ApiOperation("获取所有角色列表")
    @GetMapping
    public Result<List<CharacterInfo>> getAll() {
        return Result.success(characterService.getAllCharacters());
    }

    @ApiOperation("多条件分页查询角色")
    @GetMapping("/search")
    public Result<PageInfo<CharacterInfo>> search(CharacterQueryDTO query) {
        return Result.success(characterService.queryByCondition(query));
    }

    @ApiOperation("获取单个角色详情")
    @GetMapping("/{id:\\d+}")
    public Result<CharacterInfo> getDetail(
            @ApiParam(value = "角色ID", required = true) @PathVariable Integer id) {
        CharacterInfo character = characterService.getCharacterById(id);
        if (character == null) {
            throw new IllegalArgumentException("未找到该角色");
        }
        return Result.success(character);
    }

    @ApiOperation("新增角色（管理员）")
    @PostMapping
    @RequiresRole(1)
    public Result<String> addCharacter(
            @ApiParam(value = "角色信息", required = true) @Validated @RequestBody CharacterInfo character) {
        characterService.saveCharacter(character);
        return Result.success("添加成功");
    }

    @ApiOperation("修改角色（管理员）")
    @PutMapping
    @RequiresRole(1)
    public Result<String> updateCharacter(
            @ApiParam(value = "角色信息（含ID）", required = true) @Validated @RequestBody CharacterInfo character) {
        if (character.getId() == null) {
            throw new IllegalArgumentException("角色 ID 不能为空");
        }
        characterService.updateCharacter(character);
        return Result.success("修改成功");
    }

    @ApiOperation("删除角色（管理员）")
    @DeleteMapping("/{id:\\d+}")
    @RequiresRole(1)
    public Result<String> deleteCharacter(
            @ApiParam(value = "角色ID", required = true) @PathVariable Integer id) {
        characterService.deleteCharacter(id);
        return Result.success("删除成功");
    }

    @ApiOperation("Excel 批量导入角色（管理员）")
    @PostMapping("/import")
    @RequiresRole(1)
    public Result<String> importExcel(
            @ApiParam(value = "Excel文件(.xlsx)", required = true) @RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }
        CharacterExcelListener listener = new CharacterExcelListener(characterMapper);
        EasyExcel.read(file.getInputStream(), CharacterExcelDTO.class, listener).sheet().doRead();
        return Result.success("导入成功，共 " + listener.getTotalCount() + " 条数据");
    }
}
