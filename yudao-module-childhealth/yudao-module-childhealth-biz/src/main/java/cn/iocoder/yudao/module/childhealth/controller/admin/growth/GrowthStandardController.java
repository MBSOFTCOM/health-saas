package cn.iocoder.yudao.module.childhealth.controller.admin.growth;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.childhealth.controller.admin.growth.vo.GrowthCalcReqVO;
import cn.iocoder.yudao.module.childhealth.controller.admin.growth.vo.GrowthCalcRespVO;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.growth.GrowthStandardDO;
import cn.iocoder.yudao.module.childhealth.service.growth.GrowthStandardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 生长标准计算
 *
 * 模块: 需求9 ▲ WHO/九城市标准差百分位自动计算
 *
 * @author 系统
 */
@Tag(name = "管理后台 - 生长标准计算")
@RestController
@RequestMapping("/childhealth/growth-standard")
@Validated
public class GrowthStandardController {

    @Resource
    private GrowthStandardService growthStandardService;

    @PostMapping("/calc")
    @Operation(summary = "计算 SD 值与百分位")
    @PreAuthorize("@ss.hasPermission('childhealth:growth-standard:calc')")
    public CommonResult<GrowthCalcRespVO> calculate(@Valid @RequestBody GrowthCalcReqVO reqVO) {
        String source = reqVO.getSource() == null || reqVO.getSource().isBlank() ? "WHO" : reqVO.getSource();
        BigDecimal ageMonths = reqVO.getAgeMonths();
        boolean corrected = false;
        BigDecimal correctedAge = null;
        // 早产儿使用 Fenton 曲线 + 矫正月龄
        if (reqVO.getGestationalAgeWeeks() != null && reqVO.getGestationalAgeWeeks() < 37) {
            correctedAge = growthStandardService.calculateCorrectedAge(ageMonths, reqVO.getGestationalAgeWeeks());
            if (correctedAge.compareTo(ageMonths) != 0) {
                ageMonths = correctedAge;
                corrected = true;
            }
            // 周龄 < 50 时使用 Fenton
            if (reqVO.getGestationalAgeWeeks() < 50 && "FENTON".equalsIgnoreCase(source)) {
                BigDecimal ga = BigDecimal.valueOf(reqVO.getGestationalAgeWeeks());
                GrowthStandardDO fenton = growthStandardService.getFentonStandard(
                        reqVO.getGender(), ga, reqVO.getIndicatorType());
                if (fenton != null) {
                    BigDecimal sd = growthStandardService.calculateSd(
                            reqVO.getGender(), ga, reqVO.getIndicatorType(),
                            reqVO.getMeasuredValue(), "FENTON");
                    BigDecimal percentile = growthStandardService.sdToPercentile(sd);
                    return success(buildResp(reqVO, sd, percentile, fenton, "FENTON", corrected, correctedAge));
                }
            }
        }
        GrowthStandardDO std = growthStandardService.getStandard(
                reqVO.getGender(), ageMonths, reqVO.getIndicatorType(), source);
        if (std == null) {
            return success(null);
        }
        BigDecimal sd = growthStandardService.calculateSd(
                reqVO.getGender(), ageMonths, reqVO.getIndicatorType(),
                reqVO.getMeasuredValue(), source);
        BigDecimal percentile = growthStandardService.sdToPercentile(sd);
        return success(buildResp(reqVO, sd, percentile, std, source, corrected, correctedAge));
    }

    @GetMapping("/sd-to-percentile")
    @Operation(summary = "SD 值转百分位")
    @Parameter(name = "sd", description = "SD 值", required = true, example = "1.5")
    @PreAuthorize("@ss.hasPermission('childhealth:growth-standard:calc')")
    public CommonResult<BigDecimal> sdToPercentile(@RequestParam("sd") BigDecimal sd) {
        return success(growthStandardService.sdToPercentile(sd));
    }

    @GetMapping("/percentile-to-sd")
    @Operation(summary = "百分位转 SD 值")
    @Parameter(name = "percentile", description = "百分位 0-100", required = true, example = "84.13")
    @PreAuthorize("@ss.hasPermission('childhealth:growth-standard:calc')")
    public CommonResult<BigDecimal> percentileToSd(@RequestParam("percentile") BigDecimal percentile) {
        return success(growthStandardService.percentileToSd(percentile));
    }

    @GetMapping("/curve")
    @Operation(summary = "查询生长曲线数据（用于前端绘制曲线图）")
    @PreAuthorize("@ss.hasPermission('childhealth:growth-standard:query')")
    public CommonResult<List<GrowthStandardDO>> getCurve(
            @RequestParam("gender") Integer gender,
            @RequestParam("indicatorType") String indicatorType,
            @RequestParam(value = "source", defaultValue = "WHO") String source) {
        return success(growthStandardService.listStandardBySource(gender, indicatorType, source));
    }

    @GetMapping("/standard")
    @Operation(summary = "查询指定月龄的标准化数据")
    @PreAuthorize("@ss.hasPermission('childhealth:growth-standard:query')")
    public CommonResult<GrowthStandardDO> getStandard(
            @RequestParam("gender") Integer gender,
            @RequestParam("ageMonths") BigDecimal ageMonths,
            @RequestParam("indicatorType") String indicatorType,
            @RequestParam(value = "source", defaultValue = "WHO") String source) {
        return success(growthStandardService.getStandard(gender, ageMonths, indicatorType, source));
    }

    @GetMapping("/corrected-age")
    @Operation(summary = "计算早产儿矫正月龄")
    @PreAuthorize("@ss.hasPermission('childhealth:growth-standard:calc')")
    public CommonResult<BigDecimal> calculateCorrectedAge(
            @RequestParam("ageMonths") BigDecimal ageMonths,
            @RequestParam("gestationalAgeWeeks") Integer gestationalAgeWeeks) {
        return success(growthStandardService.calculateCorrectedAge(ageMonths, gestationalAgeWeeks));
    }

    private GrowthCalcRespVO buildResp(GrowthCalcReqVO reqVO, BigDecimal sd, BigDecimal percentile,
                                        GrowthStandardDO std, String source,
                                        boolean corrected, BigDecimal correctedAge) {
        GrowthCalcRespVO resp = new GrowthCalcRespVO();
        resp.setGender(reqVO.getGender());
        resp.setAgeMonths(reqVO.getAgeMonths());
        resp.setIndicatorType(reqVO.getIndicatorType());
        resp.setMeasuredValue(reqVO.getMeasuredValue());
        resp.setSdValue(sd);
        resp.setPercentile(percentile);
        resp.setMedian(std.getMedian());
        resp.setSdNeg2(std.getSdNeg2());
        resp.setSdPos2(std.getSdPos2());
        resp.setNutritionStatus(growthStandardService.assessNutritionStatus(sd));
        resp.setSource(source);
        resp.setCorrectedAgeUsed(corrected);
        resp.setCorrectedAge(correctedAge);
        return resp;
    }

}
