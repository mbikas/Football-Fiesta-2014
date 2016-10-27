/*
 * HistoryDetailsScreen.java
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


public class HistoryDetailsScreen extends MyMainScreen //implements FieldChangeListener
{

        private ScreenManager screenManager;
        public HistoryDetailsScreen thisClass;
        private int screenIndex = ScreenManager.HISTORY_DETAILS_SCREEN;
        
        public HistoryDetailsScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            //initialize();
        }   
        
        private static HistoryDetailsScreen historyDetailsScreen = null;
        synchronized public static HistoryDetailsScreen getInstances()
        {
            if (historyDetailsScreen == null)
                historyDetailsScreen = new HistoryDetailsScreen();
            return historyDetailsScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new HistoryDetailsScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        private History1 history;
        public void setHistory(History1 history)
        {
            this.history = history;
        }
        
        public void initialize()
        {
            this.deleteAll();
                        
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager(history.host + " -- " + history.year);            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            //vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(TeamScreen.getSubHeaderManager("Hosting Country:"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField(history.host));
            
            Bitmap imageName = Bitmap.getBitmapResource(history.year + ".jpg");
            BitmapField imageField = new BitmapField(imageName, DrawStyle.HCENTER | BitmapField.USE_ALL_WIDTH);
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(imageField);
            
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(TeamScreen.getSubHeaderManager("Positions:"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField("Champion: " + history.champion));
            vfmMain.add(StadiumDetailsScreen.getTextField("Runners-Up: " + history.runnerUp));
            vfmMain.add(StadiumDetailsScreen.getTextField("Third Place: " + history.third));
            vfmMain.add(StadiumDetailsScreen.getTextField("Fourth Place: " + history.fourth));
            
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(TeamScreen.getSubHeaderManager("Final Match Result:"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField(history.finalResult));
            
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(TeamScreen.getSubHeaderManager("Dates:"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField(history.dates));
            
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(TeamScreen.getSubHeaderManager("Total Teams:"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            vfmMain.add(StadiumDetailsScreen.getTextField(history.teams));
            
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
            vfmMain.add(TeamScreen.getSubHeaderManager("Golden Boots:"));
            vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
            for(int i=0;i<history.bootVector.size();i++)
            {         
                VerticalFieldManager vfm1 = StadiumDetailsScreen.getTextField((String)history.bootVector.elementAt(i));
                vfm1.setMargin(0,0,0,5);
                vfmMain.add(vfm1);
            }
            
            String titles[]  = {"Golden Ball:", "Best Goalkeeper:", "Best Young Player Award:","Fair Play award:"};
            String values[] = {history.goldenBall , history.goalkeeper, history.youngPlayer,  history.fairPlay};
            
            for(int i=0;i<titles.length;i++)
            {
                if(values[i].equalsIgnoreCase("null") == true)continue;
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_TOP_GAP));
                vfmMain.add(TeamScreen.getSubHeaderManager(titles[i]));
                vfmMain.add(new TranslatorField(Utils.displayWidth, Utils.TEXT_MID_GAP));
                vfmMain.add(StadiumDetailsScreen.getTextField(values[i])); 
            } 
            
            //vfmMain.add(new NullField(Field.FOCUABLE));        
            
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
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
