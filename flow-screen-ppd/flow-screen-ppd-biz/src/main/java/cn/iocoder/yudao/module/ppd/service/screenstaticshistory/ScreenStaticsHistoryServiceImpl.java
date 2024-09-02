package cn.iocoder.yudao.module.ppd.service.screenstaticshistory;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistoryPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistoryRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistorySaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenstaticshistory.ScreenStaticsHistoryDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenstaticshistory.ScreenStaticsHistoryMapper;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import jakarta.annotation.Resource;
import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_STATICS_HISTORY_NOT_EXISTS;

/**
 * 工作进展报告-统计表-导出的历史选项 Service 实现类
 *
 * @author 福乐云
 */
@Service
@Validated
public class ScreenStaticsHistoryServiceImpl implements ScreenStaticsHistoryService {

    @Resource
    private ScreenStaticsHistoryMapper screenStaticsHistoryMapper;
    @Resource
    private DeptService deptService;
    @Override
    public Long createScreenStaticsHistory(ScreenStaticsHistorySaveReqVO createReqVO) {
        // 插入
        ScreenStaticsHistoryDO screenStaticsHistory = BeanUtils.toBean(createReqVO, ScreenStaticsHistoryDO.class);
        screenStaticsHistoryMapper.insert(screenStaticsHistory);
        // 返回
        return screenStaticsHistory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long recoverScreenStaticsHistory(ScreenStaticsHistorySaveReqVO createReqVO) {
        ScreenStaticsHistoryDO historyDO = BeanUtils.toBean(createReqVO, ScreenStaticsHistoryDO.class);
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        Long deptId=deptService.getMyDept(loginUserId);
        screenStaticsHistoryMapper.delete(ScreenStaticsHistoryDO::getDeptId,deptId);
        ScreenStaticsHistoryRespVO historyRespVO = getHistory(deptId);
        if (BeanUtil.isEmpty(historyRespVO)){
            historyDO.setDeptId(deptId);
            screenStaticsHistoryMapper.insert(historyDO);
        }else {
            historyDO.setId(historyRespVO.getId());
            screenStaticsHistoryMapper.updateById(historyDO);
        }
        return null;
    }

    @Override
    public void updateScreenStaticsHistory(ScreenStaticsHistorySaveReqVO updateReqVO) {
        // 校验存在
        validateScreenStaticsHistoryExists(updateReqVO.getId());
        // 更新
        ScreenStaticsHistoryDO updateObj = BeanUtils.toBean(updateReqVO, ScreenStaticsHistoryDO.class);
        screenStaticsHistoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenStaticsHistory(Long id) {
        // 校验存在
        validateScreenStaticsHistoryExists(id);
        // 删除
        screenStaticsHistoryMapper.deleteById(id);
    }

    private void validateScreenStaticsHistoryExists(Long id) {
        if (screenStaticsHistoryMapper.selectById(id) == null) {
            throw exception(SCREEN_STATICS_HISTORY_NOT_EXISTS);
        }
    }

    @Override
    public ScreenStaticsHistoryDO getScreenStaticsHistory(Long id) {
        return screenStaticsHistoryMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenStaticsHistoryDO> getScreenStaticsHistoryPage(ScreenStaticsHistoryPageReqVO pageReqVO) {
        return screenStaticsHistoryMapper.selectPage(pageReqVO);
    }

    @Override
    public ScreenStaticsHistoryRespVO getHistory(Long deptId) {
        if (deptId==null){
            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
            deptId=deptService.getMyDept(loginUserId);
        }
        ScreenStaticsHistoryDO historyDO = screenStaticsHistoryMapper.selectOneByDept(deptId);
        ScreenStaticsHistoryRespVO respVO = BeanUtils.toBean(historyDO, ScreenStaticsHistoryRespVO.class);
        return respVO;
    }
}