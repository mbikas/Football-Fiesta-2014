/*
 * CustomButtonField.java
 * Confidential and proprietary.
 */
 
package com.api.ui.custom;

import net.rim.device.api.ui.Field;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Font;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.XYPoint;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Ui;
import java.util.Vector;
import com.utils.Utils;

/**
 * This class represents a button. 
 * 
 * Every functionality of BlackBerry API provided button ButtonField may not be
 * supported here. The distinctive feature of this button is that it can 
 * contain an icon in addition to the text label.
 * 
 * @author  Bikas
 */
public final class CustomButtonField extends Field
{
    /**
     * Text label of the button
     */
    private String label = "";
    
    /**
     * Length of label text in terms of pixels according to the current font
     */
    private int labelLength;
    
    /**
     * Vector of strings extracted from the label of button.
     * Each string is on its own line in the button.
     */
    private Vector vecLabelStr = new Vector();
    
    /**
     * Margin in pixels between the lines of a multi-line label.
     */
    private static final int MARGIN_BETWEEN_LABEL_LINES = 0;
    
    /**
     * Minimum height of label font
     */
    public static final int MIN_FONT_HEIGHT = 10;
     
    /**
     * Default height of label font
     */
    public static final int DEFAULT_FONT_HEIGHT = Utils.BUTTON_DEFAULT_SIZE;
    
    /**
     * The height of the font
     */
    private int fontHeight = DEFAULT_FONT_HEIGHT;
    
    /**
     * Default font colors
     */
    //public static final int DEFAULT_FONT_COLOR_NORMAL = 0x000000FF;
    public static final int DEFAULT_FONT_COLOR_NORMAL = 0x00FFFFFF;
    public static final int DEFAULT_FONT_COLOR_ON_FOCUS = 0x00FFFFFF;
    
    /**
     * Label font color used when button is not focused.
     */
    private int fontColorNormal = DEFAULT_FONT_COLOR_NORMAL;
    
    /**
     * Label font color used when button is focused.
     */
    private int fontColorOnFocus = DEFAULT_FONT_COLOR_ON_FOCUS;
    
    /*
     * EncodedImage is needed in order to resize the image and this resized 
     * image is drawn many times. Deriving Bitmap from EncodedImage is largely 
     * expensive. The problem is compounded by the fact that we're forced to 
     * derive this Bitmap within the paint() method and it is recommended that 
     * paint() method should not entail much calculation since it is called 
     * frequently to draw the component. Whenever the button width or height is 
     * reset, the image to be drawn must be resized accordingly.
     * 
     * So both EncodedImage and Bitmap are required for background images. 
     * Bitmap image derived from the Encoded image is stored and whenever width 
     * or height is changed, new Bitmap image is derived from the Encoded 
     * image. Thus overhead to crucial paint() method is lessened.
     */
    
    
    /**
     * Icon of the button that is used when the button is not focused.
     */
    private EncodedImage iconNormal;
    private Bitmap bitmapIconNormal;
    
    /**
     * Icon of the button that is used when the button is focused.
     */
    private EncodedImage iconOnFocus;
    private Bitmap bitmapIconOnFocus;
    
    /**
     * Alignment
     */
     public final static int ALIGNMENT_LEFT = 0x00000001;
     public final static int ALIGNMENT_RIGHT = 0x00000002;
     public final static int ALIGNMENT_TOP = 0x00000004;
     public final static int ALIGNMENT_BOTTOM = 0x00000008;
     public final static int ALIGNMENT_HORIZONTAL_CENTER = 0x00000010;
     public final static int ALIGNMENT_VERTICAL_CENTER = 0x00000020;
     
    /**
     * Default alignment for the button
     */
    public final static int DEFAULT_ALIGNMENT = ALIGNMENT_LEFT | ALIGNMENT_VERTICAL_CENTER;
     
    /**
     * Indicates the alignment of icon and label
     */
    private int alignment = DEFAULT_ALIGNMENT;
    
    /**
     * Top-left point of the label text in local coordinates within the button.
     * Whenever alignment is changed, this point is adjusted accordingly.
     */
    private XYPoint labelTopLeftPoint = new XYPoint();
    
    /**
     * Top-left point of the icon in local coordinates within the button.
     * Whenever alignment is changed, this point is adjusted accordingly.
     */
    private XYPoint iconTopLeftPoint = new XYPoint();
    
    /**
     * Safe margin between icon and text label of the button
     */
    public final static int MIN_ICON_TEXT_GAP = 5;
    
    /**
     * Distance in pixels between icon and label text.
     */
    public int iconTextGap = MIN_ICON_TEXT_GAP;
    
    /**
     * Position of label text relative to icon.
     */
    private int horizontalLabelTextPosition = ALIGNMENT_RIGHT;
    
    /**
     * Background image when a button is not focused.
     */
    private EncodedImage backgroundImageNormal;
    private Bitmap bitmapBackgroundImageNormal;
    
    /**
     * Background image when a button is focused.
     */
    private EncodedImage backgroundImageOnFocus;
    private Bitmap bitmapBackgroundImageOnFocus;
    
    /**
     * Default background colors used when background images are unavailable.
     */
    //public final static int DEFAULT_BACKGROUND_COLOR_NORMAL = 0x00D8D8D8;
    //public final static int DEFAULT_BACKGROUND_COLOR_ON_FOCUS = 0x00007EC1;
    //public final static int DEFAULT_BACKGROUND_COLOR_NORMAL = 0x00002C43;
    //public final static int DEFAULT_BACKGROUND_COLOR_ON_FOCUS = 0x00007EC1;
    public final static int DEFAULT_BACKGROUND_COLOR_NORMAL = 0x00663333;//0x00002C43;
    public final static int DEFAULT_BACKGROUND_COLOR_ON_FOCUS = 0x00FF3333;//0x00007EC1;
    
    
    /**
     * Background color used when background images are unavailable.
     */
    private int backgroundColorNormal = DEFAULT_BACKGROUND_COLOR_NORMAL;
    private int backgroundColorOnFocus = DEFAULT_BACKGROUND_COLOR_ON_FOCUS;
    
    /**
     * Width of the button
     */
    private int width;
    
    /**
     * Height of the button
     */
    private int height;
    
    private int gapBetween = 0;
    private int intialGapMinus = 0;
    
    
    
    /**
     * Default margin for the button
     */
    public final static int DEFAULT_LEFT_MARGIN =  2;
    public final static int DEFAULT_RIGHT_MARGIN = 2;
    public final static int DEFAULT_TOP_MARGIN = 0;//2;
    public final static int DEFAULT_BOTTOM_MARGIN = 0;//2;
    
    /**
     * Default padding for the button
     */
    public final static int DEFAULT_LEFT_PADDING = 5;
    public final static int DEFAULT_RIGHT_PADDING = 5;
    public final static int DEFAULT_TOP_PADDING = 2;
    public final static int DEFAULT_BOTTOM_PADDING = 2;
    
    /**
     * Margins around the text box
     */
    private int leftMargin = DEFAULT_LEFT_MARGIN;
    private int rightMargin = DEFAULT_RIGHT_MARGIN;
    private int topMargin = DEFAULT_TOP_MARGIN;
    private int bottomMargin = DEFAULT_BOTTOM_MARGIN;
    
    /**
     * Padding around the text box
     */
    private int leftPadding = DEFAULT_LEFT_PADDING;
    private int rightPadding = DEFAULT_RIGHT_PADDING;
    private int topPadding = DEFAULT_TOP_PADDING;
    private int bottomPadding = DEFAULT_BOTTOM_PADDING;
    
    /**
     * Indicates whether the button will be wide to full screen or compact with the contents
     */
    private boolean isFullScreenWide = true;
    
    /**
     * Minimum width of button. If attempt to make the button narrower than this value is made, width is set to this value.
     */
    public static final int MIN_BUTTON_WIDTH = 10;
    
    /**
     * Maximum width of button. If attempt to make the button wider than this value is made, width is set to this value.
     */
    public static final int MAX_BUTTON_WIDTH = Display.getWidth();
    
    /**
     * Minimum height of button. If attempt to make the button shorter than this value is made, height is set to this value.
     */
    public static final int MIN_BUTTON_HEIGHT = 20;
    
    /**
     * Maximum height of button. If attempt to make the button taller than this value is made, height is set to this value.
     */
    public static final int MAX_BUTTON_HEIGHT = Display.getHeight();
    
    
    /**
     * Indicates whether the button can be focused or not.
     */
    private boolean isFocusable = true;
    
    /**
     * Indicates border color of the button
     */
    private int borderColorNormal = 0x007D8589; 
    private int borderColorFocus = 0x00FFFFFF; 
    
    
    
    
    /**
     * Constants used in parsing
     */
    private final static char SPACE = ' ';
    private final static char NEW_LINE = '\n';
    private final static char COMMA = ',';

