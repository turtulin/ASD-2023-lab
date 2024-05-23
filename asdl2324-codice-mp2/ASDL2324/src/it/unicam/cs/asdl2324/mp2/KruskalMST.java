package it.unicam.cs.asdl2324.mp2;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class KruskalMST<L> {

    private ForestDisjointSets<GraphNode<L>> disjointSets;

    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<>();
    }

    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        if (g == null) throw new NullPointerException("Il grafo è null");
        if (hasNegativeWeights(g)) throw new IllegalArgumentException("Il grafo ha pesi negativi");
        if (g.isDirected()) throw new IllegalArgumentException("Il grafo è orientato");
        ArrayList<GraphEdge<L>> edgeQueue = createEdgeQueue(g);
        disjointSets = createDisjointSets(g);
        Set<GraphEdge<L>> mst = new HashSet<>();
        while (!edgeQueue.isEmpty()) {
            GraphEdge<L> edge = edgeQueue.remove(0);
            GraphNode<L> node1 = edge.getNode1();
            GraphNode<L> node2 = edge.getNode2();
            if (!areConnected(disjointSets, node1, node2)) {
                mst.add(edge);
                disjointSets.union(node1, node2);
            }
        }
        return mst;
    }

    private ArrayList<GraphEdge<L>> createEdgeQueue(Graph<L> graph) {
        ArrayList<GraphEdge<L>> edgeQueue = new ArrayList<>();
        edgeQueue.addAll(graph.getEdges());
        int n = edgeQueue.size();
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(edgeQueue, n, i);
        }
        for (int i = n - 1; i > 0; i--) {
            GraphEdge<L> temp = edgeQueue.get(0);
            edgeQueue.set(0, edgeQueue.get(i));
            edgeQueue.set(i, temp);
            heapify(edgeQueue, i, 0);
        }
        return edgeQueue;
    }

    private void heapify(ArrayList<GraphEdge<L>> edgeQueue, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && edgeQueue.get(left).getWeight() > edgeQueue.get(largest).getWeight()) {
            largest = left;
        }
        if (right < n && edgeQueue.get(right).getWeight() > edgeQueue.get(largest).getWeight()) {
            largest = right;
        }
        if (largest != i) {
            GraphEdge<L> swap = edgeQueue.get(i);
            edgeQueue.set(i, edgeQueue.get(largest));
            edgeQueue.set(largest, swap);
            heapify(edgeQueue, n, largest);
        }
    }

    private boolean hasNegativeWeights(Graph<L> graph) {
        for (GraphEdge<L> edge : graph.getEdges()) {
            if (edge.getWeight() < 0) return true;
        }
        return false;
    }

    private ForestDisjointSets<GraphNode<L>> createDisjointSets(Graph<L> graph) {
        ForestDisjointSets<GraphNode<L>> disjointSets = new ForestDisjointSets<>();
        for (GraphNode<L> node : graph.getNodes()) {
            if (!disjointSets.isPresent(node)) {
                disjointSets.makeSet(node);
            }
        }
        return disjointSets;
    }

    private boolean areConnected(ForestDisjointSets<GraphNode<L>> disjointSets, GraphNode<L> node1, GraphNode<L> node2) {
        return disjointSets.findSet(node1).equals(disjointSets.findSet(node2));
    }
}
