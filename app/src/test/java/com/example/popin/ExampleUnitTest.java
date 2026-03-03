package com.example.popin;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Calculator calc = new Calculator();
        assertEquals(4, calc.add(2, 2)); // This actually "covers" code in src/main
    }
}