package cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo;

import lombok.Data;

@Data
public class DeptVO  {
    /**
     * 部门ID
     */
    private Long id;
    /**
     * 父部门ID
     *
     * 关联 {@link #id}
     */
    private Long parentId;
    /**
     * 部门状态
     *
     */
    private Integer status;
    /**
     * 部门名称
     */
    private String name;
    private Integer type;

}
