import java.util.*;

enum Zone {
    GREEN(7), YELLOW(19), RED(37);

    private final int maxIndex;

    Zone(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    static Zone getZoneFromCellId(int index) {
        for (Zone zone : values()) {
            if (index < zone.maxIndex) {
                return zone;
            }
        }

        throw new InputMismatchException("Invalid cell id " + index);
    }
}

interface AiLogic {

    int VALUE_IF_ENEMY_NEAR = 1;
    int VALUE_IF_ALLY_NEAR = -1;
    int TARGET_LVL_3_TREES = 4;
    int SELL_AFTER_DAY = 20;
    int CONSERVE_AFTER_DAY = 22;
    int MAX_BABYS = 2;
    int MAX_TREES_IN_GREEN = 5;
    Zone TARGET_ZONE = Zone.GREEN;

}

/**
 * Interface which contains constants for possible commands, and method to
 * implement for getting the command to execute
 */
interface Action {

    String COMPLETE = "COMPLETE";
    String SEED = "SEED";
    String GROW = "GROW";
    String WAIT = "WAIT";

    /**
     * This method will be executed in System.out.println();
     *
     * @return Formatted string which is specifc for each type for command
     */
    String getCommand();

}

/**
 * A Cell represents a playable field on the map
 */
class Cell {

    private final int index;
    private final int richness;
    private final int[] neighbours;
    private final Zone zone;

    public Cell(int index, int richness, int[] neighbours) {
        this.index = index;
        this.richness = richness;
        this.neighbours = neighbours;
        this.zone = Zone.getZoneFromCellId(index);
    }

    public int getId() {
        return index;
    }

    public int getRichness() {
        return richness;
    }

    public int[] getNeighbours() {
        return neighbours;
    }

    public Zone getZone() {
        return zone;
    }

    @Override
    public String toString() {
        return "Cell{" + "index=" + index + ", richness=" + richness + ", neighbours=" + Arrays.toString(neighbours)
                + '}';
    }
}

class Tree {

    private final int cellIndex;
    private final int size;
    private final boolean isMine;
    private final boolean isDormant;

    public Tree(int cellIndex, int size, boolean isMine, boolean isDormant) {
        this.cellIndex = cellIndex;
        this.size = size;
        this.isMine = isMine;
        this.isDormant = isDormant;
    }

    public int getId() {
        return cellIndex;
    }

    public int getSize() {
        return size;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isDormant() {
        return isDormant;
    }

    @Override
    public String toString() {
        return "Tree{" + "cellIndex=" + cellIndex + ", size=" + size + ", isMine=" + isMine + ", isDormant=" + isDormant
                + '}';
    }
}

class ActionWait implements Action {

    public String getCommand() {
        return WAIT + " Zzz";
    }

    @Override
    public String toString() {
        return "ActionWait{}";
    }
}

class ActionGrow implements Action {

    private final int targetCellId;

    public ActionGrow(int targetCellId) {
        this.targetCellId = targetCellId;
    }

    public String getCommand() {
        return GROW + " " + targetCellId + " Growing";
    }

    public int getTargetCellId() {
        return targetCellId;
    }

    @Override
    public String toString() {
        return "ActionGrow{" + "targetCellId=" + targetCellId + '}';
    }
}

class ActionSeed implements Action {

    private final int sourceCellId;
    private final int targetCellId;

    public ActionSeed(int sourceCellId, int targetCellId) {
        this.sourceCellId = sourceCellId;
        this.targetCellId = targetCellId;
    }

    public String getCommand() {
        return SEED + " " + sourceCellId + " " + targetCellId;
    }

    public int getSourceCellId() {
        return sourceCellId;
    }

    public int getTargetCellId() {
        return targetCellId;
    }

    @Override
    public String toString() {
        return "ActionSeed{" + "sourceCellId=" + sourceCellId + ", targetCellId=" + targetCellId + '}';
    }
}

class ActionComplete implements Action {

    private final int targetCellId;

    // AI Logic
    private final Zone zone;

    public ActionComplete(int targetCellId) {
        this.targetCellId = targetCellId;
        this.zone = Zone.getZoneFromCellId(targetCellId);
    }

    public String getCommand() {
        return COMPLETE + " " + targetCellId;
    }

    public int getId() {
        return targetCellId;
    }

    @Override
    public String toString() {
        return "ActionComplete{" + "targetCellId=" + targetCellId + ", zone=" + zone + '}';
    }
}

class ActionFactory {

