package it.unicam.cs.asdl2324.mp2;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

/**
 * 
 * Classe singoletto che implementa l'algoritmo di Kruskal per trovare un
 * Minimum Spanning Tree di un grafo non orientato, pesato e con pesi non
 * negativi. L'algoritmo implementato si avvale della classe
 * {@code ForestDisjointSets<GraphNode<L>>} per gestire una collezione di
 * insiemi disgiunti di nodi del grafo.
 *
 * La scelta di aggiungere metodi e classi privati alla fine della classe è motivata
 * dalla volontà di migliorare la chiarezza, la modularità e la leggibilità del codice,
 * soprattutto del metodo {@code computeMSP}.
 *
 * La presenza di questi metodi consente di suddividere logicamente il lavoro svolto
 * nel metodo principale, {@code computeMSP}, in passaggi più gestibili e comprensibili.
 * Ad esempio, {@code createEdgeQueue} fornisce una visione chiara della coda prioritaria
 * degli archi, mentre {@code createDisjointSets} inizializza la struttura dati per gli insiemi
 * disgiunti. Ciò rende il metodo principale più snello, concentrato sulla logica
 * dell'algoritmo di Kruskal.
 *
 * Inoltre, l'introduzione del comparatore {@code GraphEdgeComparator} separato consente
 * di isolare la logica di confronto degli archi, migliorando la modularità e facilitando
 * la manutenzione. Il suo utilizzo in {@code createEdgeQueue} rende il codice più chiaro e
 * autoesplicativo.
 *
 * 
 * @author  Luca Tesei (template)
 *          MARTA MUSSO marta.musso@studenti.unicam.it (implementazione)
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
        this.disjointSets = new ForestDisjointSets<>();
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
        ArrayList<GraphEdge<L>> edgeQueue = createEdgeQueue(g);
        disjointSets = createDisjointSets(g);
        Set<GraphEdge<L>> mst = new HashSet<>();
        // Finché la coda non è vuota, estraggo l'arco con peso minore
        while (!edgeQueue.isEmpty()) {
            GraphEdge<L> edge = edgeQueue.remove(0);
            GraphNode<L> node1 = edge.getNode1();
            GraphNode<L> node2 = edge.getNode2();
            // Se i due nodi non sono nella stessa componente connessa, aggiungo l'arco all'albero e unisco i due insiemi
            if (!areConnected(disjointSets, node1, node2)) {
                mst.add(edge);
                disjointSets.union(node1, node2);
            }
        }
        return mst;
    }

    /**
     * Crea una coda prioritaria degli archi del grafo ordinati per peso.
     *
     * @param graph Il grafo di input.
     * @return Una coda prioritaria degli archi del grafo.
     */
    private ArrayList<GraphEdge<L>> createEdgeQueue(Graph<L> graph) {
        // Creazione della coda prioritaria senza comparatore
        ArrayList<GraphEdge<L>> edgeQueue = new ArrayList<>();
        edgeQueue.addAll(graph.getEdges());
        // Ordinamento della coda prioritaria usando il selection sort
        int n = edgeQueue.size();
        for (int i = 0; i < n - 1; i++) {
            // Trovo l'arco con il peso minimo tra i e n - 1
            int minIndex = i;
            GraphEdge<L> minEdge = edgeQueue.get(i);
            for (int j = i + 1; j < n; j++) {
                GraphEdge<L> currentEdge = edgeQueue.get(j);
                if (currentEdge.getWeight() < minEdge.getWeight()) {
                    minIndex = j;
                    minEdge = currentEdge;
                }
            }
            // Scambio l'arco con il peso minimo con l'arco in posizione i
            edgeQueue.set(minIndex, edgeQueue.get(i));
            edgeQueue.set(i, minEdge);
        }
        return edgeQueue;
    }

    /**
     * Crea la struttura dati degli insiemi disgiunti per i nodi del grafo.
     *
     * @param graph Il grafo di input.
     * @return La struttura dati degli insiemi disgiunti.
     */
    private ForestDisjointSets<GraphNode<L>> createDisjointSets(Graph<L> graph) {
        ForestDisjointSets<GraphNode<L>> disjointSets = new ForestDisjointSets<>();
        // Inizializzazione degli insiemi disgiunti con i nodi del grafo
        for (GraphNode<L> node : graph.getNodes()) {
            if (!disjointSets.isPresent(node)) {
                disjointSets.makeSet(node);
            }
        }
        return disjointSets;
    }

    /**
     * Verifica se due nodi sono nella stessa componente connessa.
     *
     * @param disjointSets La struttura dati degli insiemi disgiunti.
     * @param node1        Il primo nodo del grafo.
     * @param node2        Il secondo nodo del grafo.
     * @return True se i nodi sono nella stessa componente connessa, altrimenti false.
     */
    private boolean areConnected(ForestDisjointSets<GraphNode<L>> disjointSets, GraphNode<L> node1, GraphNode<L> node2) {
        return disjointSets.findSet(node1).equals(disjointSets.findSet(node2));
    }

    /**
     * Verifica se il grafo contiene archi con pesi negativi.
     *
     * @param graph Il grafo di input.
     * @return True se il grafo contiene archi con pesi negativi, altrimenti false.
     */
    private boolean hasNegativeWeights(Graph<L> graph) {
        for (GraphEdge<L> edge : graph.getEdges()) {
            if (edge.getWeight() < 0) return true;
        }
        return false;
    }
}