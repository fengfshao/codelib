#!/usr/bin/env bash
indices=("tmk_leads" "tmk_leads_tmk_lesson" "tmk_batch" "tmk_leads_tmk")
len=`expr ${#indices[@]} - 1`
for docIndex in $(seq 0 $len)
do
 echo ${indices[$docIndex]}
done

#获取所有,部分
echo ${indices[@]}
echo "${indices[@]}"
#arr1= ${indices[@]:0:1}
CMD=("${indices[@]:0:3}")
echo "${CMD[@]}"
#获取长度
COUNT=${#indices[@]}
echo $COUNT
