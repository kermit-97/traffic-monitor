package trafficmonitor.dati;

public abstract class DatoCentralina extends Dato {
    protected int attesaMassima;

    public DatoCentralina() {}

    public int getAttesaMassima()
    {
        return attesaMassima;
    }

    public void setAttesaMassima(int attesaMassima)
    {
        this.attesaMassima = attesaMassima;
    }
}
