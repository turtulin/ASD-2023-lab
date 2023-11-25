package it.unicam.cs.asdl2324.mp1;

import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.Objects;

/**
 * Prima di iniziare la stesura del progetto, ho cercato la struttura dati ideale
 * per implementare i multiset.
 * Ho eseguito un'attenta valutazione e ricerca, documentandomi principalmente su
 * <a href="^1^ https://www.geeksforgeeks.org/">GeeksforGeeks</a> e
 * <a href="^1^ https://www.baeldung.com/">Baeldung</a>.
 * Dopodichè, basandomi anche sul contenuto del file di istruzioni del progetto,
 * ho deciso che per rappresentare il multiset in modo efficiente dal
 * punto di vista della memoria, avrei usato una {@code HashMap<E, Integer>} che
 * associa ad ogni elemento E il suo numero di occorrenze nel multiset.
 * Questo mi permette di evitare di creare copie inutili di puntatori agli elementi
 * e di accedere al loro conteggio in tempo costante, rendendo la ricerca più efficiente.
 * Un altro vantaggio è quello di poter utilizzare alcuni metodi della classe
 * HashMap, come {@code put()}, {@code remove()} e {@code clear()}.
 * Inoltre, questa struttura dati non garantisce alcun ordine degli elementi,
 * il che è coerente con la definizione di multiset.
 * Per implementare l’iteratore, ho definito una classe interna MultisetIterator
 * perché mi permette di accedere ai campi e ai metodi della classe esterna
 * {@code MyMultiset<E>}, e di definire un iteratore specifico per il multiset.
 * Inoltre, implementa l’interfaccia {@code Iterator<E>} per permettermi
 * di restituire gli elementi del multiset uno alla volta, senza esporre
 * la struttura dati interna.
 *
 * @author  Luca Tesei (template)
 *          MARTA MUSSO marta.musso@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */

