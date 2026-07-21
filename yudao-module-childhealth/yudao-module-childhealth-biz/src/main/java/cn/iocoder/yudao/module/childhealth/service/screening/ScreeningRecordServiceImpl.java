package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.record.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningRecordDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.ScreeningRecordMapper;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 筛查记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreeningRecordServiceImpl implements ScreeningRecordService {

    @Resource
    private ScreeningRecordMapper screeningRecordMapper;

    @Override
    public Long createScreeningRecord(ScreeningRecordSaveReqVO createReqVO) {
        // 校验学生是否已有该批次的筛查记录
        validateStudentRecordUnique(createReqVO.getBatchId(), createReqVO.getStudentId());
        
        // 生成筛查流水号
        String recordNo = generateRecordNo();
        
        // 插入
        ScreeningRecordDO screeningRecord = BeanUtils.toBean(createReqVO, ScreeningRecordDO.class);
        screeningRecord.setRecordNo(recordNo);
        screeningRecord.setCheckStatus(1); // 默认进行中
        screeningRecordMapper.insert(screeningRecord);
        return screeningRecord.getId();
    }

    @Override
    public void updateScreeningRecord(ScreeningRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateScreeningRecordExists(updateReqVO.getId());
        // 更新
        ScreeningRecordDO updateObj = BeanUtils.toBean(updateReqVO, ScreeningRecordDO.class);
        screeningRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreeningRecord(Long id) {
        // 校验存在
        validateScreeningRecordExists(id);
        // 删除
        screeningRecordMapper.deleteById(id);
    }

    private void validateScreeningRecordExists(Long id) {
        if (screeningRecordMapper.selectById(id) == null) {
            throw exception(SCREENING_RECORD_NOT_EXISTS);
        }
    }

    private void validateStudentRecordUnique(Long batchId, Long studentId) {
        ScreeningRecordDO record = screeningRecordMapper.selectByBatchIdAndStudentId(batchId, studentId);
        if (record != null) {
            throw exception(SCREENING_RECORD_ALREADY_EXISTS);
        }
    }

    private String generateRecordNo() {
        return "SCR" + IdUtil.fastSimpleUUID().substring(0, 12);
    }

    @Override
    public ScreeningRecordDO getScreeningRecord(Long id) {
        return screeningRecordMapper.selectById(id);
    }

    @Override
    public PageResult<ScreeningRecordDO> getScreeningRecordPage(ScreeningRecordPageReqVO pageReqVO) {
        return screeningRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreeningRecordDO> getScreeningRecordList(ScreeningRecordListReqVO listReqVO) {
        return screeningRecordMapper.selectList(listReqVO);
    }

    @Override
    public void auditScreeningRecord(Long id, Long auditDoctor) {
        // 校验存在
        ScreeningRecordDO record = screeningRecordMapper.selectById(id);
        if (record == null) {
            throw exception(SCREENING_RECORD_NOT_EXISTS);
        }
        // 校验状态
        if (record.getCheckStatus() != 2) {
            throw exception(SCREENING_RECORD_NOT_AUDIT);
        }
        
        // 更新
        ScreeningRecordDO updateObj = new ScreeningRecordDO();
        updateObj.setId(id);
        updateObj.setCheckStatus(3); // 已审核
        updateObj.setAuditDoctor(auditDoctor);
        updateObj.setAuditTime(LocalDateTime.now());
        screeningRecordMapper.updateById(updateObj);
    }

    @Override
    public void submitAudit(Long id) {
        // 校验存在
        ScreeningRecordDO record = screeningRecordMapper.selectById(id);
        if (record == null) {
            throw exception(SCREENING_RECORD_NOT_EXISTS);
        }
        
        // 更新
        ScreeningRecordDO updateObj = new ScreeningRecordDO();
        updateObj.setId(id);
        updateObj.setCheckStatus(2); // 待审核
        screeningRecordMapper.updateById(updateObj);
    }

}