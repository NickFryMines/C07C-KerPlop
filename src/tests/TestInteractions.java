package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameEngine.Drawable;
import gameEngine.GameEngine;
import gameEngine.InteractionResult;
import levelPieces.Archer;
import levelPieces.Door;
import levelPieces.Guard;
import levelPieces.King;
import levelPieces.Room;

class TestInteractions {
	private Drawable[] gameBoard;

	@BeforeEach
	public void setupTestBoard() {
		gameBoard = new Drawable[GameEngine.BOARD_SIZE];
	}

	@Test
	void testArcherInteraction() {
		Archer archer = new Archer(0); // make an archer at the left end of the board
		for (int i = 0; i < 3; i++) { // Ensure that the archer does hits when the player is three or closer
			assertEquals(InteractionResult.HIT, archer.interact(gameBoard, i));
		}
		for (int i = 4; i < GameEngine.BOARD_SIZE; i++) { // Ensure that the archer does not interact when the player is further than three away
			assertEquals(InteractionResult.NONE, archer.interact(gameBoard, i));
		}
		archer = new Archer(GameEngine.BOARD_SIZE - 1); // make an archer at the right end of the board
		for (int i = GameEngine.BOARD_SIZE - 1; i > GameEngine.BOARD_SIZE - 4; i--) { // Ensure that the archer does hits when the player is three or closer
			assertEquals(InteractionResult.HIT, archer.interact(gameBoard, i));
		}
		for (int i = GameEngine.BOARD_SIZE - 5; i >= 0; i--) { // Ensure that the archer does not interact when the player is further than three away
			assertEquals(InteractionResult.NONE, archer.interact(gameBoard, i));
		}
	}

	@Test
	void testGuardInteraction() {
		Guard guard = new Guard(2);
		assertEquals(InteractionResult.NONE, guard.interact(gameBoard, 0));
		assertEquals(InteractionResult.HIT, guard.interact(gameBoard, 1));
		assertEquals(InteractionResult.KILL, guard.interact(gameBoard, 2));
		assertEquals(InteractionResult.HIT, guard.interact(gameBoard, 3));
		for (int i = 4; i < GameEngine.BOARD_SIZE; i++) { // Ensure that the guard does not interact when the player is further than one away
			assertEquals(InteractionResult.NONE, guard.interact(gameBoard, i));
		}
	}

	@Test
	void testKingInteraction() {
		int boardMiddle = (int) Math.round(GameEngine.BOARD_SIZE/2);
		King king = new King(boardMiddle);
		for (int i = 0; i < GameEngine.BOARD_SIZE; i++) {
			if (i == boardMiddle) {
				assertEquals(InteractionResult.ADVANCE, king.interact(gameBoard, i));
			} else {
				assertEquals(InteractionResult.NONE, king.interact(gameBoard, i));
			}
		}
	}

	@Test
	void testDoorInteraction() {
		int boardMiddle = (int) Math.round(GameEngine.BOARD_SIZE/2);
		Door door = new Door(boardMiddle);
		for (int i = 0; i < GameEngine.BOARD_SIZE; i++) {
			if (i != boardMiddle) {
				assertEquals(InteractionResult.NONE, door.interact(gameBoard, i));
			}
		}
		Map<InteractionResult, Integer> interactionCount = new HashMap<>();
		for (Map.Entry<InteractionResult, Integer> entry : Door.WEIGHTED_INTERACTION_PROBABILITY.entrySet()) {
			interactionCount.put(entry.getKey(), 0);
		}
		for (int i = 0; i < Door.TOTAL_INTERACTION_PROBABILITY; i++) {
			InteractionResult interaction = door.interact(gameBoard, boardMiddle);
			interactionCount.put(interaction, interactionCount.get(interaction));
		}
		for (Map.Entry<InteractionResult, Integer> entry : Door.WEIGHTED_INTERACTION_PROBABILITY.entrySet()) {
			assertNotEquals(0, entry.getValue());
		}
	}

	@Test
	void testRoomInteraction() {
		int boardMiddle = (int) Math.round(GameEngine.BOARD_SIZE/2);
		Room room = new Room(boardMiddle);
		for (int i = 0; i < GameEngine.BOARD_SIZE; i++) {
			if (i != boardMiddle) {
				assertEquals(InteractionResult.NONE, room.interact(gameBoard, i));
			}
		}
		Map<InteractionResult, Integer> interactionCount = new HashMap<>();
		for (Map.Entry<InteractionResult, Integer> entry : Room.WEIGHTED_INTERACTION_PROBABILITY.entrySet()) {
			interactionCount.put(entry.getKey(), 0);
		}
		for (int i = 0; i < Room.TOTAL_INTERACTION_PROBABILITY; i++) {
			InteractionResult interaction = room.interact(gameBoard, boardMiddle);
			interactionCount.put(interaction, interactionCount.get(interaction));
		}
		for (Map.Entry<InteractionResult, Integer> entry : Room.WEIGHTED_INTERACTION_PROBABILITY.entrySet()) {
			assertNotEquals(0, entry.getValue());
		}
	}
}
