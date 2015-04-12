
#include <stdio.h>

int i(int* x) {
	return *x + 1;
}

void main() {
	local callable int(int*) add;
	add = i;
	local int x;
	x = 5;
	printf("%i\n", add(&x));
	printf("%i\n", add(&x));
	printf("%i\n", add(&x));
}
