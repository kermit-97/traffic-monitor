package trafficmonitor.dati;
import java.util.Date;

public class DatoCentralinaStradale extends DatoCentralina{
    private String codCentralina;       // identifica univocamente la centralina da cui arriva
    private int numeroMacchine;         // indica il numero di macchine della rilevazione
    private double velocitaMedia;       // indica la velocità media della rilevazione
    private int indiceDiFlusso;         // indica l'indice di flusso della rilevazione

    public DatoCentralinaStradale(){}

    public DatoCentralinaStradale(Date dataOra, Coordinate coordinate, int attesaMassima, String codCentralina, int numeroMacchine, double velocitaMedia, int indiceDiFlusso) {
        super.dataOra = dataOra;
        super.coordinate = coordinate;
        super.attesaMassima = attesaMassima;
        this.codCentralina = codCentralina;
        this.numeroMacchine = numeroMacchine;
        this.velocitaMedia = velocitaMedia;
        this.indiceDiFlusso = indiceDiFlusso;
    }

    public double getVelocitaMedia()
    {
        return velocitaMedia;
    }

    public int getIndiceDiFlusso()
    {
        return indiceDiFlusso;
    }

    public String getCodCentralina()
    {
        return codCentralina;
    }

    public int getNumeroMacchine()
    {
        return numeroMacchine;
    }

    public void setCodCentralina(String codCentralina)
    {
        this.codCentralina = codCentralina;
    }

    public void setIndiceDiFlusso(int indiceDiFlusso)
    {
        this.indiceDiFlusso = indiceDiFlusso;
    }

    public void setNumeroMacchine(int numeroMacchine)
    {
        this.numeroMacchine = numeroMacchine;
    }

    public void setVelocitaMedia(double velocitaMedia)
    {
        this.velocitaMedia = velocitaMedia;
    }
}
