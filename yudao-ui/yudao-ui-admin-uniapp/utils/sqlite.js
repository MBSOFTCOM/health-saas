// 当前模拟账户
export const user = {
	id: 12345678900,
	name: "18623594687",
	nickName: "狗蛋",
	pwd: "abc123abc"
}
// 数据库名
export const dbName = 'tb_screen'
// 数据表名
export const tableNameList = [
	"tb_screen_ppd",
	"tb_screen_sputum_examination",
	"tb_screen_person",
	"tb_screen_sum",
	"tb_screen_electrocardiogram",
	"tb_screen_diagnosis",
	"tb_screen_collect",
	"tb_screen_experiment",
	"tb_screen_chest_radiograph",
	"tb_screen_images"
]
export const tbScreenPerson = "tb_screen_person"
export const tbScreenDistrict = "tb_screen_district"
//采集表
export const tbScreenCollect = "tb_screen_collect"
//ppd表
export const tbScreenPpd = "tb_screen_ppd"
// 试剂配置表
export const tbScreenReagent = "tb_screen_reagent"
// 试剂批次明细表
export const tbScreenConsume = "tb_screen_consume"
// 消耗量明细
export const tbScreenConsumeRecord = "tb_screen_consume_record"
// dr表名
export const tbScreenChestRadiograph = "tb_screen_chest_radiograph"
// ct组
export const tbScreenComputedTomography = "tb_screen_computed_tomography"
// 痰检表名
export const tbScreenSputumExamination = "tb_screen_sputum_examination"
// 实验室表
export const tbScreenExperiment = "tb_screen_experiment"
// 心电表
export const tbScreenElectrocardiogram = "tb_screen_electrocardiogram"
// 诊断组
export const tbScreenDiagnosis = "tb_screen_diagnosis"
// 工作队伍
export const tbScreenUser = "tb_screen_user"
// 汇总
export const tbScreenSum = "tb_screen_sum"
// 移动端各组离线图片信息表
export const tbScreenImages = "tb_screen_images"
export const errorKey="errorUpload"
//全局变量
//登录人员信息（离线）
uni.$person = {}
//筛查类型
uni.$screenType = null
//工作年度
// uni.$year = null
//登录人员信息 (在线)
uni.$user = {}

//登录状态。true为在线
uni.$loginStatus = true;

uni.$showFlow = false

