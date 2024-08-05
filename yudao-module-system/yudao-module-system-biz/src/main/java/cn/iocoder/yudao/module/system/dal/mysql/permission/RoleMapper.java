package cn.iocoder.yudao.module.system.dal.mysql.permission;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.permission.vo.role.RolePageReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.permission.RoleDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.List;

@Mapper
public interface RoleMapper extends BaseMapperX<RoleDO> {

    default PageResult<RoleDO> selectPage(RolePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RoleDO>()
                .likeIfPresent(RoleDO::getName, reqVO.getName())
                .likeIfPresent(RoleDO::getCode, reqVO.getCode())
                .eqIfPresent(RoleDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BaseDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(RoleDO::getSort));
    }

    default RoleDO selectByName(String name) {
        return selectOne(RoleDO::getName, name);
    }

    default RoleDO selectByCode(String code) {
        return selectOne(RoleDO::getCode, code);
    }

    default List<RoleDO> selectListByStatus(@Nullable Collection<Integer> statuses) {
        return selectList(RoleDO::getStatus, statuses);
    }

    /**
     * 获取登录账号的角色编码
     * @param loginUserId
     * @return
     */
    @Select("SELECT DISTINCT sr.code FROM system_role as sr " +
            "INNER JOIN system_user_role as sur on sr.id = sur.role_id " +
            "INNER JOIN system_users as su on su.id = sur.user_id " +
            "WHERE sur.user_id = #{loginUserId} AND sur.deleted = 0 AND su.deleted = 0 AND sr.deleted = 0")
    @InterceptorIgnore(tenantLine = "true")
    List<String> getRoleCode(Long loginUserId);

    /**
     * 非管理员获取角色列表
     * @return
     */
    @Select("SELECT DISTINCT sr.id, sr.name, sr.code FROM system_role as sr " +
            "WHERE sr.id > #{roleId} AND sr.deleted = 0 AND sr.status = 0 ")
    List<RoleDO> getRoleListByOtherRole(Long roleId);

    @Select("SELECT DISTINCT sr.id FROM system_role as sr " +
            "INNER JOIN system_user_role as sur on sr.id = sur.role_id " +
            "INNER JOIN system_users as su on su.id = sur.user_id " +
            "WHERE sur.user_id = #{loginUserId} AND sur.deleted = 0 AND su.deleted = 0 AND sr.deleted = 0")
    @InterceptorIgnore(tenantLine = "true")
    List<Long> getLoginRoleId(Long loginUserId);

    @Select("SELECT id FROM system_role WHERE code = #{code} AND deleted = 0 AND status = 0")
    @InterceptorIgnore(tenantLine = "true")
    Long getIdByCode(String code);

}
