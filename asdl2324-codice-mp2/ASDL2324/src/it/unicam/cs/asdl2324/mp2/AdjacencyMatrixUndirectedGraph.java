package it.unicam.cs.asdl2324.mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {

    protected Map<GraphNode<L>, Integer> nodesIndex;

    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<>();
        this.nodesIndex = new HashMap<>();
    }

    @Override
    public int nodeCount() {
        return nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        int count = 0;
        for (ArrayList<GraphEdge<L>> row : matrix) {
            for (GraphEdge<L> edge : row) {
                if (edge != null) count++;
            }
        }
        return count / 2;
    }

    @Override
    public void clear() {
        nodesIndex.clear();
        matrix.clear();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (nodesIndex.containsKey(node)) return false;
        nodesIndex.put(node, nodeCount());
        ArrayList<GraphEdge<L>> newRow = new ArrayList<>();
        for (int i = 0; i < nodeCount(); i++) {
            newRow.add(null);
        }
        matrix.add(newRow);
        for (int i = 0; i < nodeCount() - 1; i++) {
            matrix.get(i).add(null);
        }
        return true;
    }

    @Override
    public boolean addNode(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        return addNode(new GraphNode<>(label));
    }

    @Override
    public void removeNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        int index = nodesIndex.get(node);
        nodesIndex.remove(node);
        matrix.remove(index);
        for (ArrayList<GraphEdge<L>> row : matrix) {
            row.remove(index);
        }
        for (GraphNode<L> n : nodesIndex.keySet()) {
            int i = nodesIndex.get(n);
            if (i > index) nodesIndex.put(n, i - 1);
        }
    }

    @Override
    public void removeNode(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        removeNode(new GraphNode<>(label));
    }

    @Override
    public void removeNode(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        removeNode(getNode(i));
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (nodesIndex.containsKey(node)) return node;
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        for (int i = 0; i < nodeCount(); i++) {
            if (getNode(i).getLabel().equals(label)) {
                return getNode(i);
            }
        }
        return null;
    }

    @Override
    public GraphNode<L> getNode(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        for (GraphNode<L> node : nodesIndex.keySet()) {
            if (nodesIndex.get(node) == i) return node;
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        return nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        return getNodeIndexOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null) throw new NullPointerException("Arco null");
        if (edge.isDirected() != isDirected()) throw new IllegalArgumentException("Arco non compatibile con il grafo");
        int[] indices = getIndicesAndCheckNodes(edge);
        int i = indices[0];
        int j = indices[1];
        if (matrix.get(i).get(j) != null && matrix.get(i).get(j).equals(edge)) return false;
        matrix.get(i).set(j, edge);
        matrix.get(j).set(i, edge);
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        return addEdge(new GraphEdge<>(node1, node2, isDirected()));
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        return addEdge(new GraphEdge<>(node1, node2, isDirected(), weight));
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        return addEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        return addWeightedEdge(new GraphNode<>(label1), new GraphNode<>(label2), weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return addEdge(getNode(i), getNode(j));
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return addWeightedEdge(getNode(i), getNode(j), weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        if (edge == null) throw new NullPointerException("Arco null");
        int[] indices = getIndicesAndCheckNodes(edge);
        if (matrix.get(indices[0]).get(indices[1]) != null) {
            matrix.get(indices[0]).set(indices[1], null);
            if (!isDirected()) {
                matrix.get(indices[1]).set(indices[0], null);
            }
        } else throw new IllegalArgumentException("Arco inesistente");
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        removeEdge(new GraphEdge<>(node1, node2, isDirected()));
    }

    @Override
    public void removeEdge(L label1, L label2) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        removeEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public void removeEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        removeEdge(getNode(i), getNode(j));
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        if (edge == null) throw new NullPointerException("Arco null");
        int[] indices = getIndicesAndCheckNodes(edge);
        return matrix.get(indices[0]).get(indices[1]);
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) throw new IllegalArgumentException("Nodo inesistente");
        return getEdge(new GraphEdge<>(node1, node2, isDirected()));
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        return getEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return matrix.get(i).get(j);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        return getAdjNodes(nodesIndex.get(node));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        return getAdjacentNodesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return getAdjNodes(i);
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        Set<GraphEdge<L>> edges = new HashSet<>();
        Set<GraphNode<L>> adjacentNodes = getAdjNodes(nodesIndex.get(node));
        for (GraphNode<L> adjacentNode : adjacentNodes) {
            edges.add(getEdge(node, adjacentNode));
        }
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        return getEdgesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return getEdgesOf(getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> edges = new HashSet<>();
        for (GraphNode<L> node : getNodes()) {
            Set<GraphNode<L>> adjacentNodes = getAdjNodes(nodesIndex.get(node));
            for (GraphNode<L> adjacentNode : adjacentNodes) {
                edges.add(getEdge(node, adjacentNode));
            }
        }
        return edges;
    }

    private int[] getIndicesAndCheckNodes(GraphEdge<L> edge) {
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) throw new IllegalArgumentException("Nodo inesistente");
        return new int[]{nodesIndex.get(node1), nodesIndex.get(node2)};
    }

    private Set<GraphNode<L>> getAdjNodes(int i) {
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();
        for (int j = 0; j < nodeCount(); j++) {
            if (matrix.get(i).get(j) != null) {
                adjacentNodes.add(getNode(j));
            }
        }
        return adjacentNodes;
    }
}
