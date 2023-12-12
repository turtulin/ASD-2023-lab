package it.unicam.cs.asdl2324.es11;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 * Classe singoletto che fornisce lo schema generico di visita Breadth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class BFSVisitor<L> {

    /**
     * Esegue la visita in ampiezza di un certo grafo a partire da un nodo
     * sorgente. Setta i valori seguenti valori associati ai nodi: distanza
     * intera, predecessore. La distanza indica il numero minimo di archi che si
     * devono percorrere dal nodo sorgente per raggiungere il nodo e il
     * predecessore rappresenta il padre del nodo in un albero di copertura del
     * grafo. Ogni volta che un nodo viene visitato viene eseguito il metodo
     * visitNode sul nodo. In questa classe il metodo non fa niente, basta
     * creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *                   il grafo da visitare.
     * @param source
     *                   il nodo sorgente.
     * @throws NullPointerException
     *                                      se almeno un valore passato è null
     * @throws IllegalArgumentException
     *                                      se il nodo sorgente non appartiene
     *                                      al grafo dato
     */
    public void BFSVisit(Graph<L> g, GraphNode<L> source) {
        // Controlla se i parametri sono nulli o non validi
        if (g == null) throw new NullPointerException("Tentativo di visitare un grafo null");
        if (source == null) throw new NullPointerException("Tentativo di visitare a partire da un nodo null");
        if (!g.containsNode(source)) throw new IllegalArgumentException("Tentativo di visitare a partire da un nodo non appartenente al grafo");
        // Crea una coda di nodi da visitare e inserisci il nodo sorgente
        Queue<GraphNode<L>> queue = new LinkedList<GraphNode<L>>();
        queue.add(source);
        // Inizializza la distanza del nodo sorgente a zero e il suo predecessore a null
        source.setIntegerDistance(0);
        source.setPrevious(null);
        // Crea un set di nodi visitati e inserisci il nodo sorgente
        Set<GraphNode<L>> visited = new HashSet<GraphNode<L>>();
        visited.add(source);
        // Finché la coda non è vuota, estrai il primo nodo e chiamalo u
        while (!queue.isEmpty()) {
            GraphNode<L> u = queue.poll();
            // Per ogni nodo v adiacente a u, controlla se è stato già visitato
            for (GraphNode<L> v : g.getAdjacentNodesOf(u)) {
                if (!visited.contains(v)) {
                    // Se non è stato visitato, inseriscilo nella coda, aggiorna la sua distanza a quella di u più uno, il suo predecessore a u e aggiungilo al set dei visitati
                    queue.add(v);
                    v.setIntegerDistance(u.getIntegerDistance() + 1);
                    v.setPrevious(u);
                    visited.add(v);
                }
            }
            // Chiamare il metodo visitNode su u
            visitNode(u);
        }
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la BFS quando i nodi passano da grigio a nero.
     * Ridefinire il metodo in una sottoclasse per effettuare azioni specifiche.
     * 
     * @param n
     *              il nodo visitato
     */
    public void visitNode(GraphNode<L> n) {
        /*
         * In questa classe questo metodo non fa niente. Esso può essere
         * ridefinito in una sottoclasse per fare azioni particolari.
         */
    }

}
