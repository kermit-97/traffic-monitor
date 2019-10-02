package trafficmonitor.gestori;

import trafficmonitor.dati.Coordinate;
import trafficmonitor.dati.SegmentoStradale;

import java.rmi.*;
import java.util.ArrayList;

public interface IRemoteGestoreSegmentoStradale extends Remote {
    ArrayList<SegmentoStradale> inviaSegmentiStradali() throws RemoteException;
}
