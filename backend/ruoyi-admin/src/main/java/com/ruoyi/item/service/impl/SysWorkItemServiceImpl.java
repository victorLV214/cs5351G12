package com.ruoyi.item.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.item.mapper.SysWorkItemMapper;
import com.ruoyi.item.domain.SysWorkItem;
import com.ruoyi.item.service.ISysWorkItemService;

/**
 * 工作项 Service业务层处理
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-20
 */
@Service
public class SysWorkItemServiceImpl implements ISysWorkItemService {
    @Autowired
    private SysWorkItemMapper sysWorkItemMapper;

    /**
     * 查询工作项
     *
     * @param workItemId 工作项主键
     * @return 工作项
     */
    @Override
    public SysWorkItem selectSysWorkItemByWorkItemId(Long workItemId) {
        return sysWorkItemMapper.selectSysWorkItemByWorkItemId(workItemId);
    }

    /**
     * 查询工作项列表
     *
     * @param sysWorkItem 工作项
     * @return 工作项
     */
    @Override
    public List<SysWorkItem> selectSysWorkItemList(SysWorkItem sysWorkItem) {
        return sysWorkItemMapper.selectSysWorkItemList(sysWorkItem);
    }

    /**
     * 新增工作项
     *
     * @param sysWorkItem 工作项
     * @return sysWorkItem.id
     */
    @Override
    public Long insertSysWorkItem(SysWorkItem sysWorkItem) {
        sysWorkItem.setCreateTime(DateUtils.getNowDate());
        sysWorkItem.setUpdateTime(DateUtils.getNowDate());
        sysWorkItem.setDelFlag("0"); // 存在

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysWorkItem.setCreateBy(username);

        int result = sysWorkItemMapper.insertSysWorkItem(sysWorkItem);
        return result > 0 ? sysWorkItem.getWorkItemId() : result;
    }

    /**
     * 修改工作项
     *
     * @param sysWorkItem 工作项
     * @return sysWorkItem.id
     */
    @Override
    public Long updateSysWorkItem(SysWorkItem sysWorkItem) {
        sysWorkItem.setUpdateTime(DateUtils.getNowDate());

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysWorkItem.setUpdateBy(username);

        int result = sysWorkItemMapper.updateSysWorkItem(sysWorkItem);
        return result > 0 ? sysWorkItem.getWorkItemId() : result;
    }

    /**
     * 批量删除工作项
     *
     * @param workItemIds 需要删除的工作项主键
     * @return 结果
     */
    @Override
    public int deleteSysWorkItemByWorkItemIds(Long[] workItemIds) {
        return sysWorkItemMapper.deleteSysWorkItemByWorkItemIds(workItemIds);
    }

    /**
     * 删除工作项信息
     *
     * @param workItemId 工作项主键
     * @return 结果
     */
    @Override
    public int deleteSysWorkItemByWorkItemId(Long workItemId) {
        return sysWorkItemMapper.deleteSysWorkItemByWorkItemId(workItemId);
    }
}
