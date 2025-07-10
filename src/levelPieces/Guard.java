package levelPieces;

import java.util.Random;

import gameEngine.Drawable;
import gameEngine.InteractionResult;
import gameEngine.Moveable;

public class Guard extends GamePiece implements Moveable {
	private static final int MAX_RIGHT_MOVEMENT = 1;
	private static final int MAX_LEFT_MOVEMENT = -1;
	private Random randomMovementGenerator = new Random();

	public Guard(int location) {
		super('G', "Guard", location);
	}

	@Override
	public InteractionResult interact(Drawable[] gameBoard, int playerLocation) {
		if (getLocation() - playerLocation == 0) { // If on the same tile as the guard, kill the player
			return InteractionResult.KILL;
		}
		if (Math.abs(getLocation() - playerLocation) == 1) { // Hit the player if they are close to the guard
			return InteractionResult.HIT;
		}
		return InteractionResult.NONE; // If not close to the guard, nothing happens
	}

	@Override
	public void move(Drawable[] gameBoard, int playerLocation) {
		int randomMovement = randomMovementGenerator.nextInt(MAX_RIGHT_MOVEMENT - MAX_LEFT_MOVEMENT + 1) + MAX_LEFT_MOVEMENT; // randomly get an integer between MAX right and left movements (inclusive)
		int newLocation = getLocation()+randomMovement; // Set to move by random amount		
		int occupiedSpaceAdjustDirection = 1;
		if (randomMovement < 0) {
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
	}

}
