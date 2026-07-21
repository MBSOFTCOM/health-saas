package cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow;
import com.baomidou.mybatisplus.annotation.*; import lombok.Data; import java.time.LocalDateTime;
@Data @TableName("qrcode_management") public class QrCodeManagementDO { @TableId(type=IdType.AUTO) private Long id; private String qrcodeNo; private Long batchId; private Long studentId; private String qrcodeUrl; private String qrcodeContent; private Integer printStatus; private LocalDateTime printTime; private Boolean isUsed; private LocalDateTime createTime; }
