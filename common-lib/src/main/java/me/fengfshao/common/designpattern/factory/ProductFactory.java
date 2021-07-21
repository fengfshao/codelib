package me.fengfshao.common.designpattern.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Author: fengfshao
 * Date: 2021/7/21 16:18
 * Package: me.fengfshao.common.designpattern.factory.v1
 * Description:
 */
public class ProductFactory {

    /*v1，硬编码，这种写法，只是缩小new 关键字的范围*/
    Product getProduct1(String type) {
        if (type.equalsIgnoreCase("a")) {
            return new ProductA();
        } else if (type.equalsIgnoreCase("b")) {
            return new ProductB();
        } else {
            return null;
        }
    }

    /*v2，这种写法，关键在于这个map如何初始化，主要思路有
     * 1.在配置文件中配置，初始化时解析配置
     * 2.实现类中添加注解，工厂类初始化时反射扫描特定包下特定注解的类 */
    private Map<String, String> type2clazzName = new HashMap<>();

    {
        type2clazzName.put("a", "me.fengfshao.common.designpattern.factory.v1.ProductA");
    }

    Product getProduct2(String type) throws Exception {
        Class<?> clazz = Class.forName(type2clazzName.get(type));
        return (Product) clazz.newInstance();
    }

    /*v3，通过jdk的service provider，这种方法类似v2配置文件的思路
     * 需要在resource下的META-INF->services->me.fengfshao.common.designpattern.factory.Product文件中定义好路径*/
    private Map<String, Class<? extends Product>> type2clazz = new HashMap<>();
    {
        ServiceLoader<Product> serviceLoader = ServiceLoader.load(Product.class);

        for (Product fs : serviceLoader) {
            type2clazz.put(fs.getType(), fs.getClass());
        }
    }

    Product getProduct3(String type) throws Exception {
        return type2clazz.get(type).newInstance();
    }


    public static void main(String[] args) throws Exception {
        ProductFactory pf = new ProductFactory();
        System.out.println(pf.type2clazz);
        Product p = pf.getProduct3("a");
        System.out.println(p.getType());
    }
}
