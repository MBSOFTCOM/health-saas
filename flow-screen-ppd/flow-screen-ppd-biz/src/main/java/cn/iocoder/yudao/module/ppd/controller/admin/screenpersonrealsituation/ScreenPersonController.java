package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation;


import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.*;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.examinationForm.ExaminationFormRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo.nitoce.NoticeRespVO;
import cn.iocoder.yudao.module.ppd.controller.admin.screenstaticshistory.vo.ScreenStaticsHistorySaveReqVO;
import cn.iocoder.yudao.module.ppd.dal.dataobject.screenpersonrealsituation.ScreenPersonDO;
import cn.iocoder.yudao.module.ppd.dal.mysql.screendistrict.ScreenDistrictMapper;
import cn.iocoder.yudao.module.ppd.dal.mysql.screenpoint.ScreenPointMapper;
import cn.iocoder.yudao.module.ppd.service.screenpersonrealsituation.ScreenPersonService;
import cn.iocoder.yudao.module.ppd.service.screenstaticshistory.ScreenStaticsHistoryService;
import cn.iocoder.yudao.module.ppd.utils.CustomSheetWriteHandler;
import cn.iocoder.yudao.module.system.dal.dataobject.dept.DeptDO;
import cn.iocoder.yudao.module.system.service.dept.DeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.successMsg;
import static cn.iocoder.yudao.module.infra.enums.ErrorCodeConstants.FILE_IS_EMPTY;


@Tag(name = "管理后台 - 摸底")
@RestController
@RequestMapping("/tb/screen-person")
@Validated
@Slf4j
public class ScreenPersonController {

    @Resource
    private ScreenPersonService screenPersonService;

    @Resource
    private ScreenDistrictMapper districtMapper;

    @Resource
    private ScreenPointMapper screenPointMapper;

    @Resource
    private DeptService deptService;
    @Resource
    private ScreenStaticsHistoryService screenStaticsHistoryService;


    @PostMapping("/create")
    @Operation(summary = "创建摸底")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:create')")
    public CommonResult<Long> createScreenPerson(@Valid @RequestBody ScreenPersonSaveReqVO createReqVO) {
        return success(screenPersonService.createScreenPerson(createReqVO));
    }


