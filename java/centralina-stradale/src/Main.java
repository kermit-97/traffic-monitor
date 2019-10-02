import trafficmonitor.centraline.ControllerCentralinaStradale;
import trafficmonitor.dati.Coordinate;
import trafficmonitor.dati.SegmentoStradale;

import javax.swing.*;
import java.util.Date;

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
        Date data = new Date();

        // vengono istanziate 7 centraline

        Coordinate c1 = new Coordinate("45.800047", "9.090065");
        Coordinate c2 = new Coordinate("45.799053", "9.090840");
        SegmentoStradale s1 = new SegmentoStradale("Via Leone Leoni", "s000001", c1, c2, 0, 30);
        Coordinate cs1 = new Coordinate("45.799670","9.090313");
        ControllerCentralinaStradale cen1 = new ControllerCentralinaStradale("cs000001",true,true,cs1,data,s1);

        Coordinate c3 = new Coordinate("45.798996", "9.090880");
        Coordinate c4 = new Coordinate("45.798026", "9.091616");
        SegmentoStradale s2 = new SegmentoStradale("Via Leone Leoni","s000002", c3, c4, 0, 30);
        Coordinate cs2 = new Coordinate("45.798451","9.091227");
        ControllerCentralinaStradale cen2 = new ControllerCentralinaStradale("cs000002",true,true,cs2,data,s2);

        Coordinate c5 = new Coordinate("45.798022", "9.091681");
        Coordinate c6 = new Coordinate("45.798406", "9.092733");
        SegmentoStradale s3 = new SegmentoStradale("Via Viganò","s000003", c5, c6, 0, 50);
        Coordinate cs3 = new Coordinate("45.798265","9.092233");
        ControllerCentralinaStradale cen3 = new ControllerCentralinaStradale("cs000003",true,true,cs3,data,s3);

        Coordinate c7 = new Coordinate("45.798438", "9.092823");
        Coordinate c8 = new Coordinate("45.798889", "9.094062");
        SegmentoStradale s4 = new SegmentoStradale("Via Viganò","s000004", c7, c8, 0, 50);
        Coordinate cs4 = new Coordinate("45.798761","9.093526");
        ControllerCentralinaStradale cen4 = new ControllerCentralinaStradale("cs000004",true,true,cs4,data,s4);


        Coordinate c9 = new Coordinate("45.799906", "9.093251");
        Coordinate c10 = new Coordinate("45.799448", "9.092041");
        SegmentoStradale s5 = new SegmentoStradale("Via dei Mille","s000005", c9, c10, 0, 30);
        Coordinate cs5 = new Coordinate("45.799639","9.092650");
        ControllerCentralinaStradale cen5 = new ControllerCentralinaStradale("cs000005",true,true,cs5,data,s5);

        Coordinate c11 = new Coordinate("45.799414", "9.091936");
        Coordinate c12 = new Coordinate("45.799053", "9.090917");
        SegmentoStradale s6 = new SegmentoStradale("Via dei Mille","s000006", c11, c12, 0, 30);
        Coordinate cs6 = new Coordinate("45.799277","9.091435");
        ControllerCentralinaStradale cen6 = new ControllerCentralinaStradale("cs000006",true,true,cs6,data,s6);

        Coordinate c13 = new Coordinate("45.798935", "9.094064");
        Coordinate c14 = new Coordinate("45.799881", "9.093329");
        SegmentoStradale s7 = new SegmentoStradale("Via Palestro","s000007", c13, c14, 0, 30);
        Coordinate cs7 = new Coordinate("45.799462","9.093749");
        ControllerCentralinaStradale cen7 = new ControllerCentralinaStradale("cs000007",true,true,cs7,data,s7);
    }

}
