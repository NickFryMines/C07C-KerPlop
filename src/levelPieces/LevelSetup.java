package levelPieces;

import java.util.ArrayList;
import gameEngine.Drawable;
import gameEngine.Moveable;

public class LevelSetup {
	private Drawable[] board;
	private int currentLevel;
	private int[] levelStarting = {4, 19};
	private String[] levelLayouts = {
			"_A_____DRWD__G___K___",
			"KG_RD__D__R__G_______"
	};

	public void createLevel(int levelNum) {
		if (levelNum <= 0 || levelNum > levelLayouts.length) {
			currentLevel = -1;
			board = null;
			return;
		}

		currentLevel = levelNum - 1;
		ArrayList<Drawable> boardBuilder = new ArrayList<Drawable>();
		for (int i = 0; i < levelLayouts[currentLevel].length(); i++) {
			boardBuilder.add(createPiece(levelLayouts[currentLevel].charAt(i), i));
		}
		board = boardBuilder.toArray(new Drawable[boardBuilder.size()]);
	}

	private Drawable createPiece(char code, int location) {
		switch (code) {
		case 'A': return new Archer(location);
		case 'K': return new King(location);
		case 'D': return new Door(location);
		case 'R': return new Room(location);
		case 'G': return new Guard(location);
		case 'W': return new Window();
		case '_': return null;
		default: return null;
		}
	}

	public Drawable[] getBoard() {
		return board;
	}

	public ArrayList<Moveable> getMovingPieces() {
		ArrayList<Moveable> moving = new ArrayList<>();
		if (board == null) {
			return moving;
		}
		for (Drawable levelPiece : board) {
			if (levelPiece instanceof Moveable) {
				moving.add((Moveable) levelPiece);
			}
		}
		return moving;
	}

	public ArrayList<GamePiece> getInteractingPieces() {
		ArrayList<GamePiece> interactable = new ArrayList<>();
		if (board == null) {
			return interactable;
		}
		for (Drawable levelPiece : board) {
			if (levelPiece instanceof GamePiece) {
				interactable.add((GamePiece) levelPiece);
			}
		}
		return interactable;
	}

	public int getPlayerStartLoc() {
		if (currentLevel == -1) {
			return -1;
		}
		return levelStarting[currentLevel];
	}

}
