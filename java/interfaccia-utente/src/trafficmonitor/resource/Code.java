package trafficmonitor.resource;

import trafficmonitor.centraline.Centralina;
import trafficmonitor.dati.SegmentoStradale;

import java.util.ArrayList;

public class Code {

    private String codice;

    public String getCodice() {
        return codice;
    }

    private String setSegmenti(ArrayList<SegmentoStradale> listaSegmenti) {
        StringBuilder segmenti = new StringBuilder("var segmenti = [");
        StringBuilder indice = new StringBuilder("var indice = [");
        for (SegmentoStradale s : listaSegmenti) {
            segmenti.append("[{lat:" + s.getCoordinateInizio().getLatitudine() +", lng:" + s.getCoordinateInizio().getLongitudine() +"}," +
                    "{lat:" + s.getCoordinateFine().getLatitudine() +", lng:" +s.getCoordinateFine().getLongitudine() +"}],");
            indice.append(s.getIndiceDiFlussoAttuale() + ",");
        }
        segmenti.deleteCharAt(segmenti.length()-1);     // elimino l'ultima virgola
        indice.deleteCharAt(indice.length()-1);         // elimino l'utlima virgola

        segmenti.append("];");
        indice.append("];");

        return segmenti.toString() + indice.toString();
    }

    private String setCentraline(ArrayList<Centralina> listaCentraline) {
        StringBuilder centraline = new StringBuilder("var centraline = [");
        StringBuilder nome = new StringBuilder("var nome = [");
        for (Centralina c : listaCentraline) {
            centraline.append("{lat:" + c.getCoordinate().getLatitudine() +", lng:" + c.getCoordinate().getLongitudine() +"},");
            nome.append("'" + c.getCodSeriale() + "',");
        }
        centraline.deleteCharAt(centraline.length()-1);     // elimino l'ultima virgola
        nome.deleteCharAt(nome.length()-1);                 // elimino l'utlima virgola

        centraline.append("];");
        nome.append("];");

        return centraline.toString() + nome.toString();
    }

    public Code(ArrayList<SegmentoStradale> listaSegmenti, ArrayList<Centralina> listaCentraline) {
        this.codice = this.cod1 + "\n" + setCentraline(listaCentraline) + "\n" + this.cod2 + "\n" + setSegmenti(listaSegmenti) + "\n" + this.cod3;

        // System.out.println(codice);
    }

    private static String cod1 = "<html>\n" +
            "<head>\n" +
            "<script src=\"http://maps.google.com/maps/api/js?key=AIzaSyD30_f2Wuyl6ttOqPeo43vcZ3JPg2gXLoU&sensor=false\"></script>\n" +
            "<script>\n" +
            "\tvar map ;\n" +
            "\tfunction crea(){\n" +
            "\t\tvar centroCoord = new google.maps.LatLng(45.800077,9.092796); //coordinate in cui \"centrare\" la nostra mappa\n" +
            "\t\tvar opzioni = {zoom: 17, center: centroCoord, mapTypeId:google.maps.MapTypeId.ROADMAP};\n" +
            "\t\t// oggetto opzioni con le proprietà della mappa da visualizzare\n" +
            "\t\t//zoom indica di quanto zoommare la mappa\n" +
            "\t\t//center coordinate da centrare\n" +
            "\t\t//proprietàmapTypeId definisce il tipo di terreno\n" +
            "\t\tmap = new google.maps.Map(document.getElementById(\"mappa\"), opzioni); //associo al div la mia mappa\n" +
            "\n" +
            "\t\t//----lista centraline";
    private static String cod2 = "//----lista centraline\n" +
            "\n" +
            "\t\t//inserimento marker centralina\n" +
            "\t\tfor(i=0;i<centraline.length;++i){\n" +
            "\t\t\t//marker\n" +
            "\t\t\tvar marker = new google.maps.Marker({\n" +
            "\t\t\t\t\t position: centraline[i],\n" +
            "\t\t\t\t\t map: map,\n" +
            "\t\t\t\t\t label : nome[i]\n" +
            "\t\t\t\t });\n" +
            "\t\t}\n" +
            "\n" +
            "\t\t//----lista segmenti";
    private static String cod3 = "//----lista segmenti\n" +
            "\t\tfor(i=0;i<segmenti.length;++i){\n" +
            "\t\t\t//liena\n" +
            "\t    var statoSegmenti = new google.maps.Polyline({\n" +
            "\t          path: segmenti[i],\n" +
            "\t          geodesic: true,\n" +
            "\t          strokeColor: selectColor(indice[i]),\n" +
            "\t          strokeOpacity: 0.5,\n" +
            "\t          strokeWeight: 8\n" +
            "\t    });\n" +
            "\t\t\tstatoSegmenti.setMap(map);\n" +
            "\t\t}\n" +
            "\n" +
            "\t}\n" +
            "\tfunction selectColor(indice){\n" +
            "\t\tif((indice >= 0)&& (indice <= 30)){\n" +
            "\t\t\treturn '#28B740'; //verde - segmento libero\n" +
            "\t\t}\n" +
            "\t\tif((indice >30) && (indice <=70)){\n" +
            "\t\t\treturn '#E7D720';//giallo - segmento traficato\n" +
            "\t\t}\n" +
            "\t\tif((indice > 70) && (indice <=100)){\n" +
            "\t\t\treturn '#FF0000'; //rosso - segmento molto trafficato\n" +
            "\t\t}\n" +
            "\t}\n" +
            "  </script>\n" +
            "  </head>\n" +
            "  <body onload=\"crea()\">\n" +
            "  <div id=\"mappa\" style=\"position:absolute;top:0;left:0;height:100%;width:100%;\"></div>\n" +
            "  </div>\n" +
            "  </body>\n" +
            "  </html>";

}
