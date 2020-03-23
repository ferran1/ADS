package model;

public class Location {
    private final static double ESTIMATED_TRAVEL_PER_SQUARE = 1.5;
    private int x;
    private int y;

    public Location(int xCoordinate, int yCoordinate) {
        this.x = xCoordinate;
        this.y = yCoordinate;
    }

    public double travelTime(Location location) {
        int tempX = Math.abs(location.x - this.x);
        int tempY = Math.abs(location.y - this.y);

        return Math.sqrt(Math.pow(tempX, 2) + Math.pow(tempY, 2)) * ESTIMATED_TRAVEL_PER_SQUARE;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "x = " + x + "\ty: " + y;
    }
}
