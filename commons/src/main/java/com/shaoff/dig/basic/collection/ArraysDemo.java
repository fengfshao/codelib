package com.shaoff.dig.basic.collection;

import java.util.Arrays;

/**
 * @author leonardo
 * @since 2024/12/3
 */

public class ArraysDemo {

  public static void main(String[] args) {
    int[] arr = {4, 9, 3, 6, 10};
    Arrays.sort(arr,0,4);

    System.out.println(Arrays.toString(arr));
    String[] strArr = {"hello", "world"};
    System.out.println(Arrays.toString(strArr));

    System.out.println(Arrays.binarySearch(arr, 11));

  }
}
