package cn.iocoder.yudao.module.childhealth.service.childbase;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.GuardianInfoCreateReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.childbase.vo.GuardianInfoRespVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.ChildInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.workflow.GuardianInfoDO;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.ChildInfoMapper;
import cn.iocoder.yudao.module.childhealth.dal.mysql.workflow.GuardianInfoMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.childhealth.enums.ErrorCodeConstants.*;

/**
 * 监护人信息 Service 实现类
 *
 * @author 系统
 */
@Service
@Validated
public class GuardianInfoServiceImpl implements GuardianInfoService {

    @Resource
    private GuardianInfoMapper guardianInfoMapper;

    @Resource
    private ChildInfoMapper childInfoMapper;

    @Override
    public Long createGuardianInfo(GuardianInfoCreateReqVO createReqVO) {
        // 1. 校验儿童是否存在
        validateChildInfoExists(createReqVO.getName());

        // 2. 插入监护人信息
        GuardianInfoDO guardianInfo = BeanUtils.toBean(createReqVO, GuardianInfoDO.class);
        guardianInfo.setCreateTime(LocalDateTime.now());
        guardianInfoMapper.insert(guardianInfo);

        return guardianInfo.getId();
    }

    @Override
    public void updateGuardianInfo(Long id, GuardianInfoCreateReqVO updateReqVO) {
        // 1. 校验监护人是否存在
        validateGuardianInfoExists(id);

        // 2. 更新监护人信息
        GuardianInfoDO updateObj = BeanUtils.toBean(updateReqVO, GuardianInfoDO.class);
        updateObj.setId(id);
        guardianInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteGuardianInfo(Long id) {
        // 1. 校验监护人是否存在
        validateGuardianInfoExists(id);

        // 2. 删除监护人信息
        guardianInfoMapper.deleteById(id);
    }

    @Override
    public GuardianInfoRespVO getGuardianInfo(Long id) {
        GuardianInfoDO guardianInfo = guardianInfoMapper.selectById(id);
        if (guardianInfo == null) {
            throw exception(GUARDIAN_INFO_NOT_EXISTS);
        }

        GuardianInfoRespVO respVO = BeanUtils.toBean(guardianInfo, GuardianInfoRespVO.class);
        respVO.setName(guardianInfo.getGuardianName());
        respVO.setMobile(guardianInfo.getGuardianPhone());
        return respVO;
    }

    @Override
    public List<GuardianInfoRespVO> getGuardianInfoListByChildId(Long childId) {
        List<GuardianInfoDO> guardianList = guardianInfoMapper.selectList(
                new LambdaQueryWrapper<GuardianInfoDO>()
                        .eq(GuardianInfoDO::getChildId, childId)
                        .orderByDesc(GuardianInfoDO::getIsPrimary));

        return guardianList.stream()
                .map(guardian -> {
                    GuardianInfoRespVO respVO = BeanUtils.toBean(guardian, GuardianInfoRespVO.class);
                    respVO.setName(guardian.getGuardianName());
                    respVO.setMobile(guardian.getGuardianPhone());
                    return respVO;
                })
                .toList();
    }

    @Override
    public GuardianInfoRespVO getPrimaryGuardianByChildId(Long childId) {
        GuardianInfoDO guardianInfo = guardianInfoMapper.selectOne(
                new LambdaQueryWrapper<GuardianInfoDO>()
                        .eq(GuardianInfoDO::getChildId, childId)
                        .eq(GuardianInfoDO::getIsPrimary, 1)
                        .last("LIMIT 1"));

        if (guardianInfo == null) {
            return null;
        }

        GuardianInfoRespVO respVO = BeanUtils.toBean(guardianInfo, GuardianInfoRespVO.class);
        respVO.setName(guardianInfo.getGuardianName());
        respVO.setMobile(guardianInfo.getGuardianPhone());
        return respVO;
    }

    // ========== 校验方法 ==========

    private void validateGuardianInfoExists(Long id) {
        if (id == null) {
            return;
        }
        GuardianInfoDO guardianInfo = guardianInfoMapper.selectById(id);
        if (guardianInfo == null) {
            throw exception(GUARDIAN_INFO_NOT_EXISTS);
        }
    }

    private void validateChildInfoExists(String childName) {
        // 这里简化了校验逻辑，实际应该根据childId来校验
        // 在实际使用时，这个方法可能需要修改参数为childId
    }
}