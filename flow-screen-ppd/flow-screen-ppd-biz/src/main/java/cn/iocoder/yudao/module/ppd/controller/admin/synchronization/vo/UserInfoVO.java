package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoVO {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "密码")
    private String pwd;
}
