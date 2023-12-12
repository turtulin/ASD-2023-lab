package it.unicam.cs.asdl2324.es11;
/**
 * Classe singoletto che fornisce lo schema generico di visita Depth-First di
 * un grafo rappresentato da un oggetto di tipo Graph<L>.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L> le etichette dei nodi del grafo
 */
public class DFSVisitor<L> {

    // Variabile "globale" per far andare avanti il tempo durante la DFS e
    // assegnare i relativi tempi di scoperta e di uscita dei nodi
    // E' protected per permettere il test JUnit
    protected int time;

    /**
     * Esegue la visita in profondità di un certo grafo. Setta i valori seguenti
     * valori associati ai nodi: tempo di scoperta, tempo di fine visita,
     * predecessore. Ogni volta che un nodo viene visitato viene eseguito il
     * metodo visitNode sul nodo. In questa classe il metodo non fa niente,
     * basta creare una sottoclasse e ridefinire il metodo per eseguire azioni
     * particolari.
     * 
     * @param g
     *              il grafo da visitare.
     * @throws NullPointerException
     *                                  se il grafo passato è null
     */
    public void DFSVisit(Graph<L> g) {
        // Controlla se il grafo è nullo
        if (g == null)
            throw new NullPointerException("Tentativo di visitare un grafo null");
        // Inizializza il tempo a zero
        this.time = 0;
        // Per ogni nodo del grafo, imposta il predecessore a null e il colore a bianco
        for (GraphNode<L> node : g.getNodes()) {
            node.setPrevious(null);
            node.setColor(0);
        }
        // Per ogni nodo del grafo, se il nodo è bianco, chiama il metodo recDFS sul nodo
        for (GraphNode<L> node : g.getNodes()) {
            if (node.getColor() == 0) {
                recDFS(g, node);
            }
        }

    }

    /*
     * Esegue la DFS ricorsivamente sul nodo passato.
     * 
     * @param g il grafo
     * 
     * @param u il nodo su cui parte la DFS
     */
    protected void recDFS(Graph<L> g, GraphNode<L> u) {
        // Incrementa il tempo e imposta il tempo di scoperta del nodo u al valore corrente del tempo
        this.time++;
        u.setEnteringTime(this.time);
        // Cambia il colore del nodo u da bianco a grigio
        u.setColor(1);
        // Per ogni nodo v adiacente a u, se il nodo v è bianco, imposta il predecessore di v a u e chiama ricorsivamente il metodo recDFS su v
        for (GraphNode<L> v : g.getAdjacentNodesOf(u)) {
            if (v.getColor() == 0) {
                v.setPrevious(u);
                recDFS(g, v);
            }
        }
        // Incrementa di nuovo il tempo e imposta il tempo di fine visita del nodo u al valore corrente del tempo
        this.time++;
        u.setExitingTime(this.time);
        // Cambia il colore del nodo u da grigio a nero
        u.setColor(2);
        // Chiamare il metodo visitNode su u
        visitNode(u);
    }

    /**
     * Questo metodo, che di default non fa niente, viene chiamato su tutti i
     * nodi visitati durante la DFS nel momento in cui il colore passa da grigio
     * a nero. Ridefinire il metodo in una sottoclasse per effettuare azioni
     * specifiche.
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
