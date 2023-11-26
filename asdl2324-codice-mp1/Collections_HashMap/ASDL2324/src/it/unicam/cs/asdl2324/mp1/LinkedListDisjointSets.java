package it.unicam.cs.asdl2324.mp1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Anche in questo caso, prima di iniziare la stesura del progetto, mi sono
 * documentata riguardo le strutture dati più efficienti per rappresentare
 * set di insiemi disgiunti.
 * Ho quindi deciso di usare una HashMap che associa al valore un insieme disgiunto
 * composto da elementi che mantengono la loro struttura di LinkedList tramite gli
 * appositi puntatori il cui comportamento è definito nell'interfaccia
 * {@code DisjointSetElement}, e alla chiave il suo  elemento rappresentante di
 * tipo {@code DisjointSetElement}.
 *
 * Questo tipo di rappresentazione rende più efficiente la ricerca dei
 * rappresentanti e anche dei valori contenuti all'interno dei singoli
 * insiemi disgiunti.
 * Inoltre, ho potuto sfruttare alcuni metodi della classe HashMap, come
 * {@code keySet()} che mi permettono di evitare, ad esempio, di inizializzare
 * un set apposito per contenere tutti i rappresentanti.
 *
 * @author  Luca Tesei (template)
 *      MARTA MUSSO marta.musso@studenti.unicam.it (implementazione)
 *
 */

public class LinkedListDisjointSets implements DisjointSets {
    // La Map che associa un rappresentante al suo insieme.
    private Map<DisjointSetElement, DisjointSetElement> map;

    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
        this.map = new HashMap<>();
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
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
        // Il primo elemento inserito nella HashMap sarà la chiave (rappresentante)
        // ma anche il primo valore nella LinkedList
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
        // Ottengo i rappresentanti degli insiemi di cui fanno parte e1 ed e2.
        DisjointSetElement r1 = e1.getRef1();
        DisjointSetElement r2 = e2.getRef1();
        // Se fanno parte dello stesso insieme, non faccio niente.
        if (r1 == r2) return;
        // Ottengo la cardinalità degli insiemi rappresentati da r1 e r2.
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
     * Unisce due insiemi disgiunti, accondando r2 a r1.
     * @param r1
     *                  la chiave dell'insieme con cardinalità maggiore
     * @param r2
     *                  la chiave dell'insieme con cardinalità minore
     */
    // Definisco questo metodo per evitare blocchi di codice ripetuti
    // nel metodo union() e renderlo più chiaro e pulito.
    private void merge (DisjointSetElement r1, DisjointSetElement r2) {
        DisjointSetElement current = r2;
        // Scorro tutti gli elementi dell'insieme rappresentato da r2,
        // aggiornando il loro puntatore ref1 a r1.
        while (current != null) {
            current.setRef1(r1);
            current = current.getRef2();
        }
        DisjointSetElement last = r1;
        // Scorro tutti gli elementi dell'insieme rappresentato da r1
        // fino ad arrivare all'ultimo.
        while (last.getRef2() != null) {
            last = last.getRef2();
        }
        // Aggiorno il puntatore ref2 dell'ultimo elemento dell'insieme
        // rappresentato da r1 a r2.
        last.setRef2(r2);
        this.map.remove(r2);
    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
        // Ritorno il set di tutte le chiavi contenute nella HashMap.
        return this.map.keySet();
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(DisjointSetElement e) {
        if (e == null) throw new NullPointerException
                ("Tentativo di ottenere gli elementi di un insieme disgiunto contenente un elemento null");
        if (!this.isPresent(e)) throw new IllegalArgumentException
                ("Tentativo di ottenere gli elementi di un insieme disgiunto contenente un elemento non presente");
        // L'HashSet che conterrà tutti gli elementi dell'insieme.
        Set<DisjointSetElement> result = new HashSet<>();
        // Il rappresentante dell'insieme di cui fa parte e.
        DisjointSetElement r = e.getRef1();
        // Scorro tutti gli elementi e li aggiungo all'HashSet creato prima.
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
        // Per ottenere la cardinalità dell'insieme di cui fa parte e, è sufficiente trovare
        // il suo rappresentante e richiamare il metodo getNumber() che fornisce il valore della
        // dimensione dell'insieme associato alla chiave.
        return e.getRef1().getNumber();
    }
}