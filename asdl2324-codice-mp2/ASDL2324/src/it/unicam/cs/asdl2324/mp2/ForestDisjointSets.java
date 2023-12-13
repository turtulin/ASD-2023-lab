package it.unicam.cs.asdl2324.mp2;

import java.util.*;

/**
 * Implementazione dell'interfaccia <code>DisjointSets<E></code> tramite una
 * foresta di alberi ognuno dei quali rappresenta un insieme disgiunto. Si
 * vedano le istruzioni o il libro di testo Cormen et al. (terza edizione)
 * Capitolo 21 Sezione 3.
 * 
 * @author Luca Tesei (template) **INSERIRE NOME, COGNOME ED EMAIL
 *         xxxx@studenti.unicam.it DELLO STUDENTE** (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi degli insiemi disgiunti
 */
public class ForestDisjointSets<E> implements DisjointSets<E> {

    /*
     * Mappa che associa ad ogni elemento inserito il corrispondente nodo di un
     * albero della foresta. La variabile è protected unicamente per permettere
     * i test JUnit.
     */
    protected Map<E, Node<E>> currentElements;
    
    /*
     * Classe interna statica che rappresenta i nodi degli alberi della foresta.
     * Gli specificatori sono tutti protected unicamente per permettere i test
     * JUnit.
     */
    protected static class Node<E> {
        /*
         * L'elemento associato a questo nodo
         */
        protected E item;

        /*
         * Il parent di questo nodo nell'albero corrispondente. Nel caso in cui
         * il nodo sia la radice allora questo puntatore punta al nodo stesso.
         */
        protected Node<E> parent;

        /*
         * Il rango del nodo definito come limite superiore all'altezza del
         * (sotto)albero di cui questo nodo è radice.
         */
        protected int rank;

        /**
         * Costruisce un nodo radice con parent che punta a se stesso e rango
         * zero.
         * 
         * @param item
         *                 l'elemento conservato in questo nodo
         * 
         */
        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    /**
     * Costruisce una foresta vuota di insiemi disgiunti rappresentati da
     * alberi.
     */
    public ForestDisjointSets() {
        currentElements = new HashMap<>();
    }

    @Override
    public boolean isPresent(E e) {
        // Controlla se la mappa contiene la chiave e
        return currentElements.containsKey(e);
    }

    /*
     * Crea un albero della foresta consistente di un solo nodo di rango zero il
     * cui parent è se stesso.
     */
    @Override
    public void makeSet(E e) {
        if (e == null) throw new NullPointerException("L'elemento è nullo");
        if (isPresent(e)) throw new IllegalArgumentException("Il nodo con questo elemento è già presente");
        // Crea un nuovo nodo con l'elemento e
        Node<E> node = new Node<>(e);
        // Aggiunge il nodo alla mappa con la chiave e
        currentElements.put(e, node);
    }

    /*
     * L'implementazione del find-set deve realizzare l'euristica
     * "compressione del cammino". Si vedano le istruzioni o il libro di testo
     * Cormen et al. (terza edizione) Capitolo 21 Sezione 3.
     */
    @Override
    public E findSet(E e) {
        if (e == null) throw new NullPointerException("L'elemento è null");
        // Ottiene il nodo corrispondente all'elemento e dalla mappa
        Node<E> node = currentElements.get(e);
        // Se il nodo non esiste, lancia un'eccezione
        if (node == null) return null;
        // Se il nodo è la radice, restituisce il suo elemento
        if (node.parent == node) {
            return node.item;
        }
        // Altrimenti, ricorsivamente trova il rappresentante del nodo e aggiorna il suo parent
        node.parent = currentElements.get(findSet(node.parent.item));
        return node.parent.item;
    }

    /*
     * L'implementazione dell'unione deve realizzare l'euristica
     * "unione per rango". Si vedano le istruzioni o il libro di testo Cormen et
     * al. (terza edizione) Capitolo 21 Sezione 3. In particolare, il
     * rappresentante dell'unione dovrà essere il rappresentante dell'insieme il
     * cui corrispondente albero ha radice con rango più alto. Nel caso in cui
     * il rango della radice dell'albero di cui fa parte e1 sia uguale al rango
     * della radice dell'albero di cui fa parte e2 il rappresentante dell'unione
     * sarà il rappresentante dell'insieme di cui fa parte e2.
     */
    @Override
    public void union(E e1, E e2) {
        if (e1 == null || e2 == null) throw new NullPointerException("Elemento nullo nella foresta");
        if (!isPresent(e1) || !isPresent(e2)) throw new IllegalArgumentException("Elemento non presente nella foresta");
        // Trova i rappresentanti degli elementi e1 ed e2
        E x = findSet(e1);
        E y = findSet(e2);
        // Se sono uguali, non fa nulla
        if (x.equals(y)) return;
        // Altrimenti, ottiene i nodi corrispondenti ai rappresentanti
        Node<E> xRoot = currentElements.get(x);
        Node<E> yRoot = currentElements.get(y);
        // Confronta i ranghi dei nodi e li unisce in base all'euristica
        if (xRoot.rank < yRoot.rank) {
            xRoot.parent = yRoot;
        } else if (xRoot.rank > yRoot.rank) {
            yRoot.parent = xRoot;
        } else {
            // In questo caso, il nodo 2 deve rimanere 1
            xRoot.parent = yRoot;
            yRoot.rank++;
        }
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        // Crea un insieme vuoto per contenere i rappresentanti
        Set<E> representatives = new HashSet<>();
        // Itera su tutti gli elementi della mappa
        for (E e : currentElements.keySet()) {
            // Trova il rappresentante dell'elemento e lo aggiunge all'insieme
            E rep = findSet(e);
            representatives.add(rep);
        }
        // Restituisce l'insieme dei rappresentanti
        return representatives;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        if (e == null) throw new NullPointerException("L'elemento è nullo");
        if (!isPresent(e)) throw new IllegalArgumentException("L'elemento non è presente in alcun nodo");
        // Crea un insieme vuoto per contenere gli elementi dell'insieme
        Set<E> elements = new HashSet<>();
        // Trova il rappresentante dell'elemento e
        E rep = findSet(e);
        // Itera su tutti gli elementi della mappa
        for (E x : currentElements.keySet()) {
            // Se l'elemento ha lo stesso rappresentante di e, lo aggiunge all'insieme
            if (findSet(x).equals(rep)) {
                elements.add(x);
            }
        }
        // Restituisce l'insieme degli elementi
        return elements;
    }

    @Override
    public void clear() {
        // Svuota la mappa
        currentElements.clear();
    }

}
