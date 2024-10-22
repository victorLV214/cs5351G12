package com.ruoyi.requirement.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.requirement.mapper.SysRequirementMapper;
import com.ruoyi.requirement.domain.SysRequirement;
import com.ruoyi.requirement.service.ISysRequirementService;

/**
 * 需求Service业务层处理
 *
 * @author sarek
 * @date 2024-10-14
 */
@Service
public class SysRequirementServiceImpl implements ISysRequirementService 
{
    @Autowired
    private SysRequirementMapper sysRequirementMapper;

    /**
     * 查询需求
     * 
     * @param requirementId 需求主键
     * @return 需求
     */
    @Override
    public SysRequirement selectSysRequirementByRequirementId(Long requirementId)
    {
        return sysRequirementMapper.selectSysRequirementByRequirementId(requirementId);
    }

    /**
     * 查询需求列表
     * 
     * @param sysRequirement 需求
     * @return 需求
     */
    @Override
    public List<SysRequirement> selectSysRequirementList(SysRequirement sysRequirement)
    {
        return sysRequirementMapper.selectSysRequirementList(sysRequirement);
    }

    /**
     * 新增需求
     * 
     * @param sysRequirement 需求
     * @return 结果
     */
    @Override
    public int insertSysRequirement(SysRequirement sysRequirement)
    {
        sysRequirement.setCreateTime(DateUtils.getNowDate());
        return sysRequirementMapper.insertSysRequirement(sysRequirement);
    }

    /**
     * 修改需求
     * 
     * @param sysRequirement 需求
     * @return 结果
     */
    @Override
    public int updateSysRequirement(SysRequirement sysRequirement)
    {
        sysRequirement.setUpdateTime(DateUtils.getNowDate());
        return sysRequirementMapper.updateSysRequirement(sysRequirement);
    }

    /**
     * 批量删除需求
     * 
     * @param requirementIds 需要删除的需求主键
     * @return 结果
     */
    @Override
    public int deleteSysRequirementByRequirementIds(Long[] requirementIds)
    {
        return sysRequirementMapper.deleteSysRequirementByRequirementIds(requirementIds);
    }

    /**
     * 删除需求信息
     * 
     * @param requirementId 需求主键
     * @return 结果
     */
    @Override
    public int deleteSysRequirementByRequirementId(Long requirementId)
    {
        return sysRequirementMapper.deleteSysRequirementByRequirementId(requirementId);
    }
}
