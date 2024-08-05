package cn.iocoder.yudao.module.ppd.dal.mysql.screenrepeatperson;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo.ScreenRepeatPersonPageReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenrepeatperson.ScreenRepeatPersonDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 重复筛查人员管理 Mapper
 *
 * @author 侯卿
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenRepeatPersonMapper extends BaseMapperX<ScreenRepeatPersonDO> {

    default PageResult<ScreenRepeatPersonDO> selectPage(ScreenRepeatPersonPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenRepeatPersonDO>()
                .likeIfPresent(ScreenRepeatPersonDO::getPermanentAddress, reqVO.getPermanentAddress())
                .likeIfPresent(ScreenRepeatPersonDO::getAddress, reqVO.getAddress())
                .eqIfPresent(ScreenRepeatPersonDO::getNation, reqVO.getNation())
                .eqIfPresent(ScreenRepeatPersonDO::getFirstType, reqVO.getFirstType())
                .eqIfPresent(ScreenRepeatPersonDO::getMoreType, reqVO.getMoreType())
                .likeIfPresent(ScreenRepeatPersonDO::getSchoolOrTemple, reqVO.getSchoolOrTemple())
                .likeIfPresent(ScreenRepeatPersonDO::getClassroom, reqVO.getClassroom())
                .eqIfPresent(ScreenRepeatPersonDO::getIsNew, reqVO.getIsNew())
                .eqIfPresent(ScreenRepeatPersonDO::getIsScreened, reqVO.getIsScreened())
                .likeIfPresent(ScreenRepeatPersonDO::getScreenPoint, reqVO.getScreenPoint())
                .betweenIfPresent(ScreenRepeatPersonDO::getScreenTime, reqVO.getScreenTime())
                .eqIfPresent(ScreenRepeatPersonDO::getYear, reqVO.getYear())
                .eqIfPresent(ScreenRepeatPersonDO::getScreenType, reqVO.getScreenType())
                .likeIfPresent(ScreenRepeatPersonDO::getIdNum, reqVO.getIdNum())
                .likeIfPresent(ScreenRepeatPersonDO::getName, reqVO.getName())
                .likeIfPresent(ScreenRepeatPersonDO::getTel, reqVO.getTel())
                .eqIfPresent(ScreenRepeatPersonDO::getSex, reqVO.getSex())
                .orderByDesc(ScreenRepeatPersonDO::getIdNum));
    }

    /**
     * 查询 有没有身份证号、工作年度、筛查类型都满足的记录
     * @param idNum 身份证号
     * @param year 工作年度
     * @param screenType 筛查类型
     * @return 返回满足的id
     */
    Long isNullByIdNumYear(@Param("idNum") String idNum,
                           @Param("year") Integer year,
                           @Param("screenType") Integer screenType);


    /**
     * 查询是否有剩余未处理的重复人员
     * @return 返回剩余未处理的记录条数
     */
    Integer getIsRemainRepeatPerson();


    /**
     * 重复人员恢复至摸底库，查询摸底库中是否存在 与恢复人员 的身份证号、工作年度、筛查类型一样的记录
     * @param idNum 身份证号
     * @param year 工作年度
     * @param screenType 筛查类型
     * @return 返回满足条件的记录条数
     */
    Integer isExist(@Param("idNum") String idNum,
                    @Param("year") Integer year,
                    @Param("screenType") Integer screenType);}