//初始化的表
export const tableSqls = [
	{
		tableName: 'tb_screen_user',
		sql: `CREATE TABLE "tb_screen_user" (
				"id" INTEGER PRIMARY KEY AUTOINCREMENT ,
				"name" TEXT, -- 用户名
				"nickName" TEXT, -- 昵称
				"pwd" TEXT, --密码
				"groupName" TEXT, --分组
				"screenPoint" TEXT, -- 筛查点名称
				"agency" TEXT, -- 筛查单位
				"year"  INTEGER -- 工作年度
			  );`
	},
	{
		tableName: 'tb_screen_point',
		sql: `CREATE TABLE "tb_screen_point" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,
			"name"  TEXT,-- 筛查点名称
			"worker"  TEXT,-- 队长
			"creator"  TEXT,-- 创建者
			"updater"  TEXT,-- 更新者
			"updateTime"  INTEGER,-- 更新时间
			"deleted"  INTEGER,-- 是否删除
			"collectWorker"  TEXT,-- 采集组工作人员
			"ppdWorker"  TEXT,-- PDD组工作人员
			"drctWorker"  TEXT,-- DR/CT组工组人员
			"sputumWorker"  TEXT,-- 痰检组工作人员
			"experimentWorker"  TEXT,-- 实验组工作人员
			"electrocardiogramWorker"  TEXT,-- 心电图组工作人员
			"diagnosisWorker"  TEXT,-- 诊断组工作人员
			"screenDept"  TEXT,-- 筛查单位名称
			"year"  INTEGER,-- 工作年度
			"createTime" DATE --创建时间
			  );`
	},
	{
		tableName: 'tb_screen_collect',
		sql: `CREATE TABLE "tb_screen_collect" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,-- 主键
			"screenId" TEXT , -- 筛查编号(生成的编码)
			"syncId" INTEGER , -- 同步时唯一编码
			"personId" INTEGER , -- 对应摸底表中id
			"idNum" TEXT , -- 身份证号
			"age" INTEGER , -- 填写问卷时的年龄
			"schoolName" TEXT , -- 填写问卷时，学生所处学校
			"classroom" TEXT , -- 填写问卷时，学生所处班级
			"tel" TEXT , -- 填写问卷时，学生的联系人电话
			"outcome" TEXT , -- 结果症状
			"contacted" INTEGER , -- 2年内是否有与结核病患者的接触史
			"doctorSignature" TEXT , -- 医生签名
			"screenAgency" TEXT , -- 筛查单位
			"screenTime" TEXT , -- 筛查时间
			"screenOrder" INTEGER , -- 筛查次序
			"screenPoint" TEXT , -- 筛查点
			"creator" TEXT , -- 创建者
			"createTime" TEXT , -- 创建时间
			"updater" TEXT , -- 更新者
			"updateTime" TEXT , -- 更新时间
			"padId" TEXT , -- 平板数据标识
			"year" INTEGER , -- 年份
			"statusFlag" INTEGER, --状态标识（1-新增 2-修改）
			"screenType" TEXT  -- 筛查类型  1--常规、2--新生、3--应急
			);`
	},
	{
		tableName: 'tb_screen_ppd',
		sql: `CREATE TABLE "tb_screen_ppd" (
				"id" INTEGER PRIMARY KEY AUTOINCREMENT ,--主键
				"screenId" TEXT , -- 筛查编号(生成的编码)
				"syncId" INTEGER , -- 同步时唯一编码
				"personId" INTEGER , -- 对应摸底表中id
				"idNum" TEXT , -- 身份证号
				"reagentId" INTEGER , -- 试剂表中的id
				"reagentSpecsNum" INTEGER , -- 试剂的使用人份
				"transverseDiameter" INTEGER , -- 硬结的横径 单位mm
				"longitudinalDiameter" INTEGER , -- 硬结的纵径 单位mm
				"blushTransverseDiameter" INTEGER , -- 红晕的横径 单位mm
				"blushLongitudinalDiameter" INTEGER , -- 红晕的纵径 单位mm
				"bleb" INTEGER , -- 1-水泡 2-双圈 3-坏死 4-淋巴管炎 
				"injection" INTEGER , -- 是否注射(1-是 0-否)
				"injectionWay" INTEGER , -- 注射方式（根据字典）
				"outcome" INTEGER , -- 结果。皮肤反应类型 1-阳性 0-阴性
				"doctorSignature" TEXT , -- 医生签名
				"actualPhoto" TEXT , -- ppd注射实拍图
				"blushPhoto" TEXT , -- 红晕编辑图
				"scleromaPhoto" TEXT , -- 硬结编辑图
				"injectionAgency" TEXT , -- 注射单位
				"screenOrder" INTEGER , -- 筛查次序
				"screenTime" DATE , -- 筛查时间
				"creator" TEXT , -- 创建者
				"createTime" DATE , -- 创建时间
				"updater" TEXT , -- 更新者
				"updateTime" DATE , -- 更新时间
				"deleted" INTEGER , -- 是否删除
				"year" INTEGER , -- 年份
				"padId" TEXT , -- 平板数据标识
				"statusFlag" INTEGER, --状态标识（1-新增 2-修改）
				"screenType" INTEGER  -- 筛查类型  1--常规、2--新生、3--应急
			  );`
	},
	{
		tableName:tbScreenReagent,
		sql: `CREATE TABLE ${tbScreenReagent}(
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,--主键
			"deptId" INTEGER , --部门id
			"name" TEXT, -- 试剂名称 
			"type" INTEGER, -- 试剂类型 
			"reagentSpecsNum" INTEGER, -- 转换系数（人次） 
			"usable" INTEGER, -- 是否启用（0正常 1停用） 
			"titer" INTEGER, -- 效价 
			"potencyUnit" INTEGER, -- 效价单位（1：U，2：IU） 
			"specification" INTEGER, -- 规格 
			"specificationUnit" INTEGER, -- 规格单位（1：ml，2：g，3：mg） 
			"packageUnit" INTEGER, -- 包装单位（1：支，2：瓶，3：份） 
			"manufacturer" TEXT, -- 供应商 
			"threshold" INTEGER, -- 库存预警值（按试剂） 
			"creator" TEXT, -- 创建者 
			"createTime" DATE, -- 创建时间 
			"updater" TEXT, -- 更新者 
			"updateTime" DATE, -- 更新时间 
			"statusFlag" INTEGER, --状态标识（1-新增 2-修改）
			"deleted" INTEGER -- 是否删除 
		)`
	},
	{
		tableName: tbScreenConsume,
		sql:`CREATE TABLE "${tbScreenConsume}" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,--主键
			"reagentId" INTEGER, --试剂id
			"deptId" INTEGER , --部门id
			"reagentName" TEXT, --试剂名称
			"reagentType" INTEGER, --试剂类型
			"consumeOrder" INTEGER, --消耗序位
			"reagentSpecsNum" INTEGER, --转换系数（人次）
			"threshold" INTEGER, --库存预警值（按试剂）
			"usable" INTEGER, --是否启用（0正常 1停用）
			"bathNumber" TEXT, --批次号
			"currentNumber" INTEGER, --当前库存
			"inboundNumber" INTEGER, --入库量（按试剂）
			"indate" TEXT, --有效期
			"manufactureDate" INTEGER, --生产日期
			"creator" TEXT, --创建者
			"createTime" DATE, --创建时间
			"updater" TEXT, --更新者
			"updateTime" DATE, --更新时间
			"deleted" INTEGER --是否删除
			 )`
	},
	{
		tableName: tbScreenConsumeRecord,
		sql:`CREATE TABLE "${tbScreenConsumeRecord}" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,--主键
			"consumeId" INTEGER, --消耗管理表id
			"changeNumber" INTEGER, --变化量
			"type" INTEGER, --变化类型（1：筛查自动扣减，2：手动增加库存，3：手动减少库存，4：入库增加库存）
			"creator" TEXT, --创建者
			"createTime" DATE, --创建时间
			"updater" TEXT, --更新者
			"updateTime" DATE, --更新时间
			"deleted" INTEGER --是否删除
		)`
	},
	{
		tableName: 'tb_screen_chest_radiograph',
		sql: `CREATE TABLE "tb_screen_chest_radiograph" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT , --主键
			"screenId" TEXT, --筛查编号
			"syncId" INTEGER, --同步时唯一编码
			"personId" INTEGER, --对应摸底表中id
			"idNum" TEXT , -- 身份证号
			"chestRadiographCode" TEXT, --胸片编号
			"chestRadiograph" TEXT, --胸片
			"photoTime" INTEGER, --胸片采集时间
			"doctorSignature" TEXT, --医生签名
			"outcome" INTEGER, --结果。2-其他异常 1-疑似结核 0-无异常
			"screenOrder" INTEGER, --筛查次序
			"screenTime" INTEGER, --筛查时间
			"year" INTEGER, --年份
			"screenType" INTEGER, --筛查类型  1--常规、2--新生、3--应急
			"remark" TEXT, --其他异常说明
			"creator" TEXT, --创建者
			"createTime" INTEGER, --创建时间
			"updater" TEXT, --更新者
			"statusFlag" INTEGER, --状态标识（1-新增 2-修改）
			"padId" TEXT , -- 平板数据标识
			"updateTime" INTEGER --更新时间
			);`
	},
	{
		tableName:'tb_screen_computed_tomography',
		sql:`create table "tb_screen_computed_tomography"(
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,--主键
			"screenId" TEXT ,--筛查编号
			"syncId" INTEGER ,--同步时唯一编码
			"personId" INTEGER ,--对应摸底表中id
			"idNum" TEXT , -- 身份证号
			"computedTomographyCode" TEXT ,--胸片编号
			"computedTomography" TEXT ,--胸片
			"photoTime" INTEGER ,--胸片采集时间
			"doctorSignature" TEXT ,--医生签名
			"outcome" INTEGER ,--结果。2-其他异常 1-疑似结核 0-无异常
			"screenOrder" INTEGER ,--筛查次序
			"screenTime" INTEGER ,--筛查时间
			"year" INTEGER ,--年份
			"screenType" INTEGER ,--筛查类型  1--常规、2--新生、3--应急
			"remark" TEXT ,--其他异常说明
			"creator" TEXT ,--创建者
			"createTime" INTEGER ,--创建时间
			"updater" TEXT ,--更新者
			"updateTime" INTEGER --更新时间
		)`
	},
	{
		tableName: 'tb_screen_diagnosis',
		sql: `CREATE TABLE "tb_screen_diagnosis" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,
			"screenId" TEXT, --筛查编号
			"syncId" INTEGER , --同步时唯一编码
			"personId" INTEGER  , --对应摸底表中id
			"outcome" INTEGER  , --结果。1-利福平耐药 2-病原学阳性 3-病原学阴性 4-无病原学结果
			"doctorSignature" TEXT , --医生签名
			"treatmentProgram" TEXT , --治疗方案
			"report" INTEGER, --是否网报
			"preventiveTreatment" INTEGER, --符合潜伏治疗条件者是否进行预防性治疗 0-否 1-是
			"screenOrder" INTEGER,  --筛查次序
			"screenTime" DATE, --筛查时间
			"screenPoint" TEXT,  --筛查点
			"year" INTEGER ,--年份
			"screenType" INTEGER ,--筛查类型  1--常规、2--新生、3--应急
			"createTime" DATE --创建时间
			);`
	},
	{
		tableName: 'tb_screen_electrocardiogram',
		sql: `CREATE TABLE "tb_screen_electrocardiogram" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,
			"screenId" TEXT, --筛查编号
			"syncId" INTEGER , --同步时唯一编码
			"personId" INTEGER  , --对应摸底表中id
			"doctorSignature" TEXT , --医生签名图片路径（平板本地路径，保存在 /doc/..中）
			"electrocardiogram" TEXT, --心电图照片路径（平板本地路径，保存在 /doc/..中）
			"screenOrder" INTEGER,  --筛查次序
			"screenTime" DATE, --筛查时间
			"screenPoint" TEXT,  --筛查点
			"year" INTEGER ,--年份
			"screenType" INTEGER, --筛查类型  1--常规、2--新生、3--应急
			"createTime" DATE, --创建时间
			"remark" TEXT, --备注
			"testResult" TEXT --检测结果
			  );`
	},
	{
		tableName: 'tb_screen_sum',
		sql: `CREATE TABLE "tb_screen_sum" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT, -- 主键
			"year" INTEGER, -- 筛查年份
			"screenType" INTEGER, -- 筛查类型
			"screenId" TEXT, -- 筛查编号
			"idNum" TEXT , -- 身份证
			"personId" INTEGER, -- 对应摸底表中id
			"syncId" INTEGER, -- 同步时唯一编码
			"lastCollectTime" INTEGER, -- 最近一次采集时间
			"collectNum" INTEGER, -- 采集次数
			"lastPpdTime" INTEGER, -- 最近一次tst时间
			"ppdNum" INTEGER, -- tst次数
			"lastChestRadiographTime" INTEGER, -- 最近一次做胸片时间
			"chestRadiographNum" INTEGER, -- dr次数
			"lastComputedTomographyTime" INTEGER, -- 最近一次做ct的时间
			"computedTomographyNum" INTEGER, -- ct次数
			"lastSputumExaminationTime" INTEGER, -- 最近一次痰检时间
			"sputumExaminationNum" INTEGER, -- 痰检次数
			"lastExperimentTime" INTEGER, -- 最近一次实验组时间
			"experimentNum" INTEGER, -- 实验次数
			"lastElectrocardiogramTime" INTEGER, -- 最近一次心电图时间
			"electrocardiogramNum" INTEGER, -- 心电图次数
			"lastDiagnosisTime" INTEGER, -- 最近一次诊断时间
			"diagnosisNum" INTEGER, -- 诊断次数
			"curFinish" TEXT, -- 当前已完成的分组
			"collectId" INTEGER, -- 采集表id
			"ppdId" INTEGER, -- ppd表id
			"chestRadiographId" INTEGER, -- dr胸片表id
			"computedTomographyId" INTEGER, -- ct表id
			"sputumExaminationId" INTEGER, -- 痰检表id
			"experimentId" INTEGER, -- 实验组id
			"electrocardiogramId" INTEGER, -- 心电图表id
			"diagnosisId" INTEGER, -- 诊断表id
			"padId" TEXT , -- 平板数据标识
			"statusFlag" INTEGER, --状态标识（1-新增 2-修改）
			"createTime" DATE --创建时间
			  );`
	},
	{
		tableName: 'tb_screen_person',
		sql: `CREATE TABLE "tb_screen_person" (
			  "id" INTEGER PRIMARY KEY AUTOINCREMENT ,
			  "screenType" INTEGER, --筛查类型  1--常规、2--新生、3--应急
			  "year" INTEGER ,--年份
			  "syncId" INTEGER , --同步时唯一编码
			  "deptId" INTEGER , --部门id
			  "screenId" TEXT, --筛查编号
			  "idNum" TEXT  , --身份证号
			  "name" TEXT , --姓名
			  "age" INTEGER , --年龄
			  "tel" INTEGER , --联系电话
			  "sex" INTEGER , --性别
			  "height" INTEGER , --身高
			  "weight" INTEGER , --体重
			  "permanentAddress" TEXT , --户籍地址
			  "permanentAddressProvince" TEXT , --户籍地址-省
			  "permanentAddressCity" TEXT , --户籍地址-市
			  "permanentAddressCounty" TEXT , --户籍地址-县
			  "permanentAddressTown" TEXT , --户籍地址-乡镇
			  "address" TEXT , --现住地址
			  "province" TEXT , --现住地址-省
			  "city" TEXT , --现住地址-市
			  "county" TEXT , --现住地址-县
			  "town" TEXT , --现住地址-乡镇
			  "nation" INTEGER , --民族
			  "firstType" INTEGER , --第一人群分类
			  "moreType" INTEGER , --多人群分类
			  "schoolOrTemple" TEXT , --学校或寺庙
			  "classroom" TEXT , --班级
			  "isNew" INTEGER , --是否新增
			  "isScreened" INTEGER , --是否筛查
			  "isNewStudent" INTEGER, --是否新生
			  "screenPoint" TEXT , --筛查点
			  "remark" TEXT , --备注
			  "screenTime" DATE, --筛查时间
			  "updater" TEXT, --更新者
			  "updateTime" DATE, --修改时间
			  "creator" TEXT, --创建者
			  "statusFlag" INTEGER, --状态标识（1-新增 2-修改）
			  "createTime" DATE, --创建时间
			  "studentType" INTEGER, -- 学生分类
			  "screenStartTime" DATE, -- 计划筛查时间-开始
			  "screenEndTime" DATE, -- 计划筛查时间-结束
			  "guardianTel" TEXT  -- 监护人手机号
			  
	  );`
	},
	{
		tableName: 'tb_screen_sputum_examination',
		sql: `CREATE TABLE "tb_screen_sputum_examination" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,
			"screenId" TEXT, --筛查编号
			"syncId" INTEGER , --同步时唯一编码
			"personId" INTEGER  , --患者id
			"sputumExamination" INTEGER  , --是否痰检(0-未痰检，1-已经痰检)
			"atomization" INTEGER ,-- 是否雾化(0-否 1- 是)
			"type" INTEGER  , --痰标本类型，1-无痰 2-即时痰 3-发放晨痰 4-夜间痰盒
		  "doctorSignature" TEXT , --医生签名
		  "forthwithSputum" TEXT ,--即时痰照片
		  "forthwithSputumCode" TEXT ,--即时痰标本号
		  "forthwithSputumTime" TEXT ,--即时痰照片采集时间
		  "eveningSputum" TEXT ,--晨痰照片
		  "eveningSputumCode" TEXT ,--晨痰标本号
		  "eveningSputumTime" TEXT ,--晨痰照片采集时间
		  "morningSputum" TEXT ,--夜痰照片
		  "morningSputumCode" TEXT ,--夜痰标本号
		  "morningSputumTime" TEXT ,--夜痰照片采集时间
		  "outcome" TEXT  , --结果
    	  "screenOrder" INTEGER,  --筛查次序
		  "screenTime" DATE, --筛查时间
		  "createTime" DATE, --创建时间
		  "updateTime" DATE, --更新时间
		  "creator" TEXT, --创建者
		  "updater" TEXT, --更新者
		   "screenType" INTEGER, --筛查类型  1--常规、2--新生、3--应急
		  "year" INTEGER --工作年份
		);`
	},
	{
		tableName: 'tb_screen_experiment',
		sql: `CREATE TABLE "tb_screen_experiment" (
				"id" INTEGER PRIMARY KEY AUTOINCREMENT ,
				"screenId" TEXT, --筛查编号
				"syncId" INTEGER , --同步时唯一编码
				"personId" INTEGER  , --对应摸底表中id
				"result" INTEGER , --实验结果（1=痰涂片阳性、2=痰培养阳性、3=药敏耐药、4=分子生物学阳性、5=潜伏感染）
				"screenTime" DATE, --筛查时间
				"screenOrder" INTEGER,  --筛查次序
				"screenPoint" TEXT  , --筛查点
				"createTime" DATE, --创建时间
				"screenType" INTEGER, --筛查类型  1--常规、2--新生、3--应急
				"year" INTEGER --工作年份
			  );`
	},
	{
		tableName: 'tb_screen_images',
		sql: `CREATE TABLE "tb_screen_images" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,
			"syncId" INTEGER , --同步时唯一编码
			"screenId" TEXT, --筛查编号
			"personId" INTEGER  , --患者id
			"idNum" TEXT , -- 身份证号
			"type" INTEGER  , --图片来源，1-DR 2-CT 3-实验室-痰菌培养图 4-心电图 5-即时痰、6-晨痰、7-夜间痰、8-采集组医生签名、9-ppd组医生签名、10-dr医生签名、11-ct医生签名、12-痰检组医生签名、13-实验室组医生签名、14-诊断组医生签名、15-心电图组医生签名
			"path" TEXT , -- 图片路径（本地路径，保存在 /doc/..中）
			"url" TEXT, -- 网络图片路径（完整路径）
			"screenTime" DATE, --筛查时间
			"screenOrder" INTEGER,  --筛查次序
			"screenPoint" TEXT  , --筛查点
			"year" INTEGER, -- 年份
			"screenType" INTEGER, --筛查类型  1--常规、2--新生、3--应急
			"statusFlag" INTEGER, --状态标识（1-新增 2-修改）
			"createTime" DATE --创建时间
			);`
	},
	{
		tableName: 'tb_screen_district',
		sql: `CREATE TABLE "tb_screen_district" (
			"id" INTEGER PRIMARY KEY AUTOINCREMENT ,
			"code" TEXT,
			"level" TEXT,
			"name" TEXT,
			"parentCode" TEXT)`
	}
]

