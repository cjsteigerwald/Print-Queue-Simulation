package print;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Coarse:          CS 142 Java II
 * Class:           PrinterGUI
 * File:            PrinterGUI.java
 * Description:     PrinterGUI is the user interface for the Print Queue 
 * Simulation program.  Allows user to input parameters for simulation.
 * PrinterGUI validates all parameter's twice before sending to other classes.
 * Key pressed event is used to validate that correct parameters are initially
 * entered, then when the start button is used text fields are checked to make
 * sure that they are not void of data.  Then parameters are converted to 
 * integers before being sent to other classes.
 * 
 * GUI has three tabs to display data and statistics from simulation.  The 
 * queue tab:
 * ~ display current clients in queue
 * 
 * The server state tab dynamically displays information for each print server:
 * ~ Mean service rate
 * ~ Current service rate
 * ~ Current client ID
 * ~ Time remaining to finish job
 * ~ User can scroll over printer to display as tool tip client ID, print size
 * and time entered queue
 * 
 * The system summary tab displays:
 * ~ Average time client is in queue
 * ~ Average time client is in system
 * ~ Average service time per client
 * ~ Average jobs in queue
 * ~ Average jobs in system
 * ~ Percent of time system sat idle
 * ~ Total clients processed during simulation
 * ~ Total clients received during simulation
 * ~ Average mean rate of server
 * ~ Average service rate of server
 * ~ A text list showing clients still in queue
 * 
 * Additionally user can save simulation statistics to file of choice.
 * 
 * Created:         June 1, 2013
 * Hours:           10
 * Platform:        jdk 1.7.0_17; NetBeans IDE 7.3; Windows 7
 * Environment:     PC, Windows Vista Business, jdk 7.0, NetBeans 7.0.1
 * @author:         Chris Steigerwald
 * @author:         Tyler Kowalczik
 * @version         1.0
 * 
 * @see             java.awt.event.KeyEvent;
 * @see             javax.swing.JLabel;
 * @see             javax.swing.JTextField;
 * @see             javax.swing.JOptionPane;
 * @see             import java.util.logging.Level
 * @see             java.awt.Container;
 * @see             java.awt.Dimension;
 * @see             java.awt.Toolkit;
 * @see             java.text.DecimalFormat;
 * @see             java.util.ArrayList;
 * @see             javax.swing.DefaultListModel;
 * @see             javax.swing.JComboBox;
 * @see             logo image: http://www.clipartsfree.net/svg/laser-printer-ln_Clipart_svg_File.svg
 */
public class PrinterGUI extends javax.swing.JFrame
{
    
    /**
     * -------------------------------------------------------------------------
     * INSTANCE FIELDS
     * -------------------------------------------------------------------------
     */
    // minumum mean deviation as defined by program set to 50
    protected static final int MIN_MEAN = 1;
    // maximum mean deviation as defined by program set to 150
    protected static final int MAX_MEAN = 150;
    // minumum duration time of simulation as defined by program set to 1
    protected static final int MIN_DURATION = 1;
    // maximum duration time of simulation as defined by program set to 1000
    protected static final int MAX_DURATION = 1000;
    // minimum mean arrival time as defined by program set to 1
    protected static final int MIN_ARRIVAL = 1;
    // maximum mean arrival time as defined by program set to 30
    protected static final int MAX_ARRIVAL = 30;
    protected static final int MAX_NUM_SERVERS = 4;
    // array to hold specified interval times to display statistics for user to 
    // choose from in the intervalJComboBox on the GUI
    protected static final int[] SIZE_INTERVAL_ARRAY = {1, 5, 10};
    // array to hold specified number of printer for numPrintersJComboBox   
    protected static final int[] SIZE_PRINTER_ARRAY = {1,2,3,4};
    protected static JLabel[] labels;
    // String Buffer
    StringBuffer display = new StringBuffer("");
    //
    private DefaultListModel myModel;
    //
    static DecimalFormat decFormat = new DecimalFormat("###.##");
    //Referemce to current instantiation of SimulationDriver
    private SimulationDriver myDriver;
    
    private String filePath;
    
    private StatsSummary mySummary;
    
    
    

    /**
     * Creates new form PrinterGUI
     */
    public PrinterGUI()
    {
        initComponents();
        // class startUp method
        startUp();
        this.myModel = new DefaultListModel();
        this.queueJList.setModel(this.myModel);
        this.dupQueueJList.setModel(myModel);
        this.filePath = "";
        this.myJFileChooser.setCurrentDirectory(null);
    }

