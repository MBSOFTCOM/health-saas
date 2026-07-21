package cn.iocoder.yudao.module.childhealth.service.casemanagement.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseFollowupRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.CaseFollowupRecordMapper;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.CaseFollowupRecordService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 专案随访记录 Service 实现类
 *
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
@Service
@Validated
public class CaseFollowupRecordServiceImpl implements CaseFollowupRecordService {

    @Resource
    private CaseFollowupRecordMapper caseFollowupRecordMapper;

    @Override
    public Long createCaseFollowupRecord(Object createReqVO) {
        // TODO 后续替换为 CaseFollowupRecordSaveReqVO
        CaseFollowupRecordDO caseFollowupRecord = BeanUtils.toBean(createReqVO, CaseFollowupRecordDO.class);
        caseFollowupRecordMapper.insert(caseFollowupRecord);
        return caseFollowupRecord.getId();
    }

    @Override
    public void updateCaseFollowupRecord(Object updateReqVO) {
        // TODO 后续替换为 CaseFollowupRecordSaveReqVO，并校验存在
        // validateCaseFollowupRecordExists(updateReqVO.getId());
        CaseFollowupRecordDO updateObj = BeanUtils.toBean(updateReqVO, CaseFollowupRecordDO.class);
        caseFollowupRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteCaseFollowupRecord(Long id) {
        validateCaseFollowupRecordExists(id);
        caseFollowupRecordMapper.deleteById(id);
    }

    private void validateCaseFollowupRecordExists(Long id) {
        if (caseFollowupRecordMapper.selectById(id) == null) {
            throw exception(CASE_FOLLOWUP_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public CaseFollowupRecordDO getCaseFollowupRecord(Long id) {
        return caseFollowupRecordMapper.selectById(id);
    }

    @Override
    public PageResult<CaseFollowupRecordDO> getCaseFollowupRecordPage(PageParam pageParam) {
        // TODO 后续替换为 CaseFollowupRecordPageReqVO，并实现分页查询条件
        return caseFollowupRecordMapper.selectPage(pageParam, null);
    }

    @Override
    public List<CaseFollowupRecordDO> selectListByCaseId(Long caseId) {
        return caseFollowupRecordMapper.selectListByCaseId(caseId);
    }

    @Override
    public int batchInsert(List<CaseFollowupRecordDO> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }
        Boolean result = caseFollowupRecordMapper.insertBatch(records);
        return result != null && result ? 1 : 0;
    }

}
