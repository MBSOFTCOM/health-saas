package cn.iocoder.yudao.module.ppd.service.screenpoint;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.ScreenPersonImportRespVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpoint.ScreenPointDO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo.*;
import cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept.DeptSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.user.vo.user.UserRespVO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 筛查点 Service 接口
 *
 * @author 芋道源码
 */
public interface ScreenPointService {

    /**
     * 创建筛查点
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenPoint(@Valid ScreenPointSaveReqVO createReqVO);

    /**
     * 更新筛查点
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenPoint(@Valid ScreenPointSaveReqVO updateReqVO);

    /**
     * 删除筛查点
     *
     * @param id 编号
     */
    void deleteScreenPoint(Long id);

    /**
     * 获得筛查点
     *
     * @param id 编号
     * @return 筛查点
     */
    ScreenPointDO getScreenPoint(Long id);

    /**
     * 获得筛查点分页
     *
     * @param pageReqVO 分页查询
     * @return 筛查点分页
     */
    PageResult<ScreenPointDO> getScreenPointPage(ScreenPointPageReqVO pageReqVO);

    /**
     * 导出筛查点
     * @param pageReqVO
     * @return
     */
    PageResult<ScreenPointDO> getScreenPointPage2(ScreenPointPageReqVO pageReqVO);

    /**
     * 获取筛查点列表
     * @return
     */
    List<String> getScreenPointList();

    Boolean screenUpdatePoint(ScreenUpdateVO screenUpdateVO);

    ScreenPersonImportRespVO importScreenPoint(List<ScreenPointImportVO> list);

    List<ScreenPointImportVO> createSampleData();

    /**
     * 获取登录用户的机构id（数据库中表示dept_id）
     * @return 机构id
     */
    Long getUserLoginAgencyId();

    /**
     * 获得当前用户所属部门及以下的用户列表
     */
    List<UserVO> getUserList(Long deptId);
    /**
     * 获得指定部门的所有子部门
     *
     * @param id 部门编号
     * @return 子部门列表
     */
    List<DeptVO> getChildDeptList(Long id);

    /**
     * 获取所有用户
     * @return
     */
    List<UserVO> getAllUserList();

    /**
     *  获取部门id和名称
     * @return
     */
    List<DeptVO> getDeptList();

    void setWorker(Long userId, Long screenPointId);

        /*
     * 查看当前用户分配的筛查点
     * @param userId
     * @return
     /
         */
//    Long getIsDistribute(Long userId);

    /**
     * 在当前年度是否被分配组长
     * @return
     */
    Boolean getDistributeYear(Long userId, Integer year);

    List<UserRespVO> getCollectList(List<Long> collectList);

    /**
     * 用户-筛查点表插入数据
     */
    void updateScreenPoint(Long screenPointId, Long userId, Long roleId);

    void setCollect(String collectUserIdStr, Long screenPointId, Integer type);

    /**
     * 删除各组成员
     */
    void deleteScreenPointCollect(String collectWorker, Long screenPointId, Long deletedData, Integer type);

    /**
     * 设置新队长，删除原来队长的角色
     * @param newCapId
     * @param oldCapId
     */
    void setCap(Long newCapId, Long oldCapId, Long screenPointId);

    void updateDept(DeptSaveReqVO updateReqVO);
}