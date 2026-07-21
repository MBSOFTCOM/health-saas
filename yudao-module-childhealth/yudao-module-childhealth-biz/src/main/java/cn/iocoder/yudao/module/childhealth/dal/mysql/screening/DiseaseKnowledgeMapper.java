package cn.iocoder.yudao.module.childhealth.dal.mysql.screening;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.DiseaseKnowledgeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 疾病知识库 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DiseaseKnowledgeMapper extends BaseMapperX<DiseaseKnowledgeDO> {

    default PageResult<DiseaseKnowledgeDO> selectPage(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.knowledge.DiseaseKnowledgePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DiseaseKnowledgeDO>()
                .likeIfPresent(DiseaseKnowledgeDO::getDiseaseCode, reqVO.getDiseaseCode())
                .likeIfPresent(DiseaseKnowledgeDO::getDiseaseName, reqVO.getDiseaseName())
                .eqIfPresent(DiseaseKnowledgeDO::getCategory, reqVO.getCategory())
                .eqIfPresent(DiseaseKnowledgeDO::getStatus, reqVO.getStatus())
                .orderByDesc(DiseaseKnowledgeDO::getId));
    }

    default List<DiseaseKnowledgeDO> selectList(cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.knowledge.DiseaseKnowledgeListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DiseaseKnowledgeDO>()
                .likeIfPresent(DiseaseKnowledgeDO::getDiseaseCode, reqVO.getDiseaseCode())
                .likeIfPresent(DiseaseKnowledgeDO::getDiseaseName, reqVO.getDiseaseName())
                .eqIfPresent(DiseaseKnowledgeDO::getCategory, reqVO.getCategory())
                .eqIfPresent(DiseaseKnowledgeDO::getStatus, reqVO.getStatus())
                .orderByDesc(DiseaseKnowledgeDO::getId));
    }

    default DiseaseKnowledgeDO selectByDiseaseCode(String diseaseCode) {
        return selectOne(DiseaseKnowledgeDO::getDiseaseCode, diseaseCode);
    }

    default List<DiseaseKnowledgeDO> selectByCategory(String category) {
        return selectList(new LambdaQueryWrapperX<DiseaseKnowledgeDO>()
                .eq(DiseaseKnowledgeDO::getCategory, category)
                .eq(DiseaseKnowledgeDO::getStatus, 1)
                .orderByAsc(DiseaseKnowledgeDO::getId));
    }

}