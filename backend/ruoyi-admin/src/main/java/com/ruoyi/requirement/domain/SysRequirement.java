package com.ruoyi.requirement.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 需求对象 sys_requirement
 *
 * @author sarek
 * @date 2024-10-14
 */
public class SysRequirement extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 需求ID */
    private Long requirementId;

    /** 项目ID */
    @Excel(name = "项目ID")
    private Long projectId;

    /** 需求标题 */
    @Excel(name = "需求标题")
    private String title;

    /** 需求详细描述 */
    @Excel(name = "需求详细描述")
    private String description;

    /** 优先级（1低 2中 3高） */
    @Excel(name = "优先级", readConverterExp = "1=低,2=中,3=高")
    private Integer priority;

    /** 需求状态 */
    @Excel(name = "需求状态")
    private String status;

    /** 负责人ID */
    @Excel(name = "负责人ID")
    private Long assignedTo;

    /** 预期完成日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预期完成日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dueDate;

    /** 实际完成日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际完成日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completedDate;

    /** 需求类型 */
    @Excel(name = "需求类型")
    private String type;

    /** 估计工作量 */
    @Excel(name = "估计工作量")
    private BigDecimal estimatedEffort;

    /** 实际工作量 */
    @Excel(name = "实际工作量")
    private BigDecimal actualEffort;

    /** 版本号 */
    @Excel(name = "版本号")
    private String version;

    /** 删除标志（0:存在 2:删除） */
    private String delFlag;

    public void setRequirementId(Long requirementId) 
    {
        this.requirementId = requirementId;
    }

    public Long getRequirementId() 
    {
        return requirementId;
    }
    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }
    public void setPriority(Integer priority) 
    {
        this.priority = priority;
    }

    public Integer getPriority() 
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
    public void setAssignedTo(Long assignedTo) 
    {
        this.assignedTo = assignedTo;
    }

    public Long getAssignedTo() 
    {
        return assignedTo;
    }
    public void setDueDate(Date dueDate) 
    {
        this.dueDate = dueDate;
    }

    public Date getDueDate() 
    {
        return dueDate;
    }
    public void setCompletedDate(Date completedDate) 
    {
        this.completedDate = completedDate;
    }

    public Date getCompletedDate() 
    {
        return completedDate;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setEstimatedEffort(BigDecimal estimatedEffort) 
    {
        this.estimatedEffort = estimatedEffort;
    }

    public BigDecimal getEstimatedEffort() 
    {
        return estimatedEffort;
    }
    public void setActualEffort(BigDecimal actualEffort) 
    {
        this.actualEffort = actualEffort;
    }

    public BigDecimal getActualEffort() 
    {
        return actualEffort;
    }
    public void setVersion(String version) 
    {
        this.version = version;
    }

    public String getVersion() 
    {
        return version;
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
            .append("requirementId", getRequirementId())
            .append("projectId", getProjectId())
            .append("title", getTitle())
            .append("description", getDescription())
            .append("priority", getPriority())
            .append("status", getStatus())
            .append("assignedTo", getAssignedTo())
            .append("dueDate", getDueDate())
            .append("completedDate", getCompletedDate())
            .append("type", getType())
            .append("estimatedEffort", getEstimatedEffort())
            .append("actualEffort", getActualEffort())
            .append("version", getVersion())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
