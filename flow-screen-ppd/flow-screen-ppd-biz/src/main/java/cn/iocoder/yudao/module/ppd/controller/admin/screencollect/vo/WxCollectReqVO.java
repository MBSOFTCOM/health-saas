package cn.iocoder.yudao.module.ppd.controller.admin.screencollect.vo;

import cn.iocoder.yudao.module.ppd.utils.EnumValue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxCollectReqVO {
    /**
     * 学生id
     */
    @NotNull(message = "未提供学生的id")
    private Long studentId ;
    /**
     * 咳嗽、咳痰（两周以上）1：是  2：否
     */
    @NotNull(message = "未提供咳嗽症状")
    @EnumValue(intValues = {1,2} ,message = "咳嗽症状值错误或未提供")
    private Integer cough;
    /**
     * 血痰或咳血1：是  2：否
     */
    @NotNull(message = "未提供咳血症状")
    @EnumValue(intValues = {1,2} ,message = "咳血症状值错误或未提供")
    private Integer bloodySputum;
    /**
     * 夜间盗汗1：是  2：否
     */
    @NotNull(message = "未提供夜间盗汗症状")
    @EnumValue(intValues = {1,2} ,message = "夜间盗汗症状值错误或未提供")
    private Integer nightSweat;
    /**
     * 体重减轻（超过6斤）1：是  2：否
     */
    @NotNull(message = "未提供体重减轻症状")
    @EnumValue(intValues = {1,2} ,message = "体重减轻症状值错误或未提供")
    private Integer loseWeight;
    /**
     * 发热1：是  2：否
     */
    @NotNull(message = "未提供发热症状")
    @EnumValue(intValues = {1,2} ,message = "发热症状值错误或未提供")
    private Integer fever;
    /**
     * 食欲不振1：是  2：否
     */
    @NotNull(message = "未提供食欲不振症状")
    @EnumValue(intValues = {1,2} ,message = "食欲不振症状值错误或未提供")
    private Integer inappetence;
    /**
     * 胸痛1：是  2：否
     */
    @NotNull(message = "未提供胸痛症状")
    @EnumValue(intValues = {1,2} ,message = "胸痛症状值错误或未提供")
    private Integer chestPain;
    /**
     * 是否有肺结核密切接触史1：是  2：否
     */
    @NotNull(message = "未提供肺结核密切接触史")
    @EnumValue(intValues = {1,2} ,message = "肺结核密切接触史值错误或未提供")
    private Integer contactHistory;
}
