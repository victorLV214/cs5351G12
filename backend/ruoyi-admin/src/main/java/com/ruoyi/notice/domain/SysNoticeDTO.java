package com.ruoyi.notice.domain;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.domain.SysNotice;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "公告实体")
    private SysNotice sysNotice;
    /**
     * 公告接收人
     */
    @ApiModelProperty(value = "接收公告用户ID列表")
    private List<Long> userIds;
}
