package cn.iocoder.yudao.module.cd.enums;


import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants{
    ErrorCode EXCEL_EMPTY=new ErrorCode(30001,"excel中没有数据");
    ErrorCode EMPTY=new ErrorCode(30000,"没有数据");

    // ========== 摸底  ==========
    ErrorCode SCREEN_PERSON_NOT_EXISTS = new ErrorCode(10001, "患者不存在");
    ErrorCode SCREEN_PERSON_IMPORT_ORDER_NOT_NUMBER = new ErrorCode(100010, "excel中的序号必须全部是大于0的正整数");

    ErrorCode IDNUM_ESITS = new ErrorCode(10002, "身份证号重复！");


    // ========= 实验室组 =========
    ErrorCode SCREEN_EXPERIMENT_NOT_EXISTS = new ErrorCode(10003, "实验室组不存在");
    ErrorCode SCREEN_EXPERIMENT_DATA_EXISTS = new ErrorCode(100031, "实验室组数据重复");


    // ======== 痰检组 ============
    ErrorCode SCREEN_SPUTUM_EXAMINATION_NOT_EXISTS = new ErrorCode(10004, "痰检组不存在");

    ErrorCode IMAGE_NOT_EXISTS = new ErrorCode(10005, "照片不存在");

    // ========== 筛查点 ==========
    ErrorCode SCREEN_POINT_NOT_EXISTS = new ErrorCode(10006, "筛查点不存在");
    ErrorCode SCREEN_POINT_EXISTS = new ErrorCode(100062, "该年度该筛查单位的筛查点已存在");
    ErrorCode SCREEN_POINT_EXISTS2 = new ErrorCode(100062, "筛查点已存在");

    ErrorCode SCREEN_DIAGNOSIS_NOT_EXISTS = new ErrorCode(10006, "诊断组不存在");


    ErrorCode SCREEN_COLLECT_NOT_EXISTS = new ErrorCode(10007, "采集不存在");

    ErrorCode SCREEN_PPD_NOT_EXISTS = new ErrorCode(10008, "ppd组记录不存在");

    ErrorCode SCREEN_CHEST_RADIOGRAPH_NOT_EXISTS = new ErrorCode(10009, "ct、dr组不存在");
    ErrorCode SCREEN_ELECTROCARDIOGRAM_NOT_EXISTS = new ErrorCode(100010, "心电图组不存在");

    ErrorCode SCREEN_SUM_NOT_EXISTS = new ErrorCode(100011, "汇总不存在");
    ErrorCode WORKER_IS_DISTRIBUTED = new ErrorCode(100012, "该队长在该工作年度已被分配！");

    ErrorCode SCREEN_GRPUP_EXISTS = new ErrorCode(100070, "该组类型不存在！");

    ErrorCode IMAGE_NOT_EXIT_SCREEN_TIME = new ErrorCode(100013, "图片筛查时间不能为空！");
    // ========== 重复筛查人员管理 ==========
    ErrorCode SCREEN_REPEAT_PERSON_NOT_EXISTS = new ErrorCode(100080, "重复筛查人员不存在");

    // ========== 消耗管理 ==========
    ErrorCode SCREEN_CONSUME_NOT_EXISTS = new ErrorCode(20000, "消耗管理不存在");


    // ========== 试剂  ==========
    ErrorCode SCREEN_REAGENT_NOT_EXISTS = new ErrorCode(20001, "试剂不存在");
    ErrorCode SCREEN_REAGENT_Is_EXISTS = new ErrorCode(20002, "该试剂名称已存在！");
    // ========== 消耗管理记录 ==========
    ErrorCode SCREEN_CONSUME_RECORD_LIST_NULL=new ErrorCode(20006,"未上传试剂消耗记录");
    ErrorCode SCREEN_CONSUME_RECORD_NOT_EXISTS = new ErrorCode(20003, "消耗管理记录不存在");
    ErrorCode SCREEN_CONSUME_CURRENT_NUMBER_IS_NOT_ENOUGH = new ErrorCode(20004, "当前库存不足已减少");
    ErrorCode SCREEN_CONSUME_IS_EXISTS = new ErrorCode(20005, "消耗管理中有相应的试剂记录了！");

    // ========== 工作进展报告-统计表-导出的历史选项 TODO 补充编号 ==========
    ErrorCode SCREEN_STATICS_HISTORY_NOT_EXISTS = new ErrorCode(200100, "工作进展报告-统计表-导出的历史选项不存在");

}



