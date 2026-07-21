package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("referral_record")
public class ReferralRecordDO extends BaseDO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String referralNo;
    private Long studentId;
    private Long positiveId;
    private String referralReason;
    private String referralItems;
    private String targetHospital;
    private String targetDept;
    private String targetDoctor;
    private Integer referralStatus;
    private String feedbackContent;
}
