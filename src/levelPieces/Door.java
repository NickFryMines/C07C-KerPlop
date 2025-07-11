package levelPieces;

/**
 * 
 * @author Nick Fry
 * @author Wesley Montgomery
 * 
 */

import gameEngine.Drawable;
import gameEngine.InteractionResult;
import java.util.Map;
import java.util.Random;

public class Door extends GamePiece {
	private static final Map<InteractionResult, Integer> WEIGHTED_INTERACTION_PROBABILITY = Map.of(InteractionResult.ADVANCE, 5, InteractionResult.GET_POINT, 45, InteractionResult.HIT, 55);
	private static final int TOTAL_INTERACTION_PROBABILITY = WEIGHTED_INTERACTION_PROBABILITY.values().stream().mapToInt(Integer::intValue).sum();
	private Random randomGenerator = new Random();

	public Door(int location) {
		super('D', "Door", location);
	}

	@Override
	public InteractionResult interact(Drawable[] gameBoard, int playerLocation) {
		if (getLocation() != playerLocation) {
			return InteractionResult.NONE;
		}
		int randomInteraction = randomGenerator.nextInt(TOTAL_INTERACTION_PROBABILITY) + 1; // randomly get an integer between 0 and TOTAL_INTERACTION_PROBABILITY (inclusive)
		int initialProbability = 0;
        for (Map.Entry<InteractionResult, Integer> entry : WEIGHTED_INTERACTION_PROBABILITY.entrySet()) {
            int interactionProbability = entry.getValue();
        	if (initialProbability < randomInteraction && randomInteraction <= interactionProbability) {
        		return entry.getKey();
        	}
        	initialProbability += interactionProbability;
        }
		return InteractionResult.HIT;	
	}

}
