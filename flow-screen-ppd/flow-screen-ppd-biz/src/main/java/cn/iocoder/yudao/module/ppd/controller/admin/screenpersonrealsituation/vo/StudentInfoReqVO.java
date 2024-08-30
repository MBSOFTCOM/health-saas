package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class StudentInfoReqVO {
    private Long id;
    /**
     * 学生姓名
     */
    private String name;
    /**
     * 学生手机号
     */
    private String phone;
    /**
     * 学生身份证
     */
    private String idcard;
    /**
     * 学生所在学校名称
     */
    private String schoolName;
    /**
     * 学生所在班级
     */
    private String className;
}
