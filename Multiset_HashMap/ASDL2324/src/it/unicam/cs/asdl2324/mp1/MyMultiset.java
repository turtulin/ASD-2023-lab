package it.unicam.cs.asdl2324.mp1;

// Potrei utilizzare:
//      import java.util.*;
// per rendere il codice più conciso, pulito, leggibile e flessibile.
// Tuttavia, ho scelto di utilizzare le importazioni specifiche
// per avere un codice più esplicito e sicuro e per sapere esattamente
// quali classi sono state importate e da dove provengono.
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.HashSet;

/**
 * Prima di iniziare la stesura del progetto, ho cercato la struttura dati ideale
 * per implementare i multiset.
 * Ho eseguito un'attenta valutazione e ricerca, documentandomi principalmente su
 * <a href="^1^ https://www.geeksforgeeks.org/">GeeksforGeeks</a> e
 * <a href="^1^ https://www.baeldung.com/">Baeldung</a>
 * Dopodichè, basandomi anche sul contenuto del file di istruzioni del progetto,
 * ho deciso che per rappresentare il multiset in modo efficiente dal
 * punto di vista della memoria, avrei usato una {@code HashMap<E, Integer>} che
 * associa ad ogni elemento E il suo numero di occorrenze nel multiset.
 * Questo mi permette di evitare di creare copie inutili di puntatori agli elementi
 * e di accedere al loro conteggio in tempo costante.
 * Un altro vantaggio è quello di poter utilizzare alcuni metodi della classe
 * HashMap, come {@code put()}, {@code remove()} e {@code keySet()}.
 * Inoltre, questa struttura dati non garantisce alcun ordine degli elementi,
 * il che è coerente con la definizione di multiset.
 * Per implementare l’iteratore, ho definito una classe interna MultisetIterator
 * perché mi permette di accedere ai campi e ai metodi della classe esterna
 * {@code MyMultiset<E>}, e di definire un iteratore specifico per il multiset.
 * Inoltre, ho scelto di farle implementare l’interfaccia {@code Iterator<E>} perché
 * mi permette di restituire gli elementi del multiset uno alla volta, senza esporre
 * la struttura dati interna, e di seguire il contratto stabilito dall’interfaccia.
 *
 * @author  Luca Tesei (template)
 *          Marta Musso marta.musso@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */

