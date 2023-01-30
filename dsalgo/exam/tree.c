# include <stdio.h>
# include <stdlib.h>
# define M 100

typedef struct node{
    int data;
    struct node *left;
    struct node *right;
}BTNODE,*BTREE;

void visit(BTREE root){
    printf("%c\n",root->data);
}

void preorder(BTREE root){
    if(root!=NULL){
       visit(root); 
       preorder(root->left);
       preorder(root->right);
    }
}

void postorder(BTREE root){
    if(root!=NULL){
       postorder(root->left);
       postorder(root->right);
       visit(root); 
    }
}

void preorder1(BTREE root){
    BTREE cur=root;
    BTREE stack[M];
    int top=-1;

    while(cur!=NULL||top!=-1){
        while(cur!=NULL){
            visit(cur);
            stack[++top]=cur;
            cur=cur->left;
        }
        cur=stack[top--];
        cur=cur->right;
    }
}

void inorder1(BTREE root) {

}

void postorder1(BTREE root){
    BTREE cur=root;
    BTREE stack[M];
    int top=-1;
    int mark[M]; // 标识stack[i]中结点第几次入栈
    
    while(cur!=NULL||top!=-1){
        while(cur!=NULL){
            int idx=++top;
            stack[idx]=cur;
            mark[idx]=1;
            cur=cur->left;
        }
        
        if(mark[top]==1){
            // 由于此时需出栈后入栈，故不实际进行出入栈操作
            mark[top]=2;
            cur=stack[top]->right;
        }else if(mark[top]==2){
            visit(stack[top--]);
        }
    }
}

void postorder2(BTREE root,int item){
    BTREE cur=root;
    BTREE stack[M];
    int top=-1;
    int mark[M]; // 标识stack[i]中结点第几次入栈
    int found=0;
    while(cur!=NULL||top!=-1){
        while(cur!=NULL){
            stack[++top]=cur;
            mark[top]=1;
            cur=cur->left;
        }
        
        if(mark[top]==1){
            // 由于此时需出栈后入栈，故不实际进行出入栈操作
            mark[top]=2;
            cur=stack[top]->right;
        }else if(mark[top]==2){
            BTREE node=stack[top--];
            if(found==1){
                printf("%c\n",node->data);
            }
            if(node->data==item){
                found=1;
            }
        }
    }
}

// 根据前序遍历创建二叉树，叶节点左右自树为-1
BTREE createBTREE(int data[],int *idx){
    int e=data[*idx];
    *idx=*idx+1;
    if(e==-1){
        return NULL;
    }else{
        BTREE a= (BTREE) malloc(sizeof(BTNODE));
        a->data=e;
        a->left=createBTREE(data,idx);   
        a->right=createBTREE(data,idx);
        return a;
    }
}


//   a
//  b  c
//    d e
//   f g

int main(int argc,char* argv[]) {
    int *idx=(int*) malloc(sizeof(int));
    *idx=0;
    //int arr[15]={'A','B',-1,-1,'C',-1,-1};
    int arr[15]={'A','B',-1,-1,'C','D','F',-1,-1,'G',-1,-1,'E',-1,-1};
    BTREE root=createBTREE(arr,idx);
    postorder2(root,'E');
}

