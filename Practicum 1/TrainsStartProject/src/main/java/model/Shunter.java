package model;

/**
 * This class is the blueprint of shunter
 * A shunter is used to move wagons from one train to the other,
 * it can also be used to hook wagon(s) to a train or detach wagon(s) from a train.
 * @author Abdul, Ferran
 */
public class Shunter {


    /* four helper methods than are used in other methods in this class to do checks */
    /**
     * Because a train can only exist of passenger or freight wagons, this method checks is a wagon is suitable
     * @param train is the passed in train instance
     * @param wagon is the wagon to check if its suitable with the passed in train instance
     * @return true if wagon is suitable, false if wagon isn't suitable
     */
    private static boolean isSuitableWagon(Train train, Wagon wagon) {
        // trains can only exist of passenger wagons or of freight wagons
        if (train.hasNoWagons()) {
            return true;
        } else if (train.getFirstWagon() instanceof PassengerWagon && wagon instanceof PassengerWagon) {
            return true;
        } else if (train.getFirstWagon() instanceof FreightWagon && wagon instanceof FreightWagon) {
            return true;
        }
        return false;
    }

    /**
     * This method checks is 2 wagons are suitable
     * @param one is the first passed in wagon
     * @param two is the second passed in wagon
     * @return true if the wagons are suitable, false if the wagons aren't suitable
     */
    private static boolean isSuitableWagon(Wagon one, Wagon two) {
        // passenger wagons can only be hooked onto passenger wagons
        if (one instanceof PassengerWagon && two instanceof PassengerWagon) {
            return true;
        } else if (one instanceof FreightWagon && two instanceof FreightWagon) {
            return true;
        }
        return false;
    }

    /**
     * This method checks if a train as place for a row of wagons
     * @param train is the passed in train instance
     * @param wagon is the passed in wagon instance with the row of wagons attached
     * @return true if train has place for the row of wagons, false if it doesn't
     */
    private static boolean hasPlaceForWagons(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for a row of wagons
        int maxCapacity = train.getEngine().getMaxWagons();
        int wagons = wagon.getNumberOfWagonsAttached();
        int numberOfWagons = train.getNumberOfWagons() + wagons;
        return maxCapacity != numberOfWagons;
    }

    /**
     * This method checks if a train has place for exactly one wagon
     * @param train is the passed in train instance
     * @param wagon is the passed in wagon instance
     * @return true of the train has place for the wagon, false if it doesn't
     */
    private static boolean hasPlaceForOneWagon(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for one wagon

        int maxCapacity = train.getEngine().getMaxWagons();
        return train.getNumberOfWagons() + 1 <= maxCapacity;
    }

    /**
     * This method hooks a wagon on the train rear
     * @param train is the passed in train instance
     * @param wagon is the passed in wagon instance, hook this wagon on the rear of train instance
     * @return true if the wagon is hooked on the rear of the train
     */
    public static boolean hookWagonOnTrainRear(Train train, Wagon wagon) {
         /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         find the last wagon of the train
         hook the wagon on the last wagon (see Wagon class)
         adjust number of Wagons of Train */

        if (train.hasNoWagons()) {
            train.setFirstWagon(wagon);
            train.resetNumberOfWagons();
            return true;
        } else if (hasPlaceForWagons(train, wagon) && isSuitableWagon(train, wagon)) {
            Wagon firstWagon = train.getFirstWagon();
            Wagon lastWagon = firstWagon.getLastWagonAttached();
            lastWagon.setNextWagon(wagon);
            wagon.setPreviousWagon(lastWagon);
            train.resetNumberOfWagons();
            return true;
        }
        return false;
    }

    /**
     * This method hooks a wagon on the train front
     * @param train is the passed in train instance
     * @param wagon is the passed in wagon instance, hook this wagon on the front of train instance
     * @return true if the wagon is hooked on the front of the train
     */
    public static boolean hookWagonOnTrainFront(Train train, Wagon wagon) {
        /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         if Train has no wagons hookOn to Locomotive
         if Train has wagons hookOn to Locomotive and hook firstWagon of Train to lastWagon attached to the wagon
         adjust number of Wagons of Train */


        if (hasPlaceForOneWagon(train, wagon) && isSuitableWagon(train, wagon)) {
            if (train.getFirstWagon() == null) {
                train.setFirstWagon(wagon);
                return true;
            } else {
                Wagon firstWagon = train.getFirstWagon();
                Wagon lastWagon = train.getFirstWagon().getLastWagonAttached();

                wagon.setNextWagon(firstWagon.getNextWagon());
                firstWagon.setNextWagon(null);
                lastWagon.setNextWagon(firstWagon);
                train.setFirstWagon(wagon);
                train.resetNumberOfWagons();
                return true;
            }
        }
        return false;
    }

