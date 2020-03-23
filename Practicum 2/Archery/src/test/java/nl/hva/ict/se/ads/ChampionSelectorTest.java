package nl.hva.ict.se.ads;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ChampionSelectorTest {
    protected Comparator<Archer> comparator;

    @BeforeEach
    public void createComparator() {
        // Instantiate your own comparator here...
        // comparator = new .....();
        comparator = (o1, o2) -> {
            if (o1.getTotalScore() > o2.getTotalScore()) {
                return -1;
            } else if (o1.getTotalScore() < o2.getTotalScore()) {
                return 1;
            } else {
                if (o1.getWeightScore() > o2.getWeightScore()) {
                    return -1;
                } else if (o1.getWeightScore() < o2.getWeightScore()) {
                    return 1;
                } else {
                    if (o1.getId() > o2.getId()) {
                        return -1;
                    } else if (o1.getId() < o2.getId()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        };
    }

    @Test
    public void selInsSortAndCollectionSortResultInSameOrder() {
        List<Archer> unsortedArchersForSelIns = Archer.generateArchers(23);
        List<Archer> unsortedArchersForCollection = new ArrayList<>(unsortedArchersForSelIns);

        List<Archer> sortedArchersSelIns = ChampionSelector.selInsSort(unsortedArchersForSelIns, comparator);
        List<Archer> sortedArchersCollection = ChampionSelector.collectionSort(unsortedArchersForCollection, comparator);

        assertEquals(sortedArchersCollection, sortedArchersSelIns);
    }
}
