package trafficmonitor.centraline;

import trafficmonitor.dati.*;
import trafficmonitor.gestori.IRemoteGestoreCentralina;

import java.rmi.Naming;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class
CentralinaStradale extends Centralina
{
    private int numeroMacchine;         // indica il numero di macchine nella rilevazione in corso
    private double velocitaMedia;       // indica la veloctià media della rilevazione in corso
    private int indiceDiFLusso;         // indica l'indice di flusso calcolato dai dati della rilevazione
    private int nRilevazioni;           // conta le rilevazioni effettuate (utilizzato per simulare il traffico)

    private CentralinaStradaleGUI view;
    Timer timer = new Timer();

    public CentralinaStradale(String codSeriale, boolean attivo, boolean funzionante, Coordinate coordinate, Date ultimoInvio, SegmentoStradale segmento){
        this.numeroMacchine = 0;
        this.velocitaMedia = 0;
        this.indiceDiFLusso = 0;
        super.periodicita = 60; //tempo in secondi
        super.codSeriale = codSeriale;
        super.attivo = attivo;
        super.funzionante = funzionante;
        super.coordinate = coordinate;
        super.segmentoStradale = segmento;
        super.attesaMassima = 60*2;
        super.ultimoInvio = ultimoInvio;
        this.view = null;
        int nRilevazioni = 0;
    }

    // costruttore della centralina, con la View per il Model-View-Controller
    public CentralinaStradale(String codSeriale, boolean attivo, boolean funzionante, Coordinate coordinate, Date ultimoInvio, SegmentoStradale segmento, CentralinaStradaleGUI w){
        this.numeroMacchine = 0;
        this.velocitaMedia = 0;
        this.indiceDiFLusso = 0;
        super.periodicita = 60; //tempo in secondi
        super.codSeriale = codSeriale;
        super.attivo = attivo;
        super.funzionante = funzionante;
        super.coordinate = coordinate;
        super.segmentoStradale = segmento;
        super.attesaMassima = 60*2;
        super.ultimoInvio = ultimoInvio;
        int nRilevazioni = 0;
        //grafica
        this.view = w;
        setGUI();
        //inizio della routine
        startTimer();
    }

    public CentralinaStradale(int numMacchine, double velocitaMedia, int indiceDiFlusso, int periodicita,
                              String codSeriale, boolean attivo, boolean funzionante, Coordinate coordinate,
                              SegmentoStradale segmento, int attesaMassima, Date ultimoInvio) {
        this.numeroMacchine = numMacchine;
        this.velocitaMedia = velocitaMedia;
        this.indiceDiFLusso = indiceDiFlusso;
        super.periodicita = periodicita;
        super.codSeriale = codSeriale;
        super.attivo = attivo;
        super.funzionante = funzionante;
        super.coordinate = coordinate;
        super.segmentoStradale = segmento;
        super.attesaMassima = attesaMassima;
        super.ultimoInvio = ultimoInvio;
        int nRilevazioni = 0;
        startTimer();
    }

    public void setIndiceDiFlusso(int indice) {
        this.indiceDiFLusso = indice;
    }

    public int getIndiceDiFlusso() {
        return this.indiceDiFLusso ;
    }

    public void setNumeroMacchine(int numeroMacchine) {
        this.numeroMacchine = numeroMacchine;
    }

    public int getNumeroMacchine() {
        return this.numeroMacchine ;
    }

    public void setVelocitaMedia(double velocitaMedia) {
        this.velocitaMedia = velocitaMedia;
    }

    public double getVelocitaMedia() {
        return this.velocitaMedia ;
    }

    /**
     * Quando la centralina viene rimessa in funzione fa partire il timer di rilevazione
     * @param funzionante
     */
    @Override
    public void setFunzionante(boolean funzionante) {
        super.setFunzionante(funzionante);
        if (funzionante){timer_periodoRilevazione();}
    }

    /**
     * Generazione pseudocasuale del traffico attraverso un vettore e la velocità massima possibile sulla strada
     * Il traffico avraà un andamento decrescente lineare periodico
     *
     * @param vMax  fattore moltiplicativo
     */
    private void simulazioneTraffico(double vMax,  int index) {
        double [] andamento = {0.2, 0.35, 0.45, 0.5, 0.6, 0.75, 0.8, 0.99};
        int dimAndamento = 8;
        // indice nel vettore
        int i = index % 8;
        // calcolo fattore di disturbo casual 5% di VMax
        double disturbo = (vMax/100) *5;

        // velocità media
        this.velocitaMedia = (andamento[i] * vMax) + (Double)(((Math.random()*2) -1 )*disturbo); // numero random (0:2) -1 per generare numero negativo * il fattore di disturbo
        // numero di macchine è inversamente proporzionale alla velocità media
        this.numeroMacchine = (int) (1+((1-andamento[i]) * vMax) + (Double)(((Math.random()*2) -1 )*disturbo));
    }

    /**
     * Resetta indice di flusso, velocità media e numero di macchine per la prossima rilevazione
     */
    private void reset() {
        indiceDiFLusso = 0 ;
        velocitaMedia = 0 ;
        numeroMacchine = 0 ;
    }

    /**
     * Calcola l'indice di flusso prelevando la velocità massima dal segmento stradale
     * Viene calcolato un indice percentuale, dove 100 = traffico massimo
     */
    public void calcolaIndice() {
        // velMedia/velMax
        double temp = this.velocitaMedia / segmentoStradale.getVelMax();
        // genera un indice complementare (1 = traffico minimo), motivo per cui si caloola il complementare
        // superando il limite di veolcità si ottiene un valore > 1 che viene troncato
        if(temp >= 1){
            temp = 1;
        }
        // complementare in percentuale
        this.indiceDiFLusso = (int) (100 - temp * 100);
    }

    /**
     * Effettua la rilevazione con i dati a disposizione
     */
    @Override
    protected void effettuaRilevazione() {
        this.velocitaMedia = 10;
        this.numeroMacchine = 1;
        // controllo se è attivata la simulazione del traffico automatica
        if(view.getCheckBox_trafficoAutomatico().isSelected()) {
            simulazioneTraffico(segmentoStradale.getVelMax(), nRilevazioni);
        } else {
            // rilevazione con dati dalla GUI
            try{
                this.numeroMacchine =  Integer.parseInt(view.getTextField_numeroMacchine().getText());
                this.velocitaMedia = Double.parseDouble(view.getTextField_velMedia().getText());
            }
            catch (NumberFormatException e){
                System.out.println(e.getMessage());
            }
        }
        nRilevazioni ++;
    }

    /**
     * Calcola la periodicità della rilevazione del dato successivo, basandosi sui dati di quella attuale
     *   Indice              Periodicità
     *  00-20				60 s
     *  21-30				45 s
     *  31-50				30 s
     *  51-70				20 s
     *  71-100				10 s
     */
    @Override
    protected void calcolaPeriodicita() {
        if (0 >=  this.indiceDiFLusso &&  this.indiceDiFLusso <= 20) {
            this.periodicita = 60;
        } else if (20 >  this.indiceDiFLusso &&  this.indiceDiFLusso <= 30) {
            periodicita = 45;
        } else if (30 >  this.indiceDiFLusso &&  this.indiceDiFLusso <= 50) {
            this.periodicita = 30;
        } else if (50 >  this.indiceDiFLusso &&  this.indiceDiFLusso <= 70) {
            periodicita = 20;
        } else if (71 >  this.indiceDiFLusso &&  this.indiceDiFLusso <= 100) {
            this.periodicita = 10;
        }
    }

    /**
     * La diagnostica rende inattiva la centralina nel caso non sia funzionante
     * Non funzionante di fatto indica che c'è un guasto, motivo per cui disattiviamo la centralina
     */
    @Override
    protected void diagnostica() {
        this.attivo = this.funzionante;
        if(this.attivo){
            view.getJLabel_statoCentralina().setText("Centralina ATTIVA");
            view.getJLabel_statoCentralina().setForeground(new java.awt.Color(34, 139, 34));
        }
        else{view.getJLabel_statoCentralina().setText("Centralina INATTIVA");
            view.getJLabel_statoCentralina().setForeground(new java.awt.Color(205, 38, 38));
        }
    }

    /**
     * Quando scade il timer, iniza la routine di calcolo e invio della rilevazione
     * Routine: calcola indice, periodicità, genera dato, invia dato, reset delle variabili
     * Fa ripartire il timer (periodo di rilevazione)
     */
    @Override
    protected void  inviaRilevazione() {
        calcolaIndice();
        calcolaPeriodicita();
        // genera dato
        Date dataOra = new Date();
        this.ultimoInvio = dataOra;

        DatoCentralinaStradale dato = new DatoCentralinaStradale(dataOra,this.coordinate,this.periodicita*2,this.codSeriale,this.numeroMacchine,this.velocitaMedia,this.indiceDiFLusso);

        // mostra il dato nella GUI
        setDatoGUI(dato);

        // invia dato con RMI invocando metodo remoto
        try {
            String registryUrl = "rmi://" + RMIConfig.GESTORECENTRALINA.getIP() + RMIConfig.GESTORECENTRALINA.getPort() + RMIConfig.GESTORECENTRALINA.getHostName() ;
            // istanzia interfaccia gestore remoto
            IRemoteGestoreCentralina iRemoteGestoreCentralina = (IRemoteGestoreCentralina) Naming.lookup(registryUrl);
            // chiamata metodo
            iRemoteGestoreCentralina.riceviDatoCentralina(dato);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }

        reset();
        startTimer();
    }

    /**
     * Fa partire il timer per il periodo di rilevazione
     */
    private void startTimer() {
        timer_periodoRilevazione();
    }

    /**
     *  Scaduta la periodicità viene eseguita la procedura di rilevazione dei dati e le operazioni che precedono l'invio dei dati
     */
    private void timer_periodoRilevazione(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                diagnostica();
                // se la centralina è funzionante allora effettua ed invia la rilevazione
                if (getFunzionante()) {
                    effettuaRilevazione();
                    inviaRilevazione();
                }
            }
        },this.periodicita*1000);
    }

    /**
     * Aggiorna la GUI mostrando il dato
     * @param d indica il dato che verrà mostrato nella GUI
     */
    private void setDatoGUI(DatoCentralinaStradale d) {
        if (!view.getJPanel_infoDato2().isVisible()){
            view.getJPanel_infoDato2().setVisible(true);
        }
        view.getJLabel_dato_nomeCentralina().setText("Mittente: " + d.getCodCentralina());
        view.getJLabel_dato_numeroMacchine().setText("Numero macchine: " + (d.getNumeroMacchine()));
        view.getJLabel_dato_velMedia().setText("Velocità media: " + (d.getVelocitaMedia()));
        view.getJLabel_dato_indiceDiFlusso().setText("Indice di flusso: "+ (d.getIndiceDiFlusso()));
        view.getJLabel_dato_attesaMassima().setText("Attesa massima: "+ (d.getAttesaMassima()));
        view.getJLabel_dato_dataOra().setText("Data :" + d.getDataOra().toString());
    }

    private void setGUI() {
        view.getJLabel_nomeCentralina().setText("Centralina " +this.codSeriale);
        view.getJLabel_nomeStrada().setText("Segmento monitorato: "+segmentoStradale.getNome());
        view.getJLabel_velMax().setText("Velocità massima: "+Double.toString(segmentoStradale.getVelMax()));
        if (this.attivo) {
            view.getJLabel_statoCentralina().setText("Centralina ATTIVA");
            view.getJLabel_statoCentralina().setForeground(new java.awt.Color(34, 139, 34));
        }
        else {view.getJLabel_statoCentralina().setText("Centralina INATTIVA");
            view.getJLabel_statoCentralina().setForeground(new java.awt.Color(205, 38, 38));
        }
        view.getJPanel_infoDato2().setVisible(false);
    }
}