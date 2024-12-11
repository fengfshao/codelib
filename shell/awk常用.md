# awk常用

### 某个字段求和
```
test_shao:0:225452542
test_shao:5:222246476
test_shao:4:222568980
test_shao:1:223153200
test_shao:2:221796792
test_shao:3:220343832
```

对上面数据，执行
```bash
awk -F":" '{sum+=$3} END {print "sum=",sum}'
```
字符串插值
```bash
awk '{printf "the field1:%s,field2:%s\n",$1,$2;}'
```