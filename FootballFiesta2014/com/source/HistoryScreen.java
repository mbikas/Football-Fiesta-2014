/*
 * HistoryScreen.java
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
@Date 05-01-2014
*/


public class HistoryScreen extends MyMainScreen implements FieldChangeListener
{

        private ScreenManager screenManager;
        public HistoryScreen thisClass;
        private int screenIndex = ScreenManager.HISTORY_SCREEN;
        
        public HistoryScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            initialize();
        }   
        
        private static HistoryScreen historyScreen = null;
        synchronized public static HistoryScreen getInstances()
        {
            if (historyScreen == null)
                historyScreen = new HistoryScreen();
            return historyScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new HistoryScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);      
            initialize();      
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        public void initialize()
        {
            this.deleteAll();              
                                       
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("History");            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            for(int i=0;i<Utils.historyVector.size();i++)
            {
                History1 history = (History1) Utils.historyVector.elementAt(i);
                
                HorizontalFieldManager hfm = getHistoryManager(history);
                vfmMain.add(new TranslatorField(Utils.displayWidth,Utils.TEXT_TOP_GAP)); 
                vfmMain.add(hfm);
                vfmMain.add(new TranslatorField(Utils.displayWidth,Utils.TEXT_TOP_GAP)); 
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Color.BLACK));                      
            }          
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }       
        
        private HorizontalFieldManager getHistoryManager(History1 history)
        {
            String imageName = history.year + "_small.jpg";
            Bitmap imageSmall = Bitmap.getBitmapResource(imageName);
            int availableWidth = Utils.displayWidth - imageSmall.getWidth() - 10;
                        
            VerticalFieldManager vfmLeft = new VerticalFieldManager();
            vfmLeft.add(new BitmapField(imageSmall));
            vfmLeft.setMargin(0,0,0,5);
        
            VerticalFieldManager vfmRight = new VerticalFieldManager();
            CustomLabelField lblName = new CustomLabelField(history.host + " " + history.year , Utils.fontHeightText, Utils.colorTeamName);
            lblName.setFont(Utils.getBoldFont(Utils.fontHeightText));
            
            CustomLabelField lblChampion  = new CustomLabelField("Champion: "+ history.champion , Utils.fontHeightText, Utils.colorText);
            CustomLabelField lblRunner = new CustomLabelField("Runners-Up: "+ history.runnerUp , Utils.fontHeightText, Utils.colorText);
            
            HyperlinkButtonField btnMore = new HyperlinkButtonField("More", Utils.colorText, Utils.colorTextFocus, Utils.bgColor, 0, 0);
            btnMore.setButtonName(history.year);
            btnMore.setFont(Utils.getBoldFont(Utils.fontHeightText));
            btnMore.setChangeListener(this);
            
            vfmRight.add(lblName);
            vfmRight.add(new TranslatorField(availableWidth,1));
            vfmRight.add(lblChampion);
            vfmRight.add(new TranslatorField(availableWidth,1));
            vfmRight.add(lblRunner);
            vfmRight.add(new TranslatorField(availableWidth,1));
            btnMore.setMargin(0,0,0,(availableWidth-btnMore.getPreferredWidth())/2);
            vfmRight.add(btnMore);
            vfmRight.setMargin(0,0,0,10);
            
            HorizontalFieldManager hfm = new HorizontalFieldManager();
            hfm.add(vfmLeft);
            hfm.add(vfmRight);
            return hfm;
        }
        
        private HorizontalFieldManager getHistoryManagerPrevious(History1 history)
        {
            String label = history.year + " -- " + history.champion;
            HyperlinkButtonField btnMore = new HyperlinkButtonField(label, Utils.colorTeamName, Utils.colorTextFocus, Utils.bgColor, 0, 0);
            btnMore.setButtonName(history.year);
            btnMore.setFont(Utils.getBoldFont(Utils.fontHeightText+1));
            btnMore.setChangeListener(this);            
            btnMore.setMargin(0,0,0,10);
            HorizontalFieldManager hfm = new HorizontalFieldManager();
            hfm.add(btnMore);
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
              
            History1 history = Utils.getHistoryFromYear(text);
            
            int nextIndex = ScreenManager.HISTORY_DETAILS_SCREEN;
            if (screenManager.mainScreenArray[nextIndex] == null)
                screenManager.mainScreenArray[nextIndex] = new HistoryDetailsScreen();
            
            HistoryDetailsScreen screen = (HistoryDetailsScreen)screenManager.mainScreenArray[nextIndex];
            screen.setHistory(history);
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