public class MyMultiset<E> implements Multiset<E> {
    // La chiave è l'elemento E, il valore è il numero di occorrenze.
    private Map<E, Integer> map;
    // Il numero totale di elementi nel multiset, contando le occorrenze.
    // Non uso il metodo size() della classe HashMap perchè restituisce il
    // numero di coppie chiave-valore nella mappa, non il numero totale
    // di elementi comprese le occorrenze.
    private int size;
    // Il numero di modifiche strutturali al multiset.
    private int modCount;

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
        this.map = new HashMap<>();
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int count(Object element) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        // Un implementazione simile sarebbe stata possibile utilizzando il metodo get() come segue:
        //      Integer occurrences = this.map.get(element);
        //      if (occurrences == null) return 0;
        //      return occurrences;
        // ma in questo caso, in accordo con l'implementazione dei metodi get() e getOrDefault()
        // nella classe HashMap, i quali risultano identici a parte per il loro valore di ritorno,
        // mi accorgo che utilizzando il metodo getOrDefault risparmerei le righe di codice
        // sopra riportate.
        return this.map.getOrDefault(element, 0);
    }

    @Override
    public int add(E element, int occurrences) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        if (occurrences < 0) throw new IllegalArgumentException("Le occorrenze devono essere non negative");
        // Inizializzo il valore di oldCount con il valore attuale di occorrenze,
        // possibilmente zero.
        int oldCount = this.count(element);
        if (occurrences > (Integer.MAX_VALUE - oldCount))
            throw new IllegalArgumentException("Le occorrenze superano il limite massimo");
        // Uso il metodo put per aggiornare il valore associato alla chiave nella HashMap.
        this.map.put(element, oldCount +  occurrences);
        // Aggiorno il numero totale di elementi e il numero di modifiche strutturali
        // solo se è stato modificato il numero di occorrenze.
        this.size += occurrences;
        if (occurrences != 0) this.modCount++;
        return oldCount;
    }

    @Override
    public void add(E element) {
        this.add(element, 1);
    }

    @Override
    public int remove(Object element, int occurrences) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        if (occurrences < 0) throw new IllegalArgumentException("Le occorrenze devono essere non negative");
        // Inizializzo il valore di oldCount con il valore attuale di occorrenze,
        // possibilmente zero.
        int oldCount = this.count(element);
        int newCount = oldCount - occurrences;
        if (newCount <= 0) {
            this.map.remove(element);
            // Imposto newCount a zero per evitare valori negativi.
            newCount = 0;
        } else this.map.put((E) element, newCount);
        // Aggiorno il numero totale di elementi e il numero di modifiche strutturali
        // solo se è stato modificato il numero di occorrenze.
        this.size -= (oldCount - newCount);
        if (oldCount != newCount) this.modCount++;
        return oldCount;
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        // Il metodo ritorna true se oldCount (valore ritornato da remove())
        // era maggiore di zero e quindi element apparteneva alla HashMap ed
        // è stata rimossa un'occorrenza, false altrimenti.
        return this.remove(element, 1) > 0;
    }

    @Override
    public int setCount(E element, int count) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        if (count < 0) throw new IllegalArgumentException("Il conteggio deve essere non negativo");
        // Inizializzo il valore di oldCount con il valore attuale di occorrenze,
        // possibilmente zero.
        int oldCount = this.count(element);
        if (count == 0) {
            if (this.map.containsKey(element)) {
                this.map.remove(element);
                this.modCount++;
            }
            return oldCount;
        }
        this.map.put(element, count);
        // Aggiorno il numero totale di elementi e il numero di modifiche strutturali
        // solo se è stato modificato il numero di occorrenze.
        this.size += (count - oldCount);
        if (oldCount != count) this.modCount++;
        return oldCount;
    }

    @Override
    public Set<E> elementSet() {
        // Creo un nuovo HashSet<E> con il keySet della HashMap interna come parametro.
        return new HashSet<E>(this.map.keySet());
    }

    @Override
    public Iterator<E> iterator() {
        return new MultisetIterator();
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        // Uso l'iteratore definito dalla classe MultisetIterator per scorrere tutti gli
        // elementi della mappa.
        MultisetIterator itr = new MultisetIterator();
        while (itr.hasNext()) {
            if (itr.next().equals(element)) return true;
        }
        return false;
    }

    @Override
    public void clear() {
        // Uso il metodo clear dell'interfaccia Map perchè se usassi l'iteratore
        // ad ogni rimozione di un elemento lancerebbe una ConcurrentModificationException
        // come richiesto dall'iteratore fail-fast.
        this.map.clear();
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public boolean isEmpty() {
        // Approfitto dell'esistenza della variabile size che mi permette di
        // evitare l'utilizzo di metodi come isEmpty() dell'interfaccia Map
        return this.size == 0;
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
        if (this == obj) return true;
        if (obj == null || !(obj instanceof MyMultiset<?>)) return false;
        MyMultiset<?> that = (MyMultiset<?>) obj;
        // Utilizzare questo controllo mi fa risparmiare tempo e memoria rispetto
        // all'utilizzo di un iteratore.
        return size == that.size && Objects.equals(map, that.map);
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(size, map);
    }

    /**
     * Classe che rappresenta un iteratore per un multiset.
     */
    private class MultisetIterator implements Iterator<E> {
        private Iterator<E> keyIterator;
        private E currentKey;
        // Il numero da occorrenze ancora da iterare della currentKey.
        private int currentCount;
        // Il valore di modCount al momento della creazione dell'iteratore.
        private int itrModCount;

        /**
         * Crea un iterator vuoto per un multiset, estrapolando le sue chiavi.
         */
        public MultisetIterator() {
            this.keyIterator = MyMultiset.this.map.keySet().iterator();
            this.currentKey = null;
            this.currentCount = 0;
            this.itrModCount = MyMultiset.this.modCount;
        }

        @Override
        public boolean hasNext() {
            // Controllo che il multiset non sia stato modificato.
            checkForComodification();
            // Il metodo ritorna true se esiste una chiave successiva non null alla currentKey,
            // oppure se la currentKey ha ancora delle occorrenze da iterare
            // (e di conseguenza il numero delle sue occorrenze iterate è maggiore di zero).
            return this.keyIterator.hasNext() || this.currentCount > 0;
        }

        @Override
        public E next() {
            // Controllo che il multiset non sia stato modificato.
            checkForComodification();
            if (!hasNext()) {
                throw new NoSuchElementException("Non ci sono più elementi");
            }
            // Se il numero di occorrenze ancora da iterare è zero, passo alla chiave successiva
            // a meno che non sia null, in tal caso lancio una NoSuchElementException.
            if (this.currentCount == 0) {
                if (!this.keyIterator.hasNext()) {
                    throw new NoSuchElementException("Non ci sono più elementi");
                }
                this.currentKey = this.keyIterator.next();
                // Assegno a currenCount il valore di occorrenze della currentKey.
                this.currentCount = MyMultiset.this.map.get(this.currentKey);
            }
            // Se il numero di occorrenze ancora da iterare è maggiore di zero, diminuisco di
            // uno il valore di currentCount e ritorno il valore associato alla currentKey.
            this.currentCount--;
            return this.currentKey;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("L'operazione remove non è supportata");
        }

        /**
         * Lancia un'eccezione se il multiset è stato modificato strutturalmente.
         */
        private void checkForComodification() {
            // Controllo se il numero di modifiche strutturali al multiset è diverso
            // da quello memorizzato alla creazione dell'iteratore.
            if (MyMultiset.this.modCount != this.itrModCount) {
                throw new ConcurrentModificationException("Il multiset è stato modificato");
            }
        }
    }
}