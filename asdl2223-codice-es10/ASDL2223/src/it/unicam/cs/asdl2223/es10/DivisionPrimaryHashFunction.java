package it.unicam.cs.asdl2223.es10;

public class DivisionPrimaryHashFunction implements PrimaryHashFunction {

    @Override
    public int hash(int key, int m) {
        return Math.abs(key % m); 
                        
    }

}