/**
 *根据
 * @param table 表名
 * @param personId 患者id
 * @param screenType 筛查类型
 */
export async function getMaxScreenOrder(table, personId,screenType) {
	if (!screenType){
		screenType=uni.$screenType
	}
	// console.log(uni.$screenType)
	let sql = `select ifnull(max(screenOrder),0 ) maxOrder from ${table} where personId=${personId} and screenType=${screenType}`
	let order = await promise(dbName, sql)
	// console.log("order[0].maxOrder=",order[0])
	if (!order[0].maxOrder || order[0].maxOrder == 'null' || (order[0].maxOrder + '').trim() == '') {
		return 0
	}
	return order[0].maxOrder
}

/**
 * 统计患者表记录数量
 * @param queryParam 查询参数
 * @returns {Promise<*>}
 */
export function countPatient(queryParam) {
	return count(dbName, tbScreenPerson, queryParam)
}
/**
 * 获取患者列表分页
 * @param pageNo 分页，页码
 * @param pageSize 分页，页条数
 * @param name 患者姓名
 * @param idNum 身份证号
 * @param firstType 第一人群分类
 * @param moreType 多人群分类
 * @param startTime 筛查开始时间
 * @param endTime 筛查结束时间
 * @param screenId 筛查编号
 * @param timeCol 筛查时间字段名
 */
