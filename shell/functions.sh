#判断绝对路径
function isAbs() {
    if [[ "$1" =~ ^/.* ]];then
        return 0
    else
        return 1
    fi
}