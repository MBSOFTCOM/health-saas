package cn.iocoder.yudao.module.ppd.service.screensputumexamination;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenexperiment.vo.ScreenExperimentPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo.ScreenSputumExaminationPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screensputumexamination.vo.ScreenSputumExaminationSaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screensputumexamination.ScreenSputumExaminationDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screensputumexamination.ScreenSputumExaminationMapper;
import com.github.yulichang.query.MPJQueryWrapper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_SPUTUM_EXAMINATION_NOT_EXISTS;

/**
 * 痰检组 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreenSputumExaminationServiceImpl implements ScreenSputumExaminationService {

    @Resource
    private ScreenSputumExaminationMapper screenSputumExaminationMapper;

    @Override
    public Long createScreenSputumExamination(ScreenSputumExaminationSaveReqVO createReqVO) {
        // 插入
        ScreenSputumExaminationDO screenSputumExamination = BeanUtils.toBean(createReqVO, ScreenSputumExaminationDO.class);
        screenSputumExaminationMapper.insert(screenSputumExamination);
        // 返回
        return screenSputumExamination.getId();
    }

    @Override
    public void updateScreenSputumExamination(ScreenSputumExaminationSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenSputumExaminationExists(updateReqVO.getId());
        // 更新
        ScreenSputumExaminationDO updateObj = BeanUtils.toBean(updateReqVO, ScreenSputumExaminationDO.class);
        screenSputumExaminationMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenSputumExamination(Long id) {
        // 校验存在
        validateScreenSputumExaminationExists(id);
        // 删除
        screenSputumExaminationMapper.deleteById(id);
    }

    private void validateScreenSputumExaminationExists(Long id) {
        if (screenSputumExaminationMapper.selectById(id) == null) {
            throw exception(SCREEN_SPUTUM_EXAMINATION_NOT_EXISTS);
        }
    }

    @Override
    public ScreenSputumExaminationDO getScreenSputumExamination(Long id) {
        return screenSputumExaminationMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenSputumExaminationDO> getScreenSputumExaminationPage(ScreenSputumExaminationPageReqVO pageReqVO) {
        return screenSputumExaminationMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<ScreenSputumExaminationDO> getScreenSputumExaminationPageJoinPerson(ScreenExperimentPageReqVO pageReqVO) {
        // 创建一个MPJQueryWrapper实例
        MPJQueryWrapper<ScreenSputumExaminationDO> queryWrapper = new MPJQueryWrapper<>();
        // 选择所有字段
        queryWrapper.selectAll(ScreenSputumExaminationDO.class);
        // 左连接
        queryWrapper.leftJoin("tb_screen_person p ON t.person_id = p.id");
        // 条件1: 仅当deleted字段为0时
        queryWrapper.eq("t.deleted", "0");
        // 条件2: 仅当forthwith_sputum_code不为空时
        String forthwithSputumCode = pageReqVO.getForthwithSputumCode();
        if (StringUtils.isNotBlank(forthwithSputumCode)) { // 使用Apache Commons Lang的StringUtils类来检查字符串是否不为空
            queryWrapper.eq("t.forthwith_sputum_code", forthwithSputumCode);
        }
        // 条件3: 仅当screen_time数组不为空且长度为2时
        LocalDateTime[] screenTime = pageReqVO.getScreenTime();
        if (ArrayUtils.isNotEmpty(screenTime) && screenTime.length == 2) { // 使用Apache Commons Lang的ArrayUtils类来检查数组
            queryWrapper.between("t.screen_time", screenTime[0], screenTime[1]);
        }
        // 条件4：夜痰标本号
        String eveningSputumCode = pageReqVO.getEveningSputumCode();
        if(StringUtils.isNotBlank(eveningSputumCode)){
            queryWrapper.eq("t.evening_sputum_code", eveningSputumCode);
        }
        // 条件5：晨痰标本号
        String morningSputumCode = pageReqVO.getMorningSputumCode();
        if(StringUtils.isNotBlank(morningSputumCode)){
            queryWrapper.eq("t.morning_sputum_code", morningSputumCode);
        }
        // 条件6：年份
        String year = pageReqVO.getYear();
        if(StringUtils.isNotBlank(year)){
            queryWrapper.eq("t.YEAR", year);
        }
        // 条件7：筛查类型
        String screenType = pageReqVO.getScreenType();
        if(StringUtils.isNotBlank(screenType)){
            queryWrapper.eq("t.screen_type", screenType);
        }
        // 条件8： 摸底表患者姓名
        String patientName = pageReqVO.getSearchName();
        if(StringUtils.isNotBlank(patientName)){
            queryWrapper.like("p.name", patientName);
        }
        // 条件9：摸底表患者身份证号
        String idCard = pageReqVO.getSearchIdCard();
        if(StringUtils.isNotBlank(idCard)){
            queryWrapper.like("p.`id_num`", idCard);
        }
        // 排序 id排降序
        queryWrapper.orderByDesc("t.id");

        // 执行查询
        return screenSputumExaminationMapper.selectJoinPage(pageReqVO, ScreenSputumExaminationDO.class, queryWrapper);
    }

}