export async function getPatientPage(pageNo, pageSize, name, idNum, firstType, moreType, startTime, endTime, screenId,
	timeCol) {
	// console.log("调用sql")
	let moreTypeNum = 0
	// console.log(moreType);
	moreType.forEach(i => {
		moreTypeNum += i
	})
	// console.log(moreTypeNum);
	// console.log("开始分页")
	let result = {
		total: 0,
		data: []
	}
	let sizeIndex = 0
	let querySql = `select p.*,c.curFinish from ${tbScreenPerson} p left join ${tbScreenSum} c on p.id=c.personId `
	let countSql =
		`select count(DISTINCT p.id) num from ${tbScreenPerson} p left join  ${tbScreenSum} c on c.personId =p.id `
	if (pageNo < 1 || pageSize < 1) {
		return new Promise((resolve, reject) => {
			reject("分页参数错误")
		});
	}
	sizeIndex = (pageNo - 1) * pageSize
	let queryWhere = ' '
	queryWhere = ` where p.screenType = ${uni.$screenType} and`
	if (name || idNum || (firstType && firstType.length > 0) || (moreType && moreType.length > 0) || screenId || (
			startTime && endTime)) {

		if (name) {
			queryWhere += ` name like '%${name}%' and `
		}
		if (idNum) {
			queryWhere += ` p.idNum like '%${idNum}%' and `
		}
		if ((firstType && firstType.length > 0) || (moreType && moreType.length > 0)) {
			queryWhere += '('
			if (firstType && firstType.length > 0) {
				queryWhere += ` firstType in (${firstType}) and `
			}
			if (moreType && moreType.length > 0) {
				queryWhere += ` (moreType & ${moreTypeNum}) > 0 and `
			}
			if (queryWhere.includes('and  (moreType')) {
				queryWhere = queryWhere.replace('and  (moreType', 'or  (moreType')
			}
			if (queryWhere.trim().endsWith('and')) {
				// console.log("进入判断")
				queryWhere = queryWhere.trim()
				queryWhere = queryWhere.replace(/\band\b$/, ') and ')
				// console.log("querySql=",querySql+queryWhere)
			}
		}
		if (screenId) {
			queryWhere += ` p.screenId like '%${screenId}%' and `
		}
		if ((startTime && endTime)) {
			queryWhere += ` date(c.${timeCol}) between date('${startTime}') and  date('${endTime}') and `
		}
		queryWhere = queryWhere.trim()
		if (queryWhere.endsWith('and')) {
			let lastIndexOf = queryWhere.lastIndexOf('and');
			queryWhere = queryWhere.replace(/\band\b$/, "")

		}
	}
	queryWhere = queryWhere.trim()
	if (queryWhere.endsWith('and')) {
		let lastIndexOf = queryWhere.lastIndexOf('and');
		queryWhere = queryWhere.replace(/\band\b$/, "")
	}
	// 统计total数量
	countSql += queryWhere
	// console.log("count===="+countSql)
	let total = await promise(dbName, countSql)
	result.total = total[0].num
	// console.log("total="+total)

	// 数据查询
	queryWhere += ` order by id desc`
	queryWhere += ` limit ${sizeIndex},${pageSize}`
	querySql += queryWhere

	// console.log("query=" + querySql)
	let data = await promise(dbName, querySql)
	result.data = data

	return result
}

