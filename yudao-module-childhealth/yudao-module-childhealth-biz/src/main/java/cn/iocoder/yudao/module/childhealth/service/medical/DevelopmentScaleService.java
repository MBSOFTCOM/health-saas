package cn.iocoder.yudao.module.childhealth.service.medical;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentScaleDO;

import java.util.List;

/**
 * 发育评估量表定义表 Service 接口
 *
 * 模块: A. 儿童基础健康检查（A6-发育评估量表定义表，18+套）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface DevelopmentScaleService {

    /**
     * 创建发育评估量表
     *
     * @param saveReqVO 创建信息（后续替换为 DevelopmentScaleSaveReqVO）
     * @return 编号
     */
    Long createDevelopmentScale(Object saveReqVO);

    /**
     * 更新发育评估量表
     *
     * @param saveReqVO 更新信息（后续替换为 DevelopmentScaleSaveReqVO）
     */
    void updateDevelopmentScale(Object saveReqVO);

    /**
     * 删除发育评估量表
     *
     * @param id 编号
     */
    void deleteDevelopmentScale(Long id);

    /**
     * 获得发育评估量表
     *
     * @param id 编号
     * @return 发育评估量表
     */
    DevelopmentScaleDO getDevelopmentScale(Long id);

    /**
     * 获得发育评估量表分页
     *
     * @param pageParam 分页查询（后续替换为 DevelopmentScalePageReqVO）
     * @return 发育评估量表分页
     */
    PageResult<DevelopmentScaleDO> getDevelopmentScalePage(PageParam pageParam);

    /**
     * 按量表编码查询
     *
     * @param scaleCode 量表编码
     * @return 发育评估量表
     */
    DevelopmentScaleDO selectByCode(String scaleCode);

    /**
     * 查询所有启用量表
     *
     * @return 启用量表列表
     */
    List<DevelopmentScaleDO> selectActiveList();

}
