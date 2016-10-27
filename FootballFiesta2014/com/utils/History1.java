/*
 * History1.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.utils;

import java.util.Vector;
import net.rim.device.api.util.Persistable;

/**
 *@author Bikas
 *@date  05/01/2014
 */
public class History1 implements Persistable
{
    public String year;
    public String host;
    public String dates;
    public String teams;
    public String champion;
    public String runnerUp;
    public String third;
    public String fourth;
    public Vector bootVector = new Vector();
    public String goldenBall;
    public String goalkeeper;
    public String youngPlayer;
    public String fairPlay;
    public String finalResult;
        
     
    public History1() {    }
} 
