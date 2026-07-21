package cn.iocoder.yudao.module.childhealth.service.external.adapter;

import cn.iocoder.yudao.module.childhealth.service.external.dto.ObstetricsHighRiskNewbornDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * 产科系统对接适配器
 *
 * 适用需求：
 * - 需求 34：产科高危儿/早产儿/低体重儿列表管理与未建册随访
 *
 * 真实实现需对接产科系统（数据库/接口），
 * 当前提供 Mock 实现用于开发期联调。
 */
public interface ObstetricsIntegrationAdapter {

    /**
     * 需求 34：查询产科出院的高危新生儿列表
     *
     * @param startDate 出院开始日期
     * @param endDate 出院结束日期
     * @return 高危新生儿列表
     */
    List<ObstetricsHighRiskNewbornDTO> fetchHighRiskNewbornList(LocalDate startDate, LocalDate endDate);

    /**
     * 需求 34：按高危类型查询（PRETERM/LOW_WEIGHT/ASPHYXIA/HYPERBILIRUBINEMIA/HIE/INHERITED_METABOLIC）
     *
     * @param highRiskType 高危类型
     * @param startDate 出院开始日期
     * @param endDate 出院结束日期
     * @return 高危新生儿列表
     */
    List<ObstetricsHighRiskNewbornDTO> fetchByHighRiskType(String highRiskType,
                                                            LocalDate startDate, LocalDate endDate);

    /**
     * 需求 34：根据母亲身份证号查询产科新生儿信息
     *
     * @param motherIdCard 母亲身份证号
     * @return 新生儿列表
     */
    List<ObstetricsHighRiskNewbornDTO> fetchByMotherIdCard(String motherIdCard);

    /**
     * 需求 34：根据产科新生儿ID查询详情
     *
     * @param obstetricsNewbornId 产科新生儿ID
     * @return 详情
     */
    ObstetricsHighRiskNewbornDTO fetchByObstetricsId(String obstetricsNewbornId);

    /**
     * 检查产科系统连接状态
     */
    boolean checkConnection();

}
