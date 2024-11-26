package com.ruoyi.member.mapper;

import java.util.List;

import com.ruoyi.member.domain.SysProjectMember;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目成员 Mapper接口
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-18
 */
@Mapper
public interface SysProjectMemberMapper {
    /**
     * 查询项目成员
     *
     * @param projectMemberId 项目成员主键
     * @return 项目成员
     */
    public SysProjectMember selectSysProjectMemberByProjectMemberId(Long projectMemberId);

    /**
     * 根据用户ID查询项目成员
     * @param userId 用户ID
     * @return 项目成员
     */
    List<SysProjectMember> selectSysProjectMemberByUserId(Long userId);

    /**
     * 查询项目成员列表
     *
     * @param sysProjectMember 项目成员
     * @return 项目成员集合
     */
    public List<SysProjectMember> selectSysProjectMemberList(SysProjectMember sysProjectMember);

    /**
     * 新增项目成员
     *
     * @param sysProjectMember 项目成员
     * @return 结果
     */
    public int insertSysProjectMember(SysProjectMember sysProjectMember);

    /**
     * 修改项目成员
     *
     * @param sysProjectMember 项目成员
     * @return 结果
     */
    public int updateSysProjectMember(SysProjectMember sysProjectMember);

    /**
     * 删除项目成员
     *
     * @param projectMemberId 项目成员主键
     * @return 结果
     */
    public int deleteSysProjectMemberByProjectMemberId(Long projectMemberId);

    /**
     * 批量删除项目成员
     *
     * @param projectMemberIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysProjectMemberByProjectMemberIds(Long[] projectMemberIds);

    /**
     * 查询项目成员数量
     *
     * @param projectId 项目ID
     * @return 成员数量
     */
    public int countMembersByProjectId(Long projectId);

    /**
     * 根据用户ID查询其参与的项目ID列表
     *
     * @param userId 用户ID
     * @return 项目ID列表
     */
    List<Long> selectProjectIdsByUserId( Long userId);

}
