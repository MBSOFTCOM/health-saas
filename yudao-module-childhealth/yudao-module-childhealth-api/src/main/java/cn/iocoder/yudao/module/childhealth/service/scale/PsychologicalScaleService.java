package cn.iocoder.yudao.module.childhealth.service.scale;

import cn.iocoder.yudao.module.childhealth.api.scale.dto.PsychologicalScaleCreateReqDTO;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.PsychologicalScaleRespDTO;

import java.util.List;
import java.util.Map;
import cn.iocoder.yudao.module.childhealth.api.scale.dto.ScaleAssessmentDTO.*;

public interface PsychologicalScaleService {
    Long createPsychologicalScale(PsychologicalScaleCreateReqDTO createReqDTO);
    PsychologicalScaleRespDTO getPsychologicalScale(Long id);
    List<PsychologicalScaleRespDTO> getPsychologicalScalesByAgeAndType(Integer ageMonths, String scaleType);
    List<PsychologicalScaleRespDTO> getAllActivePsychologicalScales();
    List<QuestionResponse> getQuestions(Long scaleId);
    Long submitAssessment(SubmitRequest request);
    AssessmentResponse getAssessment(Long id);
    List<AssessmentResponse> getAssessments(Long childId, Long scaleId);
    Map<String, String> getScoring(Long scaleId);
    Long createReport(Long assessmentId);
}
