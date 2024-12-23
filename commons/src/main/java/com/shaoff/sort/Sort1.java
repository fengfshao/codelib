package com.shaoff.sort;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Sort1 {
    static interface Sorter{
        void sort(int[] arr);
    }
    static class BubbleSort implements Sorter{
        @Override
        public void sort(int[] arr) {
            //边界检查
            if(arr==null||arr.length<2)
                return;
            //最后一个要冒出到的位置
            int lastPos=0;
            //最后一个位置无需冒出，已经有序
            for(int i=arr.length-1;i>0;i=lastPos){
                //每轮冒泡前假设数组已经有序
                lastPos=0;
                for(int j=0;j<i;j++){
                    if(arr[j]>arr[j+1]){
                        int temp=arr[j];
                        arr[j]=arr[j+1];
                        arr[j+1]=temp;
                        lastPos=j;
                    }
                }
            }
        }
    }
    static class SelectSort implements Sorter{
        @Override
        public void sort(int[] arr) {
            //边界检查
            if(arr==null||arr.length<2) return;
            //每轮选出最小的元素
            for(int i=0;i<arr.length-1;i++){
                int minVal=arr[i];
                int minPos=i;
                for(int j=i+1;j<arr.length;j++){
                    if(arr[j]<minVal){
                        minVal=arr[j];
                        minPos=j;
                    }
                }
                arr[minPos]=arr[i];
                arr[i]=minVal;
            }
        }
    }

    static class InsertSorter implements Sorter{
        @Override
        public void sort(int[] arr) {
            //边界检查
            if(arr==null||arr.length<2) return;
            //初始时假设数组[0,1)有序，从元素1开始向左寻找插入位置
            for(int i=1;i<arr.length;i++){
                //要尝试插入的元素
                int key=arr[i],j;
                //寻找可插入的位置,并移动部门元素
                for(j=i-1;j>=0&&key<arr[j];j--){
                    arr[j+1]=arr[j];
                }
                //此时j=-1或arr[j]<=key
                arr[j+1]=key;
            }
        }
    }

    static class MergeSort implements Sorter{
        @Override
        public void sort(int[] arr) {
            sort(arr,0,arr.length-1);
        }

        private void sort(int[] arr, int p, int r) {
            if(p<r){
                int q=(p+r)/2;
                sort(arr,p,q);
                sort(arr,q+1,r);
                merge(arr,p,q,r);
            }
        }

        private void merge(int[] arr, int p, int q, int r) {
            int[] arr1=new int[q-p+1];
            int[] arr2=new int[r-q];
            for(int i=0;i<arr1.length;i++)
                arr1[i]=arr[p+i];
            for(int i=0;i<arr2.length;i++)
                arr2[i]=arr[q+1+i];

            int i1=0,i2=0,i=p;
            while (i1<arr1.length&&i2<arr2.length){
                if(arr1[i1]<=arr2[i2])
                    arr[i++]=arr1[i1++];
                else
                    arr[i++]=arr2[i2++];
            }

            while (i1<arr1.length)
                    arr[i++]=arr1[i1++];
            while (i2<arr2.length)
                    arr[i++]=arr2[i2++];
        }
    }

    static class QuickSorter implements Sorter{
        @Override
        public void sort(int[] arr) {
            sort(arr,0,arr.length-1);
        }

        private void sort(int[] arr, int l, int r) {
            if(l<r){
                int q=partition(arr,l,r);
                sort(arr,l,q-1);
                sort(arr,q+1,r);
            }
        }

        private int partition(int[] arr, int l, int r) {
            assert l < arr.length && r < arr.length && l <= r;
            int key = arr[l];
            int i = l, j = r + 1; //arr[l+1...i] 为前半部分,arr[j...r]为后半部分
            while (i < j - 1) {
                if (arr[i + 1] < key) {
                    i++;
                } else {
                    int tmp = arr[j - 1];
                    arr[j - 1] = arr[i + 1];
                    arr[i + 1] = tmp;
                    j--;
                }
            }
            arr[l] = arr[i];
            arr[i] = key;
            return i;
        }
    }

    public static void main(String[] args) {
//        testSort(new InsertSorter());
        int[] input=new int[]{1,4,2,5};
        new QuickSorter().sort(input);
        System.out.println(Arrays.toString(input));
    }

    static void testSort(Sorter sorter){
        Random rd= new Random();
        for(int i=0;i<10;i++){
            int n=rd.nextInt(20);
            int[] input=new int[n];
            for(int j=0;j<n;j++) input[j]=rd.nextInt(10);
            TreeMap<Integer,Integer> ori=new TreeMap<>();
            for(int num:input)
                    ori.put(num,ori.getOrDefault(num,0)+1);
//            System.out.println(Arrays.toString(input));
            sorter.sort(input);
            TreeMap<Integer,Integer> sorted=new TreeMap<>();
            for(int num:input)
                sorted.put(num,sorted.getOrDefault(num,0)+1);
            for(Map.Entry<Integer, Integer> entry:sorted.entrySet()){
                if(entry.getValue()!=ori.get(entry.getKey())) {
                    System.out.println("wrong");
                    break;
                }
            }
            if(!checkSorted(input)) {
                System.out.println("wrong");
                break;
            }
//            System.out.println(Arrays.toString(input));
        }
        System.out.println("ok");
    }

    static boolean checkSorted(int[] arr){
        if(arr==null||arr.length==0) return true;
        for(int i=0;i<arr.length-1;i++){
            if(arr[i]>arr[i+1]) return false;
        }
        return true;
    }
}
