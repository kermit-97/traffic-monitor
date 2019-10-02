package trafficmonitor.gestori;

import trafficmonitor.dati.DatoCentralina;
import trafficmonitor.centraline.Centralina;
import trafficmonitor.dati.DatoCentralinaStradale;
import trafficmonitor.dati.SegmentoStradale;

import java.rmi.*;
import java.rmi.server.*;
import java.util.*;
import java.util.stream.Collectors;

public class GestoreCentralina extends UnicastRemoteObject implements IGestoreCentralina, IRemoteGestoreCentralina {

    private static GestoreCentralina istanza;

    static int timer_diagnostica = 150;

    static {
        try {
            istanza = new GestoreCentralina();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static GestoreCentralina getInstance() {
        return istanza;
    }

    private GestoreCentralina() throws  RemoteException {
        this.listaCentraline = DatabaseManager.getInstance().getCentraline();

        timerDiagnostica();
    }

    public GestoreCentralina(ArrayList<Centralina> listaCentraline) throws RemoteException {
        this.listaCentraline = listaCentraline;
    }

    private ArrayList<Centralina> listaCentraline;

    private Timer timer = new Timer();

    public ArrayList<Centralina> getListaCentraline() {
        return listaCentraline;
    }

    public void setListaCentraline(ArrayList<Centralina> listaCentraline) {
        this.listaCentraline = listaCentraline;
    }

    /**
     * Controlla, per ogni centralina, se l'attesa massima dopo l'ultimo invio è già passata
     * se è cosi chiama il metodo segnalaCentralinaGuasta che si occupa di
     * modificare gli attribuiti della centralina
     */
    private void diagnostica() {
        System.out.println("GestoreCentralina:diagnostica");
        for (Centralina c : listaCentraline) {
            Date curr = new Date();
            // se è passato il tempo di attesa massimo dall'ultimo invio e la centralina è attiva, la segnalo come guasta
            if ((curr.getTime() > c.getUltimoInvio().getTime() + (c.getAttesaMassima()*1000)) && c.getAttivo()) {
                segnalaCentralinaGuasta(listaCentraline.indexOf(c));
                // update centralina guasta nel database
                DatabaseManager.getInstance().updateFunzionamentoCentralina(c.getCodSeriale(),false);
            }
        }
    }

    /**
     * Cambia il valore che indica se la centralina è guasta
     * @param index indica quale centralina segnare come guasta
     */
    private void segnalaCentralinaGuasta(int index) {
        System.out.println("GestoreCentralina:segnalaCentralinaGuasta");
        listaCentraline.get(index).setAttivo(false);

        GestoreSegmentoStradale.getInstance().aggiornaIndiceDiFlusso(0, listaCentraline.get(index).getSegmentoStradale());
    }

    /**
     * Cambia il valore della centralina che indica se è funzionante
     * @param codSeriale indica la centralina da segnare come attiva
     */
    private void segnalaCentralinaFunzionante(String codSeriale) {
        System.out.println("GestoreCentralina:segnalaCentralinaFunzionante");
        List<Centralina> listaFiltrata = listaCentraline.stream().filter(c -> codSeriale.equals(c.getCodSeriale())).collect(Collectors.toList());
        try {
            if (!(listaFiltrata.isEmpty())) {
                if (!(listaFiltrata.get(0).getAttivo())) {
                    listaFiltrata.get(0).setAttivo(true);
                    // update centralina attiva nel database
                    DatabaseManager.getInstance().updateFunzionamentoCentralina(codSeriale, true);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Quando ricevo un nuovo dato da una centralina, aggiorno l'ultimo invio da parte di questa centralina
     * Lo aggiorno sia nel DB che nella lista del gestore
     * @param dato indica il dato ricevuto dalla centralina
     */
    private void aggiornaUltimoInvioCentralina(DatoCentralinaStradale dato) {
        System.out.println("GestoreCentralina:aggiornaUltimoInvioCentralina");
        //aggiorno ultimo invio nel database
        DatabaseManager.getInstance().updateUltimoInvioCentralina(((DatoCentralinaStradale) dato).getCodCentralina(),dato.getDataOra());
        //aggiornamento ultimo invio nel gestore
        try {
            List<Centralina> listaFiltrata = listaCentraline.stream().filter(s -> dato.getCodCentralina().equals(s.getCodSeriale())).collect(Collectors.toList());
            if (!listaFiltrata.isEmpty()) {
                listaFiltrata.get(0).setUltimoInvio(dato.getDataOra());
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Ogni volta che ricevo un dato dalla centralina, esso contiente un campo di attesa massima da associare alla centralina
     * Questo indica il tempo massimo entro il quale dovrei ricevere un nuovo dato dalla centralina, altrimenti potrebbe esserci un guasto
     * @param dato indica il dato ricevuto dalla centralina
     */
    private void aggiornaAttesaMassima(DatoCentralinaStradale dato) {
        System.out.println("GestoreCentralina:aggiornaAttesaMassima");
        try{
            List<Centralina> listaFiltrata = listaCentraline.stream().filter(s -> dato.getCodCentralina().equals(s.getCodSeriale())).collect(Collectors.toList());
            if(!listaFiltrata.isEmpty()) {
                listaFiltrata.get(0).setAttesaMassima(dato.getAttesaMassima());
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inoltra il dato ricevuto dalla centralina al GestoreDato, che si occupa della sua elaborazione
     * @param dato indica il dato ricevuto dalla centralina
     */
    private void inoltraDatoCentralina(DatoCentralinaStradale dato) {
        System.out.println("GestoreCentralina:inoltraDatoCentralina");
        GestoreDato.getInstance().riceviDatoCentralina(dato);
    }

    /**
     * Ogni timer_diagnostica secondi fa partire la funzione di diagnostica
     */
    private void timerDiagnostica(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("------------------");
                System.out.println("------------------");
                System.out.println("------------------");
                System.out.println("------------------");

                diagnostica();
            }
        }, timer_diagnostica*1000, timer_diagnostica*1000);
    }

    /**
     * @param segmento segmento da controllare
     * @return true se il segmento è monitorato da una centralina attiva, false altrimenti
     */
    @Override
    public boolean isPresenteCentralina(SegmentoStradale segmento){
        System.out.println("GestoreCentralina:isPresenteCentralina");
        try{
            String codS = segmento.getCodSegmento();
            List<Centralina> listaFiltrata = listaCentraline.stream().filter(c -> codS.equals(c.getSegmentoStradale().getCodSegmento())).collect(Collectors.toList());
            if (listaFiltrata.isEmpty()) {
                return false;
            } else {
                return listaFiltrata.get(0).getAttivo() ;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    /**
     * Metodo chiamato da remoto tramite RMI, riceve un dato dalla centralina
     * @param dato indica il dato della centralina
     * @throws RemoteException
     */
    @Override
    public void riceviDatoCentralina(DatoCentralina dato) throws RemoteException {
        System.out.println("GestoreCentralina:riceviDatoCentralina");
        try {
            DatoCentralinaStradale datoStradale = (DatoCentralinaStradale) dato;
            segnalaCentralinaFunzionante(datoStradale.getCodCentralina());
            // aggiornamento dati
            aggiornaUltimoInvioCentralina(datoStradale);
            aggiornaAttesaMassima(datoStradale);
            // inoltro il dato per l'elaborazione
            inoltraDatoCentralina(datoStradale);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metodo chiamato da remoto tramite RMI, invia la lista delle centraline
     * @return la lista delle centraline
     * @throws RemoteException
     */
    @Override
    public ArrayList<Centralina> inviaStatoCentraline() throws RemoteException {
        System.out.println("GestoreCentralina:inviaStatoCentraline");
        return listaCentraline;
    }
}
