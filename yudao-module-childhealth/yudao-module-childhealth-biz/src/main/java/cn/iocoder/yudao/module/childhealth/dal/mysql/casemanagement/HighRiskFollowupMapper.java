package cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.HighRiskFollowupDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 高危儿随访 Mapper
 *
 * 模块: B.高危儿专案管理
 * 创建日期: 2026-07-20
 */
@Mapper
public interface HighRiskFollowupMapper extends BaseMapperX<HighRiskFollowupDO> {

    /**
     * 按随访编号查询
     */
    default HighRiskFollowupDO selectByFollowupNo(String followupNo) {
        return selectOne(HighRiskFollowupDO::getFollowupNo, followupNo);
    }

    /**
     * 按高危新生儿ID查询随访列表
     */
    default List<HighRiskFollowupDO> selectListByNewbornId(Long newbornId) {
        return selectList(new LambdaQueryWrapperX<HighRiskFollowupDO>()
                .eqIfPresent(HighRiskFollowupDO::getNewbornId, newbornId)
                .orderByAsc(HighRiskFollowupDO::getFollowupDate));
    }

    /**
     * 按儿童ID查询随访列表
     */
    default List<HighRiskFollowupDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<HighRiskFollowupDO>()
                .eqIfPresent(HighRiskFollowupDO::getChildId, childId)
                .orderByAsc(HighRiskFollowupDO::getFollowupDate));
    }

    /**
     * 按随访类型查询列表
     */
    default List<HighRiskFollowupDO> selectListByFollowupType(Integer followupType) {
        return selectList(new LambdaQueryWrapperX<HighRiskFollowupDO>()
                .eqIfPresent(HighRiskFollowupDO::getFollowupType, followupType)
                .orderByDesc(HighRiskFollowupDO::getFollowupDate));
    }

    /**
     * 按医生ID查询随访列表
     */
    default List<HighRiskFollowupDO> selectListByDoctorId(Long doctorId) {
        return selectList(new LambdaQueryWrapperX<HighRiskFollowupDO>()
                .eqIfPresent(HighRiskFollowupDO::getDoctorId, doctorId)
                .orderByDesc(HighRiskFollowupDO::getFollowupDate));
    }

}
