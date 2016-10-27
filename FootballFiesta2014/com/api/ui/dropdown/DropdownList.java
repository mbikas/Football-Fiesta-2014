/*
 * DropdownList.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package com.api.ui.dropdown;



import net.rim.device.api.io.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import net.rim.device.api.ui.decor.*;

import com.api.ui.custom.*;
import com.api.ui.component.BaseButtonField;

import com.utils.Utils;

public class DropdownList extends BaseButtonField implements FieldChangeListener 
{
    //DropdownItem[] mItems;
    int mIndex;    
    private String[] choices;
    private Bitmap[] _bitmaps;
    private Bitmap icon;
    private static final int NORMAL = 0;
    private static final int FOCUS = 1;
    private static final int TEXT_COLOR = Color.BLACK;
    private static final int TEXT_COLOR_FOCUS = Color.RED;
    private int textColor = TEXT_COLOR;
    private int textColorFocus = TEXT_COLOR_FOCUS;
    private Font font = Font.getDefault();
    private int width;
    
    long _style = Field.FOCUSABLE;
    
    public DropdownList(String[] choices, int choiceIndex, Bitmap backgroundImage, Bitmap backgroundImageOver, Bitmap icon, int width, Font font) 
    {
        super(Field.FOCUSABLE);
        this.choices = choices;
        _bitmaps = new Bitmap[] { backgroundImage, backgroundImageOver };
        this.width = width;
        this.font = font;
        this.icon = icon;
        adjustBackgroundImage();
        mIndex = choiceIndex;
        updateIndex(mIndex);
        setChangeListener(this);   
    }
    
    
    
    private void adjustBackgroundImage()
    {
        if (_bitmaps[NORMAL].getWidth() == width) return;
        int height = font.getHeight() + 5;
        //if(_bitmaps[NORMAL].getHeight() > height)height = _bitmaps[NORMAL].getHeight();
        
        _bitmaps[NORMAL] = Utils.resizeBitmap(_bitmaps[NORMAL], width,height); 
        _bitmaps[FOCUS] = Utils.resizeBitmap(_bitmaps[FOCUS], width,height);
    }
    
    
    public void setImage(Bitmap normalState)
    {
            _bitmaps[NORMAL] = normalState;
            invalidate();
    }

    public void setFocusImage(Bitmap focusState)
    {
            _bitmaps[FOCUS] = focusState;
            invalidate();
    }

    public int getPreferredWidth()
    {
            return width;
    }

    public int getPreferredHeight()
    {
            return _bitmaps[NORMAL].getHeight();
    }

    protected void layout(int width, int height)
    {
            setExtent(width, _bitmaps[NORMAL].getHeight());
    }
    
    protected void paint(Graphics g)
    {
            int index = g.isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS) ? FOCUS
                            : NORMAL;
            g.drawBitmap(0, 0, _bitmaps[index].getWidth(), _bitmaps[index]
                            .getHeight(), _bitmaps[index], 0, 0);

            if (choices[mIndex].equals("") == false)
            {
                if (isFocus() == false)
                    g.setColor(textColor);
                else
                    g.setColor(textColorFocus);
                    
                    g.setFont(font);
                    
                    String str =  getWordsToDisplay(choices[mIndex], width);
                    //int x = (_bitmaps[index].getWidth() - getStringLength(choices[mIndex], font)) / 2;
                    int x = 10;
                    int y = (_bitmaps[index].getHeight() - this.getFont().getHeight()) / 2;
                    g.drawText(str, x, y);
            }
    }
    
    private String getWordsToDisplay(String label, int width)
    {
        String str[] = Utils.split(label, " ");        
        if (getStringLength(str[0], font) > width) return ( str[0].substring(0, width-3) + "...");
        
        String temp=str[0];
        for (int i=1;i<str.length;i++)
        {
            if (getStringLength(temp+" "+str[i], font) > width-3) return temp+"...";
            temp += " "+str[i];
        }
        return temp;
    }
    
    
    

    public static int getStringLength(String str, Font font)
    {
            return font.getAdvance(str);
    }

    /**
        * With this commented out the default focus will show through If an app
        * doesn't want focus colours then it should override this and do nothing
        **/
    /*
        * protected void paintBackground( Graphics g ) { // Nothing to do here }
        */

    protected void drawFocus(Graphics g, boolean on)
    {
            super.drawFocus(g, on);
            boolean oldDrawStyleFocus = g
                            .isDrawingStyleSet(Graphics.DRAWSTYLE_FOCUS);
            try
            {
                    if (on)
                    {
                            g.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, true);
                    }
                    paintBackground(g);
                    paint(g);
            }
            finally
            {
                    g.setDrawingStyle(Graphics.DRAWSTYLE_FOCUS, oldDrawStyleFocus);
            }

    }
    
    public void fieldChanged(Field field, int context) 
    {
        getScreen().getUiEngine().pushScreen(new DDImagesPopUp());
    }

    public void updateIndex(int index) {
        mIndex = index;
        //mWidth = mItem.mBitmap.getWidth() + 6 + 18 + 3;
        //mHeight = mItem.mBitmap.getHeight() + 6;
        this.invalidate();
    }
    
    
    public int getSelectedIndex()
    {
        return this.mIndex;
    }
    protected boolean keyChar(char character, int status, int time)
        {
                if (character == Characters.ENTER)
                {
                        clickButton();
                        return true;
                }
                return super.keyChar(character, status, time);
        }

        protected boolean navigationClick(int status, int time)
        {
                clickButton();
                return true;
        }

        protected boolean trackwheelClick(int status, int time)
        {
                clickButton();
                return true;
        }

    /**
        * A public way to click this button
        */
    public void clickButton()
    {
            fieldChangeNotify(0);
    }

    
    public void setDirty(boolean dirty)
    {
    }

    public void setMuddy(boolean muddy)
    {
    }

    class DDImagesPopUp extends PopupScreen implements FieldChangeListener 
    {

        public DDImagesPopUp() 
        {
            super( new VerticalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR));
            int length = choices.length;
            CustomButtonField button[] = new CustomButtonField[length];
            int maxWidth = 0;
            for (int i = 0; i < choices.length; i++) {
                button[i] = new CustomButtonField(icon, choices[i], false, font);
                if (button[i].getButtonWidth()>maxWidth)maxWidth=button[i].getButtonWidth();
                //add(new SpacerField(width, 1, Color.BLACK));
                button[i].setChangeListener(this);
            }
            
             for (int i = 0; i < choices.length; i++) {
                button[i].setWidth(maxWidth+40);
                add(button[i]);
            }
            //setFieldWithFocus(getField(mIndex));
        }

        protected boolean keyChar(char key, int status, int time) {
            if (Keypad.KEY_ESCAPE == key) {
                this.close();
                return true;
            }else
                return super.keyChar(key, status, time);
            }

        public void fieldChanged(Field field, int context) {
            updateIndex(getFieldWithFocusIndex());
            close();
        }
    }
}
