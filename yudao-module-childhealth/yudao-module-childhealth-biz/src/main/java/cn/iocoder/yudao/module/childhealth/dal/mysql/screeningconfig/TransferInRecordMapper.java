package cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.TransferInRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

/**
 * 转入档案 Mapper
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@Mapper
public interface TransferInRecordMapper extends BaseMapperX<TransferInRecordDO> {

    /**
     * 按转入编号查询
     */
    default TransferInRecordDO selectByTransferNo(String transferNo) {
        return selectOne(TransferInRecordDO::getTransferNo, transferNo);
    }

    /**
     * 按儿童ID查询转入记录
     */
    default List<TransferInRecordDO> selectListByChildId(Long childId) {
        return selectList(new LambdaQueryWrapperX<TransferInRecordDO>()
                .eqIfPresent(TransferInRecordDO::getChildId, childId)
                .orderByDesc(TransferInRecordDO::getTransferDate));
    }

    /**
     * 按接诊状态查询
     */
    default List<TransferInRecordDO> selectListByReceiveStatus(Integer receiveStatus) {
        return selectList(new LambdaQueryWrapperX<TransferInRecordDO>()
                .eqIfPresent(TransferInRecordDO::getReceiveStatus, receiveStatus)
                .orderByDesc(TransferInRecordDO::getTransferDate));
    }

    /**
     * 按来源机构编码查询
     */
    default List<TransferInRecordDO> selectListBySourceOrgCode(String sourceOrgCode) {
        return selectList(new LambdaQueryWrapperX<TransferInRecordDO>()
                .eqIfPresent(TransferInRecordDO::getSourceOrgCode, sourceOrgCode)
                .orderByDesc(TransferInRecordDO::getTransferDate));
    }

    /**
     * 按接诊医生ID查询
     */
    default List<TransferInRecordDO> selectListByReceiveDoctorId(Long receiveDoctorId) {
        return selectList(new LambdaQueryWrapperX<TransferInRecordDO>()
                .eqIfPresent(TransferInRecordDO::getReceiveDoctorId, receiveDoctorId)
                .orderByDesc(TransferInRecordDO::getReceiveDate));
    }

    /**
     * 按转入日期范围查询
     */
    default List<TransferInRecordDO> selectListByDateRange(LocalDate startDate, LocalDate endDate) {
        return selectList(new LambdaQueryWrapperX<TransferInRecordDO>()
                .geIfPresent(TransferInRecordDO::getTransferDate, startDate)
                .leIfPresent(TransferInRecordDO::getTransferDate, endDate)
                .orderByDesc(TransferInRecordDO::getTransferDate));
    }

}
