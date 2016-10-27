/*
 * SquadDetailsScreen.java
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
import net.rim.device.api.util.SimpleSortingVector;
import net.rim.device.api.util.Comparator;
import java.util.Vector;

import com.io.*;
/*
@author Bikas
@Date 08-06-2014
*/


public class SquadDetailsScreen extends MyMainScreen implements FieldChangeListener
{
        private ScreenManager screenManager;
        public SquadDetailsScreen thisClass;
        private int screenIndex = ScreenManager.SQUAD_DETAILS_SCREEN;
        
        public SquadDetailsScreen()
        {
            super();
            screenManager = ScreenManager.getInstance();
            thisClass = this;
            //initialize();
        }   
        
        private static SquadDetailsScreen pointTableScreen = null;
        synchronized public static SquadDetailsScreen getInstances()
        {
            if (pointTableScreen == null)
                pointTableScreen = new SquadDetailsScreen();
            return pointTableScreen;
        }
        
        public void pushTheScreen(MainScreen previousScreen, int previousScreenIndex)
        {
            if (screenManager.mainScreenArray[screenIndex] == null)
                screenManager.mainScreenArray[screenIndex] = new SquadDetailsScreen();
            screenManager.previousScreenArray[screenIndex].push(previousScreenIndex);
            UiApplication.getUiApplication().pushScreen(screenManager.mainScreenArray[screenIndex]);            
            previousScreen.close();
        }        
        VerticalScrollManager vfmMain;      
        
        private Squad squad;
        public void setSquad(Squad squad)
        {
            this.squad = squad;
        }  
        
        public void initialize()
        {
            this.deleteAll();  
            getSortedPlayersByPosition();
            
            VerticalFieldManager vfmBackground = Utils.getMainVerticalManager(Utils.displayWidth, Utils.displayHeight);
            HorizontalFieldManager imageManager = Utils.getTopImageAndButtonManager("Final Squad -- "+ squad.team_name);            
            vfmBackground.add(imageManager);
            
            VerticalFieldManager vfmBody = new VerticalFieldManager();
            vfmBody.setBackground(Utils.BACKGROUNG_WITH_IMAGE);
            vfmMain = new VerticalScrollManager(Utils.displayWidth, Utils.BODY_HEIGHT);
            vfmMain.add(new SpacerField(Utils.displayWidth, Utils.headerSpacerHeight, Utils.headerSpacerColor));
            
            setColWidth();
            
            try
            {
                BitmapField imageField = new BitmapField(Bitmap.getBitmapResource(squad.team_id+".png"), DrawStyle.HCENTER | BitmapField.USE_ALL_WIDTH);
                vfmMain.add(imageField);
            }
            catch(Exception e){}
            
            VerticalFieldManager vfmSubHeader1 = TeamScreen.getSubHeaderManager("COACH");
            vfmMain.add(new TranslatorField(Utils.displayWidth,5)); 
            vfmMain.add(vfmSubHeader1);
            vfmMain.add(StadiumDetailsScreen.getTextTitleField(squad.coach));
            
            VerticalFieldManager vfm = TeamScreen.getSubHeaderManager("SQUAD");
            vfmMain.add(vfm);
            vfmMain.add(getPlayerManager(null, fontHeightMatchTeam, Utils.colorTeamName, "POS"));
            vfmMain.add(new SpacerField(Utils.displayWidth-10, 2, Utils.colorSpacer));
                
            for(int i=0;i<sortingVector.size();i++)
            {
                Player player = (Player) sortingVector.elementAt(i);
                vfmMain.add(getPlayerManager(player, fontHeightMatchText, Utils.colorText, player.position));
                vfmMain.add(new SpacerField(Utils.displayWidth-10, 1, Utils.colorSpacer));
            }             
            vfmBody.add(vfmMain);
            vfmBackground.add(vfmBody);
            this.add(vfmBackground);
        }
        
