package cn.iocoder.yudao.module.childhealth.dal.mysql.medical;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentScaleQuestionDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 发育评估量表题目表 Mapper
 *
 * 模块: A. 儿童基础健康检查（A7-发育评估量表题目表）
 * 创建日期: 2026-07-20
 */
@Mapper
public interface DevelopmentScaleQuestionMapper extends BaseMapperX<DevelopmentScaleQuestionDO> {

    /**
     * 按量表ID查询所有题目（按排序字段升序）
     */
    default List<DevelopmentScaleQuestionDO> selectListByScaleId(Long scaleId) {
        return selectList(new LambdaQueryWrapperX<DevelopmentScaleQuestionDO>()
                .eqIfPresent(DevelopmentScaleQuestionDO::getScaleId, scaleId)
                .orderByAsc(DevelopmentScaleQuestionDO::getSort));
    }

    /**
     * 按量表ID和题号查询单个题目
     */
    default DevelopmentScaleQuestionDO selectByQuestionNo(Long scaleId, Integer questionNo) {
        return selectOne(new LambdaQueryWrapperX<DevelopmentScaleQuestionDO>()
                .eqIfPresent(DevelopmentScaleQuestionDO::getScaleId, scaleId)
                .eqIfPresent(DevelopmentScaleQuestionDO::getQuestionNo, questionNo));
    }

    /**
     * 按量表ID和维度查询题目
     */
    default List<DevelopmentScaleQuestionDO> selectListByDimension(Long scaleId, String dimension) {
        return selectList(new LambdaQueryWrapperX<DevelopmentScaleQuestionDO>()
                .eqIfPresent(DevelopmentScaleQuestionDO::getScaleId, scaleId)
                .eqIfPresent(DevelopmentScaleQuestionDO::getDimension, dimension)
                .orderByAsc(DevelopmentScaleQuestionDO::getSort));
    }

    /**
     * 统计量表的题目数量
     */
    default Long selectCountByScaleId(Long scaleId) {
        return selectCount(new LambdaQueryWrapperX<DevelopmentScaleQuestionDO>()
                .eqIfPresent(DevelopmentScaleQuestionDO::getScaleId, scaleId));
    }

    /**
     * 按量表ID和反向计分标记查询题目
     */
    default List<DevelopmentScaleQuestionDO> selectListByReverseScore(Long scaleId, Integer reverseScore) {
        return selectList(new LambdaQueryWrapperX<DevelopmentScaleQuestionDO>()
                .eqIfPresent(DevelopmentScaleQuestionDO::getScaleId, scaleId)
                .eqIfPresent(DevelopmentScaleQuestionDO::getReverseScore, reverseScore)
                .orderByAsc(DevelopmentScaleQuestionDO::getSort));
    }

}
