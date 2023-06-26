package view.Table;

import java.util.EventObject;

/**
 * Classe che si occupa di gestire i dati nella Tabella
 * per l'evento ELIMINA VOCE
 */
public class TableEvent extends EventObject {
    private int rowToDelete;

    public TableEvent(Object source) {
        super(source);
    }

    public TableEvent(Object source, int rowToDelete) {
        super(source);
        this.rowToDelete = rowToDelete;
    }
    
    /** 
     * @return int indice riga da eliminare
     */
    public int getRowToDelete(){
        return this.rowToDelete;
    }

    
    /** 
     * @param rowToDelete 
     */
    public void setRowToDelete(int rowToDelete){
        this.rowToDelete = rowToDelete;
    }
    
    
}
