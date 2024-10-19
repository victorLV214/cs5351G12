package com.ruoyi.defect.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.defect.mapper.SysDefectMapper;
import com.ruoyi.defect.domain.SysDefect;
import com.ruoyi.defect.service.ISysDefectService;

/**
 * 缺陷 Service业务层处理
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-17
 */
@Service
public class SysDefectServiceImpl implements ISysDefectService {
    @Autowired
    private SysDefectMapper sysDefectMapper;

    /**
     * 查询缺陷
     *
     * @param defectId 缺陷主键
     * @return 缺陷
     */
    @Override
    public SysDefect selectSysDefectByDefectId(Long defectId) {
        return sysDefectMapper.selectSysDefectByDefectId(defectId);
    }

    /**
     * 查询缺陷列表
     *
     * @param sysDefect 缺陷
     * @return 缺陷
     */
    @Override
    public List<SysDefect> selectSysDefectList(SysDefect sysDefect) {
        return sysDefectMapper.selectSysDefectList(sysDefect);
    }

    /**
     * 新增缺陷
     *
     * @param sysDefect 缺陷
     * @return sysDefect.id
     */
    @Override
    public Long insertSysDefect(SysDefect sysDefect) {
        sysDefect.setCreateTime(DateUtils.getNowDate());
        sysDefect.setUpdateTime(DateUtils.getNowDate());
        sysDefect.setDelFlag("0"); // 存在

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysDefect.setCreateBy(username);

        int result = sysDefectMapper.insertSysDefect(sysDefect);
        return result > 0 ? sysDefect.getDefectId() : result;
    }

    /**
     * 修改缺陷
     *
     * @param sysDefect 缺陷
     * @return sysDefect.id
     */
    @Override
    public Long updateSysDefect(SysDefect sysDefect) {
        sysDefect.setUpdateTime(DateUtils.getNowDate());

        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        String username = user.getUserName();
        sysDefect.setUpdateBy(username);

        int result = sysDefectMapper.updateSysDefect(sysDefect);
        return result > 0 ? sysDefect.getDefectId() : result;
    }

    /**
     * 批量删除缺陷
     *
     * @param defectIds 需要删除的缺陷主键
     * @return 结果
     */
    @Override
    public int deleteSysDefectByDefectIds(Long[] defectIds) {
        return sysDefectMapper.deleteSysDefectByDefectIds(defectIds);
    }

    /**
     * 删除缺陷信息
     *
     * @param defectId 缺陷主键
     * @return 结果
     */
    @Override
    public int deleteSysDefectByDefectId(Long defectId) {
        return sysDefectMapper.deleteSysDefectByDefectId(defectId);
    }
}
