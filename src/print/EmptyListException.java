package print;

// EmptyListException.java
// Class EmptyListException definition
/**
 * <pre>
 * Class:           EmptyListException
 * File:            EmptyListException.java
 * Description:     This class is used if the queue is empty by handling the
 * exception by the authors of this program and not letting java handle it
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
public class EmptyListException extends RuntimeException
{
    public EmptyListException( String name )
    {
        super( "The " + name + " is empty" );
    }
}