/**
 * 获取心电图首页统计数据
 * 
 */
export async function getElectrocardiogramDataView() {
	
	const now = new Date();
	const currentYear = now.getFullYear();
	
	let result = {
		todayCount: 0,
		currentMonthCount: 0,
		currentYearCount: 0
	};

	let querySql = `SELECT COUNT(DISTINCT e.personId) AS today_count
					FROM ${tbScreenElectrocardiogram} e
					WHERE DATE(e.screenTime) = DATE('now') and screenType = ${uni.$screenType} and year = ${currentYear};`
	let today_count = await promise(dbName, querySql);
	querySql = `SELECT COUNT(DISTINCT e.personId) AS current_month_count
				FROM ${tbScreenElectrocardiogram} e
				WHERE strftime('%m', e.screenTime) = strftime('%m', 'now')
				AND strftime('%Y', e.screenTime) = strftime('%Y', 'now') and screenType = ${uni.$screenType} and year = ${currentYear};`
	let current_month_count = await promise(dbName, querySql);
	querySql = `SELECT COUNT(DISTINCT e.personId) AS current_year_count
				FROM ${tbScreenElectrocardiogram} e
				WHERE strftime('%Y', e.screenTime) = strftime('%Y', 'now') and screenType = ${uni.$screenType} and year = ${currentYear};`
	let current_year_count = await promise(dbName, querySql);
	querySql = `SELECT COUNT(*) AS total,
				  SUM(CASE WHEN testResult = '正常心电图' THEN 1 ELSE 0 END) AS normal_ecg_count,
				  SUM(CASE WHEN testResult = '其他' THEN 1 ELSE 0 END) AS other_ecg_count,
				  SUM(CASE WHEN testResult = '窦性心律' THEN 1 ELSE 0 END) AS sinus_rhythm_count
				FROM (
				  SELECT
					personId,
					testResult,
					screenTime,
					(SELECT MAX(screenTime) FROM ${tbScreenElectrocardiogram} WHERE personId = t.personId) AS latest_screenTime
				  FROM ${tbScreenElectrocardiogram} t
				  where screenType = ${uni.$screenType} and year = ${currentYear}
				) subquery
				WHERE screenTime = latest_screenTime;`
	let TestResultData = await promise(dbName, querySql);

	result.TestResultData = TestResultData;
	result.todayCount = today_count;
	result.currentMonthCount = current_month_count;
	result.currentYearCount = current_year_count;

	return result;
}

