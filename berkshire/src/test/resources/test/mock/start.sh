#! /bin/bash

echo 'print current dir'

pwd

cd  build/classes/

java -Djava.ext.dirs=../../lib/provided:../../lib/compile runtime.MockRpcServer
