package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PasswordMatchVO {
    @Schema(description = "输入的密码")
    private String rawPassword;

    @Schema(description = "用户的密码")
    private String password;
}
