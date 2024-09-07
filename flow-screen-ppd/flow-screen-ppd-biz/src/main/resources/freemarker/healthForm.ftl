<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>体检表</title>

    <style>
      body {
        font-family: "宋体", SimSun, STSong, sans-serif;
      }
      table {
        table-layout: fixed; /* 设置表格宽度固定 */
        border-collapse: collapse;
        margin-top: 5px;
      }
      td,
      th {
        word-break: break-all; /* 处理长单词换行 */
        white-space: normal; /* 允许文本换行 */
        border: 1px solid black;
        padding: 8px;
        text-align: center;
        width: 85px;
      }
      .info {
        margin-top: 10px;
      }
      .content {
        width: 650px;
        padding: 10px;
      }
      .title {
        text-align: center;
        font-size: 20px;
        font-weight: 600;
      }
      .text {
        text-align: left;
        width: 632px;
      }
      .trs {
        transform: scale(1.5);
        width: 30px;
      }
      .text-left {
        text-align: left;
      }
    </style>
  </head>
  <body>
    <div class="content">
      <div class="main">
        <div class="title">体检表</div>
        <div class="info">
          <div>筛查编号：${screeningNumber}</div>
          <div>身份证号：${idNumber}</div>
          <div>姓名：${name}</div>
          <div>年龄：${age} 岁</div>
          <div>体检日期：${examinationDate}</div>
        </div>
        <table>
          <tr>
            <th colspan="8">人群分类(可多选)</th>
          </tr>
          <tr>
            <td colspan="2">活动性肺结核密切接触者</td>
            <td class="trs"><#if closeContactWithActivePulmonaryTB>√</#if><#if !(closeContactWithActivePulmonaryTB)>□</#if></td>
            <td rowspan="2">老年人</td>
            <td class="trs" rowspan="2"><#if elderly>√</#if><#if !(elderly)>□</#if></td>
            <td colspan="2">HIV/AIDS</td>
            <td class="trs"><#if HIVorAIDS>√</#if><#if !(HIVorAIDS)>□</#if></td>
          </tr>
          <tr>
            <td rowspan="4">在校师生</td>
            <td>0-5岁学生</td>
            <td class="trs"><#if student0To5Years>√</#if><#if !(student0To5Years)>□</#if></td>
            <td colspan="2">既往结核病患者</td>
            <td class="trs"><#if pastTBPatient>√</#if><#if !(pastTBPatient)>□</#if></td>
          </tr>
          <tr>
            <td>6-14岁学生</td>
            <td class="trs"><#if student6To14Years>√</#if><#if !(student6To14Years)>□</#if></td>
            <td rowspan="3">糖尿病患者</td>
            <td class="trs" rowspan="3"><#if diabetesPatient>√</#if><#if !(diabetesPatient)>□</#if></td>
            <td rowspan="3">非重点人群</td>
            <td>0-5岁</td>
            <td class="trs"><#if nonKeyPopulation0To5Years>√</#if><#if !(nonKeyPopulation0To5Years)>□</#if></td>
          </tr>
          <tr>
            <td>≥15岁学生</td>
            <td class="trs"><#if studentOver15Years>√</#if><#if !(studentOver15Years)>□</#if></td>
            <td>6-14岁</td>
            <td class="trs"><#if nonKeyPopulation6To14Years>√</#if><#if !(nonKeyPopulation6To14Years)>□</#if></td>
          </tr>
          <tr>
            <td>教职工</td>
            <td class="trs"><#if schoolStaff>√</#if><#if !(schoolStaff)>□</#if></td>
            <td>≥15岁</td>
            <td class="trs"><#if nonKeyPopulationOver15Years>√</#if><#if !(nonKeyPopulationOver15Years)>□</#if></td>
          </tr>
        </table>

        <table style="margin-top: 5px">
          <tr>
            <td class="text">
              <b>活动性肺结核密切接触者:</b>
              症状筛查+ppd+胸片检查,异常或强阳性进行实验室检查。<br />
              <b>0-5岁学生:</b>症状筛查,有症状做 ppd,强阳性进一步检查;
              <b>6-14岁学生:</b> 症状查+ppd,有症状或强 阳性进一步检查;
              <b>≥15岁学生:</b> 症状筛
              査+ppd+胸片检查,有症状或强阳性或异常进一步检查。<br />
              <b>教职工:</b> 症状筛查+胸片检查,有症状或异常进一步检查。<br />
              <b>老年人、糖尿病患者、HIV/AIDS和既往结核病患者:</b>
              症状筛査+胸片检查,有症状或异常进一步检查。<br />
              <b>0-5岁非重点人群:</b>症状筛查,有症状做ppd,强阳性进一步检查;
              <b>6-14岁非重点人群:</b> 症状筛查+ppd,有症状或强阳性进一步检查;
              <b>≥15岁非重点人群:</b> 症状筛查+胸片检查,有症状或异常进一步检查。
            </td>
          </tr>
        </table>
        <table style="margin-top: 5px">
          <tr>
            <th class="text" colspan="5">您最近1个月是否有一下症状?</th>
          </tr>
          <tr>
            <td class="text-left" colspan="2">1)咳嗽、咳痰(超过2周)</td>
            <td><#if coughOrSputumForMoreThanOneWeek>有</#if><#if !(coughOrSputumForMoreThanOneWeek)>无</#if></td>
            <td class="text-left">2)咳血或血痰</td>
            <td><#if hemoptysisOrBloodSputum>有</#if><#if !(hemoptysisOrBloodSputum)>无</#if></td>
          </tr>
          <tr>
            <td class="text-left" colspan="2">3)发热</td>
            <td><#if fever>有</#if><#if !(fever)>无</#if></td>
            <td class="text-left">4)胸痛</td>
            <td><#if chestPain>有</#if><#if !(chestPain)>无</#if></td>
          </tr>
          <tr>
            <td class="text-left" colspan="2">5)乏力、盗汗</td>
            <td><#if nightSweats>有</#if><#if !(nightSweats)>无</#if></td>
            <td class="text-left">6)食欲不振</td>
            <td><#if lossOfAppetite>有</#if><#if !(lossOfAppetite)>无</#if></td>
          </tr>
          <tr>
            <td class="text-left" colspan="2">7)体重减轻(超过6斤)</td>
            <td><#if weightLossOverSixPounds>有</#if><#if !(weightLossOverSixPounds)>无</#if></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td class="trs">√</td>
            <td><b>ppd</b></td>
            <td class="text-left" colspan="2">
              注射时间：${ppdInjectionTimeStr}<br />
              结果：
              <#if ppdOutcome == 1>
              阳性
              <#else>
              阴性
              </#if>
            </td>
            <td class="text-left">医生签名:<#if ppdDoctorSignature><img src="${ppdDoctorSignature}" width="100" height="50"/></#if></td>
          </tr>
          <tr>
            <td class="trs">√</td>
            <td><b>胸部X线</b></td>
            <td class="text-left" colspan="2">
              <div style="float: left; transform: scale(1.5)">
                <#if noTBRelatedAbnormalities>√</#if><#if !(noTBRelatedAbnormalities)>□</#if>
              </div>
              <div style="float: left; margin: 0 5px">无结核相关异常</div>
              <div style="float: left; transform: scale(1.5); margin: 0 5px">
                <#if suspectedTB>√</#if><#if !(suspectedTB)>□</#if>
              </div>
              <div style="float: left">疑似结核</div>
              <br />
              <div>机器中与患者对应的编码：${chestXRayCode}</div>
            </td>
            <td class="text-left">医生签名:<#if chestXRayDoctorSignature><img src="${chestXRayDoctorSignature}" width="100" height="50"/></#if></td>
          </tr>
        </table>
      </div>
    </div>
  </body>

</html>
