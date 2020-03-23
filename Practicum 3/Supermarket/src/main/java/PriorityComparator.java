import java.util.Comparator;

public class PriorityComparator implements Comparator<Customer> {

    @Override
    public int compare(Customer o1, Customer o2) {

        int itemThreshold = ((PriorityCashier) o1.getCheckOutCashier()).getMaxNumPriorityItems();

        if (o1.getNumberOfItems() <= itemThreshold && o2.getNumberOfItems() > itemThreshold) {
            return -1;
        } else if (o1.getNumberOfItems() > itemThreshold) {
            return 1;
        } else { //equal
            return 0;
        }
    }
}
