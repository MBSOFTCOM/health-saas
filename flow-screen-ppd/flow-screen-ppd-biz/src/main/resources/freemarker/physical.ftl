<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <meta name="divport" content="width=device-width, initial-scale=1.0" />
    <title>体检表</title>
.

    <style>
        body {
            background: #ffffff;
            font-family: "宋体", SimSun, STSong, sans-serif;
            font-size: 16px;
        }

        .physical_examination {
            display: flex;
            flex-direction: column;
        }

        .text_name {
            /* border: 1px solid red; */
            align-items: center;
            justify-content: center;
            text-align: center;
            font-size: 2vw;
        }

        .input_content {
            position: relative;
            padding: 0 5vh;
        }

        .info_idCard {
            position: absolute;
            margin: -3.5vh 0 0 10vw;
        }

        .info_name {
        }

        .info_age {
            margin: -3.5vh 0 0 10vw;
        }

        .info_date {
            margin: -3.5vh 0 0 20vw;
        }

        .check_signature {
            margin-top: 5vh;
            margin-bottom: 2vh;
            margin-left: 80%;
        }

        .symptom_input_content {
            border: 2px solid;
            margin: 1vh 5vh 0 5vh;
            height: 60vh;

            display: flex;
            flex-direction: column;

            .symptom_input_content_item_1{
                flex: 1;
            // border: 1px solid;
                border-bottom-style: solid;

                display: flex;

                .symptom_input_content_item_1_item4_1{
                    flex: 1;
                // border: 1px solid;
                    border-right-style: solid;
                    padding-left: 1vw;

                    display: flex;

                    .symptom_input_content_item_1_item4_1_1{
                        flex: 1;
                        border-right-style: solid;

                    }
                    .symptom_input_content_item_1_item4_1_3{
                        flex: 3;
                        border-right-style: solid;
                        padding-left: 1vw;
                    }
                }
                .symptom_input_content_item_1_item4_2{
                    flex: 2;
                // border: 1px solid;
                    border-right-style: solid;
                    padding-left: 1vw;
                }
            }
            .symptom_input_content_item_2{
                flex: 2;
            // border: 1px solid;

                display: flex;

                .symptom_input_content_item_1_item4_1{
                    flex: 1;
                // border: 1px solid;
                    border-right-style: solid;
                    padding-left: 1vw;

                    display: flex;

                    .symptom_input_content_item_1_item4_1_1{
                        flex: 1;
                        border-right-style: solid;

                    }
                    .symptom_input_content_item_1_item4_1_3{
                        flex: 3;
                        border-right-style: solid;
                        padding-left: 1vw;
                    }
                }

                .symptom_input_content_item_1_item4_1{
                    flex: 1;
                // border: 1px solid;
                    border-right-style: solid;
                    padding-left: 1vw;
                }
                .symptom_input_content_item_1_item4_2{
                    flex: 2;
                // border: 1px solid;
                    border-right-style: solid;
                    padding-left: 1vw;
                }
            }
        }

        .tips_content {
            border: 2px solid;
            margin: 1vh 5vh 0 5vh;
            height: 30vh;
            padding: 1vw;
        }

        .crowd_class_title{
            font-weight: 700;
        }
        .crowd_class {
            border: 2px solid ;
            display: flex;
            flex-direction: column;
            margin: 1vh 5vh 0 5vh;
            align-items: center;
            font-size: 1.5vw;
        }

        .crowd_class_content {
        // border: 1px solid ;
            margin: 0 5vh 0 5vh;
            height: 30vh;
            display: flex;

            .content_one{
                flex: 2;
                border: 2px solid;
                border-top-style: none;
                border-top-style: none;

                display: flex;
                flex-direction: column;

                .content_one_head{
                    flex: 1;
                // border: 1px solid;

                    display: flex;

                    .content_one_head_left{
                    // border: 1px solid;
                        border-bottom-style: solid;
                        border-right-style: solid;
                        flex: 4;
                        padding-left: 1vw;
                    }
                    .content_one_head_right{
                    // border: 1px solid;
                        border-bottom-style: solid;
                        flex: 1;
                        padding-left: 1vw;
                    }
                }
                .content_one_content{
                    flex: 4;

                    display: flex;

                    .content_one_content_item_1{
                        border-right-style: solid;
                        flex: 1;
                        padding-top: 8vh;
                        text-align: center;
                    }
                    .content_one_content_item_2{
                        border-right-style: solid;
                        flex: 2;

                        display: flex;
                        flex-direction: column;

                        .content_one_content_item_2_item_1{
                            flex: 1;
                            border-bottom-style: solid;
                            padding-left: 1vw;
                        }
                        .content_one_content_item_2_item_2{
                            flex: 1;
                            border-bottom-style: solid;
                            padding-left: 1vw;
                        }
                        .content_one_content_item_2_item_3{
                            flex: 1;
                            border-bottom-style: solid;
                            padding-left: 1vw;
                        }
                        .content_one_content_item_2_item_4{
                            flex: 1;
                            padding-left: 1vw;
                        }
                    }
                    .content_one_content_item_3{
                        flex: 1;

                        display: flex;
                        flex-direction: column;

                        .content_one_content_item_3_item_1{
                            flex: 1;
                            border-bottom-style: solid;
                            padding-left: 1vw;
                        }
                        .content_one_content_item_3_item_2{
                            flex: 1;
                            border-bottom-style: solid;
                            padding-left: 1vw;
                        }
                        .content_one_content_item_3_item_3{
                            flex: 1;
                            border-bottom-style: solid;
                            padding-left: 1vw;
                        }
                        .content_one_content_item_3_item_4{
                            flex: 1;
                            padding-left: 1vw;
                        }
                    }
                }
            }

            .content_tow{
                flex: 1;
                border-bottom-style: solid;
                border-right-style: solid;
                border-top-style: none;

                display: flex;
                flex-direction: column;

                .content_tow_item_column3_2{
                    flex: 2;
                    border-bottom-style: solid;
                    display: flex;

                    .content_tow_item_column3_item_1{
                        flex: 1;
                        border-right-style: solid;

                        padding-top: 5vh;
                        text-align: center;

                        display: flex;
                        flex-direction: column;


                    }
                    .content_tow_item_column3_item_1point5{
                        flex: 1.5;

                        display: flex;
                        flex-direction: column;

                        .content_tow_item_column3_item_1point5_item_col_1{
                            flex: 1;
                            border-bottom-style: solid;
                            border-right-style: solid;
                            padding-left: 1vw;
                        }

                        .content_tow_item_column3_item_1_item_col_1{
                            flex: 1;
                            border-bottom-style: solid;
                            padding-left: 1vw;
                        }
                    }
                }
                .content_tow_item_column3_1{
                    flex: 1;
                    border-bottom-style: solid;

                    display: flex;

                    .content_tow_item_column3_item_2point5{
                        flex: 2.5;
                        border-right-style: solid;
                        padding-top: 1vh;
                        padding-left: 1vw;
                    }
                    .content_tow_item_column3_item_1{
                        flex: 1;

                        padding-top: 1vh;
                        padding-left: 1vw;
                    }
                }
            }

            .content_three{
                flex: 1.5;
            // border: 1px solid ;
                border-right-style: solid;
                border-bottom-style: solid;

                display: flex;
                flex-direction: column;

                .content_three_item_1{
                    flex: 1;
                // border: 1px solid ;
                    border-bottom-style: solid;

                    display: flex;

                    .content_three_item_1_1{
                        flex: 1;
                    // border: 1px solid ;
                        border-left-style: solid;

                        padding-top: 1vh;
                        padding-left: 1vw;
                    }
                    .content_three_item_1_4{
                        flex: 4;
                    // border: 1px solid ;
                        padding-top: 1vh;
                        padding-left: 1vw;
                    }
                }
                .content_three_item_2{
                    flex: 2;
                // border: 1px solid ;

                    display: flex;

                    .content_three_item_2_1{
                        flex: 1;
                    // border: 1px solid ;
                        border-right-style: solid;

                        display: flex;
                        flex-direction: column;
                        .content_three_item_2_1_col_1{
                            flex: 1;
                        // border: 1px solid ;
                            border-bottom-style: solid;
                            padding-left: 1vw;
                        }
                    }
                    .content_three_item_2_0point5{
                        flex: 0.5;
                    // border: 1px solid ;

                        display: flex;
                        flex-direction: column;
                        .content_three_item_2_1_col_1{
                            flex: 1;
                        // border: 1px solid ;
                            border-bottom-style: solid;
                            padding-left: 1vw;
                        }
                    }
                }
            }
        }
    </style>
