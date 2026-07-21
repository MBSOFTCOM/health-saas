package cn.iocoder.yudao.module.childhealth.api.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息推送DTO
 */
public final class MessagePushDTO {
    private MessagePushDTO() {}

    /**
     * 消息推送配置创建请求
     */
    @Data
    public static class MessagePushConfigCreateRequest {
        @NotBlank private String configCode;
        @NotBlank private String configName;
        @NotNull private Integer pushType; // 1催检 2复筛 3随访 4宣教
        @NotNull private Integer pushChannel; // 1短信 2微信 3APP推送
        private String templateId;
        @NotBlank private String templateContent;
        private String pushRule; // JSON配置
    }

    /**
     * 消息推送配置响应
     */
    @Data
    public static class MessagePushConfigResponse {
        private Long id;
        private String configCode;
        private String configName;
        private Integer pushType;
        private Integer pushChannel;
        private String templateId;
        private String templateContent;
        private String pushRule;
        private Integer status;
        private LocalDateTime createTime;
    }

    /**
     * 消息推送请求
     */
    @Data
    public static class MessagePushRequest {
        private Long configId;
        @NotNull private Integer pushType;
        @NotNull private Integer pushChannel;
        @NotNull private Integer targetType; // 1儿童 2监护人 3学生
        @NotNull private Long targetId;
        private String mobile;
        private String openid;
        @NotBlank private String pushContent;
        private LocalDateTime scheduledPushTime; // 计划推送时间,null表示立即推送
    }

    /**
     * 批量消息推送请求
     */
    @Data
    public static class BatchMessagePushRequest {
        private Long configId;
        @NotNull private Integer pushType;
        @NotNull private Integer pushChannel;
        @NotNull private Integer targetType;
        @NotNull private List<Long> targetIds;
        @NotBlank private String pushContent;
        private LocalDateTime scheduledPushTime;
    }

    /**
     * 消息推送记录响应
     */
    @Data
    public static class MessagePushLogResponse {
        private Long id;
        private Long configId;
        private Integer pushType;
        private Integer pushChannel;
        private Integer targetType;
        private Long targetId;
        private String mobile;
        private String openid;
        private String pushContent;
        private LocalDateTime pushTime;
        private Integer pushStatus; // 1成功 2失败 3待推送 4已取消
        private String errorMsg;
        private Boolean isRead;
        private LocalDateTime createTime;
    }

    /**
     * 消息推送查询请求
     */
    @Data
    public static class MessagePushQueryRequest {
        private Long configId;
        private Integer pushType;
        private Integer pushChannel;
        private Integer targetType;
        private Long targetId;
        private Integer pushStatus;
        private LocalDate pushDateStart;
        private LocalDate pushDateEnd;
        private Integer pageNum;
        private Integer pageSize;
    }

    /**
     * 健康文章创建请求
     */
    @Data
    public static class HealthArticleCreateRequest {
        @NotBlank private String articleCode;
        @NotBlank private String title;
        private String category;
        private String tags; // JSON数组
        private String coverUrl;
        private String summary;
        @NotBlank private String content;
        private String author;
        private Integer isTop; // 0否 1是
        private LocalDateTime publishTime;
    }

    /**
     * 健康文章更新请求
     */
    @Data
    public static class HealthArticleUpdateRequest {
        @NotNull private Long id;
        private String title;
        private String category;
        private String tags;
        private String coverUrl;
        private String summary;
        private String content;
        private String author;
        private Integer isTop; // 0否 1是
        private Integer publishStatus; // 0草稿 1待发布 2已发布 3已下线
        private LocalDateTime publishTime;
    }

    /**
     * 健康文章响应
     */
    @Data
    public static class HealthArticleResponse {
        private Long id;
        private String articleCode;
        private String title;
        private String category;
        private String tags;
        private String coverUrl;
        private String summary;
        private String content;
        private String author;
        private Integer viewCount;
        private Integer isTop; // 0否 1是
        private LocalDateTime publishTime;
        private Integer publishStatus; // 0草稿 1待发布 2已发布 3已下线
        private LocalDateTime createTime;
    }

    /**
     * 文章阅读记录响应
     */
    @Data
    public static class ArticleReadLogResponse {
        private Long id;
        private Long articleId;
        private Integer readerType; // 1医护 2家长
        private Long readerId;
        private LocalDateTime readTime;
        private Integer readDuration; // 阅读时长(秒)
        private LocalDateTime createTime;
    }

    /**
     * 消息推送统计响应
     */
    @Data
    public static class MessagePushStatisticsResponse {
        private Integer totalPushed;
        private Integer successCount;
        private Integer failedCount;
        private Integer pendingCount;
        private Integer cancelledCount;
        private Integer readCount;
        private Integer unreadCount;
    }
}