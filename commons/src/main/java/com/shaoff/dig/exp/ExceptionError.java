package com.shaoff.dig.exp;

/**
 * Author: shaoff
 * Date: 2020/11/18 18:31
 * Package: exp
 * Description:
 * 1.Throwable子类都可以抛出
 * 2.Throwable子类都可以捕获
 */
public class ExceptionError {
    /*Exception,RuntimeException,Error都可以主动抛出*/
    static void f1(){
            throw new StackOverflowError();
    }
    static void f2(){
        throw new NullPointerException();
    }

    /*都可以catch*/
    static void f3(){
        try{
            f1();
            f2();
        }catch (StackOverflowError s){
            System.out.println("StackOverflow");
        }catch (NullPointerException e){
            System.out.println("Null");
        }
    }

    public static void main(String[] args) {
        f3();
    }
}
