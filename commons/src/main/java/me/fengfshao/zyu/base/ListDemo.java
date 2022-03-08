package me.fengfshao.zyu.base;

/**
 * Author: fengfshao
 * Date: 2022/2/26 14:11
 * Description:
 *
 */

interface ScequenceList {

    /**
     * 向线性表中追加一个元素
     * @param num 元素值
     */
    void add(int num);

    /**
     * 向线性表中插入一个元素
     * @param idx 插入的位置
     * @param num 插入的元素值
     */
    void set(int idx, int num);

    /**
     * 删除线性表中的一个元素
     * @param num 删除的元素值
     * @return true删除成功，false删除失败（该表中不存在此元素）
     */
    boolean delete(int num);

    /**
     * 查找线性表中的一个元素
     * @param num 查找的元素
     * @return true找到了，false未查找到该元素
     */
    boolean contains(int num);

    /**
     * 从idx位置取一个元素
     * @param idx 取的位置
     * @return 取到的该位置的元素值
     */
    int get(int idx);

}

class ZzzList implements ScequenceList {

    private int[] nums;
    private int n;

    public ZzzList(int initialCapacity) {
        this.nums = new int[initialCapacity];
        this.n= 0;
    }

    public ZzzList() {
        this(5);
    }



    @Override
    public void add(int num) {
        if (n == nums.length) {
            int[] newNums =new int[nums.length*2];
            for(int i=0;i<nums.length;i++){
                newNums[i]=nums[i];
            }
            this.nums=newNums;
        }
        nums[n]=num;
        n=n+1;
    }

    @Override
    public void set(int idx, int num) {

    }

    @Override
    public boolean delete(int num) {
        return false;
    }

    @Override
    public boolean contains(int num) {
        return false;
    }

    @Override
    public int get(int idx) {
        return nums[idx];
    }
}

public class ListDemo {

    public static void main(String[] args) {
        ScequenceList list = new ZzzList();
        /*int[] arr=new int[3]; // 1,2,3
        arr[0]=1;
        arr[1]=2;
        arr[2]=3;*/

        list.add(1);
        list.add(2);

        list.add(2);
        list.add(3);
        list.add(2);

        list.add(2);
        list.add(2);
        list.add(27);

        System.out.println(list.get(0));
        System.out.println(list.get(1));
        System.out.println(list.get(2));


        int[] arr=new int[3]; // 1,2,3
        arr[0]=4;
        arr[1]=5;
        arr[2]=6;


        for (int i=0;i<arr.length;i++){
            System.out.println(arr[i]);
        }

    }
}
