package cn.iocoder.yudao.module.ppd.service.screenreagent;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentImportRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentImportVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenreagent.vo.ScreenReagentSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenreagent.ScreenReagentDO;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

/**
 * 试剂 Service 接口
 *
 * @author 侯卿
 */
public interface ScreenReagentService {

    /**
     * 创建试剂
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScreenReagent(@Valid ScreenReagentSaveReqVO createReqVO);

    /**
     * 更新试剂
     *
     * @param updateReqVO 更新信息
     */
    void updateScreenReagent(@Valid ScreenReagentSaveReqVO updateReqVO);

    /**
     * 删除试剂
     *
     * @param id 编号
     */
    void deleteScreenReagent(Long id);

    /**
     * 获得试剂
     *
     * @param id 编号
     * @return 试剂
     */
    ScreenReagentDO getScreenReagent(Long id);

    /**
     * 获得试剂分页
     *
     * @param pageReqVO 分页查询
     * @return 试剂分页
     */
    PageResult<ScreenReagentDO> getScreenReagentPage(ScreenReagentPageReqVO pageReqVO);
    /**
     * 获得可用试剂数据
     *
     * @param pageReqVO ScreenReagentPageReqVO
     * @return 可用试剂的列表
     */
     List<ScreenReagentDO> getUsableReagent(ScreenReagentPageReqVO pageReqVO);

    /**
     *禁用试剂
     */
    Boolean forbidScreenReagent(Long id);

    /**
     *启用试剂
     */
    Boolean recoverScreenReagent(Long id);

    /**
     * 试剂导入模板
     */
    List<ScreenReagentImportVO> createSampleData();

    /**
     * Excel下拉框数据
     */
    void addSelectedData(String dictType, int index, Map<Integer, List<String>> selectedData);

    /**
     * 导入试剂
     */
    ScreenReagentImportRespVO importReagent(List<ScreenReagentImportVO> list);

    /**
     * 获取试剂列表
     */
    List<ScreenReagentDO> getReagentList();
    /**
     * 获取试剂列表
     */
    List<ScreenReagentDO> getReagentInfoList();

    /**
     * 获取试剂列表--名称
     */
    List<String> getReagentList2();


}