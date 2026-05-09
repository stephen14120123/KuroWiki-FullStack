package com.wiki.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("武器信息")
public class WeaponInfo {

    @ApiModelProperty("武器ID")
    private Integer id;

    @NotBlank(message = "武器名称不能为空")
    @ApiModelProperty(value = "武器名称", required = true)
    private String name;

    @NotNull(message = "稀有度不能为空")
    @Min(value = 3, message = "稀有度最低为3星")
    @Max(value = 5, message = "稀有度最高为5星")
    @ApiModelProperty(value = "稀有度（3-5）", required = true)
    private Integer rarity;

    @NotBlank(message = "武器类型不能为空")
    @ApiModelProperty(value = "武器类型（迅刀/长刃/臂铠/音感仪/佩枪）", required = true)
    private String weaponType;

    @ApiModelProperty("基础攻击力")
    private Integer baseAtk;

    @ApiModelProperty("副属性类型")
    private String subStatType;

    @ApiModelProperty("副属性数值")
    private String subStatValue;

    @ApiModelProperty("武器技能名称")
    private String skillName;

    @ApiModelProperty("武器技能描述")
    private String skillDesc;

    @ApiModelProperty("武器图片URL")
    private String imageUrl;
}
