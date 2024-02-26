package engine;

import engine.pieces.Piece;
import engine.player.AI;
import engine.player.Human;
import engine.player.Player;
import engine.ui.Board;
import engine.ui.Renderer;
import javax.swing.*;
import java.util.ArrayList;
import static engine.helpers.GlobalHelper.TEAM_ONE;
import static engine.helpers.GlobalHelper.TEAM_TWO;

public class Engine implements Runnable {
    JFrame frame;
    Renderer renderer;
    Player playerOne;
    Player playerTwo;
    Board board;
    private boolean running;
    private int playerTurn;
    private Thread thread;

    // Generic bootstrap code to initialize a swing project.
    // Renderer is where all graphics handling occurs.
    public Engine() {
        this.renderer = new Renderer();
        this.frame = new JFrame("Chessboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(renderer);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Initialize all game components
    public void initialize() {
        playerOne = new Human(TEAM_ONE, renderer.getPieceImages(1), false);
        playerTwo = new AI(TEAM_TWO, renderer.getPieceImages(2), true);
        playerTurn = TEAM_ONE;

        //========================================================================
        ArrayList<Piece> gamePieces = new ArrayList<>(playerOne.getPieces());
        gamePieces.addAll(playerTwo.getPieces());
        //========================================================================

        board = new Board(gamePieces, renderer);

    }

    // When start is called on a thread it auto-invokes a Runnable's run() method.
    public synchronized void start() {
        if (running) return;

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    // Call to pause engine execution
    public synchronized void stop() {
        if (!running) return;

        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run(){
        while(running){
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
}
