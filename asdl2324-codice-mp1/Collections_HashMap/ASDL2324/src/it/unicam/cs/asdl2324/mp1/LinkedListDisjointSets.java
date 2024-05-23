package it.unicam.cs.asdl2324.mp1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LinkedListDisjointSets implements DisjointSets {
    private Map<DisjointSetElement, DisjointSetElement> map;

    public LinkedListDisjointSets() {
        this.map = new HashMap<>();
    }

    @Override
    public boolean isPresent(DisjointSetElement e) {
        return e.getRef1() != null;
    }

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

    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
        if (e == null) throw new NullPointerException
                ("Tentativo di trovare il rappresentante di un elemento null");
        if (!this.isPresent(e)) throw new IllegalArgumentException
                ("Tentativo di trovare il rappresentante di un elemento non presente");
        return e.getRef1();
    }

    @Override
    public void union(DisjointSetElement e1, DisjointSetElement e2) {
        if (e1 == null || e2 == null) throw new NullPointerException
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
        if (e == null) throw new NullPointerException
                ("Tentativo di ottenere la cardinalità di un insieme disgiunto contenente un elemento null");
        if(!this.isPresent(e)) throw new IllegalArgumentException
                ("Tentativo di ottenere la cardinalità di un insieme disgiunto contenente un elemento non presente");
        return e.getRef1().getNumber();
    }
}
