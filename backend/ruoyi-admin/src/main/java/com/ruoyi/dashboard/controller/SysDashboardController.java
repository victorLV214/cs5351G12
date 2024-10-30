package com.ruoyi.dashboard.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.dashboard.service.ISysDashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘 Controller
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-31
 */
@Api("仪表盘")
@RestController
@RequestMapping("/dev-api")
public class SysDashboardController extends BaseController {

    @Autowired
    private ISysDashboardService dashboardService;

    @ApiOperation("获取仪表盘数据")
    @PreAuthorize("@ss.hasPermi('dev-api:dashboard')")
    @GetMapping("/dashboard")
    public AjaxResult getDashboard() {
        return success(dashboardService.getDashboardData());
    }
}
