package it.unicam.cs.asdl2324.es11;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class BFSVisitor<L> {
    public void BFSVisit(Graph<L> g, GraphNode<L> source) {
        if (g == null) throw new NullPointerException("Tentativo di visitare un grafo null");
        if (source == null) throw new NullPointerException("Tentativo di visitare a partire da un nodo null");
        if (!g.containsNode(source)) throw new IllegalArgumentException("Tentativo di visitare a partire da un nodo non appartenente al grafo");
        Queue<GraphNode<L>> queue = new LinkedList<GraphNode<L>>();
        queue.add(source);
        source.setIntegerDistance(0);
        Set<GraphNode<L>> visited = new HashSet<GraphNode<L>>();
        visited.add(source);
        while (!queue.isEmpty()) {
            GraphNode<L> u = queue.poll();
            for (GraphNode<L> v : g.getAdjacentNodesOf(u)) {
                if (!visited.contains(v)) {
                    queue.add(v);
                    v.setIntegerDistance(u.getIntegerDistance() + 1);
                    v.setPrevious(u);
                    visited.add(v);
                }
            }
            visitNode(u);
        }
    }

    public void visitNode(GraphNode<L> n) {
    }

}
