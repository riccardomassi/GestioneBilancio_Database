package view.Panel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 * Classe che serve per creare la stringa da visualizzare
 * dopo aver scelto la data nel calendario
 */
public class DateFormatter extends AbstractFormatter{

    
    /** 
     * @param text stringa della data selezionata
     * @return Date data convertita in valore Date 
     * @throws ParseException
     */
    @Override
    public Date stringToValue(String text) throws ParseException {
        if(text != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date data = dateFormat.parse(text);

            return data;
        }
        return null;
    }

    
    /** 
     * @param value valore della data selezionata
     * @return String data selezionata convertita in stringa
     * @throws ParseException
     * 
     * Metodo che ritorna la data selezionata in formato stringa
     */
    @Override
    public String valueToString(Object value) throws ParseException {
        if(value != null){
            Calendar calendar = (Calendar)value;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = dateFormat.format(calendar.getTime());

            return strDate;
        }
        return null;
    }
    
}
