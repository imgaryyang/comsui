#! /bin/bash

ps -ef | grep "java.ext.dirs" | grep -v "grep" | awk '{print $2}' | xargs kill -9
