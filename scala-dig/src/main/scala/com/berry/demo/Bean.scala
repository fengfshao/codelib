package com.berry.demo

/**
 * scala 类对成员和getter/setter的设定
 * <p>1. var会生成编译生成getter/setter，val只会生成getter，并用final修饰</p>
 * <p>2. 构造函数的参数如果标注了val/var会被编译为成员变量，否则只是一个构造函数的参数，如果其他函数没有使用到</p>
 *
 * 对应如下结构的java类
 * <pre>
 *   public class Bean {
 *      private final int b;
 *      private int a;
 *      public Bean(int b,int c){
 *        this.b=b
 *        this.a=c
 *      }
 *      public getB() {
 *        return b;
 *      }
 *      public setA(int a){
 *         this.a=a;
 *      }
 *      public getA(){
 *        return a;
 *      }
 *   }
 * </pre>
 * @author fengfshao
 * @since 2023/6/20
 *
 */
class Bean(val b: Int, c: Int) {
  var a: Int = c
}
