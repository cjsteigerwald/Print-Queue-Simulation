package print;

/**
 * Course:          CS 142 Java II
 * Class:           ListNode.java
 * File:            ListNode.java
 * Description:     This class is just a self referential data structure that is
 * to be stored in a list.
 * 
 * Created:         June 1, 2013
 * Hours:           1
 * Platform:        jdk 1.7.0_17; NetBeans IDE 7.3; Windows 7
 * Environment:     PC, Windows Vista Business, jdk 7.0, NetBeans 7.0.1
 * @author:         Chris Steigerwald
 * @author:         Tyler Kowalczik
 * @version         1.0
 */
class ListNode
{
    //The data the object stores
    protected Object data;
    //The reference to the next ListNode in the linked list if it exists
    protected ListNode next;
    
    /**
     * <pre>
     * Constructor: ListNode
     * Description: Constructor that accepts one parameter which is the data it 
     * will store. The next ListNode reference is set to null.
     * @param data The object to be stored in the ListNode
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public ListNode( Object data )
    {
        this( data, null );
    }
    
    /**
     * <pre>
     * Constructor: ListNode
     * Description: Overloaded constructor that accepts two parameters for both 
     * the data and the next ListNode reference respectively.
     * @param data The object to be stored in the ListNode
     * @param nextNode The next ListNode in the linked list
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public ListNode( Object data, ListNode nextNode )
    {
        this.data = data; // this node refers to Object o
        this.next = nextNode; // set next to refer to next
    }
    
    /**
     * <pre>
     * Method:      getObject
     * Discription: Simple getter method for retrieving whatever data is stored inside the
     * inside the ListNode.
     * precondition:    queue is not null
     * postcondition:   return object from ListNode
     * @param non
     * @return Object The object stored in the ListNode
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public Object getObject()
    {
        return data;
    }
    
    /**
     * <pre>
     * Method:      getNext
     * Discription: Simple method for retrieving the next ListNode in a linked 
     * list.
     * precondition:    listNode is not null
     * postcondition:   return next
     * @param none
     * @return ListNode the obj. reference to the next ListNode in the linked list
     * @author Tyler Kowalczik
     * @author Chris Steigerwald
     * @date 6/1/2013
     * </pre>
     */
    public ListNode getNext()
    {
        return next;
    }
}