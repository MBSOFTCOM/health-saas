package cn.iocoder.yudao.module.childhealth.service.external.adapter;

import cn.iocoder.yudao.module.childhealth.service.external.dto.HisChildInfoDTO;
import cn.iocoder.yudao.module.childhealth.service.external.dto.HisNeonatalDiagnosisDTO;

import java.util.List;

/**
 * HIS 系统对接适配器
 *
 * 适用需求：
 * - 需求 2：HIS 自动建册
 * - 需求 5：自动抓取新生儿住院诊断数据预警
 *
 * 真实实现需对接医院 HIS 系统（HL7/HTTPS/数据库直连），
 * 当前提供 Mock 实现用于开发期联调。
 */
public interface HisIntegrationAdapter {

    /**
     * 需求 2：根据医保卡号或 HIS 患者ID 拉取儿童建档信息
     *
     * @param hisPatientId HIS 患者ID（可空）
     * @param medicareCardNo 医保卡号（可空）
     * @return HIS 儿童信息，未找到返回 null
     */
    HisChildInfoDTO fetchChildInfo(String hisPatientId, String medicareCardNo);

    /**
     * 需求 2：按母亲身份证号拉取关联的儿童列表（孕保分娩数据拉取）
     *
     * @param motherIdCard 母亲身份证号
     * @return 儿童信息列表
     */
    List<HisChildInfoDTO> fetchChildrenByMotherIdCard(String motherIdCard);

    /**
     * 需求 5：根据 HIS 患者ID 拉取新生儿住院诊断数据
     *
     * @param hisPatientId HIS 患者ID
     * @return 诊断列表
     */
    List<HisNeonatalDiagnosisDTO> fetchNeonatalDiagnoses(String hisPatientId);

    /**
     * 需求 5：根据母亲身份证号拉取所有新生儿住院诊断（用于建册时预警）
     *
     * @param motherIdCard 母亲身份证号
     * @return 诊断列表
     */
    List<HisNeonatalDiagnosisDTO> fetchNeonatalDiagnosesByMother(String motherIdCard);

    /**
     * 检查 HIS 系统连接状态
     *
     * @return 连接是否正常
     */
    boolean checkConnection();

}
