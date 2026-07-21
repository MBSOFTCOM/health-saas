package cn.iocoder.yudao.module.childhealth.service.screeningconfig;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig.ScreeningDepartmentDO;

import java.util.List;

/**
 * 筛查科室 Service 接口
 *
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface ScreeningDepartmentService {

    /**
     * 创建筛查科室
     *
     * @param saveReqVO 创建信息（后续替换为 ScreeningDepartmentSaveReqVO）
     * @return 编号
     */
    Long createScreeningDepartment(Object saveReqVO);

    /**
     * 更新筛查科室
     *
     * @param saveReqVO 更新信息（后续替换为 ScreeningDepartmentSaveReqVO）
     */
    void updateScreeningDepartment(Object saveReqVO);

    /**
     * 删除筛查科室
     *
     * @param id 编号
     */
    void deleteScreeningDepartment(Long id);

    /**
     * 获得筛查科室
     *
     * @param id 编号
     * @return 筛查科室
     */
    ScreeningDepartmentDO getScreeningDepartment(Long id);

    /**
     * 获得筛查科室分页
     *
     * @param pageParam 分页查询（后续替换为 ScreeningDepartmentPageReqVO）
     * @return 筛查科室分页
     */
    PageResult<ScreeningDepartmentDO> getScreeningDepartmentPage(PageParam pageParam);

    /**
     * 按科室编码查询
     *
     * @param deptCode 科室编码
     * @return 筛查科室
     */
    ScreeningDepartmentDO selectByCode(String deptCode);

    /**
     * 查询所有启用科室
     *
     * @return 启用科室列表
     */
    List<ScreeningDepartmentDO> selectActiveList();

}
