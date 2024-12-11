package com.shaoff.dig.basic.collection.my;


public class BinarySearchTree<E extends Comparable<E>> {
    public static class BinaryTreeNode<T> {
        public BinaryTreeNode<T> left;
        public BinaryTreeNode<T> right;
        public T value;

        public BinaryTreeNode(){ }

        public BinaryTreeNode(T value) {
            this.value = value;
        }

        public BinaryTreeNode(BinaryTreeNode left, BinaryTreeNode right, T value) {
            this.left = left;
            this.right = right;
            this.value = value;
        }

        @Override
        public String toString() {
            return ""+value;
        }
    }
    private BinaryTreeNode<E> root;
    private int size;

    public BinaryTreeNode<E> getRoot() {
        return root;
    }

    public void add(E val) {
        root = add(root, val);
        size++;
    }

    private BinaryTreeNode<E> add(BinaryTreeNode<E> root, E val) {
        if (root == null) {
            return new BinaryTreeNode<>(val);
        }
        int cp = val.compareTo(root.value);
        if (cp < 0) {
            root.left = add(root.left, val);
        } else if (cp > 0) {
            root.right = add(root.right, val);
        }
        return root;
    }

    public int size() {
        return size;
    }

    public boolean contains(E val) {
        return contains(root, val);
    }

    private boolean contains(BinaryTreeNode<E> root, E val) {
        if (root == null) {
            return false;
        }
        int cp = val.compareTo(root.value);
        if (cp < 0) {
            return contains(root.left, val);
        } else if (cp > 0) {
            return contains(root.right, val);
        } else {
            return true;
        }
    }


    public E minVal() {
        BinaryTreeNode<E> minBinaryTreeNode = findMin(root);
        return minBinaryTreeNode == null ? null : minBinaryTreeNode.value;
    }

    public E maxvalue() {
        BinaryTreeNode<E> maxBinaryTreeNode = findMax(root);
        return maxBinaryTreeNode == null ? null : maxBinaryTreeNode.value;
    }

    private BinaryTreeNode<E> findMin(BinaryTreeNode<E> root) {
        BinaryTreeNode<E> cur = root;
        if (cur == null) {
            return null;
        } else {
            while (cur.left != null)
                cur = cur.left;
            return cur;
        }
    }

    private BinaryTreeNode<E> findMax(BinaryTreeNode<E> root) {
        BinaryTreeNode<E> cur = root;
        if (cur == null) {
            return null;
        } else {
            while (cur.right != null) {
                cur = cur.right;
            }
            return cur;
        }
    }

    private BinaryTreeNode<E> removeMin(BinaryTreeNode<E> root) {
        BinaryTreeNode<E> cur = root;
        if (cur == null) {
            return null;
        }
        BinaryTreeNode<E> parent = cur;
        while (cur.left != null) {
            parent = cur;
            cur = cur.left;
        }
        BinaryTreeNode<E> min = cur;
        if (min == parent) {//根节点是最小
            return min.right;
        } else {
            parent.left = min.right;
            return root;
        }
    }

    public BinaryTreeNode<E> remove(BinaryTreeNode<E> root, E value) {
        if (root == null) {
            return null;
        }
        int comp = root.value.compareTo(value);
        if (comp < 0) {
            root.left = remove(root.left, value);
        } else if (comp > 0) {
            root.right = remove(root.right, value);
        } else if (root.left != null && root.right != null) {
            E minVal = findMin(root.right).value;
            root.value = minVal;
            root.right = remove(root.right, minVal);
            size--;
        } else {
            root = (root.left != null) ? root.left : root.right;
            size--;
        }
        return root;
    }


    public static void main(String[] args) {
        BinarySearchTree<Integer> bst=new BinarySearchTree<>();
        bst.add(1);
        bst.add(3);
        bst.add(2);
        bst.add(9);
        bst.add(8);
        bst.add(6);
        bst.add(7);
        String s=TreeUtil.inview(bst.getRoot());
        System.out.println(s);
    }
}
