package cn.iocoder.yudao.module.ppd.service.screenconsume;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumePageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsume.vo.ScreenConsumeSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsume.ScreenConsumeDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenconsume.ScreenConsumeMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenconsumerecord.ScreenConsumeRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_CONSUME_CURRENT_NUMBER_IS_NOT_ENOUGH;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_CONSUME_NOT_EXISTS;

/**
 * 消耗管理 Service 实现类
 *
 * @author 侯卿
 */
@Service
@Validated
public class ScreenConsumeServiceImpl implements ScreenConsumeService {

    @Resource
    private ScreenConsumeMapper screenConsumeMapper;

    @Resource
    private ScreenConsumeRecordMapper screenConsumeRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createScreenConsume(ScreenConsumeSaveReqVO createReqVO) {
        // 插入
        ScreenConsumeDO screenConsume = BeanUtils.toBean(createReqVO, ScreenConsumeDO.class);
        screenConsumeMapper.insert(screenConsume);
        screenConsumeRecordMapper.insert(
                new ScreenConsumeRecordDO().setConsumeId(screenConsume.getId())
                        .setType(4).setChangeNumber(screenConsume.getInboundNumber()));
        // 返回
        return screenConsume.getId();
    }

    @Override
    public void updateScreenConsume(ScreenConsumeSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenConsumeExists(updateReqVO.getId());
        // 更新
        ScreenConsumeDO updateObj = BeanUtils.toBean(updateReqVO, ScreenConsumeDO.class);
        screenConsumeMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenConsume(Long id) {
        // 校验存在
        validateScreenConsumeExists(id);
        // 删除
        screenConsumeMapper.deleteById(id);
    }

    private void validateScreenConsumeExists(Long id) {
        if (screenConsumeMapper.selectById(id) == null) {
            throw exception(SCREEN_CONSUME_NOT_EXISTS);
        }
    }

    @Override
    public ScreenConsumeDO getScreenConsume(Long id) {
        return screenConsumeMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenConsumeDO> getScreenConsumePage(ScreenConsumePageReqVO pageReqVO) {
        return screenConsumeMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean increaseScreenConsume(Long id, Integer number) {

        Integer count = screenConsumeMapper.increaseScreenConsume(id, number);

        int insertCount =
                screenConsumeRecordMapper.insert(
                        new ScreenConsumeRecordDO()
                                .setConsumeId(id)
                                .setChangeNumber(number)
                                .setType(2));

        return count > 0 && insertCount > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean decreaseScreenConsume(Long id, Integer number) {

        ScreenConsumeDO screenConsumeDO = screenConsumeMapper.selectById(id);

        if (screenConsumeDO.getCurrentNumber() - number >= 0){
            Integer count = screenConsumeMapper.decreaseScreenConsume(id, number);

            int insertCount =
                    screenConsumeRecordMapper.insert(
                            new ScreenConsumeRecordDO()
                                    .setConsumeId(id)
                                    .setChangeNumber(number)
                                    .setType(3));

            return count > 0 && insertCount > 0;
        }else {
            throw exception(SCREEN_CONSUME_CURRENT_NUMBER_IS_NOT_ENOUGH);
        }
    }

}