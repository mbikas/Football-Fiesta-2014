/*
 * Team.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.utils;

import java.util.Vector;
import net.rim.device.api.util.Persistable;

/**
 *@author Bikas
 *@date  01/01/2014
 */
public class Team1 implements Persistable
{
    public String id;
    public String name;
    public String nickName;
    public String region;
    public String flagImageName;
    public String flagImageNameSmall;
    public int rank;
    public String coach;
    public String captain;
    public int appearances;
    public String bestResults;
    public Vector keyPlayers = new Vector();
    //public String wikiLink;
     
    public Team1() {    }
} 
