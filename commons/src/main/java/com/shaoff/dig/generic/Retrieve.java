package com.shaoff.dig.generic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Author: shaoff
 * Date: 2020/6/17 01:36
 * Package: generic
 * Description:
 * 所谓的JVM擦出泛型信息，并不是一刀切的，或是这句话是有定语的
 * 在某些场景下，是可以用过反射来获取类型信息的
 * <p>
 * a.无法获取泛型信息的场景
 * 1.声明了一个泛型类或是泛型方法，想要得知这个类或方法的泛型信息
 *      由于Container<Integer>还是Container<String> 都是一个类Container,无法获取泛型信息
 *
 * 2.一个具体类型的泛型类对象，是可以通过反射获取其中的泛型信息的，但是无法获取到局部变量中的泛型信息
 * <p>
 * b.可以获取泛型信息的场景
 * 1.获取成员变量Field的泛型信息
 * 2.获取方法的返回值，参数的泛型信息
 *  对于Field，Method类，它们提供了获取genericType的方法，方法返回Type类型的对象，ParameterizedType是其的子类，
 *  该类的getActualArguments方法可以获取到泛型参数
 *
 *  之所以这些场景可以获取到，个人认为是因为这些类型信息在编译器就已经确定了，即已经写在代码中了，javac将这些信息存放在字节码中，
 *  运行时存在于class信息中，这里需要区分的是，Retrieve中成员变量Container<String> str的具体类型信息，存在于
 *  Retrieve中而非存在于Container中，Container甚至不知道自己是个泛型类
 *
 *  总结：
 *  不能够获取泛型信息是因为泛型类本身不包含任何泛型信息，无论是A<T1>还是A<T2>都是类A
 *  使用了泛型的成员变量和成员方法能够通过反射获取是因为具体的类型参数被存在了所属类的class信息中
 *
 *  局部变量无法获取泛型参数信息，就是因此其泛型参数并没有被任何一个类所记录。
 *
 */

class Container<T> {
    private T value;

    public Container(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

public class Retrieve {
    static Container<String> str = new Container<>("abc");

    public static void main(String[] args) throws Exception {
        Container<Integer> integer = new Container<>(1);
        getGenericFieldTypes("str");
        String name = integer.getClass().getTypeName();
        System.out.println(name);

        getMethodParameterTypes();
    }

    public static void getGenericFieldTypes(String fieldName) throws Exception {
        Field field = Retrieve.class.getDeclaredField(fieldName);
        Type genericsFieldType = field.getGenericType();
        if (genericsFieldType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericsFieldType;
            Type[] fieldArgTypes = parameterizedType.getActualTypeArguments();
            for (Type fieldArgType : fieldArgTypes) {
                Class fieldArgClass = (Class) fieldArgType;
                System.out.println("泛型字段的类型：" + fieldArgClass);
            }
        }
    }

    public static void getMethodReturnType() throws Exception {
        Method method = Retrieve.class.getMethod("getStringList", null);
        System.out.println(method.getReturnType());
        Type retrunType = method.getGenericReturnType();
        System.out.println(retrunType);
        if (retrunType instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) retrunType;
            Type[] typeArguments = type.getActualTypeArguments();
            for (Type typeArgument : typeArguments) {
                Class typeArgClass = (Class) typeArgument;
                System.out.println("泛型类型：" + typeArgClass);
            }
        }
    }

    public static void getMethodParameterTypes() throws Exception {
        Method method = Retrieve.class.getMethod("setList", List.class);
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type genericType : genericParameterTypes) {
            if (genericType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                Type[] types = parameterizedType.getActualTypeArguments();
                for (Type type : types) {
                    Class realType = (Class) type;
                    System.out.println("方法参数的类型：" + realType);
                }
            }
        }
    }
}
