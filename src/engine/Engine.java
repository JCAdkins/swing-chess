package engine;

import engine.hardware.Coordinate;
import engine.hardware.pieces.Piece;
import engine.player.AI;
import engine.player.Human;
import engine.player.Player;
import engine.hardware.Board;
import engine.stockfish.FenGenerator;
import engine.stockfish.StockFishAI;
import engine.ui.components.panels.EndGamePanel;
import engine.ui.Renderer;
import engine.ui.components.panels.UpgradePawnPanel;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import static engine.helpers.GlobalHelper.*;
import static java.util.function.Function.identity;

public class Engine implements Runnable {
    JFrame frame;
    EndGamePanel endGamePanel;
    UpgradePawnPanel upgradePawnPanel;
    Renderer renderer;
    Player playerOne;
    Player playerTwo;
    Board board;
    private boolean running;
    private volatile AtomicInteger playerTurn;
    private Thread thread;
    private final StockFishAI stockFishAI;
    private final FenGenerator fenGenerator;


    // Generic bootstrap code to initialize a swing project.
    // Renderer is where all graphics handling occurs.
    public Engine() {
        this.stockFishAI = new StockFishAI();
        this.fenGenerator = new FenGenerator();
        this.renderer = new Renderer(this);
        this.frame = new JFrame("Chessboard");
        this.endGamePanel = new EndGamePanel(this);
        endGamePanel.setVisible(false);
        this.upgradePawnPanel = new UpgradePawnPanel(this);
        upgradePawnPanel.setVisible(false);

        JLayeredPane containerPanel = new JLayeredPane();
        containerPanel.setPreferredSize(renderer.getPreferredSize());
        containerPanel.setBackground(Color.black);
        renderer.setBounds(0,0,512,518);
        endGamePanel.setBounds(96,96,320,320);
        upgradePawnPanel.setBounds(96,96,320,320);
        containerPanel.add(renderer, 0, 0);
        containerPanel.add(endGamePanel, 1, 0);
        containerPanel.add(upgradePawnPanel,1,0);

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
        playerOne = new Human(TEAM_ONE, playerOneSprites);
//        playerOne = new AI(TEAM_ONE, playerOneSprites);
        playerTwo = new AI(TEAM_TWO, playerTwoSprites);
//        playerTwo = new Human(TEAM_TWO, playerTwoSprites);
        playerTurn = new AtomicInteger(TEAM_ONE);
        playerOne.setPlayerTurn(true);
        playerTwo.setPlayerTurn(false);
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
        try {
            stockFishAI.sendCommand("uci", identity(), (s) -> s.startsWith("uciok"), 2000L);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        while(running) {
//            simpleAI();
            stockFishAI();
        }
    }

    // This AI method was very simplistic and was intended to be used only for app development.
    // Post app development Stockfish is to be used to generate AI moves.
    private void simpleAI() {
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

    private void stockFishAI(){
        if (playerOne.isPlayerTurn() && playerOne.isAI() && playerOne.canMove()) {
            renderer.performAiMove(playerOne.generateStockFishMove(stockFishAIMove()));
            renderer.removeCheckFromSelf();
            refreshGame();
        }

        if (board.checkGameStatus(playerTurn.get()) != CONTINUE_GAME)
            endGame(board.checkGameStatus(playerTurn.get()));

        if (playerTwo.isPlayerTurn() && playerTwo.isAI() && playerTwo.canMove()) {
            ArrayList<Coordinate> move = playerTwo.generateStockFishMove(stockFishAIMove());
            renderer.performAiMove(move);
            renderer.removeCheckFromSelf();
            refreshGame();
        }

        if (board.checkGameStatus(playerTurn.get()) != CONTINUE_GAME)
            endGame(board.checkGameStatus(playerTurn.get()));
    }
    private String stockFishAIMove(){
        try {

            ArrayList<String> list = (ArrayList<String>) stockFishAI.sendCommand("position fen " + fenGenerator.generate(board), identity(), s -> s.startsWith("readyok"), 2000L);
            System.out.println(list);

            return stockFishAI.sendCommand("go movetime 3000", lines -> lines.stream().filter(s->s.startsWith("bestmove")).findFirst().get(), (line) -> line.startsWith("bestmove"), 5000L).split(" ")[1];
        } catch (Exception e) {
            throw new RuntimeException(e);
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
        if (playerTurn.get() == TEAM_ONE) {
            playerOne.setPlayerTurn(true);
            playerTwo.setPlayerTurn(false);
        } else {
            playerOne.setPlayerTurn(false);
            playerTwo.setPlayerTurn(true);
        }
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

    public void setUpgradePawnPanelVisible(boolean b) {
        upgradePawnPanel.setVisible(b);
    }

    public void upgradePlayerPawn(Class<? extends Piece> cl, Image image){
        System.out.println(cl);
        renderer.setUpgradedPawn(cl, image);
        upgradePawnPanel.setVisible(false);
    }

    public void setUpgradePawnPanelIcons(Image[] pieceImages) {
        upgradePawnPanel.setImageIcons(pieceImages);
    }
}