</head>
<body>
<div class="physical_examination">
    <div class="text_name">体检表</div>
    <div class="input_content">
        <div class="info_code">个人编号：001</div>
        <div class="info_idCard">身份证号：360302200009290617</div>
        <div class="info_name">姓名：刘华奇</div>
        <div class="info_age">年龄：23 岁</div>
        <div class="info_date">体检日期：2024年3月26日</div>
    </div>
    <div class="crowd_class">
        <div class="crowd_class_title">人群分类（可多选）</div>
    </div>
    <div class="crowd_class_content">
        <div class="content_one">
            <div class="content_one_head">
                <div class="content_one_head_left">
                    活动性肺结核密切接触者
                </div>
                <div class="content_one_head_right">
                    □
                </div>
            </div>

            <div class="content_one_content">
                <div class="content_one_content_item_1">
                    在校师生
                </div>
                <div class="content_one_content_item_2">
                    <div class="content_one_content_item_2_item_1">
                        0-5岁学生
                    </div>
                    <div class="content_one_content_item_2_item_2">
                        6-14岁学生
                    </div>
                    <div class="content_one_content_item_2_item_3">
                        ≥15岁学生
                    </div>
                    <div class="content_one_content_item_2_item_4">
                        教职工
                    </div>
                </div>
                <div class="content_one_content_item_3">
                    <div class="content_one_content_item_3_item_1">
                        □
                    </div>
                    <div class="content_one_content_item_3_item_2">
                        □
                    </div>
                    <div class="content_one_content_item_3_item_3">
                        □
                    </div>
                    <div class="content_one_content_item_3_item_4">
                        □
                    </div>
                </div>
            </div>
        </div>
        <div class="content_tow">