/**
 * sqlite事务
 * @param option
 * @returns {Promise<unknown>}
 */
export function transactionOp(option){
	return new Promise((resolve,reject)=>{
        plus.sqlite.transaction({
            name: dbName,
            operation: option,
            success: function(e){
                // console.log('transaction success!');
				resolve(200)
			},
            fail: function(e){
                console.log('transaction failed: '+JSON.stringify(e));
				reject(e)
            }
        });
	})
}

/**
 * sqlite事务类型
 * @type {{COMMIT: string, BEGIN: string, ROLLBACK: string}}
 */
const transactionOption={
	BEGIN:'begin',
	COMMIT:'commit',
	ROLLBACK:'rollback',
}

/**
 * 开启事务
 * @returns {Promise<*>}
 */
export function openTransaction(){
	return transactionOp(transactionOption.BEGIN)
}
/**
 * 提交事务
 * @returns {Promise<*>}
 */
export function commitTransaction(){
	return transactionOp(transactionOption.COMMIT)
}
/**
 * 事务回滚
 * @returns {Promise<*>}
 */
export function rollbackTransaction(){
	return transactionOp(transactionOption.ROLLBACK)
}
/**
 * sqlite函数
 * @param dataBase 数据库名，非表名
 * @param sql 用于执行的sql
 * @returns {Promise<unknown>}
 */
export async function promise(dataBase, sql) {
	return await new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: dataBase,
			// sql: "select * from userInfo limit 3 offset 3",
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				reject(e);
				throw new Error(e)
				console.log("promise函数", e);
			}
		})
	})
}
/**
 * 清空表数据
 * @param tabName {string}
 */
export const emptyData = (tabName) => {
	let sql=`delete from ${tabName}`
	// console.log(sql);
	return promise(dbName,sql)
}
/**
 * 统计表记录数量，适用于全是and 连接的精准查询，不支持模糊
 * @param datebase 库名
 * @param table 表名
 * @param queryParam 查询参数
 * @returns {Promise<unknown>}
 */
export function count(datebase, table, queryParam) {
	// console.log("开始统计")
	let queryWhere = ''
	let sql = ""
	if (JSON.stringify(queryParam) !== '{}') {
		let dataKeys = Object.keys(queryParam)
		dataKeys.forEach((item, index) => {
			queryWhere += (
				`${item}=${JSON.stringify(queryParam[item])}${dataKeys.length - 1 !== index ? " and " : ""}`
			)
		})
		sql = `select count(*) from ${table} where ${queryWhere}`
		/*if (pageParam.pageNo && pageParam.pageSize){
			if (pageParam.pageNo>=1 && pageParam.pageSize>0){
				let index=(pageParam.pageNo-1)*pageParam.pageSize
				sql+=`limit ${queryParam.pageNo},${index}`
			}else {
				return new Promise((resolve, reject) => {
					reject("分页参数错误")
				});
			}
		}*/
	} else {
		sql = `select count(*) from ${table}`
	}
	// console.log("sql="+sql)
	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: datebase,
			// sql: "select * from userInfo limit 3 offset 3",
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				reject(e);
			}
		})
	})
}