    /**
     * Constructor with only button label.
     * 
     * @param label Text label of the button
     */
    public CustomButtonField(final String label)
    {
        this(label, false);
    }
    
    /**
     * Constructor with button label and indication to width.
     * 
     * @param label Text label of the button
     * @param isFullScreen Indicates whether the button will be wide to display screen
     */
    public CustomButtonField(final String label, boolean isFullScreenWide)
    {
        // Let the super class handle its own chores.
        super();
        
        // Default label is empty string.
        if (label != null)
        {
            this.label = label;
        }
        
        this.isFullScreenWide = isFullScreenWide;
        
        updateFontHeight(this.fontHeight, getDefaultFont());
        
        //parseLabel();
        
        // Set width and height of the button
        int width = (isFullScreenWide == true) ? Display.getWidth() : (leftMargin + leftPadding + labelLength + rightPadding + rightMargin);
        setWidth(width);
        
        
        
        int numOfLinesInLabel = this.vecLabelStr.size();
        int labelHeight = numOfLinesInLabel * this.fontHeight + (numOfLinesInLabel - 1) * MARGIN_BETWEEN_LABEL_LINES;
        int height = topMargin + topPadding + labelHeight + bottomPadding + bottomMargin;
        setHeight(height);
    }
    
    
    /**
     * Constructor with button label and indication to width.
     * 
     * @param label Text label of the button
     * @param isFullScreen Indicates whether the button will be wide to display screen
     */
    public CustomButtonField(final String label, Font font, boolean isFullScreenWide)
    {
        // Let the super class handle its own chores.
        super();
        
        // Default label is empty string.
        if (label != null)
        {
            this.label = label;
        }
        
        this.isFullScreenWide = isFullScreenWide;
        
        updateFontHeight(font.getHeight(), font);
        
        //parseLabel();
        
        // Set width and height of the button
        int width = (isFullScreenWide == true) ? Display.getWidth() : (leftMargin + leftPadding + labelLength + rightPadding + rightMargin);
        setWidth(width);
        
        
        
        int numOfLinesInLabel = this.vecLabelStr.size();
        int labelHeight = numOfLinesInLabel * this.fontHeight + (numOfLinesInLabel - 1) * MARGIN_BETWEEN_LABEL_LINES;
        int height = topMargin + topPadding + labelHeight + bottomPadding + bottomMargin;
        setHeight(height);
    }
    
    
    /**
     * Constructor with icon and text label
     * 
     * @param icon      Icon of the button
     * @param label     Text label of the button
     */
    public CustomButtonField(final EncodedImage iconNormal, final String label, boolean isFullScreenWide)
    {
        super();
        
        if (iconNormal != null)
        {
            this.iconNormal = iconNormal;
            this.bitmapIconNormal = iconNormal.getBitmap();
            
            // Icon on focus is not available now. So display the normal icon when focused.
            this.iconOnFocus = iconNormal;
            this.bitmapIconOnFocus = this.bitmapIconNormal;
        }
        
        // Default label is empty string.
        if (label != null)
        {
            this.label = label;
        }
        
        this.isFullScreenWide = isFullScreenWide;
        
        updateFontHeight(this.fontHeight, getDefaultFont());
                
        int newWidth = (isFullScreenWide == true) ? Display.getWidth() : (leftMargin + leftPadding + labelLength + rightPadding + rightMargin);
        if (isFullScreenWide == false && this.iconNormal != null)
        {
            newWidth += this.bitmapIconNormal.getWidth() + iconTextGap;
        }
        setWidth(newWidth);
        
        // Since font height and button width have been set, label is also 
        // parsed in setWidth(). Now determine how many lines the label 
        // requires to be displayed with the width and font height. Then 
        // calculate the required height.
        int numOfLinesInLabel = this.vecLabelStr.size();
        int labelHeight = numOfLinesInLabel * this.fontHeight + (numOfLinesInLabel - 1) * MARGIN_BETWEEN_LABEL_LINES;
        int height = topMargin + topPadding + Math.max(labelHeight, this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getHeight()) + bottomPadding + bottomMargin;
        setHeight(height);
    }
    
    /**
     * Constructor with icon and text label
     * 
     * @param icon      Icon of the button
     * @param label     Text label of the button
     */
    public CustomButtonField(final EncodedImage iconNormal, final String label, boolean isFullScreenWide, Font font)
    {
        super();
        
        if (iconNormal != null)
        {
            this.iconNormal = iconNormal;
            this.bitmapIconNormal = iconNormal.getBitmap();
            
            // Icon on focus is not available now. So display the normal icon when focused.
            this.iconOnFocus = iconNormal;
            this.bitmapIconOnFocus = this.bitmapIconNormal;
        }
        
        // Default label is empty string.
        if (label != null)
        {
            this.label = label;
        }
        
        this.isFullScreenWide = isFullScreenWide;
        this.fontHeight = font.getHeight();
        updateFontHeight(this.fontHeight, font);
                
        int newWidth = (isFullScreenWide == true) ? Display.getWidth() : (leftMargin + leftPadding + labelLength + rightPadding + rightMargin);
        if (isFullScreenWide == false && this.iconNormal != null)
        {
            newWidth += this.bitmapIconNormal.getWidth() + iconTextGap;
        }
        setWidth(newWidth);
        
        // Since font height and button width have been set, label is also 
        // parsed in setWidth(). Now determine how many lines the label 
        // requires to be displayed with the width and font height. Then 
        // calculate the required height.
        int numOfLinesInLabel = this.vecLabelStr.size();
        int labelHeight = numOfLinesInLabel * this.fontHeight + (numOfLinesInLabel - 1) * MARGIN_BETWEEN_LABEL_LINES;
        int height = topMargin + topPadding + Math.max(labelHeight, this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getHeight()) + bottomPadding + bottomMargin;
        setHeight(height);
    }
    
    /**
     * Constructor with icon and text label
     * 
     * @param icon      Icon of the button
     * @param label     Text label of the button
     */
    public CustomButtonField(final Bitmap iconNormal, final String label, boolean isFullScreenWide, Font font)
    {
        super();
        
        if (iconNormal != null)
        {
            //this.iconNormal = null;
            this.bitmapIconNormal = iconNormal;
            
            // Icon on focus is not available now. So display the normal icon when focused.
            this.bitmapIconOnFocus = this.bitmapIconNormal;
        }
        
        // Default label is empty string.
        if (label != null)
        {
            this.label = label;
        }
        
        this.isFullScreenWide = isFullScreenWide;
        this.fontHeight = font.getHeight();
        updateFontHeight(this.fontHeight, font);
                
        int newWidth = (isFullScreenWide == true) ? Display.getWidth() : (leftMargin + leftPadding + labelLength + rightPadding + rightMargin);
        if (isFullScreenWide == false && this.iconNormal != null)
        {
            newWidth += this.bitmapIconNormal.getWidth() + iconTextGap;
        }
        setWidth(newWidth);
        
        // Since font height and button width have been set, label is also 
        // parsed in setWidth(). Now determine how many lines the label 
        // requires to be displayed with the width and font height. Then 
        // calculate the required height.
        int numOfLinesInLabel = this.vecLabelStr.size();
        int labelHeight = numOfLinesInLabel * this.fontHeight + (numOfLinesInLabel - 1) * MARGIN_BETWEEN_LABEL_LINES;
        int height = topMargin + topPadding + Math.max(labelHeight, this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getHeight()) + bottomPadding + bottomMargin;
        setHeight(height);
    }
    
    /**
     * Constructor with icon and text label
     * 
     * @param icon      Icon of the button
     * @param label     Text label of the button
     */
    public CustomButtonField(final Bitmap iconNormal, final String label, boolean isFullScreenWide)
    {
        super();
        
        if (iconNormal != null)
        {
            this.bitmapIconNormal = iconNormal;
            
            // Icon on focus is not available now. So display the normal icon when focused.
            this.bitmapIconOnFocus = this.bitmapIconNormal;
        }
        
        // Default label is empty string.
        if (label != null)
        {
            this.label = label;
        }
        
        this.isFullScreenWide = isFullScreenWide;
        
        updateFontHeight(this.fontHeight, getDefaultFont());
                
        int newWidth = (isFullScreenWide == true) ? Display.getWidth() : (leftMargin + leftPadding + labelLength + rightPadding + rightMargin);
        if (isFullScreenWide == false && this.iconNormal != null)
        {
            newWidth += this.bitmapIconNormal.getWidth() + iconTextGap;
        }
        setWidth(newWidth);
        
        // Since font height and button width have been set, label is also 
        // parsed in setWidth(). Now determine how many lines the label 
        // requires to be displayed with the width and font height. Then 
        // calculate the required height.
        int numOfLinesInLabel = this.vecLabelStr.size();
        int labelHeight = numOfLinesInLabel * this.fontHeight + (numOfLinesInLabel - 1) * MARGIN_BETWEEN_LABEL_LINES;
        int height = topMargin + topPadding + Math.max(labelHeight, this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getHeight()) + bottomPadding + bottomMargin;
        setHeight(height);
    }
    
