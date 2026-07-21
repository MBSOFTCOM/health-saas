package cn.iocoder.yudao.module.childhealth.dal.mysql.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningDepartmentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 筛查科室 Mapper
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@Mapper
public interface ScreeningDepartmentMapper extends BaseMapperX<ScreeningDepartmentDO> {

    /**
     * 按科室编码查询
     */
    default ScreeningDepartmentDO selectByDeptCode(String deptCode) {
        return selectOne(ScreeningDepartmentDO::getDeptCode, deptCode);
    }

    /**
     * 按科室类型查询启用科室
     */
    default List<ScreeningDepartmentDO> selectListByDeptType(String deptType) {
        return selectList(new LambdaQueryWrapperX<ScreeningDepartmentDO>()
                .eqIfPresent(ScreeningDepartmentDO::getDeptType, deptType)
                .eqIfPresent(ScreeningDepartmentDO::getStatus, 1)
                .orderByAsc(ScreeningDepartmentDO::getSort));
    }

    /**
     * 按负责人ID查询负责的科室
     */
    default List<ScreeningDepartmentDO> selectListByPrincipalId(Long principalId) {
        return selectList(new LambdaQueryWrapperX<ScreeningDepartmentDO>()
                .eqIfPresent(ScreeningDepartmentDO::getPrincipalId, principalId)
                .eqIfPresent(ScreeningDepartmentDO::getStatus, 1));
    }

    /**
     * 按科室名称模糊查询
     */
    default List<ScreeningDepartmentDO> selectListByDeptNameLike(String deptName) {
        return selectList(new LambdaQueryWrapperX<ScreeningDepartmentDO>()
                .likeIfPresent(ScreeningDepartmentDO::getDeptName, deptName)
                .eqIfPresent(ScreeningDepartmentDO::getStatus, 1)
                .orderByAsc(ScreeningDepartmentDO::getSort));
    }

    /**
     * 查询所有启用的科室（按排序正序）
     */
    default List<ScreeningDepartmentDO> selectActiveList() {
        return selectList(new LambdaQueryWrapperX<ScreeningDepartmentDO>()
                .eqIfPresent(ScreeningDepartmentDO::getStatus, 1)
                .orderByAsc(ScreeningDepartmentDO::getSort));
    }

}
