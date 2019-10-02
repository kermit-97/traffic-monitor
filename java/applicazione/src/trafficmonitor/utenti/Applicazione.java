package trafficmonitor.utenti;

import trafficmonitor.dati.*;
import trafficmonitor.gestori.IRemoteGestoreDato;

import java.net.Inet4Address;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.Date;

public class Applicazione extends UnicastRemoteObject implements IApplicazione, IRemoteApplicazione {
    private ApplicazioneGUI view ;
    private Coordinate ultimaPosizione;
    private Utente utente;
    private String ip;
    private String port;

    public Applicazione() throws RemoteException{}

    public Applicazione(ApplicazioneGUI view, Coordinate ultimaPosizione, Utente utente) throws RemoteException{
        this.view = view;
        this.ultimaPosizione = ultimaPosizione;
        this.utente = utente;
        try {
            this.ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        this.port = Integer.toString((int)(Math.random()*1000));
        //preparazione ricezione notifiche
        appListener();
        setGui();
    }

    private void acquisisciPosizione() {
        //nuova posizione
        Coordinate c = new Coordinate(view.getTextField1_latitudine().getText(),view.getTextField2_longitudine().getText());
        this.ultimaPosizione = c ;
        utente.setPosizione(c);
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUltimaPosizione(Coordinate ultimaPosizione) {
        this.ultimaPosizione = ultimaPosizione;
    }

    public Coordinate getUltimaPosizione() {
        return ultimaPosizione;
    }

    private void setGui(){
        Date data = new Date();
        view.getJLabel_Clock().setText(data.getHours() + ":" + data.getMinutes());

        for( TipoSegnalazione e:TipoSegnalazione.values() ){
            view.getJComboBox_Segnalazione().addItem(e.getDescrizione());
        }
    }

    /**
     * Alla ricezione della notifica, mette il testo nella TextArea e mostra il Panel con il contenuto
     * @param notifica indica la notifica ricevuta
     */
    private void mostraNotifica(Notifica notifica){
        Date data = new Date();
        view.getJLabel_Clock().setText(data.getHours() + ":" + data.getMinutes());
        view.getTextArea1_Notifiche().setText(notifica.getTesto());
        view.getTabbedPane1().setSelectedIndex(1);
    }

    /**
     * Prepara la segnalazione prendendo i dati dalla GUI
     * @param t
     */
    public void gestisciDatiView(TipoSegnalazione t){
        Date data = new Date();
        acquisisciPosizione(); //aggiorno la posizione corrente
        SegnalazioneUtente seg = new SegnalazioneUtente(data,this.ultimaPosizione,utente.getUsername(),t);
        //chiamata al metodo che inoltrerà la segnalazione
        inviaSegnalazione(seg);
    }

    /**
     * Iniva la segnalazione generata tramite RMI al sistema centrale
     * @param segnalazione
     */
    private void inviaSegnalazione(SegnalazioneUtente segnalazione) {
        //rmi
        System.out.println("Applicazione:inviaSegnalazione " + segnalazione.getTipoSegnalazione()+"["+segnalazione.getUsername()+"]");
        try {
            //String hostName = "/GestoreDato";
            //String portNum = "1234" ;
            String registryUrl = "rmi://" + RMIConfig.GESTOREDATO.getIP() + RMIConfig.GESTOREDATO.getPort() + RMIConfig.GESTOREDATO.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreDato iRemoteGestoreDato = (IRemoteGestoreDato) Naming.lookup(registryUrl);

            //chiamata funzione
            iRemoteGestoreDato.riceviSegnalazione(segnalazione);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Riceve tramite RMI la notifica dal sistema centrale, chiama il metodo incaricato di mostrarla
     * @param notifica indica la notifica ricevuta
     * @throws RemoteException
     */
    @Override
    public void riceviNotifica(Notifica notifica) throws RemoteException{
        //modifico la view
         mostraNotifica(notifica);
    }

    /**
     * Metodo chiamato da remoto, richiede l'invio della posizione attuale
     * La posizione viene presa dalla GUI e inviata al sistema centrale tramite RMI
     * @throws RemoteException
     */
    @Override
    public void inviaPosizione() throws RemoteException{
        acquisisciPosizione(); //salva la posizione attuqale nell'applicazione e nell'utente associato
        //creo il DatoPosizione
        Date dataOra = new Date();
        DatoPosizione dato = new DatoPosizione(dataOra,this.ultimaPosizione,this.utente.getUsername());
        //invoca metodo remoto
        try {
            String registryUrl = "rmi://" + RMIConfig.GESTOREDATO.getIP() + RMIConfig.GESTOREDATO.getPort() + RMIConfig.GESTOREDATO.getHostName() ;

            //istanzia interfaccia gestore remoto
            IRemoteGestoreDato iRemoteGestoreDato = (IRemoteGestoreDato) Naming.lookup(registryUrl);

            //chiamata funzione
            iRemoteGestoreDato.riceviPosizioneUtente(dato);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Avvia l'applicazione come server per poter ricevere le notifiche che verranno inviate dal sistemacentrale
     */
    private void appListener(){
        String portNum = this.port, registryURLApplicazione;
        try{
            //non necessario perchè gestori singleton
            //SomeImpl exportedObj = new SomeImpl();
            startRegistry( Integer.parseInt(portNum) );
            // register the object under the name "some"
            registryURLApplicazione = "rmi://localhost:" + portNum + "/Applicazione";
            Naming.rebind(registryURLApplicazione, this);
            System.out.println("Some Server ready.");
        } catch (Exception re) {
            System.out.println("Exception in SomeServer.main: " + re);
        }
    }

    // This method starts a RMI registry on the local host, if it
    // does not already exist at the specified port number.
    private static void startRegistry(int rmiPortNum) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(rmiPortNum);
            registry.list();
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
