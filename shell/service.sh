#!/usr/bin/env bash
BASE_DIR=$(cd $(dirname $0);pwd)
LOG_DIR=$BASE_DIR/logs
#echo $BASE_DIR
JVM_ARGS="-Xms512m -Xmx512m -XX:MaxDirectMemorySize=64m
    -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError
    -Xloggc:$LOG_DIR/gc.log
    -XX:+UseGCLogFileRotation
    -XX:NumberOfGCLogFiles=5
    -XX:GCLogFileSize=20480k"

start () {
  running=$(status)
  if [ "x$running" == "xtrue" ]; then
    echo "agent already running."
    return
  fi

  version="unknown"
  for f in $(ls "$BASE_DIR/lib"); do
    if [[ $f =~ ^pulsaragent ]]; then
      l=$((${#f}-16))
      version=${f:12:$l}
    fi
  done

  echo "starting agent of version $version ..."
  mkdir -p "$LOG_DIR"
  cmd="java $JVM_ARGS -cp $BASE_DIR/:$BASE_DIR/lib/* -DpulsarAgentHome=$BASE_DIR -DagentVersion=$version com.xxx.pcg.video.FSJavaAgent"

  $cmd 2>&1 >"$LOG_DIR"/stdout.log &
  if [ "$?" -eq "0" ]; then
     echo "finished start agent."
     echo $!>$BASE_DIR/agent.pid
  else
     echo "start agent failed."
     kill -9 "$!"
  fi
}

status () {
  retval="false"
  if [ -e $BASE_DIR/agent.pid ]; then
    if kill -0 `cat $BASE_DIR/agent.pid`> /dev/null 2>&1; then
      retval="true"
    fi
  fi
  echo "$retval"
}

stop () {
  running=$(status)
  if [ "x$running" == "xfalse" ]; then
    echo "agent not running."
    return
  fi
  kill `cat $BASE_DIR/agent.pid`
  rm $BASE_DIR/agent.pid
  echo "finished stop agent."
}

main() {
  case "$1" in
      start)
         start
         ;;
      stop)
         stop
         ;;
      restart)
         $0 stop
         $0 start
         ;;
      status)
         running=$(status)
         if [ "x$running" == "xtrue" ]; then
            echo agent is running, pid=`cat $BASE_DIR/agent.pid`
         else
            echo agent is NOT running
            exit 1
         fi
         ;;
      *)
         echo "Usage: $0 {start|stop|status|restart}"
  esac
}

main "$@"
  cmd="java $JVM_ARGS -cp $BASE_DIR/:$BASE_DIR/lib/* -DpulsarAgentHome=$BASE_DIR -DagentVersion=$version com.tencsssssssssssssss