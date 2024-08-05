package cn.iocoder.yudao.module.ppd.controller.admin.screenexperiment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 筛查次序/时间下拉列表数据
 */
@Data
public class ScreenOrderValue {

    /**
     * 次序
     */
    private Integer value;

    /**
     * 筛查次序/筛查时间
     */
    private String label;

    /**
     *  筛查次序/筛查时间 用于兼容移动端下拉框显示
     */
    private String text;


    // 以下数据可能在选中后发生变化的 需要一同带回前端 这个主键对应的是痰检组中id
    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "17051")
    private Long id;

    @Schema(description = "筛查编号", example = "14019")
    private String screenId;

    @Schema(description = "筛查时间", example = "14019")
    private LocalDateTime screenTime;
}
