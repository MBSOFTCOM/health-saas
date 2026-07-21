package cn.iocoder.yudao.module.childhealth.service.medical.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.DevelopmentScaleQuestionDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.DevelopmentScaleQuestionMapper;
import cn.iocoder.yudao.module.childhealth.service.medical.DevelopmentScaleQuestionService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.DEVELOPMENT_SCALE_QUESTION_NOT_EXISTS;

/**
 * 发育评估量表题目表 Service 实现类
 *
 * 模块: A. 儿童基础健康检查（A7-发育评估量表题目表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class DevelopmentScaleQuestionServiceImpl implements DevelopmentScaleQuestionService {

    @Resource
    private DevelopmentScaleQuestionMapper developmentScaleQuestionMapper;

    @Override
    public Long createDevelopmentScaleQuestion(Object saveReqVO) {
        // TODO 后续替换为 DevelopmentScaleQuestionSaveReqVO
        DevelopmentScaleQuestionDO question = BeanUtils.toBean(saveReqVO, DevelopmentScaleQuestionDO.class);
        developmentScaleQuestionMapper.insert(question);
        return question.getId();
    }

    @Override
    public void updateDevelopmentScaleQuestion(Object saveReqVO) {
        // TODO 后续替换为 DevelopmentScaleQuestionSaveReqVO
        DevelopmentScaleQuestionDO updateObj = BeanUtils.toBean(saveReqVO, DevelopmentScaleQuestionDO.class);
        validateDevelopmentScaleQuestionExists(updateObj.getId());
        developmentScaleQuestionMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevelopmentScaleQuestion(Long id) {
        validateDevelopmentScaleQuestionExists(id);
        developmentScaleQuestionMapper.deleteById(id);
    }

    @Override
    public DevelopmentScaleQuestionDO getDevelopmentScaleQuestion(Long id) {
        return developmentScaleQuestionMapper.selectById(id);
    }

    @Override
    public PageResult<DevelopmentScaleQuestionDO> getDevelopmentScaleQuestionPage(PageParam pageParam) {
        // TODO 后续替换为 DevelopmentScaleQuestionPageReqVO，并增加查询条件
        return developmentScaleQuestionMapper.selectPage(pageParam, null);
    }

    @Override
    public List<DevelopmentScaleQuestionDO> selectListByScaleId(Long scaleId) {
        return developmentScaleQuestionMapper.selectListByScaleId(scaleId);
    }

    @Override
    public void batchInsert(List<DevelopmentScaleQuestionDO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        developmentScaleQuestionMapper.insertBatch(list);
    }

    private void validateDevelopmentScaleQuestionExists(Long id) {
        if (id == null || developmentScaleQuestionMapper.selectById(id) == null) {
            throw exception(DEVELOPMENT_SCALE_QUESTION_NOT_EXISTS);
        }
    }

}
