package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.ScreeningPositiveDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 筛查阳性记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ScreeningPositiveMapper extends BaseMapperX<ScreeningPositiveDO> {

    default PageResult<ScreeningPositiveDO> selectPage(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.positive.ScreeningPositivePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreeningPositiveDO>()
                .eqIfPresent(ScreeningPositiveDO::getRecordId, reqVO.getRecordId())
                .eqIfPresent(ScreeningPositiveDO::getStudentId, reqVO.getStudentId())
                .likeIfPresent(ScreeningPositiveDO::getDiseaseCode, reqVO.getDiseaseCode())
                .likeIfPresent(ScreeningPositiveDO::getDiseaseName, reqVO.getDiseaseName())
                .eqIfPresent(ScreeningPositiveDO::getPositiveLevel, reqVO.getPositiveLevel())
                .eqIfPresent(ScreeningPositiveDO::getNeedRecheck, reqVO.getNeedRecheck())
                .eqIfPresent(ScreeningPositiveDO::getRecheckStatus, reqVO.getRecheckStatus())
                .orderByDesc(ScreeningPositiveDO::getId));
    }

    default List<ScreeningPositiveDO> selectByRecordId(Long recordId) {
        return selectList(new LambdaQueryWrapperX<ScreeningPositiveDO>()
                .eq(ScreeningPositiveDO::getRecordId, recordId)
                .orderByAsc(ScreeningPositiveDO::getId));
    }

    default List<ScreeningPositiveDO> selectByStudentId(Long studentId) {
        return selectList(new LambdaQueryWrapperX<ScreeningPositiveDO>()
                .eq(ScreeningPositiveDO::getStudentId, studentId)
                .orderByDesc(ScreeningPositiveDO::getId));
    }

}