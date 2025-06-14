#!/usr/bin/env bash
while getopts u:a:f: flag
do
    # shellcheck disable=SC2220
    case "${flag}" in
        u) username=${OPTARG};;
        a) age=${OPTARG};;
        f) fullname=${OPTARG};;
    esac
done
echo ${username}
echo ${age}
echo ${fullname}