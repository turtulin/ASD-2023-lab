package it.unicam.cs.asdl2324.mp1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.HashSet;


/**
 * Per implementare il multiset, ho usato una HashMap<E, Integer> come struttura dati interna,
 * dove la chiave E è l’elemento del multiset, e il valore Integer è il numero di occorrenze dell’elemento.
 * Ho scelto di usare una HashMap perché mi permette di memorizzare gli elementi distinti del multiset
 * e le loro occorrenze in modo efficiente e rapido, sfruttando il meccanismo di hashing.
 * Inoltre, la HashMap non garantisce alcun ordine degli elementi, il che è coerente con
 * la definizione di multiset.
 * Per implementare l’iteratore, ho definito una classe interna MultisetIterator che implementa
 * l’interfaccia Iterator<E>. Ho scelto di usare una classe interna perché mi permette di accedere
 * ai campi e ai metodi della classe esterna MyMultiset<E>, e di definire un iteratore specifico
 * per il multiset.
 * Inoltre, ho scelto di implementare l’interfaccia Iterator<E> perché mi permette di restituire
 * gli elementi del multiset uno alla volta, senza esporre la struttura dati interna, e di seguire
 * il contratto stabilito dall’interfaccia.
 * La classe interna MultisetIterator usa un Iterator<E> interno che itera sul keySet della HashMap interna,
 * e una variabile che tiene conto delle occorrenze di ogni chiave. Ho scelto di usare un Iterator<E>
 * perché mi permette di ottenere un iteratore sul Set<E> che contiene tutti gli elementi distinti
 * del multiset, sfruttando il metodo keySet della classe HashMap.
 * Inoltre, ho scelto di usare una variabile per le occorrenze perché mi permette di restituire
 * gli elementi del multiset in base al loro numero di occorrenze, usando il metodo get della classe
 * HashMap per ottenere il valore associato a ogni chiave.
 * 
 * @author Luca Tesei (template) Marta Musso marta.musso@studenti.unicam.it (implementazione)
 *
 * @param <E>
 *                il tipo degli elementi del multiset
 */
public class MyMultiset<E> implements Multiset<E> {
    // La HashMap che rappresenta il multiset
    // La chiave è l'elemento E, il valore è il numero di occorrenze
    private HashMap<E, Integer> map;
    // Il numero totale di elementi nel multiset, contando le occorrenze
    // Non posso usare il metodo size() perchè estituisce il numero di chiavi
    // nella HashMap, non il numero totale di elementi nel multiset.
    private int size;
    // Il numero di modifiche strutturali al multiset
    // Serve per controllare la validità dell'iteratore
    private int modCount;

    /**
     * Crea un multiset vuoto.
     */
    public MyMultiset() {
        // Inizializzo la HashMap vuota
        this.map = new HashMap<>();
        // Inizializzo il numero di elementi e di modifiche a zero
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        // Restituisco il numero di elementi nel multiset
        return this.size;
    }

    @Override
    public int count(Object element) {
        // Controllo che l'elemento non sia null
        Objects.requireNonNull(element, "L'elemento non può essere null");
        // Restituisco il valore associato alla chiave nella HashMap,
        // o zero se non presente
        return this.map.getOrDefault(element, 0);
    }

    @Override
    public int add(E element, int occurrences) {
        // Controllo che l'elemento non sia null
        Objects.requireNonNull(element, "L'elemento non può essere null");
        // Controllo che le occorrenze non siano negative
        if (occurrences < 0) {
            throw  new IllegalArgumentException("Le occorrenze devono essere non negative");
        }
        // Controllo che le occorrenze non superino il limite massimo
        // Uso il metodo count per ottenere il numero di occorrenze attuali dell'elemento
        if (occurrences > Integer.MAX_VALUE - this.count(element)) {
            throw new IllegalArgumentException("Le occorrenze superano il limite massimo");
        }
        // Aggiungo le occorrenze al valore associato alla chiave nella HashMap,
        // o inserisco una nuova coppia se non presente.
        // Uso il metodo getOrDefault per ottenere il valore precedente della chiave,
        // o zero se non è presente
        int oldCount = this.map.getOrDefault(element, 0);
        // Calcolo il nuovo valore sommando le occorrenze
        int newCount = oldCount + occurrences;
        // Uso il metodo put per aggiornare il valore associato alla chiave nella HashMap
        this.map.put(element, newCount);
        // Aggiorno il numero totale di elementi e il numero di modifiche strutturali
        // Incremento il numero di elementi occurrences
        this.size += occurrences;
        // Incremento il numero di modifiche di uno
        this.modCount++;
        // Restituisco il valore precedente della chiave
        return oldCount;
    }

    @Override
    public void add(E element) {
        // Chiamo il metodo add con occurrences uguale a uno
        // Questo equivale ad aggiungere una singola occorrenza dell'elemento
        this.add(element, 1);
    }

    @Override
    public int remove(Object element, int occurrences) {
        // Controllo che l'elemento non sia null
        Objects.requireNonNull(element, "L'elemento non può essere null");
        // Controllo che le occorrenze siano non negative
        if (occurrences < 0) {
            throw new IllegalArgumentException("Le occorrenze devono essere non negative");
        }
        // Rimuovo le occorrenze al valore associato alla chiave della HashMap, se presente
        // Uso il metodo getOrDefault per ottenere il valore precedente della chiave,
        // o zero se non presente
        int oldCount = this.map.getOrDefault(element, 0);
        // Calcolo il nuovo valore sottraendo le occorrenze
        int newCount = oldCount - occurrences;
        // Se il nuovo valore è zero o negativo, rimuovo la chiave della HashMap
        // Uso il metodo remove della classe HashMap
        if (newCount <= 0) {
            this.map.remove(element);
            // Imposto il nuovo valore a zero per evitare valori negativi
            newCount = 0;
        } else {
            // Altrimenti aggiorno il valore associato alla chiave nella HashMap
            // Uso il metodo put della classe HashMap
            this.map.put((E) element, newCount);
        }
        // Aggiorno il numero totale di elementi e il numero di modifiche strutturali
        // Decremento il numero di elementi della differenza tra il vecchio e il nuovo valore
        this.size -= (oldCount - newCount);
        // Se il valore precedente era diverso dal nunovo, il multiset è cambiato
        // Incremento il numero di modifiche di uno
        if (oldCount != newCount) {
            this.modCount++;
        }
        // Restituisco il valore precedente della chiave
        return oldCount;
    }

