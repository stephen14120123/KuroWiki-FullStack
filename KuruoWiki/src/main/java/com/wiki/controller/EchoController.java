package com.wiki.controller;

import com.wiki.common.RequiresRole;
import com.wiki.common.Result;
import com.wiki.entity.EchoInfo;
import com.wiki.service.EchoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 声骸模块 RESTful 接口
 */
@Api(tags = "声骸管理")
@RestController
@RequestMapping("/api/echoes")
public class EchoController {

    @Autowired
    private EchoService echoService;

    @ApiOperation("获取所有声骸列表")
    @GetMapping({"", "/list"})
    public Result<List<EchoInfo>> getAll() {
        return Result.success(echoService.getAllEchoes());
    }

    @ApiOperation("获取单个声骸详情")
    @GetMapping("/{id:\\d+}")
    public Result<EchoInfo> getDetail(
            @ApiParam(value = "声骸ID", required = true) @PathVariable Integer id) {
        EchoInfo echo = echoService.getEchoById(id);
        if (echo == null) {
            throw new IllegalArgumentException("未找到该声骸");
        }
        return Result.success(echo);
    }

    @ApiOperation("新增声骸（管理员）")
    @PostMapping
    @RequiresRole(1)
    public Result<String> addEcho(
            @ApiParam(value = "声骸信息", required = true) @Validated @RequestBody EchoInfo echo) {
        echoService.saveEcho(echo);
        return Result.success("声骸添加成功");
    }

    @ApiOperation("修改声骸（管理员）")
    @PutMapping
    @RequiresRole(1)
    public Result<String> updateEcho(
            @ApiParam(value = "声骸信息（含ID）", required = true) @Validated @RequestBody EchoInfo echo) {
        if (echo.getId() == null) {
            throw new IllegalArgumentException("声骸 ID 不能为空");
        }
        echoService.updateEcho(echo);
        return Result.success("声骸修改成功");
    }

    @ApiOperation("删除声骸（管理员）")
    @DeleteMapping("/{id:\\d+}")
    @RequiresRole(1)
    public Result<String> deleteEcho(
            @ApiParam(value = "声骸ID", required = true) @PathVariable Integer id) {
        echoService.deleteEcho(id);
        return Result.success("声骸删除成功");
    }
}
