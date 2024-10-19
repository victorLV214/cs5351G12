package com.ruoyi.defect.mapper;

import java.util.List;

import com.ruoyi.defect.domain.SysDefect;
import org.apache.ibatis.annotations.Mapper;

/**
 * 缺陷 Mapper接口
 *
 * @author AB-IN-LIUSY
 * @date 2024-10-17
 */
@Mapper
public interface SysDefectMapper {
    /**
     * 查询缺陷
     *
     * @param defectId 缺陷主键
     * @return 缺陷
     */
    public SysDefect selectSysDefectByDefectId(Long defectId);

    /**
     * 查询缺陷列表
     *
     * @param sysDefect 缺陷
     * @return 缺陷集合
     */
    public List<SysDefect> selectSysDefectList(SysDefect sysDefect);

    /**
     * 新增缺陷
     *
     * @param sysDefect 缺陷
     * @return 结果
     */
    public int insertSysDefect(SysDefect sysDefect);

    /**
     * 修改缺陷
     *
     * @param sysDefect 缺陷
     * @return 结果
     */
    public int updateSysDefect(SysDefect sysDefect);

    /**
     * 删除缺陷
     *
     * @param defectId 缺陷主键
     * @return 结果
     */
    public int deleteSysDefectByDefectId(Long defectId);

    /**
     * 批量删除缺陷
     *
     * @param defectIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysDefectByDefectIds(Long[] defectIds);
}
