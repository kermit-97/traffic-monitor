package trafficmonitor.utenti;

import trafficmonitor.centraline.Centralina;
import trafficmonitor.centraline.CentralinaStradale;
import trafficmonitor.dati.*;
import trafficmonitor.gestori.IRemoteGestoreCentralina;
import trafficmonitor.gestori.IRemoteGestoreDato;
import trafficmonitor.gestori.IRemoteGestoreSegmentoStradale;
import trafficmonitor.resource.Code;

import javax.swing.*;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Date;

public class InterfacciaUtente {

    private InterfacciaUtenteGUI view;

    public InterfacciaUtente(InterfacciaUtenteGUI view) {
        this.view = view;
        aggiornaInformazioni();
    }

    public InterfacciaUtente(){

    }

    private void mostraMappa() {
        //richiede i segmenti al Gestore Segmenti
        ArrayList<SegmentoStradale> listaSegmenti = new  ArrayList<SegmentoStradale>();
        ArrayList<Centralina> listaCentraline = new ArrayList<Centralina>();
        //invoca metodo remoto
        try {
            //String hostName = "/GestoreSegmentoStradale";
            //String portNum = "1234" ;
            String registryUrl = "rmi://" + RMIConfig.GESTORESEGMENTOSTRADALE.getIP() + RMIConfig.GESTORESEGMENTOSTRADALE.getPort() + RMIConfig.GESTORESEGMENTOSTRADALE.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreSegmentoStradale iGestoreSegmentoStradale =(IRemoteGestoreSegmentoStradale) Naming.lookup(registryUrl);

            //chiamata funzione
            listaSegmenti = iGestoreSegmentoStradale.inviaSegmentiStradali();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        try {
            //String hostName = "/GestoreCentralina";
            //String portNum = "1234" ;
            String registryUrl = "rmi://" + RMIConfig.GESTORECENTRALINA.getIP() + RMIConfig.GESTORECENTRALINA.getPort() + RMIConfig.GESTORECENTRALINA.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreCentralina iRemoteGestoreCentralina = (IRemoteGestoreCentralina) Naming.lookup(registryUrl);

            //chiamata funzione
            listaCentraline = iRemoteGestoreCentralina.inviaStatoCentraline();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        Code script = new Code(listaSegmenti, listaCentraline);
        view.getBrowser().loadURL(script.getCodice());
    }

    /**
     * Richiede l'invio dello storico al GestoreDato
     * Riceve in risposta una collection di dati che rappresenta lo storico delle situazioni di traffico
     * Mostra la collection nel form
     */
    private void mostraStorico()
    {
        // rmi
        ArrayList<Dato> storico = new  ArrayList<Dato>();
        //invoca metodo remoto
        try {
            //String hostName = "/GestoreDato";
            //String portNum = "1234" ;
            String registryUrl = "rmi://" + RMIConfig.GESTOREDATO.getIP() + RMIConfig.GESTOREDATO.getPort() + RMIConfig.GESTOREDATO.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreDato iRemoteGestoreDato =(IRemoteGestoreDato)Naming.lookup(registryUrl);

            //chiamata funzione
            storico = iRemoteGestoreDato.inviaStorico();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }


        DefaultListModel storicoCentraline = new DefaultListModel();
        DefaultListModel storicoSegnalazioni = new DefaultListModel();

        for (Dato d : storico) {
            if (d.getClass() == DatoCentralinaStradale.class) {
                try {
                    DatoCentralinaStradale casted = (DatoCentralinaStradale) d;
                    String line = casted.getDataOra() + " | Centralina:  " + casted.getCodCentralina() + " | Indice:  " + casted.getIndiceDiFlusso() + " | Vel. media:  " + casted.getVelocitaMedia() + " | N. macchine:  " + casted.getNumeroMacchine();
                    storicoCentraline.addElement(line);
                } catch (ClassCastException e) {
                    System.out.println("Expected: " + e);
                } catch (Exception e) {
                    System.out.println("Unexpected: " + e);
                }
            }
            else if (d.getClass() == SegnalazioneUtente.class) {
                try {
                    SegnalazioneUtente casted = (SegnalazioneUtente) d;
                    String line = casted.getDataOra() + " | Utente:  " + casted.getUsername() + " | Tipo segnalazione:  " + casted.getTipoSegnalazione().getEtichetta() + " | Coordinate:  " + casted.getCoordinate().getLatitudine() + ", " + casted.getCoordinate().getLongitudine();
                    storicoSegnalazioni.addElement(line);
                } catch (ClassCastException e) {
                    System.out.println("Expected: " + e);
                } catch (Exception e) {
                    System.out.println("Unexpected: " + e);
                }
            }
        }

        view.getJList_DatoCentralinaStradale().setModel(storicoCentraline);
        view.getJList_SegnalazioneUtente().setModel(storicoSegnalazioni);
    }

    /**
     * Richiede l'invio dell'insieme delle centraline al GestoreCentraline
     * Riceve in risposta una collection di dati che rappresenta l'insieme delle centraline
     * Mostra la collection nel form
     */
    private void mostraStatoCentraline() {
        // rmi
        ArrayList<Centralina> listaCentraline = new  ArrayList<Centralina>();
        //invoca metodo remoto
        try {
            //String hostName = "/GestoreCentralina";
            //String portNum = "1234" ;
            String registryUrl = "rmi://" + RMIConfig.GESTORECENTRALINA.getIP() + RMIConfig.GESTORECENTRALINA.getPort() + RMIConfig.GESTORECENTRALINA.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreCentralina iRemoteGestoreCentralina = (IRemoteGestoreCentralina) Naming.lookup(registryUrl);

            //chiamata funzione
            listaCentraline = iRemoteGestoreCentralina.inviaStatoCentraline();
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        DefaultListModel statoCentraline = new DefaultListModel();
        statoCentraline.addElement("  NOME CENTRALINA                    ATTIVA   ");
        statoCentraline.addElement("______________________________________________");
        for (Centralina c : listaCentraline) {
            try {
                String line = "           " + c.getCodSeriale() + "                            " + c.getAttivo();
                statoCentraline.addElement(line);
            } catch (ClassCastException e) {
                System.out.println("Expected: " + e);
            } catch (Exception e) {
                System.out.println("Unexpected: " + e);
            }
        }
        view.getJList_StatoCentralinaStradale().setModel(statoCentraline);
    }

    public void aggiornaInformazioni() {
        mostraStorico();
        mostraStatoCentraline();
        mostraMappa();
    }
}
