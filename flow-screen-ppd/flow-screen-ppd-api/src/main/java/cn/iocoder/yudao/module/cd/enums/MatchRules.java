package cn.iocoder.yudao.module.cd.enums;

public interface MatchRules {
    String ID_NUMBER = "^([1-9]\\d{5})([1-3]\\d|[4-9]\\d|[1-9]\\d\\d)(\\d{2})([1-7]\\d|[1-9]\\d\\d)(\\d{3})([0-9X])$";
    String TEL="^(1\\d{10})$";
}
