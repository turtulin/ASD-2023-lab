/**
 * 
 */
package it.unicam.cs.asdl2324.mp2;

import java.util.ArrayList;
import java.util.Collections;
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
        // Il numero di nodi è uguale alla dimensione della mappa
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
        // Per cancellare tutti i nodi e gli archi basta svuotare la mappa e la matrice di adiacenza
        nodesIndex.clear();
        matrix.clear();
    }

    @Override
    public boolean isDirected() {
        // Questa classe implementa un grafo non orientato, quindi restituisco sempre false
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
        // Aggiungo il nodo alla mappa con l'indice corrispondente al numero di nodi attuali
        nodesIndex.put(node, nodeCount());
        // Aggiungo una nuova riga alla matrice di adiacenza, inizializzata con elementi null
        matrix.add(new ArrayList<>(Collections.nCopies(nodeCount(), null)));
        // Aggiungo un nuovo elemento null a ogni riga esistente della matrice di adiacenza
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
        // Creo un nuovo nodo con l'etichetta data e lo aggiungo al grafo
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
        // Ottengo l'indice del nodo da rimuovere
        int index = nodesIndex.get(node);
        // Rimuovo il nodo dalla mappa
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
        // Creo un nuovo nodo con l'etichetta data e lo rimuove dal grafo
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
        // Altrimenti, ottengo il nodo corrispondente all'indice e lo rimuovo dal grafo
        GraphNode<L> node = getNode(i);
        removeNode(node);
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
        // Scorro la lista dei nodi
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
        // Cerco il nodo nella mappa nodesIndex che ha l'indice i
        for (GraphNode<L> node : nodesIndex.keySet()) {
            if (nodesIndex.get(node) == i) return node;
        }
        return null;
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        // Restituisco l'indice associato al nodo nella mappa nodesIndex
        return nodesIndex.get(node);
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        // Creo un nuovo nodo con l'etichetta data e uso il metodo getNodeIndexOf per ottenere l'indice
        return getNodeIndexOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
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
        // Crea un nuovo arco non pesato tra i due nodi, orientato o no a seconda del grafo
        GraphEdge<L> edge = new GraphEdge<>(node1, node2, isDirected());
        // Aggiunge l'arco al grafo usando il metodo addEdge
        return addEdge(edge);
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        // Crea un nuovo arco pesato tra i due nodi, orientato o no a seconda del grafo
        GraphEdge<L> edge = new GraphEdge<>(node1, node2, isDirected(), weight);
        // Aggiunge l'arco al grafo usando il metodo addEdge
        return addEdge(edge);
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        // Crea due nuovi nodi con le etichette date e usa il metodo addEdge per aggiungere l'arco
        return addEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        // Crea due nuovi nodi con le etichette date e usa il metodo addWeightedEdge per aggiungere l'arco
        return addWeightedEdge(new GraphNode<>(label1), new GraphNode<>(label2), weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        // Ottiene i nodi corrispondenti agli indici usando il metodo getNode
        GraphNode<L> node1 = getNode(i);
        GraphNode<L> node2 = getNode(j);
        // Aggiunge l'arco tra i due nodi usando il metodo addEdge
        return addEdge(node1, node2);
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        // Ottiene i nodi corrispondenti agli indici usando il metodo getNode
        GraphNode<L> node1 = getNode(i);
        GraphNode<L> node2 = getNode(j);
        // Aggiunge l'arco pesato tra i due nodi usando il metodo addWeightedEdge
        return addWeightedEdge(node1, node2, weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        int[] indices = getIndicesAndCheckNodes(edge);
        int i = indices[0];
        int j = indices[1];
        // Rimuove l'arco dalla matrice di adiacenza, se esiste
        if (matrix.get(i).get(j) != null) {
            matrix.get(i).set(j, null);
            // Se il grafo non è orientato, rimuove anche l'arco simmetrico
            if (!isDirected()) {
                matrix.get(j).set(i, null);
            }
        } else throw new IllegalArgumentException("Arco inesistente");
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        // Crea un nuovo arco tra i due nodi, orientato o no a seconda del grafo
        GraphEdge<L> edge = new GraphEdge<>(node1, node2, isDirected());
        // Rimuove l'arco uguale a quello creato usando il metodo removeEdge
        removeEdge(edge);
    }

    @Override
    public void removeEdge(L label1, L label2) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        // Crea due nuovi nodi con le etichette date e usa il metodo removeEdge per rimuovere l'arco
        removeEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public void removeEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        // Crea due nuovi nodi con gli indici dati e usa il metodo removeEdge per rimuovere l'arco
        removeEdge(getNode(i), getNode(j));
    }

    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        int[] indices = getIndicesAndCheckNodes(edge);
        int i = indices[0];
        int j = indices[1];
        // Restituisce l'arco nella matrice di adiacenza, se esiste, o null altrimenti
        return matrix.get(i).get(j);
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) throw new IllegalArgumentException("Nodo inesistente");
        // Crea un nuovo arco tra i due nodi, orientato o no a seconda del grafo
        GraphEdge<L> edge = new GraphEdge<>(node1, node2, isDirected());
        // Restituisce l'arco uguale a quello creato usando il metodo getEdge
        return getEdge(edge);
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        if (label1 == null || label2 == null) throw new NullPointerException("Etichetta null");
        // Crea due nuovi nodi con le etichette date e usa il metodo getEdge per ottenere l'arco
        return getEdge(new GraphNode<>(label1), new GraphNode<>(label2));
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        if (i < 0 || i >= nodeCount() || j < 0 || j >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        // Restituisce l'arco nella matrice di adiacenza, se esiste, o null altrimenti
        return matrix.get(i).get(j);
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (node == null) throw new NullPointerException("Nodo null");
        if (!nodesIndex.containsKey(node)) throw new IllegalArgumentException("Nodo inesistente");
        // Crea un nuovo insieme vuoto per contenere i nodi adiacenti
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();
        // Ottiene l'indice del nodo
        int i = nodesIndex.get(node);
        // Scorre la riga della matrice di adiacenza corrispondente al nodo
        for (int j = 0; j < nodeCount(); j++) {
            // Se c'è un arco tra il nodo e il nodo di indice j, aggiunge quest'ultimo all'insieme
            if (matrix.get(i).get(j) != null) {
                adjacentNodes.add(getNode(j));
            }
        }
        // Restituisce l'insieme dei nodi adiacenti
        return adjacentNodes;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        // Crea un nuovo nodo con l'etichetta data e usa il metodo getAdjacentNodesOf per ottenere i nodi adiacenti
        return getAdjacentNodesOf(new GraphNode<L>(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        // Crea un nuovo insieme vuoto per contenere i nodi adiacenti
        Set<GraphNode<L>> adjacentNodes = new HashSet<>();
        // Scorre la riga della matrice di adiacenza corrispondente al nodo
        for (int j = 0; j < nodeCount(); j++) {
            // Se c'è un arco tra il nodo di indice i e il nodo di indice j, aggiunge quest'ultimo all'insieme
            if (matrix.get(i).get(j) != null) {
                adjacentNodes.add(getNode(j));
            }
        }
        // Restituisce l'insieme dei nodi adiacenti
        return adjacentNodes;
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
        // Crea un nuovo insieme vuoto per contenere gli archi connessi
        Set<GraphEdge<L>> edges = new HashSet<>();
        // Ottiene l'indice del nodo
        int i = nodesIndex.get(node);
        // Scorre la riga della matrice di adiacenza corrispondente al nodo
        for (int j = 0; j < nodeCount(); j++) {
            // Se c'è un arco tra il nodo e il nodo di indice j, lo aggiunge all'insieme
            if (matrix.get(i).get(j) != null) {
                edges.add(matrix.get(i).get(j));
            }
        }
        // Restituisce l'insieme degli archi connessi
        return edges;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        if (label == null) throw new NullPointerException("Etichetta null");
        // Crea un nuovo nodo con l'etichetta data e usa il metodo getEdgesOf per ottenere gli archi connessi
        return getEdgesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        if (i < 0 || i >= nodeCount()) throw new IndexOutOfBoundsException("Indice non valido");
        // Crea un nuovo nodo con l'indice dato e usa il metodo getEdgesOf per ottenere gli archi connessi
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
        // Crea un nuovo insieme vuoto per contenere gli archi
        Set<GraphEdge<L>> edges = new HashSet<>();
        // Scorre la matrice di adiacenza
        for (int i = 0; i < nodeCount(); i++) {
            for (int j = 0; j < nodeCount(); j++) {
                // Se c'è un arco tra il nodo di indice i e il nodo di indice j, lo aggiunge all'insieme
                if (matrix.get(i).get(j) != null) {
                    edges.add(matrix.get(i).get(j));
                }
            }
        }
        // Restituisce l'insieme degli archi
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
        if (edge == null) throw new NullPointerException("Arco null");
        // Ottiene i nodi sorgente e destinazione dell'arco
        GraphNode<L> node1 = edge.getNode1();
        GraphNode<L> node2 = edge.getNode2();
        // Se uno dei due nodi non esiste nel grafo, lancia un'eccezione
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) throw new IllegalArgumentException("Nodo inesistente");
        // Ottiene gli indici dei nodi
        int i = nodesIndex.get(node1);
        int j = nodesIndex.get(node2);
        return new int[]{i, j};
    }
}
