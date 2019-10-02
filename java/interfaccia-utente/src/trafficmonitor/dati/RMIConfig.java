package trafficmonitor.dati;

public enum RMIConfig {
    // elementi dell'enumerazione
    GESTORECENTRALINA("/GestoreCentralina"),
    GESTOREDATO("/GestoreDato"),
    GESTORESEGMENTOSTRADALE("/GestoreSegmentoStradale"),
    GESTOREUTENTE("/GestoreUtente");

    // attributi
    private static String IP = "localhost:";
    private String hostName;
    private static String port = "1234";

    RMIConfig(String nome)
    {
        this.hostName = nome;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIP() {
        return IP;
    }

    public String getHostName() {
        return hostName;
    }

    public String getPort() {
        return port;
    }
}
