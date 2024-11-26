package com.ruoyi.member.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.member.mapper.SysProjectMemberMapper;
import com.ruoyi.member.domain.SysProjectMember;
import com.ruoyi.member.service.ISysProjectMemberService;

/**
 * 项目成员 Service业务层处理
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-18
 */
@Service
public class SysProjectMemberServiceImpl implements ISysProjectMemberService {
    @Autowired
    private SysProjectMemberMapper sysProjectMemberMapper;

    /**
     * 查询项目成员
     *
     * @param projectMemberId 项目成员主键
     * @return 项目成员
     */
    @Override
    public SysProjectMember selectSysProjectMemberByProjectMemberId(Long projectMemberId) {
        return sysProjectMemberMapper.selectSysProjectMemberByProjectMemberId(projectMemberId);
    }
    /**
     * 根据项目成员的userId查询
     * @param userId 项目成员对应用户的userId
     * @return 项目成员
     */
    @Override
    public List<SysProjectMember> selectSysProjectMemberByUserId(Long userId) {
        return sysProjectMemberMapper.selectSysProjectMemberByUserId(userId);
    }

    /**
     * 查询项目成员列表
     *
     * @param sysProjectMember 项目成员
     * @return 项目成员
     */
    @Override
    public List<SysProjectMember> selectSysProjectMemberList(SysProjectMember sysProjectMember) {
        return sysProjectMemberMapper.selectSysProjectMemberList(sysProjectMember);
    }

    /**
     * 新增项目成员
     *
     * @param sysProjectMember 项目成员
     * @return sysProjectMember.id
     */
    @Override
    public Long insertSysProjectMember(SysProjectMember sysProjectMember) {
        sysProjectMember.setCreateTime(DateUtils.getNowDate());
        sysProjectMember.setUpdateTime(DateUtils.getNowDate());

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysProjectMember.setCreateBy(username);

        int result = sysProjectMemberMapper.insertSysProjectMember(sysProjectMember);
        return result > 0 ? sysProjectMember.getProjectMemberId() : result;
    }

    /**
     * 修改项目成员
     *
     * @param sysProjectMember 项目成员
     * @return 结果
     */
    @Override
    public Long updateSysProjectMember(SysProjectMember sysProjectMember) {
        sysProjectMember.setUpdateTime(DateUtils.getNowDate());

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysProjectMember.setUpdateBy(username);

        int result = sysProjectMemberMapper.updateSysProjectMember(sysProjectMember);
        return result > 0 ? sysProjectMember.getProjectMemberId() : result;
    }

    /**
     * 批量删除项目成员
     *
     * @param projectMemberIds 需要删除的项目成员主键
     * @return 结果
     */
    @Override
    public int deleteSysProjectMemberByProjectMemberIds(Long[] projectMemberIds) {
        return sysProjectMemberMapper.deleteSysProjectMemberByProjectMemberIds(projectMemberIds);
    }

    /**
     * 删除项目成员信息
     *
     * @param projectMemberId 项目成员主键
     * @return 结果
     */
    @Override
    public int deleteSysProjectMemberByProjectMemberId(Long projectMemberId) {
        return sysProjectMemberMapper.deleteSysProjectMemberByProjectMemberId(projectMemberId);
    }
}
