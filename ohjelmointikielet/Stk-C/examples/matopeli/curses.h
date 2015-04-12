
#ifndef __CURSES_H
#define __CURSES_H

native void raw() __from "ncurses.h";
native void call_api_keypad() __from "natives/cursesapi.c";
native void noecho() __from "ncurses.h";
native void initscr() __from "ncurses.h";
native void endwin() __from "ncurses.h";
native void curs_set(int i) __from "ncurses.h";
native int getch() __from "ncurses.h";
native int mvaddch(int y, int x, char ch) __from "ncurses.h";

#endif
