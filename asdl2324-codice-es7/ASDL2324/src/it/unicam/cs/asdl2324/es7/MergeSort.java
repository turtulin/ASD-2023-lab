/**
 * 
 */
package it.unicam.cs.asdl2324.es7;

import java.util.ArrayList;
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

    private int counter;
    
    public SortingAlgorithmResult<E> sort(List<E> l) {
        List<E> sortedList = mergeSort(l);
        SortingAlgorithmResult<E> result = new SortingAlgorithmResult<>(sortedList, counter);
        return result;
    }

    private List<E> split(List<E> list) {
        if(list.size() <= 1) {
            return list;
        }
        List<E> left = new ArrayList<>();
        List<E> right = new ArrayList<>();
        for(int i = 0; i < list.size(); i++) {
            if(i % 2 == 0) {
                left.add(list.get(i));
            }
            else {
                right.add(list.get(i));
            }
        }
        List<E> result = new ArrayList<>();
        result.addAll(left);
        result.addAll(right);
        return result;
    }


    private List<E> merge(List<E> left, List<E> right) {
        List<E> result = new ArrayList<>();
        while(!left.isEmpty() && !right.isEmpty()) {
            counter++;
            if(left.get(0).compareTo(right.get(0)) <= 0) {
                result.add(left.remove(0));
            }
            else {
                result.add(right.remove(0));
            }
        }
        result.addAll(left);
        result.addAll(right);
        return result;
    }

    private List<E> mergeSort(List<E> list) {
        if(list.size() <= 1) {
            return list;
        }
        List<E> splitList = split(list);
        int mid = splitList.size() / 2;
        List<E> left = mergeSort(splitList.subList(0, mid));
        List<E> right = mergeSort(splitList.subList(mid, splitList.size()));
        List<E> result = merge(left, right);
        return result;
    }

    public String getName() {
        return "MergeSort";
    }
}
