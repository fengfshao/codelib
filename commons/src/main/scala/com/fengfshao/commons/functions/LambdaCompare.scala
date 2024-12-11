package com.fengfshao.commons.functions

/**
 * 对比scala的函数和java中的lambda表达式
 *
 * @author fengfshao
 * @since 2023/7/21
 *
 */

class LambdaCompare {
  def invoke(): Unit = {
    // map中的调用会编译出一个Function1的子类
    // 在java的lambda表达式实现中，通过invokeDynamic指令实现，避免生成大量的类及类加载的开销
    Array(("abc",1),("def",2),("xyz",3)).map(_._2+1)
  }
}
