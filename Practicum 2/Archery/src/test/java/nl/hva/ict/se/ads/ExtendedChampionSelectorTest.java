package nl.hva.ict.se.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Place all your own tests for ChampionSelector in this class. Tests in any other class will be ignored!
 */
public class ExtendedChampionSelectorTest extends ChampionSelectorTest {
    @Test
    public void checkSortComparator() {
        List<Archer> unsortedArchersForSelIns = Archer.generateArchers(10);
        List<Archer> sortedArchersSelIns = ChampionSelector.selInsSort(unsortedArchersForSelIns, comparator);
        List<Archer> sortedArchersQuick = ChampionSelector.quickSort(unsortedArchersForSelIns, comparator);
        List<Archer> sortedArchersCollection = ChampionSelector.collectionSort(unsortedArchersForSelIns, comparator);


        assertEquals(sortedArchersSelIns, sortedArchersQuick);
        assertEquals(sortedArchersCollection, sortedArchersQuick);
    }

    @Test
    public void testEfficiencyAlgorithms() {
        int nrArchers = 100;

        boolean selectionIsDone = false;
        boolean quickIsDone = false;
        boolean collectionIsDone = false;

        while (!selectionIsDone || !quickIsDone || !collectionIsDone) {
            List<Archer> unsortedArchers = Archer.generateArchers(nrArchers);
            List<Archer> unsortedArchersForSel = new ArrayList<>(unsortedArchers);
            List<Archer> unsortedArchersForQuick = new ArrayList<>(unsortedArchers);
            List<Archer> unsortedArchersForCollection = new ArrayList<>(unsortedArchers);

            long endTimeSelection = 0;
            long endTimeQuick = 0;
            long endTimeCollection = 0;

            if (!selectionIsDone) {
                System.out.println("Selection sort: ");
                long startTime = System.currentTimeMillis();
                ChampionSelector.selInsSort(unsortedArchersForSel, comparator);
                long endTime = System.currentTimeMillis();
                endTimeSelection = (endTime - startTime);

                System.out.println("endTime Selection: " + endTimeSelection);
                System.out.println("number of archers Selection: " + nrArchers);

                if (endTimeSelection >= 20e3 || nrArchers >= 5e6) {
                    System.out.print("Selection sort is finished,");
                    System.out.println(" last number of archers: " + nrArchers);

                    selectionIsDone = true;
                }

                System.out.println();
            }

            if (!quickIsDone) {
                System.out.println("Quick sort: ");
                long startTime = System.currentTimeMillis();
                ChampionSelector.quickSort(unsortedArchersForQuick, comparator);
                long endTime = System.currentTimeMillis();
                endTimeQuick = (endTime - startTime);

                System.out.println("endTime Quick: " + endTimeQuick);
                System.out.println("number of archers Quick: " + nrArchers);

                if (endTimeQuick >= 20e3 || nrArchers >= 5e6) {
                    System.out.print("Quick sort is finished,");
                    System.out.println(" last number of archers: " + nrArchers);

                    quickIsDone = true;
                }

                System.out.println();
            }

            if (!collectionIsDone) {
                System.out.println("Collection sort: ");
                long startTime = System.currentTimeMillis();
                ChampionSelector.collectionSort(unsortedArchersForCollection, comparator);
                long endTime = System.currentTimeMillis();
                endTimeCollection = (endTime - startTime);

                System.out.println("endTime Collection: " + endTimeCollection);
                System.out.println("number of archers Collection: " + nrArchers);
                if (endTimeCollection >= 20e3 || nrArchers >= 5e6) {
                    System.out.print("Collection sort is finished,");
                    System.out.println(" last number of archers: " + nrArchers);

                    collectionIsDone = true;
                }

                System.out.println();
            }

            nrArchers *= 2;
        }
    }
}
