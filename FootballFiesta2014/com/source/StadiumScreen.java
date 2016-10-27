/*
 * StadiumScreen.java
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
@Date 01-01-2014
*/


public class StadiumScreen extends MyMainScreen implements FieldChangeListener
{

        private ScreenManager screenManager;
        public StadiumScreen thisClass;
        private int screenIndex = ScreenManager.STADIUM_SCREEN;
        
        public StadiumScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            initialize();
        }   
        
        private static StadiumScreen stadiumScreen = null;
        synchronized public static StadiumScreen getInstances()
        {
            if (stadiumScreen == null)
                stadiumScreen = new StadiumScreen();
            return stadiumScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new StadiumScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        public void initialize()
        {
            this.deleteAll();              
        
            int stadiums = Utils.stadiumVector.size();
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("Stadiums");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            for(int i=0;i<stadiums;i++)
            {
                Stadium1 stadium = (Stadium1) Utils.stadiumVector.elementAt(i);
                
                HorizontalFieldManager hfm = getStadiumManager(stadium);
                vfmMain.add(new TranslatorField(Utils.displayWidth,3)); 
                vfmMain.add(hfm);
                vfmMain.add(new TranslatorField(Utils.displayWidth,3)); 
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Color.BLACK));                      
            }          
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }       
        
        private HorizontalFieldManager getStadiumManager(Stadium1 stadium)
        {
            Bitmap imageSmall = Bitmap.getBitmapResource(stadium.imageName);
            int availableWidth = Utils.displayWidth - imageSmall.getWidth() - 10;
                        
            VerticalFieldManager vfmLeft = new VerticalFieldManager();
            vfmLeft.add(new BitmapField(imageSmall));
            vfmLeft.setMargin(0,0,0,5);
        
            VerticalFieldManager vfmRight = new VerticalFieldManager();
            CustomLabelField lblName = new CustomLabelField(stadium.name , Utils.fontHeightText, Utils.colorTeamName);
            lblName.setFont(Utils.getBoldFont(Utils.fontHeightText));
            
            CustomLabelField lblCity = new CustomLabelField(stadium.city , Utils.fontHeightText, Utils.colorText);
            CustomLabelField lblCapacity = new CustomLabelField("("+stadium.capacity +" seats)", Utils.fontHeightText, Utils.colorText);
            
            HyperlinkButtonField btnMore = new HyperlinkButtonField("More", Utils.colorText, Utils.colorTextFocus, Utils.bgColor, 0, 0);
            btnMore.setButtonName(stadium.id);
            btnMore.setFont(Utils.getBoldFont(Utils.fontHeightText));
            btnMore.setChangeListener(this);
            
            vfmRight.add(lblName);
            vfmRight.add(new TranslatorField(availableWidth,1));
            vfmRight.add(lblCity);
            vfmRight.add(new TranslatorField(availableWidth,1));
            vfmRight.add(lblCapacity);
            vfmRight.add(new TranslatorField(availableWidth,1));
            btnMore.setMargin(0,0,0,(availableWidth-btnMore.getPreferredWidth())/2);
            vfmRight.add(btnMore);
            vfmRight.setMargin(0,0,0,10);
            
            HorizontalFieldManager hfm = new HorizontalFieldManager();
            hfm.add(vfmLeft);
            hfm.add(vfmRight);
            return hfm;
        } 
        
        protected void makeMenu(Menu menu, int instance)
        {
            menu.add(backMenu);
            menu.add(increaseFontMenu);
            menu.add(decreaseFontMenu);
        }        
        private MenuItem backMenu = new MenuItem(Utils.BACK_TEXT, 100, 10)
        {
            public void run()
            {
                backToPreviousScreen();
            }
        };
        private MenuItem increaseFontMenu = new MenuItem("Increase Font", 110, 10)
        {
                public void run()
                {
                    Utils.increaseFont();
                    initialize();
                }
        };
        
        private MenuItem decreaseFontMenu = new MenuItem("Decrease Font", 120, 10)
        {
                public void run()
                {
                    Utils.decreaseFont();
                    initialize();
                }
        };
        private void backToPreviousScreen()
        {
            Utils.backToPreviousScreen(screenIndex, thisClass);
        }
        
        public boolean onClose()
        {
            backToPreviousScreen();
            return true;
        }
        private void closeScreen()
        {
            this.close();
        }
        
        public void fieldChanged(Field field, int context)
        {
            String text = Utils.getButtonText(field);
            int buttonId = Utils.getButtonId(field);
              
            Stadium1 stadium = Utils.getStadiumFromID(text);
            
            int nextIndex = ScreenManager.STADIUM_DETAILS_SCREEN;
            if (screenManager.mainScreenArray[nextIndex] == null)
                screenManager.mainScreenArray[nextIndex] = new StadiumDetailsScreen();
            
            StadiumDetailsScreen screen = (StadiumDetailsScreen)screenManager.mainScreenArray[nextIndex];
            screen.setStadium(stadium);
            screen.initialize();
            screenManager.previousScreenArray[nextIndex].push(screenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[nextIndex]); 
            closeScreen();            
        }
        
        
        private int previousWidth, previousHeight;
        public void checkOrientation()
        {
            if(previousWidth != Utils.displayWidth || previousHeight != Utils.displayHeight)
                initialize();
            previousWidth = Utils.displayWidth;
            previousHeight = Utils.displayHeight;
        }
        
}


