package print;

import java.awt.event.KeyEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;
/**
 *Class:               Validation
* File:                Validation.java
* Description:         Validates entered values
* @author:             Chris Steigerwald
* Environment:         PC, Windows Vista Business, jdk 7.0, NetBeans 7.0.1
* Date:                04/26/2012
* @version             7.0.1
* @see                 java.util.regex.Matcher
* @see                 java.util.regex.Pattern
 */
public class Validation {
    
    /**
    *<pre>
    *	Method:		validText()
    *	Description:	Validates that charcters are valid
    *	@return:	boolean 
    *	@param:		String: char c
    *	@author:	Chris Steigerwald
    *   @see:           java.util.regex.Matcher
    *   @see:           java.util.regex.Pattern
    *	Date:		5/15/2013
    *<pre>
    **/
    public static boolean validText(char c)
    {
        boolean isValid = false;

        if(Character.isLetter(c) || Character.isISOControl(c))
            return true;
        else
            return false;
    }    
    
     /**
    *<pre>
    *	Method:		checkComplete()
    *	Description:	Validates that paramters from PrinterGUI
    *	@return:	boolean 
    *	@param:		String: String check
    *	@author:	Chris Steigerwald
    *   @see:           java.util.regex.Matcher
    *   @see:           java.util.regex.Pattern
    *	Date:		6/012013
    *<pre>
    **/
     public static boolean checkComplete(String check)
     {
         if(check.equals(""))
         {
             return false;
         }
         else
         {
             return true;
         }         
     }
  
    /**
    *<pre>
    * Method:		validDigit()
    * Description:	Validates that digits are entered, allows user to use
    *                   backspace and delete.  Returns boolean true or false
    * precondition:     is user input as parameter
    * postcondition:    boolean response if user data is within defined parameters
    * typed key is digit, backspace, or delete key press
    * @return:          boolean 
    * @param:		String: char c
    * @author:          Chris Steigerwald
    * @author:          Tyler Kowalczik
    * @see:             java.awt.event.KeyEvent;   
    * @date:		6/01/2013
    *<pre>
    **/
    public static boolean validDigit(char c)
    {    
        boolean isValid = false;

        if(!(Character.isDigit(c)|| (c == KeyEvent.VK_BACK_SPACE) || 
            (c == KeyEvent.VK_DELETE)))
            return false;
        else
            return true;   
    }
    
    /**
    *<pre>
    * Method:		checkSize()
    * Description:	Validates that user entered data is within expected 
    * minimum and maximum boundries of calling method.  Method requires three
    * paramter: user input data(check), minimum(minValue) and maximum(maxValue) 
    * to check against.
    * precondition:     three parameters, user input, and minimum and maximum values
    * postcondition:    boolean response if user data is withing defined parameters
    * @return:          boolean 
    * @param:		int check, minValue, maxValue
    * @author:          Chris Steigerwald
    * @author:          Tyler Kowalczik
    * @see:             java.awt.event.KeyEvent;   
    * @date:		6/01/2013
    *<pre>
    **/
    public static boolean checkSize(int check, int minValue, int maxValue)
    {   
        if ( (check >= minValue) && (check <= maxValue) )
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
}