    /**
     * Constructor with background images and label of the button.
     * 
     * @param backgroundImageNormal     Background image when a button is not focused.
     * @param backgroundImageOnFocus    Background image when a button is focused.
     * @param label                     Text label of the button
     */
    public CustomButtonField(final EncodedImage backgroundImageNormal, final EncodedImage backgroundImageOnFocus, final String label)
    {
        this(backgroundImageNormal, backgroundImageOnFocus, null, label, false);
    }
    
    public CustomButtonField(final EncodedImage backgroundImageNormal, final EncodedImage backgroundImageOnFocus, final String label, Font font)
    {
        this(backgroundImageNormal, backgroundImageOnFocus, null, label, false, font);
    }
    
    
    public CustomButtonField(final int backgroundColorNormal, final int backgroundImageOnFocus, final String label, final boolean isFullScreenWide, Font font)
    {
        this(label, font, isFullScreenWide);
        this.backgroundColorNormal = backgroundColorNormal;
        this.backgroundColorOnFocus = backgroundImageOnFocus;
    }
    
    
    public CustomButtonField(final int backgroundColorNormal, final int backgroundImageOnFocus, final String label, final boolean isFullScreenWide)
    {
        this(label, isFullScreenWide);
        this.backgroundColorNormal = backgroundColorNormal;
        this.backgroundColorOnFocus = backgroundImageOnFocus;
    }
    
    
    /**
     * Constructor with background images, label and indication to button width.
     * 
     * @param backgroundImageNormal     Background image when a button is not focused.
     * @param backgroundImageOnFocus    Background image when a button is focused.
     * @param label                     Text label of the button
     * @param isFullScreenWide          Indicates whether the button will be wide to display screen
     */
    public CustomButtonField(final EncodedImage backgroundImageNormal, final EncodedImage backgroundImageOnFocus, final String label, boolean isFullScreenWide)
    {
        this(backgroundImageNormal, backgroundImageOnFocus, null, label, isFullScreenWide);
    }
    
    /**
     * Preferred constructor with all necessary parameters
     * 
     * @param backgroundImageNormal     Background image when a button is not focused.
     * @param backgroundImageOnFocus    Background image when a button is focused.
     * @param iconNormal                      Icon of the button
     * @param label                     Text label of the button
     * @param isFullScreenWide          Indicates whether the button will be wide to display screen
     */
    public CustomButtonField(final EncodedImage backgroundImageNormal, final EncodedImage backgroundImageOnFocus, 
                                final EncodedImage iconNormal, final String label, boolean isFullScreenWide)
    {
        // Let the other constructor handle icon and label
        this(iconNormal, label, isFullScreenWide);
        
        // Set background images
        this.backgroundImageNormal = backgroundImageNormal;
        this.backgroundImageOnFocus = backgroundImageOnFocus;
        
        // The background images must comply with the available width and height.
        adjustBackgroundImagesToNewSize();
    }
    
    public CustomButtonField(final EncodedImage backgroundImageNormal, final EncodedImage backgroundImageOnFocus, 
            final EncodedImage iconNormal, final String label, boolean isFullScreenWide, Font font)
        {
                // Let the other constructor handle icon and label
                this(iconNormal, label, isFullScreenWide, font);
                
                // Set background images
                this.backgroundImageNormal = backgroundImageNormal;
                this.backgroundImageOnFocus = backgroundImageOnFocus;
                
                // The background images must comply with the available width and height.
                adjustBackgroundImagesToNewSize();
        }
    
    /**
     * Preferred constructor with all necessary parameters
     * 
     * @param backgroundImageNormal     Background image when a button is not focused.
     * @param backgroundImageOnFocus    Background image when a button is focused.
     * @param iconNormal                      Icon of the button
     * @param label                     Text label of the button
     */
    public CustomButtonField(final EncodedImage backgroundImageNormal, final EncodedImage backgroundImageOnFocus, 
                                final EncodedImage iconNormal, final String label)
    {
        // Let the other constructor handle icon and label
        this(backgroundImageNormal, backgroundImageOnFocus, iconNormal, label, false);
    }
    
        
    /**
     * Makes the specified width valid by expanding or truncating to minimum or 
     * maximum button width respectively. If the specified width is valid, then 
     * it is returned as it is.
     * 
     * @param width Width of the button
     * @return width A valid width.
     */
    private int validateButtonWidth(int width)
    {
        if (width < MIN_BUTTON_WIDTH)
        {
          width = MIN_BUTTON_WIDTH;
        }
        else if (width > MAX_BUTTON_WIDTH)
        {
            width = MAX_BUTTON_WIDTH;
        }
        
        return width;
    }
    
    /**
     * Makes the specified height valid by expanding or truncating to minimum or 
     * maximum button height respectively. If the specified height is valid, then 
     * it is returned as it is.
     * 
     * @param height Height of the button
     * @return height A valid height
     */
    private int validateButtonHeight(int height)
    {
        if (height < MIN_BUTTON_HEIGHT)
        {
          height = MIN_BUTTON_HEIGHT;
        }
        else if (height > MAX_BUTTON_HEIGHT)
        {
            height = MAX_BUTTON_HEIGHT;
        }
        
        return height;
    }
    
    /**
     * Parses the label and populates the vector that contains the strings 
     * extracted from the label. This method computes the number of lines the 
     * label requires to be displayed.
     * 
     * Before calling this method, be sure that the label, label font height, 
     * label length in pixels i.e. labelLength and button width have been set 
     * already.
     */
    private void parseLabel()
    {
        Font font = getFont();
        
        if (this.labelLength == 0) // for the sake of caution :)
        {
            this.labelLength = font.getAdvance(this.label);
        }
        
        if (this.width == 0) // for the sake of caution :)
        {
            int newWidth = (isFullScreenWide == true) ? Display.getWidth() : (leftMargin + leftPadding + labelLength + rightPadding + rightMargin);
            if (isFullScreenWide == false && this.iconNormal != null)
            {
                newWidth += this.iconNormal.getWidth() + iconTextGap;
            }
            this.width = validateButtonWidth(newWidth);
        }
        
        // Available width to draw the label
        int availableWidth = this.width - (this.leftMargin + this.leftPadding + (this.iconNormal == null ? 0 : iconNormal.getWidth() + iconTextGap) + this.rightPadding + this.rightMargin);
        
        this.vecLabelStr.removeAllElements();
        int length = this.label.length();
        
        // Start and end index of label string. Marks the current line.
        int startIndex = 0;
        int endIndex = 0;
        char ch = 0;
        String str;
        
        // Check if available width can accommodate at least every character in the label. Otherwise we fall in an infinite loop
        /*
        for (int i = length-1; i >= 0; --i)
        {
            ch = this.label.charAt(i);
            if (font.getAdvance(ch) > availableWidth)
            {
                return;
            }
        }
        */
        
        while (endIndex < length)
        {
            // Extract a line
            int numOfWords = 0;
            while (endIndex < length)
            {
                // Get the next whole word
                int numOfCharsInWord = 0;
                for (int i = endIndex; i < length; ++i)
                {
                    ch = this.label.charAt(i);
                    ++numOfCharsInWord;
                    if (ch == SPACE || ch == NEW_LINE || ch == COMMA)
                    {
                        break;
                    }
                }
                
                if (font.getAdvance(this.label.substring(startIndex, endIndex+numOfCharsInWord)) > availableWidth)
                {
                    if (numOfWords == 0) // available width can't accommodate even a single word
                    {
                        // Now find out where the word cuts off appropriately
                        for (int tempEndIndex = endIndex+numOfCharsInWord-1; tempEndIndex > startIndex; --tempEndIndex)
                        {
                            if (font.getAdvance(this.label.substring(startIndex, tempEndIndex)) < availableWidth)
                            {
                                endIndex = tempEndIndex;
                                break;
                            }
                        }
                    }
                    
                    break;
                }
                else
                {
                    endIndex += numOfCharsInWord;
                    ++numOfWords;
                    
                    if (ch == NEW_LINE)
                    {
                        break;
                    }
                }
            }
            
            if (startIndex == endIndex) // force at least one character anyhow
            {
                ++endIndex;
            }
            
            str = this.label.substring(startIndex, endIndex).trim();
            if (str.length() > 0)
            {
                this.vecLabelStr.addElement(str);
            }
            
            startIndex = endIndex;
        }
    }
    
