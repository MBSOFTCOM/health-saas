package cn.iocoder.yudao.module.childhealth.controller.admin.rule;
import cn.iocoder.yudao.framework.common.pojo.CommonResult; import cn.iocoder.yudao.module.childhealth.api.rule.dto.AbnormalRuleDTO.*; import cn.iocoder.yudao.module.childhealth.service.rule.AbnormalRuleService; import jakarta.annotation.Resource; import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.util.List; import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
@RestController @RequestMapping("/childhealth/rules") public class AbnormalRuleController { @Resource private AbnormalRuleService service;
 @GetMapping("/{id}") @PreAuthorize("@ss.hasPermission('childhealth:rule:query')") public CommonResult<RuleResponse> get(@PathVariable Long id){return success(service.get(id));}
 @PostMapping @PreAuthorize("@ss.hasPermission('childhealth:rule:create')") public CommonResult<Long> create(@Valid @RequestBody RuleSaveRequest request){return success(service.create(request));}
 @PutMapping("/{id}") @PreAuthorize("@ss.hasPermission('childhealth:rule:update')") public CommonResult<Boolean> update(@PathVariable Long id,@Valid @RequestBody RuleSaveRequest request){service.update(id,request);return success(true);}
 @PostMapping("/match") @PreAuthorize("@ss.hasPermission('childhealth:rule:match')") public CommonResult<List<RuleResponse>> match(@Valid @RequestBody MatchRequest request){return success(service.match(request));}
}
