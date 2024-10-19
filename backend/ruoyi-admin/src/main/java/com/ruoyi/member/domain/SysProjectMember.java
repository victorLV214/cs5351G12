package com.ruoyi.member.domain;

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
 * 项目成员对象 sys_project_member
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-18
 */
@ApiModel("项目成员实体")
public class SysProjectMember extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 项目成员ID
     */
    @ApiModelProperty(value = "项目成员ID", example = "1")
    private Long projectMemberId;

    /**
     * 项目ID
     */
    @ApiModelProperty(value = "项目ID", example = "1001")
    @Excel(name = "项目ID")
    private Long projectId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "5001")
    @Excel(name = "用户ID")
    private Long userId;

    /**
     * 成员角色
     */
    @ApiModelProperty(value = "成员角色", example = "开发人员")
    @Excel(name = "成员角色")
    private String role;

    /**
     * 加入日期
     */
    @ApiModelProperty(value = "加入日期", example = "2024-01-01")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "加入日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinDate;

    /**
     * 离开日期
     */
    @ApiModelProperty(value = "离开日期", example = "2024-12-31")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "离开日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date leaveDate;

    /**
     * 是否活跃（0否 1是）
     */
    @ApiModelProperty(value = "是否活跃", example = "1", notes = "0表示否，1表示是")
    @Excel(name = "是否活跃", readConverterExp = "0=否,1=是")
    private Integer isActive;

    /**
     * 具体权限
     */
    @ApiModelProperty(value = "具体权限", example = "read,write")
    @Excel(name = "具体权限")
    private String permissions;

    /**
     * 时间分配百分比
     */
    @ApiModelProperty(value = "时间分配百分比", example = "50.0")
    @Excel(name = "时间分配百分比")
    private BigDecimal allocationPercentage;

    /**
     * 额外说明
     */
    @ApiModelProperty(value = "额外说明", example = "该成员为兼职")
    @Excel(name = "额外说明")
    private String notes;

    public void setProjectMemberId(Long projectMemberId) {
        this.projectMemberId = projectMemberId;
    }

    public Long getProjectMemberId() {
        return projectMemberId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setAllocationPercentage(BigDecimal allocationPercentage) {
        this.allocationPercentage = allocationPercentage;
    }

    public BigDecimal getAllocationPercentage() {
        return allocationPercentage;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("projectMemberId", getProjectMemberId())
                .append("projectId", getProjectId())
                .append("userId", getUserId())
                .append("role", getRole())
                .append("joinDate", getJoinDate())
                .append("leaveDate", getLeaveDate())
                .append("isActive", getIsActive())
                .append("permissions", getPermissions())
                .append("allocationPercentage", getAllocationPercentage())
                .append("notes", getNotes())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
