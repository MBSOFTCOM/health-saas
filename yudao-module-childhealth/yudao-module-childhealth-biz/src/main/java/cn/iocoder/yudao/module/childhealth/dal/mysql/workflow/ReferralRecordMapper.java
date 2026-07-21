package cn.iocoder.yudao.module.childhealth.dal.mysql.workflow;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.referral.vo.ReferralRecordPageReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ReferralRecordDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReferralRecordMapper extends BaseMapperX<ReferralRecordDO> {

    default PageResult<ReferralRecordDO> selectPage(ReferralRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ReferralRecordDO>()
                .likeIfPresent(ReferralRecordDO::getReferralNo, reqVO.getReferralNo())
                .eqIfPresent(ReferralRecordDO::getStudentId, reqVO.getStudentId())
                .eqIfPresent(ReferralRecordDO::getPositiveId, reqVO.getPositiveId())
                .eqIfPresent(ReferralRecordDO::getReferralStatus, reqVO.getReferralStatus())
                .orderByDesc(ReferralRecordDO::getId));
    }

    default ReferralRecordDO selectByReferralNo(String referralNo) {
        return selectOne(ReferralRecordDO::getReferralNo, referralNo);
    }

    /**
     * 按学生ID列表统计转介数（用于统计接口聚合）
     */
    default Long countByStudentIds(List<Long> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            return 0L;
        }
        return selectCount(new LambdaQueryWrapperX<ReferralRecordDO>()
                .in(ReferralRecordDO::getStudentId, studentIds));
    }

    /**
     * 按阳性记录ID列表统计转介数
     */
    default Long countByPositiveIds(List<Long> positiveIds) {
        if (positiveIds == null || positiveIds.isEmpty()) {
            return 0L;
        }
        return selectCount(new LambdaQueryWrapperX<ReferralRecordDO>()
                .in(ReferralRecordDO::getPositiveId, positiveIds));
    }

    /**
     * 查询全部转介记录数（用于无过滤条件的汇总）
     */
    default Long countAll() {
        return selectCount(null);
    }

}
