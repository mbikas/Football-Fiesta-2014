/*
 * MatchDetails.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.utils;
import net.rim.device.api.system.Bitmap;

/**
 * 
 */
public class MatchDetails 
{
    public static final int TEAM_LEFT = 0;
    public static final int TEAM_RIGHT = 1;
    public int minute;
    public String playerName;
    public String goalText;
    public Bitmap image;
    public boolean isGoal;
    public int teamside;
    public MatchDetails() {    }
} 
