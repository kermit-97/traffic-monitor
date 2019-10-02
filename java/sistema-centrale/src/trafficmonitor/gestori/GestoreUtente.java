package trafficmonitor.gestori;

import trafficmonitor.dati.Coordinate;
import trafficmonitor.utenti.IRemoteApplicazione;
import trafficmonitor.utenti.Utente;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestoreUtente extends UnicastRemoteObject implements IGestoreUtente, IRemoteGestoreUtente
{
    private static GestoreUtente istanza;

    static {
        try {
            istanza = new GestoreUtente();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static GestoreUtente getInstance() {
        return istanza;
    }

    private GestoreUtente() throws RemoteException {
        this.listaUtenti = DatabaseManager.getInstance().getUtenti();
    }

    public GestoreUtente(ArrayList<Utente> listaUtenti) throws RemoteException {
        this.listaUtenti = listaUtenti;
    }

    ArrayList<Utente> listaUtenti;

    public ArrayList<Utente> getListaUtenti() {
        return listaUtenti;
    }

    public void setListaUtenti(ArrayList<Utente> listaUtenti) {
        this.listaUtenti = listaUtenti;
    }

    /**
     * @param username indica l'utente di cui aggionrnare la posizione
     * @param nuovaPosizione indica la nuova posizione
     */
    public void aggiornaPosizioneUtente(String username, Coordinate nuovaPosizione) {
        System.out.println("GestoreUtente:aggiornaPosizioneUtente");
        //filtro sulla lista uenti tramite username e aggiorno posizione
        try{
            List<Utente> listaFiltrata = listaUtenti.stream().filter(s -> username.equals(s.getUsername())).collect(Collectors.toList());
            if(!listaFiltrata.isEmpty()) {
                listaFiltrata.get(0).setPosizione(nuovaPosizione);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Per tutti gli utenti salvati chiede un aggiornamento della loro posizione
     */
    private void richiediPosizioneUtente() {
        System.out.println("GestoreUtente:richiediPosizioneUtente");
        for (Utente u : listaUtenti){
            try {
                String hostName = "/Applicazione";
                String portNum = u.getPort() ;
                String ipAddress = u.getIp() + ":";
                //String ipAddress = "192.168.137.128:";
                String registryUrl = "rmi://"+ ipAddress + portNum + hostName ;

                //istanzia interfaccia gestore remoto
                IRemoteApplicazione iRemoteApplicazione = (IRemoteApplicazione) Naming.lookup(registryUrl);

                //chiamata funzione
                iRemoteApplicazione.inviaPosizione();
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Controlla quali utenti sono nel raggio di notifica
     * @param centro indica il punto dal quale calcolare la distanza
     * @return la lista di utente entro una certa distanza da centro
     */
    @Override
    public ArrayList<Utente> controllaUtentiNelRaggio(Coordinate centro) {
        System.out.println("GestoreUtente:controllaUtentuNelRaggio");
        richiediPosizioneUtente();
        ArrayList<Utente> utentiNelRaggio = new ArrayList<>();
        for (Utente u:listaUtenti){
            // calcola distanza posizione utente dal centro del segmento sul quale è avvenuto l'evento
            Double distanza = u.getPosizione().calcolaDistanzaMetri(centro);
            if ( distanza <= GestoreNotifica.raggio){
                utentiNelRaggio.add(u);
            }
        }
        return utentiNelRaggio ;
    }

    /**
     * Riceve l'IP che sta utilizzando un utente su una applicazione
     * @param username indica l'utente che sta usando l'applicazione
     * @param ip indica l'indirizzo IP
     * @param portNum indica la porta utilizzata dall'RMI
     * @throws RemoteException
     */
    @Override
    public void riceviIPPortUtente(String username, String ip, String portNum) throws RemoteException {
        System.out.println("GestoreUtente:riceviIPPortUtente");
        try{
            List<Utente> listaFiltrata = listaUtenti.stream().filter(s -> username.equals(s.getUsername())).collect(Collectors.toList());
            if(!listaFiltrata.isEmpty()) {
                // salvo l'IP ricevuto
                listaFiltrata.get(0).setIp(ip);
                listaFiltrata.get(0).setPort(portNum);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
