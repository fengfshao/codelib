#!/bin/bash
#
# 通用输入输出函数

if [[ -z "$DEBUG_LEVEL" ]]; then
  DEBUG_LEVEL="INFO"
else
  DEBUG_LEVEL=$(echo ${DEBUG_LEVEL} | tr '[a-z]' '[A-X]')
fi

readonly DEBUG_LEVEL
readonly DEBUG_LEVEL_ARRAY=("DEBUG" "INFO" "WARN" "ERROR")

case "${DEBUG_LEVEL}" in
  ${DEBUG_LEVEL_ARRAY[0]}) DEBUG=0 ;;
  ${DEBUG_LEVEL_ARRAY[1]}) DEBUG=1 ;;
  ${DEBUG_LEVEL_ARRAY[2]}) DEBUG=2 ;;
  ${DEBUG_LEVEL_ARRAY[3]}) DEBUG=3 ;;
esac

readonly DEBUG


##################################################
# ERROR 日志输出函数
# Globals:
#   debug -- 日志级别
# Arguments:
#   输入的字符串
# Returns:
#   None
##################################################
err() {
  [[ ${DEBUG} -le 3 ]] && echo "$(date +'%Y/%m/%d %H:%M:%S') ERROR: $*" >&2
  return 0
}

##################################################
# WARN 日志输出函数
# Globals:
#   debug -- 日志级别
# Arguments:
#   输入的字符串
# Returns:
#   None
##################################################
warn() {
  [[ ${DEBUG} -le 2 ]] && echo "$(date +'%Y/%m/%d %H:%M:%S') WARN: $*" >&1
  return 0
}

info() {
  [[ ${DEBUG} -le 1 ]] && echo "$(date +'%Y/%m/%d %H:%M:%S') INFO: $*" >&1
  return 0
}

debug() {
  [[ ${DEBUG} -le 0 ]] && echo "$(date +'%Y/%m/%d %H:%M:%S') DEBUG: $*" >&1
  return 0
}
