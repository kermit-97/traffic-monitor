package trafficmonitor.dati;

import java.util.Date;

public class SegnalazioneUtente extends Dato {
    private String username;                        // indica l'utente associato alla segnalazione
    private TipoSegnalazione tipoSegnalazione;      // indica il tipo di segnalazione

    public SegnalazioneUtente(){}

    public SegnalazioneUtente(Date dataOra, Coordinate coordinate, String username, TipoSegnalazione tipoSegnalazione) {
        super.dataOra = dataOra;
        super.coordinate = coordinate;
        this.username = username;
        this.tipoSegnalazione = tipoSegnalazione;
    }

    public void setUsername(String idUtente) {
        this.username = idUtente;
    }

    public String getUsername() {
        return username;
    }

    public TipoSegnalazione getTipoSegnalazione() {
        return tipoSegnalazione;
    }

    public void setTipoSegnalazione(TipoSegnalazione tipoSegnalazione) {
        this.tipoSegnalazione = tipoSegnalazione;
    }
}
