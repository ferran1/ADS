import java.time.LocalTime;
import java.util.PriorityQueue;

public class PriorityCashier extends FIFOCashier {
    public int maxNumPriorityItems;

    public PriorityCashier(String name, int maxNumPriorityItems) {
        super(name);
        waitingQueue = new PriorityQueue<>(new PriorityComparator());
        this.maxNumPriorityItems = maxNumPriorityItems;
    }

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
            waitingQueue.add(customer);
            int waitingTime = expectedWaitingTime(customer);
            customer.setActualWaitingTime(waitingTime);
            customer.setActualCheckOutTime(expectedCheckOutTime(customer.getNumberOfItems()));
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
    public int expectedWaitingTime(Customer customer) {
        int waitingTime = 0;

        for (Customer c : waitingQueue) {
            if (customer.getNumberOfItems() <= maxNumPriorityItems && currentCustomer == null) {
                waitingTime = 0;
            } else {
                waitingTime += c.getActualCheckOutTime();
            }
        }
        if (currentCustomer != null) {
            waitingTime += currentCustomer.getActualCheckOutTime();
        }
        return waitingTime;
    }

    public int getMaxNumPriorityItems() {
        return maxNumPriorityItems;
    }
}
