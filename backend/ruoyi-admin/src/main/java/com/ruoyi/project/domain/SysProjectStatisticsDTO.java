package com.ruoyi.project.domain;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

@ApiModel("项目统计信息")
public class SysProjectStatisticsDTO extends SysProject {

    private static final long serialVersionUID = 1L;

    /** 总需求数 */
    @ApiModelProperty(value = "总需求数", example = "100")
    private Integer totalRequirements;

    /** 已完成的需求数 */
    @ApiModelProperty(value = "已完成的需求数", example = "80")
    private Integer completedRequirements;

    /** 总缺陷数 */
    @ApiModelProperty(value = "总缺陷数", example = "50")
    private Integer totalDefects;

    /** 已解决的缺陷数 */
    @ApiModelProperty(value = "已解决的缺陷数", example = "45")
    private Integer completedDefects;

    /** 总任务数 */
    @ApiModelProperty(value = "总任务数", example = "200")
    private Integer totalTasks;

    /** 已完成的任务数 */
    @ApiModelProperty(value = "已完成的任务数", example = "180")
    private Integer completedTasks;

    /** 成员数量 */
    @ApiModelProperty(value = "成员数量", example = "10")
    private Integer memberCount;

    /** 迭代数量 */
    @ApiModelProperty(value = "迭代数量", example = "5")
    private Integer iterationCount;

    /** 当前迭代进度 */
    @ApiModelProperty(value = "当前迭代进度", example = "75.5")
    private BigDecimal currentIterationProgress;

    public Integer getTotalDefects() {
        return totalDefects;
    }

    public void setTotalDefects(Integer totalDefects) {
        this.totalDefects = totalDefects;
    }

    public Integer getTotalRequirements() {
        return totalRequirements;
    }

    public void setTotalRequirements(Integer totalRequirements) {
        this.totalRequirements = totalRequirements;
    }

    public Integer getCompletedRequirements() {
        return completedRequirements;
    }

    public void setCompletedRequirements(Integer completedRequirements) {
        this.completedRequirements = completedRequirements;
    }

    public Integer getCompletedDefects() {
        return completedDefects;
    }

    public void setCompletedDefects(Integer completedDefects) {
        this.completedDefects = completedDefects;
    }

    public Integer getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Integer totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Integer getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Integer completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Integer getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    public Integer getIterationCount() {
        return iterationCount;
    }

    public void setIterationCount(Integer iterationCount) {
        this.iterationCount = iterationCount;
    }

    public BigDecimal getCurrentIterationProgress() {
        return currentIterationProgress;
    }

    public void setCurrentIterationProgress(BigDecimal currentIterationProgress) {
        this.currentIterationProgress = currentIterationProgress;
    }
}
