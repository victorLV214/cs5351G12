package com.ruoyi.notice.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.notice.domain.SysUserNotice;
import com.ruoyi.notice.service.ISysUserNoticeService;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 通知公告_用户Controller
 *
 * @author sarek
 * @date 2024-10-28
 */
@RestController
@Api(tags = "用户通知管理")
@RequestMapping("/system/user/notice")
public class SysUserNoticeController extends BaseController
{
    @Autowired
    private ISysUserNoticeService sysUserNoticeService;
    @Autowired
    private ISysNoticeService sysNoticeService;

    /**
     * 查询当前用户收到的通知列表
     */

    @GetMapping("/list")
    @ApiOperation(value = "查询对应用户收到的通知列表")
    public TableDataInfo list(SysNotice sysNotice, @RequestParam Long userId)
    {
        startPage();
        SysUserNotice sysUserNotice = new SysUserNotice();
        sysUserNotice.setUserId(userId);
        List<SysUserNotice> userNotices = sysUserNoticeService.selectSysUserNoticeList(sysUserNotice);

        List<SysNotice> list = new ArrayList<>();
        for(SysUserNotice userNotice : userNotices){
            SysNotice notice = sysNoticeService.selectNoticeById(userNotice.getNoticeId());
            notice.setUserReadStatus(userNotice.getIsRead());
            list.add(notice);
        }

        return getDataTable(list);
    }


    /**
     * 设置用户的通知阅读状态
     * is_read: 0:未读 1:已读
     */
    @Log(title = "通知公告的用户阅读状态", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    @ApiOperation(value = "设置用户的通知阅读状态")
    public AjaxResult setReadStatus(@RequestBody SysUserNotice sysUserNotice)
    {
        // 若为1 则更新为当前时间
        if (sysUserNotice.getIsRead() == 1) {
            sysUserNotice.setReadTime(DateUtils.getNowDate());
        }
        return toAjax(sysUserNoticeService.updateSysUserNotice(sysUserNotice));
    }
}
