package cn.iocoder.yudao.module.ppd.service.screenconsumerecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenconsumerecord.vo.ScreenConsumeRecordSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenconsumerecord.ScreenConsumeRecordDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenconsumerecord.ScreenConsumeRecordMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.*;

/**
 * 消耗管理记录 Service 实现类
 *
 * @author 侯卿
 */
@Service
@Validated
public class ScreenConsumeRecordServiceImpl implements ScreenConsumeRecordService {

    @Resource
    private ScreenConsumeRecordMapper screenConsumeRecordMapper;

    @Override
    public Long createScreenConsumeRecord(ScreenConsumeRecordSaveReqVO createReqVO) {
        // 插入
        ScreenConsumeRecordDO screenConsumeRecord = BeanUtils.toBean(createReqVO, ScreenConsumeRecordDO.class);
        screenConsumeRecordMapper.insert(screenConsumeRecord);
        // 返回
        return screenConsumeRecord.getId();
    }

    @Override
    public void updateScreenConsumeRecord(ScreenConsumeRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenConsumeRecordExists(updateReqVO.getId());
        // 更新
        ScreenConsumeRecordDO updateObj = BeanUtils.toBean(updateReqVO, ScreenConsumeRecordDO.class);
        screenConsumeRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenConsumeRecord(Long id) {
        // 校验存在
        validateScreenConsumeRecordExists(id);
        // 删除
        screenConsumeRecordMapper.deleteById(id);
    }

    private void validateScreenConsumeRecordExists(Long id) {
        if (screenConsumeRecordMapper.selectById(id) == null) {
            throw exception(SCREEN_CONSUME_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public ScreenConsumeRecordDO getScreenConsumeRecord(Long id) {
        return screenConsumeRecordMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenConsumeRecordDO> getScreenConsumeRecordPage(ScreenConsumeRecordPageReqVO pageReqVO) {
        return screenConsumeRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ScreenConsumeRecordRespVO> getScreenConsumeRecordList(Long id) {
        return screenConsumeRecordMapper.getScreenConsumeRecordList(id,null,null);
    }

    @Override
    public PageResult<ScreenConsumeRecordRespVO> getScreenConsumeRecordList(Long id, Integer pageSize, Integer pageNo) {
        if (pageSize<=0){
            throw exception(REQUEST_GET_SIZE_ERROR);
        }
        if (pageNo<=0){
            throw exception(REQUEST_GET_NO_ERROR);
        }
        pageNo=(pageNo-1)*pageSize;
        PageResult<ScreenConsumeRecordRespVO> pageResult = new PageResult<>(screenConsumeRecordMapper.getScreenConsumeRecordList(id, pageSize, pageNo), screenConsumeRecordMapper.countScreenConsumeRecordList(id));
        return pageResult;
    }
}