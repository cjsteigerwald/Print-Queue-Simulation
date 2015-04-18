
package print;

import java.text.DecimalFormat;

/** 
 * <pre>
 * Class:           SystemAnalytics
 * File:            SystemAnalytics.java
 * Description:     This class performs statistical analysis of the simulation
 * dynamically.  User is able to view the following statistics: 
 * Average time a Client is in queue
 * Average Service time per Client
 * Average jobs performed in a time interval by simulation
 * Average jobs in the queue
 * Percent time a Server is idle
 * Total time a Server is idle
 * 
 * Created:         6/1/2013
 * Hours:           10
 * Platform:        jdk 1.7.0_17; NetBeans IDE 7.3; Windows 7
 * Environment:     PC, Windows Vista Business, jdk 7.0, NetBeans 7.0.1
 * @author:         Chris Steigerwald
 * @author:         Tyler Kowalczik
 * @version         1.0 
 * 
 * @see             java.text.DecimalFormat;
 * </pre>
 */
public class SystemAnalytics implements Runnable
{
    //These fields are updated by the run() method.
    private double avgJobsInQueue;
    private double totalSampledJobsInSystem;
    private double totalSampledJobsInQueue;
    private double avgJobsInSystem;
    //These fields are updated by calls from seperate threads
    private double avgTimeInQueue;
    private long totalTimeInQueue;
    private double avgTimeInSystem;
    private long totalTimeInSystem;
    private double avgServiceTime;
    private long totalServiceTime;
    //These fields are for setting the parameters of the run() method
    private int simulationDuration;
    private int refreshRate;    //Determines how fast values are updated in real time in run()
    private long threadStartTime;
    //This field is just a lock to protect against Thread interleaving
    private Object lock;
    //Network SystemAnalytics communicates with
    private PrinterGUI.SimulationDriver network;
    //used to count # of iterations used for averages.
    private int iterCount;
    private double jobsProcessed;
    private double systemPercentIdle;
    private int totalSystemIdle;
    private int intRefreshRate;
    private double totalRunningTime;
    private int numServers;
    //
    private DecimalFormat decFormat;
    
    /**
     * <pre>
     * Constructor:     SystemAnalytics
     * Description:     Overloaded constructor that initializes commonly used
     * fields.  
     * @param network
     * @param duration
     * @param refreshRate 
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected SystemAnalytics(PrinterGUI.SimulationDriver network, int duration, int refreshRate,
            int numServers)
    {
        this.network = network;
        this.simulationDuration = duration * 1000;
        this.intRefreshRate = refreshRate;
        this.refreshRate = refreshRate * 1000;
        this.iterCount = 1;
        this.decFormat = new DecimalFormat("###.##");
        this.lock = new Object();
        this.totalTimeInSystem = 0;
        this.totalRunningTime = 0.0;
        this.totalSystemIdle = 0;
        this.jobsProcessed = 0.0;
        this.numServers = numServers;
    }
    
    /**
     * <pre>
     * Method:      incrementJobCount
     * Description: Simple method for incrementing the job count
     * precondition:    That there is jobs in simulation
     * postcondition:   Increment the job count by 1
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected void incrementJobCount()
    {
        synchronized(lock)
        {
            this.jobsProcessed++;
        }
    }
    
    /**
     * <pre>
     * Method:      calculateAverageTimeInQueue
     * Description: Simple method for calculating the average time in queue
     * of all Clients during the simulation.  timeInQueue is passed in and is
     * added to total time in queue for all servers.  Total time in queue
     * is divided by jobs processed and mulitplied by 1000 to return time in
     * seconds.
     * precondition:    That there have been Clients in the queue  
     * postcondition:   Return the average time in queue for Clients
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected void calculateAverageTimeInQueue(long timeInQueue)
    {
        synchronized(lock)
        {
            this.totalTimeInQueue += timeInQueue;
            this.avgTimeInQueue = Double.parseDouble(
                    decFormat.format((this.totalTimeInQueue / this.jobsProcessed) / 1000));
        }
    }
    
    /**
     * Simple method for retrieving the average time in queue
     * @return 
     */
    protected double getAverageTimeInQueue()
    {
        return this.avgTimeInQueue;
    }

