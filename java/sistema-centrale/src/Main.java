import trafficmonitor.dati.RMIConfig;
import trafficmonitor.gestori.GestoreCentralina;
import trafficmonitor.gestori.GestoreDato;
import trafficmonitor.gestori.GestoreSegmentoStradale;
import trafficmonitor.gestori.GestoreUtente;

import javax.swing.*;
import java.rmi.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class Main {

    public static void main(String[] args) {

        String portNum = "1234", registryURLGestoreCentralina, registryURLGestoreDato, registryURLGestoreSegmentoStradale, registryURLGestoreUtente;

        // setup RMI
        try {
            startRegistry( Integer.parseInt(portNum) );
            registryURLGestoreCentralina = "rmi://localhost:" + RMIConfig.GESTORECENTRALINA.getPort() + RMIConfig.GESTORECENTRALINA.getHostName();
            registryURLGestoreDato = "rmi://localhost:" + RMIConfig.GESTOREDATO.getPort() + RMIConfig.GESTOREDATO.getHostName();
            registryURLGestoreSegmentoStradale = "rmi://localhost:" + RMIConfig.GESTORESEGMENTOSTRADALE.getPort() + RMIConfig.GESTORESEGMENTOSTRADALE.getHostName();
            registryURLGestoreUtente = "rmi://localhost:" + RMIConfig.GESTOREUTENTE.getPort() + RMIConfig.GESTOREUTENTE.getHostName();
            Naming.rebind(registryURLGestoreCentralina, GestoreCentralina.getInstance());
            Naming.rebind(registryURLGestoreDato, GestoreDato.getInstance());
            Naming.rebind(registryURLGestoreSegmentoStradale, GestoreSegmentoStradale.getInstance());
            Naming.rebind(registryURLGestoreUtente, GestoreUtente.getInstance());
            System.out.println("Sistema centrale PRONTO");
        } catch (Exception re) {
            System.out.println(re.getMessage());
        }

        try {
            // Set System L&F
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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
    }

    /**
     * Fa partire il registro RMI sulla porta rmiPortNum se non esiste già
     * @param rmiPortNum indica la porta su cui creare il registro RMI
     * @throws RemoteException
     */
    private static void startRegistry(int rmiPortNum) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(rmiPortNum);
            registry.list( );
            // The above call will throw an exception
            // if the registry does not already exist
        } catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry is not located at port " + rmiPortNum);
            Registry registry = LocateRegistry.createRegistry(rmiPortNum);
            System.out.println("RMI registry created at port " + rmiPortNum);
        }
    }
}
