package cn.iocoder.yudao.module.childhealth.dal.mysql.workflow;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.ChildBaseInfoPageReqVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ChildInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 儿童基本信息 Mapper 接口
 *
 * @author 系统
 */
@Mapper
public interface ChildInfoMapper extends BaseMapperX<ChildInfoDO> {

    /**
     * 分页查询儿童基本信息列表
     *
     * @param pageReqVO 分页查询条件
     * @return 分页结果
     */
    default PageResult<ChildInfoDO> selectPage(ChildBaseInfoPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<ChildInfoDO>()
                .likeIfPresent(ChildInfoDO::getChildCode, pageReqVO.getChildCode())
                .likeIfPresent(ChildInfoDO::getChildName, pageReqVO.getName())
                .eqIfPresent(ChildInfoDO::getGender, pageReqVO.getGender())
                .eqIfPresent(ChildInfoDO::getIdCard, pageReqVO.getIdCard())
                .eqIfPresent(ChildInfoDO::getIsHighRisk, pageReqVO.getIsHighRisk())
                .eqIfPresent(ChildInfoDO::getStatus, pageReqVO.getStatus())
                .betweenIfPresent(ChildInfoDO::getBirthDate, pageReqVO.getBirthDateStart(), pageReqVO.getBirthDateEnd())
                .orderByDesc(ChildInfoDO::getId));
    }

}
