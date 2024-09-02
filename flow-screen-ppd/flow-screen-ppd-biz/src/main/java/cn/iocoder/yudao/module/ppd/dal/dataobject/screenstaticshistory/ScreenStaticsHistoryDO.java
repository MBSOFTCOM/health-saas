package cn.iocoder.yudao.module.ppd.dal.dataobject.screenstaticshistory;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 工作进展报告-统计表-导出的历史选项 DO
 *
 * @author 福乐云
 */
@TableName("tb_screen_statics_history")
@KeySequence("tb_screen_statics_history_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreenStaticsHistoryDO extends BaseDO {

    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 机构id
     */
    private Long deptId;
    /**
     * 表格标题
     */
    private String tableTittle;
    /**
     * 学校名称
     */
    private String school;
    /**
     * 医院名称
     */
    private String hospital;
    /**
     * 行政区划名称
     */
    private String district;
    /**
     * 联系人姓名
     */
    private String contact;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 注射人姓名
     */
    private String injectionPeople;
    /**
     * 查验人姓名
     */
    private String checkPeople;
    /**
     * 基本信息勾选，如'[1,2,3,4]'
     */
    private String infoList;

}