public class MyMultiset<E> implements Multiset<E> {
    // La HashMap che rappresenta il multiset.
    // La chiave è l'elemento E, il valore è il numero di occorrenze.
    private HashMap<E, Integer> map;
    // Il numero totale di elementi nel multiset, contando le occorrenze
    // Non uso il metodo size() della classe HashMap perchè restituisce il
    // numero di coppie chiave-valore nella mappa, non il numero totale
    // di elementi nel multiset, contando le occorrenze.
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
        // Aggiorno il valore di oldCount con il valore attuale di occorrenze,
        // possibilmente zero.
        int oldCount = this.count(element);
        if (occurrences > (Integer.MAX_VALUE - oldCount))
            throw new IllegalArgumentException("Le occorrenze superano il limite massimo");
        // Uso il metodo put per aggiornare il valore associato alla chiave nella HashMap
        this.map.put(element, oldCount +  occurrences);
        // Aggiorno il numero totale di elementi e il numero di modifiche strutturali
        this.size += occurrences;
        this.modCount++;
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
        int oldCount = this.count(element);
        int newCount = oldCount - occurrences;
        // Uso il metodo remove() della classe HashMap
        if (newCount <= 0) {
            this.map.remove(element);
            // Imposto il newCount a zero per evitare valori negativi
            newCount = 0;
        } else this.map.put((E) element, newCount);
        // Aggiorno il numero totale di elementi e il numero di modifiche strutturali
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
        int oldCount = this.count(element);
        if (count == 0) {
            this.map.remove(element);
            return oldCount;
        }
        this.map.put(element, count);
        this.size += (count - oldCount);
        if (oldCount != count) this.modCount++;
        return oldCount;
    }

    @Override
    public Set<E> elementSet() {
        // Creo un nuovo HashSet<E> con il keySet della HashMap interna come parametro
        return new HashSet<E>(this.map.keySet());
    }

    @Override
    public Iterator<E> iterator() {
        return new MultisetIterator();
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        // Uso il metodo containsKey della classe HashMap
        // Devo usare Iterator
        return this.map.containsKey(element);
    }

    @Override
    public void clear() {
        // Uso iterator
        this.map.clear();
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public boolean isEmpty() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof MyMultiset<?>)) return false;
        MyMultiset<?> that = (MyMultiset<?>) o;
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

    // Classe interna che implementa l'iteratore per il multiset
    private class MultisetIterator implements Iterator<E> {
        // Un iteratore per il keySet della HashMap interna
        // Questo iteratore permette di scorrere le chiavi della HashMap
        private Iterator<E> keyIterator;
        private E currentKey;
        // Il numero di volte che la chiave corrente è stata restituita
        private int currentCount;
        // Il numero di modifiche strutturali al multiset al momento della creazione
        // dell'iteratore
        // Serve per controllare la validità dell'iteratore
        private int expectedModCount;

        // Costruttore dell'iteratore SCRIVI JAVADOC
        public MultisetIterator() {
            // Inizializzo l'iteratore per il keySet
            // Uso il metodo iterator della classe Set
            this.keyIterator = MyMultiset.this.map.keySet().iterator();
            this.currentKey = null;
            this.currentCount = 0;
            // Memorizzo il numero di modifiche strutturali al multiset
            // Uso la variabile modCount della classe MyMultiSet
            this.expectedModCount = MyMultiset.this.modCount;
        }

        @Override
        public boolean hasNext() {
            // Controllo se il multiset è stato modificato strutturalmente
            // Uso il metodo checkForComodification definito in seguito
            checkForComodification();
            // Restituisco true se il keyIterator ha ancora chiavi da restituire
            // o se il contatore è maggiore di zero
            // Uso il metodo hasNext della classe Iterator
            return this.keyIterator.hasNext() || this.currentCount > 0;
        }

        @Override
        public E next() {
            /*
            checkForComodification();
            // Controllo se ci sono ancora elementi da restituire
            // Uso il metodo hasNext definito sopra
            if (!hasNext()) {
                throw new NoSuchElementException("Non ci sono più elementi");
            }
            // Se il contatore è zero, passo alla prossima chiave nel keyIterator
            if (this.currentCount == 0) {
                // Uso il metodo next della classe Iterator
                this.currentKey = this.keyIterator.next();
                // Uso il metodo get della classe HashMap per ottenere il valore associato
                // alla chiave corrente
                this.currentCount = MyMultiset.this.map.get(this.currentKey);
            }
            // Restituisco la chiave corrente e decremento il contatore
            this.currentCount--;
            return this.currentKey;

             */
            // Controllo se il multiset è stato modificato strutturalmente
            // Uso il metodo checkForComodification definito in seguito
            checkForComodification();
            // Controllo se ci sono ancora elementi da restituire
            // Uso il metodo hasNext definito sopra
            if (!hasNext()) {
                throw new NoSuchElementException("Non ci sono più elementi");
            }
            // Se il contatore è zero, passo alla prossima chiave nel keyIterator
            if (this.currentCount == 0) {
                // Controllo se il keyIterator ha ancora chiavi da restituire
                // Uso il metodo hasNext della classe Iterator
                if (!this.keyIterator.hasNext()) {
                    throw new NoSuchElementException("Non ci sono più elementi");
                }
                // Uso il metodo next della classe Iterator
                this.currentKey = this.keyIterator.next();
                // Uso il metodo get della classe HashMap per ottenere il valore associato
                // alla chiave corrente
                this.currentCount = MyMultiset.this.map.get(this.currentKey);
            }
            // Restituisco la chiave corrente e decremento il contatore
            this.currentCount--;
            return this.currentKey;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("L'operazione remove non è supportata");
        }

        /**
         * Lancia un'eccezione se il multiset è stato modificato strutturalmente
         */
        private void checkForComodification() {
            // Controllo se il numero di modifiche strutturali al multiset è diverso
            // da quello memorizzato
            // Uso la variabile modCount della classe MyMultiSet
            if (MyMultiset.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException("Il multiset è stato modificato");
            }
        }
    }
}