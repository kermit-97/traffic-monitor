package trafficmonitor.test;

import org.junit.*;
import trafficmonitor.dati.Coordinate;

import static java.lang.Double.parseDouble;

public class CoordinateTest {
    static Coordinate posizione ;
    @BeforeClass
    public static void setUpBeforClass() throws Exception {
        System.out.println("--- Start test Coordinate ---");
    }

    @AfterClass
    public static void tearDownAfterClass() throws  Exception {
        System.out.println("--- End Test ---");
    }

    @Before
    public void setUp() throws Exception {
        posizione = new Coordinate("45.800047","9.090065");
    }

    @After
    public void tearDown() throws Exception {
        posizione = null ;
    }

    @Test
    public void testPuntoMedio() throws Exception{
        System.out.print("Test funzione punto medio ");
        Coordinate puntoMedio = posizione.puntoMedio(new Coordinate("45.799053","9.090840"));
        Coordinate puntoMedioExpected = new Coordinate("45.799549","9.090452");
        Assert.assertEquals(parseDouble(puntoMedioExpected.getLatitudine()),parseDouble(puntoMedio.getLatitudine()),0.000009);
        Assert.assertEquals(parseDouble(puntoMedioExpected.getLongitudine()),parseDouble(puntoMedio.getLongitudine()),0.000009);
        System.out.println("- OK");
    }
}
