#!/usr/bin/env bash

#可以自动用函数退的退出码当返回值，或是return指定，必须是0-255

function dbl() {
    read -p "Enter a value: " value
    echo "doubling the value"
    return $[ $value * 2 ]
}
dbl
echo "The new value is $?"

#echo字符串当返回值
function dbl2() {
  local name
  read -p "Enter a name: " name
  echo $name
}
res=$(dbl2)
echo "input name is: $res"

#传参数组
function addarray() {
  local sum=0
  local arr
  arr=($(echo "$@"))
  for num in ${arr[*]}
  do
      sum=$[ $sum + $num ]
  done
  echo $sum
}

myarr=(1 2 10 12)
echo "origin array is: ${myarr[*]}"
sum=$(addarray ${myarr[*]})
echo "the sum of array is: $sum"

#判断绝对路径
function isAbs() {
    if [[ "$1" =~ ^/.* ]];then
        return 0
    else
        return 1
    fi
}

#调用库函数
. ./myfunc.sh
res=$(addem 1 3)
echo $res