package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.RecheckNotifyRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 复筛通知记录 Mapper
 *
 * 创建日期: 2026-07-20
 */
@Mapper
public interface RecheckNotifyRecordMapper extends BaseMapperX<RecheckNotifyRecordDO> {

    /**
     * 按阳性记录ID查询所有通知记录
     */
    default List<RecheckNotifyRecordDO> selectListByPositive(Long positiveId) {
        return selectList(new LambdaQueryWrapperX<RecheckNotifyRecordDO>()
                .eqIfPresent(RecheckNotifyRecordDO::getPositiveId, positiveId)
                .orderByDesc(RecheckNotifyRecordDO::getNotifyTime));
    }

    /**
     * 按学生ID查询所有通知记录
     */
    default List<RecheckNotifyRecordDO> selectListByStudent(Long studentId) {
        return selectList(new LambdaQueryWrapperX<RecheckNotifyRecordDO>()
                .eqIfPresent(RecheckNotifyRecordDO::getStudentId, studentId)
                .orderByDesc(RecheckNotifyRecordDO::getNotifyTime));
    }

    /**
     * 按学校ID查询通知记录（用于复筛工作台）
     */
    default List<RecheckNotifyRecordDO> selectListBySchool(Long schoolId) {
        return selectList(new LambdaQueryWrapperX<RecheckNotifyRecordDO>()
                .eqIfPresent(RecheckNotifyRecordDO::getSchoolId, schoolId)
                .orderByDesc(RecheckNotifyRecordDO::getNotifyTime));
    }

    /**
     * 按通知状态查询（用于统计）
     */
    default List<RecheckNotifyRecordDO> selectListByStatus(Integer notifyStatus) {
        return selectList(new LambdaQueryWrapperX<RecheckNotifyRecordDO>()
                .eqIfPresent(RecheckNotifyRecordDO::getNotifyStatus, notifyStatus));
    }

    /**
     * 按复筛记录ID查询关联通知
     */
    default List<RecheckNotifyRecordDO> selectListByRecheck(Long recheckId) {
        return selectList(new LambdaQueryWrapperX<RecheckNotifyRecordDO>()
                .eqIfPresent(RecheckNotifyRecordDO::getRecheckId, recheckId));
    }

}
