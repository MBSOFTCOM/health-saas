package cn.iocoder.yudao.module.ppd.dal.mysql.screensputumexamination;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo.ScreenSputumExaminationPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensputumexamination.ScreenSputumExaminationDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 痰检组 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenSputumExaminationMapper extends BaseMapperX<ScreenSputumExaminationDO> {

    default PageResult<ScreenSputumExaminationDO> selectPage(ScreenSputumExaminationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenSputumExaminationDO>()
                .eqIfPresent(ScreenSputumExaminationDO::getScreenId, reqVO.getScreenId())
                .eqIfPresent(ScreenSputumExaminationDO::getAtomization, reqVO.getAtomization())
                .eqIfPresent(ScreenSputumExaminationDO::getSyncId, reqVO.getSyncId())
                .eqIfPresent(ScreenSputumExaminationDO::getDoctorSignature, reqVO.getDoctorSignature())
                .eqIfPresent(ScreenSputumExaminationDO::getSputumExamination, reqVO.getSputumExamination())
                .eqIfPresent(ScreenSputumExaminationDO::getType, reqVO.getType())
                .eqIfPresent(ScreenSputumExaminationDO::getForthwithSputum, reqVO.getForthwithSputum())
                .eqIfPresent(ScreenSputumExaminationDO::getForthwithSputumCode, reqVO.getForthwithSputumCode())
                .betweenIfPresent(ScreenSputumExaminationDO::getScreenTime, reqVO.getScreenTime())
                .eqIfPresent(ScreenSputumExaminationDO::getScreenOrder, reqVO.getScreenOrder())
                .eqIfPresent(ScreenSputumExaminationDO::getPersonId, reqVO.getPersonId())
                .eqIfPresent(ScreenSputumExaminationDO::getEveningSputum, reqVO.getEveningSputum())
                .eqIfPresent(ScreenSputumExaminationDO::getEveningSputumCode, reqVO.getEveningSputumCode())
                .eqIfPresent(ScreenSputumExaminationDO::getMorningSputum, reqVO.getMorningSputum())
                .eqIfPresent(ScreenSputumExaminationDO::getMorningSputumCode, reqVO.getMorningSputumCode())
                .eqIfPresent(ScreenSputumExaminationDO::getOutcome, reqVO.getOutcome())
                .eqIfPresent(ScreenSputumExaminationDO::getYear, reqVO.getYear())
                .eqIfPresent(ScreenSputumExaminationDO::getScreenType, reqVO.getScreenType())
                .orderByDesc(ScreenSputumExaminationDO::getId));
    }

    /**
     * 根据personId获取到痰检组最近一次筛查数据
     * @param personId 摸底表id
     * @return 痰检组最近一次筛查数据
     */
    Integer selectByPersonIdLastTime(Long personId);
     /**
     * 根据id获取到痰检组最近一次筛查数据
     * @param id 痰检id
     * @return 痰检记录的次序
     */
    Integer selectOrderById(Long id);

    /**
     * 同步 痰检-分页
     */
    List<ScreenSputumExaminationDO> getSputumExaminationData(ScreenSputumExaminationPageReqVO reqVO);

    /**
     * 同步 痰检-分页 条数
     */
    Long getSputumExaminationCount(ScreenSputumExaminationPageReqVO reqVO);
}