package cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskNewbornDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 高危新生儿台账 Mapper
 *
 * 模块: B.高危儿专案管理
 * 创建日期: 2026-07-20
 */
@Mapper
public interface HighRiskNewbornMapper extends BaseMapperX<HighRiskNewbornDO> {

    /**
     * 按儿童档案ID查询
     */
    default HighRiskNewbornDO selectByChildId(Long childId) {
        return selectOne(HighRiskNewbornDO::getChildId, childId);
    }

    /**
     * 按随访状态查询列表
     */
    default List<HighRiskNewbornDO> selectListByFollowupStatus(Integer followupStatus) {
        return selectList(new LambdaQueryWrapperX<HighRiskNewbornDO>()
                .eqIfPresent(HighRiskNewbornDO::getFollowupStatus, followupStatus)
                .orderByDesc(HighRiskNewbornDO::getRegisterDate));
    }

    /**
     * 按是否建册查询列表
     */
    default List<HighRiskNewbornDO> selectListByIsRegistered(Integer isRegistered) {
        return selectList(new LambdaQueryWrapperX<HighRiskNewbornDO>()
                .eqIfPresent(HighRiskNewbornDO::getIsRegistered, isRegistered)
                .orderByDesc(HighRiskNewbornDO::getRegisterDate));
    }

    /**
     * 按关联专案ID查询
     */
    default HighRiskNewbornDO selectByCaseId(Long caseId) {
        return selectOne(HighRiskNewbornDO::getCaseId, caseId);
    }

    /**
     * 按高危类型模糊查询
     */
    default List<HighRiskNewbornDO> selectListByHighRiskType(String highRiskType) {
        return selectList(new LambdaQueryWrapperX<HighRiskNewbornDO>()
                .likeIfPresent(HighRiskNewbornDO::getHighRiskTypes, highRiskType)
                .orderByDesc(HighRiskNewbornDO::getRegisterDate));
    }

}
