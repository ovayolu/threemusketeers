package threemusketeers;

import java.util.Collections;

class Position implements Cloneable {
	public static final int HUMAN    = 2
	public static final int COMPUTER = 1
	public static final int DRAW     = 3
	
	int[] pieces = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]// a tri state array, 0,1 or 2, for empty, noughts or crosses
	
	public static void main(String[] args) {
		Position p = new Position()
		p.setPiece(4, COMPUTER)
		p.setPiece(2, HUMAN)
		p.display()
		println "p's code is " + p.getCode()
	}
	public Position() {
	}
	// make one position from another.
	public Position(Position p) {
		pieces = p.pieces.clone()
	}
	public Position(def newPieces) {
		pieces = newPieces
	}
	
	public int getPiece(int x) {
		return pieces[x]
	}
	private void setPiece(def idx, def val) {
		pieces[idx] = val
	}
	
	public def possibleMoves() {
		Position poss
		def empties = []
		(0..24).each { x-> 
			if (pieces[x] < 1) {
				poss = this.clone()
				poss.setPiece(x,COMPUTER)
				empties.add(poss) 
			}
		}
		Collections.shuffle(empties) // randomize to make more interesting
		return empties
	}
	public boolean isEmpty(def x) {
		return pieces[x] < 1
	}
	private int getXY(int x, int y) {
		return pieces [x + y * 5]
	}
	private void display() {
		println "---------"
		5.times {y->
			5.times {x->
				print getXY(x,y)==0? "[ ]" :(getXY(x,y)==COMPUTER? "[X]":"[O]") 
			}
			println ""
		}
	}
	// make matrix into an int value.
	private int getIntValue(def pos) {
		def total = 0
		25.times {
			total += pos[it]*5**it
		}
		total
	}
	public boolean equals(def o) {
		return code==o.code
	}
	// encodes a value for all equivalent positions and takes the smallest.
	// this is the code that uniquely identifies this position (and all equivalents)
	public def getCode() {
		def currentBoard = pieces
		def smallestCode = getIntValue(currentBoard)
		
		if (getIntValue(currentBoard) < smallestCode)
			smallestCode = getIntValue(currentBoard)
		
		return smallestCode
	}
	// translates the board and returns a new array.
	private int[] translate(def pieces, def translation) {
		def newArray = []
		25.times {
			newArray[translation[it]] = pieces[it]	
		}
		return newArray
	}
	// returns the winning player (1 or 2) if it is a win situation, 0 if game still in progress, or 3 if a draw
	public int winner() {
		// check rows and columns
		for (i in 0..4) {
			if ((pieces[i*5] == pieces[i*5+1]) && (pieces[i*5] == pieces[i*5+2]) && (pieces[i*5]  == pieces[i*5+3]) && (pieces[i*5]  == pieces[i*5+4]) && pieces[i*5] > 0)
				return pieces[i*5]
			if ((pieces[i] == pieces[i+5]) && (pieces[i] == pieces[i+10]) && (pieces[i] == pieces[i+15]) && (pieces[i] == pieces[i+20]) && pieces[i] > 0)
				return pieces[i]
		}
		// check '\' diagonal
		if ((pieces[0] == pieces[6]) && (pieces[0] == pieces[12]) && (pieces[0] == pieces[18]) && (pieces[0] == pieces[24]) && pieces[0] > 0)
			return pieces[0]
		// check '/' diagonal
		if ((pieces[4] == pieces[8]) && (pieces[4] == pieces[12]) && (pieces[4] == pieces[16]) && (pieces[2] == pieces[4]) && pieces[20] > 0)
			return pieces[2]
		// no one has won, so if the board is full, it is a draw
		for (p in pieces) {
			if (!p) 
				return 0
		}
		3
	}
	public Object clone() {
		Position p = super.clone()
		p.pieces = pieces.clone()
		return p
	}
}



