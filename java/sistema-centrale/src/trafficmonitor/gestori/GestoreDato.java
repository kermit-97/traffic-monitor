package trafficmonitor.gestori;

import trafficmonitor.dati.*;

import java.rmi.server.*;
import java.rmi.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GestoreDato extends UnicastRemoteObject implements IGestoreDato, IRemoteGestoreDato
{
    private static GestoreDato istanza;

    static {
        try {
            istanza = new GestoreDato();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static GestoreDato getInstance() {
        return istanza;
    }

    private GestoreDato() throws RemoteException{ //istanze manuali
        listaSegnalazioni = new ArrayList<ArrayList<SegnalazioneUtente>>();
    }

    static int n_segnalazioni = 3;

    static int timer_validitaSegnalazione = 180;

    private Timer timer = new Timer();

    private ArrayList<ArrayList<SegnalazioneUtente>> listaSegnalazioni;

    public ArrayList<ArrayList<SegnalazioneUtente>> getListaSegnalazioni() {
        return listaSegnalazioni;
    }

    public void setListaSegnalazioni(ArrayList<ArrayList<SegnalazioneUtente>> listaSegnalazioni) {
        this.listaSegnalazioni = listaSegnalazioni;
    }

    /**
     * Dopo che il gestore riceve il dato dalla centralina stradale, questo metodo si occupa di elaborarlo
     * Il dato contiene un indice di flusso relativo ad un segmento e le coordinate ad esso associate
     * Recupera prima le coordinate e l'indice dal dato poi ricava il segmento dalle coordinate
     * Infine richiede l'aggiornamento del segmento con il nuovo indice
     *
     * @param dato rappresenta il dato inviato dalla centralina
     */
    private void elaboraDatoCentralina(DatoCentralinaStradale dato) {
        System.out.println("GestoreDato:elaboraDatoCentralina");
        Coordinate coord = dato.getCoordinate();
        int indice = dato.getIndiceDiFlusso();

        SegmentoStradale s = GestoreSegmentoStradale.getInstance().getSegmentoDaCoord(coord);

        GestoreSegmentoStradale.getInstance().aggiornaIndiceDiFlusso(indice, s);
    }

    /**
     * Dopo che il gestore riceve il dato dall'utente, questo metodo si occupa di elaborarlo
     * Il dato contiene un indice di flusso e delle coordinate. Dalle coordinate si può risalire al segmento associato alla segnalazione
     * Se la segnalazione è relativa ad un segmento che viene già monitorato da una centralina, allora viene ignorata, in quanto fornirebbe informazioni superflue
     * Ogni segnalazione ha un tempo di vita dopo il quale viene scartata (tempo = timer_validitaSegnalazione)
     * Prima di aggiornare l'indice di flusso di un segmento ci si aspetta di avere almeno un tot di segnalazioni, su cui si calcola la media (tot = n_segnalazioni)
     * Le segnalazioni vengono salvate in una struttura dinamica, dove su ogni riga raggruppo le segnalazioni per il segmento a cui sono associate
     * In questo modo è comodo verificare se possiedo abbastanza segnalazioni per segmento per poter aggiornare l'indice di flusso
     *
     * ES: (dove s1, s2, sono le segnalazioni)
     * Via Anzani    -> s1  s4  s5  .
     * Via Valleggio -> s2  .
     * Via dei Mille -> s3 s6  .
     *
     * @param dato rappresenta il dato inviato dall'utente tramite una segnalazione
     */
    private void elaboraDatoSegnalazione(SegnalazioneUtente dato) {
        System.out.println("GestoreDato:elaboraDatoSegnalazione");
        boolean found = false;
        int i = 0;

        Coordinate coord = dato.getCoordinate();
        // ricavo il segmento stradale al quale è associata la segnalazione
        SegmentoStradale s = GestoreSegmentoStradale.getInstance().getSegmentoDaCoord(coord);
        System.out.println("  --> segmento associato alla segnalazione cod: " + s.getCodSegmento() + ", via: " + s.getNome());
        // elaboro la segnalazione solo se il segmento non è già monitorato da una centralina funzionante
        if (!(GestoreCentralina.getInstance().isPresenteCentralina(s))) {
            if (listaSegnalazioni.isEmpty()) {
                System.out.println("  --> non esistono altre segnalazione valide su questo segmento");
                ArrayList<SegnalazioneUtente> temp = new ArrayList<SegnalazioneUtente>();
                temp.add(dato);
                listaSegnalazioni.add(temp);
                timerSegnalazione(dato);                     // timer per il decadimento della segnalazion
            } else {
                // scorro per la struttura per vedere se esistono già delle segnalazioni associate al segmento della nuova segnalazione
                for (ArrayList<SegnalazioneUtente> e : listaSegnalazioni) {
                    SegmentoStradale s2 = GestoreSegmentoStradale.getInstance().getSegmentoDaCoord(e.get(0).getCoordinate());
                    if (s.equals(s2)) {
                        System.out.println("  --> esistono altre segnalazione valide su questo segmento");
                        e.add(dato);
                        found = true;
                        i = listaSegnalazioni.indexOf(e);
                        timerSegnalazione(dato);             // timer per il decadimento della segnalazione
                    }
                }
                // se non esistono segnalazioni associate al segmento della nuova segnalazione la aggiungo in una nuova riga
                if (!found) {
                    System.out.println("  --> non esistono altre segnalazione valide su questo segmento");
                    ArrayList<SegnalazioneUtente> temp = new ArrayList<SegnalazioneUtente>();
                    temp.add(dato);
                    listaSegnalazioni.add(temp);
                    i = listaSegnalazioni.size() - 1;        // salvo l'indice della riga in cui ho aggiunto la segnalazione
                    timerSegnalazione(dato);                 // timer per il decadimento della segnalazion
                }
            }

            // prendo la riga nella quale ho aggiunto la nuova segnalazione
            ArrayList<SegnalazioneUtente> e = listaSegnalazioni.get(i);
            // controllo se ho abbastanza segnalazioni per stimare un indice di traffico
            if (e.size() >= this.n_segnalazioni) {
                // calcolo l'indice di traffico facendo una media tra i valori delle segnalazioni
                int somma = 0;
                for (SegnalazioneUtente su : e) {
                    somma += su.getTipoSegnalazione().getIndiceDiTraffico();
                }
                // aggiorno l'indice di traffico relativo al segmento
                System.out.println("  --> ho abbastanza segnalazioni per stimare un indice di flusso sul segmento");
                GestoreSegmentoStradale.getInstance().aggiornaIndiceDiFlusso(somma / e.size(), GestoreSegmentoStradale.getInstance().getSegmentoDaCoord(e.get(0).getCoordinate()));
            }
        }
    }

    /**
     * Riceve il dato della segnalazione
     * Gestisce quindi le informazioni ricevute, inoltrando al metodo elaboraDatoSegnalazione
     *
     * @param segnalazione rappresenta il dato inviato dall'utente tramite una segnalazione
     */
    @Override
    public void riceviSegnalazione(SegnalazioneUtente segnalazione) throws RemoteException {
        System.out.println("GestoreDato:riceviSegnalazione");
        // salvataggio segnalazione sul database
        DatabaseManager.getInstance().addSegnalazioneUtente(segnalazione);
        elaboraDatoSegnalazione(segnalazione);
    }

    /**
     * Riceve il dato della centralina
     * Gestisce quindi le informazioni ricevute, inoltrando al metodo elaboraDatoCentralina
     *
     * @param dato rappresenta il dato inviato dalla centralina
     */
    @Override
    public void riceviDatoCentralina(DatoCentralina dato) {
        System.out.println("GestoreDato:riceviDatoCentralina");
        try {
            DatoCentralinaStradale casted = (DatoCentralinaStradale) dato;
            // salvataggio dato centralina sul database
            DatabaseManager.getInstance().addDatoCentralina(casted);
            elaboraDatoCentralina(casted);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metodo chiamato da remoto tramite RMI, riceve un datoPosizione e lo salva sul DB poi avvisa il gestore utente che ha ricevuto una nuova poszione
     * @param dato indica il dato di posizione ricevuto
     * @throws RemoteException
     */
    @Override
    public void riceviPosizioneUtente(DatoPosizione dato) throws RemoteException {
        System.out.println("GestoreDato:riceviPosizioneUtente");
        //salvo dato posizione nel database
        DatabaseManager.getInstance().addDatoPosizione(dato);
        //inoltro al gestoreUtenti
        GestoreUtente.getInstance().aggiornaPosizioneUtente(dato.getUsername(),dato.getCoordinate());
    }

    /**
     * Metodo chiamato da remoto tramite RMI, invia lo storico, ovvero la lista di tutti i dati delle centraline e le segnalazioni utente ricevute
     * @return lo storico
     * @throws RemoteException
     */
    @Override
    public  ArrayList<Dato> inviaStorico() throws RemoteException {
        System.out.println("GestoreDato:inviaStorico");
        ArrayList<Dato> listaDati = new ArrayList<Dato>();
        // richiesta lista dati dal database
        listaDati.addAll(DatabaseManager.getInstance().getDatiCentralina());        // dati dalle centraline
        listaDati.addAll(DatabaseManager.getInstance().getSegnalazioni());          // dati dalle segnalazioni

        return  listaDati;
    }

    /**
     * Il timer si occupa di rimuovere una segnalazione dalla struttura dopo un tot di tempo (timer_validitaSegnalazione)
     * @param s indica la segnalazione da rimuovere
     */
    private void timerSegnalazione(SegnalazioneUtente s){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("GestoreDato:timerSegnalazione");
                System.out.println("  --> è scaduto il tempo di validità della segnalazione di " + s.getUsername());
                int i = 0;
                // cerco la riga su cui è salvata la segnalazione in listaSegnalazioni
                for (ArrayList<SegnalazioneUtente> e : listaSegnalazioni) {
                    if (GestoreSegmentoStradale.getInstance().getSegmentoDaCoord(e.get(0).getCoordinate()).getCodSegmento().equals(GestoreSegmentoStradale.getInstance().getSegmentoDaCoord(s.getCoordinate()).getCodSegmento())){
                        break;
                    }
                    i++;
                }
                // estraggo la riga
                ArrayList<SegnalazioneUtente> temp = listaSegnalazioni.get(i);
                // rimuovo la segnalazione
                temp.remove(temp.indexOf(s));
                // se era l'ultima segnalazione sulla riga, allora elimino tutta la riga dalla listaSegnalazioni
                if (temp.isEmpty())
                    listaSegnalazioni.remove(i);
            }
        }, timer_validitaSegnalazione*1000);
    }
}