package it.unicam.cs.asdl2324.es11;

public class GraphNode<L> {

    public static int COLOR_WHITE = 0;

    public static int COLOR_GREY = 1;

    public static int COLOR_BLACK = 2;

    private final L label;

    private int color;

    private double floatingPointDistance;

    private int integerDistance;

    private int enteringTime;

    private int exitingTime;

    private GraphNode<L> previous;

    public GraphNode(L label) {
        if (label == null)
            throw new NullPointerException("Etichetta nel nodo nulla");
        this.label = label;
    }

    public L getLabel() {
        return this.label;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getIntegerDistance() {
        return this.integerDistance;
    }

    public void setIntegerDistance(int distance) {
        this.integerDistance = distance;
    }

    public double getFloatingPointDistance() {
        return this.floatingPointDistance;
    }

    public void setFloatingPointDistance(double distance) {
        this.floatingPointDistance = distance;
    }

    public GraphNode<L> getPrevious() {
        return this.previous;
    }

    public void setPrevious(GraphNode<L> previous) {
        this.previous = previous;
    }

    public int getEnteringTime() {
        return this.enteringTime;
    }

    public void setEnteringTime(int time) {
        this.enteringTime = time;
    }

    public int getExitingTime() {
        return this.exitingTime;
    }

    public void setExitingTime(int time) {
        this.exitingTime = time;
    }

    @Override
    public int hashCode() {
        return this.label.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof GraphNode))
            return false;
        GraphNode<?> other = (GraphNode<?>) obj;
        if (this.label.equals(other.label))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Nodo[ " + label.toString() + " ]";
    }

}