/**
 * 多人群分类处理
 * @param decimal 分类值
 * @returns {*[]} 分类列表
 */
export function splitDecimalIntoList(decimal) {
	let result = [];
	let power = 0;

	while (decimal > 0) {
		if ((decimal & 1) === 1) {
			result.push(Math.pow(2, power));
		}
		decimal >>= 1;
		power++;
	}

	return result;
}

/**
 * 根据id主键查询记录
 * @param dataBase 库
 * @param table 表
 * @param id 主键id
 */
export async function getById(dataBase, table, id) {
	let sql = `select * from ${table} where id=${id}`
	let result = await promise(dataBase, sql)
	return result
}

//===============患者表接口
//患者修改
export function updatePerson(setData, id) {
	let setStr = '';
	let sql = '';
	if (JSON.stringify(setData) !== '{}') {
		let dataKeys = Object.keys(setData);
		dataKeys.forEach((item, index) => {
			// console.log(setData[item]);
			setStr += `${item}=${JSON.stringify(setData[item])}${dataKeys.length - 1 !== index ? "," : ""}`;
		});
		sql = `UPDATE ${tbScreenPerson} SET ${setStr} WHERE id = ${id}`;
	} else {
		return Promise.reject("没有修改值！");
	}
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: 'tb_screen',
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				console.log(e);
				reject(e);
			}
		});
	});
}


//根据身份证，筛查编号查看是否有重复数据；修改时，先排除自身
export function repeatCheck(idNum, screenId, id) {
	let sql = '';

	sql =
		`SELECT COUNT(*) count FROM ${tbScreenPerson} WHERE screenType = ${uni.$screenType} and idNum= '${idNum}' AND year =${uni.$person.year} AND screenType =${uni.$screenType}`

	if (id != null) {
		sql = sql + ` AND id !=${id}`
	}


	// sql = `SELECT COUNT(*) count FROM ${tbScreenPerson} WHERE id!=${id}`

	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: 'tb_screen',
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				// console.log(e);
				reject(e);
			}
		});
	});
}

//=====================汇总表接口

//根据患者id查询其对应采集组的最近一次时间和次数
export function getGather(id,year,screenType) {
	let setStr = '';
	let sql = '';

	sql = `SELECT * FROM ${tbScreenSum}  WHERE personId = ${id} AND screenType=${screenType} AND year=${year}`;
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: 'tb_screen',
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				console.log(e);
				reject(e);
			}
		});
	});
}

// 心电图表
export function getElectrocardiogram() {
	let setStr = '';
	let sql = '';

	sql = `SELECT * FROM ${tbScreenElectrocardiogram} WHERE screenType = ${uni.$screenType}`;

	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: 'tb_screen',
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				console.log(e);
				reject(e);
			}
		});
	});
}


//========================采集表接口

//查询采集组数据
export function getCollect() {
	let setStr = '';
	let sql = '';

	sql = `SELECT * FROM ${tbScreenCollect} WHERE screenType = ${uni.$screenType} AND year=${uni.$person.year}`;

	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: 'tb_screen',
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				console.log(e);
				reject(e);
			}
		});
	});
}

//根据摸底人员id查询其采集组数据
export function getCollectByPatientId(personId) {
	let setStr = '';
	let sql = '';

	sql = `SELECT * FROM ${tbScreenCollect} WHERE personId =${personId} AND year=${uni.$person.year} AND screenType=${uni.$screenType}`;

	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: 'tb_screen',
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				console.log(e);
				reject(e);
			}
		});
	});
}

//根据患者id次人的采集组最近的记录id
export function getCollectToId(id) {
	let setStr = '';
	let sql = '';

	sql = `SELECT id FROM ${tbScreenCollect} WHERE personId= ${id} AND year=${uni.$person.year} AND screenType=${uni.$screenType} ORDER BY screenTime DESC LIMIT 1`;
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: 'tb_screen',
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				reject(e);
			}
		});
	});
}


//根据患者id和筛查次序查询采集数据
export function getCollectOen(id, order,year,screenType) {
	let setStr = '';
	let sql = '';

	sql = `SELECT * FROM ${tbScreenCollect} WHERE personId= ${id} AND screenOrder= ${order} AND year = ${year} AND screenType=${screenType}`;
	// console.log("SQL:" + sql);

	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: 'tb_screen',
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				console.log(e);
				reject(e);
				throw new Error(e)
			}
		});
	});
}

//根据患者id和筛查次序修改采集数据
export function updateCollect(setData, id, order,year,screenType) {
	let setStr = '';
	let sql = '';

	if (JSON.stringify(setData) !== '{}') {
		let dataKeys = Object.keys(setData)
		dataKeys.forEach((item, index) => {
			// console.log(setData[item])
			setStr += (
				`${item}=${JSON.stringify(setData[item])}${dataKeys.length - 1 !== index ? "," : ""}`
			)
		})
		sql = `UPDATE ${tbScreenCollect} SET ${setStr}  WHERE personId= ${id} AND screenOrder= ${order} AND year=${year} AND screenType = ${screenType}`;
	}

	// console.log("SQL:" + sql);
	if (sql != '') {
		
		return new Promise((resolve, reject) => {
			plus.sqlite.selectSql({
				name: 'tb_screen',
				sql: sql,
				success(e) {
					
					resolve(e);
				},
				fail(e) {
					console.log(e);
					reject(e);
				}
			});
		});
	}
	console.log("无修改");
}

