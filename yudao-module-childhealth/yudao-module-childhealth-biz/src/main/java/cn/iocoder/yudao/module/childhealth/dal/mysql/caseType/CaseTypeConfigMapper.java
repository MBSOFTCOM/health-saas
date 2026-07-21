package cn.iocoder.yudao.module.childhealth.dal.mysql.caseType;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.caseType.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.caseType.CaseTypeConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 专案类型配置 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface CaseTypeConfigMapper extends BaseMapperX<CaseTypeConfigDO> {

    default PageResult<CaseTypeConfigDO> selectPage(CaseTypeConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CaseTypeConfigDO>()
                .likeIfPresent(CaseTypeConfigDO::getTypeCode, reqVO.getTypeCode())
                .likeIfPresent(CaseTypeConfigDO::getTypeName, reqVO.getTypeName())
                .eqIfPresent(CaseTypeConfigDO::getCategory, reqVO.getCategory())
                .eqIfPresent(CaseTypeConfigDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(CaseTypeConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CaseTypeConfigDO::getId));
    }

}