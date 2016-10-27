/*
 * PlayerDetailsScreen.java
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

import com.io.*;
import com.utils.*;
import com.api.ui.component.*;
import com.api.ui.custom.*;
import net.rim.device.api.util.SimpleSortingVector;
import net.rim.device.api.util.Comparator;
import java.util.Vector;
/*
@author Bikas
@Date 05-01-2014
*/


public class PlayerDetailsScreen extends MyMainScreen implements FieldChangeListener
{

        private ScreenManager screenManager;
        public PlayerDetailsScreen thisClass;
        private int screenIndex = ScreenManager.PLAYER_DETAILS_SCREEN;
        
        public PlayerDetailsScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            //initialize();
        }   
        
        private static PlayerDetailsScreen teamDetailScreen = null;
        synchronized public static PlayerDetailsScreen getInstances()
        {
            if (teamDetailScreen == null)
                teamDetailScreen = new PlayerDetailsScreen();
            return teamDetailScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new PlayerDetailsScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }
        
        VerticalScrollManager vfmMain;
        
        private Player player;
        private String teamName;
        public void setPlayer(Player player, String teamName)
        {
            this.player = player;
            this.teamName = teamName;
        }
        
        String headers[] = new String[6];
        String values[] = new String[6];
        
        private String getPostionDetails(String pos)
        {
            if(pos.equalsIgnoreCase("GK") == true)return "Goalkeeper";
            if(pos.equalsIgnoreCase("DF") == true)return "Defender";
            if(pos.equalsIgnoreCase("MF") == true)return "Midfielder";
            if(pos.equalsIgnoreCase("MF") == true)return "Forward";
            return pos;
            
        }
        public void initialize()
        {
            this.deleteAll();              
                                      
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager(player.name);            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            headers[0] = "Team Name"; headers[1] = "Player Name"; headers[2] = "Position";
            headers[3] = "Club Name"; headers[4] = "Appearances"; headers[5] = "Age";
            values[0] = teamName; values[1] = player.name; values[2] = getPostionDetails(player.position); 
            values[3] = player.club; values[4] = player.caps; values[5] = player.age; 
            
            for(int i=0;i<headers.length;i++)
            {
                //if(header[i])
                VerticalFieldManager vfmSubHeader1 = TeamScreen.getSubHeaderManager(headers[i]);
                vfmMain.add(new TranslatorField(Utils.displayWidth,5)); 
                vfmMain.add(vfmSubHeader1);
                vfmMain.add(StadiumDetailsScreen.getTextField(values[i]));
            }
            
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


