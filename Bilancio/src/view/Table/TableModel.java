package view.Table;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Voce;

/**
 * Classe che gestisce il modello della tabella
 */
public class TableModel extends AbstractTableModel{

    private List<Voce> listaVoci;
    private String[] nomiColonne = {"ID", "Data", "Ammontare", "Descrizione"};

    /** 
     * @param listaVoci lista delle voci della Tabella
     */
    public void setData(List<Voce> listaVoci){
        this.listaVoci = listaVoci;
    }

    
    /** 
     * @param column
     * @return String
     * 
     * metodo per inserire i titoli delle colonne
     */
    @Override
    public String getColumnName(int column){
        return nomiColonne[column];
    }

    
    /** 
     * @return int numero colonne
     */
    @Override
    public int getColumnCount() {
        return 4;
    }

    
    /** 
     * @return int numero righe
     */
    @Override
    public int getRowCount() {
       return listaVoci.size();
    }

    /**
     * @param rowIndex indice di riga
     * @return Int
     * 
     * Metodo che ritorno l'id della voce selezionata
     */
    public int getIdAt(int rowIndex){
        //seleziono la riga della Voce
        Voce voce = listaVoci.get(rowIndex);

        //ritorno l'id della voce selezionata
        return voce.getId();
    }

    /**
     * @param rowIndex indice di riga
     * @return Voce
     * 
     * Metodo che ritorna la voce selezionata
     */
    public Voce getVoceAt(int rowIndex){
        //seleziono la riga della Voce
        Voce voce = listaVoci.get(rowIndex);

        //ritorno la voce selezionata
        return voce;
    }
    
    /** 
     * @param rowIndex indice riga
     * @param columnIndex indice colonna
     * @return Object
     * 
     * Metodo che ritorna il valore della riga e colonna passati
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        //seleziono la riga della Voce
        Voce voce = listaVoci.get(rowIndex);

        //seleziono la colonna corretta in base al columnIndex passato
        switch(columnIndex){
            case 0: return voce.getId();
            case 1: return voce.getData();
            case 2: return voce.getAmmontare();
            case 3: return voce.getDescrizione();
            default: return "";
        }
    }
}