<#--            <div class="content_tow_item_column3_2">-->
<#--                <div class="content_tow_item_column3_item_1">-->
<#--                    僧尼-->
<#--                </div>-->
<#--                <div class="content_tow_item_column3_item_1point5">-->
<#--                    <div class="content_tow_item_column3_item_1point5_item_col_1">-->
<#--                        0-5岁-->
<#--                    </div>-->
<#--                    <div class="content_tow_item_column3_item_1point5_item_col_1">-->
<#--                        6-14岁-->
<#--                    </div>-->
<#--                    <div class="content_tow_item_column3_item_1point5_item_col_1" style="border-bottom-style: none;">-->
<#--                        ≥ 15岁-->
<#--                    </div>-->
<#--                </div>-->
<#--                <div class="content_tow_item_column3_item_1point5">-->
<#--                    <div class="content_tow_item_column3_item_1_item_col_1">-->
<#--                        □-->
<#--                    </div>-->
<#--                    <div class="content_tow_item_column3_item_1_item_col_1">-->
<#--                        □-->
<#--                    </div>-->
<#--                    <div class="content_tow_item_column3_item_1_item_col_1" style="border-bottom-style: none;">-->
<#--                        □-->
<#--                    </div>-->
<#--                </div>-->
<#--            </div>-->
            <div class="content_tow_item_column3_1">
                <div class="content_tow_item_column3_item_2point5">
                    老年人
                </div>
                <div class="content_tow_item_column3_item_1">
                    □
                </div>
            </div>
            <div class="content_tow_item_column3_1" style="border-bottom-style: none;">
                <div class="content_tow_item_column3_item_2point5">
                    糖尿病患者
                </div>
                <div class="content_tow_item_column3_item_1">
                    □
                </div>
            </div>
        </div>
        <div class="content_three">
            <div class="content_three_item_1">
                <div class="content_three_item_1_4">
                    HIV/AIDS
                </div>
                <div class="content_three_item_1_1">
                    □
                </div>
            </div>
            <div class="content_three_item_1">
                <div class="content_three_item_1_4">
                    既往结核病患者
                </div>
                <div class="content_three_item_1_1">
                    □
                </div>
            </div>
            <div class="content_three_item_2">
                <div class="content_three_item_2_1" style="padding-top: 5vh;padding-left: 1vw;">
                    非重点人群
                </div>
                <div class="content_three_item_2_1">
                    <div class="content_three_item_2_1_col_1">
                        0-5岁
                    </div>
                    <div class="content_three_item_2_1_col_1">
                        6-14岁
                    </div>
                    <div class="content_three_item_2_1_col_1" style="border-bottom-style: none;">
                        ≥15岁
                    </div>
                </div>
                <div class="content_three_item_2_0point5">
                    <div class="content_three_item_2_1_col_1">
                        □
                    </div>
                    <div class="content_three_item_2_1_col_1">
                        □
                    </div>
                    <div class="content_three_item_2_1_col_1" style="border-bottom-style: none;">
                        □
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="tips_content">
        <strong>活动性肺结核密切接触者：</strong> 症状筛查+TST+胸片检查，异常或强阳性进行实验室检查。
        <br/>
        <strong>0-5岁学生：</strong> 症状筛查+查验卡痕，有症状做 TST，强阳性进一步检查；<strong>6-14 岁学生：</strong>症状筛查+TST+查验卡痕，有症状或强
        阳性进一步检查；<strong>≥15 岁学生：</strong>症状筛查+TST+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查；<strong>教职工：</strong>症状筛
        查+胸片检查，有症状或异常进一步检查。
        <br/>
        <#--<strong>僧尼：</strong>0-5 岁、6-14 岁同学生；≥15 岁症状筛查+查验卡痕+胸片检查，有症状或强阳性或异常进一步检查。
        <br/>-->
        <strong>老年人、糖尿病患者、HIV/AIDS 和既往结核病患者：</strong>症状筛查+胸片检查，有症状或异常进一步检查。
        <br/>
        <strong>0-5 岁非重点人群：</strong>症状筛查+查验卡痕，有症状做 TST，强阳性进一步检查；<strong>6-14 岁非重点人群：</strong>症状筛查+TST+查验卡痕，
        有症状或强阳性进一步检查；<strong>≥15 岁非重点人群：</strong>症状筛查+胸片检查，有症状或异常进一步检查。
    </div>
    <div class="symptom_input_content">
        <div class="symptom_input_content_item_1">
            <strong style="padding: 2vh;font-size: 1.5vw;">您最近 1 个月内是否有以下症状？</strong>
        </div>
        <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1">
                1）咳嗽、咳痰（超过 1 周）
            </div>
            <div class="symptom_input_content_item_1_item4_1">
                有 无
            </div>
            <div class="symptom_input_content_item_1_item4_1">
                5）夜间盗汗
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                有 无
            </div>
        </div>
        <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1">
                2）咯血或血痰
            </div>
            <div class="symptom_input_content_item_1_item4_1">
                有 无
            </div>
            <div class="symptom_input_content_item_1_item4_1">
                6）食欲不振
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                有 无
            </div>
        </div>
        <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1">
                3）发热
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                有 无
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-left-style: solid;">
                7）乏力
            </div>
            <div class="symptom_input_content_item_1_item4_1" style=" border-right-style: none;">
                有 无
            </div>
        </div>
        <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1">
                4）胸痛
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                有 无
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-left-style: solid;">
                8）体重减轻（超过 6 斤）
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                有 无
            </div>
        </div>
        <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1">
                <div class="symptom_input_content_item_1_item4_1_1">
                    □
                </div>
                <div class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none;">
                    <strong>查验卡痕</strong>
                </div>
            </div>
            <div class="symptom_input_content_item_1_item4_2">
                □ 有 □无 □无法判断
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                医生签字：
            </div>
        </div>
        <div class="symptom_input_content_item_1">
            <div class="symptom_input_content_item_1_item4_1">
                <div class="symptom_input_content_item_1_item4_1_1">
                    □
                </div>
                <div class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none;">
                    <strong>TST</strong>
                </div>
            </div>
            <div class="symptom_input_content_item_1_item4_2">
                注射时间： 年 月 日 时（24 小时制）
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                医生签字：
            </div>
        </div>
        <div class="symptom_input_content_item_2">
            <div class="symptom_input_content_item_1_item4_1">
                <div class="symptom_input_content_item_1_item4_1_1">
                    □
                </div>
                <div class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none;">
                    <strong>胸部 X 线</strong>
                </div>
            </div>
            <div class="symptom_input_content_item_1_item4_2">
                □无结核相关异常 □疑似结核
                <br/>
                <div style="padding-top: 2vh;">
                    机器中与患者对应的编码：
                </div>
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                医生签字：
            </div>
        </div>
        <div class="symptom_input_content_item_1" style="border-top-style: solid; border-bottom-style: none;">
            <div class="symptom_input_content_item_1_item4_1">
                <div class="symptom_input_content_item_1_item4_1_1">
                    □
                </div>
                <div class="symptom_input_content_item_1_item4_1_3" style="border-right-style: none;">
                    <strong>痰标本</strong>
                </div>
            </div>
            <div class="symptom_input_content_item_1_item4_2">
                □即时痰 □发放晨痰、夜间痰盒 □无痰
            </div>
            <div class="symptom_input_content_item_1_item4_1" style="border-right-style: none;">
                医生签字：
            </div>
        </div>
    </div>
    <div class="check_signature">质检人员签字:</div>
</div>

</body>
</html>