package view.File;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FilterText extends FileFilter{

    
    /** 
     * @param file nome file selezionato
     * @return boolean
     * 
     * Metodo che permette di visualizzare solo i file con estensione .txt
     */
    @Override
    public boolean accept(File file) {
        //se il file é una cartella deve essere visibile sempre
        if(file.isDirectory()){
            return true;
        }

        String estensione = Utils.getExtension(file);

        if(estensione == null){
            return false;
        }

        if(estensione.equals("txt")){
            return true;
        }
        
        return false;
    }

    
    /** 
     * @return String
     * 
     * Metodo che ritorna la stringa da mostare nel menu
     * per la scelta del file
     */
    @Override
    public String getDescription() {
        return "File Bilancio (*.txt)";
    }
    
}
