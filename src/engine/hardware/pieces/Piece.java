package engine.hardware.pieces;

import engine.player.Player;
import engine.simulate.BoardSimulator;
import engine.hardware.Board;
import engine.hardware.Coordinate;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static engine.helpers.GlobalHelper.*;

public abstract class Piece {
    private final boolean isAI;
    private boolean isAlive;
    Coordinate position;
    Coordinate drawPosition;
    private final int team;
    private final Image sprite;
    private boolean hasCheck;

    public Piece(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        this.isAI = isAI;
        this.isAlive = true;
        this.position = position;
        this.drawPosition = drawPosition;
        this.team = team;
        this.sprite = sprite;
        this.hasCheck =  false;
    }

    public Piece(Piece piece) {
        this.isAI = piece.isAI();
        this.position = new Coordinate(piece.position.getX(), piece.position.getY());
        this.drawPosition = new Coordinate(piece.drawPosition.getX(), piece.drawPosition.getY());
        this.team = piece.getTeam();
        this.sprite = piece.getSprite();
        this.hasCheck = piece.hasCheck;
        this.isAlive = piece.isAlive;
    }

    public boolean isAI() {
        return isAI;
    }

    public boolean canMove(Board b) {
        return !getMovesDeep(b,true).isEmpty();
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
        this.drawPosition = new Coordinate(convertToPixelX(position.getX(), 512), convertToPixelY(position.getY(), 512));
    }

    public Coordinate getDrawPosition() {
        return drawPosition;
    }

    public void setDrawPosition(Coordinate drawPosition) {
        this.drawPosition = drawPosition;
    }

    public int getTeam() {
        return team;
    }

    public boolean isHasCheck() {
        return hasCheck;
    }

    public void setHasCheck(boolean hasCheck) {
        this.hasCheck = hasCheck;
    }

    public Image getSprite() {
        return sprite;
    }

    public ArrayList<Coordinate> getMovesDeep(Board b, boolean check) {
        ArrayList<Coordinate> possibleMoves = addAllPossibleMoves(b, check);
        removeIllegalMoves(possibleMoves, b);
        addCastles(possibleMoves, b);
        return possibleMoves;
    }

    public ArrayList<Coordinate> getMovesShallow(Board b) {
        ArrayList<Coordinate> possibleMoves = addAllPossibleMoves(b, false);
        addCastles(possibleMoves, b);
        return possibleMoves;

    }

    public abstract Piece copy();

    public void addCastles(ArrayList<Coordinate> possibleMoves, Board b) {
        // Nothing, will be overridden when needed
    }

    abstract ArrayList<Coordinate> addAllPossibleMoves(Board b, boolean check);

    void removeIllegalMoves(ArrayList<Coordinate> possibleMoves, Board b){
        removeOffBoardMoves(possibleMoves, b);
        removeAllMovesOnSelf(possibleMoves, b);
            removeAllOtherMoves(possibleMoves, b);
    }

    public ArrayList<Coordinate> getAvailableMovesInCheck(Board b, ArrayList<Coordinate> movesList) {
        Player player = b.getPlayer(getTeam());
        ArrayList<Piece> piecesThatHaveCheck = player.getPiecesThatHaveCheck();

        if (removeCheckAndPieceCannotMove(b, piecesThatHaveCheck))
            return movesList;
        else
            return getMovesInCheckHelper(b, movesList);
    }

    public boolean removeCheckAndPieceCannotMove(Board b, ArrayList<Piece> piecesThatHaveCheck){
        BoardSimulator sim = new BoardSimulator(b);
        sim.removePieces(piecesThatHaveCheck);
        sim.runPlayerCheck(getTeam());
        return sim.movingThePieceWillResultInCheck(this);

    }

    private ArrayList<Coordinate> getMovesInCheckHelper(Board b, ArrayList<Coordinate> movesList){
        BoardSimulator sim = new BoardSimulator(b);
        for(Coordinate move : this.getMovesDeep(b, false)){
            if (!sim.isInCheckAfterMove(move, this))
                movesList.add(move);
            sim.reset();
        }
        return movesList;
    }

    public boolean pieceMoveWillResultInCheck(Board b) {
        BoardSimulator sim = new BoardSimulator(b);
        return sim.movingThePieceWillResultInCheck(this);
    }

    public Player getPlayer(Board b){
        return team == TEAM_ONE ? b.getPlayerOne() : b.getPlayerTwo();
    }

    private void removeOffBoardMoves(ArrayList<Coordinate> possibleMoves, Board b) {
        List<Coordinate> illegalMoves = possibleMoves.stream().filter(move -> move.getX() < 0 || move.getY() < 0 || move.getX() > 7 || move.getY() > 7).toList();
        illegalMoves.forEach(possibleMoves::remove);
    }
    private void removeAllMovesOnSelf(ArrayList<Coordinate> possibleMoves, Board b){
        Iterator<Coordinate> iterator = possibleMoves.iterator();
        while (iterator.hasNext()) {
            Coordinate move = iterator.next();
            for (Piece piece : b.getPieces()) {
                if (move.equals(piece.getPosition()) && this.getTeam() == piece.getTeam()) {
                    iterator.remove();
                    break;
                }
            }
        }
    }

    abstract void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b);

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true; // Same object reference
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false; // Different classes or null reference
        }
        Piece other = (Piece) obj; // Typecast to Person
        return isAI == other.isAI && position.equals(other.position) && drawPosition.equals(other.drawPosition)
                && team == other.getTeam() && sprite.equals(other.sprite);
    }

    public boolean isAlive(){
        return this.isAlive;
    }

    public void remove() {
        this.isAlive = false;
    }

}