    public static void parse(Game game, String action) {
        String[] parts = action.split(" ");
        switch (parts[0]) {
            case Action.WAIT:
                return;
            case Action.SEED:
                game.seedActions.add(new ActionSeed(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));
                return;
            case Action.GROW:
                game.growActions.add(new ActionGrow(Integer.parseInt(parts[1])));
                return;
            case Action.COMPLETE:
                game.completeActions.add(new ActionComplete(Integer.parseInt(parts[1])));
                return;
        }

        throw new InputMismatchException("Invalid action " + action);
    }
}

class Game {

    public static final ActionWait WAIT_ACTION = new ActionWait();
    public static final Map<Integer, Cell> BOARD = new HashMap<>();

    Map<Integer, Tree> trees = new HashMap<>();
    List<ActionGrow> growActions = new ArrayList<>();
    List<ActionSeed> seedActions = new ArrayList<>();
    List<ActionComplete> completeActions = new ArrayList<>();

    int[] myTreesSizes = new int[4];
    int[] numTressInZones = new int[3];

    int day;
    int nutrients;
    int mySun;
    int opponentSun;
    int myScore;
    int opponentScore;
    boolean opponentIsWaiting;

    private static boolean isTreeShadowed(int day, Collection<Tree> trees, Tree checkTree) {
        int shadowDirection = (day + 3) % 6;

        Cell cell = Game.BOARD.get(checkTree.getId());
        for (int i = 0; i < 3; i++) {

            if (cell.getNeighbours()[shadowDirection] == -1) {
                return false;
            }

            cell = Game.BOARD.get(cell.getNeighbours()[shadowDirection]);

            for (Tree shadowTree : trees) {
                if (shadowTree.getId() == cell.getId() && shadowTree.getSize() > i
                        && shadowTree.getSize() >= checkTree.getSize()) {
                    return true;
                }
            }
        }

        return false;
    }

    public void debug() {
        System.err.println(this);
    }

    public void debugActions() {
        System.err.println(this.seedActions);
        System.err.println(this.growActions);
        System.err.println(this.completeActions);
    }

    public Action decideNextAction() {
        debugActions();

        // Game Lasts 24 days
        if (day >= AiLogic.SELL_AFTER_DAY) {
            Action bestAction = getBestActionComplete(false);
            if (bestAction != null) {
                return bestAction;
            }

            ActionGrow bestActionB = getBestActionGrow();

            if (bestActionB != null) {
                return bestActionB;
            }

            return WAIT_ACTION;
        }

        if (myTreesSizes[3] >= AiLogic.TARGET_LVL_3_TREES) {
            Action bestAction = getBestActionComplete(true);
            if (bestAction != null) {
                return bestAction;
            }
        }

        if (day >= AiLogic.CONSERVE_AFTER_DAY) {
            if (myTreesSizes[0] + myTreesSizes[1] + myTreesSizes[2] >= AiLogic.MAX_BABYS) {
                ActionGrow bestActionB = getBestActionGrow();

                if (bestActionB != null) {
                    return bestActionB;
                } else {
                    return WAIT_ACTION;
                }
            }
        }

        // Capture the middle
        // Find if i have a spell seed in the green zone
        if (day < 4) {
            for (ActionSeed action : seedActions) {
                if (numTressInZones[0] < AiLogic.MAX_TREES_IN_GREEN
                        && Game.BOARD.get(action.getTargetCellId()).getZone() == AiLogic.TARGET_ZONE) {
                    return action;
                }
            }
        } else {
            if (numTressInZones[0] + numTressInZones[1] + numTressInZones[2] < AiLogic.MAX_TREES_IN_GREEN) {
                int mostEnemies = 0;
                Action bestAction = null;
                for (ActionSeed action : seedActions) {
                    int sumForCell = 0;
                    for (int neighbour : BOARD.get(action.getTargetCellId()).getNeighbours()) {
                        Tree tree = trees.get(neighbour);

                        if (neighbour == -1) {
                            continue;
                        }

                        if (tree != null && tree.isMine()) {
                            sumForCell += AiLogic.VALUE_IF_ALLY_NEAR;
                        }

                        if (tree != null && !tree.isMine()) {
                            sumForCell += AiLogic.VALUE_IF_ENEMY_NEAR;
                        }
                    }

                    if (sumForCell > mostEnemies && sumForCell < 5) {
                        mostEnemies = sumForCell;
                        bestAction = action;
                    }
                }
                if (bestAction != null) {
                    return bestAction;
                }
            }
        }

        // Grow grow grow
        ActionGrow bestActionB = getBestActionGrow();

        if (bestActionB != null) {
            return bestActionB;
        }

        return WAIT_ACTION;
    }

