package trafficmonitor.gestori;

import trafficmonitor.dati.Coordinate;
import trafficmonitor.utenti.Utente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IRemoteGestoreUtente extends Remote {
    void riceviIPPortUtente(String username, String ip, String portNum) throws RemoteException;
}
