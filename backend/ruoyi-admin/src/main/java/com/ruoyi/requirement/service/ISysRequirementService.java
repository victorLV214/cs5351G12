package com.ruoyi.requirement.service;

import java.util.List;
import com.ruoyi.requirement.domain.SysRequirement;

/**
 * 需求Service接口
 *
 * @author sarek
 * @date 2024-10-14
 */
public interface ISysRequirementService 
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
     * 批量删除需求
     * 
     * @param requirementIds 需要删除的需求主键集合
     * @return 结果
     */
    public int deleteSysRequirementByRequirementIds(Long[] requirementIds);

    /**
     * 删除需求信息
     * 
     * @param requirementId 需求主键
     * @return 结果
     */
    public int deleteSysRequirementByRequirementId(Long requirementId);
}
