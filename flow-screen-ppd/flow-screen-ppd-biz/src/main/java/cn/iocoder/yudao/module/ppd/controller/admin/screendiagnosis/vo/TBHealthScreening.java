package cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 体检单
 */
@Data
public class TBHealthScreening {
    // 筛查编号
    private String screeningNumber;
    // 身份证号
    private String idNumber;
    // 姓名
    private String name;
    // 年龄
    private int age;
    // 体检日期
    private String examinationDate;

    // 是否活动性肺结核密切接触者
    private boolean closeContactWithActivePulmonaryTB;
    // 是否僧尼0-5岁
    private boolean monkOrNun0To5Years;
    // 是否僧尼6-14岁
    private boolean monkOrNun6To14Years;
    // 是否僧尼≥ 15岁
    private boolean monkOrNunOver15Years;
    // 是否HIV/AIDS
    private boolean HIVorAIDS;
    // 是否既往结核病患者
    private boolean pastTBPatient;
    // 是否在校师生0-5岁学生
    private boolean student0To5Years;
    // 是否在校师生6-14岁学生
    private boolean student6To14Years;
    // 是否在校师生≥15岁学生
    private boolean studentOver15Years;
    // 是否在校师生教职工
    private boolean schoolStaff;
    // 是否老年人
    private boolean elderly;
    // 是否糖尿病患者
    private boolean diabetesPatient;
    // 是否非重点人群0-5岁
    private boolean nonKeyPopulation0To5Years;
    // 是否非重点人群6-14岁
    private boolean nonKeyPopulation6To14Years;
    // 是否非重点人群≥15岁
    private boolean nonKeyPopulationOver15Years;

    // 症状
    // 咳嗽、咳痰（超过 1 周） 新生=1 其他=1
    private boolean coughOrSputumForMoreThanOneWeek;
    // 咯血或血痰 新生=2 其他=2
    private boolean hemoptysisOrBloodSputum;
    // 发热 其他=3
    private boolean fever;
    // 反复发烧2周以上 新生=3
    private boolean persistentFever;
    // 淋巴结肿大  新生=4
    private boolean lymphoidEnlargement;
    // 胸痛 其他=4
    private boolean chestPain;
    // 夜间盗汗 其他=5
    private boolean nightSweats;
    // 食欲不振 其他=6
    private boolean lossOfAppetite;
    // 乏力 其他=7
    private boolean fatigue;
    // 体重减轻（超过 6 斤） 其他=8
    private boolean weightLossOverSixPounds;
    // 有无卡痕 其他=9
    private Boolean checkMark;
    // 是否做过查验卡痕
    private boolean isDoneCheckMark;
    // 采集医生签字
    private String collectDoctorSignature;

    // PPD测试
    // 是否做过ppd
    private boolean ppdTestDone;
    // 注射时间
    private LocalDateTime ppdInjectionTime;
    private String ppdInjectionTimeStr;
    //PPD结果
    private Integer ppdOutcome;
    // 医生签字完整url
    private String ppdDoctorSignature;

    // 胸部X线检查
    // 是否做过胸部X线
    private boolean chestXRayDone;
    // 是否无结核相关异常
    private boolean noTBRelatedAbnormalities;
    // 是否疑似结核
    private boolean suspectedTB;
    // 机器中与患者对应的编码
    private String chestXRayCode;
    // 2-其他异常 1-疑似结核 0-无异常
    private Integer outcome;
    // 医生签字完整url
    private String chestXRayDoctorSignature;

    // 痰标本检查
    // 是否做过痰标本
    private boolean sputumSpecimenDone;
    // 是否即时痰
    private boolean immediateSputum;
    // 是否发放晨痰
    private boolean morningSputum;
    // 是否夜间痰
    private boolean nighttimeSputum;
    // 是否无痰
    private boolean noSputum;
    // 痰标本类型，1-无痰 2-即时痰 3-发放晨痰 4-夜间痰盒
    private Integer type;
    // 医生签字
    private String sputumDoctorSignature;
}

