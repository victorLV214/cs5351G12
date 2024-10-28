package com.ruoyi.iteration.mapper;

import java.util.List;
import com.ruoyi.iteration.domain.SysIteration;
import org.apache.ibatis.annotations.Mapper;

/**
 * 迭代Mapper接口
 * 
 * @author ruoyi
 * @date 2024-10-27
 */
@Mapper
public interface SysIterationMapper 
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
     * 删除迭代
     * 
     * @param iterationId 迭代主键
     * @return 结果
     */
    public int deleteSysIterationByIterationId(Long iterationId);

    /**
     * 批量删除迭代
     * 
     * @param iterationIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysIterationByIterationIds(Long[] iterationIds);
}