package it.unicam.cs.asdl2324.mp1;

import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;
import java.util.Objects;

public class MyMultiset<E> implements Multiset<E> {
    private Map<E, Integer> map;
    private int size;
    private int modCount;

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
        return this.map.getOrDefault(element, 0);
    }

    @Override
    public int add(E element, int occurrences) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        if (occurrences < 0) throw new IllegalArgumentException("Le occorrenze devono essere non negative");
        int oldCount = this.count(element);
        if (occurrences > (Integer.MAX_VALUE - oldCount))
            throw new IllegalArgumentException("Le occorrenze superano il limite massimo");
        this.map.put(element, oldCount + occurrences);
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
        int oldCount = this.count(element);
        int newCount = oldCount - occurrences;
        if (newCount <= 0) {
            this.map.remove(element);
            newCount = 0;
        } else this.map.put((E) element, newCount);
        this.size -= (oldCount - newCount);
        if (oldCount != newCount) this.modCount++;
        return oldCount;
    }

    @Override
    public boolean remove(Object element) {
        return this.remove(element, 1) > 0;
    }

    @Override
    public int setCount(E element, int count) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        if (count < 0) throw new IllegalArgumentException("Il conteggio deve essere non negativo");
        int oldCount = this.count(element);
        if (count == 0) {
            if (this.map.containsKey(element)) {
                this.map.remove(element);
                this.modCount++;
            }
            return oldCount;
        }
        this.map.put(element, count);
        this.size += (count - oldCount);
        if (oldCount != count) this.modCount++;
        return oldCount;
    }

    @Override
    public Set<E> elementSet() {
        return new HashSet<E>(this.map.keySet());
    }

    @Override
    public Iterator<E> iterator() {
        return new MultisetIterator();
    }

    @Override
    public boolean contains(Object element) {
        if (element == null) throw new NullPointerException("L'elemento non può essere null");
        MultisetIterator itr = new MultisetIterator();
        while (itr.hasNext()) {
            if (itr.next().equals(element)) return true;
        }
        return false;
    }

    @Override
    public void clear() {
        this.map.clear();
        this.size = 0;
        this.modCount = 0;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !(obj instanceof MyMultiset<?>)) return false;
        MyMultiset<?> that = (MyMultiset<?>) obj;
        return size == that.size && Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, map);
    }

    private class MultisetIterator implements Iterator<E> {
        private Iterator<E> keyIterator;
        private E currentKey;
        private int currentCount;
        private int itrModCount;

        public MultisetIterator() {
            this.keyIterator = MyMultiset.this.map.keySet().iterator();
            this.currentKey = null;
            this.currentCount = 0;
            this.itrModCount = MyMultiset.this.modCount;
        }

        @Override
        public boolean hasNext() {
            checkModification();
            return this.keyIterator.hasNext() || this.currentCount > 0;
        }

        @Override
        public E next() {
            checkModification();
            if (!hasNext()) {
                throw new NoSuchElementException("Non ci sono più elementi");
            }
            if (this.currentCount == 0) {
                if (!this.keyIterator.hasNext()) {
                    throw new NoSuchElementException("Non ci sono più elementi");
                }
                this.currentKey = this.keyIterator.next();
                this.currentCount = MyMultiset.this.map.get(this.currentKey);
            }
            this.currentCount--;
            return this.currentKey;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("L'operazione remove non è supportata");
        }

        private void checkModification() {
            if (MyMultiset.this.modCount != this.itrModCount) {
                throw new ConcurrentModificationException("Il multiset è stato modificato");
            }
        }
    }
}
