package com.ruoyi.dashboard.domain;

import com.ruoyi.project.domain.SysProject;
import com.ruoyi.task.domain.SysTask;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 仪表盘对象 sys_dashboard
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-31
 */
@ApiModel("仪表盘数据")
public class SysDashboard {

    @ApiModelProperty("规划中的项目列表")
    private List<SysProject> plannedProjects;

    @ApiModelProperty("进行中的项目列表")
    private List<SysProject> ongoingProjects;

    @ApiModelProperty("已完成的项目列表")
    private List<SysProject> completedProjects;

    @ApiModelProperty("进行中的任务列表")
    private List<SysTask> ongoingTasks;

    @ApiModelProperty("已完成的任务列表")
    private List<SysTask> completedTasks;

    // Getter 和 Setter 方法

    public List<SysProject> getPlannedProjects() {
        return plannedProjects;
    }

    public void setPlannedProjects(List<SysProject> plannedProjects) {
        this.plannedProjects = plannedProjects;
    }

    public List<SysProject> getOngoingProjects() {
        return ongoingProjects;
    }

    public void setOngoingProjects(List<SysProject> ongoingProjects) {
        this.ongoingProjects = ongoingProjects;
    }

    public List<SysProject> getCompletedProjects() {
        return completedProjects;
    }

    public void setCompletedProjects(List<SysProject> completedProjects) {
        this.completedProjects = completedProjects;
    }

    public List<SysTask> getOngoingTasks() {
        return ongoingTasks;
    }

    public void setOngoingTasks(List<SysTask> ongoingTasks) {
        this.ongoingTasks = ongoingTasks;
    }

    public List<SysTask> getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(List<SysTask> completedTasks) {
        this.completedTasks = completedTasks;
    }
}