package it.unicam.cs.asdl2223.es10;

import java.util.*;

public class CollisionListResizableHashTable<E> implements Set<E> {
    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size;
    private Object[] table;
    private final PrimaryHashFunction phf;
    private int modCount;

    private int getCurrentCapacity() {
        return this.table.length;
    };

    private int getCurrentThreshold() {
        return (int) (getCurrentCapacity() * LOAD_FACTOR);
    }

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
        this.table = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.modCount = 0;
    }

    protected static class Node<E> {
        protected E item;

        protected Node<E> next;

        Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

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

    protected Object[] getTable() {
        return this.table;
    }

    protected PrimaryHashFunction getPhf() {
        return this.phf;
    }

}
