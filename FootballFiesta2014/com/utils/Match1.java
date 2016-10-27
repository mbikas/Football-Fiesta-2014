/*
 * Match1.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.utils;

import java.util.Vector;
import net.rim.device.api.util.Persistable;

/**
 *@author Bikas
 *@date  01/0129/2014
 */
public class Match1 implements Persistable
{
    public int id;
    public String date;
    public String time;
    public String stadium;
    //public String team1;
    public String team1Id;
    public String team1Name;
    public String flag1;
    //public String team2;
    public String team2Id;
    public String team2Name;
    public String flag2;
    public String group;
    public String title;
    public int team1_score;
    public int team2_score;
    public String extra;
    public Match1() {    }
} 
