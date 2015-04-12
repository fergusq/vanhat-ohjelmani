/*
 * errors.h
 *
 *  Created on: 3.9.2012
 *      Author: iikka
 */

#ifndef ERRORS_H_
#define ERRORS_H_


const int RUNTIME_EXCEPTION = 1;
const int STACK_UNDERFLOW_EXCEPTION = 2;
const int STACK_OVERFLOW_EXCEPTION = 3;
const int DIVIDE_BY_ZERO_EXCEPTION = 4;

const int RUNTIME_ERROR = 1;
const int VERIFICATION_ERROR = 2;

class RuntimeException {
public:
	int errorCode;
	char*msg;
	RuntimeException() {
		errorCode = RUNTIME_EXCEPTION;
	}
	RuntimeException(char* msg) {
		errorCode = RUNTIME_EXCEPTION;
		this->msg = msg;
	}
	RuntimeException(int error) {
		errorCode = error;
	}
};

class StackUnderFlowException : public RuntimeException {
public:
	StackUnderFlowException() {
		errorCode = STACK_UNDERFLOW_EXCEPTION;
	}
};

class StackOverFlowException : public RuntimeException {
public:
	StackOverFlowException() {
		errorCode = STACK_OVERFLOW_EXCEPTION;
	}
};

class DivideByZeroException : public RuntimeException {
public:
	DivideByZeroException() {
		errorCode = DIVIDE_BY_ZERO_EXCEPTION;
	}
};

class RuntimeError {
public:
	int errorCode;
	RuntimeError() {
		errorCode = RUNTIME_ERROR;
	}
};


#endif /* ERRORS_H_ */
