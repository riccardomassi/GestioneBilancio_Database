package view.Table;

import javax.swing.*;

import java.awt.*;
import java.util.List;
import model.Voce;
import java.awt.event.*;

import model.Postgres;

/**
 * Classe che gestisce il pannello della Tabella
 * Viene passato il database al costruttore per la funzione elimina voce
 */
public class TablePanel extends JPanel {
    
    private JTable table;
    private TableModel tableModel;
    private JPopupMenu popupMenu;
    private TableListener tableListener;
    
    public TablePanel(Postgres postgres){
        tableModel = new TableModel();
        table = new JTable(tableModel);

        /*
         * Gestione Popup Menu tasto destro per eliminare
         * una voce dalla tabella
         */
        popupMenu = new JPopupMenu();
        JMenuItem menuItemEliminaVoce = new JMenuItem("Elimina Voce");
        popupMenu.add(menuItemEliminaVoce);

        //Visualizzo il popup menu cliccando col tasto destro
        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                //Button3 = tasto destro mouse
                if(e.getButton() == MouseEvent.BUTTON3){
                    popupMenu.show(table, e.getX(), e.getY());
                }
            }
        });

        //Cliccando su "Elimina Voce" viene eliminata la voce selezionata
        menuItemEliminaVoce.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                int rowIndexDelete = table.getSelectedRow();
                //elimino la riga dalla tabella
                tableModel.fireTableRowsDeleted(rowIndexDelete, rowIndexDelete);

                //elimino la voce dal database e aggiorno la tabella
                int idToDelete = tableModel.getIdAt(rowIndexDelete);
                postgres.delVoce(idToDelete);
                setData(postgres.getVoci());
                aggiorna();

                TableEvent tableEvent = new TableEvent(this, rowIndexDelete);

                if(tableListener != null){
                    tableListener.tableEventListener(tableEvent);
                }
            }
        });
        
        //setto il layout della tabella
        setLayout(new BorderLayout());
        //aggiungo la tabella al centro del pannello
        add(new JScrollPane(table), BorderLayout.CENTER);

    }
    
    /** 
     * @param listaVoci
     * 
     * Metodo che mi permette di settare le Voci nel TableModel
     */
    public void setData(List<Voce> listaVoci){
        tableModel.setData(listaVoci);
    }

    /*
     * Metodo che aggiorna la Tabella coi nuovi dati aggiunti
     */
    public void aggiorna(){
        tableModel.fireTableDataChanged();
    }

    
    /** 
     * @param textToSearch testo da cercare 
     * @param currentIndex indice di riga corrente
     * @return int
     *
     * Metodo che cerca la stringa passata a partire dall'indice di riga passato 
     * all'interno della Tabella, e se la trova viene evidenziata
     */
    public int searchText(String textToSearch, int currentIndex){
        currentIndex = findNextIndex(textToSearch, currentIndex + 1);
        //Evidenzio la riga che contiene la stringa trovata
        table.setRowSelectionInterval(currentIndex, currentIndex);
        
        return currentIndex;
    }

    
    /** 
     * @param textToSearch testo da cercare
     * @param startIndex indice riga da cui partire a cercare
     * @return int
     *
     * Metodo per la gestione dell'indice di riga di partenza
     * per la ricerca del testo nella tabella
     */
    private int findNextIndex(String textToSearch, int startIndex) {
        for (int i = startIndex; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                //salvo ogni cella in una stringa da confrontare col testo
                String descrizione = tableModel.getValueAt(i, j).toString();
                    if (descrizione.contains(textToSearch)) {
                        //ritorno l'indice della riga in cui è stato trovato il testo
                        return i;
                    }
            }
        }
        return 0;
    }

    
    /** 
     * @param tableListener
     *
     * Metodo per impostare il TableListener
     */
    public void setTableListener(TableListener tableListener){
        this.tableListener = tableListener;
    }   

    
    /** 
     * @return TableModel
     *
     * Metodo per ottenere il TableModel
     */
    public TableModel getModel(){
        return tableModel;
    }

    /** 
     * @return Jtable
     *
     * Metodo per ottenere il JTable
     */
    public JTable getTable(){
        return table;
    }

    /**
     * Metodo stampare la tabella con la stampante
     */
    public void print(){
        try{
            table.print();
        }catch(java.awt.print.PrinterException e1){
            JOptionPane.showMessageDialog(TablePanel.this, "Ammontare non può essere 0", 
                            "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
}