    /**
     * Generate and return a Bitmap Image scaled according to the specified width and height.
     * 
     * @param image     EncodedImage object
     * @param width     Intended width of the returned Bitmap object
     * @param height    Intended height of the returned Bitmap object
     * @return Bitmap object
     */
    private Bitmap getScaledBitmapImage(EncodedImage image, int width, int height)
    {
        // Handle null image
        if (image == null)
        {
            return null;
        }
        
        //return bestFit(image.getBitmap(), width, height);
        
        int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
        int currentHeightFixed32 = Fixed32.toFP(image.getHeight());
        
        int requiredWidthFixed32 = Fixed32.toFP(width);
        int requiredHeightFixed32 = Fixed32.toFP(height);
        
        int scaleXFixed32 = Fixed32.div(currentWidthFixed32, requiredWidthFixed32);
        int scaleYFixed32 = Fixed32.div(currentHeightFixed32, requiredHeightFixed32);
        
        image = image.scaleImage32(scaleXFixed32, scaleYFixed32);
        
        return image.getBitmap();
    }
    
    private static int[] rescaleArray(int[] ini, int x, int y, int x2, int y2)
    {
        int out[] = new int[x2*y2];
        for (int yy = 0; yy < y2; yy++)
        {
            int dy = yy * y / y2;
            for (int xx = 0; xx < x2; xx++)
            {
                int dx = xx * x / x2;
                out[(x2 * yy) + xx] = ini[(x * dy) + dx];
            }
        }
        return out;
    }


    public static Bitmap resizeBitmap(Bitmap image, int width, int height)
    {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        
        // Need an array (for RGB, with the size of original image)
        int rgb[] = new int[imageWidth * imageHeight];
        
        // Get the RGB array of image into "rgb"
        image.getARGB(rgb, 0, imageWidth, 0, 0, imageWidth, imageHeight);
        
        // Call to our function and obtain rgb2
        int rgb2[] = rescaleArray(rgb, imageWidth, imageHeight, width, height);
        
        // Create an image with that RGB array
        Bitmap temp2 = new Bitmap(width, height);
        
        temp2.setARGB(rgb2, 0, width, 0, 0, width, height);
        
        return temp2;
    }

    public static Bitmap bestFit(Bitmap image, int maxWidth, int maxHeight)
    {
        // getting image properties
        int w = image.getWidth();
        int h = image.getHeight();
        
        //  get the ratio
        int ratiow = 100 * maxWidth / w;
        int ratioh = 100 * maxHeight / h;
        
        // this is to find the best ratio to
        // resize the image without deformations
        int ratio = Math.min(ratiow, ratioh);
        
        // computing final desired dimensions
        int desiredWidth = w * ratio / 100;
        int desiredHeight = h * ratio / 100;
        
        //resizing
        return resizeBitmap(image, desiredWidth, desiredHeight);
    }
    
    /**
     * Updates the background images. This method must be called whenever the width or height undergoes change.
     */
    private void adjustBackgroundImagesToNewSize()
    {
        // Handle the case where any of the two dimensions is yet to be set.
        if (this.width <= 0 || this.height <= 0)
        {
            return;
        }
        
        if (backgroundImageNormal != null)
        {
            this.bitmapBackgroundImageNormal = getScaledBitmapImage(backgroundImageNormal, width -(rightMargin), height - (bottomMargin));
        }
        
        if (backgroundImageOnFocus != null)
        {
            this.bitmapBackgroundImageOnFocus = getScaledBitmapImage(backgroundImageOnFocus, width - (rightMargin), height - (bottomMargin));
        }
    }
    
    /**
     * If the available height for drawing icon is less than the original icon 
     * height, this method shrinks the icon to available height.
     */
    private void adjustIconToNewSize()
    {
        int availableHeight = this.height - (this.topMargin + this.topPadding + this.bottomPadding + this.bottomMargin);
        
        // Normal icon
        if (this.iconNormal != null)
        {
            int iconHeight = this.iconNormal.getHeight();
            if (availableHeight < iconHeight)
            {
                // Shrink the icon
                this.bitmapIconNormal = getScaledBitmapImage(this.iconNormal, this.iconNormal.getWidth(), availableHeight);
            }
        }
        
        // Icon on focus
        if (this.iconOnFocus != null && this.iconOnFocus != this.iconNormal)
        {
            int iconHeight = this.iconOnFocus.getHeight();
            if (availableHeight < iconHeight)
            {
                this.bitmapIconOnFocus = getScaledBitmapImage(this.iconOnFocus, this.iconOnFocus.getWidth(), availableHeight);
            }
        }
        else
        {
            this.bitmapIconOnFocus = this.bitmapIconNormal;
        }
    }
    
    private Font getDefaultFont()
    {
        Font font = getFont();
        font = font.derive(font.getStyle() | Font.PLAIN, this.fontHeight);
        return font;
        //setFont(font);
    }
    
    /**
     * Sets the font height and performs the core necessary changes needed. 
     * Various private and public methods, when font height needs to be changed 
     * in order to adapt to another changed parameter, uses this method and 
     * perfoms different tasks specific to each in their own methods.
     * 
     * If the specified font height is smaller than the minimum one, it is 
     * increased to minimum one. If the specified font height is the same as 
     * the current one, nothing is done.
     * 
     * If the label cannot fit with the specified font height, button height is 
     * increased to accommodate the entire label within the button.
     * 
     * @param fontHeight an integer
     * @return a boolean indicating if the update to specified font height has changed button height
     */
    private boolean updateFontHeight(int fontHeight, Font font)
    {
        if (fontHeight < MIN_FONT_HEIGHT)
        {
            fontHeight = MIN_FONT_HEIGHT;
        }

        this.fontHeight = fontHeight;
        
        // indicates if new font height necessitates expansion of button height 
        // and button height is changed accordingly.
        boolean heightChanged = false;
        
        // We want bold label text and our own font height. So customize the default font
        //Font font = getFont();
        //font = font.derive(font.getStyle() | Font.PLAIN | Font.BOLD, this.fontHeight);
        setFont(font);
        
        // Although we have derived a new font with our specified font height, 
        // for reason unknown, the height of derived font is not equal to our 
        // specified font. But actual font height is necessary to align the 
        // icon and label properly. So store the new font height.
        this.fontHeight = font.getHeight();
        
        // Font height change affects length of label in terms of pixels
        labelLength = font.getAdvance(this.label);
        
        if (this.width > 0 && this.height > 0)
        {
            // New font height has been set. So parse the label.
            parseLabel();
            
            // Now determine how many lines the label requires to be displayed with the 
            // new font height. Then calculate the minimum height required.
            int numOfLinesInLabel = this.vecLabelStr.size();
            int labelHeight = numOfLinesInLabel * this.fontHeight + (numOfLinesInLabel - 1) * MARGIN_BETWEEN_LABEL_LINES;
            int newHeight = topMargin + topPadding + (this.bitmapIconNormal == null ? labelHeight : Math.max(labelHeight, this.bitmapIconNormal.getHeight())) + bottomPadding + bottomMargin;
            newHeight = validateButtonHeight(newHeight);
            
            // We could change the height with setHeight() method. But in setHeight() 
            // method, the font height is adapted to the specified height and the 
            // wheel is reinvented. No need.
            if (newHeight > this.height) // if current height cannot accommodate the entire label
            {
                this.height = newHeight;
                heightChanged = true;
            }
        }
        
        return heightChanged;
    }
    
    /**
     * Sets the font height for the label.
     * 
     * Button width will remain the same but button height may be changed in 
     * accordance with the new font height in order to accommodate the full 
     * label with new font height.
     * 
     * If you want a button with your specified width and height and are 
     * willing to compromise with font height, use setWidth() and setHeight() 
     * methods instead. If you set both width and height, use setSize() method.
     * 
     * @param fontHeight the height of the font
     */
    public void setFontHeight(int fontHeight)
    {
        if (updateFontHeight(fontHeight, getDefaultFont()) == true)
        {
            // Since we have changed height, we must adjust background images, icons and adjust alignment.
            adjustBackgroundImagesToNewSize();
            //adjustIconToNewSize();
        }
        
        // New font height. New arrangement of label.
        adjustAlignment();
    }
    
        public void setFontHeight(int fontHeight, Font font)
    {
        if (updateFontHeight(fontHeight, font) == true)
        {
            // Since we have changed height, we must adjust background images, icons and adjust alignment.
            adjustBackgroundImagesToNewSize();
            //adjustIconToNewSize();
        }
        
        // New font height. New arrangement of label.
        adjustAlignment();
    }
    
    
    
    public void setBorderColor(int borderColorNormal, int borderColorFocus)
    {
        this.borderColorNormal = borderColorNormal;
        this.borderColorFocus = borderColorFocus;
    }
    
