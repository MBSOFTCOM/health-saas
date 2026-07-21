package cn.iocoder.yudao.module.childhealth.service.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderRuleDO;

import java.util.List;

/**
 * 体检催检规则 Service 接口
 *
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
public interface ExamReminderRuleService {

    /**
     * 创建体检催检规则
     *
     * @param createReqVO 创建信息（占位，后续替换为 ExamReminderRuleSaveReqVO）
     * @return 编号
     */
    Long createExamReminderRule(Object createReqVO);

    /**
     * 更新体检催检规则
     *
     * @param updateReqVO 更新信息（占位，后续替换为 ExamReminderRuleSaveReqVO）
     */
    void updateExamReminderRule(Object updateReqVO);

    /**
     * 删除体检催检规则
     *
     * @param id 编号
     */
    void deleteExamReminderRule(Long id);

    /**
     * 获得体检催检规则
     *
     * @param id 编号
     * @return 体检催检规则
     */
    ExamReminderRuleDO getExamReminderRule(Long id);

    /**
     * 获得体检催检规则分页
     *
     * @param pageParam 分页查询（占位，后续替换为 ExamReminderRulePageReqVO）
     * @return 体检催检规则分页
     */
    PageResult<ExamReminderRuleDO> getExamReminderRulePage(PageParam pageParam);

    /**
     * 查询所有启用的催检规则
     *
     * @return 启用的规则列表
     */
    List<ExamReminderRuleDO> selectActiveList();

    /**
     * 按体检类型查询规则
     *
     * @param examType 体检类型
     * @return 规则列表
     */
    List<ExamReminderRuleDO> selectByExamType(String examType);

}
