package cn.iocoder.yudao.module.childhealth.api.device.dto;
import jakarta.validation.constraints.*; import lombok.Data;
@Data public class DeviceCollectRequest { @NotBlank private String deviceType; @NotBlank private String deviceCode; private Long examId; private Long screeningRecordId; @NotBlank private String dataContent; }
