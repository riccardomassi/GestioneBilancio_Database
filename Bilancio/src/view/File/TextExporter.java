package view.File;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.table.TableModel;

/**
 * Classe che permette di esportare la tabella in formato testo
 * ovvero ogni voce separata da uno spazio
 */
public class TextExporter {
    public TextExporter(){

    }

    
    /** 
     * @param tableModel modello astratto della tabella
     * @param fileName nome file su cui salvare
     * @throws IOException
     */
    public void export(TableModel tableModel, File fileName) throws IOException{
        FileWriter txtWriter;
        try {
            txtWriter = new FileWriter(fileName);

            // scrive l'intestazione del file di testo
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                txtWriter.append(tableModel.getColumnName(i));
                txtWriter.append(" ");
            }
            txtWriter.append("\n");

            // scrive i dati della tabella
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    txtWriter.append(tableModel.getValueAt(i, j).toString());
                    txtWriter.append(" ");
                }
                txtWriter.append("\n");
            }

            txtWriter.flush();
            txtWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
