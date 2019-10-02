package trafficmonitor.centraline;

import javax.swing.*;

public class CentralinaStradaleGUI {
    private JPanel JPanel_CentralinaStradale;
    private JPanel JPanel_setTraffico;
    private JPanel JPanel_setStatoCentrlina;
    private JPanel JPanel_infoCentralina;
    private JLabel JLabel_nomeCentralina;
    private JLabel JLabel_statoCentralina;
    private JLabel JLabel_nomeStrada;
    private JLabel JLabel_velMax;
    private JCheckBox checkBox_statoCentralina;
    private JPanel JPanel_infoDato;
    private JPanel JPanel_infoDato2;
    private JLabel JLabel_dato_nomeCentralina;
    private JLabel JLabel_dato_dataOra;
    private JLabel JLabel_dato_attesaMassima;
    private JLabel JLabel_dato_numeroMacchine;
    private JLabel JLabel_dato_velMedia;
    private JLabel JLabel_dato_indiceDiFlusso;
    private JCheckBox checkBox_trafficoAutomatico;
    private JTextField textField_numeroMacchine;
    private JTextField textField_velMedia;
    private JLabel JLabel_id_setNumeroMacchine;
    private JLabel JLabel_id_setVelMedia;
    private JPanel JPanel_container;
    private JPanel JPanel_setter;
    private JLabel JLabel_countdown;

    public CentralinaStradaleGUI(String nomeC){
        JFrame frame = new JFrame(nomeC);
        frame.setSize(750,450);
        frame.setContentPane(JPanel_CentralinaStradale);
        frame.setVisible(true);
    }

    public JLabel getJLabel_nomeCentralina() {
        return JLabel_nomeCentralina;
    }

    public JLabel getJLabel_velMax() {
        return JLabel_velMax;
    }

    public JLabel getJLabel_statoCentralina() {
        return JLabel_statoCentralina;
    }

    public JLabel getJLabel_nomeStrada() {
        return JLabel_nomeStrada;
    }

    public JCheckBox getCheckBox_statoCentralina() {
        return checkBox_statoCentralina;
    }

    public JPanel getJPanel_infoDato2() {
        return JPanel_infoDato2;
    }

    public JLabel getJLabel_countdown() {
        return JLabel_countdown;
    }

    //etichette ultimo dato inviato
    public JLabel getJLabel_dato_nomeCentralina() {
        return JLabel_dato_nomeCentralina;
    }

    public JLabel getJLabel_dato_attesaMassima() {
        return JLabel_dato_attesaMassima;
    }

    public JLabel getJLabel_dato_numeroMacchine() {
        return JLabel_dato_numeroMacchine;
    }

    public JLabel getJLabel_dato_dataOra() {
        return JLabel_dato_dataOra;
    }

    public JLabel getJLabel_dato_velMedia() {
        return JLabel_dato_velMedia;
    }

    public JLabel getJLabel_dato_indiceDiFlusso() {
        return JLabel_dato_indiceDiFlusso;
    }

    //manipolazione dati input
    public JTextField getTextField_velMedia() {
        return textField_velMedia;
    }

    public JTextField getTextField_numeroMacchine() {
        return textField_numeroMacchine;
    }

    public JCheckBox getCheckBox_trafficoAutomatico() {
        return checkBox_trafficoAutomatico;
    }
}
