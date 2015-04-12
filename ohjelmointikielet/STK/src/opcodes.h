/*
 * opcodes.h
 *
 *  Created on: 3.9.2012
 *      Author: iikka
 */

#ifndef OPCODES_H_
#define OPCODES_H_

using namespace std;

	const int NOP = 			0;
	const int PUSH = 			1;
	const int PUSH_INT = 		2;
	const int PUSH_DOUBLE = 	3;
	const int POP = 			4;
	const int DUP = 			5;
	//const int DROP = 4;
	const int REVERSE = 		6;
	const int EXCH = 			7;
	const int OVER = 			8;
	const int ROTL = 			9;
	const int ROTR = 			10;
	const int ADD = 			11;
	const int SUB = 			12;
	const int MUL = 			13;
	const int DIV = 			14;
	const int EXP = 			15;
	const int MOD = 			16;
	const int EQ = 				17;
	const int GT = 				18;
	const int LT = 				19;
	const int AND = 			20;
	const int OR = 				21;
	const int XOR = 			22;
	const int NOT = 			23;
	const int PRINT = 			24;
	const int READ = 			25;
	const int PRINTM = 			26;
	const int READM = 			27;
	const int GOTO = 			28;
	const int PUSHR = 			29;
	const int POPR = 			30;
	const int PUSHP = 			31;
	const int POPP = 			32;
	const int THROW = 			33;
	const int CALLF = 			34;
	const int OVERF = 			35;
	const int DUPT = 			36;
	const int REF = 			37;
	const int MEMDUMP = 		100;
	const int FUNC_NAME = 		101;
	const int CODE_START = 		102;
	const int FILENAME = 		103;
	const int LINE = 			104;

	string opArray[38] = {
		"nop",
		"push",
		"push_int",
		"push_double",
		"pop",
		"dup",
		"reverse",
		"exch",
		"over",
		"rotl",
		"rotr",
		"add",
		"sub",
		"mul",
		"div",
		"exp",
		"mod",
		"eq",
		"gt",
		"lt",
		"and",
		"or",
		"xor",
		"not",
		"print",
		"read",
		"printm",
		"readm",
		"goto",
		"pushr",
		"popr",
		"pushp",
		"popp",
		"throw",
		"callf",
		"overf",
		"dupt",
		"ref"
	};


	const int version1 = 0;
	const int version2 = 0;
	const int version3 = 1;



#endif /* OPCODES_H_ */
