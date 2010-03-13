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
		while (curPos.winner() < 1) { 
			boolean moveLegal = false
			while (!moveLegal) {
				moveLegal = getHumanMove()
				if (!moveLegal)
					message ( "Illegal Move. Try again...")
			}
			updateBoard()
			if (curPos.winner() < 1) {
				makeComputerMove()
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
	public void message(String s) {
		println s
	}
	public void updateBoard() {
		curPos.display()
	}
}