    public void setButtonFont(Font font)
    {
        this.fontHeight = font.getHeight();
        updateFontHeight(this.fontHeight, font);
      //  invalidate();
        //setFont(font);
    }
    
    public void setButtonFont(Font font, int fontHeight)
    {
        this.fontHeight = fontHeight;
        updateFontHeight(this.fontHeight, font);
        //setFont(font);
    }
    
    /**
     * Returns the font height of the label
     */
    public int getFontHeight()
    {
        return fontHeight;
    }
    
    /**
     * Sets the color of font when button is not focused.
     * 
     * @param fontColorNormal an integer with format 0x00RRGGBB
     */
    public void setFontColorNormal(int fontColorNormal)
    {
        this.fontColorNormal = fontColorNormal;
    }
    
    /**
     * Returns the font color used for label when the button is not focused.
     */
    public int getFontColorNormal()
    {
        return this.fontColorNormal;
    }
    
    /**
     * Sets the color of font when button is focused.
     * 
     * @param fontColorOnFocus an integer with format 0x00RRGGBB
     */
    public void setFontColorOnFocus(int fontColorOnFocus)
    {
        this.fontColorOnFocus = fontColorOnFocus;
    }
    
    /**
     * Returns the font color used for label when the button is focused.
     */
    public int getFontColorOnFocus()
    {
        return this.fontColorOnFocus;
    }
    
    /**
     * Sets the icon used when button is focused.
     * @param iconOnFocus An EncodedImage object
     */
    public void setIconOnFocus(EncodedImage iconOnFocus)
    {
        if (iconOnFocus != null)
        {
            this.iconOnFocus = iconOnFocus;
            
            /*
            int w = this.bitmapIconNormal.getWidth();
            int h = this.bitmapIconNormal.getHeight();
            int min = w < h ? w : h;
            this.bitmapIconOnFocus = getScaledBitmapImage(this.iconOnFocus, min, min);
            */
            
            //this.bitmapIconOnFocus = getScaledBitmapImage(this.iconOnFocus, this.bitmapIconNormal.getWidth(), this.bitmapIconNormal.getHeight());
            
            // the above one is correct. But we use this one because the scaled image becomes fragmented.
            this.bitmapIconOnFocus = this.iconOnFocus.getBitmap();
        }
    }
    
    /**
     * Returns the icon used when button is focused.
     * @return An EncodedImage object
     */
    public EncodedImage getIconOnFocus()
    {
        return this.iconOnFocus;
    }
    
    /**
     * Sets the alignment.
     * 
     * @param alignment     OR combination of two static constants from two different groups defined in this class: 
     *                      (1) ALIGNMENT_LEFT, ALIGNMENT_RIGHT, ALIGNMENT_HORIZONTAL_CENTER, 
     *                      (2) ALIGNMENT_TOP, ALIGNMENT_BOTTOM, ALIGNMENT_VERTICAL_CENTER
     */
    public void setAlignment(int newAlignment)
    {
        // Clear alignment mask
        this.alignment = 0;
        
        // Horizontal alignment
        if ((newAlignment & ALIGNMENT_RIGHT) != 0)
        {
            this.alignment |= ALIGNMENT_RIGHT;
        }
        else if ((newAlignment & ALIGNMENT_HORIZONTAL_CENTER) != 0)
        {
            this.alignment |= ALIGNMENT_HORIZONTAL_CENTER;
        }
        else // default
        {
            this.alignment |= ALIGNMENT_LEFT;
        }
        
        
        // Vertical alignment
        if ((newAlignment & ALIGNMENT_BOTTOM) != 0)
        {
            this.alignment |= ALIGNMENT_BOTTOM;
        }
        else if ((newAlignment & ALIGNMENT_TOP) != 0)
        {
            this.alignment |= ALIGNMENT_TOP;
        }
        else
        {
            this.alignment |= ALIGNMENT_VERTICAL_CENTER;
        }
        
        // New alignment has been set. Now adjust the top left point of icon and label.
        adjustAlignment();
    }
    
    /**
     * Adjust the top-left points of icon and label with the alignment.
     */
    private void adjustAlignment()
    {
        int leftBlankSpace = this.leftMargin + this.leftPadding;
        int rightBlankSpace = this.rightPadding + this.rightMargin;
        
        // Horizontal space required to display the icon properly
        //
        // [MODIFICATION]
        // 2008/01/12
        // Due to varying sizes of icon, the buttons' labels are not aligned 
        // properly. So hardcoded value for horizontalIconSpace is needed keeping in mind 
        // the maximum possible value of icon width.
        int horizontalIconSpace = this.bitmapIconNormal == null ? 0 : (this.isFullScreenWide == false ? (this.bitmapIconNormal.getWidth() + iconTextGap) : 20 + iconTextGap);
        //int horizontalIconSpace = iconNormal == null ? 0 : (iconNormal.getWidth() + iconTextGap);
        
        int topBlankSpace = topMargin + topPadding;
        int bottomBlankSpace = bottomMargin + bottomPadding;
        
        // Horizontal alignment
        if ((this.alignment & ALIGNMENT_HORIZONTAL_CENTER) != 0)
        {
            int horizontalEmptySpace = this.width - (leftBlankSpace + horizontalIconSpace + labelLength + rightBlankSpace);
            if (horizontalEmptySpace < 0)
            {
                horizontalEmptySpace = 0;
            }
            
            if (this.horizontalLabelTextPosition == ALIGNMENT_LEFT)
            {
                this.labelTopLeftPoint.x = leftBlankSpace + horizontalEmptySpace / 2;
                this.iconTopLeftPoint.x = this.width - (rightBlankSpace + horizontalEmptySpace/2 + (this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getWidth()));
            }
            else // default horizontal label text position - ALIGNMENT_RIGHT
            {
                this.iconTopLeftPoint.x = leftBlankSpace + horizontalEmptySpace/2;
                this.labelTopLeftPoint.x = this.iconTopLeftPoint.x + horizontalIconSpace;
            }
        }
        else if ((this.alignment & ALIGNMENT_RIGHT) != 0)
        {
            if (this.horizontalLabelTextPosition == ALIGNMENT_LEFT)
            {
                this.iconTopLeftPoint.x = this.width - (rightBlankSpace + (this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getWidth()));
                
                int horizontalEmptySpace = this.width - (leftBlankSpace + horizontalIconSpace + labelLength + rightBlankSpace);
                if (horizontalEmptySpace < 0)
                {
                    horizontalEmptySpace = 0;
                }
                this.labelTopLeftPoint.x = leftBlankSpace + horizontalEmptySpace;
            }
            else // default horizontal label text position - ALIGNMENT_RIGHT
            {
                this.labelTopLeftPoint.x = this.width - (labelLength + rightBlankSpace);
                this.iconTopLeftPoint.x = this.labelTopLeftPoint.x - horizontalIconSpace;
            }
        }
        else // default - ALIGNMENT_LEFT
        {
            if (this.horizontalLabelTextPosition == ALIGNMENT_LEFT)
            {
                this.labelTopLeftPoint.x = leftBlankSpace;
                
                // Calculation of X-coordinate of icon's top-left point is not 
                // straight-forward. The problem is compounded by the facts 
                // that label may be of multi-line and there may be empty space 
                // horizontally if label is short.
                int horizontalEmptySpace = this.width - (leftBlankSpace + horizontalIconSpace + labelLength + rightBlankSpace);
                if (horizontalEmptySpace < 0) // if label is of multi-line
                {
                    horizontalEmptySpace = 0;
                }
                this.iconTopLeftPoint.x = this.width - (rightBlankSpace + horizontalEmptySpace + (this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getWidth()));
            }
            else // default horizontal label text position - ALIGNMENT_RIGHT
            {
                this.iconTopLeftPoint.x = leftBlankSpace;
                this.labelTopLeftPoint.x = leftBlankSpace + horizontalIconSpace;
            }
        }
        
        
        // Vertical alignment
        if ((alignment & ALIGNMENT_TOP) != 0)
        {
            this.iconTopLeftPoint.y = topBlankSpace;
            this.labelTopLeftPoint.y = topBlankSpace;
        }
        else if ((alignment & ALIGNMENT_BOTTOM) != 0)
        {
            int numOfLinesInLabel = this.vecLabelStr.size();
            this.iconTopLeftPoint.y = this.height - (bottomBlankSpace + numOfLinesInLabel * this.fontHeight + (numOfLinesInLabel-1) * MARGIN_BETWEEN_LABEL_LINES);
        }
        else // default - ALIGNMENT_VERTICAL_CENTER
        {
            int verticalEmptySpaceForIcon = this.bitmapIconNormal == null 
                                            ? 0 
                                            : (this.height - (topBlankSpace + bottomBlankSpace + this.bitmapIconNormal.getHeight()));
            
            this.iconTopLeftPoint.y = topBlankSpace + verticalEmptySpaceForIcon / 2;
            
            int numOfLinesInLabel = this.vecLabelStr.size();
            int labelHeight = numOfLinesInLabel * this.fontHeight + (numOfLinesInLabel-1) * MARGIN_BETWEEN_LABEL_LINES;
            
            int verticalEmptySpaceForLabel = this.height - (topBlankSpace + labelHeight + bottomBlankSpace);
            this.labelTopLeftPoint.y = topBlankSpace + verticalEmptySpaceForLabel / 2;
        }
    }
    