    private ActionGrow getBestActionGrow() {
        ActionGrow bestAction = null;
        int maxPoints = -1;

        for (ActionGrow action : growActions) {
            int totalPoints = getPointsForGrow(action);
            if (totalPoints > maxPoints) {
                bestAction = action;
                maxPoints = totalPoints;
            }
        }

        return bestAction;
    }

    private Action getBestActionComplete(boolean careForNextRound) {
        ActionComplete bestAction = null;

        int maxPoints = -1;
        for (ActionComplete action : completeActions) {
            int totalPoints = getPointsForComplete(action);

            if (careForNextRound) {
                boolean willTreeBeShadowedNextRound = isTreeShadowed(day + 1, trees.values(),
                        trees.get(action.getId()));
                totalPoints += willTreeBeShadowedNextRound ? 50 : -50;
            }

            if (totalPoints > maxPoints) {
                bestAction = action;
                maxPoints = totalPoints;
            }
        }

        if (bestAction != null) {
            return bestAction;
        }

        return null;
    }

    private int getPointsForComplete(ActionComplete action) {
        return BOARD.get(action.getId()).getRichness() + trees.get(action.getId()).getSize();
    }

    private int getPointsForGrow(ActionGrow action) {
        return BOARD.get(action.getTargetCellId()).getRichness() + trees.get(action.getTargetCellId()).getSize() + 1;
    }

    @Override
    public String toString() {
        return "Game{" + "board=" + BOARD + ", trees=" + trees + ", growActions=" + growActions + ", seedActions="
                + seedActions + ", completeActions=" + completeActions + ", myTreesSizes="
                + Arrays.toString(myTreesSizes) + ", day=" + day + ", nutrients=" + nutrients + ", mySun=" + mySun
                + ", opponentSun=" + opponentSun + ", myScore=" + myScore + ", opponentScore=" + opponentScore
                + ", opponentIsWaiting=" + opponentIsWaiting + '}';
    }
}

class Player {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Game game = new Game();

        int numberOfCells = in.nextInt();
        for (int i = 0; i < numberOfCells; i++) {
            int index = in.nextInt();
            int richness = in.nextInt();
            int neigh0 = in.nextInt();
            int neigh1 = in.nextInt();
            int neigh2 = in.nextInt();
            int neigh3 = in.nextInt();
            int neigh4 = in.nextInt();
            int neigh5 = in.nextInt();
            int[] neighs = new int[] { neigh0, neigh1, neigh2, neigh3, neigh4, neigh5 };
            Cell cell = new Cell(index, richness, neighs);
            Game.BOARD.put(index, cell);
        }

        // TODO Track from board cell values richness

        while (true) {
            loadGameStateGeneral(in, game);
            readGameStateTrees(in, game);
            readGameStateActions(in, game);

            Action action = game.decideNextAction();
            System.out.println(action.getCommand() + " " + Arrays.toString(game.myTreesSizes));
        }
    }

    private static void loadGameStateGeneral(Scanner in, Game game) {
        game.day = in.nextInt();
        game.nutrients = in.nextInt();
        game.mySun = in.nextInt();
        game.myScore = in.nextInt();
        game.opponentSun = in.nextInt();
        game.opponentScore = in.nextInt();
        game.opponentIsWaiting = in.nextInt() != 0;
    }

    private static void readGameStateActions(Scanner in, Game game) {
        game.growActions.clear();
        game.seedActions.clear();
        game.completeActions.clear();

        int numberOfPossibleActions = in.nextInt();
        in.nextLine();

        for (int i = 0; i < numberOfPossibleActions; i++) {
            String possibleAction = in.nextLine();

            // Parse each possible action and add it to the lists
            ActionFactory.parse(game, possibleAction);
        }
    }

    private static void readGameStateTrees(Scanner in, Game game) {
        game.trees.clear();
        game.myTreesSizes = new int[4];
        game.numTressInZones = new int[3];

        int numberOfTrees = in.nextInt();
        for (int i = 0; i < numberOfTrees; i++) {
            int cellIndex = in.nextInt();
            int size = in.nextInt();
            boolean isMine = in.nextInt() != 0;
            boolean isDormant = in.nextInt() != 0;

            Tree tree = new Tree(cellIndex, size, isMine, isDormant);
            game.trees.put(cellIndex, tree);

            if (isMine) {
                game.myTreesSizes[size] = game.myTreesSizes[size] + 1;
                int index = cellIndex < 7 ? 0 : cellIndex < 19 ? 1 : 2;
                game.numTressInZones[index]++;
            }
        }
    }
}