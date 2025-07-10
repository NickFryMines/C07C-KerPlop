package levelPieces;

import gameEngine.Drawable;
import gameEngine.InteractionResult;

public class King extends GamePiece {

	public King(int location) {
		super('K', "King", location);
	}

	@Override
	public InteractionResult interact(Drawable[] gameBoard, int playerLocation) {
		if (getLocation() == playerLocation) {
			return InteractionResult.ADVANCE;
		}
		return InteractionResult.NONE;
	}

}
