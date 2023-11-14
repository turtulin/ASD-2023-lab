package it.unicam.cs.asdl2324.es7;

import java.util.List;

// TODO completare import

/**
 * Implementazione dell'algoritmo di Insertion Sort integrata nel framework di
 * valutazione numerica. L'implementazione è in loco.
 * 
 * @author Template: Luca Tesei, Implementazione: Collettiva
 *
 * @param <E>
 *                Una classe su cui sia definito un ordinamento naturale.
 */
public class InsertionSort<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    public SortingAlgorithmResult<E> sort(List<E> l) {
        if(l == null) throw new NullPointerException("Tentativo di ordinare una lista null");
        if(l.size() <= 1) return new SortingAlgorithmResult<>(l, 0);

        int count = 0;

        for(int j = 1; j < l.size(); j++) {
            E temp = l.get(j);
            int i = j - 1;
            while (i >= 0 && l.get(i).compareTo(temp) > 0) {
                l.set(i + 1, l.get(i));
                i--;
            }
            l.set(i + 1, temp);
            count += j - i;
        }
        return null;
    }


    public String getName() {
        return "InsertionSort";
    }
}
