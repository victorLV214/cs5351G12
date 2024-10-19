package com.ruoyi.item.domain;

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
 * 工作项对象 sys_work_item
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-20
 */
@ApiModel("工作项实体")
public class SysWorkItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 工作项ID
     */
    @ApiModelProperty(value = "工作项ID", example = "1")
    private Long workItemId;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", example = "100")
    @Excel(name = "项目ID")
    private Long projectId;

    /**
     * 迭代ID
     */
    @ApiModelProperty(value = "迭代ID", example = "200")
    @Excel(name = "迭代ID")
    private Long iterationId;

    /**
     * 工作项标题
     */
    @ApiModelProperty(value = "工作项标题", example = "设计登录页面")
    @Excel(name = "工作项标题")
    private String title;

    /**
     * 工作项详细描述
     */
    @ApiModelProperty(value = "工作项详细描述", example = "负责设计并开发登录页面")
    @Excel(name = "工作项详细描述")
    private String description;

    /**
     * 工作项类型
     */
    @ApiModelProperty(value = "工作项类型", example = "开发任务")
    @Excel(name = "工作项类型")
    private String type;

    /**
     * 工作项状态
     */
    @ApiModelProperty(value = "工作项状态", example = "进行中")
    @Excel(name = "工作项状态")
    private String status;

    /**
     * 优先级（1低 2中 3高 4紧急）
     */
    @ApiModelProperty(value = "优先级", allowableValues = "1, 2, 3, 4", example = "2")
    @Excel(name = "优先级", readConverterExp = "1=低,2=中,3=高,4=紧急")
    private Integer priority;

    /**
     * 负责人ID
     */
    @ApiModelProperty(value = "负责人ID", example = "300")
    @Excel(name = "负责人ID")
    private Long assignedTo;

    /**
     * 开始日期
     */
    @ApiModelProperty(value = "开始日期", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 预期完成日期
     */
    @ApiModelProperty(value = "预期完成日期", example = "2024-01-10")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预期完成日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dueDate;

    /**
     * 实际完成日期
     */
    @ApiModelProperty(value = "实际完成日期", example = "2024-01-09")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际完成日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completedDate;

    /**
     * 估计工作量
     */
    @ApiModelProperty(value = "估计工作量", example = "5.5")
    @Excel(name = "估计工作量")
    private BigDecimal estimatedEffort;

    /**
     * 实际工作量
     */
    @ApiModelProperty(value = "实际工作量", example = "6.0")
    @Excel(name = "实际工作量")
    private BigDecimal actualEffort;

    /**
     * 父工作项ID
     */
    @ApiModelProperty(value = "父工作项ID", example = "500")
    @Excel(name = "父工作项ID")
    private Long parentWorkItemId;

    /**
     * 相关需求ID
     */
    @ApiModelProperty(value = "相关需求ID", example = "600")
    @Excel(name = "相关需求ID")
    private Long relatedRequirementId;

    /**
     * 相关缺陷ID
     */
    @ApiModelProperty(value = "相关缺陷ID", example = "700")
    @Excel(name = "相关缺陷ID")
    private Long relatedDefectId;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty(value = "删除标志", allowableValues = "0, 2", example = "0")
    private String delFlag;

    public void setWorkItemId(Long workItemId) {
        this.workItemId = workItemId;
    }

    public Long getWorkItemId() {
        return workItemId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setIterationId(Long iterationId) {
        this.iterationId = iterationId;
    }

    public Long getIterationId() {
        return iterationId;
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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
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

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setEstimatedEffort(BigDecimal estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
    }

    public BigDecimal getEstimatedEffort() {
        return estimatedEffort;
    }

    public void setActualEffort(BigDecimal actualEffort) {
        this.actualEffort = actualEffort;
    }

    public BigDecimal getActualEffort() {
        return actualEffort;
    }

    public void setParentWorkItemId(Long parentWorkItemId) {
        this.parentWorkItemId = parentWorkItemId;
    }

    public Long getParentWorkItemId() {
        return parentWorkItemId;
    }

    public void setRelatedRequirementId(Long relatedRequirementId) {
        this.relatedRequirementId = relatedRequirementId;
    }

    public Long getRelatedRequirementId() {
        return relatedRequirementId;
    }

    public void setRelatedDefectId(Long relatedDefectId) {
        this.relatedDefectId = relatedDefectId;
    }

    public Long getRelatedDefectId() {
        return relatedDefectId;
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
                .append("workItemId", getWorkItemId())
                .append("projectId", getProjectId())
                .append("iterationId", getIterationId())
                .append("title", getTitle())
                .append("description", getDescription())
                .append("type", getType())
                .append("status", getStatus())
                .append("priority", getPriority())
                .append("assignedTo", getAssignedTo())
                .append("startDate", getStartDate())
                .append("dueDate", getDueDate())
                .append("completedDate", getCompletedDate())
                .append("estimatedEffort", getEstimatedEffort())
                .append("actualEffort", getActualEffort())
                .append("parentWorkItemId", getParentWorkItemId())
                .append("relatedRequirementId", getRelatedRequirementId())
                .append("relatedDefectId", getRelatedDefectId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
