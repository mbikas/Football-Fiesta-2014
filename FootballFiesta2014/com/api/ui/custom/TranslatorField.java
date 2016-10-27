/*
 * TranslatorField.java
 *
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
 
/**
 * author Bikas
 */
public class TranslatorField extends HorizontalFieldManager
{
    private int width;
    private int height;
    
    public TranslatorField(int width, int height) 
    {
        this.width = width;
        this.height = height;            
    }
    
    protected void sublayout(int maxWidth, int maxHeight)
    {
        super.sublayout(getPreferredWidth(), getPreferredHeight());
        setExtent(getPreferredWidth(), getPreferredHeight()); 
    }    
    public void paint(Graphics graphics)
    {
        graphics.translate(getPreferredWidth(), getPreferredHeight()); 
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
