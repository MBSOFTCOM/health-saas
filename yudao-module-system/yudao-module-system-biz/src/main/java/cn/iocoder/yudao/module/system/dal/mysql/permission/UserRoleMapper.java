package cn.iocoder.yudao.module.system.dal.mysql.permission;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.UserRoleDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Collection;
import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapperX<UserRoleDO> {

    default List<UserRoleDO> selectListByUserId(Long userId) {
        return selectList(UserRoleDO::getUserId, userId);
    }

    default void deleteListByUserIdAndRoleIdIds(Long userId, Collection<Long> roleIds) {
        delete(new LambdaQueryWrapper<UserRoleDO>()
                .eq(UserRoleDO::getUserId, userId)
                .in(UserRoleDO::getRoleId, roleIds));
    }

    default void deleteListByUserId(Long userId) {
        delete(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getUserId, userId));
    }

    default void deleteListByRoleId(Long roleId) {
        delete(new LambdaQueryWrapper<UserRoleDO>().eq(UserRoleDO::getRoleId, roleId));
    }

    default List<UserRoleDO> selectListByRoleIds(Collection<Long> roleIds) {
        return selectList(UserRoleDO::getRoleId, roleIds);
    }

    @Update("UPDATE system_user_role SET deleted = 1 WHERE user_id = #{oldCapId} AND role_id = #{roleId} AND deleted = 0")
    @InterceptorIgnore(tenantLine = "true")
    void deleteCap(@Param("oldCapId") Long oldCapId, @Param("roleId") Long roleId);

    @Select("SELECT id FROM system_user_role WHERE user_id = #{capId} AND role_id = #{roleId} AND deleted = 0")
    @InterceptorIgnore(tenantLine = "true")
    Long selcetIsDistribute(@Param("capId") Long capId, @Param("roleId") Long roleId);

    @Update("UPDATE system_user_role SET deleted = 1 WHERE user_id = #{deletedData} AND role_id = #{collectRoleId} AND deleted = 0")
    @InterceptorIgnore(tenantLine = "true")
    void setDeletedData(@Param("deletedData") Long deletedData, @Param("collectRoleId") Long collectRoleId);

    @Select("SELECT id FROM system_user_role WHERE deleted = 0 AND user_id = #{userId} AND role_id = #{roleId}")
    @InterceptorIgnore(tenantLine = "true")
    Long selectByuserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

}
