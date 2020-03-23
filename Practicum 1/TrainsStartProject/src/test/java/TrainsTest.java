import model.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class TrainsTest {

    private ArrayList<PassengerWagon> pwList;
    private ArrayList<FreightWagon> fwList;
    private Train firstPassengerTrain;
    private Train secondPassengerTrain;
    private Train firstFreightTrain;
    private Train secondFreightTrain;

    @BeforeEach
    private void makeListOfPassengerWagons() {
        pwList = new ArrayList<>();
        pwList.add(new PassengerWagon(3, 100));
        pwList.add(new PassengerWagon(24, 100));
        pwList.add(new PassengerWagon(17, 140));
        pwList.add(new PassengerWagon(32, 150));
        pwList.add(new PassengerWagon(38, 140));
        pwList.add(new PassengerWagon(11, 100));
    }

    @BeforeEach
    private void makeListOfFreightWagons() {
        fwList = new ArrayList<>();
        fwList.add(new FreightWagon(4, 100));
        fwList.add(new FreightWagon(25, 100));
        fwList.add(new FreightWagon(18, 140));
        fwList.add(new FreightWagon(33, 150));
        fwList.add(new FreightWagon(39, 140));
        fwList.add(new FreightWagon(12, 100));
    }

    private void makeTrains() {
        Locomotive thomas = new Locomotive(2453, 7);
        Locomotive gordon = new Locomotive(5277, 8);
        Locomotive emily = new Locomotive(4383, 11);
        Locomotive rebecca = new Locomotive(2275, 4);

        firstPassengerTrain = new Train(thomas, "Amsterdam", "Haarlem");
        firstFreightTrain = new Train(gordon, "Istanbul", "Ankara");


        for (Wagon w : pwList) {
            Shunter.hookWagonOnTrainRear(firstPassengerTrain, w);
        }
        for (Wagon w : fwList) {
            Shunter.hookWagonOnTrainRear(firstFreightTrain, w);
        }
        secondPassengerTrain = new Train(gordon, "Joburg", "Cape Town");

    }

    @Test
    public void checkNumberOfWagonsOnTrain() {
        makeTrains();
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
    }

    @Test
    public void checkNumberOfSeatsOnTrain() {
        makeTrains();
        assertEquals(730, firstPassengerTrain.getNumberOfSeats());
    }

    @Test
    public void checkGetTotalMaxWeight() {
        makeTrains();
        assertEquals(730, firstFreightTrain.getTotalMaxWeight());
    }

    @Test
    public void checkPositionOfWagons() {
        makeTrains();
        int position = 1;
        for (PassengerWagon pw : pwList) {
            assertEquals(position, firstPassengerTrain.getPositionOfWagon(pw.getWagonId()));
            position++;
        }

    }

    @Test
    public void checkHookOneWagonOnTrainFront() {
        makeTrains();
        Shunter.hookWagonOnTrainFront(firstPassengerTrain, new PassengerWagon(21, 140));
        assertEquals(7, firstPassengerTrain.getNumberOfWagons(), "Train should have 7 wagons");
        assertEquals(1, firstPassengerTrain.getPositionOfWagon(21));

    }

    @Test
    public void checkHookRowWagonsOnTrainRearFalse() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(firstPassengerTrain, w1);
        assertEquals(6, firstPassengerTrain.getNumberOfWagons(), "Train should have still have 6 wagons, capacity reached");
        assertEquals(-1, firstPassengerTrain.getPositionOfWagon(43));
    }

    @Test
    public void checkMoveOneWagon() {
        makeTrains();
        Shunter.moveOneWagon(firstPassengerTrain, secondPassengerTrain, pwList.get(3));
        assertEquals(5, firstPassengerTrain.getNumberOfWagons(), "Train should have 5 wagons");
        assertEquals(-1, firstPassengerTrain.getPositionOfWagon(32));
        assertEquals(1, secondPassengerTrain.getNumberOfWagons(), "Train should have 1 wagon");
        assertEquals(1, secondPassengerTrain.getPositionOfWagon(32));

    }

    @Test
    public void checkMoveRowOfWagons() {
        makeTrains();
        Wagon w1 = new PassengerWagon(11, 100);
        Shunter.hookWagonOnWagon(w1, new PassengerWagon(43, 140));
        Shunter.hookWagonOnTrainRear(secondPassengerTrain, w1);
        Shunter.moveAllFromTrain(firstPassengerTrain, secondPassengerTrain, pwList.get(2));
        assertEquals(2, firstPassengerTrain.getNumberOfWagons(), "Train should have 2 wagons");
        assertEquals(2, firstPassengerTrain.getPositionOfWagon(24));
        assertEquals(6, secondPassengerTrain.getNumberOfWagons(), "Train should have 6 wagons");
        assertEquals(4, secondPassengerTrain.getPositionOfWagon(32));
    }

    @Test
    public void checkDetachWagonOneWagon() {
        makeTrains();
        secondFreightTrain = new Train(new Locomotive(11, 12), "Istanbul", "Sivas");
        secondFreightTrain.setFirstWagon(new FreightWagon(10, 100));
        assertEquals(100, secondFreightTrain.getTotalMaxWeight());
    }

    @Test
    public void checkIteratorHasNext() {
        makeTrains();
        firstFreightTrain = new Train(new Locomotive(1234, 4), "Ankara", "Bolu");
        firstFreightTrain.setFirstWagon(fwList.get(0));
        firstFreightTrain.getFirstWagon().setNextWagon(fwList.get(1));
        Iterator it = firstFreightTrain.iterator();

        assertTrue(it.hasNext());
    }

    @Test
    public void checkIteratorNext() {
        makeTrains();
        firstFreightTrain = new Train(new Locomotive(1234, 4), "Ankara", "Bolu");
        firstFreightTrain.setFirstWagon(fwList.get(0));
        firstFreightTrain.getFirstWagon().setNextWagon(fwList.get(1));
        Iterator it = firstFreightTrain.iterator();

        assertEquals(it.next(), fwList.get(0));
    }

    @Test
    public void checkHookWagonOnTrainRearPositive() {
        makeTrains();
        int numberOfWagons = firstFreightTrain.getNumberOfWagons();
        Shunter.hookWagonOnTrainRear(firstFreightTrain, new FreightWagon(66, 300));
        firstFreightTrain.resetNumberOfWagons();
        assertEquals(numberOfWagons + 1, firstFreightTrain.getNumberOfWagons());
    }

    @Test
    public void checkWagonOnPosition() {
        makeTrains();
        Wagon lastWagon = firstPassengerTrain.getFirstWagon().getLastWagonAttached();
        assertEquals(firstPassengerTrain.getWagonOnPosition(6), lastWagon);
    }

    @Test
    public void checkResetNumberOfWagons() {
        makeTrains();
        Shunter.hookWagonOnTrainRear(firstFreightTrain, new FreightWagon(123, 1000));
        Shunter.hookWagonOnTrainRear(firstFreightTrain, new FreightWagon(1234, 1000));
        assertEquals(8, firstFreightTrain.getNumberOfWagons());
    }

    @Test
    public void checkDetachAllFromTrain() {
        makeTrains();
        Shunter.detachAllFromTrain(firstFreightTrain, fwList.get(2));
        assertEquals(fwList.get(1), firstFreightTrain.getFirstWagon().getNextWagon());
        assertNull(firstFreightTrain.getFirstWagon().getNextWagon().getNextWagon());
    }

    @Test
    public void checkDetachOneWagon() {
        makeTrains();
        Shunter.detachOneWagon(firstFreightTrain, fwList.get(0));
        assertEquals(fwList.get(1), firstFreightTrain.getFirstWagon());
    }

    @Test
    public void checkMoveAllFromTrain() {
        makeTrains();
        Train thirdPassengerTrain = new Train(new Locomotive(12, 4), "Utrecht", "London");
        Shunter.moveAllFromTrain(firstPassengerTrain, thirdPassengerTrain, firstPassengerTrain.getFirstWagon());
        assertNull(firstPassengerTrain.getFirstWagon());
        assertEquals(pwList.get(0), thirdPassengerTrain.getFirstWagon());
    }

    @Test
    public void checkMoveAllFromTrain2() {
        makeTrains();
        secondPassengerTrain.setFirstWagon(new PassengerWagon(221, 102));
        secondPassengerTrain.getFirstWagon().setNextWagon(new PassengerWagon(123, 111));
        Shunter.moveAllFromTrain(firstPassengerTrain, secondPassengerTrain, pwList.get(1));
        assertNull(firstPassengerTrain.getFirstWagon().getNextWagon());
        assertEquals(pwList.get(5), secondPassengerTrain.getFirstWagon().getLastWagonAttached());
    }

    @Test
    public void checkGetLastWagonAttached() {
        makeTrains();
        assertEquals(pwList.get(pwList.size() - 1), firstPassengerTrain.getFirstWagon().getLastWagonAttached());
    }

    @Test
    public void checkSetNextWagon(){
        makeTrains();
        PassengerWagon passengerWagon = new PassengerWagon(122, 211);
        pwList.get(pwList.size() - 1).setNextWagon(passengerWagon);
        assertEquals(passengerWagon, pwList.get(pwList.size() - 1).getNextWagon());
        pwList.get(pwList.size() - 1).setNextWagon(null);
        assertNull(pwList.get(pwList.size() - 1).getNextWagon());
    }

    @Test
    public void checkGetNumberOfWagonsAttached() {
        makeTrains();
        assertEquals(5, firstPassengerTrain.getFirstWagon().getNumberOfWagonsAttached());
    }
}
