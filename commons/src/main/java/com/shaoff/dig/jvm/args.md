# JVM（8） 常用参数记录
## 参数分类
所谓的jvm参数，其实就是java -xx中的xx.按照oracle的文档，用的是option这个词，所以下面就用选项指参数。    

### 标准选项
所有的JVM实现都支持标准选项，他们用于一些常规操作，如检查JRE版本，设置环境变量，开启冗余输出等。

#### 示例
**在示例选项中提到的选项会在下面再进行详细介绍，有兴趣的还可以跳转至参考文献中的官方文档**
1. -client 客户端模式
2. -Dproperty=value 设置系统属性
3. -verbose:class 显示加载的类的信息
4. -verbose:gc 显示GC事件

### 非标准选项
这些选项通常Hotspot专用的，以-X开头  

#### 示例
1. -Xbatch 取消后台编译
1. -Xbootclasspath:path
2. -Xloggc:filename 将GC事件记录至文件，无需额外再加-verbose:gc
3. -Xms,-Xmx,-Xmn

### 高级运行时选项
这类选项以-XX开头，大致分为
1. -XX+/- 开启/关闭某个选项
2. -XX:option=number 数字可以包含k或是K表示KB,m,g以此类推
3. -XX:option=string

#### 示例
1. -XX:+PrintGCDetails 每次GC打印详细message
2. -XX:OnOutOfMemoryError=string 设置一些列命令，用分号分割
3. -XX:ThreadStackSize=size
## 堆
|参数|示例|说明|
|-|-|-|
|-Xms|-Xms2G|堆的初始值|
|-Xmx|-Xmx5G|堆的最大值|


## 参考文档
### 官方文档
https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html
https://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html#BABDJJFI


