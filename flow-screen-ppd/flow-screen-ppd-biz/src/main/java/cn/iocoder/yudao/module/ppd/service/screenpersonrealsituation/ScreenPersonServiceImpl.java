package cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.word.Word07Writer;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import cn.iocoder.yudao.module.ppd.controller.admin.screendiagnosis.vo.TBHealthScreening;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.*;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.nitoce.*;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenchestradiograph.ScreenChestRadiographDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect.ScreenCollectDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screencomputedtomography.ScreenComputedTomographyDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenimages.ScreenImagesDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenppd.ScreenPpdDO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenrepeatperson.ScreenRepeatPersonDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screencomputedtomography.ScreenComputedTomographyMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screendistrict.ScreenDistrictMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenimages.ScreenImagesMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpersonrealsituation.ScreenPersonMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpoint.ScreenPointMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenrepeatperson.ScreenRepeatPersonMapper;
import cn.iocoder.yudao.module.ppd.service.screendiagnosis.ScreenDiagnosisService;
import cn.iocoder.yudao.module.system.api.dict.DictDataApi;
import cn.iocoder.yudao.module.system.api.dict.dto.DictDataRespDTO;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import com.google.common.annotations.VisibleForTesting;
import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.dromara.hutool.core.bean.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.cd.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.cd.enums.MatchRules.ID_NUMBER;
import static cn.iocoder.yudao.module.cd.enums.MatchRules.TEL;
import static cn.iocoder.yudao.module.cd.enums.MatchRulesMsg.*;
import static cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation.NoticeMsg.*;


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
    @Resource
    private ScreenPointMapper screenPointMapper;
    @Resource
    private DeptService deptService;
    @Resource
    private Configuration configuration;

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

        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        screenPerson.setDeptId(deptService.getMyDept(loginUserId));

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
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        Long myDeptId = deptService.getMyDept(loginUserId);

        // 获取所有子部门
        List<DeptDO> childDeptList = deptService.getChildDeptList(myDeptId);
        // 以及当前部门
        childDeptList.add(deptService.getDept(myDeptId));

        // 提取部门ID
        List<Long> deptIds = childDeptList.stream()
                .map(DeptDO::getId)
                .collect(Collectors.toList());

        pageReqVO.setDeptList(deptIds);

        return screenPersonMapper.selectPage(pageReqVO);
    }



    @Override
    public PageResult<ScreenPersonDO> getScreenedPage(ScreenPersonPageReqVO pageReqVO) {

        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        Long myDeptId = deptService.getMyDept(loginUserId);

        // 获取所有子部门以及当前部门
        List<DeptDO> childDeptList = deptService.getChildDeptList(myDeptId);
        childDeptList.add(deptService.getDept(myDeptId));

        // 提取部门ID
        List<Long> deptIds = childDeptList.stream()
                .map(DeptDO::getId)
                .collect(Collectors.toList());

        pageReqVO.setDeptList(deptIds);


        return screenPersonMapper.selectScreenedPage(pageReqVO);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ScreenPersonImportRespVO importScreenPerson(List<ScreenPersonImportVO> list, Integer year,
                                                       Integer screenType, LocalDateTime screenStartTime,
                                                       LocalDateTime screenEndTime,
                                                       Long deptId) {
        if (list==null || list.isEmpty()){
            throw exception(EXCEL_EMPTY);
        }
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
                .filter(vo -> !vo.isEmpty(vo)).toList();
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
            if (idNum==null || idNum.isEmpty()){
                uniqueList.add(obj);
            }
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
            int order=0;
            StringBuffer  errorMsg=new StringBuffer();
            try {
                order = Integer.parseInt(obj.getOrder());
                if (order<0){
                    throw exception(SCREEN_PERSON_IMPORT_ORDER_NOT_NUMBER);
                }
            }catch (Exception e){
                throw exception(SCREEN_PERSON_IMPORT_ORDER_NOT_NUMBER);
            }
            if (obj.getName()==null || obj.getName().isEmpty()){
                errorMsg.append("姓名未填");
            }
            if (obj.getIdNum()==null || obj.getIdNum().isEmpty()){
                errorMsg.append(ID_NUMBER_EMPTY);
            }else {
                if (!obj.getIdNum().matches(ID_NUMBER)){
                    errorMsg.append(ID_NUMBER_MATCH_ERROR);
                }
            }
            if (obj.getScreenPoint()==null || obj.getScreenPoint().isEmpty()){
                errorMsg.append(SCREEN_POINT_EMPTY);
            }else {
                Long pointId = screenPointMapper.getIdByName(obj.getScreenPoint(), year, deptService.getDept(deptId).getName());
                if (pointId==null){
                    errorMsg.append(SCREEN_POINT_NOT_EXIST);
                }
            }
            if (obj.getTel()==null || obj.getTel().isEmpty()){
                errorMsg.append(TEL_EMPTY);
            }else {
                if (!obj.getTel().matches(TEL) ){
                    errorMsg.append(TEL_MATCH_ERROR);
                }
            }
            if (obj.getGuardianTel()!=null && !obj.getGuardianTel().matches(TEL)){
                errorMsg.append("监护人手机号格式错误;");
            }
            if (code1 != null && countyCode1 != null && cityCode1 != null && provinceCode1 != null) {
                if (!code1.substring(0, 6).equals(countyCode1.substring(0,6)) || !code1.substring(0, 4).equals(cityCode1.substring(0, 4)) || !code1.substring(0, 2).equals(provinceCode1.substring(0, 2))) {
                    errorMsg.append("该人员户籍省市县乡不匹配;");
                }
            }else {
                errorMsg.append("该人员户籍省市县乡缺失;");
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
                    errorMsg.append("该人员现住址的省市县乡不匹配;");
                }
            }else {
                errorMsg.append("该人员现住址的省市县乡缺失;");
            }
            if (!errorMsg.isEmpty()){
                failureSpecification.put(order,errorMsg.toString());
                iterator.remove();
                continue;
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
            obj.setScreenStartTime(screenStartTime);
            obj.setScreenEndTime(screenEndTime);
            obj.setDeptId(deptId);
            processScreenPerson(screenType, year, obj, batchInsert, batchUpdate, createSpecification, failureSpecification);
        }


        Iterator<ScreenPersonImportVO> iterator2 = duplicateList.iterator();
        while (iterator2.hasNext()) {
            ScreenPersonImportVO obj = iterator2.next();
            int order=0;
            StringBuffer  errorMsg=new StringBuffer();
            try {
                order = Integer.parseInt(obj.getOrder());
                if (order<0){
                    throw exception(SCREEN_PERSON_IMPORT_ORDER_NOT_NUMBER);
                }
            }catch (Exception e){
                throw exception(SCREEN_PERSON_IMPORT_ORDER_NOT_NUMBER);
            }
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

            if (obj.getName()==null || obj.getName().isEmpty()){
                errorMsg.append("姓名未填");
            }
            if (obj.getIdNum()==null || obj.getIdNum().isEmpty()){
                errorMsg.append(ID_NUMBER_EMPTY);
            }else {
                if (!obj.getIdNum().matches(ID_NUMBER)){
                    errorMsg.append(ID_NUMBER_MATCH_ERROR);
                }
            }
            if (obj.getScreenPoint()==null || obj.getScreenPoint().isEmpty()){
                errorMsg.append(SCREEN_POINT_EMPTY);
            }else {
                Long pointId = screenPointMapper.getIdByName(obj.getScreenPoint(), year, deptService.getDept(deptId).getName());
                if (pointId==null){
                    errorMsg.append(SCREEN_POINT_NOT_EXIST);
                }
            }
            if (obj.getTel()==null || obj.getTel().isEmpty()){
                errorMsg.append(TEL_EMPTY);
            }else {
                if (!obj.getTel().matches(TEL) ){
                    errorMsg.append(TEL_MATCH_ERROR);
                }
            }
            if (obj.getGuardianTel()!=null && !obj.getGuardianTel().matches(TEL)){
                errorMsg.append("监护人手机号格式错误;");
            }
            if (code1 != null && countyCode1 != null && cityCode1 != null && provinceCode1 != null) {
                if (!code1.substring(0, 6).equals(countyCode1.substring(0, 6)) || !code1.substring(0, 4).equals(cityCode1.substring(0, 4)) || !code1.substring(0, 2).equals(provinceCode1.substring(0, 2))) {
                    errorMsg.append("该重复人员户籍省市县乡不匹配;");
                }
            }else {
                errorMsg.append("该重复人员户籍省市县乡缺失;");
            }

            if (code != null && countyCode != null && cityCode != null && provinceCode != null){
                if (!code.substring(0, 6).equals(countyCode.substring(0, 6)) || !code.substring(0, 4).equals(cityCode.substring(0, 4)) || !code.substring(0,2).equals(provinceCode.substring(0,2))) {
                    errorMsg.append("该重复人员现住址的省市县乡不匹配;");
                }
            }else {
                errorMsg.append("该重复人员现住址的省市县乡缺失;");
            }
            if (!errorMsg.isEmpty()){
                failureSpecification.put(order,errorMsg.toString());
                iterator2.remove();
                continue;
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
            obj.setScreenStartTime(screenStartTime);
            obj.setScreenEndTime(screenEndTime);
            obj.setDeptId(deptId);
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
    Long id = screenPersonMapper.isNullByIdNumYearDeptId(obj.getIdNum(), year, screenType,obj.getDeptId());
    if (id == null && obj.getIdNum() != null) {
        batchInsert.add(BeanUtils.toBean(obj, ScreenPersonDO.class));
        createSpecification.add("");
    } else {
        batchUpdate.add(BeanUtils.toBean(obj, ScreenPersonDO.class));
        failureSpecification.put(failureSpecification.size(), "该人员已存在,数据已更新！");
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
            failureSpecification.put(failureSpecification.size(), "该人员已存在,数据已更新！");
        }
    }


    @Override
    public List<ScreenPersonImportVO> createSampleData() {
        return List.of(
                ScreenPersonImportVO.builder().order("1").name("张三").idNum("360888888888888888").nation(24).studentType(1)
                        .tel("18888888888").height(BigDecimal.valueOf(175.22)).weight(BigDecimal.valueOf(55.2))
                        .permanentAddress("重庆市重庆市辖区万州区高笋塘街道").permanentAddressProvince("重庆市")
                        .permanentAddressCity("重庆市辖区").permanentAddressCounty("万州区").permanentAddressTown("高笋塘街道")
                        .address("重庆市重庆市辖区万州区高笋塘街道").province("重庆市").city("重庆市辖区").county("万州区")
                        .town("高笋塘街道").firstType(0).remark("备注").guardianTel("18888888888").build()
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

        List<ChestRadiographVO> drList =
                screenPersonMapper.getDRList(patientId, year, screenType).parallelStream().collect(Collectors.toList());
        List<ChestRadiographVO> ctList =
                        screenPersonMapper.getCTList(patientId, year, screenType).parallelStream().collect(Collectors.toList());

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
                .setDRList(drList)
                .setCTList(ctList)
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
    public String updateImag2(Long personId, String screenId,Integer imageType, Integer screenOrder, Integer year, Integer screenType, InputStream file) {

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
    @Transactional(rollbackFor = Exception.class)
    public String updateCtImage(Long personId, String screenId,String idNum,Integer imageType, Integer screenOrder, Integer year, Integer screenType, InputStream file) {

        // 存储文件
        String imageStr = fileApi.createFile(IoUtil.readBytes(file));

        Long id = screenImagesMapper.selectCtIsNull(idNum, imageType, screenOrder, year, screenType);

        ScreenImagesDO screenImagesDO = new ScreenImagesDO();
        screenImagesDO.setPersonId(personId)
                .setScreenId(screenId)
                .setIdNum(idNum)
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
            screenComputedTomographyDO.setPersonId(personId).setScreenId(screenId).setIdNum(idNum)
                    .setYear(year).setScreenOrder(screenOrder).setScreenType(screenType).setComputedTomography(imageStr);
            Long ctId = screenComputedTomographyMapper.getCtIsExist( idNum, screenOrder, screenType, year);
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

    @Override
    public void exportStatistics(ScreenPersonStatisticsReqVO reqVO, HttpServletResponse response) {
        try  {
            // 通过工具类创建writer，默认创建xls格式
            ExcelWriter writer = ExcelUtil.getWriter();

            String hospital = ObjectUtil.isNull(reqVO.getHospital()) ? "" : reqVO.getHospital();
            String schoolName = ObjectUtil.isNull(reqVO.getSchool()) ? "" : reqVO.getSchool();
            String district = ObjectUtil.isNull(reqVO.getDistrict()) ? "" : reqVO.getDistrict();
            String concat = ObjectUtil.isNull(reqVO.getContact()) ? "" : reqVO.getContact();
            String concatPhone = ObjectUtil.isNull(reqVO.getContactPhone()) ? "" : reqVO.getContactPhone();
            String injectionPeople = ObjectUtil.isNull(reqVO.getInjectionPeople()) ? "" : reqVO.getInjectionPeople();
            String checkPeople = ObjectUtil.isNull(reqVO.getCheckPeople()) ? "" : reqVO.getCheckPeople();
            writer.merge(19, reqVO.getTableTittle());
            writer.merge(19, "学校：" + schoolName + "        " + "医院：" + hospital + "        " + "（区县："
                    + district + "）" + "        "  +   "联系人：" + concat  + "        " + "联系电话：" + concatPhone + "        " +  "注射人："
                    + injectionPeople + "        " + "查验人：" + checkPeople  );
            writer.merge(2, 2, 0, 5, "基本信息", true);
            writer.merge(2, 2, 6, 13, "EC检查", true);
            writer.merge(2, 2, 14, 16, "X光胸片", true);
            writer.writeCellValue(17, 2, "结果", true);
            writer.merge(2, 2, 18, 19, "备注", true);
            writer.writeCellValue(19, 2, "其他备注", true);

            writer.setColumnWidth(1, 20);
            writer.setColumnWidth(2, 15);
            writer.setColumnWidth(5, 25);
            writer.setColumnWidth(6, 15);
            writer.setColumnWidth(7, 15);
            writer.setColumnWidth(10, 30);
            writer.setColumnWidth(11, 15);
            writer.setColumnWidth(12, 25);
            writer.setColumnWidth(13, 20);
            writer.setColumnWidth(14, 20);
            writer.setColumnWidth(15, 15);
            writer.setColumnWidth(18, 55);
            //跳过当前行，非必须，在此演示用
            writer.passCurrentRow();

            // 人员
            List<PatientInfoReqVO> personInfo = reqVO.getPersonInfo();
            // 勾选数据
            List<Integer> infoList = reqVO.getInfoList();

            List<StatisticExportVO2> list = new ArrayList<>();

            // 序号
            Integer index = 1;

            for (PatientInfoReqVO obj : personInfo) {
                // 查找单个人员信息
                StatisticExportVO statisticExportVO = screenPersonMapper.getByPatientInfo(obj);
                // 处理勾选数据
                resolveExportData(statisticExportVO,infoList);

                StatisticExportVO2 statisticExportVO2 = new StatisticExportVO2();
                // 将statisticExportVO的数据复制给statisticExportVO2
                BeanUtil.copyProperties(statisticExportVO, statisticExportVO2, false);
                // 使用辅助方法设置属性
                setIsDoPpd(statisticExportVO, statisticExportVO2);
                setBleb(statisticExportVO, statisticExportVO2);
                setDiameterFlag(statisticExportVO, statisticExportVO2);
                setOutcomePpd(statisticExportVO, statisticExportVO2);
                setIsDoX(statisticExportVO, statisticExportVO2);
                setOutcomeDr(statisticExportVO, statisticExportVO2);

                statisticExportVO2.setId(index);
                list.add(statisticExportVO2);
                index++;
            }


            List<StatisticExportVO2> rows = CollUtil.newArrayList(list);
            // 一次性写出内容，使用默认样式，强制输出标题
            writer.write(rows, true);
            if (ObjectUtil.isNotNull(infoList) && infoList.size() < 20){
                Workbook workbook = writer.getWorkbook();
                Sheet sheetAt = workbook.getSheetAt(0);
                // 处理勾选数据，没勾则隐藏
                for (int i = 0; i < 20; i++) {
                    if (!infoList.contains(i)) {
                        sheetAt.setColumnHidden(i, true);
                    }
                }
            }

            //response为HttpServletResponse对象
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            response.setHeader("Content-Disposition","attachment;filename=test.xls");

            ServletOutputStream out=response.getOutputStream();

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
    public void exportScreenPersonArchive(ScreenPersonStatisticsReqVO reqVO, HttpServletRequest request, HttpServletResponse response) {
        List<String> files = new ArrayList<>();
        List<PatientInfoReqVO> personInfo = reqVO.getPersonInfo();
        int i = 1;
        try {
            for (PatientInfoReqVO obj : personInfo) {
                // 每次循环创建一个新的 Word07Writer 实例
                Word07Writer writer = new Word07Writer();
                String name = obj.getName();
                String idCardNumber = obj.getIdNum();
                String school = obj.getSchool();
                String classroom = obj.getClassroom();
                // 添加段落（标题）
                writer.addText(ParagraphAlignment.CENTER, new Font("黑体", Font.PLAIN, 16), "结核病筛查PPD皮肤试验知情告知书");
                writer.addText(new Font("宋体", Font.PLAIN, 16), "");
                // 添加段落（正文）
                writer.addText(new Font("宋体", Font.PLAIN, 10), "【疾病简介】结核病是由结核杆菌感染所致的慢性传染病，主要由开放性肺结核病人咳嗽、打 ");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "喷嚏及大声说话时通过空气传播所致。缺乏对结核杆菌特异性免疫力的人群一旦感染，结核杆 ");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "菌可经血循环播散至全身。人体各组织器官均可感染、发生结核病变。如果得了肺结核不能及 ");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "时、彻底治疗会对自己的健康造成严重的伤害，而且还会传染他人。");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "【皮试禁忌】患急性疾病(如麻疹、湿疹、百日咳、流行性感冒、肺炎)、急性眼结膜炎、急性 ");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "中耳炎、广泛皮肤病者及过敏体质者(对奶粉过敏)暂不使用。一个月内接种过疫苗的暂不使");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "用。");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "【注意事项】");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "1、PPD注射后请原地休息，观察30分钟后，如无不适方可离开。观察期如有不适须立即告知   ");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "当班医生或护士。");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "2.保持PPD注射部位清洁于燥，禁揉搓、抓挠、涂擦药物，腕部禁止佩戴手表及饰品。");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "3.受试者于PPD注射后72小时查验反应结果。");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "【受试者姓名】______" + name + "_____   身份证号码________" + idCardNumber + "________");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "【学校班级】_________________" + school + "____" + classroom + "__________________");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "请仔细阅读并理解以上内容，受试者健康状况良好，无皮试禁忌症，愿意接受PPD皮肤试验。");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "如拒绝接受PPD皮肤试验，请说明原因_______________________________________");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(ParagraphAlignment.RIGHT, new Font("宋体", Font.PLAIN, 10), "家长确认签名：____________________");
                writer.addText(new Font("宋体", Font.PLAIN, 10), "");
                writer.addText(ParagraphAlignment.RIGHT, new Font("宋体", Font.PLAIN, 10), "年   月   日");

                // 写出到文件，每次循环生成唯一的文件名
                String realPath = request.getSession().getServletContext().getRealPath("/");
                String parentPath = new File(realPath).getParent() + "/table";
                File dir = new File(parentPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String fileName = parentPath + "/" + name + "_" + i  + ".docx";
                files.add(fileName);
                writer.flush(new File(fileName));
                // 关闭
                writer.close();
                i++;
            }
        } catch (IORuntimeException e) {
            e.printStackTrace();
        }
        writeToZip(response, files);
    }

    @Override
    public void exportScreenPersonArchive2(ScreenPersonStatisticsReqVO reqVO, HttpServletRequest request, HttpServletResponse response) {
        List<PatientInfoReqVO> personInfoList = reqVO.getPersonInfo();
        List<String> filePathList = new ArrayList<>();

        String realPath = request.getSession().getServletContext().getRealPath("/");
        String parentPath = new File(realPath).getParent() + "/table";
        File dir = new File(parentPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        int index = 1;

        for (PatientInfoReqVO obj : personInfoList) {
            TBHealthScreening tbHealthScreening = screenDiagnosisService.getTbHealthScreening(obj.getId(), obj.getYear(), obj.getScreenType());
            String fileName = parentPath + "/" + index + "_" + obj.getName() + ".pdf";
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                Template template = configuration.getTemplate("healthForm.ftl");
                StringWriter result = new StringWriter(1024);
                template.process(tbHealthScreening, result);
                String content = result.toString();
                ITextRenderer renderer = new ITextRenderer();
                ITextFontResolver fontResolver = renderer.getFontResolver();
                fontResolver.addFont("/fontcss/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                renderer.setDocumentFromString(content);
                renderer.layout();
                renderer.createPDF(outputStream);
                renderer.finishPDF();

                try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                    outputStream.writeTo(fileOutputStream);
                }
                filePathList.add(fileName);
                index++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            writeToZip(response, filePathList);
        } finally {
            for (String filePath : filePathList) {
                new File(filePath).delete();
            }
        }
    }



    public void writeToZip(HttpServletResponse response, List<String> files) {
        String fileName = "知情同意书.zip";
        try (
                OutputStream os = response.getOutputStream();
                ZipOutputStream zos = new ZipOutputStream(os)
        ) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
            // 遍历文件列表
            for (String filePath : files) {
                File file = new File(filePath);
                if (!file.exists() || !file.isFile()) {
                    // 处理文件不存在或路径无效的情况
                    continue;
                }
                try (FileInputStream fis = new FileInputStream(file)) {
                    // 创建一个新的 ZIP 条目
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);
                    // 将文件内容写入到 ZIP 文件中
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    // 关闭当前条目
                    zos.closeEntry();
                } catch (IOException e) {
                    // 处理文件读取或写入错误
                    e.printStackTrace();
                }
            }
            // 不需要显式关闭 zos 和 os，因为 try-with-resources 会自动关闭它们
        } catch (IOException e) {
            // 处理响应流相关的错误
            e.printStackTrace();
        }
    }


    // 处理勾选数据
    private void resolveExportData(StatisticExportVO statisticExportVO, List<Integer> infoList) {
        // 创建一个 Set 来提高 contains 操作的效率
        Set<Integer> infoSet = new HashSet<>(infoList);

        for (int i = 0; i <= infoSet.size(); i++) {
            if (!infoSet.contains(i)) {
                switch (i) {
                    case 0 -> statisticExportVO.setId(null);
                    case 2 -> statisticExportVO.setClassroom(null);
                    case 3 -> statisticExportVO.setName(null);
                    case 4 -> statisticExportVO.setAge(null);
                    case 5 -> statisticExportVO.setIdNum(null);
                    case 6 -> statisticExportVO.setPpdScreenTime(null);
                    case 7 -> statisticExportVO.setIsDoPpd(null);
                    case 8 -> statisticExportVO.setTransverseDiameter(null);
                    case 9 -> statisticExportVO.setLongitudinalDiameter(null);
                    case 10 -> statisticExportVO.setBleb(null);
                    case 13 -> statisticExportVO.setOutcomePpd(null);
                    case 12 -> statisticExportVO.setDiameterFlag(null);
                    case 14 -> statisticExportVO.setPhotoTime(null);
                    case 15 -> statisticExportVO.setIsDoX(null);
                    case 16 -> statisticExportVO.setOutcomeDr(null);
                    case 17 -> statisticExportVO.setIsDia(null);
                    case 18 -> statisticExportVO.setRemark(null);
                    case 19 -> statisticExportVO.setOtherRemark(null);
                    case 1 -> statisticExportVO.setSchool(null);
                    case 11 -> statisticExportVO.setAverageDiameter(null);
                    default -> {
                    }
                }
            }
        }
    }

    // 导出表格--处理是否已做ppd
    private void setIsDoPpd(StatisticExportVO source, StatisticExportVO2 target) {
        if (ObjectUtil.isNotNull(source.getIsDoPpd())) {
            target.setIsDoPpd(source.getIsDoPpd() == 1 ? " √ " : " × ");
        }
    }
    // 导出表格--处理是否双圈、水泡等
    private void setBleb(StatisticExportVO source, StatisticExportVO2 target) {
        if (ObjectUtil.isNotNull(source.getBleb()) && !StrUtil.isEmptyIfStr(source.getBleb())) {
            target.setBleb(" √ ");
        }
    }

    // 导出表格--处理是否平均长度小于15mm
    private void setDiameterFlag(StatisticExportVO source, StatisticExportVO2 target) {
        target.setDiameterFlag(source.getDiameterFlag() == 1 ? " √ " : " × ");
    }

    // 导出表格--处理ppd判读结果
    private void setOutcomePpd(StatisticExportVO source, StatisticExportVO2 target) {
        if (ObjectUtil.isNotNull(source.getOutcomePpd())) {
            switch (source.getOutcomePpd()) {
                case 1 -> target.setOutcomePpd("阳性");
                case 2 -> target.setOutcomePpd("阴性");
                default -> target.setOutcomePpd("无");
            }
        }
    }

    // 导出表格--处理是否做x胸片
    private void setIsDoX(StatisticExportVO source, StatisticExportVO2 target) {
        if (ObjectUtil.isNotNull(source.getIsDoX())) {
            target.setIsDoX(source.getIsDoX() == 1 ? " √ " : " × ");
        }
    }

    // 导出表格--处理DR结果
    private void setOutcomeDr(StatisticExportVO source, StatisticExportVO2 target) {
        if (ObjectUtil.isNotNull(source.getOutcomeDr())) {
            switch (source.getOutcomeDr()) {
                case 1 -> target.setOutcomeDr("疑似结合");
                case 2 -> target.setOutcomeDr("其他异常");
                case 0 -> target.setOutcomeDr("无异常");
                default -> target.setOutcomeDr("无结果");
            }
        }
    }

    @Override
    public List<StudentInfo> getStudentByGuardianTel(String tel, Integer screenType) {
        if (screenType==null){
            screenType=2;
        }
        List<StudentInfo> studentInfoReqVOS = screenPersonMapper.selectStudentByGuardianTel(tel, screenType);
        List<StudentInfo> list= studentInfoReqVOS.stream().collect(Collectors.groupingBy(StudentInfo::getIdcard)).values()
                .stream().flatMap(e-> Arrays.asList(e.stream().sorted(Comparator.comparing(StudentInfo::getYear).reversed()).findFirst().get())
                        .stream()).collect(Collectors.toList());
        return list;
    }

    @Override
    public ScreenCollectDO getPpdNotice(String idNum, Date curDate, Integer screenType, Integer screenOrder) {
        if (curDate==null){
            // 获取当前时间
            curDate = new Date();
        }
        if (screenType==null){
            screenType=2;
        }
        ScreenCollectDO screenCollectDO = screenPersonMapper.selectPpdNotice(idNum, curDate, screenType, screenOrder);
        return screenCollectDO;
    }

    @Override
    public ScreenPpdDO getPpdDetailNotice(String idNum, Date curDate,Integer screenType,Integer screenOrder) {
        if (curDate==null){
            // 获取当前时间
            curDate = new Date();
        }
        if (screenType==null){
            screenType=2;
        }
        ScreenPpdDO screenPpdDO = screenPersonMapper.selectPpdDetailNotice(idNum,curDate,screenType,screenOrder);
        return screenPpdDO;
    }

    @Override
    public ScreenComputedTomographyDO getCtDetailNotice(String idNum, Date curDate, Integer screenType, Integer screenOrder) {
        if (curDate==null){
            // 获取当前时间
            curDate = new Date();
        }
        if (screenType==null){
            screenType=2;
        }
        ScreenComputedTomographyDO computedTomographyDO = screenPersonMapper.selectCtDetailNotice(idNum, curDate, screenType, screenOrder);
        return computedTomographyDO;
    }

    @Override
    public ScreenChestRadiographDO getDrDetailNotice(String idNum, Date curDate, Integer screenType, Integer screenOrder) {
        if (curDate==null){
            // 获取当前时间
            curDate = new Date();
        }
        if (screenType==null){
            screenType=2;
        }
        ScreenChestRadiographDO chestRadiographDO = screenPersonMapper.selectDrDetailNotice(idNum, curDate, screenType, screenOrder);
        return chestRadiographDO;
    }

    @Override
    public NoticeRespVO getStudentNoticeByIdNum(String idNum, Date curDate, Integer screenType) {
        NoticeRespVO noticeRespVO = new NoticeRespVO();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // ppd筛查通知
        ScreenCollectDO ppdNotice = getPpdNotice(idNum, curDate, screenType, null);
        if (!BeanUtil.isEmpty(ppdNotice)){
            ScreenPpdDO ppdNoticeDO = getPpdDetailNotice(idNum, curDate,screenType,ppdNotice.getScreenOrder());
            if (!BeanUtil.isEmpty(ppdNotice) && (BeanUtil.isEmpty(ppdNoticeDO) || ppdNoticeDO.getScreenOrder() <ppdNotice.getScreenOrder())){
                NoticeBase ppdBase = new NoticeBase();
                ppdBase.setDate(ppdNotice.getCreateTime().format(fmt));
                ppdBase.setNoticeMsg(PPD_NOTICE.getValue());
                noticeRespVO.setPpdNotice(ppdBase);
            }
        }

        // ppd更新通知
        ScreenPpdDO ppdDO = getPpdDetailNotice(idNum, curDate,screenType,null);
        if (!BeanUtil.isEmpty(ppdDO)){
            PpdUpdateNotice ppdUpdateNotice = new PpdUpdateNotice();
            ppdUpdateNotice.setDate(ppdDO.getCreateTime().format(fmt));
            ppdUpdateNotice.setPpdDetail(BeanUtils.toBean(ppdDO, PpdDetailRespVO.class));
            ppdUpdateNotice.setNoticeMsg(PPD_UPDATE_NOTICE.getValue());
            noticeRespVO.setPpdUpdateNotice(ppdUpdateNotice);
            // dr筛查通知
            ScreenChestRadiographDO chestRadiographDO = getDrDetailNotice(idNum, curDate, screenType, ppdDO.getScreenOrder());
            if (BeanUtil.isEmpty(chestRadiographDO) || chestRadiographDO.getScreenOrder()<ppdDO.getScreenOrder()){
                NoticeBase drNoticeBase = new NoticeBase();
                drNoticeBase.setDate(ppdDO.getCreateTime().format(fmt));
                drNoticeBase.setNoticeMsg(DR_NOTICE.getValue());
                noticeRespVO.setDrNotice(drNoticeBase);
            }
        }

//        ct结果更新通知
        ScreenComputedTomographyDO tomographyDO = getCtDetailNotice(idNum, curDate, screenType, null);
        if (!BeanUtil.isEmpty(tomographyDO)){
            CtDetailRespVO ctDetailRespVO = BeanUtils.toBean(tomographyDO, CtDetailRespVO.class);
            CtUpdateNotice ctUpdateNotice = new CtUpdateNotice();
            ctUpdateNotice.setNoticeMsg(CT_UPDATE_NOTICE.getValue());
            ctUpdateNotice.setDate(tomographyDO.getCreateTime().format(fmt));
            ctUpdateNotice.setCtDetailRespVO(ctDetailRespVO);
            noticeRespVO.setCtUpdateNotice(ctUpdateNotice);
        }
        //        dr结果更新通知
        ScreenChestRadiographDO chestRadiographDO = getDrDetailNotice(idNum, curDate, screenType, null);
        if (!BeanUtil.isEmpty(chestRadiographDO)){
            CtDetailRespVO ctDetailRespVO = BeanUtils.toBean(chestRadiographDO, CtDetailRespVO.class);
            ctDetailRespVO.setComputedTomographyCode(chestRadiographDO.getChestRadiographCode());
            CtUpdateNotice ctUpdateNotice = new CtUpdateNotice();
            ctUpdateNotice.setNoticeMsg(DR_UPDATE_NOTICE.getValue());
            ctUpdateNotice.setDate(chestRadiographDO.getCreateTime().format(fmt));
            ctUpdateNotice.setCtDetailRespVO(ctDetailRespVO);
            noticeRespVO.setDrUpdateNotice(ctUpdateNotice);
        }


        return noticeRespVO;
    }
}