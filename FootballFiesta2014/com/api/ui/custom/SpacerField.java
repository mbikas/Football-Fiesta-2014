/*
 * SpacerField.java
 * @ author Bikas
 */

package com.api.ui.custom;

import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import java.util.*;


/**
 * SpaceField for both horizontal and vertical
 * 
 */
public class SpacerField extends HorizontalFieldManager
{
    private int width;
    private int height;
    //default white
    private int backgroundColor = 0x00ffffff;
    
    public SpacerField(int width, int height) 
    {
        this.width = width;
        this.height = height;            
    }
    
    public SpacerField(int width, int height, int backgroundColor) 
    {
        this.width = width;
        this.height = height;            
        this.backgroundColor = backgroundColor;
    }
    
    protected void sublayout(int maxWidth, int maxHeight)
    {
        super.sublayout(getPreferredWidth(), getPreferredHeight());
        setExtent(getPreferredWidth(), getPreferredHeight()); 
    }    
    public void paint(Graphics graphics)
    {
        graphics.setBackgroundColor(this.backgroundColor);
        graphics.clear();
        super.paint(graphics);
    }
    public int getPreferredWidth()
    {
        return width;
    }
    public int getPreferredHeight()
    {
        return height;
    }
} 
