package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class UserLoginInfoVO {
    @Schema(description = "主键")
    private Long id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "密码")
    private String pwd;

    @Schema(description = "角色/分组")
    private String groupName;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "筛查单位")
    private String agency;

    @Schema(description = "筛查单位map")
    private List<Map<String, Object>> screenPointList;
}
