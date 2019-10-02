package trafficmonitor.utenti;

import javax.swing.*;

public class ApplicazioneGUI {
    private JPanel JPanel_Applicazione;
    private JComboBox JComboBox_Segnalazione;
    private JButton button1;
    private JPanel JPanel_Notifiche;
    private JTextArea TextArea1_Notifiche;
    private JTextField TextField1_latitudine;
    private JTextField textField2_longitudine;
    private JPanel JPanel_Segnalazione;
    private JPanel JPanel_Posizione;
    private JPanel JPanel_ContainerSegnalazione;
    private JPanel JPanle_ContainerNotifiche;
    private JPanel JPanel_TextAreaNotifiche;
    private JTabbedPane tabbedPane1;
    private JLabel JLabel_statoTraffico;
    private JPanel JPanel_Clock;
    private JLabel JLabel_Clock;
    private JTextField TextField3_Username;

    public ApplicazioneGUI(String username, String lat, String lon) {
        JFrame frame = new JFrame(username);
        frame.setContentPane(JPanel_Applicazione);
        frame.setSize(370,500);
        getTextField1_latitudine().setText(lat);
        getTextField2_longitudine().setText(lon);
        frame.setVisible(true);
    }

    public JLabel getJLabel_Clock() {
        return JLabel_Clock;
    }

    public void setJLabel_Clock(JLabel JLabel_Clock) {
        this.JLabel_Clock = JLabel_Clock;
    }

    public JPanel getJPanel_ContainerSegnalazione() {
        return JPanel_ContainerSegnalazione;
    }

    public JPanel getJPanle_ContainerNotifiche() {
        return JPanle_ContainerNotifiche;
    }

    public JTabbedPane getTabbedPane1() {
        return tabbedPane1;
    }

    public JPanel getJPanel_Clock() {
        return JPanel_Clock;
    }

    public void setTextField3_Username(JTextField textField3_Username) {
        TextField3_Username = textField3_Username;
    }

    public JTextField getTextField3_Username() {
        return TextField3_Username;
    }

    public void setTextField1_latitudine(JTextField textField1_latitudine) {
        TextField1_latitudine = textField1_latitudine;
    }

    public JTextField getTextField1_latitudine() {
        return TextField1_latitudine;
    }

    public void setTextField2_longitudine(JTextField textField2_longitudine) {
        this.textField2_longitudine = textField2_longitudine;
    }

    public JTextField getTextField2_longitudine() {
        return textField2_longitudine;
    }

    public void setJPanel_Applicazione(JPanel JPanel_Applicazione) {
        this.JPanel_Applicazione = JPanel_Applicazione;
    }

    public JPanel getJPanel_Applicazione() {
        return JPanel_Applicazione;
    }

    public void setJComboBox_Segnalazione(JComboBox JComboBox_Segnalazione) {
        this.JComboBox_Segnalazione = JComboBox_Segnalazione;
    }

    public JComboBox getJComboBox_Segnalazione() {
        return JComboBox_Segnalazione;
    }


    public void setTextArea1_Notifiche(JTextArea textArea1_Notifiche) {
        TextArea1_Notifiche = textArea1_Notifiche;
    }

    public JTextArea getTextArea1_Notifiche() {
        return TextArea1_Notifiche;
    }

    public void setButton1(JButton button1) {
        this.button1 = button1;
    }

    public JButton getButton1() {
        return button1;
    }


    public void setJPanel_Notifiche(JPanel JPanel_Notifiche) {
        this.JPanel_Notifiche = JPanel_Notifiche;
    }

    public JPanel getJPanel_Notifiche() {
        return JPanel_Notifiche;
    }

    public void setJPanel_Segnalazione(JPanel JPanel_Segnalazione) {
        this.JPanel_Segnalazione = JPanel_Segnalazione;
    }

    public JPanel getJPanel_Segnalazione() {
        return JPanel_Segnalazione;
    }

    public void setJPanel_Posizione(JPanel JPanel_Posizione) {
        this.JPanel_Posizione = JPanel_Posizione;
    }

    public JPanel getJPanel_Posizione() {
        return JPanel_Posizione;
    }
}
