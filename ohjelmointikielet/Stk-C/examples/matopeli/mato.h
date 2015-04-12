#ifndef __MATO_H
#define __MATO_H

#include <std.h>

struct Mato {
	int x;
	int y;
	int hannat;
	Mato* hanta;
};

typedef struct Mato Mato;

Mato* teeMato(int x, int y, int hannanPituus) {
	Mato*mato;
	Mato*mato2;
	Mato*mato3;
	int i;

	mato = malloc(sizeof(Mato));
	mato->x = x;
	mato->y = y;
	mato->hannat = hannanPituus;
	i = 0;
	mato3 = 0;
	while (i < hannanPituus) {
		i++;
		mato2 = malloc(sizeof(Mato));
		mato2->x = x;
		mato2->y = y;
		mato2->hannat = i;
		mato2->hanta = mato3;
		mato3 = mato2;
	}
	mato->hanta = mato3;
	return mato;
}

#endif
