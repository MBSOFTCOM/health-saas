package cn.iocoder.yudao.module.childhealth.service.medical.impl;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.medical.MedicalRecordItemDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.medical.MedicalRecordItemMapper;
import cn.iocoder.yudao.module.childhealth.service.medical.MedicalRecordItemService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.MEDICAL_RECORD_ITEM_NOT_EXISTS;

/**
 * 病历结构化字段值表 Service 实现类
 *
 * 模块: A. 儿童基础健康检查（A3-病历结构化字段值表）
 * 创建日期: 2026-07-20
 *
 * @author 芋道源码
 */
@Service
@Validated
@Slf4j
public class MedicalRecordItemServiceImpl implements MedicalRecordItemService {

    @Resource
    private MedicalRecordItemMapper medicalRecordItemMapper;

    @Override
    public Long createMedicalRecordItem(Object saveReqVO) {
        // TODO 后续替换为 MedicalRecordItemSaveReqVO
        MedicalRecordItemDO item = BeanUtils.toBean(saveReqVO, MedicalRecordItemDO.class);
        medicalRecordItemMapper.insert(item);
        return item.getId();
    }

    @Override
    public void updateMedicalRecordItem(Object saveReqVO) {
        // TODO 后续替换为 MedicalRecordItemSaveReqVO
        MedicalRecordItemDO updateObj = BeanUtils.toBean(saveReqVO, MedicalRecordItemDO.class);
        validateMedicalRecordItemExists(updateObj.getId());
        medicalRecordItemMapper.updateById(updateObj);
    }

    @Override
    public void deleteMedicalRecordItem(Long id) {
        validateMedicalRecordItemExists(id);
        medicalRecordItemMapper.deleteById(id);
    }

    @Override
    public MedicalRecordItemDO getMedicalRecordItem(Long id) {
        return medicalRecordItemMapper.selectById(id);
    }

    @Override
    public PageResult<MedicalRecordItemDO> getMedicalRecordItemPage(PageParam pageParam) {
        // TODO 后续替换为 MedicalRecordItemPageReqVO，并增加查询条件
        return medicalRecordItemMapper.selectPage(pageParam, null);
    }

    @Override
    public List<MedicalRecordItemDO> selectListByRecordId(Long recordId) {
        return medicalRecordItemMapper.selectListByRecordId(recordId);
    }

    @Override
    public void batchInsert(List<MedicalRecordItemDO> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        medicalRecordItemMapper.insertBatch(list);
    }

    private void validateMedicalRecordItemExists(Long id) {
        if (id == null || medicalRecordItemMapper.selectById(id) == null) {
            throw exception(MEDICAL_RECORD_ITEM_NOT_EXISTS);
        }
    }

}
