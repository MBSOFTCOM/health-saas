package cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ScreenReagentImportRespVO {
    @Schema(description = "创建成功的数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> createSpecification;

    @Schema(description = "更新成功的数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> updateSpecification;

    @Schema(description = "导入失败的集合，key 为剂型值，value 为失败原因", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<Integer, String> failureSpecification;
}
