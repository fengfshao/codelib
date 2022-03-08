package me.fengfshao.zyu.base;

import scala.Int;

/**
 * Author: fengfshao
 * Date: 2022/2/26 13:33
 * Description:
 *
 */
public class BinaryDemo {

    public static void main(String[] args) {
        int a=10;
        System.out.println(Integer.toBinaryString(-7));
        int[] studentsGrade=new int[3]; // 11 12 13
        studentsGrade[0]=11;
        studentsGrade[1]=12;
        studentsGrade[2]=13;

        //15 12
        int[] studentsGrade1=new int[5];
        for(int i=0;i<studentsGrade.length;i++){
            studentsGrade1[i]=studentsGrade[i];
        }


        //int[] studentsGrade1=new int[11];


        Integer integer = Integer.parseInt("1000000000000000000000000000111",2);
        System.out.println(integer);
    }

}
