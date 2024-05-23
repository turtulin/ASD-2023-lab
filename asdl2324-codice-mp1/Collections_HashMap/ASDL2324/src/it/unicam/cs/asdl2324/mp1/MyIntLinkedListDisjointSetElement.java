package it.unicam.cs.asdl2324.mp1;

public class MyIntLinkedListDisjointSetElement implements DisjointSetElement {

    private final int value;

    private DisjointSetElement representative;

    private DisjointSetElement nextElement;

    private int size;

    public MyIntLinkedListDisjointSetElement(int value) {
        this.value = value;
        this.representative = null;
        this.nextElement = null;
        this.size = 0;
    }

    public int getValue() {
        return value;
    }

    @Override
    public DisjointSetElement getRef1() {
        return this.representative;
    }

    @Override
    public void setRef1(DisjointSetElement e) {
        this.representative = e;
    }

    @Override
    public DisjointSetElement getRef2() {
        return this.nextElement;
    }

    @Override
    public void setRef2(DisjointSetElement e) {
        this.nextElement = e;
    }

    @Override
    public int getNumber() {
        return this.size;
    }

    @Override
    public void setNumber(int n) {
        this.size = n;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof MyIntLinkedListDisjointSetElement))
            return false;
        MyIntLinkedListDisjointSetElement other = (MyIntLinkedListDisjointSetElement) obj;
        if (representative == null) {
            if (other.representative != null)
                return false;
        } else if (this.representative != other.representative)
            return false;
        if (this.value != other.value)
            return false;
        return true;
    }

}
