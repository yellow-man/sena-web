#!/bin/sh

activator "run -Dconfig.file=conf/application-local-dev.conf -Dlogger.file=conf/logger-local.xml 9001" -jvm-debug 9999