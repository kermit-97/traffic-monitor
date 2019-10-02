package trafficmonitor.utenti;

import trafficmonitor.dati.Coordinate;

public class Utente {
    private String username;
    private String password;
    private String email;
    private Coordinate posizione;
    private String ip;
    private String port;

    public Utente(String username, String password, String email, Coordinate posizione) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.posizione = posizione;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setPosizione(Coordinate coordinate) {
        this.posizione = coordinate;
    }

    public Coordinate getPosizione() {
        return this.posizione;
    }
}
