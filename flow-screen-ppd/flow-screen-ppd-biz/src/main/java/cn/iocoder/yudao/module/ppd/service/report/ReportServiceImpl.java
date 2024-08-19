package cn.iocoder.yudao.module.ppd.service.report;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.report.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.report.ReportMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screendistrict.ScreenDistrictMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpersonrealsituation.ScreenPersonMapper;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Resource
    private DictDataApi dictDataApi;

    @Resource
    private ScreenPersonMapper screenPersonMapper;

    @Resource
    private ReportMapper reportMapper;

    @Resource
    private DeptService deptService;

    @Resource
    private ScreenDistrictMapper districtMapper;

    @Override
    public List<Index> getReportData(Integer moreType, Integer currentYear,
                                     Integer quarter, Integer screenType) {
        // key： 多人群分类，value： 指标对象
        Map<Integer, Index> map = new HashMap<>();
        // 应查人集合
        extracted(moreType, currentYear, quarter, screenType, map);
        // 实查人集合
        extracted1(moreType, currentYear, quarter, screenType, map);
        // 活动性肺结核集合
        extracted2(moreType, currentYear, quarter, screenType, map);
        // 病原学阳性集合
        extracted3(moreType, currentYear, quarter, screenType, map);
        if (OLD.equals(moreType)) {
            // 确诊及治疗中患者集合
            extracted4(moreType, currentYear, quarter, screenType, map);
            // 潜伏感染者集合
            extracted5(moreType, currentYear, quarter, screenType, map);
        }
        // 糖尿病患者人数、HIV/AIDS患者数 统计
        if (DIABETES.equals(moreType) || HIV.equals(moreType)) {
            extracted6(moreType, currentYear, quarter, screenType, map);
        }
        extracted(moreType, currentYear, screenType, map);
        extracted1(moreType, currentYear, screenType, map);
        extracted2(moreType, currentYear, screenType, map);
        extracted3(moreType, currentYear, screenType, map);
        extracted4(moreType, currentYear, screenType, map);
        extracted5(moreType, currentYear, screenType, map);
        extracted6(moreType, currentYear, screenType, map);

        ArrayList<Index> indices = new ArrayList<>();
        indices.add(map.get(moreType));
        return indices;
    }

    @Override
    public Index setSomeData(Index index, FilmingReqVO filmingReqVO) {
        FilmingReqVO reqVO = new FilmingReqVO().setYear(filmingReqVO.getYear()).setQuarter(filmingReqVO.getQuarter()).setMoreType(filmingReqVO.getMoreType()).setScreenType(filmingReqVO.getScreenType());
        reqVO.initQuarter();
        reqVO.initYear();
        if (index == null) {
            index = new Index();
        }
//        季度值
//        应拍片人数
        index.setExpectedXRayCount(getFilmingByMoreType(reqVO));
//        实拍片人数
        index.setActualXRayCount(getActualTakeCtNum(reqVO));
//        应采痰人数
        index.setExpectedSputumTestCount(getSputumCollectionByMoreType(reqVO));
//        实际采痰人数
        index.setActualSputumCheckCount(getActualSputumCollectionNum(reqVO));
//        年度值
        reqVO.setQuarter(null);
//        应拍片人数
        index.setExpectedXRayCountY(getFilmingByMoreType(reqVO));
//        实拍片人数
        index.setActualXRayCountY(getActualTakeCtNum(reqVO));
//        应采痰人数
        index.setExpectedSputumTestCountY(getSputumCollectionByMoreType(reqVO));
//        实际采痰人数
        index.setActualSputumCheckCountY(getActualSputumCollectionNum(reqVO));

        return index;
    }

    private void extracted6(Integer moreType, Integer currentYear, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> diabetesAndHivList = screenPersonMapper
                .getDiabetesAndHivList(currentYear, null, screenType);

        diabetesAndHivList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    if (moreType == 16) {
                        mapIndex.setDiabeticPatientCountY(mapIndex.getDiabeticPatientCountY() != null ?
                                mapIndex.getDiabeticPatientCountY() + 1 : 1);
                    }

                    if (moreType == 128) {
                        mapIndex.setHIVPositiveCountY(mapIndex.getHIVPositiveCountY() != null ?
                                mapIndex.getHIVPositiveCountY() + 1 : 1);
                    }
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);

                    if (moreType == 16) {
                        index.setDiabeticPatientCountY(1);
                    }
                    if (moreType == 128) {
                        index.setHIVPositiveCountY(1);
                    }
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted5(Integer moreType, Integer currentYear, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> latentInfectionList = screenPersonMapper
                .getLatentInfectionList(currentYear, null, screenType);

        latentInfectionList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setLatentInfectionCountY(mapIndex.getLatentInfectionCountY() != null ?
                            mapIndex.getLatentInfectionCountY() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setLatentInfectionCountY(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted4(Integer moreType, Integer currentYear, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> confirmedAndTreatingPatientsList = screenPersonMapper
                .getConfirmedAndTreatingPatientsList(currentYear, null, screenType);

        confirmedAndTreatingPatientsList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setConfirmedAndTreatingCountY(mapIndex.getConfirmedAndTreatingCountY() != null ?
                            mapIndex.getConfirmedAndTreatingCountY() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setConfirmedAndTreatingCountY(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted3(Integer moreType, Integer currentYear, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> pathogenPositiveList = screenPersonMapper
                .getPathogenPositiveList(currentYear, null, screenType);

        pathogenPositiveList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setPathogenPositiveCountY(mapIndex.getPathogenPositiveCountY() != null ?
                            mapIndex.getPathogenPositiveCountY() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setPathogenPositiveCountY(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted2(Integer moreType, Integer currentYear, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> activeTuberculosisList = screenPersonMapper
                .getActiveTuberculosisList(currentYear, null, screenType);

        activeTuberculosisList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setActiveTuberculosisCountY(mapIndex.getActiveTuberculosisCountY() != null ?
                            mapIndex.getActiveTuberculosisCountY() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setActiveTuberculosisCountY(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted1(Integer moreType, Integer currentYear, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> plannedScreeningList = screenPersonMapper
                .findPlannedScreening(currentYear, null, screenType);

        plannedScreeningList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setActualCheckedCountY(mapIndex.getActualCheckedCountY() != null ?
                            mapIndex.getActualCheckedCountY() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setActualCheckedCountY(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted(Integer moreType, Integer currentYear, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> screenedCountFromPlanList = screenPersonMapper
                .getScreenedCountFromPlan(currentYear, null, screenType);

        // 统计数量
        screenedCountFromPlanList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setExpectedCheckCountY(mapIndex.getExpectedCheckCountY() != null ?
                            mapIndex.getExpectedCheckCountY() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setExpectedCheckCountY(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted6(Integer moreType, Integer currentYear, Integer quarter, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> diabetesAndHivList = screenPersonMapper
                .getDiabetesAndHivList(currentYear, quarter, screenType);

        diabetesAndHivList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    if (moreType == 16) {
                        mapIndex.setDiabeticPatientCount(mapIndex.getDiabeticPatientCount() != null ?
                                mapIndex.getDiabeticPatientCount() + 1 : 1);
                    }

                    if (moreType == 128) {
                        mapIndex.setHIVPositiveCount(mapIndex.getHIVPositiveCount() != null ?
                                mapIndex.getHIVPositiveCount() + 1 : 1);
                    }
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);

                    if (moreType == 16) {
                        index.setDiabeticPatientCount(1);
                    }
                    if (moreType == 128) {
                        index.setHIVPositiveCount(1);
                    }
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted5(Integer moreType, Integer currentYear, Integer quarter, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> latentInfectionList = screenPersonMapper
                .getLatentInfectionList(currentYear, quarter, screenType);

        latentInfectionList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setLatentInfectionCount(mapIndex.getLatentInfectionCount() != null ?
                            mapIndex.getLatentInfectionCount() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setLatentInfectionCount(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted4(Integer moreType, Integer currentYear, Integer quarter, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> confirmedAndTreatingPatientsList = screenPersonMapper
                .getConfirmedAndTreatingPatientsList(currentYear, quarter, screenType);

        confirmedAndTreatingPatientsList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setConfirmedAndTreatingCount(mapIndex.getConfirmedAndTreatingCount() != null ?
                            mapIndex.getConfirmedAndTreatingCount() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setConfirmedAndTreatingCount(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted3(Integer moreType, Integer currentYear, Integer quarter, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> pathogenPositiveList = screenPersonMapper
                .getPathogenPositiveList(currentYear, quarter, screenType);

        pathogenPositiveList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setPathogenPositiveCount(mapIndex.getPathogenPositiveCount() != null ?
                            mapIndex.getPathogenPositiveCount() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setPathogenPositiveCount(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted2(Integer moreType, Integer currentYear, Integer quarter, Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> activeTuberculosisList = screenPersonMapper
                .getActiveTuberculosisList(currentYear, quarter, screenType);

        activeTuberculosisList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setActiveTuberculosisCount(mapIndex.getActiveTuberculosisCount() != null ?
                            mapIndex.getActiveTuberculosisCount() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setActiveTuberculosisCount(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted1(Integer moreType, Integer currentYear, Integer quarter,
                            Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> plannedScreeningList = screenPersonMapper
                .findPlannedScreening(currentYear, quarter, screenType);

        plannedScreeningList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setActualCheckedCount(mapIndex.getActualCheckedCount() != null ?
                            mapIndex.getActualCheckedCount() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setActualCheckedCount(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    private void extracted(Integer moreType, Integer currentYear, Integer quarter,
                           Integer screenType, Map<Integer, Index> map) {
        List<ScreenPersonDO> screenedCountFromPlanList = screenPersonMapper
                .getScreenedCountFromPlan(currentYear, quarter, screenType);

        // 统计数量
        screenedCountFromPlanList.forEach(item -> {
            // 解析数据库中加密的多人群分类并累加统计数据
            List<Integer> integers = resolveMoreTypeToList(item.getMoreType());

            if (integers.contains(moreType)) {
                Index mapIndex = map.get(moreType);
                if (mapIndex != null) {
                    mapIndex.setExpectedCheckCount(mapIndex.getExpectedCheckCount() != null ?
                            mapIndex.getExpectedCheckCount() + 1 : 1);
                } else {
                    Index index = new Index();
                    index.setMoreType(moreType);
                    index.setExpectedCheckCount(1);
                    map.put(moreType, index);
                }
            }
        });
    }

    @Override
    public Integer getFilming(FilmingReqVO reqVO) {
        reqVO.initQuarter();
        reqVO.initYear();
//        1. 查询应拍片人数
//        1.1 查询6-14岁学生、僧尼和0-5岁非重点人群
        FilmingReqVO filmingStudentReq = new FilmingReqVO().setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType()).setMoreType(33).setLeftAge(6).setRightAge(14);
        List<Long> sixToFourteenStudent = reportMapper.listPersonByTypeAndAge(filmingStudentReq);
        FilmingReqVO fiveStudentReq = new FilmingReqVO().setLeftAge(0).setRightAge(5).setMoreType(0).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> littleFiveNor = reportMapper.listPersonByTypeAndAge(fiveStudentReq);

//        1.2 查找第一步为并行流程且需要做胸片检查的
//        1.2.1 查询含有学生或僧尼身份的患者 且年龄大于等于15
        FilmingReqVO studentAndMonkReq = new FilmingReqVO().setLeftAge(15).setMoreType(33).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> studentAndMonkFifteen = reportMapper.listPersonByTypeAndMinAge(studentAndMonkReq);
//        1.2.2 查找所有含有密接者身份的患者
        FilmingReqVO closerReq = new FilmingReqVO().setMoreType(8).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> closers = reportMapper.listPersonByTypeAndMinAge(closerReq);
//        1.2.3 查找所有含有非重点人群身份的患者  且年龄6-14
        FilmingReqVO norSixToFourteenReq = new FilmingReqVO().setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType()).setMoreType(0).setLeftAge(6).setRightAge(14);
        List<Long> norSixToFourteen = reportMapper.listPersonByTypeAndAge(norSixToFourteenReq);
//        1.2.4 查找老年人、糖尿病、HIV/AIDS、既往患者、  教职工
        FilmingReqVO staffAndOld = new FilmingReqVO().setMoreType(4 + 2 + 16 + 64 + 128).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> staffIds = reportMapper.listPersonByTypeAndMinAge(staffAndOld);
//        1.3 查找全流程为一条线的人群 ：0-5岁学生 和 僧尼
        FilmingReqVO fiveStudentAndMonkReq = new FilmingReqVO().setLeftAge(0).setRightAge(5).setMoreType(33).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> fiveStudentAndMonk = reportMapper.listPersonByTypeAndAge(fiveStudentAndMonkReq);
//        1.4 将1.1-1.3中得到的数组去重
        Set<Long> hashSet = new HashSet<>(sixToFourteenStudent);
        List<List<Long>> ito = new ArrayList<>();
        ito.add(sixToFourteenStudent);
        ito.add(littleFiveNor);
        ito.add(studentAndMonkFifteen);
        ito.add(closers);
        ito.add(norSixToFourteen);
        ito.add(staffIds);
        ito.add(fiveStudentAndMonk);
        for (int i = 1; i < ito.size(); i++) {
            Collection<Long> intersection = CollectionUtil.intersection(hashSet, ito.get(i));
            ito.set(i, new ArrayList<>(CollectionUtil.disjunction(ito.get(i), intersection)));
            hashSet.addAll(ito.get(i));
        }
//        1.5 将去重后的人群id，根据不同流程分类去检查是否异常
//        1.5.1 将6-14岁学生、僧尼和0-5岁非重点人群，查询采集组和ppd组起码有一个异常的
        List<Long> collectAndPpd = ito.get(0);
        collectAndPpd.addAll(ito.get(1));
        Integer collectAndPpdErrorNum = 0;
        FilmingReqVO filmingReqVO;
        if (collectAndPpd != null && !collectAndPpd.isEmpty()) {
            filmingReqVO = new FilmingReqVO().setPersonIds(collectAndPpd).setQuarter(reqVO.getQuarter()).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            collectAndPpdErrorNum = reportMapper.getCollectAndPpdErrorNum(filmingReqVO);
        }
//        1.5.2 计算1.2中的list大小总和
        List<Long> mustDoCt = ito.get(2);
        mustDoCt.addAll(ito.get(3));
        mustDoCt.addAll(ito.get(4));
        mustDoCt.addAll(ito.get(5));
        Integer mustDoCtNum = mustDoCt.size();
//        1.5.3 对1.3 中查找全流程为一条线的人群 ：0-5岁学生 和 僧尼 的ppd异常的
        List<Long> studentAndMonk = ito.get(6);
        Integer ppdErrorNum = 0;
        if (studentAndMonk != null && !studentAndMonk.isEmpty()) {
            filmingReqVO = new FilmingReqVO().setPersonIds(studentAndMonk).setQuarter(reqVO.getQuarter()).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            ppdErrorNum = reportMapper.getPpdErrorNum(filmingReqVO);
        }
        Integer resultNum = collectAndPpdErrorNum + mustDoCtNum + ppdErrorNum;
        System.out.println(resultNum);
        return resultNum;
    }

    @Override
    public Integer getFilmingByMoreType(FilmingReqVO reqVO) {
        CollectUtils<Long> utils = new CollectUtils<>();
        reqVO.initQuarter();
        reqVO.initYear();
        if (SCHOOL.equals(reqVO.getMoreType())) {
            //        1.1 查询6-14岁学生
            FilmingReqVO filmingStudentReq = new FilmingReqVO().setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType()).setMoreType(STUDENT).setLeftAge(6).setRightAge(14);
            List<Long> sixToFourteenStudent = reportMapper.listPersonByTypeAndAge(filmingStudentReq);
            //        1.2.1 查询含有学生年龄大于等于15
            FilmingReqVO studentAndMonkReq = new FilmingReqVO().setLeftAge(15).setMoreType(STUDENT).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> studentAndMonkFifteen = reportMapper.listPersonByTypeAndMinAge(studentAndMonkReq);
//        查找全流程为一条线的人群 ：0-5岁学生
            FilmingReqVO fiveStudentAndMonkReq = new FilmingReqVO().setLeftAge(0).setRightAge(5).setMoreType(STUDENT).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> fiveStudentAndMonk = reportMapper.listPersonByTypeAndAge(fiveStudentAndMonkReq);
//        1.2.4 查找教职工
            FilmingReqVO staffAndOld = new FilmingReqVO().setMoreType(STAFF).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> staffIds = reportMapper.listPersonByTypeAndMinAge(staffAndOld);

            List<List<Long>> ito = new ArrayList<>();
            ito.add(sixToFourteenStudent);
            ito.add(studentAndMonkFifteen);
            ito.add(fiveStudentAndMonk);
            ito.add(staffIds);
            List<List<Long>> result = utils.removeDuplicates(ito);
            Integer collectAndPpdErrorNum = 0;
            FilmingReqVO filmingReqVO;
            if (result.get(0) != null && !result.get(0).isEmpty()) {
                filmingReqVO = new FilmingReqVO().setPersonIds(result.get(0)).setQuarter(reqVO.getQuarter()).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
                collectAndPpdErrorNum = reportMapper.getCollectAndPpdErrorNum(filmingReqVO);
            }
            List<Long> mustDoCt = result.get(1);
            mustDoCt.addAll(result.get(3));
            Integer mustDoCtNum = mustDoCt.size();
            Integer ppdErrorNum = 0;
            if (result.get(2) != null && !result.get(2).isEmpty()) {
                filmingReqVO = new FilmingReqVO().setPersonIds(result.get(2)).setQuarter(reqVO.getQuarter()).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
                ppdErrorNum = reportMapper.getPpdErrorNum(filmingReqVO);
            }
            Integer resultNum = collectAndPpdErrorNum + mustDoCtNum + ppdErrorNum;
            return resultNum;
        } else if (MONK.equals(reqVO.getMoreType())) {
            // 查询6-14岁僧尼
            FilmingReqVO filmingStudentReq = new FilmingReqVO().setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType()).setMoreType(MONK).setLeftAge(6).setRightAge(14);
            List<Long> sixToFourteenStudent = reportMapper.listPersonByTypeAndAge(filmingStudentReq);
            // 查询含有僧尼年龄大于等于15
            FilmingReqVO studentAndMonkReq = new FilmingReqVO().setLeftAge(15).setMoreType(MONK).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> studentAndMonkFifteen = reportMapper.listPersonByTypeAndMinAge(studentAndMonkReq);
//        查找全流程为一条线的人群 ：0-5岁僧尼
            FilmingReqVO fiveStudentAndMonkReq = new FilmingReqVO().setLeftAge(0).setRightAge(5).setMoreType(MONK).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> fiveStudentAndMonk = reportMapper.listPersonByTypeAndAge(fiveStudentAndMonkReq);

            List<List<Long>> ito = new ArrayList<>();
            ito.add(sixToFourteenStudent);
            ito.add(studentAndMonkFifteen);
            ito.add(fiveStudentAndMonk);
            List<List<Long>> result = utils.removeDuplicates(ito);
            Integer collectAndPpdErrorNum = 0;
            FilmingReqVO filmingReqVO;
            if (result.get(0) != null && !result.get(0).isEmpty()) {
                filmingReqVO = new FilmingReqVO().setPersonIds(result.get(0)).setQuarter(reqVO.getQuarter()).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
                collectAndPpdErrorNum = reportMapper.getCollectAndPpdErrorNum(filmingReqVO);
            }
            Integer mustDoCtNum = result.get(1).size();
            Integer ppdErrorNum = 0;
            if (result.get(2) != null && !result.get(2).isEmpty()) {
                filmingReqVO = new FilmingReqVO().setPersonIds(result.get(2)).setQuarter(reqVO.getQuarter()).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
                ppdErrorNum = reportMapper.getPpdErrorNum(filmingReqVO);
            }
            Integer resultNum = collectAndPpdErrorNum + mustDoCtNum + ppdErrorNum;
            return resultNum;
        } else if (CLOSER.equals(reqVO.getMoreType()) || OLD.equals(reqVO.getMoreType()) || DIABETES.equals(reqVO.getMoreType()) || HIV.equals(reqVO.getMoreType())) {
            FilmingReqVO closerReq = new FilmingReqVO().setMoreType(reqVO.getMoreType()).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> closers = reportMapper.listPersonByTypeAndMinAge(closerReq);
            return closers.size();
        }

        return -1;
    }

    @Override
    public Integer getSputumCollection(FilmingReqVO reqVO) {
        reqVO.initQuarter();
        reqVO.initYear();
//        0-5岁学生和僧尼人群
        FilmingReqVO zoneToFiveStudent = new FilmingReqVO().setLeftAge(0).setRightAge(5).setMoreType(1 + 32).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> zoneToFiveStudentIds = reportMapper.listPersonByTypeAndAge(zoneToFiveStudent);
//        6-14岁学生和僧尼人群
        FilmingReqVO sixToFourteenStudent = new FilmingReqVO().setLeftAge(6).setRightAge(14).setMoreType(1 + 32).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> sixToFourteenStudentIds = reportMapper.listPersonByTypeAndAge(sixToFourteenStudent);
//        >=15岁学生和僧尼人群
        FilmingReqVO fifteenStudent = new FilmingReqVO().setLeftAge(15).setMoreType(1 + 32).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> fifteenStudentIds = reportMapper.listPersonByTypeAndMinAge(fifteenStudent);
//        老年人、糖尿病、HIV/AIDS、既往患者人群和教职工人群
        FilmingReqVO staffAndOld = new FilmingReqVO().setMoreType(4 + 2 + 16 + 64 + 128).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> staffIds = reportMapper.listPersonByTypeAndMinAge(staffAndOld);
//        密接者人群
        FilmingReqVO closerReq = new FilmingReqVO().setMoreType(8).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> closers = reportMapper.listPersonByTypeAndMinAge(closerReq);
//        0-5岁非重点人群
        FilmingReqVO zoneToFiveNor = new FilmingReqVO().setLeftAge(0).setRightAge(5).setMoreType(0).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> zoneToFiveNorIds = reportMapper.listPersonByTypeAndAge(zoneToFiveNor);
//        6-14岁非重点人群
        FilmingReqVO sixToFourteenNor = new FilmingReqVO().setLeftAge(6).setRightAge(14).setMoreType(0).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> sixToFourteenNorIds = reportMapper.listPersonByTypeAndAge(sixToFourteenNor);
//        >=15岁非重点人群
        FilmingReqVO fifteenNor = new FilmingReqVO().setLeftAge(15).setMoreType(0).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
        List<Long> fifteenNorIds = reportMapper.listPersonByTypeAndMinAge(fifteenNor);
// 将得到的所有人群的id数组去重
        Set<Long> hashSet = new HashSet<>(zoneToFiveStudentIds);
        List<List<Long>> ito = new ArrayList<>(); // 存储去重后的多个人群分类的id
/*        数据顺序
0：0-5岁学生和僧尼人群 ；
1：6-14岁学生和僧尼人群；
2：>=15岁学生和僧尼人群
3：老年人、糖尿病、HIV/AIDS、既往患者人群和教职工人群
4：密接者人群
5：0-5岁非重点人群
6：6-14岁非重点人群
7：>=15岁非重点人群
 */
        ito.add(zoneToFiveStudentIds);
        ito.add(sixToFourteenStudentIds);
        ito.add(fifteenStudentIds);
        ito.add(staffIds);
        ito.add(closers);
        ito.add(zoneToFiveNorIds);
        ito.add(sixToFourteenNorIds);
        ito.add(fifteenNorIds);
        for (int i = 1; i < ito.size(); i++) {
            Collection<Long> intersection = CollectionUtil.intersection(hashSet, ito.get(i));
            ito.set(i, new ArrayList<>(CollectionUtil.disjunction(ito.get(i), intersection)));
            hashSet.addAll(ito.get(i));
        }
        Integer resultNum = 0;
        Integer num = 0; // 通过sql查询出的具体人群分类的人数
        FilmingReqVO filmingReqVO = new FilmingReqVO().setScreenType(reqVO.getScreenType()).setQuarter(reqVO.getQuarter()).setYear(reqVO.getYear());

        List<Long> personIds1 = new ArrayList<>();
        List<Long> personIds2 = new ArrayList<>();
        List<Long> personIds3 = new ArrayList<>();
        List<Long> personIds4 = new ArrayList<>();
        personIds1.addAll(ito.get(0));

        personIds2.addAll(ito.get(1));
        personIds2.addAll(ito.get(5));

        personIds3.addAll(ito.get(2));
        personIds3.addAll(ito.get(4));
        personIds3.addAll(ito.get(6));

        personIds4.addAll(ito.get(3));
        personIds4.addAll(ito.get(7));

        List<List<Long>> personIto = new ArrayList<>();  // 存储不同流程的人群id列表
        personIto.add(personIds1);
        personIto.add(personIds2);
        personIto.add(personIds3);
        personIto.add(personIds4);

        for (int i = 0; i < personIto.size(); i++) {
            if (personIto.get(i).isEmpty()) {
                continue;
            }
            filmingReqVO.setPersonIds(personIto.get(i));
            switch (i) {
                case 0:
                    num = reportMapper.countCtOrDrError(filmingReqVO);
                    break;
                case 1:
                    num = reportMapper.getCollectAndPpdErrorNum(filmingReqVO);
                    break;
                case 2:
                    num = reportMapper.countCollectOrPpdOrCtOrDrError(filmingReqVO);
                    break;
                case 3:
                    num = reportMapper.countCollectOrCtOrDrError(filmingReqVO);
                    break;
            }
            resultNum += num;
        }
        return resultNum;
    }

    @Override
    public Integer getSputumCollectionByMoreType(FilmingReqVO reqVO) {
        CollectUtils<Long> utils = new CollectUtils<>();
        reqVO.initQuarter();
        reqVO.initYear();
        if (SCHOOL.equals(reqVO.getMoreType())) {
            //        1.1 查询6-14岁学生
            FilmingReqVO filmingStudentReq = new FilmingReqVO().setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType()).setMoreType(STUDENT).setLeftAge(6).setRightAge(14);
            List<Long> sixToFourteenStudent = reportMapper.listPersonByTypeAndAge(filmingStudentReq);
            //        1.2.1 查询含有学生年龄大于等于15
            FilmingReqVO studentAndMonkReq = new FilmingReqVO().setLeftAge(15).setMoreType(STUDENT).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> studentAndMonkFifteen = reportMapper.listPersonByTypeAndMinAge(studentAndMonkReq);
//        查找全流程为一条线的人群 ：0-5岁学生
            FilmingReqVO fiveStudentAndMonkReq = new FilmingReqVO().setLeftAge(0).setRightAge(5).setMoreType(STUDENT).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> fiveStudentAndMonk = reportMapper.listPersonByTypeAndAge(fiveStudentAndMonkReq);
//        1.2.4 查找教职工
            FilmingReqVO staffAndOld = new FilmingReqVO().setMoreType(STAFF).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> staffIds = reportMapper.listPersonByTypeAndMinAge(staffAndOld);

            List<List<Long>> ito = new ArrayList<>();
            ito.add(sixToFourteenStudent);
            ito.add(studentAndMonkFifteen);
            ito.add(fiveStudentAndMonk);
            ito.add(staffIds);
            List<List<Long>> result = utils.removeDuplicates(ito);
            Integer resultNum = 0;
            Integer num = 0; // 通过sql查询出的具体人群分类的人数
            FilmingReqVO filmingReqVO = new FilmingReqVO().setScreenType(reqVO.getScreenType()).setQuarter(reqVO.getQuarter()).setYear(reqVO.getYear());
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).isEmpty()) {
                    continue;
                }
                filmingReqVO.setPersonIds(result.get(i));
                switch (i) {
                    case 0:
                        num = reportMapper.getCollectAndPpdErrorNum(filmingReqVO);
                        break;
                    case 1:
                        num = reportMapper.countCollectOrPpdOrCtOrDrError(filmingReqVO);
                        break;
                    case 2:
                        num = reportMapper.countCtOrDrError(filmingReqVO);
                        break;
                    case 3:
                        num = reportMapper.countCollectOrCtOrDrError(filmingReqVO);
                        break;
                }
                resultNum += num;
            }
            return resultNum;
        } else if (MONK.equals(reqVO.getMoreType())) {
            // 查询6-14岁僧尼
            FilmingReqVO filmingStudentReq = new FilmingReqVO().setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType()).setMoreType(MONK).setLeftAge(6).setRightAge(14);
            List<Long> sixToFourteenStudent = reportMapper.listPersonByTypeAndAge(filmingStudentReq);
            // 查询含有僧尼年龄大于等于15
            FilmingReqVO studentAndMonkReq = new FilmingReqVO().setLeftAge(15).setMoreType(MONK).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> studentAndMonkFifteen = reportMapper.listPersonByTypeAndMinAge(studentAndMonkReq);
            //        查找全流程为一条线的人群 ：0-5岁僧尼
            FilmingReqVO fiveStudentAndMonkReq = new FilmingReqVO().setLeftAge(0).setRightAge(5).setMoreType(MONK).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> fiveStudentAndMonk = reportMapper.listPersonByTypeAndAge(fiveStudentAndMonkReq);

            List<List<Long>> ito = new ArrayList<>();
            ito.add(sixToFourteenStudent);
            ito.add(studentAndMonkFifteen);
            ito.add(fiveStudentAndMonk);
            List<List<Long>> result = utils.removeDuplicates(ito);
            Integer num = 0; // 通过sql查询出的具体人群分类的人数
            Integer resultNum = 0;
            FilmingReqVO filmingReqVO = new FilmingReqVO().setScreenType(reqVO.getScreenType()).setQuarter(reqVO.getQuarter()).setYear(reqVO.getYear());
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).isEmpty()) {
                    continue;
                }
                filmingReqVO.setPersonIds(result.get(i));
                switch (i) {
                    case 0:
                        num = reportMapper.getCollectAndPpdErrorNum(filmingReqVO);
                        break;
                    case 1:
                        num = reportMapper.countCollectOrPpdOrCtOrDrError(filmingReqVO);
                        break;
                    case 2:
                        num = reportMapper.countCtOrDrError(filmingReqVO);
                        break;
                }
                resultNum += num;
            }
            return resultNum;
        } else if (CLOSER.equals(reqVO.getMoreType())) {
            FilmingReqVO closerReq = new FilmingReqVO().setMoreType(CLOSER).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> closers = reportMapper.listPersonByTypeAndMinAge(closerReq);
            if (closers != null && !closers.isEmpty()) {
                closerReq.setPersonIds(closers);
                closerReq.setQuarter(reqVO.getQuarter());
                Integer integer = reportMapper.countCollectOrPpdOrCtOrDrError(closerReq);
                return integer;
            }
            return 0;
        } else if (OLD.equals(reqVO.getMoreType()) || DIABETES.equals(reqVO.getMoreType()) || HIV.equals(reqVO.getMoreType())) {
            FilmingReqVO filmingReqVO = new FilmingReqVO().setMoreType(reqVO.getMoreType()).setYear(reqVO.getYear()).setScreenType(reqVO.getScreenType());
            List<Long> ids = reportMapper.listPersonByTypeAndMinAge(filmingReqVO);
            filmingReqVO.setPersonIds(ids).setQuarter(reqVO.getQuarter());
            if (ids != null && !ids.isEmpty()) {
                return reportMapper.countCollectOrCtOrDrError(filmingReqVO);
            }
            return 0;
        }
        return -1;
    }

    @Override
    public Integer getActualTakeCtNum(FilmingReqVO reqVO) {
        reqVO.initQuarter();
        reqVO.initYear();
        return reportMapper.getActualTakeCtNum(reqVO);
    }

    @Override
    public Integer getActualSputumCollectionNum(FilmingReqVO reqVO) {
        reqVO.initQuarter();
        reqVO.initYear();
        return reportMapper.getActualSputumCollectionNum(reqVO);
    }

    /**
     * 传入数据库中加密的多人群分类值解析成字典类型key数组
     *
     * @param moreType 数据库中加密的多人群分类值
     * @return 多人群分类字典类型key数组
     */
    public List<Integer> resolveMoreTypeToList(Integer moreType) {
        if (moreType == null || moreType == 0) {
            return Collections.emptyList();
        }
        List<DictDataRespDTO> dictList = dictDataApi.getDictDataList("tb_more_people_type");
        Map<Integer, String> typeMap = new HashMap<>();
        for (DictDataRespDTO obj : dictList) {
            typeMap.put(Integer.valueOf(obj.getValue()), obj.getLabel());
        }
        return typeMap.keySet().stream().filter(s -> (moreType & s) == s).collect(Collectors.toList());
    }


    @Override
    public List<SummaryRespSchoolVO> getSchoolSummary(String districtCode, Integer year, String screenPoint, Integer type) {
        // 如果没有选择行政区划，默认为当前所在部门的行政区划
        if (ObjectUtil.isNull(districtCode)) {
            Long deptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());
            districtCode = districtMapper.getDistrictCode(deptId);
        }

        List<SummaryRespSchoolVO> list = reportMapper.getSchoolSummary(year, districtCode, type);

        return list;
    }

    @Override
    public void exportSchoolSummary(HttpServletResponse response, List<SummaryRespSchoolVO> list) {

        try {
            // 通过工具类创建writer，默认创建xls格式
            ExcelWriter writer = ExcelUtil.getWriter();

            writer.merge(0, 0, 4, 12, "筛查情况", true);
            writer.passCurrentRow();
            writer.merge(1, 1, 7, 8, "PPD试验结果", true);
            writer.passCurrentRow();
            writer.merge(1, 1, 10, 12, "X线胸片检查人数", true);

            writer.setColumnWidth(1, 15);
            writer.setColumnWidth(0, 15);
            writer.setColumnWidth(2, 25);
            writer.setColumnWidth(3, 15);
            writer.setColumnWidth(4, 35);
            writer.setColumnWidth(5, 25);
            writer.setColumnWidth(6, 15);
            writer.setColumnWidth(7, 15);
            writer.setColumnWidth(9, 30);

            List<SummaryRespSchoolExportVO> newList = new ArrayList<>();

            // 获取字典数据并转换为映射
            List<DictDataRespDTO> dictList = dictDataApi.getDictDataList("student_type");
            Map<String, String> studentTypeMap = dictList.stream()
                    .collect(Collectors.toMap(DictDataRespDTO::getValue, DictDataRespDTO::getLabel));

            // 遍历原始列表并转换
            for (SummaryRespSchoolVO item : list) {
                SummaryRespSchoolExportVO newItem = new SummaryRespSchoolExportVO();
                newItem.setDistrictName(item.getDistrictName());
                newItem.setSchoolName(item.getSchoolName());
                newItem.setActualStudentNumber(item.getActualStudentNumber());
                newItem.setSymptomsNumber(item.getSymptomsNumber());
                newItem.setPpdNumber(item.getPpdNumber());
                newItem.setPpdReNumber(item.getPpdReNumber());
                newItem.setFeminine(item.getFeminine());
                newItem.setMasculine(item.getMasculine());
                newItem.setNoPpdNumber(item.getNoPpdNumber());
                newItem.setSumXrayNumber(item.getSumXrayNumber());
                newItem.setNormalXrayNumber(item.getNormalXrayNumber());
                newItem.setAbnormalXrayNumber(item.getAbnormalXrayNumber());

                // 使用映射来快速获取 studentType 的标签
                String studentTypeLabel = studentTypeMap.get(item.getStudentType().toString());
                newItem.setStudentType(studentTypeLabel);

                newList.add(newItem);
            }

            // 如果你需要使用 CollUtil 可以这样做
            List<SummaryRespSchoolExportVO> rows = new ArrayList<>(newList);
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(rows, true);
//            writer.merge(0,2,0,0,"序号",true);
            writer.merge(0, 2, 0, 0, "学校所在区划", true);
            writer.merge(0, 2, 1, 1, "学校全称", true);
            writer.merge(0, 2, 2, 2, "学生类别", true);
            writer.merge(0, 2, 3, 3, "实际招生人数", true);
            writer.merge(1, 2, 4, 4, "有肺结核可以症状或密切接触史人数", true);
            writer.merge(1, 2, 5, 5, "PPD皮试人数", true);
            writer.merge(1, 2, 6, 6, "PPD复验人数", true);
            writer.merge(1, 2, 9, 9, "不适宜PPD筛查人数（禁忌症）", true);

            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=test.xls");

            ServletOutputStream out = response.getOutputStream();

            writer.flush(out, true);
            // 关闭writer，释放内存
            writer.close();
            //此处记得关闭输出Servlet流
            IoUtil.close(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SummaryRespAgencyVO> getAgencySummary(String districtCode, Integer year, String screenPoint, Integer type) {

        // 如果没有选择行政区划，默认为当前所在部门的行政区划
        if (ObjectUtil.isNull(districtCode)) {
            Long deptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());
            districtCode = districtMapper.getDistrictCode(deptId);
        }

        List<SummaryRespAgencyVO> list = reportMapper.getAgencySummary(year, districtCode, type);

        return list;
    }

    @Override
    public void exportAgencySummary(HttpServletResponse response, List<SummaryRespAgencyVO> list) {

        try{
            // 通过工具类创建writer，默认创建xls格式
            ExcelWriter writer = ExcelUtil.getWriter();

            writer.merge(0,0,3,5,"PPD试验结果",true);
            writer.passCurrentRow();

            writer.setColumnWidth(1, 20);
            writer.setColumnWidth(0, 20);
            writer.setColumnWidth(2, 15);
            writer.setColumnWidth(3, 15);

            List<SummaryRespAgencyVO> rows = CollUtil.newArrayList(list);
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(rows, true);
//            writer.merge(0,1,0,0,"序号",true);
            writer.merge(0,1,0,0,"筛查机构所在区",true);
            writer.merge(0,1,1,1,"筛查机构全称",true);
            writer.merge(0,1,2,2,"PPD试验人数",true);

            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition", "attachment;filename=test.xls");

            ServletOutputStream out = response.getOutputStream();

            writer.flush(out, true);
            // 关闭writer，释放内存
            writer.close();
            //此处记得关闭输出Servlet流
            IoUtil.close(out);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
