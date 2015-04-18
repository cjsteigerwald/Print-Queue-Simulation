
package print;

import java.io.IOException;

/**
 * Course:          CS 142 Java II
 * Class:           SaveSimulation.java
 * File:            Server.java
 * Description:     Simple class for writing to a file that accepts a StatsSummary object
 * 
 * Created:         June 1, 2013
 * Hours:           1
 * Platform:        jdk 1.7.0_17; NetBeans IDE 7.3; Windows 7
 * Environment:     PC, Windows Vista Business, jdk 7.0, NetBeans 7.0.1
 * @author:         Chris Steigerwald
 * @author:         Tyler Kowalczik
 * @version         1.0
 * @see             java.io.IOException
 */
public class SaveSimulation 
{
    private WriteFile wf;
    
    /**
     * Simple constructor that attempts to open the file
     * @param filePath
     * @throws IOException 
     */
    public SaveSimulation(String filePath) throws IOException
    {
        this.wf = new WriteFile(filePath);
    }
    
    /**
     * This method writes the summary objects toString method to the current file.
     * @param summary 
     */
    public void saveSimulation(StatsSummary summary)
    {
        this.wf.write(summary.toString());
        wf.close();
    }
}
