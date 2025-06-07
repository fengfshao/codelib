#!/usr/bin/env bash
#短路或
if echo "a" || echo "b" ;then
  echo "c"
fi

#双方括号添加字符串匹配，未必都匹配
input="/home/sakura"
if [[ $input =~ ^/.* ]];then
  echo "$input is a absolute path"
fi

# ==支持的是通配fu
if [[ $input == /* ]];then
  echo "$input is a absolute path"
fi

#case 匹配
case $USER in
sakura | rich)
  echo "Welcome,$USER"
  echo "Please enjoy your visit";;
testing)
  echo "Please specify testing account";;
*)
  echo "Sorry,you not not allowed here";;
esac


# $* 将所有参数存为一个单词，$@ 保存为多个独立的单词

#处理选项
while [ -n "$1" ]
do
  case "$1" in
  -a) echo "Found the -a option" ;;
  -b) echo "Found the -b option" ;;
  -c) echo "Found the -c option" ;;
  *) echo "$1 is not an option" ;;
  esac
  shift
done