/*
 *------------------------------------------------------------------------------
 * NOTES: 
 * !) The client class times MUST be set by the driver class as it stands right
 * now in the implementation of the code. 
 * -----------------------------------------------------------------------------
 */
package print;

/** 
 * <pre>
 * Class:           Client
 * File:            Client.java
 * Description:     The Client class is used to generate new clients for the 
 * print queue simulation.  The class is instantiated by passing in the system
 * time at arrival.  The Client sets the Client ID, jobSize, timeInQueue,
 * timeInSystem, and durationOfServices.  This class has get methods for:
 * getJobSize(), getTimeArrived(), getTimeInQueue(), getTimeInSystem(),
 * getDurationOfService().  
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
public class Client
{
  /**
   * ---------------------------------------------------------------------------
   * STATIC FIELDS
   * ---------------------------------------------------------------------------
   */
    
  //The average job size.
  private static int meanJobSize = 100;
  //Only one randomNumber generator is needed seeing as all objects of this class have
  //normally distributed job sizes with the same mean.
  private static Random randGen;
  //Keeps track of all client ID's
  private static int nextID;
  
  /**
   * ---------------------------------------------------------------------------
   * INSTANCE FIELDS
   * ---------------------------------------------------------------------------
   */
  
  //The printer that serves a particular client.
  private Server myServer;
  //All the other important properties of the client class.
  //Note: tBegan, tEnded are set by beginService(Server server, Time time) method
  private int ID;
  private int jobSize;
  private long timeArrived;
  private long timeInQueue;
  private long timeInSystem; 
  //This field is an int as apposed to long because of how it's used/set in the server obj.
  private long durationOfService;
  private int intTimeArrived;
  
  /**
   * static initialization block used to initialize the static random number generator.
   */
  static
  {
      randGen = new Random(meanJobSize);
      nextID = 1;
  }
  
 
  /**
   * <pre>
   * Constructor:   Client
   * Description:   Simple constructor that initializes commonly used fields.
   * @param timeArrived
   * @param intTimeArrived
   * @author Tyler Kowalczik
   * @author Chris Steigerwald
   * @date 6/1/2013
   * </pre>
   */
  public Client(long timeArrived, int intTimeArrived)
  {
      this.timeArrived = timeArrived;
      this.intTimeArrived = intTimeArrived;
      // scaled gaussian number
      this.jobSize = (int) randGen.scaledNextGaussian();
      //this.jobSize = (int) randGen.intNextExponential();
      this.ID = nextID;
      nextID++;
  }
   
    /**
     * <pre>
     * Method:      getJobSize
     * Description: Simple method for getting the jobSize of this class.
     * precondition:    Client class is instantiated
     * postcondition:   Returns jobSize to calling method
     * @param none
     * @return int jobSize
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */  
   public int getJobSize()
   {
       return this.jobSize;
   }
   
    /**
     * <pre>
     * Method:      getTimeArrived
     * Description: Simple method for retrieving the time the client started to 
     * be serviced.
     * precondition:    Client class is instantiated
     * postcondition:   Returns timeArrived to calling method
     * @param none
     * @return long timeArrived to calling method
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
   public long getTimeArrived()
   {
       return this.timeArrived;
   }

    /**
     * <pre>
     * Method:      getTimeInQueue
     * Description: Simple method for retrieving the time the client was in the 
     * queue.
     * precondition:    Client class is instantiated
     * postcondition:   Returns timeInQueue to calling method
     * @param none
     * @return long timeInQueue to calling method
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
   public long getTimeInQueue()
   {
       return this.timeInQueue;
   }
   
   /**
     * <pre>
     * Method:      getTimeInSystem
     * Description: Simple method for retrieving the time the total time client 
     * is in simulation
     * precondition:    Client class is instantiated
     * postcondition:   Returns timeInSystem to calling method
     * @param none
     * @return long timeInSystem to calling method
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
   public long getTimeInSystem()
   {
       return this.timeInSystem;
   }
   
   /**
     * <pre>
     * Method:      getDurationOfService
     * Description: Simple method for retrieving the time duration of time it 
     * took client to 
     * be printed
     * precondition:    Client class is instantiated
     * postcondition:   Returns durationOfService to calling method
     * @param none
     * @return long durationOfService to calling method
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
   public long getDurationOfService()
   {
       return this.durationOfService;
   }
   
   public int getID()
   {
       return this.ID;
   }
   
   /**
     * <pre>
     * Method:          beginService
     * Description:     This method is called when a server begins processing a 
     * client. It sets the server reference and assigns appropriate values to 
     * the client's time values. Every client is processed by SystemAnalytics 
     * once it begins being served.
     * precondition:    Requires server, timeStarted, and durationOfService
     * to be passed in.
     * postcondition:   Sets variables for server, durationOfServices,timeInQueue
     * for this Client.
     * @param Server server: the server that will perform services
     * @param timeStarted: the time that the server recieved the client
     * @param durationOfService: the length of time it takes server to finish job
     * @return none
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
   public void beginService(Server server, long timeStarted, long durationOfService)
   {
       this.myServer = server;
       this.durationOfService = durationOfService;
       this.timeInQueue = timeStarted - timeArrived;
       System.out.println("Time started for " + this.ID + ": " + timeStarted);
       System.out.println("Time Arrived for " + this.ID + ": " + this.timeArrived);
       System.out.println("Time in queue for Client " + this.ID + ": " + this.timeInQueue);
       //Remember timeInSystem is a long, so multiply int dur by 1000;
       this.timeInSystem = timeInQueue + this.durationOfService; //loss of precision
       System.out.println("Time in system " + this.timeInSystem);
   }
   
    /**
     * Method: endService
     * All this method does is set the tEnded field of the client object. Client
     * records should be saved for statistical analysis, and knowledge of the server
     * which served the client is needed for those calculations (ex. printer averages).
     * @param time  The time the service ended.
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 5/22/13
     */
//   public void endService(long time)
//   {
//       tEnded = time;
//   }
   
  /**
    * <pre>
    * Method:       setMeanJobSize
    * Description:  This method sets the client class' static meanJobSize field. 
    * The meanJobSize field is passed to the Random class random number 
    * generator, and the random number generator uses that value to generate 
    * job sizes with an exponentialdistribution with the supplied meanJobSize as 
    * the average size of each print job.
    * precondition:     that an int jobSize is passed in
    * postcondition:    sets a randomly generated job size by calling the 
    * @param jobSize The mean job size of printer jobs.
    * @return void
    * @author Tyler Kowalczik
    * @author Chris Steigerwald
    * @date 6/1/2013
    * </pre>
    */
   protected static void setMeanJobSize(int jobSize)
   {
       meanJobSize = jobSize;
       randGen.setMean(meanJobSize);
   }
   
   /**
     * <pre>
     * Method:      resetIDs
     * Description: Simple method to resetID for clients to number 1 after the  
     * simulation has completed
     * precondition:    Simulation has ended
     * postcondition:   Resets client ID to 1
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
   protected static void resetIDs()
   {
       nextID = 1;
   }
   
   /**
     * <pre>
     * Method: toString
     * This is the class overridden toString method that returns a formatted String
     * representation of the fields of the client object. It's used for display and
     * debugging.
     * precondition:    Client class is instantiated
     * postcondition:   Returns jobSize to calling method
     * @return String The String representation of the client object.
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
  @Override
   public String toString()
   {
       
       return "Client ID: (" + this.ID + ")" + "  Size: (" + this.jobSize +
               ")" +"  Time Arrived: (" + this.intTimeArrived + ")";
   }
}
