package trafficmonitor.utenti;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteApplicazione extends Remote {
    void riceviNotifica(Notifica notifica)throws RemoteException;
    void inviaPosizione()throws RemoteException;
}