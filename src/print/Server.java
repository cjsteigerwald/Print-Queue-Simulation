package print;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Course:          CS 142 Java II
 * Class:           Server.java
 * File:            Server.java
 * Description:     This class is a runnable class that is designed to extract and
 * process clients from the ClientQueue class contained within the PrintPackage
 * package. The Server class also contains important fields and public interfaces
 * for setting/retrieving statistical data.
 * 
 * Created:         June 1, 2013
 * Hours:           1
 * Platform:        jdk 1.7.0_17; NetBeans IDE 7.3; Windows 7
 * Environment:     PC, Windows Vista Business, jdk 7.0, NetBeans 7.0.1
 * @author:         Chris Steigerwald
 * @author:         Tyler Kowalczik
 * @version         1.0
 * @see             java.text.DecimalFormat
 * @see             java.util.ArrayList
 */

public class Server implements Runnable
{
    /*
     * -------------------------------------------------------------------------
     * STATIC FIELDS
     * -------------------------------------------------------------------------
     */
    //Random number generator used to generate the meanServiceRate of all printer
    //objects
    static final Random MEAN_SERVICE_GENERATOR;
    //100.0 is a percentage value that represents print speed. 100.0 means
    //the printer prints an average of 1 page per second.
    static final double AVERAGE_SERVICE_RATE = 100.0;
    //This is the standard deviation for the meanServiceRate
    static final double STANDARD_SERVICE_RATE_DEVIATION = 20.0;
    //this is the standard deviation for each print job.
    static final double STANDARD_PRINT_JOB_DEVIATION = 10.0;
    //Formats the printer rates into a percentage format.
    static DecimalFormat decFormat = new DecimalFormat("###.##");
    //Keeps track of what the next printer's ID will be.
    static char nextID;
    
    /*
     * -------------------------------------------------------------------------
     * INSTANCE FIELDS
     * -------------------------------------------------------------------------
     */
    //Random number generator used to print the variable rates of each print job.
    private Random printJobRateGenerator;
    //the printer's id
    private char id;
    //The current client being serviced.
    private Client currentClient;
    //The server's meanService and currentService for current print job.
    private double meanServiceRate, currentServiceRate;
    //The driver GUI this is connected to
    private PrinterGUI.SimulationDriver network;
    //Keeps track of all the clients this particular server served
    private int numClientsServed;
    // Keeps track of whether or not the server is busy
    private boolean isFree;
    //
    private long durationOfCurrentService;
    //Keeps track of the time the last service ended;
    private long lastServiceTimeEnded;
    //The duration of the simulation (used to eventually stop the thread);
    private int simulationDuration;
    //Time run() was invoked
    private long threadStartTime;
    //Amount of time server is free
    private long idleTime;
    //integer representation of the duration of the service
    private int intDurationOfService;
    // sum of the service rates
    private double sumServiceRates;
    // current average service rate
    private double averageServiceRate;

    
    /**
     * This static initialization block is used to set the values of nextID and
     * the MEAN_SERVICE_GENERATOR that are shared by all objects of this class.
     */
    static
    {
        nextID = 'A';
        MEAN_SERVICE_GENERATOR = new Random(AVERAGE_SERVICE_RATE,
                STANDARD_SERVICE_RATE_DEVIATION);
    }
    
    /**
     * <pre>
     * Constructor: Server
     * Description: Default constructor that sets the id, the meanServiceRate, 
     * and the  printJobRateGenerator. The printJobRateGenerator belongs to the 
     * object and it's values are dependent on the server object's 
     * meanServiceRate.
     * @param network
     * @param simulationDuration
     * @param threadStartTime
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public Server(PrinterGUI.SimulationDriver network, int simulationDuration,
            long threadStartTime)
    {
        this.id = nextID;
        nextID++;
        //Format the generated service rate for each printer and 
        //conver the formatted value to a double.
        this.meanServiceRate = Double.parseDouble(decFormat.format(
                MEAN_SERVICE_GENERATOR.nextGaussian()));
        //Use above value as mean for jobRate generator
        this.printJobRateGenerator = new Random(meanServiceRate, 
                STANDARD_PRINT_JOB_DEVIATION);
        this.network = network;
        //convert int to long
        
        this.simulationDuration = simulationDuration;
        this.numClientsServed = 0;
        this.sumServiceRates = 0.0;
        this.threadStartTime = threadStartTime;
        this.lastServiceTimeEnded = this.threadStartTime;
        this.isFree = true;
    }
    
   /**
     * <pre>
     * Method:      getClient
     * Description: A simple getter method for retrieving the current client 
     * being serviced.
     * precondition:    a Client has been created
     * postcondition:   return a cuurent Client
     * @param none
     * @return currentClient
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public Client getClient()
    {
        return currentClient;
    }
    
     /**
     * <pre>
     * Method:      getID
     * Description: A simple getter for retrieving the current ID of Server
     * precondition:    a Server has been created
     * postcondition:   return Server ID
     * @param none
     * @return id
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public char getID()
    {
        return this.id;
    }
    
   /**
     * <pre>
     * Method:      beginServing()
     * Description: This method grabs a client at the front of the current queue 
     * and begins to service the client object. Notice that it is the 
     * responsibility of the caller to supply this method with the time that the 
     * Server began serving the client.
     * precondition:    A client and startTime is passed in
     * postcondition:   The Server is started for simulation
     * @param client
     * @param time 
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public void beginServing(Client client, long startTime)
    {
        this.numClientsServed++;
        this.currentClient = client;
        //Reset the printer job rate
        this.currentServiceRate = Double.parseDouble(decFormat.format(
                printJobRateGenerator.nextGaussian()));
        this.sumServiceRates += this.currentServiceRate;
        //By adding one to the end of this statement we are always rounding
        //our service times up.
        this.durationOfCurrentService = (long)((this.currentClient.getJobSize() / 
                (this.currentServiceRate / 100.0)) * 1000);
        this.intDurationOfService = (int) durationOfCurrentService / 1000;

        this.idleTime += (startTime - this.lastServiceTimeEnded);
        //begin serving client
        client.beginService(this, System.currentTimeMillis(), this.durationOfCurrentService);

        this.network.updateClientStats(currentClient);
    }
    
    /**
     * <pre>
     * Method:      calculateAverageServiceRate
     * Description: Method to calculate the average service rate of simulation
     * by taking the sum of all service rates and dividing by number of clients
     * served during simulation
     * precondition:    Simulation is ended
     * postcondition:   Average service rate is calculated
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/13
     * </pre>
     */
    private void calculateAverageServiceRate()
    {
        this.averageServiceRate = this.sumServiceRates / this.numClientsServed;
    }
    
