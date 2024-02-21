package engine;

import engine.pieces.Piece;
import engine.player.AI;
import engine.player.Human;
import engine.player.Player;
import engine.ui.Board;
import engine.ui.Renderer;
import javax.swing.*;
import java.util.ArrayList;

public class Engine implements Runnable {
    private static final int TEAM_ONE = 1;
    private static final int TEAM_TWO = -1;
    JFrame frame;
    Renderer renderer;
    Player playerOne;
    Player playerTwo;
    Board board;

    public Engine() {
        this.renderer = new Renderer();
        this.frame = new JFrame("Chessboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(renderer);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void initialize() {
        playerOne = new Human(1, renderer.getPieceImages(1), false);
        playerTwo = new AI(2, renderer.getPieceImages(2), true);

        //========================================================================
        ArrayList<Piece> gamePieces = new ArrayList<>(playerOne.getPieces());
        gamePieces.addAll(playerTwo.getPieces());
        //========================================================================

        board = new Board(gamePieces, renderer);

    }

    @Override
    public void run(){
        int playerTurn = TEAM_ONE;
        while(true){
            if (playerTurn == TEAM_ONE){
                if (playerOne.isAI()) {
                    playerOne.generateMove();
                    renderer.repaint();
                    playerTurn *= -1; // Set turn to player two.
                }

            }
            if (playerTurn == TEAM_TWO) {
                if (playerTwo.isAI()) {
                    playerTwo.generateMove();
                    renderer.repaint();
                    playerTurn *= -1; // Set turn to player one.
                }
            }

        }
    }

    private boolean getGameStatus() {
        //System.out.println("getting game status");
        return true;
    }

}
