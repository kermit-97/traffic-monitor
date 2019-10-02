package trafficmonitor.centraline;

import trafficmonitor.dati.Coordinate;
import trafficmonitor.dati.SegmentoStradale;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.util.Date;

public class ControllerCentralinaStradale {

    public ControllerCentralinaStradale(String codSeriale, boolean attivo, boolean funzionante, Coordinate coordinate, Date ultimoInvio, SegmentoStradale segmento) {

        CentralinaStradaleGUI view = new CentralinaStradaleGUI(codSeriale);
        CentralinaStradale cs = new CentralinaStradale(codSeriale, attivo, funzionante, coordinate, ultimoInvio, segmento, view);

        /*view.getCheckBox_statoCentralina().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cs.setFunzionante(view.getCheckBox_statoCentralina().isSelected());
            }
        });*/

        view.getCheckBox_statoCentralina().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cs.setFunzionante(view.getCheckBox_statoCentralina().isSelected());
            }
        });

        view.getTextField_numeroMacchine().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                //chiamata all'inserimento
                //se viene inserito un carattere diverso da un numero viene cancellato
                try{
                    int n = Integer.parseInt(view.getTextField_numeroMacchine().getText());
                }
                catch (NumberFormatException ex){
                   System.out.println("Numro macchine not int");
                    correctInsert();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //chiamata alla cancellazione
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                //non so quando viene chiamata
            }
            private void correctInsert(){
                Runnable correctInsert = new Runnable() {
                    @Override
                    public void run() {
                        //elimino l'ultimo carattere inserito, procedura eseguita da Event Dispatcher Thread
                        remLast(view.getTextField_numeroMacchine());
                    }
                };
                SwingUtilities.invokeLater(correctInsert);
            }
        });

        view.getTextField_velMedia().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                //chiamata all'inserimento
                //se viene inserito un carattere diverso da un numero viene cancellato
                try{
                    Double n = Double.parseDouble(view.getTextField_velMedia().getText());

                }
                catch (NumberFormatException ex){
                    System.out.println("Velocità media not double");
                    correctInsert();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //chiamata alla cancellazione
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                //non so quando viene chiamata
            }
            private void correctInsert(){
                Runnable correctInsert = new Runnable() {
                    @Override
                    public void run() {
                        //elimino l'ultimo carattere inserito, procedura eseguita da Event Dispatcher Thread
                        remLast(view.getTextField_velMedia());
                    }
                };
                SwingUtilities.invokeLater(correctInsert);
            }
        });

        view.getCheckBox_trafficoAutomatico().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if( view.getCheckBox_trafficoAutomatico().isSelected()){
                    view.getTextField_velMedia().setEnabled(false);
                    view.getTextField_numeroMacchine().setEnabled(false);
                }
                else{
                    view.getTextField_velMedia().setEnabled(true);
                    view.getTextField_numeroMacchine().setEnabled(true);
                }
            }
        });
    }
    private void remLast(JTextField textField){
        textField.setText(textField.getText().substring(0,textField.getText().length()-1));
    }
}

