package it.unicam.cs.asdl2324.mp1;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * La classe che implementa l'interfaccia {@code DisjointSets} usando le liste concatenate
 * per rappresentare gli insiemi disgiunti.
 * Per memorizzare e manipolare gli insiemi disgiunti, usa una mappa che associa ad ogni
 * elemento il suo rappresentante e un set che contiene tutti i rappresentanti
 * degli insiemi disgiunti attualmente presenti.
 *
 * @author  Luca Tesei (template)
 *          Marta Musso marta.musso@studenti.unicam.it (implementazione)
 *
 */


public class LinkedListDisjointSets implements DisjointSets {
    private HashMap<DisjointSetElement, DisjointSetElement> map;

    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
        this.map = new LinkedHashMap<>();
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
        if (e == null) throw new NullPointerException("Tentativo di verificare la presenza di un elemento null");
        return e.getRef1() != null;
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
        if (e == null) throw new NullPointerException
                ("Tentativo di creare un insieme disgiunto con un elemento null");
        if (this.isPresent(e)) throw new IllegalArgumentException
                ("Tentativo di creare un insieme disgiunto con un elemento già presente");
        e.setRef1(e);
        e.setRef2(null);
        e.setNumber(1);
        this.map.put(e, e);
    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
        if ( e == null) throw new NullPointerException
                ("Tentativo di trovare il rappresentante di un elemento null");
        if (!this.isPresent(e)) throw new IllegalArgumentException
                ("Tentativo di trovare il rappresentante di un elemento non presente");
        return e.getRef1();
    }

    /*
     * Dopo l'unione di due insiemi effettivamente disgiunti il rappresentante
     * dell'insieme unito è il rappresentate dell'insieme che aveva il numero
     * maggiore di elementi tra l'insieme di cui faceva parte {@code e1} e
     * l'insieme di cui faceva parte {@code e2}. Nel caso in cui entrambi gli
     * insiemi avevano lo stesso numero di elementi il rappresentante
     * dell'insieme unito è il rappresentante del vecchio insieme di cui faceva
     * parte {@code e1}.
     *
     * Questo comportamento è la risultante naturale di una strategia che
     * minimizza il numero di operazioni da fare per realizzare l'unione nel
     * caso di rappresentazione con liste concatenate.
     *
     */
    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {
        if ( e1 == null || e2 == null) throw new NullPointerException
                ("Tentativo di unire due insiemi disgiunti con almeno un elemento null");
        if (!this.isPresent(e1) || !this.isPresent(e2)) throw new IllegalArgumentException
                ("Tentativo di unire due insiemi disgiunti con almeno un elemento non presente");
        DisjointSetElement r1 = e1.getRef1();
        DisjointSetElement r2 = e2.getRef1();
        if (r1 == r2) return;
        int c1 = r1.getNumber();
        int c2 = r2.getNumber();
        if (c1 >= c2) {
            merge(r1, r2);
            r1.setNumber(c1 + c2);
        }
        else {
            merge(r2, r1);
            r2.setNumber(c1 + c2);
        }
    }

    /**
     * @param r1
     * @param r2
     */
    private void merge (DisjointSetElement r1, DisjointSetElement r2) {
        DisjointSetElement current = r2;
        while (current != null) {
            current.setRef1(r1);
            current = current.getRef2();
        }
        DisjointSetElement last = r1;
        while (last.getRef2() != null) {
            last = last.getRef2();
        }
        last.setRef2(r2);
        this.map.remove(r2);
    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
        return this.map.keySet();
        //return this.representatives;
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(DisjointSetElement e) {
        if (e == null) throw new NullPointerException
                ("Tentativo di ottenere gli elementi di un insieme disgiunto contenente un elemento null");
        if (!this.isPresent(e)) throw new IllegalArgumentException
                ("Tentativo di ottenere gli elementi di un insieme disgiunto contenente un elemento non presente");
        Set<DisjointSetElement> result = new HashSet<>();
        DisjointSetElement r = e.getRef1();
        while (r != null) {
            result.add(r);
            r = r.getRef2();
        }
        return result;
    }

    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {
        if ( e == null) throw new NullPointerException
                ("Tentativo di ottenere la cardinalità di un insieme disgiunto contenente un elemento null");
        if(!this.isPresent(e)) throw new IllegalArgumentException
                ("Tentativo di ottenere la cardinalità di un insieme disgiunto contenente un elemento non presente");
        return e.getRef1().getNumber();
    }
}