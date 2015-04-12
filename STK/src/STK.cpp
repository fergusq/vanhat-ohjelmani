//============================================================================
// Name        : STK.cpp
// Author      : Iikka Hauhio
// Version     :
// Copyright   : (c) 2012 Iikka Hauhio
// Description : STK Interpreter in C++, Ansi-style
//============================================================================

// reading a complete binary file
#include <iostream>
#include <fstream>
#include <algorithm>
#include <vector>
#include <math.h>
#include <cstring>
#include <dlfcn.h>
#include "errors.h"
#include "opcodes.h"
using namespace std;

long int size;
char * memblock;

#define MAX_SIZE 1000
using namespace std;

class STValue {
protected:
double d; // NEW
public:
	STValue(){}
	virtual ~STValue(){}
	virtual int getInt() {
		cout << "DEBUG STValue" << endl;
		return d;
	}
	virtual double getDouble() {return d;}
	virtual STValue* Kloonaa() {return new STValue(*this);};
};

class SInt : public STValue {
private:
	int value;
public:
	SInt(int i) {value = i;d = i;}
	~SInt(){}
	int getInt() {
		cout << "DEBUG SInt " << value << endl;
		return value;
	}
	double getDouble() {return value;}
	STValue* Kloonaa() {return new SInt(*this);};

};
class SDouble : public STValue {
private:
	double value;
public:
	SDouble(int i) {value = i;d = i;}
	~SDouble() {}
	int getInt() {
		cout << "DEBUG SDouble" << endl;
		return value;
	}
	double getDouble() {return value;}
	STValue* Kloonaa() {return new SDouble(*this);};
};

class SValue {
private:
	STValue value;
public:
	SValue() {value =* new SInt(0);}
	SValue(STValue *s){value =* s;}
	~SValue(){}
	int getInt() {
		cout << "DEBUG SValue" << endl;
		return value.getInt();
	}
	double getDouble() {return value.getDouble();}
	SValue* Kloonaa() {return new SValue(*this);};
};

static char* fileName;


struct Node {
	int data;
	Node* link;
};

class ListStack {
private:
	Node* top;
	int count;

public:
	ListStack() {
		top = NULL;
		count = 0;
	}

	void Push(int element) {
		Node* temp = new Node();
		temp->data = element;
		temp->link = top;
		top = temp;
		count++;
	}

	int Pop() {
		if (top == NULL) {
			throw new StackUnderFlowException();
		}
		int ret = top->data;
		Node* temp = top->link;
		delete top;
		top = temp;
		count--;
		return ret;
	}

	int Top() {
		return top->data;
	}

	int Size() {
		return count;
	}

	bool isEmpty() {
		return (top == NULL) ? true : false;
	}
};

class ArrayStack {
private:
	int top;
public:
	double data[MAX_SIZE];
	//vector<double> data;
//public:
	ArrayStack() {
		top = -1;
		//data = new double[MAX_SIZE];
	}

	~ArrayStack() {
		//for (int i = this->Size() - 1; i >= 0; i--) {
		//	if (this->data[i] != 0) delete this->data[i];
		//}
		//delete[] data;
	}

	void Push(double element) {

		if (top >= MAX_SIZE) {
			throw new StackOverFlowException();
			/*int additionalSize = MAX_SIZE-top;
			double * tmp = new double[MAX_SIZE+additionalSize];
			for (int i = 0; i < top; i++) {
				tmp[i] = data[i];
				//cout << data[i] << ",";
			}
			delete[] data;
			data = tmp;*/
		}
		//++top;
		//data.push_back(element);
		data[++top] = element;
	}

	double Pop() {
		//cout << "Stack.POP " << endl;
		if (top == -1) {
			throw new StackUnderFlowException();
		}
		//double value = ;
		//data.pop_back();
		return data[top--];
	}

	double Top() {
		//cout << "TOP" << endl;
		return data[top];
	}

	void RotR() {
		ArrayStack stack(*this);
		double a = this->Top();
		for (int i = top; i >= 0; i--) {
			this->data[i + 1] = stack.data[i];
		}
		this->data[0] = a;
		//cout << "ROTR" << endl;
		//rotate(data.begin(),data.begin()-1,data.end());
	}

