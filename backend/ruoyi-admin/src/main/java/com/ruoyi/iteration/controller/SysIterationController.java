package com.ruoyi.iteration.controller;

import java.util.HashMap;
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
import com.ruoyi.iteration.domain.SysIteration;
import com.ruoyi.iteration.service.ISysIterationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 迭代Controller
 * 
 * @author ruoyi
 * @date 2024-10-27
 */
@RestController
@RequestMapping("/dev-api/project/iteration")
@Api(tags = "迭代管理")
public class SysIterationController extends BaseController
{
    @Autowired
    private ISysIterationService sysIterationService;

    /**
     * 查询迭代列表
     */
    @PreAuthorize("@ss.hasPermi('dev-api:project:iteration:list')")
    @GetMapping("/list")
    @ApiOperation(value = "查询迭代列表")
    public TableDataInfo list(SysIteration sysIteration)
    {
        startPage();
        List<SysIteration> list = sysIterationService.selectSysIterationList(sysIteration);
        return getDataTable(list);
    }

    /**
     * 导出迭代列表
     */
    @PreAuthorize("@ss.hasPermi('dev-api:project:iteration:export')")
    @Log(title = "迭代", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "导出迭代列表")
    public void export(HttpServletResponse response, SysIteration sysIteration)
    {
        List<SysIteration> list = sysIterationService.selectSysIterationList(sysIteration);
        ExcelUtil<SysIteration> util = new ExcelUtil<SysIteration>(SysIteration.class);
        util.exportExcel(response, list, "迭代数据");
    }

    /**
     * 获取迭代详细信息
     */
    @PreAuthorize("@ss.hasPermi('dev-api:project:iteration:query')")
    @GetMapping(value = "/{iterationId}")
    @ApiOperation(value = "获取迭代详细信息")
    public AjaxResult getInfo(@PathVariable("iterationId") Long iterationId)
    {
        return success(sysIterationService.selectSysIterationByIterationId(iterationId));
    }

    /**
     * 新增迭代
     */
    @PreAuthorize("@ss.hasPermi('dev-api:project:iteration:add')")
    @Log(title = "迭代", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增迭代")
    public AjaxResult add(@RequestBody SysIteration sysIteration)
    {
        int rows = sysIterationService.insertSysIteration(sysIteration);
        return rows > 0
                ? AjaxResult.success(Map.of("requirementId", sysIteration))
                : AjaxResult.error();
    }

    /**
     * 修改迭代
     */
    @PreAuthorize("@ss.hasPermi('dev-api:project:iteration:edit')")
    @Log(title = "迭代", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改迭代")
    public AjaxResult edit(@RequestBody SysIteration sysIteration)
    {
        return toAjax(sysIterationService.updateSysIteration(sysIteration));
    }

    /**
     * 删除迭代
     */
    @PreAuthorize("@ss.hasPermi('dev-api:project:iteration:remove')")
    @Log(title = "迭代", businessType = BusinessType.DELETE)
	@DeleteMapping("/{iterationIds}")
    @ApiOperation(value = "删除迭代")
    public AjaxResult remove(@PathVariable Long[] iterationIds)
    {
        return toAjax(sysIterationService.deleteSysIterationByIterationIds(iterationIds));
    }
}
