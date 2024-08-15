package cn.iocoder.yudao.module.system.dal.mysql.dept;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

@Mapper
@InterceptorIgnore(tenantLine = "true")
public interface DeptMapper extends BaseMapperX<DeptDO> {

    default List<DeptDO> selectList(DeptListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeptDO>()
                .likeIfPresent(DeptDO::getName, reqVO.getName())
                .eqIfPresent(DeptDO::getStatus, reqVO.getStatus()));
    }

    default DeptDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(DeptDO::getParentId, parentId, DeptDO::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(DeptDO::getParentId, parentId);
    }

    default List<DeptDO> selectListByParentId(Collection<Long> parentIds) {
        return selectList(DeptDO::getParentId, parentIds);
    }

    @Select("SELECT id FROM system_dept WHERE name = #{deptName} AND deleted = 0 AND status = 0")
    Long getDeptId(String deptName);

    @Select("SELECT dept_id FROM system_users WHERE id = #{loginUserId}")
    Long getMyDept(Long loginUserId);

    @Select("SELECT name FROM system_dept WHERE deleted = 0 AND status = 0")
    List<String> getDeptList();

    @Select("SELECT id, name, district_code FROM system_dept WHERE deleted = 0 AND status = 0 AND district_code = #{districtCode}")
    List<DeptDO> getListByDistrictCode(String districtCode);
}
