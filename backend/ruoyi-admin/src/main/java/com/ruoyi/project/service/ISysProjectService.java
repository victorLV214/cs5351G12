package com.ruoyi.project.service;

import java.util.List;
import com.ruoyi.project.domain.SysProject;

/**
 * 项目 Service接口
 * 
 * @author AB-IN-LIUSY
 * @date 2024-10-14
 */
public interface ISysProjectService 
{
    /**
     * 查询项目
     * 
     * @param projectId 项目主键
     * @return 项目
     */
    public SysProject selectSysProjectByProjectId(Long projectId);

    /**
     * 查询项目列表
     * 
     * @param sysProject 项目
     * @return 项目集合
     */
    public List<SysProject> selectSysProjectList(SysProject sysProject);

    /**
     * 新增项目
     * 
     * @param sysProject 项目
     * @return sysProject.id
     */
    public Long insertSysProject(SysProject sysProject);

    /**
     * 修改项目
     * 
     * @param sysProject 项目
     * @return sysProject.id
     */
    public Long updateSysProject(SysProject sysProject);

    /**
     * 批量删除项目
     * 
     * @param projectIds 需要删除的项目主键集合
     * @return 结果
     */
    public int deleteSysProjectByProjectIds(Long[] projectIds);

    /**
     * 删除项目信息
     * 
     * @param projectId 项目主键
     * @return 结果
     */
    public int deleteSysProjectByProjectId(Long projectId);
}
