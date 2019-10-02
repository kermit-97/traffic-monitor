package trafficmonitor.dati;

import java.util.Date;

public class DatoPosizione extends Dato {
    private String username;        // indica l'utente a cui appartiene la posizione

    public DatoPosizione() {}

    public DatoPosizione(Date dataOra, Coordinate coordinate, String username) {
        super.dataOra = dataOra;
        super.coordinate = coordinate;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
