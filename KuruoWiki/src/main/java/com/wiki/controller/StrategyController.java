package com.wiki.controller;

import com.wiki.common.Result;
import com.wiki.entity.StrategyGuide;
import com.wiki.service.StrategyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 攻略模块 RESTful 接口
 */
@Api(tags = "攻略管理")
@RestController
@RequestMapping("/api/strategies")
public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @ApiOperation("获取攻略列表（可按角色筛选）")
    @GetMapping
    public Result<List<StrategyGuide>> getList(
            @ApiParam("角色ID（可选，不传则返回全部）") @RequestParam(required = false) Integer characterId) {
        if (characterId != null) {
            return Result.success(strategyService.getByCharacterId(characterId));
        }
        return Result.success(strategyService.getAllStrategies());
    }

    @ApiOperation("获取攻略详情")
    @GetMapping("/{id:\\d+}")
    public Result<StrategyGuide> getDetail(
            @ApiParam(value = "攻略ID", required = true) @PathVariable Integer id) {
        StrategyGuide strategy = strategyService.getById(id);
        if (strategy == null) {
            throw new IllegalArgumentException("未找到该攻略");
        }
        return Result.success(strategy);
    }

    @ApiOperation("发布新攻略（需登录）")
    @PostMapping
    public Result<String> add(
            @ApiParam(value = "攻略内容", required = true) @Validated @RequestBody StrategyGuide strategy,
            HttpServletRequest request) {
        Integer currentUserId = (Integer) request.getAttribute("currentUserId");
        strategy.setUserId(currentUserId);
        boolean success = strategyService.addStrategy(strategy);
        return success ? Result.success("攻略发布成功") : Result.error("发布失败");
    }

    @ApiOperation("编辑攻略（仅作者可操作）")
    @PutMapping
    public Result<String> update(
            @ApiParam(value = "攻略内容（含ID）", required = true) @Validated @RequestBody StrategyGuide strategy,
            HttpServletRequest request) {
        Integer currentUserId = (Integer) request.getAttribute("currentUserId");
        StrategyGuide oldStrategy = strategyService.getById(strategy.getId());

        if (oldStrategy == null) {
            throw new IllegalArgumentException("攻略不存在");
        }
        if (!oldStrategy.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("只能编辑自己的攻略");
        }

        boolean success = strategyService.updateStrategy(strategy);
        return success ? Result.success("攻略已更新") : Result.error("更新失败");
    }

    @ApiOperation("删除攻略（仅作者可操作）")
    @DeleteMapping("/{id:\\d+}")
    public Result<String> delete(
            @ApiParam(value = "攻略ID", required = true) @PathVariable Integer id,
            HttpServletRequest request) {
        Integer currentUserId = (Integer) request.getAttribute("currentUserId");
        StrategyGuide oldStrategy = strategyService.getById(id);

        if (oldStrategy == null) {
            throw new IllegalArgumentException("攻略不存在");
        }
        if (!oldStrategy.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("只能删除自己的攻略");
        }

        boolean success = strategyService.removeStrategy(id);
        return success ? Result.success("攻略已删除") : Result.error("删除失败");
    }
}
