package com.wiki.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel("声骸信息")
public class EchoInfo {

    @ApiModelProperty("声骸ID")
    private Integer id;

    @NotBlank(message = "声骸名称不能为空")
    @ApiModelProperty(value = "声骸名称", required = true)
    private String name;

    @NotNull(message = "Cost值不能为空")
    @Min(value = 1, message = "Cost最小为1")
    @Max(value = 4, message = "Cost最大为4")
    @ApiModelProperty(value = "Cost值（1-4）", required = true)
    private Integer cost;

    @ApiModelProperty("套装效果名称")
    private String sonataEffect;

    @ApiModelProperty("技能描述")
    private String skillDesc;

    @ApiModelProperty("声骸图片URL")
    private String imageUrl;
}
