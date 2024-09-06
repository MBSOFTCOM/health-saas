package cn.iocoder.yudao.module.ppd.service.screencollect;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectPageReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.ScreenCollectSaveReqVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo.WxCollectReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screencollect.ScreenCollectMapper;
import cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation.ScreenPersonService;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import jakarta.annotation.Resource;
import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Calendar;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.*;

/**
 * 采集 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreenCollectServiceImpl implements ScreenCollectService {

    @Resource
    private ScreenCollectMapper screenCollectMapper;
    @Resource
    private ScreenPersonService screenPersonService;
    @Resource
    private DeptService deptService;

    @Override
    public Long createScreenCollect(ScreenCollectSaveReqVO createReqVO) {
        // 插入
        ScreenCollectDO screenCollect = BeanUtils.toBean(createReqVO, ScreenCollectDO.class);
        screenCollectMapper.insert(screenCollect);
        // 返回
        return screenCollect.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createScreenCollect(WxCollectReqVO createReqVO) {
        ScreenPersonDO screenPerson = screenPersonService.getScreenPerson(createReqVO.getStudentId());
        if (BeanUtil.isEmpty(screenPerson)){
            throw exception(SCREEN_PERSON_NOT_EXISTS);
        }
        ScreenCollectDO screenCollectDO = wxVoToDO(createReqVO);
        if (BeanUtil.isEmpty(screenCollectDO)){
            throw exception(WX_COLLECT_ADD_FAIL);
        }
        String idNum = screenPerson.getIdNum();
        Integer age=0;
        Integer order=1;
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        Long deptId = deptService.getMyDept(loginUserId);
        // 根据身份证号screenPerson.getIdNum()计算年龄
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH)+1;
        int dayNow = cal.get(Calendar.DATE);

        int year = Integer.parseInt(idNum.substring(6, 10));
        int month = Integer.parseInt(idNum.substring(10,12));
        int day = Integer.parseInt(idNum.substring(12,14));

        if ((month < monthNow) || (month == monthNow && day<= dayNow) ){
            age = yearNow - year;
        }else {
            age = yearNow - year-1;
        }
        order=getLastOrder(2,yearNow,screenPerson.getId(),2);
        screenCollectDO.setDataSources(1);
        screenCollectDO.setIdNum(idNum);
        screenCollectDO.setAge(age);
        screenCollectDO.setClassroom(screenPerson.getClassroom());
        screenCollectDO.setSchoolName(screenPerson.getSchoolOrTemple());
        screenCollectDO.setScreenId(screenPerson.getScreenId());
        screenCollectDO.setScreenAgency(deptService.getDept(deptId).getName());
        screenCollectDO.setTel(screenPerson.getTel());
        screenCollectDO.setScreenType(2);
        screenCollectDO.setScreenPoint(screenPerson.getScreenPoint());
        screenCollectDO.setYear(yearNow);
        screenCollectDO.setScreenOrder(order);
        screenCollectMapper.insert(screenCollectDO);
        return screenCollectDO.getId();
    }

    /**
     * 将微信小程序获取的新增参数转换为DO实体
     * @param reqVO  微信小程序新增参数
     * @return 采集组实体
     */
    private ScreenCollectDO wxVoToDO(WxCollectReqVO reqVO){
        ScreenCollectDO screenCollectDO = new ScreenCollectDO();
        StringBuffer outcome = new StringBuffer();
        if (reqVO.getCough()==1){
            outcome.append("1");
        }
        if (reqVO.getBloodySputum()==1){
            outcome.append("2");
        }
        if (reqVO.getNightSweat()==1){
            outcome.append("3");
        }
        if (reqVO.getLoseWeight()==1){
            outcome.append("4");
        }
        if (reqVO.getFever()==1){
            outcome.append("5");
        }
        if (reqVO.getInappetence()==1){
            outcome.append("6");
        }
        if (reqVO.getChestPain()==1){
            outcome.append("7");
        }
        if (reqVO.getContactHistory()==1){
            screenCollectDO.setContacted(1);
        }else if (reqVO.getContactHistory()==2){
            screenCollectDO.setContacted(2);
        }
        if (!outcome.isEmpty()){
            screenCollectDO.setOutcome(outcome.toString());
        }
        screenCollectDO.setPersonId(reqVO.getStudentId());
        return screenCollectDO;
    }
    @Override
    public void updateScreenCollect(ScreenCollectSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenCollectExists(updateReqVO.getId());
        // 更新
        ScreenCollectDO updateObj = BeanUtils.toBean(updateReqVO, ScreenCollectDO.class);
        screenCollectMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenCollect(Long id) {
        // 校验存在
        validateScreenCollectExists(id);
        // 删除
        screenCollectMapper.deleteById(id);
    }

    private void validateScreenCollectExists(Long id) {
        if (screenCollectMapper.selectById(id) == null) {
            throw exception(SCREEN_COLLECT_NOT_EXISTS);
        }
    }

    @Override
    public ScreenCollectDO getScreenCollect(Long id) {
        return screenCollectMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenCollectDO> getScreenCollectPage(ScreenCollectPageReqVO pageReqVO) {
        return screenCollectMapper.selectPage(pageReqVO);
    }

    @Override
    public Integer getLastOrder(Integer screenType, Integer year, Long personId, Integer dataSource) {
        ScreenCollectDO wxLastTime = screenCollectMapper.selectWxLastTime(personId, year, screenType, dataSource);
        if (BeanUtil.isEmpty(wxLastTime) || wxLastTime.getScreenOrder()==null){
            return 1;
        }
        return wxLastTime.getScreenOrder();
    }
}