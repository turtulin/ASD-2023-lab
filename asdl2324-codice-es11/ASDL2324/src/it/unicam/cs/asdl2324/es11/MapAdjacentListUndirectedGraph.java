/**
 * 
 */
package it.unicam.cs.asdl2324.es11;

import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/**
 * Implementazione della classe astratta {@code Graph<L>} che realizza un grafo
 * non orientato. Per la rappresentazione viene usata una variante della
 * rappresentazione a liste di adiacenza. A differenza della rappresentazione
 * standard si usano strutture dati più efficienti per quanto riguarda la
 * complessità in tempo della ricerca se un nodo è presente (pseudocostante, con
 * tabella hash) e se un arco è presente (pseudocostante, con tabella hash). Lo
 * spazio occupato per la rappresentazione risultà tuttavia più grande di quello
 * che servirebbe con la rappresentazione standard.
 * 
 * Le liste di adiacenza sono rappresentate con una mappa (implementata con
 * tabelle hash) che associa ad ogni nodo del grafo i nodi adiacenti. In questo
 * modo il dominio delle chiavi della mappa è il set dei nodi, su cui è
 * possibile chiamare il metodo contains per testare la presenza o meno di un
 * nodo. Ad ogni chiave della mappa, cioè ad ogni nodo del grafo, non è
 * associata una lista concatenata dei nodi collegati, ma un set di oggetti
 * della classe GraphEdge<L> che rappresentano gli archi connessi al nodo: in
 * questo modo la rappresentazione riesce a contenere anche l'eventuale peso
 * dell'arco (memorizzato nell'oggetto della classe GraphEdge<L>). Per
 * controllare se un arco è presente basta richiamare il metodo contains in
 * questo set. I test di presenza si basano sui metodi equals ridefiniti per
 * nodi e archi nelle classi GraphNode<L> e GraphEdge<L>.
 * 
 * Questa classe non supporta le operazioni di rimozione di nodi e archi e le
 * operazioni indicizzate di ricerca di nodi e archi.
 * 
 * @author Template: Luca Tesei, Implementazione: collettiva
 *
 * @param <L>
 *                etichette dei nodi del grafo
 */
public class MapAdjacentListUndirectedGraph<L> extends Graph<L> {

    /*
     * Le liste di adiacenza sono rappresentate con una mappa. Ogni nodo viene
     * associato con l'insieme degli archi uscenti. Nel caso in cui un nodo non
     * abbia archi uscenti è associato con un insieme vuoto.
     */
    private final Map<GraphNode<L>, Set<GraphEdge<L>>> adjacentLists;

    /**
     * Crea un grafo vuoto.
     */
    public MapAdjacentListUndirectedGraph() {
        // Inizializza la mappa con la mappa vuota
        this.adjacentLists = new HashMap<GraphNode<L>, Set<GraphEdge<L>>>();
    }

    @Override
    public int nodeCount() {
        // Il numero di nodi è uguale al numero di chiavi nella mappa
        return this.adjacentLists.size();
    }

    @Override
    public int edgeCount() {
        // Il numero di archi è uguale alla somma delle dimensioni dei set associati ai nodi
        int count = 0;
        for (Set<GraphEdge<L>> edges : this.adjacentLists.values()) {
            count += edges.size();
        }
        // Poiché il grafo è non orientato, ogni arco è contato due volte, quindi bisogna dividere per due
        return count / 2;
    }

