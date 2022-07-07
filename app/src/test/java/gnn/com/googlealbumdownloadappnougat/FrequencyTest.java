package gnn.com.googlealbumdownloadappnougat;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gnn.com.photos.Photo;

public class FrequencyTest {
    @Test
    public void never() {
        Frequency frequency = new Frequency("-1");
        assertTrue(frequency.equals(Frequency.NEVER));
    }

    @Test
    public void always() {
        Frequency frequency = new Frequency("0");
        assertTrue(frequency.equals(Frequency.ALWAYS));
    }

    @Test
    public void value() {
        Frequency frequency = new Frequency("12");
        assertTrue(frequency.equals(12));
    }

    @Test
    public void test_toString_never() {
        Frequency frequency = new Frequency("-1");
        String s = frequency.toString();
        assertTrue(s.equals("-1"));
    }

    @Test
    public void test_toString_always() {
        Frequency frequency = new Frequency("0");
        String s = frequency.toString();
        assertTrue(s.equals("0"));
    }

    @Test
    public void test_toString_value() {
        Frequency frequency = new Frequency("12");
        String s = frequency.toString();
        assertTrue(s.equals("12"));
    }
}