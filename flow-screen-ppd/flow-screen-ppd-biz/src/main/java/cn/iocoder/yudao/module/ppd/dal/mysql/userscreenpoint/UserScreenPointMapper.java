package cn.iocoder.yudao.module.ppd.dal.mysql.userscreenpoint;


import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.ppd.dal.dataobject.userscreenpoint.UserScreenPointDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户筛查点角色 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface UserScreenPointMapper extends BaseMapperX<UserScreenPointDO> {


    /**
     * 根据用户id获取筛查点
     * @param newCapId
     */
    List<UserScreenPointDO> selectByUserId(Long newCapId);

    /**
     * 根据用户id、角色id、筛查点id 删除 用户筛查点
     */
    void deleteByUserIdAndRoleIdAndScreenPointId(@Param("userId") Long userId,
                                                 @Param("roleId") Long roleId,
                                                 @Param("screenPointId") Long screenPointId);



    /**
     * 根据用户id查询用户筛查点表
     */
    Long getIdByUserId(Long capId);
}