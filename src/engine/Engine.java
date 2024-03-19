package engine;

import engine.hardware.pieces.Piece;
import engine.player.AI;
import engine.player.Human;
import engine.player.Player;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import engine.ui.Renderer;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

import static engine.helpers.GlobalHelper.*;

public class Engine implements Runnable {
    JFrame frame;
    Renderer renderer;
    Scanner scanner;
    Player playerOne;
    Player playerTwo;
    Board board;
    private boolean running;
    private volatile AtomicInteger playerTurn;
    private Thread thread;

    // Generic bootstrap code to initialize a swing project.
    // Renderer is where all graphics handling occurs.
    public Engine() {
        this.scanner = new Scanner(System.in);
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
        playerOne = new Human(TEAM_ONE, renderer.getPieceImages(TEAM_ONE));
        playerTwo = new AI(TEAM_TWO, renderer.getPieceImages(2));
//        playerTwo = new Human(TEAM_TWO, renderer.getPieceImages(TEAM_TWO));
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

    String input;

    @Override
    public void run(){
        while(running){
            if (playerTurn.get() == TEAM_ONE && playerOne.isAI()) {
                renderer.performAiMove(playerOne.generateMove(board.getT1Pieces(), board));
                renderer.removeCheckFromSelf();
                refreshGame();
            }

            if (board.checkGameStatus() != CONTINUE_GAME)
                endGame(board.checkGameStatus());

            if (playerTurn.get() == TEAM_TWO && playerTwo.isAI()) {
                    renderer.performAiMove(playerTwo.generateMove(board.getT2Pieces(), board));
                    renderer.removeCheckFromSelf();
                    refreshGame();
            }

            if (board.checkGameStatus() != CONTINUE_GAME)
                endGame(board.checkGameStatus());

        }
    }

    private void printAppDump(Player player) {
        System.out.println("======= Player " + player.getTeam() + "=======");
        System.out.println("inCheck: " + player.isInCheck());
        System.out.println("canMove: " + player.canMove());
        ArrayList<Coordinate> moves = new ArrayList<>();
        for(Piece p : player.getPieces()){
            moves.addAll(p.getMovesDeep(board, true));
        }
        System.out.println("available moves: " + moves.size());
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
        System.out.println("======= Game over! ========");
        if (code == TEAM_ONE) {
            if (playerTwo.isAI()) {
                System.out.println("AI wins!");
            } else {
                System.out.println("Team 2 is winner!");
            }
        } else if (code == TEAM_TWO){
            if (playerTwo.isAI())
                System.out.println("You are a winner!");
            else
                System.out.println("Team 1 is winner!");
            }
        else System.out.println("Game is a draw.");
    }
}
