/*
 * Squad.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.utils;


import java.util.Vector;
import net.rim.device.api.util.Persistable;

/**
 *@author Bikas
 *@date  05/16/2014
 */
public class Squad  implements Persistable
{
    public String team_id;
    public String team_name;
    public String coach;
    public Vector playerVector = new Vector();
    public Squad() 
    {
        team_id = "";
        team_name = "";
        coach = "";
        playerVector = new Vector();
    }
} 
