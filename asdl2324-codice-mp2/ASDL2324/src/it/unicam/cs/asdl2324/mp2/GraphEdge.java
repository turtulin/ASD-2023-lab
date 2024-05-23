package it.unicam.cs.asdl2324.mp2;

public class GraphEdge<L> {

    private final GraphNode<L> node1;

    private final GraphNode<L> node2;

    private final boolean directed;

    private double weight;

    public GraphEdge(GraphNode<L> node1, GraphNode<L> node2, boolean directed,
            double weight) {
        if (node1 == null)
            throw new NullPointerException("Nodo 1 dell'arco nullo");
        if (node2 == null)
            throw new NullPointerException("Nodo 2 dell'arco nullo");
        this.node1 = node1;
        this.node2 = node2;
        this.directed = directed;
        this.weight = weight;
    }

    public GraphEdge(GraphNode<L> node1, GraphNode<L> node2, boolean directed) {
        if (node1 == null)
            throw new NullPointerException("Nodo 1 dell'arco nullo");
        if (node2 == null)
            throw new NullPointerException("Nodo 2 dell'arco nullo");
        this.node1 = node1;
        this.node2 = node2;
        this.directed = directed;
        this.weight = Double.NaN;
    }

    public boolean hasWeight() {
        return !Double.isNaN(this.weight);
    }

    public GraphNode<L> getNode1() {
        return this.node1;
    }
    
    public GraphNode<L> getNode2() {
        return this.node2;
    }

    public boolean isDirected() {
        return this.directed;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (directed ? 1231 : 1237);
        result = prime * result + (node1.hashCode() + node2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof GraphEdge))
            return false;
        GraphEdge<?> other = (GraphEdge<?>) obj;
        if (directed != other.directed)
            return false;
        if (directed) {
            if (!node1.equals(other.node1))
                return false;
            if (!node2.equals(other.node2))
                return false;
            return true;
        } else { 
            if (node1.equals(other.node1) && node2.equals(other.node2))
                return true;
            if (node1.equals(other.node2) && node2.equals(other.node1))
                return true;
            return false;
        }
    }

    @Override
    public String toString() {
        if (this.directed)
            return "Edge [ " + this.node1.toString() + " --> "
                    + this.node2.toString() + " ]";
        else
            return "Edge [ " + this.node1.toString() + " -- "
                    + this.node2.toString() + " ]";
    }
}
