package cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation;

import cn.hutool.core.io.IoUtil;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.TBHealthScreening;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencomputedtomography.ScreenComputedTomographyDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenimages.ScreenImagesDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenrepeatperson.ScreenRepeatPersonDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screencomputedtomography.ScreenComputedTomographyMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screendistrict.ScreenDistrictMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenimages.ScreenImagesMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpersonrealsituation.ScreenPersonMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenrepeatperson.ScreenRepeatPersonMapper;
import cn.iocoder.yudao.module.ppd.service.screendiagnosis.ScreenDiagnosisService;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.*;


/**
 * 摸底 Service 实现类
 *
 * @author 侯卿
 */
@Service
@Validated
@Transactional
public class ScreenPersonServiceImpl implements ScreenPersonService {

    @Resource
    private ScreenPersonMapper screenPersonMapper;
    @Resource
    private ScreenImagesMapper screenImagesMapper;
    @Resource
    private ScreenDistrictMapper screenDistrictMapper;
    @Resource
    private DictDataApi dictDataApi;
    @Resource
    private FileApi fileApi;
    @Resource
    private ScreenDiagnosisService screenDiagnosisService;
    @Resource
    private ScreenRepeatPersonMapper screenRepeatPersonMapper;
    @Resource
    private ScreenComputedTomographyMapper screenComputedTomographyMapper;

    @Override
    public Long createScreenPerson(ScreenPersonSaveReqVO createReqVO) {
        // 插入
        ScreenPersonDO screenPerson = BeanUtils.toBean(createReqVO, ScreenPersonDO.class);

        Long idNum = screenPersonMapper.isNullByIdNumYear(createReqVO.getIdNum(), screenPerson.getYear(), screenPerson.getScreenType());
        if (idNum != null) {
            throw exception(IDNUM_ESITS);
        }
        // 判断教职工
        if (screenPerson.getFirstType() == 4) {
            screenPerson.setMoreType(screenPerson.getMoreType() + 4);
        }
        if (screenPerson.getMoreType() != null) {
            //判断学生
            if (!resolveMoreTypeToString(screenPerson.getMoreType()).contains("学生")) {
                screenPerson.setClassroom("").setIsNewStudent(0);
            }
        }

        Integer year = screenPerson.getYear();

        String town = screenPerson.getTown();

        List<String> screenIdList = screenPersonMapper.getMaxScreenId(year, town);
        // 根据镇名称查询对应的区域代码
//        String code = screenDistrictMapper.selectByName(town);

        // 找到该乡镇最大的筛查编号
        String maxScreenId = constructMaxScreenId(screenIdList, town.substring(0,9), year, screenPerson.getScreenType());

        String screenId = makeScreenId(maxScreenId, town.substring(0,9));

        screenPerson.setScreenId(screenId);

        screenPersonMapper.insert(screenPerson);
        // 返回
        return screenPerson.getId();
    }

    @Override
    public void updateScreenPerson(ScreenPersonSaveReqVO updateReqVO) {
        // 校验存在
        validateScreenPersonExists(updateReqVO.getId());
        // 更新
        ScreenPersonDO updateObj = BeanUtils.toBean(updateReqVO, ScreenPersonDO.class);
        Integer firstType = updateObj.getFirstType();
        Integer moreType = updateObj.getMoreType();

        // 根据第一个类型判断并更新更多类型
        if (firstType == 4) {
            moreType += 4;
        }

        // 判断学生和僧尼
        String resolvedMoreType = resolveMoreTypeToString(moreType);
        if (moreType != null) {
            if (!resolvedMoreType.contains("学生")) {
                updateObj.setClassroom("").setIsNewStudent(0);
            }
        }

        updateObj.setMoreType(moreType);
        screenPersonMapper.updateById(updateObj);
    }

    @Override
    public void deleteScreenPerson(Long id) {
        // 校验存在
        validateScreenPersonExists(id);
        // 删除
        screenPersonMapper.deleteById(id);
    }

    private void validateScreenPersonExists(Long id) {
        if (screenPersonMapper.selectById(id) == null) {
            throw exception(SCREEN_PERSON_NOT_EXISTS);
        }
    }

    @Override
    public ScreenPersonDO getScreenPerson(Long id) {
        return screenPersonMapper.selectById(id);
    }

