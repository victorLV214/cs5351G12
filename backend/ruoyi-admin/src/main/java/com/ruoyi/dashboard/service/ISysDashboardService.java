package com.ruoyi.dashboard.service;

import com.ruoyi.dashboard.domain.SysDashboard;

public interface ISysDashboardService {
    /**
     * 获取当前用户的仪表盘数据
     *
     * @return 仪表盘数据
     */
    SysDashboard getDashboardData();
}
