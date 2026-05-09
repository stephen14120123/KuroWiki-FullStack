package com.wiki.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CharacterInfo {
    @ExcelProperty("角色ID")
    private Integer id;

    @NotBlank(message = "角色名称不能为空")
    @ExcelProperty("角色名")
    private String name;

    @NotNull(message = "稀有度不能为空")
    @Min(value = 4, message = "稀有度最低为4星")
    @Max(value = 5, message = "稀有度最高为5星")
    @ExcelProperty("稀有度")
    private Integer rarity;

    @NotBlank(message = "属性不能为空")
    @ExcelProperty("属性")
    private String element;

    @NotBlank(message = "武器类型不能为空")
    @ExcelProperty("武器类型")
    private String weaponType;

    @ExcelProperty("技能描述")
    private String description;

    @ExcelProperty("立绘路径")
    private String imageUrl;

    @ExcelProperty("生命值")
    private Integer hp;

    @ExcelProperty("攻击力")
    private Integer atk;

    @ExcelProperty("防御力")
    private Integer def;

    @ExcelProperty("暴击率")
    private Integer crit;

    @ExcelProperty("能量")
    private Integer energy;

    // ===== 详情页扩展字段 =====

    /** 背景故事 */
    private String backstory;

    /** 技能信息（JSON 格式存储，如：[{"name":"共鸣技能","desc":"..."},{"name":"共鸣解放","desc":"..."}]） */
    private String skills;

    /** 养成建议（如推荐武器、声骸搭配等） */
    private String buildGuide;
}