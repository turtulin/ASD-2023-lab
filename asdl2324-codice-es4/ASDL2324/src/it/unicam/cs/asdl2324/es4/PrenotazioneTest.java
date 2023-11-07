/**
 * 
 */
package it.unicam.cs.asdl2324.es4;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Luca Tesei
 *
 */
class PrenotazioneTest {

    private Aula a;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        a = new Aula("LA1", "Polo Lodovici");
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl1920.mp1.Prenotazione#hashCode()}.
     */
    @Test
    final void testHashCode() {
        GregorianCalendar g1 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g2 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts1 = new TimeSlot(g1, g2);
        Prenotazione p1 = new Prenotazione(a, ts1, "Luca Tesei",
                "Lezione ASDL");
        GregorianCalendar g3 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g4 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts2 = new TimeSlot(g3, g4);
        Prenotazione p2 = new Prenotazione(new Aula("LA1", "Polo Lodovici"),
                ts2, "", "");
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
        assertTrue(p1.hashCode() == p2.hashCode());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl1920.mp1.Prenotazione#Prenotazione(it.unicam.cs.asdl1920.mp1.Aula, it.unicam.cs.asdl1920.mp1.TimeSlot, java.lang.String, java.lang.String)}.
     */
    @Test
    final void testPrenotazione() {
        GregorianCalendar g1 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g2 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts1 = new TimeSlot(g1, g2);
        // Testa se il controllo di null è corretto
        assertThrows(NullPointerException.class,
                () -> new Prenotazione(a, ts1, "Luca Tesei", null));
        assertThrows(NullPointerException.class,
                () -> new Prenotazione(a, ts1, null, "Lezione ASDL"));
        assertThrows(NullPointerException.class,
                () -> new Prenotazione(a, null, "Luca Tesei", "Lezione ASDL"));
        assertThrows(NullPointerException.class, () -> new Prenotazione(null,
                ts1, "Luca Tesei", "Lezione ASDL"));
        assertThrows(NullPointerException.class, () -> new Prenotazione(null,
                null, "Luca Tesei", "Lezione ASDL"));
        assertThrows(NullPointerException.class,
                () -> new Prenotazione(null, null, null, "Lezione ASDL"));
        assertThrows(NullPointerException.class,
                () -> new Prenotazione(null, null, null, null));
    }

    /**
     * Test method for {@link it.unicam.cs.asdl1920.mp1.Prenotazione#getAula()}.
     */
    @Test
    final void testGetAula() {
        GregorianCalendar g1 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g2 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts1 = new TimeSlot(g1, g2);
        Prenotazione p1 = new Prenotazione(a, ts1, "Luca Tesei",
                "Lezione ASDL");
        assertEquals(new Aula("LA1","pippo"),p1.getAula());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl1920.mp1.Prenotazione#getTimeSlot()}.
     */
    @Test
    final void testGetTimeSlot() {
        GregorianCalendar g1 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g2 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts1 = new TimeSlot(g1, g2);
        Prenotazione p1 = new Prenotazione(a, ts1, "Luca Tesei",
                "Lezione ASDL");
        GregorianCalendar g3 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g4 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts2 = new TimeSlot(g3, g4);
        assertEquals(ts2, p1.getTimeSlot());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl1920.mp1.Prenotazione#getDocente()}.
     */
    @Test
    final void testGetDocente() {
        GregorianCalendar g1 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g2 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts1 = new TimeSlot(g1, g2);
        Prenotazione p1 = new Prenotazione(a, ts1, "Luca Tesei",
                "Lezione ASDL");
        assertEquals("Luca Tesei", p1.getDocente());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl1920.mp1.Prenotazione#getMotivo()}.
     */
    @Test
    final void testGetMotivo() {
        GregorianCalendar g1 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g2 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts1 = new TimeSlot(g1, g2);
        Prenotazione p1 = new Prenotazione(a, ts1, "Luca Tesei",
                "Lezione ASDL");
        assertEquals("Lezione ASDL", p1.getMotivo());
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl1920.mp1.Prenotazione#equals(java.lang.Object)}.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    final void testEqualsObject() {
        GregorianCalendar g1 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g2 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts1 = new TimeSlot(g1, g2);
        Prenotazione p1 = new Prenotazione(a, ts1, "Luca Tesei",
                "Lezione ASDL");
        GregorianCalendar g3 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g4 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts2 = new TimeSlot(g3, g4);
        Prenotazione p2 = new Prenotazione(new Aula("LA1", "Polo Lodovici"),
                ts2, "", "");
        assertTrue(p1.equals(p2));
        assertTrue(p2.equals(p1));
        GregorianCalendar g5 = new GregorianCalendar(2019, 10, 4, 14, 00);
        GregorianCalendar g6 = new GregorianCalendar(2019, 10, 4, 16, 00);
        TimeSlot ts3 = new TimeSlot(g5, g6);
        Prenotazione p3 = new Prenotazione(a, ts3, " ", "");
        assertFalse(p1.equals(p3));
        assertFalse(p2.equals(p3));
        assertFalse(p3.equals(p1));
        assertFalse(p3.equals(p2));
        Prenotazione p4 = new Prenotazione(new Aula("AA1", "Polo Lodovici"),
                ts1, " ", "");
        assertFalse(p1.equals(p4));
        assertFalse(p4.equals(p1));
        // Classe diversa
        assertFalse(p1.equals("Pippo"));
        // Null
        assertFalse(p1.equals(null));
    }

    /**
     * Test method for
     * {@link it.unicam.cs.asdl1920.mp1.Prenotazione#compareTo(it.unicam.cs.asdl1920.mp1.Prenotazione)}.
     */
    @Test
    final void testCompareTo() {
        GregorianCalendar g1 = new GregorianCalendar(2019, 10, 4, 11, 00);
        GregorianCalendar g2 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts1 = new TimeSlot(g1, g2);
        GregorianCalendar g3 = new GregorianCalendar(2019, 10, 4, 10, 00);
        GregorianCalendar g4 = new GregorianCalendar(2019, 10, 4, 13, 00);
        TimeSlot ts2 = new TimeSlot(g3, g4);
        Prenotazione p1 = new Prenotazione(a, ts1, "AA", "BB");
        Prenotazione p2 = new Prenotazione(new Aula("M1","Pippo"), ts2, "CC", "DD");
        Prenotazione p3 = new Prenotazione(a, ts2, "CC", "DD");
        // p2 inizia prima di p1
        assertTrue(p1.compareTo(p2) > 0);
        assertTrue(p2.compareTo(p1) < 0);
        g3.roll(Calendar.HOUR_OF_DAY, 1);
        // ts2 == ts1, aula di p1 precede aula di p2
        assertTrue(p1.compareTo(p2) < 0);
        assertTrue(p2.compareTo(p1) > 0);
        // p1 == p3
        assertTrue(p1.compareTo(p3) == 0);
        g3.roll(Calendar.HOUR_OF_DAY, 1);
        // p2 inizia dopo p1
        assertTrue(p1.compareTo(p2) < 0);
        assertTrue(p2.compareTo(p1) > 0);
        // p2 inizia dopo p1
        g4.roll(Calendar.HOUR_OF_DAY, 1);
        assertTrue(p2.compareTo(p1) > 0);
        assertTrue(p1.compareTo(p2) < 0);
    }

}
