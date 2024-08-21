package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;


import cn.hutool.core.annotation.Alias;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class StatisticExportVO2 implements Serializable {

    @Alias("序号")
    private Integer id;

    @Alias("单位(学校)")
    private String school;

    @Alias("班级")
    private String classroom;

    @Alias("姓名")
    private String name;

    @Alias("年龄")
    private Integer age;

    @Alias("身份证号码")
    private String idNum;

    @Alias("时间")
    private LocalDateTime ppdScreenTime; // PPD检查时间

    @Alias("已做（√）")
    private String isDoPpd;

    @Alias("横径mm")
    private Integer transverseDiameter;

    @Alias("纵径mm")
    private Integer longitudinalDiameter;

    @Alias("双圈、水泡、坏死或淋巴炎（√）")
    private String bleb;

    @Alias("硬结平均直径")
    private Double averageDiameter;

    @Alias("是否硬结平均直径<15mm")
    private String diameterFlag;

    @Alias("PPD判读结果")
    private String outcomePpd;

    @Alias("拍片时间")
    private LocalDateTime photoTime;

    @Alias("已做(打√)")
    private String isDoX;

    @Alias("结果")
    private String outcomeDr;

    @Alias("是否确诊")
    private Integer isDia;

    @Alias("未筛查原因:1.近1月打了疫苗;2.过敏体质;3.其它疾病(详述)")
    private String remark;

    @Alias("其他备注")
    private String otherRemark;


}
