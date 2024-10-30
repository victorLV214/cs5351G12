package com.ruoyi.task.domain;

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
 * 任务对象 sys_task
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-30
 */
@ApiModel("任务实体")
public class SysTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 任务ID
     */
    @ApiModelProperty(value = "任务ID", example = "1")
    private Long taskId;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", example = "100")
    @Excel(name = "项目ID")
    private Long projectId;

    /**
     * 任务标题
     */
    @ApiModelProperty(value = "任务标题", example = "任务标题示例")
    @Excel(name = "任务标题")
    private String title;

    /**
     * 任务描述
     */
    @ApiModelProperty(value = "任务描述", example = "任务描述示例")
    @Excel(name = "任务描述")
    private String description;

    /**
     * 任务状态
     */
    @ApiModelProperty(value = "任务状态", example = "进行中")
    @Excel(name = "任务状态")
    private String status;

    /**
     * 任务优先级（0：低，1：中，2：高，3：紧急）
     */
    @ApiModelProperty(value = "任务优先级", example = "2", notes = "0：低，1：中，2：高，3：紧急")
    @Excel(name = "任务优先级", readConverterExp = "0=低，1中，2高，3紧急")
    private Integer priority;

    /**
     * 负责人ID
     */
    @ApiModelProperty(value = "负责人ID", example = "101")
    @Excel(name = "负责人ID")
    private Long assignedTo;

    /**
     * 截止日期
     */
    @ApiModelProperty(value = "截止日期", example = "2023-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "截止日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dueDate;

    /**
     * 预估工时
     */
    @ApiModelProperty(value = "预估工时", example = "8.5")
    @Excel(name = "预估工时")
    private BigDecimal estimatedHours;

    /**
     * 实际工时
     */
    @ApiModelProperty(value = "实际工时", example = "7.5")
    @Excel(name = "实际工时")
    private BigDecimal actualHours;

    /**
     * 父任务ID
     */
    @ApiModelProperty(value = "父任务ID", example = "0")
    @Excel(name = "父任务ID")
    private Long parentTaskId;

    /**
     * 标签（JSON格式）
     */
    @ApiModelProperty(value = "标签", example = "[\"标签1\", \"标签2\"]", notes = "JSON格式")
    @Excel(name = "标签", readConverterExp = "JSON格式")
    private String tags;

    /**
     * 迭代ID
     */
    @ApiModelProperty(value = "迭代ID", example = "200")
    @Excel(name = "迭代ID")
    private Long iterationId;

    /**
     * 复杂度（0：简单，1：中等，2：复杂）
     */
    @ApiModelProperty(value = "复杂度", example = "1", notes = "0：简单，1：中等，2：复杂")
    @Excel(name = "复杂度", readConverterExp = "0=简单，1中等，2复杂")
    private Integer complexity;

    /**
     * 依赖任务（JSON格式）
     */
    @ApiModelProperty(value = "依赖任务", example = "[1, 2, 3]", notes = "JSON格式")
    @Excel(name = "依赖任务", readConverterExp = "JSON格式")
    private String dependencies;

    /**
     * 完成时间
     */
    @ApiModelProperty(value = "完成时间", example = "2023-12-15")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completedAt;

    /**
     * 是否归档（0否 1是）
     */
    @ApiModelProperty(value = "是否归档", example = "1", notes = "0否 1是")
    @Excel(name = "是否归档", readConverterExp = "0=否,1=是")
    private Integer isArchived;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty(value = "删除标志", example = "0", notes = "0代表存在 2代表删除")
    private String delFlag;

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setEstimatedHours(BigDecimal estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public BigDecimal getEstimatedHours() {
        return estimatedHours;
    }

    public void setActualHours(BigDecimal actualHours) {
        this.actualHours = actualHours;
    }

    public BigDecimal getActualHours() {
        return actualHours;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTags() {
        return tags;
    }

    public void setIterationId(Long iterationId) {
        this.iterationId = iterationId;
    }

    public Long getIterationId() {
        return iterationId;
    }

    public void setComplexity(Integer complexity) {
        this.complexity = complexity;
    }

    public Integer getComplexity() {
        return complexity;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setCompletedAt(Date completedAt) {
        this.completedAt = completedAt;
    }

    public Date getCompletedAt() {
        return completedAt;
    }

    public void setIsArchived(Integer isArchived) {
        this.isArchived = isArchived;
    }

    public Integer getIsArchived() {
        return isArchived;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("taskId", getTaskId())
                .append("projectId", getProjectId())
                .append("title", getTitle())
                .append("description", getDescription())
                .append("status", getStatus())
                .append("priority", getPriority())
                .append("assignedTo", getAssignedTo())
                .append("dueDate", getDueDate())
                .append("estimatedHours", getEstimatedHours())
                .append("actualHours", getActualHours())
                .append("parentTaskId", getParentTaskId())
                .append("tags", getTags())
                .append("iterationId", getIterationId())
                .append("complexity", getComplexity())
                .append("dependencies", getDependencies())
                .append("completedAt", getCompletedAt())
                .append("isArchived", getIsArchived())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