        public int fontHeightMatchTeam = Utils.fontHeightText+3;
        public int fontHeightMatchText = Utils.fontHeightText+1;
        public int colWidths[];
        public void setColWidth()
        {
            int usedWidth = 0, width1, nameWidth;
            int stringLengthPosition, stringLengthCaps;
            while (true)
            {
                stringLengthPosition = Utils.getStringLength("POSXX", Utils.getNormalFont(fontHeightMatchText));
                //stringLengthCaps = Utils.getStringLength("CAPS", Utils.getBoldFont(fontHeightMatchTeam));
                width1 = stringLengthPosition;
                nameWidth = (Utils.displayWidth - width1 - 10);
                usedWidth = width1 + nameWidth + 10;
                if(usedWidth <= Utils.displayWidth)break;
                fontHeightMatchTeam--;
                fontHeightMatchText--;
            }
            
            colWidths = new int [2];
            colWidths[0] = nameWidth;
            colWidths[1] = stringLengthPosition;
        }  
        
        public CustomGridFieldManager getPlayerManager(Player player, int fontHeightText, int fontColor, String pos)
        {
            CustomLabelField lblPos = new CustomLabelField(pos, fontHeightText, fontColor);
            lblPos.setFont(Utils.getNormalFont(fontHeightText));
            
            HorizontalFieldManager hfm = new HorizontalFieldManager();
            if(player == null)
            {
                CustomLabelField lblPlayerName = new CustomLabelField("Player", fontHeightText, fontColor, DrawStyle.HCENTER);
                lblPlayerName.setFont(Utils.getBoldFont(fontHeightText));
                hfm.add(lblPlayerName);
                hfm.setMargin(0,0,0,20);
            }
            else
            {
                long style =  RichTextField.USE_TEXT_WIDTH | DrawStyle.HCENTER | RichTextField.FOCUSABLE;
                HyperlinkButtonField btnPlayerName = new HyperlinkButtonField(player.name, Utils.colorTeamName, Utils.colorTextFocus, Utils.bgColor, 0, 0);
                btnPlayerName.setFont(Utils.getBoldFont(fontHeightMatchTeam));
                btnPlayerName.setChangeListener(thisClass);
                btnPlayerName.setButtonName(player.player_id);
                btnPlayerName.setMargin(0,0,0,5);
                
                hfm.add(btnPlayerName);
                hfm.setMargin(0,0,0,10);
            }            
            
            CustomGridFieldManager grid = new CustomGridFieldManager(colWidths, 0);
            grid.add(hfm);
            grid.add(lblPos);
            return grid;            
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
        
        private Player getPlayerFromId(String player_id)
        {
            for(int i=0;i<squad.playerVector.size();i++)
            {
                Player player = (Player) squad.playerVector.elementAt(i);
                if(player.player_id.equalsIgnoreCase(player_id) == true)
                    return player;
            }
            return null;
        }
        
        public void fieldChanged(Field field, int context)
        {
            String text = Utils.getButtonText(field);
            int buttonId = Utils.getButtonId(field); 
            
            Player player = getPlayerFromId(text);  
            //Dialog.alert(player.name);              
            
            int nextIndex = ScreenManager.PLAYER_DETAILS_SCREEN;
            if (screenManager.mainScreenArray[nextIndex] == null)
                screenManager.mainScreenArray[nextIndex] = new PlayerDetailsScreen();                
            PlayerDetailsScreen screen = (PlayerDetailsScreen)screenManager.mainScreenArray[nextIndex];
            screen.setPlayer(player, squad.team_name);
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
        
        SimpleSortingVector sortingVector = new SimpleSortingVector();
        public void getSortedPlayersByPosition()
        {
            sortingVector.removeAllElements();
            sortingVector.setSortComparator(new Comparator() 
            {
                public int compare(Object o1, Object o2) { 
                
                    Player player1 = (Player)o1;
                    Player player2 = (Player)o2;
                    int result = player1.position.compareTo(player2.position);
                    if(result < 0)          return -1;
                    else if(result == 0)    return 0;
                    else                    return 1;
                }
            });
            
            for(int i=0;i<squad.playerVector.size();i++)
            {
                Player p = (Player)squad.playerVector.elementAt(i);
                sortingVector.addElement(p);
            }        
            sortingVector.reSort();             
        }
}


