/**
 * 
 */
package it.unicam.cs.asdl2324.mp2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * Classe che implementa un grafo non orientato tramite matrice di adiacenza.
 * Non sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 * 
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 * 
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato in fase di inserimento. Il dominio
 * della mappa rappresenta quindi l'insieme dei nodi.
 *
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco. Un oggetto uguale (secondo equals) e
 * con lo stesso peso (se gli archi sono pesati) deve essere presente nella
 * posizione j, i della matrice.
 * 
 * Questa classe non supporta i metodi di cancellazione di nodi e archi, ma
 * supporta tutti i metodi che usano indici, utilizzando l'indice assegnato a
 * ogni nodo in fase di inserimento.
 *
 *
 * I due metodi privati aggiunti alla fine della classe,{@code getIndicesAndCheckNodes} e {@code getAdjNodes},
 * sono stati introdotti per migliorare l'efficienza, promuovere la modularità del codice e
 * ridurre la duplicazione di codice.
 * Il metodo {@code getIndicesAndCheckNodes} ottiene gli indici dei nodi associati a un arco e
 * verifica la loro presenza nel grafo, facilitando la gestione degli indici nella matrice di adiacenza.
 * Il metodo {@code getAdjNodes} restituisce l'insieme dei nodi adiacenti a un nodo di indice specificato,
 * migliorando l'organizzazione del codice e facilitando l'implementazione di operazioni che coinvolgono
 * i nodi adiacenti.
 * L'aggiunta di questi metodi privati contribuisce alla chiarezza del codice, alla sua manutenibilità e
 * alla facilità di comprensione delle operazioni fondamentali del grafo.
 *
 * 
 * @author  Luca Tesei (template)
 *          MARTA MUSSO marta.musso@studenti.unicam.it (implementazione)
 */
public class AdjacencyMatrixUndirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;

    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixUndirectedGraph() {
        this.matrix = new ArrayList<>();
        this.nodesIndex = new HashMap<>();
    }

    @Override
    public int nodeCount() {
        return nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        // Il numero di archi è uguale alla metà del numero di elementi non null nella matrice di adiacenza
        int count = 0;
        for (ArrayList<GraphEdge<L>> row : matrix) {
            for (GraphEdge<L> edge : row) {
                if (edge != null) count++;
            }
        }
        return count / 2;
    }

    @Override
    public void clear() {
        nodesIndex.clear();
        matrix.clear();
    }

    @Override
    public boolean isDirected() {
        return false;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (nodesIndex.containsKey(node)) return false;
        nodesIndex.put(node, nodeCount());
        ArrayList<GraphEdge<L>> newRow = new ArrayList<>();
        // Aggiungi un elemento nullo per ogni iterazione del ciclo for
        for (int i = 0; i < nodeCount(); i++) {
            newRow.add(null);
        }
        // Aggiungi la nuova riga alla matrice
        matrix.add(newRow);
        // Aggiungi un elemento nullo alla fine di ogni riga esistente
        for (int i = 0; i < nodeCount() - 1; i++) {
            matrix.get(i).add(null);
        }
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        return addNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        int index = nodesIndex.get(node);
        nodesIndex.remove(node);
        // Rimuovo la riga corrispondente al nodo dalla matrice di adiacenza
        matrix.remove(index);
        // Rimuovo la colonna corrispondente al nodo dalla matrice di adiacenza
        for (ArrayList<GraphEdge<L>> row : matrix) {
            row.remove(index);
        }
        // Decremento di uno gli indici dei nodi che erano maggiori dell'indice del nodo rimosso
        for (GraphNode<L> n : nodesIndex.keySet()) {
            int i = nodesIndex.get(n);
            if (i > index) nodesIndex.put(n, i - 1);
        }
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        removeNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        removeNode(getNode(i));
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (nodesIndex.containsKey(node)) return node;
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        for (int i = 0; i < nodeCount(); i++) {
            // Se il nodo di indice i ha la stessa etichetta, lo restituisco
            if (getNode(i).getLabel().equals(label)) {
                return getNode(i);
            }
        }
        return null;
    }

    @Override
    public GraphNode<L> getNode(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        for (GraphNode<L> node : nodesIndex.keySet()) {
            if (nodesIndex.get(node) == i) return node;
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        return nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        return getNodeIndexOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null) throw new NullPointerException("Arco null");
        if (edge.isDirected() != isDirected()) throw new IllegalArgumentException("Arco non compatibile con il grafo");
        int[] indices = getIndicesAndCheckNodes(edge);
        int i = indices[0];
        int j = indices[1];
        // Se la matrice di adiacenza contiene già un arco uguale, restituisco false
        if (matrix.get(i).get(j) != null && matrix.get(i).get(j).equals(edge)) return false;
        // Altrimenti, aggiungo l'arco alla matrice di adiacenza
        matrix.get(i).set(j, edge);
        // Aggiungo anche l'arco simmetrico
        matrix.get(j).set(i, edge);
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        return addEdge(new GraphEdge<>(node1, node2, isDirected()));
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        return addEdge(new GraphEdge<>(node1, node2, isDirected(), weight));
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        return addEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        return addWeightedEdge(new GraphNode<>(label1), new GraphNode<>(label2), weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return addEdge(getNode(i), getNode(j));
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return addWeightedEdge(getNode(i), getNode(j), weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        if (edge == null) throw new NullPointerException("Arco null");
        int[] indices = getIndicesAndCheckNodes(edge);
        // Rimuovo l'arco dalla matrice di adiacenza, se esiste
        if (matrix.get(indices[0]).get(indices[1]) != null) {
            matrix.get(indices[0]).set(indices[1], null);
            // Se il grafo non è orientato, rimuovo anche l'arco simmetrico
            if (!isDirected()) {
                matrix.get(indices[1]).set(indices[0], null);
            }
        } else throw new IllegalArgumentException("Arco inesistente");
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        removeEdge(new GraphEdge<>(node1, node2, isDirected()));
    }

    @Override
    public void removeEdge(L label1, L label2) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        removeEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public void removeEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        removeEdge(getNode(i), getNode(j));
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        if (edge == null) throw new NullPointerException("Arco null");
        int[] indices = getIndicesAndCheckNodes(edge);
        return matrix.get(indices[0]).get(indices[1]);
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) throw new IllegalArgumentException("Nodo inesistente");
        return getEdge(new GraphEdge<>(node1, node2, isDirected()));
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        return getEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return matrix.get(i).get(j);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        return getAdjNodes(nodesIndex.get(node));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        return getAdjacentNodesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return getAdjNodes(i);
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        Set<GraphEdge<L>> edges = new HashSet<>();
        // Ottengo l'insieme dei nodi adiacenti al nodo
        Set<GraphNode<L>> adjacentNodes = getAdjNodes(nodesIndex.get(node));
        // Aggiungo gli archi connessi al nodo all'insieme edges
        for (GraphNode<L> adjacentNode : adjacentNodes) {
            edges.add(getEdge(node, adjacentNode));
        }
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        return getEdgesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        return getEdgesOf(getNode(i));
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        throw new UnsupportedOperationException("Operazione non supportata in un grafo non orientato");
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        Set<GraphEdge<L>> edges = new HashSet<>();
        for (GraphNode<L> node : getNodes()) {
            // Ottengo l'insieme dei nodi adiacenti al nodo
            Set<GraphNode<L>> adjacentNodes = getAdjNodes(nodesIndex.get(node));
            // Aggiungo gli archi connessi ai nodi adiacenti all'insieme edges
            for (GraphNode<L> adjacentNode : adjacentNodes) {
                edges.add(getEdge(node, adjacentNode));
            }
        }
        return edges;
    }

    /**
     * Ottiene gli indici dei nodi associati all'arco e verifica la presenza dei nodi nel grafo.
     *
     * @param edge Arco per il quale ottenere gli indici dei nodi e verificare la presenza nel grafo.
     * @return Un array di interi contenente gli indici dei nodi associati all'arco,
     * dove indices[0] è l'indice del nodo sorgente e indices[1] è l'indice del nodo destinazione.
     * @throws NullPointerException     Se l'arco fornito è nullo.
     * @throws IllegalArgumentException Se uno dei nodi associati all'arco non esiste nel grafo.
     */
    private int[] getIndicesAndCheckNodes(GraphEdge<L> edge) {
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) throw new IllegalArgumentException("Nodo inesistente");
        // Ottengo gli indici dei nodi
        return new int[]{nodesIndex.get(node1), nodesIndex.get(node2)};
    }

    /**
     * Restituisce l'insieme dei nodi adiacenti al nodo di indice i.
     * Un nodo è adiacente se c'è un arco tra il nodo di indice i e il nodo adiacente.
     * @param i l'indice del nodo di cui si vogliono ottenere i nodi adiacenti
     * @return l'insieme dei nodi adiacenti al nodo di indice i
     */
    private Set<GraphNode<L>> getAdjNodes(int i) {
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();
        // Scorro la riga della matrice di adiacenza corrispondente al nodo
        for (int j = 0; j < nodeCount(); j++) {
            // Se c'è un arco tra il nodo e il nodo di indice j, lo aggiungo all'insieme
            if (matrix.get(i).get(j) != null) {
                adjacentNodes.add(getNode(j));
            }
        }
        return adjacentNodes;
    }
}