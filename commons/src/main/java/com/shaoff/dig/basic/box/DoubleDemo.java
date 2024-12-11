package com.shaoff.dig.basic.box;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: shaoff
 * Date: 2020/9/2 18:39
 * Package: box
 * Description:
 */
public class DoubleDemo {
    static void doubleJson() throws JsonProcessingException{
        double d=Double.NEGATIVE_INFINITY;
        System.out.println(d);

        ObjectMapper om = new ObjectMapper();
        Map<String,Object> map=new HashMap<>();
        map.put("abc",Double.NEGATIVE_INFINITY);

        String j=om.writeValueAsString(map);
        System.out.println(j);
    }


    public static void main(String[] args)  {
        doubleCompare();
    }

    //比较double时使用封装类的compare方法，而不是==，因为有NaN
    //NaN表示 not a number，同时用0.0/0.0表示
    //NaN和任何值比较时都返回false，使用Double.compare可以比较两个NaN的值
    //1.0/0.0表示正无穷，-1.0/0.0表示负无穷
    static void doubleCompare(){
        double d1=1;
        double d2=2;
        int f = Double.compare(d1, d2);

        System.out.println(0.0/0.0);
        System.out.println(1.0/0.0);
        System.out.println(-1.0/0.0);

        System.out.println(Double.NaN == Double.NaN);
        System.out.println(Double.compare(Double.NaN,Double.NaN));
        System.out.println(1.0<Double.NaN);

        //获得NaN
        System.out.println(0.0 / 0.0);
        System.out.println(0.2 / 0.0); //Infinity
        System.out.println(-0.3 / 0.0);//-Infinity
        System.out.println(Math.sqrt(-2)); //开负数返回NaN

        System.out.println(Double.POSITIVE_INFINITY > 1.0);
        System.out.println(Double.NEGATIVE_INFINITY==Double.NEGATIVE_INFINITY);
    }
}
