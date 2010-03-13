package threemusketeers;

import java.util.HashSet;

class Game {	
	Position lastMove       = new Position()
	Position curPos         = new Position()
	GamePanel gamePanel
	public static final FILE_NAME = "badmoves.bin"
	HashSet badMoves
	
	public Game() {
	}
	public static void main(String[] args) {
		def g = new Game()
		g.play()		
	}
	private void persistBadMoves() {
		new ObjectOutputStream (new FileOutputStream(FILE_NAME)).withStream { 
			it.writeObject(badMoves)
		}
	}
	private void dePersistBadMoves() {
		try {
			new ObjectInputStream (new FileInputStream(FILE_NAME)).withStream { 
				badMoves = it.readObject()
			}
		}
		catch (Exception e) {
			badMoves = new HashSet()
		}
	}
	
	// ML
	private boolean getMusketeerMove() {
		// based on code from tictactoe getHumanMove()
    
		message ("Choose a musketeer to move: [1, 2, 3]")
		def m = getMusketeer()
		if ((m < 1) || (m > 3))
		  return false
		message ("Choose a direction: [N, S, E, W]")
		def d = getDirection()
		
		// not sure if this will work ...
		// cloning the current board position is something that works
		// in tictactoe, not sure if it applies to our game?
		Position newPos = curPos.clone()
		// moveMusketeer instead of setPiece ...
		def success
		success = newPos.moveMusketeer(m, d)
		// TODO: save bad moves to the bad moves file?
    if (success) {
  		curPos = newPos
  	}
		return success
    //	
	}
	
	// ML - based on getHumanMove and getMusketeerMove
	private boolean getRichelieuMove() {

	  message ("Choose one of Richelieu's men, by x,y position (e.g. 0,1)")
	  def x = getTextConsoleMove() 
		message ("Choose a direction: [N, S, E, W]")
		def d = getDirection()
	  Position newPos = curPos.clone()
	  def success
	  success = newPos.moveRichelieu(x, d)
	  if (success) {
	    curPos = newPos
	  }
	  return success
	}
	
	private boolean getHumanMove() {		
		def x = getTextConsoleMove()
		if (!curPos.isEmpty(x)) 
			return false
		Position humanMove = curPos.clone()
		humanMove.setPiece(x, Position.HUMAN)
		if (humanMove.winner()==Position.HUMAN) {
			badMoves.add(curPos.getCode())
		}
		curPos = humanMove
		return true
	}
	private void makeComputerMove() {
		for (p in curPos.possibleMoves()) {
			if (!badMoves.contains(p.getCode())) {
				curPos = p
				lastMove = curPos
				return   // move is made
			}
		}
		//only bad moves available from here. Store last move as bad move, make random move
		lastMove.display()
		badMoves.add(lastMove.getCode())
		curPos = curPos.possibleMoves().getAt(0);
	}
	
	public play () {
		dePersistBadMoves()
		// ML
		updateBoard()
		//
		while (curPos.winner() < 1) { 
			boolean moveLegal = false
			while (!moveLegal) {
				// ML
				//moveLegal = getHumanMove()
				moveLegal = getMusketeerMove()
				if (!moveLegal) {
					message ( "Illegal Move. Try again...")
					updateBoard()
			  }
			}
			updateBoard()
			
			if (curPos.winner() < 1) {
				//makeComputerMove()
				getRichelieuMove()
				updateBoard()
			}
		}
		message( "And the winner is - " + (curPos.winner()==1? "Computer" : (curPos.winner()== 2 ?"Person" : "No one. It's a draw")))
		println "Bad move list currently: ${badMoves}"
		persistBadMoves()
	}
	// reads move from user in form  "x,y"  e.g.  0,2
	private int getTextConsoleMove() {
		def move
		BufferedReader br = new BufferedReader( new InputStreamReader (System.in))  
		move = br.readLine()
		def fields = move.split(",")
		return Integer.parseInt(fields[0])+ Integer.parseInt(fields[1])*5
	}
	// ML
	private int getMusketeer() {
	  def musk
		BufferedReader br = new BufferedReader( new InputStreamReader (System.in))
		musk = br.readLine()
		return Integer.parseInt(musk)	  
	}
	// ML
	private char getDirection() {
	  def d
		BufferedReader br = new BufferedReader( new InputStreamReader (System.in))
		d = br.readLine()
		return d	  	  
	} 
	public void message(String s) {
		println s
	}
	public void updateBoard() {
		curPos.display()
	}
}
