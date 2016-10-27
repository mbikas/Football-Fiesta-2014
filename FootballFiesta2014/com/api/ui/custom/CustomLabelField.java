/*
 * CustomLabelField.java
 */

package com.api.ui.custom;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.DrawStyle;

/**
 * @author bikas
 * @date    03.02.09
 */
public class CustomLabelField extends LabelField
{
    
    private String labelText;
    
    private int topMargin = 12;
    private int rightMargin = 0;
    private int bottomMargin = 3;
    private int leftMargin = 12;  
    
    private static int DEFAULT_FONT_COLOR = 0x0000FF;  
    private static int DEFAULT_FONT_SIZE = 6;
    private int fontColor = DEFAULT_FONT_COLOR;
    private int fontSize = DEFAULT_FONT_SIZE;
    
    private long style =  Field.USE_ALL_WIDTH;
    
    public CustomLabelField( String labelText) 
    {    
        super( labelText);
        //FontFamily fontfam[]=FontFamily.getFontFamilies();
        //Font font= fontfam[0].getFont(FontFamily.SCALABLE_FONT,fontSize);
        Font font = Font.getDefault();
        font = font.derive(font.getStyle(), fontSize);
        
        this.labelText = labelText;
        setFont( font);
     }
     
    public CustomLabelField( String labelText, long style) 
    {    
        super( labelText, style);
        //FontFamily fontfam[]=FontFamily.getFontFamilies();
        //Font font= fontfam[0].getFont(FontFamily.SCALABLE_FONT,fontSize);
        Font font = Font.getDefault();
        font = font.derive(font.getStyle(), fontSize);
        
        this.labelText = labelText;
        setFont( font);
     }
    
    public CustomLabelField( String labelText, int fontSize) 
    {    
        super(labelText);
        this.labelText = labelText;
        this.fontSize = fontSize;        
        //FontFamily fontfam[]=FontFamily.getFontFamilies();
        //Font font= fontfam[0].getFont(FontFamily.SCALABLE_FONT,fontSize);
        Font font = Font.getDefault();
        font = font.derive(font.getStyle(), fontSize);
        
        setFont(font);     
    }
    
    public CustomLabelField( String labelText, int fontSize, int fontColor) 
    {    
        super(labelText);
        this.labelText = labelText;
        this.fontSize = fontSize;   
        this.fontColor = fontColor;
             
        //FontFamily fontfam[]=FontFamily.getFontFamilies();
        //Font font= fontfam[0].getFont(FontFamily.SCALABLE_FONT,fontSize);
        Font font = Font.getDefault();
        font = font.derive(font.getStyle(), fontSize);
        
        setFont(font);     
    }
    
    public CustomLabelField( String labelText, int fontSize, int fontColor, long style) 
    {    
        super(labelText, style);
        this.labelText = labelText;
        this.fontSize = fontSize;   
        this.fontColor = fontColor;
             
        //FontFamily fontfam[]=FontFamily.getFontFamilies();
        //Font font= fontfam[0].getFont(FontFamily.SCALABLE_FONT,fontSize);
        Font font = Font.getDefault();
        font = font.derive(font.getStyle(), fontSize);
        
        setFont(font);     
    }
    
    /*
    public void setLabelMargin(int top,int right, int bottom, int left)
    {   
        topMargin = top;
        rightMargin = right;
        bottomMargin = bottom;
        leftMargin= left;
        this.setMargin(topMargin, rightMargin, bottomMargin, leftMargin);        
    }
   */
   
    
    public void setFontSize(int fontSize)
    {
        this.fontSize = fontSize;
        //FontFamily fontfam[]=FontFamily.getFontFamilies();
        //Font font= fontfam[0].getFont(FontFamily.SCALABLE_FONT,fontSize);
        Font font = Font.getDefault();
        font = font.derive(font.getStyle(), fontSize);
        setFont(font); 
    }  
    
     
    public void setFontColor(int fontColor)
    {
        this.fontColor = fontColor;        
    } 
    
    protected void paint(Graphics graphics)
    {   
       graphics.setColor( fontColor);
       super.paint(graphics);      
    }
} 