    public void setIconTextGap(int iconTextGap)
    {
        int maxIconTextGap = this.width - (this.leftMargin + this.leftPadding + (this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getWidth() + this.iconTextGap) + this.rightPadding + this.rightMargin);
        if (this.label != null)
        {
            maxIconTextGap -= 20;
        }
        
        /*
         * Icon-text gap may become negative - an imprabable case which is 
         * handled by checking maximum icon-text gap first and then minimum one.
         */
        if (iconTextGap > maxIconTextGap)
        {
            iconTextGap = maxIconTextGap;
        }
        if (iconTextGap < MIN_ICON_TEXT_GAP)
        {
            return;
        }
        
        this.iconTextGap = iconTextGap;
        
        // Now effective width for label drawing has been changed. So need for 
        // adaptation arises.
        setWidth(this.width);
    }
    
    /**
     * Sets the relative position of label text relative to the icon.
     * 
     * @param horizontalLabelTextPosition Either ALIGNMENT_RIGHT or ALIGNMENT_LEFT
     */
    public void setHorizontalLabelTextPosition(int horizontalLabelTextPosition)
    {
        // Used to avoid invalid argument
        int position = 0;
        
        if ((horizontalLabelTextPosition & ALIGNMENT_LEFT) != 0)
        {
            position |= ALIGNMENT_LEFT;
        }
        else if ((horizontalLabelTextPosition & ALIGNMENT_RIGHT) != 0)
        {
            position |= ALIGNMENT_RIGHT;
        }
        
        if ((position | 0x00000000) != 0)
        {
            this.horizontalLabelTextPosition = position;
        }
        
        adjustAlignment();
    }
    
    /**
     * Sets the background color when button is not focused.
     * @param backgroundColorNormal An integer with format 0x00RRGGBB
     */
    public void setBackgroundColorNormal(int backgroundColorNormal)
    {
        this.backgroundColorNormal = backgroundColorNormal;
    }
    
    /**
     * Returns background color used when button is not focused.
     * @return An integer with format 0x00RRGGBB
     */
    public int getBackgroundColorNormal()
    {
        return this.backgroundColorNormal;
    }
    
    /**
     * Sets the background color when button is focused.
     * @param backgroundColorOnFocus An integer with format 0x00RRGGBB
     */
    public void setBackgroundColorOnFocus(int backgroundColorOnFocus)
    {
        this.backgroundColorOnFocus = backgroundColorOnFocus;
    }
    
    /**
     * Returns background color used when button is focused.
     * @return An integer with format 0x00RRGGBB
     */
    public int getBackgroundColorOnFocus()
    {
        return this.backgroundColorOnFocus;
    }
    
    /**
     * Sets the width of the button.
     * 
     * If the label cannot fit within the button with the new width, label font 
     * height is reduced. If the reduced font height is less than minimum, it 
     * is set to minimum font height. If label still cannot fit itself within 
     * the button with the reduced font height, then button height is changed 
     * to accommodate the entire label with the minimum font height.
     * 
     * Bear in mind that this method may affect button height to accommodate 
     * the entire label.
     * 
     * @param width Width of the button
     */
    public void setWidth(int width)
    {
        width = validateButtonWidth(width);
        
        // Just in case ...
        // Commented out later.
        // REASON: When left or right margin is changed, the space to display 
        // the icon and label is also affected. So adjustment is needed which 
        // is done in the remaining portion of this method.
        /*
        if (this.width == width)
        {
            return;
        }
        */
        
        int prevLabelWidth = 0;
        if (this.width != 0) // avoids first call to setWidth() in the constructor
        {
            prevLabelWidth = this.labelLength;
        }
        
        /*
         * setWidth() method may be called even when button width is not 
         * actually changed but the margins or paddings or icon-text gap are 
         * changed. In the latter case, unnecessary calculations should be 
         * avoided and following two bools work in this regard.
         */
        boolean dimensionChanged = false;
        
        if (this.width != width)
        {
            this.width = width;
            dimensionChanged = true;
        }
        
        // New width has been set. So adjust label.
        // Done here because of margin/padding change.
        parseLabel();

        if (prevLabelWidth != 0) // avoids the first call to setWidth() from the constructor
        {
            int numOfLinesInLabel = this.vecLabelStr.size();
            int currentLabelWidth = (this.width - (this.leftMargin + this.leftPadding + (this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getWidth() + iconTextGap) + this.rightPadding + this.rightMargin)) * numOfLinesInLabel;
            
            /*
             * If new width is less than previous width, we try to reduce 
             * the font height so that the label fit within the button. If 
             * derived font height is less than minimum font height, we set it 
             * to minimum, then calculate the button height for the entire 
             * label with this minimum font height.
             */
            if (currentLabelWidth < prevLabelWidth)
            {
                int newFontHeight = (int)(((double)currentLabelWidth / (double)prevLabelWidth) * (double)this.fontHeight);
                if (updateFontHeight(newFontHeight, getDefaultFont()) == true) // if font height change affects button height
                {
                    dimensionChanged = true;
                }
            }
        }
        
        if (this.height > 0) // avoids the case where height is yet to set
        {
            if (dimensionChanged == true) //  avoids cases where width or height is not actually changed.
            {
                // Update the background bitmap images according to the reset width and possibly height.
                adjustBackgroundImagesToNewSize();
                
                // Width does not affect icon. However, here height may be changed.
                // So icon also must be adapted.
                //adjustIconToNewSize();
            }
            
            // Adjust alignment with the new width and possibly height.
            adjustAlignment();
        }
    }
    
    /**
     * Sets the width of the button according to the length of the specified string.
     * 
     * If two or more buttons are desired to be of same width, set the width of 
     * the all buttons with a common string not shorter than the labels of the 
     * buttons.
     * 
     * If the width of reference string is greater than display width, 
     * it is curtailed to default width.
     * 
     * @param str Reference string for the width of the button
     */
    public void setWidth(String refStr)
    {
        this.labelLength = getFont().getAdvance(refStr);
        int tempWidth = leftMargin + leftPadding + ((this.bitmapIconNormal == null) ? 0 : this.bitmapIconNormal.getWidth() + iconTextGap) + labelLength + rightPadding + rightMargin;
        
        setWidth(tempWidth);
    }
    
    /**
     * Sets the height of the button.
     * 
     * If the specified height is less than minimum button height or more than 
     * maximum button height, the height will be expanded or truncated 
     * respectively.
     * 
     * If label of the button does not fit within the specified height, font 
     * height will be reduced. If even that fails, the specified height cannot 
     * be honored and the best possible height is determined and set. Yet it is 
     * ensured that the width is not affected.
     * 
     * If the specified button height is greater than current height, the 
     * label remains the same with the current font height.
     * 
     * @param height Height of the button
     */
    public void setHeight(int height)
    {
        height = validateButtonHeight(height);
        
        // Just in case ...
        // Commented out later.
        // REASON: When top or bottom margin is changed, the space to display 
        // the icon and label is also affected. So adjustment is needed which 
        // is done in the remaining portion of this method.
        /*
        if (this.height == height)
        {
            return;
        }
        */
        
        int prevLabelWidth = 0;
        if (this.height != 0) // avoids first call to setHeight() in the constructor
        {
            prevLabelWidth = this.labelLength;
        }
        
        /*
         * setHeight() method may be called even when button width is not 
         * actually changed but the margins or paddings or icon-text gap are 
         * changed. In the latter case, unnecessary calculations should be 
         * avoided and following bool works in this regard.
         */
        boolean dimensionChanged = false;
        
        if (this.height != height)
        {
            this.height = height;
            dimensionChanged = true;
        }
        
        if (prevLabelWidth != 0) // avoids first call to setHeight() in the constructor
        {
            //int numOfLinesInLabel = this.vecLabelStr.size();
            int numOfLinesInLabel = (this.height - (this.topMargin + this.topPadding + this.bottomPadding + this.bottomMargin)) / (this.fontHeight + MARGIN_BETWEEN_LABEL_LINES);
            int currentLabelWidth = (this.width - (this.leftMargin + this.leftPadding + (this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getWidth() + iconTextGap) + this.rightPadding + this.rightMargin)) * numOfLinesInLabel;
            
            /*
             * If new label width is smaller than the previous one, we try to reduce 
             * the font height so that the label fit within the button. If 
             * derived font height is less than minimum font height, we set it 
             * to minimum, then calculate the button height for the entire 
             * label with this minimum font height.
             */
            if (currentLabelWidth < prevLabelWidth)
            {
                int newFontHeight = (int)(((double)currentLabelWidth/ (double)prevLabelWidth) * (double)this.fontHeight);
                if (updateFontHeight(newFontHeight, getDefaultFont()) == true) // if font height change affects button height
                {
                    dimensionChanged = true;
                }
            }
        }
        
        if (this.width > 0) // avoids the case where height is set before width
        {
            if (dimensionChanged == true) // height is not changed actually
            {
                // Update the background bitmap images according to the reset height
                adjustBackgroundImagesToNewSize();
                //adjustIconToNewSize();
            }
            
            // Adjust alignment with the new height
            adjustAlignment();
        }
    }
    
