package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.knowledge.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.DiseaseKnowledgeDO;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 疾病知识库 Service 接口
 *
 * @author 芋道源码
 */
public interface DiseaseKnowledgeService {

    /**
     * 创建疾病知识库
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDiseaseKnowledge(@Valid DiseaseKnowledgeSaveReqVO createReqVO);

    /**
     * 更新疾病知识库
     *
     * @param updateReqVO 更新信息
     */
    void updateDiseaseKnowledge(@Valid DiseaseKnowledgeSaveReqVO updateReqVO);

    /**
     * 删除疾病知识库
     *
     * @param id 编号
     */
    void deleteDiseaseKnowledge(Long id);

    /**
     * 获得疾病知识库
     *
     * @param id 编号
     * @return 疾病知识库
     */
    DiseaseKnowledgeDO getDiseaseKnowledge(Long id);

    /**
     * 获得疾病知识库分页
     *
     * @param pageReqVO 分页查询
     * @return 疾病知识库分页
     */
    PageResult<DiseaseKnowledgeDO> getDiseaseKnowledgePage(DiseaseKnowledgePageReqVO pageReqVO);

    /**
     * 获得疾病知识库列表
     *
     * @param listReqVO 列表查询
     * @return 疾病知识库列表
     */
    List<DiseaseKnowledgeDO> getDiseaseKnowledgeList(DiseaseKnowledgeListReqVO listReqVO);

    /**
     * 根据疾病编码获取疾病知识
     *
     * @param diseaseCode 疾病编码
     * @return 疾病知识库
     */
    DiseaseKnowledgeDO getByDiseaseCode(String diseaseCode);

    /**
     * 根据分类获取疾病知识列表
     *
     * @param category 分类
     * @return 疾病知识库列表
     */
    List<DiseaseKnowledgeDO> getByCategory(String category);

}