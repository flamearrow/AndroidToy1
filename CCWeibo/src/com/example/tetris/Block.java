package com.example.tetris;


/**
 * denotes different block shapes on Tetris game
 * 
 * @author flamearrow
 * 
 */
public class Block {
	// hex value for each letter:
	/*
	 * L: 0x4460
	 * 0100
	 * 0100
	 * 0110
	 * 0000 
	 *
	 * rL: 0x2260
	 * 0010
	 * 0010
	 * 0110
	 * 0000
	 * 
	 * S: 0x0360
	 * 0000
	 * 0011
	 * 0110
	 * 0000
	 * 
	 * rS: 0x0C60
	 * 0000
	 * 1100
	 * 0110
	 * 0000
	 * 
	 * T: 0x0E40
	 * 0000
	 * 1110
	 * 0100
	 * 0000
	 * 
	 * O: 0x0660
	 * 0000
	 * 0110
	 * 0110
	 * 0000
	 * 
	 * I: 0x4444
	 * 0100
	 * 0100
	 * 0100
	 * 0100
	 */
	public enum Value {
		L(0x4460), rL(0x2260), S(0x0360), rS(0x0C60), T(0x0E40), O(0x0660), I(0x4444);
		private final int hexV;
		Value(int hexV) {
			this.hexV = hexV;
		}
		public int gethexV() {
			return hexV;
		}
	}
	
	public enum Direction {
		Up, Down, Left, Right
	}
	
	Value value;
	Direction direction;
	int color;

	public Block(Value argV, int argC, Direction argD) {
		this.value = argV;
		this.color = argC;
		this.direction = argD;
	}
}
