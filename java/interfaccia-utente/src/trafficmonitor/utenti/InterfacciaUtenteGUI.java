package trafficmonitor.utenti;

import trafficmonitor.resource.SimpleSwingBrowser;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class InterfacciaUtenteGUI {

    private JPanel JPanel_InterfacciaUtente;
    private JPanel JPanel_Mappa;
    private JList JList_DatoCentralinaStradale;
    private JList JList_SegnalazioneUtente;
    private JButton JButton_Aggiorna;
    private SimpleSwingBrowser browser;

    private JList JList_StatoCentralinaStradale;
    private JScrollPane JPanel_Dati;
    private JScrollPane JPanel_Segnalazioni;

    public InterfacciaUtenteGUI() {
        JFrame frame = new JFrame("InterfacciaUtenteGUI");
        frame.setContentPane(JPanel_InterfacciaUtente);
        frame.setSize(1200,950);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
    }

    public SimpleSwingBrowser getBrowser() {
        return browser;
    }

    public void setBrowser(SimpleSwingBrowser browser) {
        this.browser = browser;
    }

    public JPanel getJPanel_Mappa() {
        return JPanel_Mappa;
    }

    public void setJPanel_Mappa(JPanel JPanel_Mappa) {
        this.JPanel_Mappa = JPanel_Mappa;
    }

    public JList getJList_DatoCentralinaStradale() {
        return JList_DatoCentralinaStradale;
    }

    public void setJList_DatoCentralinaStradale(JList JList_DatoCentralinaStradale) {
        this.JList_DatoCentralinaStradale = JList_DatoCentralinaStradale;
    }

    public JList getJList_SegnalazioneUtente() {
        return JList_SegnalazioneUtente;
    }

    public void setJList_SegnalazioneUtente(JList JList_SegnalazioneUtente) {
        this.JList_SegnalazioneUtente = JList_SegnalazioneUtente;
    }

    public JButton getJButton_Aggiorna() {
        return JButton_Aggiorna;
    }

    public void setJButton_Aggiorna(JButton JButton_Aggiorna) {
        this.JButton_Aggiorna = JButton_Aggiorna;
    }

    public JList getJList_StatoCentralinaStradale() {
        return JList_StatoCentralinaStradale;
    }

    public void setJList_StatoCentralinaStradale(JList JList_StatoCentralinaStradale) {
        this.JList_StatoCentralinaStradale = JList_StatoCentralinaStradale;
    }

    private void createUIComponents() {
        JPanel_Mappa = new JPanel();
        browser = new SimpleSwingBrowser();
        JPanel_Mappa.add(browser);
        browser.setVisible(true);
        //URL url = getClass().getResource("../resource/maps.html");
        //File f = new File(url.getPath());
        //browser.loadURL(f.toURI().toString());
    }
}
