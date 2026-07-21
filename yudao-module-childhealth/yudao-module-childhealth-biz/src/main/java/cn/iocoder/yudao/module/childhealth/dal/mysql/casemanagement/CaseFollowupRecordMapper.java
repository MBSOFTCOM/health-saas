package cn.iocoder.yudao.module.childhealth.dal.mysql.casemanagement;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.casemanagement.CaseFollowupRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 专案随访记录 Mapper
 *
 * 模块: B.专案管理
 * 创建日期: 2026-07-20
 */
@Mapper
public interface CaseFollowupRecordMapper extends BaseMapperX<CaseFollowupRecordDO> {

    /**
     * 按随访编号查询
     */
    default CaseFollowupRecordDO selectByFollowupNo(String followupNo) {
        return selectOne(CaseFollowupRecordDO::getFollowupNo, followupNo);
    }

    /**
     * 按专案ID查询随访记录列表
     */
    default List<CaseFollowupRecordDO> selectListByCaseId(Long caseId) {
        return selectList(new LambdaQueryWrapperX<CaseFollowupRecordDO>()
                .eqIfPresent(CaseFollowupRecordDO::getCaseId, caseId)
                .orderByAsc(CaseFollowupRecordDO::getFollowupDate));
    }

    /**
     * 按儿童ID查询随访记录列表
     */
    default List<CaseFollowupRecordDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<CaseFollowupRecordDO>()
                .eqIfPresent(CaseFollowupRecordDO::getChildId, childId)
                .orderByAsc(CaseFollowupRecordDO::getFollowupDate));
    }

    /**
     * 按随访医生ID查询列表
     */
    default List<CaseFollowupRecordDO> selectListByDoctorId(Long doctorId) {
        return selectList(new LambdaQueryWrapperX<CaseFollowupRecordDO>()
                .eqIfPresent(CaseFollowupRecordDO::getDoctorId, doctorId)
                .orderByDesc(CaseFollowupRecordDO::getFollowupDate));
    }

    /**
     * 按随访类型查询列表
     */
    default List<CaseFollowupRecordDO> selectListByFollowupType(Integer followupType) {
        return selectList(new LambdaQueryWrapperX<CaseFollowupRecordDO>()
                .eqIfPresent(CaseFollowupRecordDO::getFollowupType, followupType)
                .orderByDesc(CaseFollowupRecordDO::getFollowupDate));
    }

}
