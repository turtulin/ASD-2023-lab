package it.unicam.cs.asdl2324.es12;


import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Classe singoletto che implementa l'algoritmo di Prim per trovare un Minimum
 * Spanning Tree di un grafo non orientato, pesato e con pesi non negativi.
 * 
 * L'algoritmo richiede l'uso di una coda di min priorità tra i nodi che può
 * essere realizzata con una semplice ArrayList (non c'è bisogno di ottimizzare
 * le operazioni di inserimento, di estrazione del minimo, o di decremento della
 * priorità).
 * 
 * Si possono usare i colori dei nodi per registrare la scoperta e la visita
 * effettuata dei nodi.
 * 
 * @author @author Template: Luca Tesei, Implementazione: collettiva
 * 
 * @param <L>
 *                tipo delle etichette dei nodi del grafo
 *
 */
public class PrimMST<L> {

    // La coda di priorità che contiene i nodi da visitare
    private PriorityQueue<GraphNode<L>> queue;

    // L'insieme dei nodi già visitati
    private List<GraphNode<L>> visited;

    /*
     * In particolare: si deve usare una coda con priorità che può semplicemente
     * essere realizzata con una List<GraphNode<L>> e si deve mantenere un
     * insieme dei nodi già visitati
     */

    /**
     * Crea un nuovo algoritmo e inizializza la coda di priorità con una coda
     * vuota.
     */
    public PrimMST() {
        queue = new PriorityQueue<>();
        visited = new ArrayList<>();
    }

    /**
     * Utilizza l'algoritmo goloso di Prim per trovare un albero di copertura
     * minimo in un grafo non orientato e pesato, con pesi degli archi non
     * negativi. Dopo l'esecuzione del metodo nei nodi del grafo il campo
     * previous deve contenere un puntatore a un nodo in accordo all'albero di
     * copertura minimo calcolato, la cui radice è il nodo sorgente passato.
     * 
     * @param g
     *              un grafo non orientato, pesato, con pesi non negativi
     * @param s
     *              il nodo del grafo g sorgente, cioè da cui parte il calcolo
     *              dell'albero di copertura minimo. Tale nodo sarà la radice
     *              dell'albero di copertura trovato
     * 
     * @throw NullPointerException se il grafo g o il nodo sorgente s sono nulli
     * @throw IllegalArgumentException se il nodo sorgente s non esiste in g
     * @throw IllegalArgumentException se il grafo g è orientato, non pesato o
     *        con pesi negativi
     */
    /*public void computeMSP(Graph<L> g, GraphNode<L> s) {
        // Controlla se il grafo e il nodo sorgente sono validi
        if (g == null || s == null) {
            throw new NullPointerException("Grafo o nodo sorgente nullo");
        }
        if (!g.containsNode(s)) {
            throw new IllegalArgumentException("Nodo sorgente non esistente nel grafo");
        }
        if (g.isDirected()) {
            throw new IllegalArgumentException("Grafo orientato");
        }
        if (!g.isWeighted()) {
            throw new IllegalArgumentException("Grafo non pesato");
        }
        if (g.hasNegativeWeights()) {
            throw new IllegalArgumentException("Grafo con pesi negativi");
        }
        // Aggiunge il nodo sorgente alla coda di priorità
        queue.add(s);
        // Finché la coda non è vuota
        while (!queue.isEmpty()) {
            // Estrae il nodo con la priorità minima (cioè il peso minimo)
            GraphNode<L> u = queue.poll();
            // Se il nodo non è già stato visitato
            if (!visited.contains(u)) {
                // Lo aggiunge all'insieme dei visitati
                visited.add(u);
                // Per ogni arco uscente dal nodo
                for (GraphEdge<L> e : g.getOutgoingEdgesOf(u)) {
                    // Ottiene il nodo adiacente
                    GraphNode<L> v = e.getNode2();
                    // Se il nodo adiacente non è già stato visitato
                    if (!visited.contains(v)) {
                        // Imposta il peso del nodo adiacente come il peso dell'arco
                        v.setWeight(e.getWeight());
                        // Imposta il campo previous del nodo adiacente come il nodo corrente
                        v.setPrevious(u);
                        // Aggiunge il nodo adiacente alla coda di priorità
                        queue.add(v);
                    }
                }
            }
        }
    }
    */

}
