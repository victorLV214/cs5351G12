package com.ruoyi.notice.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.notice.mapper.SysUserNoticeMapper;
import com.ruoyi.notice.domain.SysUserNotice;
import com.ruoyi.notice.service.ISysUserNoticeService;

/**
 * 通知公告_用户Service业务层处理
 *
 * @author ruoyi
 * @date 2024-10-28
 */
@Service
public class SysUserNoticeServiceImpl implements ISysUserNoticeService
{
    @Autowired
    private SysUserNoticeMapper sysUserNoticeMapper;

    /**
     * 查询通知公告列表
     *
     * @param sysUserNotice 通知公告
     * @return 通知公告
     */
    @Override
    public List<SysUserNotice> selectSysUserNoticeList(SysUserNotice sysUserNotice)
    {
        return sysUserNoticeMapper.selectSysUserNoticeList(sysUserNotice);
    }


    /**
     * 新增通知公告
     *
     * @param sysUserNotice 通知公告
     * @return 结果
     */
    @Override
    public int insertSysUserNotice(SysUserNotice sysUserNotice)
    {
        sysUserNotice.setCreateTime(DateUtils.getNowDate());
        return sysUserNoticeMapper.insertSysUserNotice(sysUserNotice);
    }

    /**
     * 修改通知公告
     *
     * @param sysUserNotice 通知公告
     * @return 结果
     */
    @Override
    public int updateSysUserNotice(SysUserNotice sysUserNotice)
    {
        return sysUserNoticeMapper.updateSysUserNotice(sysUserNotice);
    }


    /**
     * 批量插入通知公告信息
     */
    @Override
    public int insertBatch(List<SysUserNotice> records){
        return sysUserNoticeMapper.insertBatch(records);
    }
}
