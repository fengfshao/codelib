package com.shaoff.dig.exp;

/**
 * Author: shaoff
 * Date: 2020/12/4 07:24
 * Package: exp
 * Description:
 *
 * https://dzone.com/articles/java-classnotfoundexception-vs-noclassdeffounderro#:~:text=ClassNotFoundException%20is%20an%20exception%20that,was%20missing%20at%20run%20time.
 */

class UserClass{
    static {
        System.out.println("initing");
        int a=1/0;
    }
}
public class NoClassDef {

    public static void main(String[] args) throws Exception {
//        Thread.currentThread().getContextClassLoader().loadClass("com.fengfshao.javal.exp.UserClass");
//        UserClass c=new UserClass();
        // 第一次加载类出现异常，并且catch住继续执行后
        // 当第二次加载时，会出现java.lang.NoClassDefFoundError: Could not initialize class的错误
        Class c;
        try{
            c= Class.forName("com.shaoff.dig.exp.UserClass");
        }catch (Throwable e){
        }
        c= Class.forName("com.shaoff.dig.exp.UserClass");

    }
}