    /**
     * Sets the size of the button.
     * 
     * If the specified width and height does not fall within the valid range, 
     * they will be adjusted to minimum or maximum limit as it seems suitable.
     * 
     * If the label cannot fit within the new width and height, the label font 
     * height is reduced. If the label cannot fit even with minimum font 
     * height, then the specified button height is not honored and height is 
     * adjusted so that entire label with the minimum font height can be 
     * accommodated within the button.
     * 
     * @param width     Width of the button
     * @param height    Height of the button
     */
    public void setSize(int width, int height)
    {
        // Do nothing in case of non-positive integers
        if (width <= 0)
        {
            return;
        }
        if (height <= 0)
        {
            return;
        }
        
        // Avoid invalid width and height
        width = validateButtonWidth(width);
        height = validateButtonHeight(height);
        
        int prevLabelWidth = this.labelLength;
        
        boolean dimensionChanged = false;
        
        if (this.width != width)
        {
            this.width = width;
            dimensionChanged = true;
            
            // New width has been set. So adjust label.
            parseLabel();
        }
        
        if (this.height != height)
        {
            this.height = height;
            dimensionChanged = true;
        }

        // Now adapt the label to the new width and height
        
        // With the existing font height, determine how many lines we can accommodate with the new height
        int numOfLinesInLabel = (this.height - (this.topMargin + this.topPadding + this.bottomPadding + this.bottomMargin)) / (this.fontHeight + MARGIN_BETWEEN_LABEL_LINES);
        //int numOfLinesInLabel = this.vecLabelStr.size();
        
        // Now we obtain an approximate value of the currently available total width for the label.
        int currentLabelWidth = (this.width - (this.leftMargin + this.leftPadding + (this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getWidth() + iconTextGap) + this.rightPadding + this.rightMargin)) * numOfLinesInLabel;
        
        /*
         * If new width is less than previous width, we try to reduce 
         * the font height so that the label fit within the button. If 
         * derived font height is less than minimum font height, we set it 
         * to minimum, then calculate the button height for the entire 
         * label with this minimum font height.
         */
        if (currentLabelWidth < prevLabelWidth)
        {
            int newFontHeight = (int)(((double)currentLabelWidth / (double)prevLabelWidth) * (double)this.fontHeight);
            
            if (updateFontHeight(newFontHeight, getDefaultFont()) == true) // if font height change affects button height
            {
                 dimensionChanged = true;
            }
        }
        
        if (dimensionChanged == true)
        {
            // Update the background bitmap images and icon according to the reset width and height
            adjustBackgroundImagesToNewSize();
            //adjustIconToNewSize();
        }
        
        // Adjust alignment with the new width and height
        adjustAlignment();
    }
    
    
    /**
     * Returns the label text.
     * 
     * @return Label text
     */
    public String getText()
    {
        return label;
    }
    
    /**
     * Returns the width of this button
     * 
     * @return Width of button
     */
    public int getButtonWidth()
    {
        return width;
    }
    
    /**
     * Returns the height of this button
     * 
     * @return Height of button
     */
    public int getButtonHeight()
    {
        return height;
    }
    
    /**
     * Sets the margin to the left of the button
     * 
     * @param newLeftMargin an integer specifying the left margin
     */
    public void setLeftMargin(int newLeftMargin)
    {
        if (newLeftMargin < 0 || this.leftMargin == newLeftMargin)
        {
            return;
        }
        
        this.leftMargin = newLeftMargin;
        adaptToHorizontalMarginChange();
    }
    
    /**
     * Returns the left margin of the button.
     * 
     * @return a non-negative integer
     */
    public int getLeftMargin()
    {
        return this.leftMargin;
    }
    
    /**
     * Sets the margin to the right of the button
     * 
     * @param newRightMargin an integer specifying the right margin
     */
    public void setRightMargin(int newRightMargin)
    {
        if (newRightMargin < 0 || this.rightMargin == newRightMargin)
        {
            return;
        }
        
        this.rightMargin = newRightMargin;
        adaptToHorizontalMarginChange();
    }
    
    /**
     * Returns the right margin of the button.
     * 
     * @return a non-negative integer
     */
    public int getRightMargin()
    {
        return this.rightMargin;
    }
    
    private void adaptToHorizontalMarginChange()
    {
        int prevLabelWidth = this.labelLength;
        
        // Left margin has been changed. So available space to paint label has 
        // also changed. So parse label.
        parseLabel();
        
        int numOfLinesInLabel = this.vecLabelStr.size();
        int currentLabelWidth = (this.width - (this.leftMargin + this.leftPadding + (this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getWidth() + iconTextGap) + this.rightPadding + this.rightMargin)) * numOfLinesInLabel;
        
        adaptToMarginChange(prevLabelWidth, currentLabelWidth);
    }
    
    /**
     * Sets the margin to the top of the button
     * 
     * @param newTopMargin an integer specifying the top margin
     */
    public void setTopMargin(int newTopMargin)
    {
        this.topMargin = newTopMargin;
        adaptToVerticalMarginChange();
    }
    
    /**
     * Returns the top margin of the button.
     * 
     * @return a non-negative integer
     */
    public int getTopMargin()
    {
        return this.topMargin;
    }
    
    /**
     * Sets the margin to the bottom of the button
     * 
     * @param newBottomMargin an integer specifying the bottom margin
     */
    public void setBottomMargin(int newBottomMargin)
    {
        this.bottomMargin = newBottomMargin;
        adaptToVerticalMarginChange();
    }
    
    /**
     * Returns the bottom margin of the button.
     * 
     * @return a non-negative integer
     */
    public int getBottomMargin()
    {
        return this.bottomMargin;
    }
    
    private void adaptToVerticalMarginChange()
    {
        int prevLabelWidth = this.labelLength;
        //int numOfLinesInLabel = this.vecLabelStr.size();
        int numOfLinesInLabel = (this.height - (this.topMargin + this.topPadding + this.bottomPadding + this.bottomMargin)) / (this.fontHeight + MARGIN_BETWEEN_LABEL_LINES);
        int currentLabelWidth = (this.width - (this.leftMargin + this.leftPadding + (this.bitmapIconNormal == null ? 0 : this.bitmapIconNormal.getWidth() + iconTextGap) + this.rightPadding + this.rightMargin)) * numOfLinesInLabel;
        adaptToMarginChange(prevLabelWidth, currentLabelWidth);
    }
    
    private void adaptToMarginChange(int prevLabelWidth, int currentLabelWidth)
    {
        boolean heightChanged = false;
        if (currentLabelWidth < prevLabelWidth)
        {
            int newFontHeight = (int)(((double)currentLabelWidth/ (double)prevLabelWidth) * (double)this.fontHeight);
            if (updateFontHeight(newFontHeight, getDefaultFont()) == true) // if font height change affects button height
            {
                heightChanged = true;
            }
        }
        
        adjustBackgroundImagesToNewSize();
        
        if (heightChanged == true) // height is changed
        {
            //adjustIconToNewSize();
        }
        
        // Adjust alignment with the new height
        adjustAlignment();
    }
    
    public void setMarginButton(int leftMargin, int rightMargin, int topMargin, int bottomMargin)
    {
        setLeftMargin(leftMargin);
        setRightMargin(rightMargin);
        setTopMargin(topMargin);
        setBottomMargin(bottomMargin);
    }
    
    /**
     * Sets whether this button will be focusable or not.
     * 
     * @param isFocusable true if focusable, false otherwise.
     */
    public void setFocusable(boolean isFocusable)
    {
        this.isFocusable = isFocusable;
    }
    
    /**
     * Returns the preferred width of the button.
     * 
     * This function is overriden to ensure proper layout within some of the layout managers.
     * 
     * @return Preferred width of the button
     * @see net.rim.device.api.ui.Field#getPreferredWidth()
     */
    public int getPreferredWidth()
    {
        return width;
    }
    
