package trafficmonitor.gestori;

import trafficmonitor.dati.Coordinate;
import trafficmonitor.dati.SegmentoStradale;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.stream.Collectors;

public class GestoreSegmentoStradale extends UnicastRemoteObject implements IGestoreSegmentoStradale,IRemoteGestoreSegmentoStradale {

    private static GestoreSegmentoStradale istanza;

    static {
        try {
            istanza = new GestoreSegmentoStradale();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static GestoreSegmentoStradale getInstance() {
        return istanza;
    }

    private GestoreSegmentoStradale() throws RemoteException {
        this.listaSegmentiStradali = DatabaseManager.getInstance().getSegmentiStradali();

    }

    public GestoreSegmentoStradale(ArrayList<SegmentoStradale> listaSegmentiStradali) throws RemoteException {
        this.listaSegmentiStradali = listaSegmentiStradali;
    }

    static int timer_decadimentoIndice = 300;       // tempo in secondi dopo il quale viene azzerato l'indice su un segmento non monitorato da centralina

    ArrayList<SegmentoStradale> listaSegmentiStradali;

    public ArrayList<SegmentoStradale> getListaSegmentiStradali() {
        return listaSegmentiStradali;
    }

    public void setListaSegmentiStradali(ArrayList<SegmentoStradale> listaSegmentiStradali) {
        this.listaSegmentiStradali = listaSegmentiStradali;
    }

    /**
     * L'evento si verifica se avviene un cambiamento di fascia tra l'indice precedente e quello attuale
     * Se tale cambiamento è confermato anche nel nuovo indice ricevuto allora l'evento è confermato
     *
     * 0-30   fascia di traffico 1
     * 30-70  fascia di traffico 2
     * 70-100 fascia di traffico 3
     *
     * @param nuovoIndice indica il nuovo indice rilevato sul segmento
     * @param segmento indica il segmento al quale si fa riferimento
     */
    private void valutaEvento(int nuovoIndice, SegmentoStradale segmento) {
        System.out.println("GestoreSegmentoStradale:valutaEvento");
        int indicePrecedente = segmento.getIndiceDiFlussoPrecedente();
        int indiceAttuale = segmento.getIndiceDiFlussoAttuale();
        if(calcolaFascia(indicePrecedente) < calcolaFascia(indiceAttuale)) {
            // indice precedente è inferiore a quello attuale, controllo i nuovo indice in cerca dell'evento
            if(calcolaFascia(indiceAttuale) <= calcolaFascia(nuovoIndice)) {
                // gestisco evento
                GestoreNotifica.getInstance().generaNotificaDaEvento(segmento,nuovoIndice);
            }
        }
    }

    /**
     * Dato un indice di flusso stabilisce a quale fascia di traffico appartiene
     * @param indice indica il dato da elaborare per ottenere la fascia
     * @return indica la fascia a cui appartiene l'indice
     */
    private int calcolaFascia(int indice) {
        System.out.println("GestoreSegmentoStradale:calcolaFascia");
        if (indice >= 0 && indice <= 30) {
            return 1;
        }
        if (indice > 30 && indice <= 70) {
            return 2;
        }
        if (indice > 70 && indice <= 100) {
            return 3;
        }
        return 0;
    }

    /**
     * Il timer si occupa di azzerare l'indice di flusso attuale di un segmento NON monitorato da una centralina, dopo un periodo di tempo (timer_validitaSegnalazione)
     * @param s segmento su cui azzerare l'idice
     */
    Timer timer = new Timer();
    private void decadimentoIndiceSegmento(SegmentoStradale s){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // controlla che non sia arrivata una nuova segnalazione che ha aggiornato il dato sul segmento mentre il timer era in funzione
                if(!((s.getUltimoAggiornamento().getTime() + (GestoreSegmentoStradale.timer_decadimentoIndice*1000)) > new Date().getTime())){
                    System.out.println("GestoreSegmentoStradale:decadimento indice segmento --> è scaduto il periodo di validita del segmento " + s.getCodSegmento() + ", " + s.getNome());
                    s.setIndiceDiFlussoPrecedente(s.getIndiceDiFlussoAttuale());
                    s.setIndiceDiFlussoAttuale(0);
                    DatabaseManager.getInstance().updateIndiceDiFlussoSegmentoStradale(s.getCodSegmento(), 0);
                }
            }
        }, this.timer_decadimentoIndice*1000);
    }

    /**
     * Metodo chiamato da remoto tramite RMI, restituisce tutti i segmenti stradali
     * @return la lista dei segmenti stradali
     * @throws RemoteException
     */
    @Override
    public ArrayList<SegmentoStradale> inviaSegmentiStradali() throws RemoteException {
        System.out.println("GestoreSegmentoStradale:inviaSegmentiStradali");
        return listaSegmentiStradali;
    }

    /**
     * @param codSegmento indica il codice del segmento
     * @return il segmento associato al codice
     */
    @Override
    public SegmentoStradale getSegmentoDaCodice(String codSegmento) {
        System.out.println("GestoreSegmentoStradale:getSegmentoDaCodice");
        try{
            List<SegmentoStradale> listaFiltrata = listaSegmentiStradali.stream().filter(s -> codSegmento.equals(s.getCodSegmento())).collect(Collectors.toList());
            if(!listaFiltrata.isEmpty()) {
                return listaFiltrata.get(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Partendo da una generica coordinata ritorna il segmento stradale associato.
     * Il segmento associato è quello che ha distanza minore  tra il punto medio del segmento e le coordinate passate
     * @param coord indica le coordinate da elaborare
     * @return ritorna il segmento associato allle coordinate
     */
    @Override
    public SegmentoStradale getSegmentoDaCoord(Coordinate coord) {
        System.out.println("GestoreSegmentoStradale:getSegmentoDaCoord");
        Double minDistanza = Double.MAX_VALUE;
        SegmentoStradale segmentoVicino = null;

        Coordinate pMedio;
        // cerco il segmento con distanza minima tra il suo punto medio e le coordinate
        for (SegmentoStradale s:listaSegmentiStradali) {
            pMedio=s.puntoMedio();
            if(minDistanza>pMedio.distanza(coord)){
                minDistanza = pMedio.distanza(coord);
                segmentoVicino = s;
            }
        }
        return segmentoVicino;
    }

     /**
     * Viene chiamata dal gestore dato, si occupa di valutare l'evento
     * Fa partire il timer di decadenza per i segmenti non monitorati dalle centraline
     * Salva il nuovo indice sul segmento e nel database.
     * @param indice indica il nuovo indice di flusso
     * @param segmento indica il segmento a cui si riferisce
     */
    @Override
    public void aggiornaIndiceDiFlusso(int indice, SegmentoStradale segmento) {
        System.out.println("GestoreSegmentoStradale:aggiornaIndiceDiFlusso");
        // valuto se c'è un evento da notificare
        valutaEvento(indice, segmento);     // il segmento cambia prima che siano arrivate tutte le notiche?
        // controllo se è un segmento monitorato da una centralina
        if(!GestoreCentralina.getInstance().isPresenteCentralina(segmento)) {
            // non è presente, è una segnalazione di un utente quindi avrà una decadenza
            decadimentoIndiceSegmento(segmento);
        }
        // salvo il nuovo indice facedo lo swap tra gli indici precedenti
        Date data = new Date();
        int temp = segmento.getIndiceDiFlussoAttuale();
        segmento.setUltimoAggiornamento(data);
        segmento.setIndiceDiFlussoAttuale(indice);
        segmento.setIndiceDiFlussoPrecedente(temp);
        //update indice di flusso nel Database
        DatabaseManager.getInstance().updateIndiceDiFlussoSegmentoStradale(segmento.getCodSegmento(),segmento.getIndiceDiFlussoAttuale());
    }
}