     /**
     * <pre>
     * Method:      calculateAverageTimeInSystem
     * Description: Simple method for calculating the average time a Client is
     * in the simulation.  timeInSystem is passed in and added to the total
     * of all Clients time in the simulation and divided by total number of
     * Clients in the simulation and mulitplied by 1000 to return time in 
     * seconds.
     * precondition:    That there have been Clients in the queue  
     * postcondition:   Return the average time a Client has been in the simulation
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected void calculateAverageTimeInSystem(long timeInSystem)
    {
        synchronized(lock)
        {
            this.totalTimeInSystem += timeInSystem;
            this.avgTimeInSystem = Double.parseDouble(
                    decFormat.format((this.totalTimeInSystem / (this.jobsProcessed)) / 1000));
        }
    }
     /**
     * <pre>
     * Method:      getAverageTimeInSystem
     * Description: When called this method returns the average time a job is
     * in the system.
     * @param none
     * @return double avgTimeInSystem
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected double getAverageTimeInSystem()
    {
        return this.avgTimeInSystem;
    }
    
     /**
     * <pre>
     * Method:      calculateAverageServiceTime
     * Description: Simple method for calculating the average time it takes a 
     * Server to complete a job.  serviceTime is passed in and added to the 
     * total time all Clients have serviced and divided by the Clients that
     * have been serviced and mulitplied by 1000 to return time in seconds.
     * precondition:   That a Client has been serviced by a Server 
     * postcondition:  Return the average time to service a Client
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    protected void calculateAverageServiceTime(long serviceTime)
    {
        synchronized(lock)
        {
            this.totalServiceTime += serviceTime;
            this.avgServiceTime = Double.parseDouble(
                    decFormat.format((this.totalServiceTime / this.jobsProcessed) / 1000));
        }
    }
    
     /**
     * <pre>
     * Method:      getAverageServiceTime
     * Description: When called this method returns the average service time of
     * the job.
     * @param none
     * @return double avgServiceTime
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */ 
    protected double getAverageServiceTime()
    {
        return this.avgServiceTime;
    }
    
    /**
     * <pre>
     * Method:      calculateAverageJobsInSystem
     * Description: Simple method for getting calculating the average jobs that
     * have been in the simulation.
     * precondition:    That a Client has been serviced by a Server
     * postcondition:   Return the average Clients serviced by a Server.
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    private void calculateAverageJobsInSystem()
    {
        this.totalSampledJobsInSystem += this.network.getNumJobsInSystem();
        this.avgJobsInSystem = Double.parseDouble(
                decFormat.format(this.totalSampledJobsInSystem / this.iterCount));
    }
    
    /**
     * <pre>
     * Method:      getAverageServiceTime
     * Description: When called this method returns the average jobs currently
     * in the system
     * @param none
     * @return double avgJobsInSystem
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */ 
    protected double getAverageJobsInSystem()
    {
        return this.avgJobsInSystem;
    }
    /**
     * <pre>
     * Method:      calculateAverageJobsInQueue
     * Description: Simple method for calculating the average Clients in the
     * queue.
     * precondition:    A Client has been sent to the queue
     * postcondition:   Return the average Clients in the queue
     * @param none
     * @return none
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    private void calculateAverageJobsInQueue()
    {
        this.totalSampledJobsInQueue += this.network.getNumJobsInQueue();
        this.avgJobsInQueue = Double.parseDouble(
                decFormat.format(this.totalSampledJobsInQueue / this.iterCount));
    }
    
    /**
     * <pre>
     * Method:      getAverageJobsInQueue
     * Description: When called this method returns the average jobs currently
     * in the queue
     * @param none
     * @return double avgJobsInQueue
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */ 
    protected double getAverageJobsInQueue()
    {
        return this.avgJobsInQueue;
    }  
    
    /**
     * <pre>
     * Method:      calculateSystemPercentIdle
     * Description: Simple method for calculating the percent of time all Servers
     * have been idle.
     * precondition:    The simulation is run
     * postcondition:   Return the percent of time Servers have been idle
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    private void calculateSystemPercentIdle()
    {
        for (int i = 0; i < numServers; i++)
        {
            if (this.network.getServers().get(i).isFree())
                totalSystemIdle += intRefreshRate;
        }
        
        this.systemPercentIdle = Double.parseDouble(
                decFormat.format((totalSystemIdle / totalRunningTime) * 100));
    }
    
    /**
     * simple method for retrieving the average percent idle
     * @return 
     */
    protected double getAveragePercentIdle()
    {
        return this.systemPercentIdle;
    }
    
    /**
     * Simple method for getting the total amount of clients processed
     * @return 
     */
    protected int getTotalClientsProcessed()
    {
        return (int) this.jobsProcessed;
    }
    
     /**
     * <pre>
     * Method:      run
     * Description: A loop is used to until the simulation ends to update stats
     * and keep track of the number of loops performed for calculations.
     * precondition:  Simulation is run, and preconditions met  
     * postcondition: A running total of stats is updated dynamically  
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
        
        this.threadStartTime = System.currentTimeMillis();
        long timeSimEnds = threadStartTime + simulationDuration;
        
        try
        {
            while (System.currentTimeMillis() < timeSimEnds)
            {
                Thread.sleep(refreshRate);
                this.totalRunningTime += numServers * intRefreshRate;
                calculateAverageJobsInQueue();
                calculateAverageJobsInSystem();
                calculateSystemPercentIdle();
                this.network.updateStatFields();
                this.iterCount++;
            }
        }
        catch (InterruptedException e)
        {}
    }
}
