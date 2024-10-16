package com.ruoyi.project.controller;

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
import com.ruoyi.project.domain.SysProject;
import com.ruoyi.project.service.ISysProjectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目管理 Controller
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-14
 */
@Api("项目管理")
@RestController
@RequestMapping("/dev-api/project")
public class SysProjectController extends BaseController {
    @Autowired
    private ISysProjectService sysProjectService;

    /**
     * 查询项目列表
     */
    @ApiOperation("项目列表")
    @PreAuthorize("@ss.hasPermi('dev-api:project:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysProject sysProject) {
        startPage();
        List<SysProject> list = sysProjectService.selectSysProjectList(sysProject);
        return getDataTable(list);
    }

    /**
     * 导出项目列表
     */
    @ApiOperation("导出项目")
    @PreAuthorize("@ss.hasPermi('dev-api:project:export')")
    @Log(title = "项目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysProject sysProject) {
        List<SysProject> list = sysProjectService.selectSysProjectList(sysProject);
        ExcelUtil<SysProject> util = new ExcelUtil<SysProject>(SysProject.class);
        util.exportExcel(response, list, "项目数据");
    }

    /**
     * 获取项目详细信息
     */
    @ApiOperation("查询项目")
    @PreAuthorize("@ss.hasPermi('dev-api:project:query')")
    @GetMapping(value = "/{projectId}")
    public AjaxResult getInfo(@PathVariable("projectId") Long projectId) {
        return success(sysProjectService.selectSysProjectByProjectId(projectId));
    }

    /**
     * 新增项目
     */
    @ApiOperation("新增项目")
    @PreAuthorize("@ss.hasPermi('dev-api:project:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysProject sysProject) {
        long projectId = sysProjectService.insertSysProject(sysProject);
        return projectId > 0 ? AjaxResult.success(Map.of(
                "projectId", projectId
        )) : AjaxResult.error();
    }

    /**
     * 修改项目
     */
    @ApiOperation("修改项目")
    @PreAuthorize("@ss.hasPermi('dev-api:project:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysProject sysProject) {
        long projectId = sysProjectService.updateSysProject(sysProject);
        return projectId > 0 ? AjaxResult.success(Map.of(
                "projectId", projectId
        )) : AjaxResult.error();
    }

    /**
     * 删除项目
     */
    @ApiOperation("删除项目")
    @PreAuthorize("@ss.hasPermi('dev-api:project:remove')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{projectIds}")
    public AjaxResult remove(@PathVariable Long[] projectIds) {
        return toAjax(sysProjectService.deleteSysProjectByProjectIds(projectIds));
    }
}
