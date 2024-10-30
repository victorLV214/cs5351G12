package com.ruoyi.dashboard.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.dashboard.domain.SysDashboard;
import com.ruoyi.dashboard.service.ISysDashboardService;
import com.ruoyi.member.mapper.SysProjectMemberMapper;
import com.ruoyi.project.domain.SysProject;
import com.ruoyi.project.mapper.SysProjectMapper;
import com.ruoyi.task.domain.SysTask;
import com.ruoyi.task.mapper.SysTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 仪表盘 Service业务层处理
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-31
 */
@Service
public class SysDashboardServiceImpl implements ISysDashboardService {

    @Autowired
    private SysProjectMemberMapper sysProjectMemberMapper;

    @Autowired
    private SysProjectMapper sysProjectMapper;

    @Autowired
    private SysTaskMapper sysTaskMapper;

    @Override
    public SysDashboard getDashboardData() {
        Long userId = SecurityUtils.getUserId();
        SysDashboard sysDashboard = new SysDashboard();

        // 获取用户参与的项目ID列表
        List<Long> projectIds = sysProjectMemberMapper.selectProjectIdsByUserId(userId);

        if (projectIds != null && !projectIds.isEmpty()) {
            // 获取项目列表
            List<SysProject> projects = sysProjectMapper.selectProjectsByIds(projectIds);

            // 根据项目状态分类
            List<SysProject> plannedProjects = projects.stream()
                    .filter(p -> "规划中".equals(p.getStatus()))
                    .collect(Collectors.toList());

            List<SysProject> ongoingProjects = projects.stream()
                    .filter(p -> "进行中".equals(p.getStatus()))
                    .collect(Collectors.toList());

            List<SysProject> completedProjects = projects.stream()
                    .filter(p -> "已完成".equals(p.getStatus()))
                    .collect(Collectors.toList());

            sysDashboard.setPlannedProjects(plannedProjects);
            sysDashboard.setOngoingProjects(ongoingProjects);
            sysDashboard.setCompletedProjects(completedProjects);
        }

        // 获取用户的任务列表
        List<SysTask> tasks = sysTaskMapper.selectTasksByUserId(userId);

        if (tasks != null && !tasks.isEmpty()) {
            // 根据任务状态分类
            List<SysTask> ongoingTasks = tasks.stream()
                    .filter(t -> "进行中".equals(t.getStatus()))
                    .collect(Collectors.toList());

            List<SysTask> completedTasks = tasks.stream()
                    .filter(t -> "已完成".equals(t.getStatus()))
                    .collect(Collectors.toList());

            sysDashboard.setOngoingTasks(ongoingTasks);
            sysDashboard.setCompletedTasks(completedTasks);
        }

        return sysDashboard;
    }
}
