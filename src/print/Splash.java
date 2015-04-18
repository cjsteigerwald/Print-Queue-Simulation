/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package print;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * Class:           Shipping Hub
 * File:            Splash.java
 * Description:     Splash Class provides a splash screen on startup for the 
 * Shipping Hub program, and is called in PugetSoundEnergyGUI
 * in the main at start-up.  The splash screens startup length of 
 * time is measured in milliseconds determined by a timer in the main in the
 * PugetSoundEnergyGUI Class.  This Class displays a logo and copyright. 
 * Date:            5/15/2013
 * @author:         Chris Steigerwald
 * Environment:     PC, Windows 7 Pro, jdk1.7.10, NetBeans 7.3
 * @version:        1.0
 * @see             java.awt.Color
 * @see             java.awt.Dimension
 * @see             java.awt.BorderLayout
 * @see             java.awt.Dimension
 * @see             java.awt.Font
 * @see             java.awt.Toolkit
 * @see             javax.swing.BorderFactory
 * @see             javax.swing.ImageIcon
 * @see             javax.swing.JLabel
 * @see             javax.swing.JPanel
 * @see             javax.swing.JWindow
 */

/**
 * <pre>
 * Splash
 * </pre>
 * Public class Splash is the main method of Splash Class and extends JWindow.
 * Provides class level variables and methods.
 * @author Chris
 */
public class Splash extends JWindow
{
 // Declaration of class level variables.
 private int duration;  // how long the splash screen will show in milliseconds
  
  /**
   * <pre>
   * Splash
   * </pre>
   * Main constructor for the Splash class.  Length of time measured in 
   * milliseconds is passed in from PugetSoundEnergyGUI and stored in duration
   * variable
   * @param dur 
   * @author: Chris Steigerwald
   */
  public Splash(int dur)
  {
      duration = dur;
  }
  
  /**
   * <pre>
   * showSplash
   * </pre>
   * Creates splash screen frame that contains company logo and copyright.
   * @see             java.awt.Color
   * @see             java.awt.Dimension
   * @see             java.awt.BorderLayout
   * @see             java.awt.Dimension
   * @see             java.awt.Font
   * @see             java.awt.Toolkit
   * @see             javax.swing.BorderFactory
   * @see             javax.swing.ImageIcon
   * @see             javax.swing.JLabel
   * @see             javax.swing.JPanel
   * @see             javax.swing.JWindow
   * @author: Chris Steigerwald
   */  
  public void showSplash()
  {
      Color myLightBlue = new Color(0,153,255); // Custom color of blue
      JPanel content = (JPanel)getContentPane(); 
      content.setBackground(new java.awt.Color(0,153,255));
      
      // Set the window's bounds, centering the window
      int width = 300;
      int height = 200;
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (screen.width-width)/2;
      int y = (screen.height-height)/2;
      setBounds(x,y,width,height);
      
      // Build the splash screen
      // Crete labels for splashscreen
      
      //JLabel label1 = new JLabel(new ImageIcon("src/Resources/loading.gif"));
      JLabel label = new JLabel(new ImageIcon("src/Resources/printerLogo.png"));
      JLabel copyrt = new JLabel ("Copyright 2013, Printer Queue Simulation", JLabel.CENTER);
      copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
      copyrt.setForeground(Color.WHITE);
      content.add(label, BorderLayout.CENTER); // add to JPanel
      //content.add(label1, BorderLayout.CENTER); // add to JPanel
      content.add(copyrt, BorderLayout.SOUTH);
      Color border = new Color(50, 20, 20, 55);
      content.setBorder(BorderFactory.createLineBorder(border, 10));
      
      //Display it
      setVisible(true);
      
      // Wait a predetermind amount of time determined by PugetSoundEnergyGUI
      try
      {
          Thread.sleep(duration);
      }
      catch (Exception e)
      {
          e.printStackTrace();
      }
      
      setVisible(false);  
  }   
}