package com.ruoyi.notice.domain;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysNotice;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author sarek
 */
@Data
public class SysNoticeDTO implements Serializable {
    /**
     * 公告数据
     */
    private SysNotice sysNotice;
    /**
     * 公告接收人
     */
    private List<Long> userIds;
}
