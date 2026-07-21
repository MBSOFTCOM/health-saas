package cn.iocoder.yudao.module.childhealth.service.casemanagement;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderTemplateDO;

import java.util.List;

/**
 * 体检提醒模板 Service 接口
 *
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
public interface ExamReminderTemplateService {

    /**
     * 创建体检提醒模板
     *
     * @param createReqVO 创建信息（占位，后续替换为 ExamReminderTemplateSaveReqVO）
     * @return 编号
     */
    Long createExamReminderTemplate(Object createReqVO);

    /**
     * 更新体检提醒模板
     *
     * @param updateReqVO 更新信息（占位，后续替换为 ExamReminderTemplateSaveReqVO）
     */
    void updateExamReminderTemplate(Object updateReqVO);

    /**
     * 删除体检提醒模板
     *
     * @param id 编号
     */
    void deleteExamReminderTemplate(Long id);

    /**
     * 获得体检提醒模板
     *
     * @param id 编号
     * @return 体检提醒模板
     */
    ExamReminderTemplateDO getExamReminderTemplate(Long id);

    /**
     * 获得体检提醒模板分页
     *
     * @param pageParam 分页查询（占位，后续替换为 ExamReminderTemplatePageReqVO）
     * @return 体检提醒模板分页
     */
    PageResult<ExamReminderTemplateDO> getExamReminderTemplatePage(PageParam pageParam);

    /**
     * 按模板编码查询模板
     *
     * @param templateCode 模板编码
     * @return 模板
     */
    ExamReminderTemplateDO selectByCode(String templateCode);

    /**
     * 查询所有启用的提醒模板
     *
     * @return 启用的模板列表
     */
    List<ExamReminderTemplateDO> selectActiveList();

}
