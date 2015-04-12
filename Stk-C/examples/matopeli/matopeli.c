
/*
 * Matopeli
 * tehnyt Iikka Hauhio
 */

#include <std.h>
#include "pelikentta.h"
#include "curses.h"

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

void main() {

	initscr();
	call_api_keypad();
	raw();
	noecho();
	curs_set(0);
	
	struct Pelikentta* peli;
	peli = malloc(sizeof(Pelikentta));
	peli->otukset = malloc(100);
	peli->kartta = makeMatrix(32, 32, 0);
	int i;
	int j;
	for (i = 0; i < 32; i++) {
		for (j = 0; j < 32; j++) {
			peli->kartta[i][j] = malloc(sizeof(Solu));
			if (i == 0 || j==0 || i==31 || j==31) peli->kartta[i][j]->tyyppi = TYYPPI_SEINA;
			else peli->kartta[i][j]->tyyppi = TYYPPI_VAPAA;
		}
	}

	Mato* pelaaja;
	pelaaja = teeMato(5, 5, 4);

	peli->otukset[0] = pelaaja;
	peli->otukset[1] = 0;

	do {
	} while (paivitaPelikantta(peli));

	free(pelaaja);
	free(peli->otukset);
	free(peli);

	endwin();

	return 0;
}
