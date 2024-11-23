package com.ruoyi.web.controller.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.notice.domain.SysNoticeDTO;
import com.ruoyi.notice.domain.SysUserNotice;
import com.ruoyi.notice.service.ISysUserNoticeService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.system.service.ISysNoticeService;

/**
 * 公告 信息操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/notice")
@Api(tags = "通知公告管理")
public class SysNoticeController extends BaseController
{
    @Autowired
    private ISysNoticeService noticeService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysUserNoticeService userNoticeService;

    /**
     * 获取通知公告列表
     * GET /system/notice/list
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取通知公告列表")
    public TableDataInfo list(SysNotice notice)
    {
        startPage();
        List<SysNotice> list = noticeService.selectNoticeList(notice);
        return getDataTable(list);
    }

    /**
     * 根据通知公告编号获取详细信息
     * GET /system/notice/{noticeId}
     */
    @GetMapping(value = "/{noticeId}")
    @ApiOperation(value = "获取通知公告详细信息")
    public AjaxResult getInfo(@PathVariable Long noticeId)
    {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     * POST /system/notice
     *
     * @param sysNoticeDTO 通知数据传输对象，包含通知内容和接收通知的用户ID列表
     */
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增通知公告")
    public AjaxResult add(@RequestBody SysNoticeDTO sysNoticeDTO)
    {
        SysNotice notice = sysNoticeDTO.getSysNotice();
        List<Long> userIds = sysNoticeDTO.getUserIds();

        notice.setCreateBy(getUsername());

        // 插入公告
        Long noticeId = noticeService.insertNotice(notice);
        // 插入信息成功则保存用户与通知的关系
        if (noticeId > 0){
            saveUserNoticeRelation(notice.getNoticeId(), userIds);
        }
        return noticeId > 0
                ? AjaxResult.success(Map.of("noticeId", notice.getNoticeId()))
                : AjaxResult.error();
    }

    /**
     * 保存用户与通知的关系
     * @param noticeId 通知ID
     * @param userIds 用户ID列表
     */
    private void saveUserNoticeRelation(Long noticeId, List<Long> userIds) {
        if (userIds != null && !userIds.isEmpty()) {
            List<SysUserNotice> userNotices = new ArrayList<>();
            for (Long userId : userIds) {
                SysUserNotice userNotice = new SysUserNotice();
                userNotice.setNoticeId(noticeId);
                userNotice.setUserId(userId);
                userNotices.add(userNotice);
            }
            // 批量保存用户与通知的关系
            userNoticeService.insertBatch(userNotices);
        }
    }

    /**
     * 修改通知公告
     * PUT /system/notice
     */
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改通知公告")
    public AjaxResult edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(getUsername());
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     * DELETE /system/notice/{noticeIds}
     */
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    @ApiOperation(value = "删除通知公告")
    public AjaxResult remove(@PathVariable Long[] noticeIds)
    {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
