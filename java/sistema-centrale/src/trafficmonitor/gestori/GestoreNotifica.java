package trafficmonitor.gestori;

import trafficmonitor.dati.Coordinate;
import trafficmonitor.dati.SegmentoStradale;
import trafficmonitor.dati.TipoSegnalazione;
import trafficmonitor.utenti.IRemoteApplicazione;
import trafficmonitor.utenti.Notifica;
import trafficmonitor.utenti.Utente;

import java.rmi.Naming;
import java.util.ArrayList;

public class GestoreNotifica implements IGestoreNotifica {
    private static GestoreNotifica istanza = new GestoreNotifica();

    private GestoreNotifica() { }

    public static GestoreNotifica getInstance() {
        return istanza;
    }

    static double raggio = 250;     // raggio di notifica in metri

    /**
     * Riceve la notifica generata dalla funzione generaNotificaDaEvento, richiede al gestore utenti gli utenti
     * ai quali deve inviare la notifica (ossia quelli che si trovano nel raggio di notifica) ed invia la notifica con RMI
     * @param notifica indica la notifica da inviare
     */
    private void inviaNotifica(Notifica notifica, SegmentoStradale segmento) {
        System.out.println("GestoreNotifica:inviaNotifica");
        Coordinate puntoMedio = segmento.puntoMedio() ;
        // richiedere utenti a gestore utenti
        ArrayList<Utente> utentiNelRaggio = GestoreUtente.getInstance().controllaUtentiNelRaggio(puntoMedio);
        // invia notifica con rmi
        for (Utente u :utentiNelRaggio) {
            //invoca metodo remoto
            System.out.println("  --> invio notifica a " + u.getUsername());
            try {
                String hostName = "/Applicazione";
                String portNum = u.getPort() ;
                String ipAddress = u.getIp() + ":";
                String registryUrl = "rmi://"+ ipAddress + portNum + hostName ;

                // istanzia interfaccia gestore remoto
                IRemoteApplicazione iRemoteApplicazione = (IRemoteApplicazione) Naming.lookup(registryUrl);

                // chiamata funzione
                iRemoteApplicazione.riceviNotifica(notifica);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Genera una notifica in seguito ad un evento verificatosi su un segmento stradale
     * @param segmento indica il segmento sul quale si è verificato l'evento
     * @param indiceDiFlusso indica lo stato del traffico in quel segmento
     */
    @Override
    public void generaNotificaDaEvento(SegmentoStradale segmento, int indiceDiFlusso) {
        System.out.println("GestoreNotifica:generaNotificaDaEvento");
        // a seconda dell'indice, la notifica avrà un testo diverso
        String testoNotifica = "Aggiornamento traffico in " + segmento.getNome() + ": " ;
        if(indiceDiFlusso>=0 && indiceDiFlusso <=30){
            testoNotifica += TipoSegnalazione.STRADALIBERA.getDescrizione();
        }
        if(indiceDiFlusso>30 && indiceDiFlusso <=70){
            testoNotifica += TipoSegnalazione.TRAFFICOSCORREVOLE.getDescrizione();
        }
        if(indiceDiFlusso>70 && indiceDiFlusso <=100){
            testoNotifica += TipoSegnalazione.TRAFFICOELEVATO.getDescrizione();
        }
        // genero la notifica
        Notifica notifica = new Notifica(testoNotifica);
        inviaNotifica(notifica, segmento);
    }
}
