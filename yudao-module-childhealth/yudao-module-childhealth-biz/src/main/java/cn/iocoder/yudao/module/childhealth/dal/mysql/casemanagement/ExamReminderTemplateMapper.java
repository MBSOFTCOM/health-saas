package cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.ExamReminderTemplateDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 体检提醒模板 Mapper
 *
 * 模块: B.体检催检
 * 创建日期: 2026-07-20
 */
@Mapper
public interface ExamReminderTemplateMapper extends BaseMapperX<ExamReminderTemplateDO> {

    /**
     * 按模板编码查询
     */
    default ExamReminderTemplateDO selectByTemplateCode(String templateCode) {
        return selectOne(ExamReminderTemplateDO::getTemplateCode, templateCode);
    }

    /**
     * 按模板类型查询列表
     */
    default List<ExamReminderTemplateDO> selectListByTemplateType(Integer templateType) {
        return selectList(new LambdaQueryWrapperX<ExamReminderTemplateDO>()
                .eqIfPresent(ExamReminderTemplateDO::getTemplateType, templateType)
                .orderByAsc(ExamReminderTemplateDO::getTemplateCode));
    }

    /**
     * 按推送渠道查询列表
     */
    default List<ExamReminderTemplateDO> selectListByChannel(String channel) {
        return selectList(new LambdaQueryWrapperX<ExamReminderTemplateDO>()
                .eqIfPresent(ExamReminderTemplateDO::getChannel, channel)
                .orderByAsc(ExamReminderTemplateDO::getTemplateCode));
    }

    /**
     * 按科室ID查询列表
     */
    default List<ExamReminderTemplateDO> selectListByDeptId(Long deptId) {
        return selectList(new LambdaQueryWrapperX<ExamReminderTemplateDO>()
                .eqIfPresent(ExamReminderTemplateDO::getDeptId, deptId)
                .orderByAsc(ExamReminderTemplateDO::getTemplateCode));
    }

    /**
     * 按推送渠道和状态查询列表
     */
    default List<ExamReminderTemplateDO> selectListByChannelAndStatus(String channel, Integer status) {
        return selectList(new LambdaQueryWrapperX<ExamReminderTemplateDO>()
                .eqIfPresent(ExamReminderTemplateDO::getChannel, channel)
                .eqIfPresent(ExamReminderTemplateDO::getStatus, status)
                .orderByAsc(ExamReminderTemplateDO::getTemplateCode));
    }

    /**
     * 查询所有启用的提醒模板
     */
    default List<ExamReminderTemplateDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<ExamReminderTemplateDO>()
                .eqIfPresent(ExamReminderTemplateDO::getStatus, 1)
                .orderByAsc(ExamReminderTemplateDO::getTemplateType)
                .orderByAsc(ExamReminderTemplateDO::getTemplateCode));
    }

}
