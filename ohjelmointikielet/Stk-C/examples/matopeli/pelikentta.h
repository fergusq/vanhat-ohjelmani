
#ifndef __PELIKENTTA_H
#define __PELIKENTTA_H

#include <std.h>
#include "curses.h"
#include "mato.h"

#define		TYYPPI_VAPAA	0
#define		TYYPPI_OTUS	1
#define		TYYPPI_SEINA	2

struct Solu {
	int tyyppi;
	Mato* otus;
};

typedef struct Solu Solu;

struct Pelikentta {
	Solu*** kartta;
	Mato** otukset;
};

typedef struct Pelikentta Pelikentta;

void piirraPelikantta(Pelikentta* peli, Mato* mato1) {
	local int i;
	local int j;
	local Mato* mato;
	for (i = 0; i < 32; i++) {
		for (j = 0; j < 32; j++) {
			if (peli->kartta[i][j]->tyyppi == TYYPPI_OTUS) {
				
				mato = peli->kartta[i][j]->otus;
				if (mato1 != mato) mvaddch(mato->y, mato->x, 'm');
				else mvaddch(mato->y, mato->x, '@');
			} else if (peli->kartta[i][j]->tyyppi == TYYPPI_SEINA) {
				mvaddch(j, i, '#');
			} else {
				mvaddch(j, i, '.');
				mato = 0;
			}
		}
	}


}

int paivitaPelikantta(Pelikentta* peli) {
	int to;
	int mv;
	Mato*h1;
	Mato*h2;
	int x;
	int y;
	int x2;
	int y2;

	int i;
	for (i = 0; peli->otukset[i]; i++) {
		piirraPelikantta(peli, peli->otukset[i]);

		peli->kartta[peli->otukset[i]->x][peli->otukset[i]->y]->otus = 0;
		peli->kartta[peli->otukset[i]->x][peli->otukset[i]->y]->tyyppi = TYYPPI_VAPAA;

		x = peli->otukset[i]->x;
		y = peli->otukset[i]->y;

		mv = FALSE;

		to = getch();
		if (to == 'a' && peli->kartta[peli->otukset[i]->x-1][peli->otukset[i]->y]->tyyppi == TYYPPI_VAPAA) {peli->otukset[i]->x = peli->otukset[i]->x - 1;mv=TRUE;}
		if (to == 'd' && peli->kartta[peli->otukset[i]->x+1][peli->otukset[i]->y]->tyyppi == TYYPPI_VAPAA) {peli->otukset[i]->x = peli->otukset[i]->x + 1;mv=TRUE;}
		if (to == 'w' && peli->kartta[peli->otukset[i]->x][peli->otukset[i]->y-1]->tyyppi == TYYPPI_VAPAA) {peli->otukset[i]->y = peli->otukset[i]->y - 1;mv=TRUE;}
		if (to == 's' && peli->kartta[peli->otukset[i]->x][peli->otukset[i]->y+1]->tyyppi == TYYPPI_VAPAA) {peli->otukset[i]->y = peli->otukset[i]->y + 1;mv=TRUE;}
		if (to == 'q') return FALSE;

		if (mv) {
			h1 = peli->otukset[i];
			h2 = 0;
			while(h1->hannat) {
				h1 = h1->hanta;
				x2 = h1->x;
				y2 = h1->y;
				peli->kartta[h1->x][h1->y]->otus = 0;
				peli->kartta[h1->x][h1->y]->tyyppi = TYYPPI_VAPAA;
				h1->x = x;
				h1->y = y;
				x = x2;
				y = y2;
				peli->kartta[h1->x][h1->y]->otus = h1;
				peli->kartta[h1->x][h1->y]->tyyppi = TYYPPI_OTUS;
			}
		}

		peli->kartta[peli->otukset[i]->x][peli->otukset[i]->y]->otus = peli->otukset[i];
		peli->kartta[peli->otukset[i]->x][peli->otukset[i]->y]->tyyppi = TYYPPI_OTUS;
	}
	return TRUE;
}

#endif
