package levelPieces;

import gameEngine.Drawable;
import gameEngine.InteractionResult;
import gameEngine.Moveable;

public class Archer extends GamePiece implements Moveable {
	private int nextMove = 0; // track where the archer is in the sequence of moves
	private int[] movementSequence = {-1, 1, -1, 1, 1}; // describe sequence of moves for the archer to follow

	public Archer(int location) {
		super('A', "Archer", location);
	}

	@Override
	public InteractionResult interact(Drawable[] gameBoard, int playerLocation) {
		if (Math.abs(getLocation() - playerLocation) < 3) { // Hit the player if they are close to the archer
			return InteractionResult.HIT;
		}
		return InteractionResult.NONE; // If not close to the archer, nothing happens
	}

	@Override
	public void move(Drawable[] gameBoard, int playerLocation) {
		int directionModifier = 1; // Create a variable to track if the player is to the left or the right
		if (playerLocation - getLocation() < 0) { // If the player is to the right, set modifier to go opposite
			directionModifier = -1;
		}
		int newLocation = getLocation() + directionModifier * movementSequence[nextMove]; // Set to move by random amount		
		int occupiedSpaceAdjustDirection = 1;
		if (directionModifier * movementSequence[nextMove] < 0) {
			occupiedSpaceAdjustDirection = -1;
		}
		while (0 > newLocation || newLocation > gameBoard.length || (gameBoard[newLocation] != null && newLocation != getLocation())) {
			if (0 > newLocation) {
				occupiedSpaceAdjustDirection = 1;
				newLocation += occupiedSpaceAdjustDirection;
			} else if (newLocation > gameBoard.length) {
				occupiedSpaceAdjustDirection = -1;
			}
			newLocation += occupiedSpaceAdjustDirection;
		}
		gameBoard[getLocation()] = null;
		gameBoard[newLocation] = this;
		setLocation(newLocation);
		nextMove++;
		if (nextMove >= movementSequence.length) {
			nextMove = 0;
		}
	}

}
