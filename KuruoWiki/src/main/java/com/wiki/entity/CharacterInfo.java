package com.wiki.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class CharacterInfo {
    @ExcelProperty("角色ID")
    private Integer id;

    @ExcelProperty("角色名")
    private String name;

    @ExcelProperty("稀有度")
    private Integer rarity;

    @ExcelProperty("属性")
    private String element;

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