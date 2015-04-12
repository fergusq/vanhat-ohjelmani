package org.kaivos.sc;

public class OPCODE {
	
	public static final int NOP = 			0;
	public static final int PUSH = 			1;
	public static final int PUSH_INT = 		2;
	public static final int PUSH_DOUBLE = 	3;
	public static final int POP = 			4;
	public static final int DUP = 			5;
	//public static final int DROP = 4;
	public static final int REVERSE = 		6;
	public static final int EXCH = 			7;
	public static final int OVER = 			8;
	public static final int ROTL = 			9;
	public static final int ROTR = 			10;
	public static final int ADD = 			11;
	public static final int SUB = 			12;
	public static final int MUL = 			13;
	public static final int DIV = 			14;
	public static final int EXP = 			15;
	public static final int MOD = 			16;
	public static final int EQ = 			17;
	public static final int GT = 			18;
	public static final int LT = 			19;
	public static final int AND = 			20;
	public static final int OR = 			21;
	public static final int XOR = 			22;
	public static final int NOT = 			23;
	public static final int PRINT = 		24;
	public static final int READ = 			25;
	public static final int PRINTM = 		26;
	public static final int READM = 		27;
	public static final int GOTO = 			28;
	public static final int PUSHR = 		29;
	public static final int POPR = 			30;
	public static final int PUSHP = 		31;
	public static final int POPP = 			32;

	public static final SValue FALSE = new SBoolean(false);
	public static final SValue TRUE = new SBoolean(true);
	
	public static final String[] op = {
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
		"popp"
	};
	
	public static final int PP_INITS = 0; // INIT Stack
	public static final int PP_DUPC = 1;  // DUPlicate and Carry variable
	public static final int PP_SECV = 2;  // SEt and Carry Variable
	public static final String[] pp_op = {
		"inits",
		"dupc",
		"secv",
	};
}
