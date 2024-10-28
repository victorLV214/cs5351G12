package com.ruoyi.notice.mapper;

import java.util.List;
import com.ruoyi.notice.domain.SysUserNotice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通知公告_用户Mapper接口
 *
 * @author sarek
 * @date 2024-10-28
 */
@Mapper
public interface SysUserNoticeMapper
{
    /**
     * 查询通知公告
     *
     * @param id 通知公告主键
     * @return 通知公告
     */
    public SysUserNotice selectSysUserNoticeById(Long id);

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
     * 修改通知公告
     *
     * @param sysUserNotice 通知公告
     * @return 结果
     */
    public int updateSysUserNotice(SysUserNotice sysUserNotice);

    /**
     * 批量修改通知公告阅读状态
     *
     * @param userid
     * @param noticeIds
     * @return 结果
     */
    public int updateSysUserNoticeBatch(Long userid, String[] noticeIds);

    /**
     * 批量新增
     *
     * @param list 通知公告用户集合
     * @return  结果
     */
    public int insertBatch(List<SysUserNotice> list);

}
