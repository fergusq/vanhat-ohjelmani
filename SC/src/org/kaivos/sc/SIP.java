package org.kaivos.sc;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl;

public class SIP {
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		if (args.length < 1) {
			System.out.println("Usage: java org.kaivos.SIP <file>");
			return;
		}
		
		boolean debug = false;
		boolean decompile = false;
		
		if (args.length > 1) {
			if (args[1].equals("-d")) debug = true;
			if (args[1].equals("-dc")) decompile = true;
		}
		
		String filen2 = args[0];
		File file2 = new File(filen2);
		DataInputStream in = null;
		try {
			in = new DataInputStream(new FileInputStream(file2));
		} catch (FileNotFoundException e) {e.printStackTrace(); return;
		}

		byte[] header = new byte[3];
		try {
			in.read(header);
		} catch (IOException e) {e.printStackTrace(); return;
		}
		if (header[0] != 'S' || header[1] != 'B' || header[2] != 'C') {
			System.err.println("Wrong header!");
			//return;
		}
		
		
		
		ArrayList<Token> code = new ArrayList<Token>();
		
		try {
			while (true) {
				int i = in.read();
				
				if (i == -1) break;
				
				Token t = new Token();
				t.op = i;
				if (i == OPCODE.PUSH_INT) {
					t.value = new SInteger(in.readInt());
				}
				if (i == OPCODE.PUSH_DOUBLE) {
					t.value = new SDouble(in.readDouble());
				}
				if (i == OPCODE.PUSH) {
					t.value = new SInteger(in.read());
				}
				code.add(t);
			}
		} catch (IOException e) {e.printStackTrace(); return;
		}
		
		in.close();
		
