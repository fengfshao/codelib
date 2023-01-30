#!/usr/bin/env bash
BASE_DIR=$(cd $(dirname $0);pwd)

#line1=$(cat $1 |head -n 1)
#main="${line1:2}"
#echo "begin run $main..."

HADOOP_CLASSPATH=$HADOOP_HOME/etc/hadoop:$HADOOP_HOME/share/hadoop/tools/lib/*:$HADOOP_HOME/share/hadoop/hdfs/lib/*
java -cp $BASE_DIR/:$BASE_DIR/debug-helper-1.0.jar:$BASE_DIR/lib/*:$HADOOP_CLASSPATH -DdebugHome=$BASE_DIR $1