package com.shaoff.dig.basic.exp;

/**
 * Author: shaoff
 * Date: 2020/7/22 20:55
 * Package: exp
 * Description:
 * 1.try/catch块中有return语句，finally块依然得到执行，如
 * try{
 * a=1;
 * b=2;
 * return a;
 * }catch{
 * <p>
 * }finally{
 * a=3;
 * }
 * 未发生异常时，流程为
 * {code of try}
 * iload
 * istore
 * {code of finally}
 * iload
 * ireturn
 * <p>
 *
 * 2.异常的实现机制
 * 异常在编译后的字节码层面实现，编译后的方法中，finally块的字节码被分别增加到try和catch的字节码后面。
 * 方法的整体指令分为以下部分
 * 1.try+finally 后跟(return 或goto跳转）
 * 2.catch+finally 后跟(return 或goto跳转）
 * 3.未被捕获异常抛出的字节码
 * 4.finally块之后的代码
 */
public class ReturnInCatchFinally {
    /*finally虽然修改了变量a，但此时变量a的值已经保持到了特定本地变量表中，等待finally语句执行结束再进行return,
     * 如果finally块有return,最终返回finally的返回值*/
    static int f() {
        int a;
        try {
            int b = 1;
            a = b - 1;
            return a;
        } catch (Exception e) {
            int c = 2;
            return c;
        } finally {
            a = 10;
            return 10;
        }
    }


    public static void main(String[] args) {
        int res = f2();
        System.out.println(res);
    }

    /*观察字节码*/
    static int f2() {
        int x;
        try {
            x = 1;
        } catch (Exception e) {
            x = 2;
        } finally {
            x = -1;
        }
        int b = 3;
        x += b;
        return x;
    }
}
