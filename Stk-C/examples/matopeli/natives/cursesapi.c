
#ifndef __CURSESAPI_C
#define __CURSESAPI_C

#include <ncurses.h>

void call_api_keypad() {
	keypad(stdscr, TRUE);
}

#endif
