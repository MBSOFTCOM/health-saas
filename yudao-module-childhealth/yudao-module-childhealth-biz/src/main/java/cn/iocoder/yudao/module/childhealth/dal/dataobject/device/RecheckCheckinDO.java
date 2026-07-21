package cn.iocoder.yudao.module.childhealth.dal.dataobject.device;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 复筛报到登记表 DO
 *
 * 对应表: recheck_checkin
 * 模块: D. 移动端功能补全
 * 创建日期: 2026-07-20
 */
@TableName("recheck_checkin")
@KeySequence("recheck_checkin_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecheckCheckinDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 复筛记录ID
     */
    private Long recheckId;

    /**
     * 阳性记录ID
     */
    private Long positiveId;

    /**
     * 儿童ID
     */
    private Long childId;

    /**
     * 学生ID
     */
    private Long studentId;

    /**
     * 报到编号
     */
    private String checkinNo;

    /**
     * 报到时间
     */
    private LocalDateTime checkinTime;

    /**
     * 报到方式 1扫码 2手动
     */
    private Integer checkinMethod;

    /**
     * 扫码内容
     */
    private String qrcodeContent;

    /**
     * 初筛阳性项目JSON
     */
    private String positiveItemsJson;

    /**
     * 现场状态 0待接诊 1已接诊 2已检查 3已离场
     */
    private Integer onSiteStatus;

    /**
     * 接诊医生ID
     */
    private Long receiveDoctorId;

    /**
     * 接诊时间
     */
    private LocalDateTime receiveTime;

    /**
     * 备注
     */
    private String remark;

}
