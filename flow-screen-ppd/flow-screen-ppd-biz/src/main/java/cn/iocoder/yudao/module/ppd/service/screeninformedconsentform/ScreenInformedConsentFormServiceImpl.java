package cn.iocoder.yudao.module.ppd.service.screeninformedconsentform;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screeninformedconsentform.vo.ScreenInformedConsentFormSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screeninformedconsentform.ScreenInformedConsentFormDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screeninformedconsentform.ScreenInformedConsentFormMapper;
import cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation.ScreenPersonService;
import jakarta.annotation.Resource;
import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.*;

/**
 * 知情同意书 Service 实现类
 *
 * @author 福乐云
 */
@Service
@Validated
public class ScreenInformedConsentFormServiceImpl implements ScreenInformedConsentFormService {

    @Resource
    private ScreenInformedConsentFormMapper screenInformedConsentFormMapper;
    @Resource
    private ScreenPersonService screenPersonService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createScreenInformedConsentForm(ScreenInformedConsentFormSaveReqVO createReqVO) {
        Long studentId = createReqVO.getStudentId();
        ScreenPersonDO screenPerson = screenPersonService.getScreenPerson(studentId);
        if (BeanUtil.isEmpty(screenPerson)){
            throw exception(SCREEN_PERSON_NOT_EXISTS);
        }
        // 复杂校验 关闭
        /*Integer isSign = createReqVO.getIsSign();
        if (isSign==null){
            createReqVO.setIsSign(2);
        }else if (isSign!=1 && isSign!=2){
            throw exception(SCREEN_INFORMED_CONSENT_FORM_IS_SIGN_VALUE_ERROR);
        }
        if (createReqVO.getIsSign()==1 && (createReqVO.getSignature()==null || createReqVO.getSignature().isEmpty() || createReqVO.getSignature().trim().isEmpty())){
            throw exception(SCREEN_INFORMED_CONSENT_FORM_PATH);
        } else if (createReqVO.getIsSign() == 2 && (createReqVO.getReason() == null || createReqVO.getReason().isEmpty() || createReqVO.getReason().trim().isEmpty())) {
            throw exception(SCREEN_INFORMED_CONSENT_FORM_REASON);
        }
        if (createReqVO.getReason()!=null && !createReqVO.getReason().isEmpty()){
            createReqVO.setReason(createReqVO.getReason().trim());
        }
        if (createReqVO.getIsSign()==1){
            createReqVO.setReason(null);
        } else if (createReqVO.getIsSign() == 2) {
            createReqVO.setSignature(null);
        }*/
        // 插入
        ScreenInformedConsentFormDO screenInformedConsentForm = BeanUtils.toBean(createReqVO, ScreenInformedConsentFormDO.class);
        screenInformedConsentForm.setClassroom(screenPerson.getClassroom());
        screenInformedConsentForm.setSchoolName(screenPerson.getSchoolOrTemple());

        screenInformedConsentFormMapper.insert(screenInformedConsentForm);
        // 返回
        return screenInformedConsentForm.getId();
    }

    @Override
    public void updateScreenInformedConsentForm(ScreenInformedConsentFormSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenInformedConsentFormExists(updateReqVO.getId());
        // 更新
        ScreenInformedConsentFormDO updateObj = BeanUtils.toBean(updateReqVO, ScreenInformedConsentFormDO.class);
        screenInformedConsentFormMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenInformedConsentForm(Long id) {
        // 校验存在
        validateScreenInformedConsentFormExists(id);
        // 删除
        screenInformedConsentFormMapper.deleteById(id);
    }

    private void validateScreenInformedConsentFormExists(Long id) {
        if (screenInformedConsentFormMapper.selectById(id) == null) {
            throw exception(SCREEN_INFORMED_CONSENT_FORM_NOT_EXISTS);
        }
    }

    @Override
    public ScreenInformedConsentFormDO getScreenInformedConsentForm(Long id) {
        return screenInformedConsentFormMapper.selectById(id);
    }

    @Override
    public ScreenInformedConsentFormRespVO getLastInformedConsentForm(Long studentId) {
        ScreenPersonDO screenPerson = screenPersonService.getScreenPerson(studentId);
        if (BeanUtil.isEmpty(screenPerson)){
            throw exception(SCREEN_PERSON_NOT_EXISTS);
        }
        ScreenInformedConsentFormDO informedConsentFormDO = screenInformedConsentFormMapper.selectLastOne(studentId);
        if (BeanUtil.isEmpty(informedConsentFormDO)){
            return new ScreenInformedConsentFormRespVO();
        }
        ScreenInformedConsentFormRespVO respVO = BeanUtils.toBean(informedConsentFormDO, ScreenInformedConsentFormRespVO.class);
        respVO.setIdNum(screenPerson.getIdNum());
        respVO.setName(screenPerson.getName());
        return respVO;
    }

    @Override
    public PageResult<ScreenInformedConsentFormDO> getScreenInformedConsentFormPage(ScreenInformedConsentFormPageReqVO pageReqVO) {
        return screenInformedConsentFormMapper.selectPage(pageReqVO);
    }

}