    @Override
    public void clear() {
        this.adjacentLists.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa grafi non orientati
        return false;
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        // Il set dei nodi è uguale al set delle chiavi della mappa
        return this.adjacentLists.keySet();
    }

    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di aggiungere un nodo null");
        // Se il nodo è già presente, non fare nulla e restituire false
        if (this.containsNode(node)) return false;
        // Altrimenti, aggiungi il nodo alla mappa con un set vuoto di archi e restituisci true
        this.adjacentLists.put(node, new HashSet<GraphEdge<L>>());
        return true;
    }

    @Override
    public boolean removeNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di rimuovere un nodo null");
        throw new UnsupportedOperationException("Rimozione dei nodi non supportata");
    }

    @Override
    public boolean containsNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di verificare la presenza di un nodo null");
        // Il nodo è presente se è una chiave della mappa
        return this.adjacentLists.containsKey(node);
    }

    @Override
    public GraphNode<L> getNodeOf(L label) {
        if (label == null) throw new NullPointerException("Tentativo di ricercare un nodo con etichetta null");
        // Cerca il nodo con l'etichetta data tra le chiavi della mappa
        for (GraphNode<L> node : this.adjacentLists.keySet()) {
            if (node.getLabel().equals(label)) return node;
        }
        // Se non lo trova, restituisci null
        return null;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null) throw new NullPointerException("Tentativo di ricercare un nodo con etichetta null");
        throw new UnsupportedOperationException("Ricerca dei nodi con indice non supportata");
    }

    @Override
    public GraphNode<L> getNodeAtIndex(int i) {
        throw new UnsupportedOperationException("Ricerca dei nodi con indice non supportata");
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (!this.containsNode(node)) throw new IllegalArgumentException("");
        if (node == null) throw new NullPointerException("Tentativo di ottenere i nodi adiacenti a un nodo null");
        // Se il nodo non è presente, restituisci null
        if (!this.containsNode(node)) return null;
        // Altrimenti, crea un set vuoto di nodi adiacenti
        Set<GraphNode<L>> adjNodes = new HashSet<GraphNode<L>>();
        // Per ogni arco uscente dal nodo, aggiungi il nodo opposto al set
        for (GraphEdge<L> edge : this.adjacentLists.get(node)) {
            adjNodes.add(edge.getNode2());
        }
        // Restituisci il set dei nodi adiacenti
        return adjNodes;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di ottenere i nodi predecessori di un nodo null");
        // Poiché il grafo è non orientato, i nodi predecessori sono gli stessi dei nodi adiacenti
        return this.getAdjacentNodesOf(node);
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // Crea un set vuoto di archi
        Set<GraphEdge<L>> edges = new HashSet<GraphEdge<L>>();
        // Per ogni nodo del grafo, aggiungi al set tutti gli archi uscenti
        for (GraphNode<L> node : this.adjacentLists.keySet()) {
            edges.addAll(this.adjacentLists.get(node));
        }
        // Restituisci il set degli archi
        return edges;
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null) throw new NullPointerException("Tentativo di aggiungere un arco null");
        if ((!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) ||
                (!this.isDirected() && edge.isDirected() || this.isDirected() && !edge.isDirected()))
            throw new IllegalArgumentException("");
        // Se l'arco è già presente, non fare nulla e restituisci false
        if (this.containsEdge(edge)) return false;
        // Se uno dei due nodi dell'arco non è presente, non fare nulla e restituisci false
        if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) return false;
        // Altrimenti, aggiungi l'arco al set degli archi uscenti di entrambi i nodi e restituisci true
        this.adjacentLists.get(edge.getNode1()).add(edge);
        this.adjacentLists.get(edge.getNode2()).add(new GraphEdge<L>(edge.getNode2(), edge.getNode1(), false));
        return true;
    }

    @Override
    public boolean removeEdge(GraphEdge<L> edge) {
        throw new UnsupportedOperationException("Rimozione degli archi non supportata");
    }

    @Override
    public boolean containsEdge(GraphEdge<L> edge) {
        if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) throw new IllegalArgumentException("");
        if (edge == null) throw new NullPointerException("Tentativo di verificare la presenza di un arco null");
        // Se uno dei due nodi dell'arco non è presente, restituisci false
        if (!this.containsNode(edge.getNode1()) || !this.containsNode(edge.getNode2())) return false;
        // Altrimenti, controlla se l'arco è nel set degli archi uscenti del primo nodo
        return this.adjacentLists.get(edge.getNode1()).contains(edge);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Tentativo di ottenere gli archi di un nodo null");
        if (!this.containsNode(node)) throw new IllegalArgumentException();
        if (this.isDirected()) throw new UnsupportedOperationException();
        // Se il nodo non è presente, restituisci null
        if (!this.containsNode(node)) return null;
        // Altrimenti, restituisci il set degli archi uscenti dal nodo
        return this.adjacentLists.get(node);
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Archi entranti non significativi in un grafo non orientato");
    }
}
