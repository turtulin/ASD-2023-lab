package it.unicam.cs.asdl2324.mp1;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

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
    // La mappa che associa ad ogni elemento il suo rappresentante
    private HashMap<DisjointSetElement, DisjointSetElement> map;
    // Il set dei rappresentanti degli insiemi disgiunti attualmente presenti
    private HashSet<DisjointSetElement> representatives;

    /**
     * Crea una collezione vuota di insiemi disgiunti.
     */
    public LinkedListDisjointSets() {
        // Inizializza la mappa e il set come vuoti
        this.map = new HashMap<DisjointSetElement, DisjointSetElement>();
        this.representatives = new HashSet<DisjointSetElement>();
    }

    /*
     * Nella rappresentazione con liste concatenate un elemento è presente in
     * qualche insieme disgiunto se il puntatore al suo elemento rappresentante
     * (ref1) non è null.
     */
    @Override
    public boolean isPresent(DisjointSetElement e) {
        // Controllo che e non sia null
        // Come nella classe MyMultiset, anzichè usare
        // if (e == null) throw new NullPointerException("Messaggio");
        // utilizzo un metodo statico della classe Objects che richiede
        // solo una chiamata e un messaggio opzionale, rendendo il codice
        // più compatto, leggibile ed efficiente
        Objects.requireNonNull(e, "Tentativo di verificare la presenza di un elemento null");
        // Controllo se la mappa contiene e come chiave
        return this.map.containsKey(e);
    }

    /*
     * Nella rappresentazione con liste concatenate un nuovo insieme disgiunto è
     * rappresentato da una lista concatenata che contiene l'unico elemento. Il
     * rappresentante deve essere l'elemento stesso e la cardinalità deve essere 1.
     */
    @Override
    public void makeSet(DisjointSetElement e) {
        // Controllo  che e non sia null
        Objects.requireNonNull(e, "Tentativo di creare un insieme disgiunto con un elemento null");
        // Controllo che non sia già presente nella collezione
        if (this.isPresent(e)) {
            throw new IllegalArgumentException(
                    "Tentativo di creare un insieme disgiunto con un elemento già presente");
        }
        // Imposto il puntatore ref1 di e a se stesso
        e.setRef1(e);
        // Imposto il puntatore ref2 di e a null
        e.setRef2(null);
        // Imposto il numero di e a 1
        e.setNumber(1);
        // Inserisco la coppia (e, e) nella mappa
        this.map.put(e, e);
        // Inserisco e nel set dei rappresentanti
        this.representatives.add(e);
    }

    /*
     * Nella rappresentazione con liste concatenate per trovare il
     * rappresentante di un elemento basta far riferimento al suo puntatore
     * ref1.
     */
    @Override
    public DisjointSetElement findSet(DisjointSetElement e) {
        // Controllo che è non sia null
        Objects.requireNonNull(e, "Tentativo di trovare il rappresentante di un elemento null");
        // Controllo che e sia presente nella collezione
        if (!this.isPresent(e)) {
            throw new IllegalArgumentException(
                    "Tentativo di trovare il rappresentante di un elemento non presente");
        }
        // Restituisce il valore associato a e nella mappa
        return this.map.get(e);
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
        // Controllo che e1 ed e2 non siano null
        Objects.requireNonNull(e1, "Tentativo di unire due insiemi disgiunti" +
                "con il primo elemento null");
        Objects.requireNonNull(e2, "Tentativo di unire due insiemi disgiunti" +
                "con il secondo elemento null");
        // Controlla che e1 ed e2 siano presenti nella collezione
        if (!this.isPresent(e1) || !this.isPresent(e2)) {
            throw new IllegalArgumentException(("Tentativo di unire due insiemi disgiunti" +
                    "con almeno un elemento non presente"));
        }
        // Trovo i rappresentanti di e1 ed e2 usando la mappa
        DisjointSetElement r1 = this.map.get(e1);
        DisjointSetElement r2 = this.map.get(e2);
        // Se r1 e r2 sono uguali, termino l'operazione
        if (r1 == r2) {
            return;
        }
        // Altrimenti, unisco i due insiemi disgiunti
        // Trovo le cardinalità dei due insiemi disgiunti usando il numero di r1 e r2
        int c1 = r1.getNumber();
        int c2 = r2.getNumber();
        // Se c1 è maggiore o uguale a c2, appendo la lista di r2 alla fine di quella di 21
        if (c1 >= c2) {
            // Trovo l'ultimo elemento della lista di r1
            DisjointSetElement last1 = r1;
            while (last1.getRef2() != null) {
                last1 = last1.getRef2();
            }
            // Imposto il puntatore ref2 di last1 a r2
            last1.setRef2(r2);
            // Imposto il puntatore ref1 di r2 a r1
            r2.setRef1(r1);
            // Aggiorno il numero di r1
            r1.setNumber(c1 + c2);
            // Scorro la lista di r2 e aggiorno i puntatori ref1 e la mappa
            DisjointSetElement current = r2;
            while (current != null) {
                // Imposto il puntatore ref1 di current a r1
                current.setRef1(r1);
                // Aggiorno la mappa
                this.map.put(current, r1);
                // Passo al prossimo elemento
                current = current.getRef2();
            }
            // Rimuovo r2 dal set dei rappresentanti
            this.representatives.remove(r2);
        }
        // Se c1 è minore di c2, appendo la lista di r1 alla fine di quella di r2
        else {
            // Trovo l'ultimo elemento delle lista di r2
            DisjointSetElement last2 = r2;
            while (last2.getRef2() != null) {
                last2 = last2.getRef2();
            }
            // Imposto il puntatore ref2 di last2 a r1
            last2.setRef2(r1);
            // Imposto il puntatore ref1 di r1 a r2
            r1.setRef1(r2);
            // Aggiorno il numero di r2
            r2.setNumber(c1 + c2);
            // Scorro la lista di r1 e aggiorno i puntatori ref1 e la mappa
            DisjointSetElement current = r1;
            while (current != null) {
                // Imposto il puntatore ref1 di current a r2
                current.setRef1(r2);
                // Aggiorno la mappa
                this.map.put(current, r2);
                // Passo al prossimo elemento
                current = current.getRef2();
            }
            // Rimuovi r1 dal set dei rappresentanti
            this.representatives.remove(r1);
        }
    }

    @Override
    public Set<DisjointSetElement> getCurrentRepresentatives() {
        return this.representatives;
    }

    @Override
    public Set<DisjointSetElement> getCurrentElementsOfSetContaining(DisjointSetElement e) {
        // Controllo che e non sia null
        Objects.requireNonNull(e, "Tentativo di ottenere gli elementi di un" +
                "insieme disgiunto contenente un elemento null");
        // Controllo che sia presente nella collezione
        if (!this.isPresent(e)) {
            throw new IllegalArgumentException("Tentativo di ottenere gli elementi di un" +
                    "insieme disgiunto contenente un elemento non presente");
        }
        // Creo un nuovo set vuoto di tipo DisjointSetElement
        Set<DisjointSetElement> result = new HashSet<DisjointSetElement>();
        // Trovo il rappresentante dell'insieme disgiunto di e usando la mappa
        DisjointSetElement r = this.map.get(e);
        // Scorro la lista concatenata di r e aggiungi ogni elemento al set
        DisjointSetElement current = r;
        while (current != null) {
            // Aggiungo current al set
            result.add(current);
            // Passo al prossimo elemento
            current = current.getRef2();
        }
        // Restituisci il set creato
        return result;
    }

    @Override
    public int getCardinalityOfSetContaining(DisjointSetElement e) {
        // Controllo che e non sia null
        Objects.requireNonNull(e, "Tentativo di ottenere la cardinalità di un insieme" +
                "disgiunto contenente un elemento null");
        // Controllo che e sia presente nella collezione
        if(!this.isPresent(e)) {
            throw new IllegalArgumentException("Tentativo di ottenere la cardinalità di un insieme" +
                    "dsgiunto contenente un elemento non presente");
        }
        // Trovo il rappresentante dell'insieme disgiunto di e usando la mappa
        DisjointSetElement r = this.map.get(e);
        // Restituisce il numero di r, che corrisponde alla cardinalità dell'insieme
        // disgiunto di e
        return r.getNumber();
    }
}