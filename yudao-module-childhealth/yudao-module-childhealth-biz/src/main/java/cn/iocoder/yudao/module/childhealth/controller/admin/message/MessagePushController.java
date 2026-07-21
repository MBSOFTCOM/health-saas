package cn.iocoder.yudao.module.childhealth.controller.admin.message;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.childhealth.api.message.MessagePushService;
import cn.iocoder.yudao.module.childhealth.api.message.dto.MessagePushDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 消息推送Controller
 */
@Tag(name = "管理后台 - 消息推送")
@RestController
@RequestMapping("/childhealth/message")
public class MessagePushController {

    @Resource
    private MessagePushService messagePushService;

    // ==================== 消息推送配置管理 ====================

    @PostMapping("/config")
    @Operation(summary = "创建消息推送配置")
    @PreAuthorize("@ss.hasPermission('childhealth:message:create')")
    public CommonResult<Long> createMessagePushConfig(@Valid @RequestBody MessagePushConfigCreateRequest request) {
        return success(messagePushService.createMessagePushConfig(request));
    }

    @PutMapping("/config/{id}/status")
    @Operation(summary = "更新消息推送配置状态")
    @PreAuthorize("@ss.hasPermission('childhealth:message:update')")
    public CommonResult<Boolean> updateMessagePushConfigStatus(
            @PathVariable Long id,
            @RequestParam Integer status) {
        messagePushService.updateMessagePushConfigStatus(id, status);
        return success(true);
    }

    @GetMapping("/config/{id}")
    @Operation(summary = "获取消息推送配置详情")
    @Parameter(name = "id", description = "配置ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:message:query')")
    public CommonResult<MessagePushConfigResponse> getMessagePushConfig(@PathVariable Long id) {
        return success(messagePushService.getMessagePushConfig(id));
    }

    @GetMapping("/config/active")
    @Operation(summary = "获取所有启用的消息推送配置")
    @PreAuthorize("@ss.hasPermission('childhealth:message:query')")
    public CommonResult<List<MessagePushConfigResponse>> getActiveMessagePushConfigs() {
        return success(messagePushService.getActiveMessagePushConfigs());
    }

