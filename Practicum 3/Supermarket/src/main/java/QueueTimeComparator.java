import java.util.Comparator;

public class QueueTimeComparator implements Comparator<Customer> {
    @Override
    public int compare(Customer o1, Customer o2) {
        return o1.getQueuedAt().compareTo(o2.getQueuedAt());
    }
}
