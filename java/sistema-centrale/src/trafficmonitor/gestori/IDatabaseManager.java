package trafficmonitor.gestori;

import trafficmonitor.centraline.Centralina;
import trafficmonitor.dati.*;
import trafficmonitor.utenti.Utente;

import java.util.ArrayList;
import java.util.Date;

public interface IDatabaseManager {

    ArrayList<SegmentoStradale> getSegmentiStradali();
    void updateIndiceDiFlussoSegmentoStradale(String codSegmento, int index);

    ArrayList<Centralina> getCentraline();
    void updateFunzionamentoCentralina(String codSeriale, boolean attiva);
    void updateUltimoInvioCentralina(String codSeriale, Date data);

    ArrayList<Utente> getUtenti();

    ArrayList<Dato> getDatiCentralina();
    void addDatoCentralina(DatoCentralinaStradale dato);
    ArrayList<Dato> getSegnalazioni();
    void addSegnalazioneUtente(SegnalazioneUtente segnalazione);

    ArrayList<Dato> getPosizioni();
    void addDatoPosizione(DatoPosizione dato);
}