    @Override
    public PageResult<ScreenPersonDO> getScreenPersonPage(ScreenPersonPageReqVO pageReqVO) {
        return screenPersonMapper.selectPage(pageReqVO);
    }


    @Override
    public PageResult<ScreenPersonDO> getScreenedPage(ScreenPersonPageReqVO pageReqVO) {
        return screenPersonMapper.selectScreenedPage(pageReqVO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScreenPersonImportRespVO importScreenPerson(List<ScreenPersonImportVO> list, Integer year, Integer screenType, LocalDateTime screenTime) {
        // 初始化变量
        List<ScreenPersonDO> batchInsert = new ArrayList<>();
        List<ScreenPersonDO> batchUpdate = new ArrayList<>();
        List<ScreenRepeatPersonDO> batchInsert2 = new ArrayList<>();
        List<ScreenRepeatPersonDO> batchUpdate2 = new ArrayList<>();
        List<String> createSpecification = new ArrayList<>();
        // 重复人员
        List<String> createRepeatSpecification = new ArrayList<>();
        Map<Integer, String> failureSpecification = new HashMap<>();
        ScreenPersonImportRespVO screenPersonImportRespVO = ScreenPersonImportRespVO.builder().build();

        // 创建一个 Map 用于记录 idNum 出现的次数
        Map<String, Integer> idNumCountMap = new HashMap<>();

        if (!list.isEmpty()) {
            list.remove(list.size() - 1);
        }

        // 使用 Stream 过滤空对象
        List<ScreenPersonImportVO> filteredList = list.stream()
                .filter(vo -> !vo.isEmpty())
                .collect(Collectors.toList());

        // 创建两个新列表，用于存储重复和非重复数据
        List<ScreenPersonImportVO> duplicateList = new ArrayList<>();
        List<ScreenPersonImportVO> uniqueList = new ArrayList<>();
        // 遍历列表，记录 idNum 出现的次数
        for (ScreenPersonImportVO obj : filteredList) {
            String idNum = obj.getIdNum();
            idNumCountMap.put(idNum, idNumCountMap.getOrDefault(idNum, 0) + 1);
        }
        // 遍历列表，根据 idNum 的出现次数将数据放入不同的列表
        for (ScreenPersonImportVO obj : filteredList) {
            String idNum = obj.getIdNum();
            if (idNumCountMap.get(idNum) > 1) {
                // 这是重复数据，放入重复列表
                duplicateList.add(obj);
            } else {
                // 这是非重复数据，放入非重复列表
                uniqueList.add(obj);
            }
        }
        // 创建一个空的ImportVO列表，用于存放最终的结果
        List<ImportVO> importVOList = new ArrayList<>();
        // 遍历uniqueList中的每个ScreenPersonImportVO对象
        // 使用迭代器遍历 uniqueList，以便在遍历过程中安全地移除元素
        Iterator<ScreenPersonImportVO> iterator = uniqueList.iterator();
        while (iterator.hasNext()) {
            ScreenPersonImportVO obj = iterator.next();
            // 根据乡镇名称查询对应的区域代码
            String code = screenDistrictMapper.selectByName(obj.getTown());
            // 根据省名称查询对应的区域代码
            String provinceCode = screenDistrictMapper.selectByName(obj.getProvince());
            // 根据市/州名称查询对应的区域代码
            String cityCode = screenDistrictMapper.selectByName(obj.getCity());
            // 根据区/县名称查询对应的区域代码
            String countyCode = screenDistrictMapper.selectByName(obj.getCounty());
            // 根据户籍乡镇名称查询对应的区域代码
            String code1 = screenDistrictMapper.selectByName(obj.getPermanentAddressTown());
            // 根据户籍省名称查询对应的区域代码
            String provinceCode1 = screenDistrictMapper.selectByName(obj.getPermanentAddressProvince());
            // 根据户籍市/州名称查询对应的区域代码
            String cityCode1 = screenDistrictMapper.selectByName(obj.getPermanentAddressCity());
            // 根据户籍区/县名称查询对应的区域代码
            String countyCode1 = screenDistrictMapper.selectByName(obj.getPermanentAddressCounty());

            if (code1 != null && countyCode1 != null && cityCode1 != null && provinceCode1 != null) {
                if (!code1.substring(0, 6).equals(countyCode1.substring(0,6)) || !code1.substring(0, 4).equals(cityCode1.substring(0, 4)) || !code1.substring(0, 2).equals(provinceCode1.substring(0, 2))) {
                    failureSpecification.put(failureSpecification.size(), "该摸底人员户籍省市县乡不匹配");
                    iterator.remove(); // 使用迭代器的 remove 方法移除当前元素
                    continue; // 跳过后续的操作，继续下一轮循环
                }
            }else {
                failureSpecification.put(failureSpecification.size(), "该摸底人员户籍省市县乡缺失");
                iterator.remove(); // 使用迭代器的 remove 方法移除当前元素
                continue; // 跳过后续的操作，继续下一轮循环
            }

            if (code != null && countyCode != null && cityCode != null && provinceCode != null){
                if (code.substring(0, 6).equals(countyCode.substring(0, 6)) && code.substring(0, 4).equals(cityCode.substring(0, 4)) && code.substring(0,2).equals(provinceCode.substring(0, 2))){
                    boolean isCodeUnique = true;
                    // 检查当前code是否已存在于 importVOList 中，若存在则将 isCodeUnique 标记为 false
                    for (ImportVO importVO : importVOList) {
                        if (importVO.getCode().substring(0, 9).equals(code.substring(0, 9))) {
                            isCodeUnique = false;
                            break; // 跳出循环
                        }
                    }
                    // 如果 code 是唯一的，则将对象添加到 importVOList 中
                    if (isCodeUnique) {
                        // 获取该乡最大的筛查编号列表
                        List<String> screenIdList = screenPersonMapper.getMaxScreenId(year, code);
                        // 构造最大的筛查编号
                        String maxScreenId = constructMaxScreenId(screenIdList, code.substring(0, 9), year, screenType);
                        // 创建 ImportVO 对象并设置属性
                        ImportVO importVO = new ImportVO();
                        importVO.setScreenId(maxScreenId);
                        importVO.setCode(code.substring(0, 9));
                        importVO.setName(obj.getTown());
                        importVO.setScreenPersonDO(BeanUtils.toBean(obj, ScreenPersonDO.class));
                        importVOList.add(importVO); // 将 importVO 添加到 importVOList 中
                    }
                }else {
                    failureSpecification.put(failureSpecification.size(), "该摸底人员所在省市县乡不匹配");
                    iterator.remove(); // 使用迭代器的 remove 方法移除当前元素
                }
            }else {
                failureSpecification.put(failureSpecification.size(), "该摸底人员所在省市县乡缺失");
                iterator.remove(); // 使用迭代器的 remove 方法移除当前元素
            }

            obj.setProvince(provinceCode);
            obj.setCity(cityCode);
            obj.setCounty(countyCode);
            obj.setTown(code);
            obj.setPermanentAddressProvince(provinceCode1);
            obj.setPermanentAddressCity(cityCode1);
            obj.setPermanentAddressCounty(countyCode1);
            obj.setPermanentAddressTown(code1);
        }

        // 处理非重复人员
        for (ScreenPersonImportVO obj : uniqueList) {
            String newScreenId = "";
            for (ImportVO importVO : importVOList) {
                if (obj.getTown().substring(0, 9).equals(importVO.getCode())) {
                    String s = importVO.getScreenId();
                    // 生成 筛查编号
                    newScreenId = makeScreenId(s, importVO.getCode());
                    importVO.setScreenId(newScreenId);
                    break;
                }
            }
            obj.setScreenId(newScreenId);
            obj.setYear(year);
            obj.setScreenTime(screenTime);
            processScreenPerson(screenType, year, obj, batchInsert, batchUpdate, createSpecification, failureSpecification);
        }


        Iterator<ScreenPersonImportVO> iterator2 = duplicateList.iterator();
        while (iterator2.hasNext()) {
            ScreenPersonImportVO obj = iterator2.next();
            // 根据乡镇名称查询对应的区域代码
            String code = screenDistrictMapper.selectByName(obj.getTown());
            // 根据省名称查询对应的区域代码
            String provinceCode = screenDistrictMapper.selectByName(obj.getProvince());
            // 根据市/州名称查询对应的区域代码
            String cityCode = screenDistrictMapper.selectByName(obj.getCity());
            // 根据区/县名称查询对应的区域代码
            String countyCode = screenDistrictMapper.selectByName(obj.getCounty());
            // 根据户籍乡镇名称查询对应的区域代码
            String code1 = screenDistrictMapper.selectByName(obj.getPermanentAddressTown());
            // 根据户籍省名称查询对应的区域代码
            String provinceCode1 = screenDistrictMapper.selectByName(obj.getPermanentAddressProvince());
            // 根据户籍市/州名称查询对应的区域代码
            String cityCode1 = screenDistrictMapper.selectByName(obj.getPermanentAddressCity());
            // 根据户籍区/县名称查询对应的区域代码
            String countyCode1 = screenDistrictMapper.selectByName(obj.getPermanentAddressCounty());

            if (code1 != null && countyCode1 != null && cityCode1 != null && provinceCode1 != null) {
                if (!code1.substring(0, 6).equals(countyCode1.substring(0, 6)) || !code1.substring(0, 4).equals(cityCode1.substring(0, 4)) || !code1.substring(0, 2).equals(provinceCode1.substring(0, 2))) {
                    failureSpecification.put(failureSpecification.size(), "该重复人员户籍省市县乡不匹配");
                    iterator2.remove(); // 使用迭代器的 remove 方法移除当前元素
                    continue; // 跳过后续的操作，继续下一轮循环
                }
            }else {
                failureSpecification.put(failureSpecification.size(), "该重复人员户籍省市县乡缺失");
                iterator2.remove(); // 使用迭代器的 remove 方法移除当前元素
                continue; // 跳过后续的操作，继续下一轮循环
            }

            if (code != null && countyCode != null && cityCode != null && provinceCode != null){
                if (!code.substring(0, 6).equals(countyCode.substring(0, 6)) || !code.substring(0, 4).equals(cityCode.substring(0, 4)) || !code.substring(0,2).equals(provinceCode.substring(0,2))) {
                    failureSpecification.put(failureSpecification.size(), "该重复人员所在省市县乡不匹配");
                    iterator2.remove(); // 使用迭代器的 remove 方法移除当前元素
                }
            }else {
                failureSpecification.put(failureSpecification.size(), "该重复人员所在省市县乡缺失");
                iterator2.remove(); // 使用迭代器的 remove 方法移除当前元素
            }
            obj.setProvince(provinceCode);
            obj.setCity(cityCode);
            obj.setCounty(countyCode);
            obj.setTown(code);
            obj.setPermanentAddressProvince(provinceCode1);
            obj.setPermanentAddressCity(cityCode1);
            obj.setPermanentAddressCounty(countyCode1);
            obj.setPermanentAddressTown(code1);
        }

        // 处理重复人员
        for (ScreenPersonImportVO obj : duplicateList) {
            obj.setYear(year);
            obj.setScreenTime(screenTime);
            processScreenPerson2(screenType, year, obj, batchInsert2, batchUpdate2, createRepeatSpecification, failureSpecification);
        }

        // 批量插入和更新
        if (!batchInsert.isEmpty()) {
            screenPersonMapper.insertBatch(batchInsert);
        }
        if (!batchInsert2.isEmpty()){
            screenRepeatPersonMapper.insertBatch(batchInsert2);
        }
        if (!batchUpdate.isEmpty()) {
            screenPersonMapper.updateBatch(batchUpdate);
        }
        if (!batchUpdate2.isEmpty()) {
            screenRepeatPersonMapper.updateBatch(batchUpdate2);
        }

        return screenPersonImportRespVO
                .setCreateSpecification(createSpecification)
                .setFailureSpecification(failureSpecification)
                .setRepeateSpecification(createRepeatSpecification);
    }


    /**
     * 找到该乡镇最大的筛查编号
     */
    public static String constructMaxScreenId(List<String> screenIdList, String code, int year, int screenType) {
        // 初始化最大个人编号为 0
        int maxPersonalId = 0;
        String maxScreenId = "";
        String frontStr = "";
        String lastStr = "";

        if (screenIdList.size() > 0) {
            // 遍历获取到的 screenIdList 列表
            for (String screenId : screenIdList) {
                // 获取个人编号部分
                String personalIdString = screenId.substring(9, 14);
                frontStr = screenId.substring(0, 9);
                lastStr = screenId.substring(14, 19);
                // 将个人编号部分转换为整数
                int personalId = Integer.parseInt(personalIdString);
                // 更新最大个人编号
                if (personalId > maxPersonalId) {
                    maxPersonalId = personalId;
                }
            }
            maxScreenId = frontStr + String.format("%05d", maxPersonalId) + lastStr;
        } else {
            maxScreenId = code + String.format("%05d", maxPersonalId) + String.valueOf(year) + String.valueOf(screenType);
        }

        return maxScreenId;
    }


    // 生成筛查编号
    public static String makeScreenId(String maxScreenId, String code){
        String personalIdString = maxScreenId.substring(9, 14); // 注意：substring的参数是从0开始的索引，所以第10位对应索引9
        String lastStr = maxScreenId.substring(14, 19);
        // 将个人编号部分转换为整数
        int personalId = Integer.parseInt(personalIdString);
        personalId += 1;

        return code + String.format("%05d", personalId) + lastStr;
    }



    private void processScreenPerson(Integer screenType, Integer year, ScreenPersonImportVO obj, List<ScreenPersonDO> batchInsert,
                                 List<ScreenPersonDO> batchUpdate, List<String> createSpecification,
                                 Map<Integer, String> failureSpecification) {

    List<DictDataRespDTO> dictList = dictDataApi.getDictDataList("tb_more_people_type");
    // 处理更多类型信息
    Map<String, Integer> groups = new HashMap<>();

    // 多人群分类
    for (DictDataRespDTO dict : dictList) {
        groups.put(dict.getLabel(), Integer.valueOf(dict.getValue()));
    }
    Integer firstType = obj.getFirstType();
    String moreTypeStr = obj.getMoreTypeStr();
    // 非重点人群
    if (firstType == 2) {
        if (moreTypeStr != null) {
            failureSpecification.put(failureSpecification.size(), "第一人群分类不可以选择多人群分类！");
            return;
        }
        obj.setMoreType(0);
        // 教职工
    } else if (firstType == 4) {
        // 第一个类型是教职工，只能选择密接者、糖尿病或既往患者
        List<String> allowedTypes = Arrays.asList("密接者", "糖尿病", "既往患者", "教职工", "HIV/AIDS");
        Integer moreType = 0;
        // 没有选
        if (moreTypeStr == null) {
            moreType = moreType + 4;
        }
        // 选了1个
        if (moreTypeStr != null && !moreTypeStr.contains(",")) {
            String s = moreTypeStr;
            if (allowedTypes.contains(s)) {
                moreType += groups.getOrDefault(s, 0);
            }
            if (!moreTypeStr.contains("教职工")) {
                moreType = moreType + 4;
            }
        }
        // 选了多个
        if (moreTypeStr != null && moreTypeStr.contains(",")) {
            String[] split = moreTypeStr.split(",");
            for (String s : split) {
                if (allowedTypes.contains(s)) {
                    moreType += groups.getOrDefault(s, 0);
                }
            }
            if (!moreTypeStr.contains("教职工")) {
                moreType = moreType + 4;
            }
        }
        obj.setMoreType(moreType);
        // 重点人群分类
    } else if (firstType == 1) {
        Integer moreType = 0;
        // 未选择多人群分类
        if (moreTypeStr == null) {
            failureSpecification.put(failureSpecification.size(), "未选择多人群分类！");
            return;
        }
        // 选了一个
        if (!moreTypeStr.contains(",")) {
            moreType += groups.getOrDefault(moreTypeStr, 0);
            obj.setMoreType(moreType);
        }
        // 选了多个
        if (moreTypeStr.contains(",")) {
            String[] split = moreTypeStr.split(",");
            for (String s : split) {
                moreType += groups.getOrDefault(s, 0);
            }
            obj.setMoreType(moreType);
        }
    } else {
        failureSpecification.put(failureSpecification.size(), "未选择第一人群分类！");
        return;
    }
    // 筛查类型
    obj.setScreenType(screenType);
    // 计算年龄
    String yearSubStr = obj.getIdNum().substring(6, 10);
    int birthYear = Integer.parseInt(yearSubStr);
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    obj.setAge(currentYear - birthYear);
    // 判断性别
    char genderChar = obj.getIdNum().charAt(obj.getIdNum().length() - 2);
    obj.setSex((genderChar - '0') % 2 == 0 ? 1 : 0);
    // 判断是否插入或更新
    Long id = screenPersonMapper.isNullByIdNumYear(obj.getIdNum(), year, screenType);
    if (id == null && obj.getIdNum() != null) {
        batchInsert.add(BeanUtils.toBean(obj, ScreenPersonDO.class));
        createSpecification.add("");
    } else {
        batchUpdate.add(BeanUtils.toBean(obj, ScreenPersonDO.class));
        failureSpecification.put(failureSpecification.size(), "该摸底人员已存在,数据已更新！");
    }
}


    private void processScreenPerson2(Integer screenType, Integer year, ScreenPersonImportVO obj, List<ScreenRepeatPersonDO> batchInsert2,
                                     List<ScreenRepeatPersonDO> batchUpdate2, List<String> createRepeatSpecification,
                                     Map<Integer, String> failureSpecification) {

        List<DictDataRespDTO> dictList = dictDataApi.getDictDataList("tb_more_people_type");
        // 处理更多类型信息
        Map<String, Integer> groups = new HashMap<>();
        // 多人群分类
        for (DictDataRespDTO dict : dictList) {
            groups.put(dict.getLabel(), Integer.valueOf(dict.getValue()));
        }
        Integer firstType = obj.getFirstType();
        String moreTypeStr = obj.getMoreTypeStr();
        // 非重点人群
        if (firstType == 2) {
            if (moreTypeStr != null) {
                failureSpecification.put(failureSpecification.size(), "重复人员第一人群分类不可以选择多人群分类！");
                return;
            }
            obj.setMoreType(0);
            // 教职工
        } else if (firstType == 4) {
            // 第一个类型是教职工，只能选择密接者、糖尿病或既往患者
            List<String> allowedTypes = Arrays.asList("密接者", "糖尿病", "既往患者", "教职工", "HIV/AIDS");
            Integer moreType = 0;
            // 没有选
            if (moreTypeStr == null) {
                moreType = moreType + 4;
            }
            // 选了1个
            if (moreTypeStr != null && !moreTypeStr.contains(",")) {
                String s = moreTypeStr;
                if (allowedTypes.contains(s)) {
                    moreType += groups.getOrDefault(s, 0);
                }
                if (!moreTypeStr.contains("教职工")) {
                    moreType = moreType + 4;
                }
            }
            // 选了多个
            if (moreTypeStr != null && moreTypeStr.contains(",")) {
                String[] split = moreTypeStr.split(",");
                for (String s : split) {
                    if (allowedTypes.contains(s)) {
                        moreType += groups.getOrDefault(s, 0);
                    }
                }
                if (!moreTypeStr.contains("教职工")) {
                    moreType = moreType + 4;
                }
            }
            obj.setMoreType(moreType);
            // 重点人群分类
        } else if (firstType == 1) {
            Integer moreType = 0;
            // 未选择多人群分类
            if (moreTypeStr == null) {
                failureSpecification.put(failureSpecification.size(), "重复人员未选择多人群分类！");
                return;
            }
            // 选了一个
            if (!moreTypeStr.contains(",")) {
                moreType += groups.getOrDefault(moreTypeStr, 0);
                obj.setMoreType(moreType);
            }
            // 选了多个
            if (moreTypeStr.contains(",")) {
                String[] split = moreTypeStr.split(",");
                for (String s : split) {
                    moreType += groups.getOrDefault(s, 0);
                }
                obj.setMoreType(moreType);
            }
        } else {
            failureSpecification.put(failureSpecification.size(), "重复人员未选择第一人群分类！");
            return;
        }
        // 筛查类型
        obj.setScreenType(screenType);
        // 计算年龄
        String yearSubStr = obj.getIdNum().substring(6, 10);
        int birthYear = Integer.parseInt(yearSubStr);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        obj.setAge(currentYear - birthYear);
        // 判断性别
        char genderChar = obj.getIdNum().charAt(obj.getIdNum().length() - 2);
        obj.setSex((genderChar - '0') % 2 == 0 ? 1 : 0);
        // 判断是否插入或更新
        Long id = screenRepeatPersonMapper.isNullByIdNumYear(obj.getIdNum(), year, screenType);
        if (id == null && obj.getIdNum() != null) {
            batchInsert2.add(BeanUtils.toBean(obj, ScreenRepeatPersonDO.class));
            createRepeatSpecification.add("");
        } else {
            batchUpdate2.add(BeanUtils.toBean(obj, ScreenRepeatPersonDO.class));
            failureSpecification.put(failureSpecification.size(), "该摸底人员已存在,数据已更新！");
        }
    }


    @Override
    public List<ScreenPersonImportVO> createSampleData() {
        return List.of(
                ScreenPersonImportVO.builder().name("张三").idNum("360888888888888888").nation(24)
                        .tel("18888888888").height(BigDecimal.valueOf(175.22)).weight(BigDecimal.valueOf(55.2))
                        .permanentAddress("重庆市重庆市辖区万州区高笋塘街道").permanentAddressProvince("重庆市")
                        .permanentAddressCity("重庆市辖区").permanentAddressCounty("万州区").permanentAddressTown("高笋塘街道")
                        .address("重庆市重庆市辖区万州区高笋塘街道").province("重庆市").city("重庆市辖区").county("万州区")
                        .town("高笋塘街道").firstType(0).remark("备注").build()
        );
    }


    @Override
    public void selectedData(Integer index, List<DictDataRespDTO> dictDataRespDTOS, Map<Integer, List<String>> selectedData) {
        if (dictDataRespDTOS != null) {
            List<String> dataList = dictDataRespDTOS.stream().map(DictDataRespDTO::getLabel).collect(Collectors.toList());
            if (!dataList.isEmpty()) {
                selectedData.put(index, dataList);
            }
        }
    }


    @Override
    public void addSelectedData(String dictType, int index, Map<Integer, List<String>> selectedData) {
        List<DictDataRespDTO> dictList = dictDataApi.getDictDataList(dictType);
        if (dictList.size() > 0) {
            selectedData(index, dictList, selectedData);
        }
    }

    @Override
    public String resolveMoreTypeToString(Integer moreType) {
        if (moreType == null || moreType == 0) {
            return "";
        }

        List<DictDataRespDTO> dictList = dictDataApi.getDictDataList("tb_more_people_type");

        Map<Integer, String> typeMap = new HashMap<>();

        for (DictDataRespDTO obj : dictList) {
            typeMap.put(Integer.valueOf(obj.getValue()), obj.getLabel());
        }

        String result = typeMap.entrySet().stream()
                .filter(entry -> (moreType & entry.getKey()) == entry.getKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.joining("，"));

        return result;
    }

    @Override
    public PatientInfoList getPatientInfoList(Long patientId, Integer year, Integer screenType) {
        // 使用并行流来提高性能
        List<CollectVO> checkList =
                screenPersonMapper.getCheckList(patientId, year, screenType).parallelStream().collect(Collectors.toList());

        List<ChestRadiographVO> ctdrList =
                screenPersonMapper.getCTDRList(patientId, year, screenType).parallelStream().collect(Collectors.toList());

        /*List<DiagnosisVO> diagnoList =
                screenPersonMapper.getDiagnoList(patientId, year, screenType).parallelStream().collect(Collectors.toList());

        List<ElectrocardiogramVO> electList =
                screenPersonMapper.getElectList(patientId, year, screenType).parallelStream().collect(Collectors.toList());

        List<SputumExaminationVO> sputumList =
                screenPersonMapper.getSputumList(patientId, year, screenType).parallelStream().collect(Collectors.toList());*/

        List<ScreenTstVO> ppdList =
                screenPersonMapper.getPPDList(patientId, year, screenType).parallelStream().collect(Collectors.toList());

        TBHealthScreening tbHealthScreening = screenDiagnosisService.getTbHealthScreening(patientId, year, screenType);

        // 使用构造函数初始化PatientInfoList对象
        return new PatientInfoList()
                .setCheckList(checkList)
                .setCTDRList(ctdrList)
                .setPPDList(ppdList)
                .setTbHealthScreening(tbHealthScreening);
    }


    @Override
    public ImageVO getImageUrl(Long personId, Integer type, String screenId, Integer screenOrder, Integer year, Integer screenType) {
        return screenPersonMapper.getImageUrl(personId, type, screenId, screenOrder, year, screenType);
    }


    @Override
    public String updateImage(Long imageId, InputStream file) {
        validateImageExists(imageId);
        // 存储文件
        String imageStr = fileApi.createFile(IoUtil.readBytes(file));
        // 更新路径
        screenPersonMapper.updateImage(imageId, imageStr);
        return imageStr;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String updateImag2(Long personId, String screenId, Integer imageType, Integer screenOrder, Integer year, Integer screenType, InputStream file) {

        // 存储文件
        String imageStr = fileApi.createFile(IoUtil.readBytes(file));

        Long id = screenImagesMapper.selectIsNull(personId, screenId, imageType, screenOrder, year, screenType);

        ScreenImagesDO screenImagesDO = new ScreenImagesDO();
        screenImagesDO.setPersonId(personId)
                .setScreenId(screenId)
                .setUrl(imageStr)
                .setScreenOrder(screenOrder)
                .setType(imageType);

        if (id == null) {
            screenImagesDO.setYear(year).setScreenType(screenType).setScreenTime(LocalDateTime.now());
            screenImagesMapper.insert(screenImagesDO);
        } else {
            screenImagesDO.setId(id);
            screenImagesMapper.updateById(screenImagesDO);
        }

        if (imageType == 2){
            ScreenComputedTomographyDO screenComputedTomographyDO = new ScreenComputedTomographyDO();
            screenComputedTomographyDO.setPersonId(personId).setScreenId(screenId)
                    .setYear(year).setScreenOrder(screenOrder).setScreenType(screenType).setComputedTomography(imageStr);
            Long ctId = screenComputedTomographyMapper.getIsExist(personId, screenId, screenOrder, screenType, year);
            if (ctId != null){
                screenComputedTomographyDO.setId(ctId);
                screenComputedTomographyMapper.updateById(screenComputedTomographyDO);
            }
        }

        return imageStr;
    }

    @Override
    public void resolveDistrict(ScreenPersonDO obj) {
        try {
            obj.setProvince(screenDistrictMapper.getNameByCode(obj.getProvince()));
            obj.setCity(screenDistrictMapper.getNameByCode(obj.getCity()));
            obj.setCounty(screenDistrictMapper.getNameByCode(obj.getCounty()));
            obj.setTown(screenDistrictMapper.getNameByCode(obj.getTown()));

            obj.setPermanentAddressProvince(screenDistrictMapper.getNameByCode(obj.getPermanentAddressProvince()));
            obj.setPermanentAddressCity(screenDistrictMapper.getNameByCode(obj.getPermanentAddressCity()));
            obj.setPermanentAddressCounty(screenDistrictMapper.getNameByCode(obj.getPermanentAddressCounty()));
            obj.setPermanentAddressTown(screenDistrictMapper.getNameByCode(obj.getPermanentAddressTown()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resolveDistrict2(ScreenRepeatPersonDO obj) {
        try {
            obj.setProvince(screenDistrictMapper.getNameByCode(obj.getProvince()));
            obj.setCity(screenDistrictMapper.getNameByCode(obj.getCity()));
            obj.setCounty(screenDistrictMapper.getNameByCode(obj.getCounty()));
            obj.setTown(screenDistrictMapper.getNameByCode(obj.getTown()));

            obj.setPermanentAddressProvince(screenDistrictMapper.getNameByCode(obj.getPermanentAddressProvince()));
            obj.setPermanentAddressCity(screenDistrictMapper.getNameByCode(obj.getPermanentAddressCity()));
            obj.setPermanentAddressCounty(screenDistrictMapper.getNameByCode(obj.getPermanentAddressCounty()));
            obj.setPermanentAddressTown(screenDistrictMapper.getNameByCode(obj.getPermanentAddressTown()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @VisibleForTesting
    void validateImageExists(Long imageId) {
        if (imageId == null) {
            return;
        }
        ImageVO imageVO = screenPersonMapper.selectImageById(imageId);
        if (imageVO == null) {
            throw exception(IMAGE_NOT_EXISTS);
        }

    }

}