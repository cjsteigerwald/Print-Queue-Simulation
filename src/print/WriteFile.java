
package print;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * WriteFile.java
 * Simple class used to write to a file one line at a time.
 * <pre>
 *    Platform: jdk 1.7.0_4; NetBeans IDE 7.0; Windows 7
 *    Course: CS 142
 *    Hours: 1 hour
 *    Created on Apr 24, 2013, 10:00 PM
 *    Revised on
 * </pre>
 *
 * @author:	tylerkowalczik@yahoo.com
 * @version: 	%1%
 * @see:     	javax.swing.io*
 */

public class WriteFile
{
    //All that's needed for this class' operations is a FileWriter
    private FileWriter filename;

    /**
     * A constructor that accepts a String parameter for the specified file path to
     * write to.
     * @param inputFilename The string representation of the file path.
     * @throws IOException If the file is unable to be located or created
     * an IOException is thrown.
     */
    public WriteFile(String inputFilename) throws IOException
    {
        try
        {
            filename = new FileWriter(inputFilename, false);
        }
        catch (FileNotFoundException ex) 
        {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * This method writes to the specified file one line at a time.
     * @param item A line of text to be written to the specified file. 
     */
    public void write(String item)
    {        
        PrintWriter output = new PrintWriter(filename);
        output.println(item);
    }

    /**
     * Simple method that closes the file that was being written to.
     * If the file isn't closed than changes are liable to be unsaved.
     */
    public void close()
    {
        try
        {
            filename.close();
        }
        catch (IOException ex) 
        {
        }
    }
}
