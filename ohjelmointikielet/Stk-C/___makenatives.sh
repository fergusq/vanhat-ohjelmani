gcc -c -fPIC ___natives.c -o ___natives.o
gcc -shared -Wl,-soname,libnatives.so.1 -o libnatives.so.1.0.1  ___natives.o
