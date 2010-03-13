package threemusketeers;

import java.util.Collections;

class Position implements Cloneable {
	public static final int HUMAN    = 2
	public static final int COMPUTER = 1
	public static final int DRAW     = 3
	//
	public static final int RICHELIEU = 4
	
	// a 5 state array:
	// 0 = empty
	// 1,2,3 = musketeers
	// 4 = cardinal richelieu's man
	int[] pieces = [4,4,4,4,3
	               ,4,4,4,4,4
	               ,4,4,2,4,4
	               ,4,4,4,4,4
	               ,1,4,4,4,4]
	
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
	
	// ML - let's use moveMusketeer instead of setPiece ...
	public boolean moveMusketeer(def musketeer, def direction) {
	  // TODO: check that the direction is valid
	  //       for the musketeer in question
	  //       e.g. at start, 1 can not move South
	  //       for now, assume this check is done ...
	  def oldPos = getMusketeerLocation(musketeer)
	  //println "DEBUG: old position = " + oldPos
	  //println "DEBUG: currently = " + pieces[oldPos]
	  def newPos
	  newPos = getNewPosition(oldPos, direction)
	  if (newPos < 0) {
	    return false
	  }

    //println "DEBUG: new position = " + newPos
	  //println "DEBUG: currently = " + pieces[newPos]
	  // assuming all checks are passed ...
	  pieces[oldPos] = 0 // make the old position empty
	  
	  if (pieces[newPos] > 0) {
  	  pieces[newPos] = musketeer // move musketeer to new position
  	} else {
  	  println "ERROR: new position must not be empty."
  	  return false
  	}
	  //println "DEBUG: old position = " + oldPos
	  //println "DEBUG: currently = " + pieces[oldPos]
    //println "DEBUG: new position = " + newPos
	  //println "DEBUG: currently = " + pieces[newPos]	  
	} 
	
	// ML
	private def getMusketeerLocation(def m) {
	  def retVal
	  (0..24).each { x ->
	    if (pieces[x] == m)
	      retVal = x
	  }
	  retVal
	}
	
	public boolean moveRichelieu(def position, def direction) {
	  println "NOT YET IMPLEMENTED: moveRichelieu"
	  return true
	}
	
	// ML
	private def getNewPosition(def oldPos, def direction) {
	  // returns negative value when newPos is negative, or > 24
    def newPos
	  switch (direction) {
	    case "N":
	      newPos = oldPos - 5
	      if (newPos < 0) {
	        println "ERROR: can't move North from here."
	      }
	      break
	    case "S":
	      newPos = oldPos + 5
	      if (newPos > 24) {
	        println "ERROR: can't move South from here."
	      }
	      break
	    case "E":
	      // check for 4, 9, 14, 19, 24
	      def addOne = oldPos + 1
	      if (addOne % 5 == 0) {
	        println "ERROR: can't move East from here."
	        newPos = -1
	      } else {
  	      newPos = oldPos + 1
  	    }
	      break
	    case "W":
	      // check for when oldPos mod 5 = 0
	      if (oldPos % 5 == 0) {
	        println "ERROR: can't move West from here."
	        newPos = -1
	      } else {
	        newPos = oldPos - 1
	      }
	      break
	    default:
	      println "ERROR: direction must be N, S, E, W."
	      newPos = -1
	  }
	  if (newPos > 24) {
	    newPos = -1
	  }
	  newPos
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
				// ML
				// commented out:
				//print getXY(x,y)==0? "[ ]" :(getXY(x,y)==COMPUTER? "[X]":"[O]")
				//  new code:
				// Display [ ] if empty (0)
				// Display the number for a musketeer (1,2,3)
				// Display X for RICHELIEU
				print getXY(x,y)==0? "[ ]" :(getXY(x,y)==RICHELIEU? "[X]":"["+getXY(x,y)+"]")  
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
		// ML for now just return 0
		return 0
		
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



