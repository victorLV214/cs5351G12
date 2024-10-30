package com.ruoyi.requirement.mapper;

import java.util.List;
import com.ruoyi.requirement.domain.SysRequirement;
import org.apache.ibatis.annotations.Mapper;

/**
 * 需求Mapper接口
 *
 * @author sarek
 * @date 2024-10-14
 */
@Mapper
public interface SysRequirementMapper 
{
    /**
     * 查询需求
     * 
     * @param requirementId 需求主键
     * @return 需求
     */
    public SysRequirement selectSysRequirementByRequirementId(Long requirementId);

    /**
     * 查询需求列表
     * 
     * @param sysRequirement 需求
     * @return 需求集合
     */
    public List<SysRequirement> selectSysRequirementList(SysRequirement sysRequirement);

    /**
     * 新增需求
     * 
     * @param sysRequirement 需求
     * @return 结果
     */
    public int insertSysRequirement(SysRequirement sysRequirement);

    /**
     * 修改需求
     * 
     * @param sysRequirement 需求
     * @return 结果
     */
    public int updateSysRequirement(SysRequirement sysRequirement);

    /**
     * 删除需求
     * 
     * @param requirementId 需求主键
     * @return 结果
     */
    public int deleteSysRequirementByRequirementId(Long requirementId);

    /**
     * 批量删除需求
     * 
     * @param requirementIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysRequirementByRequirementIds(Long[] requirementIds);

    /**
     * 查询项目中的需求总数
     *
     * @param projectId 项目ID
     * @return 需求总数
     */
    public int countRequirementsByProjectId(Long projectId);

    /**
     * 查询项目中已完成的需求数量
     *
     * @param projectId 项目ID
     * @return 已完成的需求数量
     */
    public int countCompletedRequirementsByProjectId(Long projectId);
}
