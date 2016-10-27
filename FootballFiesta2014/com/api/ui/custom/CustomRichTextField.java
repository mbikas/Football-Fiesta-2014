/*
 * CustomRichTextField.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.api.ui.custom;


import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;




/**
 *@author Bikas 
 */
public class CustomRichTextField extends RichTextField
{
    int textColor;
    
    public CustomRichTextField(String text, int textColor) 
    {    
        this( text, textColor, RichTextField.NON_FOCUSABLE);
    }
    public CustomRichTextField(String text, int textColor, long style) 
    {    
        super(text, style);
        this.textColor = textColor;
       // Background background = BackgroundFactory.createSolidTransparentBackground(Color.
            //                  .createSolidBackground(bgColor);
        //       setBackground(background);
    }
    
    public void paint(Graphics g)
    {
        //int oldColor = g.getColor();
        //g.clear();
        g.setColor(textColor);
        super.paint(g);
        //g.setColor(oldColor);
    }
} 
