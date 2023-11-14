/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.List;

// TODO completare import

/**
 * Implementazione dell'algoritmo di Merge Sort integrata nel framework di
 * valutazione numerica. Non è richiesta l'implementazione in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class MergeSort<E extends Comparable<E>> implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
        return null;
    }

    private void mergeSort(List<E> l, int left, int right) {
        if(left > right || (left <= 1 && right <= 1)) {
            throw new IllegalArgumentException("La parte sinistra deve essere più piccola di quella a destra");
        }
        int middle = (left + right) / 2;
        mergeSort(l, left, middle);
        mergeSort(l, middle + 1, right);
        merge(l, left, middle, right);
    }

    private void merge(List<E> l, int left, int middle, int right) {
        int m1 = middle - left + 1;
        int m2 = right - middle;
        List<E> B = l.subList(left, middle);
        List<E> C = l.subList(middle + 1, left);

        int i = 1;
        int j = 1;
        int k = left;

        while(i < m1 && j < m2) {
            if(B.get(i).compareTo(C.get(j)) <= 0) {
                l.set();
            }
        }
    }

    public String getName() {
        return "MergeSort";
    }
}
