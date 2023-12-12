package it.unicam.cs.asdl2324.mp2;

import java.util.*;


/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 * 
 * @author  Luca Tesei (template) **INSERIRE NOME, COGNOME ED EMAIL
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
    // Una lista di archi ordinati per peso crescente
    private List<GraphEdge<L>> sortedEdges;

    /**
     * Costruisce un calcolatore di un albero di copertura minimo che usa
     * l'algoritmo di Kruskal su un grafo non orientato e pesato.
     */
    public KruskalMST() {
        this.disjointSets = new ForestDisjointSets<GraphNode<L>>();
        this.sortedEdges = new ArrayList<GraphEdge<L>>();
    }

    /**
     * Utilizza l'algoritmo goloso di Kruskal per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. L'albero restituito non è radicato, quindi è rappresentato
     * semplicemente con un sottoinsieme degli archi del grafo.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @return l'insieme degli archi del grafo g che costituiscono l'albero di
     *         copertura minimo trovato
     * @throw NullPointerException se il grafo g è null
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    public Set<GraphEdge<L>> computeMSP(Graph<L> g) {
        // Controllare se il grafo è null
        if (g == null) {
            throw new NullPointerException("Grafo nullo");
        }
        // Controllare se il grafo è orientato, non pesato o con pesi negativi
        if (g.isDirected()) {
            throw new IllegalArgumentException("Grafo non valido");
        }
        // Creare un insieme vuoto per contenere gli archi dell'albero di copertura minimo
        Set<GraphEdge<L>> mst = new HashSet<GraphEdge<L>>();
        // Aggiungere tutti i nodi del grafo agli insiemi disgiunti
        for (GraphNode<L> node : g.getNodes()) {
            disjointSets.makeSet(node);
        }
        // Aggiungere tutti gli archi del grafo alla lista di archi ordinati
        for (GraphEdge<L> edge : g.getEdges()) {
            sortedEdges.add(edge);
        }
        // Ordinare la lista di archi per peso crescente
        Collections.sort(sortedEdges, new Comparator<GraphEdge<L>>() {
            @Override
            public int compare(GraphEdge<L> e1, GraphEdge<L> e2) {
                return Double.compare(e1.getWeight(), e2.getWeight());
            }
        });
        // Per ogni arco nella lista ordinata
        for (GraphEdge<L> edge : sortedEdges) {
            // Prendere i nodi sorgente e destinazione dell'arco
            GraphNode<L> source = edge.getNode1();
            GraphNode<L> destination = edge.getNode2();
            // Se i nodi appartengono a insiemi disgiunti diversi
            if (disjointSets.findSet(source) != disjointSets.findSet(destination)) {
                // Aggiungere l'arco all'insieme dell'albero di copertura minimo
                mst.add(edge);
                // Unire i due insiemi disgiunti
                disjointSets.union(source, destination);
            }
        }
        // Restituire l'insieme dell'albero di copertura minimo
        return mst;
    }
}
