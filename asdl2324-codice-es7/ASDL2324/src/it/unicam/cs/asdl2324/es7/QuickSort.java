/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.List;

// TODO completare import

/**
 * Implementazione del QuickSort con scelta della posizione del pivot fissa.
 * L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 * @param <E>
 *                il tipo degli elementi della sequenza da ordinare.
 *
 */
public class QuickSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null) {
            throw new NullPointerException("La lista da ordinare non può essere null");
        }
        if(l.isEmpty()) {
            return new SortingAlgorithmResult<E>(l,0);
        }
        int counter = 0;
        counter += quickSort(l, 0, l.size() - 1);
        return new SortingAlgorithmResult<E>(l, counter);
    }

    private int quickSort(List<E> l, int left, int right) {
        if(left < right) {
            int pivot = (left + right) / 2;
            swap(l, pivot, right);
            int partition = partition(l, left, right);
            int counter = quickSort(l, left, partition - 1);
            counter += quickSort(l, partition + 1, right);
            return counter;
        }
        else {
            return 0;
        }
    }

    private int partition(List<E> l, int left, int right) {
        E pivot = l.get(right);
        int i = left;
        int counter = 0;
        for(int j = left; j < right; j++) {
            counter++;
            if(l.get(j).compareTo(pivot) <= 0) {
                swap(l, i, j);
                i++;
            }
        }
        swap(l, i, right);
        return i;
    }

    private void swap(List<E> l, int i, int j) {
        if(i < 0 || i >= l.size() || j < 0 || j >= l.size()) {
            throw new IndexOutOfBoundsException("Gli indici da scambiare non sono validi");
        }
        E temp = l.get(i);
        l.set(i, l.get(j));
        l.set(j, temp);
    }

    @Override
    public String getName() {
        return "QuickSort";
    }

}
