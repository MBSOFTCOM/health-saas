package cn.iocoder.yudao.module.ppd.controller.admin.synchronization.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WorkTeamVO {
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Schema(description = "姓名")
    private String name;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "密码")
    private String pwd;

    @Schema(description = "筛查点")
    private String screenPoint;

    @Schema(description = "角色/分组")
    private String groupName;

    @Schema(description = "筛查单位")
    private String agency;

    @Schema(description = "工作年度")
    private Integer year;

}
