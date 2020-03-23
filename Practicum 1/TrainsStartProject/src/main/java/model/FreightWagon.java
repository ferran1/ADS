package model;

/**
 * This class is the blueprint of a freight wagon
 * It inherits from its asbtract parent class (Wagon)
 * @author Abdul, Ferran
 */
public class FreightWagon extends Wagon {
    private int maxWeight;

    public FreightWagon(int wagonId, int maxWeight) {
        super(wagonId);
        this.maxWeight = maxWeight;
    }

    /**
     * @return maxWeight attribute
     */
    public int getMaxWeight() {
        return maxWeight;
    }
}
