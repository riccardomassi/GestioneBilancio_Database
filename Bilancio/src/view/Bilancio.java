package view;

import model.Postgres;

public class Bilancio {
    
    /** 
     * @param args
     * @throws Exception
     * 
     * Main che si occupa di creare il Frame
     */
    public static void main(String[] args) throws Exception {
        Postgres postgres = new Postgres();
        postgres.connect();
        new Frame(postgres);
    }
}
