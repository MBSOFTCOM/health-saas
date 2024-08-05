package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ScreenPersonImportRespVO {
    @Schema(description = "创建成功的数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> createSpecification;

    private List<String> createRepeatSpecification;

    @Schema(description = "更新成功的数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> updateSpecification;

    @Schema(description = "重复的数组", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> repeateSpecification;

    @Schema(description = "导入失败的机构集合，key 为剂型值，value 为失败原因", requiredMode = Schema.RequiredMode.REQUIRED)
    private Map<Integer, String> failureSpecification;
}