	void RotL() {
		ArrayStack stack(*this);
		double a = this->data[0];
				for (int i = 0; i < top; i++) {
					this->data[i] = stack.data[i + 1];
				}
				this->data[top] = a;
		//rotate(data.begin(),data.begin()+1,data.end());
	}

	void Reverse() {
		double data2[MAX_SIZE];
		//cout << "[";
		for (int i = 0, j = top; i <= top; i++,j--) {
			data2[j] = data[i];
			//cout << data[i] << ",";
		}
		//cout << "]" << endl;
		//cout << "[";
		for (int i = 0; i <= top; i++) {
			this->data[i] = data2[i];
			//cout << data[i] << ",";
		}
		//cout << "]" << endl;
		//cout << "REVERSE" << endl;
		//reverse(data.begin(), data.end());
	}

	void Dump() {
		cout << "[";
		for (int i = 0; i < top; i++) {
			cout << data[i] << ",";
		}
		cout << data[top];
		cout << "]" << endl;
	}

	void ErrDump() {
		cerr << "[";
		for (int i = 0; i < top; i++) {
			cerr << data[i] << ",";
		}
		cerr << data[top];
		cerr << "]" << endl;
	}

	int Size() {
		return top + 1;
	}

	bool isEmpty() {
		return (top == -1) ? true : false;
	}
};

class OP {
public:
	int op;
	signed int value;
	unsigned int line;
};




int TestStack() {
	ListStack s;
	try {
		if (s.isEmpty()) {
			cout << "Stack is empty" << endl;
		}
		// Push elements
		s.Push(100);
		s.Push(200);
		// Size of stack
		cout << "Size of stack = " << s.Size() << endl;
		// Top element
		cout << "POP " << s.Pop() << endl;
		// Pop element
		cout << "POP " << s.Pop() << endl;
		// Pop element
		cout << "POP " << s.Pop() << endl;
		// Pop element
		cout << "POP " << s.Pop() << endl;
	} catch (...) {
		cout << "Some exception occured" << endl;
	}

	ArrayStack s2;
	try {
		if (s2.isEmpty()) {
			cout << "Stack is empty" << endl;
		}
		// Push elements
		s2.Push(100);
		s2.Push(200);
		s2.Push(300);
		s2.Push(400);
		s2.Push(500);
		s2.RotL();
		s2.Reverse();
		// Size of stack
		cout << "Size of stack = " << s2.Size() << endl;
		// Pop element
		cout << "aPOP " << (int)s2.Pop() << endl;
		// Pop element
		cout << "bPOP " << (int)s2.Pop() << endl;
		// Pop element
		cout << "cPOP " << (int)s2.Pop() << endl;
		// Pop element
		cout << "dPOP " << (int)s2.Pop() << endl;
		cout << "ePOP " << (int)s2.Pop() << endl;
	} catch (...) {
		cout << "Some exception occured" << endl;
	}
	return 0;
}

char *append(const char *oldstring, const char c)
{
    int result;
    char *newstring;
    result = asprintf(&newstring, "%s%c", oldstring, c);
    if (result == -1) newstring = NULL;
    return newstring;
}

char *iappend(const char *oldstring, const int c)
{
    int result;
    char *newstring;
    result = asprintf(&newstring, "%s%i", oldstring, c);
    if (result == -1) newstring = NULL;
    return newstring;
}

char *catstr(const char *oldstring, const char* str)
{
    int result;
    char *newstring;
    result = asprintf(&newstring, "%s%s", oldstring, str);
    if (result == -1) newstring = NULL;
    return newstring;
}

bool beVerbose;
bool disassemble;
bool debugInfo;
typedef double (*FUNC)(double);

