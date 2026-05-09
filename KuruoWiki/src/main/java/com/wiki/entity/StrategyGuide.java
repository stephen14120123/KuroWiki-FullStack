package com.wiki.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@ApiModel("攻略信息")
public class StrategyGuide {

    @ApiModelProperty("攻略ID")
    private Integer id;

    @ApiModelProperty("发布者ID")
    private Integer userId;

    @ApiModelProperty("关联角色ID")
    private Integer characterId;

    @NotBlank(message = "攻略标题不能为空")
    @ApiModelProperty(value = "攻略标题", required = true)
    private String title;

    @NotBlank(message = "攻略内容不能为空")
    @ApiModelProperty(value = "攻略正文", required = true)
    private String content;

    @ApiModelProperty("浏览量")
    private Integer views;

    @ApiModelProperty("发布时间")
    private Date createTime;

    // 扩展字段（联表查询填充）
    @ApiModelProperty("作者昵称")
    private String authorName;

    @ApiModelProperty("关联角色名")
    private String characterName;

    @ApiModelProperty("作者信息")
    private User author;
}
