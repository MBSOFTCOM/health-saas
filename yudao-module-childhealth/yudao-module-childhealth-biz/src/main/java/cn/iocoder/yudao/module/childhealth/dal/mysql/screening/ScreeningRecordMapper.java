package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 筛查记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ScreeningRecordMapper extends BaseMapperX<ScreeningRecordDO> {

    default PageResult<ScreeningRecordDO> selectPage(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.record.ScreeningRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreeningRecordDO>()
                .likeIfPresent(ScreeningRecordDO::getRecordNo, reqVO.getRecordNo())
                .eqIfPresent(ScreeningRecordDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(ScreeningRecordDO::getStudentId, reqVO.getStudentId())
                .betweenIfPresent(ScreeningRecordDO::getScreeningDate, reqVO.getScreeningDate())
                .eqIfPresent(ScreeningRecordDO::getCheckStatus, reqVO.getCheckStatus())
                .eqIfPresent(ScreeningRecordDO::getHasPositive, reqVO.getHasPositive())
                .orderByDesc(ScreeningRecordDO::getId));
    }

    default List<ScreeningRecordDO> selectList(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.record.ScreeningRecordListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ScreeningRecordDO>()
                .eqIfPresent(ScreeningRecordDO::getBatchId, reqVO.getBatchId())
                .eqIfPresent(ScreeningRecordDO::getStudentId, reqVO.getStudentId())
                .eqIfPresent(ScreeningRecordDO::getCheckStatus, reqVO.getCheckStatus())
                .eqIfPresent(ScreeningRecordDO::getHasPositive, reqVO.getHasPositive())
                .orderByDesc(ScreeningRecordDO::getId));
    }

    default ScreeningRecordDO selectByRecordNo(String recordNo) {
        return selectOne(ScreeningRecordDO::getRecordNo, recordNo);
    }

    default ScreeningRecordDO selectByBatchIdAndStudentId(Long batchId, Long studentId) {
        return selectOne(new LambdaQueryWrapperX<ScreeningRecordDO>()
                .eq(ScreeningRecordDO::getBatchId, batchId)
                .eq(ScreeningRecordDO::getStudentId, studentId));
    }

}