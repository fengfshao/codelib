package com.shaoff.dig.collection;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 基于AVL树实现的TreeMap，使用迭代方式进行二叉树的操作
 */
public class TreeMap<K, V> implements Map<K, V> {
    Entry<K, V> root;
    private int size;

    static final class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        Entry<K, V> left;
        Entry<K, V> right;
        Entry<K, V> parent; // 如果是递归实现不需要
        /**
         * 记录高度值计算平衡因子
         */
        int h;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.h = 1;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(root, key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        Entry<K, V> en = find(root, key);
        if (en != null) {
            return en.value;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        return add(key, value);
    }

    @Override
    public V remove(Object key) {
        return remove(root, key);
    }

    private V remove(Entry node, Object key) {
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K, V> cur = node;
        while (cur != null) {
            int cmp = k.compareTo(cur.key);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            } else {
                break;
            }
        }
        if (cur != null) {
            V res = cur.value;
            cur.value = null; // help gc
            size -= 1;
            if (cur.left == null) {
                if (cur.right != null) {
                    cur.right.parent = cur.parent;
                }
                if (cur == root) {
                    root = cur.right;
                } else if (cur.parent.left == cur) {
                    cur.parent.left = cur.right;
                } else {
                    cur.parent.right = cur.right;
                }
                adjust(cur.parent);
            } else if (cur.right == null) {
                cur.left.parent = cur.parent;
                if (cur == root) {
                    root = cur.left;
                } else if (cur.parent.left == cur) {
                    cur.parent.left = cur.left;
                } else {
                    cur.parent.right = cur.left;
                }
                cur.parent = cur.left;
                adjust(cur.parent);
            } else {
                // 要删除的结点左右子树都不为空，使用右子树的最左结点进行值替换，再删除冗余的值
                // 使用右子树的最左结点依然可以保持二叉树的有序性，并且最左结点的左右子树必然为空
                Entry minValue = findMin(cur.right);
                cur.key = (K) minValue.key;
                cur.value = (V) minValue.value;
                remove(cur.right, cur.key);
                size += 1; // 多减的加回去
            }
            return res;
        }
        return null;
    }


    @Override
    public void clear() {
        size = 0;
        root = null;
        // TODO help gc
    }