FUNC *sfuncs;
void sip(char code[]) {
	if (beVerbose) cout << "size: " << size << endl;
	if (beVerbose) cout << "disassemble: " << disassemble << endl;
	char * functions[size];
	vector<OP> code2;
	int count = 0;
	int lineCount = 0;
	if (beVerbose) cout << "=== Header ===" << endl;
	if (beVerbose) {
		cout << "Reading magic number...\t";
	}
	if (code[0] == 'S' && code[1] == 'B' && code[2] == 'C') {
		if (beVerbose) cout << "[DONE]" << endl;
	} else {
		if (beVerbose) cout << "[FAILED]" << endl;
		throw new RuntimeException();
	}
	bool wasLinef = false;
	for (int i = 3; i < size; i++) {
		OP op;
		op.op = code[i];

		if (op.op == LINE) {
			if ((wasLinef||!beVerbose) && disassemble) cout << endl;
			wasLinef = true;
			//if (beVerbose) cout << "=== Code ===" << endl;
			lineCount++;
			if (disassemble && beVerbose) cout << lineCount << "";
			continue;
		} else {
			wasLinef = false;
			if (disassemble && beVerbose) cout << "\t";
		}
		if (disassemble && beVerbose) {
			if (op.op == NOP) {
				if (functions[code2.size()]) {
					cout << functions[code2.size()] << ":";
				} else {
					cout << count++ << ": nop";
				}
			}
			else if (op.op < 100) cout << count++ << ": " << opArray[op.op];
			else if (op.op != LINE) cout << code2.size() << ": " << op.op;
		} else if (disassemble) {
			if (op.op == NOP) {
				if (functions[code2.size()]) {
					cout << functions[code2.size()] << ":";
				} else {
					cout << "nop";
				}
			}
			else if (op.op < 100 && op.op != PUSH && op.op != PUSH_INT && op.op != PUSH_DOUBLE) cout << opArray[op.op];
			else if (op.op != LINE && op.op != PUSH && op.op != PUSH_INT && op.op != PUSH_DOUBLE) cout << "<" << op.op << ">";
		}
		if (op.op == PUSH) {
			op.value = code[++i];
			if (disassemble && beVerbose) cout << ", " << op.value;
			else if (disassemble) cout << op.value;
		}
		if (op.op == PUSH_INT) {
			i++;
			op.value = (op.value << 8) + (int)(unsigned char)code[i+0];
			op.value = (op.value << 8) + (int)(unsigned char)code[i+1];
			op.value = (op.value << 8) + (int)(unsigned char)code[i+2];
			op.value = (op.value << 8) + (int)(unsigned char)code[i+3];
			//op.value = ((code[i+0]<<24)|(code[i+1]<<16)|(code[i+2]<<8)|(code[i+3]));
			if (disassemble && beVerbose) cout << ", " << op.value;
			else if (disassemble) cout << op.value;
			//if (disassemble) cout << " [" << (int)(unsigned char)code[i+0] << ", " << (int)(unsigned char)code[i+1] << ", " << (int)(unsigned char)code[i+2] << ", " << (int)(unsigned char)code[i+3] << "]";
			//op.value = code[++i];
			i+=3;
		}
		if (op.op == PUSH_DOUBLE) {
			// TODO tee push_double
		}
		if (disassemble && beVerbose) {
			if (op.op != LINE) cout << "\n";
		} else if (disassemble) {
			if (op.op != LINE) cout << " ";
		}
		if (op.op == FUNC_NAME) {
			i++;
			op.value = (op.value << 8) + (int)(unsigned char)code[i+0];
			op.value = (op.value << 8) + (int)(unsigned char)code[i+1];
			op.value = (op.value << 8) + (int)(unsigned char)code[i+2];
			op.value = (op.value << 8) + (int)(unsigned char)code[i+3];
			int pos = op.value;
			//op.value = ((code[i+0]<<24)|(code[i+1]<<16)|(code[i+2]<<8)|(code[i+3]));
			//if (disassemble) cout << ", " << op.value;
			//if (disassemble) cout << " [" << (int)(unsigned char)code[i+0] << ", " << (int)(unsigned char)code[i+1] << ", " << (int)(unsigned char)code[i+2] << ", " << (int)(unsigned char)code[i+3] << "]";
			//op.value = code[++i];
			i+=3;
			int length = code[++i];
			i++;
			char * name = "";
			int j;
			for (j = 0; j < length; j++) {
				name = append(name, code[i+j]);
			}
			i+=j-1;
			//i++;
			if (beVerbose) cout << "found function " << name << " at " << pos << endl;
			functions[pos] = name;
			continue;
		}
		if (op.op == FILENAME) {
			int length = code[++i];
			i++;
			char * name = "";
			int j;
			for (j = 0; j < length; j++) {
				name = append(name, code[i+j]);
			}
			i+=j-1;
			//i++;
			if (beVerbose) cout << "== File " << name << " ==" << endl;
			fileName = name;
			continue;
		}
		if (op.op == CODE_START) {
			if (beVerbose) cout << "=== Code ===" << endl;
			lineCount = 0;
			continue;
		}
		op.line = lineCount;
		code2.push_back(op);
		//if (disassemble) cout << i-3 << ": " << opArray[op.op] << "," << op.value << endl;
	}
	if (disassemble) return;
	vector<ArrayStack > stack;
	vector<int> pcStack;
	vector<int> lineStack;
	vector<int> callStack;

	pcStack.push_back(0);
	lineStack.push_back(0);
	callStack.push_back(0);

	ArrayStack tmt_stack;
	stack.push_back(tmt_stack);

	double memory[300000];

	int PC = 0;
	int DC = 0;

	if (beVerbose) cout << "=== Program started ===" << endl;

	bool DEBUG = debugInfo;

	int currLine = 0;


	try {

	while (1) {
		pcStack[DC] = PC;
		lineStack[DC] = 0;

		OP op = code2.at(PC);
		currLine = op.line;

		lineStack[DC] = currLine;

		if ((DEBUG)) {

		stack[DC].Dump();

		cout << PC << ": " << opArray[op.op];
		if ((op.op == PUSH|| op.op == PUSH_INT)) {
			cout << ", " << op.value;
		}
		cout << endl;

		}
		switch (op.op) {
			case NOP:
				PC++;
				break;
			case PUSH:
			case PUSH_INT:
				stack.at(DC).Push(op.value);
				PC++;
								break;
			case POP:
				stack.at(DC).Pop();
				PC++;
								break;
			case DUP:
				stack.at(DC).Push((stack.at(DC).Top()));
				PC++;
								break;
			case REVERSE:
				stack.at(DC).Reverse();
				PC++;
								break;
			case EXCH:
			{
				double a5 = stack.at(DC).Pop();
				double b5 = stack.at(DC).Pop();
				stack.at(DC).Push(a5);
				stack.at(DC).Push(b5);
				PC++;
			}					break;
			case OVER:
				stack.at(DC).Push(stack.at(DC).data[0]);
				PC++;
								break;
			case ROTL:
				stack.at(DC).RotL();
				PC++;
								break;
			case ROTR:
				stack.at(DC).RotR();
				PC++;
								break;
			case ADD:
			{
				double x = 	stack.at(DC).Pop();
				double y =		stack.at(DC).Pop();
				stack.at(DC).Push(((x+y)));
				PC++;
			}break;
			case SUB:
			{
				double x2 = 	stack.at(DC).Pop();
				double y2 =		stack.at(DC).Pop();
				stack.at(DC).Push(((y2-x2)));
				PC++;
			}break;
			case MUL:
			{
				double x3 = 	stack.at(DC).Pop();
				double y3 =		stack.at(DC).Pop();
				stack.at(DC).Push(((x3*y3)));
				PC++;
			}break;
			case DIV:
			{
				double x4 = 	stack.at(DC).Pop();
				double y4 =		stack.at(DC).Pop();
				if (x4 == 0) {
					throw new DivideByZeroException();
				}
				//cout << endl << "div: " << (y4)/(x4) << endl;
				stack.at(DC).Push((double)(y4)/(x4));
				PC++;
			}break;
			case EXP:
			{
				double x4 = 	stack.at(DC).Pop();
				double y4 =		stack.at(DC).Pop();
				stack.at(DC).Push(pow(y4,x4));
				PC++;
			}break;
			case MOD:
			{
				int x4 = 		stack.at(DC).Pop();
				int y4 =		stack.at(DC).Pop();
				stack.at(DC).Push((y4%x4));
				PC++;
			}break;
			case EQ:
			{
				double x5 = 	stack.at(DC).Pop();
				double y5 =		stack.at(DC).Pop();
				stack.at(DC).Push(((x5==y5)));
				PC++;
			}break;
			case GT:
			{
				double x6 = 	stack.at(DC).Pop();
				double y6 =		stack.at(DC).Pop();
				stack.at(DC).Push(((x6<y6)));
				PC++;
			}break;
			case LT:
			{
				double x7 = 	stack.at(DC).Pop();
				double y7 =		stack.at(DC).Pop();
				stack.at(DC).Push(((x7>y7)));
				PC++;
			}break;
			case AND:
			{
				int x8 = 	stack.at(DC).Pop();
				int y8 =		stack.at(DC).Pop();
				stack.at(DC).Push(((x8&&y8)));
				PC++;
			}break;
			case OR:
			{
				int x9 = 	stack.at(DC).Pop();
				int y9 =		stack.at(DC).Pop();
				stack.at(DC).Push(((x9||y9)));
				PC++;
			}break;
			case XOR:
			{
				int x10 = 	stack.at(DC).Pop();
				int y10 =	stack.at(DC).Pop();
				stack.at(DC).Push(((x10^y10)));
				PC++;
			}break;
			case NOT:
			{
				double x11 = 	stack.at(DC).Pop();
				stack.at(DC).Push(!x11);
				PC++;
			}break;
			case PRINT:
			{
				int x12 = 		stack.at(DC).Pop();
				double y12 =	stack.at(DC).Pop();
				if (x12 == 0) {
					//cout << "";
					cout << y12;
					//printf("%i", y12);
					//cout.flush();
				} else if (x12 == 1) {
					if (y12 != 0)
					cout<< ((char) y12);
				}
				else if (x12 == 2) {
					cerr << y12;
				} else if (x12 == 3) {
					if (y12 != 0)
					cerr<< ((char) y12);
				}
				cout.flush();
				PC++;
			}break;
			case READ:
			{
				int x13 = 	stack.at(DC).Pop();
				if (x13 == 0) {
					int y13;
					cin >> y13;
					stack.at(DC).Push(y13);
				}
				if (x13 == 1) {
					char y13;
					cin.get(y13);
					stack.at(DC).Push(y13);
				}
				PC++;
			}break;
			case PRINTM:
			{
				int x14 = 	stack.at(DC).Pop();
				double y14 =		stack.at(DC).Pop();

				if (x14 > 300000) {
					x14 -= 300000;
					int _dc = x14 / MAX_SIZE;
					int in = x14 % MAX_SIZE;

					stack.at(_dc).data[in] = y14;
				} else

				memory[x14] = y14;
				PC++;
			}break;
			case READM:
			{
				int x15 = 	stack.at(DC).Pop();

				if (x15 > 300000) {
					x15 -= 300000;
					int _dc = x15 / MAX_SIZE;
					int in = x15 % MAX_SIZE;

					stack.at(DC).Push(stack.at(_dc).data[in]);
				} else

				stack.at(DC).Push(memory[x15]);
				PC++;
			}break;
			case GOTO:
			{
				int x16 = 	stack.at(DC).Pop();
				int y16 =		stack.at(DC).Pop();
				if (x16 > 0) PC = y16;
				else PC++;
			}break;
			case THROW:
				throw new RuntimeException(stack.at(DC).Pop());
			case CALLF:
			{
				int function = stack.at(DC).Pop();
				double parameter = stack.at(DC).Pop();
				double answer = (*sfuncs[function])(parameter);
				stack.at(DC).Push(answer);
				PC++;
			} break;
			case OVERF:
			{
				int from = stack.at(DC).Pop();
				if (from >= stack[DC].Size()) throw new RuntimeException("Stack index out of bounds");
				stack.at(DC).Push(stack[DC].data[from]);
				PC++;
			} break;
			case DUPT:
			{
				int from = stack.at(DC).Pop();
				double value = stack.at(DC).Top();
				stack.at(DC).data[from] = value;
				PC++;
			} break;
			case REF:{
				int from = stack.at(DC).Pop();
				stack.at(DC).Push(300000 + DC * MAX_SIZE + from);
				++PC;
			}break;
			case PUSHP:
			case PUSHR:
			{
				int goto1 = stack.at(DC).Pop();
				int x17;
				if (op.op == PUSHP)  x17 = stack.at(DC).Pop();
				ArrayStack i;
				stack.push_back(i);
				if (op.op == PUSHP) for (int j = 0; j < x17; j++) {
					double i = stack.at(DC).Pop();
					stack.at(DC+1).Push(i);
				}

				pcStack[DC] =  PC;
				lineStack[DC] =  currLine;
				DC++;
				pcStack.push_back(goto1);
				lineStack.push_back(-1);
				callStack.push_back(goto1);
				PC = goto1;
				if (DEBUG) {
					cout << "PUSH " << PC << "," << DC << ", args: [" << x17;
					stack[DC].Dump();
					cout << "]" << endl;
				}

				break;
			}break;
			case POPR:
			{

				double i2 = stack[DC].Size() > 0 ? stack.at(DC).Pop() : 0;

				stack.pop_back();

				DC--;
				if (DC <= -1) {
					goto out;
				}
				PC = pcStack.at(pcStack.size()-2);
				if(DEBUG) cout << "POP " << PC << ", " << DC << endl;
				//int goto1 = pcStack.at(pcStack.size()-1);
				//pcStack.pop_back();
				pcStack.pop_back();
				lineStack.pop_back();
				callStack.pop_back();
				//pcStack.push_back(goto1);
				stack.at(DC).Push(i2);
				++PC;
			}break;
			case MEMDUMP:
			{
				cout << "Memory Dump [";
				for (int i2 = 0; i2 < memory[0]; i2++) {
					cout << memory[i2];
					if (i2 != memory[0]-1) cout << ", ";
				}
				cout << "] ";
				cout << "Stack Dump: ";
				stack[DC].Dump();
				++PC;
			}break;
			default:
			{
				cout << "Unknown instruction " + op.op << "!" << endl;
				throw new RuntimeException("Unknown instruction!");
			}break;
		}}
	} catch (RuntimeException *ex) {
		//cout << "RuntimeException" << endl;
		switch (ex->errorCode) {
			case RUNTIME_EXCEPTION:
				cerr << "RuntimeException";
				if (ex->msg != 0) {
					cerr << " (" << ex->msg << ")";
				}
				cerr << endl;
				break;
			case STACK_OVERFLOW_EXCEPTION:
				cerr << "StackOverflowException" << endl;
				break;
			case STACK_UNDERFLOW_EXCEPTION:
				cerr << "StackUnderflowException" << endl;
				break;
			case DIVIDE_BY_ZERO_EXCEPTION:
				cerr << "DivideByZeroException" << endl;
				break;
			default:
				cerr << "RuntimeException (" << hex << ex->errorCode << dec << ")" << endl;
				break;
		}
		OP op = code2.at(PC);
		cerr << "\t" << "on (" << PC << ") " << opArray[op.op] << ", " << op.value << endl;
		for (int i = callStack.size()-1; i >= 0; i--) {
			if (functions[callStack[i]]) {
				cerr << "\t" << "at " << functions[callStack[i]] << "+" << hex << (pcStack[i]-callStack[i]) <<
						dec << " (" << fileName << ":" << lineStack[i] << ")" << endl;
			}
			else cerr << "\t" << "at " << callStack[i] << "+" << hex << (pcStack[i]-callStack[i]) <<
					dec << " (" << fileName << ":" << lineStack[i] << ")" << endl;
		}

		/*cerr << "Memory Dump [";
		for (int i2 = 0; i2 < memory[0]; i2++) {
			cerr << memory[i2];
			if (i2 != memory[0]-1) cerr << ", ";
		}
		cerr << "]\n";*/

		cerr << "Stack Dump: ";
		stack[DC].ErrDump();
	} catch (RuntimeError *ex) {
		//cout << "RuntimeException" << endl;
		switch (ex->errorCode) {
			case RUNTIME_ERROR:
				cerr << "RuntimeError" << endl;
				break;
			case VERIFICATION_ERROR:
				cerr << "VerificationError" << endl;
				break;
			default:
				cerr << "RuntimeError (" << hex << ex->errorCode << dec << ")" << endl;
				break;
		}
		for (int i = callStack.size()-1; i >= 0; i--) {
			if (functions[callStack[i]]) {
				cerr << "\t" << "at " << functions[callStack[i]] << "+" << hex << (pcStack[i]-callStack[i]) <<
						dec << " (" << fileName << ":" << lineStack[i] << ")" << endl;
			}
			else cerr << "\t" << "at " << callStack[i] << "+" << hex << (pcStack[i]-callStack[i]) <<
					dec << " (" << fileName << ":" << lineStack[i] << ")" << endl;
		}
	}



	out:
	if (beVerbose) cout << "=== Program terminated ===\n";
	return;
}

