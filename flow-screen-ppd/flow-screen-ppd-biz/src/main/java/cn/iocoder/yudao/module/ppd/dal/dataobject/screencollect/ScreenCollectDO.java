package cn.iocoder.yudao.module.ppd.dal.dataobject.screencollect;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 采集 DO
 *
 * @author 芋道源码
 */
@TableName("tb_screen_collect")
@KeySequence("tb_screen_collect_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenCollectDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 筛查编号
     */
    private String screenId;
    /**
     * 同步时唯一编码
     */
    private Long syncId;
    /**
     * 医生签名
     */
    private String doctorSignature;
    /**
     * 筛查单位
     */
    private String screenAgency;
    /**
     * 筛查时间
     */
    private LocalDateTime screenTime;
    /**
     * 筛查次序
     */
    private Integer screenOrder;
    /**
     * 身份证
     */
    private String idNum;
    /**
     * 对应摸底表中id
     */
    private Long personId;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 2年内是否有与结核病患者的接触史 0-否, 1-是
     */
    private Integer contacted;
    /**
     * 填写问卷时学校名称
     */
    private String schoolName;
    /**
     * 填写问卷时班级
     */
    private String classroom;
    /**
     * 填写问卷时联系电话
     */
    private String tel;
    /**
     * 对应摸底表中患者姓名
     */
    private String name;
    /**
     * 结果（分新生筛查和其他）。	新生：1-咳嗽、咳痰不小于2周 2-痰中带血或咯血 3-反复发热2周以上 4-淋巴结肿大	其他：1-咳嗽、咳痰（超过一周）2-血痰或咯血 3-发热	4--胸痛 5-夜间盗汗 6-食欲不振 7-乏力 8-体重减轻（超过6斤 9-卡痕异常	
     */
    private String outcome;
    /**
     * 筛查点
     */
    private String screenPoint;
    /**
     * 工作年度
     */
    private Integer year;
    /**
     * 筛查类型
     */
    private Integer screenType;

    /**
     * 数据来源 0-非微信小程序 1-微信小程序;默认1
     */
    private Integer dataSources;

    /**
     * pad上的主键加上身份证号，作为唯一判断
     */
    private String  padId;

}