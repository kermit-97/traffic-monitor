package trafficmonitor.utenti;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControllerInterfacciaUtente {
    public ControllerInterfacciaUtente() {
        InterfacciaUtenteGUI view = new InterfacciaUtenteGUI();
        InterfacciaUtente interfacciaUtente = new InterfacciaUtente(view);

        view.getJButton_Aggiorna().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                interfacciaUtente.aggiornaInformazioni();
            }
        });
    }

}
