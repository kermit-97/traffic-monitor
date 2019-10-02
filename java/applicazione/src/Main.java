import trafficmonitor.dati.Coordinate;
import trafficmonitor.utenti.*;

import javax.swing.*;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        }
        catch (ClassNotFoundException e) {
            // handle exception
        }
        catch (InstantiationException e) {
            // handle exception
        }
        catch (IllegalAccessException e) {
            // handle exception
        }

        Coordinate c1 = new Coordinate("45.801906", "9.092918");        // utente in via Francesco Anzani
        Utente u1 = new Utente("arya_stark","123456","noone@westeros.com",c1);

        Coordinate c2 = new Coordinate("45.798268", "9.092142");        // utente in via Viganò
        Utente u2 = new Utente("tyler_durden","123456","tyler@soap.com",c2);

        Coordinate c3 = new Coordinate("45.801233", "9.091384");        // utente su altro segmento di via Francesco Anzani
        Utente u3 = new Utente("doctor_who","123456","doctor@tardis.com",c3);

        try {
            ControllerApplicazione controllerApplicazione1 = new ControllerApplicazione(u1);
            ControllerApplicazione controllerApplicazione2 = new ControllerApplicazione(u2);
            ControllerApplicazione controllerApplicazione3 = new ControllerApplicazione(u3);
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }

        /*
        * Posizioni lontane da segmenti monitorati da centraline
        * 1. lat: 45.801906 long: 9.092918 - Via Francesco Anzani
        * 2. lat: 45.800926 long: 9.092875 - Via Palestro
        * 3. lat: 45.801233 long: 9.091384 - Via Francesco Anzani
        *
        * Posizioni vicine a segmenti monitorati da centraline
        * 1. lat: 45.798268 long: 9.092142 - Via Viganò
        * 2. lat: 45.798268 long: 9.092142 - Via Leone Leoni
        * 3. lat: 45.799390 long: 9.092348 - Via Dei Mille
        *
        * */




    }
}
