package cn.iocoder.yudao.module.childhealth.controller.admin.external;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.service.external.adapter.DeviceIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.adapter.HisIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.adapter.LisIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.adapter.ObstetricsIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.adapter.PacsIntegrationAdapter;
import cn.iocoder.yudao.module.childhealth.service.external.dto.DeviceMeasurementDTO;
import cn.iocoder.yudao.module.childhealth.service.external.dto.HisChildInfoDTO;
import cn.iocoder.yudao.module.childhealth.service.external.dto.HisNeonatalDiagnosisDTO;
import cn.iocoder.yudao.module.childhealth.service.external.dto.LisReportDTO;
import cn.iocoder.yudao.module.childhealth.service.external.dto.ObstetricsHighRiskNewbornDTO;
import cn.iocoder.yudao.module.childhealth.service.external.dto.PacsReportDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 外部系统对接测试 Controller
 *
 * 仅用于开发期联调，验证各适配器（Mock/Real）的数据拉取能力。
 * 生产环境建议关闭或加权限控制。
 */
@Tag(name = "外部系统对接测试")
@RestController
@RequestMapping("/childhealth/external")
public class ExternalSystemTestController {

    @Resource private HisIntegrationAdapter hisAdapter;
    @Resource private ObstetricsIntegrationAdapter obstetricsAdapter;
    @Resource private LisIntegrationAdapter lisAdapter;
    @Resource private PacsIntegrationAdapter pacsAdapter;
    @Resource private DeviceIntegrationAdapter deviceAdapter;

    // ============ HIS ============

    @GetMapping("/his/child-info")
    @Operation(summary = "HIS 拉取儿童信息")
    public CommonResult<HisChildInfoDTO> fetchChildInfo(
            @Parameter(description = "HIS 患者ID") @RequestParam(required = false) String hisPatientId,
            @Parameter(description = "医保卡号") @RequestParam(required = false) String medicareCardNo) {
        return success(hisAdapter.fetchChildInfo(hisPatientId, medicareCardNo));
    }

    @GetMapping("/his/neonatal-diagnoses")
    @Operation(summary = "HIS 拉取新生儿住院诊断")
    public CommonResult<List<HisNeonatalDiagnosisDTO>> fetchNeonatalDiagnoses(
            @Parameter(description = "HIS 患者ID") @RequestParam String hisPatientId) {
        return success(hisAdapter.fetchNeonatalDiagnoses(hisPatientId));
    }

    @GetMapping("/his/connection")
    @Operation(summary = "HIS 连接检查")
    public CommonResult<Boolean> checkHisConnection() {
        return success(hisAdapter.checkConnection());
    }

    // ============ 产科 ============

    @GetMapping("/obstetrics/high-risk-list")
    @Operation(summary = "产科高危新生儿列表")
    public CommonResult<List<ObstetricsHighRiskNewbornDTO>> fetchHighRiskList(
            @Parameter(description = "开始日期 yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @Parameter(description = "结束日期 yyyy-MM-dd") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return success(obstetricsAdapter.fetchHighRiskNewbornList(startDate, endDate));
    }

    @GetMapping("/obstetrics/connection")
    @Operation(summary = "产科系统连接检查")
    public CommonResult<Boolean> checkObstetricsConnection() {
        return success(obstetricsAdapter.checkConnection());
    }

    // ============ LIS ============

    @GetMapping("/lis/reports")
    @Operation(summary = "LIS 拉取检验报告")
    public CommonResult<List<LisReportDTO>> fetchLisReports(
            @Parameter(description = "HIS 患者ID") @RequestParam String hisPatientId) {
        return success(lisAdapter.fetchReportsByPatient(hisPatientId));
    }

    @GetMapping("/lis/connection")
    @Operation(summary = "LIS 系统连接检查")
    public CommonResult<Boolean> checkLisConnection() {
        return success(lisAdapter.checkConnection());
    }

    // ============ PACS ============

    @GetMapping("/pacs/reports")
    @Operation(summary = "PACS 拉取影像报告")
    public CommonResult<List<PacsReportDTO>> fetchPacsReports(
            @Parameter(description = "HIS 患者ID") @RequestParam String hisPatientId) {
        return success(pacsAdapter.fetchReportsByPatient(hisPatientId));
    }

    @GetMapping("/pacs/connection")
    @Operation(summary = "PACS 系统连接检查")
    public CommonResult<Boolean> checkPacsConnection() {
        return success(pacsAdapter.checkConnection());
    }

    // ============ 设备 ============

    @GetMapping("/device/simulate")
    @Operation(summary = "模拟设备测量数据")
    public CommonResult<DeviceMeasurementDTO> simulateMeasurement(
            @Parameter(description = "设备序列号") @RequestParam(required = false, defaultValue = "MOCK_DEVICE_001") String deviceSerialNo) {
        return success(deviceAdapter.simulateMeasurement(deviceSerialNo));
    }

    @GetMapping("/device/online")
    @Operation(summary = "设备在线状态检查")
    public CommonResult<Boolean> isDeviceOnline(
            @Parameter(description = "设备序列号") @RequestParam String deviceSerialNo) {
        return success(deviceAdapter.isDeviceOnline(deviceSerialNo));
    }

}
