package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import gameEngine.Drawable;
import gameEngine.GameEngine;
import levelPieces.Archer;
import levelPieces.Guard;
import levelPieces.Window;

class TestMovingPieces {
	private Drawable[] gameBoard;

	@BeforeEach
	public void setupTestBoard() {
		gameBoard = new Drawable[GameEngine.BOARD_SIZE];
	}

	/*
	 * Tests Archer's specified sequential motion
	 * Strategy:
	 * - Place Archer in position 1
	 * - Place immovable pieces at positions 0 and 2
	 * - Set player location to last position on game board
	 * - Move the Archer 10 times and verify location after each move
	 * - Set player location to 0
	 * - Move the Archer 10 times and verify location after each move
	 */
	@Test
	public void testArcherMove() {
		gameBoard[0] = new Window();
		gameBoard[2] = new Window();
		Archer archer = new Archer(1);
		archer.move(gameBoard, GameEngine.BOARD_SIZE-1);
		// Archer should not move when attempting to move left and it is at the edge of the board or there is only pieces between it and the edge of the board
		assertEquals(1, archer.getLocation());
		archer.move(gameBoard, GameEngine.BOARD_SIZE-1);
		// Archer should jump pieces when moving to a space where there is a piece
		assertEquals(3, archer.getLocation());
		archer.move(gameBoard, GameEngine.BOARD_SIZE-1);
		assertEquals(1, archer.getLocation());
		archer.move(gameBoard, GameEngine.BOARD_SIZE-1);
		assertEquals(3, archer.getLocation());
		archer.move(gameBoard, GameEngine.BOARD_SIZE-1);
		// Archer should follow normal sequence when no pieces are in the way
		for (int i = 4; i < 14; i++) {
			assertEquals(i, archer.getLocation());
			archer.move(gameBoard, GameEngine.BOARD_SIZE-1); // Left
			assertEquals(i-1, archer.getLocation());
			archer.move(gameBoard, GameEngine.BOARD_SIZE-1); // Right
			assertEquals(i, archer.getLocation());
			archer.move(gameBoard, GameEngine.BOARD_SIZE-1); // Left
			assertEquals(i-1, archer.getLocation());
			archer.move(gameBoard, GameEngine.BOARD_SIZE-1); // Right
			assertEquals(i, archer.getLocation());
			archer.move(gameBoard, GameEngine.BOARD_SIZE-1); // Right
		}
		// Archer should follow opposite of normal sequence when player is to the left
		for (int i = 14; i > 4; i--) {
			assertEquals(i, archer.getLocation());
			archer.move(gameBoard, 0); // Right
			assertEquals(i+1, archer.getLocation());
			archer.move(gameBoard, 0); // Left
			assertEquals(i, archer.getLocation());
			archer.move(gameBoard, 0); // Right
			assertEquals(i+1, archer.getLocation());
			archer.move(gameBoard, 0); // Left
			assertEquals(i, archer.getLocation());
			archer.move(gameBoard, 0); // Left
		}
		// Archer should be at position 4
		assertEquals(4, archer.getLocation());
	}

	/*
	 * Tests Guard's random motion
	 * Strategy:
	 * - Place Guard in the middle of the game board
	 * - Put a immovable at the right-most limit of the Guard's movement ability
	 * - Move the Guard many times
	 * - The Guard should have not moved on some occasions
	 * - The Guard should not have moved more than its movement limit to the right + 1
	 * 		- It may have moved to its movement limit + 1 if it was to land on another piece otherwise
	 * - The Guard should not have moved more than its movement limit to the left
	 */
	@Test
	void testGuardMove() {
		// Find the middle of the board to track the guard's starting location
		int boardMiddle = (int) Math.round(GameEngine.BOARD_SIZE/2);
		// Find the total range of spaces that the guard could move to
		int possibleMoves = (Guard.MAX_MOVEMENT * 2) + 2;
		// Find where to put another piece so that the guard has to jump it based on board size and max movement distance
		int immovableLocation;
		if (boardMiddle + Guard.MAX_MOVEMENT < GameEngine.BOARD_SIZE ) {
			immovableLocation = boardMiddle + Guard.MAX_MOVEMENT;
		} else {
			immovableLocation = GameEngine.BOARD_SIZE - 1;
		}
		gameBoard[immovableLocation] = new Window(); // Set up the piece for the guard to jump
		int[] movementTracker = new int[possibleMoves]; // Set up an array to track where the guard lands 
		// In this array 0 is as far left as the guard can go and possibleMoves is as far right as the guard can go
		for (int i = 0; i < 20 * Guard.MAX_MOVEMENT; i++) {
			Guard guard = new Guard(boardMiddle); // Create a new guard at the middle of the board
			guard.move(gameBoard, 1);
			movementTracker[guard.getLocation()-boardMiddle+Guard.MAX_MOVEMENT]++; // Find how far the guard moved and record that it moved there
			if (guard.getLocation() < 0) { // If the guard is off the left of the board, fail the test
				fail("Guard moved beyond the left of the board");
			} else if (GameEngine.BOARD_SIZE <= guard.getLocation()) { // If the guard is off the right of the board, fail the test
				fail("Guard moved beyond the right of the board");
			} else if (guard.getLocation() == immovableLocation) { // If the guard landed on the other piece, fail the test
				fail("Guard landed on another piece");
			}
			gameBoard[guard.getLocation()] = null; // Reset the board for the next loop
		}
		for (int i = 0; i < possibleMoves; i++) { // Verify that the guard went to all of its possible options at least once
			int currentLocation = boardMiddle - Guard.MAX_MOVEMENT + i;
			if (currentLocation >= 0 && currentLocation < GameEngine.BOARD_SIZE && currentLocation != immovableLocation) { // Account for the guard's movement restrictions
				assertNotEquals(0, movementTracker[i]);
			} else {
				assertEquals(0, movementTracker[i]);
			}
		}
	}
}
