package com.ruoyi.requirement.controller;

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
import com.ruoyi.requirement.domain.SysRequirement;
import com.ruoyi.requirement.service.ISysRequirementService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 需求Controller
 *
 * @author sarek
 * @date 2024-10-14
 */
@RestController
@RequestMapping("/dev-api/project/{projectId}/requirement")
@Api(tags = "需求管理")
public class SysRequirementController extends BaseController
{
    @Autowired
    private ISysRequirementService sysRequirementService;

    /**
     * 查询需求列表
     * GET /dev-api/project/{projectId}/requirement/list
     */
    @PreAuthorize("@ss.hasPermi('system:requirement:list')")
    @GetMapping("/list")
    @ApiOperation(value = "查询需求列表")
    public TableDataInfo list(SysRequirement sysRequirement)
    {
        System.out.println(sysRequirement);
        startPage();
        List<SysRequirement> list = sysRequirementService.selectSysRequirementList(sysRequirement);
        return getDataTable(list);
    }

    /**
     * 导出需求列表
     * GET /dev-api/project/{projectId}/requirement/export
     */
    @PreAuthorize("@ss.hasPermi('system:requirement:export')")
    @Log(title = "需求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "导出需求列表")
    public void export(HttpServletResponse response, SysRequirement sysRequirement)
    {
        List<SysRequirement> list = sysRequirementService.selectSysRequirementList(sysRequirement);
        ExcelUtil<SysRequirement> util = new ExcelUtil<SysRequirement>(SysRequirement.class);
        util.exportExcel(response, list, "需求数据");
    }

    /**
     * 获取需求详细信息
     * GET /dev-api/project/{projectId}/requirement/{reqId}
     */
    @PreAuthorize("@ss.hasPermi('system:requirement:query')")
    @GetMapping(value = "/{reqId}")
    @ApiOperation(value = "获取需求详细信息")
    public AjaxResult getInfo(@PathVariable("reqId") Long requirementId)
    {
        return success(sysRequirementService.selectSysRequirementByRequirementId(requirementId));
    }

    /**
     * 新增需求
     * POST /dev-api/project/{projectId}/requirement
     */
    @PreAuthorize("@ss.hasPermi('system:requirement:add')")
    @Log(title = "需求", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增需求")
    public AjaxResult add(@RequestBody SysRequirement sysRequirement)
    {
        int rows = sysRequirementService.insertSysRequirement(sysRequirement);
        Map<String, Long> data = new HashMap<>();
        data.put("requirementId", sysRequirement.getRequirementId());
        return rows > 0 ? AjaxResult.success(data) : AjaxResult.error();
    }

    /**
     * 修改需求
     *  PUT  /dev-api/project/{projectId}/requirement
     */
    @PreAuthorize("@ss.hasPermi('system:requirement:edit')")
    @Log(title = "需求", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改需求")
    public AjaxResult edit(@RequestBody SysRequirement sysRequirement)
    {
        return toAjax(sysRequirementService.updateSysRequirement(sysRequirement));
    }

    /**
     * 删除需求
     * DELETE  /dev-api/project/{id}/requirement/{requirementIds}
     */
    @PreAuthorize("@ss.hasPermi('system:requirement:remove')")
    @Log(title = "需求", businessType = BusinessType.DELETE)
	@DeleteMapping("/{requirementIds}")
    @ApiOperation(value = "删除需求")
    public AjaxResult remove(@PathVariable Long[] requirementIds)
    {
        return toAjax(sysRequirementService.deleteSysRequirementByRequirementIds(requirementIds));
    }
}
