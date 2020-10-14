package com.example.mail.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 通用参数
 */
@Data
public class BaseModel {

    @ApiModelProperty("分页参数")
    private int pageNum;

    @ApiModelProperty("分页参数")
    private int pageSize;
}
