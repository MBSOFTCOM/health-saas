package cn.iocoder.yudao.module.childhealth.service.casemanagement.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderLogDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement.ExamReminderLogMapper;
import cn.iocoder.yudao.module.childhealth.service.casemanagement.ExamReminderLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 体检催检日志 Service 实现类
 *
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
@Service
@Validated
public class ExamReminderLogServiceImpl implements ExamReminderLogService {

    @Resource
    private ExamReminderLogMapper examReminderLogMapper;

    @Override
    public Long createExamReminderLog(Object createReqVO) {
        // TODO 后续替换为 ExamReminderLogSaveReqVO
        ExamReminderLogDO examReminderLog = BeanUtils.toBean(createReqVO, ExamReminderLogDO.class);
        examReminderLogMapper.insert(examReminderLog);
        return examReminderLog.getId();
    }

    @Override
    public void updateExamReminderLog(Object updateReqVO) {
        // TODO 后续替换为 ExamReminderLogSaveReqVO，并校验存在
        // validateExamReminderLogExists(updateReqVO.getId());
        ExamReminderLogDO updateObj = BeanUtils.toBean(updateReqVO, ExamReminderLogDO.class);
        examReminderLogMapper.updateById(updateObj);
    }

    @Override
    public void deleteExamReminderLog(Long id) {
        validateExamReminderLogExists(id);
        examReminderLogMapper.deleteById(id);
    }

    private void validateExamReminderLogExists(Long id) {
        if (examReminderLogMapper.selectById(id) == null) {
            throw exception(EXAM_REMINDER_LOG_NOT_EXISTS);
        }
    }

    @Override
    public ExamReminderLogDO getExamReminderLog(Long id) {
        return examReminderLogMapper.selectById(id);
    }

    @Override
    public PageResult<ExamReminderLogDO> getExamReminderLogPage(PageParam pageParam) {
        // TODO 后续替换为 ExamReminderLogPageReqVO，并实现分页查询条件
        return examReminderLogMapper.selectPage(pageParam, null);
    }

    @Override
    public void resendPush(Long logId) {
        // TODO 业务方法：手动补发推送
        //  1. 校验日志存在且状态为失败（pushStatus=2）或待发送（pushStatus=0）
        //  2. 调用消息推送服务（短信/微信/APP）重新发送
        //  3. 更新日志状态为已补发（pushStatus=4），重试次数+1，记录新的响应数据
        validateExamReminderLogExists(logId);
        ExamReminderLogDO updateObj = new ExamReminderLogDO();
        updateObj.setId(logId);
        updateObj.setPushStatus(4); // 4已补发
        updateObj.setPushTime(LocalDateTime.now());
        examReminderLogMapper.updateById(updateObj);
    }

    @Override
    public void cancelPush(Long logId) {
        // TODO 业务方法：取消推送
        //  1. 校验日志存在且状态为待发送（pushStatus=0）
        //  2. 调用消息推送服务取消待发送任务（如支持）
        //  3. 更新日志状态为已取消（pushStatus=3）
        validateExamReminderLogExists(logId);
        ExamReminderLogDO updateObj = new ExamReminderLogDO();
        updateObj.setId(logId);
        updateObj.setPushStatus(3); // 3已取消
        examReminderLogMapper.updateById(updateObj);
    }

    @Override
    public List<ExamReminderLogDO> selectListByRuleId(Long ruleId) {
        return examReminderLogMapper.selectListByRuleId(ruleId);
    }

}
