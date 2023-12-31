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
        if (g == null) throw new NullPointerException("Il grafo è null");
        if (hasNegativeWeights(g)) throw new IllegalArgumentException("Il grafo ha pesi negativi");
        if (g.isDirected()) throw new IllegalArgumentException("Il grafo è orientato");
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
        // Ordinamento della coda prioritaria usando l'heap sort
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

    /**
     * Metodo ausiliario per mantenere la proprietà di heap.
     * @param edgeQueue la coda prioritaria degli archi del grafo
     * @param n la dimensione dell'heap
     * @param i l'indice del nodo corrente
     */
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
}