    /**
     * Returns the preferred height of the button.
     * 
     * This function is overriden to ensure proper layout within some of the layout managers.
     * 
     * @return Preferred height of the button
     * @see net.rim.device.api.ui.Field#getPreferredHeight()
     */
    public int getPreferredHeight()
    {
        return height;
    }
    
    /**
     * Lays out field contents.
     * 
     * Implementation of this method is required since this is an abstract class in super class Field.
     * 
     * @param width     Width of the button
     * @param height    Height of the button
     */
    protected void layout(int width, int height)
    {
        setExtent(Math.min(getPreferredWidth(), width), Math.min(getPreferredHeight(), height));
    }
    
    
    public void setGapBetweenIconAndText(int gapBetween)
    {
        this.gapBetween = gapBetween;
    }
    
    public void setIntialGapLess(int intialGapMinus)
    {
        this.intialGapMinus = intialGapMinus;
    }
    
    
    /**
     * Invoked by the framework to redraw a portion of this field.
     * 
     * This is an abstract method in the super class and hence needs to be 
     * implemented by this class.
     * 
     * A field's manager invokes this method when an area of this field has 
     * been marked as invalid. All painting should be done in field-local 
     * coordinates (for example, 0,0 is the top left corner of the field's 
     * pane).
     * 
     * @param graphics Graphics context for drawing this button
     */
    protected void paint(Graphics graphics)
    {
        // Required to reset the color to current color after all drawing
        int currentColor = graphics.getColor();
        
        int w = width - (leftMargin + rightMargin );
        int h = height - (topMargin + bottomMargin );
        
        int iconWidth = 0;
        if (this.bitmapIconNormal != null)
        {
            iconWidth = bitmapIconNormal.getWidth();
        }
        
        if(isFocus() == false) // normal
        {
            // Background
            if (this.bitmapBackgroundImageNormal != null)
            {
                //BitmapgetScaledBitmapImage(bitmapBackgroundImageNormal, w, h);
                graphics.drawBitmap(leftMargin, topMargin, w, h, this.bitmapBackgroundImageNormal, 0, 0);
                graphics.setColor(borderColorNormal);
                graphics.drawRoundRect(leftMargin, topMargin, w, h, 10, 10);
                
            }
            else
            {
                graphics.setColor(this.backgroundColorNormal);
                
                //graphics.fillRect(leftMargin, topMargin, w, h);
                graphics.fillRoundRect(leftMargin, topMargin, w, h, 10, 10);
                
                // border
                //graphics.setColor(borderColorNormal);
                //graphics.drawRoundRect(leftMargin, topMargin, w, h, 10, 10);
                
                
                /*
                int[] xPoints = { leftMargin+10, leftMargin+w-10, leftMargin+w-10, leftMargin+10 };
                int[] yPoints = { topMargin+10, topMargin+10, topMargin+h-10, topMargin+h-10 };
                
                int colorStart = 0x00002A40;
                int colorEnd = 0x003B59;
                int[] colors = { colorStart, colorEnd, colorEnd, colorStart };
                graphics.drawShadedFilledPath(xPoints, yPoints, null, colors, null);
                */
            }
            
            // Icon
            if (this.bitmapIconNormal != null)
            {
                graphics.drawBitmap(iconTopLeftPoint.x - intialGapMinus, iconTopLeftPoint.y, this.bitmapIconNormal.getWidth(), this.bitmapIconNormal.getHeight(), this.bitmapIconNormal, 0, 0);
                //graphics.drawBitmap(20, iconTopLeftPoint.y, this.bitmapIconNormal.getWidth(), this.bitmapIconNormal.getHeight(), this.bitmapIconNormal, 0, 0);
            }
            
            
            // Label
            graphics.setColor(this.fontColorNormal);
            
            int size = this.vecLabelStr.size();
            int y = labelTopLeftPoint.y;
            for (int i = 0; i < size; ++i)
            {
                graphics.drawText((String)this.vecLabelStr.elementAt(i),  labelTopLeftPoint.x + gapBetween - intialGapMinus + iconWidth, y);
                y += this.fontHeight + MARGIN_BETWEEN_LABEL_LINES;
            }
        }
        else // button is focused
        {
            // Background
            if (this.bitmapBackgroundImageOnFocus != null)
            {
                graphics.drawBitmap(leftMargin, topMargin, w, h, this.bitmapBackgroundImageOnFocus, 0, 0);
                graphics.setColor(0x00000000);
                graphics.drawRect(leftMargin, topMargin, w, h);
                graphics.setColor(borderColorFocus);
                graphics.drawRoundRect(leftMargin, topMargin, w, h, 10, 10);
            }
            else
            {
                graphics.setColor(this.backgroundColorOnFocus);
                
                //graphics.fillRect(leftMargin, topMargin, w, h);
                graphics.fillRoundRect(leftMargin, topMargin, w, h, 10, 10);
                
                // border
                graphics.setColor(borderColorFocus);
                graphics.drawRoundRect(leftMargin, topMargin, w, h, 10, 10);
                
                //drawing underline
                //graphics.drawLine(leftMargin, topMargin + h, leftMargin + w, topMargin + h);
                
                /*
                int[] xPoints = { leftMargin+10, leftMargin+w-10, leftMargin+w-10, leftMargin+10 };
                int[] yPoints = { topMargin+10, topMargin+10, topMargin+h-10, topMargin+h-10 };
                
                int colorStart = 0x00007DBD;
                int colorEnd = 0x00A8FF;
                int[] colors = { colorStart, colorEnd, colorEnd, colorStart };
                graphics.drawShadedFilledPath(xPoints, yPoints, null, colors, null);
                */
            }
            
            // Icon
            if (this.bitmapIconOnFocus != null)
            {
                graphics.drawBitmap(iconTopLeftPoint.x - intialGapMinus, iconTopLeftPoint.y, this.bitmapIconOnFocus.getWidth(), this.bitmapIconOnFocus.getHeight(), this.bitmapIconOnFocus, 0, 0);
            }
            
            // Label
            graphics.setColor(this.fontColorOnFocus);
            
            int size = this.vecLabelStr.size();
            int y = labelTopLeftPoint.y;
            for (int i = 0; i < size; ++i)
            {
                graphics.drawText((String)this.vecLabelStr.elementAt(i),  labelTopLeftPoint.x + gapBetween - intialGapMinus + iconWidth, y);
                y += this.fontHeight + MARGIN_BETWEEN_LABEL_LINES;
            }
        }
        
        graphics.setColor(currentColor);
    }

    /**
     * Determines if this button accepts the focus.
     * 
     * @return true if this button is focusable, false otherwise.
     */
    public boolean isFocusable()
    {
        return isFocusable;
    }
    
    /**
     * Retrieves this button's current focus region.
     * 
     * The framework uses this method to retrieve the current focus region for 
     * this button, in local coordinates; that is, the region that is made 
     * visible by the framework (by scrolling) when the button has the focus.
     * 
     * @param rect  Object to contain the focus rectangle for this field in 
     *              local coordinates
     */
    public void getFocusRect(XYRect rect)
    {
        rect.set(leftMargin, topMargin, width - (leftMargin + rightMargin), height - (topMargin + bottomMargin));
    }
    
    /**
     * Draws the focus indicator for this button. 
     * 
     * A manager invokes this method after painting the button. The manager 
     * initializes the graphics object passed in for drawing with local 
     * coordinates. It is also assumed that the region is already drawn 
     * correctly with the opposing state for the on parameter.
     * 
     * @param graphics      Graphics context for drawing the focus.
     * @param on            True if the focus should be set; otherwise, false.
     */
    protected void drawFocus(Graphics graphics, boolean on)
    {
        invalidate();
    }
    
    public boolean keyChar(char key, int status, int time)
    {
        if (key == Characters.ENTER)
        {
            fieldChangeNotify(0);
            return true;
        }
        return false;
    }
    
    
    /**
     * Overridden so that the Event Dispatch thread can catch this event
     * instead of having it be caught here.
     * @see net.rim.device.api.ui.Field#navigationClick(int, int)
     */
    protected boolean navigationClick(int status, int time) 
    {
        fieldChangeNotify(0);
        return true;
    }
    
    // DEBUG
    // Remove it after updating
    public int getLabelLength()
    {
        return this.labelLength;
    }
    
    // Used to identify this button
    
    private String name="";
    private int id = -1;
    
    public int getButtonId()
    {
        return id;
    }
    
    public void setButtonId(int id)
    {
        this.id = id;
    }
    
    public String getButtonName()
    {
        return name;
    }
    
    public void setButtonName(String name)
    {
        this.name = name;
    }
    protected boolean onSavePrompt() {
         return true;
  }
}
