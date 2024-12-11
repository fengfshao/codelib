package com.shaoff.dig.string1;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: shaoff
 * Date: 2020/5/25 03:49
 * Package: string1
 * Description:
 * 测试java常量池
 */
public class ConstantPool {
    static final String literal1="hello";
    static String literal2="hello";

    /**
     * 测试非final是否字面量
     */
    static void test0(){
        System.out.println(literal1==literal2);
    }
    /**
     * 字面量，""引用字符串都自动加在常量池
     * 当使用""创建字符串，首先在常量池中找，有则返回，无则添加到常量池后返回
     *
     * 使用intern方法也可以将堆中创建的字符串放一份到常量池并返回常量池的引用
     * 如果此时常量池有，直接返回它
     * 如果此时常量池没有，添加后返回
     * https://www.journaldev.com/797/what-is-java-string-pool
     */
    static void  test1(){
        String b="hello";
        String c=new String("hello");
        assert b==literal1;
        assert b!=c; //堆中和常量池中两个"hello"
        assert b==c.intern(); //interl直接返回常量池的hello

        String d=new String("world");
        String e=d.intern(); //"world"添加到常量池
        assert e!=d;
        String f="1world".substring(1); //产生新string对象，既不是常量池中的，也不是之前堆中的
        assert f.equals(d);
        assert (f!=d && f!=e);
    }

    /**
     * how many string are created
     */
    static void test3(){
        String a="used";//put to pool
        String n=new String("used"); //just create in heap
        String u=new String("unsed"); //just create in heap, "unsed" already created when as args
        String t=new String("usa".substring(1)); //three: usa,sa,t
        String v="sa";
        System.out.println(v==t.intern());
    }

    /**
     * https://stackoverflow.com/questions/30911800/what-happens-if-string-pool-runs-out-of-memory
     * https://stackoverflow.com/questions/11700320/is-string-literal-pool-a-collection-of-references-to-the-string-object-or-a-col
     */
    static void test4(){
        String n1=new String("sff"); //auto in pool and create additional in heap
        String n2=n1.intern(); //返回的不是保存的堆的引用，因为已经有了
        System.out.println(n2!=n1);
        String n3=new String("shaoff2").substring(1); //just create in heap
//        String n4="shaoff";
//        System.out.println(n3.intern()==n4);
        System.out.println(n3.intern()==n3);
    }

    /**
     * metaspace string pool reference oom
     */
    static void test5(){
        //-XX:MaxMetaspaceSize=10m -XX:Xmx=2g
        Random random=new Random();
        List<String> arr=new ArrayList<>();
        int counter=0;
        while(true){
            try{
                byte[] bytes=new byte[2];
                random.nextBytes(bytes);
                arr.add(new String(bytes, StandardCharsets.UTF_8).intern());
                counter++;
            }catch (Throwable t){
                t.printStackTrace();
                System.out.println(counter);
                break;
            }
            System.out.println(counter);
        }
    }

    public static void main(String[] args) {
        test1();
    }
}
