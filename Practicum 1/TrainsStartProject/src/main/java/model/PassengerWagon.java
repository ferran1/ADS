package model;

/**
 * This class is the blueprint of a passenger wagon
 * It inherits from its asbtract parent class (Wagon)
 * @author Abdul, Ferran
 */
public class PassengerWagon extends Wagon {
    private int numberOfSeats;

    public PassengerWagon(int wagonId, int numberOfSeats) {
        super(wagonId);
        this.numberOfSeats = numberOfSeats;
    }

    /**
     * @return numberOfSeats attribute
     */
    public int getNumberOfSeats() {
        return numberOfSeats;
    }
}
