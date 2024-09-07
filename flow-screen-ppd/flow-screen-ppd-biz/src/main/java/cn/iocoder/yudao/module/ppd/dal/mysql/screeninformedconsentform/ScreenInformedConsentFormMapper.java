package cn.iocoder.yudao.module.ppd.dal.mysql.screeninformedconsentform;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screeninformedconsentform.ScreenInformedConsentFormDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

/**
 * 知情同意书 Mapper
 *
 * @author 福乐云
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenInformedConsentFormMapper extends BaseMapperX<ScreenInformedConsentFormDO> {

    default PageResult<ScreenInformedConsentFormDO> selectPage(ScreenInformedConsentFormPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenInformedConsentFormDO>()
                .eqIfPresent(ScreenInformedConsentFormDO::getStudentId, reqVO.getStudentId())
                .likeIfPresent(ScreenInformedConsentFormDO::getSchoolName, reqVO.getSchoolName())
                .eqIfPresent(ScreenInformedConsentFormDO::getClassroom, reqVO.getClassroom())
                .betweenIfPresent(ScreenInformedConsentFormDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ScreenInformedConsentFormDO::getId));
    }
    ScreenInformedConsentFormDO selectLastOne(Long personId);
}