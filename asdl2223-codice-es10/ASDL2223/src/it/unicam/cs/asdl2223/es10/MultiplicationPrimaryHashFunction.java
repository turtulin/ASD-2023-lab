package it.unicam.cs.asdl2223.es10;

public class MultiplicationPrimaryHashFunction implements PrimaryHashFunction {

    @Override
    public int hash(int key, int m) {
        double phi = (Math.sqrt(5) - 1) / 2;
        double v = key * phi;
        double v1 = m * (v - Math.floor(v));
        return Math.abs((int) v1);
    }

}
