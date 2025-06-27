package cn.iocoder.yudao;

public class CommonTest {


    public static void main(String[] args) {
        // 修正后的正则表达式
        String p = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        String idnum = "120102201810256246"; // 这里可以替换为你要验证的身份证号码
        boolean matches = idnum.matches(p);
        System.out.println("身份证号码匹配结果: " + matches);
    }
}
