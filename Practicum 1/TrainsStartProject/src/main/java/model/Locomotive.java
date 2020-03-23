package model;

/**
 * This represents the locomotive of a train
 * @author Abdul, Ferran
 */
public class Locomotive {
    private int locNumber;
    private int maxWagons;

    public Locomotive(int locNumber, int maxWagons) {
        this.locNumber = locNumber;
        this.maxWagons = maxWagons;
    }

    /**
     * This method returns the maxWagons attribute of a locomotive
     * @return maxWagons attribute
     */
    public int getMaxWagons() {
        return maxWagons;
    }

    @Override
    public String toString() {
        return String.format("{Loc %d}", locNumber);
    }
}