    private V add(K key, V val) {
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) key;
        Entry<K, V> pre = null;
        Entry<K, V> cur = root;
        while (cur != null) {
            int cmp = k.compareTo(cur.key);
            pre = cur;
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            } else {
                V oldValue = cur.value;
                cur.setValue(val);
                return oldValue;
            }
        }
        Entry<K, V> newNode = new Entry<>(key, val);
        size += 1;
        if (pre == null) {
            root = newNode;
        } else {
            int cmp = k.compareTo(pre.key);
            newNode.parent = pre;
            if (cmp < 0) {
                pre.left = newNode;
            } else {
                pre.right = newNode;
            }
        }
        // 自底向上更新每个父结点的高度，并判断是否需要调整
        // 插入结点后可能影响父结点路径上的一个 https://blog.csdn.net/xiaojin21cen/article/details/97602146
        adjust(pre);
        return null;
    }

    // 参考 https://oi-wiki.org/ds/avl/
    private void fixBalance(Entry<K, V> node) {
        Entry<K, V> newNode = null;
        int factor = factor(node);
        if (factor < -1) {
            if (factor(node.left) < 0) {
                // LL case
                newNode = rotateRight(node);
            } else {
                // LR case
                node.left = rotateLeft(node.left);
                newNode = rotateRight(node);
            }
        } else if (factor > 1) {
            if (factor(node.right) > 0) {
                // RR case
                newNode = rotateLeft(node);
            } else {
                // RL case
                node.right = rotateRight(node.right);
                newNode = rotateLeft(node);
            }
        }
        // 更新父结点对旋转后结点的引用
        if (newNode != null) {
            if (newNode.parent == null) {
                root = newNode;
            } else {
                if (newNode.parent.left == node) {
                    newNode.parent.left = newNode;
                } else {
                    newNode.parent.right = newNode;
                }
            }
        }
    }

    private Entry<K, V> find(Entry<K, V> root, Object val) {
        @SuppressWarnings("unchecked")
        Comparable<? super K> k = (Comparable<? super K>) val;
        Entry<K, V> cur = root;
        while (cur != null) {
            int cmp = k.compareTo(cur.key);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            } else {
                return cur;
            }
        }
        return null;
    }

    private void adjust(Entry node) {
        // 自底向上更新每个父结点的高度，并判断是否需要调整
        // 插入结点后可能影响父结点路径上的一个 https://blog.csdn.net/xiaojin21cen/article/details/97602146
        while (node != null) {
            node.h = 1 + Math.max(getH(node.left), getH(node.right));
            fixBalance(node);
            node = node.parent;
        }
    }

    private static int getH(Entry en) {
        if (en == null)
            return 0;
        else return en.h;
    }

    /**
     * 按照定义，右子树高度-左子树高度，平衡树中平衡因子只有-1,0,1
     */
    private static int factor(Entry root) {
        return getH(root.right) - getH(root.left);
    }

    private static void updateH(Entry root) {
        root.h = 1 + Math.max(getH(root.left), getH(root.right));
    }

    /**
     * 将node替换为replaceNode，更新其父结点的引用
     */
    private void updateParentRef(Entry node, Entry replaceNode) {
        if (node == root) {
            root = replaceNode;
            return;
        }
        if (node.parent.left == node) {
            node.parent.left = replaceNode;
        } else {
            node.parent.right = replaceNode;
        }
    }

    private static Entry rotateRight(Entry root) {
        Entry newRoot = root.left;
        root.left = newRoot.right;
        newRoot.right = root;
        // 调整高度
        updateH(root);
        updateH(newRoot);
        // 更新parent
        Entry originP = root.parent;
        root.parent = newRoot;
        newRoot.parent = originP;
        if (root.left != null) {
            root.left.parent = root;
        }
        return newRoot;
    }


    private static Entry rotateLeft(Entry root) {
        Entry newRoot = root.right;
        root.right = newRoot.left;
        newRoot.left = root;
        // 调整高度
        updateH(root);
        updateH(newRoot);
        // 更新parent
        Entry originP = root.parent;
        root.parent = newRoot;
        newRoot.parent = originP;
        if (root.right != null) {
            root.right.parent = root;
        }
        return newRoot;
    }

    private static Entry findMin(Entry root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    @Override
    public void putAll(Map m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public static void test1() {
        // LL
        Map<Integer, String> testMap = new TreeMap<>();
        TreeMap<Integer, String> debugMap = (TreeMap<Integer, String>) testMap;
        testMap.put(5, "a");
        testMap.put(3, "c");
        testMap.put(6, "b");
        testMap.put(4, "f");
        testMap.put(1, "x");
        testMap.put(0, "z");
        //assert testMap.containsKey(3);
        System.out.println(debugMap.root);
    }

    public static void test2() {
        // RR
        Map<Integer, String> testMap = new TreeMap<>();
        TreeMap<Integer, String> debugMap = (TreeMap<Integer, String>) testMap;
        testMap.put(5, "a");
        testMap.put(6, "c");
        testMap.put(4, "b");
        testMap.put(3, "f");
        testMap.put(2, "x");
        //assert testMap.containsKey(3);
        System.out.println(debugMap.root);
    }

    public static void test3() {
        // LR
        Map<Integer, String> testMap = new TreeMap<>();
        TreeMap<Integer, String> debugMap = (TreeMap<Integer, String>) testMap;
        testMap.put(7, "a");
        testMap.put(9, "c");
        testMap.put(3, "b");
        testMap.put(1, "f");
        testMap.put(5, "x");
        testMap.put(4, "g");
        //assert testMap.containsKey(3);
        System.out.println(debugMap.root);
    }

    public static void test4() {
        // RL
        Map<Integer, String> testMap = new TreeMap<>();
        TreeMap<Integer, String> debugMap = (TreeMap<Integer, String>) testMap;
        testMap.put(7, "a");
        testMap.put(3, "c");
        testMap.put(11, "b");
        testMap.put(12, "f");
        testMap.put(8, "x");
        testMap.put(10, "g");
        System.out.println(debugMap.root);
    }

    public static void testRemove() {
        TreeMap<Integer, String> testMap = new TreeMap<>();
        testMap.put(1, "a");
        System.out.println(testMap.remove(1).equals("a"));
        System.out.println(testMap.root == null);
        testMap.put(2, "a");
        testMap.put(3, "b");
        System.out.println(testMap.remove(2) == "a");
        System.out.println(testMap.root.key == 3);

        testMap.put(4, "f");
        testMap.put(1, "a");
        testMap.remove(3);
        System.out.println(testMap.root.parent == null);
        System.out.println(testMap.root.right == null);
        System.out.println(testMap.root.left.key == 1);
        System.out.println(testMap.root.value == "f");

        System.out.println(testMap.remove(1) == "a");
        System.out.println(testMap.root.left == null);
    }

    public static void testRemove2() {
        TreeMap<Integer, String> testMap = new TreeMap<>();
        testMap.put(3, "f");
        testMap.put(1, "b");
        testMap.put(5, "f");
        testMap.put(4, "f");
        testMap.put(6, "f");

        testMap.remove(1);
        System.out.println(testMap.root.key == 4);
        System.out.println(testMap.root.right.right.key == 6);
    }

    public static void main(String[] args) {
        testRemove2();
//        Map<Integer, String> testMap = new TreeMap<>();
//        TreeMap<Integer, String> debugMap = (TreeMap<Integer, String>) testMap;
//        testMap.put(7, "a");
//        testMap.put(9, "c");
//        testMap.put(3, "b");
//        testMap.put(1, "f");
//        testMap.put(5, "x");
//        testMap.put(4, "g");
//        //assert testMap.containsKey(3);
//        System.out.println(debugMap.root);
    }
}

