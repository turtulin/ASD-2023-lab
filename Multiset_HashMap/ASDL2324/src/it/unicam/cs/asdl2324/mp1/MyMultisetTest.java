package it.unicam.cs.asdl2324.mp1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Classe di test per MyMultiset
 *
 * @author Luca Tesei
 *
 */
public class MyMultisetTest {

    @Test
    public void testMyMultiset() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        assertEquals(true, m.isEmpty());
    }

    @Test
    public void testSizeEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        assertEquals(0, m.size());
    }

    @Test
    public void testSizeOne() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1);
        assertEquals(1, m.size());
    }

    @Test
    public void testSizeMore() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1);
        m.add(1);
        m.add(2);
        m.add(3);
        m.add(4);
        assertEquals(5, m.size());
    }

    @Test
    public void testCountNull() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        assertThrows(NullPointerException.class, () -> m.count(null));
    }

    @Test
    public void testCountZero() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(3);
        assertEquals(0, m.count(2));
    }

    @Test
    public void testCountOne() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(3);
        assertEquals(1, m.count(3));
    }

    @Test
    public void testCountMore() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(3);
        m.add(2);
        m.add(3);
        assertEquals(2, m.count(3));
    }

    @Test
    public void testAddEIntNull() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        assertThrows(NullPointerException.class, () -> m.add(null, 3));
    }

    @Test
    public void testAddEIntNegOccurrences() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        assertThrows(IllegalArgumentException.class, () -> m.add(3, -1));
    }

    @Test
    public void testAddEIntMoreThanMAX_VALUE() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(3, Integer.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> m.add(3, 1));
    }

    @Test
    public void testAddEfficientSpaceRepresentation() {
        MyMultiset<Integer> m = new MyMultiset<Integer>();
        for (int i = 0; i <= 120000; i++)
            m.add(i, Integer.MAX_VALUE);
        /*
         * Con una rappresentazione non compatta a questo punto dovrebbero
         * essere stati consumati circa 14 GB di memoria, e quindi probabilmente
         * viene lanciata una OutOfMemoryException e il test fallisce
         */
    }

    @Test
    public void testAddEIntAddZeroNoElement() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 3);
        assertEquals(0, m.add(1, 0));
        assertEquals(0, m.count(1));
    }

    @Test
    public void testAddEIntAddZeroElement() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 3);
        assertEquals(3, m.add(2, 0));
        assertEquals(3, m.count(2));
    }

    @Test
    public void testAddEIntAddOneNoElement() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 3);
        m.add(3, 1);
        assertEquals(1, m.count(3));
    }

    @Test
    public void testAddEIntAddMoreNoElement() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 3);
        m.add(3, 3);
        assertEquals(3, m.count(3));
    }

    @Test
    public void testAddEIntAddOneElement() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 3);
        assertEquals(3, m.count(2));
        m.add(2, 1);
        m.add(3, 4);
        assertEquals(4, m.count(2));
    }

    @Test
    public void testAddEIntAddMoreElement() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 3);
        assertEquals(3, m.count(2));
        m.add(2, 2);
        m.add(3, 4);
        m.add(2, 1);
        assertEquals(6, m.count(2));
    }

    @Test
    public void testAddENull() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        assertThrows(NullPointerException.class, () -> m.add(null));
    }

    @Test
    public void testAddEMaxvalue() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(3, Integer.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> m.add(3));
    }

    @Test
    public void testAddENoElement() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(3, Integer.MAX_VALUE);
        m.add(2);
        assertEquals(1, m.count(2));
    }

    @Test
    public void testAddEWithElement() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(3, Integer.MAX_VALUE - 1);
        m.add(3);
        assertEquals(Integer.MAX_VALUE, m.count(3));
    }

    @Test
    public void testRemoveObjectIntNull() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertThrows(NullPointerException.class, () -> m.remove(null, 3));
    }

    @Test
    public void testRemoveObjectIntNegOccurrences() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertThrows(IllegalArgumentException.class, () -> m.remove(2, -3));
    }

    @Test
    public void testRemoveObjectIntZeroOccurrences() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertEquals(1, m.remove(2, 0));
        assertEquals(1, m.count(2));
    }

    @Test
    public void testRemoveObjectIntZeroOccurrencesNoElement() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertEquals(0, m.remove(3, 0));
        assertEquals(0, m.count(3));
    }

    @Test
    public void testRemoveObjectIntNOccurrencesNoFewer() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 5);
        assertEquals(5, m.remove(2, 3));
        assertEquals(2, m.count(2));
    }

    @Test
    public void testRemoveObjectIntNOccurrencesFewer() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 5);
        assertEquals(5, m.remove(2, 7));
        assertEquals(0, m.count(2));
    }

    @Test
    public void testRemoveObjectNull() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 5);
        assertThrows(NullPointerException.class, () -> m.remove(null));
    }

    @Test
    public void testRemoveObjectPresentMoreThanOne() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 5);
        assertEquals(true, m.remove(2));
        assertEquals(4, m.count(2));
    }

    @Test
    public void testRemoveObjectPresentOne() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertEquals(true, m.remove(2));
        assertEquals(0, m.count(2));
    }

    @Test
    public void testRemoveObjectNotPresent() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertEquals(false, m.remove(3));
    }

    @Test
    public void testSetCountNull() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertThrows(NullPointerException.class, () -> m.setCount(null, 3));
    }

    @Test
    public void testSetCountNegOccurrences() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertThrows(IllegalArgumentException.class, () -> m.setCount(2, -3));
    }

    @Test
    public void testSetCountEIntZeroOccurrences() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertEquals(0, m.setCount(3, 0));
        assertEquals(0, m.count(3));
    }

    @Test
    public void testSetCountEIntNElements() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 3);
        assertEquals(3, m.setCount(2, 5));
        assertEquals(5, m.count(2));
        assertEquals(5, m.setCount(2, 0));
        assertEquals(0, m.count(2));
        assertEquals(0, m.setCount(2, 10));
    }

    @Test
    public void testSetCountEIntNElementsToZero() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 3);
        assertEquals(3, m.setCount(2, 0));
        assertEquals(0, m.count(2));
    }

    @Test
    public void testElementSetEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        Set<Integer> ms = m.elementSet();
        Set<Integer> empty = new HashSet<Integer>();
        assertEquals(true, empty.equals(ms));
    }

    @Test
    public void testElementSetEmptyNotEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        Set<Integer> ms = m.elementSet();
        ms.add(4);
        Set<Integer> empty = new HashSet<Integer>();
        assertEquals(false, empty.equals(ms));
    }

    @Test
    public void testElementSetNotEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1);
        m.add(2, 3);
        m.add(2);
        m.add(-13, 7);
        Set<Integer> ms = m.elementSet();
        Set<Integer> s = new HashSet<Integer>();
        s.add(2);
        s.add(-13);
        s.add(1);
        assertEquals(true, s.equals(ms));
    }

    @Test
    public void testElementSetNotEmptyNotEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1);
        m.add(2, 3);
        m.add(2);
        m.add(-13, 7);
        Set<Integer> ms = m.elementSet();
        Set<Integer> s = new HashSet<Integer>();
        s.add(-2);
        s.add(-13);
        s.add(1);
        assertEquals(false, s.equals(ms));
    }

    @Test
    public void testIteratorEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        Iterator<Integer> itr = m.iterator();
        assertEquals(false, itr.hasNext());
    }

    @Test
    public void testIteratorSet() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1);
        m.add(2);
        m.add(3);
        Iterator<Integer> itr = m.iterator();
        Set<Integer> s = new HashSet<Integer>();
        s.add(1);
        s.add(2);
        s.add(3);
        while (itr.hasNext()) {
            Integer n = itr.next();
            assertEquals(true, s.remove(n));
        }
        assertEquals(true, s.isEmpty());
    }

    @Test
    public void testIteratorList() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1, 3);
        m.add(2, 1);
        m.add(3, 2);
        Iterator<Integer> itr = m.iterator();
        List<Integer> l = new LinkedList<Integer>();
        l.add(1);
        l.add(1);
        l.add(1);
        l.add(2);
        l.add(3);
        l.add(3);
        while (itr.hasNext()) {
            Integer n = itr.next();
            assertEquals(true, l.remove(n));
        }
        assertEquals(true, l.isEmpty());
    }

    @Test
    public void testIteratorSequence() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1, 3);
        m.add(3, 2);
        Iterator<Integer> itr = m.iterator();
        Integer n = itr.next();
        if (n.equals(1)) {
            int n1 = itr.next();
            int n2 = itr.next();
            assertEquals(1, n1);
            assertEquals(1, n2);
        } else {
            int n1 = itr.next();
            assertEquals(3, n1);
        }
        n = itr.next();
        if (n.equals(1)) {
            int n1 = itr.next();
            int n2 = itr.next();
            assertEquals(1, n1);
            assertEquals(1, n2);
        } else {
            int n1 = itr.next();
            assertEquals(3, n1);
        }
        assertEquals(false, itr.hasNext());
    }


    @Test
    public void testIteratorFailFastAddCount() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1, 3);
        Iterator<Integer> itr = m.iterator();
        assertTrue(itr.hasNext());
        Integer i = itr.next();
        assertEquals(i, 1);
        // Modifica il multiset
        m.add(2, 4);
        // Continuo l'iterazione
        assertThrows(ConcurrentModificationException.class, () -> itr.next());
    }

    @Test
    public void testIteratorFailFastAddCount1() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1, 3);
        Iterator<Integer> itr = m.iterator();
        assertTrue(itr.hasNext());
        Integer i = itr.next();
        assertEquals(i, 1);
        m.setCount(1, 0);
        // Continuo l'iterazione
        assertThrows(ConcurrentModificationException.class, () -> itr.next());
    }

    @Test
    public void testIteratorFailFastAdd() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1, 3);
        Iterator<Integer> itr = m.iterator();
        assertTrue(itr.hasNext());
        Integer i = itr.next();
        assertEquals(i, 1);
        // Modifica il multiset
        m.add(2);
        // Continuo l'iterazione
        assertThrows(ConcurrentModificationException.class, () -> itr.next());
    }

    @Test
    public void testIteratorFailFastRemoveCount() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1, 3);
        Iterator<Integer> itr = m.iterator();
        assertTrue(itr.hasNext());
        Integer i = itr.next();
        assertEquals(i, 1);
        // Non modifica il multiset
        m.remove(1, 0);
        // Continuo
        i = itr.next();
        assertEquals(i, 1);
        // Modifico
        m.remove(1, 1);
        // Continuo l'iterazione
        assertThrows(ConcurrentModificationException.class, () -> itr.next());
    }

    @Test
    public void testIteratorFailFastRemove() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1, 1);
        m.add(2, 2);
        Iterator<Integer> itr = m.iterator();
        assertTrue(itr.hasNext());
        itr.next();
        // Non modifico
        m.remove(3);
        // Continuo
        itr.next();
        // Modifico
        m.remove(1);
        // Continuo l'iterazione
        assertThrows(ConcurrentModificationException.class, () -> itr.next());
    }

    @Test
    public void testIteratorFailFastSetCount() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1, 5);
        Iterator<Integer> itr = m.iterator();
        assertTrue(itr.hasNext());
        Integer i = itr.next();
        assertEquals(i, 1);
        // Non modifica il multiset
        m.setCount(1, 5);
        // Continuo
        i = itr.next();
        assertEquals(i, 1);
        // Modifico
        m.setCount(1, 100);
        // Continuo l'iterazione
        assertThrows(ConcurrentModificationException.class, () -> itr.next());
    }

    @Test
    public void testIteratorFailFastClear() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(1, 5);
        Iterator<Integer> itr = m.iterator();
        assertTrue(itr.hasNext());
        Integer i = itr.next();
        assertEquals(i, 1);
        // Modifico
        m.clear();
        // Continuo l'iterazione
        assertThrows(ConcurrentModificationException.class, () -> itr.next());
    }

    @Test
    public void testContainsObjectNull() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 5);
        assertThrows(NullPointerException.class, () -> m.contains(null));
    }

    @Test
    public void testContainsObjectPresentMoreThanOne() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2, 5);
        assertEquals(true, m.contains(2));
    }

    @Test
    public void testContainsObjectPresentOne() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertEquals(true, m.contains(2));
    }

    @Test
    public void testContainsObjectNotPresent() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        assertEquals(false, m.contains(3));
    }

    @Test
    public void testClear() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(2);
        m.add(3, 4);
        m.add(4, 89);
        m.clear();
        assertEquals(true, m.isEmpty());
    }

    @Test
    public void testClearEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.clear();
        assertEquals(true, m.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        assertEquals(true, m.isEmpty());
    }

    @Test
    public void testIsEmptyNotEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m.add(-47298);
        m.add(0);
        assertEquals(false, m.isEmpty());
    }

    @Test
    public void testEqualsObjectEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m1 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m2 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        assertEquals(true, m1.equals(m2));
    }

    @Test
    public void testEqualsObjectEmptyNotEmpty() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m1 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m2 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m2.add(3);
        assertEquals(false, m1.equals(m2));
    }

    @Test
    public void testEqualsObject() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m1 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m1.add(3, 5);
        m1.setCount(4, 7);
        m1.setCount(5, 9);
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m2 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m2.add(5, 9);
        m2.add(3);
        m2.setCount(3, 5);
        m2.add(4);
        m2.add(4, 6);
        assertEquals(true, m1.equals(m2));
    }

    @Test
    public void testEqualsObject2() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m1 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m1.add(3, 5);
        m1.setCount(2, 3);
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m2 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m2.add(5, 3);
        m2.setCount(3, 2);
        assertEquals(false, m1.equals(m2));
    }

    @Test
    public void TestHashcode() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m1 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m1.add(3, 5);
        m1.setCount(4, 7);
        m1.setCount(5, 9);
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m2 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m2.add(5, 9);
        m2.add(3);
        m2.setCount(3, 5);
        m2.add(4);
        m2.add(4, 6);
        assertEquals(true, m1.hashCode() == m2.hashCode());
    }

    @Test
    public void TestHashcode2() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m1 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m1.add(3, 5);
        m1.setCount(2, 3);
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m2 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m2.add(5, 3);
        m2.setCount(3, 2);
        assertEquals(false, m1.hashCode() == m2.hashCode());
    }

    @Test
    public void TestEqualsHashcode() {
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m1 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m1.add(3, 5);
        m1.setCount(4, 7);
        m1.setCount(5, 9);
        it.unicam.cs.asdl2324.mp1.MyMultiset<Integer> m2 = new it.unicam.cs.asdl2324.mp1.MyMultiset<Integer>();
        m2.add(5, 9);
        m2.add(3);
        m2.setCount(3, 5);
        m2.add(4);
        m2.add(4, 6);
        assertEquals(true, m1.equals(m2));
        assertEquals(true, m1.hashCode() == m2.hashCode());
    }

}

