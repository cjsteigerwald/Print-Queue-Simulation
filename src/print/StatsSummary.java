/**
 * Coarse:          CS 142 Java II
 * Class:           StatsSummary
 * File:            StatsSummary.java
 * Description:     StatsSummary is intended to store all the results of a simulation
 * ran by the SimulationDriver class (inner class of PrinterGUI).
 * 
 * Created:         June 1, 2013
 * Hours:           10
 * Platform:        jdk 1.7.0_17; NetBeans IDE 7.3; Windows 7
 * Environment:     PC, Windows Vista Business, jdk 7.0, NetBeans 7.0.1
 * @author:         Chris Steigerwald
 * @author:         Tyler Kowalczik
 * @version         1.0
 */
package print;


public class StatsSummary 
{ 
    //System variables
    protected String avgTimeInQueue;
    protected String avgTimeInSystem;
    protected String avgServiceTime;
    protected String avgJobsInQueue;
    protected String avgJobsInSystem;
    protected String percentTimeIdle;
    protected String totalClientsProcessed;
    protected String totalClientsReceived;
    //Server variables
    protected String avgMeanRate;
    protected String avgServiceRate;
    //User set variables
    protected String numPrinters;
    protected String meanJobSize;
    protected String durationOfSimulation;
    protected String meanArrivalTime;
    protected String refreshRate;
    
    /*
     * This trivial constructor initializes all stats summary's fields to ensure
     * the integrity of the data when accessed by a caller.
     */
    public StatsSummary()
    {
        avgTimeInQueue = "";
        avgTimeInSystem = "";
        avgServiceTime = "";
        avgJobsInQueue = "";
        avgJobsInSystem = "";
        percentTimeIdle = "";
        totalClientsProcessed = "";
        totalClientsReceived = "";
        avgMeanRate = "";
        avgServiceRate = "";
        numPrinters = "";
        meanJobSize = "";
        durationOfSimulation = "";
        meanArrivalTime = "";
        refreshRate = "";
    }
    
    /**
     * This toString method has been overwritten to write text to a file.
     * @return 
     */
    @Override
    public String toString()
    {
        StringBuilder display = new StringBuilder();
        display.append("PARAMETERS OF SIMULATION" + 
                "\n\nNumber of printers: " + this.numPrinters +
                "\nMean job size: " + this.meanJobSize +
                "\nDuration of simulation: " + this.durationOfSimulation +
                "\nMean arrival time: " + this.meanArrivalTime +
                "\nSelected RefreshRate: " + this.refreshRate + "\n\n");
        
        display.append("CLIENT STATISTICS" + 
                "\n\nAverage time a client spent in the queue: " + this.avgTimeInQueue + " seconds" +
                "\nAverage time a client spent in the system: " + this.avgTimeInSystem + " seconds" +
                "\nAverage service time for a client: " + this.avgServiceTime + " seconds\n\n");
        
        display.append("SYSTEM STATISTICS" + 
                "\n\nAverage number of jobs in the queue: " + this.avgJobsInQueue +
                "\nAverage number of jobs in the system: " + this.avgJobsInSystem +
                "\nPercent of time system was idle: " + this.percentTimeIdle + 
                "\nTotal number of clients dequeued: " + this.totalClientsProcessed +
                "\nTotal number of clients received: " + this.totalClientsReceived + "\n\n");
        
        display.append("SERVER STATISTICS" + "\n(Rates represent a percent value "
                + "where 100.0 means 1 page per second)" +
                "\n\nAverage mean service rate: " + this.avgMeanRate +
                "\nAverage service rate: " + this.avgServiceRate);
        return display.toString();
    }
}
