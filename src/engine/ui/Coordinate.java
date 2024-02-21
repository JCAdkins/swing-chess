package engine.ui;

public class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        // Check if the object being compared is the same instance as this object
        if (this == o) {
            return true;
        }

        // Check if the object being compared is null or not an instance of Coordinate
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // Cast the object to a Coordinate
        Coordinate otherCoordinate = (Coordinate) o;

        // Check if the x and y coordinates are equal
        return x == otherCoordinate.x && y == otherCoordinate.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

}
