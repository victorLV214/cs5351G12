package com.ruoyi.item.service;

import java.util.List;

import com.ruoyi.item.domain.SysWorkItem;

/**
 * 工作项 Service接口
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-20
 */
public interface ISysWorkItemService {
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
     * @return sysWorkItem.id
     */
    public Long insertSysWorkItem(SysWorkItem sysWorkItem);

    /**
     * 修改工作项
     *
     * @param sysWorkItem 工作项
     * @return sysWorkItem.id
     */
    public Long updateSysWorkItem(SysWorkItem sysWorkItem);

    /**
     * 批量删除工作项
     *
     * @param workItemIds 需要删除的工作项主键集合
     * @return 结果
     */
    public int deleteSysWorkItemByWorkItemIds(Long[] workItemIds);

    /**
     * 删除工作项信息
     *
     * @param workItemId 工作项主键
     * @return 结果
     */
    public int deleteSysWorkItemByWorkItemId(Long workItemId);
}
