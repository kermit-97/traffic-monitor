package trafficmonitor.dati;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private String latitudine;
    private String longitudine;
    private static double raggioTerra = 6378.137 ;      // utilizzato per il calcolo della distanza in metri date due coordinate

    public Coordinate(){}

    public Coordinate(String lat, String lng) {
        this.latitudine = lat;
        this.longitudine = lng;
    }

    public String getLatitudine()
    {
        return latitudine;
    }

    public String getLongitudine()
    {
        return longitudine;
    }

    public void setLatitudine(String latitudine)
    {
        this.latitudine = latitudine;
    }

    public void setLongitudine(String longitudine)
    {
        this.longitudine = longitudine;
    }

    /**
     * Due coordinate sono uguali se latitudine e longitude sono uguali
     */
    public boolean equals(Coordinate c) {
        return Double.parseDouble(this.latitudine)==Double.parseDouble(c.latitudine) && Double.parseDouble(this.longitudine)==Double.parseDouble(c.longitudine);
    }

    /**
     * Calcola il punto medio tra le coordinate dell'oggetto e le coordinate c passsate
     * @param c
     * @return il punto medio del segmento rappresentato agli estremi dalle coordinate dell'oggetto e le coordinate c
     */
    public Coordinate puntoMedio(Coordinate c) {
        Double lat1 = Double.parseDouble(this.latitudine);
        Double long1 = Double.parseDouble(this.longitudine);
        Double lat2 = Double.parseDouble(c.latitudine);
        Double long2 = Double.parseDouble(c.longitudine);
        return new Coordinate(Double.toString((lat1+lat2)/2),Double.toString((long1+long2)/2));
    }

    /**
     * Calocla la distanza tra le coordinate dell'oggetto e le coordinate passate
     * @param c
     * @return la distanza in metri tra le coordinate dell'oggeto e le coordinate c
     */
    public double calcolaDistanzaMetri(Coordinate c) {
        Double lat1 = Double.parseDouble(this.latitudine);
        Double long1 = Double.parseDouble(this.longitudine);
        Double lat2 = Double.parseDouble(c.latitudine);
        Double long2 = Double.parseDouble(c.longitudine);
        Double distanza = (raggioTerra*Math.PI*Math.sqrt(Math.pow(lat2-lat1,2) + Math.cos(lat2*Math.PI/180)*Math.cos(lat1*Math.PI/180)*Math.pow(long2-long1,2))/180);

        return distanza*1000 ;
    }

    /**
     * Calcola la distanza tra le coordinate dell'oggetto e le coordinate passate
     * @param c
     * @return la distanza puntuale tra le coordinate dell'oggetto e le coordinate c
     */
    public double distanza(Coordinate c) {
        Double lat1 = Double.parseDouble(this.latitudine);
        Double long1 = Double.parseDouble(this.longitudine);
        Double lat2 = Double.parseDouble(c.latitudine);
        Double long2 = Double.parseDouble(c.longitudine);

        return  Math.sqrt(Math.pow(lat2-lat1,2.0) + Math.pow(long2-long1,2.0));
    }
}
