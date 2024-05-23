package it.unicam.cs.asdl2324.es1;

public class EquazioneSecondoGrado
        implements Comparable<EquazioneSecondoGrado> {

    private static final double EPSILON = 1.0E-15;

    final private double a;

    final private double b;

    final private double c;

    public EquazioneSecondoGrado(double a, double b, double c) {
        if (Math.abs(a) < EPSILON)
            throw new IllegalArgumentException("L'equazione di secondo grado"
                    + " non può avere coefficiente a uguale a zero");
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;

        temp = Double.doubleToLongBits(a);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(b);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(c);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EquazioneSecondoGrado))
            return false;
        EquazioneSecondoGrado other = (EquazioneSecondoGrado) obj;
        if (Double.doubleToLongBits(this.a) != Double.doubleToLongBits(other.a))
            return false;
        if (Double.doubleToLongBits(this.b) != Double.doubleToLongBits(other.b))
            return false;
        if (Double.doubleToLongBits(this.c) != Double.doubleToLongBits(other.c))
            return false;
        return true;
    }

    @Override
    public int compareTo(EquazioneSecondoGrado o) {
        if (o == null)
            throw new NullPointerException("Tentativo di confrontare con null");
        if (this.a < o.a)
            return -1;
        else if (this.a > o.a)
            return 1;
        if (this.b < o.b)
            return -1;
        else if (this.b > o.b)
            return 1;
        if (this.c < o.c)
            return -1;
        else if (this.c > o.c)
            return 1;
        return 0;
    }

    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append(a + " x^2 ");
        if (b != 0)
            s.append("+ " + b + " x ");
        if (c != 0)
            s.append("+ " + c + " = 0");
        return s.toString();
    }

}
