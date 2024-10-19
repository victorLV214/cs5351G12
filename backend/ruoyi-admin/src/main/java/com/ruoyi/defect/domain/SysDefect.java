package com.ruoyi.defect.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 缺陷对象 sys_defect
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-17
 */
@ApiModel("缺陷实体")
public class SysDefect extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 缺陷ID
     */
    @ApiModelProperty(value = "缺陷ID", example = "1001")
    private Long defectId;

    /**
     * 项目ID
     */
    @Excel(name = "项目ID")
    @ApiModelProperty(value = "项目ID", example = "2001")
    private Long projectId;

    /**
     * 缺陷标题
     */
    @Excel(name = "缺陷标题")
    @ApiModelProperty(value = "缺陷标题", example = "界面加载异常")
    private String title;

    /**
     * 缺陷详细描述
     */
    @Excel(name = "缺陷详细描述")
    @ApiModelProperty(value = "缺陷详细描述", example = "在用户登录后，首页界面无法正常加载，显示空白。")
    private String description;

    /**
     * 严重程度（1低 2中 3高 4严重）
     */
    @Excel(name = "严重程度", readConverterExp = "1=低,2=中,3=高,4=严重")
    @ApiModelProperty(value = "严重程度（1低 2中 3高 4严重）", example = "3")
    private Integer severity;

    /**
     * 优先级（1低 2中 3高 4紧急）
     */
    @Excel(name = "优先级", readConverterExp = "1=低,2=中,3=高,4=紧急")
    @ApiModelProperty(value = "优先级（1低 2中 3高 4紧急）", example = "4")
    private Integer priority;

    /**
     * 缺陷状态
     */
    @Excel(name = "缺陷状态")
    @ApiModelProperty(value = "缺陷状态", example = "已修复")
    private String status;

    /**
     * 报告人ID
     */
    @Excel(name = "报告人ID")
    @ApiModelProperty(value = "报告人ID", example = "3001")
    private Long reportedBy;

    /**
     * 负责人ID
     */
    @Excel(name = "负责人ID")
    @ApiModelProperty(value = "负责人ID", example = "4001")
    private Long assignedTo;

    /**
     * 解决时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "解决时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "解决时间", example = "2024-10-15")
    private Date resolvedAt;

    /**
     * 关闭时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "关闭时间", width = 30, dateFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "关闭时间", example = "2024-10-16")
    private Date closedAt;

    /**
     * 预期修复版本
     */
    @Excel(name = "预期修复版本")
    @ApiModelProperty(value = "预期修复版本", example = "v1.1.0")
    private String expectedFixVersion;

    /**
     * 实际修复版本
     */
    @Excel(name = "实际修复版本")
    @ApiModelProperty(value = "实际修复版本", example = "v1.0.9")
    private String actualFixVersion;

    /**
     * 重现步骤
     */
    @Excel(name = "重现步骤")
    @ApiModelProperty(value = "重现步骤", example = "1. 登录系统 2. 进入首页 3. 观察页面加载")
    private String reproductionSteps;

    /**
     * 环境信息
     */
    @Excel(name = "环境信息")
    @ApiModelProperty(value = "环境信息", example = "Windows 10, Chrome 89")
    private String environment;

    /**
     * 相关需求ID
     */
    @Excel(name = "相关需求ID")
    @ApiModelProperty(value = "相关需求ID", example = "5001")
    private Long relatedRequirementId;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）", example = "0")
    private String delFlag;

    public void setDefectId(Long defectId) {
        this.defectId = defectId;
    }

    public Long getDefectId() {
        return defectId;
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

    public void setSeverity(Integer severity) {
        this.severity = severity;
    }

    public Integer getSeverity() {
        return severity;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setReportedBy(Long reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Long getReportedBy() {
        return reportedBy;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setResolvedAt(Date resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public Date getResolvedAt() {
        return resolvedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setExpectedFixVersion(String expectedFixVersion) {
        this.expectedFixVersion = expectedFixVersion;
    }

    public String getExpectedFixVersion() {
        return expectedFixVersion;
    }

    public void setActualFixVersion(String actualFixVersion) {
        this.actualFixVersion = actualFixVersion;
    }

    public String getActualFixVersion() {
        return actualFixVersion;
    }

    public void setReproductionSteps(String reproductionSteps) {
        this.reproductionSteps = reproductionSteps;
    }

    public String getReproductionSteps() {
        return reproductionSteps;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setRelatedRequirementId(Long relatedRequirementId) {
        this.relatedRequirementId = relatedRequirementId;
    }

    public Long getRelatedRequirementId() {
        return relatedRequirementId;
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
                .append("defectId", getDefectId())
                .append("projectId", getProjectId())
                .append("title", getTitle())
                .append("description", getDescription())
                .append("severity", getSeverity())
                .append("priority", getPriority())
                .append("status", getStatus())
                .append("reportedBy", getReportedBy())
                .append("assignedTo", getAssignedTo())
                .append("resolvedAt", getResolvedAt())
                .append("closedAt", getClosedAt())
                .append("expectedFixVersion", getExpectedFixVersion())
                .append("actualFixVersion", getActualFixVersion())
                .append("reproductionSteps", getReproductionSteps())
                .append("environment", getEnvironment())
                .append("relatedRequirementId", getRelatedRequirementId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("delFlag", getDelFlag())
                .toString();
    }
}
