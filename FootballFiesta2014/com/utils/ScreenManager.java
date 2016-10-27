/*
 * ScreenManager.java
 *
 * © <your company here>, 2003-2008
 * Confidential and proprietary.
 */

package com.utils;


import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.container.MainScreen;

/**
 * 
 */
public class ScreenManager
{
    
    public static final int numOfScreen = 20;
    
    public static final int HOME_SCREEN = 0;
    public static final int TEAM_SCREEN = 1;
    public static final int STADIUM_SCREEN = 2;
    public static final int STADIUM_DETAILS_SCREEN = 3;
    public static final int CITY_SCREEN = 4;
    public static final int CITY_DETAILS_SCREEN = 5;
    public static final int HISTORY_SCREEN = 6;
    public static final int HISTORY_DETAILS_SCREEN = 7;
    public static final int TEAM_DETAILS_SCREEN = 8;
    public static final int COUNTDOWN_SCREEN = 9;
    public static final int MATCH_SCREEN = 10;
    public static final int POINT_TABLE_SCREEN = 11;
    public static final int TICKET_SCREEN = 12;
    public static final int MATCH_DETAILS_SCREEN = 13;
    public static final int SQUAD_SCREEN = 14;
    public static final int SQUAD_DETAILS_SCREEN = 15;
    public static final int PLAYER_DETAILS_SCREEN = 16;
    public static final int MATCH_TODAY_SCREEN = 17;
    public static final int SCORER_SCREEN = 18;
    public static final int TOURNAMENT_SUMMARY_SCREEN = 19;
    
    
    public MainScreen[] mainScreenArray;
    public CustomStack[] previousScreenArray;
    public static ScreenManager screenManager;
    
    public int currrentScreenIndex; 
    
    public String previousScreenNameForTitle;
    
    public ScreenManager()
    {
        mainScreenArray = new MainScreen[numOfScreen];
        
        previousScreenArray = new CustomStack[numOfScreen];
        for( int index = 0; index < numOfScreen; index++)
        {
            previousScreenArray[index] = new CustomStack(5);
        }
    }
    
    
    public MainScreen getScreen(int index)
    {
        return mainScreenArray[index];        
    }
    
    /**
     * Returns the only instance of this class.
     * 
     * @return Instance of an ScreenManager
     */
    synchronized public static ScreenManager getInstance()
    {
        if (screenManager == null)
        {
            screenManager = new ScreenManager();
        }        
        return screenManager;
    }
    
    
    public void clear()
    {
        mainScreenArray = null;
        previousScreenArray = null;
        previousScreenNameForTitle = null;
    }
   
} 