//获取各分类人数
export function statisticsNum(time, setData) {
	let setStr = '';
	let sql = '';

	if (JSON.stringify(setData) !== '{}') {
		let dataKeys = Object.keys(setData)
		dataKeys.forEach((item, index) => {
			// console.log(setData[item])
			setStr += (
				`${item}=${JSON.stringify(setData[item])}${dataKeys.length - 1 !== index ? " and " : ""}`
			)
		})
		sql = `SELECT COUNT(CASE WHEN strftime('%Y', screenTime) = strftime('%Y', '${time}') THEN 1 END) AS yearNum,
					COUNT(CASE WHEN strftime('%Y-%m', screenTime) = strftime('%Y-%m', '${time}') THEN 1 END) AS monthlyNum,
					COUNT(CASE WHEN strftime('%Y-%m-%d', screenTime) = strftime('%Y-%m-%d', '${time}') THEN 1 END) AS dayNum
					COUNT(CASE WHEN a.contacted = 0 THEN 1 END) AS noContacted,
				    COUNT(CASE WHEN a.contacted = 1 THEN 1 END) AS contacted,
					COUNT(CASE WHEN a.outcome GLOB '*[1-7]*' THEN 1 END) AS symptom,
					COUNT(CASE WHEN a.outcome NOT GLOB '*[1-7]*' THEN 1 END) AS noSymptom
			   FROM (SELECT MAX(screenTime) AS screenTime,outcome,contacted FROM ${tbScreenCollect} GROUP BY screenId) AS a
			   WHERE ${setData}
			   GROUP BY screenId`;
	} else {
		sql =
			`SELECT COUNT(CASE WHEN strftime('%Y', screenTime) = strftime('%Y', '${time}') THEN 1 END) AS yearNum,
					  COUNT(CASE WHEN strftime('%Y', screenTime) = strftime('%Y', '${time}')  AND (a.outcome GLOB '*[1-7]*') THEN 1 END) AS yearAbnormalNum,
					  COUNT(CASE WHEN strftime('%Y-%m', screenTime) = strftime('%Y-%m', '${time}')  THEN 1 END) AS monthlyNum,
					  COUNT(CASE WHEN strftime('%Y-%m', screenTime) = strftime('%Y-%m', '${time}') AND (a.outcome GLOB '*[1-7]*') THEN 1 END) AS monthlyAbnormalNum,
					  COUNT(CASE WHEN strftime('%Y-%m-%d', screenTime) = strftime('%Y-%m-%d', '${time}') THEN 1 END) AS dayNum,
					  COUNT(CASE WHEN strftime('%Y-%m-%d', screenTime) = strftime('%Y-%m-%d', '${time}') AND (a.outcome GLOB '*[1-7]*') THEN 1 END ) AS dayAbnormalNum,
					  COUNT(CASE WHEN a.contacted = 0 THEN 1 END) AS noContacted,
					  COUNT(CASE WHEN a.contacted = 1 THEN 1 END) AS contacted,
					  COUNT(CASE WHEN a.outcome LIKE '%2%' THEN 1 END) AS hemoptysisNum,
					  COUNT(CASE WHEN a.outcome LIKE '%3%' THEN 1 END) AS nightSweatNum,
					  COUNT(CASE WHEN a.outcome LIKE '%4%' THEN 1 END) AS loseWeight,
					  COUNT(CASE WHEN a.outcome LIKE '%5%' THEN 1 END) AS feverNum,
					  COUNT(CASE WHEN a.outcome LIKE '%6%' THEN 1 END) AS anorexiaNum,
					  COUNT(CASE WHEN a.outcome LIKE '%7%' THEN 1 END) AS chestPainNum
			   FROM (SELECT MAX(screenTime) AS screenTime,outcome,contacted FROM ${tbScreenCollect} WHERE screenType = ${uni.$screenType} AND year=${uni.$person.year} GROUP BY screenId) AS a;`;


		/* 		sql = `SELECT COUNT(CASE WHEN strftime('%Y-%m-%d', screenTime) = strftime('%Y-%m-%d', '${time}') AND (a.outcome NOT LIKE '%9%') THEN 1 END ) AS dayAbnormalNum
                        FROM (SELECT MAX(screenTime) AS screenTime,outcome FROM ${tbScreenCollect} GROUP BY screenId) AS a;`	 */
	}

	// console.log("SQL:" + sql);
	if (sql != '') {
		return new Promise((resolve, reject) => {
			plus.sqlite.selectSql({
				name: 'tb_screen',
				sql: sql,
				success(e) {
					resolve(e);
				},
				fail(e) {
					console.log(e);
					reject(e);
				}
			});
		});
	}
}


//初始化几条数据
export function inList() {
	let sql = ` INSERT INTO tb_screen_collect (screenId,personId,outcome,screenOrder,screenTime)
				VALUES(61616100001,1,'12',1,'2024-01-01'),(61616100001,1,'12',2,'2024-01-02'),
					  (61616100002,2,'12',1,'2024-01-01'),(61616100002,1,'12',2,'2024-01-02'),
					  (61616100003,3,'12',1,'2024-01-01'),(61616100003,1,'12',2,'2024-01-02'),
					  (61616100004,4,'12',1,'2024-01-01'),(61616100004,1,'12',2,'2024-01-02') `
	console.log("sql", sql);
	return new Promise((resolve, reject) => {
		plus.sqlite.selectSql({
			name: 'tb_screen',
			sql: sql,
			success(e) {
				resolve(e);
			},
			fail(e) {
				console.log(e);
				reject(e);
			}
		});
	});
}