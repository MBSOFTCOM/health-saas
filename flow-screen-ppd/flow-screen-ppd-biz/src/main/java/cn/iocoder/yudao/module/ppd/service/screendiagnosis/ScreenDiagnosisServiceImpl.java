package cn.iocoder.yudao.module.ppd.service.screendiagnosis;


import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenchestradiograph.ScreenChestRadiographMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screencollect.ScreenCollectMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screendiagnosis.ScreenDiagnosisMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenimages.ScreenImagesMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpersonrealsituation.ScreenPersonMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenppd.ScreenPpdMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screensputumexamination.ScreenSputumExaminationMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.format.DateTimeFormatter;
import java.util.*;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_DIAGNOSIS_NOT_EXISTS;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.SCREEN_PERSON_NOT_EXISTS;

/**
 * 诊断组 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ScreenDiagnosisServiceImpl implements ScreenDiagnosisService {

    @Resource
    private ScreenPersonMapper screenPersonMapper;
    @Resource
    private ScreenCollectMapper screenCollectMapper;
    @Resource
    private ScreenImagesMapper screenImagesMapper;
    @Resource
    private ScreenPpdMapper screenPpdMapper;
    @Resource
    private ScreenChestRadiographMapper screenChestRadiographMapper;
    @Resource
    private ScreenSputumExaminationMapper screenSputumExaminationMapper;



    @Override
    public TBHealthScreening getTbHealthScreening(Long personId, Integer year, Integer screenType)  {
        // 只需获取每组中最新一次筛查数据
        TBHealthScreening tbHealthScreening = new TBHealthScreening();

        ScreenPersonDO screenPersonDO = screenPersonMapper.selectById(personId);
        if(screenPersonDO == null){
            throw exception(SCREEN_PERSON_NOT_EXISTS);
        }
        boolean isNewStud = screenPersonDO.getIsNewStudent() != null && screenPersonDO.getIsNewStudent() == 1;
        int age = screenPersonDO.getAge() != null ? screenPersonDO.getAge() : -1;

        // 采集组最近一次采集症状数据
        ScreenCollectDO screenCollectDO = screenCollectMapper
                .selectByPersonIdLastTime(personId, year, screenType);
        tbHealthScreening.setDoneCheckMark(screenCollectDO != null);
        tbHealthScreening.setCheckMark(null);

        if(screenCollectDO != null){
            // 症状结果
            String outcome = screenCollectDO.getOutcome();

            // 体检日期
            if(screenCollectDO.getScreenTime() != null){
                tbHealthScreening.setExaminationDate(DateTimeFormatter.ofPattern("yyyy年MM月dd日")
                        .format(screenCollectDO.getScreenTime()));
            }

            // 症状部分内容
            tbHealthScreening.setCoughOrSputumForMoreThanOneWeek(outcome.contains("1"));
            tbHealthScreening.setHemoptysisOrBloodSputum(outcome.contains("2"));
            tbHealthScreening.setFever(outcome.contains("3") && !isNewStud);
            tbHealthScreening.setPersistentFever(outcome.contains("3") && isNewStud);
            tbHealthScreening.setLymphoidEnlargement(outcome.contains("4") && isNewStud);
            tbHealthScreening.setChestPain(outcome.contains("4") && !isNewStud);
            tbHealthScreening.setNightSweats(outcome.contains("5") && !isNewStud);
            tbHealthScreening.setLossOfAppetite(outcome.contains("6") && !isNewStud);
            tbHealthScreening.setFatigue(outcome.contains("7") && !isNewStud);
            tbHealthScreening.setWeightLossOverSixPounds(outcome.contains("8") && !isNewStud);

            tbHealthScreening.setCheckMark(outcome.contains("9") && !isNewStud);

            String url = screenImagesMapper.selectLastTimeUrl(personId, 8, year, screenType);
            tbHealthScreening.setCollectDoctorSignature(url);
        }

        // 表头信息
        tbHealthScreening.setScreeningNumber(screenPersonDO.getScreenId());
        tbHealthScreening.setIdNumber(screenPersonDO.getIdNum());
        tbHealthScreening.setName(screenPersonDO.getName());
        tbHealthScreening.setAge(screenPersonDO.getAge());

        // 人群分类
        Integer moreType = screenPersonDO.getMoreType();
        Integer firstType = screenPersonDO.getFirstType();

        boolean isMonkOrNun = firstType != null && firstType == 2;

        boolean isTeacher = moreType != null &&
                isObtainable(moreType, 4);
        boolean isMonk = moreType != null
                && isObtainable(moreType, 32);
        boolean isStud = moreType != null
                && isObtainable(moreType, 1);
        boolean isElderly = moreType != null
                && isObtainable(moreType, 2);
        boolean isCloseContactWithActivePulmonaryTB = moreType != null
                && isObtainable(moreType, 8);
        boolean isPastTBPatient = moreType != null
                && isObtainable(moreType, 64);
        boolean isDiabetesPatient = moreType != null
                && isObtainable(moreType, 16);

        boolean isMonkOrNun0To5Years = isMonk && (age >= 0 && age <= 5);
        boolean isMonkOrNun6To14Years = isMonk && (age >= 6 && age <= 14);
        boolean isMonkOrNunOver15Years = isMonk && (age >= 15);

        tbHealthScreening.setCloseContactWithActivePulmonaryTB(isCloseContactWithActivePulmonaryTB);

        tbHealthScreening.setMonkOrNun0To5Years(isMonkOrNun0To5Years);
        tbHealthScreening.setMonkOrNun6To14Years(isMonkOrNun6To14Years);
        tbHealthScreening.setMonkOrNunOver15Years(isMonkOrNunOver15Years);

        // TODO 数据库中未存储该字段数据
        tbHealthScreening.setHIVorAIDS(false);

        tbHealthScreening.setPastTBPatient(isPastTBPatient);

        // 在校师生
        boolean isStudent0To5Years = isStud && (age >= 0 && age <= 5);
        boolean isStudent6To14Years = isStud && (age >= 6 && age <= 14);
        boolean isStudentOver15Years = isMonk && (age >= 15);

        tbHealthScreening.setStudent0To5Years(isStudent0To5Years);
        tbHealthScreening.setStudent6To14Years(isStudent6To14Years);
        tbHealthScreening.setStudentOver15Years(isStudentOver15Years);

        tbHealthScreening.setSchoolStaff(isTeacher);
        tbHealthScreening.setElderly(isElderly);
        tbHealthScreening.setDiabetesPatient(isDiabetesPatient);

        // 是否非重点人群
        boolean isNonKeyPopulation0To5Years = isMonkOrNun && (age >= 0 && age <= 5);
        boolean isNonKeyPopulation6To14Years = isMonkOrNun &&  (age >= 6 && age <= 14);
        boolean isNonKeyPopulationOver15Years = isMonkOrNun && (age >= 15);

        tbHealthScreening.setNonKeyPopulation0To5Years(isNonKeyPopulation0To5Years);
        tbHealthScreening.setNonKeyPopulation6To14Years(isNonKeyPopulation6To14Years);
        tbHealthScreening.setNonKeyPopulationOver15Years(isNonKeyPopulationOver15Years);

        // ppd测试数据
        TBHealthScreening ppdData = screenPpdMapper
                .selectByPersonIdLastTime(personId);
        tbHealthScreening.setPpdTestDone(ppdData != null);
        if(ppdData != null && ppdData.getPpdInjectionTime() != null){
            tbHealthScreening.setPpdInjectionTimeStr(DateTimeFormatter
                    .ofPattern("yyyy年MM月dd日")
                    .format(ppdData.getPpdInjectionTime()));
        }
        // 从离线图片表中获取数据
        String url = screenImagesMapper.selectLastTimeUrl(personId, 9, year, screenType);
        tbHealthScreening.setPpdDoctorSignature(url);

        // 胸部X线检查
        TBHealthScreening chestRadiographData = screenChestRadiographMapper.selectByPersonIdLastTime(personId);
        tbHealthScreening.setChestXRayDone(chestRadiographData != null);
        boolean isNoTBRelatedAbnormalities = (chestRadiographData != null && chestRadiographData.getOutcome() != null)
                && chestRadiographData.getOutcome() == 0;
        boolean isSuspectedTB = (chestRadiographData != null && chestRadiographData.getOutcome() != null)
                && chestRadiographData.getOutcome() == 1;
        tbHealthScreening.setNoTBRelatedAbnormalities(isNoTBRelatedAbnormalities);
        tbHealthScreening.setSuspectedTB(isSuspectedTB);
        String chestXRayCode = chestRadiographData != null ? chestRadiographData.getChestXRayCode() : "";
        tbHealthScreening.setChestXRayCode(chestXRayCode);

        tbHealthScreening.setChestXRayDoctorSignature(screenImagesMapper.selectLastTimeUrl(personId, 10
                , year, screenType));

        // 痰标本
        Integer typeSputumExamination = screenSputumExaminationMapper
                .selectByPersonIdLastTime(personId);
        tbHealthScreening.setSputumSpecimenDone(typeSputumExamination != null);
        tbHealthScreening.setImmediateSputum(typeSputumExamination != null && typeSputumExamination == 2);
        tbHealthScreening.setMorningSputum(typeSputumExamination != null && typeSputumExamination == 3);
        tbHealthScreening.setNighttimeSputum(typeSputumExamination != null && typeSputumExamination == 4);
        tbHealthScreening.setNoSputum(typeSputumExamination != null && typeSputumExamination == 1);

        tbHealthScreening.setSputumDoctorSignature(screenImagesMapper.selectLastTimeUrl(personId, 12
                , year, screenType));
        return tbHealthScreening;
    }


    /**
     * moreType 表示数据库存储的分类编号之和
     * @param moreType 分类编号之和
     * @param n 单个分类代表的编号
     * @return 是否存在
     */
    public boolean isObtainable(int moreType, int n) {
        Map<Integer, String> groups = new HashMap<>();
        groups.put(1, "学生");
        groups.put(2, "老年人");
        groups.put(4, "教职工");
        groups.put(8, "密接者");
        groups.put(16, "糖尿病");
        groups.put(32, "僧尼");
        groups.put(64, "既往患者");

        // 将分类编号进行排序
        List<Integer> keys = new ArrayList<>(groups.keySet());
        keys.sort(Collections.reverseOrder());

        for (int key : keys) {
            if ((moreType & key) == key) {
                if(n == key){
                    return true;
                }
                moreType -= key;
            }
        }

        return false;
    }
}