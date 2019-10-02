package trafficmonitor.test;

import org.junit.*;
import trafficmonitor.centraline.CentralinaStradale;
import trafficmonitor.dati.Coordinate;
import trafficmonitor.dati.SegmentoStradale;

import java.util.Date;

import static java.lang.Double.parseDouble;

public class CentralinaStradaleTest {
    static CentralinaStradale centralina ;
    @BeforeClass
    public static void setUpBeforClass() throws Exception {
        System.out.println("--- Start test Centralina Stradale ---");
    }

    @AfterClass
    public static void tearDownAfterClass() throws  Exception {
        System.out.println("--- End Test ---");
    }

    @Before
    public void setUp() throws Exception {
        Date data = new Date();
        Coordinate inizio = new Coordinate("45.800047","9.090065");
        Coordinate fine = new Coordinate("45.799053","9.090840");
        Coordinate puntoMedio = inizio.puntoMedio(fine);
        SegmentoStradale segmento= new SegmentoStradale("Via Leone Leoni","S000001",inizio,fine,50,50);
        centralina = new CentralinaStradale(10,10,50,30,"c000001",true, true,puntoMedio,segmento,30,data);
    }

    @After
    public void tearDown() throws Exception {
        centralina = null ;
    }

    @Test
    public void testCalcolaIndice() throws Exception{
        System.out.println("Test funzione calcolaIndice");

        centralina.calcolaIndice();
        Assert.assertEquals(80,centralina.getIndiceDiFlusso());

        System.out.println("- OK");
    }
}
