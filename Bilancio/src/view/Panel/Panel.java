package view.Panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.*;

import org.jdatepicker.impl.*;

import model.Postgres;
import model.Voce;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import view.Table.TablePanel;

/**
 * Classe che si occupa di gestire il Panello
 */
public class Panel extends JPanel{

    private JLabel labelData;
    private UtilDateModel dateModel;
    private Properties properties;
    private JDatePanelImpl datePanel;
    private JDatePickerImpl datePicker;
    private DateFormatter dateFormatter;

    private JLabel labelAmmontare;
    private JTextField fieldAmmontare;

    private JLabel labelDescrizione;
    private JTextField fieldDescrizione;

    private JLabel labelVisualizza;
    private JComboBox<String> boxVisualizza;
    private JTextField fieldVisualizza;

    private JButton aggiungi;
    private JButton modifica;

    private JLabel labelRicerca;
    private JTextField fieldRicerca;
    private JButton ricerca;

    private JLabel labelInizio;
    private JTextField fieldInizio;
    private JLabel labelFine;
    private JTextField fieldFine;

    private JButton visualizza;
    private JButton indietro;

    /**
     * @param database lista che tiene memoria delle voci in tabella
     * @param tablePanel pannello della tabella
     * @param voci lista che gestisce le voci della tabella
     * @param fieldTotale testo che stampa il totale
     * 
     * Classe che gestisce il Pannello
     */
    public Panel(TablePanel tablePanel, JTextField fieldTotale, Postgres postgres){
        /*
         * Set layout e border
         */
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Gestione Dati"));

        //connessione al server PostgreSQL
        //postgres.connect();

        /*
         * Componenti
         */
        labelData = new JLabel("Data:");
        //le prossime 9 righe di codice servono per la creazione del calendario
        dateModel = new UtilDateModel();
        properties = new Properties();
        properties.put("text.today","Today");
        properties.put("text.month","Month");
        properties.put("text.year","Year");
        dateModel.setSelected(true); //metodo per settare la data odierna di default
        datePanel = new JDatePanelImpl(dateModel, properties);
        dateFormatter = new DateFormatter();
        datePicker = new JDatePickerImpl(datePanel, dateFormatter);
        
        // Imposto label e field Ammontare
        labelAmmontare = new JLabel("Ammontare:");
        fieldAmmontare = new JTextField(25);
        
        // Imposto label e field Descrizione
        labelDescrizione = new JLabel("Descrizione:");
        fieldDescrizione = new JTextField(25);

        // Imposto bottone Aggiungi e Modifica
        aggiungi = new JButton("Aggiungi");
        modifica = new JButton("Modifica");

        //Giorno, Settimana, Mese, Anno da visualizzare
        labelVisualizza = new JLabel("Filtra:");
        String[] boxOptions = {"Giorno", "Settimana", "Mese", "Anno", "Altro"};
        boxVisualizza = new JComboBox<>(boxOptions);

        // Imposto label e field Inizio e Fine 
        labelInizio = new JLabel("Inizio:");
        fieldInizio = new JTextField(10);
        labelFine = new JLabel("Fine:");
        fieldFine = new JTextField(10);
        //Defualt sono invisibili, diventano visibili quando l'utente vuole
        //visualizzare i dati in un range di tempo arbitrario
        labelInizio.setVisible(false);
        fieldInizio.setVisible(false);
        labelFine.setVisible(false);
        fieldFine.setVisible(false);

        // Imposto field Visualizza
        fieldVisualizza = new JTextField(20);
        fieldVisualizza.setVisible(true);

        // Imposto bottoni Visualizza e Indietro
        visualizza = new JButton("visualizza");
        indietro = new JButton("Indietro");

        // Imposto label, field e bottone Ricerca
        labelRicerca = new JLabel("Cerca testo:");
        fieldRicerca = new JTextField(25);
        ricerca = new JButton("Ricerca");

        tablePanel.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tablePanel.getTable().getSelectedRow();

                    if (selectedRow != -1) {
                        //ottengo la voce selezionata e prendo i suoi parametri
                        Voce selectedVoce = tablePanel.getModel().getVoceAt(selectedRow);
                        String data = selectedVoce.getData();
                        double ammontare = selectedVoce.getAmmontare();
                        String descrizione = selectedVoce.getDescrizione();  
                        
                        fieldAmmontare.setText(String.valueOf(ammontare));
                        fieldDescrizione.setText(descrizione);
                        Date date = null;
                        try {
                            date = dateFormatter.stringToValue(data);
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        dateModel.setValue(date);
                    }
                }
            }
        });

        /*
         * Gestione bottone Aggiungi:
         * Viene creata una classe anonima ActionListener che implementa il metodo actionPerformed,
         * che permette di gestire cosa accade quando viene premuto il bottone
         */
        aggiungi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    String data = datePicker.getJFormattedTextField().getText();
                    double ammontare = Double.parseDouble(fieldAmmontare.getText());
                    String descrizione = fieldDescrizione.getText();
                    //Arrotondo a due cifre decimali
                    ammontare = Math.round(ammontare*100.0)/100.0;

                    //Se ammontare = 0 stampo errore
                    if (ammontare == 0){
                        JOptionPane.showMessageDialog(Panel.this, "Ammontare non può essere 0", 
                            "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        //recupero l'ultimo id dal database
                        int currentId = postgres.getLastId()+1;

                        //aggiungo la voce nel database e aggiorno la tabella
                        Voce voce = new Voce(currentId, data, ammontare, descrizione);
                        postgres.addVoce(voce);
                        tablePanel.setData(postgres.getVoci());
                        tablePanel.aggiorna();

                        //gestione somma totale del bilancio
                        fieldTotale.setText(postgres.getTotale());
                    }
                } catch(Exception e1){
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(Panel.this, "Dati inseriti errati", 
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }

                //Reset dei TextField e della data
                fieldAmmontare.setText("");
                fieldDescrizione.setText("");
                Date date = new Date();
                dateModel.setValue(date);
                dateModel.setSelected(true);
            }
        });

        /*
         * Gestione bottone Modifica
         */
        modifica.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    String data = datePicker.getJFormattedTextField().getText();
                    double ammontare = Double.parseDouble(fieldAmmontare.getText());
                    String descrizione = fieldDescrizione.getText();
                    //Arrotondo a due cifre decimali
                    ammontare = Math.round(ammontare*100.0)/100.0;

                    if (ammontare == 0){
                        JOptionPane.showMessageDialog(Panel.this, "Ammontare non può essere 0", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        int rowToChange = tablePanel.getTable().getSelectedRow();
                        //recupero l'id della voce selezionata
                        int idVoceToChange = tablePanel.getModel().getIdAt(rowToChange);
                        Voce voceToChange = new Voce(idVoceToChange, data, ammontare, descrizione);

                        //Modifico la voce nel database e aggiorno la tabella
                        postgres.modifyVoce(voceToChange);
                        tablePanel.setData(postgres.getVoci());
                        tablePanel.aggiorna();
                        
                        //gestione somma totale del bilancio
                        fieldTotale.setText(postgres.getTotale());
                    }
                } catch(Exception e1){
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(Panel.this, "Dati inseriti errati", 
                        "Errore", JOptionPane.ERROR_MESSAGE);
                }

                //Reset dei TextField e della data
                fieldAmmontare.setText("");
                fieldDescrizione.setText("");
                Date date = new Date();
                dateModel.setValue(date);
                dateModel.setSelected(true);
            }
        });

        /*
         * Gestione bottone Ricerca
         */
        ricerca.addActionListener(new ActionListener(){
            int index = -1;
            @Override
            public void actionPerformed(ActionEvent e){
                String textToSearch = fieldRicerca.getText().toString();
                //Se il text field è vuoto esco
                if (textToSearch.equals("")){
                    return;
                }
                //chiamo il metodo che cerca il testo e salvo l'indice di riga 
                //dell'ultimo match trovato
                index = tablePanel.searchText(textToSearch, index);
            }
        });

        /*
         * Set label e field per visualizzare i dati nella Tabella
         */
        boxVisualizza.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String boxChoose = boxVisualizza.getSelectedItem().toString();

                /*
                 * Se l'utente seleziona Altro devo mostrare
                 * i label e i field di Inizio e Fine per inserire una data arbitraria
                 * altrimenti mostro solamente i label e field Visualizza
                 */
                if (boxChoose.equals("Altro")){
                    fieldVisualizza.setVisible(false);
                    labelInizio.setVisible(true);
                    fieldInizio.setVisible(true);
                    labelFine.setVisible(true);
                    fieldFine.setVisible(true);
                }
                else{
                    fieldVisualizza.setVisible(true);
                    labelInizio.setVisible(false);
                    fieldInizio.setVisible(false);
                    labelFine.setVisible(false);
                    fieldFine.setVisible(false);
                }
            }
        });

        /*
         * Gestione bottone Visualizza
         */
        visualizza.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String boxChoose = boxVisualizza.getSelectedItem().toString();
                Date inizio = null;
                Date fine = null;

                switch(boxChoose){
                    /*
                     * Prendo la data inserita e la setto come
                     * inizio e fine range 
                     */
                    case "Giorno": {
                        String day = fieldVisualizza.getText();
                        try {
                            inizio = new SimpleDateFormat("dd/MM/yyyy").parse(day);
                            fine = new SimpleDateFormat("dd/MM/yyyy").parse(day);

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(Panel.this, "Formato data errato\n Scrivere data nel formato dd/MM/yyyy", 
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                    
                    /*
                     * Prendo la data inserita e la setto come
                     * inizio range e poi aggiungo 6 giorni e setto
                     * la data di fine range
                     */
                    case "Settimana": {
                        String week = fieldVisualizza.getText();
                        try {
                            inizio = new SimpleDateFormat("dd/MM/yyyy").parse(week);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(inizio);
                            calendar.add(Calendar.DATE, 6);
                            fine = calendar.getTime();

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(Panel.this, "Formato data errato\n Scrivere data nel formato dd/MM/yyyy",
                             "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    } 

                    /*
                     * Nel field visualizza viene inserito mese e anno (MM/yyyy),
                     * vado a calcolare il primo e ultimo giorno del mese e 
                     * li setto come inizio e fine range
                     */
                    case "Mese": {
                        String date = fieldVisualizza.getText();
                        Calendar calendar = Calendar.getInstance();

                        //Se il TextField è vuoto Errore
                        if (fieldVisualizza.getText().equals("")){
                            JOptionPane.showMessageDialog(Panel.this, "Formato data errato\n Scrivere mese e anno nel formato MM/yyyy", 
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                        else{
                            try {
                                int month = Integer.parseInt(date.substring(0, 2));
                                int year = Integer.parseInt(date.substring(3));
                                // calendar conta i mesi da 0 a 11, quindi bisogna sottrarre 1
                                calendar.set(Calendar.MONTH, month - 1); 
                                calendar.set(Calendar.YEAR, year);
                                // impostare il primo giorno del mese
                                calendar.set(Calendar.DAY_OF_MONTH, 1);
                                // ottenere la data di inizio nel tipo Date
                                inizio = calendar.getTime(); 
    
                                // impostare l'ultimo giorno del mese
                                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                                // ottenere la data di fine nel tipo Date
                                fine = calendar.getTime(); 
    
                            } catch (NumberFormatException e1) {
                                e1.printStackTrace();
                                JOptionPane.showMessageDialog(Panel.this, "Formato data errato\n Scrivere mese e anno nel formato MM/yyyy", 
                                "Errore", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                        break;
                    }

                    /*
                     * Prendo l'anno inserito,
                     * setto il primo giorno e mese con 1 gennaio e setto inizio range
                     * setto il primo giorno e mese con 31 dicembre e setto fine range
                     */
                    case "Anno": {
                        int year = 0;
                        // Try Catch per controllare che l'anno sia giusto
                        try{
                            year = Integer.parseInt(fieldVisualizza.getText());
                        }catch (NumberFormatException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(Panel.this, "Anno errato\n Scrivere anno nel formato yyyy", 
                            "Errore", JOptionPane.ERROR_MESSAGE);
                        }

                        Calendar cal = Calendar.getInstance();
                        // set anno con quello inserito dall'utente
                        cal.set(Calendar.YEAR, year);
                        // set mese con Gennaio
                        cal.set(Calendar.MONTH, Calendar.JANUARY);
                        // set giorno 1
                        cal.set(Calendar.DAY_OF_MONTH, 1);
                        // ottenere la data di inizio nel tipo Date
                        inizio = cal.getTime();
                        
                        // set mese con Dicembre
                        cal.set(Calendar.MONTH, Calendar.DECEMBER);
                        // set giorno 31
                        cal.set(Calendar.DAY_OF_MONTH, 31);
                        // ottenere la data di fine nel tipo Date
                        fine = cal.getTime();
                        break;
                    }

                    /*
                     * Setto inizio e fine range con le date inserite
                     * nei rispettivi field
                     */
                    case "Altro": {
                        String inizioStr = fieldInizio.getText();
                        String fineStr = fieldFine.getText();
                        try {
                            inizio = new SimpleDateFormat("dd/MM/yyyy").parse(inizioStr);
                            fine = new SimpleDateFormat("dd/MM/yyyy").parse(fineStr);

                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            JOptionPane.showMessageDialog(Panel.this, "Formato data errato\n Scrivere data nel formato dd/MM/yyyy", 
                                "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                }

                ArrayList<Voce> newVoci = new ArrayList<Voce>();
                double totale = 0;

                //Creo il nuovo array di appoggio con le voci comprese nelle date di inizio e fine
                for (Voce v : postgres.getVoci()){
                    try {
                        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(v.getData());
                        if (date.compareTo(inizio) >= 0 && date.compareTo(fine) <= 0){
                            newVoci.add(v);
                            totale += v.getAmmontare();
                        }
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                }
        
                if(!newVoci.isEmpty()){
                    //Arrotondo a due cifre decimali
                    totale = Math.round(totale*100.0)/100.0;
                    //Aggiorno il valore del totale
                    fieldTotale.setText("Totale: "+totale);
                    // Aggiorno la tabella coi nuovi dati
                    tablePanel.setData(newVoci);
                    tablePanel.aggiorna();
                }
                else{
                    JOptionPane.showMessageDialog(Panel.this, "Niente da visualizzare", 
                        "Info", JOptionPane.INFORMATION_MESSAGE);
                }

                //reset dei TextField
                fieldVisualizza.setText("");
                fieldInizio.setText("");
                fieldFine.setText("");
            }
        });

        /*
         * Gestione bottone Indietro
         */
        indietro.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //Torno alla tabella coi dati iniziali
                tablePanel.setData(postgres.getVoci());
                tablePanel.aggiorna();

                //Aggiorno il valore del totale
                fieldTotale.setText(postgres.getTotale());
            }
        });

        /*
         * Layout
         */
        GridBagConstraints gbc = new GridBagConstraints();

        //gbc etichetta Data
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 0, 0, 5);
        add(labelData, gbc);
        //gbc field Data
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(datePicker, gbc);

        //gbc etichetta Ammontare
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 0, 0, 5);
        add(labelAmmontare, gbc);
        //gbc field Ammontare
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 0, 0, 0);
        add(fieldAmmontare, gbc);

        //gbc etichetta Descrizione
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(5, 0, 0, 5);
        add(labelDescrizione, gbc);
        //gbc field Descrizione
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 0, 0, 0);
        add(fieldDescrizione, gbc);

        //gbc bottone Aggiungi
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10, 0, 0, 130);
        add(aggiungi, gbc);

        //gbc bottone Modifica
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(modifica, gbc);

        //gbc label Visualizza
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 30, 0, 0);
        add(labelVisualizza, gbc);
        //gbc ComboBox Visualizza
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(boxVisualizza, gbc);
        //gbc field Visualizza
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 110, 0, 0);
        add(fieldVisualizza, gbc);

        //gbc etichetta Inizio
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 0, 0, 0);
        add(labelInizio, gbc);
        //gbc field Inizio
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 40, 0, 0);
        add(fieldInizio, gbc);

        //gbc etichetta Fine
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(8, 90, 0, 0);
        add(labelFine, gbc);
        //gbc field Fine
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 125, 0, 0);
        add(fieldFine, gbc);

        //gbc bottone Visualizza
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        gbc.weighty = 0.3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10, 0, 0, 130);
        add(visualizza, gbc);

        //gbc bottone Indietro
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.weightx = 0.3;
        gbc.weighty = 0.3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(indietro, gbc);

        //gbc etichetta Ricerca
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(labelRicerca, gbc);
        //gbc field Ricerca
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 100);
        add(fieldRicerca, gbc);

        //gbc bottone Ricerca
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.01;
        gbc.weighty = 0.01;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        gbc.insets = new Insets(5, 150, 0, 0);
        add(ricerca, gbc);
    }
}
