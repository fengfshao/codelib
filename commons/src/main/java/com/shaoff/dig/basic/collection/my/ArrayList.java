package com.shaoff.dig.basic.collection.my;

import java.io.Serializable;
import java.util.*;

public class ArrayList<T> extends AbstractCollection<T> implements Iterable<T>, Collection<T>, RandomAccess, Cloneable, Serializable {
    /**
     * 标识序列化与反序列化的兼容性
     */
    private static final long serialVersionUID = 127788888100903042L;
    /**
     * 共享的空数组，可避免不同的空List创建多余的对象
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};
    /**
     * jvm允许最大数组长度
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    /**
     * 底层数组，声明为transient避免参与默认序列化，自行序列化
     */
    private transient Object[] elementData;
    /**
     * 元素个数
     */
    private int size;
    private transient int modCount = 0;

    public ArrayList() {
        this.elementData = EMPTY_ELEMENTDATA;
    }

    public ArrayList(int initialCapacity) {
        this.elementData = new Object[initialCapacity];
    }

    @Override
    public boolean add(T t) {
        modCount += 1;
        ensureCapacityEnough(size + 1);
        elementData[size++] = t;
        return true;
    }

    private void ensureCapacityEnough(int n) {
        if (elementData == EMPTY_ELEMENTDATA) {
            n = Math.max(10, n); // 10是默认初始的容量
        }
        if (n > elementData.length) {
            grow(n);
        }
    }

    /**
     * 先增加1.5倍，如果不够最少容量就按最少容量扩
     *
     * @param minCapacity 最少需要的
     */
    private void grow(int minCapacity) {
        int newCapacity = elementData.length + elementData.length >> 1;
        newCapacity = Math.max(newCapacity, minCapacity);
        newCapacity = Math.min(MAX_ARRAY_SIZE, newCapacity); // 如果超过最大容量将会数组越界add时
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    @Override
    public int size() {
        return size;
    }

    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @SuppressWarnings("unchecked")
    public T get(int pos) {
        rangeCheck(pos);
        return (T) elementData[pos];
    }

    @SuppressWarnings("unchecked")
    public T set(int pos, T ele) {
        rangeCheck(pos);
        T oldValue = (T) elementData[pos];
        elementData[pos] = ele;
        return oldValue;
    }

    /**
     * modCount是必要的，无论是使用索引遍历还是迭代器，那么都要维护一个当前索引信息，在List有结构性修改后
     * 此值需要额外维护才能保证其正确性，迭代器访问时迭代器内部维护好了索引，可以安全的遍历
     */
    @Override
    public boolean remove(Object o) {
        int idx = indexOf(o);
        if (idx > -1) {
            return removeIdx(idx);
        } else {
            return false;
        }
    }

    /**
     * 删除元素时不会调整底层数组长度，可能是认为下次大概率还会接近这个值，
     * 留给用户主动trim
     */
    public boolean removeIdx(int idx) {
        rangeCheck(idx);
        modCount += 1;
        System.arraycopy(elementData, idx + 1, elementData, idx, size - 1 - idx);
/*
        for (int j = idx; j < size - 1; j++) {
            elementData[j] = elementData[j + 1];
        }
*/
        elementData[--size] = null;
        return true;
    }

    public void trimToSize() {
        modCount += 1;
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(elementData, size);
        }
    }

    private void rangeCheck(int idx) {
        if (idx >= elementData.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public void clear() {
        modCount += 1;
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<T> {
        //要被查看的下一元素的索引
        private int cur = 0;
        private int lastRet = -1;
        private int expectedModCount = modCount;

        @Override
        public boolean hasNext() {
            return cur != size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T next() {
            checkMod();
            if (cur > size) {
                throw new NoSuchElementException();
            }
            lastRet = cur;
            return (T) elementData[cur++];
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            checkMod();
            ArrayList.this.removeIdx(lastRet);
            cur = lastRet;
            lastRet = -1;
            expectedModCount = modCount; // 仅允许使用迭代器访问时在遍历中删除
        }

        private void checkMod() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    public static void main(String[] args) {
        //System.out.println(new byte[MAX_ARRAY_SIZE]);
        int oldCapacity = 16;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        System.out.println(newCapacity);

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(3);
        list.add(2);

        List<Integer> javaList = new java.util.ArrayList<>();
        javaList.add(1);
        javaList.add(3);
        javaList.add(2);

        /* 对比不使用迭代器访问时删除，造成访问错乱的问题 */
        int sum1 = 0;
        for (int i = 0; i < javaList.size(); i++) {
            if (javaList.get(i) == 3) {
                javaList.remove(i);
            } else {
                sum1 += javaList.get(i);
            }
        }
        System.out.println(sum1);

        // 由于迭代器模式内部正确维护了删除时的索引，因此可以正常执行
        int sum2 = 0;
        Iterator<Integer> it= list.iterator();
        while (it.hasNext()) {
            int num=it.next();
            if (num == 3) {
                it.remove();
            } else {
                sum2 += num;
            }
        }
        System.out.println(sum2);
    }
}
