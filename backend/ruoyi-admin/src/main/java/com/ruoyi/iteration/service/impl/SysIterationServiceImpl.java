package com.ruoyi.iteration.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.iteration.mapper.SysIterationMapper;
import com.ruoyi.iteration.domain.SysIteration;
import com.ruoyi.iteration.service.ISysIterationService;

/**
 * 迭代Service业务层处理
 * 
 * @author sarek
 * @date 2024-10-27
 */
@Service
public class SysIterationServiceImpl implements ISysIterationService 
{
    @Autowired
    private SysIterationMapper sysIterationMapper;

    /**
     * 查询迭代
     * 
     * @param iterationId 迭代主键
     * @return 迭代
     */
    @Override
    public SysIteration selectSysIterationByIterationId(Long iterationId)
    {
        return sysIterationMapper.selectSysIterationByIterationId(iterationId);
    }

    /**
     * 查询迭代列表
     * 
     * @param sysIteration 迭代
     * @return 迭代
     */
    @Override
    public List<SysIteration> selectSysIterationList(SysIteration sysIteration)
    {
        return sysIterationMapper.selectSysIterationList(sysIteration);
    }

    /**
     * 新增迭代
     * 
     * @param sysIteration 迭代
     * @return 结果
     */
    @Override
    public int insertSysIteration(SysIteration sysIteration)
    {
        sysIteration.setCreateTime(DateUtils.getNowDate());
        return sysIterationMapper.insertSysIteration(sysIteration);
    }

    /**
     * 修改迭代
     * 
     * @param sysIteration 迭代
     * @return 结果
     */
    @Override
    public int updateSysIteration(SysIteration sysIteration)
    {
        sysIteration.setUpdateTime(DateUtils.getNowDate());
        return sysIterationMapper.updateSysIteration(sysIteration);
    }

    /**
     * 批量删除迭代
     * 
     * @param iterationIds 需要删除的迭代主键
     * @return 结果
     */
    @Override
    public int deleteSysIterationByIterationIds(Long[] iterationIds)
    {
        return sysIterationMapper.deleteSysIterationByIterationIds(iterationIds);
    }

    /**
     * 删除迭代信息
     * 
     * @param iterationId 迭代主键
     * @return 结果
     */
    @Override
    public int deleteSysIterationByIterationId(Long iterationId)
    {
        return sysIterationMapper.deleteSysIterationByIterationId(iterationId);
    }
}
