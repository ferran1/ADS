package nl.hva.ict.se.ads;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Given a list of Archer's this class can be used to sort the list using one of three sorting algorithms.
 */
public class ChampionSelector {
    /**
     * This method uses either selection sort or insertion sort for sorting the archers.
     */
    public static List<Archer> selInsSort(List<Archer> archers, Comparator<Archer> scoringScheme) {
        //Selection sort
        for (int i = 0; i < archers.size(); i++) {
            int index = i;
            for (int j = i + 1; j < archers.size(); j++) {
                if (scoringScheme.compare(archers.get(j), archers.get(index)) < 0) {
                    index = j;
                }
            }
            Archer smallerArcher = archers.get(index);
            archers.set(index, archers.get(i));
            archers.set(i, smallerArcher);
        }

        return archers;
    }

    /**
     * This method uses quick sort for sorting the archers.
     */
    public static List<Archer> quickSort(List<Archer> archers, Comparator<Archer> scoringScheme) {
        quickSortAlgorithm(archers, 0, archers.size() - 1, scoringScheme);
        return archers;
    }

    private static void quickSortAlgorithm(List<Archer> list, int first, int last, Comparator<Archer> scoringScheme) {
        // check if first can be divided into 2
        if (first < last) {
            int pivotIndex = partition(list, first, last, scoringScheme);
            quickSortAlgorithm(list, first, pivotIndex - 1, scoringScheme);
            quickSortAlgorithm(list, pivotIndex + 1, last, scoringScheme);

        }
    }

    private static int partition(List<Archer> archers, int first, int last, Comparator<Archer> scoringScheme) {
        Archer pivot = archers.get(first);
        int low = first + 1; // Index for forward search, we do +1 because we do not compare pivot with pivot
        int high = last; // Index for backward search, so end of list
        while (low < high) {
            // Search forward from left until low is greater than pivot
            // because you want all greater elements on the right
            while (low <= high && scoringScheme.compare(archers.get(low), pivot) <= 0) {
                low++;
            }
            // Search backward from right until high is smaller than pivot
            // because you want all smaller elements on the left
            while (low <= high && scoringScheme.compare(archers.get(high), pivot) > 0) {
                high--;
            }
            // if both while loop conditions return false we check if low pos is still
            // lower than high pos
            if (low < high) {
                exchange(archers, high, low);
            }
        }
        // if low position has exceeded high position, swap the pivot with the high position
        exchange(archers, archers.indexOf(pivot), high);
        return high;
    }


    /**
     * This method uses the Java collections sort algorithm for sorting the archers.
     */
    public static List<Archer> collectionSort(List<Archer> archers, Comparator<Archer> scoringScheme) {
        Collections.sort(archers, scoringScheme);
        return archers;
    }

    /**
     * This method uses quick sort for sorting the archers in such a way that it is able to cope with an Iterator.
     *
     * <b>THIS METHOD IS OPTIONAL</b>
     */
    public static Iterator<Archer> quickSort(Iterator<Archer> archers, Comparator<Archer> scoringScheme) {
        return null;
    }

    private static void exchange(List<Archer> archers, int i, int j) {
        Archer archer = archers.get(i);
        archers.set(i, archers.get(j));
        archers.set(j, archer);
    }

}
