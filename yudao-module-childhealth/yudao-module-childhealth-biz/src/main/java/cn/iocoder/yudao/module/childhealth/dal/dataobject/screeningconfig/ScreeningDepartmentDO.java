package cn.iocoder.yudao.module.childhealth.dal.dataobject.screeningconfig;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 筛查科室表 DO
 *
 * 对应表: screening_department
 * 模块: C.五健筛查配置
 * 创建日期: 2026-07-20
 */
@TableName("screening_department")
@KeySequence("screening_department_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningDepartmentDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 科室编码
     */
    private String deptCode;

    /**
     * 科室名称
     */
    private String deptName;

    /**
     * 科室类型 视力/口腔/骨骼/心理/体形/综合
     */
    private String deptType;

    /**
     * 绑定体检项目JSON
     */
    private String bindItemsJson;

    /**
     * 绑定筛查任务JSON
     */
    private String bindTasksJson;

    /**
     * 业务统计JSON
     */
    private String statisticsJson;

    /**
     * 负责人ID
     */
    private Long principalId;

    /**
     * 负责人姓名
     */
    private String principalName;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态 1启用 0停用
     */
    private Integer status;

}
