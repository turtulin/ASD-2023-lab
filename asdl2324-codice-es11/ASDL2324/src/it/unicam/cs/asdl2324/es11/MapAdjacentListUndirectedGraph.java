package it.unicam.cs.asdl2324.es11;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class MapAdjacentListUndirectedGraph<L> extends Graph<L> {

    private final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    public MapAdjacentListUndirectedGraph() {
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
        return this.adjacentLists.size();
    }

    @Override
    public int edgeCount() {
        int count = 0;
        for (Set<GraphEdge<L>> edges : this.adjacentLists.values()) {
            count += edges.size();
        }
        return count / 2;
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return this.adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di aggiungere un nodo null");
        if (this.containsNode(node)) return false;
        this.adjacentLists.put(node, new HashSet<GraphEdge<L>>());
        return true;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException("Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di verificare la presenza di un nodo null");
        return this.adjacentLists.containsKey(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        if (label == null) throw new NullPointerException("Tentativo di ricercare un nodo con etichetta null");
        for (GraphNode<L> node : this.adjacentLists.keySet()) {
            if (node.getLabel().equals(label)) return node;
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null) throw new NullPointerException("Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException("Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException("Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (!this.containsNode(node)) throw new IllegalArgumentException("");
        if (node == null) throw new NullPointerException("Tentativo di ottenere i nodi adiacenti a un nodo null");
        if (!this.containsNode(node)) return null;
        Set<GraphNode<L>> adjNodes = new HashSet<GraphNode<L>>();
        for (GraphEdge<L> edge : this.adjacentLists.get(node)) {
            adjNodes.add(edge.getNode2());
        }
        return adjNodes;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di ottenere i nodi predecessori di un nodo null");
        return this.getAdjacentNodesOf(node);
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();
        for (GraphNode<L> node : this.adjacentLists.keySet()) {
            edges.addAll(this.adjacentLists.get(node));
        }
        return edges;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null) throw new NullPointerException("Tentativo di aggiungere un arco null");
        if ((!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) ||
                (!this.isDirected() && edge.isDirected() || this.isDirected() && !edge.isDirected()))
            throw new IllegalArgumentException("");
        if (this.containsEdge(edge)) return false;
        if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) return false;
        this.adjacentLists.get(edge.getNode1()).add(edge);
        this.adjacentLists.get(edge.getNode2()).add(new GraphEdge<L>(edge.getNode2(), edge.getNode1(), false));
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException("Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) throw new IllegalArgumentException("");
        if (edge == null) throw new NullPointerException("Tentativo di verificare la presenza di un arco null");
        if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) return false;
        return this.adjacentLists.get(edge.getNode1()).contains(edge);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di ottenere gli archi di un nodo null");
        if (!this.containsNode(node)) throw new IllegalArgumentException();
        if (this.isDirected()) throw new UnsupportedOperationException();
        if (!this.containsNode(node)) return null;
        return this.adjacentLists.get(node);
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Archi entranti non significativi in un grafo non orientato");
    }
}
