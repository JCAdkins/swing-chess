package engine.pieces;

import engine.ui.Coordinate;
import java.awt.*;

public abstract class Piece implements Movable {
    private final boolean isAI;
    private boolean canMove;
    Coordinate position;
    Coordinate drawPosition;
    private final int team;
    private final Image sprite;

    public Piece(boolean isAI, Coordinate position, Coordinate drawPosition, int team, Image sprite) {
        this.isAI = isAI;
        this.canMove = true;
        this.position = position;
        this.drawPosition = drawPosition;
        this.team = team;
        this.sprite = sprite;
    }

    public boolean isAI() {
        return isAI;
    }

    public boolean isCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public Coordinate getPosition() {
        return position;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
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
}

