package trafficmonitor.test;

import org.junit.*;

import trafficmonitor.dati.RMIConfig;
import trafficmonitor.dati.SegnalazioneUtente;
import trafficmonitor.gestori.IRemoteGestoreDato;
import trafficmonitor.utenti.Applicazione;

import java.rmi.Naming;

public class ApplicazioneTest {
    private static Applicazione applicazione ;

    @BeforeClass
    public static void setUpBeforClass() throws Exception {
        System.out.println("--- Start test Applicazione ---");
    }

    @AfterClass
    public static void tearDownAfterClass() throws  Exception {
        System.out.println("--- End Test ---");
    }

    @Before
    public void setUp() throws Exception {
        applicazione = new Applicazione();

    }

    @After
    public void tearDown() throws Exception {
        applicazione = null ;
    }

    @Test
    public void testRMI() throws Exception{
        // rmi test collegamento a GestoreDato
        try {
            //String hostName = "/GestoreDato";
            //String portNum = "1234" ;
            String registryUrl = "rmi://" + RMIConfig.GESTOREDATO.getIP() + RMIConfig.GESTOREDATO.getPort() + RMIConfig.GESTOREDATO.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreDato iGestoreDato =(IRemoteGestoreDato) Naming.lookup(registryUrl);

            SegnalazioneUtente segnalazioneUtente = new SegnalazioneUtente();
            //chiamata funzione
            iGestoreDato.riceviSegnalazione(segnalazioneUtente);
            System.out.println("Collegato a " + RMIConfig.GESTOREDATO);
        }
        catch (Exception ex){
            System.out.println("Errore collegamento a " + RMIConfig.GESTOREDATO);
        }
    }
}