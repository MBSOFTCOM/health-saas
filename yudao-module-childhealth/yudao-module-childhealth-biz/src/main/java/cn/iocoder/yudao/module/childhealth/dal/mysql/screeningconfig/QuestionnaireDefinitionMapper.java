package cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.QuestionnaireDefinitionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 问卷定义 Mapper
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@Mapper
public interface QuestionnaireDefinitionMapper extends BaseMapperX<QuestionnaireDefinitionDO> {

    /**
     * 按问卷编码查询
     */
    default QuestionnaireDefinitionDO selectByCode(String code) {
        return selectOne(QuestionnaireDefinitionDO::getCode, code);
    }

    /**
     * 按类型查询启用问卷
     */
    default List<QuestionnaireDefinitionDO> selectListByType(Integer type) {
        return selectList(new LambdaQueryWrapperX<QuestionnaireDefinitionDO>()
                .eqIfPresent(QuestionnaireDefinitionDO::getType, type)
                .eqIfPresent(QuestionnaireDefinitionDO::getStatus, 1)
                .orderByDesc(QuestionnaireDefinitionDO::getVersion));
    }

    /**
     * 按适用月龄范围查询启用问卷
     */
    default List<QuestionnaireDefinitionDO> selectListByApplicableAge(Integer ageMonth) {
        return selectList(new LambdaQueryWrapperX<QuestionnaireDefinitionDO>()
                .leIfPresent(QuestionnaireDefinitionDO::getApplicableAgeMin, ageMonth)
                .geIfPresent(QuestionnaireDefinitionDO::getApplicableAgeMax, ageMonth)
                .eqIfPresent(QuestionnaireDefinitionDO::getStatus, 1));
    }

    /**
     * 按名称模糊查询启用问卷
     */
    default List<QuestionnaireDefinitionDO> selectListByNameLike(String name) {
        return selectList(new LambdaQueryWrapperX<QuestionnaireDefinitionDO>()
                .likeIfPresent(QuestionnaireDefinitionDO::getName, name)
                .eqIfPresent(QuestionnaireDefinitionDO::getStatus, 1));
    }

    /**
     * 查询所有启用问卷
     */
    default List<QuestionnaireDefinitionDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<QuestionnaireDefinitionDO>()
                .eqIfPresent(QuestionnaireDefinitionDO::getStatus, 1)
                .orderByAsc(QuestionnaireDefinitionDO::getType)
                .orderByDesc(QuestionnaireDefinitionDO::getVersion));
    }

}