    @Override
    public boolean remove(Object element) {
        // Chiamo il metodo remove con occurrences uguale a uno
        // Questo equivale a rimuovere una singola occorrenza dell'elemento
        int oldCount = this.remove(element, 1);
        // Restituisco true se il valore precedente era maggiore di zero, false altrimenti
        // Questo significa che l'elemento era presente nel multiset e che è stato rimosso
        return oldCount > 0;
    }

    @Override
    public int setCount(E element, int count) {
        // Controllo che l'elemento non sia null
        Objects.requireNonNull(element, "L'elemento non può essere null");
        // Controllo che il conteggio non sia negativo
        if (count < 0) {
            throw new IllegalArgumentException("Il conteggio deve essere non negativo");
        }
        // Imposto il valore associato alla chiave nella HashMap uguale a count,
        // o inserisco una nuova coppia se non presente
        // Uso il metodo getOrDefault per ottenere il valore precedente della chiave,
        // o zero se non presente
        int oldCount = this.map.getOrDefault(element, 0);
        // Uso il metodo put per aggiornare il valore associato alla chiave nella HashMap
        this.map.put(element, count);
        // Se il conteggio è zero o negativo, rimuovo la chiave della HashMap
        // Uso il metodo remove della classe HashMap
        if (count <= 0) {
            this.map.remove(element);
            // Imposto il conteggio a zero per evitare valori negativi
            count = 0;
        }
        // Aggiorno il numero totale di elementi e il numero di modifiche strutturali
        // Incremento o decremento il numero di elementi della differenza tra
        // il nuovo e il vecchio valore
        this.size += (count - oldCount);
        // Se il valore precedente era diverso dal nunovo, il multiset è cambiato
        // Incremento il numero di modifiche di uno
        if (oldCount != count) {
            this.modCount++;
        }
        // Restituisco il valore precedente della chiave
        return oldCount;
    }

    @Override
    public Set<E> elementSet() {
        // Creo un nuovo HasSet<E> con il keySet della HashMap interna come parametro
        // Questo crea una copia del keySet, che è un Set<E> contenente tutti
        // gli elementi distinti del multiset
        return new HashSet<E>(this.map.keySet());
    }

    @Override
    public Iterator<E> iterator() {
        // Restituisco un iteratore interno per il multiset
        return new MultisetIterator();
    }

    @Override
    public boolean contains(Object element) {
        // Controllo che l'elemento non sia null
        Objects.requireNonNull(element, "L'elemento non può essere null");
        // Restituisco true se la HashMap interna contiene la chiave element, false altrimenti
        // Uso il metodo containsKey della classe HashMap
        return this.map.containsKey(element);
    }

    @Override
    public void clear() {
        // Chiamo il metodo clear della HashMap interna, che rimuove tutte le coppie chiave-valore
        this.map.clear();
        // Azzero il numero totale di elementi e il numero di modifiche strutturali
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public boolean isEmpty() {
        // Restituisco tre se la HashMap interna è vuota, false altrimenti
        // Uso il metodo isEmpty della classe HashMap
        return this.map.isEmpty();
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
        // Controllo se l'oggetto è lo stesso
        if (this == obj) {
            return true;
        }
        // Controllo se l'oggetto è un'istanza di MyMultiset
        if (!(obj instanceof MyMultiset<?>)) {
            return false;
        }
        // Casto l'oggetto a MYMultiset
        MyMultiset<?> other = (MyMultiset<?>) obj;
        // Confronto le HashMap interne usando il metodo equals della classe HashMap
        // Questo metodo confronta le coppie cihave valore delle due HashMap
        // e restituisce true se sono uguali, false altrimenti
        return this.map.equals(other.map);
    }

    /*
     * Da ridefinire in accordo con la ridefinizione di equals.
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        // Uso il metodo hashCode della classe HashMap, applicato alla HashMap interna
        // Questo metodo calcola il codice hash delle coppie chiave valore della HashMap
        // e restituisce il loro valore sommato
        // Questo assicura che due multiset uguali abbiano lo stesso codice hash
        return this.map.hashCode();
    }

    // Classe interna che implementa l'iteratore per il multiset
    private class MultisetIterator implements Iterator<E> {
        // Un iteratore per il keySet della HashMap interna
        // Questo iteratore permette di scorrere le chiavi della HashMap
        private Iterator<E> keyIterator;
        // La chiave corrente
        private E currentKey;
        // Il numero di volte che la chiave corrente è stata restituita
        private int currentCount;
        // Il numero di modifiche strutturali al multiset al momento della creazione
        // dell'iteratore
        // Serve per controllare la validità dell'iteratore
        private int expectedModCount;

        // Costruttore dell'iteratore
        public MultisetIterator() {
            // Inizializzo l'iteratore per il keySet
            // Uso il metodo iterator della classe Set
            this.keyIterator = MyMultiset.this.map.keySet().iterator();
            // Inizializzo la chiave corrente a null
            this.currentKey = null;
            // Inizializzo il contatore a zero
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