package it.unicam.cs.asdl2324.es7;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

class SortingAlgorithmTest {

    @SuppressWarnings("unchecked")
    @Test
    final void testSort() {
        List<Integer> empty = new ArrayList<Integer>();
        List<Integer> oneElement = new ArrayList<Integer>();
        oneElement.add(1);
        ArrayList<Integer> l1 = new ArrayList<Integer>();
        l1.add(54);
        l1.add(2);
        ArrayList<Integer> l2 = new ArrayList<Integer>();
        l2.add(54);
        l2.add(2);
        l2.add(12);
        ArrayList<Integer> l3 = new ArrayList<Integer>();
        l3.add(54);
        l3.add(2);
        l3.add(12);
        l3.add(1);
        ArrayList<Integer> l4 = new ArrayList<Integer>();
        l4.add(54);
        l4.add(2);
        l4.add(12);
        l4.add(2);
        l4.add(1);
        SortingAlgorithm<Integer> insertionSort = new InsertionSort<Integer>();
        SortingAlgorithm<Integer> mergeSort = new MergeSort<Integer>();
        SortingAlgorithm<Integer> quickSort = new QuickSort<Integer>();
        SortingAlgorithm<Integer> quickSortRandom = new QuickSortRandom<Integer>();
        assertThrows(NullPointerException.class, () -> insertionSort.sort(null));
        assertThrows(NullPointerException.class, () -> mergeSort.sort(null));
        assertThrows(NullPointerException.class, () -> quickSort.sort(null));
        assertThrows(NullPointerException.class, () -> quickSortRandom.sort(null));
        SortingAlgorithmResult<Integer> rEmpty = insertionSort.sort(empty);
        assertTrue(rEmpty.getL().size() == 0);
        assertTrue(rEmpty.getCountCompare() == 0);
        rEmpty = mergeSort.sort(empty);
        assertTrue(rEmpty.getL().size() == 0);
        assertTrue(rEmpty.getCountCompare() == 0);
        rEmpty = quickSort.sort(empty);
        assertTrue(rEmpty.getL().size() == 0);
        assertTrue(rEmpty.getCountCompare() == 0);
        rEmpty = quickSortRandom.sort(empty);
        assertTrue(rEmpty.getL().size() == 0);
        assertTrue(rEmpty.getCountCompare() == 0);
        SortingAlgorithmResult<Integer> rOne = insertionSort.sort(oneElement);
        assertTrue(rOne.getL().size() == 1);
        assertTrue(rOne.getCountCompare() == 0);
        rOne = mergeSort.sort(oneElement);
        assertTrue(rOne.getL().size() == 1);
        assertTrue(rOne.getCountCompare() == 0);
        rOne = quickSort.sort(oneElement);
        assertTrue(rOne.getL().size() == 1);
        assertTrue(rOne.getCountCompare() == 0);
        rOne = quickSortRandom.sort(oneElement);
        assertTrue(rOne.getL().size() == 1);
        assertTrue(rOne.getCountCompare() == 0);
        SortingAlgorithmResult<Integer> r = insertionSort.sort((ArrayList<Integer>) l1.clone());
        assertTrue(r.getL().get(0).equals(2));
        assertTrue(r.getL().get(1).equals(54));
        r = mergeSort.sort((ArrayList<Integer>) l1.clone());
        assertTrue(r.getL().get(0).equals(2));
        assertTrue(r.getL().get(1).equals(54));
        r = quickSort.sort((ArrayList<Integer>) l1.clone());
        assertTrue(r.getL().get(0).equals(2));
        assertTrue(r.getL().get(1).equals(54));
        r = quickSortRandom.sort((ArrayList<Integer>) l1.clone());
        assertTrue(r.getL().get(0).equals(2));
        assertTrue(r.getL().get(1).equals(54));
        r = insertionSort.sort((ArrayList<Integer>) l2.clone());
        assertTrue(r.getL().get(0).equals(2));
        assertTrue(r.getL().get(1).equals(12));
        assertTrue(r.getL().get(2).equals(54));
        r = mergeSort.sort((ArrayList<Integer>) l2.clone());
        assertTrue(r.getL().get(0).equals(2));
        assertTrue(r.getL().get(1).equals(12));
        assertTrue(r.getL().get(2).equals(54));
        r = quickSort.sort((ArrayList<Integer>) l2.clone());
        assertTrue(r.getL().get(0).equals(2));
        assertTrue(r.getL().get(1).equals(12));
        assertTrue(r.getL().get(2).equals(54));
        r = quickSortRandom.sort((ArrayList<Integer>) l2.clone());
        assertTrue(r.getL().get(0).equals(2));
        assertTrue(r.getL().get(1).equals(12));
        assertTrue(r.getL().get(2).equals(54));
        r = insertionSort.sort((ArrayList<Integer>) l3.clone());
        assertTrue(r.getL().get(0).equals(1));
        assertTrue(r.getL().get(1).equals(2));
        assertTrue(r.getL().get(2).equals(12));
        assertTrue(r.getL().get(3).equals(54));
        r = mergeSort.sort((ArrayList<Integer>) l3.clone());
        assertTrue(r.getL().get(0).equals(1));
        assertTrue(r.getL().get(1).equals(2));
        assertTrue(r.getL().get(2).equals(12));
        assertTrue(r.getL().get(3).equals(54));
        r = quickSort.sort((ArrayList<Integer>) l3.clone());
        assertTrue(r.getL().get(0).equals(1));
        assertTrue(r.getL().get(1).equals(2));
        assertTrue(r.getL().get(2).equals(12));
        assertTrue(r.getL().get(3).equals(54));
        r = quickSortRandom.sort((ArrayList<Integer>) l3.clone());
        assertTrue(r.getL().get(0).equals(1));
        assertTrue(r.getL().get(1).equals(2));
        assertTrue(r.getL().get(2).equals(12));
        assertTrue(r.getL().get(3).equals(54));
        r = insertionSort.sort((ArrayList<Integer>) l4.clone());
        assertTrue(r.getL().get(0).equals(1));
        assertTrue(r.getL().get(1).equals(2));
        assertTrue(r.getL().get(2).equals(2));
        assertTrue(r.getL().get(3).equals(12));
        assertTrue(r.getL().get(4).equals(54));
        r = mergeSort.sort((ArrayList<Integer>) l4.clone());
        assertTrue(r.getL().get(0).equals(1));
        assertTrue(r.getL().get(1).equals(2));
        assertTrue(r.getL().get(2).equals(2));
        assertTrue(r.getL().get(3).equals(12));
        assertTrue(r.getL().get(4).equals(54));
        r = quickSort.sort((ArrayList<Integer>) l4.clone());
        assertTrue(r.getL().get(0).equals(1));
        assertTrue(r.getL().get(1).equals(2));
        assertTrue(r.getL().get(2).equals(2));
        assertTrue(r.getL().get(3).equals(12));
        assertTrue(r.getL().get(4).equals(54));
        r = quickSortRandom.sort((ArrayList<Integer>) l4.clone());
        assertTrue(r.getL().get(0).equals(1));
        assertTrue(r.getL().get(1).equals(2));
        assertTrue(r.getL().get(2).equals(2));
        assertTrue(r.getL().get(3).equals(12));
        assertTrue(r.getL().get(4).equals(54));
        Random randomGenerator = new Random();
        for (int length = 6; length <= 20; length++) {
            ArrayList<Integer> l = new ArrayList<Integer>();
            for (int j = 0; j < length; j++) {
                Integer x = new Integer(randomGenerator.nextInt(50));
                l.add(x);
            }
            r = insertionSort.sort((ArrayList<Integer>) l.clone());
            assertTrue(r.checkOrder());
            r = mergeSort.sort((ArrayList<Integer>) l.clone());
            assertTrue(r.checkOrder());
            r = quickSort.sort((ArrayList<Integer>) l.clone());
            assertTrue(r.checkOrder());
            r = quickSortRandom.sort((ArrayList<Integer>) l.clone());
            assertTrue(r.checkOrder());
        }
    }
}
