package com.shaoff.dig.collection;


import java.util.*;

public class LinkedHashMap<K, V> implements Map<K, V> {

    private static final long serialVersionUID = -1135090104265442625L;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int MAXIMUM_CAPACITY = 1 << 30;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    transient Node<K, V>[] table;
    transient int size;
    transient int modCount;
    transient int loadFactor;
    /**
     * 超过后要进行resize的当前size阈值
     */
    int threshold;

    Node<K, V> head;
    Node<K, V> tail;
    final boolean accessOrder;

    public LinkedHashMap(boolean accessOrder) {
        this.accessOrder = accessOrder;
    }

    public LinkedHashMap() {
        this(false);
    }

    static class Node<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;
        // linked hashmap only
        Node<K, V> before;
        Node<K, V> after;

        public Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
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
        public V setValue(V newValue) {
            V res = value;
            value = newValue;
            return res;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Node<?, ?> node = (Node<?, ?>) o;
            return hash == node.hash && Objects.equals(key, node.key) && Objects.equals(value, node.value);
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
    }

    /**
     * 高16位和低16位异或后作为hash码，这是为了防止如果某些key的hash仅在高位不同时，与tab.length-1的掩码&后的冲突
     */
    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
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
        return getNode(hash(key), key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Node<K, V>[] tab = table;
        if (tab != null) {
            for (Node<K, V> kvNode : tab) {
                for (Node<K, V> e = kvNode; e != null; e = e.next) {
                    if (Objects.equals(value, e.value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        Node<K, V> node = getNode(hash(key), key);
        if (node == null) {
            return null;
        } else {
            if (accessOrder)
                afterNodeAccess(node);
            return node.value;
        }
    }

    @Override
    public V put(K key, V value) {
        return putVal(hash(key), key, value, false, true);
    }

    @Override
    public V remove(Object key) {
        Node<K, V> e = removeNode(hash(key), key, null, false);
        return e == null ? null : e.value;
    }

    Node<K, V> removeNode(int hash, Object key, Object value,
                          boolean matchValue) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, index;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (p = tab[index = (n - 1) & hash]) != null) {
            Node<K, V> node = null, e;
            if (p.hash == hash && Objects.equals(p.key, key)) {
                node = p;
            } else if ((e = p.next) != null) {
                do {
                    if (e.hash == hash && Objects.equals(e.key, key)) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
            if (node != null && (!matchValue || Objects.equals(node.value, value))) {
                if (node == p) {
                    tab[index] = node.next;
                } else {
                    p.next = node.next;
                }
                ++modCount;
                --size;
                afterNodeRemoval(node);
                return node;
            }
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            K key = e.getKey();
            V value = e.getValue();
            putVal(hash(key), key, value, false, true);
        }
    }

    @Override
    public void clear() {
        Node<K, V>[] tab;
        modCount++;
        if ((tab = table) != null && size > 0) {
            size = 0;
            Arrays.fill(tab, null);
        }
    }

    @Override
    public Set<K> keySet() {
        return null; // TODO
    }

    @Override
    public Collection<V> values() {
        return null; // TODO
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null; // TODO
    }

    @Override
    public boolean equals(Object o) {
        return false; // TODO
    }

    @Override
    public int hashCode() {
        return 0; // TODO
    }

    final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        Node<K, V>[] tab;
        Node<K, V> p;
        int n, i;
        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }
        if ((p = tab[i = (n - 1) & hash]) == null) {
            Node<K, V> newNode = new Node<>(hash, key, value, null);
            linkNodeLast(newNode);
            tab[i] = newNode;
        } else {
            Node<K, V> e;
            if (p.hash == hash && Objects.equals(p.key, key)) {
                e = p;
            } else {
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {
                        Node<K, V> newNode = new Node<>(hash, key, value, null);
                        linkNodeLast(newNode);
                        p.next = newNode;
                        break;
                    }
                    if (e.hash == hash && Objects.equals(e.key, key)) {
                        break;
                    }
                    p = e;
                }
            }
            if (e != null) { // existing mapping for key
                V oldValue = e.value;
                if (!onlyIfAbsent || oldValue == null) {
                    e.value = value;
                }
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;
        if (++size > threshold) {
            resize();
        }
        afterNodeInsertion(evict);
        return null;
    }

    private void linkNodeLast(Node<K, V> node) {
        if (tail == null) {
            head = tail = node;
        } else {
            node.before = tail;
            tail.after = node;
            tail = node;
        }
    }

    private void afterNodeInsertion(boolean evict) {
        if (evict && removeEldestEntry(head)) {
            removeNode(hash(head.key), head.key, null, false);
        }
    }

    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return false;
    }


    void afterNodeRemoval(Node<K, V> e) { // unlink
        Node<K, V> a = e.after;
        Node<K, V> b = e.before;
        e.before = e.after = null;
        if (b == null)
            head = a;
        else
            b.after = a;
        if (a == null)
            tail = b;
        else
            a.before = b;
    }

    // move node e to the last
    private void afterNodeAccess(Node<K, V> e) {
        if (tail != e) {
            Node<K, V> a = e.after;
            Node<K, V> b = e.before;
            assert a != null;
            if (b == null) {
                a.before = null;
                head = a;
                e.before = tail;
                tail.after = e;
            } else {
                b.after = a;
                a.before = b;
                e.before = tail;
                tail.after = e;
            }
            e.after = null;
            tail = e;
            modCount += 1;
        }
    }

    Node<K, V> getNode(int hash, Object key) {
        Node<K, V>[] tab;
        Node<K, V> first, e;
        int n;
        K k;
        if ((tab = table) != null && (n = tab.length) > 0 &&
                (first = tab[(n - 1) & hash]) != null) {
            if (first.hash == hash && // always check first node
                    ((k = first.key) == key || (key != null && key.equals(k)))) {
                return first;
            }
            if ((e = first.next) != null) {
                do {
                    if (e.hash == hash &&
                            ((k = e.key) == key || (key != null && key.equals(k)))) {
                        return e;
                    }
                } while ((e = e.next) != null);
            }
        }
        return null;
    }

    Node<K, V>[] resize() {
        Node<K, V>[] oldTab = table;
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                    oldCap >= DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1; // double threshold
            }
        } else if (oldThr > 0) // initial capacity was placed in threshold
        {
            newCap = oldThr;
        } else {               // zero initial threshold signifies using defaults
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }
        if (newThr == 0) {
            float ft = (float) newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ?
                    (int) ft : Integer.MAX_VALUE);
        }
        threshold = newThr;
        @SuppressWarnings({"rawtypes", "unchecked"})
        Node<K, V>[] newTab = new Node[newCap];
        table = newTab;
        if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K, V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null) {
                        newTab[e.hash & (newCap - 1)] = e;
                    } else { // preserve order
                        Node<K, V> loHead = null, loTail = null;
                        Node<K, V> hiHead = null, hiTail = null;
                        Node<K, V> next;
                        do {
                            next = e.next;
                            if ((e.hash & oldCap) == 0) { // 无需移动的部分
                                if (loTail == null) {
                                    loHead = e;
                                } else {
                                    loTail.next = e;
                                }
                                loTail = e;
                            } else {
                                if (hiTail == null) { // 需要移动的部分
                                    hiHead = e;
                                } else {
                                    hiTail.next = e;
                                }
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead; // 需要移动的部分移动的位置是固定的
                        }
                    }
                }
            }
        }
        return newTab;
    }

    private static class LRUCache<K,V> extends LinkedHashMap<K,V> {
        private final int capacity;

        private LRUCache(int capacity) {
            super(true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Entry<K, V> eldest) {
            return size > capacity;
        }
    }
    public static void main(String[] args) {
        LRUCache<String, Object> map = new LRUCache<>(3);
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        System.out.println(map.get("a"));
        map.put("d","");
        System.out.println(map.containsKey("b"));
    }
}