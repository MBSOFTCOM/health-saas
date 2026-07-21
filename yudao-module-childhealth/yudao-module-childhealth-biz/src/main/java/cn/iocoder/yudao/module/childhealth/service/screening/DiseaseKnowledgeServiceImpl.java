package cn.iocoder.yudao.module.childhealth.service.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.knowledge.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.DiseaseKnowledgeDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.screening.DiseaseKnowledgeMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 疾病知识库 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DiseaseKnowledgeServiceImpl implements DiseaseKnowledgeService {

    @Resource
    private DiseaseKnowledgeMapper diseaseKnowledgeMapper;

    @Override
    public Long createDiseaseKnowledge(DiseaseKnowledgeSaveReqVO createReqVO) {
        // 校验疾病编码唯一
        validateDiseaseCodeUnique(null, createReqVO.getDiseaseCode());
        
        DiseaseKnowledgeDO diseaseKnowledge = BeanUtils.toBean(createReqVO, DiseaseKnowledgeDO.class);
        diseaseKnowledgeMapper.insert(diseaseKnowledge);
        return diseaseKnowledge.getId();
    }

    @Override
    public void updateDiseaseKnowledge(DiseaseKnowledgeSaveReqVO updateReqVO) {
        validateDiseaseKnowledgeExists(updateReqVO.getId());
        validateDiseaseCodeUnique(updateReqVO.getId(), updateReqVO.getDiseaseCode());
        
        DiseaseKnowledgeDO updateObj = BeanUtils.toBean(updateReqVO, DiseaseKnowledgeDO.class);
        diseaseKnowledgeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDiseaseKnowledge(Long id) {
        validateDiseaseKnowledgeExists(id);
        diseaseKnowledgeMapper.deleteById(id);
    }

    private void validateDiseaseKnowledgeExists(Long id) {
        if (diseaseKnowledgeMapper.selectById(id) == null) {
            throw exception(DISEASE_KNOWLEDGE_NOT_EXISTS);
        }
    }

    private void validateDiseaseCodeUnique(Long id, String diseaseCode) {
        DiseaseKnowledgeDO knowledge = diseaseKnowledgeMapper.selectByDiseaseCode(diseaseCode);
        if (knowledge == null) {
            return;
        }
        if (id == null) {
            throw exception(DISEASE_CODE_DUPLICATE);
        }
        if (!knowledge.getId().equals(id)) {
            throw exception(DISEASE_CODE_DUPLICATE);
        }
    }

    @Override
    public DiseaseKnowledgeDO getDiseaseKnowledge(Long id) {
        return diseaseKnowledgeMapper.selectById(id);
    }

    @Override
    public PageResult<DiseaseKnowledgeDO> getDiseaseKnowledgePage(DiseaseKnowledgePageReqVO pageReqVO) {
        return diseaseKnowledgeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DiseaseKnowledgeDO> getDiseaseKnowledgeList(DiseaseKnowledgeListReqVO listReqVO) {
        return diseaseKnowledgeMapper.selectList(listReqVO);
    }

    @Override
    public DiseaseKnowledgeDO getByDiseaseCode(String diseaseCode) {
        return diseaseKnowledgeMapper.selectByDiseaseCode(diseaseCode);
    }

    @Override
    public List<DiseaseKnowledgeDO> getByCategory(String category) {
        return diseaseKnowledgeMapper.selectByCategory(category);
    }

}