package com.ruoyi.dashboard.service;

import com.ruoyi.dashboard.domain.SysDashboard;

/**
 * 仪表盘 Service接口
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-31
 */
public interface ISysDashboardService {
    /**
     * 获取当前用户的仪表盘数据
     *
     * @return 仪表盘数据
     */
    SysDashboard getDashboardData();
}
