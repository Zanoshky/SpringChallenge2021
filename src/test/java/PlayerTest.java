import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class PlayerTest {

    public Game createBoardWithNoObstacles() {
        Game.BOARD.put(0, new Cell(0, 3, new int[] { 1, 2, 3, 4, 5, 6 }));
        Game.BOARD.put(1, new Cell(1, 3, new int[] { 7, 8, 2, 0, 6, 18 }));
        Game.BOARD.put(2, new Cell(2, 3, new int[] { 8, 9, 10, 3, 0, 1 }));
        Game.BOARD.put(3, new Cell(3, 3, new int[] { 2, 10, 11, 12, 4, 0 }));
        Game.BOARD.put(4, new Cell(4, 3, new int[] { 0, 3, 12, 13, 14, 5 }));
        Game.BOARD.put(5, new Cell(5, 3, new int[] { 6, 0, 4, 14, 15, 16 }));
        Game.BOARD.put(6, new Cell(6, 3, new int[] { 18, 1, 0, 5, 16, 17 }));
        Game.BOARD.put(7, new Cell(7, 2, new int[] { 19, 20, 8, 1, 18, 36 }));
        Game.BOARD.put(8, new Cell(8, 2, new int[] { 20, 21, 9, 2, 1, 7 }));
        Game.BOARD.put(9, new Cell(9, 2, new int[] { 21, 22, 23, 10, 2, 8 }));
        Game.BOARD.put(10, new Cell(10, 2, new int[] { 9, 23, 24, 11, 3, 2 }));
        Game.BOARD.put(11, new Cell(11, 2, new int[] { 10, 24, 25, 26, 12, 3 }));
        Game.BOARD.put(12, new Cell(12, 2, new int[] { 3, 11, 26, 27, 13, 4 }));
        Game.BOARD.put(13, new Cell(13, 2, new int[] { 4, 12, 27, 28, 29, 14 }));
        Game.BOARD.put(14, new Cell(14, 2, new int[] { 5, 4, 13, 29, 30, 15 }));
        Game.BOARD.put(15, new Cell(15, 2, new int[] { 16, 5, 14, 30, 31, 32 }));
        Game.BOARD.put(16, new Cell(16, 2, new int[] { 17, 6, 5, 15, 32, 33 }));
        Game.BOARD.put(17, new Cell(17, 2, new int[] { 35, 18, 6, 16, 33, 34 }));
        Game.BOARD.put(18, new Cell(18, 2, new int[] { 36, 7, 1, 6, 17, 35 }));
        Game.BOARD.put(19, new Cell(19, 1, new int[] { -1, -1, 20, 7, 36, -1 }));
        Game.BOARD.put(20, new Cell(20, 1, new int[] { -1, -1, 21, 8, 7, 19 }));
        Game.BOARD.put(21, new Cell(21, 1, new int[] { -1, -1, 22, 9, 8, 20 }));
        Game.BOARD.put(22, new Cell(22, 1, new int[] { -1, -1, -1, 23, 9, 21 }));
        Game.BOARD.put(23, new Cell(23, 1, new int[] { 22, -1, -1, 24, 10, 9 }));
        Game.BOARD.put(24, new Cell(24, 1, new int[] { 23, -1, -1, 25, 11, 10 }));
        Game.BOARD.put(25, new Cell(25, 1, new int[] { 24, -1, -1, -1, 26, 11 }));
        Game.BOARD.put(26, new Cell(26, 1, new int[] { 11, 25, -1, -1, 27, 12 }));
        Game.BOARD.put(27, new Cell(27, 1, new int[] { 12, 26, -1, -1, 28, 13 }));
        Game.BOARD.put(28, new Cell(28, 1, new int[] { 13, 27, -1, -1, -1, 29 }));
        Game.BOARD.put(29, new Cell(29, 1, new int[] { 14, 13, 28, -1, -1, 30 }));
        Game.BOARD.put(30, new Cell(30, 1, new int[] { 15, 14, 29, -1, -1, 31 }));
        Game.BOARD.put(31, new Cell(31, 1, new int[] { 32, 15, 30, -1, -1, -1 }));
        Game.BOARD.put(32, new Cell(32, 1, new int[] { 33, 16, 15, 31, -1, -1 }));
        Game.BOARD.put(33, new Cell(33, 1, new int[] { 34, 17, 16, 32, -1, -1 }));
        Game.BOARD.put(34, new Cell(34, 1, new int[] { -1, 35, 17, 33, -1, -1 }));
        Game.BOARD.put(35, new Cell(35, 1, new int[] { -1, 36, 18, 17, 34, -1 }));
        Game.BOARD.put(36, new Cell(36, 1, new int[] { -1, 19, 7, 18, 35, -1 }));

        Game game = new Game();
        game.seedActions.add(new ActionSeed(30, 29));
        game.seedActions.add(new ActionSeed(30, 15));
        game.seedActions.add(new ActionSeed(30, 14));
        game.seedActions.add(new ActionSeed(30, 31));
        game.seedActions.add(new ActionSeed(27, 13));
        game.seedActions.add(new ActionSeed(27, 28));
        game.seedActions.add(new ActionSeed(27, 12));
        game.seedActions.add(new ActionSeed(27, 26));

        game.seedActions = Arrays.asList(new ActionSeed(1, 20), new ActionSeed(2, 20), new ActionSeed(2, 17),
                new ActionSeed(1, 7), new ActionSeed(1, 9), new ActionSeed(2, 27), new ActionSeed(22, 9),
                new ActionSeed(1, 6), new ActionSeed(1, 21), new ActionSeed(2, 18), new ActionSeed(2, 35),
                new ActionSeed(1, 36), new ActionSeed(2, 11), new ActionSeed(2, 19), new ActionSeed(1, 16),
                new ActionSeed(2, 12), new ActionSeed(2, 7), new ActionSeed(2, 10), new ActionSeed(1, 18),
                new ActionSeed(22, 20), new ActionSeed(2, 6), new ActionSeed(2, 36), new ActionSeed(22, 23),
                new ActionSeed(2, 26), new ActionSeed(2, 21), new ActionSeed(1, 17), new ActionSeed(1, 10),
                new ActionSeed(2, 13), new ActionSeed(2, 16), new ActionSeed(22, 21), new ActionSeed(22, 10),
                new ActionSeed(2, 9), new ActionSeed(2, 23), new ActionSeed(1, 35), new ActionSeed(1, 19),
                new ActionSeed(2, 15));

        game.completeActions = Arrays.asList(new ActionComplete(2));
        game.growActions = Arrays.asList(new ActionGrow(22), new ActionGrow(1));

        game.mySun = 2;
        game.opponentSun = 7;
        game.myScore = 20;
        game.opponentScore = 0;
        game.opponentIsWaiting = false;
        game.day = 9;
        game.nutrients = 19;
        game.myTreesSizes = new int[] { 0, 0, 2, 1 };

        game.trees.put(1, new Tree(1, 3, true, false));
        game.trees.put(2, new Tree(2, 2, true, false));
        game.trees.put(22, new Tree(22, 1, true, false));

        game.trees.put(25, new Tree(25, 2, false, false));
        game.trees.put(3, new Tree(3, 3, false, false));
        game.trees.put(4, new Tree(4, 2, false, false));
        game.trees.put(5, new Tree(5, 2, false, false));
        game.trees.put(31, new Tree(31, 2, false, false));

        return game;
    }

    @Test
    public void test() {
        Game simple = createBoardWithNoObstacles();
        Action rootAction = simple.decideNextAction();
        assertEquals("SEED 2 11", rootAction.getCommand());
    }
}