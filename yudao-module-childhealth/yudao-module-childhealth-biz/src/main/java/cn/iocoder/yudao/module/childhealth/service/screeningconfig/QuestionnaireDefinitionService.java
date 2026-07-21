package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.QuestionnaireDefinitionDO;

import java.util.List;

/**
 * 问卷定义 Service 接口
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface QuestionnaireDefinitionService {

    /**
     * 创建问卷定义
     *
     * @param saveReqVO 创建信息（后续替换为 QuestionnaireDefinitionSaveReqVO）
     * @return 编号
     */
    Long createQuestionnaireDefinition(Object saveReqVO);

    /**
     * 更新问卷定义
     *
     * @param saveReqVO 更新信息（后续替换为 QuestionnaireDefinitionSaveReqVO）
     */
    void updateQuestionnaireDefinition(Object saveReqVO);

    /**
     * 删除问卷定义
     *
     * @param id 编号
     */
    void deleteQuestionnaireDefinition(Long id);

    /**
     * 获得问卷定义
     *
     * @param id 编号
     * @return 问卷定义
     */
    QuestionnaireDefinitionDO getQuestionnaireDefinition(Long id);

    /**
     * 获得问卷定义分页
     *
     * @param pageParam 分页查询（后续替换为 QuestionnaireDefinitionPageReqVO）
     * @return 问卷定义分页
     */
    PageResult<QuestionnaireDefinitionDO> getQuestionnaireDefinitionPage(PageParam pageParam);

    /**
     * 按问卷编码查询
     *
     * @param code 问卷编码
     * @return 问卷定义
     */
    QuestionnaireDefinitionDO selectByCode(String code);

    /**
     * 查询所有启用问卷
     *
     * @return 启用问卷列表
     */
    List<QuestionnaireDefinitionDO> selectActiveList();

}
