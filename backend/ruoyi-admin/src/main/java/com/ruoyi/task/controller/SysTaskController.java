package com.ruoyi.task.controller;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

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
import com.ruoyi.task.domain.SysTask;
import com.ruoyi.task.service.ISysTaskService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 任务 Controller
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-30
 */
@Api("任务管理")
@RestController
@RequestMapping("/dev-api/project/task")
public class SysTaskController extends BaseController {
    @Autowired
    private ISysTaskService sysTaskService;

    /**
     * 查询任务列表
     */
    @ApiOperation("任务列表")
    @GetMapping("/list")
    public TableDataInfo list(SysTask sysTask) {
        startPage();
        List<SysTask> list = sysTaskService.selectSysTaskList(sysTask);
        return getDataTable(list);
    }

    /**
     * 导出任务列表
     */
    @ApiOperation("导出任务")
    @Log(title = "任务", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTask sysTask) {
        List<SysTask> list = sysTaskService.selectSysTaskList(sysTask);
        ExcelUtil<SysTask> util = new ExcelUtil<SysTask>(SysTask.class);
        util.exportExcel(response, list, "任务数据");
    }

    /**
     * 获取任务详细信息
     */
    @ApiOperation("查询任务")
    @GetMapping(value = "/{taskId}")
    public AjaxResult getInfo(@PathVariable("taskId") Long taskId) {
        return success(sysTaskService.selectSysTaskByTaskId(taskId));
    }

    /**
     * 新增任务
     */
    @ApiOperation("新增任务")
    @PreAuthorize("@ss.hasPermi('dev-api:project:task:add')")
    @Log(title = "任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysTask sysTask) {
        long taskId = sysTaskService.insertSysTask(sysTask);
        return taskId > 0 ? AjaxResult.success(Map.of(
                "taskId", taskId
        )) : AjaxResult.error();
    }

    /**
     * 修改任务
     */
    @ApiOperation("修改任务")
    @PreAuthorize("@ss.hasPermi('dev-api:project:task:edit')")
    @Log(title = "任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysTask sysTask) {
        long taskId = sysTaskService.updateSysTask(sysTask);
        return taskId > 0 ? AjaxResult.success(Map.of(
                "taskId", taskId
        )) : AjaxResult.error();
    }

    /**
     * 删除任务
     */
    @ApiOperation("删除任务")
    @PreAuthorize("@ss.hasPermi('dev-api:project:task:remove')")
    @Log(title = "任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{taskIds}")
    public AjaxResult remove(@PathVariable Long[] taskIds) {
        return toAjax(sysTaskService.deleteSysTaskByTaskIds(taskIds));
    }
}