    /**
     * This method hooks a wagon on a wagon
     * @param first is the first passed in wagon
     * @param second is the second passed in wagon
     * @return true if the second wagon is hooked on the first wagon
     */
    public static boolean hookWagonOnWagon(Wagon first, Wagon second) {
        /* check if wagons are of the same kind (suitable)
         * if so make second wagon next wagon of first */

        if (isSuitableWagon(first, second)) {
            first.setNextWagon(second);
            return true;
        }
        return false;
    }

    /**
     * This method detaches all wagons from a train
     * @param train is the passed in train instance
     * @param wagon is the passed in wagon instance
     * @return true if the wagons are detached from the train
     */
    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */

        if (train.getPositionOfWagon(wagon.getWagonId()) != -1) {
            if (!wagon.hasPreviousWagon()){
                train.setFirstWagon(null);
                train.resetNumberOfWagons();
            }else {
                wagon.getPreviousWagon().setNextWagon(null);
                wagon.setPreviousWagon(null);
                train.resetNumberOfWagons();
            }
            return true;
        }
        return false;
    }

    /**
     * This method detaches one wagon from a train
     * @param train is the passed in train instance
     * @param wagon is the passed in wagon instance
     * @return true if the wagon is detached from the train
     */
    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */

        if (train.getPositionOfWagon(wagon.getWagonId()) != -1) {
            if (!wagon.hasPreviousWagon()) {
                Wagon nextWagon = wagon.getNextWagon();
                nextWagon.setPreviousWagon(null);
                train.setFirstWagon(nextWagon);
            }else {
                Wagon prevWagon = wagon.getPreviousWagon();
                Wagon nextWagon = wagon.getNextWagon();
                prevWagon.setNextWagon(nextWagon);
                wagon.setPreviousWagon(null);
                wagon.setNextWagon(null);
            }
            return true;
        }
        return false;
    }

    /**
     * This method moves a wagon with all successors from one train to the other
     * @param from is the train instance where the row of wagons should be detached from
     * @param to is the train instance where the row of wagons should be attached to
     * @param wagon is the wagon with all successors
     * @return true if the wagon with all successors has succesfully been detached from train from and attached to train to
     */
    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from
         check if wagon is correct for train and if engine can handle new wagons
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */


        if (from.getPositionOfWagon(wagon.getWagonId()) != -1 && isSuitableWagon(to, wagon) && hasPlaceForWagons(to, wagon)) {

            if (to.hasNoWagons()){
                detachAllFromTrain(from, wagon);
                to.setFirstWagon(wagon);
            }else {
                detachAllFromTrain(from, wagon);
                hookWagonOnTrainRear(to, wagon);
            }
            from.resetNumberOfWagons();
            to.resetNumberOfWagons();
            return true;
        }
        return false;

    }

    /**
     * This method moves one wagon from one train to the other
     * @param from is the train instance where the wagon should be detached from
     * @param to is the train instance where wagon should be attached to
     * @param wagon is the passed in wagon instance
     * @return true if the wagon has succesfully been detached from train from and attached to train to
     */
    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon

        if (from.getPositionOfWagon(wagon.getWagonId()) != -1 && isSuitableWagon(to, wagon) && hasPlaceForOneWagon(to, wagon)) {
            Wagon prevWagon = wagon.getPreviousWagon();
            Wagon nextWagon = wagon.getNextWagon();
            prevWagon.setNextWagon(nextWagon);
            wagon.setPreviousWagon(null);
            wagon.setNextWagon(null);
            from.resetNumberOfWagons();

            if (to.hasNoWagons()) {
                detachOneWagon(from, wagon);
                to.setFirstWagon(wagon);
                to.resetNumberOfWagons();
            } else {
                detachOneWagon(from, wagon);
                to.getFirstWagon().getLastWagonAttached().setNextWagon(wagon);
                to.resetNumberOfWagons();
            }
            return true;
        }
        return false;

    }
}
