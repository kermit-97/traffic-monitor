package trafficmonitor.gestori;

import trafficmonitor.centraline.Centralina;
import trafficmonitor.dati.*;
import trafficmonitor.utenti.Utente;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager implements IDatabaseManager {

    private static DatabaseManager istanza = new DatabaseManager();

    private DatabaseManager() { }

    public static DatabaseManager getInstance() {
        return istanza;
    }

    private static Connection getDBconnection() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:db/traffic_monitor.db";
            // crea una connessione con il db
            conn = DriverManager.getConnection(url);
            System.out.println("Connessione a SQLite stabilita");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    /**
     * @return la lista di segmenti stradali recuperata dal database
     */
    @Override
    public ArrayList<SegmentoStradale> getSegmentiStradali(){
        ArrayList<SegmentoStradale> segmenti = new ArrayList<SegmentoStradale>();
        String nome;
        String codSegmento;
        Coordinate coordInizio;
        Coordinate coordFine;
        int velMax;
        int indiceDiFlusso;

        String selectSQL = "SELECT * FROM Segmento" ;
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            // esecuzione select SQL
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                nome = rs.getString("nome");
                codSegmento = rs.getString("codSegmento");
                coordInizio = new Coordinate(rs.getString("latInizio"),rs.getString("lonInizio"));
                coordFine = new Coordinate(rs.getString("latFine"),rs.getString("lonFine"));
                velMax = rs.getInt("velMax");
                indiceDiFlusso = 0;
                //creazione istanza centralina
                SegmentoStradale s = new SegmentoStradale(nome,codSegmento,coordInizio,coordFine,indiceDiFlusso,velMax);
                segmenti.add(s);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage() + "   "+e.getSQLState() + "  " + e.getErrorCode());
        }
        return segmenti;
    }

    /**
     * @param codSegmento indica il segmento da aggiornare nel db
     * @param index indica il nuovo indice del segmento
     */
    @Override
    public void updateIndiceDiFlussoSegmentoStradale(String codSegmento, int index){
        String selectSQL = "UPDATE Segmento SET indiceDiFlusso = ? WHERE codSegmento = ?";
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            // esecuzione select SQL
            pstmt.setInt(1, index);
            pstmt.setString(2, codSegmento);

            //esecuzione
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * @return la lista delle centraline recuperata dal database
     */
    @Override
    public ArrayList<Centralina> getCentraline(){
        //query DB e cast del risultato in oggetti di tipo centralina in un arrylist
        ArrayList<Centralina> centraline = new ArrayList<Centralina>();
        Coordinate coordinate;
        String codSeriale;
        Boolean attivo;
        Boolean funzionante;
        Date ultimoInvio;
        SegmentoStradale segmento;

        String selectSQL = "SELECT * FROM Centralina" ;
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            // esecuzione select SQL
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                codSeriale = rs.getString("codSeriale");
                attivo = rs.getBoolean("attivo");
                funzionante = rs.getBoolean("funzionante");
                coordinate = new Coordinate(rs.getString("lat"),rs.getString("lon"));
                ultimoInvio = new Date();
                segmento = GestoreSegmentoStradale.getInstance().getSegmentoDaCodice(rs.getString("codSegmento"));
                //creazione istanza centralina
                Centralina c = new Centralina(codSeriale,attivo,funzionante,coordinate,ultimoInvio,segmento);
                centraline.add(c);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return centraline ;
    }

    /**
     * Aggiorna lo stato di funzionamento di una centralina
     * @param codSeriale indica il codice della centralina da aggiornare
     * @param attiva indica lo stato da aggiornare
     */
    @Override
    public void updateFunzionamentoCentralina(String codSeriale, boolean attiva){
        String selectSQL = "UPDATE Centralina SET attivo = ? WHERE codSeriale = ?";
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            // esecuzione select SQL
            pstmt.setBoolean(1, attiva);
            pstmt.setString(2, codSeriale);

            //esecuzione
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Aggiorna l'ultimo invio di dato effettuato da una centralina
     * @param codSeriale indica il codice della centralina da aggiornare
     * @param data indica la data/ora da aggiornare come ultimo invio
     */
    @Override
    public void updateUltimoInvioCentralina(String codSeriale, Date data){
        String selectSQL = "UPDATE Centralina SET ultimoInvio = ? WHERE codSeriale = ?";
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            // esecuzione select SQL
            pstmt.setDate(1, new java.sql.Date(data.getTime()));
            pstmt.setString(2, codSeriale);

            //esecuzione
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * @return la lista degli utenti recuperata dal database
     */
    @Override
    public ArrayList<Utente> getUtenti(){
        //query DB e cast del risultato in oggetti di tipo utente in un arraylist
        ArrayList<Utente> utenti = new ArrayList<Utente>();
        String username;
        String email;
        String password;
        Coordinate coord;

        String selectSQL = "SELECT * FROM Utente" ;
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            // esecuzione select SQL
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                username = rs.getString("username");
                email = rs.getString("email");
                password = rs.getString("password");
                coord = new Coordinate(rs.getString("lat"),rs.getString("lon"));
                //crazione istanza utente
                Utente u = new Utente(username,email,password,coord);
                utenti.add(u);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "   "+e.getSQLState() + "  " + e.getErrorCode());
        }
        return utenti ;
    }

    /**
     * @return la lista dei dati inviati dalle centraline recuperata dal database
     */
    @Override
    public ArrayList<Dato> getDatiCentralina(){
        //query DB e cast del risultato in oggetti di tipo DatoCentralina in un arraylist
        ArrayList<Dato> datiCentralinaStradale = new ArrayList<Dato>();
        String codCentralina;
        Date dataOra;
        Coordinate coord;
        int numeroMacchine;
        double velMedia;
        int indiceDiFlusso;
        int attesaMassiama;

        String selectSQL = "SELECT * FROM DatoCentralinaStradale" ;
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            // esecuzione select SQL
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                codCentralina = rs.getString("codCentralina");
                numeroMacchine = rs.getInt("numeroMacchine");
                dataOra = rs.getDate("dataOra");
                coord = new Coordinate(rs.getString("lat"),rs.getString("lon"));
                velMedia = rs.getInt("velocitaMedia");
                indiceDiFlusso = rs.getInt("indiceDiFlusso");
                attesaMassiama = rs.getInt("attesaMassima");
                //crazione istanza dato centralina stradale
                DatoCentralinaStradale d = new DatoCentralinaStradale(dataOra,coord,attesaMassiama,codCentralina,numeroMacchine,velMedia,indiceDiFlusso);
                datiCentralinaStradale.add(d);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "   "+e.getSQLState() + "  " + e.getErrorCode());
        }
        return datiCentralinaStradale ;
    }

    /**
     * Inserisce nel database il dato inviato da una centralina
     * @param dato indica il dato da aggiungere al database
     */
    @Override
    public void addDatoCentralina(DatoCentralinaStradale dato){
        String selectSQL = "INSERT INTO DatoCentralinaStradale (codCentralina, dataOra, lat, lon, numeroMacchine, velocitaMedia, indiceDiFlusso, attesaMassima) VALUES (?, ?, ?, ?, ?, ?, ?, ?)" ;
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, dato.getCodCentralina());
            pstmt.setDate(2, new java.sql.Date(dato.getDataOra().getTime()));
            pstmt.setString(3,dato.getCoordinate().getLatitudine());
            pstmt.setString(4,dato.getCoordinate().getLongitudine());
            pstmt.setInt(5,dato.getNumeroMacchine());
            pstmt.setDouble(6,dato.getVelocitaMedia());
            pstmt.setInt(7,dato.getIndiceDiFlusso());
            pstmt.setInt(8,dato.getAttesaMassima());
            //esecuzione
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "   "+e.getSQLState() + "  " + e.getErrorCode());
        }
    }

    /**
     * @return la lista delle segnalazioni inviate dagli utenti recuperata dal database
     */
    @Override
    public ArrayList<Dato> getSegnalazioni(){
        //query DB e cast del risultato in oggetti di tipo DatoCentralina in un arraylist
        ArrayList<Dato> datiSegnalazione = new ArrayList<Dato>();
        String username;
        Date dataOra;
        Coordinate coord;
        TipoSegnalazione tipoSegn;

        String selectSQL = "SELECT * FROM SegnalazioneUtente" ;
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            // esecuzione select SQL
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                username = rs.getString("username");
                dataOra = rs.getDate("dataOra");
                coord = new Coordinate(rs.getString("lat"),rs.getString("lon"));
                tipoSegn = TipoSegnalazione.getByIndice(rs.getInt("indiceDiFlusso"));
                //crazione istanza segnalazione utente
                SegnalazioneUtente su = new SegnalazioneUtente(dataOra,coord,username,tipoSegn);
                datiSegnalazione.add(su);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "   "+e.getSQLState() + "  " + e.getErrorCode());
        }
        return datiSegnalazione ;
    }

    /**
     * Inserisce nel database la segnalazione inviata da un utente
     * @param segnalazione indica la segnalazione da aggiungere
     */
    @Override
    public void addSegnalazioneUtente(SegnalazioneUtente segnalazione){
        String selectSQL = "INSERT INTO SegnalazioneUtente (username, dataOra, lat, lon, indiceDiFlusso) VALUES (?, ?, ?, ?, ?)" ;
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, segnalazione.getUsername());
            pstmt.setDate(2, new java.sql.Date(segnalazione.getDataOra().getTime()));
            pstmt.setString(3,segnalazione.getCoordinate().getLatitudine());
            pstmt.setString(4,segnalazione.getCoordinate().getLongitudine());
            pstmt.setInt(5,segnalazione.getTipoSegnalazione().getIndiceDiTraffico());

            //esecuzione
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "   "+e.getSQLState() + "  " + e.getErrorCode());
        }
    }

    /**
     * @return la lista delle posizioni degli utenti recuperata dal database
     */
    @Override
    public ArrayList<Dato> getPosizioni(){
        //query DB e cast del risultato in oggetti di tipo DatoCentralina in un arraylist
        ArrayList<Dato> datiPosizioni = new ArrayList<Dato>();
        String username;
        Date dataOra;
        Coordinate coord;

        String selectSQL = "SELECT * FROM DatoPosizione" ;
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            // esecuzione select SQL
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                username = rs.getString("username");
                dataOra = rs.getDate("dataOra");
                coord = new Coordinate(rs.getString("lat"),rs.getString("lon"));
                //crazione istanza dato posizione
                DatoPosizione dp = new DatoPosizione(dataOra,coord,username);
                datiPosizioni.add(dp);
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "   "+e.getSQLState() + "  " + e.getErrorCode());
        }
        return datiPosizioni ;
    }

    /**
     * Insiere nel database il dato di posizione di un utente
     * @param dato indica il dato da aggiungere
     */
    @Override
    public void addDatoPosizione(DatoPosizione dato){
        String selectSQL = "INSERT INTO DatoPosizione (username, lat, lon, dataOra) VALUES (?, ?, ?, ?)" ;
        try (Connection conn = getDBconnection(); PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setString(1, dato.getUsername());
            pstmt.setString(2,dato.getCoordinate().getLatitudine());
            pstmt.setString(3,dato.getCoordinate().getLongitudine());
            pstmt.setDate(4, new java.sql.Date(dato.getDataOra().getTime()));

            //esecuzione
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage() + "   "+e.getSQLState() + "  " + e.getErrorCode());
        }
    }

}
