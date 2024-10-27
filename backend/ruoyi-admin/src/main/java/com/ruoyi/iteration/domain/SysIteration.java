package com.ruoyi.iteration.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 迭代对象 sys_iteration
 * 
 * @author ruoyi
 * @date 2024-10-27
 */
public class SysIteration extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 迭代ID */
    private Long iterationId;

    /** 项目ID */
    @Excel(name = "项目ID")
    private Long projectId;

    /** 迭代名称 */
    @Excel(name = "迭代名称")
    private String name;

    /** 迭代详细描述 */
    @Excel(name = "迭代详细描述")
    private String description;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    /** 迭代状态 */
    @Excel(name = "迭代状态")
    private String status;

    /** 迭代目标 */
    @Excel(name = "迭代目标")
    private String goal;

    /** 计划完成的故事点数 */
    @Excel(name = "计划完成的故事点数")
    private Long plannedStoryPoints;

    /** 实际完成的故事点数 */
    @Excel(name = "实际完成的故事点数")
    private Long completedStoryPoints;

    /** 团队速度 */
    @Excel(name = "团队速度")
    private BigDecimal velocity;

    /** 迭代回顾总结 */
    @Excel(name = "迭代回顾总结")
    private String retrospective;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setIterationId(Long iterationId) 
    {
        this.iterationId = iterationId;
    }

    public Long getIterationId() 
    {
        return iterationId;
    }
    public void setProjectId(Long projectId) 
    {
        this.projectId = projectId;
    }

    public Long getProjectId() 
    {
        return projectId;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
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
    public void setEndDate(Date endDate) 
    {
        this.endDate = endDate;
    }

    public Date getEndDate() 
    {
        return endDate;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setGoal(String goal) 
    {
        this.goal = goal;
    }

    public String getGoal() 
    {
        return goal;
    }
    public void setPlannedStoryPoints(Long plannedStoryPoints) 
    {
        this.plannedStoryPoints = plannedStoryPoints;
    }

    public Long getPlannedStoryPoints() 
    {
        return plannedStoryPoints;
    }
    public void setCompletedStoryPoints(Long completedStoryPoints) 
    {
        this.completedStoryPoints = completedStoryPoints;
    }

    public Long getCompletedStoryPoints() 
    {
        return completedStoryPoints;
    }
    public void setVelocity(BigDecimal velocity) 
    {
        this.velocity = velocity;
    }

    public BigDecimal getVelocity() 
    {
        return velocity;
    }
    public void setRetrospective(String retrospective) 
    {
        this.retrospective = retrospective;
    }

    public String getRetrospective() 
    {
        return retrospective;
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
            .append("iterationId", getIterationId())
            .append("projectId", getProjectId())
            .append("name", getName())
            .append("description", getDescription())
            .append("startDate", getStartDate())
            .append("endDate", getEndDate())
            .append("status", getStatus())
            .append("goal", getGoal())
            .append("plannedStoryPoints", getPlannedStoryPoints())
            .append("completedStoryPoints", getCompletedStoryPoints())
            .append("velocity", getVelocity())
            .append("retrospective", getRetrospective())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .toString();
    }
}
