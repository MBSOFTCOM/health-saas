package cn.iocoder.yudao.module.cd.enums;

public interface MatchRulesMsg extends MatchRules{
//    反馈后缀
    String SUFFIX_EMPTY="未填;";
    String SUFFIX_NULL="为空;";
    String SUFFIX_NOT_MATCH="格式错误;";
    String SUFFIX_NOT_EXIST="不匹配或不存在;";
//    属性
    String ID_NUMBER="身份证号";
    String TEL="联系电话";
    String SCREEN_POINT="筛查点";
//    错误反馈
    String ID_NUMBER_MATCH_ERROR=ID_NUMBER+SUFFIX_NOT_MATCH;
    String ID_NUMBER_EMPTY=ID_NUMBER+SUFFIX_EMPTY;
    String TEL_MATCH_ERROR=TEL+SUFFIX_NOT_MATCH;
    String TEL_EMPTY=TEL+SUFFIX_EMPTY;
    String SCREEN_POINT_EMPTY=SCREEN_POINT+SUFFIX_EMPTY;
    String SCREEN_POINT_NOT_EXIST=SCREEN_POINT+SUFFIX_NOT_EXIST;

}
