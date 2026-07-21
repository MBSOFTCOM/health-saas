package cn.iocoder.yudao.module.childhealth.service.caseType;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.caseType.vo.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.caseType.CaseTypeConfigDO;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 专案类型配置 Service 接口
 *
 * @author 芋道源码
 */
public interface CaseTypeConfigService {

    /**
     * 创建专案类型配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCaseTypeConfig(@Valid CaseTypeConfigSaveReqVO createReqVO);

    /**
     * 更新专案类型配置
     *
     * @param updateReqVO 更新信息
     */
    void updateCaseTypeConfig(@Valid CaseTypeConfigSaveReqVO updateReqVO);

    /**
     * 删除专案类型配置
     *
     * @param id 编号
     */
    void deleteCaseTypeConfig(Long id);

    /**
     * 获得专案类型配置
     *
     * @param id 编号
     * @return 专案类型配置
     */
    CaseTypeConfigDO getCaseTypeConfig(Long id);

    /**
     * 获得专案类型配置分页
     *
     * @param pageReqVO 分页查询
     * @return 专案类型配置分页
     */
    PageResult<CaseTypeConfigDO> getCaseTypeConfigPage(CaseTypeConfigPageReqVO pageReqVO);

    /**
     * 获得专案类型配置列表
     *
     * @return 列表
     */
    List<CaseTypeConfigDO> getCaseTypeConfigList();

}