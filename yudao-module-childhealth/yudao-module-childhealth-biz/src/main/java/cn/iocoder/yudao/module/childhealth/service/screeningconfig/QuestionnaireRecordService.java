package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.QuestionnaireRecordDO;

import java.util.List;

/**
 * 问卷答卷 Service 接口
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface QuestionnaireRecordService {

    /**
     * 创建问卷答卷
     *
     * @param saveReqVO 创建信息（后续替换为 QuestionnaireRecordSaveReqVO）
     * @return 编号
     */
    Long createQuestionnaireRecord(Object saveReqVO);

    /**
     * 更新问卷答卷
     *
     * @param saveReqVO 更新信息（后续替换为 QuestionnaireRecordSaveReqVO）
     */
    void updateQuestionnaireRecord(Object saveReqVO);

    /**
     * 删除问卷答卷
     *
     * @param id 编号
     */
    void deleteQuestionnaireRecord(Long id);

    /**
     * 获得问卷答卷
     *
     * @param id 编号
     * @return 问卷答卷
     */
    QuestionnaireRecordDO getQuestionnaireRecord(Long id);

    /**
     * 获得问卷答卷分页
     *
     * @param pageParam 分页查询（后续替换为 QuestionnaireRecordPageReqVO）
     * @return 问卷答卷分页
     */
    PageResult<QuestionnaireRecordDO> getQuestionnaireRecordPage(PageParam pageParam);

    /**
     * 自动计分：根据问卷题型与答案计算总分，回写答卷
     *
     * @param id 答卷ID
     */
    void autoScore(Long id);

    /**
     * 按问卷ID及儿童ID查询答卷
     *
     * @param questionnaireId 问卷ID
     * @param childId 儿童ID
     * @return 答卷列表
     */
    List<QuestionnaireRecordDO> selectByQuestionnaireAndChild(Long questionnaireId, Long childId);

}
