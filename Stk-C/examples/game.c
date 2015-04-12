#include <std.h>
#include <stdio.h>
#include <stack.h>

void main() {

	int in;
	int goal;
	Stack* stack1;
	Stack* stack2;
	int i;

	printf("=== Number game ===\n");

	goal = 27;

	while (in != goal) {

		printf("game/number_game/Type number> ");
		in = getint(); while (getc() != '\n'){}

		if (in < goal) printf("Too small!\n");
		if (in > goal) printf("Too large!\n");

	}

	printf("You won!\n");

	printf("=== Stack game ===\n");

	stack1 = createStack();
	stack2 = createStack();

	for (i = 0; i < 2; i++) {

		printf("game/stack_game/Stack1> ");
		in = getint(); while (getc() != '\n'){}

		push(&stack1, in);

		printf("game/stack_game/Stack2> ");
		in = getint(); while (getc() != '\n'){}

		push(&stack2, in);

	}

	for (i = 0; i < 2; i++) {

		printf("game/stack_game/Stack2> ");
		in = getint(); while (getc() != '\n'){}

		if (in != pop(&stack2)) printf("Wrong!\n");

		printf("game/stack_game/Stack1> ");
		in = getint(); while (getc() != '\n'){}

		if (in != pop(&stack1)) printf("Wrong!\n");

	}

	return 0;
}
