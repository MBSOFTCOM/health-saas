package cn.iocoder.yudao.module.ppd.service.screenrepeatperson;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo.ScreenRepeatPersonPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenrepeatperson.vo.ScreenRepeatPersonSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenrepeatperson.ScreenRepeatPersonDO;
import jakarta.validation.Valid;

/**
 * 重复筛查人员管理 Service 接口
 *
 * @author 侯卿
 */
public interface ScreenRepeatPersonService {

    /**
     * 创建重复筛查人员管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenRepeatPerson(@Valid ScreenRepeatPersonSaveReqVO createReqVO);

    /**
     * 更新重复筛查人员管理
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenRepeatPerson(@Valid ScreenRepeatPersonSaveReqVO updateReqVO);

    /**
     * 导出是对多人群分类的处理
     */
    String resolveMoreTypeToString(Integer moreType);

    /**
     * 删除重复筛查人员管理
     *
     * @param id 编号
     */
    void deleteScreenRepeatPerson(Long id);

    /**
     * 获得重复筛查人员管理
     *
     * @param id 编号
     * @return 重复筛查人员管理
     */
    ScreenRepeatPersonDO getScreenRepeatPerson(Long id);

    /**
     * 获得重复筛查人员管理分页
     *
     * @param pageReqVO 分页查询
     * @return 重复筛查人员管理分页
     */
    PageResult<ScreenRepeatPersonDO> getScreenRepeatPersonPage(ScreenRepeatPersonPageReqVO pageReqVO);

    /**
     * 查询是否有重复人员名单未处理
     * @return 返回 true 或 false
     */
    Boolean getIsRemainRepeatPerson();

    /**
     * 重复人员恢复至摸底库，查询摸底库中是否存在 与恢复人员 的身份证号、工作年度、筛查类型一样的记录
     * @param id 重复人员id
     * @return 返回 true 或 false
     */
    Boolean isExist(Long id);

    /**
     * 重复人员恢复至摸底库
     * @param id 重复人员id
     * @return 返回 true 或 false
     */
    Boolean recoverData(Long id);
}