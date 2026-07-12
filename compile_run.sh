#!/bin/bash
cd "$(dirname "$0")"
mkdir -p bin
echo "Compiling..."
javac -cp "lib/*" -d bin src/*.java
if [ $? -ne 0 ]; then echo "Compilation FAILED."; exit 1; fi
echo "Starting app..."
java -cp "lib/*:bin" Main
