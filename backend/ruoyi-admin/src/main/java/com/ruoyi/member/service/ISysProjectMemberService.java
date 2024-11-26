package com.ruoyi.member.service;

import java.util.List;

import com.ruoyi.member.domain.SysProjectMember;

/**
 * 项目成员 Service接口
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-18
 */
public interface ISysProjectMemberService {
    /**
     * 查询项目成员
     *
     * @param projectMemberId 项目成员主键
     * @return 项目成员
     */
    public SysProjectMember selectSysProjectMemberByProjectMemberId(Long projectMemberId);

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
     * @return sysProjectMember.id
     */
    public Long insertSysProjectMember(SysProjectMember sysProjectMember);

    /**
     * 修改项目成员
     *
     * @param sysProjectMember 项目成员
     * @return sysProjectMember.id
     */
    public Long updateSysProjectMember(SysProjectMember sysProjectMember);

    /**
     * 批量删除项目成员
     *
     * @param projectMemberIds 需要删除的项目成员主键集合
     * @return 结果
     */
    public int deleteSysProjectMemberByProjectMemberIds(Long[] projectMemberIds);

    /**
     * 删除项目成员信息
     *
     * @param projectMemberId 项目成员主键
     * @return 结果
     */
    public int deleteSysProjectMemberByProjectMemberId(Long projectMemberId);
}
