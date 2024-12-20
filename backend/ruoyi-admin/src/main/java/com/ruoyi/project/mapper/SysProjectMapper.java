package com.ruoyi.project.mapper;

import java.util.List;

import com.ruoyi.project.domain.SysProject;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目 Mapper接口
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-14
 */
@Mapper
public interface SysProjectMapper {
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
     * @return 结果
     */
    public int insertSysProject(SysProject sysProject);

    /**
     * 修改项目
     *
     * @param sysProject 项目
     * @return 结果
     */
    public int updateSysProject(SysProject sysProject);

    /**
     * 删除项目
     *
     * @param projectId 项目主键
     * @return 结果
     */
    public int deleteSysProjectByProjectId(Long projectId);

    /**
     * 批量删除项目
     *
     * @param projectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysProjectByProjectIds(Long[] projectIds);

    /**
     * 根据项目ID列表查询项目列表
     *
     * @param projectIds 项目ID列表
     * @return 项目列表
     */
    List<SysProject> selectProjectsByIds(List<Long> projectIds);

}
