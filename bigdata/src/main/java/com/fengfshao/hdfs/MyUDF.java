package com.fengfshao.hdfs;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.MapObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;

import java.util.Map;

/**
 * @author leonardo
 * @since 2024/8/28
 */

public class MyUDF extends GenericUDF {

    private MapObjectInspector mapOI;
    private PrimitiveObjectInspector keyOI;

    @Override
    public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
        // 初始化你的UDF，设置返回值的ObjectInspector
        this.mapOI = (MapObjectInspector) arguments[0];
        this.mapOI = new LinkedMapObjectInspector(mapOI.getMapKeyObjectInspector(), mapOI.getMapValueObjectInspector());
        return PrimitiveObjectInspectorFactory.javaStringObjectInspector;
    }

    public Object evaluate(DeferredObject[] arguments) throws HiveException {
        // 实现你的逻辑，处理输入并返回结果
        Map<?, ?> map = mapOI.getMap(arguments[1].get());
        System.out.println("map: " + map.getClass());
        StringBuilder stringBuilder = new StringBuilder();
        map.forEach((k, v) -> {
            stringBuilder.append(k + " ");
        });
        System.out.println("keyList: " + stringBuilder.toString());
        System.out.println("clazz: " + arguments[1].get().getClass());
        return "exampleresult";
    }

    @Override
    public String getDisplayString(String[] children) {
        // 返回你的UDF的字符串表示
        return "example_udf(?)";
    }
}
