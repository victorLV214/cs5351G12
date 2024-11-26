package com.ruoyi.member.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.member.domain.SysProjectMember;
import com.ruoyi.member.service.ISysProjectMemberService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目成员 Controller
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-18
 */
@Api("项目成员管理")
@RestController
@RequestMapping("/dev-api/project/member")
public class SysProjectMemberController extends BaseController {
    @Autowired
    private ISysProjectMemberService sysProjectMemberService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询项目成员列表
     */
    @ApiOperation("项目成员列表")
    @GetMapping("/list")
    public TableDataInfo list(SysProjectMember sysProjectMember) {
        startPage();
        List<SysProjectMember> list = sysProjectMemberService.selectSysProjectMemberList(sysProjectMember);
        for (SysProjectMember member : list) {
            SysUser sysUser = sysUserService.selectUserById(member.getUserId());
            member.setUserName(sysUser.getUserName());
            member.setNickName(sysUser.getNickName());
        }
        return getDataTable(list);
    }

    /**
     * 导出项目成员列表
     */
    @ApiOperation("导出项目成员")
    @Log(title = "项目成员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SysProjectMember sysProjectMember) {
        List<SysProjectMember> list = sysProjectMemberService.selectSysProjectMemberList(sysProjectMember);
        ExcelUtil<SysProjectMember> util = new ExcelUtil<SysProjectMember>(SysProjectMember.class);
        util.exportExcel(response, list, "项目成员数据");
    }

    /**
     * 获取项目成员详细信息
     */
    @ApiOperation("查询项目成员")
    @GetMapping(value = "/{projectMemberId}")
    public AjaxResult getInfo(@PathVariable("projectMemberId") Long projectMemberId) {
        return success(sysProjectMemberService.selectSysProjectMemberByProjectMemberId(projectMemberId));
    }

    /**
     * 新增项目成员
     */
    @ApiOperation("新增项目成员")
    @Log(title = "项目成员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysProjectMember sysProjectMember) {
        long projectMemberId = sysProjectMemberService.insertSysProjectMember(sysProjectMember);
        return projectMemberId > 0 ? AjaxResult.success(Map.of(
                "projectMemberId", projectMemberId
        )) : AjaxResult.error();
    }

    /**
     * 修改项目成员
     */
    @ApiOperation("修改项目成员")
    @Log(title = "项目成员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysProjectMember sysProjectMember) {
        long projectMemberId = sysProjectMemberService.updateSysProjectMember(sysProjectMember);
        return projectMemberId > 0 ? AjaxResult.success(Map.of(
                "projectMemberId", projectMemberId
        )) : AjaxResult.error();
    }

    /**
     * 删除项目成员
     */
    @ApiOperation("删除项目成员")
    @Log(title = "项目成员", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectMemberIds}")
    public AjaxResult remove(@PathVariable Long[] projectMemberIds) {
        return toAjax(sysProjectMemberService.deleteSysProjectMemberByProjectMemberIds(projectMemberIds));
    }
}
