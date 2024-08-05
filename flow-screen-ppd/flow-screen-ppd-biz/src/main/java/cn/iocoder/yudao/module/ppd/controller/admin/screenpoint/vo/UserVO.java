package cn.iocoder.yudao.module.ppd.controller.admin.screenpoint.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserVO implements Serializable {

    private Long id;

    private String nickname;

    private String mobile;

    private String deptName;
}
