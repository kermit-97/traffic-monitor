package trafficmonitor.utenti;
import java.io.Serializable;

public class Notifica implements Serializable {

    private String testo;

    public Notifica(String testo){
        this.testo = testo ;
    }

    public String getTesto()
    {
        return this.testo ;
    }

    public void setTesto(String testo)
    {
        this.testo = testo ;
    }
}
