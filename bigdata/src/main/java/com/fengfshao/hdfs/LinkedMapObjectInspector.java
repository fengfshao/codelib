package com.fengfshao.hdfs;

import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.SettableMapObjectInspector;
import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Writable;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedMapObjectInspector implements SettableMapObjectInspector {
    protected final ObjectInspector keyInspector;
    protected final ObjectInspector valueInspector;

    public LinkedMapObjectInspector(ObjectInspector keyInspector, ObjectInspector valueInspector) {
        this.keyInspector = keyInspector;
        this.valueInspector = valueInspector;
    }

    @Override
    public String getTypeName() {
        return "map<" + this.keyInspector.getTypeName() + "," + this.valueInspector.getTypeName() + ">";
    }

    @Override
    public Category getCategory() {
        return Category.MAP;
    }

    @Override
    public ObjectInspector getMapKeyObjectInspector() {
        return this.keyInspector;
    }

    @Override
    public ObjectInspector getMapValueObjectInspector() {
        return this.valueInspector;
    }

    @Override
    public Object getMapValueElement(Object data, Object key) {
        if (data != null && key != null) {
            if (data instanceof ArrayWritable) {
                Writable[] mapContainer = ((ArrayWritable)data).get();
                if (mapContainer != null && mapContainer.length != 0) {
                    Writable[] mapArray = ((ArrayWritable)mapContainer[0]).get();
                    Writable[] arr$ = mapArray;
                    int len$ = mapArray.length;

                    for(int i$ = 0; i$ < len$; ++i$) {
                        Writable obj = arr$[i$];
                        ArrayWritable mapObj = (ArrayWritable)obj;
                        Writable[] arr = mapObj.get();
                        if (key.equals(arr[0])) {
                            return arr[1];
                        }
                    }

                    return null;
                } else {
                    return null;
                }
            } else if (data instanceof Map) {
                return ((Map)data).get(key);
            } else {
                throw new UnsupportedOperationException("Cannot inspect " + data.getClass().getCanonicalName());
            }
        } else {
            return null;
        }
    }

    @Override
    public Map<?, ?> getMap(Object data) {
        if (data == null) {
            return null;
        } else if (!(data instanceof ArrayWritable)) {
            if (data instanceof Map) {
                return (Map)data;
            } else {
                throw new UnsupportedOperationException("Cannot inspect " + data.getClass().getCanonicalName());
            }
        } else {
            Writable[] mapContainer = ((ArrayWritable)data).get();
            if (mapContainer != null && mapContainer.length != 0) {
                Writable[] mapArray = ((ArrayWritable)mapContainer[0]).get();
                Map<Writable, Writable> map = new LinkedHashMap();
                Writable[] arr$ = mapArray;
                int len$ = mapArray.length;

                for(int i$ = 0; i$ < len$; ++i$) {
                    Writable obj = arr$[i$];
                    ArrayWritable mapObj = (ArrayWritable)obj;
                    Writable[] arr = mapObj.get();
                    map.put(arr[0], arr[1]);
                }

                return map;
            } else {
                return null;
            }
        }
    }

    @Override
    public int getMapSize(Object data) {
        if (data == null) {
            return -1;
        } else if (data instanceof ArrayWritable) {
            Writable[] mapContainer = ((ArrayWritable)data).get();
            return mapContainer != null && mapContainer.length != 0 ? ((ArrayWritable)mapContainer[0]).get().length : -1;
        } else if (data instanceof Map) {
            return ((Map)data).size();
        } else {
            throw new UnsupportedOperationException("Cannot inspect " + data.getClass().getCanonicalName());
        }
    }

    @Override
    public Object create() {
        Map<Object, Object> m = new LinkedHashMap<>();
        return m;
    }

    @Override
    public Object put(Object map, Object key, Object value) {
        Map<Object, Object> m = (LinkedHashMap)map;
        m.put(key, value);
        return m;
    }

    @Override
    public Object remove(Object map, Object key) {
        Map<Object, Object> m = (LinkedHashMap)map;
        m.remove(key);
        return m;
    }

    @Override
    public Object clear(Object map) {
        Map<Object, Object> m = (LinkedHashMap)map;
        m.clear();
        return m;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (this.keyInspector == null ? 0 : this.keyInspector.hashCode());
        result = 31 * result + (this.valueInspector == null ? 0 : this.valueInspector.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            LinkedMapObjectInspector other = (LinkedMapObjectInspector)obj;
            if (this.keyInspector == null) {
                if (other.keyInspector != null) {
                    return false;
                }
            } else if (!this.keyInspector.equals(other.keyInspector)) {
                return false;
            }

            if (this.valueInspector == null) {
                if (other.valueInspector != null) {
                    return false;
                }
            } else if (!this.valueInspector.equals(other.valueInspector)) {
                return false;
            }

            return true;
        }
    }
}
