package it.unicam.cs.asdl2324.mp2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class ForestDisjointSets<E> implements DisjointSets<E> {

    protected Map<E, Node<E>> currentElements;
    
    protected static class Node<E> {

        protected E item;

        protected Node<E> parent;

        protected int rank;

        public Node(E item) {
            this.item = item;
            this.parent = this;
            this.rank = 0;
        }

    }

    public ForestDisjointSets() {
        currentElements = new HashMap<>();
    }

    @Override
    public boolean isPresent(E e) {
        if (e == null) throw new NullPointerException("Elemento null");
        return currentElements.containsKey(e);
    }

    @Override
    public void makeSet(E e) {
        if (e == null) throw new NullPointerException("Elemento  null");
        if (isPresent(e)) throw new IllegalArgumentException("Nodo già presente");
        currentElements.put(e, new Node<>(e));
    }

    @Override
    public E findSet(E e) {
        if (e == null) throw new NullPointerException("Elemento null");
        Node<E> node = currentElements.get(e);
        if (node == null) return null;
        if (node.parent == node) return node.item;
        node.parent = currentElements.get(findSet(node.parent.item));
        return node.parent.item;
    }

    @Override
    public void union(E e1, E e2) {
        if (e1 == null || e2 == null) throw new NullPointerException("Elemento nullo nella foresta");
        if (!isPresent(e1) || !isPresent(e2)) throw new IllegalArgumentException("Elemento non presente nella foresta");
        if (findSet(e1).equals(findSet(e2))) return;
        Node<E> xRoot = currentElements.get(findSet(e1));
        Node<E> yRoot = currentElements.get(findSet(e2));
        if (xRoot.rank > yRoot.rank) {
            yRoot.parent = xRoot;
        } else if (xRoot.rank < yRoot.rank) {
            xRoot.parent = yRoot;
        } else {
            xRoot.parent = yRoot;
            yRoot.rank++;
        }
    }

    @Override
    public Set<E> getCurrentRepresentatives() {
        Set<E> representatives = new HashSet<>();
        for (E e : currentElements.keySet()) {
            representatives.add(findSet(e));
        }
        return representatives;
    }

    @Override
    public Set<E> getCurrentElementsOfSetContaining(E e) {
        if (e == null) throw new NullPointerException("L'elemento è nullo");
        if (!isPresent(e)) throw new IllegalArgumentException("L'elemento non è presente in alcun nodo");
        Set<E> elements = new HashSet<>();
        E rep = findSet(e);
        for (E x : currentElements.keySet()) {
            if (findSet(x).equals(rep)) {
                elements.add(x);
            }
        }
        return elements;
    }

    @Override
    public void clear() {
        currentElements.clear();
    }
}
