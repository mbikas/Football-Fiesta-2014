/*
 * MyMainScreen.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */
package com.source;

import net.rim.device.api.ui.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.*;
import java.io.IOException;
import java.io.EOFException;
import java.io.InputStream;
import net.rim.device.api.util.StringUtilities;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Background;

import com.utils.*;
import com.api.ui.component.*;
import com.api.ui.custom.*;
import java.util.Vector;


/*
@author Bikas
@Date 23-06-2013
*/


public class MyMainScreen extends MainScreen
{

        public MyMainScreen thisClass;
                
        public MyMainScreen()
        {
            super(NO_VERTICAL_SCROLL | USE_ALL_HEIGHT);
            //int directions = net.rim.device.api.system.Display.ORIENTATION_PORTRAIT;
            //net.rim.device.api.ui.Ui.getUiEngineInstance().setAcceptableDirections(directions);
        }  
        
        protected void makeMenu(Menu menu, int instance)
        {
            menu.add(exitMenu);            
        }        
        private MenuItem exitMenu = new MenuItem("Exit", 100, 10)
        {
                public void run()
                {
                    promptClose();
                }
        };

        
        public void promptClose()
        {
            if (Dialog.ask(Dialog.D_YES_NO, "Exit Application?") == Dialog.YES)
            {
                System.exit(0);
            }
        }
        
        private void closeScreen()
        {
            this.close();
        }
    
        // The regular getFieldAtLocation returns wrong values in open spaces in complex managers, so we override it
        public int getFieldAtLocation(int x, int y)
        {
            XYRect rect = new XYRect();
            int index = getFieldCount() -1;
            while (index >= 0)
            {
                getField(index).getExtent(rect);
                if (rect.contains(x, y))
                break;
                --index;
            }
            return index;
        }
        
        protected boolean onSavePrompt() 
        {
            return true;
        }  
        
        private boolean changing = false;
        protected void sublayout(int width, int height)
        {
            //super.sublayout(width, height);
            if (Utils.displayWidth != Display.getWidth() || Utils.displayHeight != Display.getHeight())
            {
                Utils.displayWidth = Display.getWidth();
                Utils.displayHeight = Display.getHeight();
                //HomeScreen.setImages();           
                changing = true;
                Utils.orientationChanged();
                changing = false;
                initialize();
            }       
            super.sublayout(width, height); 
        }
    
    public void initialize(){
    }
    /*
    protected boolean touchEvent(TouchEvent event) 
    {
        boolean returnValue = super.touchEvent(event);
        
        switch(event.getEvent()) 
        {
            case TouchEvent.DOWN:
                int index = getFieldAtLocation(event.getX(1), event.getY(1));
                // Ignore click events outside any fields
                if (index == -1)
                    return returnValue;
                return super.touchEvent(event); 
            case TouchEvent.MOVE:
                return true;
            case TouchEvent.UP:
                return true;
            case TouchEvent.CLICK:
                return true;
                
        }
        return returnValue;
    }  
    */ 
    

}
