package cn.iocoder.yudao.module.childhealth.dal.mysql.workflow;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.transfer.vo.TransferArchivePageReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.TransferArchiveDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TransferArchiveMapper extends BaseMapperX<TransferArchiveDO> {

    default PageResult<TransferArchiveDO> selectPage(TransferArchivePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TransferArchiveDO>()
                .likeIfPresent(TransferArchiveDO::getTransferNo, reqVO.getTransferNo())
                .eqIfPresent(TransferArchiveDO::getChildId, reqVO.getChildId())
                .eqIfPresent(TransferArchiveDO::getTransferType, reqVO.getTransferType())
                .betweenIfPresent(TransferArchiveDO::getTransferDate, reqVO.getTransferDate())
                .eqIfPresent(TransferArchiveDO::getStatus, reqVO.getStatus())
                .orderByDesc(TransferArchiveDO::getId));
    }

    default TransferArchiveDO selectByTransferNo(String transferNo) {
        return selectOne(TransferArchiveDO::getTransferNo, transferNo);
    }

}