int main (int argc, char* argv[]) {
	//TestStack();
	//return 0;
	char* libraries = 0;
	for (int i = 1; i < argc; i++) {
		char* arg = argv[i];
		if (!strcmp(arg, "-V")) {
			beVerbose = true;
			continue;
		}
		if (!strcmp(arg, "-D")) {
			cout << "Disassembly" << endl;
			disassemble = true;
			continue;
		}
		if (!strcmp(arg, "-d")) {
			cout << "Debug" << endl;
			debugInfo = true;
			continue;
		}
		if (!strcmp(arg, "-l")) {
			libraries = argv[++i];
			cout << "Using library file " << libraries << endl;
			continue;
		}
		if (!strcmp(arg, "-h") || !strcmp(arg, "--help")) {
			cout << "Usage: stk [options] file" << endl
			<< "Options:" << endl
			<< "\t-V\t\t\tbe verbose" << endl
			<< "\t-D\t\t\tdisassemble" << endl
			<< "\t-d\t\t\tshow debug information" << endl
			<< "\t-l lib\t\t\tlibrary" << endl
			<< "\t-h\t--help\t\tshow this help text" << endl
			<< "\t-v\t--version\tshow version" << endl;
			return 0;
		}
		if (!strcmp(arg, "-v") || !strcmp(arg, "--version")) {
			cout << "Version " << version1 << "." << version2 << "." << version3 << endl;
			return 0;
		}
		if (i < argc-1) cout << "Unknown argument: " << arg << endl;
	}
	fileName = argv[argc-1];

	void* handle;

	if (libraries) {
		switch (libraries[0]) {
			case '/':
				break;
			case '~':
				libraries = &libraries[1];

				break;
			default:
				libraries = catstr("./", libraries);
				break;
		}
		handle = dlopen(libraries, RTLD_LAZY);
		if (!handle) {
			cerr << dlerror() << endl;
			exit(EXIT_FAILURE);
		}
		dlerror();
		int (*func)(void);
		*(void **) (&func) = dlsym(handle, "functions");
		char*error;
		if ((error = dlerror()) != NULL) {
			fprintf(stderr, "%s\n", error);
			exit(EXIT_FAILURE);
		}
		int functions = (*func)();

		sfuncs = new FUNC[functions];

		double (*func1)(double);
		for (int i = 0; i < functions; i++) {
			*(void **) (&func1) = dlsym(handle, iappend("func", i));
			sfuncs[i] = func1;
		}
	}


  ifstream file (argv[argc-1], ios::in|ios::binary|ios::ate);
  if (file.is_open())
  {
    size = file.tellg();
    memblock = new char [size];
    file.seekg (0, ios::beg);
    file.read ((char*)memblock, size);
    sip(memblock);
    file.close();

    if (beVerbose) cout << "exit()\n";

    delete[] memblock;
  }
  else cout << "Unable to open file " << fileName << endl;

  if (libraries) {
	  dlclose(handle);
	  delete[] sfuncs;
  }

  return 0;
}

