package engine;

import engine.pieces.Piece;
import engine.player.AI;
import engine.player.Human;
import engine.player.Player;
import engine.ui.Board;
import engine.ui.Renderer;
import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static engine.helpers.GlobalHelper.*;

public class Engine implements Runnable {
    JFrame frame;
    Renderer renderer;
    Player playerOne;
    Player playerTwo;
    Board board;
    private boolean running;
    private volatile AtomicInteger playerTurn;
    private Thread thread;

    // Generic bootstrap code to initialize a swing project.
    // Renderer is where all graphics handling occurs.
    public Engine() {
        this.renderer = new Renderer(this);
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
        playerTurn = new AtomicInteger(TEAM_ONE);

        //========================================================================
        ArrayList<Piece> gamePieces = new ArrayList<>(playerOne.getPieces());
        gamePieces.addAll(playerTwo.getPieces());
        //========================================================================

        board = new Board(gamePieces, renderer, playerOne, playerTwo);

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
            if (playerTurn.get() == TEAM_ONE){
                if (playerOne.isAI()) {
                    renderer.performAiMove(playerOne.generateMove(board.getT1Pieces(), board));
                    renderer.repaint();
                    switchPlayers();
                    runPlayerChecks();
                }
            }

            if (board.checkGameStatus() != CONTINUE_GAME)
                endGame(board.checkGameStatus());

            if (playerTurn.get() == TEAM_TWO) {
                if (playerTwo.isAI()) {
                    renderer.performAiMove(playerTwo.generateMove(board.getT2Pieces(), board));
                    renderer.repaint();
                    switchPlayers();
                    runPlayerChecks();
                }
            }

            if (board.checkGameStatus() != CONTINUE_GAME)
                endGame(board.checkGameStatus());


        }
    }

    public void runPlayerChecks() {
        playerOne.runChecks(board);
        playerTwo.runChecks(board);
    }

    public void switchPlayers(){
        playerTurn.set(playerTurn.get() * SWITCH_TEAM);
    }

    public int getPlayerTurn(){
        return playerTurn.get();
    }

    private void endGame(int i) {
//        System.out.println("Game over!\nCode: " + i);
    }
}
