package model;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class is the blueprint of a train.
 * This class uses the iterable interface so that there can be
 * iterated through the wagons of this train.
 *
 * @author Abdul, Ferran
 */

public class Train implements Iterable<Wagon> {
    private Locomotive engine;
    private Wagon firstWagon;
    private String destination;
    private String origin;
    private int numberOfWagons;

    public Train(Locomotive engine, String origin, String destination) {
        this.engine = engine;
        this.destination = destination;
        this.origin = origin;
    }

    /**
     * This class implements the Iterator interface. This class is used
     * to be able to iterate through wagons of a Train object.
     */
    private class TrainItertor implements Iterator<Wagon> {
        Wagon currentWagon = null;

        @Override
        public boolean hasNext() {
            if (currentWagon == null && firstWagon != null) {
                return true;
            } else if (currentWagon != null) {
                return currentWagon.hasNextWagon();
            }
            return false;
        }

        @Override
        public Wagon next() {
            if (currentWagon == null && firstWagon != null) {
                currentWagon = firstWagon;
                return currentWagon;
            } else if (currentWagon != null) {
                currentWagon = currentWagon.getNextWagon();
                return currentWagon;
            }
            throw new NoSuchElementException();
        }
    }

    /**
     * Gets the firstWagon attribute value of this train.
     *
     * @return the firstWagon object
     */
    public Wagon getFirstWagon() {
        return firstWagon;
    }

    /**
     * Sets the firstWagon attribute value of this train.
     *
     * @param firstWagon the new Wagon object
     */
    public void setFirstWagon(Wagon firstWagon) {
        this.firstWagon = firstWagon;
    }

    /**
     * Reset the number of wagons by setting the number of wagons again.
     */
    public void resetNumberOfWagons() {
       /*  when wagons are hooked to or detached from a train,
         the number of wagons of the train should be reset
         this method does the calculation */
        numberOfWagons = 0;
        if (!hasNoWagons()) {
            numberOfWagons = firstWagon.getNumberOfWagonsAttached() + 1;
        }
    }

    /**
     * @return the number of wagons this train has
     */
    public int getNumberOfWagons() {
        return numberOfWagons;
    }

    /**
     * @return the Locomotive object
     */
    public Locomotive getEngine() {
        return engine;
    }


    /* three helper methods that are usefull in other methods */

    /**
     * @return if train has wagons
     */
    public boolean hasNoWagons() {
        return (firstWagon == null);
    }

    /**
     * @return if train is PassengerWagon
     */
    public boolean isPassengerTrain() {
        return firstWagon instanceof PassengerWagon;
    }

    /**
     * @return if train is FreightWagon
     */
    public boolean isFreightTrain() {
        return firstWagon instanceof FreightWagon;
    }

    /**
     * While there are wagons in this train look for every
     * wagon if this wagon has the same position as given
     * in the parameter
     *
     * @param wagonId the wagonId of the Wagon object
     * @return the position of the wagon with the same wagonId
     */
    public int getPositionOfWagon(int wagonId) {
        // find a wagon on a train by id, return the position (first wagon had position 1)
        // if not found, than return -1

        if (!hasNoWagons()) {
            int position = 1;
            Wagon wagon = firstWagon;
            if (getFirstWagon().getWagonId() == wagonId) {
                return position;
            } else {
                while (wagon.hasNextWagon()) {
                    if (wagon.getWagonId() != wagonId) {
                        position++;

                    } else {
                        return position;
                    }
                    wagon = wagon.getNextWagon();
                }
                if (wagon.getWagonId() == wagonId) {
                    return position;
                }
                return -1;
            }
        }
        return -1;
    }

    /**
     * Look for each position in the set of wagons of this train
     * and check if the position equals the given parameter.
     *
     * @param position the position as an int of the wagon object
     * @return the wagon on the given position
     * @throws IndexOutOfBoundsException if the position is not in the set of wagons
     */
    public Wagon getWagonOnPosition(int position) throws IndexOutOfBoundsException {
        /* find the wagon on a given position on the train
         position of wagons start at 1 (firstWagon of train)
         use exceptions to handle a position that does not exist */

        int pos = 0;
        if (position == 1) {
            return getFirstWagon();
        } else {
            if (firstWagon.hasNextWagon()) {
                Wagon wagon = firstWagon.getNextWagon();
                pos++;
                if (position != pos && !wagon.hasNextWagon()) {
                    throw new IndexOutOfBoundsException();
                }
                while (position != pos && wagon.hasNextWagon()) {
                    pos++;
                    wagon = wagon.getNextWagon();
                }
                return wagon;
            }
            return getFirstWagon();
        }
    }

    /**
     * Loop through each wagon of this train and sum up the number of seats
     *
     * @return the number of seats of the whole train
     */
    public int getNumberOfSeats() {
        /* give the total number of seats on a passenger train
         for freight trains the result should be 0 */

        if (isPassengerTrain()) {
            int totalSeats = 0;
            for (Wagon wagon : this) {
                totalSeats += ((PassengerWagon) wagon).getNumberOfSeats();
            }
            return totalSeats;
        } else return 0;
    }

    /**
     * Loop through each wagon of this train and sum up the weight of each wagon
     *
     * @return the total weight of the whole set of wagons
     */
    public int getTotalMaxWeight() {
        /* give the total maximum weight of a freight train
         for passenger trains the result should be 0 */
        if (isFreightTrain()) {
            int totalMaxWeight = 0;
            for (Wagon wagon : this) {
                totalMaxWeight += ((FreightWagon) wagon).getMaxWeight();
            }
            return totalMaxWeight;
        }else return 0;

    }

    /**
     * @return an Iterator object which iterates through wagons of this train
     */
    @Override
    public Iterator<Wagon> iterator() {
        return new TrainItertor();
    }

    /**
     * @return a String which prints this trains attributes
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(engine.toString());
        Wagon next = this.getFirstWagon();
        while (next != null) {
            result.append(next.toString());
            next = next.getNextWagon();
        }
        result.append(String.format(" with %d wagons and %d seats from %s to %s", numberOfWagons, getNumberOfSeats(), origin, destination));
        return result.toString();
    }
}
