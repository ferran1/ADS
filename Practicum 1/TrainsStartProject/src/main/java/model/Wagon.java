package model;

/**
 * This abstract class is the blueprint of a wagon from a train with
 * attributes identical for each wagon.
 *
 * @author Abdul, Ferran
 */

public abstract class Wagon {
    private int wagonId;
    private Wagon previousWagon;
    private Wagon nextWagon;

    public Wagon(int wagonId) {
        this.wagonId = wagonId;
    }

    /**
     * This method calls
     * itself repeatedly until the number of wagons attached
     * is equal to 0. Then the method returns the last wagon
     * it is called with.
     * @return This method returns a Wagon object.
     *         The last wagon which is attached to the instatiated
     *         wagon object is returned
     */
    public Wagon getLastWagonAttached() {
        // find the last wagon of the row of wagons attached to this wagon
        // if no wagons are attached return this wagon
        if (getNumberOfWagonsAttached() != 0) {
            return nextWagon.getLastWagonAttached();
        } else {
            return this;
        }
    }

    /**
     * Sets the nextWagon attribute value of this wagon.
     * @param nextWagon the new next Wagon object
     */
    public void setNextWagon(Wagon nextWagon) {
        // when setting the next wagon, set this wagon to be previous wagon of next wagon
        if (nextWagon != null) {
            this.nextWagon = nextWagon;
            nextWagon.setPreviousWagon(this);
        }else {
            this.nextWagon = null;
        }
    }

    /**
     * Gets the previousWagon attribute value of this wagon.
     * @return the previousWagon object
     */
    public Wagon getPreviousWagon() {
        return previousWagon;
    }

    /**
     * Sets the previousWagon attribute value of this wagon.
     * @param previousWagon the new previous Wagon object
     */
    public void setPreviousWagon(Wagon previousWagon) {
        this.previousWagon = previousWagon;
    }

    /**
     * Gets the nextWagon object of this wagon.
     * @return the nextWagon object
     */
    public Wagon getNextWagon() {
        return nextWagon;
    }

    /**
     * Gets the wagonId of this wagon.
     * @return the wagonId attribute value of this wagon
     */
    public int getWagonId() {
        return wagonId;
    }

    /**
     * Gets the number of wagons that is attached
     * to this wagon. It uses recursion to get
     * the number of attached wagons. The attached
     * wagons are the nextWagons from this wagon.
     * @return the number of wagons that are attached to this wagon
     */
    public int getNumberOfWagonsAttached() {
        Wagon wagonIndex = this;

        if (hasNextWagon()) {
            wagonIndex = wagonIndex.getNextWagon();
            return 1 + wagonIndex.getNumberOfWagonsAttached();
        } else
            return 0;
    }

    /**
     * Check if this wagon has nextWagon
     * @return a boolean if this wagon has a next wagon
     */
    public boolean hasNextWagon() {
        return !(nextWagon == null);
    }

    /**
     * Check if this wagon has previousWagon
     * @return a boolean if this wagon has a previous wagon
     */
    public boolean hasPreviousWagon() {
        return !(previousWagon == null);
    }

    /**
     * @return a String which prints this wagons wagonId
     */
    @Override
    public String toString() {
        return String.format("[Wagon %d]", wagonId);
    }
}
