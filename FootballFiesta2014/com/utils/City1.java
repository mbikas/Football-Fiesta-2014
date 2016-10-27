/*
 * City1.java
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
public class City1 implements Persistable 
{
    public String id;
    public String name;
    public String imageName;
    public String imageNameBig;
    public String stadium;
    public Vector clubVector = new Vector();
    public String history;
    public String weather;
    
     
    public City1() {    }
} 
