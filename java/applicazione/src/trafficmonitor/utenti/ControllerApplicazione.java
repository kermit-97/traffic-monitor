package trafficmonitor.utenti;

import trafficmonitor.dati.RMIConfig;
import trafficmonitor.dati.TipoSegnalazione;
import trafficmonitor.gestori.IRemoteGestoreDato;
import trafficmonitor.gestori.IRemoteGestoreUtente;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Inet4Address;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class ControllerApplicazione{
    public ControllerApplicazione(Utente u) throws RemoteException {
        ApplicazioneGUI view = new ApplicazioneGUI(u.getUsername(), u.getPosizione().getLatitudine(), u.getPosizione().getLongitudine());
        Applicazione applicazione = new Applicazione(view,u.getPosizione(),u);

        u.setIp(applicazione.getIp());
        u.setPort(applicazione.getPort());

        try {
            String registryUrl = "rmi://" + RMIConfig.GESTOREUTENTE.getIP() + RMIConfig.GESTOREUTENTE.getPort() + RMIConfig.GESTOREUTENTE.getHostName() ;
            //istanzia interfaccia gestore remoto
            IRemoteGestoreUtente iRemoteGestoreUtente= (IRemoteGestoreUtente) Naming.lookup(registryUrl);
            //chiamata funzione
            iRemoteGestoreUtente.riceviIPPortUtente(applicazione.getUtente().getUsername(),applicazione.getIp(),applicazione.getPort());
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        view.getButton1().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String s = view.getJComboBox_Segnalazione().getSelectedItem().toString();
                int temp = 0 ;
                if (view.getJComboBox_Segnalazione().getSelectedIndex() == 0) {temp = 85;}
                else if (view.getJComboBox_Segnalazione().getSelectedIndex() == 1) {temp = 50;}
                else if (view.getJComboBox_Segnalazione().getSelectedIndex() == 2) {temp = 15;}

                applicazione.gestisciDatiView(TipoSegnalazione.getByIndice(temp));
            }
        });
    }


}
