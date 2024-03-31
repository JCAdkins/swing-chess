package engine.hardware.pieces;

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

    public Piece(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        this.isAI = isAI;
        this.isAlive = true;
        this.position = position;
        this.drawPosition = drawPosition;
        this.team = team;
        this.sprite = sprite;
    }

    public Piece(Piece piece) {
        this.isAI = piece.isAI();
        this.position = new Coordinate(piece.position.getX(), piece.position.getY());
        this.drawPosition = new Coordinate(piece.drawPosition.getX(), piece.drawPosition.getY());
        this.team = piece.getTeam();
        this.sprite = piece.getSprite();
        this.isAlive = piece.isAlive;
    }

    public boolean isAI() {
        return isAI;
    }

    public boolean canMove(Board b) {
        return !getMovesDeep(b).isEmpty();
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

    public Image getSprite() {
        return sprite;
    }

    /**
     * @param b The current board you want to pull moves from for the piece. This method gets the moves
     *          and removes all those that are illegal. I.e., moving the piece will put the king in check
     *
     * @return Returns a full list of legal moves
     */
    public ArrayList<Coordinate> getMovesDeep(Board b) {
        if (!this.isAlive)
            return new ArrayList<>();
        ArrayList<Coordinate> possibleMoves = addAllPossibleMoves(b);
        addCastles(possibleMoves, b);
        removeIllegalMoves(possibleMoves, b, true);
        return possibleMoves;
    }

    /**
     * This method is ONLY to be used by methods invoked by getMovesDeep to avoid stack overflow
     * error from recursive calls. Using this for any other reasons will result in issues.
     *
     * @param b The current board you want to pull moves from for the piece. This method gets the moves
     *          and removes some of those that are illegal.
     *
     * @return Returns a list of legal and illegal moves
     */
    public ArrayList<Coordinate> getMovesShallow(Board b) {
        if (!this.isAlive)
            return new ArrayList<>();
        ArrayList<Coordinate> possibleMoves = addAllPossibleMoves(b);
        addCastles(possibleMoves, b);
        removeIllegalMoves(possibleMoves, b, false);
        return possibleMoves;
    }

    public abstract Piece copy();

    public void addCastles(ArrayList<Coordinate> possibleMoves, Board b) {
        // Nothing, will be overridden by King and Rook classes
    }

    abstract ArrayList<Coordinate> addAllPossibleMoves(Board b);

    void removeIllegalMoves(ArrayList<Coordinate> possibleMoves, Board b, boolean removeAllOthers){
        removeOffBoardMoves(possibleMoves, b);
        removeAllMovesOnSelf(possibleMoves, b);
        if (removeAllOthers)
            removeAllOtherMoves(possibleMoves, b);
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

    public void removeAllOtherMoves(ArrayList<Coordinate> possibleMoves, Board b){
        BoardSimulator sim = new BoardSimulator(b);
        List<Coordinate> copyList = List.copyOf(possibleMoves);
        for (Coordinate move : copyList){
            if (sim.isInCheckAfterMove(move, this))
                possibleMoves.remove(move);
            sim.reset();
        }
    }

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
        this.position = OFF_BOARD;
        this.drawPosition = OFF_BOARD;
    }

}

