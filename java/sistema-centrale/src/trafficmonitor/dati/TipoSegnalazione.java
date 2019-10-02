package trafficmonitor.dati;

import java.io.Serializable;

public enum TipoSegnalazione implements Serializable {
    // elementi dell'enumerazione
    TRAFFICOELEVATO("TrafficoElevato", "La strada presenta molti rallentamenti", 85),
    TRAFFICOSCORREVOLE("TrafficoScorrevole","La strada ha qualche rallentamento",50),
    STRADALIBERA("StradaLibera","La strada è libera",15);

    // attributi
    private String etichetta;
    private String descrizione;
    private int indiceDiTraffico; //volere medio a cui corrisponde l'indice

    // metodi
    TipoSegnalazione(String etichetta, String descrizione, int indiceDiTraffico) {
        this.etichetta = etichetta;
        this.descrizione = descrizione;
        this.indiceDiTraffico = indiceDiTraffico;
    }
    public String getEtichetta()
    {
        return etichetta;
    }
    public String getDescrizione()
    {
        return descrizione;
    }
    public int getIndiceDiTraffico()
    {
        return indiceDiTraffico;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setEtichetta(String etichetta) {
        this.etichetta = etichetta;
    }

    public void setIndiceDiTraffico(int indiceDiTraffico) {
        this.indiceDiTraffico = indiceDiTraffico;
    }

    public static TipoSegnalazione getByIndice(int indiceDiTraffico){
        if (indiceDiTraffico == 50) {return TipoSegnalazione.TRAFFICOSCORREVOLE;}
        else if (indiceDiTraffico == 85) {return TipoSegnalazione.TRAFFICOELEVATO;}
        else if (indiceDiTraffico == 15) {return TipoSegnalazione.STRADALIBERA;}
        // default
        return TipoSegnalazione.STRADALIBERA;
    }
}
