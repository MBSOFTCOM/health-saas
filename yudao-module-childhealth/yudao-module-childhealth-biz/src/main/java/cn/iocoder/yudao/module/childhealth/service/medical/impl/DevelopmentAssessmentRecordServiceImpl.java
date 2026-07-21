package cn.iocoder.yudao.module.childhealth.service.medical.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentAssessmentRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.DevelopmentAssessmentRecordMapper;
import cn.iocoder.yudao.module.childhealth.service.medical.DevelopmentAssessmentRecordService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.DEVELOPMENT_ASSESSMENT_RECORD_NOT_EXISTS;

/**
 * 发育评估记录表 Service 实现类
 *
 * 模块: A. 儿童基础健康检查（A8-发育评估记录表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DevelopmentAssessmentRecordServiceImpl implements DevelopmentAssessmentRecordService {

    @Resource
    private DevelopmentAssessmentRecordMapper developmentAssessmentRecordMapper;

    @Override
    public Long createDevelopmentAssessmentRecord(Object saveReqVO) {
        // TODO 后续替换为 DevelopmentAssessmentRecordSaveReqVO
        DevelopmentAssessmentRecordDO record = BeanUtils.toBean(saveReqVO, DevelopmentAssessmentRecordDO.class);
        developmentAssessmentRecordMapper.insert(record);
        return record.getId();
    }

    @Override
    public void updateDevelopmentAssessmentRecord(Object saveReqVO) {
        // TODO 后续替换为 DevelopmentAssessmentRecordSaveReqVO
        DevelopmentAssessmentRecordDO updateObj = BeanUtils.toBean(saveReqVO, DevelopmentAssessmentRecordDO.class);
        validateDevelopmentAssessmentRecordExists(updateObj.getId());
        developmentAssessmentRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevelopmentAssessmentRecord(Long id) {
        validateDevelopmentAssessmentRecordExists(id);
        developmentAssessmentRecordMapper.deleteById(id);
    }

    @Override
    public DevelopmentAssessmentRecordDO getDevelopmentAssessmentRecord(Long id) {
        return developmentAssessmentRecordMapper.selectById(id);
    }

    @Override
    public PageResult<DevelopmentAssessmentRecordDO> getDevelopmentAssessmentRecordPage(PageParam pageParam) {
        // TODO 后续替换为 DevelopmentAssessmentRecordPageReqVO，并增加查询条件
        return developmentAssessmentRecordMapper.selectPage(pageParam, null);
    }

    @Override
    public void autoCalculateScore(Long recordId) {
        // TODO 实现自动计分：拉取答题列表，根据量表 scoringRuleJson 计算总分、各维度得分、风险等级，回写评估记录
        log.info("[autoCalculateScore] 自动计分 recordId={}", recordId);
        validateDevelopmentAssessmentRecordExists(recordId);
    }

    @Override
    public String generateReport(Long recordId) {
        // TODO 实现报告生成：根据评估记录、答题明细、量表配置渲染 PDF 报告并落盘，回写 reportUrl
        log.info("[generateReport] 生成评估报告 recordId={}", recordId);
        validateDevelopmentAssessmentRecordExists(recordId);
        return null;
    }

    private void validateDevelopmentAssessmentRecordExists(Long id) {
        if (id == null || developmentAssessmentRecordMapper.selectById(id) == null) {
            throw exception(DEVELOPMENT_ASSESSMENT_RECORD_NOT_EXISTS);
        }
    }

}
