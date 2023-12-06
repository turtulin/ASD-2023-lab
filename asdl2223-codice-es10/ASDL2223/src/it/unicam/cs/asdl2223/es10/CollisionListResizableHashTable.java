/**
 * 
 */
package it.unicam.cs.asdl2223.es10;

import java.util.*;

/**
 * Realizza un insieme tramite una tabella hash con indirizzamento primario (la
 * funzione di hash primario deve essere passata come parametro nel costruttore
 * e deve implementare l'interface PrimaryHashFunction) e liste di collisione.
 * 
 * La tabella, poiché implementa l'interfaccia Set<E> non accetta elementi
 * duplicati (individuati tramite il metodo equals() che si assume sia
 * opportunamente ridefinito nella classe E) e non accetta elementi null.
 * 
 * La tabella ha una dimensione iniziale di default (16) e un fattore di
 * caricamento di defaut (0.75). Quando il fattore di bilanciamento effettivo
 * eccede quello di default la tabella viene raddoppiata e viene fatto un
 * riposizionamento di tutti gli elementi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 */
public class CollisionListResizableHashTable<E> implements Set<E> {

    /*
     * La capacità iniziale. E' una potenza di due e quindi la capacità sarà
     * sempre una potenza di due, in quanto ogni resize raddoppia la tabella.
     */
    private static final int INITIAL_CAPACITY = 16;

    /*
     * Fattore di bilanciamento di default. Tipico valore.
     */
    private static final double LOAD_FACTOR = 0.75;

    /*
     * Numero di elementi effettivamente presenti nella hash table in questo
     * momento. ATTENZIONE: questo valore è diverso dalla capacity, che è la
     * lunghezza attuale dell'array di Object che rappresenta la tabella.
     */
    private int size;

    /*
     * L'idea è che l'elemento in posizione i della tabella hash è un bucket che
     * contiene null oppure il puntatore al primo nodo di una lista concatenata
     * di elementi. Si può riprendere e adattare il proprio codice della
     * Esercitazione 6 che realizzava una lista concatenata di elementi
     * generici. La classe interna Node<E> è ripresa proprio da lì.
     * 
     * ATTENZIONE: la tabella hash vera e propria può essere solo un generico
     * array di Object e non di Node<E> per una impossibilità del compilatore di
     * accettare di creare array a runtime con un tipo generics. Ciò infatti
     * comporterebbe dei problemi nel sistema di check dei tipi Java che, a
     * run-time, potrebbe eseguire degli assegnamenti in violazione del tipo
     * effettivo della variabile. Quindi usiamo un array di Object che
     * riempiremo sempre con null o con puntatori a oggetti di tipo Node<E>.
     * 
     * Per inserire un elemento nella tabella possiamo usare il polimorfismo di
     * Object:
     * 
     * this.table[i] = new Node<E>(item, next);
     * 
     * ma quando dobbiamo prendere un elemento dalla tabella saremo costretti a
     * fare un cast esplicito:
     * 
     * Node<E> myNode = (Node<E>) this.table[i];
     * 
     * Ci sarà dato un warning di cast non controllato, ma possiamo eliminarlo
     * con un tag @SuppressWarning,
     */
    private Object[] table;

    /*
     * Funzion di hash primaria usata da questa hash table. Va inizializzata nel
     * costruttore all'atto di creazione dell'oggetto.
     */
    private final PrimaryHashFunction phf;

    /*
     * Contatore del numero di modifiche. Serve per rendere l'iterator
     * fail-fast.
     */
    private int modCount;

    // I due metodi seguenti sono di comodo per gestire la capacity e la soglia
    // oltre la quale bisogna fare il resize.

    /* Numero di elementi della tabella corrente */
    private int getCurrentCapacity() {
        return this.table.length;
    };

    /*
     * Valore corrente soglia oltre la quale si deve fare la resize,
     * getCurrentCapacity * LOAD_FACTOR
     */
    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

