package cn.iocoder.yudao.module.ppd.dal.mysql.screenpoint;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpoint.ScreenPointDO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo.DeptVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo.ScreenPointPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo.UserVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserRespVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;

/**
 * 筛查点 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface ScreenPointMapper extends BaseMapperX<ScreenPointDO> {

    default PageResult<ScreenPointDO> selectPage(ScreenPointPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScreenPointDO>()
                .likeIfPresent(ScreenPointDO::getName, reqVO.getName())
                .eqIfPresent(ScreenPointDO::getYear, reqVO.getYear())
                .eqIfPresent(ScreenPointDO::getScreenDept, reqVO.getScreenDept())
                .orderByDesc(ScreenPointDO::getId));
    }

    ScreenPointDO selectByName(String name);
    ScreenPointDO selectByPointName(String name);

    /**
     * 筛查点列表
     */
    List<String> getScreenPointList(@Param("deptNameList") List<String> deptNameList);

    Long getIdByName(@Param("name") String name,
                     @Param("year") Integer year,
                     @Param("screenDept") String screenDept);

    /**
     * 根据登录用户id获取机构id
     * @param loginUserId 登录用户id
     * @return 登录用户机构id
     */
    Long selectByIdTaskDeptId(Long loginUserId);

    /**
     * 根据父部门id，拿到所有本部门id及以下的id
     * @param parentIds
     * @return
     */
    List<DeptVO> selectListByParentId(@Param("parentIds") Collection<Long> parentIds);


    /**
     * 拿到用户id
     * @param deptIds
     * @return
     */
    List<UserVO> getUserList(@Param("deptIds") List<Long> deptIds);

    /**
     * 获取所有用户
     * @return
     */
    List<UserVO> getAllUserList();

    @Select("SELECT id, name FROM system_dept WHERE deleted = 0 AND status = 0")
    List<DeptVO> getDeptList();

    @Update("UPDATE tb_screen_point set worker = #{userId} WHERE id = #{screenPointId} AND deleted = 0")
    void setWorker(@Param("userId") String userId, @Param("screenPointId") Long screenPointId);


//    void setPointBath(@Param("userIds") List<Long> userIds, @Param("id") Long id);

    @Select("SELECT id FROM system_role WHERE code = #{code} AND status = 0 AND deleted = 0")
    Long getRoleId(String code);

    /*@Select("SELECT screen_point_id FROM system_users WHERE id = #{userId} AND deleted = 0 ANd status = 0")
    Long getIsDistribute(Long userId);*/

    @Select("SELECT id FROM tb_screen_point WHERE worker = #{userId} AND year = #{year} AND deleted = 0")
    Long getDistributeYear(@Param("userId") String userId, @Param("year") Integer year);

    List<UserRespVO> getCollectList(@Param("collectList") List<Long> collectList);

    @Update("UPDATE tb_screen_point set collect_worker = #{collectUserIdStr} WHERE id = #{screenPointId} AND deleted = 0")
    void setWorker2(@Param("collectUserIdStr") String collectUserIdStr, @Param("screenPointId") Long screenPointId);

    @Update("UPDATE tb_screen_point set collect_worker = #{collectWorker} WHERE id = #{screenPointId} AND deleted = 0")
    void setCollectWorkder(@Param("collectWorker") String collectWorker, @Param("screenPointId") Long screenPointId);

    @Update("UPDATE tb_screen_point set ppd_worker = #{collectUserIdStr} WHERE id = #{screenPointId} AND deleted = 0")
    void setPPDWorker(@Param("collectUserIdStr") String collectUserIdStr, @Param("screenPointId") Long screenPointId);

    @Update("UPDATE tb_screen_point set drct_worker = #{collectUserIdStr} WHERE id = #{screenPointId} AND deleted = 0")
    void setDRCTWorker(@Param("collectUserIdStr") String collectUserIdStr, @Param("screenPointId") Long screenPointId);

    @Update("UPDATE tb_screen_point set sputum_worker = #{collectUserIdStr} WHERE id = #{screenPointId} AND deleted = 0")
    void setSputumWorker(@Param("collectUserIdStr") String collectUserIdStr, @Param("screenPointId") Long screenPointId);

    @Update("UPDATE tb_screen_point set experiment_worker = #{collectUserIdStr} WHERE id = #{screenPointId} AND deleted = 0")
    void setExperimentWorker(@Param("collectUserIdStr") String collectUserIdStr, @Param("screenPointId") Long screenPointId);

    @Update("UPDATE tb_screen_point set electrocardiogram_worker = #{collectUserIdStr} WHERE id = #{screenPointId} AND deleted = 0")
    void setElectrocardiogramWorker(@Param("collectUserIdStr") String collectUserIdStr, @Param("screenPointId") Long screenPointId);

    @Update("UPDATE tb_screen_point set diagnosis_worker = #{collectUserIdStr} WHERE id = #{screenPointId} AND deleted = 0")
    void setDiagnosisWorker(@Param("collectUserIdStr") String collectUserIdStr, @Param("screenPointId") Long screenPointId);

    @Update("UPDATE tb_user_screen_point set deleted = 1 WHERE deleted = 0 AND screen_point_id = #{id}")
    void deleteByScreenPoingId(Long id);

    @Select("SELECT id FROM tb_screen_point WHERE name = #{name} AND deleted = 0")
    Long getIdByName2(String name);

    @Select("SELECT id, screen_dept FROM tb_screen_point WHERE screen_dept = #{deptName} AND deleted = 0")
    List<ScreenPointDO> getByDeptName(String deptName);
}