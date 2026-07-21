package cn.iocoder.yudao.module.childhealth.dal.dataobject.device;
import com.baomidou.mybatisplus.annotation.*; import lombok.Data; import java.time.LocalDateTime;
@Data @TableName("device_integration_log") public class DeviceIntegrationLogDO { @TableId(type=IdType.AUTO) private Long id; private String deviceType; private String deviceCode; private Long examId; private String dataContent; private LocalDateTime integrationTime; private Integer status; private String errorMsg; private LocalDateTime createTime; }
