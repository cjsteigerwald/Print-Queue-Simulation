package print;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * <pre>
 * Class:           ClientQueue.java
 * File:            ClientQueue.java
 * Description:     This class is a particular type of queue that accepts only
 * client objects. Only two capabilities are allowed when interfacing with the
 * queues data, which are to either enqueue a client at the back of the list or
 * dequeue a client from the front of the list. This class extends from List, which
 * is a class for storing and navigating through a singly linked list.
 * 
 * Created:         June 1, 2013
 * Hours:           1
 * Platform:        jdk 1.7.0_17; NetBeans IDE 7.3; Windows 7
 * Environment:     PC, Windows Vista Business, jdk 7.0, NetBeans 7.0.1
 * @author:         Chris Steigerwald
 * @author:         Tyler Kowalczik
 * @version         1.0
 * @see java.util.ArrayList
 * @see java.util.concurrent.locks.*
 * @see java.util.concurrent.locks.Condition;
 * @see java.util.concurrent.locks.ReentrantLock;
 * </pre>
 */

public class ClientQueue extends List
{
    //This lock is used to ensure that only one thread can access a ClientQueue
    //object at one time
    private final Lock myLock = new ReentrantLock();
    //This condition is used to notify a waiting thread that the queue is no longer
    //empty
    private final Condition notEmpty = myLock.newCondition();
    //Keeps track of the current number of jobs
    private int currentNumJobs;
    
    /**
     * <pre>
     * Constructor: ClientQueue
     * Description: All the default constructor does is initialize the allClients ArrayList field.
     * precondition: none
     * postcondition: creates an instance of ArrayQueue
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/01/13
     * </pre>
     */
    public ClientQueue()
    {
        super("Client Queue");
        this.currentNumJobs = 0;
    }
    
   /**
     * <pre>
     * Method:      dequeue
     * Description: This method dequeues a client object from the queue. In 
     * order for a thread to dequeue from the list it must acquire this class' 
     * lock, and if the list is currently empty the thread will wait on the 
     * notEmpty condition to be signaled.
     * precondition:    that the queue is not empty
     * postcondition:   pop a Client off the queue
     * @return Client
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/01/13
     * </pre>
     */
    protected Client dequeue() throws EmptyListException, InterruptedException
    {        
        myLock.lock();
        try
        {
            //Call to the super class' isEmpty method
            while (super.isEmpty())
            {
                notEmpty.await(); 
            }
            this.currentNumJobs--;
            //Only clients are allowed in the queue, thus object casting is safe
            return (Client) super.removeFromFront();
        }
        finally
        {
            myLock.unlock();
        }
    }
    
    /**
     * <pre>
     * Method:      enqueue
     * Description: This method adds an item to the end of the queue. In order 
     * for a thread to add an item to the list it must acquire the class' lock. 
     * Upon inserting a new item into the queue the notEmpty condition is 
     * signaled.
     * precondition:    that a queue has been instantiated
     * postcondition:   that a client is added to the queue
     * @param Client The client object to be added to the queue
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/01/13
     * </pre>
     */
    public void enqueue(Client newClient) throws InterruptedException
    {
        myLock.lock();
        try     
        {
            super.insertAtBack(newClient);
            this.currentNumJobs++;
            this.notEmpty.signal();
        }
        finally 
        {
            myLock.unlock();
        }
    }  
    
    /**
     * <pre>
     * Method:      getSize
     * Description: Since this is only an accessor method, it is relatively 
     * safe to retrieve this value even if other threads are simultaneously 
     * adding/removing from the list. This method is intended to be used for 
     * statistical analysis, therefore we're not too concerned with being off by 
     * one or two jobs whenever a sample value is retrieved.
     * precondition:    that there has been Clients added to queue
     * postcondition:   return current number of jobs in system
     * @param none
     * @param currentNumJobs an  int of jobs currently in system
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/01/13
     * </pre>
     */
    public int getSize()
    {
        return this.currentNumJobs;
    }
}
