package trafficmonitor.dati;

import java.io.Serializable;
import java.util.Date;

public class SegmentoStradale implements Serializable {
    private String nome;                    // indica il nome del segmento - via associata
    private String codSegmento;             // indica univocamente il segmento
    private Date ultimoAggiornamento;       // indica il timestamp dell'ultimo aggiornamento relativo al segmento
    private Coordinate coordinateInizio;    // indica le coordinate in cui inizia il segmento
    private Coordinate coordinateFine;      // indica le coordinate in cui finisce il segmento
    private int indiceDiFlussoAttuale;      // indica l'indice di flusso che rappresenta il traffico associato al segmento
    private int indiceDiFlussoPrecedente;   // indica l'indice di flusso precedentemente associato al segmento
    private int velMax;                     // indica il limite di velocità del segmento stradale

    public SegmentoStradale(String nome, String codSegmento, Coordinate coordinateInizio, Coordinate coordinateFine, int indiceDiFlussoAttuale, int velMax) {
        this.nome = nome;
        this.codSegmento = codSegmento;
        this.ultimoAggiornamento = new Date();
        this.coordinateInizio = coordinateInizio;
        this.coordinateFine  = coordinateFine;
        this.indiceDiFlussoAttuale = indiceDiFlussoAttuale;
        this.indiceDiFlussoPrecedente = 0;
        this.velMax = velMax;
    }

    public SegmentoStradale(String nome, String codSegmento, Date data, Coordinate coordinateInizio, Coordinate coordinateFine,
                            int indiceDiFlussoPrecedente, int indiceDiFlussoAttuale, int velMax) {
        this.nome = nome;
        this.codSegmento = codSegmento;
        this.ultimoAggiornamento = data;
        this.coordinateInizio = coordinateInizio;
        this.coordinateFine  = coordinateFine;
        this.indiceDiFlussoAttuale = indiceDiFlussoAttuale;
        this.indiceDiFlussoPrecedente = indiceDiFlussoPrecedente;
        this.velMax = velMax;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getNome()
    {
        return this.nome;
    }

    public String getCodSegmento() {
        return codSegmento;
    }

    public void setCodSegmento(String codSegmento) {
        this.codSegmento = codSegmento;
    }

    public Date getUltimoAggiornamento() {
        return ultimoAggiornamento;
    }

    public void setUltimoAggiornamento(Date ultimoAggiornamento) {
        this.ultimoAggiornamento = ultimoAggiornamento;
    }

    public void setCoordinateInizio(Coordinate coortdinateInizio)
    {
        this.coordinateInizio = coortdinateInizio;
    }

    public Coordinate getCoordinateInizio()
    {
        return this.coordinateInizio;
    }

    public Coordinate getCoordinateFine()
    {
        return this.coordinateFine;
    }

    public void setCoordinateFine(Coordinate coordinateFine)
    {
        this.coordinateFine = coordinateFine;
    }

    public void setIndiceDiFlussoAttuale(int indiceDiFlussoAttuale) {
        this.indiceDiFlussoAttuale = indiceDiFlussoAttuale;
    }

    public int getIndiceDiFlussoAttuale()
    {
        return this.indiceDiFlussoAttuale ;
    }

    public int getIndiceDiFlussoPrecedente() {
        return indiceDiFlussoPrecedente;
    }

    public void setIndiceDiFlussoPrecedente(int indiceDiFlussoPrecedente) {
        this.indiceDiFlussoPrecedente = indiceDiFlussoPrecedente;
    }

    public int getVelMax() {
        return velMax;
    }

    public void setVelMax(int velMax) {
        this.velMax = velMax;
    }

    public Coordinate puntoMedio(){
        return coordinateInizio.puntoMedio(coordinateFine);
    }
}
