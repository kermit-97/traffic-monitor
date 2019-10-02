package trafficmonitor.test;

import org.junit.*;
import trafficmonitor.centraline.Centralina;
import trafficmonitor.dati.Dato;
import trafficmonitor.dati.RMIConfig;
import trafficmonitor.dati.SegmentoStradale;
import trafficmonitor.gestori.IRemoteGestoreCentralina;
import trafficmonitor.gestori.IRemoteGestoreDato;
import trafficmonitor.gestori.IRemoteGestoreSegmentoStradale;
import trafficmonitor.utenti.ControllerInterfacciaUtente;
import trafficmonitor.utenti.InterfacciaUtente;

import java.rmi.Naming;
import java.util.ArrayList;

public class InterfacciaUtenteTest {
    private static InterfacciaUtente interfacciaUtente ;

    @BeforeClass
    public static void setUpBeforClass() throws Exception {
        System.out.println("--- Start test Interfaccia Utente ---");
    }

    @AfterClass
    public static void tearDownAfterClass() throws  Exception {
        System.out.println("--- End Test ---");
    }

    @Before
    public void setUp() throws Exception {
        interfacciaUtente= new InterfacciaUtente();

    }

    @After
    public void tearDown() throws Exception {
        interfacciaUtente = null ;
    }

    @Test
    public void testRMI() throws Exception{
        ArrayList<SegmentoStradale> listaSegmenti;

        // rmi test collegamento a GestoreSegmentoStradale
        try {
            //String hostName = "/GestoreSegmentoStradale";
            //String portNum = "1234" ;
            String registryUrl = "rmi://" + RMIConfig.GESTORESEGMENTOSTRADALE.getIP() + RMIConfig.GESTORESEGMENTOSTRADALE.getPort() + RMIConfig.GESTORESEGMENTOSTRADALE.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreSegmentoStradale iGestoreSegmentoStradale =(IRemoteGestoreSegmentoStradale) Naming.lookup(registryUrl);

            //chiamata funzione
            listaSegmenti = iGestoreSegmentoStradale.inviaSegmentiStradali();
            System.out.println("Collegato a " + RMIConfig.GESTORESEGMENTOSTRADALE);
        }
        catch (Exception ex){
            System.out.println("Errore collegamento a " + RMIConfig.GESTORESEGMENTOSTRADALE);
        }

        ArrayList<Centralina> listaCentraline;
        // rmi test collegamento a GestoreCentralina
        try {
            //String hostName = "/GestoreCentralina";
            //String portNum = "1234" ;
            String registryUrl = "rmi://" + RMIConfig.GESTORECENTRALINA.getIP() + RMIConfig.GESTORECENTRALINA.getPort() + RMIConfig.GESTORECENTRALINA.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreCentralina iRemoteGestoreCentralina = (IRemoteGestoreCentralina) Naming.lookup(registryUrl);

            //chiamata funzione
            listaCentraline = iRemoteGestoreCentralina.inviaStatoCentraline();
            System.out.println("Collegato a " + RMIConfig.GESTORECENTRALINA);
        }
        catch(Exception ex){
            System.out.println("Errore collegamento a " + RMIConfig.GESTORECENTRALINA);
        }


        ArrayList<Dato> storico = new  ArrayList<Dato>();
        // rmi test collegamento a GestoreDato
        try {
            //String hostName = "/GestoreDato";
            //String portNum = "1234" ;
            String registryUrl = "rmi://" + RMIConfig.GESTOREDATO.getIP() + RMIConfig.GESTOREDATO.getPort() + RMIConfig.GESTOREDATO.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreDato iRemoteGestoreDato =(IRemoteGestoreDato)Naming.lookup(registryUrl);

            //chiamata funzione
            storico = iRemoteGestoreDato.inviaStorico();
            System.out.println("Collegato a " + RMIConfig.GESTORECENTRALINA);
        }
        catch (Exception ex){
            System.out.println("Errore collegamento a " + RMIConfig.GESTOREDATO);
        }
    }
}