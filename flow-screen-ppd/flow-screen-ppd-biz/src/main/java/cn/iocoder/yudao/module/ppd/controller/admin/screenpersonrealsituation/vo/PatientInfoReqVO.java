package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;


import lombok.Data;

import java.io.Serializable;

@Data
public class PatientInfoReqVO implements Serializable {

    /**
     * 摸底、待筛查id
     */
    private Long id;
    /**
     * 身份证号码
     */
    private String idNum;
    /**
     * 工作年度
     */
    private Integer year;
    /**
     * 筛查类型
     */
    private Integer screenType;
    /**
     * 单位（学校）
     */
    private String school;
    /**
     * 班级
     */
    private String classroom;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
}
