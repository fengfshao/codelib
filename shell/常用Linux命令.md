常用Linux命令

**端口查进程**

1.根据进程pid查端口: lsof -i | grep pid
2.根据端口port查进程(某次面试还考过): lsof -i:port
3.根据进程pid查端口: netstat -nap | grep pid
4.根据端口port查进程 netstat -nap | grep port


**文件截取**
根据内容截取某一行到某一行

cat a.log -n|grep -m 1 container_e09_1593592877086_5435_01_000003

输出
```
 3	Container: container_e09_1593592877086_5435_01_000003 on bigdata-sandbox-16-35.qcvmbj5.zybang.com_8041
```
记录行号 A=3

cat a.log -n|grep -m 2 Container:

```
3	Container: container_e09_1593592877086_5435_01_000003 on bigdata-sandbox-16-35.qcvmbj5.zybang.com_8041
11615	Container: container_e09_1593592877086_5435_01_000005 on bigdata-sandbox-16-35.qcvmbj5.zybang.com_8041
```

记录行号 B=11615

截取A-B
 sed -n '5,10p' filename 这样你就可以只查看文件的第5行到第10行。