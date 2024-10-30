package com.ruoyi.task.mapper;

import java.util.List;

import com.ruoyi.task.domain.SysTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务 Mapper接口
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-30
 */
@Mapper
public interface SysTaskMapper {
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
     * @return 结果
     */
    public int insertSysTask(SysTask sysTask);

    /**
     * 修改任务
     *
     * @param sysTask 任务
     * @return 结果
     */
    public int updateSysTask(SysTask sysTask);

    /**
     * 删除任务
     *
     * @param taskId 任务主键
     * @return 结果
     */
    public int deleteSysTaskByTaskId(Long taskId);

    /**
     * 批量删除任务
     *
     * @param taskIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysTaskByTaskIds(Long[] taskIds);

    /**
     * 查询项目中的任务总数
     *
     * @param projectId 项目ID
     * @return 任务总数
     */
    public int countTasksByProjectId(Long projectId);

    /**
     * 查询项目中已完成的任务数量
     *
     * @param projectId 项目ID
     * @return 已完成的任务数量
     */
    public int countCompletedTasksByProjectId(Long projectId);
}
