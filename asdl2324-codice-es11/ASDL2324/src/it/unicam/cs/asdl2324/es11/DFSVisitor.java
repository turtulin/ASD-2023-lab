package it.unicam.cs.asdl2324.es11;
public class DFSVisitor<L> {
    protected int time;
    public void DFSVisit(Graph<L> g) {
        if (g == null)
            throw new NullPointerException("Tentativo di visitare un grafo null");
        this.time = 0;
        for (GraphNode<L> node : g.getNodes()) {
            node.setPrevious(null);
            node.setColor(0);
        }
        for (GraphNode<L> node : g.getNodes()) {
            if (node.getColor() == 0) {
                recDFS(g, node);
            }
        }

    }

    protected void recDFS(Graph<L> g, GraphNode<L> u) {
        this.time++;
        u.setEnteringTime(this.time);
        u.setColor(1);
        for (GraphNode<L> v : g.getAdjacentNodesOf(u)) {
            if (v.getColor() == 0) {
                v.setPrevious(u);
                recDFS(g, v);
            }
        }
        this.time++;
        u.setExitingTime(this.time);
        u.setColor(2);
        visitNode(u);
    }

    public void visitNode(GraphNode<L> n) {
    }

}
