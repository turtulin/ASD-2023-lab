package it.unicam.cs.asdl2324.mp2;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author  Luca Tesei (template)
 *          MARTA MUSSO marta.musso@studenti.unicam.it
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class KruskalMST<L> {

    /*
     * Struttura dati per rappresentare gli insiemi disgiunti utilizzata
     * dall'algoritmo di Kruskal.
     */
    private ForestDisjointSets<GraphNode<L>> disjointSets;

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     *
     * @param g un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     * copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     * con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        if (g == null) throw new NullPointerException("Il grafo non può essere null");
        if (g.isDirected()) throw new IllegalArgumentException("Il grafo deve essere non orientato");
        if (hasNegativeWeights(g)) throw new IllegalArgumentException("Il grafo deve avere pesi positivi");
        // Ordina gli archi in ordine crescente di peso
        List<GraphEdge<L>> sortedEdges = new ArrayList<>(g.getEdges());
        sortedEdges.sort(Comparator.comparingDouble(GraphEdge::getWeight));
        // Crea un disjoint set data structure
        ForestDisjointSets<GraphNode<L>> disjointSets = new ForestDisjointSets<GraphNode<L>>();
        for (GraphNode<L> node : g.getNodes()) {
            if (!disjointSets.isPresent(node)) {
                disjointSets.makeSet(node);
            }
        }
        Set<GraphEdge<L>> mst = new HashSet<>();
        for (GraphEdge<L> edge : sortedEdges) {
            GraphNode<L> u = edge.getNode1();
            GraphNode<L> v = edge.getNode2();
            // Controlla se i due nodi appartengono alla stessa componente connessa
            if (!disjointSets.findSet(u).equals(disjointSets.findSet(v))) {
                mst.add(edge);
                disjointSets.union(u, v);
            }
        }

        return mst;
    }

    private boolean hasNegativeWeights(Graph<L> g) {
        for (GraphEdge<L> edge : g.getEdges()) {
            if (edge.getWeight() < 0) return true;
        }
        return false;
    }
}