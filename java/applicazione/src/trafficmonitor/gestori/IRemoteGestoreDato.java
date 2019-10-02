package trafficmonitor.gestori;

import trafficmonitor.dati.Dato;
import trafficmonitor.dati.DatoCentralina;
import trafficmonitor.dati.DatoPosizione;
import trafficmonitor.dati.SegnalazioneUtente;

import java.rmi.*;
import java.util.ArrayList;

public interface IRemoteGestoreDato extends Remote{

    void riceviSegnalazione(SegnalazioneUtente segnalazione) throws RemoteException;

    void riceviPosizioneUtente(DatoPosizione dato)throws RemoteException;

    ArrayList<Dato> inviaStorico() throws RemoteException;


}
