//package com.shaoff.dig.collection.my;
//
//
//import com.shaoff.myds.BinarySearchTree.BinaryTreeNode;
//import java.util.ArrayList;
//
//public class TreeUtil {
//
//    public static <T> String preview(BinaryTreeNode<T> root) {
//        StringBuilder res = new StringBuilder();
//        preview1(root, res);
//        return res.toString();
//    }
//
//    public static <T> String inview(BinaryTreeNode<T> root) {
//        StringBuilder res = new StringBuilder();
//        ArrayList<BinaryTreeNode> stk = new ArrayList<>();
//        BinaryTreeNode cur = root;
//        while (cur != null || !stk.isEmpty()) {
//            while (cur != null) {
//                stk.add(cur);
//                cur = cur.left;
//            }
//            cur = stk.remove(stk.size() - 1);
//            res.append(cur.value + " ");
//            cur = cur.right;
//        }
//        return res.toString();
//    }
//
//
//    private static <T> void preview1(BinaryTreeNode<T> root, StringBuilder sb) {
//        if (root != null) {
//            sb.append(root.toString() + " ");
//            preview1(root.left, sb);
//            preview1(root.right, sb);
//        }
//    }
//}
