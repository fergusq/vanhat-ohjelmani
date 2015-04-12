/*
 * stacks.h
 *
 *  Created on: 3.9.2012
 *      Author: iikka
 */

#ifndef STACKS_H_
#define STACKS_H_


#define MAX_SIZE 200
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
	}

	~ArrayStack() {
		//for (int i = this->Size() - 1; i >= 0; i--) {
		//	if (this->data[i] != 0) delete this->data[i];
		//}
	}

	void Push(double element) {

		if (top >= MAX_SIZE) {
			throw new StackOverFlowException();
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

#endif /* STACKS_H_ */
