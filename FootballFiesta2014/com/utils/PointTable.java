/*
 * PointTable.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.utils;

import net.rim.device.api.util.Persistable;

/**
 * 
 */
public class PointTable implements Persistable
{
    public String groupName;
    public String teamId = "";
    public String teamName = "";
    public int MP = 0;
    public int W = 0;
    public int D = 0;
    public int L = 0;
    public int GF = 0;
    public int GA = 0;
    public int Pts = 0;
    
    public PointTable() { }
} 
