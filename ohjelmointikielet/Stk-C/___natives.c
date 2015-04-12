#include "stdio.h"
int _putchar_c;
double func0(double d){ /* c */
	 _putchar_c = (int)d;
	return 0;
}
double func1(double d){ /* putchar */
	return (double)putchar((int)_putchar_c);
}
#include "stdio.h"
double func2(double d){ /* getchar */
	return (double)getchar();
}
#include "ncurses.h"
double func3(double d){ /* raw */
	raw();
	return 0;
}
#include "ncurses.h"
double func4(double d){ /* initscr */
	initscr();
	return 0;
}
#include "ncurses.h"
double func5(double d){ /* endwin */
	endwin();
	return 0;
}
#include "ncurses.h"
double func6(double d){ /* getch */
	return (double)getch();
}
#include "ncurses.h"
int _mvaddch_y;
double func7(double d){ /* y */
	 _mvaddch_y = (int)d;
	return 0;
}
int _mvaddch_x;
double func8(double d){ /* x */
	 _mvaddch_x = (int)d;
	return 0;
}
char _mvaddch_ch;
double func9(double d){ /* ch */
	 _mvaddch_ch = (char)d;
	return 0;
}
double func10(double d){ /* mvaddch */
	return (double)mvaddch((int)_mvaddch_y, (int)_mvaddch_x, (char)_mvaddch_ch);
}
int functions(){return 11;}

