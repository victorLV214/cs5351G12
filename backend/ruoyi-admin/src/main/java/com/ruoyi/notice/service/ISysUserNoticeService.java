package com.ruoyi.notice.service;

import java.util.List;
import com.ruoyi.notice.domain.SysUserNotice;

/**
 * 通知公告Service接口
 *
 * @author ruoyi
 * @date 2024-10-28
 */
public interface ISysUserNoticeService
{

    /**
     * 查询通知公告列表
     *
     * @param sysUserNotice 通知公告
     * @return 通知公告集合
     */
    public List<SysUserNotice> selectSysUserNoticeList(SysUserNotice sysUserNotice);

    /**
     * 新增通知公告
     *
     * @param sysUserNotice 通知公告
     * @return 结果
     */
    public int insertSysUserNotice(SysUserNotice sysUserNotice);

    /**
     * 更改通知公告阅读状态
     *
     * @param sysUserNotice 通知公告
     * @return 结果
     */
    public int updateSysUserNotice(SysUserNotice sysUserNotice);

    /**
     * 批量新增
     * @param list
     * @return
     */
    public int insertBatch(List<SysUserNotice> list);

}