    @GetMapping("/config/type/{pushType}")
    @Operation(summary = "根据推送类型获取配置")
    @Parameter(name = "pushType", description = "推送类型", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:message:query')")
    public CommonResult<List<MessagePushConfigResponse>> getMessagePushConfigsByType(@PathVariable Integer pushType) {
        return success(messagePushService.getMessagePushConfigsByType(pushType));
    }

    // ==================== 消息推送管理 ====================

    @PostMapping("/send")
    @Operation(summary = "发送消息")
    @PreAuthorize("@ss.hasPermission('childhealth:message:send')")
    public CommonResult<Long> sendMessage(@Valid @RequestBody MessagePushRequest request) {
        return success(messagePushService.sendMessage(request));
    }

    @PostMapping("/send/batch")
    @Operation(summary = "批量发送消息")
    @PreAuthorize("@ss.hasPermission('childhealth:message:send')")
    public CommonResult<Boolean> batchSendMessage(@Valid @RequestBody BatchMessagePushRequest request) {
        messagePushService.batchSendMessage(request);
        return success(true);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消消息推送")
    @Parameter(name = "id", description = "消息ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:message:update')")
    public CommonResult<Boolean> cancelMessage(@PathVariable Long id) {
        messagePushService.cancelMessage(id);
        return success(true);
    }

    @PutMapping("/retry/{id}")
    @Operation(summary = "重试发送失败的消息")
    @Parameter(name = "id", description = "消息ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:message:send')")
    public CommonResult<Boolean> retryMessage(@PathVariable Long id) {
        messagePushService.retryMessage(id);
        return success(true);
    }

    @PutMapping("/read/{id}")
    @Operation(summary = "标记消息已读")
    @Parameter(name = "id", description = "消息ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:message:update')")
    public CommonResult<Boolean> markMessageAsRead(@PathVariable Long id) {
        messagePushService.markMessageAsRead(id);
        return success(true);
    }

    @PostMapping("/log/page")
    @Operation(summary = "分页查询消息推送记录")
    @PreAuthorize("@ss.hasPermission('childhealth:message:query')")
    public CommonResult<PageResult<MessagePushLogResponse>> getMessagePushLogPage(
            @RequestBody MessagePushQueryRequest request) {
        return success(messagePushService.getMessagePushLogPage(request));
    }

    @GetMapping("/log/{id}")
    @Operation(summary = "获取消息推送详情")
    @Parameter(name = "id", description = "消息ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:message:query')")
    public CommonResult<MessagePushLogResponse> getMessagePushLog(@PathVariable Long id) {
        return success(messagePushService.getMessagePushLog(id));
    }

    @GetMapping("/log/history")
    @Operation(summary = "获取目标的消息推送历史")
    @PreAuthorize("@ss.hasPermission('childhealth:message:query')")
    public CommonResult<List<MessagePushLogResponse>> getMessagePushHistory(
            @RequestParam Integer targetType,
            @RequestParam Long targetId) {
        return success(messagePushService.getMessagePushHistory(targetType, targetId));
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取消息推送统计")
    @PreAuthorize("@ss.hasPermission('childhealth:message:query')")
    public CommonResult<MessagePushStatisticsResponse> getMessagePushStatistics() {
        return success(messagePushService.getMessagePushStatistics());
    }

    // ==================== 健康文章管理 ====================

    @PostMapping("/article")
    @Operation(summary = "创建健康文章")
    @PreAuthorize("@ss.hasPermission('childhealth:article:create')")
    public CommonResult<Long> createHealthArticle(@Valid @RequestBody HealthArticleCreateRequest request) {
        return success(messagePushService.createHealthArticle(request));
    }

    @PutMapping("/article")
    @Operation(summary = "更新健康文章")
    @PreAuthorize("@ss.hasPermission('childhealth:article:update')")
    public CommonResult<Boolean> updateHealthArticle(@Valid @RequestBody HealthArticleUpdateRequest request) {
        messagePushService.updateHealthArticle(request);
        return success(true);
    }

    @DeleteMapping("/article/{id}")
    @Operation(summary = "删除健康文章")
    @Parameter(name = "id", description = "文章ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:article:delete')")
    public CommonResult<Boolean> deleteHealthArticle(@PathVariable Long id) {
        messagePushService.deleteHealthArticle(id);
        return success(true);
    }

    @PutMapping("/article/{id}/publish")
    @Operation(summary = "发布健康文章")
    @Parameter(name = "id", description = "文章ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:article:update')")
    public CommonResult<Boolean> publishHealthArticle(@PathVariable Long id) {
        messagePushService.publishHealthArticle(id);
        return success(true);
    }

    @PutMapping("/article/{id}/unpublish")
    @Operation(summary = "下架健康文章")
    @Parameter(name = "id", description = "文章ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:article:update')")
    public CommonResult<Boolean> unpublishHealthArticle(@PathVariable Long id) {
        messagePushService.unpublishHealthArticle(id);
        return success(true);
    }

    @GetMapping("/article/{id}")
    @Operation(summary = "获取健康文章详情")
    @Parameter(name = "id", description = "文章ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:article:query')")
    public CommonResult<HealthArticleResponse> getHealthArticle(@PathVariable Long id) {
        return success(messagePushService.getHealthArticle(id));
    }

    @GetMapping("/article/page")
    @Operation(summary = "分页查询健康文章")
    @PreAuthorize("@ss.hasPermission('childhealth:article:query')")
    public CommonResult<PageResult<HealthArticleResponse>> getHealthArticlePage(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        return success(messagePushService.getHealthArticlePage(category, status, pageNum, pageSize));
    }

    @GetMapping("/article/published")
    @Operation(summary = "获取已发布的健康文章列表")
    @PreAuthorize("@ss.hasPermission('childhealth:article:query')")
    public CommonResult<List<HealthArticleResponse>> getPublishedArticles() {
        return success(messagePushService.getPublishedArticles());
    }

    @GetMapping("/article/category/{category}")
    @Operation(summary = "根据分类获取文章")
    @Parameter(name = "category", description = "文章分类", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:article:query')")
    public CommonResult<List<HealthArticleResponse>> getArticlesByCategory(@PathVariable String category) {
        return success(messagePushService.getArticlesByCategory(category));
    }

    @GetMapping("/article/top")
    @Operation(summary = "获取置顶文章")
    @PreAuthorize("@ss.hasPermission('childhealth:article:query')")
    public CommonResult<List<HealthArticleResponse>> getTopArticles() {
        return success(messagePushService.getTopArticles());
    }

    @PostMapping("/article/{id}/read")
    @Operation(summary = "增加文章阅读量")
    @Parameter(name = "id", description = "文章ID", required = true)
    public CommonResult<Boolean> increaseReadCount(@PathVariable Long id) {
        messagePushService.increaseReadCount(id);
        return success(true);
    }

    @PostMapping("/article/{id}/log-read")
    @Operation(summary = "记录文章阅读日志")
    @Parameter(name = "id", description = "文章ID", required = true)
    public CommonResult<Boolean> logArticleRead(
            @PathVariable Long id,
            @RequestParam Integer readerType,
            @RequestParam Long readerId,
            @RequestParam Integer readDuration) {
        messagePushService.logArticleRead(id, readerType, readerId, readDuration);
        return success(true);
    }

    @GetMapping("/article/{id}/read-logs")
    @Operation(summary = "获取文章阅读日志")
    @Parameter(name = "id", description = "文章ID", required = true)
    @PreAuthorize("@ss.hasPermission('childhealth:article:query')")
    public CommonResult<List<ArticleReadLogResponse>> getArticleReadLogs(@PathVariable Long id) {
        return success(messagePushService.getArticleReadLogs(id));
    }
}