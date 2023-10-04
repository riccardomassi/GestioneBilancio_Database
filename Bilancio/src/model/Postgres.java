package model;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Classe che si occupa di gestire il database in Postgresql
 */
public class Postgres {
    private Connection connection;
    private String jdbcURL= "jdbc:postgresql://localhost:5432/bilancio";
    private String username = "ricky";
    private String password = "Rccmss01";

    /**
     * Metodo per connettersi al server
     */
    public void connect(){
        try{
            Class.forName ("org.postgresql.Driver");  // Load the Driver
            connection = DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connected to PostgreSQL server");
        }
        catch (SQLException e) {
            System.out.println("Error in connecting to PostgreSQL server");
            e.printStackTrace();
        }
        catch(ClassNotFoundException e2){
            e2.printStackTrace();
        }
    }

    /**
     * Metodo per disconnettersi dal server
     * @throws SQLException
     */
    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * @return int totale
     * 
     * Metodo che calcola e ritorna il totale del bilancio
     */
    public String getTotale(){
        double totale = 0;
        try {
            Statement stmt = connection.createStatement();
            String query = "select sum(ammontare) from vocibilancio";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                totale = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Totale: "+String.valueOf(totale);
    }

    /**
     * @return int lastId
     * 
     * Metodo che ritorna il valore dell'ultimo id attuale
     */
    public int getLastId(){
        int lastId = 0;
        try {
            Statement stmt = connection.createStatement();
            String query = "select max(id) from vocibilancio";
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                lastId = rs.getInt(1);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastId;
    }

    /**
     * @return List<Voce> lista delle voci nel databse
     * 
     * Metodo che ritorna le voci all'interno del database
     */
    public List<Voce> getVoci(){
        ArrayList<Voce> voci = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String query = "select * from vocibilancio";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                String data = rs.getString("data");
                double ammontare = rs.getDouble("ammontare");
                String descrizione = rs.getString("descrizione");
                Voce voce = new Voce(id, data, ammontare, descrizione);
                voci.add(voce);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        System.out.println(voci);
        return voci;
    }
    
    /**
     * @param voce voce del database
     * Metodo che inserisce una voce nel database
     */
    public void addVoce(Voce voce){
        int id = voce.getId();
        String data = voce.getData();
        double ammontare = voce.getAmmontare();
        String descrizione = voce.getDescrizione();

        String query = "insert into vocibilancio values(?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setString(2, data);
            statement.setDouble(3, ammontare);
            statement.setString(4, descrizione);
            statement.executeQuery();
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * @param voce voce del database
     * Metodo che elimina una voce nel database
     */
    public void delVoce(int idToDelete){
        String query = "delete from vocibilancio where id=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idToDelete);
            statement.executeQuery();
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    /**
     * @param voce voce del database
     * Metoto che modifica una voce nel database
     */
    public void modifyVoce(Voce voce){
        int id = voce.getId();
        String data = voce.getData();
        double ammontare = voce.getAmmontare();
        String descrizione = voce.getDescrizione();

        String query = "update vocibilancio set data=?, ammontare=?, descrizione=? where id=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, data);
            statement.setDouble(2, ammontare);
            statement.setString(3, descrizione);
            statement.setInt(4, id);
            statement.executeQuery();
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
