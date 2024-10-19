package com.ruoyi.item.mapper;

import java.util.List;

import com.ruoyi.item.domain.SysWorkItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工作项 Mapper接口
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-20
 */
@Mapper
public interface SysWorkItemMapper {
    /**
     * 查询工作项
     *
     * @param workItemId 工作项主键
     * @return 工作项
     */
    public SysWorkItem selectSysWorkItemByWorkItemId(Long workItemId);

    /**
     * 查询工作项列表
     *
     * @param sysWorkItem 工作项
     * @return 工作项集合
     */
    public List<SysWorkItem> selectSysWorkItemList(SysWorkItem sysWorkItem);

    /**
     * 新增工作项
     *
     * @param sysWorkItem 工作项
     * @return 结果
     */
    public int insertSysWorkItem(SysWorkItem sysWorkItem);

    /**
     * 修改工作项
     *
     * @param sysWorkItem 工作项
     * @return 结果
     */
    public int updateSysWorkItem(SysWorkItem sysWorkItem);

    /**
     * 删除工作项
     *
     * @param workItemId 工作项主键
     * @return 结果
     */
    public int deleteSysWorkItemByWorkItemId(Long workItemId);

    /**
     * 批量删除工作项
     *
     * @param workItemIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysWorkItemByWorkItemIds(Long[] workItemIds);
}
