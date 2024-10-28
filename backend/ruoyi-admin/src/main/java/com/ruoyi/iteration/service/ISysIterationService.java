package com.ruoyi.iteration.service;

import java.util.List;
import com.ruoyi.iteration.domain.SysIteration;

/**
 * 迭代Service接口
 * 
 * @author sarek
 * @date 2024-10-27
 */
public interface ISysIterationService 
{
    /**
     * 查询迭代
     * 
     * @param iterationId 迭代主键
     * @return 迭代
     */
    public SysIteration selectSysIterationByIterationId(Long iterationId);

    /**
     * 查询迭代列表
     * 
     * @param sysIteration 迭代
     * @return 迭代集合
     */
    public List<SysIteration> selectSysIterationList(SysIteration sysIteration);

    /**
     * 新增迭代
     * 
     * @param sysIteration 迭代
     * @return 结果
     */
    public int insertSysIteration(SysIteration sysIteration);

    /**
     * 修改迭代
     * 
     * @param sysIteration 迭代
     * @return 结果
     */
    public int updateSysIteration(SysIteration sysIteration);

    /**
     * 批量删除迭代
     * 
     * @param iterationIds 需要删除的迭代主键集合
     * @return 结果
     */
    public int deleteSysIterationByIterationIds(Long[] iterationIds);

    /**
     * 删除迭代信息
     * 
     * @param iterationId 迭代主键
     * @return 结果
     */
    public int deleteSysIterationByIterationId(Long iterationId);
}