    /**
     * Costruisce una Hash Table con capacità iniziale di default e fattore di
     * caricamento di default.
     */
    public CollisionListResizableHashTable(PrimaryHashFunction phf) {
        this.phf = phf;
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) {
            throw new NullPointerException("Element cannot be null");
        }
        int index = Math.abs(element.hashCode() % table.length);
        if (table[index] == null) {
            return false;
        }
        Node<E> node = (Node<E>) table[index];
        while (node != null) {
            if (node.item.equals(element)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }


    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Element cannot be null");
        }
        if (contains(e)) {
            return false;
        }
        int index = Math.abs(e.hashCode() % table.length);
        Node<E> node = new Node<>(e, null);
        if (table[index] == null) {
            table[index] = node;
        } else {
            node.next = (Node<E>) table[index];
            table[index] = node;
        }
        size++;
        modCount++;
        if (size > getCurrentThreshold()) {
            resize();
        }
        return true;
    }

    /*
     * Raddoppia la tabella corrente e riposiziona tutti gli elementi. Da
     * chiamare quando this.size diventa maggiore di getCurrentThreshold()
     */
    private void resize() {
            Object[] oldTable = table;
            table = new Object[oldTable.length * 2];
            size = 0;
            for (Object bucket : oldTable) {
                if (bucket != null) {
                    Node<E> node = (Node<E>) bucket;
                    while (node != null) {
                        add(node.item);
                        node = node.next;
                    }
                }
            }
        }


        @Override
    public boolean remove(Object o) {
            if (o == null) {
                throw new NullPointerException("Element cannot be null");
            }
            int index = Math.abs(o.hashCode() % table.length);
            if (table[index] == null) {
                return false;
            }
            Node<E> node = (Node<E>) table[index];
            Node<E> prev = null;
            while (node != null) {
                if (node.item.equals(o)) {
                    if (prev == null) {
                        table[index] = node.next;
                    } else {
                        prev.next = node.next;
                    }
                    size--;
                    modCount++;
                    return true;
                }
                prev = node;
                node = node.next;
            }
            return false;
        }


    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (element == null) {
                throw new NullPointerException("Collection cannot contain null elements");
            }
        }
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) {
            throw new NullPointerException("Collection cannot be null");
        }
        boolean modified = false;
        for (E element : c) {
            boolean added = add(element);
            if (added) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Operazione non supportata");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object element : c) {
            if (element == null) {
                throw new NullPointerException("Collection cannot contain null elements");
            }
        }
        boolean modified = false;
        for (Object element : c) {
            boolean removed = remove(element);
            if (removed) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        // Ritorno alla situazione iniziale
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    /*
     * Classe per i nodi della lista concatenata. Lo specificatore è protected
     * solo per permettere i test JUnit.
     */
    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        /*
         * Crea un nodo "singolo" equivalente a una lista con un solo elemento.
         */
        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    /*
     * Classe che realizza un iteratore per questa hash table. L'ordine in cui
     * vengono restituiti gli oggetti presenti non è rilevante, ma ogni oggetto
     * presente deve essere restituito dall'iteratore una e una sola volta.
     * L'iteratore deve essere fail-fast, cioè deve lanciare una eccezione
     * ConcurrentModificationException se a una chiamata di next() si "accorge"
     * che la tabella è stata cambiata rispetto a quando l'iteratore è stato
     * creato.
     */
    private class Itr implements Iterator<E> {
        private int numeroModificheAtteso;
        private int currentIndex;
        private Node<E> currentNode;
        private Node<E> previousNode;

        private Itr() {
            numeroModificheAtteso = modCount;
            currentIndex = 0;
            currentNode = findNextNode();
            previousNode = null;
        }

        private Node<E> findNextNode() {
            while (currentIndex < table.length) {
                if (table[currentIndex] == null) {
                    currentIndex++;
                } else {
                    return (Node<E>) table[currentIndex];
                }
            }
            return null;
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public E next() {
            if (modCount != numeroModificheAtteso) {
                throw new ConcurrentModificationException("Iterator modified inconsistently");
            }
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements");
            }
            Node<E> temp = currentNode;
            previousNode = currentNode;
            if (currentNode.next != null) {
                currentNode = currentNode.next;
            } else {
                currentIndex++;
                currentNode = findNextNode();
            }
            return temp.item;
        }
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected Object[] getTable() {
        return this.table;
    }

    /*
     * Only for JUnit testing purposes.
     */
    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}
