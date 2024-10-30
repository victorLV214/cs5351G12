package com.ruoyi.project.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.defect.mapper.SysDefectMapper;
import com.ruoyi.iteration.domain.SysIteration;
import com.ruoyi.iteration.mapper.SysIterationMapper;
import com.ruoyi.member.mapper.SysProjectMemberMapper;
import com.ruoyi.project.domain.SysProjectStatisticsDTO;
import com.ruoyi.requirement.mapper.SysRequirementMapper;
import com.ruoyi.task.mapper.SysTaskMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.mapper.SysProjectMapper;
import com.ruoyi.project.domain.SysProject;
import com.ruoyi.project.service.ISysProjectService;

/**
 * 项目 Service业务层处理
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-14
 */
@Service
public class SysProjectServiceImpl implements ISysProjectService {
    @Autowired
    private SysProjectMapper sysProjectMapper;

    @Autowired
    private SysRequirementMapper sysRequirementMapper;

    @Autowired
    private SysDefectMapper sysDefectMapper;

    @Autowired
    private SysTaskMapper sysTaskMapper;

    @Autowired
    private SysProjectMemberMapper sysProjectMemberMapper;

    @Autowired
    private SysIterationMapper sysIterationMapper;

    /**
     * 查询项目
     *
     * @param projectId 项目主键
     * @return 项目
     */
    @Override
    public SysProject selectSysProjectByProjectId(Long projectId) {
        return sysProjectMapper.selectSysProjectByProjectId(projectId);
    }

    /**
     * 查询项目列表
     *
     * @param sysProject 项目
     * @return 项目
     */
    @Override
    public List<SysProject> selectSysProjectList(SysProject sysProject) {
        return sysProjectMapper.selectSysProjectList(sysProject);
    }

    /**
     * 新增项目
     *
     * @param sysProject 项目
     * @return sysProject.id
     */
    @Override
    public Long insertSysProject(SysProject sysProject) {
        sysProject.setCreateTime(DateUtils.getNowDate());
        sysProject.setUpdateTime(DateUtils.getNowDate());
        sysProject.setDelFlag("0"); // 存在

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysProject.setCreateBy(username);

        int result = sysProjectMapper.insertSysProject(sysProject);
        return result > 0 ? sysProject.getProjectId() : result;
    }

    /**
     * 修改项目
     *
     * @param sysProject 项目
     * @return sysProject.id
     */
    @Override
    public Long updateSysProject(SysProject sysProject) {
        sysProject.setUpdateTime(DateUtils.getNowDate());

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysProject.setUpdateBy(username);

        int result = sysProjectMapper.updateSysProject(sysProject);
        return result > 0 ? sysProject.getProjectId() : result;
    }

    /**
     * 批量删除项目
     *
     * @param projectIds 需要删除的项目主键
     * @return 结果
     */
    @Override
    public int deleteSysProjectByProjectIds(Long[] projectIds) {
        return sysProjectMapper.deleteSysProjectByProjectIds(projectIds);
    }

    /**
     * 删除项目信息
     *
     * @param projectId 项目主键
     * @return 结果
     */
    @Override
    public int deleteSysProjectByProjectId(Long projectId) {
        return sysProjectMapper.deleteSysProjectByProjectId(projectId);
    }

    /**
     * 获取指定项目的统计信息
     *
     * @param projectId 项目ID
     * @return 项目统计信息
     */
    @Override
    public SysProjectStatisticsDTO getProjectStatisticsByProjectId(Long projectId) {
        // 获取项目基本信息
        SysProject project = sysProjectMapper.selectSysProjectByProjectId(projectId);
        // 创建 DTO 对象
        SysProjectStatisticsDTO dto = new SysProjectStatisticsDTO();
        // 将项目基本信息复制到 DTO 中
        BeanUtils.copyProperties(project, dto);

        // 获取需求统计信息
        int totalRequirements = sysRequirementMapper.countRequirementsByProjectId(projectId);
        int completedRequirements = sysRequirementMapper.countCompletedRequirementsByProjectId(projectId);
        dto.setTotalRequirements(totalRequirements);
        dto.setCompletedRequirements(completedRequirements);

        // 获取缺陷统计信息
         int totalDefects = sysDefectMapper.countDefectsByProjectId(projectId);
         int completedDefects = sysDefectMapper.countCompletedDefectsByProjectId(projectId);
         dto.setTotalDefects(totalDefects);
         dto.setCompletedDefects(completedDefects);

        // 获取任务统计信息
        int totalTasks = sysTaskMapper.countTasksByProjectId(projectId);
        int completedTasks = sysTaskMapper.countCompletedTasksByProjectId(projectId);
        dto.setTotalTasks(totalTasks);
        dto.setCompletedTasks(completedTasks);

        // 获取成员数量
        int memberCount = sysProjectMemberMapper.countMembersByProjectId(projectId);
        dto.setMemberCount(memberCount);

        // 获取迭代数量
        int iterationCount = sysIterationMapper.countIterationsByProjectId(projectId);
        dto.setIterationCount(iterationCount);

        BigDecimal currentIterationProgress;
        // 先获取进行中的迭代
        SysIteration inProgressIteration = sysIterationMapper.getInProgressIterationByProjectId(projectId);
        if (inProgressIteration != null) {
            // 如果有进行中的迭代，计算其进度百分比
            Long plannedPoints = inProgressIteration.getPlannedStoryPoints();
            Long completedPoints = inProgressIteration.getCompletedStoryPoints();

            if (plannedPoints != null && plannedPoints > 0 && completedPoints != null) {
                currentIterationProgress = BigDecimal.valueOf(completedPoints)
                        .divide(BigDecimal.valueOf(plannedPoints), 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            } else {
                currentIterationProgress = BigDecimal.ZERO;
            }
        } else {
            // 如果没有任何迭代，进度为0%
            currentIterationProgress = BigDecimal.ZERO;
        }
        dto.setCurrentIterationProgress(currentIterationProgress);

        return dto;
    }
}
