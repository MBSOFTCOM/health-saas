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
        // 是否是新生
        boolean isNewStud = screenPersonDO.getIsNewStudent() != null && screenPersonDO.getIsNewStudent() == 1;
        // 年龄
        int age = screenPersonDO.getAge() != null ? screenPersonDO.getAge() : -1;

        // 采集组最近一次采集症状数据
        ScreenCollectDO screenCollectDO = screenCollectMapper
                .selectByPersonIdLastTime(personId, year, screenType);
        // 是否做过查验卡痕
        tbHealthScreening.setDoneCheckMark(screenCollectDO != null);
        // 有无卡痕 其他=9
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
            // 咳嗽、咳痰（超过 2 周）
            tbHealthScreening.setCoughOrSputumForMoreThanOneWeek(outcome.contains("1"));
            // 咯血或血痰
            tbHealthScreening.setHemoptysisOrBloodSputum(outcome.contains("2"));
            // 发热
            tbHealthScreening.setFever(outcome.contains("5"));
            // 反复发烧2周以上
//            tbHealthScreening.setPersistentFever(outcome.contains("3") && isNewStud);
            // 淋巴结肿大
//            tbHealthScreening.setLymphoidEnlargement(outcome.contains("4") && isNewStud);
            // 胸痛
            tbHealthScreening.setChestPain(outcome.contains("7"));
            // 乏力、盗汗
            tbHealthScreening.setNightSweats(outcome.contains("3"));
            // 食欲不振
            tbHealthScreening.setLossOfAppetite(outcome.contains("6"));
            // 乏力
//            tbHealthScreening.setFatigue(outcome.contains("7") && !isNewStud);
            // 体重减轻（超过 6 斤）
            tbHealthScreening.setWeightLossOverSixPounds(outcome.contains("4"));
            // 有无卡痕
//            tbHealthScreening.setCheckMark(outcome.contains("9") && !isNewStud);

            // 采集组医生签名
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
        // 是否是非重点人群
        boolean isMonkOrNun = firstType != null && firstType == 2;

        // 是否是教职工
        boolean isTeacher = moreType != null &&
                isObtainable(moreType, 4);
        // 是否是僧尼
        boolean isMonk = moreType != null
                && isObtainable(moreType, 32);
        // 是否是学生
        boolean isStud = moreType != null
                && isObtainable(moreType, 1);
        // 是否是老年人
        boolean isElderly = moreType != null
                && isObtainable(moreType, 2);
        // 是否是密接者
        boolean isCloseContactWithActivePulmonaryTB = moreType != null
                && isObtainable(moreType, 8);
        // 是否是既往患者
        boolean isPastTBPatient = moreType != null
                && isObtainable(moreType, 64);
        // 是否是糖尿病
        boolean isDiabetesPatient = moreType != null
                && isObtainable(moreType, 16);
        // 是否是HIV/AIDS
        boolean isHivPatient = moreType != null
                && isObtainable(moreType, 128);

        // 僧尼
        boolean isMonkOrNun0To5Years = isMonk && (age >= 0 && age <= 5);
        boolean isMonkOrNun6To14Years = isMonk && (age >= 6 && age <= 14);
        boolean isMonkOrNunOver15Years = isMonk && (age >= 15);

        tbHealthScreening.setCloseContactWithActivePulmonaryTB(isCloseContactWithActivePulmonaryTB);

        tbHealthScreening.setMonkOrNun0To5Years(isMonkOrNun0To5Years);
        tbHealthScreening.setMonkOrNun6To14Years(isMonkOrNun6To14Years);
        tbHealthScreening.setMonkOrNunOver15Years(isMonkOrNunOver15Years);

        // 是否是HIV/AIDS
        tbHealthScreening.setHIVorAIDS(isHivPatient);
        // 是否是既往患者
        tbHealthScreening.setPastTBPatient(isPastTBPatient);

        // 在校师生
        boolean isStudent0To5Years = isStud && (age >= 0 && age <= 5);
        boolean isStudent6To14Years = isStud && (age >= 6 && age <= 14);
        boolean isStudentOver15Years = isStud && (age >= 15);

        tbHealthScreening.setStudent0To5Years(isStudent0To5Years);
        tbHealthScreening.setStudent6To14Years(isStudent6To14Years);
        tbHealthScreening.setStudentOver15Years(isStudentOver15Years);

        // 是否在校师生教职工
        tbHealthScreening.setSchoolStaff(isTeacher);
        // 是否老年人
        tbHealthScreening.setElderly(isElderly);
        // 是否糖尿病患者
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
        // ppd组医生签名
        tbHealthScreening.setPpdDoctorSignature(url);

        // 胸部X线检查
        TBHealthScreening chestRadiographData = screenChestRadiographMapper.selectByPersonIdLastTime(personId);
        // 是否做过胸部X线
        tbHealthScreening.setChestXRayDone(chestRadiographData != null);
        boolean isNoTBRelatedAbnormalities = (chestRadiographData != null && chestRadiographData.getOutcome() != null)
                && chestRadiographData.getOutcome() == 0;
        boolean isSuspectedTB = (chestRadiographData != null && chestRadiographData.getOutcome() != null)
                && chestRadiographData.getOutcome() == 1;
        // 是否无结核相关异常
        tbHealthScreening.setNoTBRelatedAbnormalities(isNoTBRelatedAbnormalities);
        // 是否疑似结核
        tbHealthScreening.setSuspectedTB(isSuspectedTB);
        String chestXRayCode = chestRadiographData != null ? chestRadiographData.getChestXRayCode() : "";
        // 机器中与患者对应的编码
        tbHealthScreening.setChestXRayCode(chestXRayCode);
        // dr医生签名
        tbHealthScreening.setChestXRayDoctorSignature(screenImagesMapper.selectLastTimeUrl(personId, 10
                , year, screenType));

        // 痰标本
        Integer typeSputumExamination = screenSputumExaminationMapper
                .selectByPersonIdLastTime(personId);
        // 是否做过痰标本
        tbHealthScreening.setSputumSpecimenDone(typeSputumExamination != null);
        // 是否即时痰
        tbHealthScreening.setImmediateSputum(typeSputumExamination != null && typeSputumExamination == 2);
        // 是否发放晨痰
        tbHealthScreening.setMorningSputum(typeSputumExamination != null && typeSputumExamination == 3);
        // 是否夜间痰
        tbHealthScreening.setNighttimeSputum(typeSputumExamination != null && typeSputumExamination == 4);
        // 是否无痰
        tbHealthScreening.setNoSputum(typeSputumExamination != null && typeSputumExamination == 1);

        // 痰标本医生签字
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
        groups.put(128, "HIV/AIDS");

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