		SIP sip = new SIP();
		try {
			if (!decompile) sip.exec(code, debug);
			else {
				sip.decompile(code);
			}
		} catch (NumberFormatException e) {e.printStackTrace();
		} catch (IOException e) {e.printStackTrace();
		}
	}

	private void decompile(ArrayList<Token> code) {
		int PC = 0;
		for (Token t : code) {
			String instr = "" + t.op;
			if (t.op < OPCODE.op.length) instr = OPCODE.op[t.op];
			System.out.println(PC + ": " + instr + " " + (t.op == OPCODE.PUSH || t.op == OPCODE.PUSH_INT ? code.get(PC).value : ""));
			PC++;
		}
		
		
	}

	private void exec(ArrayList<Token> code, boolean debug) throws NumberFormatException, IOException {
		boolean debug_continue = false;
		boolean debug_show = false;
		ArrayList<Integer> debug_breakpoints = new ArrayList<Integer>();
		
		if (debug) System.out.println("'n' to continue, 'r' to run all code");
		
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		
		ArrayList<ArrayList<SValue>> stack = new ArrayList<ArrayList<SValue>>();
		
		ArrayList<Integer> pcStack = new ArrayList<Integer>();
		pcStack.add(0);
		
		stack.add(new ArrayList<SValue>());
		
		SValue[] memory = new SValue[10000];
		for (int i = 0; i < memory.length; i++) memory[i] = new SInteger(0);
		
		int PC = 0;
		int DC = 0;
		
		exloop: while (PC < code.size()) {
			
			//System.out.println("DEEP: " + (pcStack.size()-1) + ", " + DC);
			
			pcStack.set(DC, PC);
			
			int op = code.get(PC).op;
			
			if (op > OPCODE.op.length-1) {
				System.err.println("Unknown instruction " + op + "!");
				break exloop;
			}
			
			if (debug) {
				if (debug_breakpoints.contains(PC)) {
					debug_continue = false;
				}
				String s = stack.get(DC).toString();
				if (debug_show) System.out.println("		" + DC + ". Stack: " + s);
				
				String mem = Arrays.toString(Arrays.copyOfRange(memory, 0, memory[0].toInt()+1));
				if (debug_show) System.out.println("		Memory: " + mem);
				
				
				if (!debug_continue && debug) while (true) {
					System.out.print("Debug menu> ");
					int command = System.in.read();
					System.in.read();
					if (command == 'n') {
						break;
					} else if (command == 'r'){
						debug_continue = true;
						break;
					} else if (command == 's'){
						debug_show = !debug_show;
						System.out.println("Show debug info = " + debug_show);
					} else if (command == 'b') {
						System.out.print("Breakpoint> ");
						debug_breakpoints.add(Integer.parseInt(read.readLine()));
					}
				}
				
				String instr = "" + op;
				if (op < OPCODE.op.length) instr = OPCODE.op[op];
				if (debug_show) System.out.println(PC + ": " + instr + " " + (op == OPCODE.PUSH || op == OPCODE.PUSH_INT ? code.get(PC).value : ""));
			}
			
			try {
			
			switch (op) {
			case OPCODE.NOP:
				++PC;
				break;
			case OPCODE.PUSH:
				stack.get(DC).add(code.get(PC).value);
				++PC;
				break;
			case OPCODE.PUSH_INT:
				stack.get(DC).add(code.get(PC).value);
				++PC;
				break;
			case OPCODE.PUSH_DOUBLE:
				stack.get(DC).add(code.get(PC).value);
				++PC;
				break;
			case OPCODE.POP:
				stack.get(DC).remove(stack.get(DC).size()-1);
				++PC;
				break;
			case OPCODE.DUP:
				stack.get(DC).add(stack.get(DC).get(stack.get(DC).size()-1));
				++PC;
				break;
			/*case OPCODE.DROP:
				stack.get(DC).remove(stack.get(DC).size()-1);
				++PC;
				break;*/
			case OPCODE.REVERSE:
				Collections.reverse(stack.get(DC));
				++PC;
				break;
			case OPCODE.EXCH:
				Collections.swap(stack.get(DC), stack.get(DC).size()-1, stack.get(DC).size()-2);
				++PC;
				break;
			case OPCODE.OVER:
				stack.get(DC).add(stack.get(DC).get(0));
				++PC;
				break;
			case OPCODE.ROTL:
				Collections.rotate(stack.get(DC), -1);
				++PC;
				break;
			case OPCODE.ROTR:
				Collections.rotate(stack.get(DC), 1);
				++PC;
				break;
			case OPCODE.ADD:
				stack.get(DC).add(SDouble.toSValue(
						stack.get(DC).remove(stack.get(DC).size()-1).toDouble() + stack.get(DC).remove(stack.get(DC).size()-1).toDouble()));
				++PC;
				break;
			case OPCODE.SUB:
				Collections.swap(stack.get(DC), stack.get(DC).size()-1, stack.get(DC).size()-2);
				stack.get(DC).add(SDouble.toSValue(
						stack.get(DC).remove(stack.get(DC).size()-1).toDouble() - stack.get(DC).remove(stack.get(DC).size()-1).toDouble()));
				++PC;
				break;
			case OPCODE.MUL:
				stack.get(DC).add(SDouble.toSValue(
						stack.get(DC).remove(stack.get(DC).size()-1).toDouble() * stack.get(DC).remove(stack.get(DC).size()-1).toDouble()));
				++PC;
				break;
			case OPCODE.DIV:
				Collections.swap(stack.get(DC), stack.get(DC).size()-1, stack.get(DC).size()-2);
				stack.get(DC).add(
						SDouble.toSValue(stack.get(DC).remove(stack.get(DC).size()-1).toDouble() / stack.get(DC).remove(stack.get(DC).size()-1).toDouble()));
				++PC;
				break;
			case OPCODE.EXP:
				Collections.swap(stack.get(DC), stack.get(DC).size()-1, stack.get(DC).size()-2);
				stack.get(DC).add(
						SDouble.toSValue(Math.pow(stack.get(DC).remove(stack.get(DC).size()-1).toDouble(),stack.get(DC).remove(stack.get(DC).size()-1).toDouble())));
				++PC;
				break;
			case OPCODE.MOD:
				Collections.swap(stack.get(DC), stack.get(DC).size()-1, stack.get(DC).size()-2);
				stack.get(DC).add(
						SDouble.toSValue(stack.get(DC).remove(stack.get(DC).size()-1).toDouble() % stack.get(DC).remove(stack.get(DC).size()-1).toDouble()));
				++PC;
				break;
			case OPCODE.EQ:
				stack.get(DC).add(stack.get(DC).remove(stack.get(DC).size()-1).toDouble() == stack.get(DC).remove(stack.get(DC).size()-1).toDouble()
				? OPCODE.TRUE : OPCODE.FALSE);
				++PC;
				break;
			case OPCODE.GT:
				Collections.swap(stack.get(DC), stack.get(DC).size()-1, stack.get(DC).size()-2);
				stack.get(DC).add(stack.get(DC).remove(stack.get(DC).size()-1).toDouble() > stack.get(DC).remove(stack.get(DC).size()-1).toDouble()
						? OPCODE.TRUE : OPCODE.FALSE);
				++PC;
				break;
			case OPCODE.LT:
				Collections.swap(stack.get(DC), stack.get(DC).size()-1, stack.get(DC).size()-2);
				stack.get(DC).add(stack.get(DC).remove(stack.get(DC).size()-1).toDouble()
						< stack.get(DC).remove(stack.get(DC).size()-1).toDouble() ? OPCODE.TRUE : OPCODE.FALSE);
				++PC;
				break;
			case OPCODE.AND:
				stack.get(DC).add((stack.get(DC).remove(stack.get(DC).size()-1).toBoolean() && stack.get(DC).remove(stack.get(DC).size()-1).toBoolean()) ? OPCODE.TRUE : OPCODE.FALSE);
				++PC;
				break;
			case OPCODE.OR:
				stack.get(DC).add((stack.get(DC).remove(stack.get(DC).size()-1).toBoolean() || stack.get(DC).remove(stack.get(DC).size()-1).toBoolean()) ? OPCODE.TRUE : OPCODE.FALSE);
				++PC;
				break;
			case OPCODE.XOR:
				stack.get(DC).add(!(stack.get(DC).remove(stack.get(DC).size()-1).toBoolean() && stack.get(DC).remove(stack.get(DC).size()-1).toBoolean()) ? OPCODE.TRUE : OPCODE.FALSE);
				++PC;
				break;
			case OPCODE.NOT:
				stack.get(DC).add((stack.get(DC).remove(stack.get(DC).size()-1).toBoolean()) ? OPCODE.FALSE : OPCODE.TRUE);
				++PC;
				break;
			case OPCODE.PRINT:
				if (stack.get(DC).remove(stack.get(DC).size()-1).toInt() == 0) {
					System.out.print(stack.get(DC).remove(stack.get(DC).size()-1));
				} else {
					char[] c = Character.toChars(stack.get(DC).remove(stack.get(DC).size()-1).toInt());
					System.out.print(c);
				}
				++PC;
				break;
			case OPCODE.READ:
				if (stack.get(DC).remove(stack.get(DC).size()-1).toInt() == 0) {
					stack.get(DC).add(new SInteger(Integer.parseInt(read.readLine())));
				} else {
					stack.get(DC).add(new SInteger(read.read()));
				}
				++PC;
				break;
			case OPCODE.PRINTM:
				memory[stack.get(DC).remove(stack.get(DC).size()-1).toInt()] = (stack.get(DC).remove(stack.get(DC).size()-1));
				
				++PC;
				break;
			case OPCODE.READM:
				stack.get(DC).add(memory[stack.get(DC).remove(stack.get(DC).size()-1).toInt()]);
				++PC;
				break;
			case OPCODE.GOTO:
				if (stack.get(DC).remove(stack.get(DC).size()-1).toBoolean()) {
					PC = stack.get(DC).remove(stack.get(DC).size()-1).toInt();
				} else {
					stack.get(DC).remove(stack.get(DC).size()-1);
					++PC;
				}
				
				break;
			case OPCODE.PUSHR:
			case OPCODE.PUSHP:
				int goto1 = stack.get(DC).remove(stack.get(DC).size()-1).toInt();
				stack.add(new ArrayList<SValue>());
				
				if (op == OPCODE.PUSHP) {
				int x = stack.get(DC).remove(stack.get(DC).size()-1).toInt();
				
					for (int j = 0; j < x; j++) {
						SValue i = stack.get(DC).size() > 0 ? stack.get(DC).remove(stack.get(DC).size()-1) : new SInteger(0);
						stack.get(DC+1).add(i);
					}
				}
				
				DC++;
				
				pcStack.set(DC-1, PC);
				pcStack.add(goto1);
				PC = goto1;
				if (debug) if (debug_show) System.out.println("PUSH: " + (pcStack.size()-1) + ", " + DC + " Stack: " + pcStack);
				break;
			case OPCODE.POPR:
			case OPCODE.POPP:
				SValue i2 = stack.get(DC).size() > 0 ? stack.get(DC).remove(stack.get(DC).size()-1) : new SInteger(0);
				stack.remove(stack.size()-1);
				DC--;
				if (DC == -1) {
					break exloop;
				}
				PC = pcStack.remove(pcStack.size()-2);
				stack.get(DC).add(i2);
				++PC;
				if (debug) if (debug_show) System.out.println("POP: " + (pcStack.size()-1) + ", " + DC + ", " + PC + " Stack: " + pcStack);
				break;

			default:
				break;
			}
			} catch (ArrayIndexOutOfBoundsException ex) {
				System.err.println("Array index out of bounds exception! POPR forced!");
				SValue i2 = stack.get(DC).size() > 0 ? stack.get(DC).remove(stack.get(DC).size()-1) : new SInteger(0);
				stack.remove(stack.size()-1);
				DC--;
				if (DC == -1) {
					break exloop;
				}
				PC = pcStack.remove(pcStack.size()-2);
				stack.get(DC).add(i2);
				++PC;
				if (debug) if (debug_show) System.out.println("POP: " + (pcStack.size()-1) + ", " + DC + ", " + PC + " Stack: " + pcStack);
			}
		}
		read.close();
	}

}
