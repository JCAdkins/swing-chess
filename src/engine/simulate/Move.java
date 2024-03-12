package engine.simulate;

import engine.pieces.Piece;
import engine.ui.Coordinate;

public class Move {
    Piece piece = null;
    Coordinate from;
    Coordinate to;
    int number;
    int team;

    public Move(Piece piece, Coordinate from, Coordinate to, int number){
        this.piece = piece;
        this.from = from;
        this.to = to;
        this.number = number;
        this.team = piece.getTeam();
    }

    @Override
    public String toString() {
        return "Move{" +
                "piece:" + piece +
                ", from:" + from +
                ", to:" + to +
                ", number:" + number +
                ", team:" + team +
                '}';
    }
}
