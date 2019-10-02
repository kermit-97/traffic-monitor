package trafficmonitor.centraline;

import trafficmonitor.dati.SegmentoStradale;
import trafficmonitor.dati.Coordinate;

import java.io.Serializable;
import java.util.Date;

public class Centralina implements Serializable {
    protected int periodicita;              // indica la periodicità di invio dei dati
    protected String codSeriale;            // codice seriale univoco che identifica la centralina
    protected boolean attivo;               // indica se la centralina è attiva e sta lavorando
    protected boolean funzionante;          // se false, indica che c'è un guasto
    protected Coordinate coordinate;        // indica la posizione della centralina
    protected SegmentoStradale segmentoStradale;        // indica il segmento stradale a cui è associata la centralina
    protected Date ultimoInvio;             // indica l'ultimo invio di un dato da parte della centralina
    protected int attesaMassima;            // indica il tempo di attesa massimo dopo il quale il sistema centrale indica la centralina come guasta

    public Centralina() {

    }

    public Centralina(String codSeriale, boolean attivo, boolean funzionante, Coordinate coordinate,Date ultimoInvio, SegmentoStradale segmentoStradale) {
        this.codSeriale = codSeriale;
        this.attivo = attivo;
        this.funzionante = funzionante;
        this.coordinate = coordinate;
        this.ultimoInvio = ultimoInvio;
        this.segmentoStradale = segmentoStradale;
        this.periodicita = 0;
        this.attesaMassima = 0;
    }

    public void setPeriodicita(int periodicita)
    {
        this.periodicita = periodicita;
    }

    public int getPeriodicita()
    {
        return this.periodicita;
    }

    public void setCodSeriale(String codSeriale)
    {
        this.codSeriale = codSeriale;
    }

    public String getCodSeriale()
    {
        return this.codSeriale;
    }

    public void setAttivo(boolean attivo)
    {
        this.attivo = attivo;
    }

    public boolean getAttivo()
    {
        return this.attivo;
    }

    public void setFunzionante(boolean funzionante)
    {
        this.funzionante = funzionante;
    }

    public boolean getFunzionante()
    {
        return this.funzionante;
    }

    public void setCoordinate(Coordinate coordinate)
    {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public SegmentoStradale getSegmentoStradale()
    {
        return segmentoStradale;
    }

    public void setSegmentoStradale(SegmentoStradale segmentoStradale)
    {
        this.segmentoStradale = segmentoStradale;
    }

    public void setUltimoInvio(Date data){
        this.ultimoInvio = data ;
    }

    public Date getUltimoInvio(){
        return ultimoInvio;
    }

    public void aggiornaUltimoInvio(Date data){
        this.ultimoInvio = data ;
    }

    public void setAttesaMassima(int tempo){
        this.attesaMassima = tempo ;
    }

    public int getAttesaMassima(){
        return attesaMassima;
    }

    public void aggiornaAttesaMassima(int tempo){
        this.attesaMassima = tempo ;
    }

    protected void inviaRilevazione(){}

    protected void calcolaPeriodicita(){}

    protected void effettuaRilevazione(){}

    protected void diagnostica(){}
}