     /**
     * <pre>
     * startUp     
     * This method is called at start of GUI by PugetSoundEnergyGUI().
     * Sets background color
     * Sets the displayBillJButton as default
     * Sets title for application
     * Sets image icon for main GUI
     * Sets the Date used in Arrival Time
     * Loads last parcel saved and displays it in text fields
     * @see java.awt.Toolkit;
     * @see java.awt.event.ActionEvent;
     * @return void
     * @author Chris Steigerwald
     * </pre>
     */
    private void startUp()
    {
        // Center form at start.
        setLocationRelativeTo(null);
        // Sets title for application
        this.setTitle("Printer Queue Simulation");
        // Sets the icon image for main GUI
        this.setIconImage(Toolkit.getDefaultToolkit().getImage("src/Resources/printerLogo.png")); 
        // Set addJButton as default
        this.getRootPane().setDefaultButton(startJButton);
        
        //Sets background color
        Container background = this.getContentPane();
        background.setBackground(new java.awt.Color(0,153,255));  
        // Adds items from the SIZE_INTERVAL_ARRAY array to intervalJComboBox
        setComboBox(SIZE_INTERVAL_ARRAY, refreshJComboBox);
        // Adds items from the SIZE_PRINTER_ARRAY array to numPrintersJComboBox
        setComboBox(SIZE_PRINTER_ARRAY, numPrintersJComboBox);
         // hides animatedJLabel 
        animated1JLabel.setVisible(false);
        animated2JLabel.setVisible(false);
        animated3JLabel.setVisible(false);
        animated4JLabel.setVisible(false);  
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    
    /**
     * <pre>
     * Method:          setComboBox
     * Description:     Reads the contents from the SIZE_INTERVAL_ARRAY and 
     * adds the items to the intervalJComboBox at startup, and adds contents
     * from SIZE_PRINTER_ARRAY and add items to numPrinterJComboBox
     * precondition:    SIZE_INTERVAL_ARRAY and SIZE_PRINTER_ARRAY has been 
     * instantiated
     * postcondition:   intervalJComboBox and numPrinterJComboBox has items from 
     * array in it
     * @param:          void
     * @param:          void
     * @author:         Chris Steigerwald
     * @author:         Tyler Kowalczik
     * @see:            java.awt.event.KeyEvent;   
     * @date:		6/01/2013
     *<pre>
     */
    private void setComboBox(int[] name, JComboBox comboBox)
    {
        for(int i : name)
            comboBox.addItem(i);       
    }
    
    /**
     * <pre>
     * Method:          setLabelVisible
     * Description:     Method that sets animated printer visible during 
     * simulation by taking the selected index from printersJComboBox and 
     * looping through an array of labels and setting their .setVisible 
     * properties to true or false.
     * precondition:    SIZE_INTERVAL_ARRAY and SIZE_PRINTER_ARRAY has been 
     * instantiated
     * postcondition:   intervalJComboBox and numPrinterJComboBox has items from 
     * array in it
     * @param:          name    the name of the Array being passed in to populate
     * comboBox
     * @param:          comboBox    the name of comboBox to populate
     * @author:         Chris Steigerwald
     * @author:         Tyler Kowalczik
     * @see:            java.awt.event.KeyEvent;   
     * @date:		6/01/2013
     *<pre>
     */
    private void setLabelVisible(int numPrinters, boolean value)
    {
        // An array of JLabels with animated printers that can be set to visible
        JLabel[] labels = {animated1JLabel, animated2JLabel, animated3JLabel, animated4JLabel};
        // Sets JLabels to visible when booelan true passed in or set to 
        // not visible when boolean false passed in
        for (int i = 0; i < numPrinters; i++)
        {
            labels[i].setVisible(value);
        } // end for
    }
    
    /**
    *<pre>
    * Method:		statsButtonEvent()
    * Description:	Simple method for changing the statsJTabbedPane tabs
    * win the button "Statistics" is clicked by user.  It collects the current
    * index of statsJTabbedPane and uses a switch statement to display the 
    * next tab.
    * precondition:     on startup default button, and file menu item to display
    * "statistics" and JTextArea to show queue.
    * postcondition:    changes default text to "queue" and JTextArea displays
    * simulations statistics.
    * @return:          void
    * @param:		String cmd
    * @author:          Chris Steigerwald
    * @author:          Tyler Kowalczik
    * @see:             java.awt.event.KeyEvent;   
    * @date:		6/01/2013
    *<pre>
    **/
    private void statsButtonEvent()
    {
        // variable for holding the current selected index of comboBox
        int counter = statsJTabbedPanel.getSelectedIndex();
        
        switch (counter)
        {
            case 0:
                statsJTabbedPanel.setSelectedIndex(1);
                counter = 1;
                statsJButton.setToolTipText("Show client statistics");
                break;
            case 1:
                statsJTabbedPanel.setSelectedIndex(2);
                statsJButton.setToolTipText("Show final statistics");
                counter = 2;
                break;
            case 2:
                statsJTabbedPanel.setSelectedIndex(0);
                counter = 3;
                statsJButton.setToolTipText("Show printer queue");
                break;            
        } 
    }

    /**
    *<pre>
    * Method:		inputError()
    * Description:	Creates an input error message for GUI displaying a 
    * message advising the user that input is outside set parameters.  Method 
    * requires JTextFiled of input error, min value, max value, and name of
    * text field error to be passed in.
    * precondition:     user input error as defined by validation class
    * postcondition:    display error message and request focus to offending field
    * typed key is digit, backspace, or delete key press
    * @return:          void
    * @param:		JTextField: errorJField, int min, int max, String name
    * @author:          Chris Steigerwald
    * @author:          Tyler Kowalczik
    * @see:             javax.swing.JOptionPane;   
    * @date:		6/01/2013
    *<pre>
    **/
    private void inputError(JTextField errorJField, int min, int max, String error)
    {        
        errorJField.requestFocus();
        errorJField.setText("" + min);
        errorJField.selectAll();
        JOptionPane.showMessageDialog(null, error + " size must be between " + min + "- " + max +
                "\n Field set to " + min,  "Input Error!",
                  JOptionPane.WARNING_MESSAGE);
    }
    
    /**
    *<pre>
    * Method:		formatError()
    * Description:	Creates an input error message for GUI displaying a 
    * message advising the user that input is outside set parameters.  Method 
    * requires JTextFiled of input error, min value, max value, and name of
    * text field error to be passed in.
    * precondition:     user input error as defined by validation class
    * postcondition:    display error message and request focus to offending field
    * typed key is digit, backspace, or delete key press
    * @return:          void
    * @param:		JTextField: errorJField, int min, int max, String name
    * @author:          Chris Steigerwald
    * @author:          Tyler Kowalczik
    * @see:             javax.swing.JOptionPane;
    * @date:		6/01/2013
    *<pre>
    **/
    private void formatError(JTextField errorJField, String error, int min)
    {
        JOptionPane.showMessageDialog(null,"Illegal input in:  " + error + 
                " field.\nField set to " + min,"Format Error",
                JOptionPane.ERROR_MESSAGE); 
        errorJField.requestFocusInWindow();
        errorJField.setText("" + min);
        errorJField.selectAll();
    }
    
   
    /**
    *<pre>
    * Method:		enableButtons()
    * Description:	enableButtons enables and disables button before the
    * simulation and after simulation is run.
    * precondition:     Only start and exit buttons are enabled
    * postcondition:    enables and disables corresponding buttons
    * simulations statistics.
    * @return:          void
    * @param:		boolean value
    * @author:          Chris Steigerwald
    * @author:          Tyler Kowalczik
    * @see:             java.awt.event.KeyEvent;   
    * @date:		6/01/2013
    *<pre>
    **/
    private void enableButtons(boolean value)
    {
        statsJButton.setEnabled(value);
        statsJMenuItem.setEnabled(value);
        printtJButton.setEnabled(value);
        printJMenuItem.setEnabled(value);
        clearJjButton.setEnabled(value);
        clearJMenuItem.setEnabled(value);  
        numPrintersJComboBox.setEnabled(value);
        refreshJComboBox.setEnabled(value);
        startJMenuItem.setEnabled(value);
    }
    
     /**
    *<pre>
    * Method:		startDisable()
    * Description:	Method disables buttons, menu items, and text fields
    * during simulation to prevent user from using.
    * precondition:     Buttons are disabled
    * postcondition:    enables and disables corresponding buttons
    * simulations statistics.
    * @return:          void
    * @param:		boolean value
    * @author:          Chris Steigerwald
    * @author:          Tyler Kowalczik
    * @see:             java.awt.event.KeyEvent;   
    * @date:		6/01/2013
    *<pre>
    **/
    private void startDisable(boolean value)
    {        
        startJButton.setEnabled(!value);
        startJMenuItem.setEnabled(!value);
        numPrintersJComboBox.setEnabled(!value);
        meanJobJTextField.setEditable(!value);
        durationJTextField.setEditable(!value);
        arrivalJTextField.setEditable(!value);
        refreshJComboBox.setEnabled(!value);
        saveJMenuItem.setEnabled(!value);
    }
    
    protected StringBuffer queueBuffer(String queue)
    {
        display.append(queue + " \n");
        return display;
    }
 
    /**
     * <pre>
     * Method:          clearComponents
     * 
     * Descritption:    This method is called by various methods to clear 
     * all text fields in the GUI by setting text to null
     * precondition:    components have text
     * postcondition:   text fields in GUI will be cleared
     * @return void
     * @author:         Chris Steigerwald
     * @author:         Tyler Kowalczik
     * @see:            java.awt.event.KeyEvent;   
     * @date:		6/01/2013
     * </pre>
     */
    private void clearComponents()
    {
       numPrintersJComboBox.setSelectedIndex(1);
       meanJobJTextField.setText("");
       durationJTextField.setText("");
       arrivalJTextField.setText("");
       meanServiceRate1JTextField.setText("");
       currentServiceRate1JTextField.setText("");;
       currentClient1JTextField.setText("");
       timeLeft1JTextField.setText("");
       meanServiceRate2JTextField.setText("");
       currentServiceRate2JTextField.setText("");;
       currentClient2JTextField.setText("");
       timeLeft2JTextField.setText("");
       meanServiceRate3JTextField.setText("");
       currentServiceRate3JTextField.setText("");;
       currentClient3JTextField.setText("");
       timeLeft3JTextField.setText("");
       meanServiceRate4JTextField.setText("");
       currentServiceRate4JTextField.setText("");;
       currentClient4JTextField.setText("");
       timeLeft4JTextField.setText("");
       avgTimeQueueJTextField.setText("");
       avgTimeSystemJTextField.setText("");
       avgServiceTimeJTextField.setText("");
       avgJobsInQueueJTextField.setText("");
       avgJobsInQueueJTextField.setText("");
       avgPercentIdleJTextField.setText("");
       totalClientsReceivedJTextField.setText("");
       avgMeanRateJTextField.setText("");
       avgServiceRateJTextField.setText("");
       myModel.removeAllElements();
       totalClientsProcessedJTextField.setText("");
       avgJobsInSystemJTextField.setText("");
    }
    
    private void clearLastSimulation()
    {
       meanServiceRate1JTextField.setText("");
       currentServiceRate1JTextField.setText("");;
       currentClient1JTextField.setText("");
       timeLeft1JTextField.setText("");
       meanServiceRate2JTextField.setText("");
       currentServiceRate2JTextField.setText("");;
       currentClient2JTextField.setText("");
       timeLeft2JTextField.setText("");
       meanServiceRate3JTextField.setText("");
       currentServiceRate3JTextField.setText("");;
       currentClient3JTextField.setText("");
       timeLeft3JTextField.setText("");
       meanServiceRate4JTextField.setText("");
       currentServiceRate4JTextField.setText("");;
       currentClient4JTextField.setText("");
       timeLeft4JTextField.setText("");
       avgTimeQueueJTextField.setText("");
       avgTimeSystemJTextField.setText("");
       avgServiceTimeJTextField.setText("");
       avgJobsInQueueJTextField.setText("");
       avgPercentIdleJTextField.setText("");
       totalClientsReceivedJTextField.setText("");
       avgMeanRateJTextField.setText("");
       avgServiceRateJTextField.setText("");
       myModel.removeAllElements();
       totalClientsProcessedJTextField.setText("");
       avgJobsInSystemJTextField.setText("");  
       
       setLabelVisible(MAX_NUM_SERVERS, false);
    }
    
    /**
     * <pre>
     * Method:          validateInput
     * Description:     Method is called when "Start" button is pressed on the
     * GUI, it will validate user input text fields.  First validation is to
     * check if user has filled out textField, if not a call to formatError()
     * is called that will display JOptionPane error message alerting user to 
     * fill field, and field will be set to minimum.  Second validation is to 
     * check that the data entered is within specified paramters, and will call
     * Validation.checkSize() method in validation class.  If data is outside
     * parameters user will get a warning JOptionPane alerting user that data
     * is outside parameters.
     * While data has not been entered, or is invalid method will return false
     * boolean value to "Start" button, when all input is within parameters
     * a boolean value of true will be passed to "Start" button allowing the
     * simulation to begin.
     * precondition:    user has input data into text fields.
     * postcondition:   data is within parameters, boolean true value sent to
     * "Start" button and simulation will commence
     * @param string:       the name of text field being validated
     * @param textField:    variable name of JTextField  
     * @param MIN           minimum value for associated JTextField
     * @param MAX           maximum value for associated JTextField
     * @return boolean 
     * @see:                inputError() 
     * @see:                formatError()
     * @author:             Chris Steigerwald
     * @author:             Tyler Kowalczik
     * @see:                java.awt.event.KeyEvent;   
     * @date:               6/01/2013
     * </pre>
     */
    private boolean validateInput(String string, JTextField textField, int MIN, int MAX)
    {
        // boolean variable to return if user input is valid or not valid
        boolean isValid = true;
        // String variable to hold text area name
        String error = string;
        // String variable to hold JTextField variable name
        String fieldName = textField.getText();
        
        /**
         * if statement validates that text field is not empty, so a NumFormat
         * exception is not created.  If text field is empty a call to the 
         * formatError() method is made alerting user that field is empty and a
         * a boolean value of false is sent to the "Start" button.
         * If textField is not empty data is validated by the Validation.
         * checkSize() method in the Validation class, if data is outside 
         * parameters a boolean false is sent to "Start" button.  If data is 
         * with in parameters, and boolean true is sent to "Start" button.
         */
        if (fieldName.equals(""))
        {
            formatError(textField, error, MIN);
            isValid = false;            
        } // end if
        else if (!Validation.checkSize(Integer.parseInt(textField.getText()), MIN, MAX))
        {
            inputError(textField, MIN, MAX, error); 
            isValid = false;
        }  // end else if

        return isValid;   
    }
    
    private void writeToFile(String filePath) throws IOException
    {
        SaveSimulation saveSim = new SaveSimulation(filePath);
        saveSim.saveSimulation(this.mySummary);
    }
 
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        myJFileChooser = new javax.swing.JFileChooser();
        titleJPanel = new javax.swing.JPanel();
        titleJLabel = new javax.swing.JLabel();
        logoJLabel = new javax.swing.JLabel();
        inputJPanel = new javax.swing.JPanel();
        numPrintersJLabel = new javax.swing.JLabel();
        numPrintersJComboBox = new javax.swing.JComboBox();
        meanJobJLabel = new javax.swing.JLabel();
        meanJobJTextField = new javax.swing.JTextField();
        durationJLabel = new javax.swing.JLabel();
        durationJTextField = new javax.swing.JTextField();
        arrivalJLabel = new javax.swing.JLabel();
        arrivalJTextField = new javax.swing.JTextField();
        intervalJLabel = new javax.swing.JLabel();
        refreshJComboBox = new javax.swing.JComboBox();
        buttonJPanel = new javax.swing.JPanel();
        statsJButton = new javax.swing.JButton();
        clearJjButton = new javax.swing.JButton();
        printtJButton = new javax.swing.JButton();
        exitJButton = new javax.swing.JButton();
        startJButton = new javax.swing.JButton();
        statsJTabbedPanel = new javax.swing.JTabbedPane();
        queueJPanel = new javax.swing.JPanel();
        queueJScrollPane = new javax.swing.JScrollPane();
        queueJList = new javax.swing.JList();
        printStatsJPanel = new javax.swing.JPanel();
        printer1JPanel = new javax.swing.JPanel();
        JLabel2 = new javax.swing.JLabel();
        meanServiceRate1JTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        currentServiceRate1JTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        currentClient1JTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        timeLeft1JTextField = new javax.swing.JTextField();
        animated1JLabel = new javax.swing.JLabel();
        printer2JPanel = new javax.swing.JPanel();
        JLabel6 = new javax.swing.JLabel();
        meanServiceRate2JTextField = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        currentServiceRate2JTextField = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        currentClient2JTextField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        timeLeft2JTextField = new javax.swing.JTextField();
        animated2JLabel = new javax.swing.JLabel();
        printer3JPanel = new javax.swing.JPanel();
        JLabel3 = new javax.swing.JLabel();
        meanServiceRate3JTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        currentServiceRate3JTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        currentClient3JTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        timeLeft3JTextField = new javax.swing.JTextField();
        animated3JLabel = new javax.swing.JLabel();
        printer4JPanel = new javax.swing.JPanel();
        JLabel4 = new javax.swing.JLabel();
        meanServiceRate4JTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        currentServiceRate4JTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        currentClient4JTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        timeLeft4JTextField = new javax.swing.JTextField();
        animated4JLabel = new javax.swing.JLabel();
        summaryJPanel = new javax.swing.JPanel();
        clientJPanel = new javax.swing.JPanel();
        JLabel5 = new javax.swing.JLabel();
        avgTimeQueueJTextField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        avgTimeSystemJTextField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        avgServiceTimeJTextField = new javax.swing.JTextField();
        systemJPanel = new javax.swing.JPanel();
        JLabel7 = new javax.swing.JLabel();
        avgJobsInQueueJTextField = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        avgJobsInSystemJTextField = new javax.swing.JTextField();
        avgPercentIdleJTextField = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        totalClientsProcessedJTextField = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        totalClientsReceivedJTextField = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        serverStatsJPanel = new javax.swing.JPanel();
        JLabel8 = new javax.swing.JLabel();
        avgMeanRateJTextField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        avgServiceRateJTextField = new javax.swing.JTextField();
        dupQueueJPanel = new javax.swing.JPanel();
        dupQueueJScrollPane = new javax.swing.JScrollPane();
        dupQueueJList = new javax.swing.JList();
        jMenuBar1 = new javax.swing.JMenuBar();
        fileJMenu = new javax.swing.JMenu();
        printJMenuItem = new javax.swing.JMenuItem();
        saveJMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        exitJMenuItem = new javax.swing.JMenuItem();
        actionJMenu = new javax.swing.JMenu();
        startJMenuItem = new javax.swing.JMenuItem();
        clearJMenuItem = new javax.swing.JMenuItem();
        statsJMenuItem = new javax.swing.JMenuItem();
        helpJMenu = new javax.swing.JMenu();
        aboutJMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 153, 255));
        setResizable(false);

        titleJPanel.setBackground(new java.awt.Color(0, 153, 255));

        titleJLabel.setFont(new java.awt.Font("Comic Sans MS", 0, 36)); // NOI18N
        titleJLabel.setText("Print Queue Simulation");

        logoJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/printerLogo.png"))); // NOI18N

        inputJPanel.setBackground(new java.awt.Color(0, 153, 255));

        numPrintersJLabel.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        numPrintersJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        numPrintersJLabel.setText("Number of printers:");

        numPrintersJComboBox.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        numPrintersJComboBox.setToolTipText("Select number of printers");

        meanJobJLabel.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        meanJobJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        meanJobJLabel.setText("Mean job size:");
        meanJobJLabel.setToolTipText("");

        meanJobJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        meanJobJTextField.setToolTipText("Choose interger between 1 and 150");
        meanJobJTextField.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                meanJobJTextFieldKeyTyped(evt);
            }
        });

        durationJLabel.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        durationJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        durationJLabel.setText("Duraton of simulation:");
        durationJLabel.setToolTipText("");

        durationJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        durationJTextField.setToolTipText("Choose duration between 1 and 1000");
        durationJTextField.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                durationJTextFieldKeyTyped(evt);
            }
        });

        arrivalJLabel.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        arrivalJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        arrivalJLabel.setText("Mean arrival time:");

        arrivalJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        arrivalJTextField.setToolTipText("Choose mean arrival time between 1 - 30.");
        arrivalJTextField.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyTyped(java.awt.event.KeyEvent evt)
            {
                arrivalJTextFieldKeyTyped(evt);
            }
        });

        intervalJLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        intervalJLabel.setText("Set refresh rate:");
        intervalJLabel.setToolTipText("Set statistical interval time");

        refreshJComboBox.setToolTipText("A smaller refresh rate yields more precise statistics.");

        javax.swing.GroupLayout inputJPanelLayout = new javax.swing.GroupLayout(inputJPanel);
        inputJPanel.setLayout(inputJPanelLayout);
        inputJPanelLayout.setHorizontalGroup(
            inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputJPanelLayout.createSequentialGroup()
                .addGroup(inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(inputJPanelLayout.createSequentialGroup()
                        .addGroup(inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(durationJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(arrivalJLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(inputJPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(durationJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(inputJPanelLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(arrivalJTextField))))
                    .addGroup(inputJPanelLayout.createSequentialGroup()
                        .addComponent(numPrintersJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(numPrintersJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(inputJPanelLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(meanJobJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(meanJobJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(inputJPanelLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(intervalJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(refreshJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        inputJPanelLayout.setVerticalGroup(
            inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputJPanelLayout.createSequentialGroup()
                .addGroup(inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numPrintersJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numPrintersJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(meanJobJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(meanJobJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(durationJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(durationJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(arrivalJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(arrivalJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(inputJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(intervalJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshJComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonJPanel.setBackground(new java.awt.Color(0, 153, 255));
        buttonJPanel.setEnabled(false);
        buttonJPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        statsJButton.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        statsJButton.setMnemonic('t');
        statsJButton.setText("Statistics");
        statsJButton.setToolTipText("Press to move through tab panel");
        statsJButton.setEnabled(false);
        statsJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                statsJButtonActionPerformed(evt);
            }
        });
        buttonJPanel.add(statsJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 29));

        clearJjButton.setMnemonic('c');
        clearJjButton.setText("Clear");
        clearJjButton.setToolTipText("Clear text field");
        clearJjButton.setEnabled(false);
        clearJjButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                clearJjButtonActionPerformed(evt);
            }
        });
        buttonJPanel.add(clearJjButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 0, 83, 29));

        printtJButton.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        printtJButton.setMnemonic('p');
        printtJButton.setText("Print");
        printtJButton.setToolTipText("Print results");
        printtJButton.setEnabled(false);
        printtJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                printtJButtonActionPerformed(evt);
            }
        });
        buttonJPanel.add(printtJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 83, 29));

        exitJButton.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        exitJButton.setMnemonic('x');
        exitJButton.setText("Exit");
        exitJButton.setToolTipText("Close program");
        exitJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exitJButtonActionPerformed(evt);
            }
        });
        buttonJPanel.add(exitJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 30, 83, 29));

        startJButton.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        startJButton.setMnemonic('s');
        startJButton.setText("Start");
        startJButton.setToolTipText("Start simulation");
        startJButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                startJButtonActionPerformed(evt);
            }
        });
        buttonJPanel.add(startJButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 120, 30));

        javax.swing.GroupLayout titleJPanelLayout = new javax.swing.GroupLayout(titleJPanel);
        titleJPanel.setLayout(titleJPanelLayout);
        titleJPanelLayout.setHorizontalGroup(
            titleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleJPanelLayout.createSequentialGroup()
                .addGroup(titleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(titleJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(inputJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(titleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(logoJLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(titleJPanelLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(titleJLabel)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        titleJPanelLayout.setVerticalGroup(
            titleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titleJPanelLayout.createSequentialGroup()
                .addComponent(titleJLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(titleJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(inputJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(titleJPanelLayout.createSequentialGroup()
                        .addComponent(logoJLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        statsJTabbedPanel.setBackground(new java.awt.Color(0, 153, 255));
        statsJTabbedPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        queueJPanel.setBackground(new java.awt.Color(0, 153, 255));
        queueJPanel.setToolTipText("Displays print queue and statistics");

        queueJList.setToolTipText("Display jobs in queue");
        queueJScrollPane.setViewportView(queueJList);

        javax.swing.GroupLayout queueJPanelLayout = new javax.swing.GroupLayout(queueJPanel);
        queueJPanel.setLayout(queueJPanelLayout);
        queueJPanelLayout.setHorizontalGroup(
            queueJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(queueJScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
        );
        queueJPanelLayout.setVerticalGroup(
            queueJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(queueJPanelLayout.createSequentialGroup()
                .addComponent(queueJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 33, Short.MAX_VALUE))
        );

        statsJTabbedPanel.addTab("Queue", null, queueJPanel, "Displays print queue");

        printStatsJPanel.setBackground(new java.awt.Color(0, 153, 255));

        printer1JPanel.setBackground(new java.awt.Color(0, 153, 255));
        printer1JPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Printer 1"));

        JLabel2.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        JLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JLabel2.setText("Mean service Rate:");

        meanServiceRate1JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        meanServiceRate1JTextField.setToolTipText("Mean service rate");
        meanServiceRate1JTextField.setFocusable(false);

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Current Service Rate:");

        currentServiceRate1JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        currentServiceRate1JTextField.setToolTipText("Current service rate of printer");
        currentServiceRate1JTextField.setFocusable(false);

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Current Client:");

        currentClient1JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        currentClient1JTextField.setToolTipText("Current print job");
        currentClient1JTextField.setFocusable(false);

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Time Left:");

        timeLeft1JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        timeLeft1JTextField.setToolTipText("Time to finish current print job");
        timeLeft1JTextField.setFocusable(false);

        animated1JLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Small.gif"))); // NOI18N
        animated1JLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                animated1JLabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout printer1JPanelLayout = new javax.swing.GroupLayout(printer1JPanel);
        printer1JPanel.setLayout(printer1JPanelLayout);
        printer1JPanelLayout.setHorizontalGroup(
            printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printer1JPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(printer1JPanelLayout.createSequentialGroup()
                        .addComponent(JLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(meanServiceRate1JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(printer1JPanelLayout.createSequentialGroup()
                        .addGroup(printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(printer1JPanelLayout.createSequentialGroup()
                                .addComponent(animated1JLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(currentServiceRate1JTextField)
                            .addComponent(timeLeft1JTextField)
                            .addComponent(currentClient1JTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        printer1JPanelLayout.setVerticalGroup(
            printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printer1JPanelLayout.createSequentialGroup()
                .addGroup(printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(meanServiceRate1JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentServiceRate1JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currentClient1JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(printer1JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(timeLeft1JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(animated1JLabel))
                .addContainerGap())
        );

        printer2JPanel.setBackground(new java.awt.Color(0, 153, 255));
        printer2JPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Printer 2"));

        JLabel6.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        JLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JLabel6.setText("Mean service Rate:");

        meanServiceRate2JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        meanServiceRate2JTextField.setToolTipText("Mean service rate");
        meanServiceRate2JTextField.setFocusable(false);

        jLabel18.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Current Service Rate:");

        currentServiceRate2JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        currentServiceRate2JTextField.setToolTipText("Current service rate of printer");
        currentServiceRate2JTextField.setFocusable(false);

        jLabel19.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Current Client:");

        currentClient2JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        currentClient2JTextField.setToolTipText("Current print job");
        currentClient2JTextField.setFocusable(false);

        jLabel20.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Time Left:");

        timeLeft2JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        timeLeft2JTextField.setToolTipText("Time to finish current print job");
        timeLeft2JTextField.setFocusable(false);

        animated2JLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Small.gif"))); // NOI18N
        animated2JLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                animated2JLabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout printer2JPanelLayout = new javax.swing.GroupLayout(printer2JPanel);
        printer2JPanel.setLayout(printer2JPanelLayout);
        printer2JPanelLayout.setHorizontalGroup(
            printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printer2JPanelLayout.createSequentialGroup()
                .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(printer2JPanelLayout.createSequentialGroup()
                        .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printer2JPanelLayout.createSequentialGroup()
                                .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(printer2JPanelLayout.createSequentialGroup()
                                            .addGap(68, 68, 68)
                                            .addComponent(jLabel20))
                                        .addComponent(jLabel19, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(printer2JPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(jLabel18)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(printer2JPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(JLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)))
                        .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(meanServiceRate2JTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(timeLeft2JTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currentServiceRate2JTextField, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currentClient2JTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)))
                    .addComponent(animated2JLabel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        printer2JPanelLayout.setVerticalGroup(
            printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printer2JPanelLayout.createSequentialGroup()
                .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(meanServiceRate2JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentServiceRate2JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentClient2JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(printer2JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(timeLeft2JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(animated2JLabel))
                .addContainerGap())
        );

        printer3JPanel.setBackground(new java.awt.Color(0, 153, 255));
        printer3JPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Printer 3"));

        JLabel3.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        JLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JLabel3.setText("Mean service Rate:");

        meanServiceRate3JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        meanServiceRate3JTextField.setToolTipText("Mean service rate");
        meanServiceRate3JTextField.setFocusable(false);

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Current Service Rate:");

        currentServiceRate3JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        currentServiceRate3JTextField.setToolTipText("Current service rate of printer");
        currentServiceRate3JTextField.setFocusable(false);

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Current Client:");

        currentClient3JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        currentClient3JTextField.setToolTipText("Current print job");
        currentClient3JTextField.setFocusable(false);

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Time Left:");

        timeLeft3JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        timeLeft3JTextField.setToolTipText("Time to finish current print job");
        timeLeft3JTextField.setFocusable(false);

        animated3JLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Small.gif"))); // NOI18N
        animated3JLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                animated3JLabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout printer3JPanelLayout = new javax.swing.GroupLayout(printer3JPanel);
        printer3JPanel.setLayout(printer3JPanelLayout);
        printer3JPanelLayout.setHorizontalGroup(
            printer3JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printer3JPanelLayout.createSequentialGroup()
                .addGroup(printer3JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(JLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(printer3JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(printer3JPanelLayout.createSequentialGroup()
                            .addComponent(animated3JLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabel9))
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(printer3JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(timeLeft3JTextField)
                    .addComponent(currentServiceRate3JTextField)
                    .addComponent(currentClient3JTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                    .addComponent(meanServiceRate3JTextField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        printer3JPanelLayout.setVerticalGroup(
            printer3JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printer3JPanelLayout.createSequentialGroup()
                .addGroup(printer3JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(meanServiceRate3JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer3JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentServiceRate3JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer3JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currentClient3JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer3JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(timeLeft3JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(animated3JLabel)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        printer4JPanel.setBackground(new java.awt.Color(0, 153, 255));
        printer4JPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Printer 4"));

        JLabel4.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        JLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JLabel4.setText("Mean service Rate:");

        meanServiceRate4JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        meanServiceRate4JTextField.setToolTipText("Mean service rate");
        meanServiceRate4JTextField.setFocusable(false);

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Current Service Rate:");

        currentServiceRate4JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        currentServiceRate4JTextField.setToolTipText("Current service rate of printer");
        currentServiceRate4JTextField.setFocusable(false);

        jLabel11.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Current Client:");

        currentClient4JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        currentClient4JTextField.setToolTipText("Current print job");
        currentClient4JTextField.setFocusable(false);

        jLabel12.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Time Left:");

        timeLeft4JTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        timeLeft4JTextField.setToolTipText("Time to finish current print job");
        timeLeft4JTextField.setFocusable(false);

        animated4JLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Resources/Small.gif"))); // NOI18N
        animated4JLabel.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                animated4JLabelMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout printer4JPanelLayout = new javax.swing.GroupLayout(printer4JPanel);
        printer4JPanel.setLayout(printer4JPanelLayout);
        printer4JPanelLayout.setHorizontalGroup(
            printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printer4JPanelLayout.createSequentialGroup()
                .addGroup(printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(printer4JPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(JLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(meanServiceRate4JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(printer4JPanelLayout.createSequentialGroup()
                        .addGroup(printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(printer4JPanelLayout.createSequentialGroup()
                                .addComponent(animated4JLabel)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12))
                            .addGroup(printer4JPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(timeLeft4JTextField)
                            .addComponent(currentServiceRate4JTextField)
                            .addComponent(currentClient4JTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        printer4JPanelLayout.setVerticalGroup(
            printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printer4JPanelLayout.createSequentialGroup()
                .addGroup(printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(meanServiceRate4JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentServiceRate4JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentClient4JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(printer4JPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(printer4JPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(timeLeft4JTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, printer4JPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(animated4JLabel)
                        .addContainerGap())))
        );

        javax.swing.GroupLayout printStatsJPanelLayout = new javax.swing.GroupLayout(printStatsJPanel);
        printStatsJPanel.setLayout(printStatsJPanelLayout);
        printStatsJPanelLayout.setHorizontalGroup(
            printStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printStatsJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(printStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, printStatsJPanelLayout.createSequentialGroup()
                        .addComponent(printer1JPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printer2JPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, printStatsJPanelLayout.createSequentialGroup()
                        .addComponent(printer3JPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(printer4JPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        printStatsJPanelLayout.setVerticalGroup(
            printStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(printStatsJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(printStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printer1JPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(printer2JPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(printStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(printer3JPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(printer4JPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statsJTabbedPanel.addTab("Server States", null, printStatsJPanel, "Display printer statistics");

        summaryJPanel.setBackground(new java.awt.Color(0, 153, 255));

        clientJPanel.setBackground(new java.awt.Color(0, 153, 255));
        clientJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Client Statistics"));
        clientJPanel.setToolTipText("Only clients that have been processed or are currently being processed are used for calculations.");

        JLabel5.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        JLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JLabel5.setText("Avg time in queue:");

        avgTimeQueueJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        avgTimeQueueJTextField.setToolTipText("Average job time in queue");
        avgTimeQueueJTextField.setFocusable(false);

        jLabel15.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Average time in system:");

        avgTimeSystemJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        avgTimeSystemJTextField.setToolTipText("Average job time in queue");
        avgTimeSystemJTextField.setFocusable(false);

        jLabel16.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Average service time:");

        avgServiceTimeJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        avgServiceTimeJTextField.setToolTipText("Average time to service job");
        avgServiceTimeJTextField.setFocusable(false);

        javax.swing.GroupLayout clientJPanelLayout = new javax.swing.GroupLayout(clientJPanel);
        clientJPanel.setLayout(clientJPanelLayout);
        clientJPanelLayout.setHorizontalGroup(
            clientJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientJPanelLayout.createSequentialGroup()
                .addGroup(clientJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(clientJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel15)
                        .addComponent(jLabel16))
                    .addComponent(JLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(clientJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(avgTimeSystemJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addComponent(avgServiceTimeJTextField)
                    .addComponent(avgTimeQueueJTextField))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        clientJPanelLayout.setVerticalGroup(
            clientJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(clientJPanelLayout.createSequentialGroup()
                .addGroup(clientJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avgTimeQueueJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clientJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avgTimeSystemJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(clientJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avgServiceTimeJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        systemJPanel.setBackground(new java.awt.Color(0, 153, 255));
        systemJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "System Statistics"));

        JLabel7.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        JLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JLabel7.setText("Avg jobs in queue:");

        avgJobsInQueueJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        avgJobsInQueueJTextField.setToolTipText("Average jobs in queue");
        avgJobsInQueueJTextField.setFocusable(false);

        jLabel21.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Avg jobs in system:");

        avgJobsInSystemJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        avgJobsInSystemJTextField.setToolTipText("Average jobs in system");
        avgJobsInSystemJTextField.setFocusable(false);

        avgPercentIdleJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        avgPercentIdleJTextField.setToolTipText("Percent time printers are idle");
        avgPercentIdleJTextField.setFocusable(false);

        jLabel24.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("% Of time System Idle:");

        totalClientsProcessedJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        totalClientsProcessedJTextField.setToolTipText("Total jobs processed");
        totalClientsProcessedJTextField.setFocusable(false);

        jLabel25.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Total Clients Processed:");

        totalClientsReceivedJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        totalClientsReceivedJTextField.setToolTipText("Total jobs received");
        totalClientsReceivedJTextField.setFocusable(false);

        jLabel26.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Total Clients Received:");

        javax.swing.GroupLayout systemJPanelLayout = new javax.swing.GroupLayout(systemJPanel);
        systemJPanel.setLayout(systemJPanelLayout);
        systemJPanelLayout.setHorizontalGroup(
            systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemJPanelLayout.createSequentialGroup()
                .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(systemJPanelLayout.createSequentialGroup()
                        .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(systemJPanelLayout.createSequentialGroup()
                                    .addGap(36, 36, 36)
                                    .addComponent(JLabel7))
                                .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addGroup(systemJPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(avgJobsInSystemJTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(avgPercentIdleJTextField)
                            .addComponent(avgJobsInQueueJTextField, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(systemJPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(systemJPanelLayout.createSequentialGroup()
                                .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalClientsProcessedJTextField))
                            .addGroup(systemJPanelLayout.createSequentialGroup()
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(totalClientsReceivedJTextField)))))
                .addContainerGap())
        );
        systemJPanelLayout.setVerticalGroup(
            systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemJPanelLayout.createSequentialGroup()
                .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avgJobsInQueueJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avgJobsInSystemJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(avgPercentIdleJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalClientsProcessedJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(systemJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(totalClientsReceivedJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        serverStatsJPanel.setBackground(new java.awt.Color(0, 153, 255));
        serverStatsJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Server Statistics"));

        JLabel8.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        JLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        JLabel8.setText("Avg Mean Rate:");

        avgMeanRateJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        avgMeanRateJTextField.setToolTipText("Mean service rate");
        avgMeanRateJTextField.setFocusable(false);

        jLabel17.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Avg Service Rate:");

        avgServiceRateJTextField.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        avgServiceRateJTextField.setToolTipText("Average service rate");
        avgServiceRateJTextField.setFocusable(false);

        javax.swing.GroupLayout serverStatsJPanelLayout = new javax.swing.GroupLayout(serverStatsJPanel);
        serverStatsJPanel.setLayout(serverStatsJPanelLayout);
        serverStatsJPanelLayout.setHorizontalGroup(
            serverStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serverStatsJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serverStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel17)
                    .addComponent(JLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(serverStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(avgServiceRateJTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                    .addComponent(avgMeanRateJTextField))
                .addContainerGap())
        );
        serverStatsJPanelLayout.setVerticalGroup(
            serverStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serverStatsJPanelLayout.createSequentialGroup()
                .addGroup(serverStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avgMeanRateJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(serverStatsJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avgServiceRateJTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dupQueueJPanel.setBackground(new java.awt.Color(0, 153, 255));
        dupQueueJPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Queue"));

        dupQueueJList.setFont(new java.awt.Font("SansSerif", 0, 8)); // NOI18N
        dupQueueJList.setToolTipText("Current queue");
        dupQueueJScrollPane.setViewportView(dupQueueJList);

        javax.swing.GroupLayout dupQueueJPanelLayout = new javax.swing.GroupLayout(dupQueueJPanel);
        dupQueueJPanel.setLayout(dupQueueJPanelLayout);
        dupQueueJPanelLayout.setHorizontalGroup(
            dupQueueJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dupQueueJPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dupQueueJScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        dupQueueJPanelLayout.setVerticalGroup(
            dupQueueJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dupQueueJPanelLayout.createSequentialGroup()
                .addComponent(dupQueueJScrollPane)
                .addContainerGap())
        );

        javax.swing.GroupLayout summaryJPanelLayout = new javax.swing.GroupLayout(summaryJPanel);
        summaryJPanel.setLayout(summaryJPanelLayout);
        summaryJPanelLayout.setHorizontalGroup(
            summaryJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(summaryJPanelLayout.createSequentialGroup()
                .addGroup(summaryJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(clientJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(systemJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(summaryJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dupQueueJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(serverStatsJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        summaryJPanelLayout.setVerticalGroup(
            summaryJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(summaryJPanelLayout.createSequentialGroup()
                .addGroup(summaryJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(summaryJPanelLayout.createSequentialGroup()
                        .addComponent(clientJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(systemJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(summaryJPanelLayout.createSequentialGroup()
                        .addComponent(serverStatsJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dupQueueJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        statsJTabbedPanel.addTab("System Summary", null, summaryJPanel, "Display more statistics");

        jMenuBar1.setBackground(new java.awt.Color(0, 0, 255));

        fileJMenu.setText("File");

        printJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        printJMenuItem.setText("Print");
        printJMenuItem.setToolTipText("Print ");
        printJMenuItem.setEnabled(false);
        fileJMenu.add(printJMenuItem);

        saveJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveJMenuItem.setText("Save");
        saveJMenuItem.setToolTipText("Save results");
        saveJMenuItem.setEnabled(false);
        saveJMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                saveJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(saveJMenuItem);
        fileJMenu.add(jSeparator1);

        exitJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        exitJMenuItem.setText("Exit");
        exitJMenuItem.setToolTipText("Exit program");
        exitJMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exitJMenuItemActionPerformed(evt);
            }
        });
        fileJMenu.add(exitJMenuItem);

        jMenuBar1.add(fileJMenu);

        actionJMenu.setText("Action");

        startJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        startJMenuItem.setMnemonic('s');
        startJMenuItem.setText("Start");
        startJMenuItem.setToolTipText("Start simulation");
        actionJMenu.add(startJMenuItem);

        clearJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        clearJMenuItem.setText("Clear");
        clearJMenuItem.setToolTipText("Clear text field");
        clearJMenuItem.setEnabled(false);
        clearJMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                clearJMenuItemActionPerformed(evt);
            }
        });
        actionJMenu.add(clearJMenuItem);

        statsJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        statsJMenuItem.setMnemonic('t');
        statsJMenuItem.setText("Statistics");
        statsJMenuItem.setToolTipText("Press to move through tab panel");
        statsJMenuItem.setEnabled(false);
        statsJMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                statsJMenuItemActionPerformed(evt);
            }
        });
        actionJMenu.add(statsJMenuItem);

        jMenuBar1.add(actionJMenu);

        helpJMenu.setText("Help");

        aboutJMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        aboutJMenuItem.setMnemonic('a');
        aboutJMenuItem.setText("About");
        aboutJMenuItem.setToolTipText("About form");
        aboutJMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                aboutJMenuItemActionPerformed(evt);
            }
        });
        helpJMenu.add(aboutJMenuItem);

        jMenuBar1.add(helpJMenu);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleJPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(statsJTabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 477, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(titleJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statsJTabbedPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void meanJobJTextFieldKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_meanJobJTextFieldKeyTyped
    {//GEN-HEADEREND:event_meanJobJTextFieldKeyTyped
        /**
         * As user types keys, the keystroke is sent to validation to validate
         * whether keystroke is valid digit, delete, or back keys.  If any other
         * key is presses the event is consumed.
         */    
        // instance variable for holding each key stroke
        char c = evt.getKeyChar();
        
        // If keystrok is not valid consumes event, else no action required
        if (!Validation.validDigit(c) == true)
        {
               evt.consume();        
        }  
    }//GEN-LAST:event_meanJobJTextFieldKeyTyped

    private void durationJTextFieldKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_durationJTextFieldKeyTyped
    {//GEN-HEADEREND:event_durationJTextFieldKeyTyped
        /**
         * As user types keys, the keystroke is sent to validation to validate
         * whether keystroke is valid digit, delete, or back keys.  If any other
         * key is presses the event is consumed.
         */    
        // instance variable for holding each key stroke
        char c = evt.getKeyChar();
        
        // If keystrok is not valid consumes event, else no action required
        if (!Validation.validDigit(c) == true)
        {
               evt.consume();        
        }  
    }//GEN-LAST:event_durationJTextFieldKeyTyped

    private void exitJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitJButtonActionPerformed
    {//GEN-HEADEREND:event_exitJButtonActionPerformed
        // Exits program
        System.exit(0);
    }//GEN-LAST:event_exitJButtonActionPerformed

    private void exitJMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_exitJMenuItemActionPerformed
    {//GEN-HEADEREND:event_exitJMenuItemActionPerformed
        // Calls exit button to close program
        exitJButton.doClick();
    }//GEN-LAST:event_exitJMenuItemActionPerformed

    private void startJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_startJButtonActionPerformed
    {//GEN-HEADEREND:event_startJButtonActionPerformed
        /**
         * When "Start" button is pressed in GUI will first validate user input
         * by calling the validateInput method for each JTextBox in the GUI
         * to validate that the user has entered data, and data is within
         * parameters as defined by program.  When data is validated a call
         * to enable button will enable corresponding buttons and fileMenuItems,
         * and simulation will be run
         * @see:        validateInput()
         * @see:        enableButtons()
         */  
        
        /**
         * if statement validates that all text fields are within parameters
         * if outside parameters validateInput will return boolean false and
         * simulation will not run, JOptionPane will alert user to errors.
         * When all parameters are met validateInput will return boolean true
         * allowing the program to run simulation.
         */
        if ((validateInput("Mean", meanJobJTextField, MIN_MEAN, MAX_MEAN)) &&
               (validateInput("Duration", durationJTextField, MIN_DURATION, MAX_DURATION)) &&
                (validateInput("Arrival", arrivalJTextField, MIN_ARRIVAL, MAX_ARRIVAL)))
        {
            clearLastSimulation();
            
            int meanJob = Integer.parseInt(meanJobJTextField.getText());
            //Static initialization
            Client.setMeanJobSize(meanJob);
            
            int duration = Integer.parseInt(durationJTextField.getText());
            int avgArrivalTime = Integer.parseInt(arrivalJTextField.getText());
            int refreshRate = Integer.parseInt(refreshJComboBox.getSelectedItem().toString());
            int numServers = numPrintersJComboBox.getSelectedIndex() + 1;

            this.myDriver = new SimulationDriver(duration, avgArrivalTime, 
                    refreshRate, numServers);
            
            Thread simStarter = new Thread(myDriver);
            simStarter.start();
            setLabelVisible(numServers, true);
            startDisable(true);  
            statsJButton.setEnabled(true);
            statsJMenuItem.setEnabled(true);
            
            //create a new summary for every run
            this.mySummary = new StatsSummary();
        }

    }//GEN-LAST:event_startJButtonActionPerformed

    private void statsJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_statsJButtonActionPerformed
    {//GEN-HEADEREND:event_statsJButtonActionPerformed
        /**
         * When button is pressed captures text of string of button and uses
         * the statsButtonEvent method to compare Strings.
         * @see statsButtonEvent()
         */
        //String cmd = evt.getActionCommand();
        statsButtonEvent();
        
    }//GEN-LAST:event_statsJButtonActionPerformed

    private void statsJMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_statsJMenuItemActionPerformed
    {//GEN-HEADEREND:event_statsJMenuItemActionPerformed
        // Calls the statsJButton action
        statsJButton.doClick();        
    }//GEN-LAST:event_statsJMenuItemActionPerformed

    private void aboutJMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_aboutJMenuItemActionPerformed
    {//GEN-HEADEREND:event_aboutJMenuItemActionPerformed
        // Dispalys about form
        AboutForm aboutWindow = new AboutForm();
        aboutWindow.setVisible(true);
    }//GEN-LAST:event_aboutJMenuItemActionPerformed

    private void arrivalJTextFieldKeyTyped(java.awt.event.KeyEvent evt)//GEN-FIRST:event_arrivalJTextFieldKeyTyped
    {//GEN-HEADEREND:event_arrivalJTextFieldKeyTyped
        /**
         * As user types keys, the keystroke is sent to validation to validate
         * whether keystroke is valid digit, delete, or back keys.  If any other
         * key is presses the event is consumed.
         */    
        // instance variable for holding each key stroke
        char c = evt.getKeyChar();
        
        // If keystrok is not valid consumes event, else no action required
        if (!Validation.validDigit(c) == true)
        {
               evt.consume();        
        }          
    }//GEN-LAST:event_arrivalJTextFieldKeyTyped

    private void printtJButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_printtJButtonActionPerformed
    {//GEN-HEADEREND:event_printtJButtonActionPerformed
        // Calls the print utitlities class and printes GUI window
        try
        {
            //prints JTextArea
            PrintUtilities.printComponent(this);
            PrintUtilities.printComponent(queueJPanel);
            PrintUtilities.printComponent(printStatsJPanel);
            PrintUtilities.printComponent(summaryJPanel);
        }
        catch (Exception e)
        {
            //e.printStackTrace();
        }
    }//GEN-LAST:event_printtJButtonActionPerformed

    private void clearJjButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearJjButtonActionPerformed
    {//GEN-HEADEREND:event_clearJjButtonActionPerformed
        // Clears all text fields calls clearComponents() method
        clearComponents();
    }//GEN-LAST:event_clearJjButtonActionPerformed

    private void clearJMenuItemActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_clearJMenuItemActionPerformed
    {//GEN-HEADEREND:event_clearJMenuItemActionPerformed
        // calls clear button
        clearJjButton.doClick();
    }//GEN-LAST:event_clearJMenuItemActionPerformed

    private void animated3JLabelMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_animated3JLabelMouseEntered
    {//GEN-HEADEREND:event_animated3JLabelMouseEntered
        /**
         * Displays current Client being served by printer as tool tip in the 
         * GUI when user scrolls mouse over printer icon.  A try catch is used
         * to catch exceptions if server has not received Client, or is
         * finished with job.
         */
            try
            {
                String display = this.myDriver.myServers.get(2).getClient().toString();
                animated3JLabel.setToolTipText(display);
            }
            catch(NullPointerException e)
            {}
            catch(IndexOutOfBoundsException i)
            {}
    }//GEN-LAST:event_animated3JLabelMouseEntered

    private void animated1JLabelMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_animated1JLabelMouseEntered
    {//GEN-HEADEREND:event_animated1JLabelMouseEntered
       /**
         * Displays current Client being served by printer as tool tip in the 
         * GUI when user scrolls mouse over printer icon.  A try catch is used
         * to catch exceptions if server has not received Client, or is
         * finished with job.
         */
            try
            {
                String display = this.myDriver.myServers.get(0).getClient().toString();
                animated1JLabel.setToolTipText(display);
            }
            catch(NullPointerException e)
            {}
            catch(IndexOutOfBoundsException i)
            {}
        
    }//GEN-LAST:event_animated1JLabelMouseEntered

    private void animated2JLabelMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_animated2JLabelMouseEntered
    {//GEN-HEADEREND:event_animated2JLabelMouseEntered
         /**
         * Displays current Client being served by printer as tool tip in the 
         * GUI when user scrolls mouse over printer icon.  A try catch is used
         * to catch exceptions if server has not received Client, or is
         * finished with job.
         */
            try
            {
                String display = this.myDriver.myServers.get(1).getClient().toString();
                animated2JLabel.setToolTipText(display);
            }
            catch(NullPointerException e)
            {}
            catch(IndexOutOfBoundsException i)
            {}
    }//GEN-LAST:event_animated2JLabelMouseEntered

    private void animated4JLabelMouseEntered(java.awt.event.MouseEvent evt)//GEN-FIRST:event_animated4JLabelMouseEntered
    {//GEN-HEADEREND:event_animated4JLabelMouseEntered
        /**
         * Displays current Client being served by printer as tool tip in the 
         * GUI when user scrolls mouse over printer icon.  A try catch is used
         * to catch exceptions if server has not received Client, or is
         * finished with job.
         */
            try
            {
                String display = this.myDriver.myServers.get(3).getClient().toString();
                animated4JLabel.setToolTipText(display);
            }
            catch(NullPointerException e)
            {}
            catch(IndexOutOfBoundsException i)
            {}
    }//GEN-LAST:event_animated4JLabelMouseEntered

    private void saveJMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveJMenuItemActionPerformed
        int returnVal = myJFileChooser.showSaveDialog(null);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
            File file = myJFileChooser.getSelectedFile();
            String tempPath = file.getAbsolutePath();
            try 
            {
                file.createNewFile();
                writeToFile(tempPath);
                this.filePath = tempPath;
                this.myJFileChooser.setCurrentDirectory(file);
            } 
            catch (IOException e) 
            {
                JOptionPane.showMessageDialog(null, "Error creating file.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
    }//GEN-LAST:event_saveJMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        // Instantiates splash screen
        // Splash screen set to visible for 2000 milliseconds
        // After 2000 milliseconds GUI is instantiated.
        Splash mySplash = new Splash(1000);
        mySplash.showSplash();
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(PrinterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(PrinterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(PrinterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(PrinterGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new PrinterGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel JLabel2;
    private javax.swing.JLabel JLabel3;
    private javax.swing.JLabel JLabel4;
    private javax.swing.JLabel JLabel5;
    private javax.swing.JLabel JLabel6;
    private javax.swing.JLabel JLabel7;
    private javax.swing.JLabel JLabel8;
    private javax.swing.JMenuItem aboutJMenuItem;
    private javax.swing.JMenu actionJMenu;
    private javax.swing.JLabel animated1JLabel;
    private javax.swing.JLabel animated2JLabel;
    private javax.swing.JLabel animated3JLabel;
    private javax.swing.JLabel animated4JLabel;
    private javax.swing.JLabel arrivalJLabel;
    private javax.swing.JTextField arrivalJTextField;
    private javax.swing.JTextField avgJobsInQueueJTextField;
    private javax.swing.JTextField avgJobsInSystemJTextField;
    private javax.swing.JTextField avgMeanRateJTextField;
    private javax.swing.JTextField avgPercentIdleJTextField;
    private javax.swing.JTextField avgServiceRateJTextField;
    private javax.swing.JTextField avgServiceTimeJTextField;
    private javax.swing.JTextField avgTimeQueueJTextField;
    private javax.swing.JTextField avgTimeSystemJTextField;
    private javax.swing.JPanel buttonJPanel;
    private javax.swing.JMenuItem clearJMenuItem;
    private javax.swing.JButton clearJjButton;
    private javax.swing.JPanel clientJPanel;
    private javax.swing.JTextField currentClient1JTextField;
    private javax.swing.JTextField currentClient2JTextField;
    private javax.swing.JTextField currentClient3JTextField;
    private javax.swing.JTextField currentClient4JTextField;
    private javax.swing.JTextField currentServiceRate1JTextField;
    private javax.swing.JTextField currentServiceRate2JTextField;
    private javax.swing.JTextField currentServiceRate3JTextField;
    private javax.swing.JTextField currentServiceRate4JTextField;
    private javax.swing.JList dupQueueJList;
    private javax.swing.JPanel dupQueueJPanel;
    private javax.swing.JScrollPane dupQueueJScrollPane;
    private javax.swing.JLabel durationJLabel;
    private javax.swing.JTextField durationJTextField;
    private javax.swing.JButton exitJButton;
    private javax.swing.JMenuItem exitJMenuItem;
    private javax.swing.JMenu fileJMenu;
    private javax.swing.JMenu helpJMenu;
    private javax.swing.JPanel inputJPanel;
    private javax.swing.JLabel intervalJLabel;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel logoJLabel;
    private javax.swing.JLabel meanJobJLabel;
    private javax.swing.JTextField meanJobJTextField;
    private javax.swing.JTextField meanServiceRate1JTextField;
    private javax.swing.JTextField meanServiceRate2JTextField;
    private javax.swing.JTextField meanServiceRate3JTextField;
    private javax.swing.JTextField meanServiceRate4JTextField;
    private javax.swing.JFileChooser myJFileChooser;
    private javax.swing.JComboBox numPrintersJComboBox;
    private javax.swing.JLabel numPrintersJLabel;
    private javax.swing.JMenuItem printJMenuItem;
    private javax.swing.JPanel printStatsJPanel;
    private javax.swing.JPanel printer1JPanel;
    private javax.swing.JPanel printer2JPanel;
    private javax.swing.JPanel printer3JPanel;
    private javax.swing.JPanel printer4JPanel;
    private javax.swing.JButton printtJButton;
    private javax.swing.JList queueJList;
    private javax.swing.JPanel queueJPanel;
    private javax.swing.JScrollPane queueJScrollPane;
    private javax.swing.JComboBox refreshJComboBox;
    private javax.swing.JMenuItem saveJMenuItem;
    private javax.swing.JPanel serverStatsJPanel;
    private javax.swing.JButton startJButton;
    private javax.swing.JMenuItem startJMenuItem;
    private javax.swing.JButton statsJButton;
    private javax.swing.JMenuItem statsJMenuItem;
    private javax.swing.JTabbedPane statsJTabbedPanel;
    private javax.swing.JPanel summaryJPanel;
    private javax.swing.JPanel systemJPanel;
    private javax.swing.JTextField timeLeft1JTextField;
    private javax.swing.JTextField timeLeft2JTextField;
    private javax.swing.JTextField timeLeft3JTextField;
    private javax.swing.JTextField timeLeft4JTextField;
    private javax.swing.JLabel titleJLabel;
    private javax.swing.JPanel titleJPanel;
    private javax.swing.JTextField totalClientsProcessedJTextField;
    private javax.swing.JTextField totalClientsReceivedJTextField;
    // End of variables declaration//GEN-END:variables


    /** 
     * <pre>
     * Class:           SimulationDriver
     * File:            PrinterGUI.java
     * Description:     This is the driver for the Printer Queue Simulation 
     * project.  It uses concurrency to run multiple threads allowing the program
     * to have multiple Servers that serve randomly generated number of Clients.
     * 
     * Created:         6/1/2013
     * Hours:           10
     * Platform:        jdk 1.7.0_17; NetBeans IDE 7.3; Windows 7
     * Environment:     PC, Windows Vista Business, jdk 7.0, NetBeans 7.0.1
     * @author:         Chris Steigerwald
     * @author:         Tyler Kowalczik
     * @version         1.0 
     * </pre>
     */
    protected class SimulationDriver implements Runnable
    {
        //This is a random number generator used to produce exponentially distributed
        //inter arrival times.
        Random arrivalTimeGenerator;
        //The average inter arrival time set by the user
        double averageInterArrivalTime;
        //the amount of time the simulation is supposed to run for
        int simulationDuration;
        //Keeps track of the total amount of jobs
        int numJobs;
        // Used for keeping track of system statistics
        private SystemAnalytics myStats;
        // the queue to hold all the generated clients
        protected ClientQueue myQueue;
        private ArrayList<Server> myServers; 
        //  data structure to hold all threads needed for the program.
        private ArrayList<Thread> myThreads;
        private int numServers;
        
      /**
       * <pre>
       * Constructor:   SimulationDriver
       * Description:   An overloaded constructor that initializes commonly used fields.
       * precondition: takes in 3 variables input by user:
       * duration of simulation
       * average arrival time between Clients being randomly added to queue
       * number of Servers to used during simulation.
       * @param duration
       * @param averageInterArrivalTime
       * @param numServers
       * @author Tyler Kowalczik
       * @author Chris Steigerwald
       * @date 6/1/2013
       * </pre>
       */    
        public SimulationDriver(int duration, int averageInterArrivalTime, int refreshRate,
                int numServers) 
        {
            this.simulationDuration = duration;
            this.numServers = numServers;
            this.averageInterArrivalTime = averageInterArrivalTime;
            this.arrivalTimeGenerator = new Random(this.averageInterArrivalTime);
            this.myQueue = new ClientQueue();
            numJobs = 0;
            myServers = new ArrayList<Server>();
            myThreads = new ArrayList<Thread>();
            this.myStats = new SystemAnalytics(this, this.simulationDuration,  
                    refreshRate, numServers);
            Thread statsThread = new Thread(this.myStats);
            myThreads.add(statsThread);
            mySummary = new StatsSummary();
        }
        
    /**
     * <pre>
     * Method:      addClient
     * Description: Simple method for adding client to queue
     * precondition:    cuurentTime is passed into method
     * postcondition:   a new Client is added to queue
     * @param currentTime
     * @return void
     * @return int jobSize
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        private void addClient(long currentTime, int intCurrentTime) throws InterruptedException
        {
            Client tempClient = new Client(currentTime, intCurrentTime);
            myQueue.enqueue(tempClient);
            addToDisplay(tempClient);
        }
        
    /**
     * <pre>
     * Method:      addToDisplay
     * Description: Simple method for adding the new Client to the GUI
     * precondition:    A new Client is passed in
     * postcondition:   Client is added to GUI to be displayed
     * @param client
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected void addToDisplay(Client client)
        {
            synchronized(myModel)
            {
                myModel.addElement(client);
            }
        }
        
    /**
     * <pre>
     * Method:      removeFromDisplay
     * Description: Simple method for removing Client from display
     * precondition:    Client is currently displayed in GUI
     * postcondition:   Client is removed from display in GUI
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected void removeFromDisplay()
        {
            synchronized(myModel)
            {
                if (!myModel.isEmpty())
                {
                    myModel.remove(0);
                }
            }
        }
        
    /**
     * <pre>
     * Method:      getNumJobsInSystem
     * Description: Simple method for getting current number of Clients in the
     * system.  A call is made to the queue and the Clients that are currently
     * being serviced are added to that for a total number of Clients in 
     * the system.
     * precondition:  That a Client is in the system  
     * postcondition:  Return total number of Clients in simulation 
     * @param none
     * @return int allJobs
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected int getNumJobsInSystem()
        {
            int allJobs = this.myQueue.getSize();

            for (int i = 0; i < myServers.size(); i++)
            {
                if (!myServers.get(i).isFree())
                    allJobs++;
            }
            System.out.println("Current number of jobs in network: " + allJobs);
            return allJobs;
        }  
        
    /**
     * <pre>
     * Method:      getNumJobsInQueue
     * Description: Simple method for getting the number of Clients currently
     * in the queue.
     * precondition:    The queue is not null
     * postcondition:   Return number of Clients in the queue
     * @param none
     * @return int this.myQueue.getCurrentNumJobs()
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected int getNumJobsInQueue()
        {
            return this.myQueue.getSize();
        }
        
    /**
     * <pre>
     * Method:      updateClientStats
     * Description: Simple method for calling the SystemAnalytics class to 
     * dynamically update the stats as the simulation is running.
     * precondition:    A Client is passed in
     * postcondition:   Stats are dynamically updated from SystemAnalytics class
     * @param none
     * @return int jobSize
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected void updateClientStats(Client client)
        {
            this.myStats.incrementJobCount();
            this.myStats.calculateAverageServiceTime(client.getDurationOfService());
            this.myStats.calculateAverageTimeInQueue(client.getTimeInQueue());
            this.myStats.calculateAverageTimeInSystem(client.getTimeInSystem());
        }    
        
    /**
     * <pre>
     * Method:      updateTime
     * Description: Simple method that uses a switch statement to update the 
     * time remaining for a Client on each server.  ID, and runningTime is
     * passed in.
     * precondition:   That Servers are instantiated and there is Clients 
     * postcondition:   Updated remaining service time is returned
     * @param ID
     * @param runningTime
     * @return int jobSize
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected void updateTime(char ID, int runningTime)
        {
            switch (ID) 
            {
                case 'A':   timeLeft1JTextField.setText(Integer.toString(runningTime));
                            break;
                case 'B':   timeLeft2JTextField.setText(Integer.toString(runningTime));
                            break;
                case 'C':   timeLeft3JTextField.setText(Integer.toString(runningTime));
                            break;
                case 'D':   timeLeft4JTextField.setText(Integer.toString(runningTime));
                            break;    
            }
        }
        
        /**
     * <pre>
     * Method:      updateServerFields
     * Description: Method for updating the Server fields in GUI dynamically
     * precondition:    Simulation is running, pass in meanServiceRate and
     * currentServiceRate
     * postcondition:   Dynamically update Server statistics in GUI
     * @param meanServiceRate 
     * @param currentServiceRate 
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected void updateServerFields(double meanServiceRate, double currentServiceRate,
                int clientID, char ID)
        {
            switch(ID)
            {
                case 'A':   meanServiceRate1JTextField.setText(Double.toString(meanServiceRate));
                            currentServiceRate1JTextField.setText(Double.toString(currentServiceRate));
                            currentClient1JTextField.setText(Integer.toString(clientID));
                            break;
                case 'B':   meanServiceRate2JTextField.setText(Double.toString(meanServiceRate));
                            currentServiceRate2JTextField.setText(Double.toString(currentServiceRate));
                            currentClient2JTextField.setText(Integer.toString(clientID));
                            break;
                case 'C':   meanServiceRate3JTextField.setText(Double.toString(meanServiceRate));
                            currentServiceRate3JTextField.setText(Double.toString(currentServiceRate));
                            currentClient3JTextField.setText(Integer.toString(clientID));
                            break;
                case 'D':   meanServiceRate4JTextField.setText(Double.toString(meanServiceRate));
                            currentServiceRate4JTextField.setText(Double.toString(currentServiceRate));
                            currentClient4JTextField.setText(Integer.toString(clientID));
                            break; 
            }
        }
    
    /**
     * <pre>
     * Method:      updateStatFields
     * Description: A method for updating statistical fields in GUI that is 
     * called to dynamically update fields of simulation runs
     * precondition:    That simulation is running
     * postcondition:   Dynamically updates statistical fields
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected void updateStatFields()
        {
            avgTimeQueueJTextField.setText(Double.toString(this.myStats.getAverageTimeInQueue()));
            avgTimeSystemJTextField.setText(Double.toString(this.myStats.getAverageTimeInSystem()));
            avgServiceTimeJTextField.setText(Double.toString(this.myStats.getAverageServiceTime()));
            avgJobsInQueueJTextField.setText(Double.toString(this.myStats.getAverageJobsInQueue()));
            avgJobsInSystemJTextField.setText(Double.toString(this.myStats.getAverageJobsInSystem()));
            avgPercentIdleJTextField.setText("%" + Double.toString(this.myStats.getAveragePercentIdle()));
            totalClientsProcessedJTextField.setText(Integer.toString(this.myStats.getTotalClientsProcessed()));
            totalClientsReceivedJTextField.setText(Integer.toString(this.myStats.getTotalClientsProcessed()
                    + this.myQueue.getSize()));
            avgServiceRateJTextField.setText("%" + getAverageSystemServiceRate());
        }
        
    /**
     * <pre>
     * Method:      getServers
     * Description: Simple method for returnging an ArrayList of servers.
     * precondition:    That a Server has been instantiated
     * postcondition:   Return and ArrayList of Server's
     * @param none
     * @return ArrayList<Server>
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected ArrayList<Server> getServers()
        {
            return this.myServers;
        }
        
    /**
     * <pre>
     * Method:      interruptThreads
     * Description: Simple method for pausing threads as needed
     * precondition:    
     * postcondition:   A thread is interrupted
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        private void interruptThreads()
        {
            //Interrupt all running threads.
            for (int i = 0; i < myThreads.size(); i++)
            {
                myThreads.get(i).interrupt();
            }
        }
        
    /**
     * <pre>
     * Method:          resetServerIDs
     * Description:     Resets the static field for server IDs
     * precondition:    Simulation has completed. 
     * postcondition:   JTextArea, Client ID, and Server ID set to default
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected void resetServerIDs()
        {
            Server.resetIDs();
        }
        
    /**
     * <pre>
     * Method:          resetClientIDs
     * Description:     Resets the static field for client IDs
     * precondition:    Simulation has completed. 
     * postcondition:   JTextArea, Client ID, and Server ID set to default
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */      
        protected void resetClientIDs()
        {
            Client.resetIDs();
        }
        
    /**
     * <pre>
     * Method:      resetServerFields
     * Description: Resests all server fields in GUI to ""
     * precondition:    Simulation has been run at least once
     * postcondition:   Clear all server statistical fields in GUI
     * @param ID
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        protected void resetServerFields(char ID)
        {
           switch(ID)
            {
                case 'A':   meanServiceRate1JTextField.setText("");
                            currentServiceRate1JTextField.setText("");
                            currentClient1JTextField.setText("");
                            timeLeft1JTextField.setText("");
                            break;
                case 'B':   meanServiceRate2JTextField.setText("");
                            currentServiceRate2JTextField.setText("");
                            currentClient2JTextField.setText("");
                            timeLeft2JTextField.setText("");
                            break;
                case 'C':   meanServiceRate3JTextField.setText("");
                            currentServiceRate3JTextField.setText("");
                            currentClient3JTextField.setText("");
                            timeLeft3JTextField.setText("");
                            break;
                case 'D':   meanServiceRate4JTextField.setText("");
                            currentServiceRate4JTextField.setText("");
                            currentClient4JTextField.setText("");
                            timeLeft4JTextField.setText("");
                            break; 
           }
        }
        
    /**
     * <pre>
     * Method:      getAverageSystemMeanRate
     * Description: Method for cacuclating the mean system average rate
     * precondition:   Simulation is running 
     * postcondition:  Return a formatted mean system average rate 
     * @param none
     * @return String formatted mean system average rate
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        private String getAverageSystemMeanRate()
        {
            double systemMeanRate = 0.0;
            
            for (int i = 0; i < this.myServers.size(); i++)
            {
                systemMeanRate += this.myServers.get(i).getMeanServiceRate();
            }
            
            systemMeanRate = systemMeanRate / this.myServers.size();
            
            return decFormat.format(systemMeanRate);
        }
        
    /**
     * <pre>
     * Method:      getAverageSystemServiceRate
     * Description: Method for calculating average system service rate
     * precondition:    System is running
     * postcondition:   Return formatted average system service rate
     * @param none
     * @return String formatted average system service rate
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        private String getAverageSystemServiceRate()
        {
            double averageSystemServiceRate = 0.0;
            
            for (int i = 0; i < this.myServers.size(); i++)
            {
                averageSystemServiceRate += this.myServers.get(i).getAverageServiceRate();
            }
            
            averageSystemServiceRate = averageSystemServiceRate / this.myServers.size();
            
            return decFormat.format(averageSystemServiceRate);
        }
      
        /**
         * Method for setting all of the values in the simulations stats summary
         */
        private void saveSummary()
        {
            try
            {
            mySummary.avgTimeInQueue = avgTimeQueueJTextField.getText();
            mySummary.avgTimeInSystem = avgTimeSystemJTextField.getText();
            mySummary.avgServiceTime = avgServiceTimeJTextField.getText();
            mySummary.avgJobsInQueue = avgJobsInQueueJTextField.getText();
            mySummary.avgJobsInSystem = avgJobsInSystemJTextField.getText();
            mySummary.percentTimeIdle = avgPercentIdleJTextField.getText();
            mySummary.totalClientsProcessed = totalClientsProcessedJTextField.getText();
            mySummary.totalClientsReceived = totalClientsReceivedJTextField.getText();
            mySummary.avgMeanRate = avgMeanRateJTextField.getText();
            mySummary.avgServiceRate = avgServiceRateJTextField.getText();
            mySummary.numPrinters = Integer.toString(this.numServers);
            mySummary.meanJobSize = meanJobJTextField.getText();
            mySummary.durationOfSimulation = durationJTextField.getText();
            mySummary.meanArrivalTime = arrivalJTextField.getText();
            mySummary.refreshRate = refreshJComboBox.getSelectedItem().toString();
            }
            catch (NullPointerException e)
            {
                System.out.println("null pointer");
            }
        }

    /**
     * <pre>
     * Method:      run
     * Description: Simple method for getting the jobSize of this class.
     * precondition:    
     * postcondition:   
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
        @Override
        public void run() 
        {           
            try
            {
                //This is used to track the total running time of the simulation
                int runningTime = 0;
                //vaue to be passed to concurrent threads
                long threadStartTime = System.currentTimeMillis();
                //Set the ArrayList that holds all server objects
                for (int i = 0; i < numServers; i++)
                {
                    myServers.add(new Server(this, simulationDuration, threadStartTime));
                }

                //Set the ArrayList that holds all threads
                for (int i = 0; i < numServers; i++)
                {
                    myThreads.add(new Thread(myServers.get(i)));
                }
                
                avgMeanRateJTextField.setText("%" + getAverageSystemMeanRate());
                
                for (int i = 0; i < myThreads.size(); i++)
                {
                    myThreads.get(i).start();
                }
                
                //This is used to keep track of client arrival times
                int nextArrivalTime = arrivalTimeGenerator.intNextExponential();
                
                System.out.println("Next arrival time = " + nextArrivalTime);
                
                while (runningTime < this.simulationDuration)
                {
                    if (!(runningTime < nextArrivalTime))
                    {
                        addClient(System.currentTimeMillis(), runningTime);
                        nextArrivalTime += arrivalTimeGenerator.intNextExponential();
                        numJobs++;
                    }
                    runningTime++;
                    Thread.sleep(1000);
                }

            }
            catch(InterruptedException e)
            {} 
            
            //perform final operations
            interruptThreads();
            startDisable(false);
            saveSummary();
            resetClientIDs();
            resetServerIDs();
            enableButtons(true);            
            setLabelVisible(MAX_NUM_SERVERS, false);
        }
        
    }
}
