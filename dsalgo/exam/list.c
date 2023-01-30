// 线性表，逻辑结构，可以使用顺序存储结构和链式存储结构实现

// ======== 顺序存储 ========
# include <stdio.h>
# define MAXLEN 100
typedef int ElemType;

typedef struct SeqList {
    int length;
    ElemType data[MAXLEN]; 
}SeqList;

// 未找到返回0，否则返回元素的逻辑序号（从1开始）
int locate(SeqList l, ElemType e) {
    int res=-1;
    for(int i=0;i<l.length;i++){
        if(l.data[i]==e){
            res=i;
        }
    }
    res+=1;
    return res;
}

// 插入元素，失败返回0，成功返回1，i为逻辑序号
int insert(SeqList l,ElemType e,int i) {
    if(i<1||i>l.length+1){
        return 0;
    }
    i-=1;
    for(int j=l.length;j>i;j--){
        l.data[j]=l.data[j-1];
    }
    l.data[i]=e;
    l.length+=1;
    return 1;
}

// 删除元素，失败返回0，成功返回1
// int delete(SeqList l,int i,ElemType &e) {
//     if(i<1||i>l.length){
//         return 0;
//     }
//     i-=1;
//     //e=l.data[i];
//     for(int j=i;j<l.length-1;j++){
//         l.data[j]=l.data[j+1];
//     }
//     l.length-=1;
//     return 1;
// }

int main(int argc,char* argv[]) {
    int a=1;
    int a1[12];
    printf("%d",sizeof(a1)/sizeof(int));

    return 0;
}

