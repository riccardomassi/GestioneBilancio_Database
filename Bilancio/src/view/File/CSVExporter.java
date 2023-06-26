package view.File;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.table.TableModel;

/**
 * Classe che permette di esportare la tabella in formato CSV
 * ovvero ogni voce separata da una virgola
 */
public class CSVExporter extends TextExporter{
    public CSVExporter(){

    }
    
    
    /** 
     * @param tableModel modello astratto della tabella
     * @param fileName nome file su cui salvare
     * @throws IOException
     */
    @Override
    public void export(TableModel tableModel, File fileName) throws IOException{
        FileWriter csvWriter;
        try {
            csvWriter = new FileWriter(fileName);

            // scrive l'intestazione del file CSV
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                csvWriter.append(tableModel.getColumnName(i));
                csvWriter.append(",");
            }
            csvWriter.append("\n");

            // scrive i dati della tabella
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    csvWriter.append(tableModel.getValueAt(i, j).toString());
                    csvWriter.append(",");
                }
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
