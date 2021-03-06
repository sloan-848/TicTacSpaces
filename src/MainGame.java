import java.util.Scanner;

public class MainGame {

    public static void fillNodes(State root, boolean playerTurn) {
        byte[][] data = root.getState();

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (data[i][j] == GameState.Space.EMPTY.getCode()) {
                    State child = new GameState();
                    child.transferState(root);
                    root.addChild(child);
                    if (playerTurn) {
                        child.makeMove(i, j, GameState.Space.PLAYER.getCode());
                    } else {
                        child.makeMove(i, j, GameState.Space.OPPONENT.getCode());
                    }

                    if (!child.isFinished()) {
                        fillNodes(child, !playerTurn);
                    }
                }
            }
        }

    }

    public static void printChildren(State root) {
        if (root.getChildren().length == 0) {
            System.out.println(root.toString());
        } else {
            for (State waywardSon : root.getChildren()) {
                printChildren(waywardSon);
            }
        }
    }

    public static int evalMove(State root) {
        int bestState = miniMax(root, true);
        return bestState;
    }

    public static int miniMax(State root, boolean isPlayer) {
        int bestState = 0;

        if (root.getChildren().length == 0) {
            return 0;
        }

        if (isPlayer) {
            int bestScore = miniMax(root.getChildren()[0], false);
            for (int i = 1; i < root.getChildren().length; i++) {
                int tempScore = miniMax(root.getChildren()[i], false);
                if (tempScore > bestScore) {
                    bestScore = tempScore;
                    bestState = i;
                }
            }
        } else {
            int bestScore = miniMax(root.getChildren()[0], false);
            for (int i = 1; i < root.getChildren().length; i++) {
                int tempScore = miniMax(root.getChildren()[i], false);
                if (tempScore < bestScore) {
                    bestScore = tempScore;
                    bestState = i;
                }
            }
        }
        return bestState;
    }

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        boolean gameFinished = false;

        //Generate Game Board
        State emptyState = new GameState();

        for (int i = 0; i < emptyState.getState().length; i++) {
            for (int j = 0; j < emptyState.getState()[0].length; j++) {
                emptyState.makeMove(i, j, GameState.Space.EMPTY.getCode());
            }
        }

        System.out.println("Loading...");
        //Generate state tree
        fillNodes(emptyState, true);

        //Aliased variable to keep track of current game state
        State currentState = emptyState;

        //Begin game
        System.out.println("I'll go first.");
        System.out.println(currentState);

        while (!gameFinished) { //Master game loop
            System.out.println("Thinking...");

            int myMove = evalMove(currentState);
            currentState = currentState.getChildren()[myMove];

            System.out.println(currentState);

            if (currentState.isFinished()) { //Check if finished
                switch (currentState.getWinner()) {
                    case PLAYER:
                        System.out.println("I won! :)");
                        break;
                    case OPPONENT:
                        System.out.println("You won. :(");
                        break;
                    case EMPTY:
                        System.out.println("Nobody won! :o");
                        break;
                }
                gameFinished = true;
                continue;
            }

            System.out.print("Enter your move: ");
            int nextMove = 0;
            while (!(nextMove <= 9 && nextMove > 0)) {
                nextMove = reader.nextInt();
            }
            int y = (nextMove - 1) / 3;
            int x = (nextMove - 1) % 3;

            for (State child : currentState.getChildren()) {
                if (child.getState()[y][x] == GameState.Space.OPPONENT
                        .getCode()) {
                    currentState = child;
                    break;
                }
            }
            System.out.println(currentState);

            if (currentState.isFinished()) { //Check if finished
                switch (currentState.getWinner()) {
                    case PLAYER:
                        System.out.println("I won! :)");
                        break;
                    case OPPONENT:
                        System.out.println("You won. :(");
                        break;
                    case EMPTY:
                        System.out.println("Nobody won! :o");
                        break;
                }
                gameFinished = true;
                continue;
            }
        }

        reader.close();
    }
}
