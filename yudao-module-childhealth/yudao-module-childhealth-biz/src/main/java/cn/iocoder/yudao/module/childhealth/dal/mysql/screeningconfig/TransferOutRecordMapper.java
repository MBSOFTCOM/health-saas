package cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.TransferOutRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 转出档案 Mapper
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@Mapper
public interface TransferOutRecordMapper extends BaseMapperX<TransferOutRecordDO> {

    /**
     * 按转出编号查询
     */
    default TransferOutRecordDO selectByTransferNo(String transferNo) {
        return selectOne(TransferOutRecordDO::getTransferNo, transferNo);
    }

    /**
     * 按儿童ID查询转出记录
     */
    default List<TransferOutRecordDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<TransferOutRecordDO>()
                .eqIfPresent(TransferOutRecordDO::getChildId, childId)
                .orderByDesc(TransferOutRecordDO::getTransferDate));
    }

    /**
     * 按跟踪状态查询
     */
    default List<TransferOutRecordDO> selectListByFollowupStatus(Integer followupStatus) {
        return selectList(new LambdaQueryWrapperX<TransferOutRecordDO>()
                .eqIfPresent(TransferOutRecordDO::getFollowupStatus, followupStatus)
                .orderByDesc(TransferOutRecordDO::getTransferDate));
    }

    /**
     * 按阳性等级查询
     */
    default List<TransferOutRecordDO> selectListByPositiveLevel(Integer positiveLevel) {
        return selectList(new LambdaQueryWrapperX<TransferOutRecordDO>()
                .eqIfPresent(TransferOutRecordDO::getPositiveLevel, positiveLevel)
                .orderByDesc(TransferOutRecordDO::getTransferDate));
    }

    /**
     * 按目标机构编码查询
     */
    default List<TransferOutRecordDO> selectListByTargetOrgCode(String targetOrgCode) {
        return selectList(new LambdaQueryWrapperX<TransferOutRecordDO>()
                .eqIfPresent(TransferOutRecordDO::getTargetOrgCode, targetOrgCode)
                .orderByDesc(TransferOutRecordDO::getTransferDate));
    }

    /**
     * 按转出日期范围查询
     */
    default List<TransferOutRecordDO> selectListByDateRange(LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<TransferOutRecordDO>()
                .geIfPresent(TransferOutRecordDO::getTransferDate, startDate)
                .leIfPresent(TransferOutRecordDO::getTransferDate, endDate)
                .orderByDesc(TransferOutRecordDO::getTransferDate));
    }

}