    /**
     * <pre>
     * Method:      getAverageServiceRate
     * Description: Method to return to caller average service rate
     * served during simulation
     * precondition:    average service rate is calculated
     * postcondition:   Return to caller average service rate
     * @param none
     * @return double getAverageServiceRate
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/13
     * </pre>
     */
    protected double getAverageServiceRate()
    {
        calculateAverageServiceRate();
        return this.averageServiceRate;
    }
    
   /**
     * <pre>
     * Method:      getIdleTime
     * Description: Simple method for retrieving the total amount of idleTime
     * precondition:    A Server is instantiated
     * postcondition:   The Server idle time is returned
     * @param none
     * @return  int representation of idleTime 
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected long getIdleTime()
    {
        return this.idleTime;
    }
    
    /**
     * <pre>
     * Method:      getMeanServiceRate
     * Description: Method that returns the mean service rate to caller
     * precondition:    Simulation is run
     * postcondition:   Return mean service rate
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/13
     * </pre>
     */
    protected double getMeanServiceRate()
    {
        return this.meanServiceRate;
    }
   /**
     * <pre>
     * Method:      getPercentIdle
     * Description: Percentage representation of current idle time.
     * precondition:    A Server is instantiated
     * postcondition:   The Server Idle time a percentage is returned
     * @param none
     * @return a percentage of Server idle time
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected double getPercentIdle()
    {
        return Double.parseDouble(decFormat.format(
                this.idleTime / (this.simulationDuration * 1000.0)));
    }
    
   /**
     * <pre>
     * Method:      endServing
     * Description: This method invokes the endService() method on its 
     * currentClient field, and then resets the currentClient to null.
     * precondtion:     Time Server first enters simulation
     * postcondition:   Time Server is done with simulation
     * @param time
     * @return none
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected void endServing(long time)
    { 
        this.currentClient = null;
        this.lastServiceTimeEnded = time;
    }
    
   /**
     * <pre>
     * Method:      isFree
     * Description: Returns a boolean value that is true if the server isn't busy serving a 
     * client.
     * precondition:    Server has been instantiated
     * postcondition:   Return true is Server is not currently printing a job
     * @param none
     * @return boolean true is Server free, else false
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected boolean isFree()
    {
        return this.isFree;
    }
    
    /**
     * <pre>
     * Method:      resetIDs
     * Description: Every time the simulation is reset, the ID needs to be reset.
     * precondition:    Simulation has ended
     * postcondition:   Reset Server nextID to 1
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */      
    protected static void resetIDs()
    {
        nextID = 'A';
    }

    
  
    /**
     * <pre>
     * Method:      toString
     * Description: This is this class' overridden String method that just 
     * returns a formatted string representation of the fields of this 
     * particular class.
     * precondition: Server has been instantiated
     * postcondtion: Overrides objects toString with current one
     * @param none
     * @return String Simple message used for display/debugging
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/13
     * </pre>
     */
    @Override
    public String toString()
    {
        return "ID: " + id + "\n"
                + "Mean Service Rate: " + this.meanServiceRate + "\n"
                + "Current Service Rate: " + this.currentServiceRate + "\n"
                + "Current Client: " + currentClient.toString() + "\n"
                + "Duration of current service: " + durationOfCurrentService;
    }   
    
   /**
     * <pre>
     * Method:      run
     * Description: run() continues until it is interrupted or until the duration
     * of the simulation has been met. It will attempt to dequeue a client from
     * the SimulationDriver's queue, and it will wait if the queue is empty.
     * precondition:    Client, ClientQueue, and Server have been instantiated
     * postcondition:   Systems run method is invoked
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/13
     * </pre>
     */
    @Override
    public void run() 
    {
        long timeSimEnds = threadStartTime + (simulationDuration * 1000);
        
        while (System.currentTimeMillis() < timeSimEnds)
        {
            try 
            {
                //begin serving reassigns currentServiceRate
                beginServing(network.myQueue.dequeue(), System.currentTimeMillis());
                this.isFree = false;
                this.network.removeFromDisplay();
                this.network.updateServerFields(this.meanServiceRate, this.currentServiceRate, 
                        this.currentClient.getID(), this.id);
                
                for (int i = 0; i < this.intDurationOfService; i++)
                {
                    this.network.updateTime(id, this.intDurationOfService - i);
                    Thread.sleep(1000);
                }
                endServing(System.currentTimeMillis());
                this.network.resetServerFields(this.id);
                this.isFree = true;
            } 
            catch (InterruptedException ex) 
            {}
        }
    }
}
