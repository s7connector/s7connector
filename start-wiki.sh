#!/bin/sh

echo Starting server @ http://127.0.0.1:8088/index.html

sudo docker run \
 -v ${PWD}:/media\
 -p 127.0.0.1:8088:80\
 -it --rm\
 sashgorokhov/webdav
