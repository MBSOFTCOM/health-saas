
/**
 * 0~5岁学生筛查流程
 * @type {Map<string, string>}
 */
const studentFive=new Map([
    [null,"采集组"],
    ["采集组","ppd组"],
    ["ppd组","胸片组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 6~14岁学生筛查流程
 * @type {Map<string, string>}
 */
const studentFourteen=new Map([
    [null,["采集组","ppd组"]],
    ["采集组",["胸片组","痰检组"]],
    ["ppd组",["胸片组","痰检组"]],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 15岁以上学生筛查流程
 * @type {Map<string, string>}
 */
const studentFifteen=new Map([
    [null,["采集组","ppd组","胸片组"]],
    ["采集组","痰检组"],
    ["ppd组","痰检组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 教职工筛查流程
 * @type {Map<string, string>}
 */
const teacher=new Map([
    [null,["采集组","胸片组"]],
    ["采集组","痰检组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 0~5岁僧尼筛查流程
 * @type {Map<string, string>}
 */
const monkFive=new Map([
    [null,"采集组"],
    ["采集组","ppd组"],
    ["ppd组","胸片组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 6~14岁僧尼筛查流程
 * @type {Map<string, string>}
 */
const monkFourteen=new Map([
    [null,["采集组","ppd组"]],
    ["采集组",["胸片组","痰检组"]],
    ["ppd组",["胸片组","痰检组"]],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 15岁以上僧尼筛查流程
 * @type {Map<string, string>}
 */
const monkFifteen=new Map([
    [null,["采集组","ppd组","胸片组"]],
    ["采集组","痰检组"],
    ["ppd组","痰检组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 密接者筛查流程
 * @type {Map<string, string>}
 */
const closer=new Map([
    [null,["采集组","ppd组","胸片组"]],
    ["采集组","痰检组"],
    ["ppd组","痰检组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 老年人、糖尿病、HIV/AIDS、既往患结核病患者筛查流程
 * @type {Map<string, string>}
 */
const chronic=new Map([
    [null,["采集组","胸片组"]],
    ["采集组","痰检组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 0~5岁非重点人员筛查流程
 * @type {Map<string, string>}
 */
const normalFive=new Map([
    [null,["采集组","ppd组"]],
    ["采集组","胸片组"],
    ["ppd组","胸片组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 6~14岁非重点人员筛查流程
 * @type {Map<string, string>}
 */
const normalFourteen=new Map([
    [null,["采集组","ppd组","胸片组"]],
    ["采集组","痰检组"],
    ["ppd组","痰检组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 15岁以上非重点人员筛查流程
 * @type {Map<string, string>}
 */
const normalFifteen=new Map([
    [null,["采集组","胸片组"]],
    ["采集组","痰检组"],
    ["胸片组","痰检组"],
    ["痰检组","实验组"],
    ["实验组","诊断组"],
])
/**
 * 人群分类的流程
 * @type {Map<string, Map<string, string>>}
 */
export const personType=new Map([
    [0, {type:"0~5岁学生",value:studentFive}],
    [1,{type:"6~14岁学生",value:studentFourteen}],
    [2,{type:"15岁以上学生",value:studentFifteen}],
    [3,{type:"教职工",value:teacher}],
    [4,{type:"0~5岁僧尼",value:monkFive}],
    [5,{type:"6~14岁僧尼",value:monkFourteen}],
    [6,{type:"15岁以上僧尼",value:monkFifteen}],
    [7,{type:"密切接触者",value:closer}],
    [8,{type:"老年人 | 糖尿病 | HIV/AIDS | 既往患结核病患者",value:chronic}],
    [9,{type:"0~5岁非重点人员",value:normalFive}],
    [10,{type:"6~14岁非重点人员",value:normalFourteen}],
    [11,{type:"15岁以上非重点人员",value:normalFifteen}]
])
export {
    studentFive,
    studentFourteen,
    studentFifteen,
    teacher,
    monkFive,
    monkFourteen,
    monkFifteen,
    closer,
    chronic,
    normalFive,
    normalFourteen,
    normalFifteen
}