#!/bin/bash
for filename in /Data/*.txt; do
    for ((i=0; i<=3; i++)); do
        ./MyProgram.exe "$filename" "Logs/$(basename "$filename" .txt)_Log$i.txt"
    done
done

https://stackoverflow.com/questions/17420994/how-can-i-match-a-string-with-a-regex-in-bash

