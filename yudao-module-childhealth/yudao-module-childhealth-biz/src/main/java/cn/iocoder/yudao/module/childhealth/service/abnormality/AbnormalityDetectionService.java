package cn.iocoder.yudao.module.childhealth.service.abnormality;

public interface AbnormalityDetectionService {
    boolean detectAbnormality(Object checkupData, String ruleType);
}
