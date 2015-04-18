package print;

/**
 * <pre>
 * Class:           List
 * File:            List.java
 * Description: This class is a singly linked list for the class ListNode which is also in the
 * PrintPackage package. 
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

/*
 * -----------------------------------------------------------------------------
 * NOTES: All of the synchronized keywords have been removed from method declarations.
 * It will be the responsibility of the queue class that inherits from list to
 * ensure that it is thread safe.
 * -----------------------------------------------------------------------------
 */

public class List
{
    private ListNode firstNode;
    private ListNode lastNode;
    protected String name; // String like "list" used in printing

    /**
     * <pre>
     * Constructor: List
     * Description:  This is the default constructor of the class. This 
     * constructor calls the class' overloaded constructor and passes to it an 
     * automatically assigned name.
     * @param: none
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public List()
    {
        this( "List" );
    }

    /**
     * <pre>
     * Constructor: List
     * Description: This overloaded constructor is either called implicitly or 
     * explicitly. It setsthe name of the list, as well as the firstNode and 
     * lastNode values to null.
     * @param name
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     */
    public List( String name )
    {
        this.name = name;
        firstNode = lastNode = null;
    }


    /**
     * <pre>
     * Method:      insertAtFront
     * Description: This method inserts a newly created ListNode at the 
     * beginning of the list.
     * precondition:    A Client object is passed in
     * postcondition:   A Client object is inserted at front of queue
     * @param insertItem The data of the ListNode object being created.
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public void insertAtFront( Object insertItem )
    {
        if ( isEmpty() )
            firstNode = lastNode = new ListNode( insertItem );
        else
            firstNode = new ListNode( insertItem, firstNode );
    }


    /**
     * <pre>
     * Method:       insertAtBack  
     * Description:  This method inserts a newly created ListNode at the back of 
     * insertAtBack
     * precondition:    A Client object is passed in
     * postcondition:   A Client object is inserted at back of queue
     * @param insertItem The data of the ListNode object being created.
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public void insertAtBack( Object insertItem )
    {
    if ( isEmpty() )
        firstNode = lastNode = new ListNode( insertItem );
    else
        lastNode = lastNode.next = new ListNode( insertItem );
    }
    
    /**
     * <pre>
     * Method:      removeFromFront
     * Description: This method safely removes an object from the front of the 
     * list.
     * precondition: The queue is not null
     * postcondition: an object is popped from queue
     * @param none
     * @return The data of the ListNode that is being removed.
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    
    /*
     * -------------------------------------------------------------------------
     * NOTES: Our queue class will always check to make sure that the list isn't
     * empty before dequeing.
     * -------------------------------------------------------------------------
     */
    public Object removeFromFront()
    {
        Object removeItem = null;
        if ( isEmpty() )
            throw new EmptyListException( name );
        removeItem = firstNode.data; // retrieve the data
        // reset the firstNode and lastNode references
        if ( firstNode.equals( lastNode ) )
            firstNode = lastNode = null;
        else
            firstNode = firstNode.next;
        return removeItem;
    }
    
    /**
     * <pre>
     * Method:      removeFromBack      
     * Description: This method safely removes a ListNode from the back of the 
     * list.
     * precondition:  queue is not null
     * postcondition: remove Client from back of queue
     * @return The data of the ListNode that is being removed.
     * @param none
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public Object removeFromBack()
    {
        Object removeItem = null;
        if ( isEmpty() )
            throw new EmptyListException( name );
        removeItem = lastNode.data; // retrieve the data
        // reset the firstNode and lastNode references
        if ( firstNode.equals( lastNode ) )
            firstNode = lastNode = null;
        else
        {
            ListNode current = firstNode;
            while ( current.next != lastNode ) // not last node
                current = current.next; // move to next node
            lastNode = current;
            current.next = null;
        }
        return removeItem;
    }
    
    /**
     * <pre>
     * Method:      isEmpty
     * Description: This method checks to see whether or not the list is empty.
     * precondition: queue is instantitiated
     * postcondition: returns boolean value true if empty, false if contains objects
     * @param none
     * @return Returns a boolean value True if the list is empty or False if it contains
     * an item.
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public boolean isEmpty()
    { 
        return firstNode == null;
    }
    
    /**
     * <pre>
     * Method:      print
     * Description: Simple method for printing a formatted display of the 
     * contents of the list.
     * precondition:    check to see queue is not empty
     * postcondition:   prints name and nodes
     * @param none
     * @return void
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public void print()
    {
        if ( isEmpty() )
        {
            System.out.println( "Empty " + name );
            return;
        }
        System.out.print( "The " + name + " is: " );
        ListNode current = firstNode;
        while ( current != null )
        {
            System.out.print( current.data.toString() + " " );
            current = current.next;
        }
        System.out.println( "\n" );
    }
}