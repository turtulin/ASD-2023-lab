package it.unicam.cs.asdl2324.mp1;

import java.util.Iterator;
import java.util.Set;

//TODO insireire import della Java SE che si ritengono necessari

/**
 * // TODO spiegare come viene implementato il multiset.
 * 
 * @author Luca Tesei (template) **INSERIRE NOME, COGNOME ED EMAIL
 *         xxxx@studenti.unicam.it DELLO STUDENTE** (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */
public class MyMultiset<E> implements Multiset<E> {

    // TODO inserire le variabili istanza private che si ritengono necessarie

    // TODO inserire le classi interne che si ritengono necessarie

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
        // TODO Implementare
    }

    @Override
    public int size() {
        // TODO Implementare
        return -1;
    }

    @Override
    public int count(Object element) {
        // TODO Implementare
        return -1;
    }

    @Override
    public int add(E element, int occurrences) {
        // TODO Implementare
        return -1;
    }

    @Override
    public void add(E element) {
        // TODO Implementare
    }

    @Override
    public int remove(Object element, int occurrences) {
        // TODO implementare
        return -1;
    }

    @Override
    public boolean remove(Object element) {
        // TODO implementare
        return false;

    }

    @Override
    public int setCount(E element, int count) {
        // TODO implementare
        return -1;
    }

    @Override
    public Set<E> elementSet() {
        // TODO implementare
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        // TODO implementare
        return null;
    }

    @Override
    public boolean contains(Object element) {
        // TODO implementare
        return false;
    }

    @Override
    public void clear() {
        // TODO implementare
    }

    @Override
    public boolean isEmpty() {
        // TODO implementare
        return false;
    }

    /*
     * Due multinsiemi sono uguali se e solo se contengono esattamente gli
     * stessi elementi (utilizzando l'equals della classe E) con le stesse
     * molteplicità.
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        // TODO implementare
        return false;
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        // TODO implementare
        return -1;
    }

}
