package it.unicam.cs.asdl2324.es1;

public class EquazioneSecondoGradoModificabileConRisolutore {

    private static final double EPSILON = 1.0E-15;

    private double a;

    private double b;

    private double c;

    private boolean solved;

    private SoluzioneEquazioneSecondoGrado lastSolution;

    public EquazioneSecondoGradoModificabileConRisolutore(double a, double b,
            double c) {
    }

    public double getA() {
        return a;
    }
    
    public void setA(double a) {
    }

    public double getB() {
        return b;
    }
    
    public void setB(double b) {
    }

    /**
     * @return il valore corrente del parametro c
     */
    public double getC() {
        return c;
    }

    /**
     * Cambia il parametro c di questa equazione. Dopo questa operazione
     * l'equazione andrà risolta di nuovo.
     * 
     * @param c
     *              il nuovo valore del parametro c
     */
    public void setC(double c) {
        // TODO implementare
    }
    
    public boolean isSolved() {
        return solved;
    }
    
    public void solve() {
    }

    public SoluzioneEquazioneSecondoGrado getSolution() {
        return null;
    }

}
