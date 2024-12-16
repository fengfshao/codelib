#!/usr/bin/env bash
host="10.33.131.178"
user="homework"

while [ -n "$1" ]
do
  case "$1" in
  -a) host="10.33.131.178" ;;
  -t) host="172.28.16.37"  ;;
  -d) host="192.168.236.238" ;;
  -r) user="root" ;;
  -h) host="$2" 
	   shift ;;
  -u) user="$2" 
	   shift ;;
  *) echo "$1 is not an option";exit 1 ;;
  esac
  shift
done

echo $host $user
ac=$(python ~/sh/google_auth.py)
pswd="Shape.123"
#解决expect自动ssh登录后无法使用lrzsz
export LC_CTYPE=en_US
~/sh/nk.ex $ac $pswd "shaofengfeng" $host $user
