package cn.iocoder.yudao.module.childhealth.controller.admin.screening;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.childhealth.controller.admin.screening.vo.knowledge.*;
import cn.iocoder.yudao.module.childhealth.dal.dataobject.screening.DiseaseKnowledgeDO;
import cn.iocoder.yudao.module.childhealth.service.screening.DiseaseKnowledgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 疾病知识库")
@RestController
@RequestMapping("/childhealth/disease-knowledge")
@Validated
public class DiseaseKnowledgeController {

    @Resource
    private DiseaseKnowledgeService diseaseKnowledgeService;

    @PostMapping("/create")
    @Operation(summary = "创建疾病知识库")
    @PreAuthorize("@ss.hasPermission('childhealth:disease-knowledge:create')")
    public CommonResult<Long> createDiseaseKnowledge(@Valid @RequestBody DiseaseKnowledgeSaveReqVO createReqVO) {
        return success(diseaseKnowledgeService.createDiseaseKnowledge(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新疾病知识库")
    @PreAuthorize("@ss.hasPermission('childhealth:disease-knowledge:update')")
    public CommonResult<Boolean> updateDiseaseKnowledge(@Valid @RequestBody DiseaseKnowledgeSaveReqVO updateReqVO) {
        diseaseKnowledgeService.updateDiseaseKnowledge(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除疾病知识库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:disease-knowledge:delete')")
    public CommonResult<Boolean> deleteDiseaseKnowledge(@RequestParam("id") Long id) {
        diseaseKnowledgeService.deleteDiseaseKnowledge(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得疾病知识库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('childhealth:disease-knowledge:query')")
    public CommonResult<DiseaseKnowledgeRespVO> getDiseaseKnowledge(@RequestParam("id") Long id) {
        DiseaseKnowledgeDO diseaseKnowledge = diseaseKnowledgeService.getDiseaseKnowledge(id);
        return success(BeanUtils.toBean(diseaseKnowledge, DiseaseKnowledgeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得疾病知识库分页")
    @PreAuthorize("@ss.hasPermission('childhealth:disease-knowledge:query')")
    public CommonResult<PageResult<DiseaseKnowledgeRespVO>> getDiseaseKnowledgePage(@Valid DiseaseKnowledgePageReqVO pageReqVO) {
        PageResult<DiseaseKnowledgeDO> pageResult = diseaseKnowledgeService.getDiseaseKnowledgePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DiseaseKnowledgeRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得疾病知识库列表")
    @PreAuthorize("@ss.hasPermission('childhealth:disease-knowledge:query')")
    public CommonResult<List<DiseaseKnowledgeRespVO>> getDiseaseKnowledgeList(@Valid DiseaseKnowledgeListReqVO listReqVO) {
        List<DiseaseKnowledgeDO> list = diseaseKnowledgeService.getDiseaseKnowledgeList(listReqVO);
        return success(BeanUtils.toBean(list, DiseaseKnowledgeRespVO.class));
    }

    @GetMapping("/get-by-code")
    @Operation(summary = "根据疾病编码获取疾病知识")
    @Parameter(name = "diseaseCode", description = "疾病编码", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:disease-knowledge:query')")
    public CommonResult<DiseaseKnowledgeRespVO> getByDiseaseCode(@RequestParam("diseaseCode") String diseaseCode) {
        DiseaseKnowledgeDO diseaseKnowledge = diseaseKnowledgeService.getByDiseaseCode(diseaseCode);
        return success(BeanUtils.toBean(diseaseKnowledge, DiseaseKnowledgeRespVO.class));
    }

    @GetMapping("/list-by-category")
    @Operation(summary = "根据分类获取疾病知识列表")
    @Parameter(name = "category", description = "分类", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:disease-knowledge:query')")
    public CommonResult<List<DiseaseKnowledgeRespVO>> getByCategory(@RequestParam("category") String category) {
        List<DiseaseKnowledgeDO> list = diseaseKnowledgeService.getByCategory(category);
        return success(BeanUtils.toBean(list, DiseaseKnowledgeRespVO.class));
    }

}