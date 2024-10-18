package com.ruoyi.defect.controller;

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
import com.ruoyi.defect.domain.SysDefect;
import com.ruoyi.defect.service.ISysDefectService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 缺陷管理 Controller
 * 
 * @author AB-IN-LIUSY
 * @date 2024-10-17
 */
@Api("缺陷管理")
@RestController
@RequestMapping("/dev-api/project/defect")
public class SysDefectController extends BaseController {
    @Autowired
    private ISysDefectService sysDefectService;

    /**
     * 查询缺陷列表
     */
    @ApiOperation("缺陷列表")
    @PreAuthorize("@ss.hasPermi('dev-api:project:defect:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDefect sysDefect){
        startPage();
        List<SysDefect> list = sysDefectService.selectSysDefectList(sysDefect);
        return getDataTable(list);
    }

    /**
     * 导出缺陷列表
     */
    @ApiOperation("导出缺陷")
    @PreAuthorize("@ss.hasPermi('dev-api:project:defect:export')")
    @Log(title = "缺陷管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysDefect sysDefect) {
        List<SysDefect> list = sysDefectService.selectSysDefectList(sysDefect);
        ExcelUtil<SysDefect> util = new ExcelUtil<SysDefect>(SysDefect.class);
        util.exportExcel(response, list, "缺陷数据");
    }

    /**
     * 获取缺陷详细信息
     */
    @ApiOperation("查询缺陷")
    @PreAuthorize("@ss.hasPermi('dev-api:project:defect:query')")
    @GetMapping(value = "/{defectId}")
    public AjaxResult getInfo(@PathVariable("defectId") Long defectId) {
        return success(sysDefectService.selectSysDefectByDefectId(defectId));
    }

    /**
     * 新增缺陷
     */
    @ApiOperation("新增缺陷")
    @PreAuthorize("@ss.hasPermi('dev-api:project:defect:add')")
    @Log(title = "缺陷管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysDefect sysDefect) {
        long defectId = sysDefectService.insertSysDefect(sysDefect);
        return defectId > 0 ? AjaxResult.success(Map.of(
                "defectId", defectId
        )) : AjaxResult.error();
    }

    /**
     * 修改缺陷
     */
    @ApiOperation("修改缺陷")
    @PreAuthorize("@ss.hasPermi('dev-api:project:defect:edit')")
    @Log(title = "缺陷管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysDefect sysDefect) {
        long defectId = sysDefectService.updateSysDefect(sysDefect);
        return defectId > 0 ? AjaxResult.success(Map.of(
                "defectId", defectId
        )) : AjaxResult.error();
    }

    /**
     * 删除缺陷
     */
    @ApiOperation("删除缺陷")
    @PreAuthorize("@ss.hasPermi('dev-api:project:defect:remove')")
    @Log(title = "缺陷管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{defectIds}")
    public AjaxResult remove(@PathVariable Long[] defectIds) {
        return toAjax(sysDefectService.deleteSysDefectByDefectIds(defectIds));
    }
}
