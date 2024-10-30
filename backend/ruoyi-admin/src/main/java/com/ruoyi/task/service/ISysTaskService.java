package com.ruoyi.task.service;

import java.util.List;

import com.ruoyi.task.domain.SysTask;

/**
 * 任务 Service接口
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-30
 */
public interface ISysTaskService {
    /**
     * 查询任务
     *
     * @param taskId 任务主键
     * @return 任务
     */
    public SysTask selectSysTaskByTaskId(Long taskId);

    /**
     * 查询任务列表
     *
     * @param sysTask 任务
     * @return 任务集合
     */
    public List<SysTask> selectSysTaskList(SysTask sysTask);

    /**
     * 新增任务
     *
     * @param sysTask 任务
     * @return sysTask.id
     */
    public Long insertSysTask(SysTask sysTask);

    /**
     * 修改任务
     *
     * @param sysTask 任务
     * @return sysTask.id
     */
    public Long updateSysTask(SysTask sysTask);

    /**
     * 批量删除任务
     *
     * @param taskIds 需要删除的任务主键集合
     * @return 结果
     */
    public int deleteSysTaskByTaskIds(Long[] taskIds);

    /**
     * 删除任务信息
     *
     * @param taskId 任务主键
     * @return 结果
     */
    public int deleteSysTaskByTaskId(Long taskId);
}
