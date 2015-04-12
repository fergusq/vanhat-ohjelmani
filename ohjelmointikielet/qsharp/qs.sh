#!/bin/bash

PARAMS="-F $1"

# Q SHARP CONSOLE LAUNCH SCRIPT!
# ------- DO NOT MODIFY! -------

java -cp bin:../jcurses/lib/jcurses.jar qsharp.lang.Console $PARAMS
