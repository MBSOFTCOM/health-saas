package cn.iocoder.yudao.module.childhealth.service.medical;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordItemDO;

import java.util.List;

/**
 * 病历结构化字段值表 Service 接口
 *
 * 模块: A. 儿童基础健康检查（A3-病历结构化字段值表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
public interface MedicalRecordItemService {

    /**
     * 创建病历字段值
     *
     * @param saveReqVO 创建信息（后续替换为 MedicalRecordItemSaveReqVO）
     * @return 编号
     */
    Long createMedicalRecordItem(Object saveReqVO);

    /**
     * 更新病历字段值
     *
     * @param saveReqVO 更新信息（后续替换为 MedicalRecordItemSaveReqVO）
     */
    void updateMedicalRecordItem(Object saveReqVO);

    /**
     * 删除病历字段值
     *
     * @param id 编号
     */
    void deleteMedicalRecordItem(Long id);

    /**
     * 获得病历字段值
     *
     * @param id 编号
     * @return 病历字段值
     */
    MedicalRecordItemDO getMedicalRecordItem(Long id);

    /**
     * 获得病历字段值分页
     *
     * @param pageParam 分页查询（后续替换为 MedicalRecordItemPageReqVO）
     * @return 病历字段值分页
     */
    PageResult<MedicalRecordItemDO> getMedicalRecordItemPage(PageParam pageParam);

    /**
     * 按病历ID查询所有字段值
     *
     * @param recordId 病历ID
     * @return 字段值列表
     */
    List<MedicalRecordItemDO> selectListByRecordId(Long recordId);

    /**
     * 批量插入病历字段值
     *
     * @param list 字段值列表
     */
    void batchInsert(List<MedicalRecordItemDO> list);

}
