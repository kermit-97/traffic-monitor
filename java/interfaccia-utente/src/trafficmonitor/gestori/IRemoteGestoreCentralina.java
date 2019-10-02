package trafficmonitor.gestori;

import trafficmonitor.centraline.Centralina;
import trafficmonitor.dati.DatoCentralina;

import java.rmi.*;
import java.util.ArrayList;

public interface IRemoteGestoreCentralina extends Remote{
    void riceviDatoCentralina(DatoCentralina dato) throws RemoteException ;

    ArrayList<Centralina> inviaStatoCentraline() throws RemoteException;
}
