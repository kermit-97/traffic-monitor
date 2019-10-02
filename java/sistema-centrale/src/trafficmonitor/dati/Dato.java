package trafficmonitor.dati;
import java.io.Serializable;
import java.util.Date;

public abstract class Dato implements Serializable {
    protected Date dataOra;
    protected Coordinate coordinate;

    public Dato() {}

    public Coordinate getCoordinate()
    {
        return coordinate;
    }

    public Date getDataOra()
    {
        return dataOra;
    }

    public void setCoordinate(Coordinate coordinate)
    {
        this.coordinate = coordinate;
    }

    public void setDataOra(Date dataOra)
    {
        this.dataOra = dataOra;
    }
}


