#!/bin/bash
for filename in /Data/*.txt; do
    for ((i=0; i<=3; i++)); do
        ./MyProgram.exe "$filename" "Logs/$(basename "$filename" .txt)_Log$i.txt"
    done
done

https://stackoverflow.com/questions/17420994/how-can-i-match-a-string-with-a-regex-in-bash

#!/usr/bin/env bash
#for遍历列表,for假定空格分割
for word in "Hello World"
do
    echo $word
done

#按行读取文件
file="$0"
while read line
do
  echo $line
done < $file

#遍历目录
for file in ./*
do
  if [ -d "$file" ];then
    echo "$file is a directory"
  elif [ -f "$file" ];then
    echo "$file is a file"
  fi
done