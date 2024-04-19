package reversi;

import java.util.ArrayList;

public class ReversiController implements IController {

    private IModel model;
    private IView view;
    private int[] xOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
    private int[] yOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};
    private ArrayList<Integer[]> allOpponentPieces = new ArrayList<>();
    private String message1;
    private String message2;

    @Override
    public void initialise(IModel model, IView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void startup() {
        model.setPlayer(1);
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if ((x == height/2 && y == width/2 - 1) || (x == height/2 - 1 && y == width/2)) {
                    model.setBoardContents(x, y, 2);
                } else if ((x == height/2 - 1 && y == width/2 - 1) || (x == height/2 && y == width/2)) {
                    model.setBoardContents(x, y, 1);
                } else {
                    model.setBoardContents(x, y, 0);
                }
            }
        }
        update();
    }

    @Override
    public void update() {
        boolean finished = true;
        int currentPlayer = model.getPlayer();

        for (int x = 0; x < model.getBoardWidth(); x++) {
            for (int y = 0; y < model.getBoardHeight(); y++) {
                if (model.getBoardContents(x, y) == 0 && isValidMove(currentPlayer, x, y)) {
                    finished = false;
                    break;
                }
            }
            if (!finished) {
                break;
            }
        }

        model.setFinished(finished);

        if (!finished) {
            if (currentPlayer == 1) {
                message1 = "White player – choose where to put your piece";
                message2 = "Black player – not your turn";
            } else {
            	message1 = "White player – not your turn";
                message2 = "Black player – choose where to put your piece";
            }
        } else {
            int player1Count = 0;
            int player2Count = 0;
            for (int x = 0; x < model.getBoardWidth(); x++) {
                for (int y = 0; y < model.getBoardHeight(); y++) {
                    int piece = model.getBoardContents(x, y);
                    if (piece == 1) {
                        player1Count++;
                    } else if (piece == 2) {
                        player2Count++;
                    }
                }
            }

            if (player1Count > player2Count) {
                message1 = "White won. White " + player1Count + " to Black " + player2Count + ". Reset game to replay.";
                message2 = message1;
            } else if (player2Count > player1Count) {
                message2 = "Black won. Black " + player2Count + " to White " + player1Count + ". Reset game to replay.";
                message1 = message2;
            } else {
                message1 = "Draw. Both players ended with " + player1Count + " pieces. Reset game to replay.";
                message2 = message1;
            }

        }

        view.feedbackToUser(1, message1);
        view.feedbackToUser(2, message2);
        view.refreshView();
    }
    

    @Override
    public void squareSelected(int player, int x, int y) {
    	if (player != model.getPlayer()) {
            view.feedbackToUser(player, "It is not your turn!");
            return;
        }
        if (model.hasFinished()) {
            update();
            return;
        }
        if (!isValidMove(player, x, y)) {
            view.feedbackToUser(player, "Invalid move!");
            return;
        }
        playPiece(player, x, y);
    }
    
    private boolean isValidMove(int player, int x, int y) {
        if (model.getBoardContents(x, y) != 0) {
            return false;
        }
        if (!canCaptureOpponent(player, x, y)) {
            return false;
        }
        return true;
    }
    
    private boolean canCaptureOpponent(int player, int x, int y) {
    	allOpponentPieces = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ArrayList<Integer[]> opponentPieces = new ArrayList<>();
            int dx = xOffsets[i];
            int dy = yOffsets[i];

            boolean ownPieceFound = false;

            int newX = x + dx;
            int newY = y + dy;
            while (isValidPosition(newX, newY)) {
                int piece = model.getBoardContents(newX, newY);
                if (piece == player) {
                    ownPieceFound = true;
                    break;
                } else if (piece == 0) {
                    ownPieceFound = false;
                    break;
                } else {
                    opponentPieces.add(new Integer[]{newX, newY});
                }
                newX += dx;
                newY += dy;
            }

            if (ownPieceFound && opponentPieces.size() > 0) {
            	allOpponentPieces.addAll(opponentPieces);
            }
        }

        return !allOpponentPieces.isEmpty();
    }
    
    
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < model.getBoardWidth() && y >= 0 && y < model.getBoardHeight();
    }
    
    private void playPiece(int player, int x, int y) {
        model.setBoardContents(x, y, player);

        for (Integer[] coords : allOpponentPieces) {
            int oppX = coords[0];
            int oppY = coords[1];
            model.setBoardContents(oppX, oppY, player);
        }
        switchPlayers();
        update();
    }



    private void switchPlayers() {
        int currentPlayer = model.getPlayer();
        int nextPlayer = (currentPlayer == 1) ? 2 : 1;
        model.setPlayer(nextPlayer);
    }

    @Override
    public void doAutomatedMove(int player) {
    	if (player != model.getPlayer()) {
            view.feedbackToUser(player, "It's not your turn!");
            return;
        }
    	
        ArrayList<Integer[]> validMoves = new ArrayList<>();

        for (int x = 0; x < model.getBoardWidth(); x++) {
            for (int y = 0; y < model.getBoardHeight(); y++) {
                if (isValidMove(player, x, y)) {
                    validMoves.add(new Integer[]{x, y});
                }
            }
        }

        if (validMoves.isEmpty()) {
            view.feedbackToUser(player, "No valid moves available. Passing turn.");
            switchPlayers();
            update();
            return;
        }

        int maxCaptures = -1;
        int selectedX = -1;
        int selectedY = -1;
        for (Integer[] move : validMoves) {
            int x = move[0];
            int y = move[1];
            canCaptureOpponent(player, x, y);
            if (allOpponentPieces.size() > maxCaptures) {
                maxCaptures = allOpponentPieces.size();
                selectedX = x;
                selectedY = y;
            }
        }

        canCaptureOpponent(player, selectedX, selectedY);
        playPiece(player, selectedX, selectedY);
    }


}