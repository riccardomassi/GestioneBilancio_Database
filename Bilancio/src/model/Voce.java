package model;

import java.io.Serializable;

/**
 * Classe che gestisce il modello Voce
 */
public class Voce implements Serializable{
    private int id;
    private String data;
    private Double ammontare;
    private String descrizione;
    

    public Voce(int id, String data, double ammontare, String descrizione) {
        this.id = id;
        this.data = data;
        this.ammontare = ammontare;
        this.descrizione = descrizione;
    }

    /**
     * @return ID
     * 
     * Metodo che restituisce l'id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @param id
     * 
     * Metodo che imposta l'id
     */
    public void setId(int id) {
        this.id = id;
    }

    /** 
     * @return String 
     * 
     * Metodo che restituisce la data della voce
     */
    public String getData() {
        return this.data;
    }
    
    /** 
     * @param data
     * 
     * Metodo che imposta la data della voce
     */
    public void setData(String data) {
        this.data = data;
    }
    
    /** 
     * @return String
     * 
     * Metodo che restituisce l'ammontare della voce
     */
    public double getAmmontare() {
        return this.ammontare;
    }

    
    /** 
     * @param ammontare
     * 
     * Metodo che imposta l'ammontare della voce
     */
    public void setAmmontare(double ammontare) {
        this.ammontare = ammontare;
    }

    
    /** 
     * @return String
     * 
     * Metodo che restituisce la descrizione della voce
     */
    public String getDescrizione() {
        return this.descrizione;
    }

    
    /** 
     * @param descrizione
     * 
     * Metodo che imposta la descrizione della voce
     */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
