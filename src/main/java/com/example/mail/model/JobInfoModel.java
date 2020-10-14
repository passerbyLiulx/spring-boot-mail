package com.example.mail.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("定时任务信息")
public class JobInfoModel extends BaseModel{

	@ApiModelProperty("用户ID")
	private String jobId;

	@ApiModelProperty("任务名称")
	private String taskName;

	@ApiModelProperty("任务路径")
	private String taskPath;

	@ApiModelProperty("表达式")
	private String expression;

	@ApiModelProperty("表达式说明")
	private String expressionDescription;

	@ApiModelProperty("任务周期")
	private String taskCycle;

	@ApiModelProperty("执行节点")
	private String runNode;

	@ApiModelProperty("开启状态：0关闭 1开启")
	private Integer openState;
}
