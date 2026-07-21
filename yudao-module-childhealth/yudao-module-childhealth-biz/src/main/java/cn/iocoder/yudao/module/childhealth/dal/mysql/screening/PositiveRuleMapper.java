package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.PositiveRuleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 阳性判定规则 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PositiveRuleMapper extends BaseMapperX<PositiveRuleDO> {

    default PageResult<PositiveRuleDO> selectPage(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule.PositiveRulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PositiveRuleDO>()
                .likeIfPresent(PositiveRuleDO::getRuleCode, reqVO.getRuleCode())
                .likeIfPresent(PositiveRuleDO::getRuleName, reqVO.getRuleName())
                .eqIfPresent(PositiveRuleDO::getPositiveLevel, reqVO.getPositiveLevel())
                .likeIfPresent(PositiveRuleDO::getDiseaseCode, reqVO.getDiseaseCode())
                .eqIfPresent(PositiveRuleDO::getStatus, reqVO.getStatus())
                .orderByDesc(PositiveRuleDO::getId));
    }

    default List<PositiveRuleDO> selectList(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.rule.PositiveRuleListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<PositiveRuleDO>()
                .likeIfPresent(PositiveRuleDO::getRuleCode, reqVO.getRuleCode())
                .likeIfPresent(PositiveRuleDO::getRuleName, reqVO.getRuleName())
                .eqIfPresent(PositiveRuleDO::getStatus, reqVO.getStatus())
                .orderByDesc(PositiveRuleDO::getId));
    }

    default PositiveRuleDO selectByRuleCode(String ruleCode) {
        return selectOne(PositiveRuleDO::getRuleCode, ruleCode);
    }

    default List<PositiveRuleDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<PositiveRuleDO>()
                .eq(PositiveRuleDO::getStatus, 1)
                .orderByAsc(PositiveRuleDO::getId));
    }

}