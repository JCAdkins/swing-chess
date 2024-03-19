package engine;

import engine.hardware.pieces.Piece;
import engine.player.Human;
import engine.player.Player;
import engine.hardware.Board;
import engine.ui.components.EndGamePanel;
import engine.ui.Renderer;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static engine.helpers.GlobalHelper.*;

public class Engine implements Runnable {
    JFrame frame;
    EndGamePanel endGamePanel;
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
        this.endGamePanel = new EndGamePanel(this);
        endGamePanel.setVisible(false);

        JLayeredPane containerPanel = new JLayeredPane();
        containerPanel.setPreferredSize(renderer.getPreferredSize());
        containerPanel.setBackground(Color.black);
        renderer.setBounds(0,0,512,518);
        endGamePanel.setBounds(96,96,320,320);
        containerPanel.add(renderer, 0, 0);
        containerPanel.add(endGamePanel, 1, 0);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(containerPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Initialize all game components
    public void initialize() {
        playerOne = new Human(TEAM_ONE, renderer.getPieceImages(TEAM_ONE));
//        playerTwo = new AI(TEAM_TWO, renderer.getPieceImages(2));
        playerTwo = new Human(TEAM_TWO, renderer.getPieceImages(TEAM_TWO));
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
            if (playerTurn.get() == TEAM_ONE && playerOne.isAI()) {
                renderer.performAiMove(playerOne.generateMove(board.getT1Pieces(), board));
                renderer.removeCheckFromSelf();
                refreshGame();
            }

            if (board.checkGameStatus(playerTurn.get()) != CONTINUE_GAME)
                endGame(board.checkGameStatus(playerTurn.get()));

            if (playerTurn.get() == TEAM_TWO && playerTwo.isAI()) {
                    renderer.performAiMove(playerTwo.generateMove(board.getT2Pieces(), board));
                    renderer.removeCheckFromSelf();
                    refreshGame();
            }

            if (board.checkGameStatus(playerTurn.get()) != CONTINUE_GAME)
                endGame(board.checkGameStatus(playerTurn.get()));
        }
    }

    private void refreshGame() {
        renderer.repaint();
        runPlayerChecks();
    }

    public void runPlayerChecks() {
        playerOne.runChecks(board, true);
        playerTwo.runChecks(board, true);
    }

    public void switchPlayers(){
        playerTurn.set(playerTurn.get() * SWITCH_TEAM);
    }

    public int getPlayerTurn(){
        return playerTurn.get();
    }

    private void endGame(int code) {
        String message;
        if (code == CHECK_MATE) {
            if (playerTurn.get() == TEAM_ONE)
                message = "Player two wins!";
            else
                message = "Player one wins!";
        }
        else
            message = "Game's a draw!";

        System.out.println("game over.");
        endGamePanel.setMessage(message);
        endGamePanel.setVisible(true);
        stop();


    }

    public void replayGame() {
        renderer.reset();
        initialize();
        endGamePanel.setVisible(false);
    }

    public void changeSettings() {
    }

    public void quit() {
        System.exit(0);
    }
}
