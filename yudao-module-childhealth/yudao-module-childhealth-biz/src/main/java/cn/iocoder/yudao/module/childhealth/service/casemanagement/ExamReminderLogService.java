package cn.iocoder.yudao.module.childhealth.service.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderLogDO;

import java.util.List;

/**
 * 体检催检日志 Service 接口
 *
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
public interface ExamReminderLogService {

    /**
     * 创建体检催检日志
     *
     * @param createReqVO 创建信息（占位，后续替换为 ExamReminderLogSaveReqVO）
     * @return 编号
     */
    Long createExamReminderLog(Object createReqVO);

    /**
     * 更新体检催检日志
     *
     * @param updateReqVO 更新信息（占位，后续替换为 ExamReminderLogSaveReqVO）
     */
    void updateExamReminderLog(Object updateReqVO);

    /**
     * 删除体检催检日志
     *
     * @param id 编号
     */
    void deleteExamReminderLog(Long id);

    /**
     * 获得体检催检日志
     *
     * @param id 编号
     * @return 体检催检日志
     */
    ExamReminderLogDO getExamReminderLog(Long id);

    /**
     * 获得体检催检日志分页
     *
     * @param pageParam 分页查询（占位，后续替换为 ExamReminderLogPageReqVO）
     * @return 体检催检日志分页
     */
    PageResult<ExamReminderLogDO> getExamReminderLogPage(PageParam pageParam);

    /**
     * 业务方法：手动补发推送
     * 对失败或漏发的日志进行手动补发，并更新日志状态为已补发
     *
     * @param logId 日志ID
     */
    void resendPush(Long logId);

    /**
     * 业务方法：取消推送
     * 对待发送状态的日志进行取消，并更新日志状态为已取消
     *
     * @param logId 日志ID
     */
    void cancelPush(Long logId);

    /**
     * 按规则ID查询推送日志列表
     *
     * @param ruleId 规则ID
     * @return 日志列表
     */
    List<ExamReminderLogDO> selectListByRuleId(Long ruleId);

}
