package cn.iocoder.yudao.module.ppd.controller.admin.screenpersonrealsituation.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author 侯卿
 */
@Data
public class ScreenPersonStatisticsReqVO implements Serializable {

    /**
     * 表格标题
     */
    private String tableTittle;
    /**
     * 是否勾选学校名称
     */
    private List<Integer> selectSchool;
    /**
     * 学校名称
     */
    private String school;
    /**
     * 是否勾选医院
     */
    private List<Integer> selectHospital;
    /**
     * 医院名称
     */
    private String hospital;
    /**
     * 是否勾选行政区划
     */
    private List<Integer> selectDistrict;
    /**
     * 行政区划名称
     */
    private String district;
    /**
     * 是否勾选联系人
     */
    private List<Integer> selectContact;
    /**
     * 联系人
     */
    private String contact;
    /**
     * 是否勾选联系电话
     */
    private List<Integer> selectContactPhone;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 是否勾选注射人
     */
    private List<Integer> selectInjectionPeople;
    /**
     * 注射人
     */
    private String injectionPeople;
    /**
     * 是否勾选查验人
     */
    private List<Integer> selectCheckPeople;
    /**
     * 查验人
     */
    private String checkPeople;
    /**
     * 基本信息勾选数据
     */
    private List<Integer> infoList;
    /**
     * PPD/EC检查勾选数据
     */
    private List<Integer> ppdList;
    /**
     * X光胸片勾选数据
     */
    private List<Integer> xRayList;
    /**
     * 最终结果勾选
     */
    private List<Integer> result;
    /**
     * 备注勾选
     */
    private List<Integer> remark;

}