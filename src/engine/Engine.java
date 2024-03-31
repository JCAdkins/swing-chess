package engine;

import engine.player.AI;
import engine.player.Human;
import engine.player.Player;
import engine.hardware.Board;
import engine.ui.components.EndGamePanel;
import engine.ui.Renderer;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Scanner;
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
        Image[] playerOneSprites = renderer.getPieceImages(TEAM_ONE);
        Image[] playerTwoSprites = renderer.getPieceImages(TEAM_TWO);
//        playerOne = new Human(TEAM_ONE, playerOneSprites);
        playerOne = new AI(TEAM_ONE, playerOneSprites);
        playerTwo = new AI(TEAM_TWO, playerTwoSprites);
//        playerTwo = new Human(TEAM_TWO, playerTwoSprites);
        playerTurn = new AtomicInteger(TEAM_ONE);
        board = new Board(renderer, playerOne, playerTwo);
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
        while(running) {
                if (playerTurn.get() == TEAM_ONE && playerOne.isAI() && playerOne.canMove()) {
                    renderer.performAiMove(playerOne.generateMove(board.getT1Pieces(), board));
                    renderer.removeCheckFromSelf();
                    refreshGame();
                }

                if (board.checkGameStatus(playerTurn.get()) != CONTINUE_GAME)
                    endGame(board.checkGameStatus(playerTurn.get()));

                if (playerTurn.get() == TEAM_TWO && playerTwo.isAI() && playerTwo.canMove()) {
                    renderer.performAiMove(playerTwo.generateMove(board.getT2Pieces(), board));
                    renderer.removeCheckFromSelf();
                    refreshGame();
                }

                if (board.checkGameStatus(playerTurn.get()) != CONTINUE_GAME)
                    endGame(board.checkGameStatus(playerTurn.get()));

        }
    }

    private void refreshGame() {
        runPlayerChecks();
        renderer.repaint();

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
        this.initialize();
        endGamePanel.setVisible(false);
    }

    public void changeSettings() {
        endGamePanel.setVisible(false);
    }

    public void quit() {
        System.exit(0);
    }

    public void hideEndGameDisplay() {
        endGamePanel.setVisible(false);
    }
}
