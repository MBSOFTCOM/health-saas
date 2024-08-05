package cn.iocoder.yudao.module.ppd.dal.mysql.screendiagnosis;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.LastTimeDiagnosisResultRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.ScreenDiagnosisDataVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.ScreenDiagnosisPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screendiagnosis.ScreenDiagnosisDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 诊断组 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenDiagnosisMapper extends BaseMapperX<ScreenDiagnosisDO> {

    default PageResult<ScreenDiagnosisDO> selectPage(ScreenDiagnosisPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenDiagnosisDO>()
                .eqIfPresent(ScreenDiagnosisDO::getScreenId, reqVO.getScreenId())
                .eqIfPresent(ScreenDiagnosisDO::getSyncId, reqVO.getSyncId())
                .eqIfPresent(ScreenDiagnosisDO::getDoctorSignature, reqVO.getDoctorSignature())
                .betweenIfPresent(ScreenDiagnosisDO::getScreenTime, reqVO.getScreenTime())
                .eqIfPresent(ScreenDiagnosisDO::getOutcome, reqVO.getOutcome())
                .eqIfPresent(ScreenDiagnosisDO::getTreatmentProgram, reqVO.getTreatmentProgram())
                .eqIfPresent(ScreenDiagnosisDO::getReport, reqVO.getReport())
                .eqIfPresent(ScreenDiagnosisDO::getPreventiveTreatment, reqVO.getPreventiveTreatment())
                .eqIfPresent(ScreenDiagnosisDO::getScreenOrder, reqVO.getScreenOrder())
                .eqIfPresent(ScreenDiagnosisDO::getPersonId, reqVO.getPersonId())
                .eqIfPresent(ScreenDiagnosisDO::getScreenPoint, reqVO.getScreenPoint())
                .orderByDesc(ScreenDiagnosisDO::getId));
    }
}