package com.ruoyi.project.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 项目对象 sys_project
 * 
 * @author AB-IN-LIUSY
 * @date 2024-10-14
 */
@ApiModel("项目实体")
public class SysProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 项目ID */
    @ApiModelProperty(value = "项目ID", example = "1")
    private Long projectId;

    /** 项目名称 */
    @ApiModelProperty(value = "项目名称", example = "智慧城市项目")
    @Excel(name = "项目名称")
    private String projectName;

    /** 项目编码 */
    @ApiModelProperty(value = "项目编码", example = "ZC20231001")
    @Excel(name = "项目编码")
    private String projectCode;

    /** 项目描述 */
    @ApiModelProperty(value = "项目描述", example = "智慧城市管理系统开发")
    @Excel(name = "项目描述")
    private String description;

    /** 开始日期 */
    @ApiModelProperty(value = "开始日期", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 预计结束日期 */
    @ApiModelProperty(value = "预计结束日期", example = "2024-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectedEndDate;

    /** 实际结束日期 */
    @ApiModelProperty(value = "实际结束日期", example = "2024-11-30")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualEndDate;

    /** 项目负责人ID */
    @ApiModelProperty(value = "项目负责人ID", example = "1001")
    @Excel(name = "项目负责人ID")
    private Long projectManagerId;

    /** 优先级（1低 2中 3高） */
    @ApiModelProperty(value = "优先级（1=低，2=中，3=高）", example = "2")
    @Excel(name = "优先级", readConverterExp = "1=低,2=中,3=高")
    private Long priority;

    /** 项目状态 */
    @ApiModelProperty(value = "项目状态", example = "进行中")
    @Excel(name = "项目状态")
    private String status;

    /** 完成百分比 */
    @ApiModelProperty(value = "完成百分比", example = "75.5")
    @Excel(name = "完成百分比")
    private BigDecimal completionPercentage;

    /** 项目预算 */
    @ApiModelProperty(value = "项目预算", example = "500000.00")
    @Excel(name = "项目预算")
    private BigDecimal budget;

    /** 实际花费 */
    @ApiModelProperty(value = "实际花费", example = "420000.00")
    @Excel(name = "实际花费")
    private BigDecimal actualCost;

    /** 删除标志（0代表存在 2代表删除） */
    @ApiModelProperty(value = "删除标志（0代表存在，2代表删除）", example = "0")
    private String delFlag;

    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }
    public void setProjectName(String projectName) 
    {
        this.projectName = projectName;
    }

    public String getProjectName() 
    {
        return projectName;
    }
    public void setProjectCode(String projectCode) 
    {
        this.projectCode = projectCode;
    }

    public String getProjectCode() 
    {
        return projectCode;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setStartDate(Date startDate) 
    {
        this.startDate = startDate;
    }

    public Date getStartDate() 
    {
        return startDate;
    }
    public void setExpectedEndDate(Date expectedEndDate) 
    {
        this.expectedEndDate = expectedEndDate;
    }

    public Date getExpectedEndDate() 
    {
        return expectedEndDate;
    }
    public void setActualEndDate(Date actualEndDate) 
    {
        this.actualEndDate = actualEndDate;
    }

    public Date getActualEndDate() 
    {
        return actualEndDate;
    }
    public void setProjectManagerId(Long projectManagerId) 
    {
        this.projectManagerId = projectManagerId;
    }

    public Long getProjectManagerId() 
    {
        return projectManagerId;
    }
    public void setPriority(Long priority) 
    {
        this.priority = priority;
    }

    public Long getPriority() 
    {
        return priority;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setCompletionPercentage(BigDecimal completionPercentage) 
    {
        this.completionPercentage = completionPercentage;
    }

    public BigDecimal getCompletionPercentage() 
    {
        return completionPercentage;
    }
    public void setBudget(BigDecimal budget) 
    {
        this.budget = budget;
    }

    public BigDecimal getBudget() 
    {
        return budget;
    }
    public void setActualCost(BigDecimal actualCost) 
    {
        this.actualCost = actualCost;
    }

    public BigDecimal getActualCost() 
    {
        return actualCost;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("projectId", getProjectId())
            .append("projectName", getProjectName())
            .append("projectCode", getProjectCode())
            .append("description", getDescription())
            .append("startDate", getStartDate())
            .append("expectedEndDate", getExpectedEndDate())
            .append("actualEndDate", getActualEndDate())
            .append("projectManagerId", getProjectManagerId())
            .append("priority", getPriority())
            .append("status", getStatus())
            .append("completionPercentage", getCompletionPercentage())
            .append("budget", getBudget())
            .append("actualCost", getActualCost())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