    @PutMapping("/update")
    @Operation(summary = "更新摸底")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:update')")
    public CommonResult<Boolean> updateScreenPerson(@Valid @RequestBody ScreenPersonSaveReqVO updateReqVO) {
        screenPersonService.updateScreenPerson(updateReqVO);
        return success(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除摸底")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tb:screen-person:delete')")
    public CommonResult<Boolean> deleteScreenPerson(@RequestParam("id") Long id) {
        screenPersonService.deleteScreenPerson(id);
        return success(true);
    }


    @GetMapping("/get")
    @Operation(summary = "获得摸底")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:query')")
    public CommonResult<ScreenPersonRespVO> getScreenPerson(@RequestParam("id") Long id) {
        ScreenPersonDO screenPerson = screenPersonService.getScreenPerson(id);
        return success(BeanUtils.toBean(screenPerson, ScreenPersonRespVO.class));
    }


    @GetMapping("/page")
    @Operation(summary = "获得摸底分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:query')")
    public CommonResult<PageResult<ScreenPersonRespVO>> getScreenPersonPage(@Valid ScreenPersonPageReqVO pageReqVO) {
        PageResult<ScreenPersonDO> pageResult = screenPersonService.getScreenPersonPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenPersonRespVO.class));
    }


    @GetMapping("/screened-page")
    @Operation(summary = "获得待筛查人员分页")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:query')")
    public CommonResult<PageResult<ScreenPersonRespVO>> getScreenedPage(@Valid ScreenPersonPageReqVO pageReqVO) {
        PageResult<ScreenPersonDO> pageResult = screenPersonService.getScreenedPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScreenPersonRespVO.class));
    }


    @GetMapping("/get-import-template")
    @Operation(summary = "下载摸底模板")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenPersonExcel(HttpServletResponse response) throws IOException {
        // 模板数据
        List<ScreenPersonImportVO> list = screenPersonService.createSampleData();
        // 存放下拉选列表数据
        Map<Integer, List<String>> selectedData = new HashMap<>();
        screenPersonService.addSelectedData("is_new", 3, selectedData);
        screenPersonService.addSelectedData("is_new", 4, selectedData);
        screenPersonService.addSelectedData("tb_first_people_type", 5, selectedData);
        screenPersonService.addSelectedData("tb_more_people_type", 6, selectedData);
        screenPersonService.addSelectedData("student_type", 7,  selectedData);
        screenPersonService.addSelectedData("tb_ethnic", 10, selectedData);

        // 获取地区名称数据
        List<String> provinceName = districtMapper.getProvinceName();
        List<String> cityName = districtMapper.getCityName();
        List<String> countyName = districtMapper.getCountyName();
        List<String> townName = districtMapper.getTownName();
        // 将地区名称数据放入选项数据中
        if (provinceName.size()>0){
            selectedData.put(16,provinceName);
            selectedData.put(21,provinceName);
        }
        if (cityName.size()>0){
            selectedData.put(17,cityName);
            selectedData.put(22,cityName);
        }
        if (countyName.size()>0){
            selectedData.put(18,countyName);
            selectedData.put(23,countyName);
        }
        if (townName.size()>0){
            selectedData.put(19,townName);
            selectedData.put(24,townName);
        }

        Long deptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());
        // 获取所有子部门
        List<DeptDO> deptList = deptService.getChildDeptList(deptId);
        // 以及当前部门
        deptList.add(deptService.getDept(deptId));

        // 提取部门名称并存储到列表中
        List<String> deptNameList = deptList.stream()
                .map(DeptDO::getName).toList();

        // 筛查点列表
        List<String> screenPointList = screenPointMapper.getScreenPointList(deptNameList);
        if (screenPointList.size()>0){
            selectedData.put(25, screenPointList);
        }

        // 导出 Excel 模板
        List<ScreenPersonImportTemplateVO> resVO = BeanUtils.toBean(list, ScreenPersonImportTemplateVO.class);
        ExcelUtils.write(response, "摸底人员导入模板.xls", "摸底人员数据", ScreenPersonImportTemplateVO.class,
                resVO, new CustomSheetWriteHandler().setMap(selectedData));
    }


    @GetMapping("/get-import-template2")
    @Operation(summary = "下载待筛查人员模板")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenPersonExcel2(HttpServletResponse response) throws IOException {
        // 模板数据
        List<ScreenPersonImportVO> list = screenPersonService.createSampleData();
        // 存放下拉选列表数据
        Map<Integer, List<String>> selectedData = new HashMap<>();
        screenPersonService.addSelectedData("is_new", 3, selectedData);
//        screenPersonService.addSelectedData("is_new", 3, selectedData);
        screenPersonService.addSelectedData("tb_first_people_type", 4, selectedData);
        screenPersonService.addSelectedData("tb_more_people_type", 5, selectedData);
        screenPersonService.addSelectedData("student_type", 6,  selectedData);
        screenPersonService.addSelectedData("tb_ethnic", 9, selectedData);

        // 获取地区名称数据
        List<String> provinceName = districtMapper.getProvinceName();
        List<String> cityName = districtMapper.getCityName();
        List<String> countyName = districtMapper.getCountyName();
        List<String> townName = districtMapper.getTownName();
        // 将地区名称数据放入选项数据中
        if (provinceName.size()>0){
            selectedData.put(15,provinceName);
            selectedData.put(20,provinceName);
        }
        if (cityName.size()>0){
            selectedData.put(16,cityName);
            selectedData.put(21,cityName);
        }
        if (countyName.size()>0){
            selectedData.put(17,countyName);
            selectedData.put(22,countyName);
        }
        if (townName.size()>0){
            selectedData.put(18,townName);
            selectedData.put(23,townName);
        }

        Long deptId = deptService.getMyDept(SecurityFrameworkUtils.getLoginUserId());
        // 获取所有子部门
        List<DeptDO> deptList = deptService.getChildDeptList(deptId);
        // 以及当前部门
        deptList.add(deptService.getDept(deptId));

        // 提取部门名称并存储到列表中
        List<String> deptNameList = deptList.stream()
                .map(DeptDO::getName).toList();

        // 筛查点列表
        List<String> screenPointList = screenPointMapper.getScreenPointList(deptNameList);
        if (screenPointList.size()>0){
            selectedData.put(24, screenPointList);
        }

        // 导出 Excel 模板
        ExcelUtils.write(response, "待筛查人员导入模板.xls", "待筛查人员数据", ScreenPersonImportTemplateVO2.class,
                BeanUtils.toBean(list, ScreenPersonImportTemplateVO2.class), new CustomSheetWriteHandler().setMap(selectedData));
    }


    @GetMapping("/export-excel")
    @Operation(summary = "导出摸底人员")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenPersonExcel(@Valid ScreenPersonPageReqVO pageReqVO,
                                        HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenPersonDO> list = screenPersonService.getScreenPersonPage(pageReqVO).getList();

        for (ScreenPersonDO obj : list) {
            String strMoreType = screenPersonService.resolveMoreTypeToString(obj.getMoreType());
            obj.setMoreTypeStr(strMoreType);
            screenPersonService.resolveDistrict(obj);
        }
        // 导出 Excel
        ExcelUtils.write2(response, "摸底人员表.xls", "摸底人员表", ScreenPersonRespVO.class,
                BeanUtils.toBean(list, ScreenPersonRespVO.class));
    }


    @GetMapping("/export")
    @Operation(summary = "导出待筛查人员")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenedExcel(@Valid ScreenPersonPageReqVO pageReqVO,
                                        HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScreenPersonDO> list = screenPersonService.getScreenedPage(pageReqVO).getList();

        for (ScreenPersonDO obj : list) {
            String strMoreType = screenPersonService.resolveMoreTypeToString(obj.getMoreType());
            obj.setMoreTypeStr(strMoreType);
            screenPersonService.resolveDistrict(obj);
        }

        // 导出 Excel
        ExcelUtils.write2(response, "待筛查人员表.xls", "待筛查人员表", ScreenPersonRespVO.class,
                BeanUtils.toBean(list, ScreenPersonRespVO.class));
    }


    @PostMapping("/import-template")
    @Operation(summary = "导入摸底/待筛查人员")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:create')")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
    })
    public CommonResult<ScreenPersonImportRespVO> importExcel(@RequestParam("file") MultipartFile file,
                                                              @RequestParam("year") String year,
                                                              @RequestParam("screenType") Integer screenType,
                                                              @RequestParam("screenStartTime") Long screenStartTime,
                                                              @RequestParam("screenEndTime") Long screenEndTime,
                                                              @RequestParam("deptId") Long deptId) throws Exception {
        List<ScreenPersonImportVO> list = ExcelUtils.read(file, ScreenPersonImportVO.class);
        LocalDateTime startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(screenStartTime), ZoneId.systemDefault());
        LocalDateTime endTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(screenEndTime), ZoneId.systemDefault());
        return success(screenPersonService.importScreenPerson(list, Integer.valueOf(year), screenType, startTime, endTime, deptId));
    }


    @GetMapping("/get-patientInfoList")
    @Operation(summary = "查看--患者基本信息")
    @Parameters({@Parameter(name = "year", description = "年份", required = true),
            @Parameter(name = "id", description = "患者id", required = true)})
    public CommonResult<PatientInfoList> getPatientInfoList(@RequestParam("id") Long id,
                                                            @RequestParam("year") Integer year,
                                                            @RequestParam("screenType") Integer screenType) {
        return success(screenPersonService.getPatientInfoList(id, year, screenType));
    }


    @GetMapping("/get-imageUrl")
    @Parameters({@Parameter(name = "type", description = "图片类型", required = true),
            @Parameter(name = "id", description = "患者id", required = true),
            @Parameter(name = "screenOrder", description = "筛查次序", required = true),
            @Parameter(name = "screenId", description = "筛查编号", required = true)})
    public CommonResult<ImageVO> getImageUrl(@RequestParam("personId") Long personId,
                                             @RequestParam("type") Integer type,
                                             @RequestParam("screenOrder") Integer screenOrder,
                                             @RequestParam("screenId") String screenId,
                                             @RequestParam("year") Integer year,
                                             @RequestParam("screenType") Integer screenType) {
        return success(screenPersonService.getImageUrl(personId, type, screenId, screenOrder, year, screenType));
    }


    @PostMapping(value = "/update-image")
    @Operation(summary = "更换照片")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:create')")
    public CommonResult<String> updateImage(@RequestParam("file") MultipartFile file,
                                            @RequestParam("imageId") Long imageId) throws Exception {
        if (file.isEmpty()) {
            throw exception(FILE_IS_EMPTY);
        }
        String image = screenPersonService.updateImage(imageId, file.getInputStream());
        return success(image);
    }


    @PostMapping(value = "/update-image2")
    @Operation(summary = "更换照片")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:create')")
    public CommonResult<String> updateImage2(@RequestParam("file") MultipartFile file,
                                             @RequestParam("personId") Long personId,
                                             @RequestParam("screenOrder") Integer screenOrder,
                                             @RequestParam("screenId") String screenId,
                                             @RequestParam("imageType") Integer imageType,
                                             @RequestParam("year") Integer year,
                                             @RequestParam("screenType") Integer screenType
                                             ) throws IOException {

        String image = screenPersonService.updateImag2(personId, screenId, imageType, screenOrder, year, screenType, file.getInputStream());
        return success(image);
    }
    @PostMapping(value = "/update-ct-image")
    @Operation(summary = "更换ct照片")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:create')")
    public CommonResult<String> updateCtImage2(@RequestParam("file") MultipartFile file,
                                             @RequestParam("personId") Long personId,
                                             @RequestParam("screenOrder") Integer screenOrder,
                                             @RequestParam("screenId") String screenId,
                                             @RequestParam("idNum") String idNum,
                                             @RequestParam("imageType") Integer imageType,
                                             @RequestParam("year") Integer year,
                                             @RequestParam("screenType") Integer screenType
                                             ) throws IOException {
        String image = screenPersonService.updateCtImage(personId, screenId, idNum,imageType, screenOrder, year, screenType, file.getInputStream());
        return success(image);

    }

    @GetMapping("/statistics-export")
    @Operation(summary = "工作进展报告--统计表--导出表格")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenPersonExcel(@Valid ScreenPersonStatisticsReqVO reqVO,
                                        HttpServletResponse response) {
        screenStaticsHistoryService.recoverScreenStaticsHistory(BeanUtils.toBean(reqVO, ScreenStaticsHistorySaveReqVO.class));
        screenPersonService.exportStatistics(reqVO, response);
    }

    @GetMapping("/archives-export")
    @Operation(summary = "工作进展报告--统计表--导出知情同意书")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenPersonArchive(@Valid ScreenPersonStatisticsReqVO reqVO,
                                            HttpServletRequest request, HttpServletResponse response) throws IOException {
        screenPersonService.exportScreenPersonArchive(reqVO, request, response);
    }

    @GetMapping("/archives-export2")
    @Operation(summary = "工作进展报告--统计表--导出档案")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScreenPersonArchive2(@Valid ScreenPersonStatisticsReqVO reqVO,
                                          HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(reqVO);
        screenPersonService.exportScreenPersonArchive2(reqVO, request, response);
    }
    @PostMapping("/student/list")
    @Operation(summary = "小程序-获取学生信息")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:student-query')")
    public CommonResult<List<StudentInfoReqVO>> getStudentInfo(@RequestBody @NotEmpty  @RequestParam("tel")String tel){
        List<StudentInfo> studentInfoReqVOList = screenPersonService.getStudentByGuardianTel(tel, null);
        return successMsg(BeanUtils.toBean(studentInfoReqVOList, StudentInfoReqVO.class));
    }
    @PostMapping("/student/notice")
    @Operation(summary = "小程序-获取学生检查通知")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:student-query')")
    public CommonResult<NoticeRespVO> getNotice(@RequestBody @NotEmpty @RequestParam("idcard") String idcard){
        NoticeRespVO notice = screenPersonService.getStudentNoticeByIdNum(idcard, null, null);
        return successMsg(notice);
    }
    @PostMapping("/student/examination-form")
    @Operation(summary = "小程序-获取学生体检单")
    @PreAuthorize("@ss.hasPermission('tb:screen-person:student-query')")
    public CommonResult<ExaminationFormRespVO> getExaminationForm(@RequestBody @NotEmpty @RequestParam("idcard") String idcard){
        ExaminationFormRespVO examinationForm = screenPersonService.getExaminationForm(idcard, null);
        return successMsg(examinationForm);
    }
}