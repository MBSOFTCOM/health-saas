package cn.iocoder.yudao.module.childhealth.api.eye.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EyeHealthCheckupRequest {
    private String visualInspection;      // 眼部外观检查
    private String pupilResponse;          // 瞳孔反应
    private String eyeAlignment;          // 眼位对齐

    private BigDecimal visualAcuityLeft;  // 左眼视力
    private BigDecimal visualAcuityRight; // 右眼视力
    private BigDecimal visualAcuityBoth;  // 双眼视力

    private String refractiveErrorType;   // 屈光异常类型
    private BigDecimal sphereLeft;        // 左眼球镜
    private BigDecimal sphereRight;       // 右眼球镜
}
