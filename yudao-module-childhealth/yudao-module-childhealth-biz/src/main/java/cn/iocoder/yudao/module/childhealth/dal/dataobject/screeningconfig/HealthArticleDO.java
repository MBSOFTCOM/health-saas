package cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 健康文章表 DO
 *
 * 对应表: health_article
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@TableName("health_article")
@KeySequence("health_article_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthArticleDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 文章编码
     */
    private String articleCode;

    /**
     * 标题
     */
    private String title;

    /**
     * 分类（如：眼保健/口腔/营养/心理）
     */
    private String category;

    /**
     * 封面图URL
     */
    private String coverUrl;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 正文（HTML）
     */
    private String content;

    /**
     * 标签JSON
     */
    private String tags;

    /**
     * 是否置顶 0否 1是
     */
    private Integer isTop;

    /**
     * 发布状态 0草稿 1待发布 2已发布 3已下线
     */
    private Integer publishStatus;

    /**
     * 发布时间（定时发布）
     */
    private LocalDateTime publishTime;

    /**
     * 作者
     */
    private String author;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 适用疾病编码JSON（精准推送）
     */
    private String applicableDisease;

}
