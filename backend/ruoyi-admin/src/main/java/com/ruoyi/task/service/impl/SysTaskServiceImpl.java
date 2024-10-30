package com.ruoyi.task.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.task.mapper.SysTaskMapper;
import com.ruoyi.task.domain.SysTask;
import com.ruoyi.task.service.ISysTaskService;

/**
 * 任务Service业务层处理
 *
 * @author ruoyi
 * @date 2024-10-30
 */
@Service
public class SysTaskServiceImpl implements ISysTaskService {
    @Autowired
    private SysTaskMapper sysTaskMapper;

    /**
     * 查询任务
     *
     * @param taskId 任务主键
     * @return 任务
     */
    @Override
    public SysTask selectSysTaskByTaskId(Long taskId) {
        return sysTaskMapper.selectSysTaskByTaskId(taskId);
    }

    /**
     * 查询任务列表
     *
     * @param sysTask 任务
     * @return 任务
     */
    @Override
    public List<SysTask> selectSysTaskList(SysTask sysTask) {
        return sysTaskMapper.selectSysTaskList(sysTask);
    }

    /**
     * 新增任务
     *
     * @param sysTask 任务
     * @return sysTask.id
     */
    @Override
    public Long insertSysTask(SysTask sysTask) {
        sysTask.setCreateTime(DateUtils.getNowDate());
        sysTask.setUpdateTime(DateUtils.getNowDate());
        sysTask.setDelFlag("0"); // 存在

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysTask.setCreateBy(username);

        int result = sysTaskMapper.insertSysTask(sysTask);
        return result > 0 ? sysTask.getTaskId() : result;
    }

    /**
     * 修改任务
     *
     * @param sysTask 任务
     * @return sysTask.id
     */
    @Override
    public Long updateSysTask(SysTask sysTask) {
        sysTask.setUpdateTime(DateUtils.getNowDate());

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysTask.setUpdateBy(username);

        int result = sysTaskMapper.updateSysTask(sysTask);
        return result > 0 ? sysTask.getTaskId() : result;
    }

    /**
     * 批量删除任务
     *
     * @param taskIds 需要删除的任务主键
     * @return 结果
     */
    @Override
    public int deleteSysTaskByTaskIds(Long[] taskIds) {
        return sysTaskMapper.deleteSysTaskByTaskIds(taskIds);
    }

    /**
     * 删除任务信息
     *
     * @param taskId 任务主键
     * @return 结果
     */
    @Override
    public int deleteSysTaskByTaskId(Long taskId) {
        return sysTaskMapper.deleteSysTaskByTaskId(taskId);
    }
}
