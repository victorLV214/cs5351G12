package com.ruoyi.project.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.mapper.SysProjectMapper;
import com.ruoyi.project.domain.SysProject;
import com.ruoyi.project.service.ISysProjectService;

/**
 * 项目Service业务层处理
 * 
 * @author AB-IN-LIUSY
 * @date 2024-10-14
 */
@Service
public class SysProjectServiceImpl implements ISysProjectService 
{
    @Autowired
    private SysProjectMapper sysProjectMapper;

    /**
     * 查询项目
     * 
     * @param projectId 项目主键
     * @return 项目
     */
    @Override
    public SysProject selectSysProjectByProjectId(Long projectId)
    {
        return sysProjectMapper.selectSysProjectByProjectId(projectId);
    }

    /**
     * 查询项目列表
     * 
     * @param sysProject 项目
     * @return 项目
     */
    @Override
    public List<SysProject> selectSysProjectList(SysProject sysProject)
    {
        return sysProjectMapper.selectSysProjectList(sysProject);
    }

    /**
     * 新增项目
     * 
     * @param sysProject 项目
     * @return 结果
     */
    @Override
    public Long insertSysProject(SysProject sysProject)
    {
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
     * @return 结果
     */
    @Override
    public Long updateSysProject(SysProject sysProject)
    {
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
    public int deleteSysProjectByProjectIds(Long[] projectIds)
    {
        return sysProjectMapper.deleteSysProjectByProjectIds(projectIds);
    }

    /**
     * 删除项目信息
     * 
     * @param projectId 项目主键
     * @return 结果
     */
    @Override
    public int deleteSysProjectByProjectId(Long projectId)
    {
        return sysProjectMapper.deleteSysProjectByProjectId(projectId);
    }
}
