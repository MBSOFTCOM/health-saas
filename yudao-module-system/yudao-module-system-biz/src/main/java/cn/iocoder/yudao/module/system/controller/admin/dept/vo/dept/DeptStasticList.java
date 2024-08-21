package cn.iocoder.yudao.module.system.controller.admin.dept.vo.dept;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeptStasticList implements Serializable {

    /**
     * 学校列表
     */
    private List<DeptSimpleRespVO> schoolList;
    /**
     * 医疗列表
     */
    private List<DeptSimpleRespVO> hospitalList;


}
