package cn.iocoder.yudao.module.childhealth.service.abnormality.impl;

import cn.iocoder.yudao.module.childhealth.api.eye.dto.EyeHealthCheckupRequest;
import cn.iocoder.yudao.module.childhealth.service.abnormality.AbnormalityDetectionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AbnormalityDetectionServiceImpl implements AbnormalityDetectionService {
    @Override
    public boolean detectAbnormality(Object checkupData, String ruleType) {
        if ("EYE".equalsIgnoreCase(ruleType) && checkupData instanceof EyeHealthCheckupRequest request) {
            return isBelow(request.getVisualAcuityBoth(), "0.5")
                    || isBelow(request.getVisualAcuityLeft(), "0.5")
                    || isBelow(request.getVisualAcuityRight(), "0.5")
                    || "MYOPIA".equalsIgnoreCase(request.getRefractiveErrorType())
                    || "HYPEROPIA".equalsIgnoreCase(request.getRefractiveErrorType())
                    || "ASTIGMATISM".equalsIgnoreCase(request.getRefractiveErrorType())
                    || "ABNORMAL".equalsIgnoreCase(request.getEyeAlignment());
        }
        return false;
    }

    private boolean isBelow(BigDecimal value, String threshold) {
        return value != null && value.compareTo(new BigDecimal(threshold)) < 0;
    }
}
