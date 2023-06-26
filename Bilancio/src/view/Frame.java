package view;

import javax.swing.*;

import model.Postgres;
import view.File.CSVExporter;
import view.File.FileChooser;
import view.File.FilterCSV;
import view.File.FilterText;
import view.File.TextExporter;
import view.File.Utils;
import view.Panel.Panel;
import view.Table.TableEvent;
import view.Table.TableListener;
import view.Table.TablePanel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * Classe che si occupa di gestire il Frame
 */
public class Frame extends JFrame{

    private TablePanel tablePanel;
    private Panel Panel;

    private JTextField fieldTotale;

    private FileChooser fileChooser;
    
    private TextExporter textExporter;

    public Frame(Postgres postgres){
        /*
         * Set titolo, layout e menu
         */
        super("Conto Corrente");
        setLayout(new BorderLayout());
        setJMenuBar(creaBarraMenu());

        tablePanel = new TablePanel(postgres);

        fieldTotale = new JTextField(25);
        fieldTotale.setEditable(false);

        Panel = new Panel(tablePanel, fieldTotale, postgres);

        /*
         * prendo la lista di Voci dal Database e la passo alla Tabella
         */
        tablePanel.setData(postgres.getVoci());

        //gestione somma totale del bilancio
        fieldTotale.setText(postgres.getTotale());

        /*
         * Setto il TableListener per ascoltare gli eventi
         * del TablePanel
         */
        tablePanel.setTableListener(new TableListener() {
            @Override
            public void tableEventListener(TableEvent te){
                int rowIndexDelete = te.getRowToDelete();
                postgres.delVoce(rowIndexDelete);

                //gestione somma totale del bilancio
                fieldTotale.setText(postgres.getTotale());
            }
        });

        /*
         * Gestione componenti in pannello centrale
         */
        JPanel pc = new JPanel();
        pc.setLayout(new BorderLayout());
        pc.add(tablePanel, BorderLayout.CENTER);
        pc.add(fieldTotale, BorderLayout.SOUTH);

        /*
         * Componenti
         */
        add(pc, BorderLayout.CENTER);
        add(Panel, BorderLayout.LINE_START);

        /*
         * Impostazioni frame
         */
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    
    /** 
     * @return JMenuBar
     * 
     * MenuBar che si occupa di gestire il menu File
     */
    private JMenuBar creaBarraMenu(){
        JMenuBar barraMenu = new JMenuBar();

        /*
         * Menu File
         */
        JMenu menuFile = new JMenu("File");

        JMenuItem menuItemStampa = new JMenuItem("Stampa");
        JMenuItem menuItemEsci = new JMenuItem("Esci");

        JMenu menuEsporta = new JMenu("Esporta");
        JMenuItem esportaCSV = new JMenuItem("CSV");
        JMenuItem esportaTesto = new JMenuItem("Testo");

        menuEsporta.add(esportaTesto);
        menuEsporta.add(esportaCSV);

        menuFile.add(menuEsporta);
        menuFile.add(menuItemStampa);
        menuFile.addSeparator();
        menuFile.add(menuItemEsci);

        //Action event che mi permette di gestire l'esportazione su file di tipo Testo
        esportaTesto.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //setto il FileChooser per il tipo Testo
                fileChooser = new FileChooser(2);
                fileChooser.addChoosableFileFilter(new FilterText());
                fileChooser.setAcceptAllFileFilterUsed(false);

                if (fileChooser.showSaveDialog(Frame.this) == JFileChooser.APPROVE_OPTION){
                    try {
                        File f = fileChooser.getSelectedFile();
                        //se il file non ha estensione o é diversa da .txt, viene inserita l'estensione .txt
                        if (Utils.getExtension(f) == null || !Utils.getExtension(f).equalsIgnoreCase("txt")) {
                            f = new File(f.toString() + ".txt");
                        }

                        //Polimorfismo
                        textExporter = new TextExporter();
                        textExporter.export(tablePanel.getModel(), f);

                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(Frame.this, "Impossibile esportare i dati su file", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        //Action event che mi permette di gestire l'esportazione su file di tipo CSV
        esportaCSV.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //setto il FileChooser per il tipo CSV
                fileChooser = new FileChooser(3);
                fileChooser.addChoosableFileFilter(new FilterCSV());
                fileChooser.setAcceptAllFileFilterUsed(false);

                if (fileChooser.showSaveDialog(Frame.this) == JFileChooser.APPROVE_OPTION){
                    try {
                        File f = fileChooser.getSelectedFile();
                        //se il file non ha estensione o é diversa da .csv, viene inserita l'estensione .csv
                        if (Utils.getExtension(f) == null || !Utils.getExtension(f).equalsIgnoreCase("csv")) {
                            f = new File(f.toString() + ".csv");
                        }

                        //Polimorfismo
                        textExporter = new CSVExporter();
                        textExporter.export(tablePanel.getModel(), f);

                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(Frame.this, "Impossibile esportare i dati su file", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        //Action event che permette di aprire il popup per stampare la tabella con la stampante 
        menuItemStampa.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try{
                    tablePanel.getTable().print();;
                }catch(java.awt.print.PrinterException e1){
                    JOptionPane.showMessageDialog(Frame.this, "Ammontare non può essere 0", 
                                    "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //Action event che permette di uscire dal programma se viene premuto esci nel menu File
        menuItemEsci.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(ABORT);
            }
        });
        
        barraMenu.add(menuFile);

        return barraMenu;
    }
}
