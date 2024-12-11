package com.shaoff.dig.string1;

/**
 * Author: shaoff
 * Date: 2020/11/10 22:20
 * Package: string1
 * Description:
 */
public class Builder {

    //对于不可变类的进行多阶段操作时，警惕每一步都会产生一个对象的操作
    //此时应该使用其配套的可变类，如String的StringBuilder
    static void costCompare(){
        long t1=System.currentTimeMillis();
        String res=f1();
        long t2=System.currentTimeMillis();
        f2();
        long t3=System.currentTimeMillis();
        System.out.println("cost f1= "+(t2-t1));
        System.out.println("cost f2= "+(t3-t2));
    }
    
    static String f1(){
        StringBuilder b1=new StringBuilder();
        for(int i=0;i<10000;i++){
            b1.append((char) (i + 'a'));
        }
        return b1.toString();
    }

    static String f2(){
        String b1="";
        for(int i=0;i<10000;i++){
            b1+=(char) (i + 'a');
        }
        return b1;
    }
    public static void main(String[] args) {
        costCompare();
    }
}
