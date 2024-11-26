package com.ruoyi.item.controller;

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
import com.ruoyi.item.domain.SysWorkItem;
import com.ruoyi.item.service.ISysWorkItemService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 工作项 Controller
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-20
 */
@Api("工作项管理")
@RestController
@RequestMapping("/dev-api/project/item")
public class SysWorkItemController extends BaseController {
    @Autowired
    private ISysWorkItemService sysWorkItemService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 查询工作项列表
     */
    @ApiOperation("工作项列表")
    @GetMapping("/list")
    public TableDataInfo list(SysWorkItem sysWorkItem) {
        startPage();
        List<SysWorkItem> list = sysWorkItemService.selectSysWorkItemList(sysWorkItem);
        for (SysWorkItem item : list) {
            SysUser sysUser = sysUserService.selectUserById(item.getAssignedTo());
            item.setUserName(sysUser.getUserName());
            item.setNickName(sysUser.getNickName());
        }
        return getDataTable(list);
    }

    /**
     * 导出工作项列表
     */
    @ApiOperation("导出工作项")
    @Log(title = "工作项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, @RequestBody SysWorkItem sysWorkItem) {
        List<SysWorkItem> list = sysWorkItemService.selectSysWorkItemList(sysWorkItem);
        ExcelUtil<SysWorkItem> util = new ExcelUtil<SysWorkItem>(SysWorkItem.class);
        util.exportExcel(response, list, "工作项数据");
    }

    /**
     * 获取工作项详细信息
     */
    @ApiOperation("查询工作项")
    @GetMapping(value = "/{workItemId}")
    public AjaxResult getInfo(@PathVariable("workItemId") Long workItemId) {
        return success(sysWorkItemService.selectSysWorkItemByWorkItemId(workItemId));
    }

    /**
     * 新增工作项
     */
    @ApiOperation("新增工作项")
    @Log(title = "工作项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysWorkItem sysWorkItem) {
        long workItemId = sysWorkItemService.insertSysWorkItem(sysWorkItem);
        return workItemId > 0 ? AjaxResult.success(Map.of(
                "workItemId", workItemId
        )) : AjaxResult.error();
    }

    /**
     * 修改工作项
     */
    @ApiOperation("修改工作项")
    @Log(title = "工作项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysWorkItem sysWorkItem) {
        long workItemId = sysWorkItemService.updateSysWorkItem(sysWorkItem);
        return workItemId > 0 ? AjaxResult.success(Map.of(
                "workItemId", workItemId
        )) : AjaxResult.error();
    }

    /**
     * 删除工作项
     */
    @ApiOperation("删除工作项")
    @Log(title = "工作项", businessType = BusinessType.DELETE)
    @DeleteMapping("/{workItemIds}")
    public AjaxResult remove(@PathVariable Long[] workItemIds) {
        return toAjax(sysWorkItemService.deleteSysWorkItemByWorkItemIds(workItemIds));
    }
}
