#include <std.h>
#include <stdio.h>

native int putchar(int c) __from "stdio.h";
native int getchar() __from "stdio.h";
native void raw() __from "ncurses.h";
native void initscr() __from "ncurses.h";
native void endwin() __from "ncurses.h";
native int getch() __from "ncurses.h";
native int mvaddch(int y, int x, char ch) __from "ncurses.h";

struct henkilö {
	char* nimi;
	int ikä;
};

typedef struct henkilö henkilö;

henkilö* createHenkilo() {
	return malloc(2);
}

int** makeMatrix(int width, int height, int fill);
void freeMatrix(int** matrix, int width, int height);
int div(int x, int y);
int fib(int x);
void set27(int* var);

void main() {

	int i;
	int *i1;
	int *j;
	int *k;
	int**matrix;
	henkilö* kalle;
	henkilö* taavetti;
	void* createHnkl;
	int x;

	printf("Hello World!\n");	/* Print "Hello World!"		*/

	printf("Function test\n");	/* Print Info			*/
	printn(div(8, 2));		/* Print 8/2 = 4		*/
	printn(fib(4));			/* Print Fibonacci's fourth num	*/

	/* Goto Test */

	printf("Goto Test\n");		/* Print Info			*/

	for (i = 0; i < 10; i=i+1) {
		printn(i);
		if (i == 5) goto exit;
	}
	define exit:

	printf("Pointer test\n");	/* Print Info			*/

	/* Pointer test */

	i1 = malloc(3);
	*i1 = 123;
	printn(i1);
	printn(*i1);

	j = malloc(2);
	*j = 123;
	printn(j);
	printn(*j);

	k = malloc(4);
	*k = 123;
	printn(k);
	printn(*k);

	//asm("memdump ");
	free(j);

	//asm("memdump ");
	free(i1);
	free(k);

	//asm("memdump ");


	matrix = makeMatrix(4, 4, 74);

	printf("Matrix[2][3] = %i\n", matrix[2][3]);

	freeMatrix(matrix, 4, 4);

	kalle = createHenkilo();
	kalle->nimi = "Kalle Koehenkilo";
	kalle->ikä = 58;

	//asm("memdump ");

	printf("%i: %s, ika %i vuotta\n", kalle, kalle->nimi, kalle->ikä/**/);

	//asm("memdump ");

	free(kalle->nimi);
	free(kalle);

	asm("#createHenkilo :createHnkl dupt pop");

	taavetti = call(createHnkl);

	taavetti->nimi = "Taavetti Tavallinen";
	taavetti->ikä = 34;

	printf("%i: %s, ika %i vuotta\n", taavetti, taavetti->nimi, taavetti->ikä);

	free(taavetti->nimi);
	free(taavetti);

	printf("Reference test\n");

	x = 5;

	printf("main, &x = %i, x = %i\n", &x, x);
	set27(&x);

	printf("main, &x = %i, x = %i\n", &x, x);

	putchar('t');
	putchar('\n');

	printn(getchar());
	putchar('\n');

	/* NCurses Test */

	initscr();	/* Enter to The NCurses Mode	*/
	raw();		/* Enable Instant Input		*/
	
	mvaddch(5, 5, '@');

	getch();	/* Wait for input		*/

	endwin();	/* Return from the NCurses Mode	*/

	return 0;		/* Return From The Function	*/
}

int div(int a, int b) {
	return a / b;		/* Return a divided by b	*/
}

int fib(
	int to
) {
	int a;
	int b;
	int c;
	int i;

	i = 0;
	
	a = 0;
	b = 1;
	c = 0;

	while (i < to) {
		c = a + b;
		a = b;
		b = c;
		i = i + 1;
	}

	return c;
}

int** makeMatrix(int width, int height, int fill) {
	int x;
	int y;
	int **matrix;

	matrix = malloc(width);
	for (x = 0; x < width; x++) {
		*(matrix+x) = malloc(height);
		for (y = 0; y < height; y++) {
			matrix[x][y] = fill;
		}
	}
	return matrix;
}

void freeMatrix(int** matrix, int width, int height) {
	int x;
	int y;

	for (x = 0; x < width; x++) {
		for (y = 0; y < height; y++) {
			matrix[x][y] = 0;
		}
		free(*(matrix+x));
	}
	free(matrix);
	return 0;
}

void set27(int* var) {
	printf("set27, var = %i, *var = %i\n", var, *var);

	*var = 27;
	return 0;
}
