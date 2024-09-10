package cn.iocoder.yudao.module.ppd.dal.dataobject.screenimages;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 移动端各组离线图片信息 DO
 *
 * @author 芋道源码
 */
@TableName("tb_screen_images")
@KeySequence("tb_screen_images_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenImagesDO {

    /**
     * 自增主键
     */
    @TableId
    private Long id;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 筛查编号
     */
    private String screenId;
    /**
     * 身份证
     */
    private String idNum;
    /**
     * 对应摸底表中id
     */
    private Long personId;
    /**
     * 图片来源，1-DR 2-CT 3-实验室-痰菌培养图 4-心电图	5-即时痰、6-晨痰、7-夜间痰
     */
    private Integer type;
    /**
     * 图片路径（平板本地路径，保存在 /doc/..中）
     */
    private String path;
    /**
     * 网络图片路径（完整路径）
     */
    private String url;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 筛查点
     */
    private String screenPoint;
    private Integer year;
    private Integer screenType;
    private String  padId;

}