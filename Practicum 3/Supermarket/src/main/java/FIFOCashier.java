import java.time.LocalTime;
import java.util.*;

public class FIFOCashier extends Cashier {
    public int checkoutTimePerCustomer;
    public int checkoutTimePerItem;
    public Customer currentCustomer;

    public FIFOCashier(String name) {
        super(name);
        waitingQueue = new LinkedList<>();
        this.checkoutTimePerCustomer = 20;
        this.checkoutTimePerItem = 2;
    }

    /**
     * restart the state if simulation of the cashier to initial time
     * with empty queues
     *
     * @param currentTime
     */
    @Override
    public void reStart(LocalTime currentTime) {
        this.waitingQueue.clear();
        this.currentTime = currentTime;
        this.totalIdleTime = 0;
        this.maxQueueLength = 0;
    }

    @Override
    public void add(Customer customer) {
        int checkoutTime = expectedCheckOutTime(customer.getNumberOfItems());
        customer.setActualCheckOutTime(checkoutTime);

        if (checkoutTime != 0) {
            int waitingTime = expectedWaitingTime(customer);
            customer.setActualWaitingTime(waitingTime);
            waitingQueue.add(customer);
        }

        if (waitingQueue.size() >= maxQueueLength) {
            if (currentCustomer == null) {
                maxQueueLength = waitingQueue.size();
            } else {
                maxQueueLength = waitingQueue.size() + 1;
            }
        }
    }

    @Override
    public int expectedCheckOutTime(int numberOfItems) {
        int checkoutTime = checkoutTimePerCustomer + (numberOfItems * checkoutTimePerItem);

        if (numberOfItems == 0) {
            return 0;
        }
        return checkoutTime;
    }

    // Only if multi-cashier simulation is implemented

    /**
     * calculate the currently expected waiting time of a given customer for this cashier.
     * this may depend on:
     * a) the type of cashier,
     * b) the remaining work of the cashier's current customer(s) being served
     * c) the position that the given customer may obtain in the queue
     * d) and the workload of the customers in the waiting queue in front of the given customer
     *
     * @param customer
     * @return
     */
    @Override
    public int expectedWaitingTime(Customer customer) {
        int waitingTime = 0;

        for (Customer c : waitingQueue) {
            waitingTime += c.getActualCheckOutTime();
        }
        if (currentCustomer != null) {
            waitingTime += currentCustomer.getActualCheckOutTime();
        }
        return waitingTime;
    }

    @Override
    public void doTheWorkUntil(LocalTime targetTime) {
        // get the customer from the queue if current customer is null and
        // waitingqueue has customer(s).
        if (currentCustomer == null) {
            currentCustomer = waitingQueue.poll();
        }

        // if cashier takes break and there is no customer in queue
        // update the idle time
        if (waitingQueue.isEmpty() && currentCustomer == null) {
            int differenceInTime = targetTime.toSecondOfDay() - currentTime.toSecondOfDay();
            setCurrentTime(targetTime);
            setTotalIdleTime(getTotalIdleTime() + differenceInTime);
        }

        if (currentCustomer != null) {
            int checkOutTimeCurrent = currentCustomer.getActualCheckOutTime();

            // if customer is finished before the targetTime
            if (currentTime.plusSeconds(checkOutTimeCurrent).toSecondOfDay() <= targetTime.toSecondOfDay()) {
                // set the current time to be the time of completion of the customer checkout process
                // later you can set idle time if no customer is next or help the next customer from
                // the new currentTime
                setCurrentTime(currentTime.plusSeconds(checkOutTimeCurrent));

                // reset the checkOutTime of the customer to the original checkOutTime
                currentCustomer.setActualCheckOutTime(expectedCheckOutTime(currentCustomer.getNumberOfItems()));
                // get the next customer in queue if exists
                if (!waitingQueue.isEmpty()) {
                    currentCustomer = waitingQueue.poll();
                    // do work until targetTime from the new currentTime so that the next customer
                    // is helped right after the prev customer till targetTime
                    doTheWorkUntil(targetTime);
                } else {
                    // if no customer(s) in queue (cashier takes break), update the idle time till targetTime
                    currentCustomer = null;
                    setTotalIdleTime(getTotalIdleTime() + (targetTime.toSecondOfDay() - currentTime.toSecondOfDay()));
                    setCurrentTime(targetTime);
                }
            } else if (currentTime.plusSeconds(checkOutTimeCurrent).toSecondOfDay() > targetTime.toSecondOfDay()) {
                int formulaForNewCheckOut = targetTime.toSecondOfDay() - currentTime.toSecondOfDay();
                int newCustomerCheckoutTime = currentCustomer.getActualCheckOutTime() - formulaForNewCheckOut;
                currentCustomer.setActualCheckOutTime(newCustomerCheckoutTime);
                setCurrentTime(targetTime);
            }
        }
    }

}
